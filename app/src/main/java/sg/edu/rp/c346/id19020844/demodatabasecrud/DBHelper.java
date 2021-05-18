package sg.edu.rp.c346.id19020844.demodatabasecrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "simplenotes.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NOTE = "note";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOTE_CONTENT = "note_content";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createNoteTableSql = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTE_CONTENT + " TEXT)";
        db.execSQL(createNoteTableSql);
        Log.i("info", "created tables");

        // Dummy records, to be inserted when the database is created
        for (int i = 0; i < 4; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOTE_CONTENT, "Data number " + i);
            db.insert(TABLE_NOTE, null, values);
        }
        Log.i("info", "dummy records inserted");
    }

    // Update database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
//        onCreate(db);

        db.execSQL("ALTER TABLE " + TABLE_NOTE + " ADD COLUMN module_name TEXT");
    }

    // Insert data into database
    public long insertNote(String noteContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_CONTENT, noteContent);
        // will get a number which is the record id of the table for the record inserted
        long result = db.insert(TABLE_NOTE, null, values);
        db.close();
        Log.d("SQL Insert", "ID:" + result); // id returned, shouldn't be -1
        return result;
    }

    // Update note
    public int updateNote(Note data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_CONTENT, data.getNoteContent());
        String condition = COLUMN_ID + "=?"; // each ? is substituted by a string
        // will get a number representing number of rows affected in the table
        String[] args = {String.valueOf(data.getId())};
        int result = db.update(TABLE_NOTE, values, condition, args);
        db.close();
        return result;
    }

    // Delete note
    public int deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "=?";
        // will get a number representing number of rows affected in the table
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_NOTE, condition, args);
        db.close();
        return result;
    }

    // Retrieve data from database
    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT " + COLUMN_ID + "," + COLUMN_NOTE_CONTENT + " FROM " + TABLE_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();

        // retrieve only records that contains a given keyword
//        String[] columns = {COLUMN_ID, COLUMN_NOTE_CONTENT};
//        String condition = COLUMN_NOTE_CONTENT + "Like ?";
//        String[] args = {"%" + keyword + "%"};
//        Cursor cursor = db.query(TABLE_NOTE, columns, condition, args, null, null,null, null);

        Cursor cursor = db.rawQuery(selectQuery, null); // the top code is the proper method for instead of this line
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                Note note = new Note(id, noteContent);
                notes.add(note);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }
}
