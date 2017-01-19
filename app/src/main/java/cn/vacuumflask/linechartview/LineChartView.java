package cn.vacuumflask.linechartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/1/18 0018.
 * 折线图
 */
public class LineChartView extends View {

    //坐标轴
    private Paint paintXY;
    //划线
    private Paint paintLine;
    //开始坐标
    private int startX;
    private int startY;
    //x坐标轴
    private int xAxleX;
    private int xAxleY;
    //y坐标轴
    private int yAxleX;
    private int yAxleY;
    //x轴集合
    private List<Integer> xList;
    //y轴集合
    private List<Integer> yList;
    //点的合集
    private TreeMap<Integer, Integer> coordinatePoints;

    private String TAG = "LineChartView";

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //轴坐标画笔
        paintXY = new Paint();
        paintXY.setAntiAlias(true);
        paintXY.setColor(Color.BLACK);
        paintXY.setStrokeWidth(1.8f);
        paintXY.setStyle(Paint.Style.FILL);
        //折线画笔
        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.RED);
        paintLine.setStrokeWidth(2.0f);
        paintLine.setStyle(Paint.Style.FILL);
    }

    /**
     * 设置坐标点
     *
     * @param coordinatePoints Map对象，Key为X，Value为Y
     */
    public void setCoordinatePoints(Map<Integer, Integer> coordinatePoints) {

        if (coordinatePoints == null || coordinatePoints.size() == 0) {
            return;
        }
        if (this.coordinatePoints == null) {
            this.coordinatePoints = new TreeMap<>();
        } else {
            this.coordinatePoints.clear();
        }
        //因为Map是无序的要拆分成有序的（TreeMap 会自动默认键值升序 来排序）
        Set<Map.Entry<Integer, Integer>> entrySet = coordinatePoints.entrySet();
        for (Map.Entry<Integer, Integer> entry : entrySet) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            this.coordinatePoints.put(key, value);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量控件
        int minimumWidth = getSuggestedMinimumWidth();
        int minimumHeight = getSuggestedMinimumHeight();
        setMeasuredDimension(measureValue(minimumWidth, widthMeasureSpec), measureValue(minimumHeight, heightMeasureSpec));
    }

    private int measureValue(int minimumValue, int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, minimumValue);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = minimumValue;
                break;
        }
        return result;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //起始坐标
        startX = 0;
        startY = h;
        //X轴
        xAxleX = w;
        xAxleY = startY;
        //Y轴
        yAxleX = startX;
        yAxleY = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //先画坐标轴
        canvas.drawLine(startX, startY, xAxleX, xAxleY, paintXY);//X轴
        canvas.drawLine(startX, startY, yAxleX, yAxleY, paintXY);//y轴


        if (coordinatePoints == null || coordinatePoints.size() == 0) {
            return;
        }

        //设置数据
        setData();

        //画点--连线
        int startX;//连线起始X
        int startY;//连线起始y
        for (int i = 0; i < xList.size() - 1; i++) {
            startX = xList.get(i);
            startY = yList.get(i);
            Integer x = xList.get(i + 1);
            Integer y = yList.get(i + 1);
            canvas.drawLine(startX, startY, x, y, paintLine);
        }

    }


    private void setData() {
        //简单判空
        if (xList == null) {
            xList = new ArrayList<>();
        } else {
            xList.clear();
        }
        if (yList == null) {
            yList = new ArrayList<>();
        } else {
            yList.clear();
        }

        int maxX = Math.abs(xAxleX - startX);//X轴的长
        int maxY = Math.abs(yAxleY - startY);//Y轴的长
        int sumX = 0;//X轴上的总值
        int sumY = 0;//Y轴上的总值
        ArrayList<Integer> xValue = new ArrayList<>();
        ArrayList<Integer> yValue = new ArrayList<>();
        //将点拆分X轴和Y轴的集合
        Set<Map.Entry<Integer, Integer>> entrySet = coordinatePoints.entrySet();
        for (Map.Entry<Integer, Integer> entry : entrySet) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            sumX = Math.max(key, sumX);
            sumY = Math.max(value, sumY);
            xValue.add(key);
            yValue.add(value);
        }

        int size = entrySet.size();
        for (int i = 0; i < size; i++) {
            //X轴的位置
            Integer integerX = xValue.get(i);
            int x = integerX * maxX / sumX;
            xList.add(x);
            //Y轴的位置
            Integer integerY = yValue.get(i);
            int y = integerY * maxY / sumY;
            yList.add((maxY - y));//因为Y轴是反过来的
        }
    }
}
