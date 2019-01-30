package com.pingo.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import org.apache.commons.codec.binary.Base64;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class Cache {
	private Context cxt;
	public Cache(Context cxt){
		this.cxt=cxt;
	}
	
	public Object readObject(String key) {
		Object oj=null;
		Constant.sp=cxt.getSharedPreferences("sp", Context.MODE_PRIVATE);
		String productBase64 = Constant.sp.getString(key, "");
		
		//读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
		if(base64==null)
			Log.e("demo", "对象为空");
		//封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		if(bais==null)
			Log.e("demo1", "对象为空");
		try {
			//再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				//读取对象
				oj =  bis.readObject();
				if(oj==null)
					Log.e("demo2", "对象为空");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return oj;
	}

	public void saveObject(String key,Object oj) {
		Constant.sp=cxt.getSharedPreferences("sp", Context.MODE_PRIVATE);
		
		//创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			//创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			
			//将对象写入字节流
			oos.writeObject(oj);

			//将字节流编码成base64的字符窜
			String productBase64 = new String(Base64.encodeBase64(baos
					.toByteArray()));

			Editor editor = Constant.sp.edit();
			editor.putString(key, productBase64);

			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//首先判断SD卡是否插入
	public String getSDPath(){
	            File SDdir=null;
	            boolean sdCardExist=
	Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	            if(sdCardExist){
	                    SDdir=Environment.getExternalStorageDirectory();
	            }
	            if(SDdir!=null){
	                    return SDdir.toString();
	            }
	            else{
	                    return null;
	            }
	    }
	
	public void saveBitmap(String name,Bitmap bitmap){
		//判断是否存在SD卡
		if(getSDPath()==null)
			return;
		File f = new File(Environment.getExternalStorageDirectory().getPath()+"/pingo/"+name+".jpg");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		FileOutputStream fstream;
		
		//判断目录是否存在，不存在创建目录，否则下面不能成功保存文件
		File folder= new File(Environment.getExternalStorageDirectory().getPath()+"/pingo");
		if (!folder.exists()) {
			folder.mkdirs();
          }
		try {
			fstream = new FileOutputStream(f);
			BufferedOutputStream bStream = new BufferedOutputStream(fstream);
			bStream.write(byteArray);
			if (bStream != null) {
				bStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Bitmap getBitmap(String name){
		  Log.e("yao", Environment.getExternalStorageDirectory().getPath()+"/pingo/"+name+".jpg");
		  return BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/pingo/"+name+".jpg");
	}
}
