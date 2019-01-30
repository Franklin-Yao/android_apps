package com.pingo.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pingo.activity.R;
import com.pingo.model.CartGoodsInfo;


public class OrderListViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<CartGoodsInfo> cartGoodList;	
	private onCheckedChanged listener;

	public OrderListViewAdapter(Context context, ArrayList<CartGoodsInfo> cartGoodList) {

		this.context = context;
		this.cartGoodList = cartGoodList;
	}

	public OrderListViewAdapter(Context context) {
		this.context = context;

	}

	@Override
	public int getCount() {
		return (cartGoodList != null && cartGoodList.size() == 0) ? 0: cartGoodList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View currentView, ViewGroup arg2) {
		HolderView holderView = null;
		if (currentView == null) {
			holderView = new HolderView();
			currentView = LayoutInflater.from(context).inflate(R.layout.adapter_listview_order, null);
			holderView.goods_pic = (ImageView) currentView.findViewById(R.id.iv_adapter_list_pic);
			holderView.tv_name = (TextView) currentView.findViewById(R.id.tv_name);
			holderView.tv_num = (TextView) currentView.findViewById(R.id.tv_num);
			holderView.tv_price = (TextView) currentView.findViewById(R.id.tv_price);
			

			currentView.setTag(holderView);
		} else {
			holderView = (HolderView) currentView.getTag();
		}
		if (cartGoodList.size() != 0) {

			holderView.goods_pic.setImageBitmap(cartGoodList.get(position).getGoodsImg());
			holderView.tv_name.setText(cartGoodList.get(position).getProductName());
			holderView.tv_num.setText("x"+ cartGoodList.get(position).getProductNum());
			holderView.tv_price.setText("￥"+cartGoodList.get(position).getPrice()+"");
		}
		return currentView;
	}

	public class HolderView {

		private ImageView goods_pic;
		private TextView tv_name;
		private TextView tv_num;
		private TextView tv_price;
		

	}
	
	public interface onCheckedChanged{
		
		public void getChoiceData(int position,boolean isChoice);
	}
	public void setOnCheckedChanged(onCheckedChanged listener){
		this.listener=listener;
	}

}
