package com.pingo.utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;

public class Constant {
	public static String URL="http://pingo.aliapp.com";
	public static String Token="";
	public static String [] cat={"休闲零食","奶酒饮料","文具用品","洗护美肤","家居清洁"};
	public static String [] cat1={"雪糕|冰激凌","蜜饯果干","坚果炒货","糖果|巧克力","饼干糕点","肉类|豆制品","方便食品"};
	public static String [] cat2={"牛奶乳品","果汁|饮料","饮用水","酒","茶冲咖啡"};
	public static String [] cat3={"办公文具"};
	public static String [] cat4={"洗发护发","身体护理","面部护理","口腔护理","女性护理","男性护理"};
	public static String [] cat5={"衣服晾晒","整理收纳","床上用品","家纺用品","一次性用品","纸制品","衣物清洁","驱虫驱蚊","清洁工具"};
	public static int [][]catId={{6,7,8,9,10,12,13},{14,15,16,17,18},{19},{20,21,22,23,24,25},{26,27,28,29,30,31,32,33,34}};
	public static String m_szDevIDShort = ""+Build.BOARD.length()%10+ Build.BRAND.length()%10 + 
			Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 + Build.DISPLAY.length()%10 + 
			Build.HOST.length()%10 + Build.ID.length()%10 + Build.MANUFACTURER.length()%10 + 
			Build.MODEL.length()%10 + Build.PRODUCT.length()%10 + Build.TAGS.length()%10 + 
			Build.TYPE.length()%10 + Build.USER.length()%10 ; //13 digits 
	public static SharedPreferences sp;
	// 填写从短信SDK应用后台注册得到的APPKEY
	public static String APPKEY = "59ff63e19940";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	public static String APPSECRET = "fda25bbd60c0e8210daae84dde8bb79c";
	
	public static Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
}
