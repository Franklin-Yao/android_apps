package org.dian.demonotefinal;

import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class PlayerEngine {

	private MediaPlayer mMediaPlayer;
	private Context mContext;
	public static int musicId=0;
	public GetMusicInfo mMusicInfo;
	public List<MusicInformation>musicList;

	public PlayerEngine(Context context,int musicID) {
		mContext = context;
		musicId=musicID;
		mMusicInfo=new GetMusicInfo(mContext);
		musicList=mMusicInfo.getMusicList();
        initMediaSource(initMusicUri(musicId));
	}
	
	public void play() {
		if (mMediaPlayer == null) {
			musicList=mMusicInfo.getMusicList();
			initMediaSource(initMusicUri(PlayerActivity.mId));
		}
		mMediaPlayer.start();
	}
	
	public void pause() {
		if (mMediaPlayer != null) {
			mMediaPlayer.pause();
		}
	}
	public void stop() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
		}
	}
	public void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	
	public int getDuration() {
		if (mMediaPlayer != null) {
			return mMediaPlayer.getDuration();
		}
		return -1;
	}
	
	public int getCurrentPosition() {
		if (mMediaPlayer != null) {
			return mMediaPlayer.getCurrentPosition();
		}
		return -1;
	}
	
	public void seekTo(int msec) {
		if (mMediaPlayer != null) {
			mMediaPlayer.seekTo(msec);
		}
	}
	/**
	 * 返回列表第几行的歌曲路径
	 * @param _id 表示歌曲序号，从0开始
	 */
	public String initMusicUri(int _id) {
		musicId = _id;
		String s;
		// 判断列表和列表长度不为空时才获取
		if (musicList != null && musicList.size() != 0) {
			s = musicList.get(_id).getMusicPath();
			return s;
		} else {
			// 否则返回空字符串
			return "";
		}
	}
	
	/**
	 * 初始化媒体对象
	 * @param mp3Path mp3路径
	 */
	public void initMediaSource(String mp3Path) {
		Uri mp3Uri = Uri.parse(mp3Path);
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.reset();
			mMediaPlayer = null;
		}
		// 为myMediaPlayer创建对象
		mMediaPlayer = MediaPlayer.create(mContext, mp3Uri);

	}	
}
