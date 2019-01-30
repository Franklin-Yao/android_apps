package com.michu.huanxin.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.michu.huanxin.adapter.HotNewsListAdapter;
import com.michu.pingju.activity.R;
import com.michu.pingju.bean.HotNewsItem;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 * 
 */
public class ServiceFragment extends Fragment implements OnClickListener{

	private InputMethodManager inputMethodManager;
	private LinearLayout featureFoodLin,getFriendLin,fellowPartyLin,dialectPlayLin;
	private ListView hotNewsLst;
	private List<HotNewsItem> dataList;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_service, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		initWidget();
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		dataList = new ArrayList<HotNewsItem>();
		HotNewsItem itemTest= new HotNewsItem();
		itemTest.imgUrl = "http://www.android100.org/uploadfile/2012/0320/20120320102028817.jpg";
		itemTest.infoStr = "测试信息";
		itemTest.titleStr = "测试标题";
		dataList.add(itemTest);
		HotNewsItem itemTest1 = new HotNewsItem();
		itemTest1.imgUrl = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2081654936,553689819&fm=116&gp=0.jpg";
		itemTest1.infoStr = "测试信息1";
		itemTest1.titleStr = "测试标题1";
		dataList.add(itemTest1);
		HotNewsItem itemTest2 = new HotNewsItem();
		itemTest2.imgUrl = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2396612654,916110444&fm=111&gp=0.jpg";
		itemTest2.infoStr = "测试信息2";
		itemTest2.titleStr = "测试标题2";
		dataList.add(itemTest2);
		setListView();
	}

	private void setListView() {
		// TODO Auto-generated method stub
		hotNewsLst.setAdapter(new HotNewsListAdapter(this.getActivity(),dataList));
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		featureFoodLin = (LinearLayout)getView().findViewById(R.id.fragment_service_ll_featurefood);
		getFriendLin = (LinearLayout)getView().findViewById(R.id.fragment_service_ll_getfriends);
		fellowPartyLin = (LinearLayout)getView().findViewById(R.id.fragment_service_ll_fellowparty);
		dialectPlayLin = (LinearLayout)getView().findViewById(R.id.fragment_service_ll_dialectplay);

		hotNewsLst = (ListView)getView().findViewById(R.id.fragment_service_lst_hotnews);

		featureFoodLin.setOnClickListener(this);
		getFriendLin.setOnClickListener(this);
		fellowPartyLin.setOnClickListener(this);
		dialectPlayLin.setOnClickListener(this);
	}

	void hideSoftKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.fragment_service_ll_featurefood:
			startActivity(new Intent(getActivity(),FeatureFoodActivity.class));
			break;
		case R.id.fragment_service_ll_getfriends:
			break;
		case R.id.fragment_service_ll_fellowparty:
			break;
		case R.id.fragment_service_ll_dialectplay:
			break;
		default: 
			break;
		}
	}


}