package su.mboh.asu.content.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import su.mboh.asu.content.database.model.NewDatabaseModel;

/**
 * Created by smktiufa on 16/12/16.
 */

public class NewDatabase extends SQLiteOpenHelper {

    public static final String DATABASE ="its_project";
    private static final int VERSION =1;
    private static final String DATABASE_NAME ="tbl_new";
    public static final String TBL_NEW ="tbl_new";
    private final Context context;


    public NewDatabase(Context context) {
        super(context,DATABASE, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TBL_NEW + "("
                + NewDatabaseModel.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + NewDatabaseModel.TITLE + " TEXT, "
                + NewDatabaseModel.CONTENT + " TEXT, "
                + NewDatabaseModel.CREATE_DATE + " INTEGER, "
                + NewDatabaseModel.STATUS + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < i1);
    }

}
