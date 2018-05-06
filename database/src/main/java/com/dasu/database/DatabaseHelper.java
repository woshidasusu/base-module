package com.dasu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by dasu on 2017/4/11.
 */
class DatabaseHelper extends SQLiteOpenHelper {
    static final String TAG = "database";

    private static final String TEMP_SUFFIX = "_temp_";
    private static DatabaseHelper mDbHelper;
    private SQLiteDatabase mReadableDB = null; // readable database
    private SQLiteDatabase mWritableDB = null; // writable database

    private DatabaseHelper(Context context) {
        super(context, DatabaseManager.DB_NAME, null, DatabaseManager.DB_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mDbHelper == null) {
            mDbHelper = new DatabaseHelper(context);
        }
        return mDbHelper;
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mWritableDB == null || !mWritableDB.isOpen() || mWritableDB.isReadOnly()) {
            try {
                mWritableDB = super.getWritableDatabase();
            } catch (SQLiteException e) {
                mWritableDB = null;
                Log.e(TAG, "getWritableDatabase(): Error", e);
                e.printStackTrace();
            }
        }
        return mWritableDB;
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (mReadableDB == null || !mReadableDB.isOpen()) {
            try {
                mReadableDB = super.getReadableDatabase();
            } catch (SQLiteException e) {
                mReadableDB = null;
                Log.e(TAG, "getReadableDatabase(): Error opening", e);
                e.printStackTrace();
            }
        }
        return mReadableDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()...");
        Collection<AbstractDbTable> tables = DatabaseManager.sAllTables.values();
        Iterator<AbstractDbTable> iterator = tables.iterator();
        try {
            db.beginTransaction();
            while (iterator.hasNext()) {
                iterator.next().onCreate(db);
            }
            db.setTransactionSuccessful();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 支持下列的数据库升级操作:
     * 1.添加新表
     * 2.删除旧表
     * 3.在旧表里添加新的列属性
     * 新增的列将会以默认的值填充，如果没有设置默认的值，那么会以null填充，默认的值在创建表的sql语句里设置
     * 4.在旧表里删除列属性
     * <p>
     * 以上是所有的表默认的升级操作，如果有针对某张表或某个版本的特定升级需求，那么需要重写那张表的 onUpgrade()，
     * 覆盖基类默认的升级操作。
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade(oldVersion = " + oldVersion + ", newVersion = " + newVersion + ")...");
        //获取旧版本的所有表
        Collection<String> oldTables = AbstractDbTable.listTables(db);
        if (oldTables == null || oldTables.size() == 0) {
            Log.d(TAG, "onUpgrade(): no existing tables; calling onCreate()...");
            onCreate(db);
            return;
        }

        //获取所有新表
        Set<String> newTables = DatabaseManager.sAllTables.keySet();

        try {
            db.beginTransaction();
            //删除没有在新版本的数据库里出现的旧表
            HashSet<String> removedTables = new HashSet<String>();
            for (String table : oldTables) {
                if (!newTables.contains(table)) {
                    Log.d(TAG, "onUpgrade(): remove table: " + table);
                    AbstractDbTable.dropTable(db, table);
                    removedTables.add(table);
                }
            }
            oldTables.removeAll(removedTables);

            //创建新表或对旧表升级
            AbstractDbTable curTable;
            for (String table : newTables) {
                curTable = DatabaseManager.sAllTables.get(table);
                //判断该表是否是新表，新表则创建，旧表则升级
                if (oldTables.contains(table)) {
                    String tempName = getTempTableName(table, oldTables, newTables);
                    curTable.onUpgrade(db, oldVersion, newVersion, tempName);
                } else {
                    curTable.onCreate(db);
                }
            }
            db.setTransactionSuccessful();
        } catch (Throwable e) {
            Log.e(TAG, "onUpgrade(): DB upgrade failed:", e);
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 获取表的临时命名，方便在升级表时进行拷贝操作
     * 确保临时表名不和任何表名起冲突
     *
     * @param tableName
     * @param oldTableNames
     * @param newTableNames
     * @return
     */
    private String getTempTableName(String tableName, Collection<String> oldTableNames, Set<String> newTableNames) {
        String base = tableName + TEMP_SUFFIX;
        if (!oldTableNames.contains(base) && !newTableNames.contains(base)) {
            return base;
        }
        Random random = new Random();
        String tempName;
        for (; ; ) {
            tempName = base + random.nextInt();
            if (!oldTableNames.contains(tempName) && !newTableNames.contains(tempName)) {
                return tempName;
            }
        }
    }
}
