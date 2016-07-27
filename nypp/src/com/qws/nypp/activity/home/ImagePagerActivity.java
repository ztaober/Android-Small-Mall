package com.qws.nypp.activity.home;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qws.nypp.R;
import com.qws.nypp.activity.BaseActivity;
import com.qws.nypp.config.TApplication;
import com.qws.nypp.utils.InputStreamWrapper;
import com.qws.nypp.view.photo.PhotoView;

public class ImagePagerActivity extends BaseActivity implements OnClickListener {
	
	private ViewPager mViewPager;
	private SamplePagerAdapter pagerAdapter;

	// private GalleryViewPager mViewPager;
	private CloseActReceiver receiver;
	// private Animation showAction, hideAction;

	// 底部菜单
	private RelativeLayout menu_layout;
	private TextView save_bt; // 保存到相册
	private TextView download_bt; // 下载原图
	private TextView cancel;
	List<String> items = null;
	private int curPos = 0;
	private int itemsCount = 0;
	
	private LinearLayout download_layout;
	private ImageView download_iv;
	private TextView download_tv;
	private ProgressBar download_pgb;
	private static Point mPoint = new Point(480, 800);
	

	@Override
	protected int getContentViewId() {
		return R.layout.a_image_view_pager_act;
	}

	@Override
	protected void findViews() {
		menu_layout = (RelativeLayout) this.findViewById(R.id.camera_choice);
		save_bt = (TextView) this.findViewById(R.id.save_bt);
		download_bt = (TextView) this.findViewById(R.id.download_bt);
		download_bt.setVisibility(View.GONE);
		cancel = (TextView) this.findViewById(R.id.cancle);
		cancel.setText(android.R.string.cancel);
		download_layout = (LinearLayout) findViewById(R.id.download_layout);
		download_layout.setVisibility(View.GONE);
		download_iv = (ImageView) findViewById(R.id.download_image);
		download_tv = (TextView) findViewById(R.id.download_text);
		download_pgb = (ProgressBar) findViewById(R.id.download_progress);
	}

	@Override
	protected void initData() {
		curPos = getIntent().getIntExtra("cur", 0);
		items = new ArrayList<String>();
		List<String> listPhotos = getIntent().getStringArrayListExtra("path");
		if (null != listPhotos) {
			items.addAll(listPhotos);
		}
		itemsCount = items.size();
		titleView.setTitle(curPos + 1 + "/" + itemsCount);
		pagerAdapter = new SamplePagerAdapter(items);

		mViewPager = (ViewPager) findViewById(R.id.viewer);
		mViewPager.setOffscreenPageLimit(3);
		 mViewPager.setPageMargin(150);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setCurrentItem(curPos);
		
		mViewPager.setOnPageChangeListener(new onPageChangeListener());

		receiver = new CloseActReceiver();
		IntentFilter filter = new IntentFilter("close_act");
		registerReceiver(receiver, filter);
	}

	@Override
	protected void setListener() {
		titleView.setBackBtn();
		cancel.setOnClickListener(this);
		save_bt.setOnClickListener(this);
		download_bt.setOnClickListener(this);
	}

	@Override
	protected void getData() {

	}
	
	@Override
	public void onClick(View v) {
//		if (v == moreBtn) {
//			if (menu_layout.getVisibility() == View.VISIBLE) {
//				menu_layout.setVisibility(View.GONE);
//			} else {
//				menu_layout.setVisibility(View.VISIBLE);
//			}
//		} else if (v == save_bt) {
//			// 保存到相册
//			if (items.size() != 0) {
//				boolean flag = saveImage(items.get(curPos).getLocationPath());
//				if (flag) {
//					Toast.makeText(this, getResources().getString(R.string.kim_txt_save_ok), Toast.LENGTH_SHORT).show();
//				} else {
//					Toast.makeText(this, getResources().getString(R.string.kim_txt_save_ok), Toast.LENGTH_SHORT).show();
//				}
//			}
//			menu_layout.setVisibility(View.GONE);
//
//		} else if (v == download_bt) {
//			menu_layout.setVisibility(View.GONE);
//			download_layout.setVisibility(View.VISIBLE);
//			try {
//				final IMFileManager manager = IMFileManager.getInstance(this);
//				final IMMessage msg = items.get(curPos);
//				final FileBean bean = manager.getFileBeanById(msg.getFileId());
//				final Bitmap bitmap = ImageUtil.getImage(msg.getLocationPath(), 800, 800);
//				download_tv.setText(getResources().getString(R.string.kim_txt_loading_image_ing, manager.getSizeStr(msg.getFileId())));
//				download_iv.setImageBitmap(bitmap);
//				download_pgb.setProgress(0);
//				manager.download(bean.id, new FileTransCallback() {
//					
//					@Override
//					public void onSucess(String info) {
//						download_layout.setVisibility(View.GONE);
//						if (null != bitmap && !bitmap.isRecycled()) {
//							bitmap.recycle();
//						}
//						msg.setLocationPath(bean.path);
//						msg.setFileId(-1);
//						if (msg.getChatType() == 1) {
//							ChatRoomManager.getInstance(ImageViewPagerActivity.this).saveRoomMessage(msg);
//						}
//						MessageManager.getInstance(ImageViewPagerActivity.this).saveIMMessage(msg);
//						refresh();
//					}
//					
//					@Override
//					public void onProgress(int pos) {
//						download_pgb.setProgress(pos);
//						download_tv.setText(getResources().getString(R.string.kim_txt_loading_image_ing, manager.getSizeStr(msg.getFileId())));
//					}
//					
//					@Override
//					public void onFaild(String err) {
//						Toast.makeText(ImageViewPagerActivity.this, err, Toast.LENGTH_SHORT).show();
//						download_layout.setVisibility(View.GONE);
//						if (null != bitmap && !bitmap.isRecycled()) {
//							bitmap.recycle();
//						}
//						refresh();
//					}
//				});
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		} else if (v == cancel) {
//			// 取消
//			menu_layout.setVisibility(View.GONE);
//		}
	}
	
	protected Bitmap returnBitmap(String... strings) {
		String path = strings[0];
		Bitmap bm = null;
		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			InputStreamWrapper bis = new InputStreamWrapper(fis, 8192,
					file.length());
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}

	public boolean saveImage(String path) {
		boolean is_save = false;
		ContentResolver cr = this.getContentResolver();
		try {
			String url = MediaStore.Images.Media.insertImage(cr, path, "", "");
			String imagepath = getFilePathByContentResolver(this, Uri.parse(url));
			if (imagepath != null) {
				is_save = true;
				Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri uri = Uri.parse(getFilePathByContentResolver(this,
						Uri.parse(url)));
				intent.setData(uri);
				this.sendBroadcast(intent);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is_save;
	}

	private String getFilePathByContentResolver(Context context, Uri uri) {
		if (null == uri) {
			return null;
		}
		Cursor c = context.getContentResolver().query(uri, null, null, null,
				null);
		String filePath = null;
		if (null == c) {
			throw new IllegalArgumentException("Query on " + uri
					+ " returns null result.");
		}
		try {
			if ((c.getCount() != 1) || !c.moveToFirst()) {
			} else {
				filePath = c.getString(c
						.getColumnIndexOrThrow(MediaColumns.DATA));
			}
		} finally {
			c.close();
		}
		return filePath;
	}

	/**
	 * 复写返回事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (menu_layout.getVisibility() == View.VISIBLE) {
				menu_layout.setVisibility(View.GONE);
			} else {
				this.finish();
			}
		}
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private class CloseActReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("close_act".equals(intent.getAction())) {
				if (titleView.getVisibility() == View.GONE) {
					titleView.setVisibility(View.VISIBLE);
				} else {
					titleView.setVisibility(View.GONE);

				}
			}
		}
	}

	class onPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			titleView.setTitle(arg0 + 1 + "/" + itemsCount);
			menu_layout.setVisibility(View.GONE);
		}

	}

	static class SamplePagerAdapter extends PagerAdapter {
		List<String> items = null;
		/** imageLoader默认配置 */
		DisplayImageOptions options = TApplication.getInstance().getAllOptions(R.drawable.bg_defualt_detail);

		public SamplePagerAdapter(List<String> resources) {
			this.items = resources;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			String path = items.get(position);
//			String fileName = path.substring(path.lastIndexOf("/") + 1,
//					path.length());
//			Bitmap bitmap = mMemoryCache.get(fileName);
//			if (bitmap == null) {
//				bitmap = ImageUtil.getImage(path);
//
//				if (bitmap != null) {
//					int degree = ImageUtil.readPictureDegree(path);
//					if (degree > 0) {
//						bitmap = ImageUtil.rotaingImageView(degree, bitmap);
//					}
//					mMemoryCache.put(fileName, bitmap);
//				}
//			}
			try {
//				AsyImageLoader.setImage(photoView, 0, path, mPoint, null);
				ImageLoader.getInstance().displayImage(path, photoView, options);
				
//				Bitmap bitmap = ImageUtil.getImage(path, ImageUtil.MAX_WIDTH * 2, ImageUtil.MAX_HEIGHT * 2);
//				photoView.setImageBitmap(bitmap);
			} catch (Exception e) {
				// TODO: handle exception
			}

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			PhotoView photoView = (PhotoView) object;
			photoView.gc();
			container.removeView(photoView);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}
