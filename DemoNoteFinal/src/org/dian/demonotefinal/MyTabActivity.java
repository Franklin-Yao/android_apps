package org.dian.demonotefinal;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;


public class MyTabActivity extends TabActivity {
	 private TabHost mTabhost;
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.tab);
	    	mTabhost=this.getTabHost();//从TabActivity上面获取放置Tab的TabHost	        
	    	
	        //专辑列表	        
	        TabSpec albumListSpec=mTabhost.newTabSpec("专辑列表"); 
	        albumListSpec.setIndicator("专辑列表");  
	        Intent albumListIntent = new Intent(this,AlbumListActivity.class);
	        albumListSpec.setContent(albumListIntent);  
	        //音乐列表
	        TabSpec musicListSpec=mTabhost.newTabSpec("音乐列表");  
	        musicListSpec.setIndicator("音乐列表");  
	        Intent musicListIntent = new Intent(this,MusicListActivity.class);
	        musicListSpec.setContent(musicListIntent);  
	        
	        mTabhost.addTab(musicListSpec); 
	        mTabhost.addTab(albumListSpec);  
	        
	        //改变TabWidget默认样式
	        TabWidget mTabWidget = mTabhost.getTabWidget();
	      
	        	mTabWidget.getChildAt(0).getLayoutParams().height = 50; 
	        	mTabWidget.getChildAt(1).getLayoutParams().height = 50; 
	            //修改显示字体大小
	            TextView tv = (TextView) mTabWidget.getChildAt(0).findViewById(android.R.id.title);
	            TextView tv1 = (TextView) mTabWidget.getChildAt(1).findViewById(android.R.id.title);
	            tv.setTextSize(25);
	            tv.setTextColor(this.getResources().getColorStateList(android.R.color.background_light));
	            tv1.setTextSize(25);
	            tv1.setTextColor(this.getResources().getColorStateList(android.R.color.background_light));
	        
    }

}
