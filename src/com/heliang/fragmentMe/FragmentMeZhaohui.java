package com.heliang.fragmentMe;

import com.heliang.yanglife.GlobalVar;
import com.heliang.yanglife.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentMeZhaohui extends Fragment {

	private GlobalVar appState;
	private FragmentMeLogin fragmentMeLogin;
	private ImageView iv_me_zhaohuiback;

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

		View view = inflater.inflate(R.layout.fragment_me_zhaomima, container, false);

		findView(view);
		setOnClickListener();
		findFragment();

		return view;
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
		fragmentMeLogin = new FragmentMeLogin();
	}

	public void findView(View view) {
		iv_me_zhaohuiback = (ImageView) view.findViewById(R.id.iv_me_zhaohuiback);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		iv_me_zhaohuiback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "iv_me_zhaohuiback onClicked");
				getFragmentManager().popBackStack();
			}
		});
	}
	
}
