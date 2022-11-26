/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0
const val CHANNEL_ID = "channelId"
const val isSuccessExtra = "isSuccessExtra"
const val fileNameExtra = "fileNameExtra"

fun NotificationManager.sendNotification(
    @StringRes messageBody: Int,
    applicationContext: Context,
    isSuccess: Boolean
) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java).apply {
        putExtra(isSuccessExtra, isSuccess)
        putExtra(fileNameExtra, messageBody)
    }
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
    )

    val image = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_assistant_black_24dp
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    )

        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(applicationContext.getString(messageBody))
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(image)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .addAction(
            0,
            applicationContext.getString(R.string.notification_button),
            contentPendingIntent
        )

        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())
}

fun Activity.createChannel(channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.description =
            getString(R.string.notification_title)

        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(notificationChannel)

    }
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
