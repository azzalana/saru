package su.mboh.asu.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import su.mboh.asu.content.NewsContentProvider;
import su.mboh.asu.content.database.model.NewDatabaseModel;
import su.mboh.asu.entity.New;

/**
 * Created by smktiufa on 17/12/16.
 */

public class NewDatabaseAdapter {
    private Uri dbNew = Uri.parse(NewsContentProvider.CONTENT_PATH
            + NewsContentProvider.TABLES[0]);
    private Context context;
    public  NewDatabaseAdapter(Context context){
        this.context=context;
    }
    public void save (New news){
        ContentValues values = new ContentValues();

        if(news.getId() == -1){
            values.put(NewDatabaseModel.TITLE, news.getTitle());
            values.put(NewDatabaseModel.CONTENT, news.getContent());
            values.put(NewDatabaseModel.CREATE_DATE, news.getCreateDate());
            values.put(NewDatabaseModel.STATUS, 1);

            context.getContentResolver().insert(dbNew, values);
        }else {
            values.put(NewDatabaseModel.TITLE, news.getTitle());
            values.put(NewDatabaseModel.CONTENT, news.getContent());
            values.put(NewDatabaseModel.CREATE_DATE, news.getCreateDate());
            values.put(NewDatabaseModel.STATUS, 1);

            context.getContentResolver().update(dbNew, values, NewDatabaseModel.ID + " = ? ", new String[]{news.getId()+""} );
        }
    }
    public List findNewByTitle(String title){
        String query = NewDatabaseModel.TITLE + " like ? AND " +NewDatabaseModel.STATUS + " = ? ";
        String[] parameter = {"%" + title + "%", "1"};

        Cursor cursor = context.getContentResolver().query(dbNew, null, query, parameter, NewDatabaseModel.CREATE_DATE);
        List<New> newses = new ArrayList<>();

        if (cursor != null){
            while (cursor.moveToNext()){
                New n = new New();
                n.setId(cursor.getInt(cursor.getColumnIndex(NewDatabaseModel.ID)));
                n.setTitle(cursor.getString(cursor.getColumnIndex(NewDatabaseModel.TITLE)));
                n.setContent(cursor.getString(cursor.getColumnIndex(NewDatabaseModel.CONTENT)));
                n.setCreateDate(cursor.getLong(cursor.getColumnIndex(NewDatabaseModel.CREATE_DATE)));
                n.setStatus(cursor.getInt(cursor.getColumnIndex(NewDatabaseModel.STATUS)));

                newses.add(n);
            }
        }
        return newses;


    }

    public List<New> findNewsAll(){
        String query = NewDatabaseModel.STATUS+ " = ? ";
        String[] parameter = {"1"};

        Cursor cursor = context.getContentResolver().query(dbNew, null, query, parameter, NewDatabaseModel.CREATE_DATE);
        List<New> newses = new ArrayList<>();

        if (cursor != null){
            while (cursor.moveToNext()){
                New n = new New();
                n.setId(cursor.getInt(cursor.getColumnIndex(NewDatabaseModel.ID)));
                n.setTitle(cursor.getString(cursor.getColumnIndex(NewDatabaseModel.TITLE)));
                n.setContent(cursor.getString(cursor.getColumnIndex(NewDatabaseModel.CONTENT)));
                n.setCreateDate(cursor.getLong(cursor.getColumnIndex(NewDatabaseModel.CREATE_DATE)));
                n.setStatus(cursor.getInt(cursor.getColumnIndex(NewDatabaseModel.STATUS)));

                newses.add(n);
            }
        }
        cursor.close();
        return newses;
    }

    public void delete (New news){
        ContentValues values = new ContentValues();
        values.put(NewDatabaseModel.STATUS, 0);

        context.getContentResolver().update(dbNew, values, NewDatabaseModel.ID + " = ?", new String[]{news.getId() + ""});
    }
}
