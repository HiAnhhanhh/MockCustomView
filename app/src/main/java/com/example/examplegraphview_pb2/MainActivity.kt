package com.example.examplegraphview_pb2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.examplegraphview_pb2.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val circularVew = binding.circularProgress

        binding.graphView.setData(generateRandomDataPoints())

        binding.changeProgressBtn.setOnClickListener {
            val progress = Random().nextInt(240)
            circularVew.setProgress(progress/240f)
        }
    }
    private fun generateRandomDataPoints(): List<DataPoint> {
        val random = Random()
        val data = DataPoint("26/05/2021", random.nextInt(50))
        val data1 = DataPoint("27/05/2021", random.nextInt(50))
        val data2 = DataPoint("28/05/2021", random.nextInt(50))
        val data3 = DataPoint("29/05/2021", random.nextInt(50))
        val data4 = DataPoint("30/05/2021", random.nextInt(50))
        val data5 = DataPoint("31/05/2021",  random.nextInt(50))
        val data6 = DataPoint("01/06/2021",  random.nextInt(50))
        val data7 = DataPoint("02/06/2021",  random.nextInt(50))
        val data8 = DataPoint("03/06/2021",  random.nextInt(50))
        val data9 = DataPoint("04/06/2021",  random.nextInt(50))
        val data10 = DataPoint("05/06/2021",  random.nextInt(50))

        val DataPoint = listOf(data, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10)
//        val random = Random()
//        return (0..9).map { data ->
//            DataPoint(data, random.nextInt(50))
//        }
        return DataPoint
    }
}