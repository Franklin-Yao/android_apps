package com.michu.pingju.adapter;

import java.util.HashMap;
import java.util.List;

import com.michu.pingju.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LaoXiangBangAdapter extends BaseAdapter{

	private Context context;
	private List<HashMap<String,Object>> data;
	
	public LaoXiangBangAdapter(Context context,List<HashMap<String,Object>> data)
	{
		this.context=context;
		this.data=data;				
	}
	
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) 
	{		
		ViewHolder holder=null;
		if(view==null)
		{
			view=LayoutInflater.from(context).inflate(R.layout.item_laoxiangbang, null);
			holder=new ViewHolder();
			holder.icon=(ImageView)view.findViewById(R.id.item_laoxiangbang_icon);
			holder.name=(TextView)view.findViewById(R.id.item_laoxiangbang_name);
			holder.remark=(TextView)view.findViewById(R.id.item_laoxiangbang_remark);
			
			view.setTag(holder);
		}
		else{
			holder=(ViewHolder)view.getTag();		
		}
		
		holder.name.setText((String)data.get(arg0).get("name"));		
		holder.remark.setText((String)data.get(arg0).get("remark"));
		
		return view;
	}
	
	@Override
	public int getCount() { return data.size(); }

	@Override
	public Object getItem(int arg0) {	return data.get(arg0);	}

	@Override
	public long getItemId(int arg0) { return arg0; }	
	
	class ViewHolder{
		private ImageView icon;
		private TextView name;
		private TextView remark;
	}
	
}
