package com.michu.huanxin.adapter;

import java.util.List;







import com.michu.huanxin.DemoApplication;
import com.michu.pingju.activity.R;
import com.michu.pingju.bean.HotNewsItem;
import com.michu.pingju.utils.DownImage;
import com.michu.pingju.utils.DownImage.ImageCallBack;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotNewsListAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context mContext;
	private List<HotNewsItem> dataLst;

	public HotNewsListAdapter(Context context,List<HotNewsItem> lst){
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.dataLst = lst;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataLst.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;   
		if (convertView == null)   
		{   
			convertView = mInflater.inflate(R.layout.hotnews_lst_item, null);   
			viewHolder = new ViewHolder();   
			viewHolder.rightImg = (ImageView)convertView.findViewById(R.id.hotnews_lst_item_img_right);
			viewHolder.titleTv = (TextView)convertView.findViewById(R.id.hotnews_lst_item_tv_title);
			viewHolder.infoTv = (TextView)convertView.findViewById(R.id.hotnews_lst_item_tv_info);
			convertView.setTag(viewHolder);   
		} else  
		{   
			viewHolder = (ViewHolder) convertView.getTag();   
		}
		viewHolder.titleTv.setText(dataLst.get(position).titleStr);
		viewHolder.infoTv.setText(dataLst.get(position).infoStr);
		DemoApplication.imageLoader.displayImage(dataLst.get(position).imgUrl, viewHolder.rightImg);
		viewHolder.rightImg.setTag(dataLst.get(position).imgUrl);

		return convertView;
	}

	public class ViewHolder{
		ImageView rightImg;
		TextView titleTv;
		TextView infoTv;
	}

}
