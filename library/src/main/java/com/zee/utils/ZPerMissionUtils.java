package com.zee.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.zee.log.ZLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ZPerMissionUtils {
    private OnPerMissionResultListener mOnPerMissionResultListener;
    private Activity mActivity;
    private List<String> permissions = new ArrayList<>();


    public ZPerMissionUtils(Activity activity) {
        mActivity = activity;
    }

    public void setOnPerMissionResultListener(OnPerMissionResultListener onPerMissionResultListener) {
        mOnPerMissionResultListener = onPerMissionResultListener;
    }

    public void requestPermissions() {
        requestPermissions(permissions);
    }

    public void requestPermissions(String permission) {
        List<String> list = new ArrayList<>();
        list.add(permission);
        requestPermissions(list);
    }

    public void requestPermissions(List<String> permissions) {
        List<String> tempList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissions) {
                if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(mActivity, permission)) {
                    tempList.add(permission);
                }
            }
            if (tempList.size() > 0) {
                ActivityCompat.requestPermissions(mActivity, tempList.toArray(new String[0]), 0);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions != null && permissions.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (mOnPerMissionResultListener != null) {
                        mOnPerMissionResultListener.onPerMissionResult(permissions[i], true);
                    }
                } else {
                    if (mOnPerMissionResultListener != null) {
                        mOnPerMissionResultListener.onPerMissionResult(permissions[i], false);
                    }
                }
            }
        }
    }


    public void add(String permission) {
        permissions.add(permission);
    }

    public interface OnPerMissionResultListener {
        /**
         * 权限申请结果返回
         *
         * @param permission
         * @param isSuccess
         */
        void onPerMissionResult(String permission, boolean isSuccess);
    }

    public static boolean openSettingActivity() {
        boolean success = true;
        Intent intent = new Intent();
        Context loContext = UIUtils.getApplication();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String packageName = UIUtils.getApplication().getPackageName();

        OSUtils.ROM romType = OSUtils.getRomType();
        switch (romType) {
            case EMUI: // 华为
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
                break;
            case Flyme: // 魅族
                intent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("packageName", packageName);
                break;
            case MIUI: // 小米
                String rom = getMiuiVersion();
                if ("V6".equals(rom) || "V7".equals(rom)) {
                    intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", packageName);
                } else if ("V8".equals(rom) || "V9".equals(rom)) {
                    intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", packageName);
                } else {
                    intent = getAppDetailsSettingsIntent(packageName);
                }
                break;
            case Sony: // 索尼
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity"));
                break;
            case ColorOS: // OPPO
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.PermissionManagerActivity"));
                break;
            case EUI: // 乐视
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps"));
                break;
            case LG: // LG
                intent.setAction("android.intent.action.MAIN");
                intent.putExtra("packageName", packageName);
                ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
                intent.setComponent(comp);
                break;
            case SamSung: // 三星
            case SmartisanOS: // 锤子
                gotoAppDetailSetting(packageName);
                break;
            default:
                try {
                    intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", loContext.getPackageName(), null);
                    intent.setData(uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    loContext.startActivity(intent);
                } catch (Exception e1) {
                    intent.setAction(Settings.ACTION_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    loContext.startActivity(intent);
                }
                break;
        }
        return success;
    }

    /**
     * 获取 MIUI 版本号
     */
    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ZLog.i("MiuiVersion = " + line);
        return line;
    }

    /**
     * 获取跳转「应用详情」的意图
     *
     * @param packageName 应用包名
     * @return 意图
     */
    public static Intent getAppDetailsSettingsIntent(String packageName) {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:" + packageName))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 跳转:「应用详情」界面
     *
     * @param packageName 应用包名
     */
    public static void gotoAppDetailSetting(String packageName) {
        UIUtils.getApplication().startActivity(getAppDetailsSettingsIntent(packageName));
    }
}
