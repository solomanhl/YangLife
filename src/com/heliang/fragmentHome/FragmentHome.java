package com.heliang.fragmentHome;


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
import com.heliang.fragmentHuodong.FragmentHuodong;
import com.heliang.fragmentJob.FragmentJobAdd;
import com.heliang.fragmentJob.FragmentJobDetail;
import com.heliang.fragmentJob.FragmentSearch;
import com.heliang.yanglife.GlobalVar;
import com.heliang.yanglife.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
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

public class FragmentHome extends Fragment {

	public GlobalVar appState;
	public FragmentSelectCity fragmentSelectCity;
	private FragmentSearch fragmentSearch;
	private FragmentJobAdd fragmentJobAdd;
	private FragmentJobDetail fragmentJobDetail;
	private FragmentHuodong fragmentHuodong;
	private TextView tv_city, tv_fabu, tv_home_location;
	private ImageView iv_arrow, iv_fresh, iv_qiandao, iv_huodong, iv_gongying, iv_xuqiu;
	private RelativeLayout ll_favor;
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
		
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		findView(view);	
		setOnClickListener();
		findFragment();
		
		//设置AutoListView监听器
		SetAutoListViewListener();
		
		tv_home_location.setText(appState.xuexiao);
		tv_city.setText(appState.city);
		
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
		Looper looper = Looper.myLooper();
		// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
		messageHandler = new MessageHandler(looper);

		statu = "onReFresh";
		new getTaskThread(String.valueOf(jobPage)).start();
				
//		updateUI();
				
        return view;       
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		AnimateFirstDisplayListener.displayedImages.clear();
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
		fragmentSelectCity = new FragmentSelectCity();
		fragmentSearch = new FragmentSearch();
		fragmentJobAdd = new FragmentJobAdd();
		fragmentJobDetail = new FragmentJobDetail();
		fragmentHuodong = new FragmentHuodong();
	}

	public void findView(View view){
		lv_favor = (AutoListView) view.findViewById(R.id.lv_favor);
		tv_city = (TextView) view.findViewById(R.id.tv_city);
		tv_home_location = (TextView) view.findViewById(R.id.tv_home_location);
		tv_fabu =  (TextView) view.findViewById(R.id.tv_fabu);
		iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
		iv_fresh = (ImageView) view.findViewById(R.id.iv_fresh);
		iv_qiandao = (ImageView) view.findViewById(R.id.iv_qiandao);
		iv_huodong = (ImageView) view.findViewById(R.id.iv_huodong);
		iv_xuqiu = (ImageView) view.findViewById(R.id.iv_xuqiu);
		iv_gongying = (ImageView) view.findViewById(R.id.iv_gongying);
		ll_favor = (RelativeLayout) view.findViewById(R.id.ll_favor);	
	}
	
	private void setOnClickListener() {
		// TODO Auto-generated method stub
		tv_city.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "tv_city onClicked");
//				getFragmentManager().beginTransaction().replace(R.id.container, fragmentHome).commit();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentSelectCity).addToBackStack("selcity").commit();
			}
		});
		
		tv_fabu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "tv_fabu onClicked");
				if (appState.denglu){
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentJobAdd).addToBackStack("addjob").commit();
				}else{
					Toast toast = Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				
			}
		});
		
		iv_arrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_arrow onClicked");
//				getFragmentManager().beginTransaction().replace(R.id.container, fragmentHome).commit();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentSelectCity).addToBackStack("selcity").commit();
			}
		});
		
		iv_fresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_fresh onClicked");
				statu = "onReFresh";
				jobPage = 1;
				new getTaskThread(String.valueOf(jobPage)).start();
			}
		});
		
		iv_qiandao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_qiandao onClicked");
				new qiandaoThread().start();
			}
		});
		
		iv_huodong.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_huodong onClicked");
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentHuodong).addToBackStack("huodong").commit();
			}
		});
		
//		ll_favor.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Log.i("info", "ll_favor onClicked");
//				Bundle bundle = new Bundle();  
//	            bundle.putString("job_list_type", "all");  
//	            fragmentSearch.setArguments(bundle);  
//				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentSearch).addToBackStack("search").commit();
//			}
//		});
		
		iv_gongying.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_gongying onClicked");
				Bundle bundle = new Bundle();  
	            bundle.putString("job_list_type", "all");
	            bundle.putString("gongxu", "1");//供应
	            fragmentSearch.setArguments(bundle);  
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentSearch).addToBackStack("search").commit();
			}
		});
		
		iv_xuqiu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_xuqiu onClicked");
				Bundle bundle = new Bundle();  
	            bundle.putString("job_list_type", "all");
	            bundle.putString("gongxu", "0");//需求
	            fragmentSearch.setArguments(bundle);  
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentSearch).addToBackStack("search").commit();
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
				if ("gettask".equals((String) msg.obj)) {
					updateUI();
				}else if ("dianzan".equals((String) msg.obj)) {
					updateUI_dianzan();
				}else if ("qiandao".equals((String) msg.obj)) {
					updateUI_qiandao();
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
	public class getTaskThread extends Thread {
		private String page;
		public getTaskThread(String page) {
			this.page = page;
		}

		@Override
		public void run() {
			appState.runThread = true;
			String line;
			line = getData(page);
			try {
				if (!"".equals(line) && !"null".equals(line) && null != line){
					jsonarray = new JSONArray(line.toString());
//					jsonobj = new JSONObject(line.toString());
					updateHandler("gettask");
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
	private String getData(String page) {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/job-list-by-all.jspx?schoolId=8" ;
		servletUrl += "&cityId=3"  + "&pageNo=" + page;
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
	
	// 签到----------------------------------------
		public class qiandaoThread extends Thread {
			public qiandaoThread() {
			}

			@Override
			public void run() {
				appState.runThread = true;
				String line;
				line = getQiandao();
				try {
					if (!"".equals(line) && !"null".equals(line) && null != line){
//						jsonarray = new JSONArray(line.toString());
						jsonobj = new JSONObject(line.toString());
						updateHandler("qiandao");
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
		private String getQiandao() {
			// TODO Auto-generated method stub
			String servletUrl = getString(R.string.serverURL) + "/app/user-signs.jspx?userId=" + appState.userid;
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
//					Toast toast = Toast.makeText(getApplicationContext(),"请求超时或服务器拒绝", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		private void updateUI_qiandao(){
			try {
				if ("1".equals(jsonobj.get("status").toString())){
					Toast toast = Toast.makeText(getActivity(),"签到成功", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else if ("0".equals(jsonobj.get("status").toString())){
					Toast toast = Toast.makeText(getActivity(),"已签到", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// ------------------------------------------------------------------

	// ------------------------------------------------------------------
	public class ZuJian_lvfavor {
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
	private AutoListView lv_favor;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private void updateUI() {
		// TODO Auto-generated method stub
		if ("onReFresh".equals(statu)){//如果是刷新，就新建lst
			lst = new ArrayList<HashMap<String, Object>>();
		}else if ("onLoad".equals(statu)){//如果是继续加载就接着添加
			
		}
		
		

		for(int i=0;i<jsonarray.length();i++){
			lv_favor.setResultSize(jsonarray.length());//根据结果的大小来决定footer显示
			map = new HashMap<String, Object>();
			try {
				jsonobj = new JSONObject(jsonarray.getString(i));
				if (!"".equals(jsonobj) && null != jsonobj){
					map.put("pic", R.drawable.pic1);
					map.put("pic", jsonobj.get("userImg").toString());
					map.put("userId", jsonobj.get("userId").toString());//发任务人的id
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
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			lst.add(map);
			
		}
		
//		map = new HashMap<String, Object>();
//		
//		map.put("pic", R.drawable.pic1);
//		map.put("sort", R.drawable.sort1);
//		map.put("title", "武汉***");
//		map.put("info2", "小白");
//		map.put("fee", "￥200元/小时");
//		map.put("refTime", "4分钟前");
//		map.put("comments", "10");
//		map.put("likes", "14");
//		lst.add(map);
//		map = new HashMap<String, Object>();
//		map.put("pic", R.drawable.pic1);
//		map.put("sort", R.drawable.sort2);
//		map.put("title", "武汉***");
//		map.put("info2", "小白");
//		map.put("fee", "￥100元/小时");
//		map.put("refTime", "4分钟前");
//		map.put("comments", "10");
//		map.put("likes", "14");
//		lst.add(map);

		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		// MyListAdapter saImageItems = new MyListAdapter(this, lst);// 没什么解释

		saImageItems = new MyListAdapter(getActivity(), lst);// 没什么解释
		// 绑定数据
		BinderListData(saImageItems);
		
		if ("onReFresh".equals(statu)){//如果是刷新
			//通知刷新完成
			lv_favor.onRefreshComplete();
		}else if ("onLoad".equals(statu)){//如果是继续加载
			//通知加载完成
			lv_favor.onLoadComplete();
			//移动到上次加载的的末尾
			lv_favor.smoothScrollToPosition(saImageItems.getCount() - jsonarray.length() - 1);
		}
	}

	private void SetAutoListViewListener() {
		// TODO Auto-generated method stub
		// 点击控件监听器
		lv_favor.setOnItemClickListener(new ItemClickListener());

		// 下拉刷新监听器
		lv_favor.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				statu = "onReFresh";
				jobPage = 1;
				new getTaskThread(String.valueOf(jobPage)).start();
			}

		});

		// 加载更多监听
		lv_favor.setOnLoadListener(new OnLoadListener() {
			@Override
			public void onLoad() {
				statu = "onLoad";
				jobPage++;
				new getTaskThread(String.valueOf(jobPage)).start();
			}
		});
	}

	// 绑定数据
	public void BinderListData(MyListAdapter saImageItems) {
		// ListView listView_cart = (ListView)
		// findViewById(R.id.listView_chakan);
		// 添加并且显示
		lv_favor.setAdapter(saImageItems);
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
			bundle.putString("likes", m.get("likes").toString());
			bundle.putString("userId", m.get("userId").toString());
			bundle.putString("comments", m.get("comments").toString());
            bundle.putString("title", m.get("title").toString());  
            bundle.putString("txt", m.get("txt").toString());  
            bundle.putString("jobid", m.get("jobId").toString());
            bundle.putString("pay", m.get("pay").toString());
            bundle.putString("type", m.get("type").toString());
            bundle.putString("job_list_type", "all");
            bundle.putString("job_list_type1", "fabu");
            
            fragmentJobDetail.setArguments(bundle);  
            //从首页或者搜索页进
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentJobDetail).addToBackStack("jobdetail").commit();
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
//					.displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
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
		ZuJian_lvfavor zuJian = null;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				zuJian = new ZuJian_lvfavor();
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
				zuJian = (ZuJian_lvfavor) convertView.getTag();

			}

			// 绑定数据、以及事件触发
//			 zuJian.lv_pic.setImageResource((Integer) data.get(position).get("pic"));
			//imageLoader
			 ImageLoader.getInstance().displayImage((String) data.get(position).get("pic"), zuJian.lv_pic, options, animateFirstListener);
			 zuJian.lv_sort.setImageResource((Integer) data.get(position).get("sort"));
			 zuJian.tv_lvinfo1.setText((String) data.get(position).get("title"));
//			 zuJian.tv_lvinfo2.setText((String) data.get(position).get("info2"));
			 zuJian.tv_lvinfo2.setVisibility(View.GONE);
//			 zuJian.tv_lvlocation.setText((String) data.get(position).get("fee"));
			 zuJian.iv_lvlocation.setVisibility(View.GONE);
			 zuJian.tv_lvlocation.setVisibility(View.GONE);
			 zuJian.tv_lvtime.setText((String) data.get(position).get("refTime"));
			 zuJian.tv_lvcomment.setText((String) data.get(position).get("comments"));
			 zuJian.tv_lvenjoy.setText((String) data.get(position).get("likes"));

			 //点赞
			zuJian.iv_lvenjoy.setOnClickListener(new ImageView.OnClickListener() {// 创建监听
						public void onClick(View v) {
							if (appState.denglu){
								//传发任务人的id
								new dianZan((String) data.get(position).get("userId")).start();
							}else {
								Toast toast = Toast.makeText(getActivity(),"用户未登录", Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
							}
							
						}
					});

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
	
	// 对个人点赞----------------------------------------
		public class dianZan extends Thread {
			String targetID;
			public dianZan(String targetID ) {
				this.targetID = targetID;
			}

			@Override
			public void run() {
				appState.runThread = true;
				String line;
				line = getData_dianzan(targetID);
				try {
					if (!"".equals(line) && !"null".equals(line) && null != line){
//						jsonarray = new JSONArray(line.toString());
						jsonobj = new JSONObject(line.toString());
						updateHandler("dianzan");
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
		private String getData_dianzan(String targetID) {
			// TODO Auto-generated method stub
			String servletUrl = getString(R.string.serverURL) + "/app/user-likes.jspx?likesUserId=" + targetID;
			servletUrl += "&userId=" + appState.userid;
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
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		
		private void updateUI_dianzan(){
			Toast toast = Toast.makeText(getActivity(),"赞+1", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			statu = "onReFresh";
			new getTaskThread(String.valueOf(jobPage)).start();
		}
		// ------------------------------------------------------------------

}



