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

public class AlbumListAdapter extends BaseAdapter {
	public ContentResolver cr;
	public Cursor mCursor;
	public List<MusicInformation>musicList;
	public MusicInformation mi;
	public Context context;
	public AlbumListAdapter(Context context){
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater li=LayoutInflater.from(context);
		View v=li.inflate(R.layout.layout_albumlist_item,null);
		TextView musicSinger;
		TextView musicAlbum;
		musicSinger=(TextView) v.findViewById(R.id.tv_singer);
		musicAlbum=(TextView) v.findViewById(R.id.tv_album);
		musicSinger.setText(musicList.get(position).getMusicSinger());
		musicAlbum.setText(musicList.get(position).getMusicAlbum());
		return v;
	}

}
