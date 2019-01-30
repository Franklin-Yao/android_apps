package org.dian.demonotefinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class AlbumListActivity extends Activity {
	public GridView mGirdViewAlbumList;
	public AlbumListAdapter mAlbumListAdapter;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		mGirdViewAlbumList=(GridView)findViewById(R.id.album_gv);
		mAlbumListAdapter=new AlbumListAdapter(this);
		mGirdViewAlbumList.setAdapter(mAlbumListAdapter);
		mGirdViewAlbumList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it=new Intent(AlbumListActivity.this,PlayerActivity.class);
				it.putExtra("musicId", arg2);
				startActivity(it);
			}
	});
}
}
