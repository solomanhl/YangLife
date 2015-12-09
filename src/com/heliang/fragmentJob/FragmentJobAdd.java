package com.heliang.fragmentJob;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.heliang.yanglife.GlobalVar;
import com.heliang.yanglife.R;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentJobAdd extends Fragment {

	public GlobalVar appState;
	private TextView tv_jobsend;
	private EditText et_jobtitle, et_jobpay, et_detail;
	private RadioGroup rg_job;
	private RadioButton jb_radio0, jb_radio1;
	private String type = "0";//1��Ӧ 0����
	
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
		
		View view = inflater.inflate(R.layout.fragment_job_add, container, false);
		
		
		findView(view);	
		setOnClickListener();
		findFragment();
		
		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�
		Looper looper = Looper.myLooper();
		// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper
		messageHandler = new MessageHandler(looper);
				
        return view;       
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
	}

	public void findView(View view){
		tv_jobsend = (TextView) view.findViewById(R.id.tv_jobsend);
		rg_job = (RadioGroup) view.findViewById(R.id.rg_job);
		jb_radio0 = (RadioButton) view.findViewById(R.id.jb_radio0);
		jb_radio1 = (RadioButton) view.findViewById(R.id.jb_radio1);
		et_jobtitle = (EditText) view.findViewById(R.id.et_jobtitle);
		et_jobpay = (EditText) view.findViewById(R.id.et_jobpay);
		et_detail = (EditText) view.findViewById(R.id.et_detail);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		tv_jobsend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "tv_jobsend onClicked");
				if ("".equals(et_jobtitle.getText().toString()) ||
						"".equals(et_jobpay.getText().toString()) ||
						"".equals(et_detail.getText().toString()) ){
					Toast toast = Toast.makeText(getActivity(), "����д������⡢�����ֵ����������", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{
					new getLoginThread().start();
				}
				
			}
		});		
		
		rg_job.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.jb_radio0) {
					type = "0";//����
				}else if (checkedId == R.id.jb_radio1) {
					type = "1";//��Ӧ
				}
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
				if ("addjob".equals((String) msg.obj)) {
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
					updateHandler("addjob");
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
			String servletUrl = getString(R.string.serverURL) + "/app/job-by-personal_release.jspx?userId=" + appState.userid;
			servletUrl += "&schoolId=" + appState.xuexiaoid;
			servletUrl += "&type=" + type;
			servletUrl += "&title=" + URLEncoder.encode(et_jobtitle.getText().toString(), "UTF-8");
			servletUrl += "&txt=" + URLEncoder.encode(et_detail.getText().toString(), "UTF-8") ;
			servletUrl += "&pay=" + et_jobpay.getText().toString();
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
					Toast toast = Toast.makeText(getActivity(), "���ͳɹ�", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					getFragmentManager().popBackStack();
				}else if ("0".equals(t)){
					Toast toast = Toast.makeText(getActivity(), "����ʧ��", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{
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



