package com.michu.pingju.adapter;

import java.util.HashMap;
import java.util.List;

import com.michu.pingju.activity.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactExpandableListAdapter extends BaseExpandableListAdapter{

	private List<String> groupTitle;
	private List<List<HashMap<String, Object>>> items;
	private Context context;
	
	public ContactExpandableListAdapter(Context context,List<List<HashMap<String, Object>>> items,List<String> groupTitle)
	{
		this.context=context;
		this.items=items;
		this.groupTitle=groupTitle;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {	return items.get(groupPosition).get(childPosition); }

	@Override
	public long getChildId(int groupPosition, int childPosition) { return childPosition; }

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean arg2, View view,ViewGroup arg4) {
		ItemHolder itemHolder=null;
		if(view==null)
		{
			itemHolder=new ItemHolder();
			view=LayoutInflater.from(context).inflate(R.layout.item_contact_person, null);
			itemHolder.txt=(TextView)view.findViewById(R.id.item_contact_person_name);
			itemHolder.img=(ImageView)view.findViewById(R.id.item_contact_person_img);			
			view.setTag(itemHolder);
		}
		else
		{
			itemHolder=(ItemHolder)view.getTag();
		}
		
		itemHolder.txt.setText((String)items.get(groupPosition).get(childPosition).get("name"));
		itemHolder.img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher));
		return view;
	}
	
	@Override
	public View getGroupView(int position, boolean arg1, View view, ViewGroup arg3) 
	{		
		GroupHolder groupHolder=null;
		if(view==null)		
		{
			view=LayoutInflater.from(context).inflate(R.layout.item_contact_group, null);
			groupHolder=new GroupHolder();
			groupHolder.txt=(TextView)view.findViewById(R.id.item_contact_group_name_tv);			
			view.setTag(groupHolder);
		}
		else
		{
			groupHolder=(GroupHolder)view.getTag();
		}
		groupHolder.txt.setText(groupTitle.get(position));
		
		return view;
	}

	class GroupHolder 
	{
		public TextView txt;
	}
		 
    class ItemHolder 
    {
    	public ImageView img;
		public TextView txt;
    }

	@Override
	public int getChildrenCount(int arg0) {	return items.get(arg0).size(); }

	@Override
	public Object getGroup(int arg0) {	return groupTitle.get(arg0);  }

	@Override
	public int getGroupCount() { return groupTitle.size(); }

	@Override
	public long getGroupId(int groupId) { return groupId;	}

	@Override
	public boolean hasStableIds() {		
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {		
		return false;
	}

	
}
