package com.heliang.fragmentMe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.heliang.fragmentHome.FragmentSelectCity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMePersonal extends Fragment {

	public GlobalVar appState;
	private RelativeLayout rl_me_personalxuexiao, rl_me_personalsuozaidi;
	private Button btn_update_person;
	private EditText et_personal_name, et_personal_suozaidi, et_personal_xuexiao;
	private FragmentSelectXuexiao  fragmentSelectXuexiao;
	private FragmentSelectCity fragmentSelectCity;
	
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
		
		View view = inflater.inflate(R.layout.fragment_me_personal, container, false);
		
		if (appState.denglu){
			
		}else{
			view = inflater.inflate(R.layout.fragment_me_login, container, false);
		}
		
		findView(view);	
		setOnClickListener();
		findFragment();
		
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
		Looper looper = Looper.myLooper();
		// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
		messageHandler = new MessageHandler(looper);
				
        return view;       
	}
	
	@Override
	public void onResume(){
		if (!"".equals(appState.city)){
			et_personal_suozaidi.setText(appState.city);
		}
		
		if (!"".equals(appState.xuexiao)){
			et_personal_xuexiao.setText(appState.xuexiao);
		}
		super.onResume();
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
		fragmentSelectXuexiao = new FragmentSelectXuexiao();
		fragmentSelectCity = new FragmentSelectCity();
	}

	public void findView(View view){
		btn_update_person = (Button) view.findViewById(R.id.btn_update_person);
		et_personal_name = (EditText) view.findViewById(R.id.et_personal_name);
		et_personal_suozaidi = (EditText) view.findViewById(R.id.et_personal_suozaidi);
		et_personal_xuexiao = (EditText) view.findViewById(R.id.et_personal_xuexiao);
		rl_me_personalsuozaidi = (RelativeLayout) view.findViewById(R.id.rl_me_personalsuozaidi);
		rl_me_personalxuexiao = (RelativeLayout) view.findViewById(R.id.rl_me_personalxuexiao);
		
		if (!"".equals(appState.city)){
			et_personal_suozaidi.setText(appState.city);
		}
		
		if (!"".equals(appState.xuexiao)){
			et_personal_xuexiao.setText(appState.xuexiao);
		}
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		btn_update_person.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "btn_update_person onClicked");
				if ("".equals(et_personal_name.getText().toString())){
					Toast toast = Toast.makeText(getActivity(), "请填写昵称", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{
					new personalThread().start();
				}
				
			}
		});
		
		rl_me_personalxuexiao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "rl_me_personalxuexiao onClicked");
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_me, fragmentSelectXuexiao).addToBackStack("initxuexiao").commit();
			}
		});
		
		rl_me_personalsuozaidi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "rl_me_personalsuozaidi onClicked");
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_me, fragmentSelectCity).addToBackStack("initcity").commit();
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
				if ("personal".equals((String) msg.obj)) {
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
	public class personalThread extends Thread {

		public personalThread() {

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
					updateHandler("personal");
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
		
		try {
			String servletUrl = getString(R.string.serverURL) + "/app/user-perfect.jspx?userId=";
			servletUrl += appState.userid + "&";
			servletUrl += "realname=" + URLEncoder.encode(et_personal_name.getText().toString(), "UTF-8") + "&";
			servletUrl += "schoolId=" + appState.xuexiaoid ;
			String line = "";
//			line = "{\"username\":\"admin\",\"status\":1,\"userId\":1}";
			
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
					
					SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
					Editor editor = sharedPreferences.edit();//获取编辑器
					editor.putString("username", appState.username);
					editor.putString("userid", appState.userid);
					editor.putString("nickname", appState.usernickname);
					editor.putString("xuexiao", appState.xuexiao);
					editor.putString("xuexiaoid", appState.xuexiaoid);
					editor.putString("cityName", appState.city);
					editor.putString("cityId", appState.cityid);
					editor.commit();//提交修改
					
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
}



