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
	    	mTabhost=this.getTabHost();//��TabActivity�����ȡ����Tab��TabHost	        
	    	
	        //ר���б�	        
	        TabSpec albumListSpec=mTabhost.newTabSpec("ר���б�"); 
	        albumListSpec.setIndicator("ר���б�");  
	        Intent albumListIntent = new Intent(this,AlbumListActivity.class);
	        albumListSpec.setContent(albumListIntent);  
	        //�����б�
	        TabSpec musicListSpec=mTabhost.newTabSpec("�����б�");  
	        musicListSpec.setIndicator("�����б�");  
	        Intent musicListIntent = new Intent(this,MusicListActivity.class);
	        musicListSpec.setContent(musicListIntent);  
	        
	        mTabhost.addTab(musicListSpec); 
	        mTabhost.addTab(albumListSpec);  
	        
	        //�ı�TabWidgetĬ����ʽ
	        TabWidget mTabWidget = mTabhost.getTabWidget();
	      
	        	mTabWidget.getChildAt(0).getLayoutParams().height = 50; 
	        	mTabWidget.getChildAt(1).getLayoutParams().height = 50; 
	            //�޸���ʾ�����С
	            TextView tv = (TextView) mTabWidget.getChildAt(0).findViewById(android.R.id.title);
	            TextView tv1 = (TextView) mTabWidget.getChildAt(1).findViewById(android.R.id.title);
	            tv.setTextSize(25);
	            tv.setTextColor(this.getResources().getColorStateList(android.R.color.background_light));
	            tv1.setTextSize(25);
	            tv1.setTextColor(this.getResources().getColorStateList(android.R.color.background_light));
	        
    }

}
