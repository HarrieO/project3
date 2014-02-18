package nl.mprog.apps.Hangman10196129.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import nl.mprog.apps.Hangman10196129.database.WordDatabaseHelper.WordReaderContract.FeedEntry;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.IOException;

/**
 * Created by hroosterhuis on 18/02/14.
 */
public class WordDatabase {

    private WordDatabaseHelper mDbHelper = null;
    private Context context;
    private int limit = 10;

    public WordDatabase(Context context) {
        this.context = context;
        mDbHelper = new WordDatabaseHelper(context);
    }

    public int maxLength(){
        Cursor c = mDbHelper.getReadableDatabase().rawQuery(
                "SELECT MAX(" + FeedEntry.COLUMN_NAME_LENGTH + ") as max FROM " + FeedEntry.TABLE_NAME, null
        );
        c.moveToFirst();

        return c.getInt(c.getColumnIndex("max"));
    }


    public String get(int length) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_WORD,
                FeedEntry.COLUMN_NAME_LENGTH
        };

        String whereClause = FeedEntry.COLUMN_NAME_LENGTH + " =  ?";
        String[] whereArgs = {
                String.valueOf(length)
        };

        Cursor c = db.query(FeedEntry.TABLE_NAME, projection, whereClause, whereArgs, null, null, null);
        c.moveToFirst();
        c.move((int)Math.floor(c.getCount()*Math.random()));
        return c.getString(c.getColumnIndex(FeedEntry.COLUMN_NAME_WORD));
    }

    public long count() {
        return DatabaseUtils.queryNumEntries(mDbHelper.getReadableDatabase(),
                FeedEntry.TABLE_NAME);
    }
}