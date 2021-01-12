package com.xqd.mylibrary.utlis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.xqd.mylibrary.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * 应用程序工具类.
 *
 * @author markmjw
 * @date 2014-08-17
 */
public final class AppUtil {
    private static final String TAG = "AppUtil";
    private static String sAndroidId;
    private static String mChannel;
    private static String mReqDeviceId;
    public static final String WIFI = "WIFI";
    public static final String G3 = "3G";
    public static final String G2 = "2G";
    public static final String G4 = "4G";

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     *
     * @param context
     * @return if application is in background return true, otherwise return false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否是主进程
     *
     * @return
     */
    public static boolean isMainProcess(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String packageNames = info.packageName;
            if (!TextUtils.isEmpty(packageNames) && packageNames.equals(getCurrentProcessName(context))) {
                return true;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取当前进程名称
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取签名
     *
     * @param context
     * @return
     */
    public static String getSignature(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();

            Signature[] signatures = new Signature[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                signatures = packageInfo.signingInfo.getApkContentsSigners();
            } else {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
                signatures = packageInfo.signatures;
            }
            return signatures[0].toCharsString();

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本号]
     *
     * @param context
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 系统
     *
     * @return
     */
    public static String getOS() {
        return "Android";
    }

    /**
     * 系统版本号
     *
     * @return
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 设备名称
     *
     * @return
     */
    public static String getDevice() {
        return Build.MODEL;
    }

    /**
     * 得到设备唯一识别码
     *
     * @param context
     * @return
     */
    public static String getDEVICE_ID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String DEVICEID = tm.getDeviceId();
        return DEVICEID;
    }


    /**
     * 设备唯一标示
     *
     * @param context
     * @return MD5(IMEI + android_id)
     */
    public static String getReqDeviceId(Context context) {
        if (TextUtils.isEmpty(mReqDeviceId)) {
            final StringBuilder gDidSb = new StringBuilder();
            String imei = getDEVICE_ID(context);
            if (!TextUtils.isEmpty(imei)) {
                gDidSb.append(imei);
            }

            String android_id = getAndroidId(context);
            if (!TextUtils.isEmpty(android_id)) {
                gDidSb.append(android_id);
            }

//            mReqDeviceId = TextUtil.encrypt(gDidSb.toString(), "MD5");
        }
        return mReqDeviceId;
    }

    public static String getUUID(Context context) {

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString().replace("-", "");
        return uniqueId;

    }

    /**
     * 获取androidId.
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        if (TextUtils.isEmpty(sAndroidId)) {
            sAndroidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        }
        return sAndroidId;
    }


    /**
     * 获取本地Mac地址.
     *
     * @param context the context
     * @return empty string if failed
     */
    public static String getMacAddress(Context context) {
        String macAdress = null;
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

//            WifiInfo info = wifi.getConnectionInfo();

//            macAdress = info.getMacAddress();
        } catch (Throwable e) {

        }
        return macAdress == null ? "" : macAdress;
    }

    /**
     * 通过包名来检查一个应用是否存在.
     *
     * @param context     the context
     * @param packageName the package name
     * @return true, if successful
     */
    public static boolean checkApplicationExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断手机上是否安装了某个程序（根据包名）
     *
     * @param context
     * @param pkgName
     * @return
     */
    public static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }

    }


    /**
     * 运营商
     *
     * @param context
     * @return
     */
    public static String getCarrier(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }

    /**
     * 语言、国家
     *
     * @return
     */
    public static String getLocale() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    /**
     * 获取分辨率
     *
     * @return
     */
    public static String getResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics.heightPixels + "x" + metrics.widthPixels;
    }

    /**
     * 判断是否使用MIUI
     *
     * @return
     */
    public static boolean isMIUI() {
        String systemProperty = getSystemProperty("ro.miui.ui.version.name");
        return !TextUtils.isEmpty(systemProperty);
    }

    /**
     * 判断是否Flyme os
     *
     * @return
     */
//    public static boolean isFlyme() {
//        return Build.BRAND.contains("Meizu") || Build.BRAND.contains("meizu") || Build.BRAND
//                .contains("MEIZU");
//    }

    /**
     * 判断是否Flyme os
     *
     * @return
     */
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {

            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {

                }
            }
        }
        return line;
    }

    /**
     * 是否为竖屏
     *
     * @param context
     * @return
     */
    public static boolean isPortrait(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        int ori = configuration.orientation;

        if (ori == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        } else if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取渠道号
     *
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        if (TextUtils.isEmpty(mChannel)) {
            try {
                mChannel = getMetaData(context, "UMENG_CHANNEL");
            } catch (Exception e) {

            }
        }
        return mChannel;
    }

    private static String getMetaData(Context context, String key) throws NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        if (null != pm) {
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (null != ai.metaData) {
                Object value = ai.metaData.get(key);
                if (value != null) {
                    return value.toString();
                }
            }
        }
        return null;
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 连接超时
     *
     * @param context
     */
    public static void showTimeOut(Context context) {
        Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
    }

    /**
     * 网络是否可用，包括3G和WIFI中的任意一种
     */
    public static boolean NetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null || !info.isConnected()) {
                return false;
            }
            if (info.isRoaming()) {
                return true;
            }
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        return true;
    }

    /**
     * 返回网络状态 2G 3G 4G WIFI
     *
     * @param context
     * @return
     */
    public static String GetNetworkType(Context context) {
        String strNetworkType = "";
        NetworkInfo networkInfo = null;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivity.getActiveNetworkInfo();
        } catch (Exception e) {
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = G2;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = G3;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = G4;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = G3;
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

                Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        Log.e("cocos2d-x", "Network Type : " + strNetworkType);

        return strNetworkType;
    }

    /**
     * 得到状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static View getActionBarView(Activity activity) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        int resId = activity.getResources().getIdentifier("action_bar_container", "id", "android");

        return view.findViewById(resId);
    }

    public static int getBomBarHeight(Activity context) {
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.bottom;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(Integer.MAX_VALUE);
        if (myList == null || myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 判断某个activity是否还存在
     *
     * @param mContext
     * @param className 是包名+activity的类名（例如：android.youma.com.ui.activity.track.MovenmentMovingActivity）
     * @return
     */
    public static boolean isActivityWork(Context mContext, String className) {
        boolean isWork = false;
        Intent intent = new Intent();
        intent.setClassName(mContext.getPackageName(), className);
        ResolveInfo resolveInfo = mContext.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null) {
            isWork = true;
        } else {
            isWork = false;
        }
        return isWork;
    }

    /**
     * 用于释放由于android.view.inputmethod.InputMethodManager引起的内存泄漏 在Acticity的
     *
     * @param destContext
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
//                        if (QLog.isColorLevel()) {
//                            QLog.d(ReflecterHelper.class.getSimpleName(), QLog.CLR, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext()+" dest_context=" + destContext);
//                        }
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 判断异常类型是否为超时
     *
     * @param throwable
     * @return
     */
    public static boolean isSocketTimeoutExceptionmessage(Throwable throwable) {
        if ("java.net.SocketTimeoutException".equals(throwable.getClass().getName())) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 是否打开GPS
     *
     * @param context
     * @return
     */
    public static final boolean isOPenGPS(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }

        return false;
    }

    /**
     * 判断手机是否处于锁屏状态
     *
     * @param context
     * @return
     */
    public static boolean screenLock(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
        return flag;
    }


    /**
     * 判断是否开启usb调试
     *
     * @param mContext
     * @return
     */
    public static boolean enableAdb(Context mContext) {
        return Secure.getInt(mContext.getContentResolver(), Settings.Global.ADB_ENABLED, 0) > 0;
    }

    /**
     * 判断是否开启开发者模式
     *
     * @param mContext
     * @return
     */
    public static boolean enableDevelopment(Context mContext) {
        return Secure.getInt(mContext.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) > 0;
    }

    /**
     * 判断是否允许模拟位置
     *
     * @param mContext
     * @return
     */
    public static boolean enableLocation(Context mContext) {
        return (Secure.getInt(mContext.getContentResolver(), Secure.ALLOW_MOCK_LOCATION, 0) != 0);

    }

    /**
     * 检测是否有root文件
     *
     * @return
     */
    public static boolean checkSuFile() {
        Process process = null;
        try {
            //   /system/xbin/which 或者  /system/bin/which
            process = Runtime.getRuntime().exec(new String[]{"which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    /**
     * 检测是否有root文件(这个方法写的有问题)
     *
     * @return
     */
    public static File checkRootFile() {
        File file = null;
        String[] paths = {"/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su"};
        for (String path : paths) {
            file = new File(path);
            if (file.exists()) {
                Log.e("root文件夹", file.getAbsolutePath());
                return file;
            }
        }
        return file;
    }


    public static boolean isRootSystem() {
        if (isRootSystem1() || isRootSystem2()) {
            //TODO 可加其他判断 如是否装了权限管理的apk，大多数root 权限 申请需要app配合，也有不需要的，这个需要改su源码。
            // 因为管理su权限的app太多，无法列举所有的app，特别是国外的，暂时不做判断是否有root权限管理app
            //多数只要su可执行就是root成功了，但是成功后用户如果删掉了权限管理的app，就会造成第三方app无法申请root权限，此时是用户删root权限管理app造成的。
            //市场上常用的的权限管理app的包名   com.qihoo.permmgr  com.noshufou.android.su  eu.chainfire.supersu   com.kingroot.kinguser  com.kingouser.com
            // com.koushikdutta.superuser
            //com.dianxinos.superuser  com.lbe.security.shuame com.geohot.towelroot 。。。。。。
            return true;
        } else {
            return false;
        }
    }

    private static boolean isRootSystem1() {
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists() && f.canExecute()) {
                    Log.e("root文件夹", f.getAbsolutePath());
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static boolean isRootSystem2() {
        List<String> pros = getPath();
        File f = null;
        try {
            for (int i = 0; i < pros.size(); i++) {
                f = new File(pros.get(i), "su");
                System.out.println("f.getAbsolutePath():" + f.getAbsolutePath());
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static List<String> getPath() {
        return Arrays.asList(System.getenv("PATH").split(":"));
    }


    /**
     * 请求root权限
     *
     * @return
     */
    public static synchronized boolean checkGetRootAuth() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //判断我们的应用是否在白名单中
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = true;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }

        return isIgnoring;
    }

    //申请加入白名单
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestIgnoreBatteryOptimizations(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readTextFromAsset(String assetName, Context context) {
        try {
            InputStream is = context.getAssets().open(assetName);  //获得AssetManger 对象, 调用其open 方法取得  对应的inputStream对象
            int size = is.available();//取得数据流的数据大小
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (Exception e) {
        }
        return null;
    }

    public static String readTextFromRaw(int rawId, Context context) {
        try {
            InputStream is = context.getResources().openRawResource(rawId);
            int size = is.available();//取得数据流的数据大小
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (Exception e) {
        }
        return null;
    }
}
