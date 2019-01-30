package org.dian.demonotefinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MusicListActivity extends Activity {
	public ListView  mListViewMusicList;
	public MusicListAdapter mMusicListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musiclist);
		mListViewMusicList=(ListView)findViewById(R.id.musiclist_activity_lv_musiclist);
		mMusicListAdapter=new MusicListAdapter(this);
		mListViewMusicList.setAdapter(mMusicListAdapter);
		mListViewMusicList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent it=new Intent(MusicListActivity.this,PlayerActivity.class);
				it.putExtra("musicId", arg2);
				startActivity(it);
			}
	});
}
}
