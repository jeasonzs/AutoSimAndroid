package com.example.autosimandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class WaveView extends ImageView {
    private HashMap<String,WaveChannel> channelHashMap;
    private int girdNumX = 0;
    private int girdNumY = 0;
    private float girdValueY = 20;

    private class WaveChannel {
        public WaveChannel(String name,int color) {
            this.name = name;
            this.color = color;
            this.pos = 0;
        }
        public String name;
        public int color;
        public float pos;
        public int[] wave;
    }


    public WaveView(Context context, int girdNumX, int girdNumY) {
        super(context);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        channelHashMap = new HashMap<String,WaveChannel>();
        this.girdNumX = girdNumX;
        this.girdNumY = girdNumY;
    }

    public void setGirdValueY(float girdValueY) {
        this.girdValueY = girdValueY;
    }

    public void addChannel(String name,int color) {
        WaveChannel channel = new WaveChannel(name,color);
        channelHashMap.put(name, channel);
    }

    public void setWave(String channel, int[] wave) {
        channelHashMap.get(channel).wave = wave;
    }

    public void setPos(String channel, int pos) {
        channelHashMap.get(channel).pos = pos;
    }

    private int checkInRect(int value,Rect waveRect) {
        if(value > waveRect.top+waveRect.height()-1) {
            value = waveRect.top+waveRect.height()-1;
        }
        if(value < waveRect.top - 1) {
            value = waveRect.top - 1;
        }
        return value;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        Rect waveRect = new Rect(4,4,width-4,height-4);

        super.onDraw(canvas);

        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setARGB(255, 255, 255, 255);
        paint.setPathEffect(new DashPathEffect(new float[]{2, 7}, 0));
        paint.setStrokeWidth(1);

        Path path = new Path();
        float peerX = waveRect.width()/(float)girdNumX;
        float peerY = waveRect.height()/(float)girdNumY;
        for(int i = 1; i < girdNumX; i++) {
            if(i == girdNumX/2) {
                continue;
            }
            int x = (int) (peerX * i);
            path.moveTo(waveRect.left + x, waveRect.top);
            path.lineTo(waveRect.left + x, waveRect.top + waveRect.height());
        }

        for(int i = 1; i < girdNumY; i++) {
            if(i == girdNumY/2) {
                continue;
            }
            int y = (int) (peerY*i);
            path.moveTo(waveRect.left, waveRect.top + y);
            path.lineTo(waveRect.left + waveRect.width(), waveRect.top + y);
        }

        canvas.drawPath(path, paint);

        paint.setPathEffect(null);
        paint.setStrokeWidth(2);

        path.reset();
        path.moveTo(waveRect.left, waveRect.top);
        path.lineTo(waveRect.left + waveRect.width(), waveRect.top);
        path.lineTo(waveRect.left + waveRect.width(), waveRect.top + waveRect.height());
        path.lineTo(waveRect.left,waveRect.top+waveRect.height());
        path.lineTo(waveRect.left, waveRect.top);
        canvas.drawPath(path, paint);


        paint.setPathEffect(new DashPathEffect(new float[]{1, 6}, 0));
        paint.setStrokeWidth(4);

        path.reset();
        path.moveTo(waveRect.left, waveRect.top + 2);
        path.lineTo(waveRect.left + waveRect.width(), waveRect.top + 2);

        path.moveTo(waveRect.left, waveRect.top + waveRect.height() / 2);
        path.lineTo(waveRect.left + waveRect.width(), waveRect.top + waveRect.height() / 2);

        path.moveTo(waveRect.left, waveRect.top + waveRect.height() - 2);
        path.lineTo(waveRect.left + waveRect.width(), waveRect.top + waveRect.height() - 2);

        path.moveTo(waveRect.left + 2, waveRect.top);
        path.lineTo(waveRect.left + 2, waveRect.top + waveRect.height());

        path.moveTo(waveRect.left + waveRect.width() / 2, waveRect.top);
        path.lineTo(waveRect.left + waveRect.width() / 2, waveRect.top + waveRect.height());

        path.moveTo(waveRect.left + waveRect.width() - 2, waveRect.top);
        path.lineTo(waveRect.left + waveRect.width() - 2, waveRect.top + waveRect.height());

        canvas.drawPath(path, paint);

        paint.setPathEffect(null);
        paint.setStrokeWidth(2);
        path.reset();

        Iterator iter = channelHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            WaveChannel channel = (WaveChannel)val;
            int[] wave = channel.wave;

            paint.setColor(channel.color);
            for (int j = 1; j < waveRect.width(); j++) {
                int waveCnt = Math.round((float)(wave.length)/waveRect.width()*j);
                int x = waveRect.left+j;
                int tmp1 = (int)(waveRect.height()/2 * (1.0 - channel.pos));
                int tmp2 = (int)(wave[waveCnt] / (girdValueY*8/waveRect.height()));
                int tmp3 = (int)((girdValueY*8/waveRect.height()));
                int y = (int)(waveRect.top+waveRect.height()/2 * (1.0 - channel.pos) - wave[waveCnt] / (girdValueY*8/waveRect.height()));
                y =  checkInRect(y,waveRect);
                if(j == 1) {
                    path.moveTo(x, y);
                }
                path.lineTo(x,y);
            }
        }
        canvas.drawPath(path, paint);



    }

}

