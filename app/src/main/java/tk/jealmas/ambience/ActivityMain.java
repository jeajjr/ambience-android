package tk.jealmas.ambience;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.slider.LightnessSlider;

import tk.jealmas.ambience.data.ConfigTransmitter;

public class ActivityMain extends Activity {
    private static final String TAG = ActivityMain.class.getName();

    // UI Components
    private Button commandButton;
    private ProgressBar progressBar;
    private ColorPickerView cpv;
    private LightnessSlider ls;
    private ImageView status;

    // Controll variables
    private int red = 0;
    private int green = 0;
    private int blue = 0;

    float getAmortizedAlpha(float alpha) {
        alpha /= 1024;
        return 1024 * (float) ((Math.exp(2.0 * alpha) - 1.0)/(Math.exp(2.0) - 1.0));
    }

    void readColorsSendCommand() {
        int color = cpv.getSelectedColor();
        red = (int) getAmortizedAlpha(((color & 0xFF0000) >> 16)*4);
        green = (int) getAmortizedAlpha(((color & 0x00FF00) >> 8)*4);
        blue = (int) getAmortizedAlpha((color & 0x0000FF)*4);

        Log.d(TAG, "got color " + red + "," + green + "," + blue);

        (new ConfigTransmitter(new ConfigTransmitter.ConfigTransmitterListener() {
            @Override
            public void onSuccess() {
                status.setImageResource(R.drawable.success);
            }

            @Override
            public void onFail() {
                status.setImageResource(R.drawable.fail);
            }

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        },red, green, blue)).execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cpv = (ColorPickerView) findViewById(R.id.color_picker_view);
        ls = (LightnessSlider) findViewById(R.id.lightness_slider);
        cpv.setLightnessSlider(ls);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        status = (ImageView) findViewById(R.id.status);
        commandButton = (Button) findViewById(R.id.buttonCommand);

        commandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readColorsSendCommand();

            }
        });
        /*
        cpv.setOnsetOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readColorsSendCommand();
            }
        });

        ls.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT ||
                        event.getAction() == MotionEvent.ACTION_UP) {
                    readColorsSendCommand();
                    return true;
                }
                return false;
            }
        });
        */
    }
}
