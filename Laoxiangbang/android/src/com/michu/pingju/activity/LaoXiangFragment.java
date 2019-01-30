package com.michu.pingju.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.michu.pingju.activity.R;
import com.michu.pingju.adapter.ContactExpandableListAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class LaoXiangFragment extends Fragment{
	
	private ExpandableListView expandableListView;
	private List<String> titles;
	private List<List<HashMap<String,Object>>> mListData;
	public ContactExpandableListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_laoxiang, container, false);
		init();
		initExpandListView(view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
	
	private void init()
	{
		titles=new ArrayList<String>();
		mListData=new ArrayList<List<HashMap<String, Object>>>();		
	}
	
	private void initExpandListView(View view)
	{
		getGroupTitle();		
		getChildItem();
		expandableListView=(ExpandableListView)view.findViewById(R.id.fragment_contact_expList);
		adapter=new ContactExpandableListAdapter(getActivity(),mListData,titles);
		expandableListView.setAdapter(adapter);
	}

	private void getGroupTitle()
	{
		for(int i=0;i<5;i++)
		{
			titles.add("标题"+i);
		}
	}
	
	private void getChildItem()
	{
		for(String temp:titles)
		{
			List<HashMap<String,Object>> listdata=new ArrayList<HashMap<String,Object>>();
			for(int j=0;j<6;j++)
			{
				HashMap<String,Object> tempMap=new HashMap<String,Object>();
				tempMap.put("name", "好友"+j);
				listdata.add(tempMap);
			}
			mListData.add(listdata);		
		}
	}
	
	
}

