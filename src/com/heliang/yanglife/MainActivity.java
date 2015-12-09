package com.heliang.yanglife;

import com.heliang.fragmentFriend.FragmentFriend;
import com.heliang.fragmentHome.FragmentHome;
import com.heliang.fragmentHome.FragmentSelectCity;
import com.heliang.fragmentMe.FragmentMeLogin;
import com.heliang.fragmentShequ.FragmentShequ;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private GlobalVar appState;
	public FragmentHome fragmentHome;
	public FragmentMeLogin fragmentMeLogin;
	private FragmentShequ fragmentShequ;
	private FragmentFriend fragmentFriend;
	
	private ImageView iv_home, iv_msg, iv_frends, iv_me;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		appState = (GlobalVar) getApplicationContext(); // 获得全局变量
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置成竖屏
		//去掉title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findView();
		setOnClickListener();
		findFragment();
		
		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction().add(R.id.container, fragmentHome).commit();
			getSupportFragmentManager().beginTransaction().add(R.id.container, fragmentHome).commit();
			
			iv_home.setBackgroundResource(R.drawable.nav_icon5);//变亮
			iv_msg.setBackgroundResource(R.drawable.nav_icon2);
			iv_frends.setBackgroundResource(R.drawable.nav_icon3);
			iv_me.setBackgroundResource(R.drawable.nav_icon4);
			
		}
	}


	private void findFragment() {
		// TODO Auto-generated method stub
		fragmentHome = new FragmentHome();
		fragmentMeLogin = new FragmentMeLogin();
		fragmentShequ = new FragmentShequ();
		fragmentFriend = new FragmentFriend();
	}


	private void findView() {
		// TODO Auto-generated method stub
		iv_home = (ImageView) findViewById(R.id.iv_home);
		iv_me = (ImageView) findViewById(R.id.iv_me);
		iv_msg = (ImageView) findViewById(R.id.iv_msg);
		iv_frends = (ImageView) findViewById(R.id.iv_frends);
	}
	
	private void setOnClickListener() {
		// TODO Auto-generated method stub
		iv_home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_home onClicked");
				//首先清空所有回退栈
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//				getFragmentManager().beginTransaction().replace(R.id.container, fragmentHome).commit();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentHome).addToBackStack("selcity").commit();
				iv_home.setBackgroundResource(R.drawable.nav_icon5);//变亮
				iv_msg.setBackgroundResource(R.drawable.nav_icon2);
				iv_frends.setBackgroundResource(R.drawable.nav_icon3);
				iv_me.setBackgroundResource(R.drawable.nav_icon4);
			}
		});
		
		iv_msg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_msg onClicked");
				//首先清空所有回退栈
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentShequ).addToBackStack("shequ").commit();
				iv_home.setBackgroundResource(R.drawable.nav_icon1);
				iv_msg.setBackgroundResource(R.drawable.nav_icon6);//变亮
				iv_frends.setBackgroundResource(R.drawable.nav_icon3);
				iv_me.setBackgroundResource(R.drawable.nav_icon4);
			}
		});
		
		iv_frends.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_frends onClicked");
				//首先清空所有回退栈
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentFriend).addToBackStack("friend").commit();
				iv_home.setBackgroundResource(R.drawable.nav_icon1);
				iv_msg.setBackgroundResource(R.drawable.nav_icon2);
				iv_frends.setBackgroundResource(R.drawable.nav_icon7);//变亮
				iv_me.setBackgroundResource(R.drawable.nav_icon4);
			}
		});
		
		iv_me.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_me onClicked");
//				getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentMe).commit();
//				iv_home.setBackgroundResource(R.drawable.nav_icon1);
//				iv_me.setBackgroundResource(R.drawable.nav_icon8);//变亮
				//首先清空所有回退栈
				getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				if (appState.denglu){
					Intent it = new Intent(MainActivity.this, MeActivity.class);
					startActivity(it);
				}else{
					Intent it = new Intent(MainActivity.this, MeActivity.class);
					startActivityForResult(it, 1);
				}
				
			}
		});
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != -1 && resultCode !=0) {
			appState.denglu = Boolean.valueOf(data.getStringExtra("denglu"));
			Log.e("debug",String.valueOf(appState.denglu));
			if (appState.denglu){
//				showMeFrag();
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//			return rootView;
//		}
//	}
}
