package com.base.wwmm.database;

import android.database.sqlite.SQLiteDatabase;

import com.base.wwmm.config.TApplication;
import com.base.wwmm.utils.LogUtil;

/**
 * 数据库管理工具
 * 
 * @Description
 * @author qw
 * @date 2015-6-22
 */
public class DataBaseManage {
	/** 数据库名称 */
	public static final String DATA_BASE_NAME = "BaseProject_DataBase";
	/** 数据库版本 */
	public static final int DATA_BASE_VERSION = 1;

	/** 数据库操作对象 */
	private static OperationDataBaseUtil dataBaseHelper;
	/** 公用数据库操作对象 */
	private static OperationDataBaseUtil publicDataBaseHelper;

	/**
	 * 创建私有数据库
	 * 
	 * @updateTime 2015-6-22,下午3:45:02
	 * @updateAuthor qw
	 * @param dataBaseName
	 */
	public static void createDataBase(String dataBaseName) {
		dataBaseHelper = new OperationDataBaseUtil(TApplication.context, dataBaseName, null, DATA_BASE_VERSION, new OnOperationDataBase() {
			@Override
			public void updateDataBase(SQLiteDatabase db, int oldVersion, int newVersion) {
				updateTables(db, oldVersion, newVersion);
			}

			@Override
			public void createTable(SQLiteDatabase db) {
				createTables(db);
			}
		});
		dataBaseHelper.onCreate(dataBaseHelper.getWritableDatabase());
		dataBaseHelper.close();
		dataBaseHelper = null;
	}

	/**
	 * 获取私有数据库操作对象
	 * 
	 * @updateTime 2015-6-22,下午3:45:28
	 * @updateAuthor qw
	 * @param dataBaseName
	 * @return
	 */
	public static OperationDataBaseUtil getOperationDataBaseUtil(String dataBaseName) {
		if (null != dataBaseHelper) {
			dataBaseHelper.close();
			dataBaseHelper = null;
		}
		dataBaseHelper = new OperationDataBaseUtil(TApplication.context, dataBaseName, null, DATA_BASE_VERSION);
		return dataBaseHelper;

	}

	/**
	 * 获取单独不受他人影响的数据库操作对象
	 * 
	 * @updateTime 2015-6-22,下午3:45:42
	 * @updateAuthor qw
	 * @param dataBaseName
	 * @return
	 */
	public static OperationDataBaseUtil getOperation(String dataBaseName) {
		return new OperationDataBaseUtil(TApplication.context, dataBaseName, null, DATA_BASE_VERSION);
	}

	/**
	 * 创建共有数据库
	 * 
	 * @updateTime 2015-6-22,下午3:46:13
	 * @updateAuthor qw
	 */
	public static void createPulibicDataBase() {
		publicDataBaseHelper = new OperationDataBaseUtil(TApplication.context, DATA_BASE_NAME, null, DATA_BASE_VERSION, new OnOperationDataBase() {
			@Override
			public void updateDataBase(SQLiteDatabase db, int oldVersion, int newVersion) {
				updatePublicTables(db, oldVersion, newVersion);
			}

			@Override
			public void createTable(SQLiteDatabase db) {
				createPublicTables(db);
			}
		});
		publicDataBaseHelper.onCreate(publicDataBaseHelper.getWritableDatabase());
		publicDataBaseHelper.close();
		publicDataBaseHelper = null;
	}

	/**
	 * 获取公用数据库操作对象
	 * 
	 * @updateTime 2015-6-22,下午3:46:34
	 * @updateAuthor qw
	 * @return
	 */
	public static OperationDataBaseUtil getPublicOperationDataBaseUtil() {
		if (null != publicDataBaseHelper) {
			publicDataBaseHelper.close();
			publicDataBaseHelper = null;
		}
		publicDataBaseHelper = new OperationDataBaseUtil(TApplication.context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
		return publicDataBaseHelper;
	}

	protected static void updatePublicTables(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion < oldVersion) {// 如果不是数据库升级 公用数据库升级
			return;
		}
		try {
			switch (db.getVersion()) {
			case 1:// 数据库版本1-2
			case 2:// 数据库版本2-3
				break;
			}
		} catch (RuntimeException e) {
			// 数据库更新失败
			LogUtil.i("公用数据库更新失败 ============>\n" + e.getMessage());
		}
	}

	/**
	 * 公用数据库表
	 * 
	 * @updateTime 2015-6-22,下午3:43:11
	 * @updateAuthor qw
	 * @param db
	 */
	protected static void createPublicTables(SQLiteDatabase db) {
		/**
		 * json的缓存表
		 * 
		 */
		String sql = "create table if not exists " + "json" + "(" // JSON表
				+ " id " + " integer PRIMARY KEY autoincrement, " // 自增长id.
				+ " type " + " varchar, " // 类型.
				+ " json " + " varchar" // json
				+ ");";
		db.execSQL(sql);
	}

	/**
	 * 更新私有数据库表
	 * 
	 * @updateTime 2015-6-22,下午3:43:35
	 * @updateAuthor qw
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public static void updateTables(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion < oldVersion) {// 如果不是数据库升级 私用数据库升级
			return;
		}
		try {
			switch (db.getVersion()) {
			case 1:// 数据库版本1-2
				break;
			}
		} catch (RuntimeException e) {
			LogUtil.i("私用数据库更新失败 ============>\n" + e.getMessage());
		}
	}

	/**
	 * 创建私有数据表
	 * 
	 * @updateTime 2015-6-22,下午3:47:00
	 * @updateAuthor qw
	 * @param db
	 */
	public static void createTables(SQLiteDatabase db) {

	}
}
