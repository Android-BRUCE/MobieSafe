package com.ncepu.mobilesafe.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncepu.mobilesafe.R;
import com.ncepu.mobilesafe.adapter.MyBaseAdapter;
import com.ncepu.mobilesafe.bean.BlackNumberInfo;
import com.ncepu.mobilesafe.db.dao.BlackNumberDao;

/**
 * 分页加载数据
 * 黑名单数据显示
 * @author BRUCE
 *
 */
public class CallSafeActivity extends Activity {
	private CallSafeAdapter adapter;
	private int totalPage;
	private BlackNumberDao dao;
    private ListView list_view;
    private LinearLayout ll_pb;
	private List<BlackNumberInfo> blackNumberInfo;
	/**
	 * 当前页面
	 */
	private int mCurrentPageNumber = 1;
	/**
	 * 每页展示的数据
	 */
	private int mPageSize = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_safe_activity);
        initUI();//初始化UI拿到list_view
        initData();
    }
    private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
    		ll_pb.setVisibility(View.INVISIBLE);
			tv_pageNow.setText(mCurrentPageNumber + "/" + totalPage );
    		adapter = new CallSafeAdapter(blackNumberInfo, CallSafeActivity.this);
    		list_view.setAdapter(adapter);//设置适配器
    	}
    };
	private TextView tv_pageNow;
	private EditText jumpPage;
	private ImageView iv_delete;
    /**
     * 初始化数据
     */
    private void initData() {
    	
    	new Thread(){
			public void run() {
    	    	dao = new BlackNumberDao(CallSafeActivity.this);
    	    	blackNumberInfo = dao.findPar(mCurrentPageNumber, mPageSize);
    	    	totalPage = (int)Math.ceil(dao.getAllNumber() * 1.0 / mPageSize);
    	    	//totalPage = dao.getAllNumber() / mPageSize;//一共多少页
    	    	handler.sendEmptyMessage(0);
    		};
    	}.start();

//		CallSafeAdapter adapter = new CallSafeAdapter(blackNumberInfo, this);
//		list_view.setAdapter(adapter);//设置适配器
	}
    /**
     * 初始化界面
     */
	private void initUI() {
		ll_pb = (LinearLayout) findViewById(R.id.ll_pb);
		ll_pb.setVisibility(View.VISIBLE);//数据加载完之前 显示进度条。
        list_view = (ListView) findViewById(R.id.lv_list);
        tv_pageNow = (TextView) findViewById(R.id.tv_pageNow);//当前页/总共
        jumpPage = (EditText) findViewById(R.id.et_pageWant);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
    }
	
	private class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo>{
		private CallSafeAdapter(List<BlackNumberInfo> lists,Context context){
			super(lists,context);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if(convertView == null) {
				convertView = View.inflate(CallSafeActivity.this, R.layout.item_call_safe, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
				holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
				holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_number.setText(lists.get(position).getNumber());
			String mode = lists.get(position).getMode();
			if(mode.equals("1")){
				holder.tv_mode.setText("电话 + 短信拦截");
			}else if(mode.equals("2")) {
				holder.tv_mode.setText("电话拦截");
			}else if(mode.equals("3")) {
				holder.tv_mode.setText("短信拦截");
			}
			final BlackNumberInfo info = lists.get(position);//获取当前item对象。
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String number = info.getNumber();
					boolean result = dao.delete(number);
					if(result) {
						Toast.makeText(CallSafeActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
						lists.remove(info);
						adapter.notifyDataSetChanged();//刷新页面,但是不会刷新整体的布局
						//initData();//重新刷新绘制数据的布局
						//如何在用户不断删除黑名单，当前页大小大于总页数？math.ceil();
					}
				}
			});
			return convertView;
			}
	}
	
	static class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
	/**
	 * 上一页
	 * @param v
	 */
	public void prePage(View v) {
		if(mCurrentPageNumber <= 1) {
			Toast.makeText(CallSafeActivity.this, "已经是第一页了", Toast.LENGTH_SHORT).show();
			return ;
		}
		mCurrentPageNumber--;
		initData();
	}
	/**
	 * 下一页
	 * @param v
	 */
	public void nextPage(View v) {
		if(mCurrentPageNumber >= totalPage) {
			Toast.makeText(CallSafeActivity.this, "已经是左后一页了", Toast.LENGTH_SHORT).show();
			return ;
		}
		mCurrentPageNumber++;
		initData();
	}
	/**
	 * 页面跳转
	 * @param v
	 */
	public void jump(View v) {
		String jump_number = jumpPage.getText().toString().trim();
		if(TextUtils.isEmpty(jump_number)) {
			Toast.makeText(this, "请输入正确的页码", Toast.LENGTH_SHORT);
		}else {
			int number = Integer.parseInt(jump_number);
			if (number >= 1 && number <= totalPage) {
				mCurrentPageNumber = number;
				initData();	
			}else {
				Toast.makeText(this, "请输入正确的页码", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
