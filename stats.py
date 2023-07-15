#!/usr/bin/python

import os
import sys
import subprocess
from openpyxl import load_workbook
from openpyxl.utils import get_column_letter
import pandas as pd

log_file = sys.argv[1] if len(sys.argv) > 1 else 'data.log'
apk_path = sys.argv[2] if len(sys.argv) > 2 else 'SmartSwitch.apk'
#out_dir = apk_dir
excel_name =  sys.argv[3] if len(sys.argv) > 3 else 'steps'
excel_file = None

#output = {'Activity': [], '#Stmts': [], 'Stmts': [], '#Callers': [], 'Caller entrypoints': []}


def write_results(data, sheetname):
    content = pd.DataFrame(data)
    content = content.sort_values(by='Activity', key=lambda col: col.str.lower())
    num_columns = content.shape[1]
    cell_width = 180/num_columns
    writer = None
    if os.path.isfile(excel_file):
        #file already exists, we append
        print("File already exists, appending")
        writer = pd.ExcelWriter(excel_file, engine='openpyxl', mode='a', if_sheet_exists="replace")
        writer.book = load_workbook(excel_file)
        writer.sheets = dict((ws.title, ws) for ws in writer.book.worksheets) 
        
        content.to_excel(writer, sheet_name=sheetname, index=False)
        #Settings (width of columns, text wrap)
        for col in range(1, num_columns + 1):
            letter = get_column_letter(col)
            writer.sheets[sheetname].column_dimensions[letter].width = cell_width 
            #print(writer.sheets[sheetname])
            #writer.sheets[sheetname][letter].alignment = Alignment(wrap_text=True)
    else:#
        writer = pd.ExcelWriter(excel_file, engine='xlsxwriter')

        content.to_excel(writer, sheet_name=sheetname, index=False)

        #Settings (width of columns, text wrap)
        format = writer.book.add_format({'text_wrap': True})
        writer.sheets[sheetname].set_column(0, num_columns, cell_width, cell_format=format)
        #writer.sheets[sheetname].conditional_format('', {
        #    'type': 'formula',
        #    'criteria':'',
        #    'value': 'N/A',
        #    'format': custom_format
        #})
    ws = writer.sheets[sheetname]
    max_val = ws.max_row
    ws['A2'] = f"=COUNTIF(A3:A{max_val},\"*\")"
    ws['B2'] = f"=SUM(B3:B{max_val})"
    ws['D2'] = f"=COUNTIF(D3:D{max_val},\"<>NO\")"
    writer.save()
    #writer.close()


#Get package names from all apks in folder
def get_package_names():
    package_names = {}
    #print(apk_dir)
    if not os.path.isdir(apk_path) and apk_path.endswith('.apk'):
        print(f"Parsing apk {apk_path}")
        match = None
        cmd = ["sh", "./scripts/package.sh", f"{apk_path}"]
        cmd2 = ["sh", "./scripts/activity-list.sh", f"{apk_path}"]
        p = subprocess.Popen(cmd, stdout=subprocess.PIPE)  
        for line in p.stdout:
            match = line.decode('utf8').replace("\n","").replace("'","")#.split(' ')[1]
            #match = match.split("=")[1].replace("'", "")
            #print(match)
        p.wait()

        if match is not None:
            package_names = []
            p2 = subprocess.Popen(cmd2, stdout=subprocess.PIPE) 
            for line in p2.stdout:
                package_names.append(line.decode('utf8').replace("\n","").replace("'",""))
            p2.wait()
        print(f"Done with package names for {match} {package_names}")
        return (match, package_names)
    return None

def build_act(full_name):
    splits = full_name.replace("\n","").replace("'","").replace("}","").split("/")
    #print(f"Splits: {splits}")
    if(len(splits) < 2):
        return "N/A"
    return f"{splits[0]}{splits[1]}" if splits[1].startswith(".") else splits[1]


#Info for <com.sec.android.easyMover.ui.MainActivity: void runMenuItem(com.sec.android.easyMover.host.ActivityBase$UiMenuType)>
#Targets:  [class "com/sec/android/easyMover/ui/SendOrReceiveActivity"]
#Tail nodes: [BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity]]
#Fake tail nodes [BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity$28: void onStateReceived(com.samsung.android.sdk.bixby.data.State)>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity$28], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity$28: void onStateReceived(com.samsung.android.sdk.bixby.data.State)>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity$28], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop]]
#Field nodes: [BDGUnit [msig=<com.sec.android.easyMover.IAConstants$SelectedExType: void <clinit>()>, unit=$r0 = new com.sec.android.easyMover.IAConstants$SelectedExType]]
def parse_output(activities):
    data = {}

    cmd = ['grep', '-e', "Info for", "-e", "Targets", "-e", "Tail nodes", "-e", "Fake tail nodes", log_file]
    print(f"Executing {cmd}")
    p = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    
    stmt = None
    targets = []
    callers = None
    for line in p.stdout:
        if b"Info for " in line:
            match = (line.decode('utf-8').split('<')[1])[:-2] #com.sec.android.easyMover.ui.MainActivity: void runMenuItem(com.sec.android.easyMover.host.ActivityBase$UiMenuType)>
            #match = match.split("=")[1].replace("'", "")
            stmt = match
        elif b"Targets" in line:
            match = (line.decode('utf-8').split("Targets: ")[1][1:-1].split(',')) #[class "com/sec/android/easyMover/ui/SendOrReceiveActivity"]
            targets = [val.split("class")[1].replace("\"","").replace("/",".").replace("]","").strip() for val in match if val.startswith("class")]
        elif b"Tail nodes" in line: #[BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity]]
            match = (line.decode('utf-8')).split("BDGUnit [msig=<")[1:]
            callers = [val.split(">, unit=")[0] for val in match]
        elif b"Fake tail nodes" in line:
            if callers is None:
                match = (line.decode('utf-8')).split("BDGUnit [msig=<")[1:]
                callers = [val.split(">, unit=")[0] for val in match]
            for target in targets:
                if target in activities:
                    e_stmts, e_callers = data.get(target,[[], []])
                    data[target] = [e_stmts + [stmt], e_callers + callers]
            callers = None
            stmt = None
            targets = None
    p.wait()
    print(f"Parsed info {data.keys()}")

    output = {'Activity': [], '#Stmts': [], 'Stmts': [], 'Has caller activity?': [], '#Callers': [], 'Callers': []}
    output['Activity'].append("")
    output['Activity'].extend(data.keys())

    output['#Stmts'].append("")
    output['Stmts'].append("")
    output['Has caller activity?'].append("")
    output['#Callers'].append("")
    output['Callers'].append("")
    for act, info in data.items():
        num_stmts = len(info[0])
        stmts = "\n".join(info[0])
        callers = list(set(info[1]))
        num_callers = len(callers)
        has_caller_activity = check_caller_activity(act, callers, activities)
        callers = "\n".join(callers)
        output['#Stmts'].append(num_stmts)
        output['Stmts'].append(stmts)
        output['Has caller activity?'].append(has_caller_activity)
        output['#Callers'].append(num_callers)
        output['Callers'].append(callers)

    return output


def check_caller_activity(act, callers, activities):
    if len(callers) == 0:
        return "NO"
    unique_callers = [val.split(":")[0] for val in callers if (val.split(":")[0] is not act) and val.split(":")[0] in activities]
    #print(f"Found {unique_callers} for {act}")
    return "YES" if len(unique_callers) > 0 else "NO"


def main():
    global excel_file
    global excel_name
    sheet_name, activities = get_package_names()
    excel_file = f"{excel_name}.xlsx" 
    print("Collecting stats from BackDroid")
    write_results(parse_output(activities), sheet_name)



if __name__ == '__main__':
    main()



# Landroid/content/Context;.startActivity:(Landroid/content/Intent;)V  
#    # grep dynamic code loading
#    cmd = 'cat %s | grep -e "Ldalvik/system/DexClassLoader;.loadClass:(Ljava/lang/String;)Ljava/lang/Class;" -e "Ldalvik/system/PathClassLoader;.loadClass:(Ljava/lang/String;Z)Ljava/lang/Class;"' % applog 
#    process = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
#    (out, err) = process.communicate()  #TODO error handling
#    if out.decode() != '':
#        resdcl = 1