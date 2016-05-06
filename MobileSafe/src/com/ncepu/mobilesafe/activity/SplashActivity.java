package com.ncepu.mobilesafe.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;
import org.json.JSONException;
import org.json.JSONObject;

import com.ncepu.mobilesafe.R;
import com.ncepu.mobilesafe.R.layout;
import com.ncepu.mobilesafe.utils.StreamUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_ENTER_HOME = 1;
	protected static final int CODE_URL_ERROR = 2;
	protected static final int CODE_NET_ERROR = 3;
	protected static final int CODE_JSON_ERROR = 4 ;
	HttpURLConnection conn;
	TextView tvVersion;
	String urlString = "http://10.0.2.2:8080/update.json";
	
	private String mVersionName;//服务器端版本名称
	private int mVersionCode;//服务器端版本号
	private String mDesc;//服务器端版本描述
	private String mDownloadUrl;//服务器端版本下载地址
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				//showUpdateDailog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT)
				.show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT)
				.show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析错误",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			default:
				break;
			}
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本名：" + getVersionName());
        checkVersion();
    }
    /**
     * 获取版本名字
     * @return
     */
    private String getVersionName() {
    	PackageManager packageManager = getPackageManager();
    	try {
			PackageInfo paInfo = packageManager.getPackageInfo(getPackageName(), 0);//获取包的信息
			//int versionCode  = paInfo.versionCode;
			String versionName = paInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			// 找不到包名
			e.printStackTrace();
		}
    	
		return "";
	}
    
    /*
     * 获取本地app版本号
     */
    private int getVersionCode() {
    	PackageManager packageManager = getPackageManager();
    	try {
			PackageInfo paInfo = packageManager.getPackageInfo(getPackageName(), 0);//获取包的信息
			int versionCode  = paInfo.versionCode;
			//String versionName = paInfo.versionName;
			return versionCode;
		} catch (NameNotFoundException e) {
			// 找不到包名
			e.printStackTrace();
		}
    	
		return -1;
	}
    /**
     * 校验检查版本信息
     */
    private void checkVersion() {
		final long startTime = System.currentTimeMillis();
		new Thread (){
			public void run() {
				Message msg = Message.obtain();
				try {
					URL url = new URL(urlString);
					conn = (HttpURLConnection) url.openConnection();
					
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(50000);
					conn.setReadTimeout(50000);
					conn.connect();
					
					int responsecode = conn.getResponseCode();
					if(responsecode == 200) {
						InputStream inputStream = conn.getInputStream();
						String readFrom = StreamUtils.readFromStrem(inputStream);//获取服务器端版本信息
						JSONObject jsonObject = new JSONObject(readFrom);
						mVersionName = jsonObject.getString("versionName");
						mVersionCode = jsonObject.getInt("versionCode");
						mDesc = jsonObject.getString("description");
						mDownloadUrl = jsonObject.getString("downloadUrl");
						//对比版本号
						if(mVersionCode > getVersionCode()){
							// 服务器的VersionCode大于本地的VersionCode
							// 说明有更新, 弹出升级对话框
							msg.what = CODE_UPDATE_DIALOG;
						}else{
							//版本没有更新
							msg.what = CODE_ENTER_HOME;
						}
					}
				}catch (MalformedURLException e) {
						// url错误
						e.printStackTrace();
						msg.what = CODE_URL_ERROR;
					}catch (IOException e) {
					// 网络错误。
						msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
						// jason解析失败
						msg.what = CODE_JSON_ERROR;
						e.printStackTrace();
					}finally {
						long endTime = System.currentTimeMillis();
						long timeUsed = endTime - startTime;// 访问网络花费的时间
						if (timeUsed < 2000) {
							// 强制休眠一段时间,保证闪屏页展示2秒钟
							try {
								Thread.sleep(2000 - timeUsed);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							mHandler.sendMessage(msg);
							if (conn != null) {
								conn.disconnect();// 关闭网络连接
							}
						}
					}
			}
			
		}.start();
	}
    /**
     * 进入主界面。
     */
    private void enterHome(){
    	Intent intent = new Intent(this,HomeActivity.class);
    	startActivity(intent);
    	finish();
    }
}
