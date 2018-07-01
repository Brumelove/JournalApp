/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.journal.app.activity;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.journal.app.R;
import com.journal.app.adapters.ToDoData;
import com.journal.app.adapters.ToDoListAdapter;
import com.journal.app.model.SqliteHelper;

import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ToDoMainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    FloatingActionButton addTask;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ToDoData> tdd = new ArrayList<>();
    SqliteHelper mysqlite;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_s);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        addTask = (FloatingActionButton) findViewById(R.id.imageButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        adapter = new ToDoListAdapter(tdd, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accent), getResources().getColor(R.color.divider));

       // onloaddata();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                updateCardView();
            }
        });
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            addtask ( view);

            }
        });
    }
    public void scheduleNotification(long time, String TaskTitle, String TaskPrority) {
        Calendar Calendar_Object = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final int _id = (int) System.currentTimeMillis();
        Intent myIntent = new Intent(ToDoMainActivity.this, Alarm.class);
        myIntent.putExtra("TaskTitle", TaskTitle);
        myIntent.putExtra("TaskPrority",TaskPrority);
        myIntent.putExtra("id",_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ToDoMainActivity.this,
                _id, myIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC, Calendar_Object.getTimeInMillis() + time,
                pendingIntent);

    }
    public void updateCardView() {
        swipeRefreshLayout.setRefreshing(true);
        mysqlite = new SqliteHelper(getApplicationContext());
        Cursor result = mysqlite.selectAllData();
        if (result.getCount() == 0)
        {

           tdd.clear();
            adapter.notifyDataSetChanged();


            new SweetAlertDialog(ToDoMainActivity.this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("No Task")
            .setContentText("Please add a sample Task")
            .setConfirmText("OK")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {


                    onloaddata();
                    sDialog.dismissWithAnimation();
                }
            })
            .setCancelButton("Exit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {


            sDialog.dismissWithAnimation();
               /* Intent in = new Intent(ToDoMainActivity.this, MainActivity.class);
                startActivity(in);
                ToDoMainActivity.this.finish();*/

            }
            })
            .show();

           // Toast.makeText(getApplicationContext(), "No Tasks", Toast.LENGTH_SHORT).show();
        }
        else
        {
        tdd.clear();
        adapter.notifyDataSetChanged();
        while (result.moveToNext()) {
        ToDoData tddObj = new ToDoData();
        tddObj.setToDoID(result.getInt(0));
        tddObj.setToDoTaskDetails(result.getString(1));
        tddObj.setToDoTaskPrority(result.getString(2));
        tddObj.setToDoTaskStatus(result.getString(3));
        tddObj.setToDoNotes(result.getString(4));
        tdd.add(tddObj);
        }
        adapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

      @Override
      public void onRefresh()
      {
        updateCardView();
      }


    public void addtask (View view)
    {

         LayoutInflater li = LayoutInflater.from(view.getContext());
                final View dialog = li.inflate(R.layout.custom_dailog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

                alertDialogBuilder.setView(dialog);

                EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                EditText todoNote = (EditText) dialog.findViewById(R.id.input_task_notes);
                CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);
                RadioButton rbHigh = (RadioButton) dialog.findViewById(R.id.high);
                RadioButton rbNormal = (RadioButton) dialog.findViewById(R.id.normal);
                RadioButton rbLow = (RadioButton) dialog.findViewById(R.id.low);
                Button save = (Button) dialog.findViewById(R.id.btn_save);
                Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                // create alert dialog
                final AlertDialog Dialog = alertDialogBuilder.create();

                // show it
                Dialog.show();
                Dialog.getWindow().setBackgroundDrawableResource(R.drawable.roundedittext);
                cb.setEnabled(false);


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog.dismiss();
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                EditText todoNotes = (EditText) dialog.findViewById(R.id.input_task_notes);

                if (todoText.getText().length() >= 2) {
                RadioGroup proritySelection = (RadioGroup) dialog.findViewById(R.id.toDoRG);
                String RadioSelection = new String();

                if (proritySelection.getCheckedRadioButtonId() != -1)
                {
                int id = proritySelection.getCheckedRadioButtonId();
                View radiobutton = proritySelection.findViewById(id);
                int radioId = proritySelection.indexOfChild(radiobutton);
                RadioButton btn = (RadioButton) proritySelection.getChildAt(radioId);
                RadioSelection = (String) btn.getText();
                }

                Spinner getTime = (Spinner) dialog.findViewById(R.id.spinner);
                EditText timeInNumb = (EditText) dialog.findViewById(R.id.input_task_time);

                if(getTime.getSelectedItem().toString().matches("Days") && !(timeInNumb.getText().toString().matches("")))
                {
                // Convert timeInNumb to Days in Miliseconds
                int longtime = Integer.parseInt(timeInNumb.getText().toString());
                long miliTime = longtime * 24 * 60 * 60 * 1000 ;
                scheduleNotification(miliTime,todoText.getText().toString(),RadioSelection);
                } else if (getTime.getSelectedItem().toString().matches("Minutes") && !(timeInNumb.getText().toString().matches(""))) {
                // Convert timeInNumb to Minutes in Miliseconds
                int longtime = Integer.parseInt(timeInNumb.getText().toString());
                long miliTime = longtime * 60 * 1000 ;
                scheduleNotification(miliTime,todoText.getText().toString(),RadioSelection);
                }
                else if (getTime.getSelectedItem().toString().matches("Hours") && !(timeInNumb.getText().toString().matches("")))
                {
                // Convert timeInNumb to Hours in Miliseconds
                int longtime = Integer.parseInt(timeInNumb.getText().toString());
                long miliTime = longtime * 60 * 60 * 1000 ;
                scheduleNotification(miliTime,todoText.getText().toString(),RadioSelection);
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("ToDoTaskDetails", todoText.getText().toString());
                contentValues.put("ToDoTaskPrority", RadioSelection);
                contentValues.put("ToDoTaskStatus", "Incomplete");
                contentValues.put("ToDoNotes", todoNotes.getText().toString());
                mysqlite = new SqliteHelper(getApplicationContext());
                Boolean b = mysqlite.insertInto(contentValues);
                if (b)
                {
                    Dialog.hide();
                    updateCardView();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter To Do Task", Toast.LENGTH_SHORT).show();
                }


                    }
                });

    }

      public void onloaddata()
      {


              ContentValues contentValues = new ContentValues();
              contentValues.put("ToDoTaskDetails", "Add Task");
              contentValues.put("ToDoTaskPrority", "Normal");
              contentValues.put("ToDoTaskStatus", "Incomplete");
              contentValues.put("ToDoNotes","Please don't leave without adding a task");
              mysqlite = new SqliteHelper(getApplicationContext());
              Boolean b = mysqlite.insertInto(contentValues);
              if (b)
              {
                  updateCardView();
              }



      }


      @Override
      public void onBackPressed() {
          Intent in = new Intent(this, MainActivity.class);
          startActivity(in);
          this.finish();
      }

      }

