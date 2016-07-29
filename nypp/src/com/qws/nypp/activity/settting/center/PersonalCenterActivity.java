package com.qws.nypp.activity.settting.center;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.activity.MainActivity;
import com.qws.nypp.activity.settting.AddrDetailActivity;
import com.qws.nypp.activity.settting.AddrHandActivity;
import com.qws.nypp.config.ServerConfig;
import com.qws.nypp.config.SpConfig;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.http.CallServer;
import com.qws.nypp.http.HttpListener;
import com.qws.nypp.http.NyppJsonRequest;
import com.qws.nypp.utils.AppManager;
import com.qws.nypp.utils.BitmapUtil;
import com.qws.nypp.utils.FilePathUtils;
import com.qws.nypp.utils.IntentUtil;
import com.qws.nypp.utils.LogUtil;
import com.qws.nypp.utils.SpUtil;
import com.qws.nypp.utils.ToastUtil;
import com.qws.nypp.utils.Util;
import com.qws.nypp.view.SelectPicPopupWindow;
import com.qws.nypp.view.dialog.FunctionDialog;
import com.qws.nypp.view.dialog.MenuCallback;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnUploadListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.Response;

public class PersonalCenterActivity extends BaseActivity implements OnClickListener {
	private static final int REQUEST_CAMERA = 101;
	private static final int SELECT_CAMERA = 1001;
	private static final int RESULT_REQUEST_CODE = 102;
	private String mPicPath;
	private String mHeadImagePath;
	private File mHeadFile;
	private Bitmap imageBitmap;
	private ImageView imgIv;
	private TextView nameTv;
	private DisplayImageOptions options;
	
	private String nickName;
	private String portraitUrl;
	
	private SelectPicPopupWindow mSelectPicPopupWindow;

	@Override
	protected int getContentViewId() {
		return R.layout.a_personal_center;
	}

	@Override
	protected void findViews() {
		imgIv = (ImageView) findViewById(R.id.personal_img_iv);
		nameTv = (TextView) findViewById(R.id.personal_name_tv);
	}

	@Override
	protected void initData() {
		titleView.setTitle("个人信息");
		options = TApplication.getInstance().getAllOptions(R.drawable.ic_defult_user_02);
	}
	
	@Override
	protected void onResume(boolean isBack) {
		super.onResume(isBack);
		
		nickName = SpUtil.getSpUtil().getSPValue(SpConfig.NICK_NAME,"");
		portraitUrl = SpUtil.getSpUtil().getSPValue(SpConfig.PORTRAIT_URL, "");
		nameTv.setText(nickName);
		ImageLoader.getInstance().displayImage(portraitUrl, imgIv, options);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		findViewById(R.id.personal_img_rl).setOnClickListener(this);
		findViewById(R.id.personal_name_rl).setOnClickListener(this);
		findViewById(R.id.personal_addr_rl).setOnClickListener(this);
		findViewById(R.id.personal_pwd_rl).setOnClickListener(this);
		findViewById(R.id.personal_logout_tv).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	
		case R.id.personal_img_rl:
			if(Util.isFastDoubleClick())
				break;
			mSelectPicPopupWindow = new SelectPicPopupWindow(this, itemsOnClick);
			mSelectPicPopupWindow.showAtLocation(titleView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			break;
			
		case R.id.personal_name_rl:
			IntentUtil.gotoActivity(context, ChangeNameActivity.class);
			break;
			
		case R.id.personal_addr_rl:
			IntentUtil.gotoActivity(context, AddrHandActivity.class);
			break;
			
		case R.id.personal_pwd_rl:
			IntentUtil.gotoActivity(context, ChangePwdActivity.class);
			break;
			
		case R.id.personal_logout_tv:
			FunctionDialog.show(PersonalCenterActivity.this, true,
					"温馨提示", "确定要退出当前帐号", getString(android.R.string.cancel),
					"", getString(android.R.string.ok), new MenuCallback() {

						@Override
						public void onMenuResult(int menuType) {
							if (menuType == R.id.right_bt) {
								AppManager.getAppManager().AppExit(context, false);
							}
						}
			});	
			break;

		default:
			break;
		}
	}

	@Override
	protected void getData() {

	}
	
	
	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			mSelectPicPopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.pop_choose_carme_txt:
				// 拍照   -- 需要判断SD卡
				if(!Util.ExistSDCard()) {
					ToastUtil.show("未找到SD储存卡");
					return;
				}
				// 创建临时文件
				File picFile = new File(FilePathUtils.getDefaultImagePath(context), System.currentTimeMillis() + ".png");
				mPicPath = picFile.getAbsolutePath();
		        // 跳转到系统照相机
		        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		        // 设置系统相机拍照后的输出路径
		        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
		        cameraIntent.putExtra("autofocus", true); // 自动对焦
		        startActivityForResult(cameraIntent, REQUEST_CAMERA);
				break;
			case R.id.pop_choose_library_txt:
				// 选图
				Intent picture = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(picture, SELECT_CAMERA);
				break;
			}
		}
	};
	
	// 裁剪图片
	private void checkPhotoZoom(final String path) {
		mHeadImagePath = path;
		mHeadFile = new File(FilePathUtils.getDefaultImagePath(context), SpUtil.getSpUtil().getSPValue(SpConfig.MEMBER_NAME,"default") + ".png");
		try {
			if (mHeadFile.exists()) {
				mHeadFile.delete();
			}
			mHeadFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File sFile = new File(path);
		// copy文件
		fileChannelCopy(sFile, mHeadFile);
		startPhotoZoom(Uri.fromFile(sFile), Uri.fromFile(mHeadFile), 300);// 截图
	}

	/**
	 * 复制文件
	 * 
	 * @param s 源文件
	 * @param t 目标文件
	 */
	private void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			FileChannel in = fi.getChannel();// 得到对应的文件通道
			FileChannel out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fo != null)
					fo.close();
				if (fi != null)
					fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 跳转至系统截图界面进行截图
	 * 
	 * @param data
	 * @param size
	 */
	private void startPhotoZoom(Uri sUri, Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");//动作-裁剪
		intent.setDataAndType(sUri, "image/*");//类型
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//
		// 输出文件
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		// 输出格式
		intent.putExtra("crop", false);
		intent.putExtra("aspectX", 1);// 裁剪比例
		intent.putExtra("aspectY", 1);// 裁剪比例
		intent.putExtra("outputX", size);// 输出大小
		intent.putExtra("outputY", size);// 裁剪比例后输出比例
		intent.putExtra("scale", true);// 缩放
		intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
		intent.putExtra("return-data", false);// 不返回缩略图
		intent.putExtra("noFaceDetection", true);// 关闭人脸识别
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == REQUEST_CAMERA) {
				if (resultCode == Activity.RESULT_OK) {
					// 拍照返回
					// 裁剪图片
					checkPhotoZoom(mPicPath);
				}
				
			} else if (requestCode == RESULT_REQUEST_CODE) {
				Bundle bundle = data.getExtras();
//				if (bundle != null) {
//					imageBitmap = bundle.getParcelable("data");
//				}
//				if (imageBitmap == null) {
//					imageBitmap = BitmapUtil.decodeThumbBitmapForFile(mHeadFile.getAbsolutePath(), 0, 0);
//				}
//				if (imageBitmap != null) {
//					imgIv.setImageBitmap(imageBitmap);
//				}
				// 保存头像
				saveHeadImage();
			} else  if(requestCode == SELECT_CAMERA && resultCode == Activity.RESULT_OK && null != data){
				   Uri selectedImage = data.getData();
				   String[] filePathColumns={MediaStore.Images.Media.DATA};
				   Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null,null, null);
				   c.moveToFirst();
				   int columnIndex = c.getColumnIndex(filePathColumns[0]);
				   String picturePath= c.getString(columnIndex);
				   c.close();
				   //获取图片并显示
				   checkPhotoZoom(picturePath);
				  }
	}
	
	   /**
     * 上传单个文件。
     */
    private void saveHeadImage() {
        Request<String> request = NoHttp.createStringRequest(ServerConfig.UPLOAD_FILE, RequestMethod.POST);

        // 添加普通参数。
        request.add("sign", TApplication.getInstance().getUserSign());
        request.add("name", TApplication.getInstance().getMemberId());

        // 上传文件需要实现NoHttp的Binary接口，NoHttp默认实现了FileBinary、InputStreamBinary、ByteArrayBitnary、BitmapBinary。
        // FileBinary用法
        FileBinary binary = new FileBinary(mHeadFile);
        
        /**
         * 监听上传过程，如果不需要监听就不用设置。
         * 第一个参数：what，what和handler的what一样，会在回调被调用的回调你开发者，作用是一个Listener可以监听多个文件的上传状态。
         * 第二个参数： 监听器。
         */
        binary.setUploadListener(0x01, mOnUploadListener);

        request.add("image", binary);// 添加1个文件
//            request.add("image1", fileBinary1);// 添加2个文件

        CallServer.getRequestInstance().add(this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
            	try {
            		LogUtil.t(response.get());
					JSONObject result = new JSONObject(response.get());
					portraitUrl = result.optString("url");
					if(!"".equals(portraitUrl)){
						changeImgUrl();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            }
        }, false, true);
    }
    
    private void changeImgUrl() {
		Request<JSONObject> request = new NyppJsonRequest(ServerConfig.UPDATE_MEMBER_BY_ID);
		Map<String, String> postData = new HashMap<String, String>();
		postData.put("sign", TApplication.getInstance().getUserSign());
		postData.put("id", TApplication.getInstance().getMemberId());
		postData.put("nickname", nickName);
		postData.put("portrait", portraitUrl);
		request.setRequestBody(new Gson().toJson(postData));
		CallServer.getRequestInstance().add(context, 0, request,new HttpListener<JSONObject>() {

				@Override
				public void onSucceed(int what,Response<JSONObject> response) {
					JSONObject result = response.get();// 响应结果
					if ("200".equals(result.optString("status"))) {
						SpUtil.getSpUtil().putSPValue(SpConfig.PORTRAIT_URL, portraitUrl);
						ImageLoader.getInstance().displayImage(portraitUrl, imgIv, options);
					} 
					ToastUtil.show(result.optString("declare", "未知错误"));
				}

				@Override
				public void onFailed(int what, String url, Object tag,Exception exception, int responseCode,
						long networkMillis) {
				}
		}, false, true);
	}


    /**
     * 文件上传监听。
     */
    private OnUploadListener mOnUploadListener = new OnUploadListener() {

        @Override
        public void onStart(int what) {// 这个文件开始上传。
        }

        @Override
        public void onCancel(int what) {// 这个文件的上传被取消时。
        }

        @Override
        public void onProgress(int what, int progress) {// 这个文件的上传进度发生边耍
        }

        @Override
        public void onFinish(int what) {// 文件上传完成
        }

        @Override
        public void onError(int what, Exception exception) {// 文件上传发生错误。
        }
    };

}
