package com.fruitbasket.audioplatform.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fruitbasket.audioplatform.AppCondition;
import com.fruitbasket.audioplatform.AudioService;
import com.fruitbasket.audioplatform.R;
import com.fruitbasket.audioplatform.WaveProducer;
import com.fruitbasket.audioplatform.play.Player;
import com.fruitbasket.audioplatform.play.WavePlayer;
import com.fruitbasket.audioplatform.play2.GlobalConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

final public class MainActivity extends Activity {
    private static final String TAG=".MainActivity";

    private RadioGroup channelOutRG;
    private ToggleButton waveProducerTB;
    private SeekBar waveRateSB;
    private TextView waveRateTV;

    private RadioGroup channelInRG;
    private ToggleButton recorderTB;

    private ToggleButton startBothTB;

    private Button createDirB;
    private TextView logTV;

    private ToggleButton testTB;

    private int channelOut= Player.CHANNEL_OUT_BOTH;
    //private int channelIn= AudioFormat.CHANNEL_IN_MONO;
    private int channelIn = AudioFormat.CHANNEL_IN_STEREO;
    private int waveRate;//声波的频率

    private EditText BeginHzView;
    private EditText StepHzView;
    private EditText FreqNumView;
    private EditText SimpleHzView;
    private EditText initialVolumeView;
    private EditText stepVolumeView;
    private EditText playTimeView;
    private EditText numberOfTimesView;
    private Switch allFreqView;
    public  int iBeginHz;
    public  int iStepHz;
    public  int ifreqNum ;
    public  int iSimpleHz ;
    public float initialVolume;
    public float[] stepVolume;
    public int playTime;
    public int numberOfTimes;
    public boolean allFreq;

    private Intent intent;
    private AudioService audioService;
    private ServiceConnection serviceConnection=new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG,"ServiceConnection.onServiceConnection()");
            audioService =((AudioService.AudioServiceBinder)binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG,"ServiceConnection.onServiceDisConnection()");
            audioService =null;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate()");
        setContentView(R.layout.activity_main);
        initializeViews();
        intent=new Intent(this,AudioService.class);
        if(audioService ==null) {
            Log.i(TAG,"begin to bind service");
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG,"onStart()");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG,"onResume()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG,"onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(TAG,"onStop()");
    }

    @Override
    protected void onDestroy(){
        Log.i(TAG,"onDestroy()");
        //在断开声音服务前，释放播放器资源
        if(audioService!=null){
            audioService.releasePlayer();
        }
        unbindService(serviceConnection);
        stopService(intent);//must stop the Service
        super.onDestroy();
    }

    private void initializeViews(){

        BeginHzView = (EditText)findViewById(R.id.edit_BeginHztext); ;
        StepHzView   = (EditText)findViewById(R.id.edit_StepHztext);
        FreqNumView = (EditText)findViewById(R.id.edit_freqNumtext);
        SimpleHzView = (EditText)findViewById(R.id.edit_simpleRatetext);
        initialVolumeView = (EditText) findViewById(R.id.edit_initialVolume);
        stepVolumeView = (EditText)findViewById(R.id.edit_stepVolume);
        playTimeView = (EditText)findViewById(R.id.edit_playTime);
        numberOfTimesView = (EditText)findViewById(R.id.edit_times);
        allFreqView = (Switch)findViewById(R.id.switch_allFreq);

        updateValueFromView();

        ToggleCheckedChangeListener tcListener=new ToggleCheckedChangeListener();
        channelOutRG =(RadioGroup)findViewById(R.id.channel_out_rg);
        channelOutRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.channel_out_left_rb:
                        channelOut= WavePlayer.CHANNEL_OUT_LEFT;
                        break;
                    case R.id.channel_out_right_rb:
                        channelOut= WavePlayer.CHANNEL_OUT_RIGHT;
                        break;
                    default:
                        Log.w(TAG,"initializeViews() : channelOut error");
                        channelOut=WavePlayer.CHANNEL_OUT_BOTH;
                }
            }
        });

        waveProducerTB =(ToggleButton)findViewById(R.id.wave_player_tb);
        waveProducerTB.setOnCheckedChangeListener(tcListener);

        waveRateTV =(TextView)findViewById(R.id.waverate_tv);

        waveRateSB =(SeekBar)findViewById(R.id.waverate_sb);
        waveRateSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                waveRate =progress*1000;
                waveRateTV.setText(getResources().getString(R.string.frequency,progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        waveRate = waveRateSB.getProgress()*1000;
        waveRateTV.setText(getResources().getString(R.string.frequency,waveRateSB.getProgress()));

        channelInRG=(RadioGroup)findViewById(R.id.channel_in_rg);
        channelInRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.channel_in_mono_rb:
                        channelIn=AudioFormat.CHANNEL_IN_MONO;
                        break;
                    case R.id.channel_in_stereo_rb:
                        channelIn=AudioFormat.CHANNEL_IN_STEREO;
                        break;
                    default:
                        Log.w(TAG,"initializeViews() : channelIn error");
                        channelIn=AudioFormat.CHANNEL_IN_MONO;
                }
            }
        });
        recorderTB=(ToggleButton)findViewById(R.id.recorder_tb);
        recorderTB.setOnCheckedChangeListener(tcListener);

        startBothTB=(ToggleButton)findViewById(R.id.start_both_tb);
        startBothTB.setOnCheckedChangeListener(tcListener);

        createDirB=(Button)findViewById(R.id.create_dir_b);
        createDirB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i(TAG,"onClick()");
                if(audioService!=null){
                    audioService.updateSubDir();
                }
                else{
                    Log.w(TAG,"audioService==null");
                }
                Toast.makeText(MainActivity.this,"dir created", Toast.LENGTH_SHORT).show();
            }
        });

        logTV=(TextView)findViewById(R.id.log_tv);
        logTV.setText("LOG:\n");

        testTB=(ToggleButton)findViewById(R.id.test_tb);
        testTB.setOnCheckedChangeListener(tcListener);
    }

    private boolean updateValueFromView() {
        ifreqNum   = Integer.parseInt(FreqNumView.getText().toString());

        String[] stepVolumeStr = stepVolumeView.getText().toString().split(";");
        if (stepVolumeStr.length == 0 || (stepVolumeStr.length != 1 && stepVolumeStr.length != ifreqNum)) {
            return false;
        }
        stepVolume = new float[ifreqNum];
        if (stepVolumeStr.length == 1) {
            float step = Float.parseFloat(stepVolumeStr[0]);
            stepVolume[0] = 0;
            for (int i = 1; i < ifreqNum; ++i) {
                stepVolume[i] = stepVolume[i - 1] + step;
            }
        } else {
            for (int i = 1; i < ifreqNum; ++i) {
                stepVolume[i] = Float.parseFloat(stepVolumeStr[i]);
            }
        }

        iBeginHz    = Integer.parseInt(BeginHzView.getText().toString());
        iStepHz      = Integer.parseInt(StepHzView.getText().toString());
        iSimpleHz   = Integer.parseInt(SimpleHzView.getText().toString());
        initialVolume = Float.parseFloat(initialVolumeView.getText().toString());

        playTime = Integer.parseInt(playTimeView.getText().toString());
        numberOfTimes = Integer.parseInt(numberOfTimesView.getText().toString());
        allFreq = allFreqView.isChecked();

        return true;
    }

    private class ToggleCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
        private static final String TAG="...TCCListener";

        @Override
        public void onCheckedChanged(CompoundButton button,boolean isChecked){
            switch (button.getId()) {
                case R.id.wave_player_tb:
                    if (isChecked) {
                        startPlayWav();
                    } else {
                        stopPlay();
                    }
                    break;
                case R.id.recorder_tb:
                    if (isChecked) {
                        startRecordWav();
                    } else {
                        stopRecord();
                    }
                    break;
                case R.id.start_both_tb:
                    if(isChecked){
                        if (!updateValueFromView()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,
                                            String.format("length of step volume is invalid!", ifreqNum),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                            startBothTB.setChecked(false);
                            return;
                        }

                        logTV.append("start        both: "+new SimpleDateFormat("HH:mm:ss:SSS").format(new Date())+"\n");

                        startRecordWav();
                        logTV.append("started record: "+new SimpleDateFormat("HH:mm:ss:SSS").format(new Date())+"\n");

                        startBothTB.setEnabled(false);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < numberOfTimes; ++i) {
                                    try {
                                        startPlayWav();

                                        Thread.sleep(playTime * (allFreq ? 1 : ifreqNum) + 400);
                                        stopPlay();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                stopRecord();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startBothTB.setChecked(false);
                                        startBothTB.setEnabled(true);
                                    }
                                });
                            }
                        }).start();
                    }
                    break;

                case R.id.test_tb:
                    if (isChecked) {
                        startTest();
                    }
                    else{
                        stopTest();
                    }
                    break;
                default:
                    Log.w(TAG,"onClick(): id error");
            }
        }

        private void startPlayWav(){
            Log.i(TAG,"starPlayWav()");
            if(audioService !=null){
                if (!updateValueFromView()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,
                                    String.format("length of step volume is invalid!", ifreqNum),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                    return;
                }

                GlobalConfig.INITIAL_VOLUME = initialVolume;
                GlobalConfig.STEP_VOLUME = stepVolume;
                GlobalConfig.MAX_FRAME_SIZE = (int) (playTime / 1000.0f * GlobalConfig.AUDIO_SAMPLE_RATE);
                GlobalConfig.ALL_FREQ = allFreq;

                GlobalConfig.START_FREQ=iBeginHz;
                GlobalConfig.FREQ_INTERVAL=iStepHz;
                GlobalConfig.NUM_FREQ=ifreqNum;
                ///迁移到这里处理相位数据？
                GlobalConfig.stPhaseProxy.init();//处理相位数据

                audioService.startPlayWav(channelOut, waveRate, WaveProducer.COS, iSimpleHz, iBeginHz, iStepHz, ifreqNum);
            }
            else{
                Log.w(TAG,"audioService==null");
            }
        }

        private void stopPlay(){
            Log.i(TAG,"stopPlay()");
            if(audioService !=null){
                audioService.stopPlay();
            }
            else{
                Log.w(TAG,"audioService==null");
            }
        }

        private void startRecordWav(){
            Log.i(TAG,"startRecordWav()");
            if(audioService!=null){
                System.out.println(channelIn);
                audioService.startRecordWav(
                        channelIn,
                        AppCondition.DEFAULE_SIMPLE_RATE,
                        AudioFormat.ENCODING_PCM_16BIT
                );
            }
            else{
                Log.w(TAG,"audioService==null");
            }

            //第二种录音方法
            /*try {
                //创建临时文件,注意这里的格式为.pcm
                GlobalConfig.fPcmRecordFile = File.createTempFile(GlobalConfig.sRecordPcmFileName, ".pcm", GlobalConfig.fAbsolutepath);
                GlobalConfig.fPcmRecordFile2 = File.createTempFile(GlobalConfig.sRecordPcmFileName2, ".pcm", GlobalConfig.fAbsolutepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            GlobalConfig.isRecording=true;
            GlobalConfig.stPhaseAudioRecord.initRecord();*/
        }

        private void stopRecord(){
            Log.i(TAG,"stopRecord()");
            if(audioService!=null){
                audioService.stopRecord();
            }
            else{
                Log.w(TAG,"audioService==null");
            }

            /*try {
                GlobalConfig.stPhaseAudioRecord.stopRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GlobalConfig.stPhaseProxy.destroy();
            GlobalConfig.stWaveFileUtil.destroy();*/

        }

        private void startTest(){
            Log.i(TAG,"startTest()");
            if(audioService!=null){
                audioService.startRecordTest();
            }
            else{
                Log.w(TAG,"audioService==null");
            }
        }

        private void stopTest(){
            Log.i(TAG,"stopTest()");
            if(audioService!=null){
                audioService.stopRecord();
            }
            else{
                Log.w(TAG,"audioService==null");
            }
        }
    }
}
