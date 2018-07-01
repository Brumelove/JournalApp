/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.journal.app.utils.NotificationUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class ReminderIntentService extends IntentService {

    String TaskTitle="";String TaskPrority="";
    public ReminderIntentService() {
        super("ReminderIntentService");
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        // Getting Notification Service
        if (intent != null) {
            Bundle b = intent.getExtras();
             TaskTitle = b.getString("TaskTitle");
             TaskPrority = b.getString("TaskPrority");
            int id = b.getInt("id");


        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        new NotificationUtils().remindUserBecauseCharging(getApplicationContext(), TaskTitle,  TaskPrority);
    }
}