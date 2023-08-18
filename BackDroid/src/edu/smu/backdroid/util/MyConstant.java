package edu.smu.backdroid.util;

import soot.jimple.internal.JNopStmt;

public class MyConstant {

    public static final String ServerSocketPrefix = "<java.net.ServerSocket: ";
    public static final String ServerSocketKeyword = "ServerSocket";
    public static final String HostnameVerifierKeyword = "setHostnameVerifier";
    public static final String CipherInstanceKeyword = "javax.crypto.Cipher getInstance";
    public static final String StartActKeyword = "startActivity";
    public static final String ContextKeyword = "<android.content.Context: ";
    public static final String BundleKeyword = "android.os.Bundle";
    public static final String StartActForResultKeyword = "startActivityForResult";
    public static final String PendingIntentGetActKeyWord = "getActivity";
    public static final String PendingIntentGetBrKeyWord = "getBroadcast";
    public static final String AlarmManagerSetKeyWord = "<android.app.AlarmManager: void set";
    public static final String PendingIntentSendKeyWord = "<android.app.PendingIntent: void send";
    public static final String IntentSenderSendIntentKeyWord = "<android.content.IntentSender: void sendIntent(";

    public static final String AndroidXKeyword = "<androidx.";
    public static final String AndroidKeyword = "<android";
    public static final String FragmentKeyword = "fragment";

    
    public static final String NormalPrefix = "   ";
    public static final String ForwardPrefix = "-->";
    public static final String BackPrefix = "<==";
    public static final String CriticalPrefix = "***";
    public static final String ErrorPrefix = "+++";
    
    public static final String ReturnParam = "@return";
    
    // TODO \\ or \?
    public static final String GrepMethodKeyword = ":                                        |\\[";
    
    /**
     * http://developer.android.com/reference/java/net/ServerSocket.html
     */
    public static final String ServerSocketInit1 = "<java.net.ServerSocket: void <init>(int)>";
    public static final String ServerSocketInit2 = "<java.net.ServerSocket: void <init>(int,int)>";
    public static final String ServerSocketInit3 = "<java.net.ServerSocket: void <init>(int,int,java.net.InetAddress)>";
    public static final String ServerSocketBind1 = "<java.net.ServerSocket: void bind(java.net.SocketAddress)>";
    public static final String ServerSocketBind2 = "<java.net.ServerSocket: void bind(java.net.SocketAddress,int)";
    
    /**
     * http://developer.android.com/reference/javax/net/ssl/SSLServerSocket.html
     */
    public static final String SSLServerSocketInit1 = "<javax.net.ssl.SSLServerSocket: void <init>(int)>";
    public static final String SSLServerSocketInit2 = "<javax.net.ssl.SSLServerSocket: void <init>(int,int)>";
    public static final String SSLServerSocketInit3 = "<javax.net.ssl.SSLServerSocket: void <init>(int,int,java.net.InetAddress)>";
    
    /**
     * http://developer.android.com/reference/javax/net/ServerSocketFactory.html
     */
    public static final String ServerSocketFactoryCreate1 = 
            "<javax.net.ServerSocketFactory: java.net.ServerSocket createServerSocket(int)>";
    public static final String ServerSocketFactoryCreate2 = 
            "<javax.net.ServerSocketFactory: java.net.ServerSocket createServerSocket(int,int)>";
    public static final String ServerSocketFactoryCreate3 = 
            "<javax.net.ServerSocketFactory: java.net.ServerSocket createServerSocket(int,int,java.net.InetAddress)>";
    
    /**
     * For ERP
     */
    public static final String CipherGetInstance = 
            "<javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>";
    public static final String FactorySetHostnameVerifier = 
            "<org.apache.http.conn.ssl.SSLSocketFactory: void setHostnameVerifier(org.apache.http.conn.ssl.X509HostnameVerifier)>";
    public static final String URLSetHostnameVerifier = 
            "<javax.net.ssl.HttpsURLConnection: void setHostnameVerifier(javax.net.ssl.HostnameVerifier)>";
    

    /**
     * ICC methods
     */


    public static final String StartActContextIntent= "<android.content.Context: void startActivity(android.content.Intent)>";
    public static final String StartActContextIntentBundle = "<android.content.Context: void startActivity(android.content.Intent,android.os.Bundle)>";

    public static final String GetIntent = "<android.app.Activity: android.content.Intent getIntent()>";

    public static final String OnActivityResult = "<android.app.Activity: void onActivityResult(int,int,android.content.Intent)>";

        public static final String StartActIntent= "<android.app.Activity: void startActivity(android.content.Intent)>";
        public static final String StartActIntentBundle = "<android.app.Activity: void startActivity(android.content.Intent,android.os.Bundle)>";

        public static final String AndroidX_StartActFragIntent = "<androidx.fragment.app.Fragment: void startActivity(android.content.Intent)>";
        public static final String AndroidX_StartActFragIntentBundle = "<androidx.fragment.app.Fragment: void startActivity(android.content.Intent,android.os.Bundle)>";

        public static final String StartActForResultIntent = "<android.app.Activity: void startActivityForResult(android.content.Intent,int)>";
        public static final String StartActForResultIntentBundle = "<android.app.Activity: void startActivityForResult(android.content.Intent,int,android.os.Bundle)>";
        public static final String AndroidX_StartActForResultIntent = "<androidx.activity.ComponentActivity: void startActivityForResult(android.content.Intent,int)>";
        public static final String AndroidX_StartActForResultIntentBundle = "<androidx.activity.ComponentActivity: void startActivityForResult(android.content.Intent,int,android.os.Bundle)>";

        public static final String AndroidX_StartActForResultFragIntent= "<androidx.fragment.app.Fragment: void startActivityForResult(android.content.Intent,int)>";


public static final String GetActivity = "<android.app.PendingIntent: android.app.PendingIntent getActivity(";
public static final String GetBroadcast = "<android.app.PendingIntent: android.app.PendingIntent getBroadcast(";


// package intent into a PendingIntent
    public static final String ANDROID_APP_PENDING_INTENT_GET_ACTIVITY = "<android.app.PendingIntent: android.app.PendingIntent getActivity(android.content.Context,int,android.content.Intent,int)>";
    public static final String ANDROID_APP_PENDING_INTENT_GET_SERVICE = "<android.app.PendingIntent: android.app.PendingIntent getService(android.content.Context,int,android.content.Intent,int)>";
    public static final String ANDROID_APP_PENDING_INTENT_GET_BROADCAST = "<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>";

    public static final String ANDROID_APP_PENDING_INTENT_GET_INTENTSENDER = "<android.app.PendingIntent: android.content.IntentSender getIntentSender(android.app.PendingIntent)>";

    // send PendingIntent
    //public static final String ANDROID_APP_ACTIVITYOPTIONS_REQUESTUSAGETIMEREPORT = "<android.app.ActivityOptions: void requestUsageTimeReport(android.app.PendingIntent)>";
    //public static final String ANDROID_APP_ALARMMANAGER_SETALARMCLOCK = "<android.app.AlarmManager: void setAlarmClock(android.app.AlarmManager$AlarmClockInfo,android.app.PendingIntent)>";
    public static final String[] AlarmManagerSetMethods = {"<android.app.AlarmManager: void setAndAllowWhileIdle(int,long,android.app.PendingIntent)>",
    "<android.app.AlarmManager: void setExactAndAllowWhileIdle(int,long,android.app.PendingIntent)>",
        "<android.app.AlarmManager: void setExact(int,long,android.app.PendingIntent)>",
     "<android.app.AlarmManager: void setInexactRepeating(int,long,long,android.app.PendingIntent)>",
     "<android.app.AlarmManager: void set(int,long,android.app.PendingIntent)>",
     "<android.app.AlarmManager: void setRepeating(int,long,long,android.app.PendingIntent)>",
    "<android.app.AlarmManager: void setWindow(int,long,long,android.app.PendingIntent)>"};



        public static final String[] PendingIntentSendMethods = {"<android.app.PendingIntent: void send(android.content.Context,int,android.content.Intent,android.app.PendingIntent$OnFinished,android.os.Handler,java.lang.String,android.os.Bundle)>",
    "<android.app.PendingIntent: void send(android.content.Context,int,android.content.Intent,android.app.PendingIntent$OnFinished,android.os.Handler,java.lang.String)>",
    "<android.app.PendingIntent: void send(android.content.Context,int,android.content.Intent,android.app.PendingIntent$OnFinished,android.os.Handler)>",
    "<android.app.PendingIntent: void send(android.content.Context,int,android.content.Intent)>",
    "<android.app.PendingIntent: void send(int,android.content.Intent,android.app.PendingIntent$OnFinished,android.os.Handler)>",
    "<android.app.PendingIntent: void send()>",
    "<android.app.PendingIntent: void send(int)>"};

    public static final String[] IntentSenderSendIntentMethods = {"<android.content.IntentSender: void sendIntent(android.content.Context,int,android.content.Intent,android.content.IntentSender$OnFinished,android.os.Handler)>",
        "<android.content.IntentSender: void sendIntent(android.content.Context,int,android.content.Intent,android.content.IntentSender$OnFinished,android.os.Handler,java.lang.String)>"};


//<android.app.Activity: void startActivityFromChild(android.app.Activity,android.content.Intent,int,android.os.Bundle)>
//<android.app.Activity: void startActivityFromChild(android.app.Activity,android.content.Intent,int)>
//<android.app.Activity: void startActivityFromFragment(android.app.Fragment,android.content.Intent,int,android.os.Bundle)>
//<android.app.Activity: void startActivityFromFragment(android.app.Fragment,android.content.Intent,int)>
//<android.app.Activity: void startActivityIfNeeded(android.content.Intent,int,android.os.Bundle)>
//<android.app.Activity: void startActivityIfNeeded(android.content.Intent,int)>



    /**
     * IPC methods
     */
    public static final String startService_SubSig = 
            "android.content.ComponentName startService(android.content.Intent)>";
    public static final String sendBroadcast_SubSig = 
            "void sendBroadcast(android.content.Intent)>";
    public static final String startActivityForResult_SubSig = 
            "void startActivityForResult(android.content.Intent,int)";
    
    public static final int INFO = 0;
    public static final int DEBUG = 1;
    public static final int WARN = 2;
    public static final int RELEASE = 3;
    public static int CURRENTRANK = DEBUG;
    
    public static final String GLOBALFIELD = "GLOBALFIELD";
    
    public static final String MiddleWord = "---";  //TODO more unique
    public static final String ParamMiddle = "++";
    
    /**
     * The keywords used in interface/parent search
     * Need to be within three characters
     */
    public static final String Search_IN = "IN";
    public static final String Search_INSYS = "INSYS";
    public static final String Search_INAPP = "INAPP";
    
    /**
     * Some SDK patterns
     */
    public static final String Facebook_SDK = "<com.facebook.ads.";
    public static final String Mopub_SDK = "<com.mopub.";
    public static final String GoogleAd_SDK = "<com.google.ads.";
    public static final String Heyzap_SDK = "<com.heyzap.";
    public static final String Flurry_SDK = "<com.flurry.";
    
    /**
     * Result output format
     */
    public static final String RANDOM_RES = "RANDOM";
    public static final String NULL_RES = "EMPTY";
    public static final String NO_TAG = "";
    
    public static final JNopStmt FLAG_STMT = new JNopStmt();
    public static final JNopStmt NOP_STMT = new JNopStmt();
    
    /**
     * Output string
     */
    public static final String DeadCross_Forward = "Detect a dead cross loop in Forward";
    public static final String DeadInner_Forward = "Detect a dead inner loop in Forward";
    
    public static final String DeadCross_Backward = "Detect a dead cross loop in Backward";
    public static final String DeadInner_Backward = "Detect a inner cross loop in Backward";
    
    /**
     * Manifest types
     */
    public static final int MType_ERR = -1;
    public static final int MType_PKG = 0;
    public static final int MType_APP = 1;
    public static final int MType_ACTIVITY = 2;
    public static final int MType_SERVICE = 3;
    public static final int MType_PROVIDER = 4;
    public static final int MType_RECEIVER = 5;
    
    /**
     * Maximum caller each time
     */
    public static final int MAX_CALLER_NUM = 5;
    
    public static final int MAX_LIVE_ENTRYR_NUM = 3;
    
    public static final int MAX_DEAD_ENTRYR_NUM = 5;
    
    /**
     * The detection choice. Default is DETECT_CRYPTO
     */
    public static final int DETECT_CRYPTO = 0;
    public static final int DETECT_OPENPORT = 1;
    public static final int DETECT_STARTACT = 2;
    
    public static String dumpClassSer = "../lib/android-classes.ser";
    
}
