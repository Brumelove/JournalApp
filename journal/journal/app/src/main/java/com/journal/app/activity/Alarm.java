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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 *Created by brume on 6/28/18.
 */
public class Alarm extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null) {
            Bundle b = intent.getExtras();
            String TaskTitle = b.getString("TaskTitle");
            String TaskPrority = b.getString("TaskPrority");
            int _id = b.getInt("id");
            Intent myIntent = new Intent(context, ReminderIntentService.class);
            myIntent.putExtra("TaskTitle", TaskTitle);
            myIntent.putExtra("TaskPrority",TaskPrority);
            myIntent.putExtra("id",_id);
            context.startService(myIntent);
        }

    }
}
