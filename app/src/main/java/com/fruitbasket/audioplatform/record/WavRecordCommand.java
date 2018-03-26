package com.fruitbasket.audioplatform.record;

/**
 * Created by FruitBasket on 2017/6/4.
 */

public class WavRecordCommand extends RecordCommand {

    private WavRecorder wavRecorder;

    public WavRecordCommand(WavRecorder wavRecorder){
        this.wavRecorder=wavRecorder;
    }

    @Override
    public void start() {
        wavRecorder.start();
    }

    @Override
    public void updateSubDir() {
        wavRecorder.updateSubDir();
    }

    @Override
    public void stop() {
        wavRecorder.stop();
    }
}
