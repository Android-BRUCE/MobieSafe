package com.ncepu.mobilesafe.receiver;

import com.ncepu.mobilesafe.db.dao.AddressDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 监听去电的广播接受者 需要权限: android.permission.PROCESS_OUTGOING_CALLS
 * @author BRUCE
 *已经在queryaddresssservice中动态注册了
 */
public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String number = getResultData();
		String queryResult = AddressDao.getAddress(number);
		
	}

}
