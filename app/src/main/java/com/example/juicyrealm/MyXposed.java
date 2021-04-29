package com.example.juicyrealm;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
/*
	注意！
	你需要关掉Instant Run才能在Android Studio里使用“运行App”，不然Xposed会出现找不到类的错误。
	Be Awared!
	You should disable Instant Run if you want to use 'Run App' from Android Studio, or Xposed Framework will not find module class from base.apk.
	https://developer.android.com/studio/run/#disable-ir
*/

public class MyXposed implements IXposedHookLoadPackage {

    String targetApp = "net.spacecan.juicyrealm.android.TapTap";
    String packageName;
    Boolean isFirstApplication;
    ClassLoader classLoader;
    String processName;
    ApplicationInfo appInfo;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals(targetApp)) return;
        gatherInfo(loadPackageParam);
        //Write your code here.

        XposedHelpers.findAndHookMethod("com.taptap.pay.sdk.library.TapAlertDialog", loadPackageParam.classLoader, "show", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("TapAlertDialog.show():Hook完成");
                return null;
            }
        });

        XposedHelpers.findAndHookMethod("com.taptap.pay.sdk.library.IabService", loadPackageParam.classLoader, "checkLicense", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
                XposedBridge.log("checkLicense:Hook完成");
            }
        });

        XposedHelpers.findAndHookMethod("com.taptap.pay.sdk.library.Settings", loadPackageParam.classLoader, "getLicensed", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
                XposedBridge.log("getLicensed:Hook完成");
            }
        });


    }

    private void gatherInfo(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        packageName = loadPackageParam.packageName;
        isFirstApplication = loadPackageParam.isFirstApplication;
        classLoader = loadPackageParam.classLoader;
        processName = loadPackageParam.processName;
        appInfo = loadPackageParam.appInfo;
    }
}
