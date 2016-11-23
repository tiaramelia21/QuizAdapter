package com.example.pustikom.adapterplay;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pustikom.adapterplay.com.example.pustikom.adapter.StudentArrayAdapter;
import com.example.pustikom.adapterplay.com.example.pustikom.db.StudentContract;
import com.example.pustikom.adapterplay.com.example.pustikom.db.StudentDBHelper;
import com.example.pustikom.adapterplay.com.example.pustikom.user.Student;
import com.example.pustikom.adapterplay.com.example.pustikom.user.StudentList;

/**
 * Created by pustikom on 07/10/16.
 */

public class StudentActivity extends AppCompatActivity {
    private FloatingActionButton addButton;
    private StudentArrayAdapter studentArrayAdapter;
    private ListView listItem;
    private StudentDBHelper studentDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        studentArrayAdapter = new StudentArrayAdapter(this,new StudentList());
        listItem = (ListView) findViewById(R.id.list_item);
        listItem.setAdapter(studentArrayAdapter);

        //register button
        addButton  = (FloatingActionButton) findViewById(R.id.floatingAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentActivity.this, StudentFormActivity.class);
                intent.putExtra("mode",0);
                startActivity(intent);
            }
        });

        //set listener for each list item
        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StudentActivity.this, StudentFormActivity.class);
                intent.putExtra("mode",1);

                //Todo get current student from StudentList based on position
                Student student = Student.getStudentList().get(position);
                intent.putExtra("Student",student);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        // Refresh list post add or edit student
        //this can be done by reload studentArrayAdapter to current List
        //reload listItems
        studentArrayAdapter = new StudentArrayAdapter(this, Student.getStudentList());
        listItem.setAdapter(studentArrayAdapter);
        StudentList list = Student.getStudentList();
        new DataSyncTask().execute(list);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_menu, menu);
        return true;
    }

    public StudentList populateStudentDummies(){
        SQLiteDatabase db = studentDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StudentContract.COLUMN_STUDENT_NAME,"Tiara");
        values.put(StudentContract.COLUMN_STUDENT_PHONE, "081282003420");
        values.put(StudentContract.COLUMN_STUDENT_GENDER,1);
        values.put(StudentContract.COLUMN_STUDENT_MAIL,"tiara21.amelia@gmail.com");
        values.put(StudentContract.COLUMN_STUDENT_NIM,"3145136211");

        long newRowId = db.insert(StudentContract.TABLE_NAME, null, values);

        StudentList studentList = new StudentList();
        studentList.add(new Student("3145136211","Tiara",1,"tiara21.amelia@gmail.com","081282003420"));

        return studentList;

        // Create a ContentValues object where column names are the keys,

    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.createDummyItem:
                //Todo: add action
                StudentList students = populateStudentDummies();
                studentArrayAdapter = new StudentArrayAdapter(this,students);
                listItem.setAdapter(studentArrayAdapter);
                Student.setStudentList(students);
                return true;
            case R.id.clearListItem:
                //Todo: add action
                SQLiteDatabase wdb = studentDBHelper.getWritableDatabase();
                studentDBHelper.truncateTable(wdb);
                studentArrayAdapter = new StudentArrayAdapter(this, new StudentList());
                listItem.setAdapter(studentArrayAdapter);
                Student.setStudentList(new StudentList());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DataSyncTask extends AsyncTask<StudentList,Void,StudentArrayAdapter>{

        @Override
        protected StudentArrayAdapter doInBackground(StudentList... list) {
            StudentArrayAdapter studentAdapter = new StudentArrayAdapter(getApplicationContext(),list[0]);
            return studentAdapter;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(StudentArrayAdapter studentArrayAdapter) {
            listItem.setAdapter(studentArrayAdapter);
        }
    }


}
