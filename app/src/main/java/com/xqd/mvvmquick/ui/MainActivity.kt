package com.xqd.mvvmquick.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import coil.Coil
import coil.imageLoader
import coil.load
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.android.material.snackbar.Snackbar
import com.xqd.mvvmquick.R
import com.xqd.mylibrary.utlis.AppUtil
import com.xqd.mylibrary.utlis.DataStoreUtil
import com.xqd.mylibrary.utlis.DateUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var actionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = "标题"
        toolbar.subtitle = "副标题"
        setSupportActionBar(toolbar)
        actionBar = supportActionBar!!

//        actionBar.title = "actionBar标题"
//        actionBar.subtitle = "actionBar副标题"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        actionBar.setLogo(R.mipmap.ic_launcher)
        actionBar.setDisplayUseLogoEnabled(false)
        actionBar.navigationMode = ActionBar.NAVIGATION_MODE_LIST
//        actionBar.hide()

        numberPicker.maxValue = 100
        numberPicker.minValue = 1

        button.setOnClickListener { getTime() }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        DataStoreUtil.init(this)
//        GlobalScope.launch {
//            DataStoreUtil.saveLong("type", 1589)
//        }

        GlobalScope.launch {
            Log.e("DataStore", DataStoreUtil.getLong("type").toString())
        }

//        imageView.load("https://photo.328ym.com/01ae73af6cd24356ab168b56ad268a0d")
        imageView.load("https://phototest.328ym.com/366a5a720f95474183654046bff77ea1") {
            crossfade(true)
            placeholder(R.mipmap.ic_launcher)
            transformations(CircleCropTransformation())
//            transformations(BlurTransformation(this@MainActivity,15f),RoundedCornersTransformation(10f,10f,10f,10f))
//            transformations(RoundedCornersTransformation(10f,10f,10f,10f))
        }


    }

    private fun getTime() {
        var calendar = Calendar.getInstance()
        //日期选择器
//        var datePickerDialog = DatePickerDialog(
//            this,
//            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth -> },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//        datePickerDialog.show()

        //时间选择器
        var timePickerDialog = TimePickerDialog(
            this, { view, hourOfDay, minute -> },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("1")
        menu.add("2")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }


}