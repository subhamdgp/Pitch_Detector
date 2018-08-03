package com.example.sd.pitchdetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends AppCompatActivity {
    private Button detectPitch;
    AudioProcessor p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detectPitch = (Button) findViewById(R.id.btnStart);

        detectPitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

                PitchDetectionHandler pdh = new PitchDetectionHandler() {
                    @Override
                    public void handlePitch(PitchDetectionResult result,AudioEvent e) {
                        final float pitchInHz = result.getPitch();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView text = (TextView) findViewById(R.id.textView2);
                                text.setText("" + pitchInHz);
                            }
                        });
                    }
                };
                AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
                dispatcher.addAudioProcessor(p);
                new Thread(dispatcher,"Audio Dispatcher").start();
            }
        });
    }
}