package com.xqd.mvvmquick.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.GsonBuilder
import com.xqd.mvvmquick.R
import kotlinx.android.synthetic.main.activity_degree.*
import java.lang.Exception

/**
 * @Author: XieQD
 * @Date: 2022/4/6 16:05
 * @Description:
 * Powered by GWM
 */
class DegreeActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private var geomagnetic: FloatArray? = null
    private var orentation: FloatArray? = null
    private var degrees: ArrayList<Double> = ArrayList()
    private var yAngleDegreesOld: ArrayList<Double> = ArrayList()
    private var zAngleDegreesOld: ArrayList<Double> = ArrayList()
    private var yAngleDegrees: ArrayList<Double> = ArrayList()
    private var yAngleDegreesFirst: ArrayList<Double> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_degree)

        btSave.setOnClickListener {
            var filePathy = Utils.EXTERNAL_STORAGE + "/Download/" + "角度数据yAngleDegreesOld" + System.currentTimeMillis() + ".txt"
            var filePathz = Utils.EXTERNAL_STORAGE + "/Download/" + "角度数据zAngleDegreesOld" + System.currentTimeMillis() + ".txt"
//            var filePath1 = Utils.EXTERNAL_STORAGE + "/Download/" + "角度数据yAngleDegrees.txt"
//            var filePath2 = Utils.EXTERNAL_STORAGE + "/Download/" + "角度数据yAngleDegreesFirst.txt"
//            var bean = DegreeBean(yAngleDegreesOld, yAngleDegrees, yAngleDegreesFirst)
//            var str = GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create().toJson(bean)

            yAngleDegreesOld.forEach { Utils.writeFile(filePathy, it.toString() + "\n", true) }
            zAngleDegreesOld.forEach { Utils.writeFile(filePathz, it.toString() + "\n", true) }
//            yAngleDegrees.forEach { Utils.writeFile(filePath1, it.toString(), true) }
//            yAngleDegreesFirst.forEach { Utils.writeFile(filePath2, it.toString(), true) }

//            Log.e("传感器数据》",  str)


        }

//        registerSensor()
//        permissionCheck()
        geLocalData()
    }

    private fun geLocalData() {
        val str = Utils.getTextList(this, "acc4.txt")
        str.forEach {
            var accArray = FloatArray(3)
            val x: String
            val y: String
            val z: String
            try {
                x = it.substring(it.indexOf("x:") + 2, it.indexOf(",y"))
                y = it.substring(it.indexOf("y:") + 2, it.indexOf(",z"))
                z = it.substring(it.indexOf("z:") + 2, it.indexOf(",t"))

                accArray[0] = x.toFloat()
                accArray[1] = y.toFloat()
                accArray[2] = z.toFloat()
            } catch (e: Exception) {

            }

            saveDegree(accArray)
        }
    }

    private fun registerSensor() {
        //获取传感器服务
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        /** 方向传感器需要使用加速度传感器和磁场传感器（这个可以不需要），不然的话获取y、z的数据不能变化，这里必须注册以下传感器
         * android 4.0系统摩托罗拉 defy 测试至少需要加速度传感器才可以正常获取y、z的数据
         * */
//        mSensorManager.registerListener(
//            this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME
//        )
        mSensorManager.registerListener(
            this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
//        mSensorManager.registerListener(
//            this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME
//        )
    }

    override fun onSensorChanged(event: SensorEvent) {
//        Log.e("方向传感器》", "event.sensor.getType():" + event.sensor.getType())
        var gravity = FloatArray(3)
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

        saveDegree(gravity)
    }

    private fun saveDegree(gravity: FloatArray?) {
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

//                degrees.add(yAngle)
                yAngleDegreesOld.add(yAngle)
                zAngleDegreesOld.add(zAngle)
//                if (degrees.size >= 5) {
//                    degrees = Utils.optimizePointsThree(degrees, degrees.size)
//                    yAngle = degrees[degrees.size - 1]
//                    yAngleDegreesFirst.add(degrees[0])
//                    degrees.removeAt(0)
//                }
//                yAngleDegrees.add(yAngle)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    data class DegreeBean(
        var yAngleDegreesOld: ArrayList<Double>,
        var yAngleDegrees: ArrayList<Double>,
        var yAngleDegreesFirst: ArrayList<Double>,
    )

    /**
     * 动态权限检查
     */
    private fun permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val mPermissionList = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
            )
            var permissionState = true
            for (permission in mPermissionList) {
                if (ContextCompat.checkSelfPermission(this, permission) !== PackageManager.PERMISSION_GRANTED) {
                    permissionState = false
                }
            }
            if (!permissionState) {
                ActivityCompat.requestPermissions(this, mPermissionList, 0)
            } else {
//                initClient()
            }
        } else {
//            initClient()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionState = true
        if (requestCode == 0) {
            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permissionState = false

                }
            }
            if (permissionState) {
//                initClient()
            }
        }
    }

    override fun onDestroy() {
        mSensorManager.unregisterListener(this)
        super.onDestroy()
    }
}