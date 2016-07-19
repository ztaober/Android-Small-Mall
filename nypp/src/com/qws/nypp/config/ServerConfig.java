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
	public static final String BANNER_PATH = base + "/getBannerProductList";
	/**	 热区产品列表获取*/
	public static final String HOT_PRODUCT_PATH = base + "/getHotspotProductList";
	/**	 产品列表获取*/
//	public static final String OPT_PRODUCT_PATH = base + "/getProductByPageList";
	/**	 产品列表-》自选商品*/
	public static final String OPT_PRODUCT_LIST = base + "/optionalProduct";
	/**	 产品详情获取*/
	public static final String PRODUCT_DETAIL_PATH = base + "/getProductDetail";
	/** 产品收藏 */
	public static final String PRODUCT_ADD_COLLECTION = base + "/addCollection";
	/** 产品加入进货单 */
	public static final String PRODUCT_INSERT_CART = base + "/insertCartSelective";
	/** 获取 进货单 购物车列表 */
	public static final String PRODUCT_GET_CART = base + "/selectShopCartListByMemberId";
	/** 购物车产品数量修改 */
	public static final String PRODUCT_UPDATE_CART = base + "/updateCartDetail";
	/** 购物车明细项删除 */
	public static final String PRODUCT_REMOVE_CART = base + "/removeCartDetail";
	/** 获取默认收货地址 */
	public static final String QUERY_DEFAULT_ADDRESS = base + "/queryDefaultAddress";
	/** 获取收货地址列表 */
	public static final String QUERY_ALL_ADDRESS = base + "/findContactAddressAllByPage";
	/** 添加收货地址 */
	public static final String ADD_CONTACT_ADDRESS = base + "/addContactAddress";
	/** 删除收货地址 */
	public static final String DELETE_CONTACT_ADDRESS = base + "/deleteContactAddress";
	/** 修改收货地址 */
	public static final String UPDATE_CONTACT_ADDRESS = base + "/updateContactAddress";
	/** 修改为默认收货地址 */
	public static final String UPDATE_DEFAULT_ADDRESS = base + "/updateDefaultContactAddress";

}
