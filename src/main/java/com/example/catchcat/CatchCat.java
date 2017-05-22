package com.example.catchcat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 王国晟 on 2017/5/22.
 */
public class CatchCat extends View {
    private int radiu;
    private int map[][];
    private int curX,curY;
    private final int cat = 1;
    private final int trap = 2;
    private final int mapX = 9;//宽度
    private final int mapY = 9;//高度
    private state currentState = state.CATSTART;
    private Paint paint;
    public CatchCat(Context context,AttributeSet attr){
        super(context,attr);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        radiu = dm.widthPixels/(mapX*2+1);
        curX = mapX/2;
        curY = mapY/2;
        map = new int[mapY][mapX];
        map[curY][curX] = cat;
        paint = new Paint();
    }
    private enum state{
        OVER,CATSTART,TRAPSTART
    }

    //    返回棋盘数组的函数
    public int[][] getMap(){
        return map;
    }

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
            retSize = (int)(radiu*((mapY-1)*1.8+2));
        }
        return retSize;
    }

    public void setMap(boolean iscat,int y,int x){
        if(iscat){
            map[y][x] = cat;
            map[curY][curX] = 0;
            curX = x;
            curY = y;
            currentState = state.TRAPSTART;
            if(curX == 0 || curX == mapX-1 || curY == 0 || curY == mapY-1) {
                currentState = state.OVER;
            }
        }else{
            map[y][x] = trap;
            currentState = state.CATSTART;
        }
        invalidate();
        if(currentState == state.TRAPSTART){
            setMap(false, (int) (Math.random() * mapY), (int) (Math.random() * mapX));
        }
    }

    private float tapx;
    private float tapy;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(currentState != state.CATSTART){return true;}
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                tapx = event.getX();
                tapy = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float ditY = tapy - event.getY();
                float ditX = event.getX() - tapx;
                float tan = ditY/ditX;

                if(ditX > 0) {
                    if (tan > -1 / 1.732 && tan < 1 / 1.732) {
                        setMap(true,curY,curX+1);
                    } else if (tan > 1 / 1.732) {
                        setMap(true,curY-1,curX+(curY)%2);
                    } else if (tan < -1 / 1.732) {
                        setMap(true,curY+1,curX+(curY)%2);
                    }
                }else{
                    if (tan > -1 / 1.732 && tan < 1 / 1.732) {
                        setMap(true,curY,curX-1);
                    } else if (tan > 1 / 1.732) {
                        setMap(true,curY+1,curX-(curY+1)%2);
                    } else if (tan < -1 / 1.732) {
                        setMap(true,curY-1,curX-(curY+1)%2);
                    }
                }
        }
        return true;
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
        if(currentState == state.OVER){
            canvas.drawText("游戏结束",50,100,paint);
        }
    }
}
