package com.fruitbasket.audioplatform.record;

import android.media.AudioFormat;

import com.fruitbasket.audioplatform.AppCondition;

import java.text.SimpleDateFormat;

/**
 * Created by FruitBasket on 2017/5/27.
 */

public abstract class Recorder {

    protected int channelIn;//用于指定音频的来源
    protected int sampleRate;//声音的采样频率
    protected int encoding;//指定每一个声音帧的位大小

    public Recorder(){
        this(
                AudioFormat.CHANNEL_IN_MONO,
                AppCondition.DEFAULE_SIMPLE_RATE,
                AudioFormat.ENCODING_PCM_16BIT
        );
    }

    public Recorder(int channelIn,int sampleRate,int encoding){
        this.channelIn=channelIn;
        this.sampleRate=sampleRate;
        this.encoding=encoding;
    }

    /**
     * 开始录音
     * @return true 成功开始录音
     */
    public abstract boolean start();

    /**
     * 这个方法设置有点怪异，后续要改进
     */
    public abstract void updateSubDir();

    /**
     * 停止录音
     * @return true 成功停止录音
     */
    public abstract boolean stop();

    /**
     * get a recorded file name by time.
     * @param extensionName extension name of the file
     * @return file name
     */
    protected static String getRecordedFileName(String extensionName){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH'h'-mm'm'-ss's'");
        return df.format(System.currentTimeMillis())+"."+extensionName;
    }
}
