package su.mboh.asu.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import su.mboh.asu.content.database.NewDatabase;

/**
 * Created by smktiufa on 16/12/16.
 */

public class NewsContentProvider extends ContentProvider {
    private NewDatabase dbHelper;
    public static final String TABLES[]={
      NewDatabase.TBL_NEW,
    };

    public static final String AUTHORITY = NewsContentProvider.class.getName().toLowerCase();
    public static final String CONTENT_PATH = "content://" + AUTHORITY + "/";

    private UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH) {
        {
            int i = 0;
            for (String table : TABLES) {
                addURI(AUTHORITY, table, i);
                i++;
            }
        }
    };

    @Override
    public boolean onCreate() {
        dbHelper =new NewDatabase(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        builder.setTables(TABLES[matcher.match(uri)]);
        Cursor cursor = builder.query(database, strings, s,
                strings1, null, null, s1);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database =dbHelper.getWritableDatabase();
        long id =database.insert(TABLES[matcher.match(uri)], null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri.parse(TABLES[matcher.match(uri)] + "/"+id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase database =dbHelper.getWritableDatabase();
        int  deleted = database.delete(TABLES[matcher.match(uri)] ,s,strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase database =dbHelper.getWritableDatabase();
        int  updated = database.update(TABLES[matcher.match(uri)], contentValues ,s,strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return updated;
    }
}
