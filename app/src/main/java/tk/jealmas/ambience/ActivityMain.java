package tk.jealmas.ambience;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

                int color = cpv.getSelectedColor();
                red = (int) (((color & 0xFF0000) >> 16)*4*ls.getAlpha());
                green = (int) (((color & 0x00FF00) >> 8)*4*ls.getAlpha());
                blue = (int) ((color & 0x0000FF)*4*ls.getAlpha());

                Log.d(TAG, "color " + color);

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
        });
    }
}
