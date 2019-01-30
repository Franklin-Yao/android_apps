package com.pingo.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.pingo.activity.R;
import com.pingo.model.CartGoodsInfo;
import com.pingo.utils.Constant;

public class CartListViewAdapter extends BaseAdapter {
	private Context context;
	private List<CartGoodsInfo> cartGoodList;
	private onCheckedChanged listener;
	private SwipeListView mSwipeListView;
	private HolderView holderView = null;
	private TextView tv_cart_Allprice;
	private float Allprice_cart;
	// 选中的商品总价
	private float Allprice_select;
	private String mToken;

	public CartListViewAdapter(Context context,List<CartGoodsInfo> cartGoodList, SwipeListView swipeListView,boolean[] is_choice) {
		Allprice_select = 0;
		this.context = context;
		this.cartGoodList = cartGoodList;
		this.mSwipeListView = swipeListView;
		mToken = Constant.Token;// 账户认证码
		mSwipeListView.setDividerHeight(0);
	}

	public CartListViewAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return cartGoodList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return cartGoodList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View currentView, ViewGroup arg2) {
		tv_cart_Allprice = (TextView) mSwipeListView.findViewById(R.id.tv_cart_Allprice);
		holderView = new HolderView();
		View v= LayoutInflater.from(context).inflate(R.layout.adapter_listview_cart, null);
		holderView.tv_num = (TextView) v.findViewById(R.id.tv_number);
		holderView.tv_name = (TextView) v.findViewById(R.id.tv_name);
		holderView.tv_price = (TextView) v.findViewById(R.id.tv_unitprice);
		holderView.cb_choice = (CheckBox) v.findViewById(R.id.cb_choice);
		holderView.bt_numdrop = (Button) v.findViewById(R.id.bt_numdrop);
		holderView.bt_numup = (Button) v.findViewById(R.id.bt_numup);
		holderView.goodsImg = (ImageView) v.findViewById(R.id.iv_goods_pic);
		v.setTag(holderView);
		holderView.tv_num.setText(""+ cartGoodList.get(position).getProductNum());
		holderView.tv_name.setText(""+ cartGoodList.get(position).getProductName());
		holderView.tv_price.setText(""+ cartGoodList.get(position).getPrice());
		if (cartGoodList.get(position).getGoodsImg() != null) {
			holderView.goodsImg.setImageBitmap(cartGoodList.get(position).getGoodsImg());
		}

		holderView.cb_choice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0,
					boolean choice) {
				listener.getChoiceData(position, choice);
				holderView.cb_choice.setChecked(choice);
			}
		});
		
		ImageButton del = (ImageButton) v.findViewById(R.id.id_remove);
		del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSwipeListView.closeAnimate(position);
				getDeleteGoods(context, mToken, cartGoodList.get(position),position);
				mSwipeListView.dismiss(position);
				notifyDataSetChanged();
			}
		});
		
		// 减少商品数目
		holderView.bt_numdrop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View childAt = mSwipeListView.getChildAt(position);
				//等于1不减少
				if (cartGoodList.get(position).getProductNum() >1&&childAt != null) {
					TextView tv_num = (TextView) childAt.findViewById(R.id.tv_number);
					if (tv_num != null) {    // 当item可见的时候更新
						int num = cartGoodList.get(position).getProductNum();
						tv_num.setText("" + (num - 1));
						cartGoodList.get(position).setProductNum(num - 1);
						changeGoodsNum(context,cartGoodList.get(position),position);
						holderView.cb_choice.setChecked(false);
						holderView.cb_choice.setChecked(true);
					}
				}
			}
		});
		// 增加商品数目
		holderView.bt_numup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View childAt = mSwipeListView.getChildAt(position);
				if (childAt != null) {
					TextView tv_num = (TextView) childAt.findViewById(R.id.tv_number);
					if (tv_num != null) {// 当item可见的时候更新
						int num = cartGoodList.get(position).getProductNum();
						tv_num.setText("" + (num + 1));
						cartGoodList.get(position).setProductNum(num + 1);
						holderView.cb_choice.setChecked(false);
						holderView.cb_choice.setChecked(true);
					}
				}
				changeGoodsNum(context,cartGoodList.get(position),position);
			}
		});
		return v;
	}

	public class HolderView {
		private TextView tv_num;
		private CheckBox cb_choice;
		private TextView tv_name;
		private TextView tv_price;
		private Button bt_numdrop, bt_numup;
		private ImageView goodsImg;
	}

	public interface onCheckedChanged {
		public void getChoiceData(int position, boolean isChoice);
	}

	public void setOnCheckedChanged(onCheckedChanged listener) {
		this.listener = listener;
	}

	// 修改服务器中购物车某商品数目
	public void changeGoodsNum(final Context context, final CartGoodsInfo goodsinfo,final int position) {
		int cart_id = goodsinfo.getId();
		final int product_num = goodsinfo.getProductNum();
		String url = Constant.URL+"/cart/update/product_num?token="+Constant.Token+ "&cart_id=" + cart_id + "&product_num="
				+ product_num;
		RequestQueue queue = Volley.newRequestQueue(context);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getString("status").equals("success")) {
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.e("error", error.toString());
					}
				});
		queue.add(jsObjRequest);

	}

	// 从服务器删除某商品
	public void getDeleteGoods(final Context context, final String mToken,final CartGoodsInfo goodsinfo, final int position) {

		int cart_id = goodsinfo.getId();
		String url = Constant.URL+"/cart/delete?token=" + mToken+ "&cart_id=" + cart_id;
		RequestQueue queue = Volley.newRequestQueue(context);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getString("status").equals("success")) {
								mSwipeListView.dismiss(position);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.e("error", "onErrorResponse");
					}
				});
		queue.add(jsObjRequest);
	}

}
