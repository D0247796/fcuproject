package lincyu.table;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 2016/5/31.
 */
public class DBOpenHelper extends SQLiteOpenHelper {


    public DBOpenHelper(Context context) {
        super(context, "note.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +
                NoteDB.NOTETABLE + "(title, body);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
    }

}
