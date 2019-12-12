package br.com.npx.mathgame.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.npx.mathgame.dataaccess.MathGameDbHelper;

public class MathGameBus {

    private MathGameDbHelper dbHelper;

    public MathGameBus(Context context) {
        this.dbHelper = new MathGameDbHelper(context);
    }

    public int getMaxScore() {
        int maxScore = 0;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                MathGameDbHelper.TABLE_MAX_SCORE,
        };

        Cursor cursor = db.query(
                MathGameDbHelper.TABLE_NAME,
                projection,
                "",
                null,
                "",
                "",
                ""
        );

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                maxScore = cursor.getInt(cursor.getColumnIndex(MathGameDbHelper.TABLE_MAX_SCORE));
            }
        }
        cursor.close();

        return maxScore;
    }

    public void saveMaxScore(int maxScore) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MathGameDbHelper.TABLE_MAX_SCORE, maxScore);
        db.update(MathGameDbHelper.TABLE_NAME, values, "", null);
    }

}
