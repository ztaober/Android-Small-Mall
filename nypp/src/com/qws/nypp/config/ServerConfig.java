package com.qws.nypp.config;

/**
 * 服务器配置
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class ServerConfig {

	// *****************************网请求消息状态码 ******************************//
	/** 请求接口数据成功状态码 */
	public static final String RESPONSE_STATUS_SUCCESS = "10000";
	public static final String base = "http://121.42.204.196:80/malls/app/";

	/** 服务器基本路径 */
	public static final String BASE_PATH = base + "/interface";
	/** 服务器文件上传 */
	public static final String BASE_UPLOAD = base + "/upload";
	/** 服务器文件下载 */
	public static final String BASE_DOWNLOAD = base + "/download?filename=";
	
	
	/**	 登录接口*/
	public static final String LOGIN_PATH = base + "/login";
	/**	 热区Banner获取*/
	public static final String BANNER_PATH = base + "/findBannerALL";
	/**	 热区产品列表获取*/
	public static final String HOT_PRODUCT_PATH = base + "/getHotspotProductList";
	/**	 产品列表获取*/
	public static final String OPT_PRODUCT_PATH = base + "/getProductByPageList";
	/**	 产品详情获取*/
	public static final String PRODUCT_DETAIL_PATH = base + "/getProductDetail";
	/** 产品收藏 */
	public static final String PRODUCT_ADD_COLLECTION = base + "/addCollection";

}
