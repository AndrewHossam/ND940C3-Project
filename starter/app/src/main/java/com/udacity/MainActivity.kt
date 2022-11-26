package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private val notificationManager: NotificationManager by lazy {
        ContextCompat.getSystemService(applicationContext,
            NotificationManager::class.java) as NotificationManager
    }
    private var downloadFile: DownloadFile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        loadingButton.setOnClickListener {
            loadingButton.buttonState = ButtonState.Clicked
            downloadFile = when (radioGroup.checkedRadioButtonId) {
                btnGlide.id -> {
                    downloadList.component1()
                }
                btnLoadApp.id -> {
                    downloadList.component2()

                }
                btnRetrofit.id -> {
                    downloadList.component3()

                }
                else -> {
                    Toast.makeText(this, "Please select the file to download", Toast.LENGTH_SHORT)
                        .show()
                    null
                }
            }

            downloadFile?.let {
                loadingButton.buttonState = ButtonState.Loading
                download(it.url)
            }
        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Log.i("onReceive", "onReceive: $id")
            loadingButton.isEnabled = true

            if (downloadID == id) {
                if (intent.action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    loadingButton.buttonState = ButtonState.Completed

                    val query = DownloadManager.Query()
                    query.setFilterByStatus(
                        DownloadManager.STATUS_PAUSED or
                                DownloadManager.STATUS_PENDING or
                                DownloadManager.STATUS_RUNNING or
                                DownloadManager.STATUS_SUCCESSFUL
                    )
                    val mgr = context!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    val cur: Cursor = mgr.query(query)
                    val index: Int = cur.getColumnIndex(
                        DownloadManager.COLUMN_STATUS)

                    if (cur.moveToFirst()) {
                        createChannel(
                            CHANNEL_ID,
                            getString(R.string.notification_description)
                        )
                        notificationManager.sendNotification(
                            downloadFile?.name ?: 0,
                            applicationContext,
                            index == DownloadManager.STATUS_SUCCESSFUL
                        )
                    }
                    cur.close()

                }
            }

        }
    }

    private fun download(url: String) {
        loadingButton.isEnabled = false
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }

}
