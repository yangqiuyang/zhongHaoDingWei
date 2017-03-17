package org.zhonghao.gps.biz;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Tools {

	public static String getCurrentVersion(Context context) throws NameNotFoundException
	{
		//
		PackageManager packageManager=context.getPackageManager();
		String packageName=context.getPackageName();
		
		//
		PackageInfo packageInfo=packageManager.getPackageInfo(packageName, 0);
		return packageInfo.versionName;
	}

}
