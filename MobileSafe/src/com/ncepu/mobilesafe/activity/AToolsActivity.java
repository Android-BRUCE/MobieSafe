package com.ncepu.mobilesafe.activity;

import com.ncepu.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 高级工具页面
 * 
 * @author Kevin
 * 
 */
public class AToolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}

	/**
	 * 归属地查询
	 * 
	 * @param view
	 */
	public void numberAddressQuery(View view) {
		startActivity(new Intent(this, AddressActivity.class));
	}

}
