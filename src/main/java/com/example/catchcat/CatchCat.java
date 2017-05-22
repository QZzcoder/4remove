package com.example.catchcat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by 王国晟 on 2017/5/22.
 */
public class CatchCat extends View {
    private int radiu;
    private int map[][];

    private final int cat = 1;
    private final int trap = 2;
    private final int mapX = 9;//宽度
    private final int mapY = 9;//高度
    private state currentState = state.START;
    private Paint paint;
    public CatchCat(Context context,AttributeSet attr){
        super(context,attr);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        radiu = dm.widthPixels/(mapX*2+1);
        map = new int[mapY][mapX];
        paint = new Paint();
        map[4][5] = cat;
        map[6][7] = trap;
    }
    private enum state{
        OVER,START
    }

    //    返回棋盘数组的函数
    public int[][] getMap(){return map;}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = this.getMeasuredSize(widthMeasureSpec);
        int height = this.getMeasuredSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
    private int getMeasuredSize(int length){
        int specMode = MeasureSpec.getMode(length);
        int specSize = MeasureSpec.getSize(length);
        int retSize;
        if(specMode==MeasureSpec.EXACTLY){
            retSize = specSize;
        }else{
            retSize = (int)(radiu*((mapY-1)*1.8)+1);
        }
        return retSize;
    }

    public void onDraw(Canvas canvas){
        final int trapColor = Color.RED;
        final int catColor = Color.BLUE;
        final int deColor = Color.GRAY;
        float X,Y = radiu;
        super.onDraw(canvas);
        for(int i = 0;i < mapY;i++){
            for(int j = 0;j < mapX;j++){
                X = i%2 == 0?radiu:radiu*2;
                switch(map[i][j]){
                    case cat:paint.setColor(catColor);break;
                    case trap:paint.setColor(trapColor);break;
                    default:paint.setColor(deColor);
                }
                canvas.drawCircle(X+j*2*radiu,Y+i*(float)Math.sqrt(3)*radiu,radiu,paint);
            }
        }
    }
}
