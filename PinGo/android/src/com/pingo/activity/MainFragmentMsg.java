package com.pingo.activity;
import com.pingo.model.Data;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainFragmentMsg extends Fragment implements OnClickListener{

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main_fragment_msg, null);
		TextView Title=(TextView)v.findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("消息");
    	ImageView IvRight=(ImageView)v.findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
    	ImageView IvLeft=(ImageView)v.findViewById(R.id.common_top).findViewById(R.id.iv_back);
    	IvLeft.setVisibility(View.GONE);
		return v;
	}
	public void startchat(View v) {      //小黑  对话界面
		Intent intent = new Intent (getActivity(),ChatActivity.class);	
		startActivity(intent);
		//Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
      }
	public MainFragmentMsg() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
