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
import android.widget.Toast;

public class FragmentMeZhuce extends Fragment {

	public GlobalVar appState;
	private FragmentMePersonal fragmentMePersonal;
	private ImageView iv_me_registerback, iv_me_regyanzhenma;
	private EditText et_reg_username, et_me_regyanzhenma, et_reg_password, et_reg_password_again;
	private Button btn_me_regsubmit;
	
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
		
		View view = inflater.inflate(R.layout.fragment_me_register, container, false);
		
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
		fragmentMePersonal = new FragmentMePersonal();
	}

	public void findView(View view){
		iv_me_registerback = (ImageView) view.findViewById(R.id.iv_me_registerback);
		iv_me_regyanzhenma = (ImageView) view.findViewById(R.id.iv_me_regyanzhenma);
		btn_me_regsubmit = (Button) view.findViewById(R.id.btn_me_regsubmit);
		et_reg_username = (EditText) view.findViewById(R.id.et_reg_username);
		et_me_regyanzhenma = (EditText) view.findViewById(R.id.et_me_regyanzhenma);
		et_reg_password = (EditText) view.findViewById(R.id.et_reg_password); 
		et_reg_password_again = (EditText) view.findViewById(R.id.et_reg_password_again);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		iv_me_registerback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_me_registerback onClicked");
				getFragmentManager().popBackStack();
			}
		});
		
		iv_me_regyanzhenma.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_me_regyanzhenma onClicked");
				buzhou = 1;//������֤�ֻ���
				new getYanzhenmaThread().start();
				new getYanzhen2Thread().start();
			}
		});
		
		btn_me_regsubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "btn_me_regsubmit onClicked");
				String t1, t2;
				t1 = et_reg_password.getText().toString();
				t2 = et_reg_password_again.getText().toString();
				if (t1.equals(t2)){
					new getZhuceThread().start();
				}else {
					Toast toast = Toast.makeText(getActivity(), "�����������벻һ�£��������������", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					et_reg_password.setText("");
					et_reg_password_again.setText("");
				}
				
			}
		});
	}
	
	private int buzhou=0;
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
				if ("zhuce".equals((String) msg.obj)) {
					updateUI_zhuce();
				}else if ("yanzhen".equals((String) msg.obj)) {
					updateUI_yanzhen();
				}else if ("yanzhen2".equals((String) msg.obj)) {
					updateUI_yanzhen2();
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
	// ע��----------------------------------------
	public class getZhuceThread extends Thread {

		public getZhuceThread() {

		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData_zhuce();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
//					{"username":"admin","status":1,"userId":1
//					jsonarray = new JSONArray(line.toString());
					jsonobj = new JSONObject(line.toString());
					updateHandler("zhuce");
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
	private String getData_zhuce() {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/user-register.jspx?username=" + et_reg_username.getText().toString();
		servletUrl += "&code=" + et_me_regyanzhenma.getText().toString();//��֤��
		servletUrl += "&password=" + et_reg_password.getText().toString();//����
		String line = "";
//		line = "{\"username\":\"admin\",\"status\":1,\"userId\":1}";
		try {
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
	
	// ��֤�ֻ���--------------------------------------
		public class getYanzhenmaThread extends Thread {

			public getYanzhenmaThread() {

			}

			@Override
			public void run() {
				appState.runThread = true;
				String line;
				line = getYanzhen();
				try {
					if (!"".equals(line) && !"null".equals(line) && null != line){
//						{"username":"admin","status":1,"userId":1
//						jsonarray = new JSONArray(line.toString());
						jsonobj = new JSONObject(line.toString());
						updateHandler("yanzhen");
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
		private String getYanzhen() {//�ж��ֻ����Ƿ��Ѿ���ע��
			// TODO Auto-generated method stub
			String servletUrl = getString(R.string.serverURL) + "/app/username_unique.jspx?username=";
			servletUrl += et_reg_username.getText().toString();
			String line = "";
//			line = "{\"username\":\"admin\",\"status\":1,\"userId\":1}";
			try {
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
//					Toast toast = Toast.makeText(getApplicationContext(),"����ʱ��������ܾ�", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		//�Ѿ���֤�����ֻ��ţ�������֤��------------------------------------------------------------------
		public class getYanzhen2Thread extends Thread {

			public getYanzhen2Thread() {

			}

			@Override
			public void run() {
				appState.runThread = true;
				while( buzhou == 1){//�������֤�ֻ�����׶Σ�ѭ���ȴ�
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String line;
				line = getData_yanzhen2();
				try {
					if (!"".equals(line) && !"null".equals(line) && null != line){
//						{"username":"admin","status":1,"userId":1
//						jsonarray = new JSONArray(line.toString());
						jsonobj = new JSONObject(line.toString());
						updateHandler("yanzhen2");
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
		private String getData_yanzhen2() {
			// TODO Auto-generated method stub
			String servletUrl = getString(R.string.serverURL) + "/app/captcha.jspx?username=";
			servletUrl += et_reg_username.getText().toString();
			String line = "";
//			line = "{\"username\":\"admin\",\"status\":1,\"userId\":1}";
			try {
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
//					Toast toast = Toast.makeText(getApplicationContext(),"����ʱ��������ܾ�", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		// ------------------------------------------------------------------
		
		private void updateUI_zhuce() {
			// TODO Auto-generated method stub
				try {
					String t = jsonobj.get("status").toString();
					if ("1".equals(t)){
						Toast toast = Toast.makeText(getActivity(), "ע��ɹ�", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						appState.denglu = true;
						appState.username = et_reg_username.getText().toString();
						appState.userid = jsonobj.get("userId").toString();
						getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_me, fragmentMePersonal).commit();
					}else{
						appState.denglu = false;
						Toast toast = Toast.makeText(getActivity(), "ע��ʧ��", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
	private void updateUI_yanzhen() {
		// TODO Auto-generated method stub
			try {
				String t = jsonobj.get("status").toString();
				if ("1".equals(t)){
					Toast toast = Toast.makeText(getActivity(), "�ֻ�����֤�ɹ��������Լ���ע��", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					et_reg_password.setEnabled(true);
					et_reg_password_again.setEnabled(true);
					btn_me_regsubmit.setEnabled(true);
					//������֤��
					buzhou = 2;//������֤��׶�
				}else{
					appState.denglu = false;
					Toast toast = Toast.makeText(getActivity(), "�ʺ��ѱ�ע��", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	private void updateUI_yanzhen2() {
		// TODO Auto-generated method stub
			try {
				String t = jsonobj.get("status").toString();
				if ("1".equals(t)){
//					Toast toast = Toast.makeText(getActivity(), "�ֻ�����֤�ɹ��������Լ���ע��", Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
//					et_reg_password.setEnabled(true);
//					et_reg_password_again.setEnabled(true);
//					btn_me_regsubmit.setEnabled(true);
					et_me_regyanzhenma.setText("999999");
					buzhou = 3;//ע��׶�
				}else{
					appState.denglu = false;
					Toast toast = Toast.makeText(getActivity(), "��ȡ��֤��ʧ��", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}



