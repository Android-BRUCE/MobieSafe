package com.ncepu.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {
	public static boolean isServiceRunning (Context context,String name){
		
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> activityrunning = activityManager.getRunningServices(100);
		
		for (RunningServiceInfo runningServiceInfo : activityrunning) {
			String servicenameString = runningServiceInfo.service.getClassName();
			if(servicenameString.equals(name)) {
				return true;
			}
		}
		return false;
		
	}
}
