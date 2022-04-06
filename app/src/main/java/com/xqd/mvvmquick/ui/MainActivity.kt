package com.xqd.mvvmquick.ui

import android.Manifest
import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.xqd.mvvmquick.R
import com.xqd.mylibrary.utlis.DataStoreUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.abs
import android.hardware.SensorManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var actionBar: ActionBar
    private lateinit var mSensorManager: SensorManager
    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null
    private var orentation: FloatArray? = null
    private var degrees: ArrayList<Double> = ArrayList()
    private var yAngleDegreesOld: ArrayList<Double> = ArrayList()
    private var yAngleDegrees: ArrayList<Double> = ArrayList()
    private var yAngleDegreesFirst: ArrayList<Double> = ArrayList()

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

        button.setOnClickListener {
            jump()
        }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
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

//        val request = ImageRequest.Builder(this)
//            .data("https://phototest.328ym.com/366a5a720f95474183654046bff77ea1")
//            .transformations(CircleCropTransformation(),GrayscaleTransformation())
//            .target { drawable ->
//                imageView.setImageDrawable(drawable)
//            }
//            .build()
//        this.imageLoader.enqueue(request)

        addViews()

        var oneToTen = Array(10, { i -> i + 1 })
        oneToTen.count()

        //获取传感器服务
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        registerSensor()
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
            this, { view, hourOfDay, minute -> }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        )
        timePickerDialog.show()
    }

    private fun jump() {

        // 通过包名获取要跳转的app，创建intent对象
//        val intent = packageManager.getLaunchIntentForPackage("android.youma.com")
//        // 这里如果intent为空，就说名没有安装要跳转的应用嘛
//        if (intent != null) {
//            // 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
//            intent.putExtra("name", "Liu xiang")
//            intent.putExtra("birthday", "1983-7-13")
//            startActivity(intent)
//
//        } else {
//            // 没有安装要跳转的app应用，提醒一下
//            Toast.makeText(applicationContext, "哟，赶紧下载安装这个APP吧", Toast.LENGTH_LONG).show()
//        }

        val intent = Intent()
        val bundle = Bundle()
        bundle.putString("FIRST_APP_KEY", "你好 ，MainActivity")
        intent.putExtras(bundle)
        intent.component = ComponentName(
            "android.youma.com", "android.youma.com.ui.activity.WelcomeActivity"
        )
        startActivity(intent)
    }

    private fun addViews() {
        val view1 = layoutInflater.inflate(R.layout.item_view, null, false)
        flContain.addView(view1)
        val params = view1.layoutParams as FrameLayout.LayoutParams
        val random = Random()
        val left = abs(random.nextInt() % 800)
        val top = abs(random.nextInt() % 800)
        val right = abs(random.nextInt() % 800)
        val bottom = abs(random.nextInt() % 800)
        Log.e("margins", "left" + left + "top" + top + "right" + right + "bottom" + bottom)
        params.setMargins(left, top, right, bottom)
        view1.layoutParams = params
    }


    private fun registerSensor() {
        /** 方向传感器需要使用加速度传感器和磁场传感器（这个可以不需要），不然的话获取y、z的数据不能变化，这里必须注册以下传感器
         * android 4.0系统摩托罗拉 defy 测试至少需要加速度传感器才可以正常获取y、z的数据
         * */
//        mSensorManager.registerListener(
//            this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME
//        )
        mSensorManager.registerListener(
            this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME
        )
//        mSensorManager.registerListener(
//            this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME
//        )
    }

    override fun onSensorChanged(event: SensorEvent) {
//        Log.e("方向传感器》", "event.sensor.getType():" + event.sensor.getType())
        when (event.sensor.getType()) {
            Sensor.TYPE_ACCELEROMETER -> gravity = event.values
            Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values
            Sensor.TYPE_ORIENTATION -> orentation = event.values
        }
//        if(orentation!=null){
//            Log.e("方向传感器》", " xAngle:" + orentation!![0])
//            Log.e("方向传感器》", "yAngle:" + orentation!![1])
//            Log.e("方向传感器》", " zAngle:" + orentation!![2])
//        }

        //记录rotationMatrix矩阵值
        val r = FloatArray(9)
        //记录通过getOrientation()计算出来的方位横滚俯仰值
        val values = FloatArray(3)

        if (gravity != null) {
            if (Utils.getRotationMatrix(r, gravity)) {
                SensorManager.getOrientation(r, values)
                var yAngle = Math.toDegrees(values[1].toDouble())
                var zAngle = Math.toDegrees(values[2].toDouble())
                Log.e("方向传感器》", "yAngle:" + yAngle)
                Log.e("方向传感器》", "zAngle:" + zAngle)

                degrees.add(yAngle)
                yAngleDegreesOld.add(yAngle)
                if (degrees.size >= 5) {
                    degrees = Utils.optimizePointsThree(degrees, degrees.size)
                    yAngle = degrees[degrees.size - 1]
                    yAngleDegreesFirst.add(degrees[0])
                    degrees.removeAt(0)
                }
                yAngleDegrees.add(yAngle)
            }
        }


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
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
    //    @Override
    //    public void onWindowFocusChanged(boolean hasFocus) {
    //        super.onWindowFocusChanged(hasFocus);
    //        if(hasFocus){
    //            mFragments[index].setImeerStatusBar();
    //        }
    //    }



}