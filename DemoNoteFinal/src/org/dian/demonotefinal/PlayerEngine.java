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
	 * �����б�ڼ��еĸ���·��
	 * @param _id ��ʾ������ţ���0��ʼ
	 */
	public String initMusicUri(int _id) {
		musicId = _id;
		String s;
		// �ж��б���б��Ȳ�Ϊ��ʱ�Ż�ȡ
		if (musicList != null && musicList.size() != 0) {
			s = musicList.get(_id).getMusicPath();
			return s;
		} else {
			// ���򷵻ؿ��ַ���
			return "";
		}
	}
	
	/**
	 * ��ʼ��ý�����
	 * @param mp3Path mp3·��
	 */
	public void initMediaSource(String mp3Path) {
		Uri mp3Uri = Uri.parse(mp3Path);
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.reset();
			mMediaPlayer = null;
		}
		// ΪmyMediaPlayer��������
		mMediaPlayer = MediaPlayer.create(mContext, mp3Uri);

	}	
}
