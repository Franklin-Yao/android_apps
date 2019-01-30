package com.pingo.activity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.smssdk.SMSSDK;

import com.pingo.adapter.ViewPagerAdapter;
import com.pingo.model.Data;
import com.pingo.utils.Constant;
import com.pingo.utils.IBtnCallListener;
import com.pingo.utils.NetInfo;

/**
 * 整个程序最底层的框架Activity，所有的Fragment都是依赖于此Activity而存在的
 * 
 */
public class MainActivity extends FragmentActivity  implements OnClickListener,IBtnCallListener {

	public static MainActivity instance = null;
	private NotificationManager nm;
	private ViewPager mTabPager;
	private ImageView mTabImg, IvAvatar;
	private ImageView mTab1,mTab2,mTab3,mTab4,mTab5;
	private int imageIds[];
	private int pingoNum;
    private String[] titles;
    private ArrayList<ImageView> images;
    private ArrayList<View> dots;
    private TextView title,pingoHint;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private int oldPosition = 0;//记录上一次点的位置
    private int currentItem; //当前页面
    private SharedPreferences sp=Constant.sp;
    private ScheduledExecutorService scheduledExecutorService;
    
	// 界面底部的菜单按钮
	private ImageView[] iv_menu = new ImageView[3];
	private LinearLayout[] bt_menu = new LinearLayout[3];
	private TextView[] tv_menu = new TextView[3];
	// 界面底部的菜单按钮id
	private int[] iv_menu_id = { R.id.tab_home, R.id.tab_cart,R.id.tab_user };
	private int[] bt_menu_id = { R.id.view_tab_home, R.id.view_tab_cart,R.id.view_tab_user };
	private int[] tv_menu_id = { R.id.tv_home, R.id.tv_cart,R.id.tv_user };
	// 界面底部的选中菜单按钮资源
	private int[] select_on = { R.drawable.activity_main_home_selected,R.drawable.activity_main_cart_selected, R.drawable.activity_main_user_selected };
	// 界面底部的未选中菜单按钮资源
	private int[] select_off = { R.drawable.activity_main_home, R.drawable.activity_main_cart ,R.drawable.activity_main_user };
	/** 主界面 */
	private MainFragmentHome home_F;
	private MainFragmentCart cart_F;
	private MainFragmentUser user_F;
	private IBtnCallListener btnCallListener;
	public static final int REQUEST_CODE_01 = 1;
	public static final int REQUEST_CODE_02 = 2;
	public static final int REQUEST_CODE_03 = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
      //短信SDK初始化
        SMSSDK.initSDK(this, Constant.APPKEY, Constant.APPSECRET);
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);
        Log.e("yao", "onCreate+");
        checkNetState();
        getSaveData();
		initView();
    }
    @Override
    protected void onStart() {
        super.onStart();
        getMessegeService();
    }
    
    public boolean checkNetState() {
    	if(!NetInfo.isNetworkConnected(this)){
    		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
    		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_style1, null);
    		final Dialog dialog = new AlertDialog.Builder(MainActivity.this).create();
    		dialog.setCancelable(false);
    		dialog.show();
    		dialog.getWindow().setContentView(layout);
    		TextView dialog_msg = (TextView) layout.findViewById(R.id.dialog_msg);
    		dialog_msg.setText("网络有问题，您需要去设置网络吗？");
    		Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
			btnOK.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
					Intent intent = new Intent("/");
					intent.setAction(Settings.ACTION_SETTINGS);
					startActivity(intent);
				}
			});
			Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
			btnCancel.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
				}
			});
			return false;
    	}
    	return true;
    }
    private void getMessegeService() {
    	final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent intent = new Intent();
		intent.setAction("com.max.autoStart");
		intent.putExtra("msg", "it is time for you to atend a meeting");
		// 实例化PendingIntent
		final PendingIntent pi = PendingIntent.getBroadcast(this, 0,intent, 0);
		// 获得系统时间
		final long time = System.currentTimeMillis();
		am.setRepeating(AlarmManager.RTC_WAKEUP, time,8 * 1000, pi);
	}
	
	private void getSaveData() {
		/** 得到保存的购物车数据 */  //改成从网络获取
		sp=getSharedPreferences("sp", Context.MODE_PRIVATE);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		int size = sp.getInt("ArrayCart_size", 0);
		for (int i = 0; i < size; i++) {
			hashMap.put("type", sp.getString("ArrayCart_type_" + i, ""));
			hashMap.put("color", sp.getString("ArrayCart_color_" + i, ""));
			hashMap.put("num", sp.getString("ArrayCart_num_" + i, ""));
			Data.arrayList_cart.add(hashMap);
		}
	}
	/** 判断是否已经登录 */ 
	private boolean isLogin() {
		Log.e("yao", "sp1:  "+sp.getString("isLogin", "none"));
    	if(sp.getString("isLogin", "none").equals("yes"))
    		return true;
    	else{
    		return false;
    	}
	}
	/** 添加Fragment **/
	// FragmentTransaction对fragment进行添加,移除,替换,以及执行其他动作
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.add(R.id.show_layout, fragment);
//		ft.commit();
		ft.commitAllowingStateLoss();
	}

	/** 删除Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commit();
		//必须最后调用 commit().
		//如果添加多个fragment到同一个容器, 那么添加的顺序决定了它们在view hierarchy中显示的顺序
	}
	
	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		// 设置Fragment的切换动画
//		ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

		// 判断页面是否已经创建，如果已经创建，那么就隐藏掉
		if (home_F != null) {
			ft.hide(home_F);
		}
		if (cart_F != null) {
			ft.hide(cart_F);
		}
		if (user_F != null) {
			ft.hide(user_F);
		}
		ft.show(fragment);
		ft.commitAllowingStateLoss();
	}
	
	// 初始化组件
	private void initView() {
		// 找到底部菜单的按钮并设置监听
		for (int i = 0; i < iv_menu.length; i++) {
			iv_menu[i] = (ImageView) findViewById(iv_menu_id[i]);
			bt_menu[i] = (LinearLayout) findViewById(bt_menu_id[i]);
			tv_menu[i] = (TextView) findViewById(tv_menu_id[i]);
			bt_menu[i].setOnClickListener(this);
		}
		
//		BadgeView badge = new BadgeView(this, iv_menu[1]);
//		badge.setText("1");
//		badge.show();
		// 初始化默认显示的界面
		if (home_F == null) {
			home_F = new MainFragmentHome();
			addFragment(home_F);
			showFragment(home_F);
		} else {
			showFragment(home_F);
		}
		// 设置默认首页为点击时的图片
		iv_menu[0].setImageResource(select_on[0]);
	}
    
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.view_tab_home:
			// 主界面
			if (home_F == null) {
				home_F = new MainFragmentHome();
				// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
				addFragment(home_F);
				showFragment(home_F);
			} 
			else {
				if (home_F.isHidden()) {
					showFragment(home_F);
				}
			}
			break;
		case R.id.view_tab_cart:
			// 购物车界面
			if(isLogin()){
				if (cart_F != null) {
					removeFragment(cart_F);
					cart_F = null;
				}
				cart_F = new MainFragmentCart();
				// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
				addFragment(cart_F);
				showFragment(cart_F);
			}
			else{
				intent.setClass(this,LoginActivity.class);
				intent.putExtra("action", "");
				startActivityForResult(intent, REQUEST_CODE_03);
			}
			break;
		case R.id.view_tab_user:
			// 我的个人界面
			if(isLogin()){
				if (user_F == null) {
					user_F = new MainFragmentUser();
					// 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
					if (!user_F.isHidden()) {
						addFragment(user_F);
						showFragment(user_F);
					}
				} else {
					if (user_F.isHidden()) {
						showFragment(user_F);
					}
				}
			}
			else{
				intent.setClass(this,LoginActivity.class);
				intent.putExtra("action", "");
				startActivityForResult(intent, REQUEST_CODE_03);
			}
			break;
		}

		// 设置按钮的选中和未选中资源
		for (int i = 0; i < iv_menu.length; i++) {
//			iv_menu[i].setBackgroundResource(select_off[i]);
			iv_menu[i].setImageResource(select_off[i]);
			tv_menu[i].setTextColor(getResources().getColor(R.color.text_color));
			if (v.getId() == bt_menu_id[i]) {
//				iv_menu[i].setBackgroundResource(select_on[i]);
				iv_menu[i].setImageResource(select_on[i]);
				tv_menu[i].setTextColor(getResources().getColor(R.color.text_color_selected));
			}
		}
	}
	//登陆后返回登录之前的页面
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		View v = new View(this);
		if(resultCode==Activity.RESULT_CANCELED){
			v=bt_menu[0];
		}
		else if(requestCode==REQUEST_CODE_01) {
				v=bt_menu[1];
		}
		else if(requestCode==REQUEST_CODE_02) {
			v=bt_menu[2];
		}
		onClick(v);
	}
	
	/** 返回按钮的监听 */
	@Override
	public void onBackPressed() {
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_style1, null);
		final Dialog dialog = new AlertDialog.Builder(MainActivity.this).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);
		TextView dialog_msg = (TextView) layout.findViewById(R.id.dialog_msg);
		dialog_msg.setText("您需要退出吗？");
		Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
		btnOK.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				MainActivity.this.finish();
			}
		});
		Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
		btnCancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
	}

	/** Fragment的回调函数 */
	@Override
	public void onAttachFragment(Fragment fragment) {
		try {
			btnCallListener = (IBtnCallListener) fragment;
		} catch (Exception e) {
		}

		super.onAttachFragment(fragment);
	}
	public void transferMsg() {
		if (home_F == null) {
			home_F = new MainFragmentHome();
			addFragment(home_F);
			showFragment(home_F);
		} else {
			showFragment(home_F);
		}
		iv_menu[3].setImageResource(select_off[3]);
		tv_menu[3].setTextColor(getResources().getColor(R.color.text_color));
		iv_menu[0].setImageResource(select_on[0]);
		tv_menu[0].setTextColor(getResources().getColor(R.color.text_color_selected));
	}
}