package com.fruitbasket.audioplatform.record;

/**
 * Author: FruitBasket
 * Time: 2017/7/8
 * Email: FruitBasket@qq.com
 * Source code: github.com/DevelopersAssociation
 */
public class RecorderTestCommand extends RecordCommand {

    private RecorderTest recorderTest;

    public RecorderTestCommand(RecorderTest recorderTest){
        this.recorderTest = recorderTest;
    }

    @Override
    public void start() {
        recorderTest.start();
    }

    @Override
    public void updateSubDir() {
        recorderTest.updateSubDir();
    }

    @Override
    public void stop() {
        recorderTest.stop();
    }
}
