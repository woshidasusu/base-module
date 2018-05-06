package com.dasu.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by dasu on 2017/4/12.
 */

public class GanHuoContentProvider extends ContentProvider {
    private static final String TAG = GanHuoContentProvider.class.getSimpleName();
    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = DatabaseHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String tableName = DatabaseManager.matchUri(uri);
        try {
            db.beginTransaction();
            cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        } catch (SQLException e) {
            Log.e(TAG, "query " + tableName + " error: " + e);
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri = null;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String tableName = DatabaseManager.matchUri(uri);
        long rowId = -1;
        try {
            db.beginTransaction();
            rowId = db.replace(tableName, null, values);
            return rowId > 0
                    ? ContentUris.withAppendedId(uri, rowId)
                    : ContentUris.withAppendedId(uri, -1);
        } catch (SQLException e) {
            Log.e(TAG, "insert " + tableName + " error: " + e);
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String tableName = DatabaseManager.matchUri(uri);
        int count = 0;
        try {
            db.beginTransaction();
            count = db.delete(tableName, selection, selectionArgs);
        } catch (SQLException e) {
            Log.e(TAG, "delete " + tableName + " error: " + e);
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int count = 0;
        String tableName = DatabaseManager.matchUri(uri);
        try {
            db.beginTransaction();
            count = db.update(tableName, values, selection, selectionArgs);
        } catch (SQLException e) {
            Log.e(TAG, "update " + tableName + " error: " + e);
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return count;
    }
}
