package tk.jealmas.ambience;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import tk.jealmas.ambience.data.ConfigTransmitter;

public class ActivityMain extends Activity {

    private Button commandButton;
    private EditText textIp;
    private TextView commandText;

    private SeekBar barRed;
    private SeekBar barGreen;
    private SeekBar barBlue;

    private int red = 0;
    private int green = 0;
    private int blue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commandButton = (Button) findViewById(R.id.buttonCommand);

        textIp = (EditText) findViewById(R.id.textIp);

        barRed = (SeekBar) findViewById(R.id.barRed);
        barGreen = (SeekBar) findViewById(R.id.barGreen);
        barBlue = (SeekBar) findViewById(R.id.barBlue);

        commandText = (TextView) findViewById(R.id.textCommand);

        commandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red = barRed.getProgress();
                green = barGreen.getProgress();
                blue = barBlue.getProgress();

                (new ConfigTransmitter(new ConfigTransmitter.ConfigTransmitterListener() {
                    @Override
                    public void onSuccess() {
                        commandText.setText("OK");
                    }

                    @Override
                    public void onFail() {
                        commandText.setText("FALHA");
                    }
                }, textIp.getText().toString(), red, green, blue)).execute();
            }
        });
    }
}
