package com.michu.huanxin.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;


/*
 * 
 * @author @linkr
 * 
 */
public class HttpUtils {
	private static final String TAG = "NetUtils";

	
	public static String sendGet(String url, String[] paramName,String[] paramValue) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url;
			if(paramName !=null && paramName.length != 0){
				int countInt = paramName.length;
				if(countInt > paramValue.length){
					countInt = paramValue.length;
				}
				for(int i = 0;i < countInt;i++){
					if(i != 0){
						urlName += "&";
					}
					urlName = urlName + paramName[i] + "=" + paramValue[i];
				}
			}
			System.out.println("get--->" + urlName);
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();

			// 设置通用的请求属性
//			if (LinkrApplication.strUserSession != null)
//				conn.setRequestProperty("Cookie", LinkrApplication.strUserSession);

			conn.setConnectTimeout(10000);
			conn.setReadTimeout(20000);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (android Minchu)");
			// 建立实际的连接
			conn.connect();

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		return result;
	}

	private static class MyHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}

	}

	private static class MyTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	/**
	 * 一般请求，无加密
	 * 
	 * @param urlPath
	 *            map参数
	 * @param paras
	 * @return
	 */
	public static String sendPost(String urlPath, Map<String, String> paras) {
		try {
			String temp = "";
			Iterator<Entry<String, String>> iterator = paras.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				//temp += "&" + key + "=" + URLEncoder.encode(value, "UTF-8");
				temp += "&" + key + "=" + value;
			}

			temp = temp.substring(1);
			return sendPost(urlPath, temp);
		} catch (Exception e) {

		}
		return null;
	}

	public static String sendPost(String urlPath, Map<String, String> paras, int iLogin) {
		try {
			String temp = "";
			Iterator<Entry<String, String>> iterator = paras.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				temp += "&" + key + "=" + URLEncoder.encode(value, "UTF-8");
			}

			temp = temp.substring(1);
			return sendPost(urlPath, temp,iLogin);
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */

	public static String sendPost(String url, String param) {
		if (param != null && param.length() > 0) {
//			if (param != null && param.contains("appkey"))
//				param = param + "&platform=2&version=" + LinkrApplication.strVersion + "&lang="+LinkrApplication.APP_LANG;
//			else
//				param = param + "&platform=2&version=" + LinkrApplication.strVersion + "&lang="+LinkrApplication.APP_LANG;
//		} else {
//			if (param != null && param.contains("appkey"))
//				param = "platform=2&version=" + LinkrApplication.strVersion + "&lang="+LinkrApplication.APP_LANG;
//			else
//				param = "platform=2&version=" + LinkrApplication.strVersion + "&lang="+LinkrApplication.APP_LANG;
		}

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
//			
//			if (LinkrApplication.strUserSession != null)
//				conn.setRequestProperty("Cookie", LinkrApplication.strUserSession);
			System.out.println("url ---> " + url);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(20000);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; android smartcity)");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush 输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		return result;
	}

	
	public static String sendPost(String url, String param, int iLogin) {
//		if (param != null && param.length() > 0) {
//			if (param != null && param.contains("appkey"))
//				param = param + "&platform=2&version=" + LinkrApplication.strVersion + "&lang="+LinkrApplication.APP_LANG;
//			else
//				param = param + "&platform=2&version=" + LinkrApplication.strVersion + "&lang="+LinkrApplication.APP_LANG;
//		} else {
//			if (param != null && param.contains("appkey"))
//				param = "platform=2&version=" + LinkrApplication.strVersion;
//			else
//				param = "platform=2&version=" + LinkrApplication.strVersion + "&lang="+LinkrApplication.APP_LANG;
//		}

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();

			conn.setConnectTimeout(10000);
			conn.setReadTimeout(20000);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; android smartcity)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush 输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}

			if (iLogin == 1) // 登录
			{
				String strNewSession = conn.getHeaderField("Set-Cookie");
				String[] sessionId = strNewSession.split(";");
//				LinkrApplication.strUserSession = sessionId[0];
			} else if (iLogin == 0) // 注销
			{
//				LinkrApplication.strUserSession = null;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		return result;
	}
	
	
	
	/**
	 * 向指定URL发送POST方法的请求,上传文件
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */

	public static String sendPost(String url,File file,String filehead,String fileend,Map<String, String> requestProperty,Map<String, String> paras) {
		BufferedOutputStream out = null;
		BufferedInputStream infile = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();

			
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(20000);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; android smartcity)");
			if(file!=null){
				conn.setRequestProperty("Content-Type","multiable/form-data;");
			}
			if(requestProperty!=null){
				Iterator<String> iterator = requestProperty.keySet().iterator();
				while(iterator.hasNext()){
					String key = iterator.next();
					conn.setRequestProperty(key, requestProperty.get(key));
				}
			}
			
				String temp = "";
			if(paras!=null){
				Iterator<Entry<String, String>> iterator = paras.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, String> entry = iterator.next();
					String key = entry.getKey();
					String value = entry.getValue();
					temp += "&" + key + "=" + URLEncoder.encode(value, "UTF-8");
				}
				temp = temp.substring(1);
			}
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new BufferedOutputStream(conn.getOutputStream());
			if(paras!=null){
				out.write(temp.getBytes());
			}
			if(filehead!=null){
				System.out.println(filehead);
				out.write(filehead.getBytes());
			}
			if(file!=null){
				
				infile = new BufferedInputStream(new FileInputStream(file));				
				int len;
				byte[] buffer = new byte[1024];
				while((len=infile.read(buffer))!=-1){
					out.write(buffer,0,len);
				}
			}
			if(fileend!=null){
				System.out.println(fileend);
				out.write(fileend.getBytes());
			}
			// 发送请求参数
			//out.write(buffer);
			// flush 输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if(infile !=null){
					infile.close();
				}
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		return result;
	}
	
}