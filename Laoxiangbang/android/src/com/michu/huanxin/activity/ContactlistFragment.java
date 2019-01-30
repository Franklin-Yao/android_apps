/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.michu.huanxin.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.easemob.chat.EMContactManager;
import com.michu.huanxin.Constant;
import com.michu.huanxin.DemoApplication;
import com.michu.huanxin.adapter.ContactAdapter;
import com.michu.huanxin.db.InviteMessgeDao;
import com.michu.huanxin.db.UserDao;
import com.michu.huanxin.domain.User;
import com.michu.pingju.widget.AddLaoXiangBangPopupWindow;
import com.michu.pingju.widget.AddLaoXiangPopupWindow;
import com.michu.pingju.activity.R;
import com.michu.pingju.activity.LaoXiangBangFragment;
import com.michu.pingju.activity.LaoXiangFragment;
import com.easemob.exceptions.EaseMobException;

/**
 * 联系人列表页
 * 
 */
public class ContactlistFragment extends Fragment implements OnClickListener {
	private ContactAdapter adapter;
	private List<User> contactList;	
	private boolean hidden;	
	private InputMethodManager inputMethodManager;
	private List<String> blackList;

	/**    2015-04-20    **/
	private EditText search_et;
	private ImageView add_icon_img;
	
	private AddLaoXiangBangPopupWindow add_laoxiangbang_popup;
	private AddLaoXiangPopupWindow add_laoxiang_popup;
	
	private LaoXiangBangFragment laoXiangBang_fragment;
	private LaoXiangFragment laoXiang_fragment;
	private TextView laoXiang_tv;
	private TextView laoXiangBang_tv;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	
	private boolean isShowLaoXiangFragment=true;
	
	private ImageView targetImageBtn;
	private View parentView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		parentView = inflater.inflate(R.layout.fragment_contact_list, container, false);
		targetImageBtn=(ImageView)parentView.findViewById(R.id.iv_new_contact);
		initHeader(parentView);
		initPopupWindow();	
		initFragment();
		return parentView;
	}
		

	private void initHeader(View view)
	{
		search_et=(EditText)view.findViewById(R.id.contact_search_et);
		add_icon_img=(ImageView)view.findViewById(R.id.iv_new_contact);
		add_icon_img.setOnClickListener(this);
		laoXiang_tv=(TextView)view.findViewById(R.id.fragment_contact_laoxiang);
		laoXiangBang_tv=(TextView)view.findViewById(R.id.fragment_contact_laoxiangbang);
		laoXiangBang_tv.setOnClickListener(this);
		laoXiang_tv.setOnClickListener(this);
	}
	
	private void initPopupWindow()
	{		
		add_laoxiangbang_popup = new AddLaoXiangBangPopupWindow(getActivity());		
		add_laoxiang_popup=new AddLaoXiangPopupWindow(getActivity());
		add_laoxiang_popup.setOutsideTouchable(true);
		add_laoxiangbang_popup.setOutsideTouchable(true);
	}
		
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		
		case R.id.iv_new_contact:
			
			if(isShowLaoXiangFragment)
			{
				//老乡fragment				
				if (add_laoxiang_popup.isShowing())
				{
					add_laoxiang_popup.dismiss();
				} 
				else {
					add_laoxiang_popup.popup_laoxiang_lay.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.laoxiang_translate_in));								
					add_laoxiang_popup.showAtLocation(parentView, Gravity.TOP, 0, 0);
					//add_laoxiang_popup.showAsDropDown(parentView);
				}
			}
			else
			{			
				//老乡帮fragment
				if (add_laoxiangbang_popup.isShowing())
				{
					add_laoxiangbang_popup.dismiss();
				} 
				else {
					add_laoxiangbang_popup.add_popup_layout.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.laoxiangbang_translate_in));
					targetImageBtn.getBottom();
					//add_laoxiangbang_popup.showAtLocation(targetImageBtn, Gravity.BOTTOM, 0, 0);
					add_laoxiangbang_popup.showAsDropDown(targetImageBtn);// 显示
				}
			}
			
			break;
		case R.id.fragment_contact_laoxiang:
			transaction = fragmentManager.beginTransaction();
			transaction.hide(laoXiang_fragment).hide(laoXiangBang_fragment);
			transaction.show(laoXiang_fragment).commit();
			isShowLaoXiangFragment=true;
			break;
		case R.id.fragment_contact_laoxiangbang:
			transaction = fragmentManager.beginTransaction();
			transaction.hide(laoXiang_fragment).hide(laoXiangBang_fragment);
			transaction.show(laoXiangBang_fragment).commit();
			isShowLaoXiangFragment=false;
			break;
		default:
			break;
		}
	}
	
	@SuppressLint("Recycle")
	public void initFragment()
	{
		laoXiangBang_fragment=new LaoXiangBangFragment();
		laoXiang_fragment=new LaoXiangFragment();
		fragmentManager=getActivity().getSupportFragmentManager();
		transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.fragment_contact_fragment_container, laoXiang_fragment);
		transaction.add(R.id.fragment_contact_fragment_container, laoXiangBang_fragment);		
		transaction.hide(laoXiang_fragment).hide(laoXiangBang_fragment);
		transaction.show(laoXiang_fragment).commit();		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		
		//黑名单列表
		blackList = EMContactManager.getInstance().getBlackListUsernames();
		contactList = new ArrayList<User>();
		// 获取设置contactlist
		getContactList();
		// 设置adapter		
		/*
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String username = adapter.getItem(position).getUsername();
				if (Constant.NEW_FRIENDS_USERNAME.equals(username)) {
					// 进入申请与通知页面
					User user = DemoApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
					user.setUnreadMsgCount(0);
					startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
				} else if (Constant.GROUP_USERNAME.equals(username)) {
					// 进入群聊列表页面
					startActivity(new Intent(getActivity(), GroupsActivity.class));
				} else {
					// demo中直接进入聊天页面，实际一般是进入用户详情页
					startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", adapter.getItem(position).getUsername()));
				}
			}
		});
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getActivity().getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
*/
		/*ImageView addContactView = (ImageView) getView().findViewById(R.id.iv_new_contact);
		// 进入添加好友页
		addContactView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), AddContactActivity.class));
			}
		});*/
		//---registerForContextMenu(listView);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// 长按前两个不弹menu
		if (((AdapterContextMenuInfo) menuInfo).position > 2) {
			getActivity().getMenuInflater().inflate(R.menu.context_contact_list, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			User tobeDeleteUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除此联系人
			deleteContact(tobeDeleteUser);
			// 删除相关的邀请消息
			InviteMessgeDao dao = new InviteMessgeDao(getActivity());
			dao.deleteMessage(tobeDeleteUser.getUsername());
			return true;
		}else if(item.getItemId() == R.id.add_to_blacklist){
			User user = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			moveToBlacklist(user.getUsername());
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
		}
	}

	/**
	 * 删除联系人
	 * 
	 * @param toDeleteUser
	 */
	public void deleteContact(final User tobeDeleteUser) {
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setMessage("正在删除...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUsername());
					// 删除db和内存中此用户的数据
					UserDao dao = new UserDao(getActivity());
					dao.deleteContact(tobeDeleteUser.getUsername());
					DemoApplication.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							adapter.remove(tobeDeleteUser);
							adapter.notifyDataSetChanged();

						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), "删除失败: " + e.getMessage(), 1).show();
						}
					});

				}

			}
		}).start();

	}

	/**
	 * 把user移入到黑名单
	 */
	private void moveToBlacklist(final String username){
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setMessage("正在移入黑名单...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					//加入到黑名单
					EMContactManager.getInstance().addUserToBlackList(username,false);
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), "移入黑名单成功", 0).show();
							refresh();
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), "移入黑名单失败", 0).show();
						}
					});
				}
			}
		}).start();
		
	}
	
	// 刷新ui
	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					getContactList();
					adapter.notifyDataSetChanged();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取联系人列表，并过滤掉黑名单和排序
	 */
	private void getContactList() {
		contactList.clear();
		//获取本地好友列表
		Map<String, User> users = DemoApplication.getInstance().getContactList();
		Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, User> entry = iterator.next();
			if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(Constant.GROUP_USERNAME)
					&& !blackList.contains(entry.getKey()))
				contactList.add(entry.getValue());
		}
		// 排序
		Collections.sort(contactList, new Comparator<User>() {

			@Override
			public int compare(User lhs, User rhs) {
				return lhs.getUsername().compareTo(rhs.getUsername());
			}
		});

		// 加入"申请与通知"和"群聊"
		contactList.add(0, users.get(Constant.GROUP_USERNAME));
		// 把"申请与通知"添加到首位
		contactList.add(0, users.get(Constant.NEW_FRIENDS_USERNAME));
	}

	
}


