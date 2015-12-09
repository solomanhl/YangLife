package com.heliang.yanglife;


import com.heliang.fragmentMe.FragmentMe;
import com.heliang.fragmentMe.FragmentMeLogin;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MeActivity extends FragmentActivity {

	private GlobalVar appState;
	public FragmentMe fragmentMe;
	private FragmentMeLogin fragmentMeLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		appState = (GlobalVar) getApplicationContext(); // 获得全局变量
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置成竖屏
		//去掉title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me);
		
		findView();
		setOnClickListener();
		findFragment();
		
		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction().add(R.id.container, fragmentHome).commit();
			if (appState.denglu){
				getSupportFragmentManager().beginTransaction().add(R.id.container_me, fragmentMe).commit();
			}else{
				getSupportFragmentManager().beginTransaction().add(R.id.container_me, fragmentMeLogin).commit();
			}
		}
	}


	private void findFragment() {
		// TODO Auto-generated method stub
		fragmentMe = new FragmentMe();
		fragmentMeLogin = new FragmentMeLogin();
	}


	private void findView() {
		// TODO Auto-generated method stub
	}
	
	private void setOnClickListener() {
		// TODO Auto-generated method stub
		
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
