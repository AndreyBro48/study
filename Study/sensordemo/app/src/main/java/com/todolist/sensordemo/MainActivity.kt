package com.todolist.sensordemo

import java.util.ArrayList
import android.app.ListActivity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.hardware.SensorEvent




class MainActivity : ListActivity(), SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
         //To change body of created functions use File | Settings | File Templates.
    }

    var msensorManager: SensorManager? = null //Менеджер сенсоров аппрата

    var rotationMatrix: FloatArray? = null     //Матрица поворота
    var accelData: FloatArray? = null           //Данные с акселерометра
    var magnetData: FloatArray? = null       //Данные геомагнитного датчика
    var OrientationData: FloatArray? = null //Матрица положения в пространстве

    var xyView: TextView? = null
    var xzView: TextView? = null
    var zyView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        msensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        rotationMatrix = FloatArray(16)
        accelData = FloatArray(3)
        magnetData = FloatArray(3)
        OrientationData = FloatArray(3)

        xyView = findViewById(R.id.xyValue)
        xzView = findViewById(R.id.xzValue)
        zyView = findViewById(R.id.zyValue)

        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        msensorManager?.registerListener(
            this,
            msensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )
        msensorManager?.registerListener(
            this,
            msensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_UI
        )
    }
    override fun onPause() {
        super.onPause()
        msensorManager?.unregisterListener(this)
    }
    private fun loadNewSensorData(event: SensorEvent) {
        val type = event.sensor.type //Определяем тип датчика
        if (type == Sensor.TYPE_ACCELEROMETER) { //Если акселерометр
            accelData = event.values.clone()
        }

        if (type == Sensor.TYPE_MAGNETIC_FIELD) { //Если геомагнитный датчик
            magnetData = event.values.clone()
        }
    }
    override fun onSensorChanged(event: SensorEvent) {
        loadNewSensorData(event) // Получаем данные с датчика
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelData,
            magnetData
        ) //Получаем матрицу поворота
        SensorManager.getOrientation(
            rotationMatrix,
            OrientationData
        ) //Получаем данные ориентации устройства в пространстве

        if (xyView == null || xzView == null || zyView == null) {  //Без этого работать отказалось.
            xyView = findViewById(R.id.xyValue) as TextView
            xzView = findViewById(R.id.xzValue) as TextView
            zyView = findViewById(R.id.zyValue) as TextView
        }

        //Выводим результат
        xyView!!.setText(Math.round(Math.toDegrees(OrientationData!![0].toDouble())).toString())
        xzView!!.setText(Math.round(Math.toDegrees(OrientationData!![1].toDouble())).toString())
        zyView!!.setText(Math.round(Math.toDegrees(OrientationData!![2].toDouble())).toString())
    }
}
