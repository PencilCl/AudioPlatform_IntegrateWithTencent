package com.fruitbasket.audioplatform.record;

/**
 * Created by FruitBasket on 2017/5/31.
 */

public class CRCommnad extends RecordCommand {

    private CommonRecorder commonRecoder;

    public CRCommnad(CommonRecorder commonRecoder){
        this.commonRecoder=commonRecoder;
    }

    @Override
    public void start() {
        commonRecoder.start();
    }

    @Override
    public void updateSubDir(){
        commonRecoder.updateSubDir();
    }

    @Override
    public void stop() {
        commonRecoder.stop();
    }
}
