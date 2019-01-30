package org.dian.demonotefinal;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicListAdapter extends BaseAdapter {
	public ContentResolver cr;
	public Cursor mCursor;
	public List<MusicInformation>musicList;
	public MusicInformation mi;
	public Context context;
	public MusicListAdapter(Context context){
		this.context=context;
		GetMusicInfo mGetMusicInfo=new GetMusicInfo(context);
		musicList=mGetMusicInfo.getMusicList();
	}
	
	@Override
	public int getCount() {
		return musicList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return musicList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater li=LayoutInflater.from(context);
		View v=li.inflate(R.layout.layout_musiclist_item,null);
		TextView musicName;
		TextView musicTime;
		TextView musicPath;
		TextView musicSize;
		musicName=(TextView) v.findViewById(R.id.item_musiclist_tv_name);
		musicTime=(TextView) v.findViewById(R.id.item_musiclist_tv_time);
		musicPath=(TextView) v.findViewById(R.id.item_musiclist_tv_path);
		musicSize=(TextView) v.findViewById(R.id.item_musiclist_tv_size);
		musicName.setText(musicList.get(position).getMusicName());
		musicTime.setText(musicList.get(position).getMusicTime());
		musicPath.setText(musicList.get(position).getMusicPath());
		musicSize.setText(musicList.get(position).getMusicSize()+"MB");
		return v;
	}

}
