package cse2017.in.ac.nitrkl.dontlaugh.SQLite;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.compat.BuildConfig;

/**
 * Created by me on 10/28/2017.
 */

public class DontLaughContract {

    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class PostsEntry implements BaseColumns {


        public static final String TABLE_NAME = "posts";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final String PID = "pid";
        public static final String CID = "cid";
        public static final String INFO = "info";
        public static final String SEEN = "seen";
        public static final String SHARE = "share";
        public static final String SHARE_COUNT = "shareCount";
        public static final String STAR = "star";
        public static final String TIME = "time";
        public static final String URL = "url";
        public static final String URI = "uri";


        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class CategoriesEntry implements BaseColumns {


        public static final String TABLE_NAME = "categories";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;


        public static final String CID = "cid";
        public static final String CNAME = "cname";
        public static final String URL = "url";
        public static final String URI = "uri";


        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


}
