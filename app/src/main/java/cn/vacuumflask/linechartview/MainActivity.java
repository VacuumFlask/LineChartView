package cn.vacuumflask.linechartview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineChartView lineChartView = (LineChartView) findViewById(R.id.lineChartView);
        //自己搞点数据
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(0, 0);
        hashMap.put(10, 20);
        hashMap.put(20, 90);
        hashMap.put(30, 40);
        hashMap.put(40, 90);
        hashMap.put(50, 80);
        hashMap.put(60, 90);
        hashMap.put(70, 70);
        lineChartView.setCoordinatePoints(hashMap);
    }
}
