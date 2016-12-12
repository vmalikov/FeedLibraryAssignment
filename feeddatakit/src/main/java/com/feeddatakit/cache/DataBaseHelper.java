package com.feeddatakit.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.feeddatakit.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 12/11/16.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "feedsdb";

    // Contacts table name
    private static final String TABLE_FEED = "feed";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FEED_TABLE = "CREATE TABLE " + TABLE_FEED + "("
                + DBField.ID.name + " INTEGER PRIMARY KEY, "
                + DBField.SENDER_NAME.name + " TEXT, "
                + DBField.TEXT.name + " TEXT,"
                + DBField.DATE.name + " INTEGER,"
                + DBField.MEDIA_TYPE.name + " INTEGER,"
                + DBField.MEDIA.name + " TEXT,"
                + DBField.SENDER_PROFILE_IMAGE.name + " TEXT,"
                + DBField.LIKE_COUNT.name + " INTEGER"
                + ")";
        db.execSQL(CREATE_FEED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED);

        onCreate(db);
    }

    public void addPost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = getContentValueBasedOnPost(post);

        db.insert(TABLE_FEED, null, values);
        db.close();
    }

    public Post getPost(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        DBField[] fields = DBField.values();
        String[] columns = new String[fields.length];
        for(int i = 0; i < columns.length; i++) {
            columns[i] = fields[i].name;
        }

        Cursor cursor = db.query(TABLE_FEED, columns, DBField.ID.name + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return getPostBasedOnCursor(cursor);
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<Post>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FEED;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                posts.add(getPostBasedOnCursor(cursor));
            } while (cursor.moveToNext());
        }

        // return contact list
        return posts;
    }

    public int getPostsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FEED;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updatePost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = getContentValueBasedOnPost(post);

        // updating row
        return db.update(TABLE_FEED, values, DBField.ID.name + " = ?",
                new String[] { String.valueOf(post.getId()) });
    }

    public void deletePost(Post contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FEED, DBField.ID.name + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }

    public void clearFeed() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_FEED);
        db.close();
    }

    private Post getPostBasedOnCursor(Cursor cursor) {
        return new Post()
                .setId(Integer.parseInt(cursor.getString(DBField.ID.ordinal())))
                .setSenderName(cursor.getString(DBField.SENDER_NAME.ordinal()))
                .setText(cursor.getString(DBField.TEXT.ordinal()))
                .setDate(Long.parseLong(cursor.getString(DBField.DATE.ordinal())))
                .setMediaType(Integer.parseInt(cursor.getString(DBField.MEDIA_TYPE.ordinal())))
                .setMedia(cursor.getString(DBField.MEDIA.ordinal()))
                .setSenderName(cursor.getString(DBField.SENDER_PROFILE_IMAGE.ordinal()))
                .setLikeCount(Integer.parseInt(cursor.getString(DBField.LIKE_COUNT.ordinal())));
    }

    private ContentValues getContentValueBasedOnPost(Post post) {
        ContentValues values = new ContentValues();
        values.put(DBField.ID.name, post.getId());
        values.put(DBField.SENDER_NAME.name, post.getSenderName());
        values.put(DBField.TEXT.name, post.getText());
        values.put(DBField.DATE.name, post.getDate());
        values.put(DBField.MEDIA_TYPE.name, post.getMediaType());
        values.put(DBField.MEDIA.name, post.getMedia());
        values.put(DBField.SENDER_PROFILE_IMAGE.name, post.getSenderProfileImage());
        values.put(DBField.LIKE_COUNT.name, post.getLikeCount());

        return values;
    }

    enum DBField {
        ID("id"),
        SENDER_NAME("senderName"),
        TEXT("text"),
        DATE("date"),
        MEDIA_TYPE("mediaType"),
        MEDIA("media"),
        SENDER_PROFILE_IMAGE("senderProfileImage"),
        LIKE_COUNT("likeCount"),
        ;

        DBField(String name) {
            this.name = name;
        }

        String name;
    }
}
