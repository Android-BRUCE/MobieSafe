package com.ncepu.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;
	/**
	 * 设置引导页面
	 * @author BRUCE
	 *手机滑动会调用onTouchEvent，可以在GestureDetector实现
	 */
public abstract class BaseSetupActivity extends Activity {
	GestureDetector getGestureDetector;
	SharedPreferences mPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		getGestureDetector = new GestureDetector(this,new SimpleOnGestureListener() {
			@Override
			/**
			 * 监听手势滑动事件 e1表示滑动的起点,e2表示滑动终点 velocityX表示水平速度 velocityY表示垂直速度
			 */
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {

				//判断向左还是右滑动
				if(e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
				}
				if(e1.getRawX() - e2.getRawX() > 200) {
					showNextPage();
				}
				//判断滑动的速度
				if(Math.abs(velocityX) < 150 ) {
					Toast.makeText(BaseSetupActivity.this, "滑动太慢了！", Toast.LENGTH_SHORT).show();
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	//在setupactivity中必须实现
	public abstract void showPreviousPage();
	public abstract void showNextPage();
	
	//activity中的按钮
	public void next(View v) {
		showNextPage();
	}
	public void previous(View v) {
		showPreviousPage();
	}
	/**
	 * 手指滑动调用
	 */
	@Override
		public boolean onTouchEvent(MotionEvent event) {
		getGestureDetector.onTouchEvent(event);// 委托手势识别器处理触摸事件
			return super.onTouchEvent(event);
		}
}
