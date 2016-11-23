package com.example.pustikom.adapterplay.com.example.pustikom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pustikom.adapterplay.com.example.pustikom.user.Student;
import com.example.pustikom.adapterplay.com.example.pustikom.user.StudentList;

/**
 * Created by Gregorius Andito on 11/11/2016.
 */

public class StudentDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="college.db";
    public static final String DATABASE_VERSION="1";
    private StudentDBHelper db;


    public StudentDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_STUDENT_TABLE =  "CREATE TABLE " + StudentContract.TABLE_NAME + " ("
                + StudentContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StudentContract.COLUMN_STUDENT_NAME + " TEXT NOT NULL, "
                + StudentContract.COLUMN_STUDENT_NIM + " TEXT, "
                + StudentContract.COLUMN_STUDENT_MAIL + " TEXT, "
                + StudentContract.COLUMN_STUDENT_GENDER + " INTEGER, "
                + StudentContract.COLUMN_STUDENT_PHONE + " TEXT);";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //empty
    }

    public void insertStudent(SQLiteDatabase db, Student student) {
        ContentValues values = new ContentValues();
        values.put(StudentContract.COLUMN_STUDENT_NIM,student.getNoreg());
        values.put(StudentContract.COLUMN_STUDENT_NAME,student.getName());
        values.put(StudentContract.COLUMN_STUDENT_MAIL,student.getMail());
        values.put(StudentContract.COLUMN_STUDENT_GENDER,student.getGender());
        values.put(StudentContract.COLUMN_STUDENT_PHONE,student.getPhone());
        db.insert(StudentContract.TABLE_NAME,null,values);
    }

    public void updateStudent(SQLiteDatabase db, Student student) {
        ContentValues values = new ContentValues();
        values.put(StudentContract.COLUMN_STUDENT_NIM,student.getNoreg());
        values.put(StudentContract.COLUMN_STUDENT_NAME,student.getName());
        values.put(StudentContract.COLUMN_STUDENT_MAIL,student.getMail());
        values.put(StudentContract.COLUMN_STUDENT_GENDER,student.getGender());
        values.put(StudentContract.COLUMN_STUDENT_PHONE,student.getPhone());

        String selection = StudentContract._ID+ " = ?, ";
        String[] selectionArgs = { student.getId()+"" };

        db.update(StudentContract.TABLE_NAME,values,selection,selectionArgs);
    }

    public void deleteStudent(SQLiteDatabase db, Student student) {
        ContentValues values = new ContentValues();
        values.put(StudentContract.COLUMN_STUDENT_NIM,student.getNoreg());
        values.put(StudentContract.COLUMN_STUDENT_NAME,student.getName());
        values.put(StudentContract.COLUMN_STUDENT_MAIL,student.getMail());
        values.put(StudentContract.COLUMN_STUDENT_GENDER,student.getGender());
        values.put(StudentContract.COLUMN_STUDENT_PHONE,student.getPhone());

        String selection = StudentContract._ID+ " = ?, ";
        String[] selectionArgs = { student.getId()+"" };

        db.delete(StudentContract.TABLE_NAME,selection,selectionArgs);
    }

    public void truncateTable(SQLiteDatabase db) {
        // Delete all data
        String SQL_TRUNCATE_TABLE =  "DELETE FROM " + StudentContract.TABLE_NAME;

        // Clear Cache
        String SQL_VACUUM = "VACUUM";

        // Execute the SQL statement
        db.execSQL(SQL_TRUNCATE_TABLE);
        db.execSQL(SQL_VACUUM);
    }

    public void getData(SQLiteDatabase db) {

        String[] projection = {
          StudentContract._ID,
                StudentContract.COLUMN_STUDENT_NAME,
                StudentContract.COLUMN_STUDENT_GENDER,
                StudentContract.COLUMN_STUDENT_PHONE,
                StudentContract.COLUMN_STUDENT_MAIL,
                StudentContract.COLUMN_STUDENT_NIM
        };

        Cursor cursor = db.query(
                StudentContract.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);

        try {
            int idColumnIndex = cursor.getColumnIndex(StudentContract._ID);
            int nameColumnIndex = cursor.getColumnIndex(StudentContract.COLUMN_STUDENT_NAME);
            int genderColumnIndex = cursor.getColumnIndex(StudentContract.COLUMN_STUDENT_GENDER);
            int mailColumnIndex = cursor.getColumnIndex(StudentContract.COLUMN_STUDENT_MAIL);
            int nimColumnIndex = cursor.getColumnIndex(StudentContract.COLUMN_STUDENT_NIM);
            int phoneColumnIndex = cursor.getColumnIndex(StudentContract.COLUMN_STUDENT_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                String currentMail = cursor.getString(mailColumnIndex);
                String currentNim = cursor.getString(nimColumnIndex);
                String currentPhone = cursor.getString(phoneColumnIndex);

                StudentList studentList = new StudentList();
                studentList.add(new Student(currentNim,currentName,currentGender,currentMail,currentPhone));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


}
