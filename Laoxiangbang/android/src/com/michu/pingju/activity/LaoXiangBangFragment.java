package com.michu.pingju.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.michu.pingju.activity.R;
import com.michu.pingju.adapter.LaoXiangBangAdapter;

public class LaoXiangBangFragment extends Fragment{

	private ListView listView;
	private List<HashMap<String,Object>> listMaps;
	private LaoXiangBangAdapter adapter;
	
	@Override
 	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_laoxiangbang, container, false);
		listView=(ListView)view.findViewById(R.id.fragment_laoxiangbang_listView);
		init();
		getListData();
		setAdapter();
		OnClickItem();
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);		
	}
	
	private void init()
	{
		listMaps=new ArrayList<HashMap<String,Object>>();
	}
	
	private void setAdapter()
	{
		adapter=new LaoXiangBangAdapter(getActivity(), listMaps);
		listView.setAdapter(adapter);
	}
	
	private void getListData()
	{
		for(int j=0;j<10;j++)
		{
			HashMap<String, Object> tempMap=new HashMap<String,Object>(); 
			tempMap.put("name", "武 汉老乡帮"+j);
			tempMap.put("remark", "这个是备注"+j);
			listMaps.add(tempMap);
		}
	}

	private void OnClickItem()
	{
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {			
				
			}
			
		});
	}
}

