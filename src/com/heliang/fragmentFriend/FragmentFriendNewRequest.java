package com.heliang.fragmentFriend;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentFriendNewRequest extends Fragment {

	public GlobalVar appState;
	private ImageView iv_newrequest_back;

	// public sportDataThread st = null;

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
		// return inflater.inflate(R.layout.fragment_sleep, container, false);

		View view = inflater.inflate(R.layout.fragment_friend_newrequest, container,
				false);

		findView(view);
		setOnClickListener();
		findFragment();

		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�
		Looper looper = Looper.myLooper();
		// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper
		messageHandler = new MessageHandler(looper);

		new getFriendThread().start();

		return view;
	}

	private void findFragment() {
		// TODO Auto-generated method stub
	}

	public void findView(View view) {
		lv_newrequest = (ListView) view.findViewById(R.id.lv_newrequest);
		iv_newrequest_back = (ImageView) view.findViewById(R.id.iv_newrequest_back);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		iv_newrequest_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_newrequest_back onClicked");
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
				if ("searchfriend".equals((String) msg.obj)) {
					updateUI();
				}else if ("agree".equals((String) msg.obj)) {
					updateUI1();
				}else if ("timeout".equals((String) msg.obj)) {
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
	public class getFriendThread extends Thread {

		public getFriendThread() {

		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line) {
					jsonarray = new JSONArray(line.toString());
					// jsonobj = new JSONObject(line.toString());
					updateHandler("searchfriend");
				} else {
					updateHandler("timeout");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param fieldid
	 *            �豸
	 * @return
	 */
	private String getData() {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/friends-temp-list.jspx?receiveUserId=" + appState.userid;
		String line = "";
		try {
			URL url = new URL(servletUrl);
			try {
				// �������
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				conn.setReadTimeout(appState.SO_TIMEOUT);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				line = in.readLine();
				in.close();
				conn.disconnect();
				return line;
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("info", e.toString());
				// Toast toast =
				// Toast.makeText(getApplicationContext(),"����ʱ��������ܾ�",
				// Toast.LENGTH_LONG);
				// toast.setGravity(Gravity.CENTER, 0, 0);
				// toast.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// ------------------------------------------------------------------
	
	private JSONObject jsonobj1;
	private JSONArray jsonarray1;

	// ������Ӻ�������----------------------------------------
	public class addFriendThread extends Thread {
		private String friendTempId, agree;

		public addFriendThread(String friendTempId, String agree) {
			this.friendTempId = friendTempId;
			this.agree = agree;
		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData1(friendTempId, agree);
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line) {
					jsonarray1 = new JSONArray(line.toString());
					// jsonobj1 = new JSONObject(line.toString());
					updateHandler("agree");
				} else {
					updateHandler("timeout");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param fieldid
	 *            �豸
	 * @return
	 */
	private String getData1(String friendTempId, String agree) {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/friends-add-agree.jspx?friendsTempId=" + friendTempId + "&isAgree=" + agree;
		String line = "";
		try {
			URL url = new URL(servletUrl);
			try {
				// �������
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(appState.REQUEST_TIMEOUT);
				conn.setReadTimeout(appState.SO_TIMEOUT);
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				line = in.readLine();
				in.close();
				conn.disconnect();
				return line;
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("info", e.toString());
				// Toast toast =
				// Toast.makeText(getApplicationContext(),"����ʱ��������ܾ�",
				// Toast.LENGTH_LONG);
				// toast.setGravity(Gravity.CENTER, 0, 0);
				// toast.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private void updateUI1() {
		// TODO Auto-generated method stub
	}

	// ------------------------------------------------------------------
	
	
	public class ZuJian_lv_xuexiao {
		public RelativeLayout list_friend_request;
		public TextView tv_lvfriend_request, tv_lvfriendid_request, tv_lvfriend_requestnichen;
		public Button btn_lv_agree, btn_lv_disagree;
	}

	private ArrayList<HashMap<String, Object>> lst;
	// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
	private MyListAdapter saImageItems;
	private ListView lv_newrequest;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private void updateUI() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(getActivity(), lst);// ûʲô����

		for (int i = 0; i < jsonarray.length(); i++) {
			map = new HashMap<String, Object>();
			try {
				jsonobj = new JSONObject(jsonarray.getString(i));
				map.put("firendTempId", jsonobj.get("firendTempId").toString());
				map.put("friendName", jsonobj.get("receiveUserUsername").toString());
				map.put("friendId", jsonobj.get("receiveUserId").toString());
				map.put("nickName", jsonobj.get("sendUserRealname").toString());
				map.put("cdate", jsonobj.get("cdate").toString());
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
		lv_newrequest.setAdapter(saImageItems);
		saImageItems.notifyDataSetChanged();
		// ����ؼ�������
		lv_newrequest.setOnItemClickListener(new ItemClickListener());
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
			// appState.friend = m.get("friendName").toString();
			// appState.friendid = m.get("friendId").toString();
			getFragmentManager().popBackStack();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				zuJian = new ZuJian_lv_xuexiao();
				// ��ȡ�������
				convertView = layoutInflater.inflate(R.layout.lv_friend_add, null);

				zuJian.list_friend_request = (RelativeLayout) convertView.findViewById(R.id.list_friend_request);
				zuJian.tv_lvfriend_request = (TextView) convertView.findViewById(R.id.tv_lvfriend_request);
				zuJian.tv_lvfriendid_request = (TextView) convertView.findViewById(R.id.tv_lvfriendid_request);
				zuJian.tv_lvfriend_requestnichen = (TextView) convertView.findViewById(R.id.tv_lvfriend_requestnichen);
				zuJian.btn_lv_agree = (Button) convertView.findViewById(R.id.btn_lv_agree);
				zuJian.btn_lv_disagree = (Button) convertView.findViewById(R.id.btn_lv_disagree);

				// ����Ҫע�⣬��ʹ�õ�tag���洢���ݵġ�
				convertView.setTag(zuJian);
			} else {
				zuJian = (ZuJian_lv_xuexiao) convertView.getTag();

			}

			// �����ݡ��Լ��¼�����
			zuJian.tv_lvfriend_request.setText((String) data.get(position).get("friendName"));
			zuJian.tv_lvfriendid_request.setText((String) data.get(position).get("friendId"));
			zuJian.tv_lvfriend_requestnichen.setText((String) data.get(position).get("nickName"));

			zuJian.btn_lv_agree.setOnClickListener(new Button.OnClickListener() {// ��������
				public void onClick(View v) {
					String friendID = (String) data.get(position).get("friendId");
					new addFriendThread((String) data.get(position).get("firendTempId"), "1").start();
				}
			});
			
			zuJian.btn_lv_disagree.setOnClickListener(new Button.OnClickListener() {// ��������
				public void onClick(View v) {
					String friendID = (String) data.get(position).get("friendId");
					new addFriendThread((String) data.get(position).get("firendTempId"), "0").start();
				}
			});

			return convertView;
		}
	}

}
