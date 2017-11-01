package cse2017.in.ac.nitrkl.dontlaugh.POJO;

import android.database.Cursor;

import cse2017.in.ac.nitrkl.dontlaugh.SQLite.DontLaughContract;

/**
 * Created by LENOVO on 31-10-2017.
 */

public class Post {

     String pid;
     int cid;
     String info;
     int seen;
     int share;
     int shareCount;
     int star;
     long time;
     String uri;
     String url;

    public Post() {
    }

    public Post(String pid, int cid, String info, int seen, int share, int shareCount, int star, long time, String uri, String url) {
        this.pid = pid;
        this.cid = cid;
        this.info = info;
        this.seen = seen;
        this.share = share;
        this.shareCount = shareCount;
        this.star = star;
        this.time = time;
        this.uri = uri;
        this.url = url;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Post fromCursor(Cursor cursor) {
        Post p=new Post();
        p.setUrl(cursor.getString(cursor.getColumnIndex(DontLaughContract.PostsEntry.URL)));

        return p;
    }
}
