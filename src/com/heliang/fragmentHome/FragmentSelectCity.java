package com.heliang.fragmentHome;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentSelectCity extends Fragment {

	public GlobalVar appState;
	private TextView tv_curcity1;
	private ImageView iv_selectcity_back;
	private String jibie = "shen";	//shenʡ shi��
	
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
		
		View view = inflater.inflate(R.layout.fragment_selectcity, container, false);
		findView(view);	
		setOnClickListener();
		findFragment();
//		updateUI();
		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�
		Looper looper = Looper.myLooper();
		// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper
		messageHandler = new MessageHandler(looper);

		new getCityThread().start();

        return view;       
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
	}

	public void findView(View view){
		lv_city = (ListView) view.findViewById(R.id.lv_city);
		iv_selectcity_back	 = (ImageView) view.findViewById(R.id.iv_selectcity_back); 	
		tv_curcity1 = (TextView)view.findViewById(R.id.tv_curcity1); 	
	}
	
	private void setOnClickListener() {
		// TODO Auto-generated method stub
		iv_selectcity_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_selectcity_back onClicked");
				getFragmentManager().popBackStack();
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
				if ("getcity".equals((String) msg.obj)) {
					updateUI();
				}else if ("getcity_default".equals((String) msg.obj)) {
					updateUI_defaultcity();
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
	// �����б�  ʡ��----------------------------------------
	public class getCityThread extends Thread {

		public getCityThread() {

		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
					jsonarray = new JSONArray(line.toString());
//					jsonobj = new JSONObject(line.toString());
					updateHandler("getcity");
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
		String servletUrl = getString(R.string.serverURL) + "/app/job-city.jspx";
		String line = "";
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
	
	private JSONObject jsonobj1;
	private JSONArray jsonarray1; 
	// �����б�  �м�----------------------------------------
	public class getCity1Thread extends Thread {

		public getCity1Thread() {

		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData1();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
					jsonarray = new JSONArray(line.toString());
//					jsonobj = new JSONObject(line.toString());
					updateHandler("getcity");
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
	private String getData1() {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/job-city.jspx?parentId=" + appState.shenid;
		String line = "";
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
	
	private JSONObject jsonobj3;
	private JSONArray jsonarray3; 
	// �����б�  Ĭ��----------------------------------------
	public class getCity3Thread extends Thread {

		public getCity3Thread() {

		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData3();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
					jsonarray3 = new JSONArray(line.toString());
//					jsonobj3 = new JSONObject(line.toString());
					updateHandler("getcity_default");
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
	private String getData3() {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/job-default-city.jspx";
		String line = "";
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
	private void updateUI_defaultcity() {
		// TODO Auto-generated method stub
		
		for(int i=0;i<jsonarray.length();i++){
			map = new HashMap<String, Object>();
			try {
				jsonobj = new JSONObject(jsonarray.getString(i));
				tv_curcity1.setText(jsonobj.get("cityName").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			lst.add(map);
		}

	}
	// ------------------------------------------------------------------
	
	public class ZuJian_lv_city {
		public RelativeLayout list_selectcity;
		public TextView tv_lvcity, tv_lvcityid;
	}

	private ArrayList<HashMap<String, Object>> lst;
	// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
	private MyListAdapter saImageItems;
	private ListView lv_city;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private void updateUI() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(getActivity(), lst);// ûʲô����
		
		for(int i=0;i<jsonarray.length();i++){
			map = new HashMap<String, Object>();
			try {
				jsonobj = new JSONObject(jsonarray.getString(i));
				map.put("cityName", jsonobj.get("cityName").toString());
				map.put("cityId", jsonobj.get("cityId").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			lst.add(map);
		}

		// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
		// MyListAdapter saImageItems = new MyListAdapter(this, lst);// ûʲô����

		// ������
		BinderListData(saImageItems);
	}

	// ������
	public void BinderListData(MyListAdapter saImageItems) {
		// ListView listView_cart = (ListView)
		// findViewById(R.id.listView_chakan);
		// ��Ӳ�����ʾ
		lv_city.setAdapter(saImageItems);
		saImageItems.notifyDataSetChanged();
		// ����ؼ�������
		lv_city.setOnItemClickListener(new ItemClickListener());
	}

	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int position,// The position of the view in the adapter
				long id// The row id of the item that was clicked
		) {
			Log.i("info", "click:" + String.valueOf(position));
			HashMap<String, Object> m = new HashMap<String, Object>();
			m = lst.get(position);
			
			if ("shen".equals(jibie)) {// ��ǰѡʡһ��
				appState.shen = m.get("cityName").toString();
				appState.shenid = m.get("cityId").toString();
				new getCity1Thread().start();
				jibie = "shi";
			}else if ("shi".equals(jibie)) {// ��ǰѡʡһ��
				appState.city = m.get("cityName").toString();
				appState.cityid = m.get("cityId").toString();
				jibie = "shen";
				
				SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE); //˽������
				Editor editor = sharedPreferences.edit();//��ȡ�༭��
				editor.putString("cityName", appState.city);
				editor.putString("cityId", appState.cityid);
				editor.commit();//�ύ�޸�
				
				getFragmentManager().popBackStack();
			}
			
		}
	}

	/*
	 * �������Զ����BaseAdapter��
	 */
	public class MyListAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> data;
		private LayoutInflater layoutInflater;
		private Context context;

		public MyListAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			this.context = context;
			this.data = data;
			this.layoutInflater = LayoutInflater.from(context);
		}

		/**
		 * ��ȡ����
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		/**
		 * ��ȡĳһλ�õ�����
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		/**
		 * ��ȡΨһ��ʶ
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/**
		 * android����ÿһ�е�ʱ�򣬶�������������
		 */
		ZuJian_lv_city zuJian = null;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				zuJian = new ZuJian_lv_city();
				// ��ȡ�������
				convertView = layoutInflater.inflate(R.layout.lv_selectcity, null);

				 zuJian.list_selectcity = (RelativeLayout) convertView.findViewById(R.id.list_selectcity);
				 zuJian.tv_lvcity = (TextView) convertView.findViewById(R.id.tv_lvcity);
				 zuJian.tv_lvcityid = (TextView) convertView.findViewById(R.id.tv_lvcityid);

				// ����Ҫע�⣬��ʹ�õ�tag���洢���ݵġ�
				convertView.setTag(zuJian);
			} else {
				zuJian = (ZuJian_lv_city) convertView.getTag();

			}

			// �����ݡ��Լ��¼�����
			 zuJian.tv_lvcity.setText((String) data.get(position).get("cityName"));
			 zuJian.tv_lvcityid.setText((String) data.get(position).get("cityId"));
			 

			// zuJian.btn_datanote.setOnClickListener(new
			// Button.OnClickListener(){//��������
			// public void onClick(View v) {
			// //����дnote
			// Intent intent = new Intent();
			// intent.setClass(getActivity(), Note.class);
			//
			// Bundle bundle = new Bundle();
			// bundle.putString("uid",(String) data.get(position).get("uid"));
			// bundle.putString("date",(String) data.get(position).get("date"));
			// bundle.putString("note",(String) data.get(position).get("note"));
			// intent.putExtras(bundle);
			//
			// startActivityForResult(intent, 1);//
			// ��Ҫ��һ��Activity��������,��onActivityResult()�н���
			// }
			// });

			return convertView;
		}
	}
}



