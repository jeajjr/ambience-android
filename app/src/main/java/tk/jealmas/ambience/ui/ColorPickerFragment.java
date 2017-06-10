package tk.jealmas.ambience.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.slider.LightnessSlider;

import tk.jealmas.ambience.R;
import tk.jealmas.ambience.data.ConfigTransmitter;

public class ColorPickerFragment extends Fragment {
    private static final String TAG = ColorPickerFragment.class.getName();

    // Callback
    private OnFragmentInteractionListener mListener;

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

    private double amortizationCoefficient = 5.0;

    float getAmortizedAlpha(float alpha) {
        alpha /= 1024;
        return 1024 * (float) ((Math.exp(amortizationCoefficient * alpha) - 1.0)/(Math.exp(amortizationCoefficient) - 1.0));
    }

    void readColorsSendCommand() {
        int color = cpv.getSelectedColor();
        red = (int) getAmortizedAlpha(((color & 0xFF0000) >> 16)*4);
        green = (int) getAmortizedAlpha(((color & 0x00FF00) >> 8)*4);
        blue = (int) getAmortizedAlpha((color & 0x0000FF)*4);

        Log.d(TAG, "got color " + red + "," + green + "," + blue);


        if (mListener != null) {
            mListener.onColorPicked(red, green, blue, new ConfigTransmitter.ConfigTransmitterListener() {
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
            });
        }

    }


    public ColorPickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_color_picker, container, false);

        cpv = (ColorPickerView) v.findViewById(R.id.color_picker_view);
        ls = (LightnessSlider) v.findViewById(R.id.lightness_slider);
        cpv.setLightnessSlider(ls);
        ls.setColorPicker(cpv);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        status = (ImageView) v.findViewById(R.id.status);
        commandButton = (Button) v.findViewById(R.id.buttonCommand);

        cpv.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                readColorsSendCommand();
            }
        });

        commandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readColorsSendCommand();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onColorPicked(int r, int g, int b, ConfigTransmitter.ConfigTransmitterListener ctl);
    }
}
