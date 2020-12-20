package com.example.datepickertest;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    Button pressMe, updateValues;
    EditText e1,e2,e3,e4,e5,e6;
    List<String> valuesList ;
    List<EditText> editTexts;
    FirebaseDatabase database ;
    DataTable dataTable;
    DataTableHeader header;
    ArrayList<DataTableRow> rows;
    DataTableRow row;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this, MainActivity.this, 2020, 12, 16);

         pressMe = (Button) findViewById(R.id.buttonPressMe);
         updateValues = (Button) findViewById(R.id.buttonUpdateValues);
         database = FirebaseDatabase.getInstance();

        valuesList =  new ArrayList<String>();
        editTexts = new ArrayList<EditText>();


        dataTable = findViewById(R.id.data_table);

        header = new DataTableHeader.Builder()
                .item("Hour", 1)
                .item("Value", 1)
                .build();


        rows = new ArrayList<>();
        // define 200 fake rows for table


//
//         e1 = (EditText) findViewById(R.id.editTextNumber1);
//         e2 = (EditText) findViewById(R.id.editTextNumber2);
//         e3 = (EditText) findViewById(R.id.editTextNumber3);
//         e4 = (EditText) findViewById(R.id.editTextNumber4);
//         e5 = (EditText) findViewById(R.id.editTextNumber5);
//         e6 = (EditText) findViewById(R.id.editTextNumber6);
//
//        editTexts.add(e1);
//        editTexts.add(e2);
//        editTexts.add(e3);
//        editTexts.add(e4);
//        editTexts.add(e5);
//        editTexts.add(e6);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getShiftData(1, pressMe.getText().toString());
            }
        });



        pressMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });



        updateValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                getValuesFromEditText(editTexts,6);

                for (int i = 1 ; i < 7 ; i++){

                  //  updateShiftOneData(i, Integer.parseInt(valuesList.get(i)) , 3 , pressMe.getText().toString());


                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    void getValuesFromEditText(List<EditText> es , int loopTimes){

        valuesList.clear();
        for (EditText e : es){

            valuesList.add(e.getText().toString());

        }

        Log.d("EditTextValues", String.valueOf(valuesList));



    }

    void updateShiftOneData(int hour , int value , int shift , String dateText){

        String hourString = String.valueOf(hour);
        String shiftString = String.valueOf(shift);

        DatabaseReference reference = database.getReference("Dates").child(dateText).child(shiftString).child(hourString);
        reference.setValue(value);

    }



    void getShiftData(int shift , String dateText){


        String shiftString = String.valueOf(shift);

        DatabaseReference reference = database.getReference("Dates").child(dateText).child(shiftString);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){


                    Log.d("datasnapshot",ds.getValue().toString());
                    valuesList.add(ds.getValue().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for(int i=1;i<9;i++) {


            row = new DataTableRow.Builder()
                    .value("Hour #" + i)
                    .value(String.valueOf(i))
                    .build();
            rows.add(row);
        }

        //dataTable.setTypeface(typeface);
        dataTable.setHeader(header);
        dataTable.setRows(rows);

        dataTable.inflate(MainActivity.this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {



        pressMe.setText(datePicker.getDayOfMonth()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getYear());



    }
}