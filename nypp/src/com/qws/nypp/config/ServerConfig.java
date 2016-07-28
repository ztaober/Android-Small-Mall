package com.qws.nypp.config;

/**
 * 服务器配置
 * 
 * @Description
 * @author 
 * @date 2015-6-22
 */
public class ServerConfig {
	
	public static final String APP_ID = "wxb2cb73243d32f8ce";

	// *****************************网请求消息状态码 ******************************//
	/** 请求接口数据成功状态码 */
	public static final String RESPONSE_STATUS_SUCCESS = "10000";
	public static final String base = "http://121.42.204.196:80/malls/app";

	/** 服务器基本路径 */
	public static final String BASE_PATH = base + "/interface";
	/** 服务器文件上传 */
	public static final String BASE_UPLOAD = base + "/upload";
	/** 服务器文件下载 */
	public static final String BASE_DOWNLOAD = base + "/download?filename=";
	
	
	/**	 服务质询*/
	public static final String CONTACT_US = base + "/contactUs";
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
	/**	 产品评价列表*/
	public static final String ACQUIRE_ALL_APPRAISE_BY_PROID = base + "/acquireAllAppraiseByProductId";
	/**	 产品详情web获取*/
	public static final String PRODUCT_DETAIL_PRE_PATH = base + "/getProductPresentation";
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
	
	/** 提交订单*/
	public static final String SUBMIT_ORDERS = base + "/submitOrders";
	/** 生成微信单号*/
	public static final String CREATE_WX_ORDER = base + "/createWxOrder";
	
	
	/** 获取不同状态订单数量*/
	public static final String GET_ORDERS_AMOUNT = base + "/getOrdersAmount";
	/** 根据会员编号及状态获取订单列表*/
	public static final String ACQUIRE_ALL_ORDER = base + "/acquireAllOrder";
	/** 单个订单获取*/
	public static final String UNIQUE_ORDER = base + "/uniqueOrders";
	/** 取消订单 */
	public static final String CANCLE_RODERS = base + "/cancelOrders";
	/** 删除订单 */
	public static final String REMOVE_ORDERS = base + "/removeOrders";
	/** 确认收款 */
	public static final String RECEIVE_ORDERS = base + "/receive";
	/** 申请退款 */
	public static final String APPLY_REFUND = base + "/applyRefund";
	/** 订单评价 */
	public static final String APPRAISE_ORDERS = base + "/addAppraise";
	
	/** 我的相关 */
	/**  我的收藏 */
	public static final String GET_MY_COLLECTION = base + "/selectCollectionAllByMemberId";
	/**  意见反馈 */
	public static final String COMMENTS_SUBMIT = base + "/commentsSubmitted";
	/**  修改个人信息 */
	public static final String UPDATE_MEMBER_BY_ID = base + "/updateMemberById";
	/**  修改密码 */
	public static final String UPDATE_PWD_BY_ID = base + "/updatePwdById";

}
