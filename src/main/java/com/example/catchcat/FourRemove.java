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
import android.widget.TextView;

/**
 * Created by 王国晟 on 2017/5/22.
 */
public class FourRemove extends View {
    private int radiu;
    private int map[][];
    private int curX,curY;
    private int score,stepScore = 1,removeScore = 5;
    private final int red = 1;
    private final int yellow = 2;
    private final int blue = 3;
    private final int green = 4;
    private final int white = 5;
    private final int ready[] = new int[3];
    private final int mapX = 9;//宽度
    private final int mapY = 9;//高度
    private TextView scoreText;

    private enum state{//游戏状态
        OVER,START
    }
    private state currentState;
    private Paint paint;
    public FourRemove(Context context,AttributeSet attr){
        super(context,attr);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        radiu = dm.widthPixels/(mapX*2+1);
        map = new int[mapY][mapX];
        paint = new Paint();
        ready[0] = (int)(Math.random()*4+1);
        ready[1] = (int)(Math.random()*4+1);
        ready[2] = (int)(Math.random()*4+1);
        curX = mapX/2;
        curY = mapY/2;
        map[curY][curX] = (int)(Math.random()*4+1);
        for(int i = 0;i < mapX;i++){
            map[0][i] = map[mapY-1][i] = white;
        }
        for(int i = 0;i < mapY;i++){
            map[i][0] = map[i][mapX-1] = white;
        }
        currentState = state.START;
    }

    private void updateReady(){
        ready[0] = ready[1];
        ready[1] = ready[2];
        ready[2] = (int)(Math.random()*4+1);
    }

    public void restart(){
        ready[0] = (int)(Math.random()*4+1);
        ready[1] = (int)(Math.random()*4+1);
        ready[2] = (int)(Math.random()*4+1);
        score = 0;
        map = new int[mapY][mapX];
        curX = mapX/2;
        curY = mapY/2;
        map[curY][curX] = (int)(Math.random()*4+1);
        for(int i = 0;i < mapX;i++){
            map[0][i] = map[mapY-1][i] = white;
        }
        for(int i = 0;i < mapY;i++){
            map[i][0] = map[i][mapX-1] = white;
        }
        currentState = state.START;
        invalidate();
    }
    //返回随机队列的函数
    public  int[] getReady(){
        return ready;
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
            retSize = (int)(radiu*((mapY-1)*1.8+2))+20+2*radiu;
        }
        return retSize;
    }

    private boolean testOver(int y,int x){
        /*  偶数  奇数
            00     00
            0c0   0c0
            00     00*/
        if(y%2 == 0){
            return !(map[y+1][x] == 0|| map[y-1][x] == 0|| map[y][x+1] == 0||
                    map[y][x-1] == 0|| map[y+1][x-1] == 0|| map[y-1][x-1] == 0);
        }else{
            return !(map[y+1][x] == 0|| map[y-1][x] == 0|| map[y][x+1] == 0||
                    map[y][x-1] == 0|| map[y+1][x+1] == 0|| map[y-1][x+1] == 0);
        }
    }
    private int testRemove(int y,int x,int color){
        return 0;
    }
    public void setMap(int y,int x){
        if(map[y][x] != 0 || currentState == state.OVER){return;}
        map[y][x] = ready[0];
        updateReady();
        curX = x;
        curY = y;
        score = score + stepScore + testRemove(y,x,ready[0]);
        if(testOver(curY,curX)){currentState = state.OVER;}
        invalidate();
    }

    private float tapx;
    private float tapy;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(currentState != state.START){return true;}
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
                        setMap(curY,curX+1);
                    } else if (tan > 1 / 1.732) {
                        setMap(curY-1,curX+(curY)%2);
                    } else if (tan < -1 / 1.732) {
                        setMap(curY+1,curX+(curY)%2);
                    }
                }else{
                    if (tan > -1 / 1.732 && tan < 1 / 1.732) {
                        setMap(curY,curX-1);
                    } else if (tan > 1 / 1.732) {
                        setMap(curY+1,curX-(curY+1)%2);
                    } else if (tan < -1 / 1.732) {
                        setMap(curY-1,curX-(curY+1)%2);
                    }
                }
        }
        return true;
    }

    public void onDraw(Canvas canvas){
        float X,Y = radiu;
        super.onDraw(canvas);
        for(int i = 0;i < mapY;i++){
            for(int j = 0;j < mapX;j++){
                X = i%2 == 0?radiu:radiu*2;
                switch(map[i][j]){
                    case red:paint.setColor(Color.RED);break;
                    case yellow:paint.setColor(Color.YELLOW);break;
                    case blue:paint.setColor(Color.BLUE);break;
                    case green:paint.setColor(Color.GREEN);break;
                    case white:paint.setColor(Color.WHITE);break;
                    default:paint.setColor(Color.GRAY);
                }
                canvas.drawCircle(X+j*2*radiu,Y+i*(float)Math.sqrt(3)*radiu,radiu,paint);
            }
        }
        paint.setColor(Color.BLACK);
        X = curY%2 == 0?radiu:radiu*2;
        canvas.drawCircle(X+curX*2*radiu,Y+curY*(float)Math.sqrt(3)*radiu,radiu/3,paint);

        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText("得分"+score,0,Y+(mapY)*(float)Math.sqrt(3)*radiu,paint);

        for(int i=0;i < 3;i++){
            switch (ready[i]){
                case red:paint.setColor(Color.RED);break;
                case yellow:paint.setColor(Color.YELLOW);break;
                case blue:paint.setColor(Color.BLUE);break;
                case green:paint.setColor(Color.GREEN);break;
            }
            canvas.drawCircle((float) ((mapX / 2 + i) * 2 * radiu), Y + ((mapY) * (float) Math.sqrt(3)-1)* radiu, radiu, paint);
        }
        if(currentState == state.OVER){
            paint.setTextSize(50);
            paint.setColor(Color.BLACK);
            canvas.drawText("游戏结束 得分"+score,50,100,paint);
        }
    }
}
