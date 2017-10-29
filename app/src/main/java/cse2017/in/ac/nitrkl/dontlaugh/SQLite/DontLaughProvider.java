package cse2017.in.ac.nitrkl.dontlaugh.SQLite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by me on 10/28/2017.
 */

public class DontLaughProvider extends ContentProvider {


    private static final int POSTS = 100;
    private static final int CATEGORY = 150;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DontLaughContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, DontLaughContract.PostsEntry.TABLE_NAME, POSTS);
        matcher.addURI(authority, DontLaughContract.CategoriesEntry.TABLE_NAME, CATEGORY);

        return matcher;
    }

    public DontLaughProvider() {
    }
    private DontLaughDBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = DontLaughDBHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case POSTS: {
                retCursor = defaultQuery(DontLaughContract.PostsEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case CATEGORY: {
                retCursor = defaultQuery(DontLaughContract.CategoriesEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder);
                break;
            }


        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POSTS:
                return DontLaughContract.PostsEntry.CONTENT_TYPE;
            case CATEGORY:
                return DontLaughContract.CategoriesEntry.CONTENT_TYPE;

        }

        return "";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match) {
            case POSTS: {
                long _id = db.insertWithOnConflict(
                        DontLaughContract.PostsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            }
            case CATEGORY: {
                long _id = db.insertWithOnConflict(
                        DontLaughContract.CategoriesEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            }

        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted = 0;
        switch (match) {
            case POSTS:
                rowsDeleted = db.delete(
                        DontLaughContract.PostsEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case CATEGORY:
                rowsDeleted = db.delete(
                        DontLaughContract.CategoriesEntry.TABLE_NAME, selection, selectionArgs);
                break;



        }
        return match;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (match) {
            case POSTS:
                rowsUpdated = db.update(DontLaughContract.PostsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case CATEGORY:
                rowsUpdated = db.update(DontLaughContract.CategoriesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;



        }
        return rowsUpdated;
    }

    private Cursor defaultQuery(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mOpenHelper.getReadableDatabase().query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private int bulkInsertWithConflictReplace(SQLiteDatabase db, ContentValues[] values, String tableName) {
        db.beginTransaction();
        int count = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insertWithOnConflict(
                        tableName, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                if (_id != -1) {
                    count++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }
}
