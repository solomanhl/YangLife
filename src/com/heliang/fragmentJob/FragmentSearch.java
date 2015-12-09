package com.heliang.fragmentJob;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.heliang.MyListView.AutoListView;
import com.heliang.MyListView.AutoListView.OnLoadListener;
import com.heliang.MyListView.AutoListView.OnRefreshListener;
import com.heliang.yanglife.GlobalVar;
import com.heliang.yanglife.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentSearch extends Fragment {

	public GlobalVar appState;
	private String job_list_type;
	private String job_list_type1 = "fabu";
	private String gongxu;
	private TextView tv_alltime, tv_allclassify, tv_condition;
	private ImageView iv_search_back;
	private FragmentJobDetail fragmentJobDetail;
	private int jobPage = 1;
	private String statu = "onReFresh";//onReFresh  onLoad
	
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
		
		View view = inflater.inflate(R.layout.fragment_search, container, false);
		
		findView(view);	
		setOnClickListener();
		findFragment();
		
		//设置AutoListView监听器
		SetAutoListViewListener();
		
		job_list_type = getArguments().getString("job_list_type");  
		gongxu = getArguments().getString("gongxu");
		
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
		Looper looper = Looper.myLooper();
		// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
		messageHandler = new MessageHandler(looper);
			
		statu = "onReFresh";
		jobPage = 1;
		if ("all".equals(job_list_type)){
			new SearchThread(gongxu).start();//列出所有的任务
		}else if ("me".equals(job_list_type)){
			new SearchThread2().start();//个人发布的任务
		}
		
				
        return view;       
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
		fragmentJobDetail = new FragmentJobDetail();
	}

	public void findView(View view){
		lv_search = (AutoListView) view.findViewById(R.id.lv_search);
		tv_alltime = (TextView) view.findViewById(R.id.tv_alltime);
		tv_allclassify = (TextView) view.findViewById(R.id.tv_allclassify);
		tv_condition = (TextView) view.findViewById(R.id.tv_condition);
		iv_search_back = (ImageView) view.findViewById(R.id.iv_search_back);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		tv_alltime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "tv_alltime onClicked");
//				if (tv_condition.isShown()){
//					tv_condition.setVisibility(View.GONE);
//				}else{
//					tv_condition.setVisibility(View.VISIBLE);
//				}
				jobPage = 1;
				if ("all".equals(job_list_type)){
					
				}else if ("me".equals(job_list_type)){//个人发布任务
					job_list_type1 = "fabu";
					new SearchThread2().start();
				}
				
			}
		});
		
		tv_allclassify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "tv_allclassify onClicked");
//				if (tv_condition.isShown()){
//					tv_condition.setVisibility(View.GONE);
//				}else{
//					tv_condition.setVisibility(View.VISIBLE);
//				}
				jobPage = 1;
				if ("all".equals(job_list_type)){
					new SearchThread(gongxu).start();
				}else if ("me".equals(job_list_type)){//个人接受任务
					job_list_type1 = "jieshou";
					new SearchThread1().start();
				}
			}
		});
		
		iv_search_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_search_back onClicked");
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
				if ("search".equals((String) msg.obj)) {
					updateUI();
				}else if ("personal_accept".equals((String) msg.obj)) {
					updateUI();
				}else if ("personal_send".equals((String) msg.obj)) {
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
	// 搜索工作列表----------------------------------------
	public class SearchThread extends Thread {
		String gongxu;
		public SearchThread(String gongxu) {
			this.gongxu = gongxu;
		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData(gongxu);
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
//					{"username":"admin","status":1,"userId":1
					jsonarray = new JSONArray(line.toString());
//					jsonobj = new JSONObject(line.toString());
					updateHandler("search");
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
	private String getData(String gongxu) {
		// TODO Auto-generated method stub
		String	servletUrl = getString(R.string.serverURL) + "/app/job-list-by-all.jspx?schoolId=";
		servletUrl += appState.xuexiaoid + "&cityId=";
		servletUrl += appState.cityid + "&type=";
		servletUrl += gongxu + "&pageNo=" + jobPage;
		
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
	
	// 个人接受工作列表----------------------------------------
	public class SearchThread1 extends Thread {

		public SearchThread1() {

		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData1();
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
//					{"username":"admin","status":1,"userId":1
					jsonarray = new JSONArray(line.toString());
//					jsonobj = new JSONObject(line.toString());
					updateHandler("personal_accept");
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
	private String getData1() {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/job-apply-list-by-personal.jspx?userId=" + appState.userid;
		
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
	
	// 个人发布工作列表----------------------------------------
		public class SearchThread2 extends Thread {

			public SearchThread2() {

			}

			@Override
			public void run() {
				appState.runThread = true;
				String line;
				line = getData2();
				try {
					if (!"".equals(line) && !"null".equals(line) && null != line){
//						{"username":"admin","status":1,"userId":1
						jsonarray = new JSONArray(line.toString());
//						jsonobj = new JSONObject(line.toString());
						updateHandler("personal_send");
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
		private String getData2() {
			// TODO Auto-generated method stub
			String servletUrl = getString(R.string.serverURL) + "/app/job-list-by-personal.jspx?userId=" + appState.userid;
			
			String line = "";
//			line = "{\"username\":\"admin\",\"status\":1,\"userId\":1}";
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
//					Toast toast = Toast.makeText(getApplicationContext(),"请求超时或服务器拒绝", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		// ------------------------------------------------------------------
	
	// ------------------------------------------------------------------
		public class ZuJian_lvsearch {
			public RelativeLayout list_homefrag_favor;
			public ImageView lv_pic, lv_sort;
			public TextView tv_lvinfo1, tv_lvinfo2;
			public ImageView iv_lvlocation;
			public TextView tv_lvlocation;
			public ImageView iv_lvtime;
			public TextView tv_lvtime;
			public ImageView iv_lvcomment;
			public TextView tv_lvcomment;
			public ImageView iv_lvenjoy;
			public TextView tv_lvenjoy;
		}

		private ArrayList<HashMap<String, Object>> lst;
		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		private MyListAdapter saImageItems;
		private AutoListView lv_search;
		private HashMap<String, Object> map = new HashMap<String, Object>();

		private void updateUI() {
			// TODO Auto-generated method stub
			if ("me".equals(job_list_type)){//个人任务
				tv_alltime.setText("发布的任务");
				tv_allclassify.setText("接受的任务");
			}else if ("all".equals(job_list_type)){
				tv_alltime.setText("全部时间");
				tv_allclassify.setText("全部分类");
			}
			
			
			if ("onReFresh".equals(statu)){//如果是刷新，就新建lst
				lst = new ArrayList<HashMap<String, Object>>();
			}else if ("onLoad".equals(statu)){//如果是继续加载就接着添加
				
			}
			saImageItems = new MyListAdapter(getActivity(), lst);// 没什么解释

			for(int i=0;i<jsonarray.length();i++){
				lv_search.setResultSize(jsonarray.length());//根据结果的大小来决定footer显示
				map = new HashMap<String, Object>();
				try {
					jsonobj = new JSONObject(jsonarray.getString(i));
					
//					map.put("pic", R.drawable.pic1);
					map.put("pic", jsonobj.get("userImg").toString());
					map.put("type", jsonobj.get("type").toString());
					if ("0".equals(jsonobj.get("type").toString())){
						map.put("sort", R.drawable.sort2);
					}else if ("1".equals(jsonobj.get("type").toString())){
						map.put("sort", R.drawable.sort1);
					}
					map.put("refTime", jsonobj.get("refTime").toString());
					map.put("title", jsonobj.get("title").toString());
					map.put("pay", jsonobj.get("pay").toString());
					map.put("jobId", jsonobj.get("jobId").toString());
					map.put("likes", jsonobj.get("likes").toString());
					map.put("userId", jsonobj.get("userId").toString());
					map.put("comments", jsonobj.get("comments").toString());
					map.put("txt", jsonobj.get("txt").toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				lst.add(map);
			}
			
//			map = new HashMap<String, Object>();
//			
//			map.put("pic", R.drawable.pic1);
//			map.put("sort", R.drawable.sort1);
//			map.put("info1", "武汉***");
//			map.put("info2", "小白");
//			map.put("fee", "￥200元/小时");
//			map.put("time", "4分钟前");
//			map.put("comment", "10");
//			map.put("enjoy", "14");
//			lst.add(map);
//			map = new HashMap<String, Object>();
//			map.put("pic", R.drawable.pic1);
//			map.put("sort", R.drawable.sort2);
//			map.put("info1", "武汉***");
//			map.put("info2", "小白");
//			map.put("fee", "￥100元/小时");
//			map.put("time", "4分钟前");
//			map.put("comment", "10");
//			map.put("enjoy", "14");
//			lst.add(map);

			// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
			// MyListAdapter saImageItems = new MyListAdapter(this, lst);// 没什么解释

			// 绑定数据
			BinderListData(saImageItems);
			
			if ("onReFresh".equals(statu)){//如果是刷新
				//通知刷新完成
				lv_search.onRefreshComplete();
			}else if ("onLoad".equals(statu)){//如果是继续加载
				//通知加载完成
				lv_search.onLoadComplete();
				//移动到上次加载的的末尾
//				lv_search.smoothScrollToPosition(saImageItems.getCount() - jsonarray.length() - 1);
				lv_search.setSelection(saImageItems.getCount() - jsonarray.length() - 1);//定位X行
			}
		}
		
		private void SetAutoListViewListener() {
			// TODO Auto-generated method stub
		// 点击控件监听器
		lv_search.setOnItemClickListener(new ItemClickListener());

		// 下拉刷新监听器
		lv_search.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				statu = "onReFresh";
				jobPage = 1;
				if ("all".equals(job_list_type)) {
					new SearchThread(gongxu).start();// 列出所有的任务
				} else if ("me".equals(job_list_type)) {
					new SearchThread2().start();// 个人发布的任务
				}
			}

		});

		// 加载更多监听
		lv_search.setOnLoadListener(new OnLoadListener() {
			@Override
			public void onLoad() {
				statu = "onLoad";
				jobPage++;
				if ("all".equals(job_list_type)) {
					new SearchThread(gongxu).start();// 列出所有的任务
				} else if ("me".equals(job_list_type)) {
					new SearchThread2().start();// 个人发布的任务
				}
			}
		});
		}

		// 绑定数据
		public void BinderListData(MyListAdapter saImageItems) {
			// ListView listView_cart = (ListView)
			// findViewById(R.id.listView_chakan);
			// 添加并且显示
			lv_search.setAdapter(saImageItems);
			saImageItems.notifyDataSetChanged();
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
				bundle.putString("pic", m.get("pic").toString());
				bundle.putString("refTime", m.get("refTime").toString());
				bundle.putString("likes", m.get("likes").toString());
				bundle.putString("userId", m.get("userId").toString());
				bundle.putString("comments", m.get("comments").toString());
	            bundle.putString("title", m.get("title").toString());  
	            bundle.putString("txt", m.get("txt").toString());  
	            bundle.putString("jobid", m.get("jobId").toString());
	            bundle.putString("pay", m.get("pay").toString());
	            bundle.putString("type", m.get("type").toString());
	            bundle.putString("job_list_type", job_list_type);
	            bundle.putString("job_list_type1", job_list_type1);
	            fragmentJobDetail.setArguments(bundle);  
	            if ("all".equals(job_list_type)){//从首页或者搜索页进
	            	getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentJobDetail).addToBackStack("jobdetail").commit();
	            }else if ("me".equals(job_list_type)){//从个人入口进
	            	getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_me, fragmentJobDetail).addToBackStack("jobdetail").commit();
	            }
	            
			}
		}

		/*
		 * 以下是自定义的BaseAdapter类
		 */
		public class MyListAdapter extends BaseAdapter {
			private ArrayList<HashMap<String, Object>> data;
			private LayoutInflater layoutInflater;
			private Context context;
			
			//imageLoader
			private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
			private DisplayImageOptions options;

			public MyListAdapter(Context context,
					ArrayList<HashMap<String, Object>> data) {
				this.context = context;
				this.data = data;
				this.layoutInflater = LayoutInflater.from(context);
				
				//imageLoader
				options = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.ic_stub)
						.showImageForEmptyUri(R.drawable.ic_empty)
						.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
						.cacheOnDisk(true).considerExifParams(true)
//						.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
						.displayer(new RoundedBitmapDisplayer(20))
						.build();
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
			ZuJian_lvsearch zuJian = null;

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub

				if (convertView == null) {
					zuJian = new ZuJian_lvsearch();
					// 获取组件布局
					convertView = layoutInflater.inflate(R.layout.lv_homefavor, null);

					 zuJian.list_homefrag_favor = (RelativeLayout) convertView.findViewById(R.id.list_homefrag_favor);
					 zuJian.lv_pic = (ImageView) convertView.findViewById(R.id.lv_pic);
					 zuJian.lv_sort = (ImageView) convertView.findViewById(R.id.lv_sort);
					 zuJian.tv_lvinfo1 = (TextView) convertView.findViewById(R.id.tv_lvinfo1);
					 zuJian.tv_lvinfo2 = (TextView) convertView.findViewById(R.id.tv_lvinfo2);
					 zuJian.iv_lvlocation = (ImageView) convertView.findViewById(R.id.iv_lvlocation);
					 zuJian.tv_lvlocation = (TextView) convertView.findViewById(R.id.tv_lvlocation);
					 zuJian.iv_lvtime = (ImageView) convertView.findViewById(R.id.iv_lvtime);
					 zuJian.tv_lvtime = (TextView) convertView.findViewById(R.id.tv_lvtime);
					 zuJian.iv_lvcomment = (ImageView) convertView.findViewById(R.id.iv_lvcomment);
					 zuJian.tv_lvcomment = (TextView) convertView.findViewById(R.id.tv_lvcomment);
					 zuJian.iv_lvenjoy = (ImageView) convertView.findViewById(R.id.iv_lvenjoy);
					 zuJian.tv_lvenjoy = (TextView) convertView.findViewById(R.id.tv_lvenjoy);

					// 这里要注意，是使用的tag来存储数据的。
					convertView.setTag(zuJian);
				} else {
					zuJian = (ZuJian_lvsearch) convertView.getTag();

				}

				// 绑定数据、以及事件触发
//				 zuJian.lv_pic.setImageResource((Integer) data.get(position).get("pic"));
				//imageLoader
				 ImageLoader.getInstance().displayImage((String) data.get(position).get("pic"), zuJian.lv_pic, options, animateFirstListener);
				 zuJian.lv_sort.setImageResource((Integer) data.get(position).get("sort"));
				 zuJian.tv_lvinfo1.setText((String) data.get(position).get("title"));
//				 zuJian.tv_lvinfo2.setText((String) data.get(position).get("info2"));
				 zuJian.tv_lvinfo2.setVisibility(View.GONE);
//				 zuJian.tv_lvlocation.setText((String) data.get(position).get("fee"));
				 zuJian.iv_lvlocation.setVisibility(View.GONE);
				 zuJian.tv_lvlocation.setVisibility(View.GONE);
				 zuJian.tv_lvtime.setText((String) data.get(position).get("refTime"));
				 zuJian.tv_lvcomment.setText((String) data.get(position).get("comments"));
				 zuJian.tv_lvenjoy.setText((String) data.get(position).get("likes"));

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
		
		//imageLoader
		private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

			static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (loadedImage != null) {
					ImageView imageView = (ImageView) view;
					boolean firstDisplay = !displayedImages.contains(imageUri);
					if (firstDisplay) {
						FadeInBitmapDisplayer.animate(imageView, 500);
						displayedImages.add(imageUri);
					}
				}
			}
		}
}



