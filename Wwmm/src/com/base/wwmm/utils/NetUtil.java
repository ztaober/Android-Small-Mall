package com.base.wwmm.utils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络操作类.
 * 
 * @Description 用于网络的POST 、 GET 、 MutilPart等操作
 * @author qw
 * @date 2015-6-22
 */
public class NetUtil {
	/** 本地文件上传失败 */
	public static final String EXCEPTION_UPLOAD_ERROR_STATUS = "805";
	/** 输入流 */
	private BufferedInputStream bis;
	/** http链接 */
	private HttpURLConnection mConnection;
	/** 输出流 */
	private DataOutputStream dos;

	/**
	 * 获取当前网络链接状态.
	 * 
	 * @updateTime 2015-6-22,下午3:57:04
	 * @updateAuthor qw
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
		if (connectivity == null) {
			// 判断网络服务是否为空
			return false;
		} else {
			// 判断当前是否有任意网络服务开启
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 像指定地址发送post请求提交数据
	 * 
	 * @updateTime 2015-6-22,下午3:58:52
	 * @updateAuthor qw
	 * @param path 数据提交路径.
	 * @param timeout 超时时间(毫秒).
	 * @param attribute 发送请求参数,key为属性名,value为属性值.
	 * @return 服务器的响应信息,当发生错误时返回响应码.
	 * @throws IOException 网络连接错误时抛出IOException.
	 * @throws TimeoutException 网络连接超时时抛出TimeoutException.
	 */
	public String sendPost(String path, int timeout, HashMap<String, String> attribute) throws IOException, TimeoutException {
		URL url = new URL(path);
		mConnection = (HttpURLConnection) url.openConnection();
		mConnection.setDoOutput(true); // 设置输出,post请求必须设置.
		mConnection.setDoInput(true); // 设置输入,post请求必须设置.
		mConnection.setUseCaches(false); // 设置是否启用缓存,post请求不能使用缓存.
		// 设置Content-Type属性.
		// mConnection.setRequestProperty("Content-Type",
		// "text/html;charset=utf-8");
		mConnection.setConnectTimeout(timeout);
		mConnection.setReadTimeout(timeout);
		mConnection.setRequestMethod("POST");
		mConnection.connect(); // 打开网络链接.
		dos = new DataOutputStream(mConnection.getOutputStream());
		if (attribute != null) {
			dos.writeBytes(getParams(attribute)); // 将请求参数写入网络链接.
		}
		dos.flush();
		return readResponse();
	}

	/**
	 * 像指定地址发送get请求.
	 * 
	 * @updateTime 2015-6-22,下午4:00:02
	 * @updateAuthor qw
	 * @param path 数据提交路径.
	 * @param timeout 超时时间,单位为毫秒.
	 * @return 服务器的响应信息,当发生错误时返回响应码.
	 * @throws IOException 网络连接错误时抛出IOException.
	 * @throws TimeoutException 网络连接超时时抛出TimeoutException.
	 */
	public String sendGet(String path, int timeout) throws IOException, TimeoutException {
		try {
			URL url = new URL(path);
			mConnection = (HttpURLConnection) url.openConnection();
			mConnection.setUseCaches(false); // 设置是否启用缓存,post请求不能使用缓存.
			// 设置Content-Type属性.
			// conn.setRequestProperty("Content-Type",
			// "text/html;charset=utf-8");
			mConnection.setConnectTimeout(timeout);
			mConnection.setReadTimeout(timeout);
			mConnection.setRequestMethod("GET");
			mConnection.connect(); // 打开网络链接.
			return readResponse();
		} catch (SocketTimeoutException e) {
			throw new TimeoutException(e.getMessage());
		}
	}

	/**
	 * 读取服务器响应信息.
	 * 
	 * @updateTime 2015-6-22,下午4:00:46
	 * @updateAuthor qw
	 * @return 服务器的响应信息,当发生错误时返回响应码.
	 * @throws IOException 读取信息发生错误时抛出IOException.
	 */
	private String readResponse() throws IOException {
		String result;
		int code = mConnection.getResponseCode();
		if (code == HttpURLConnection.HTTP_OK) { // 若响应码以2开头则读取响应头总的返回信息
			bis = new BufferedInputStream(mConnection.getInputStream());
			ByteArrayBuffer arrayBuffer = new ByteArrayBuffer(1024);
			int length = -1;
			while ((length = bis.read()) != -1) {
				arrayBuffer.append(length);
			}
			result = EncodingUtils.getString(arrayBuffer.toByteArray(), "UTF-8");
		} else { // 若响应码不以2开头则返回错误信息.
			return "error";
		}
		closeConnection();
		return result;
	}

	/**
	 * 将发送请求的参数构造为指定格式.
	 * 
	 * @updateTime 2015-6-22,下午4:01:32
	 * @updateAuthor qw
	 * @param attribute
	 * @return
	 */
	private String getParams(HashMap<String, String> attribute) {
		Set<String> keys = attribute.keySet(); // 获取所有参数名
		Iterator<String> iterator = keys.iterator(); // 将所有参数名进行跌代
		StringBuffer params = new StringBuffer();
		// 取出所有参数进行构造
		while (iterator.hasNext()) {
			try {
				String key = iterator.next();
				String param = key + "=" + URLEncoder.encode(attribute.get(key)) + "&";
				params.append(param);
			} catch (Exception e) {
			}
		}
		// 返回构造结果
		return params.toString().substring(0, params.toString().length() - 1);
	}

	/**
	 * 关闭链接与所有从链接中获得的流.
	 * 
	 * @updateTime 2015-6-22,下午4:03:22
	 * @updateAuthor qw
	 * @throws IOException
	 */
	private void closeConnection() throws IOException {
		if (bis != null) {
			bis.close();
		}
		if (dos != null) {
			dos.close();
		}
		if (mConnection != null) {
			mConnection.disconnect();
		}
	}

	/**
	 * 下载文件,下载文件存储至指定路径.
	 * 
	 * @updateTime 2015-6-22,下午4:03:22
	 * @updateAuthor qw
	 * @param path 下载路径.
	 * @param savePath 存储路径.
	 * @return 下载成功返回true,若下载失败则返回false.
	 * @throws MalformedURLException 建立连接发生错误抛出MalformedURLException.
	 * @throws IOException 下载过程产生错误抛出IOException.
	 */
	public boolean downloadFile(String path, String savePath) throws MalformedURLException, IOException {
		File file = null;
		InputStream input = null;
		OutputStream output = null;
		boolean success = false;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			int code = conn.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				input = conn.getInputStream();
				file = new File(savePath);
				file.createNewFile(); // 创建文件
				output = new FileOutputStream(file);
				byte buffer[] = new byte[1024];
				int read = 0;
				while ((read = input.read(buffer)) != -1) { // 读取信息循环写入文件
					output.write(buffer, 0, read);
				}
				output.flush();
				success = true;
			} else {
				success = false;
			}
		} catch (MalformedURLException e) {
			success = false;
			throw e;
		} catch (IOException e) {
			success = false;
			throw e;
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	/**
	 * 文件上传
	 * 
	 * @updateTime 2015-6-22,下午4:03:22
	 * @updateAuthor qw
	 * @param path 服务器地址
	 * @param params 附带参数集合
	 * @param files 文件集合，支持多文件上传
	 * @return
	 */
	public static String sendMultyPartRequest(String path, HashMap<String, String> params, HashMap<String, File> files) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(path);
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (params.size() > 0) {
			Set<String> paramKeys = params.keySet();
			Iterator<String> paramIterator = paramKeys.iterator();
			while (paramIterator.hasNext()) {
				String key = paramIterator.next();
				try {
					StringBody stringBody = new StringBody(params.get(key), Charset.forName(HTTP.UTF_8));
					multipartEntity.addPart(key, stringBody);
				} catch (UnsupportedEncodingException e) {
					return EXCEPTION_UPLOAD_ERROR_STATUS;
				}
			}
		}
		if (files.size() > 0) {
			Set<String> fileKeys = files.keySet();
			Iterator<String> fileIterator = fileKeys.iterator();
			while (fileIterator.hasNext()) {
				String key = fileIterator.next();
				FileBody fileBody = new FileBody(files.get(key));
				LogUtil.i(fileBody.toString());
				multipartEntity.addPart(key, fileBody);
			}
		}
		httpPost.setEntity(multipartEntity);

		String result = null;
		try {
			HttpResponse response = httpClient.execute(httpPost);
			int statueCode = response.getStatusLine().getStatusCode();
			if (statueCode == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			} else {
				result = EXCEPTION_UPLOAD_ERROR_STATUS;
			}
		} catch (Exception e) {
			result = EXCEPTION_UPLOAD_ERROR_STATUS;
			e.printStackTrace();
		} finally {
			try {
				multipartEntity.consumeContent();
				multipartEntity = null;
				httpPost.abort();
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}