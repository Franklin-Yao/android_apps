package com.pingo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainFragmentPingo extends Fragment implements OnClickListener{
	private TextView pingoHint;
	private int pingoNum;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main_fragment_pingo, null);
		initView(view);
		return view;
	}
	public MainFragmentPingo() {
		// TODO Auto-generated constructor stub
	}
	private void initView(View v) {
		TextView Title=(TextView)v.findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("拼购");
    	ImageView IvLeft=(ImageView)v.findViewById(R.id.common_top).findViewById(R.id.iv_back);
    	IvLeft.setVisibility(View.GONE);
    	ImageView IvRight=(ImageView)v.findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View arg0) {
	}

}
