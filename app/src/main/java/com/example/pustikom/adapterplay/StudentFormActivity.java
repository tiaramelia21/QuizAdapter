package com.example.pustikom.adapterplay;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.pustikom.adapterplay.com.example.pustikom.db.StudentDBHelper;
import com.example.pustikom.adapterplay.com.example.pustikom.user.Student;

/**
 * Created by eka on 04/11/16.
 */

public class StudentFormActivity extends AppCompatActivity {
    private AppCompatSpinner genderSpinner;
    private int actionMode;
    private Student student;
    private static final String ADD_MODE="Add Student";
    private static final String EDIT_MODE="Edit Student";
    private EditText nimText, nameText, mailText, phoneText;
    private StudentDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        genderSpinner = (AppCompatSpinner) findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this,R.array.gender_array,R.layout.support_simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        Intent intent = getIntent();

        //register Views
        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.floatingSaveButton);
        FloatingActionButton cancelButton = (FloatingActionButton) findViewById(R.id.floatingCancelButton);
        nimText = (EditText) findViewById(R.id.edit_nim);
        nameText = (EditText) findViewById(R.id.edit_nama);
        mailText = (EditText) findViewById(R.id.edit_email);
        phoneText = (EditText) findViewById(R.id.edit_phone);

        //setup listener
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        actionMode = intent.getIntExtra("mode",0);
        switch (actionMode){
            case 0:
                setTitle("Add Student");
                break;
            case 1:
                setTitle("Edit Student");
                //Todo: case edit preload all edit text with passed data
                student = (Student) intent.getSerializableExtra("Student");
                nimText.setText(student.getNoreg());
                nameText.setText(student.getName());
                mailText.setText(student.getMail());
                phoneText.setText(student.getPhone());
                genderSpinner.setSelection(student.getGender());
                break;
        }

        //setup listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nim = nimText.getText().toString();
                String name = nameText.getText().toString();
                int genderId = genderSpinner.getSelectedItemPosition();
                String mail = mailText.getText().toString();
                String phone = phoneText.getText().toString();
                student = new Student(nim,name,genderId,mail,phone);
                if(validateStudent(student)) {
                    saveStudent(student,actionMode);
                    finish();
                }
            }
        });
    }

    /**
     * Todo: implement validation the criterias are
     * 1. NIM must be all numbers and 8 digits
     * 2. Name must not be empty
     * 3. Any other field are optionals
     * @param student
     * @return true if all field validated, false otherwise
     */
    private boolean validateStudent(Student student){
        //put your code here, set validated to false if input not conformed
        //use nameText.setError("Please input name"); or nimText.setError("NIM must be 8 character"); in case input invalidated
        //change isValidated to false for each error found
        boolean isValidated=true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(student.getName().length()==0){
            nameText.setError("Please input name");
            isValidated=false;
        }

        if(student.getNoreg().length()!=10) {
            nimText.setError("NIM must be 10 character");
            isValidated=false;
        }

        if(student.getPhone().length() != 9) {
            phoneText.setError("Phone must be no less than 9 digits");
        }

        if(student.getMail().matches(emailPattern) && student.getMail().length() > 0){
            mailText.setError("Wrong mail format!");
        }

        return isValidated;

    }

    /**
     * Todo: implement save data
     * @param student
     */
    private void saveStudent(Student student,int mode){
        SQLiteDatabase wdb = db.getWritableDatabase();
        if(mode==0){
            //add current student to global StudentList
           db.insertStudent(wdb,student);
//            Student.getStudentList().add(student.getId(),student);
        } else{
            db.updateStudent(wdb,student);
//            Student.getStudentList().set(student.getId(),student);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        //TODO: Only load menu when in edit Mode
        if(actionMode==1)
            inflater.inflate(R.menu.edit_student_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.deleteStudentItem:
                //Todo: Implement action for delete student
//                int id=student.getId();
//                Student.getStudentList().remove(id);
                SQLiteDatabase wdb = db.getWritableDatabase();
                db.deleteStudent(wdb,student);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
