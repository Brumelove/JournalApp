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

/**
 *Created by brume on 6/28/18.
 */
public class ToDoData {
    int ToDoID;
    String ToDoTaskDetails, ToDoTaskPrority, ToDoTaskStatus, ToDoNotes;

    public int getToDoID() {
        return ToDoID;
    }

    public void setToDoID(int toDoID) {
        ToDoID = toDoID;
    }

    public String getToDoTaskDetails() {
        return ToDoTaskDetails;
    }

    public void setToDoTaskDetails(String toDoTaskDetails) {
        ToDoTaskDetails = toDoTaskDetails;
    }

    public String getToDoTaskPrority() {
        return ToDoTaskPrority;
    }

    public void setToDoTaskPrority(String toDoTaskPrority) {
        ToDoTaskPrority = toDoTaskPrority;
    }

    public String getToDoTaskStatus() {
        return ToDoTaskStatus;
    }

    public void setToDoTaskStatus(String toDoTaskStatus) {
        ToDoTaskStatus = toDoTaskStatus;
    }

    public String getToDoNotes() {
        return ToDoNotes;
    }

    public void setToDoNotes(String toDoNotes) {
        ToDoNotes = toDoNotes;
    }

    @Override
    public String toString() {
        return "ToDoData {id-" + ToDoID + ", taskDetails-" + ToDoTaskDetails + ", propity-" + ToDoTaskPrority + ", status-" + ToDoTaskStatus + ", notes-" + ToDoNotes + "}";
    }

}
