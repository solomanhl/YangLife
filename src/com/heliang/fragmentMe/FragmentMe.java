package com.heliang.fragmentMe;

import com.heliang.fragmentJob.FragmentSearch;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentMe extends Fragment {

	public GlobalVar appState;
	private FragmentSearch fragmentSearch;
	private RelativeLayout rl_me_shenqin;
//	private Button btn_regster, btn_login;
	
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
		
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		
		findView(view);	
		setOnClickListener();
		findFragment();
				
        return view;       
	}
	
	private void findFragment() {
		// TODO Auto-generated method stub
		fragmentSearch = new FragmentSearch();
	}

	public void findView(View view){
		rl_me_shenqin = (RelativeLayout) view.findViewById(R.id.rl_me_shenqin);
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		rl_me_shenqin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("info", "rl_me_shenqin onClicked");
				Bundle bundle = new Bundle();  
	            bundle.putString("job_list_type", "me");  
	            fragmentSearch.setArguments(bundle);  
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_me, fragmentSearch).addToBackStack("myjob").commit();
			}
		});
		
	}
	
}



