package fr.mineplugins.android_estiam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "products.db";
    public static final String TABLE_NAME = "product_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "product_name";
    public static final String COL_3 = "product_url";
    public static final String COL_4 = "product_liked";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
//        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, product_name TEXT UNIQUE, product_url TEXT, product_liked INTEGER NOT NULL DEFAULT 0 CHECK(product_liked IN (0,1)))");
    }
    public void onLiked(int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("lol", "onLiked: LOL");
        db.execSQL("UPDATE " + TABLE_NAME + " SET product_liked = ((product_liked | 1) - (product_liked & 1)) WHERE ID = "+position);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String product_name, String url, boolean liked)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, product_name);
        contentValues.put(COL_3, url);
        contentValues.put(COL_4, liked);
        long res = db.insert(TABLE_NAME, null, contentValues);
        return res != -1;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }
}
