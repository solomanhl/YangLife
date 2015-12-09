package com.heliang.yanglife;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.heliang.FileUtils.FileUtils;
import com.heliang.FileUtils.Net;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class WelcomeActivity extends FragmentActivity {

	private GlobalVar appState;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		appState = (GlobalVar) getApplicationContext(); // 获得全局变量
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置成竖屏
		//去掉title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		findView();
		setOnClickListener();
		findFragment();
		
		getUser();
		
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
		Looper looper = Looper.myLooper();
		// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
		messageHandler = new MessageHandler(looper);

		new getVersionThread().start();
	}


	private void getUser() {
		// TODO Auto-generated method stub
		SharedPreferences share=getSharedPreferences("user", Context.MODE_PRIVATE);
		appState.username = share.getString("username","");
		appState.userid = share.getString("userid","");
		appState.usernickname = share.getString("nickname","");
		appState.xuexiao = share.getString("xuexiao","");
		appState.xuexiaoid = share.getString("xuexiaoid","");
		appState.city = share.getString("cityName","");
		appState.cityid = share.getString("cityId","");
		
		if (!"".equals(appState.userid)){
			appState.denglu = true;
		}
	}


	private void findFragment() {
		// TODO Auto-generated method stub
	}


	private void findView() {
		// TODO Auto-generated method stub
	}
	
	private void setOnClickListener() {
		// TODO Auto-generated method stub
		
	}
	
	private Handler messageHandler;
	private void updateHandler(Object obj) {
		// 创建一个Message对象，并把得到的网络信息赋值给Message对象
		Message message = Message.obtain();// 第一步
		message = Message.obtain();// 第一步
		message.obj = obj; // 第二步
		messageHandler.sendMessage(message);// 第三步
	}
	
	// 子类化一个Handler
	class MessageHandler extends Handler {
		public MessageHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// 更新UI
			if (!((String) msg.obj == null)) {
				if ("getVersion".equals((String) msg.obj)) {
					updateApk();
				}else if ("startMain".equals((String) msg.obj)) {
					new Handler().postDelayed(new Runnable() {
						public void run() {
							Intent it = new Intent(WelcomeActivity.this, MainActivity.class);
							startActivity(it);
							finish();
						}
					}, 1000);
				}else if ("getApkUrl".equals((String) msg.obj)) {
					downLoadApk();
				} else if ("timeout".equals((String) msg.obj)) {
					Toast toast = Toast.makeText(WelcomeActivity.this, "网络连接超时", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else if ("wrongapk".equals((String) msg.obj)) {
					Toast toast = Toast.makeText(WelcomeActivity.this, "安装包错误", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}

		}
	}
	
	private JSONObject jsonobj;
	private JSONArray jsonarray; 
	// 获取服务器最新的apk版本号----------------------------------------
	public class getVersionThread extends Thread {
		public getVersionThread() {
		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getVersion();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
//					jsonarray = new JSONArray(line.toString());
					jsonobj = new JSONObject(line.toString());
					updateHandler("getVersion");
				}else{//获取不到版本信息直接进入主界面
//					updateHandler("timeout");
					updateHandler("startMain");
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param fieldid 设备
	 * @return
	 */
	private String getVersion() {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/get-edition-version.jspx?version=" + appState.AppVersionName;
		String line = "";
		try {
			URL url = new URL(servletUrl);
			try {
				// 获得连接
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				conn.setReadTimeout(appState.SO_TIMEOUT);
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				line= in.readLine();
				in.close();
				conn.disconnect();
				return line;
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("info", e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private void updateApk(){
		try {
			String status = jsonobj.get("status").toString();
			if ("0".equals(status)){//有新版本
				//弹框问是否要升级
				new AlertDialog.Builder(this)
						.setTitle("更新")
						.setMessage("发现新版本，是否更新？")
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										updateHandler("startMain");
									}
								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										//获取最新apk地址
										new getApkUrlThread().start();
									}
								}).show();
				
			}else{//
				updateHandler("startMain");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	// ------------------------------------------------------------------
	
	// 获取最新apk的地址----------------------------------------
		public class getApkUrlThread extends Thread {
			public getApkUrlThread() {
			}

			@Override
			public void run() {
				appState.runThread = true;
				String line;
				line = getApkUrl();
				try {
					if (!"".equals(line) && !"null".equals(line) && null != line){
//						jsonarray = new JSONArray(line.toString());
						jsonobj = new JSONObject(line.toString());
						updateHandler("getApkUrl");
					}else{//下不到直接进主界面
						updateHandler("startMain");
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * @param fieldid 设备
		 * @return
		 */
		private String getApkUrl() {
			// TODO Auto-generated method stub
			String servletUrl = getString(R.string.serverURL) + "/app/get-edition-news.jspx" ;
			String line = "";
			try {
				URL url = new URL(servletUrl);
				try {
					// 获得连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(appState.REQUEST_TIMEOUT);
					conn.setReadTimeout(appState.SO_TIMEOUT);
					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					line= in.readLine();
					in.close();
					conn.disconnect();
					return line;
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("info", e.toString());
//					Toast toast = Toast.makeText(getApplicationContext(),"请求超时或服务器拒绝", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		private void downLoadApk(){
			try {
				String path = jsonobj.get("editionPath").toString();
				if (!"".equals(path)){
					//下载最新apk
					new downLoadApkThread(path).start();
				}else{
					updateHandler("startMain");
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// ------------------------------------------------------------------
		
		// 下载最新apk----------------------------------------
				public class downLoadApkThread extends Thread {
					String path;
					public downLoadApkThread(String path) {
						this.path = path;
					}

					@Override
					public void run() {
						appState.runThread = true;
						down(path);
					}
				}
				
	/**
	 * @param fieldid
	 *            设备
	 */
	private void down(String path) {
		// TODO Auto-generated method stub
		FileUtils file = new FileUtils();
		Net net = new Net();
//		String appPath = getApplicationContext().getPackageResourcePath() ;
		file.deleteFile(file.SDPATH + "YangLife.apk");
		int f =net.downFile(getString(R.string.serverURL) + path , file.SDPATH,  "YangLife.apk" );
		// -1出错 0正常 1已经存在
		if (f == 0) {

		} else if (f == -1) {
			Toast toast = Toast.makeText(getApplicationContext(), "下载更新包出错",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		updateHandler(null);

		// 打开
		Intent intent = new Intent("android.intent.action.VIEW");
	    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtra("oneshot", 0);
	    intent.putExtra("configchange", 0);
	    Uri uri = Uri.fromFile(new File(file.SDPATH + "YangLife.apk" ));
	    intent.setDataAndType(uri, "application/vnd.android.package-archive");
	    
	    this.finish();

		try {
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", e.toString());
			updateHandler("wrongapk");
			
		}
	}

	// ------------------------------------------------------------------

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//			return rootView;
//		}
//	}
}
