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
		
		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�
		Looper looper = Looper.myLooper();
		// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper
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
					Toast toast = Toast.makeText(getActivity(), "����д�ǳ�", Toast.LENGTH_SHORT);
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
		// ����һ��Message���󣬲��ѵõ���������Ϣ��ֵ��Message����
		Message message = Message.obtain();// ��һ��
		message = Message.obtain();// ��һ��
		message.obj = obj; // �ڶ���
		messageHandler.sendMessage(message);// ������
	}
	
	// ���໯һ��Handler
	class MessageHandler extends Handler {
		public MessageHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// ����UI
			if (!((String) msg.obj == null)) {
				if ("personal".equals((String) msg.obj)) {
					updateUI();
				} else if ("timeout".equals((String) msg.obj)) {
					Toast toast = Toast.makeText(getActivity(), "�������ӳ�ʱ", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}

		}
	}
	
	private JSONObject jsonobj;
	private JSONArray jsonarray; 
	// ��¼----------------------------------------
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
	 * @param fieldid �豸
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
				// �������
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
//				Toast toast = Toast.makeText(getApplicationContext(),"����ʱ��������ܾ�", Toast.LENGTH_LONG);
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
					
					SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE); //˽������
					Editor editor = sharedPreferences.edit();//��ȡ�༭��
					editor.putString("username", appState.username);
					editor.putString("userid", appState.userid);
					editor.putString("nickname", appState.usernickname);
					editor.putString("xuexiao", appState.xuexiao);
					editor.putString("xuexiaoid", appState.xuexiaoid);
					editor.putString("cityName", appState.city);
					editor.putString("cityId", appState.cityid);
					editor.commit();//�ύ�޸�
					
					Intent rtnIntent = new Intent();
					rtnIntent.putExtra("denglu", "true");
					getActivity().setResult(1,rtnIntent);
					getActivity().finish();
				}else if ("-1".equals(t)){
					appState.denglu = false;
					Toast toast = Toast.makeText(getActivity(), "�ʺŽ���", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{
					appState.denglu = false;
					Toast toast = Toast.makeText(getActivity(), "�û������������", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}



