package com.heliang.yanglife;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;

//imageLoader


public class GlobalVar extends Application{
	public String firm;
	//public Integer version = Integer.valueOf(android.os.Build.VERSION.SDK);
	public String version = android.os.Build.VERSION.RELEASE;
	public float screenWidth, screenHeight;
	public float density; //屏幕密度0.75 / 1.0 /1.25/ 1.5
	public int densityDpi; //120 / 160 /200/ 240
	public float wh; 
	public TelephonyManager tm;
	public String IMEI;
	public String card1num, simserial;
	
	public static final int REQUEST_TIMEOUT = 3*1000;
	public static final int SO_TIMEOUT = 50*1000;  
	
	private PowerManager pm;
	private PowerManager.WakeLock mWakeLock;
	
	public String username = "";
	public String userid = "";
	public String usernickname = "";
	public String shen = "";
	public String shenid = "";
	public String cityid = "3";
	public String city = "";
	public String xuexiao = "";
	public String xuexiaoid = "";
	public boolean denglu = false;
	public boolean runThread = false;
	
	
	@Override
	public void onCreate() {
		//imagLoader
		
		Log.d("GlobalVar", "[ExampleApplication] onCreate");
		super.onCreate();

		getAppVersionName();
		
		//imagLoader
		initImageLoader(getApplicationContext());
	}
	
	public int AppVersionCode;
	public String AppVersionName;
	/** 
	 * 返回当前程序版本名称 
	 */  
	public void getAppVersionName() {  
		try {  
		    PackageManager pm = getPackageManager();  
		    PackageInfo pi = pm.getPackageInfo("com.heliang.yanglife", 0);  
		    AppVersionCode = pi.versionCode;
		    AppVersionName = pi.versionName;// 获取在AndroidManifest.xml中配置的版本号  
		} catch (PackageManager.NameNotFoundException e) {  
			AppVersionName = "";  
		}  
	}  

	@SuppressWarnings("deprecation")
	public void keepScreenAlive() {
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		mWakeLock.acquire(); // in onResume() call
	}

	public void unkeepScreenAlive() {
		mWakeLock.release();// in onPause() call
	}
	
	//imageLoader
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
		
}
