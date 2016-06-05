package com.ncepu.mobilesafe.activity;

import com.ncepu.mobilesafe.R;

import android.content.Intent;
import android.os.Bundle;
/**
 * 第一个引导页面
 * @author BRUCE
 *
 */
public class Setup1Activity extends BaseSetupActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setup1_activity);
	}
	/**
	public void next(View v) {
		startActivity( new Intent(Setup1Activity.this,Setup2Activity.class));
		finish();
		
		//两个界面切换的动画,1.首先在anim文件中写进入和退出的xml文件，然后在此调用
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	**/
	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void showNextPage() {
		startActivity( new Intent(Setup1Activity.this,Setup2Activity.class));
		finish();
		//两个界面切换的动画,1.首先在anim文件中写进入和退出的xml文件，然后在此调用
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
}
