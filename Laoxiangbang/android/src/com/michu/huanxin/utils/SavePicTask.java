package com.michu.huanxin.utils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.michu.huanxin.interfaces.GlobalTaskNotify;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;

public class SavePicTask extends AsyncTask<String,String,String>{

	private String fileDisStr = "";

	private GlobalTaskNotify taskNotify;
	private String filePathStr = "";
	private Bitmap avatarBm = null;

	private final int FILEPATH = 0;
	private final int BITMAP = 1;

	private int typeOperationInt = -1;

	public SavePicTask(String filePath,GlobalTaskNotify tasknotify){
		this.filePathStr = filePath;
		this.taskNotify = tasknotify;
		typeOperationInt = FILEPATH;
	}

	public SavePicTask(Bitmap bitmap,GlobalTaskNotify tasknotify){
		this.avatarBm = bitmap;
		this.taskNotify = tasknotify;
		typeOperationInt = BITMAP;
	}		

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if(typeOperationInt == FILEPATH){
			if(filePathStr == null || filePathStr.length() == 0){
				return "file path null or zero";
			}
			//加载文件路径到bitmap
//			try {  
//				ExifInterface exifInterface = new ExifInterface(filePathStr);  
//				int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
//				switch (orientation) {  
//				case ExifInterface.ORIENTATION_ROTATE_90:  
////					degree = 90;  
//					break;  
//				case ExifInterface.ORIENTATION_ROTATE_180:  
////					degree = 180;  
//					break;  
//				case ExifInterface.ORIENTATION_ROTATE_270:  
////					degree = 270;  
//					break;  
//				}  
//			} catch (IOException e) {  
//				e.printStackTrace();  
//			}
			avatarBm = BitmapFactory.decodeFile(filePathStr);
		}
		if(avatarBm == null){
			return "bm null";
		}
		//保存avatarbm原图和压缩图片
		
		FileOutputStream foutTakePic= null;
		FileOutputStream foutTakePicOld= null;
		String strPicName = "avatar";
		File fileTakePicPath = new File(Environment.getExternalStorageDirectory().getPath()+"/Michu/Avatar");
		fileTakePicPath.mkdirs();
		String strFilePathHome = fileTakePicPath.getPath()+"/"+strPicName;
		String strBmComPath = strFilePathHome + "_com";
		String strBmOldPath = strFilePathHome + "_old";
		File fInitFile = new File(strBmComPath);
		File fOldImgFile = new File(strBmOldPath);
		if(fInitFile.exists()) fInitFile.delete();
		if(fOldImgFile.exists()) fOldImgFile.delete();
		try {
			foutTakePic = new FileOutputStream(strBmComPath);
			avatarBm.compress(Bitmap.CompressFormat.JPEG, 50, foutTakePic);
			foutTakePicOld = new FileOutputStream(strBmOldPath);
			avatarBm.compress(Bitmap.CompressFormat.JPEG, 100, foutTakePicOld);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				foutTakePic.flush();
				foutTakePic.close();
				foutTakePicOld.flush();
				foutTakePicOld.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//存储图片，用作上传
		
		return strFilePathHome;
	}


	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		taskNotify.TaskEnd(result);
	}



}
