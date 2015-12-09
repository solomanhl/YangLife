package com.heliang.fragmentJob;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.heliang.yanglife.GlobalVar;
import com.heliang.yanglife.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentJobDetail extends Fragment {

	public GlobalVar appState;
	private TextView tv_jobget, tv_job_detal_title, tv_job_detail;
	private TextView tv_userid, tv_pay, tv_refTime, tv_comments, tv_likes;
	private ImageView iv_job_detailback, iv_pic;
	private String jobid, type ,pay, job_list_type;
	private String job_list_type1 = "fabu";
	
	//imageLoader
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	
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
		
		View view = inflater.inflate(R.layout.fragment_job_detail, container, false);
		
		
		findView(view);	
		setOnClickListener();
		findFragment();
		
		String title = getArguments().getString("title");  
		String txt = getArguments().getString("txt");  
		String pic = getArguments().getString("pic");  
		String refTime = getArguments().getString("refTime");  
		String likes = getArguments().getString("likes");  
		String userId = getArguments().getString("userId");  
		String comments = getArguments().getString("comments");  

		
		
		jobid = getArguments().getString("jobid");  
		type = getArguments().getString("type");  
		pay = getArguments().getString("pay");  
		job_list_type = getArguments().getString("job_list_type");  
		job_list_type1 = getArguments().getString("job_list_type1");  
		
		//imageLoader
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				// .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
				.displayer(new RoundedBitmapDisplayer(20)).build();
		// imageLoader
		ImageLoader.getInstance().displayImage(getArguments().getString("pic"), iv_pic, options, animateFirstListener);
		tv_refTime.setText(getArguments().getString("refTime"));
		tv_likes.setText(getArguments().getString("likes"));
		tv_userid.setText(getArguments().getString("userId"));
		tv_pay.setText(getArguments().getString("pay"));
		tv_comments.setText(getArguments().getString("comments"));
		tv_job_detal_title.setText(title );
		tv_job_detail.setText("    " + txt);
		
		if ("all".equals(job_list_type)){//从首页或者搜索页进
			tv_jobget.setText("抢单");
        }else if ("me".equals(job_list_type)){//从个人入口进
        	if ("fabu".equals(job_list_type1)){
        		tv_jobget.setText("撤单");
        	}else if ("jieshou".equals(job_list_type1)){
        		tv_jobget.setText("完成");
        	}
        	
        }
		
		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
		Looper looper = Looper.myLooper();
		// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
		messageHandler = new MessageHandler(looper);
				
        return view;       
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
	
	private void findFragment() {
		// TODO Auto-generated method stub
	}

	public void findView(View view){
		tv_jobget = (TextView) view.findViewById(R.id.tv_jobget);
		tv_userid = (TextView) view.findViewById(R.id.tv_userid);
		tv_pay = (TextView) view.findViewById(R.id.tv_pay);
		tv_refTime = (TextView) view.findViewById(R.id.tv_refTime);
		tv_comments = (TextView) view.findViewById(R.id.tv_comments);
		tv_likes = (TextView) view.findViewById(R.id.tv_likes);
		tv_job_detal_title = (TextView) view.findViewById(R.id.tv_job_detal_title);
		tv_job_detail = (TextView) view.findViewById(R.id.tv_job_detail);
		iv_job_detailback = (ImageView) view.findViewById(R.id.iv_job_detailback);
		iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		tv_jobget.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "tv_jobget onClicked");
				if (appState.denglu){
					if ("all".equals(job_list_type)){//从首页或者搜索页进
						new getLoginThread().start();//抢单线程
			        }else if ("me".equals(job_list_type)){//从个人入口进
			        	if ("fabu".equals(job_list_type1)){
			        		new revokeThread().start();//撤单线程
			        	}else if ("jieshou".equals(job_list_type1)){
			        		new completeThread().start();//完成线程
			        	}
			        	
			        }
					
				}else{
					Toast toast = Toast.makeText(getActivity(), "用户未登录", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});		
		
		iv_job_detailback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_job_detailback onClicked");
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
				if ("addjob".equals((String) msg.obj)) {
					updateUI();
				}else if ("revoke".equals((String) msg.obj)) {
					updateUI_revoke();
				}else if ("complete".equals((String) msg.obj)) {
					updateUI_complete();
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
	// 抢单线程----------------------------------------
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
	 * @param fieldid 设备
	 * @return
	 */
	private String getData() {
		// TODO Auto-generated method stub
		String servletUrl = getString(R.string.serverURL) + "/app/job-by-personal_accept.jspx?userId=" + appState.userid;
		servletUrl += "&jobId=" + jobid;
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

	private void updateUI() {
		// TODO Auto-generated method stub
			try {
				String t = jsonobj.get("status").toString();
				if ("1".equals(t)){
					Toast toast = Toast.makeText(getActivity(), "抢单成功", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					getFragmentManager().popBackStack();
				}else if ("0".equals(t)){
					Toast toast = Toast.makeText(getActivity(), "抢单失败", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{
					Toast toast = Toast.makeText(getActivity(), "网络超时", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	// ------------------------------------------------------------------
	
	// 撤单线程----------------------------------------
		public class revokeThread extends Thread {

			public revokeThread() {

			}

			@Override
			public void run() {
				appState.runThread = true;
				String line;
				line = getData_revoke();
				try {
					if (!"".equals(line) && !"null".equals(line) && null != line){
//						{"username":"admin","status":1,"userId":1
//						jsonarray = new JSONArray(line.toString());
						jsonobj = new JSONObject(line.toString());
						updateHandler("revoke");
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
		private String getData_revoke() {
			// TODO Auto-generated method stub
			String servletUrl = getString(R.string.serverURL) + "/app/job-by-personal_revoke.jspx?userId=" + appState.userid;
			servletUrl += "&jobId=" + jobid;
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
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		private void updateUI_revoke() {
			// TODO Auto-generated method stub
				try {
					String t = jsonobj.get("status").toString();
					if ("1".equals(t)){
						Toast toast = Toast.makeText(getActivity(), "撤单成功", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						getFragmentManager().popBackStack();
					}else if ("0".equals(t)){
						Toast toast = Toast.makeText(getActivity(), "撤单失败", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}else{
						Toast toast = Toast.makeText(getActivity(), "网络超时", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		// ------------------------------------------------------------------
		
		// 完成线程----------------------------------------
				public class completeThread extends Thread {

					public completeThread() {

					}

					@Override
					public void run() {
						appState.runThread = true;
						String line;
						line = getData_complete();
						try {
							if (!"".equals(line) && !"null".equals(line) && null != line){
//								{"username":"admin","status":1,"userId":1
//								jsonarray = new JSONArray(line.toString());
								jsonobj = new JSONObject(line.toString());
								updateHandler("complete");
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
				private String getData_complete() {
					// TODO Auto-generated method stub
					String servletUrl = getString(R.string.serverURL) + "/app/job-by-personal_complete.jspx?userId=" + appState.userid;
					servletUrl += "&jobId=" + jobid;
					String line = "";
//					line = "{\"username\":\"admin\",\"status\":1,\"userId\":1}";
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

				private void updateUI_complete() {
					// TODO Auto-generated method stub
						try {
							String t = jsonobj.get("status").toString();
							if ("1".equals(t)){
								Toast toast = Toast.makeText(getActivity(), "完成订单", Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
								getFragmentManager().popBackStack();
							}else if ("0".equals(t)){
								Toast toast = Toast.makeText(getActivity(), "确认失败", Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
							}else{
								Toast toast = Toast.makeText(getActivity(), "网络超时", Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				// ------------------------------------------------------------------
}



