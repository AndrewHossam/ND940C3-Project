package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private val fileName by lazy {
        intent.getIntExtra(fileNameExtra, 0)
    }

    private val isSuccess by lazy {
        intent.getBooleanExtra(isSuccessExtra, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        tvFileNameValue.setText(fileName)
        if (isSuccess) {
            tvStatusValue.text = getString(R.string.success)
        } else {
            tvStatusValue.text = getString(R.string.fail)
            tvStatusValue.setTextColor(R.attr.colorError)
        }

    }

}
