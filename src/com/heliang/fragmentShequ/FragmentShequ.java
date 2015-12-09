package com.heliang.fragmentShequ;


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

public class FragmentShequ extends Fragment {

	public GlobalVar appState;
	private RelativeLayout rl_shequ_fenlei;
	private ImageView iv_shequ_bankuai;
	private FragmentShequFayan fragmentShequFayan;
	
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
		
		View view = inflater.inflate(R.layout.fragment_shequ, container, false);
		findView(view);	
		setOnClickListener();
		findFragment();
//		updateUI();
		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�
		Looper looper = Looper.myLooper();
		// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper
		messageHandler = new MessageHandler(looper);

		new getCityThread().start();
		new getCityThread1().start();

        return view;       
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
		fragmentShequFayan = new FragmentShequFayan();
	}

	public void findView(View view){
		rl_shequ_fenlei = (RelativeLayout) view.findViewById(R.id.rl_shequ_fenlei);
		lv_shequ = (ListView) view.findViewById(R.id.lv_shequ);
		lv_bankuai = (ListView) view.findViewById(R.id.lv_bankuai);
		iv_shequ_bankuai = (ImageView) view.findViewById(R.id.iv_shequ_bankuai);
	}
	
	private void setOnClickListener() {
		// TODO Auto-generated method stub
		iv_shequ_bankuai.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_shequ_bankuai onClicked");
				if (rl_shequ_fenlei.isShown()){
					rl_shequ_fenlei.setVisibility(View.GONE);
				}else{
					rl_shequ_fenlei.setVisibility(View.VISIBLE);
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
				if ("getshequ".equals((String) msg.obj)) {
					updateUI();
				}else if ("getbankuai".equals((String) msg.obj)) {
					updateUI1();
				}else if ("getbybankuai".equals((String) msg.obj)) {
					updateUI2();
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
	// ����----------------------------------------
	public class getCityThread extends Thread {

		public getCityThread() {

		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
//			 line = "[{createtime:\"2015-11-03 20:20:30\",contentTitle:\"��������123\",contentID:\"27\"}]";
			line = getData();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
//					jsonarray = new JSONArray(line.toString());
					jsonobj = new JSONObject(line.toString());
					updateHandler("getshequ");
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
		String servletUrl = getString(R.string.serverURL) + "/app/forum-theme-default.jspx?pageNo=0";
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
	public class ZuJian_lv_shequ {
		public RelativeLayout list_shequ;
		public TextView tv_lvshequ, tv_lvshequid, tv_lv_username, tv_lv_time;
	}

	private ArrayList<HashMap<String, Object>> lst;
	// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
	private MyListAdapter saImageItems;
	private ListView lv_shequ;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private void updateUI() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(getActivity(), lst);// ûʲô����
		
		try {
			if (!"".equals(jsonobj.toString()) && null != jsonobj) {
				jsonarray = jsonobj.getJSONArray("topics");
				for(int i=0;i<jsonarray.length();i++){
					map = new HashMap<String, Object>();
					try {
						JSONObject jsonobj1 = new JSONObject(jsonarray.getString(i));
						map.put("userId", jsonobj1.get("userId").toString());
						map.put("title", jsonobj1.get("title").toString());
						map.put("topicId", jsonobj1.get("topicId").toString());
						map.put("cdate", jsonobj1.get("cdate").toString());
						map.put("userRealname", jsonobj1.get("userRealname").toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					lst.add(map);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		for(int i=0;i<jsonarray.length();i++){
//			map = new HashMap<String, Object>();
//			try {
//				jsonobj = new JSONObject(jsonarray.getString(i));
//				map.put("userId", jsonobj.get("userId").toString());
//				map.put("title", jsonobj.get("title").toString());
//				map.put("topicId", jsonobj.get("topicId").toString());
//				map.put("cdate", jsonobj.get("cdate").toString());
//				map.put("userRealname", jsonobj.get("userRealname").toString());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			lst.add(map);
//		}

		// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
		// MyListAdapter saImageItems = new MyListAdapter(this, lst);// ûʲô����

		// ������
		BinderListData(saImageItems);
	}

	private void updateUI2() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(getActivity(), lst);// ûʲô����
		
				for(int i=0;i<jsonarray2.length();i++){
					map = new HashMap<String, Object>();
					try {
						JSONObject jsonobj2 = new JSONObject(jsonarray.getString(i));
						map.put("userId", jsonobj2.get("userId").toString());
						map.put("title", jsonobj2.get("title").toString());
						map.put("topicId", jsonobj2.get("topicId").toString());
						map.put("cdate", jsonobj2.get("cdate").toString());
						map.put("userRealname", jsonobj2.get("userRealname").toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					lst.add(map);
				}
		
//		for(int i=0;i<jsonarray.length();i++){
//			map = new HashMap<String, Object>();
//			try {
//				jsonobj = new JSONObject(jsonarray.getString(i));
//				map.put("userId", jsonobj.get("userId").toString());
//				map.put("title", jsonobj.get("title").toString());
//				map.put("topicId", jsonobj.get("topicId").toString());
//				map.put("cdate", jsonobj.get("cdate").toString());
//				map.put("userRealname", jsonobj.get("userRealname").toString());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			lst.add(map);
//		}

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
		lv_shequ.setAdapter(saImageItems);
		saImageItems.notifyDataSetChanged();
		// ����ؼ�������
		lv_shequ.setOnItemClickListener(new ItemClickListener());
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
			
			Bundle bundle = new Bundle();  
            bundle.putString("title", (String) m.get("title"));  
            bundle.putString("userRealname", (String) m.get("userRealname"));
            bundle.putString("topicId", (String) m.get("topicId"));
            bundle.putString("cdate", (String) m.get("cdate"));
            fragmentShequFayan.setArguments(bundle);  
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentShequFayan).addToBackStack("fayan").commit();
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
		ZuJian_lv_shequ zuJian = null;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				zuJian = new ZuJian_lv_shequ();
				// ��ȡ�������
				convertView = layoutInflater.inflate(R.layout.lv_shequ, null);

				 zuJian.list_shequ = (RelativeLayout) convertView.findViewById(R.id.list_shequ);
				 zuJian.tv_lvshequ = (TextView) convertView.findViewById(R.id.tv_lvshequ);
				 zuJian.tv_lvshequid = (TextView) convertView.findViewById(R.id.tv_lvshequid);
				 zuJian.tv_lv_username = (TextView) convertView.findViewById(R.id.tv_lv_username);
				 zuJian.tv_lv_time = (TextView) convertView.findViewById(R.id.tv_lv_time);

				// ����Ҫע�⣬��ʹ�õ�tag���洢���ݵġ�
				convertView.setTag(zuJian);
			} else {
				zuJian = (ZuJian_lv_shequ) convertView.getTag();

			}

			// �����ݡ��Լ��¼�����
			 zuJian.tv_lvshequ.setText((String) data.get(position).get("title"));
			 zuJian.tv_lv_username.setText((String) data.get(position).get("userRealname"));
			 zuJian.tv_lvshequid.setText((String) data.get(position).get("topicId"));
			 zuJian.tv_lv_time.setText((String) data.get(position).get("cdate"));

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
	
	
	
	private JSONObject jsonobj1;
	private JSONArray jsonarray1; 
	// ���°��----------------------------------------
	public class getCityThread1 extends Thread {

		public getCityThread1() {

		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
//			 line = "[{createtime:\"2015-11-03 20:20:30\",contentTitle:\"��������123\",contentID:\"27\"}]";
			line = getData1();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
					jsonarray1 = new JSONArray(line.toString());
//					jsonobj1 = new JSONObject(line.toString());
					updateHandler("getbankuai");
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
		String servletUrl = getString(R.string.serverURL) + "/app/forum-theme-list.jspx?";
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
	private JSONObject jsonobj2;
	private JSONArray jsonarray2; 
	// ���°��----------------------------------------
	public class getByBankuai extends Thread {
		private String id;
		public getByBankuai(String id) {
			this.id = id;
		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
//			 line = "[{createtime:\"2015-11-03 20:20:30\",contentTitle:\"��������123\",contentID:\"27\"}]";
			line = getData2(id);
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
					jsonarray2 = new JSONArray(line.toString());
//					jsonobj2 = new JSONObject(line.toString());
					updateHandler("getbybankuai");
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
	private String getData2(String id) {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/forum-topic-list.jspx?themeId=" + id + "&pageNo=0";
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
		public class ZuJian_lv_bankuai {
			public RelativeLayout list_selectbankuai;
			public TextView tv_lvbankuaiid, tv_lvbankuai;
		}

		private ArrayList<HashMap<String, Object>> lst1;
		// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
		private MyListAdapter1 saImageItems1;
		private ListView lv_bankuai;
		private HashMap<String, Object> map1 = new HashMap<String, Object>();

		private void updateUI1() {
			// TODO Auto-generated method stub
			lst1 = new ArrayList<HashMap<String, Object>>();
			saImageItems1 = new MyListAdapter1(getActivity(), lst1);// ûʲô����
			
//				if (!"".equals(jsonobj1.toString()) && null != jsonobj1) {
//					jsonarray1 = jsonobj1.getJSONArray("topics");
					for(int i=0;i<jsonarray1.length();i++){
						map1 = new HashMap<String, Object>();
						try {
							JSONObject jsonobj1 = new JSONObject(jsonarray1.getString(i));
							map1.put("id", jsonobj1.get("id").toString());
							map1.put("title", jsonobj1.get("title").toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						lst1.add(map1);
					}
//				}
			
//			for(int i=0;i<jsonarray.length();i++){
//				map = new HashMap<String, Object>();
//				try {
//					jsonobj = new JSONObject(jsonarray.getString(i));
//					map.put("userId", jsonobj.get("userId").toString());
//					map.put("title", jsonobj.get("title").toString());
//					map.put("topicId", jsonobj.get("topicId").toString());
//					map.put("cdate", jsonobj.get("cdate").toString());
//					map.put("userRealname", jsonobj.get("userRealname").toString());
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				lst.add(map);
//			}

			// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
			// MyListAdapter saImageItems = new MyListAdapter(this, lst);// ûʲô����

			// ������
			BinderListData1(saImageItems1);
		}

		// ������
		public void BinderListData1(MyListAdapter1 saImageItems1) {
			// ListView listView_cart = (ListView)
			// findViewById(R.id.listView_chakan);
			// ��Ӳ�����ʾ
			lv_bankuai.setAdapter(saImageItems1);
			saImageItems1.notifyDataSetChanged();
			// ����ؼ�������
			lv_bankuai.setOnItemClickListener(new ItemClickListener1());
		}

		class ItemClickListener1 implements OnItemClickListener {
			public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
														// click happened
					View arg1,// The view within the AdapterView that was clicked
					int position,// The position of the view in the adapter
					long id// The row id of the item that was clicked
			) {
				Log.i("info", "click:" + String.valueOf(position));
				HashMap<String, Object> m = new HashMap<String, Object>();
				m = lst1.get(position);
				new getByBankuai( m.get("id").toString()).start();
			}
		}

		/*
		 * �������Զ����BaseAdapter��
		 */
		public class MyListAdapter1 extends BaseAdapter {
			private ArrayList<HashMap<String, Object>> data;
			private LayoutInflater layoutInflater;
			private Context context;

			public MyListAdapter1(Context context,
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
			ZuJian_lv_bankuai zuJian = null;

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					zuJian = new ZuJian_lv_bankuai();
					// ��ȡ�������
					convertView = layoutInflater.inflate(R.layout.lv_selectbankuai, null);

					 zuJian.list_selectbankuai = (RelativeLayout) convertView.findViewById(R.id.list_selectbankuai);
					 zuJian.tv_lvbankuaiid = (TextView) convertView.findViewById(R.id.tv_lvbankuaiid);
					 zuJian.tv_lvbankuai = (TextView) convertView.findViewById(R.id.tv_lvbankuai);

					// ����Ҫע�⣬��ʹ�õ�tag���洢���ݵġ�
					convertView.setTag(zuJian);
				} else {
					zuJian = (ZuJian_lv_bankuai) convertView.getTag();

				}

				// �����ݡ��Լ��¼�����
				 zuJian.tv_lvbankuaiid.setText((String) data.get(position).get("id"));
				 zuJian.tv_lvbankuai.setText((String) data.get(position).get("title"));

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



