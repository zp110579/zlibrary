package com.zee.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.zee.log.ZLog;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Administrator on 2017/9/13 0013.
 * 系统信息
 */

public class ZSystemInfoUtils {

    public static String getVersionName() {
        String version = "1.00";
        try {
            Context application = ZLibrary.getInstance().getApplicationContext();
            PackageInfo pInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (Exception e) {
            version = "1.00";
        }
        return version;
    }

    public static int getVersionCode() {
        int version = 0;
        try {
            Context application = ZLibrary.getInstance().getApplicationContext();
            PackageInfo pInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (Exception e) {
            version = 1;
        }
        return version;
    }

    /**
     * @return 手机型号
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }


    public static String getMobileName() {//获得移动运营商名称
        Context application = ZLibrary.getInstance().getApplicationContext();
        TelephonyManager telManager = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telManager.getSimOperator();
        String ProvidersName = null;

        if (imsi == null) {
            return "unknow";
        }

        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (imsi.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (imsi.startsWith("46003")) {
            ProvidersName = "中国电信";
        }

        ZLog.i("当前卡为：" + ProvidersName);
        return ProvidersName;
    }

    public static String getMetaDataValue(String key) {
        Context context = ZLibrary.getInstance().getApplicationContext();

        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String dataValue = appInfo.metaData.getString(key);
            return dataValue;
        } catch (PackageManager.NameNotFoundException e) {
            ZLog.exception(e);
        }
        return "";
    }

    public static String getIPAddress() {//获得当前IP
        Context context = ZLibrary.getInstance().getApplicationContext();
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static String getConnectedTpe() {//获得当前是WIFI还是移动网络
        Context context = ZLibrary.getInstance().getApplicationContext();
        String infor = "";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            switch (ni.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    infor = "WIFI";
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    int value = ni.getSubtype();
                    switch (value) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: //联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: //电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: //移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            infor = "2G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: //电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            infor = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            infor = "4G";
                            break;
                        default:
                            infor = "未知";
                    }
                    break;
            }
        }
        return infor;
    }

    public static String getMetaData(String key) {
        try {
            Context context = ZLibrary.getInstance().getApplicationContext();
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public final static List<Class> getManifestActivities() {

        List<Class> returnClassList = new ArrayList<Class>();
        try {
            Context context = ZLibrary.getInstance().getApplicationContext();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (packageInfo.activities != null) {
                for (ActivityInfo ai : packageInfo.activities) {
                    Class c;
                    try {
                        c = Class.forName(ai.name);
                        if (Activity.class.isAssignableFrom(c)) {
                            returnClassList.add(c);
                        }
                    } catch (ClassNotFoundException e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnClassList;
    }

    /**
     * 检查通知栏权限有没有开启
     * 参考SupportCompat包中的： NotificationManagerCompat.from(context).areNotificationsEnabled();
     */
    public static boolean isNotificationEnabled() {
        Context context = UIUtils.getApplication();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return (Integer) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg) == 0;
            } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException | RuntimeException | ClassNotFoundException ignored) {
                return true;
            }
        } else {
            return true;
        }
    }

    //重启App
    static void restartApp() {
        Context context = UIUtils.getApplication();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        //与正常页面跳转一样可传递序列化数据,在Launch页面内获得
        intent.putExtra("REBOOT", "reboot");
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
