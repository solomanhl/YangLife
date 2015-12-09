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

import com.heliang.fragmentMe.FragmentSelectXuexiao.MyListAdapter;
import com.heliang.fragmentMe.FragmentSelectXuexiao.ZuJian_lv_xuexiao;
import com.heliang.fragmentMe.FragmentSelectXuexiao.getxuexiaoThread;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentFriend extends Fragment {

	public GlobalVar appState;
	private RelativeLayout rl_add_friend, rl_add_msg;
	private FragmentFriendAdd fragmentFriendAdd;
	private FragmentFriendNewRequest fragmentFriendNewRequest;
	
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
		
		View view = inflater.inflate(R.layout.fragment_friend, container, false);
		
		findView(view);	
		setOnClickListener();
		findFragment();
		
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
		Looper looper = Looper.myLooper();
		// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
		messageHandler = new MessageHandler(looper);

		new getFriendThread().start();
				
        return view;       
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
		fragmentFriendAdd = new FragmentFriendAdd();
		fragmentFriendNewRequest = new FragmentFriendNewRequest();
	}

	public void findView(View view){
		lv_friend = (ListView) view.findViewById(R.id.lv_friend);
		rl_add_friend = (RelativeLayout) view.findViewById(R.id.rl_add_friend);
		rl_add_msg = (RelativeLayout) view.findViewById(R.id.rl_add_msg);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		rl_add_friend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "rl_add_friend onClicked");
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentFriendAdd).addToBackStack("addfriend").commit();
			}
		});
		
		rl_add_msg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "rl_add_msg onClicked");
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentFriendNewRequest).addToBackStack("newfriendrequest").commit();
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
				if ("getfriend".equals((String) msg.obj)) {
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
	// 更新----------------------------------------
	public class getFriendThread extends Thread {

		public getFriendThread() {

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
					updateHandler("getfriend");
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
		String servletUrl = getString(R.string.serverURL) + "/app/friends-list.jspx?userId=" + appState.userid;
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
	public class ZuJian_lv_xuexiao {
		public RelativeLayout list_friend;
		public TextView tv_lvfriend, tv_lvfriendid, tv_lvfriend_nichen;
	}

	private ArrayList<HashMap<String, Object>> lst;
	// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
	private MyListAdapter saImageItems;
	private ListView lv_friend;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private void updateUI() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(getActivity(), lst);// 没什么解释
		
		for(int i=0;i<jsonarray.length();i++){
			map = new HashMap<String, Object>();
			try {
				jsonobj = new JSONObject(jsonarray.getString(i));
				map.put("friendName", jsonobj.get("receiveUserUsername").toString());
				map.put("friendId", jsonobj.get("receiveUserId").toString());
				map.put("nickName", jsonobj.get("firendId").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			lst.add(map);
		}

		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		// MyListAdapter saImageItems = new MyListAdapter(this, lst);// 没什么解释

		// 绑定数据
		BinderListData(saImageItems);
	}

	// 绑定数据
	public void BinderListData(MyListAdapter saImageItems) {
		// ListView listView_cart = (ListView)
		// findViewById(R.id.listView_chakan);
		// 添加并且显示
		lv_friend.setAdapter(saImageItems);
		saImageItems.notifyDataSetChanged();
		// 点击控件监听器
		lv_friend.setOnItemClickListener(new ItemClickListener());
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
//			appState.friend = m.get("friendName").toString();
//			appState.friendid = m.get("friendId").toString();
			getFragmentManager().popBackStack();
		}
	}

	/*
	 * 以下是自定义的BaseAdapter类
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
		 * 获取列数
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		/**
		 * 获取某一位置的数据
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		/**
		 * 获取唯一标识
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/**
		 * android绘制每一列的时候，都会调用这个方法
		 */
		ZuJian_lv_xuexiao zuJian = null;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				zuJian = new ZuJian_lv_xuexiao();
				// 获取组件布局
				convertView = layoutInflater.inflate(R.layout.lv_friend, null);

				 zuJian.list_friend = (RelativeLayout) convertView.findViewById(R.id.list_friend);
				 zuJian.tv_lvfriend = (TextView) convertView.findViewById(R.id.tv_lvfriend);
				 zuJian.tv_lvfriendid = (TextView) convertView.findViewById(R.id.tv_lvfriendid);
				 zuJian.tv_lvfriend_nichen = (TextView) convertView.findViewById(R.id.tv_lvfriend_nichen);

				// 这里要注意，是使用的tag来存储数据的。
				convertView.setTag(zuJian);
			} else {
				zuJian = (ZuJian_lv_xuexiao) convertView.getTag();

			}

			// 绑定数据、以及事件触发
			 zuJian.tv_lvfriend.setText((String) data.get(position).get("friendName"));
			 zuJian.tv_lvfriendid.setText((String) data.get(position).get("friendId"));
			 zuJian.tv_lvfriend_nichen.setText((String) data.get(position).get("nickName"));

			// zuJian.btn_datanote.setOnClickListener(new
			// Button.OnClickListener(){//创建监听
			// public void onClick(View v) {
			// //弹窗写note
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
			// 需要下一个Activity返回数据,在onActivityResult()中接收
			// }
			// });

			return convertView;
		}
	}
	
}



