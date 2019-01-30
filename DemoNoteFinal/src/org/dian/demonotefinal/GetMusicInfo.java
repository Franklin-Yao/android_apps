package org.dian.demonotefinal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class GetMusicInfo {
	public ContentResolver cr;
	public Cursor mCursor;
	public List<MusicInformation>musicList;
	public MusicInformation mi;
	public Context context;
	public GetMusicInfo(Context context){
		this.context=context;
		cr=context.getContentResolver();
	}
	public ArrayList<MusicInformation>getMusicList(){
		musicList=new ArrayList<MusicInformation>();
		String[] musicInfo=new String[]{
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DATA
				};
				mCursor=cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,musicInfo,null,null,null);
				if(mCursor!=null){
					mCursor.moveToFirst();
					for(int i=0;i<mCursor.getCount();i++){
						if(mCursor.getString(0).endsWith(".mp3")){
						 mi=new MusicInformation();
						 mi.setMusicName(mCursor.getString(0));
                         mi.setMusicAlbum(mCursor.getString(1));
                         mi.setMusicSinger(mCursor.getString(2));
                         mi.setMusicTime(toTime(mCursor.getInt(3)));
                         mi.setMusicSize(toMB(mCursor.getInt(4)));
                         mi.set_id(mCursor.getInt(5));
                         mi.setMusicPath(mCursor.getString(6));
                         musicList.add(mi);
						}
						mCursor.moveToNext();	
					}
				}
			return	(ArrayList<MusicInformation>)musicList;
		}
	/**
	 * 时间转化处理
	 */
	public static String toTime(int time) {

		time /= 1000;
		int minute = time / 60;
		int second = time % 60;
		minute %= 60;
		return String.format(" %02d:%02d ", minute, second);
	}
	/**
	 * 文件大小转换，将B转换为MB
	 */
	public String toMB(int size) {
		float a = (float) size / (float) (1024 * 1024);
		String b = Float.toString(a);
		int c = b.indexOf(".");
		String fileSize = "";
		fileSize += b.substring(0, c + 2);
		return fileSize;
	}
}
