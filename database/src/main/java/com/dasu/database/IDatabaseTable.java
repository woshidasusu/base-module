package com.dasu.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * 每张数据库表都必须实现的规范
 * {@link AbstractDbTable} 该类提供了默认的表升级操作
 *
 * Created by suxq on 2018/5/5.
 */

public interface IDatabaseTable {

    /**
     * 数据库表名
     */
    String getName();

    /**
     * 数据库创建时被调用，用于创建数据库表
     */
    void onCreate(SQLiteDatabase db);

    /**
     * 数据库升级时被调用，用于更新数据库表
     *
     */
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion, String tempName);

}
