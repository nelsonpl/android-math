package br.com.npx.mathgame.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MathGameDbHelper extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MathGameDb.db";

    public static final String TABLE_NAME = "INFO";
    public static final String TABLE_MAX_SCORE = "MAX_SCORE";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE '" + TABLE_NAME + "'('" + TABLE_MAX_SCORE + "');";

    public MathGameDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + TABLE_MAX_SCORE + ") VALUES (0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
