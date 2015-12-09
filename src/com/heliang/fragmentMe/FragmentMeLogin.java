package com.heliang.fragmentMe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.heliang.yanglife.GlobalVar;
import com.heliang.yanglife.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMeLogin extends Fragment {

	public GlobalVar appState;
	private FragmentMeZhaohui fragmentMeZhaohui;
	private FragmentMeZhuce fragmentMeZhuce;
	private TextView tv_forget, tv_zhuce;
	private Button btn_login;
	private ImageView iv_me_loginback;
	private EditText ed_username, ed_password;
	
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
		
		View view = inflater.inflate(R.layout.fragment_me_login, container, false);
		
//		if (appState.denglu){
//			
//		}else{
//			view = inflater.inflate(R.layout.fragment_me_login, container, false);
//		}
		
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
		fragmentMeZhaohui = new FragmentMeZhaohui();
		fragmentMeZhuce = new FragmentMeZhuce();
	}

	public void findView(View view){
		tv_forget = (TextView) view.findViewById(R.id.tv_forget);
		tv_zhuce = (TextView) view.findViewById(R.id.tv_zhuce);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		iv_me_loginback = (ImageView) view.findViewById(R.id.iv_me_loginback);
		ed_username = (EditText) view.findViewById(R.id.ed_username); 
		ed_password = (EditText) view.findViewById(R.id.ed_password); 
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		tv_forget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "tv_forget onClicked");
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_me, fragmentMeZhaohui).addToBackStack("zhaohui").commit();
			}
		});
		
		tv_zhuce.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "tv_zhuce onClicked");
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_me, fragmentMeZhuce).addToBackStack("zhuce").commit();
			}
		});
		
		btn_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "btn_login onClicked");
				new getLoginThread().start();
			}
		});
		
		iv_me_loginback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_me_loginback onClicked");
				getActivity().finish();
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
				if ("login".equals((String) msg.obj)) {
					updateUI();
				} else if ("timeout".equals((String) msg.obj)) {
					Toast toast = Toast.makeText(getActivity(), "网络连接超时", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}

		}
	}
	
	private JSONObject jsonobj;
	private JSONArray jsonarray; 
	// 登录----------------------------------------
	public class getLoginThread extends Thread {

		public getLoginThread() {

		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
//					{"username":"admin","status":1,"userId":1
//					jsonarray = new JSONArray(line.toString());
					jsonobj = new JSONObject(line.toString());
					updateHandler("login");
				}else{
					updateHandler("timeout");
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
	private String getData() {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/user-login.jspx?username=";
		servletUrl += ed_username.getText().toString();
		servletUrl += "&password=";
		servletUrl += ed_password.getText().toString();
		String line = "";
//		line = "{\"username\":\"admin\",\"status\":1,\"userId\":1}";
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
//				Toast toast = Toast.makeText(getApplicationContext(),"请求超时或服务器拒绝", Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	// ------------------------------------------------------------------
	private void updateUI() {
		// TODO Auto-generated method stub
			try {
				String t = jsonobj.get("status").toString();
				if ("1".equals(t)){
					appState.denglu = true;
					appState.username = jsonobj.get("username").toString();
//					appState.usernickname = jsonobj.get("nickname").toString();
					appState.userid = jsonobj.get("userId").toString();
					appState.xuexiao = jsonobj.get("schoolName").toString();
					appState.xuexiaoid = jsonobj.get("schoolId").toString();
					saveUser(appState.username, appState.userid, appState.usernickname, appState.xuexiao, appState.xuexiaoid);//保存登录信息
					Intent rtnIntent = new Intent();
					rtnIntent.putExtra("denglu", "true");
					getActivity().setResult(1,rtnIntent);
					getActivity().finish();
				}else if ("-1".equals(t)){
					appState.denglu = false;
					Toast toast = Toast.makeText(getActivity(), "帐号禁用", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{
					appState.denglu = false;
					Toast toast = Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	private void saveUser(String username, String userid, String nickname, String xuexiao, String xuexiaoid) {//保存登录信息
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
		Editor editor = sharedPreferences.edit();//获取编辑器
		editor.putString("username", username);
		editor.putString("userid", userid);
		editor.putString("nickname", nickname);
		editor.putString("xuexiao", xuexiao);
		editor.putString("xuexiaoid", xuexiaoid);
		editor.commit();//提交修改
	}
}



