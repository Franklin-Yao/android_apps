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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.michu.huanxin.Constant;
import com.michu.huanxin.DemoApplication;
import com.michu.huanxin.db.UserDao;
import com.michu.huanxin.domain.User;
import com.michu.huanxin.interfaces.GlobalTaskNotify;
import com.michu.huanxin.task.LoginTask;
import com.michu.huanxin.ui.CircleImageView;
import com.michu.huanxin.utils.CommonUtils;
import com.michu.pingju.activity.R;
import com.michu.pingju.activity.SmsSendActivity;
import com.michu.pingju.utils.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 登陆页面
 * 
 */
public class LoginActivity extends BaseActivity {
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private CircleImageView iv_user_avatar;
	private Button mNewLoginButton;
	private Context cxt;
	private static boolean isServerSideLogin = false;
	private static String mAppid = "1104526854";
	private UserInfo mInfo;

	private boolean progressShow;
	private boolean autoLogin = false;

	private LocationClient locationClient;
	private String longitude,latitude;

	private String[] phoneHead = {"147","150","151","152","153","155","156","157","158","159"
			,"130","131","132","133","134","135","136","137","138","139","180","182","185"
			,"186","187","188","189"};


	//QQ登录
	public static Tencent mTencent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cxt=this;
		// 如果用户名密码都有，直接进入主页面
		if (DemoApplication.getInstance().getUserName() != null && DemoApplication.getInstance().getPassword() != null) {
			autoLogin = true;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			return;
		}
		setContentView(R.layout.activity_login);
		getLocation();

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		iv_user_avatar= (CircleImageView) findViewById(R.id.iv_user_avatar);
		mNewLoginButton = (Button) findViewById(R.id.login_btn_login);

		// 如果用户名改变，清空密码
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	/**
	 * 登陆
	 * 
	 * @param view
	 */
	public void login(View view) {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}


		DemoApplication.currentUserNick = "";

		final String username = usernameEditText.getText().toString();
		final String password = passwordEditText.getText().toString();

		if(username == null ||username.length() != 11){
			Toast.makeText(this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
			return;
		}
		if(password == null ||password.length() == 0){
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		boolean boolNormal = false;
		for(int i = 0;i < phoneHead.length && !boolNormal;i ++){
			if(username.startsWith(phoneHead[i])){
				boolNormal = true;
			}
		}
		if(!boolNormal){
			Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			progressShow = true;
			final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
			pd.setCanceledOnTouchOutside(false);
			pd.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					progressShow = false;
				}
			});
			pd.setMessage("正在登陆...");
			pd.show();
			GlobalTaskNotify taskNotify = new GlobalTaskNotify() {

				@Override
				public void TaskEnd(String resultStr) {
					System.out.println("result--->" + resultStr);
					if(resultStr != null && resultStr.length() > 0){
						try {
							JSONObject resultJson= new JSONObject(resultStr);
							JSONArray dataJsonArray = resultJson.getJSONArray("data");
							JSONObject dataJson = dataJsonArray.getJSONObject(0);
							DemoApplication.pingjuTokenStr = dataJson.getString("token");
							if(resultJson.getString("status").equals("success")){
								// 调用sdk登陆方法登陆聊天服务器
								EMChatManager.getInstance().login(username, password, new EMCallBack() {

									@Override
									public void onSuccess() {
										if (!progressShow) {
											return;
										}
										// 登陆成功，保存用户名密码
										DemoApplication.getInstance().setUserName(username);
										DemoApplication.getInstance().setPassword(password);
										runOnUiThread(new Runnable() {
											public void run() {
												pd.setMessage("正在获取好友和群聊列表...");
											}
										});
										try {
											// ** 第一次登录或者之前logout后，加载所有本地群和回话
											// ** manually load all local groups and
											// conversations in case we are auto login
											EMGroupManager.getInstance().loadAllGroups();
											EMChatManager.getInstance().loadAllConversations();

											// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
											List<String> usernames = EMContactManager.getInstance().getContactUserNames();
											EMLog.d("roster", "contacts size: " + usernames.size());
											Map<String, User> userlist = new HashMap<String, User>();
											for (String username : usernames) {
												User user = new User();
												user.setUsername(username);
												setUserHearder(username, user);
												userlist.put(username, user);
											}
											// 添加user"申请与通知"
											User newFriends = new User();
											newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
											newFriends.setNick("申请与通知");
											newFriends.setHeader("");
											userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
											// 添加"群聊"
											User groupUser = new User();
											groupUser.setUsername(Constant.GROUP_USERNAME);
											groupUser.setNick("群聊");
											groupUser.setHeader("");
											userlist.put(Constant.GROUP_USERNAME, groupUser);

											// 存入内存
											DemoApplication.getInstance().setContactList(userlist);
											// 存入db
											UserDao dao = new UserDao(LoginActivity.this);
											List<User> users = new ArrayList<User>(userlist.values());
											dao.saveContactList(users);

											// 获取群聊列表(群聊里只有groupid和groupname的简单信息),sdk会把群组存入到内存和db中
											EMGroupManager.getInstance().getGroupsFromServer();
										} catch (Exception e) {
											e.printStackTrace();
										}
										boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(DemoApplication.currentUserNick);
										if (!updatenick) {
											EMLog.e("LoginActivity", "update current user nick fail");
										}

										if (!LoginActivity.this.isFinishing())
											pd.dismiss();
										// 进入主页面
										startActivity(new Intent(LoginActivity.this, MainActivity.class));
										finish();
									}

									@Override
									public void onProgress(int progress, String status) {

									}

									@Override
									public void onError(int code, final String message) {

										if (!progressShow) {
											return;
										}
										runOnUiThread(new Runnable() {
											public void run() {
												pd.dismiss();
												Toast.makeText(getApplicationContext(), "登录失败: " + message, 0).show();
											}
										});
									}
								});

							}else{
								pd.dismiss();
								Toast.makeText(LoginActivity.this, "登陆失败--" + resultJson.getString("status"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
					}else{
						pd.dismiss();
						Toast.makeText(LoginActivity.this, "登陆失败--网络请求失败", Toast.LENGTH_SHORT).show();
					}
				}
			};
			Map<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("mobile", username);
			dataMap.put("password", password);
			dataMap.put("longitude", longitude);
			dataMap.put("latitude", latitude);
			LoginTask loginTask = new LoginTask(Constant.LOGIN,dataMap,taskNotify);
			System.out.println("login task begin");
			loginTask.execute();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_SETNICK) {

			}
		}
	}

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		Intent intent = new Intent(LoginActivity.this, SmsSendActivity.class);
		intent.putExtra("action", "register");
		intent.putExtra("phone", ""+usernameEditText.getText());
		startActivity(intent);
		//		startActivityForResult(new Intent(this, SmsSendActivity.class), 0);
	}

	public static void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
			}
		} catch(Exception e) {
		}
	}

	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {
				}

				Handler mHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						if (msg.what == 0) {
							JSONObject response = (JSONObject) msg.obj;
							if (response.has("nickname")) {
								try {
									//									usernameEditText.setVisibility(android.view.View.VISIBLE);
									usernameEditText.setText(response.getString("nickname"));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}else if(msg.what == 1){
							Bitmap bitmap = (Bitmap)msg.obj;
							iv_user_avatar.setImageBitmap(bitmap);
							//							iv_user_avatar.setVisibility(android.view.View.VISIBLE);
						}
					}
				};
				@Override
				public void onComplete(final Object response) {
					Log.e("", "response:  "+response);
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
					new Thread(){

						@Override
						public void run() {
							JSONObject json = (JSONObject)response;
							if(json.has("figureurl")){
								Bitmap bitmap = null;
								try {
									bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
								} catch (JSONException e) {

								}
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						}

					}.start();
				}

				@Override
				public void onCancel() {

				}
			};
			mInfo = new UserInfo(this, mTencent.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
			usernameEditText.setText("");
			//			mUserInfo.setVisibility(android.view.View.GONE);
			//			mUserLogo.setVisibility(android.view.View.GONE);
		}
	}

	private void updateLoginButton() {
		if (mTencent != null && mTencent.isSessionValid()) {
			if (isServerSideLogin) {
				mNewLoginButton.setTextColor(Color.BLUE);
				mNewLoginButton.setText("登录");
				//                mServerSideLoginBtn.setTextColor(Color.RED);
				//                mServerSideLoginBtn.setText("退出Server-Side账号");
			} else {
				mNewLoginButton.setTextColor(Color.RED);
				mNewLoginButton.setText("退出帐号");
				//                mServerSideLoginBtn.setTextColor(Color.BLUE);
				//                mServerSideLoginBtn.setText("Server-Side登陆");
			}
		} else {
			mNewLoginButton.setTextColor(Color.BLUE);
			mNewLoginButton.setText("登录");
			//            mServerSideLoginBtn.setTextColor(Color.BLUE);
			//            mServerSideLoginBtn.setText("Server-Side登陆");
		}
	}

	IUiListener loginListener = new BaseUiListener() {
		protected void doComplete(JSONObject values) {
			Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
			initOpenidAndToken(values);
			updateUserInfo();
			updateLoginButton();
		}
	};

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			if (null == response) {
				Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
				return;
			}
			Util.showResultDialog(LoginActivity.this, response.toString(), "登录成功");
			// 有奖分享处理
			//            handlePrizeShare();
			doComplete((JSONObject)response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			Util.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);
			Util.dismissDialog();
		}

		@Override
		public void onCancel() {
			Util.toastMessage(LoginActivity.this, "onCancel: ");
			Util.dismissDialog();
			if (isServerSideLogin) {
				isServerSideLogin = false;
			}
		}
	}
	public void OnLoginQQ(View view)
	{
		mTencent = Tencent.createInstance(mAppid, this);
		if (!mTencent.isSessionValid()) {
			mTencent.login(this, "all", loginListener);
			isServerSideLogin = false;
			Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
		} else {
			if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
				mTencent.logout(this);
				mTencent.login(this, "all", loginListener);
				isServerSideLogin = false;
				Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
				return;
			}
			mTencent.logout(this);
			updateUserInfo();
			updateLoginButton();
		}
	}

	public void OnLoginWeChat(View view){

	}
	public void OnLoginWeibo(View view){

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}

		if (DemoApplication.getInstance().getUserName() != null) {
			usernameEditText.setText(DemoApplication.getInstance().getUserName());
		}
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	private void getLocation(){
		locationClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);        //是否打开GPS
		option.setCoorType("bd09ll");       //设置返回值的坐标类型。
		option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
		option.setProdName("MichuPingju"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		option.setScanSpan(0);    //设置定时定位的时间间隔。单位毫秒
		locationClient.setLocOption(option);
		System.out.println("get location init");
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceivePoi(BDLocation arg0) {
				// TODO Auto-generated method stub
				System.out.println("onReceivePoi");

			}

			@Override
			public void onReceiveLocation(BDLocation location) {
				System.out.println("onReceiveLocation");
				// TODO Auto-generated method stub
				if (location == null) {
					return;
				}
				StringBuffer sb = new StringBuffer(256);
				sb.append("Time : ");
				sb.append(location.getTime());
				sb.append("\nError code : ");
				sb.append(location.getLocType());
				sb.append("\nLatitude : ");
				sb.append(location.getLatitude());
				latitude = String.valueOf(location.getLatitude());
				sb.append("\nLontitude : ");
				sb.append(location.getLongitude());
				longitude = String.valueOf(location.getLongitude());
				sb.append("\nRadius : ");
				sb.append(location.getRadius());
				if (location.getLocType() == BDLocation.TypeGpsLocation){
					sb.append("\nSpeed : ");
					sb.append(location.getSpeed());
					sb.append("\nSatellite : ");
					sb.append(location.getSatelliteNumber());
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append("\nAddress : ");
					sb.append(location.getAddrStr());
				}
				locationClient.stop();
				System.out.println("longitude --- >" + longitude);
				System.out.println("latitude ---> " + latitude);

			}
		});
		locationClient.start();
	}
}
