package com.ncepu.mobilesafe.activity;


import android.app.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncepu.mobilesafe.R;
import com.ncepu.mobilesafe.utils.MD5Utils;
/**
 * 主界面
 * @author BRUCE
 *
 */
public class HomeActivity extends Activity {
	
	GridView gridView;
	private SharedPreferences mPref;
	private int mpic[]  = new int[]{R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings}; 
	private String mname[ ] =  new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		gridView = (GridView) findViewById(R.id.gv_home);
		gridView.setAdapter(new HomeAdapter());
		/**
		 * 设置homeactivity的按下监听
		 */
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
					case 0:
						showPassWordDialog();
						break;
					case 1://通讯卫士
						startActivity( new Intent(HomeActivity.this, CallSafeActivity2.class) );
						break;
					case 2://软件管理
						startActivity( new Intent(HomeActivity.this, AppManagerAvtivity.class) );
						break;
					case 7:
						startActivity( new Intent(HomeActivity.this, AToolsActivity.class) );
						break;
				case 8:
					startActivity(new Intent(HomeActivity.this, SettingActivity.class));
					break;
				}
				
			}

		});
	}
	private void showPassWordDialog() {
		String m = mPref.getString("password", null);
		if (!TextUtils.isEmpty(m)) {
			// 输入密码弹窗
			showPasswordInputDialog();
		}else {
			// 如果没有设置过, 弹出设置密码的弹窗
			showPasswordSetDailog();
		}	
	}
	/**
	 * 弹出设置密码的弹窗
	 */
	private void showPasswordSetDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_set_password, null);
		dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题
		final EditText passwordfirst = (EditText) view.findViewById(R.id.et_password);
		final EditText passwordconfirm= (EditText) view.findViewById(R.id.et_password_confirm);
		
		Button btnok = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		
		btnok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String password = passwordfirst.getText().toString();
				String passwordcomfirm = passwordconfirm.getText().toString();
				
				if(!TextUtils.isEmpty(password) && !passwordcomfirm.isEmpty()){
					if(password.equals(passwordcomfirm)){
						//先保存密码以便再次登录使用。
						mPref.edit().putString("password", MD5Utils.encode(password)).commit();
						dialog.dismiss();
						
						//跳转到防盗页面
						startActivity(new Intent(HomeActivity.this
								,LostFindActivity.class));
					}else{
						Toast.makeText(HomeActivity.this, "两次密码不一致,请重新输入!",
								Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(HomeActivity.this, "输入框内容不能为空!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		//选择取消按钮。
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();			
			}
		});
		dialog.show();
	}
	/**
	 * 再次输入密码弹窗
	 */
	private void showPasswordInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_input_password,null);
		dialog.setView(view, 0, 0, 0, 0);
		Button btnCancelButton = (Button) view.findViewById(R.id.btn_cancel);
		Button btnOkButton = (Button) view.findViewById(R.id.btn_ok);
		final EditText password = (EditText) view.findViewById(R.id.et_password);
		
		
		//ok监听
		btnOkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String passwordString = password.getText().toString();
				if(!TextUtils.isEmpty(passwordString)){
					
					String savedPassword = mPref.getString("password", null);
					
				if (MD5Utils.encode(passwordString).equals(savedPassword)) {
					
					Toast.makeText(HomeActivity.this, "登陆成功！",
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					//跳转防盗页面
					startActivity(new Intent(HomeActivity.this
							,LostFindActivity.class));
				}else {
					Toast.makeText(HomeActivity.this, "密码输入错误!",
							Toast.LENGTH_SHORT).show();
					
				}
				}else {
					Toast.makeText(HomeActivity.this, "密码不能为空!",
							Toast.LENGTH_SHORT).show();
					
				}
			}
		});
		//选择取消按钮
		btnCancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		dialog.show();
	}
	class  HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mname.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mname[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = View.inflate(HomeActivity.this, R.layout.list_item, null);
			ImageView imageView = (ImageView) v.findViewById(R.id.iv_item);
			TextView textView = (TextView) v.findViewById(R.id.tv_item);
			
			imageView.setImageResource(mpic[position]);
			textView.setText(mname[position]);
			return v;
		}
	}
}
