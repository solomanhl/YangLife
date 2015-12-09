package com.heliang.fragmentShequ;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentShequFayan extends Fragment {

	public GlobalVar appState;
	private ImageView iv_shequ_fayan_back;
	private Button btn_shequ_msg_send;
	private EditText et_shequ_msg;
	private String title;  
	private String cdate;  
	private String userRealname;  
	private String topicId;  
	
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
		
		View view = inflater.inflate(R.layout.fragment_shequ_fayan, container, false);
		findView(view);	
		setOnClickListener();
		findFragment();
//		updateUI();
		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�
		Looper looper = Looper.myLooper();
		// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper
		messageHandler = new MessageHandler(looper);


		title = getArguments().getString("title");  
		cdate = getArguments().getString("cdate");  
		userRealname = getArguments().getString("userRealname");  
		topicId = getArguments().getString("topicId");  
		
		new getMsgThread().start();
		
        return view;       
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
	}

	public void findView(View view){
		lv_shequ_fayan = (ListView) view.findViewById(R.id.lv_shequ_fayan);
		iv_shequ_fayan_back	 = (ImageView) view.findViewById(R.id.iv_shequ_fayan_back);
		btn_shequ_msg_send	 = (Button) view.findViewById(R.id.btn_shequ_msg_send);
		et_shequ_msg = (EditText) view.findViewById(R.id.et_shequ_msg);
	}
	
	private void setOnClickListener() {
		// TODO Auto-generated method stub
		iv_shequ_fayan_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_shequ_fayan_back onClicked");
				getFragmentManager().popBackStack();
			}
		});
		
		btn_shequ_msg_send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "btn_shequ_msg_send onClicked");
				new sendMsgThread().start();
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
				if ("getmsg".equals((String) msg.obj)) {
					updateUI();
				}else if ("sendmsg".equals((String) msg.obj)) {
					updateUI_send();
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
	// ����Ϣ----------------------------------------
	public class getMsgThread extends Thread {

		public getMsgThread() {

		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getMsg();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
					jsonarray = new JSONArray(line.toString());
//					jsonobj = new JSONObject(line.toString());
					updateHandler("getmsg");
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
	private String getMsg() {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/forum-message-list.jspx?topicId=" + topicId + "&pageNo=1";
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
	
	// ����Ϣ----------------------------------------
		public class sendMsgThread extends Thread {

			public sendMsgThread() {

			}

			@Override
			public void run() {
				appState.runThread = true;
				String line;
				line = sendMsg();
				try {
					if (!"".equals(line) && !"null".equals(line) && null != line){
//						jsonarray = new JSONArray(line.toString());
						jsonobj = new JSONObject(line.toString());
						updateHandler("sendmsg");
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
		private String sendMsg() {
			// TODO Auto-generated method stub
			try {
				String servletUrl = getString(R.string.serverURL) + "/app/forum-message-release.jspx?topicId=" + topicId;
				servletUrl += "&userId=" + appState.userid + "&txt=" + URLEncoder.encode(et_shequ_msg.getText().toString(), "UTF-8");
				String line = "";
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
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

	private void updateUI_send() {
		// TODO Auto-generated method stub
		// �б��м�һ������
		map = new HashMap<String, Object>();
		map.put("userRealname", appState.username);
//		map.put("userRealname", appState.usernickname);//�ȴ������ǳ�
		map.put("txt", et_shequ_msg.getText().toString());
		lst.add(map);
		saImageItems.notifyDataSetChanged();
		lv_shequ_fayan.smoothScrollToPosition(saImageItems.getCount() - 1);//�ƶ���β��
		// ����ı���
		et_shequ_msg.setText("");
		//�������뷨
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
		//�õ�InputMethodManager��ʵ��
		if (imm.isActive()) {
		//�������
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
		//�ر�����̣�����������ͬ������������л�������ر�״̬��
		}
	}
		// ------------------------------------------------------------------
		
	public class ZuJian_lv_xuexiao {
		public RelativeLayout list_selectxuexiao;
		public TextView tv_lvfriendmsgname, tv_lvfriendmsgid, tv_lvfriendmsgtxt;
	}

	private ArrayList<HashMap<String, Object>> lst;
	// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
	private MyListAdapter saImageItems;
	private ListView lv_shequ_fayan;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private void updateUI() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(getActivity(), lst);// ûʲô����
		
		for(int i=0;i<jsonarray.length();i++){
			map = new HashMap<String, Object>();
			try {
				jsonobj = new JSONObject(jsonarray.getString(i));
				map.put("userRealname", jsonobj.get("userRealname").toString());
				map.put("txt", jsonobj.get("txt").toString());
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
		lv_shequ_fayan.setAdapter(saImageItems);
		saImageItems.notifyDataSetChanged();
		lv_shequ_fayan.smoothScrollToPosition(saImageItems.getCount() - 1);//�ƶ���β��
		// ����ؼ�������
		lv_shequ_fayan.setOnItemClickListener(new ItemClickListener());
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
		ZuJian_lv_xuexiao zuJian = null;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				zuJian = new ZuJian_lv_xuexiao();
				// ��ȡ�������
				convertView = layoutInflater.inflate(R.layout.lv_friendmsg, null);

				 zuJian.list_selectxuexiao = (RelativeLayout) convertView.findViewById(R.id.list_selectxuexiao);
				 zuJian.tv_lvfriendmsgname = (TextView) convertView.findViewById(R.id.tv_lvfriendmsgname);
				 zuJian.tv_lvfriendmsgtxt = (TextView) convertView.findViewById(R.id.tv_lvfriendmsgtxt);
				 zuJian.tv_lvfriendmsgid = (TextView) convertView.findViewById(R.id.tv_lvfriendmsgid);

				// ����Ҫע�⣬��ʹ�õ�tag���洢���ݵġ�
				convertView.setTag(zuJian);
			} else {
				zuJian = (ZuJian_lv_xuexiao) convertView.getTag();

			}

			// �����ݡ��Լ��¼�����
			 zuJian.tv_lvfriendmsgname.setText((String) data.get(position).get("userRealname") + "  ˵��");
			 zuJian.tv_lvfriendmsgtxt.setText((String) data.get(position).get("txt"));
//			 zuJian.tv_lvfriendmsgid.setText((String) data.get(position).get("id"));

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



