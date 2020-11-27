package com.xqd.mvvmquick.ui

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.postDelayed
import com.google.android.material.snackbar.Snackbar
import com.xqd.mvvmquick.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
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

        //函数式操作符
        val list = listOf(1, 2, 3, 4, 5, 6, 7)
//        val listSlice = list.slice(IntRange(1, 3))//2.3.4（传入的是下标起始点和要截取的元素个数）
        val listSlice = list.slice(listOf(1, 3, 4))//2.4.5（传入的是下标集合）
        listSlice.forEach { Log.e("slice>", it.toString()) }

        val listFilter = list.filter { it % 2 == 0 }
        listFilter.forEach { Log.e("filter>", it.toString()) }
        val listFilterIndex = list.filterIndexed { index, i ->
            i % 2 == 0 && index < 5
        }
        listFilterIndex.forEach { Log.e("filterIndex>", it.toString()) }

        val numberList1 = listOf(23, 65, 14, 57, 99, 123, 26, 15, 88, 37, 56)
        val numberList2 = listOf(13, 55, 24, 67, 93, 137, 216, 115, 828, 317, 16)
        val numberList3 = listOf(20, 45, 19, 7, 9, 3, 26, 5, 38, 75, 46)
        //需要注意一点的是，我们从源码看到filterTo第一个参数destination是一个可变集合类型，所以这里使用的mutableListOf初始化
        val newNumberList = mutableListOf<Int>().apply {
            numberList1.filterTo(this) {
                it % 2 == 0
            }
            numberList2.filterTo(this) {
                it % 2 == 0
            }
            numberList3.filterTo(this) {
                it % 2 == 0
            }
        }
//        newNumberList.sort()
        newNumberList.forEach {
            Log.e("filterTo>", it.toString())
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