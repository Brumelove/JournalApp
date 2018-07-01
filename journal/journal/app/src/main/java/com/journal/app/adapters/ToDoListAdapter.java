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
package com.journal.app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.journal.app.R;
import com.journal.app.model.SqliteHelper;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 *
 */
    public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {
        List<ToDoData> ToDoDataArrayList = new ArrayList<ToDoData>();
        Context context;

        public ToDoListAdapter(String details) {
            ToDoData toDoData = new ToDoData();
            toDoData.setToDoTaskDetails(details);
            ToDoDataArrayList.add(toDoData);
        }

        public ToDoListAdapter(ArrayList<ToDoData> toDoDataArrayList, Context context) {
            this.ToDoDataArrayList = toDoDataArrayList;
            this.context = context;
        }

        @Override
        public ToDoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
            ToDoListViewHolder toDoListViewHolder = new ToDoListViewHolder(view, context);
            return toDoListViewHolder;
        }

        @Override
        public void onBindViewHolder(ToDoListViewHolder holder, final int position) {
        final ToDoData td = ToDoDataArrayList.get(position);
        holder.todoDetails.setText(td.getToDoTaskDetails());
        holder.todoNotes.setText(td.getToDoNotes());
        String tdStatus = td.getToDoTaskStatus();
        if (tdStatus.matches("Complete")) {
            holder.todoDetails.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        String type = td.getToDoTaskPrority();
        String priority = "";
        if (type.matches("Normal")) {
            priority="Priority : Normal";
        } else if (type.matches("Low")) {
            priority="Priority : Low";;
        } else {
            priority="Priority : High";
        }
        holder.importance.setText(priority);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
            int id = td.getToDoID();
            SqliteHelper mysqlite = new SqliteHelper(view.getContext());
            Cursor b = mysqlite.deleteTask(id);
            if (b.getCount() == 0) {

            new Handler().post(new Runnable() {
            @Override
            public void run() {

            new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("CONFIRM")
            .setContentText("Yes i want to delete")
            .setConfirmText("OK")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
            sDialog.dismissWithAnimation();
            ToDoDataArrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,ToDoDataArrayList.size());
            notifyDataSetChanged();
                Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
            })
            .setCancelButton("Exit", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
            sDialog.dismissWithAnimation();
            }
            })
            .show();


                // Code here will run in UI thread
             /**/
            }
            });
            }
            else
            {
                Toast.makeText(view.getContext(), "Deleted else", Toast.LENGTH_SHORT).show();
            }


            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            // create alert dialog
            final AlertDialog Dialog = alertDialogBuilder.create();

            // show it
            Dialog.show();
            Dialog.getWindow().setBackgroundDrawableResource(R.drawable.roundedittext);

            if (td.getToDoTaskPrority().matches("Normal")) {
                rbNormal.setChecked(true);
            } else if (td.getToDoTaskPrority().matches("Low")) {
                rbLow.setChecked(true);
            } else {
                rbHigh.setChecked(true);
            }
            if (td.getToDoTaskStatus().matches("Complete")) {
                cb.setChecked(true);
            }
            todoText.setText(td.getToDoTaskDetails());
            todoNote.setText(td.getToDoNotes());
            Button save = (Button) dialog.findViewById(R.id.btn_save);
            Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
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
            EditText todoNote = (EditText) dialog.findViewById(R.id.input_task_notes);
            CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);

            if (todoText.getText().length() >= 2)
            {
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

            ToDoData updateTd = new ToDoData();
            updateTd.setToDoID(td.getToDoID());
            updateTd.setToDoTaskDetails(todoText.getText().toString());
            updateTd.setToDoTaskPrority(RadioSelection);
            updateTd.setToDoNotes(todoNote.getText().toString());

            if (cb.isChecked())
            {
                updateTd.setToDoTaskStatus("Complete");
            }
            else
             {
                updateTd.setToDoTaskStatus("Incomplete");
            }
            SqliteHelper mysqlite = new SqliteHelper(view.getContext());
            Cursor b = mysqlite.updateTask(updateTd);
            ToDoDataArrayList.set(position, updateTd);

            if (b.getCount() == 0) {

            new Handler().post(new Runnable() {
            @Override
            public void run() {

            notifyDataSetChanged();
            }
            });
            Dialog.hide();
            } else {


            Dialog.hide();

            }

            }
            else
            {
            Toast.makeText(view.getContext(), "Please enter To Do Task", Toast.LENGTH_SHORT).show();

            }
            }
            });
            }
        });


        }



        @Override
        public int getItemCount() {
            return ToDoDataArrayList.size();
        }

        public class ToDoListViewHolder extends RecyclerView.ViewHolder {
            TextView todoDetails, todoNotes;
            TextView importance;
            ImageView edit, deleteButton;
            ToDoData toDoData;

            public ToDoListViewHolder(View view, final Context context) {
            super(view);
            todoDetails = (TextView) view.findViewById(R.id.input_task_desc);
            todoNotes = (TextView) view.findViewById(R.id.toDoTextNotes);
            importance = (TextView) view.findViewById(R.id.priority);
            edit = (ImageView) view.findViewById(R.id.edit);
            deleteButton = (ImageView) view.findViewById(R.id.delete);
            view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
            });
            }
        }
        }
