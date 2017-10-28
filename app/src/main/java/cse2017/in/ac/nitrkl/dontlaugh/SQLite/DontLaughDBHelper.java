package cse2017.in.ac.nitrkl.dontlaugh.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by me on 10/28/2017.
 */


public class DontLaughDBHelper extends SQLiteOpenHelper {


    private static final String LOG_TAG = "DontLaughDBHelper";

    public static final int VER_1 = 1; //1.0.2_19

    public static final int DATABASE_CURRENT_VERSION = VER_1;

    public static final String DATABASE_NAME = "dont_laugh.db";
    private Context mContext;

    private static DontLaughDBHelper dbHelper;

    public DontLaughDBHelper(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_CURRENT_VERSION);
        mContext=applicationContext;
    }

    public static synchronized DontLaughDBHelper getInstance(Context ctx) {
        if (dbHelper == null) {
            dbHelper = new DontLaughDBHelper(ctx.getApplicationContext());
        }
        return dbHelper;
    }


    public DontLaughDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ver1(db);

    }

    private void ver1(SQLiteDatabase db) {
        createTablePosts(db);
        createTableCategories(db);


    }

    private void createTableCategories(SQLiteDatabase db)
    {
    }

    private void createTablePosts(SQLiteDatabase db) {
        final String SQL_CREATE_ITEM_FEED_TABLE = "CREATE TABLE IF NOT EXISTS " + DontLaughContract.PostsEntry.TABLE_NAME + " (" +
                DontLaughContract.PostsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DontLaughContract.PostsEntry.PID + " TEXT UNIQUE NOT NULL, " +
                DontLaughContract.PostsEntry.URL + " TEXT NOT NULL, " +
                DontLaughContract.PostsEntry.CID + " TEXT NOT NULL, " +
                DontLaughContract.PostsEntry.TS + " TEXT, " +
                DontLaughContract.PostsEntry.SHARE + " TEXT , " +
                DontLaughContract.PostsEntry.SEEN + " TEXT , " +
                DontLaughContract.PostsEntry.STAR + " TEXT , " +
                DontLaughContract.PostsEntry.URI + " TEXT  " +
                " );";

        db.execSQL(SQL_CREATE_ITEM_FEED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String SQL_CREATE_ITEM_FEED_TABLE = "CREATE TABLE IF NOT EXISTS " + DontLaughContract.CategoriesEntry.TABLE_NAME + " (" +
                DontLaughContract.CategoriesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DontLaughContract.CategoriesEntry.CID + " TEXT UNIQUE NOT NULL, " +
                DontLaughContract.CategoriesEntry.CNAME + " TEXT  NOT NULL, " +
                DontLaughContract.CategoriesEntry.URL + " TEXT  NOT NULL, " +
                DontLaughContract.CategoriesEntry.URI + " TEXT  " +
                " );";

        db.execSQL(SQL_CREATE_ITEM_FEED_TABLE);
    }
}
