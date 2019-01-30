package com.pingo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pingo.activity.R;
import com.pingo.utils.Constant;

public class GoodCatListAdapter extends BaseExpandableListAdapter {
	private Dialog dlg;
	private Context ctx;
	//设置组视图的图片
//    int[] logos = new int[] { R.drawable.ads1, R.drawable.ads2,R.drawable.ads2};
    //设置组视图的显示文字
    private String[] generalsTypes = Constant.cat;
    //子视图显示文字
    String [][] generals={Constant.cat1,Constant.cat2,Constant.cat3,Constant.cat4,Constant.cat5};
    //自己定义一个获得文字信息的方法
    public GoodCatListAdapter(Context context, Dialog dialog) {
        ctx = context;
        dlg=dialog;
    }
    TextView getTextView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 64);
        TextView textView = new TextView(ctx);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(60, 0, 0, 0);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
    //重写ExpandableListAdapter中的各个方法
    @Override
    public int getGroupCount() {
        return generalsTypes.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return generalsTypes[groupPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return generals[groupPosition].length;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return generals[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
    	LinearLayout parentLayout=(LinearLayout) View.inflate(ctx, R.layout.activity_main_home_item_expandablelist_parent, null);
        TextView parentTextView=(TextView) parentLayout.findViewById(R.id.tv_expandablelist_name);
        parentTextView.setText(generalsTypes[groupPosition]);
        ImageView parentImageViw=(ImageView) parentLayout.findViewById(R.id.iv_expandablelist_parent);
        //判断isExpanded就可以控制是按下还是关闭，同时更换图片
        if(isExpanded){
            parentImageViw.setBackgroundResource(R.drawable.activity_main_arrow_up);
        }else{
            parentImageViw.setBackgroundResource(R.drawable.activity_main_arrow_down_black);
        } 
        return parentLayout;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
    	RelativeLayout parentLayout=(RelativeLayout) View.inflate(ctx, R.layout.activity_main_home_item_expandablelist_child, null);
        TextView parentTextView=(TextView) parentLayout.findViewById(R.id.tv_expandablelist_name);
        parentTextView.setText(generals[groupPosition][childPosition]);
        return parentLayout;
    }

    @Override
    public boolean isChildSelectable(int groupPosition,
            int childPosition) {
        return true;
    }
}
