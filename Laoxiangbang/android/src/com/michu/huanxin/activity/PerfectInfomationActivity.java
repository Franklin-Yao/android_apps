package com.michu.huanxin.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.michu.huanxin.Constant;
import com.michu.huanxin.DemoApplication;
import com.michu.huanxin.db.SQLdm;
import com.michu.huanxin.interfaces.GlobalTaskNotify;
import com.michu.huanxin.task.QiniuTokenTask;
import com.michu.huanxin.task.QiniuUploadTask;
import com.michu.huanxin.task.RegisterTask;
import com.michu.huanxin.utils.BottomPopmenuUtils;
import com.michu.huanxin.utils.GetPinyin;
import com.michu.huanxin.utils.SavePicTask;
import com.michu.pingju.activity.R;
import com.michu.pingju.bean.PlaceItem;
import com.qiniu.android.storage.UpCompletionHandler;

public class PerfectInfomationActivity extends BaseActivity{

	private String userPhoneStr = "",pwdStr = ""
			,userNameStr = "",genderStr = "2",avatarurlStr = ""
			,provinceStr = "",cityStr = "",countyStr = "";

	private String proSelectCodeStr = "";
	private String citySelectCodeStr = "";

	private List<PlaceItem> placeLst;

	private CheckBox maleCkb, femaleCkb;
	private ImageView avatarImg;
	private EditText nickNameEt;
	private TextView provinceTv,cityTv,countyTv;

	private final int TAKEPIC = 0;
	private final int PICKPIC = 1;
	
	private String qiniuTokenStr = "";


	private SQLdm dbm;
	private SQLiteDatabase db;


	private BottomPopmenuUtils menuWindow;

	//asset 数据库



	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		userPhoneStr = bundle.getString("username");
		pwdStr = bundle.getString("password");
		setContentView(R.layout.activity_perfectinfomation);
		initWidget();
		setCheckBox();
		getQiniuToken();
		placeLst = new ArrayList<PlaceItem>();

	}



	public void selectPro(View view){
		if(!placeLst.isEmpty()){
			placeLst.clear();
		}
		try {
			dbm = new SQLdm(PerfectInfomationActivity.this);
			dbm.openDatabase();
			db = dbm.getDatabase();
			String sqlStr = "select * from province";
			Cursor proCur = db.rawQuery(sqlStr, null);
			if(proCur.moveToFirst()){
				int countInt = proCur.getCount();
				for(int i = 0; i < countInt; i++){
					PlaceItem item = new PlaceItem();
					item.codeStr = proCur.getString(proCur.getColumnIndex("code")); 
					byte bytes[] = proCur.getBlob(2); 
					item.nameStr = new String(bytes,"gbk");
					placeLst.add(item);
					proCur.moveToNext();
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int lstCountInt = placeLst.size();
		String[] placeStr = new String[lstCountInt];
		for(int i = 0;i < lstCountInt; i++){
			placeStr[i] = placeLst.get(i).nameStr;
		}

		new AlertDialog.Builder(PerfectInfomationActivity.this).setTitle(getResources().getString(R.string.province))
		.setItems(placeStr, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				provinceStr = placeLst.get(which).nameStr;
				//				provinceStr = provinceStr.substring(0, provinceStr.length() - 2);
				proSelectCodeStr = placeLst.get(which).codeStr;
				provinceStr = provinceStr.trim();
				provinceStr = provinceStr.replaceAll(" ","");
				provinceTv.setText(provinceStr);
				cityTv.setText(getResources().getString(R.string.city));
				countyTv.setText(getResources().getString(R.string.county));
				citySelectCodeStr = "";
				cityStr = "";
				countyStr = "";
			}
		}).show();
	}


	public void selectCity(View view){
		if(!placeLst.isEmpty()){
			placeLst.clear();
		}

		if(proSelectCodeStr.length() != 0){
			try {
				dbm = new SQLdm(PerfectInfomationActivity.this);
				dbm.openDatabase();
				db = dbm.getDatabase();
				String sqlStr = "select * from city where pcode='"+ proSelectCodeStr +"'";
				Cursor proCur = db.rawQuery(sqlStr, null);
				if(proCur.moveToFirst()){
					int countInt = proCur.getCount();
					for(int i = 0; i < countInt; i++){
						PlaceItem item = new PlaceItem();
						item.codeStr = proCur.getString(proCur.getColumnIndex("code")); 
						byte bytes[] = proCur.getBlob(2); 
						item.nameStr = new String(bytes,"gbk");
						placeLst.add(item);
						proCur.moveToNext();
					}
				}

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int lstCountInt = placeLst.size();
			String[] placeStr = new String[lstCountInt];
			for(int i = 0;i < lstCountInt; i++){
				placeStr[i] = placeLst.get(i).nameStr;
			}
			new AlertDialog.Builder(PerfectInfomationActivity.this).setTitle(getResources().getString(R.string.province))
			.setItems(placeStr, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					cityStr = placeLst.get(which).nameStr;
					//					cityStr = cityStr.substring(0, cityStr.length() - 2);
					citySelectCodeStr = placeLst.get(which).codeStr;
					cityStr = cityStr.trim();
					cityStr = cityStr.replaceAll(" ","");
					cityTv.setText(cityStr);
					countyTv.setText(getResources().getString(R.string.county));
					countyStr = "";
				}
			}).show();
		}
	}


	public void selectCounty(View view){
		if(!placeLst.isEmpty()){
			placeLst.clear();
		}
		if(citySelectCodeStr.length() != 0){
			try {
				dbm = new SQLdm(PerfectInfomationActivity.this);
				dbm.openDatabase();
				db = dbm.getDatabase();
				String sqlStr = "select * from district where pcode='"+ citySelectCodeStr +"'";
				Cursor proCur = db.rawQuery(sqlStr, null);
				if(proCur.moveToFirst()){
					int countInt = proCur.getCount();
					for(int i = 0; i < countInt; i++){
						PlaceItem item = new PlaceItem();
						item.codeStr = proCur.getString(proCur.getColumnIndex("code")); 
						byte bytes[] = proCur.getBlob(2); 
						item.nameStr = new String(bytes,"gbk");
						placeLst.add(item);
						proCur.moveToNext();
					}
				}

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int lstCountInt = placeLst.size();
			String[] placeStr = new String[lstCountInt];
			for(int i = 0;i < lstCountInt; i++){
				placeStr[i] = placeLst.get(i).nameStr;
			}
			new AlertDialog.Builder(PerfectInfomationActivity.this).setTitle(getResources().getString(R.string.province))
			.setItems(placeStr, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					countyStr = placeLst.get(which).nameStr;
					//					countyStr = countyStr.substring(0, countyStr.length() - 2);
					countyStr = countyStr.trim();
					countyStr = countyStr.replaceAll(" ","");
					countyTv.setText(countyStr);
				}
			}).show();
		}

	}


	private void initWidget() {
		// TODO Auto-generated method stub
		maleCkb = (CheckBox) findViewById(R.id.activity_perfectinfomation_ckb_male);
		femaleCkb = (CheckBox) findViewById(R.id.activity_perfectinfomation_ckb_female);
		avatarImg = (ImageView) findViewById(R.id.activity_perfectinfomation_img_avatar);
		nickNameEt = (EditText) findViewById(R.id.activity_perfectinfomation_et_nickname);
		provinceTv = (TextView) findViewById(R.id.activity_perfectinfomation_tv_province);
		cityTv = (TextView) findViewById(R.id.activity_perfectinfomation_tv_city);
		countyTv = (TextView) findViewById(R.id.activity_perfectinfomation_tv_county);

		avatarImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menuWindow = new BottomPopmenuUtils(PerfectInfomationActivity.this, itemsOnClick,R.layout.bottom_pic_popmenu);//显示窗口
				menuWindow.showAtLocation(PerfectInfomationActivity.this.findViewById(R.id.activity_perfectinformation_ll_top), 
						Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

			}
		});
	}

	private void setCheckBox(){
		maleCkb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					if(femaleCkb.isChecked()){
						femaleCkb.setChecked(false);
					}
					genderStr = "0";
				}
				else{
					genderStr = "2";
				}
			}
		});
		femaleCkb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					if(maleCkb.isChecked()){
						maleCkb.setChecked(false);
					}
					genderStr = "1";
				}
				else{
					genderStr = "2";
				}
			}
		});
	}

	public void register(View view){
		userNameStr = nickNameEt.getText().toString().trim();
		if(userNameStr == null || userNameStr.length() == 0){
			Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
			nickNameEt.requestFocus();
			return;
		}else if(avatarurlStr == null || avatarurlStr.length() == 0){
			Toast.makeText(this, "请上传头像", Toast.LENGTH_SHORT).show();
			nickNameEt.requestFocus();
			return;
		}
		else if(genderStr.equals("2")){
			Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
			return;
		}
		else if(provinceStr == null || provinceStr.length() == 0){
			Toast.makeText(this, "请选择省份", Toast.LENGTH_SHORT).show();
			return;
		}else if(cityStr == null || cityStr.length() == 0){
			Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show();
			return;
		}else if(countyStr == null || countyStr.length() == 0){
			Toast.makeText(this, "请选择区县", Toast.LENGTH_SHORT).show();
			return;
		}
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("正在注册...");
		pd.show();
		GlobalTaskNotify taskNotify = new GlobalTaskNotify() {

			@Override
			public void TaskEnd(String resultStr) {
				// TODO Auto-generated method stub
				System.out.println("task end");
				if(resultStr != null && resultStr.length() > 0){

					if(pd.isShowing())
						pd.dismiss();

					try {
						JSONObject resultJson= new JSONObject(resultStr);
						if(resultJson.getString("status").equals("success")){
							DemoApplication.getInstance().setUserName(userPhoneStr);
							Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
							finish();
						}else{
							Toast.makeText(PerfectInfomationActivity.this, "注册失败--" + resultJson.getString("status"), Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//								if (!PerfectInfomationActivity.this.isFinishing())
					//									pd.dismiss();
					// 保存用户名

				}else{
					Toast.makeText(PerfectInfomationActivity.this, "注册失败--网络请求失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
		String proPinStr = getFullPinYin(provinceStr);
		String cityPinStr = getFullPinYin(cityStr);
		String countyPinStr = getFullPinYin(countyStr);
		cityPinStr += "_" + proPinStr;
		countyPinStr += "_" + proPinStr;
		System.out.println("pro--->" + proPinStr);
		System.out.println("city--->" + cityPinStr);
		System.out.println("county--->" + countyPinStr);
		Map<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("mobile", userPhoneStr);
		dataMap.put("password", pwdStr);
		dataMap.put("uname", userNameStr);
		dataMap.put("gender", genderStr);
		dataMap.put("avatar_url", avatarurlStr);
		dataMap.put("province", proPinStr);
		dataMap.put("city", cityPinStr);
		dataMap.put("county", countyPinStr);
		try {
			EMChatManager.getInstance().createAccountOnServer(userPhoneStr,pwdStr);
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RegisterTask registerTask = new RegisterTask(Constant.REGISTER, dataMap, taskNotify);
		registerTask.execute();
	}


	protected void registeHuanxin(ProgressDialog pdin) {
		// TODO Auto-generated method stub
		final ProgressDialog pd = pdin;
		new Thread(new Runnable() {
			public void run() {
				try {
					// 调用sdk注册方法
					EMChatManager.getInstance().createAccountOnServer(userNameStr, pwdStr);
					runOnUiThread(new Runnable() {
						public void run() {
							if(pd.isShowing())
								pd.dismiss();
							// 保存用户名
							DemoApplication.getInstance().setUserName(userNameStr);
							Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
							finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							if (!PerfectInfomationActivity.this.isFinishing())
								pd.dismiss();
							if (e != null && e.getMessage() != null) {
								String errorMsg = e.getMessage();
								if (errorMsg.indexOf("EMNetworkUnconnectedException") != -1) {
									Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
								} else if (errorMsg.indexOf("conflict") != -1) {
									Toast.makeText(getApplicationContext(), "用户已存在！", Toast.LENGTH_SHORT).show();
								}/* else if (errorMsg.indexOf("not support the capital letters") != -1) {
									Toast.makeText(getApplicationContext(), "用户名不支持大写字母！", 0).show();
								} */else {
									Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
								}

							} else {
								Toast.makeText(getApplicationContext(), "注册失败: 未知异常", Toast.LENGTH_SHORT).show();

							}
						}
					});
				}
			}
		}).start();
	}


	public void back(View view) {
		finish();
	}

	public String getFullPinYin(String source){
		String resultStr = GetPinyin.getPingYin(source);
		return resultStr;
	}


	OnClickListener itemsOnClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.bottom_pic_popmenu_tv_takephoto:
				Intent intentTakePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intentTakePic, TAKEPIC);
				break;
			case R.id.bottom_pic_popmenu_tv_pickphoto:
				Intent intentPickPic = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intentPickPic, PICKPIC);
				break;
			default:
				menuWindow.dismiss();
				break;
			}
		}
	};

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(qiniuTokenStr == null || qiniuTokenStr == ""){
			System.out.println("token null");
			return;
		}
		switch(requestCode){
		case TAKEPIC:
			if(resultCode == Activity.RESULT_OK && data != null){
				String sdState=Environment.getExternalStorageState();
				if(!sdState.equals(Environment.MEDIA_MOUNTED)){
					return;
				}
				Bundle bundle = data.getExtras();
				Bitmap bmNewHeadPic = (Bitmap)bundle.get("data");//获取相机返回的数据，并转换为图片格式
				avatarImg.setImageBitmap(bmNewHeadPic);
				SimpleDateFormat dateFormat24 = new SimpleDateFormat("yyyyHHmmMMssdd");
				final String key = "michu_useravatar_" + userPhoneStr + "_" + 
						dateFormat24.format(Calendar.getInstance().getTime());;
				SavePicTask savePicTask = new SavePicTask(bmNewHeadPic, new GlobalTaskNotify() {
					
					@Override
					public void TaskEnd(String resultStr) {
						// TODO Auto-generated method stub
						if(resultStr != null &&!resultStr.contains("null")){
							String[] acatar = {resultStr + "_com",resultStr + "_old"};
							String[] keyStr = {key + "_com",key + "_old"};
							QiniuUploadTask qiniuTask = new QiniuUploadTask(acatar, keyStr, qiniuTokenStr, new UpCompletionHandler() {

								@Override
								public void complete(String arg0, com.qiniu.android.http.ResponseInfo arg1, JSONObject arg2) {
									// TODO Auto-generated method stub
									System.out.println("key-->" + arg0);
									avatarurlStr = Constant.QINIU_AVATARHOME + arg0;
									avatarurlStr = avatarurlStr.substring(0, avatarurlStr.length() - 4);
									System.out.println("avatarurlStr-->" + avatarurlStr);
								}

							});
							qiniuTask.execute();
						}
					}
				});
				savePicTask.execute();
			}
			break;
		case PICKPIC:
			if(resultCode == Activity.RESULT_OK && data != null){
				Uri dataUri = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				SimpleDateFormat dateFormat24 = new SimpleDateFormat("yyyyHHmmMMssdd");
				final String key = "michu_useravatar_" + userPhoneStr + "_" + 
						dateFormat24.format(Calendar.getInstance().getTime());;
						Cursor cur = this.getContentResolver().query(dataUri, filePathColumn, null, null, null);
						cur.moveToFirst();
						String avatarPathStr = cur.getString(cur.getColumnIndex(filePathColumn[0]));
						cur.close();
						cur = null;
						avatarImg.setImageURI(dataUri);
						SavePicTask savePicTask = new SavePicTask(avatarPathStr, new GlobalTaskNotify() {
							
							@Override
							public void TaskEnd(String resultStr) {
								// TODO Auto-generated method stub
								if(resultStr != null &&!resultStr.contains("null")){
									String[] acatar = {resultStr + "_com",resultStr + "_old"};
									String[] keyStr = {key + "_com",key + "_old"};
									QiniuUploadTask qiniuTask = new QiniuUploadTask(acatar, keyStr, qiniuTokenStr, new UpCompletionHandler() {

										@Override
										public void complete(String arg0, com.qiniu.android.http.ResponseInfo arg1, JSONObject arg2) {
											// TODO Auto-generated method stub
											System.out.println("key-->" + arg0);
											avatarurlStr = Constant.QINIU_AVATARHOME + arg0;
											avatarurlStr = avatarurlStr.substring(0, avatarurlStr.length() - 4);
											System.out.println("avatarurlStr-->" + avatarurlStr);
										}

									});
									qiniuTask.execute();
								}
							}
						});
						savePicTask.execute();
			}
			break;

		default:
			break;
		}
	}



	private Void getQiniuToken() {
		// TODO Auto-generated method stub
		QiniuTokenTask qiniuToken = new QiniuTokenTask(new GlobalTaskNotify() {
			
			@Override
			public void TaskEnd(String resultStr) {
				// TODO Auto-generated method stub
				try {
					JSONObject json = new JSONObject(resultStr);
					qiniuTokenStr = json.getString("qiniu_token");
					System.out.println("token--->" + qiniuTokenStr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		qiniuToken.execute();
		return null;
	}

}
