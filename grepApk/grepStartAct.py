#!/usr/bin/python
# -*- coding: utf-8 -*- 

import os, sys, time, threading, shlex, yaml
from optparse import OptionParser
#from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
from xml.dom import minidom
from os.path import join, isdir, isfile, getsize
from subprocess import Popen, PIPE
import random


DEVCONFIG = 'devconfig.txt'
APPDUMP = 'appdump'
SODUMP = 'sodump'
STRDUMP = 'strdump'
SLEEPTIME = 2
SMLSLEEP = 4
BIGSLEEP = 12 #since attack 3 requires 8s
DATAOUT = 'dataOut'
backdroid="../bin/backdroid.sh"
DIR=os.path.dirname(os.path.realpath(__file__))


"""
from http://stackoverflow.com/questions/1191374/subprocess-with-timeout
"""
class MyCmd(object):
    def __init__(self, cmd):
        self.cmd = cmd
        self.process = None

    def run(self, timeout=60):
        def target():
            self.process = Popen(self.cmd, shell=True)
            (out, err) = self.process.communicate()

        thread = threading.Thread(target=target)
        thread.start()

        thread.join(timeout)
        if thread.is_alive():
            print('Terminating: %s' % self.cmd)
            self.process.terminate()
            thread.join()

def read_yaml(file_path):
    with open(file_path, "r") as f:
        return yaml.safe_load(f)

"""
IN:  /dir1/dir2/dir3/xxx.apk
OUT: /dir1/dir2/dir3/xxx
"""
def getApkPrefix(apkpath):
    return apkpath[0:-4]

"""
.FileBrowser --> com.dropbox.android.FileBrowser
App --> org.mozilla.firefox.App
"""
def translateName(component, package):
    if component.startswith('.'):
        return '%s%s' % (package, component)
    else:
        if '.' in component:
            return component
        else:
            return '%s.%s' % (package, component)

"""
get package name for an apk
"""
def getPackageName(apk):
    p2 = Popen(['sh', f"{DIR}/package.sh", apk], stdout=PIPE)
    (out, err) = p2.communicate()
    package = out.decode().rstrip('\n')
    return package


def grepSoFile(sopath):
    os.system('strings %s > %s' % (sopath, STRDUMP))

    cmd = 'grep "^socket$" %s' % STRDUMP 
    process = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
    (out1, err1) = process.communicate()

    cmd = 'grep "^bind$" %s' % STRDUMP 
    process = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
    (out2, err2) = process.communicate()

    cmd = 'grep "^listen$" %s' % STRDUMP 
    process = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
    (out3, err3) = process.communicate()

    cmd = 'grep "^accept$" %s' % STRDUMP 
    process = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
    (out4, err4) = process.communicate()

    if out1 != '' and out2 != '' and out3 != '' and out4 != '':
        return os.path.basename(sopath)
    else:
        return ''

"""
com.facebook.orca-49249863-v104.0.0.13.69_unzip$ ls *.dex
classes2.dex  classes4.dex  classes6.dex  classes.dex
classes3.dex  classes5.dex  classes7.dex
"""
def tranDex2Dump(appunzip, applog):
    appdex = '%s/classes.dex' % appunzip
    os.system('%s -d %s > %s' % (dexdump, appdex, applog))

    # Further check starting from classes2.dex
    i = 2
    isMore = 0
    while True:
        appdex = '%s/classes%d.dex' % (appunzip, i)
        templog = '_dexdump%d.log' % i  #Just save in the cmd dir
        if os.path.exists(appdex) == True:
            os.system('%s -d %s > %s' % (dexdump, appdex, templog))
            os.system('cat %s >> %s' % (templog, applog))
            os.system('rm %s' % templog)
            isMore = 1
        else:
            break
        i = i + 1

    return isMore

def myExit(code):
    print('[Main] Start exiting...')
    sys.exit(code)

def flush():
    sys.stdout.flush()


"""
==============
main entry
==============
"""


CONFIGS = read_yaml("../conf.yml")
dexdump = CONFIGS['DEXDUMP']
dex2jar= CONFIGS['DEX2JAR']


# parse param
usage = "usage: python %prog -a apkdir -f listfile -w NO"
parser = OptionParser(usage=usage)
parser.add_option('-a', '--apk', action='store', type='string', dest='apk',
        help='The dir of apk files.')
parser.add_option('-f', '--listfile', action='store', type='string', dest='listfile',
        help='The list of apk files.')
parser.add_option('-w', '--whether', action='store', type='string', dest='whether',
        help='Whether to remove temp files.')
(options, args) = parser.parse_args()
if (not options.apk) and (not options.listfile):
    parser.error('-a (apkdir) or -f (listfile) is mandatory')
if options.apk:
    APPDIR = options.apk
    if APPDIR.endswith('/'):
        APPDIR = APPDIR[:-1]
if options.listfile:
    APPDUMP = options.listfile
ISREMOVE = True     # by default, we remove temp files
if options.whether:
    whether = options.whether
    if whether == 'NO' or whether == 'no' or whether == 'No':
        ISREMOVE = False


# read app list
applist = []
print('[Main] Read app list...')
flush()
if options.apk:
    os.system('ls %s/*.apk > %s' % (APPDIR, APPDUMP))
f = open(APPDUMP, 'r')
for line in f:
    app = line.rstrip('\n') #'appset/org.tint-10-v1.8.apk' #the full path depends on the value of appset
    applist.append(app)
if f:
    f.close()


# http://stackoverflow.com/a/415525/197165
curtime = time.strftime("%Y-%m-%d %H:%M:%S")
print('Current Time: ', curtime)
flush()

# loop app list
i = 0
for app in applist:
    apkprefix = getApkPrefix(app)
    appunzip = '%s_unzip' % apkprefix
    appdex = '%s/classes.dex' % appunzip
    applib = '%s/lib/armeabi' % appunzip
    appjar = '%s_dex2jar.jar' % apkprefix
    applog = '%s_dexdump.log' % apkprefix

    resdex = 0
    resdcl = 0
    reslib = 0
    soname = ''
    muldex = 0

    # unzip
    if os.path.exists(appunzip) == False:
        cmd = 'unzip -n -q %s -d %s' % (app, appunzip)
        process = Popen(cmd, shell=True, stderr=PIPE)
        (out, err) = process.communicate()
        #TODO refer to sdkVersion.py
        if err.decode() != '':
            print('unzip error: %s' % app) 
            flush()
            continue

    # dexdump
    if os.path.exists(applog) == False:
        muldex = tranDex2Dump(appunzip, applog)

    #TODO Do we check dexdump results like sdkVersion.py?

    # print
    i = i + 1
    package = getPackageName(app)
    print('[%d] App: %s' % (i, package))
    flush()

    # grep dex
    cmd = 'cat %s | grep -e ";.startActivity"' % applog
    process = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
    (out, err) = process.communicate()  #TODO error handling
    if out.decode() != '':
        resdex = 1
        # only at this time, we do dex2jar
        if os.path.exists(appjar) == False:
            os.system('%s -f -o %s %s' % (dex2jar, appjar, app))

    # Landroid/content/Context;.startActivity:(Landroid/content/Intent;)V
    
    # grep dynamic code loading
    cmd = 'cat %s | grep -e "Ldalvik/system/DexClassLoader;.loadClass:(Ljava/lang/String;)Ljava/lang/Class;" -e "Ldalvik/system/PathClassLoader;.loadClass:(Ljava/lang/String;Z)Ljava/lang/Class;"' % applog 
    process = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
    (out, err) = process.communicate()  #TODO error handling
    if out.decode() != '':
        resdcl = 1

    #for native code?
    if os.path.exists(applib) == True:
        os.system('ls %s/*.so > %s' % (applib, SODUMP))
        f = open(SODUMP, 'r')
        for line in f:
            sopath = line.rstrip('\r\n') #'weibo_1920_unzip/lib/armeabi/libDeviceInfo.so'
            res = grepSoFile(sopath)
            if res != '':
                #soname = '%s;;%s' % (soname, res)
                soname = res
                break
        if f:
            f.close()

        if soname != '':
            reslib = 1
            #soname = soname[2:] #soname = soname.lstrip(';;')

    # analyze parameters and conditions when resdex == 1
    if resdex and os.path.exists(appjar) and os.path.exists(applog):
        os.system('%s %s %s %s' % (backdroid, apkprefix, package, "StartAct"))
        #os.system('%s %s %s %s' % (backdroid, applog, appjar, package))

    # output
    print('[GrepStartAct] %s\t%d\t%d\t%d\t%d\t%s' % (package, resdex, resdcl, muldex, reslib, soname))
    flush()

    # rm temp files, *_dex2jar.jar has not much space
    if ISREMOVE:
        if os.path.exists(applog):
            os.system('rm %s' % applog)
        if os.path.exists(appunzip):
            os.system('rm -rf %s' % appunzip)


curtime = time.strftime("%Y-%m-%d %H:%M:%S")
print('Current Time: ', curtime)
flush()

if (not options.listfile) and options.apk:
    os.system('rm %s' % APPDUMP)
os.system('rm %s' % SODUMP)
os.system('rm %s' % STRDUMP)

# exit
myExit(0)
