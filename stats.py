#!/usr/bin/python

import os
import sys
import subprocess
from openpyxl import load_workbook
from openpyxl.utils import get_column_letter
import pandas as pd


apk_dir = sys.argv[1]
excel_name =  sys.argv[2] if len(sys.argv) > 2 else 'analysis'
log_dir = sys.argv[3] if len(sys.argv) > 3 else apk_dir
#log_file = sys.argv[1] if len(sys.argv) > 1 else 'data.log'
#apk_path = sys.argv[2] if len(sys.argv) > 2 else 'SmartSwitch.apk'
#out_dir = apk_dir

#log_file_id = "LOG"
log_file_id = "DEBUG_INTERMEDIATE_LOG"
excel_file = None

#output = {'Activity': [], '#Stmts': [], 'Stmts': [], '#Callers': [], 'Caller entrypoints': []}


def write_results(data, sheetname, summary=False):
    content = pd.DataFrame(data)
    if not summary:
        content = content.sort_values(by='Activity', key=lambda col: col.str.lower())
    num_columns = content.shape[1]
    cell_width = 200/num_columns
    writer = None
    if os.path.isfile(excel_file):
        #file already exists, we append
        print("File already exists, appending")
        writer = pd.ExcelWriter(excel_file, engine='openpyxl', mode='a', if_sheet_exists="replace")
        writer.book = load_workbook(excel_file)
        writer.sheets = dict((ws.title, ws) for ws in writer.book.worksheets) 
        
        content.to_excel(writer, sheet_name=sheetname, index=False)
        ws = writer.sheets[sheetname]
        #Settings (width of columns, text wrap)
        for col in range(1, num_columns + 1):
            letter = get_column_letter(col)
            ws.column_dimensions[letter].width = cell_width 
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
    writer.save()
    #writer.close()


#Get package names from all apks in folder
def get_package_names(apk_path):
    package_names = {}
    #print(apk_dir)
    if not os.path.isdir(apk_path) and apk_path.endswith('.apk'):
        print(f"Parsing apk {apk_path}")
        match = None
        cmd = ["sh", "./scripts/package.sh", f"{apk_path}"]
        p = subprocess.Popen(cmd, stdout=subprocess.PIPE)  
        for line in p.stdout:
            match = line.decode('utf8').replace("\n","").replace("'","")#.split(' ')[1]
            #match = match.split("=")[1].replace("'", "")
            #print(match)
        p.wait()

        if match is not None:
            return (match, get_comp_from_manifest(apk_path,"activity"), get_comp_from_manifest(apk_path,"service"), get_comp_from_manifest(apk_path,"receiver"))
    return None

def get_comp_from_manifest(apk_path, type="activity"):
    cmd2 = ["sh", "./scripts/activity-list.sh", f"{apk_path}", type]
    package_names = []
    p2 = subprocess.Popen(cmd2, stdout=subprocess.PIPE) 
    for line in p2.stdout:
        package_names.append(line.decode('utf8').replace("\n","").replace("'",""))
    p2.wait()
    #print(f"Found {type} for {apk_path} {package_names}")
    return package_names

def build_act(full_name):
    splits = full_name.replace("\n","").replace("'","").replace("}","").split("/")
    #print(f"Splits: {splits}")
    if(len(splits) < 2):
        return "N/A"
    return f"{splits[0]}{splits[1]}" if splits[1].startswith(".") else splits[1]


#Info for <com.sec.android.easyMover.ui.MainActivity: void runMenuItem(com.sec.android.easyMover.host.ActivityBase$UiMenuType)>
#Targets:  [class "com/sec/android/easyMover/ui/SendOrReceiveActivity"] or ["com/sec/android/easyMover/ui/SendOrReceiveActivity"]
#Tail nodes: [BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void onResume()>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity]]
#Fake tail nodes [BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity$28: void onStateReceived(com.samsung.android.sdk.bixby.data.State)>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity$28], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity$28: void onStateReceived(com.samsung.android.sdk.bixby.data.State)>, unit=r0 := @this: com.sec.android.easyMover.ui.MainActivity$28], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop], BDGUnit [msig=<com.sec.android.easyMover.ui.MainActivity: void <init>()>, unit=nop]]
#Field nodes: [BDGUnit [msig=<com.sec.android.easyMover.IAConstants$SelectedExType: void <clinit>()>, unit=$r0 = new com.sec.android.easyMover.IAConstants$SelectedExType]]
def parse_output(log_file, activities, services, receivers):
    data = {}
    
    cmd = ['grep', '-e', "Exception in thread \"main\"", log_file]
    print(f"Executing {cmd}")

    p = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    (out, err) = p.communicate()
    potential_exception = out.decode()
    if potential_exception != '':
        print(f"Exception {potential_exception} found in {log_file}")
        return (None, potential_exception)
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
        elif b"Targets: " in line:
            #print(line.decode("utf-8").replace("\n",""))
            match = (line.decode('utf-8').split("Targets: ")[1][1:-1].split(', ')) #[class "com/sec/android/easyMover/ui/SendOrReceiveActivity"] or ["com/sec/android/easyMover/ui/SendOrReceiveActivity"]
            targets = [val.replace("class","").replace("\"","").replace("/",".").replace("]","").strip() for val in match if val.startswith("class") or val.startswith("\"")]
            #print(f"Intermediate targets {targets}")
            targets = [val for val in targets if val in activities]
            #print(f"Obtained targets {targets}\n")
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
    #print(f"Parsed info {data.keys()}")

    output = {'Activity': [], '#Stmts': [], 'Stmts': [], 'Has caller activity?': [], 'Has caller component?': [],'#Callers': [], 'Callers': []}

    index = 2 # first lines
    max_val = len(data.keys()) + index
    output['Activity'].append(f"=COUNTIF(A3:A{max_val},\"*\")")
    output['Activity'].extend(data.keys())


    output['#Stmts'].append(f"=SUM(B3:B{max_val})")
    output['Stmts'].append("")
    output['Has caller activity?'].append(f"=COUNTIF(D3:D{max_val},\"<>NO\")")
    output['Has caller component?'].append(f"=COUNTIF(E3:E{max_val},\"<>NO\")")
    output['#Callers'].append("")
    output['Callers'].append("")
    for act, info in data.items():
        num_stmts = len(info[0])
        stmts = "\n".join(info[0])
        callers = list(set(info[1]))
        num_callers = len(callers)
        has_caller_activity = check_caller_component(act, callers, activities)
        has_caller_component = has_caller_activity or check_caller_component(act, callers, services) or check_caller_component(act, callers, receivers)
        callers = "\n".join(callers)
        output['#Stmts'].append(num_stmts)
        output['Stmts'].append(stmts)
        output['Has caller activity?'].append(has_caller_activity)
        output['Has caller component?'].append(has_caller_component)
        output['#Callers'].append(num_callers)
        output['Callers'].append(callers)

    return (output, None)


def check_caller_component(act, callers, components):
    if len(callers) == 0:
        return "NO"
    unique_callers = [val.split(":")[0] for val in callers if (val.split(":")[0] is not act) and val.split(":")[0] in components]
    #print(f"Found {unique_callers} for {act}")
    return "YES" if len(unique_callers) > 0 else "NO"

#def analysis_summary():
    #Build the summary table
    #Apk Package #Activities (total) #Activities (resolved ICC) #Activities (found caller act)

def prep_summary(max):
    summary = {'Apk': [], 'Package Name': [], '#Activities (total)': [],  '#Activities (resolved ICC)': [], '%Activities (resolved ICC)': [],'#Activities (with caller activity)': [], '%Activities (with caller activity)': [], '#Activities (with caller comp)': [],'%Activities (with caller comp)': [] }
    summary["Apk"].append("")
    summary["Package Name"].append("")
    summary["#Activities (total)"].append("")
    summary["#Activities (resolved ICC)"].append("")
    summary["#Activities (with caller activity)"].append("")
    summary["#Activities (with caller comp)"].append("")
    summary["%Activities (resolved ICC)"].append(f"=AVERAGEA(E3:E{max})")
    summary["%Activities (with caller activity)"].append(f"=AVERAGEA(G3:G{max})")
    summary["%Activities (with caller comp)"].append(f"=AVERAGEA(I3:I{max})")
    return summary

def main():
    global excel_file
    global excel_name

    excel_file = f"{excel_name}.xlsx" 
    print("Collecting stats from BackDroid")


    summary = prep_summary(50) #to automate
    summary_sheet_name = "Summary"
    write_results(summary, summary_sheet_name, summary=True)
    index = 2
    for apk in os.listdir(apk_dir):
        if apk.endswith('.apk'):
            apk_path = f"{apk_dir}/{apk}"
            apk_name = apk[:-4]
            sheet_name = apk_name
            #Get info about the apk
            package_name, activities, services, receivers = get_package_names(apk_path)
            num_activities = len(activities)
            print(f"Found apk {apk_path} => {package_name}")
            log_file = f"{log_dir}/{log_file_id}_{package_name}.log"
            if(os.path.exists(log_file)):
                index = index + 1
                print(f"Found log file: {log_file}")
                (data, exc) = parse_output(log_file,activities, services, receivers)
                summary["Apk"].append(apk_name)
                summary["Package Name"].append(package_name)
                summary["#Activities (total)"].append(num_activities)
                if exc is None:
                    summary["#Activities (resolved ICC)"].append(f"='{sheet_name}'!A2")
                    summary["%Activities (resolved ICC)"].append(f"=(100 * D{index}/C{index})")
                    summary["#Activities (with caller activity)"].append(f"='{sheet_name}'!D2")
                    summary["%Activities (with caller activity)"].append(f"=(100 * F{index}/C{index})")
                    summary["#Activities (with caller comp)"].append(f"='{sheet_name}'!E2")
                    summary["%Activities (with caller comp)"].append(f"=(100 * H{index}/C{index})")
                    write_results(data, sheet_name)
                else:
                    summary["#Activities (resolved ICC)"].append(exc)
                    summary["#Activities (with caller activity)"].append("")
                    summary["#Activities (with caller comp)"].append("")
                    summary["%Activities (resolved ICC)"].append(exc)
                    summary["%Activities (with caller activity)"].append("")
                    summary["%Activities (with caller comp)"].append("")
    write_results(summary, summary_sheet_name, summary=True)

if __name__ == '__main__':
    main()



# Landroid/content/Context;.startActivity:(Landroid/content/Intent;)V  
#    # grep dynamic code loading
#    cmd = 'cat %s | grep -e "Ldalvik/system/DexClassLoader;.loadClass:(Ljava/lang/String;)Ljava/lang/Class;" -e "Ldalvik/system/PathClassLoader;.loadClass:(Ljava/lang/String;Z)Ljava/lang/Class;"' % applog 
#    process = Popen(cmd, shell=True, stdout=PIPE, stderr=PIPE)
#    (out, err) = process.communicate()  #TODO error handling
#    if out.decode() != '':
#        resdcl = 1