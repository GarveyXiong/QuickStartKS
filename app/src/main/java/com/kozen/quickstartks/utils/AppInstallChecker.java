package com.kozen.quickstartks.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

public class AppInstallChecker {

    /**
     * 判断指定包名的APK是否已安装（基础版：仅判断存在性）
     * @param context 上下文（建议使用Application Context，避免内存泄漏）
     * @param packageName 要判断的应用包名（如：com.tencent.mm）
     * @return true=已安装，false=未安装/包名为空/异常
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        // 空值校验：包名不能为空，上下文不能为空
        if (context == null || TextUtils.isEmpty(packageName)) {
            return false;
        }

        PackageManager packageManager = context.getPackageManager();
        try {
            // 核心API：查询包名对应的应用信息，参数1=包名，参数2=查询标记（0=基础信息）
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            // 能获取到PackageInfo说明应用已安装
            return packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            // 捕获"包名未找到"异常，说明应用未安装
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // 捕获其他异常（如权限问题、系统异常），默认返回未安装
            e.printStackTrace();
            return false;
        }
    }
}
