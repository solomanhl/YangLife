package com.heliang.fragmentHuodong;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentHuodong extends Fragment {

	public GlobalVar appState;
	private ImageView iv_back;
	
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
		
		View view = inflater.inflate(R.layout.fragment_huodong, container, false);
		
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
	}

	public void findView(View view){
		lv_list = (ListView) view.findViewById(R.id.lv_list);
		iv_back = (ImageView) view.findViewById(R.id.iv_back);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_back onClicked");
				getFragmentManager().popBackStack();
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
				if ("huodong".equals((String) msg.obj)) {
					updateUI();
				}else if ("timeout".equals((String) msg.obj)) {
					Toast toast = Toast.makeText(getActivity(), "网络连接超时", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}

		}
	}
	
	private JSONObject jsonobj;
	private JSONArray jsonarray; 
	// 搜索工作列表----------------------------------------
	public class SearchThread extends Thread {
		public SearchThread() {
		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
//					{"username":"admin","status":1,"userId":1
					jsonarray = new JSONArray(line.toString());
//					jsonobj = new JSONObject(line.toString());
					updateHandler("huodong");
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
		String	servletUrl = getString(R.string.serverURL) + "/app/activity-list.jspx";
		
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
	
	// ------------------------------------------------------------------
	public class ZuJian_lv_shequ {
		public RelativeLayout list_shequ;
		public TextView tv_lvshequ, tv_lvshequid, tv_lv_username, tv_lv_time;
	}

	private ArrayList<HashMap<String, Object>> lst;
	// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
	private MyListAdapter saImageItems;
	private ListView lv_list;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private void updateUI() {
		// TODO Auto-generated method stub
		lst = new ArrayList<HashMap<String, Object>>();
		saImageItems = new MyListAdapter(getActivity(), lst);// 没什么解释
		
		try {
			if (!"".equals(jsonobj.toString()) && null != jsonobj) {
				jsonarray = jsonobj.getJSONArray("topics");
				for(int i=0;i<jsonarray.length();i++){
					map = new HashMap<String, Object>();
					try {
						JSONObject jsonobj1 = new JSONObject(jsonarray.getString(i));
						map.put("userId", jsonobj1.get("userId").toString());
						map.put("title", jsonobj1.get("title").toString());
						map.put("activityId", jsonobj1.get("activityId").toString());
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
			lv_list.setAdapter(saImageItems);
			saImageItems.notifyDataSetChanged();
			// 点击控件监听器
			lv_list.setOnItemClickListener(new ItemClickListener());
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
				
//				Bundle bundle = new Bundle();  
//	            bundle.putString("title", "title");  
//	            bundle.putString("userRealname", "userRealname");
//	            bundle.putString("activityId", "activityId");
//	            bundle.putString("cdate", "cdate");
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
			ZuJian_lv_shequ zuJian = null;

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					zuJian = new ZuJian_lv_shequ();
					// 获取组件布局
					convertView = layoutInflater.inflate(R.layout.lv_shequ, null);

					 zuJian.list_shequ = (RelativeLayout) convertView.findViewById(R.id.list_shequ);
					 zuJian.tv_lvshequ = (TextView) convertView.findViewById(R.id.tv_lvshequ);
					 zuJian.tv_lvshequid = (TextView) convertView.findViewById(R.id.tv_lvshequid);
					 zuJian.tv_lv_username = (TextView) convertView.findViewById(R.id.tv_lv_username);
					 zuJian.tv_lv_time = (TextView) convertView.findViewById(R.id.tv_lv_time);

					// 这里要注意，是使用的tag来存储数据的。
					convertView.setTag(zuJian);
				} else {
					zuJian = (ZuJian_lv_shequ) convertView.getTag();

				}

				// 绑定数据、以及事件触发
				 zuJian.tv_lvshequ.setText((String) data.get(position).get("title"));
				 zuJian.tv_lv_username.setText((String) data.get(position).get("userRealname"));
				 zuJian.tv_lvshequid.setText((String) data.get(position).get("activityId"));
				 zuJian.tv_lv_time.setText((String) data.get(position).get("cdate"));

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



