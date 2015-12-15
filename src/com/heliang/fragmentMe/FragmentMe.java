package com.heliang.fragmentMe;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.heliang.FileUtils.FileUtils;
import com.heliang.FileUtils.Net;
import com.heliang.fragmentJob.FragmentSearch;
import com.heliang.yanglife.GlobalVar;
import com.heliang.yanglife.MainActivity;
import com.heliang.yanglife.R;
import com.heliang.yanglife.WelcomeActivity;
import com.heliang.yanglife.WelcomeActivity.downLoadApkThread;
import com.heliang.yanglife.WelcomeActivity.getApkUrlThread;
import com.heliang.yanglife.WelcomeActivity.getVersionThread;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FragmentMe extends Fragment {

	public GlobalVar appState;
	private FragmentSearch fragmentSearch;
	private FragmentMeZhaohui fragmentMeZhaohui;
	private RelativeLayout rl_me_shenqin, rl_me_gaimaima, rl_me_genxin;
//	private Button btn_regster, btn_login;
	
	//public sportDataThread st = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		appState = (GlobalVar) getActivity().getApplicationContext();
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return inflater.inflate(R.layout.fragment_sleep, container, false);	
		
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		
		findView(view);	
		setOnClickListener();
		findFragment();
		
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
		Looper looper = Looper.myLooper();
		// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
		messageHandler = new MessageHandler(looper);
				
        return view;       
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
		fragmentSearch = new FragmentSearch();
		fragmentMeZhaohui = new FragmentMeZhaohui();
	}

	public void findView(View view){
		rl_me_shenqin = (RelativeLayout) view.findViewById(R.id.rl_me_shenqin);
		rl_me_gaimaima = (RelativeLayout) view.findViewById(R.id.rl_me_gaimaima);
		rl_me_genxin = (RelativeLayout) view.findViewById(R.id.rl_me_genxin);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		rl_me_shenqin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "rl_me_shenqin onClicked");
				Bundle bundle = new Bundle();  
	            bundle.putString("job_list_type", "me");  
	            fragmentSearch.setArguments(bundle);  
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_me, fragmentSearch).addToBackStack("myjob").commit();
			}
		});
		
		rl_me_gaimaima.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "rl_me_gaimaima onClicked");
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_me, fragmentMeZhaohui).addToBackStack("zhaohui").commit();
			}
		});
		
		rl_me_genxin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "rl_me_genxin onClicked");
				new getVersionThread().start();
			}
		});
		
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
				}else if ("getApkUrl".equals((String) msg.obj)) {
					downLoadApk();
				} else if ("timeout".equals((String) msg.obj)) {
					Toast toast = Toast.makeText(getActivity(), "网络连接超时", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else if ("wrongapk".equals((String) msg.obj)) {
					Toast toast = Toast.makeText(getActivity(), "安装包错误", Toast.LENGTH_SHORT);
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
				}else{//获取不到版本信息不管
					
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
				new AlertDialog.Builder(getActivity())
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
			Toast toast = Toast.makeText(getActivity().getApplicationContext(), "下载更新包出错",
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
	    
	    getActivity().finish();

		try {
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", e.toString());
			updateHandler("wrongapk");
			
		}
	}

	// ------------------------------------------------------------------
}



