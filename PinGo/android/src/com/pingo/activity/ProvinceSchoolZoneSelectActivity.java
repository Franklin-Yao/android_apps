package com.pingo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ProvinceSchoolZoneSelectActivity extends Activity {
		private int ProvinceId,SchoolId,ZoneId;
		private String Province="",School="",Building="";
		
		private int[] school = {R.array.hubei_province_item};
		private int[] BuildingOfHubei = {R.array.huake_school_item};
		private int[][] BuildingTatal={BuildingOfHubei};
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            LayoutInflater mLi = LayoutInflater.from(this);
    		View view= mLi.inflate(R.layout.activity_province_schoolzone_select, null);
    		setContentView(view);
    		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
        	Title.setText("选择省份学校和校区");
        	ImageView IvLeft=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
        	IvLeft.setVisibility(View.GONE);
        	final TextView ProvinceTv=(TextView)findViewById(R.id.tv_province);
        	final TextView SchoolTv=(TextView)findViewById(R.id.tv_school);
        	final TextView ZoneTv=(TextView)findViewById(R.id.tv_zone);
        	final TextView AdressTv=(TextView)findViewById(R.id.tv_address);
        	
        	LinearLayout ProvinceLl=(LinearLayout)findViewById(R.id.ll_province);
        	final LinearLayout SchoolLl=(LinearLayout)findViewById(R.id.ll_school);
        	final LinearLayout ZoneLl=(LinearLayout)findViewById(R.id.ll_zone);
        	ProvinceLl.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					View view=LayoutInflater.from(ProvinceSchoolZoneSelectActivity.this).inflate(R.layout.activity_procince_school_building_dialog, null);
			 		final Dialog dialog = new AlertDialog.Builder(ProvinceSchoolZoneSelectActivity.this).create();
			 		dialog.show();
			 		dialog.setCanceledOnTouchOutside(true);
			 		dialog.getWindow().setContentView(view);
			 		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			 		dialog.getWindow().setGravity(Gravity.CENTER);
			        params.width = 600;
			        params.height = 800;
			        dialog.getWindow().setAttributes(params);
			        ListView listView = (ListView) view.findViewById(R.id.list);
			        Resources res =getResources();
			        final String[] SortType=res.getStringArray(R.array.province_item);
//			        String []SortType={"按销量排序","按折扣排序","按价格排序"};
			        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			        for(int i=0;i<SortType.length;i++){
			        	Map<String, Object> map = new HashMap<String, Object>();
			        	map.put("title", SortType[i]);
			        	data.add(map);
			        }
			        SimpleAdapter adapter = new SimpleAdapter(ProvinceSchoolZoneSelectActivity.this,data,R.layout.activity_first_cat_item_cat,
			                new String[]{"title"},
			                new int[]{R.id.tv_cat_name});
			        listView.setAdapter(adapter);
			       
			        listView.setOnItemClickListener(new OnItemClickListener(){
						public void onItemClick(AdapterView<?> arg0, View arg1, int position,
								long id) {
							ProvinceId=position;
							ProvinceTv.setText(SortType[position]);
							SchoolTv.setText("请选择学校");
							ZoneTv.setText("请选择校区");
							AdressTv.setText("");
							Province=SortType[position];
							dialog.dismiss();
							final String[] SortType=getResources().getStringArray(school[ProvinceId]);
							if(SortType.length>=1)
								SchoolLl.setClickable(true);
							else
								SchoolLl.setClickable(false);
						}
			        });
				}
        	});
        	
        	SchoolLl.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					View view=LayoutInflater.from(ProvinceSchoolZoneSelectActivity.this).inflate(R.layout.activity_procince_school_building_dialog, null);
			 		final Dialog dialog = new AlertDialog.Builder(ProvinceSchoolZoneSelectActivity.this).create();
			 		dialog.show();
			 		dialog.setCanceledOnTouchOutside(true);
			 		dialog.getWindow().setContentView(view);
			 		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			 		dialog.getWindow().setGravity(Gravity.CENTER);
			        params.width = 600;
			        params.height = 800;
			        dialog.getWindow().setAttributes(params);
			        ListView listView = (ListView) view.findViewById(R.id.list);
			        Resources res =getResources();
			        final String[] SortType=res.getStringArray(school[ProvinceId]);
//			        String []SortType={"按销量排序","按折扣排序","按价格排序"};
			        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			        for(int i=0;i<SortType.length;i++){
			        	Map<String, Object> map = new HashMap<String, Object>();
			        	map.put("title", SortType[i]);
			        	data.add(map);
			        }
			        SimpleAdapter adapter = new SimpleAdapter(ProvinceSchoolZoneSelectActivity.this,data,R.layout.activity_first_cat_item_cat,
			                new String[]{"title"},
			                new int[]{R.id.tv_cat_name});
			        listView.setAdapter(adapter);
			       
			        listView.setOnItemClickListener(new OnItemClickListener(){
						public void onItemClick(AdapterView<?> arg0, View arg1, int position,
								long id) {
							SchoolId=position;
							SchoolTv.setText(SortType[position]);
							ZoneTv.setText("请选择建筑");
							School=SortType[position];
							AdressTv.setText("");
							dialog.dismiss();
							final String[] SortType=getResources().getStringArray(BuildingTatal[ProvinceId][SchoolId]);
							if(SortType.length>=1)
								ZoneLl.setClickable(true);
							else
								ZoneLl.setClickable(false);
						}
			        });
				}
        	});
        	
        	ZoneLl.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					View view=LayoutInflater.from(ProvinceSchoolZoneSelectActivity.this).inflate(R.layout.activity_procince_school_building_dialog, null);
			 		final Dialog dialog = new AlertDialog.Builder(ProvinceSchoolZoneSelectActivity.this).create();
			 		dialog.show();
			 		dialog.setCanceledOnTouchOutside(true);
			 		dialog.getWindow().setContentView(view);
			 		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			 		dialog.getWindow().setGravity(Gravity.CENTER);
			        params.width = 600;
			        params.height = 800;
			        dialog.getWindow().setAttributes(params);
			        ListView listView = (ListView) view.findViewById(R.id.list);
			        Resources res =getResources();
			        final String[] SortType=res.getStringArray(BuildingTatal[ProvinceId][SchoolId]);
//			        String []SortType={"按销量排序","按折扣排序","按价格排序"};
			        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			        for(int i=0;i<SortType.length;i++){
			        	Map<String, Object> map = new HashMap<String, Object>();
			        	map.put("title", SortType[i]);
			        	data.add(map);
			        }
			        SimpleAdapter adapter = new SimpleAdapter(ProvinceSchoolZoneSelectActivity.this,data,R.layout.activity_first_cat_item_cat,
			                new String[]{"title"},
			                new int[]{R.id.tv_cat_name});
			        listView.setAdapter(adapter);
			       
			        listView.setOnItemClickListener(new OnItemClickListener(){
						public void onItemClick(AdapterView<?> arg0, View arg1, int position,
								long id) {
							ZoneId=position;
							ZoneTv.setText(SortType[position]);
							Building=SortType[position];
							String adress=Province+School+Building;
							AdressTv.setText(adress);
							dialog.dismiss();
						}
			        });
				}
        	});
        	SchoolLl.setClickable(false);
        	ZoneLl.setClickable(false);
        }
        
        public void onTopLeft(View v) {
        	Intent intent = new Intent();
        	intent.putExtra("province", Province);
        	intent.putExtra("school", School);
        	intent.putExtra("building", Building);
        	setResult(Activity.RESULT_OK, intent);
        	finish();
    	}

    	@Override
    	public boolean onKeyDown(int keyCode, KeyEvent event) {
    		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
    			Intent intent = new Intent();
    			intent.putExtra("province", Province);
            	intent.putExtra("school", School);
            	intent.putExtra("building", Building);
            	setResult(Activity.RESULT_OK, intent);
            	finish();
    			return true;
    		}
    		return super.onKeyDown(keyCode, event);
    	}
}