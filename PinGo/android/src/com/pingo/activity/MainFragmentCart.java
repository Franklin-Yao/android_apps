package com.pingo.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.pingo.adapter.CartListViewAdapter;
import com.pingo.adapter.CartListViewAdapter.onCheckedChanged;
import com.pingo.model.CartGoodsInfo;
import com.pingo.net.FragmentCartData;
import com.pingo.utils.Cache;
import com.pingo.utils.Constant;
import com.pingo.utils.IBtnCallListener;
import com.pingo.utils.ImageDownLoadUtil;
import com.pingo.utils.Md5Util;
import com.pingo.utils.ImageDownLoadUtil.ICallBack;
import com.pingo.widget.MyDialog;

public class MainFragmentCart extends Fragment implements IBtnCallListener,onCheckedChanged, OnClickListener {
	IBtnCallListener btnCallListener;
	private RequestQueue mQueue;
	// 去逛逛，合计，结算
	private TextView tv_goShop, tv_cart_Allprice, tv_cart_buy_Ordel;
	// 购物车没货物的布局
	private LinearLayout ll_cart,ll_address;
	// 购物车的listview
	private SwipeListView swipeListView;
	// 全选
	private CheckBox cb_cart_all;
	private CartListViewAdapter adapter;
	private boolean[] is_choice;
	// 购物车商品清单
	private List<CartGoodsInfo> cartGoodList;
	// 账户的认证码
	String mToken;
	private Dialog dlg;
	private ScrollView scrollView;
	// 购物车里勾选商品的总价格
	float Allprice_cart;
	private View view;
	public MainFragmentCart() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Allprice_cart = 0;
		mQueue = Volley.newRequestQueue(getActivity());
		view = LayoutInflater.from(getActivity()).inflate(R.layout.cart_all_f2, null);
		 tv_cart_Allprice = (TextView) view.findViewById(R.id.tv_cart_Allprice);
		initView(view);
		return view;
	}

	private void initView(View view) {
		tv_goShop = (TextView) view.findViewById(R.id.tv_goShop);
		tv_cart_buy_Ordel = (TextView) view.findViewById(R.id.tv_cart_buy_or_del);
//		ll_address=(LinearLayout) view.findViewById(R.id.address);
		// tv_cart_buy_Ordel.setText(str_del);
		// 设置滚动条到顶部
		scrollView = (ScrollView) view.findViewById(R.id.scroll_list);
		scrollView.smoothScrollTo(0, 0);
		// 全选
		cb_cart_all = (CheckBox) view.findViewById(R.id.cb_cart_all);
		swipeListView = (SwipeListView) view.findViewById(R.id.listView_cart);
		cb_cart_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				/*
				 * 判断一：（全选按钮选中）全选按钮是否选择，如果选择，那么列表每一行都选中
				 * 判断二：（全选按钮取消）当取消全选按钮时，会有两种情况
				 * ，第一：主动点击全选按钮，此时直接取消列表所有的选中状态，第二：取消列表某一行，导致全选取消，此时列表其他行仍然是选中
				 * 
				 * 判断二的分析：（主动取消）判断列表每一行的选中状态，如果全部都是选中状态，那么（列表选中数=列表总数），此时属于主动取消，
				 * 则取消所有行的选中状态，反之（被动取消）则不响应
				 */
				// 记录列表每一行的选中状态数量
				int isChoice_all = 0;

				if (arg1) {
					// 设置全选
					for (int i = 0; i < cartGoodList.size(); i++) {
						// 如果选中了全选，那么就将列表的每一行都选中
						((CheckBox) (swipeListView.getChildAt(i))
								.findViewById(R.id.cb_choice)).setChecked(true);
					}
				} else {
					// 设置全部取消
					for (int i = 0; i < cartGoodList.size(); i++) {
						// 判断列表每一行是否处于选中状态，如果处于选中状态，则计数+1
						if (((CheckBox) (swipeListView.getChildAt(i)).findViewById(R.id.cb_choice)).isChecked()) {
							// 计算出列表选中状态的数量
							isChoice_all += 1;
						}
					}
					// 判断列表选中数是否等于列表的总数，如果等于，那么就需要执行全部取消操作
					if (isChoice_all == cartGoodList.size()) {
						// 如果没有选中了全选，那么就将列表的每一行都不选
						for (int i = 0; i < cartGoodList.size(); i++) {
							// 列表每一行都取消
							((CheckBox) (swipeListView.getChildAt(i)).findViewById(R.id.cb_choice)).setChecked(false);
						}
					}
				}
			}

		});

		// 购物车没货物的linearlayout布局
		ll_cart = (LinearLayout) view.findViewById(R.id.ll_cart);

		swipeListView.setOffsetLeft(this.getResources().getDisplayMetrics().widthPixels * 2 / 3);
		// 这里获取账户的购物车列表
		getPullCartGoodsList(getActivity(), Constant.Token, this);

		tv_cart_buy_Ordel.setOnClickListener(this);
		tv_goShop.setOnClickListener(this);
//		ll_address.setOnClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		btnCallListener = (IBtnCallListener) activity;
		super.onAttach(activity);
	}

	@Override
	public void transferMsg() {
		// 这里响应在FragmentActivity中的控件交互
		System.out.println("由Activity中传送来的消息");
	}
	
	/** adapter的回调函数，当点击CheckBox的时候传递点击位置和checkBox的状态 */
	@Override
	public void getChoiceData(int position, boolean isChoice) {
		// 记录列表处于选中状态的数量
		int num_choice = 0;
		Allprice_cart=0;
		for (int i = 0; i < cartGoodList.size(); i++) {
			// 判断列表中每一行的选中状态，如果是选中，计数加1
			if (null != swipeListView.getChildAt(i)&& ((CheckBox) (swipeListView.getChildAt(i)).findViewById(R.id.cb_choice)).isChecked()) {
				// 列表处于选中状态的数量+1
				num_choice += 1;
				Allprice_cart+=cartGoodList.get(i).getProductNum()* cartGoodList.get(i).getPrice();
				is_choice[i] = true;
			}
		}
		// 判断列表中的CheckBox是否全部选择
		if (num_choice == cartGoodList.size()) {
			// 如果选中的状态数量=列表的总数量，那么就将全选设置为选中
			cb_cart_all.setChecked(true);
		} else {
			cb_cart_all.setChecked(false);
		}
		tv_cart_Allprice.setText("￥" + Allprice_cart + "");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_goShop:// 点击去逛逛
			btnCallListener.transferMsg();
			break;
		case R.id.tv_cart_buy_or_del:// 点击下单
			if(tv_cart_Allprice.getText().toString().equals("0")){
				Toast.makeText(getActivity(), "您还没有选择物品呢", Toast.LENGTH_SHORT).show();
				break;
			}
			List<CartGoodsInfo> orderGoodsList = new ArrayList<CartGoodsInfo>();
			for (int i = 0; i < cartGoodList.size(); i++) {
				// 判断列表中每一行的选中状态，如果是选中，计数加1
				if (is_choice[i]) {
					orderGoodsList.add(cartGoodList.get(i));
				}
			}
			Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("goodslist",(ArrayList<CartGoodsInfo>) orderGoodsList);
			bundle.putString("PriceAll", tv_cart_Allprice.getText().toString());
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/** 删除数组中的一个元素 */
	public static boolean[] deleteByIndex(boolean[] array, int index) {
		boolean[] newArray = new boolean[array.length - 1];
		for (int i = 0; i < newArray.length; i++) {
			if (i < index) {
				newArray[i] = array[i];
			} else {
				newArray[i] = array[i + 1];
			}
		}
		return newArray;
	}

	public void getPullCartGoodsList(final Context context,
			final String mToken, final onCheckedChanged listener) {
		dlg=MyDialog.createLoadingDialog(getActivity(),"小卓去服务器取东西了，马上回来");
    	dlg.show();
		String url = Constant.URL+"/cart/list?token=" + mToken;
		RequestQueue queue = Volley.newRequestQueue(context);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						dlg.dismiss();
						Log.e("yao", "listresData:" + response);
						try {
							if (response.getString("status").equals("success")) {
								LinearLayout ll_pay=(LinearLayout) view.findViewById(R.id.ll_pay);
								ll_pay.setVisibility(View.VISIBLE);
								JSONArray jos=response.getJSONArray("data");
								cartGoodList = new ArrayList<CartGoodsInfo>();
								for (int i = 0; i < jos.length(); i++) {
									JSONObject jobj = (JSONObject) jos.get(i);
									try {
										CartGoodsInfo goodsInfo = new CartGoodsInfo();
										goodsInfo.setId(jobj.getInt("id"));
										goodsInfo.setGoodsId(jobj.getInt("goods_id"));
										goodsInfo.setProductId(jobj.getInt("product_id"));
										goodsInfo.setProductName(jobj.getString("product_name"));
										goodsInfo.setProductDsec(jobj.getString("pdt_desc"));
										goodsInfo.setProductNum(jobj.getInt("product_num"));
										goodsInfo.setPrice(jobj.getDouble("price"));
										cartGoodList.add(goodsInfo);
										Log.e("wxmarr", ""+ cartGoodList.get(i).getProductName());
										// 获取商品图片
										getPullProductImg(context,cartGoodList.get(i), i);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								
								if (cartGoodList != null&& cartGoodList.size() != 0) {
									adapter = new CartListViewAdapter(getActivity(), cartGoodList,swipeListView,is_choice);
									adapter.setOnCheckedChanged(listener);
									swipeListView.setAdapter(adapter);
									fixListViewHeight(swipeListView);
									is_choice = new boolean[cartGoodList.size()];
									ll_cart.setVisibility(View.GONE);
								} else {
									ll_cart.setVisibility(View.VISIBLE);
								}
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

	// 获取商品图片
	public void getPullProductImg(final Context context,final CartGoodsInfo goodsinfo, final int position) {
		int goods_id = goodsinfo.getGoodsId();
		String url = Constant.URL+"//goods/getGoodsInfo?goods_id="+ goods_id;
		RequestQueue queue = Volley.newRequestQueue(context);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if (response.getString("status").equals("success")) {
								Log.e("picstatus", "picstatus:" + response);
								final RequestQueue mQueue;
								mQueue = Volley.newRequestQueue(context);
								// 获取反馈json数据
								String imgInfo = new String();
								imgInfo = response.getString("data");
								// 获取商品数组
								JSONArray jarr = new JSONArray(imgInfo);
								JSONObject jobj = (JSONObject) jarr.get(0);
								goodsinfo.setProductName(jobj.getString("name"));
								View childAt = swipeListView.getChildAt(position);
								if (childAt != null) {
									TextView tv_name = (TextView) childAt
											.findViewById(R.id.tv_name);
									if (tv_name != null) {// 当item可见的时候更新
										tv_name.setText(goodsinfo
												.getProductName());
									}
								}
								String ImgURL = jobj.getString("pic");
								final View childAt1 = swipeListView.getChildAt(position);
								
								//判断是否存在缓存
								final String picName=Md5Util.getMD5Str(ImgURL);
								Bitmap bmp=new Cache(getActivity()).getBitmap(picName);
								if(bmp!=null){
									Log.e("yao", "购物车图片存在");
									goodsinfo.setGoodsImg(bmp);
									if (childAt1 != null) {
										ImageView iv_goodspic = (ImageView) childAt1.findViewById(R.id.iv_goods_pic);
										if (iv_goodspic != null) {// 当item可见的时候更新
											iv_goodspic.setImageBitmap(bmp);
										}
									}
								}
								else
								new ImageDownLoadUtil().readBitmapViaVolley1(
										mQueue, ImgURL, new ICallBack() {
											@Override
											public void onSuccess(Bitmap bp) {
												goodsinfo.setGoodsImg(bp);
												new Cache(getActivity()).saveBitmap(picName, bp);
												if (childAt1 != null) {
													ImageView iv_goodspic = (ImageView) childAt1.findViewById(R.id.iv_goods_pic);
													if (iv_goodspic != null) {// 当item可见的时候更新
														iv_goodspic.setImageBitmap(bp);
													}
												}
											}
											public void onFailure() {
											}
										});
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
		public void fixListViewHeight(SwipeListView listView) {
			// 如果没有设置数据适配器，则ListView没有子项，返回。
			ListAdapter listAdapter = listView.getAdapter();
			int totalHeight = 0;
			if (listAdapter == null) {
				return;
			}
			for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
				View listViewItem = listAdapter.getView(index, null, listView);
				// 计算子项View 的宽高
				listViewItem.measure(0, 0);
				// 计算所有子项的高度和
				totalHeight += listViewItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			// listView.getDividerHeight()获取子项间分隔符的高度
			// params.height设置ListView完全显示需要的高度
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount()));
			listView.setLayoutParams(params);
		}
}