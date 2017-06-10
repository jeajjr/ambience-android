package tk.jealmas.ambience.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import tk.jealmas.ambience.R;
import tk.jealmas.ambience.data.ConfigTransmitter;

public class ActivityMain extends Activity implements ColorPickerFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new ColorPickerFragment())
                .commit();
    }

    @Override
    public void onColorPicked(int r, int g, int b, ConfigTransmitter.ConfigTransmitterListener resultListener) {
        (new ConfigTransmitter(resultListener, r, g, b)).execute();
    }
}
