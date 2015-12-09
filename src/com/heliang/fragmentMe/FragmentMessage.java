package com.heliang.fragmentMe;

import com.heliang.yanglife.GlobalVar;
import com.heliang.yanglife.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentMessage extends Fragment {

	public GlobalVar appState;
	
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
		
		View view = inflater.inflate(R.layout.fragment_message, container, false);
		
		findView(view);	
		setOnClickListener();
		findFragment();
				
        return view;       
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
	}

	public void findView(View view){
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
	}
	
}



