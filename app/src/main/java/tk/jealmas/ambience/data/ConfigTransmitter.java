package tk.jealmas.ambience.data;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ConfigTransmitter extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = ConfigTransmitter.class.getName();

    private static String IP = "192.168.0.10";
    private static Integer PORT = 45225;
    private static Integer TIMEOUT = 2000; //ms

    private ConfigTransmitterListener configTransmitter;
    private int red;
    private int green;
    private int blue;

    public ConfigTransmitter(ConfigTransmitterListener configTransmitter, int red, int green, int blue) {
        this.configTransmitter = configTransmitter;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public interface ConfigTransmitterListener {
        void onSuccess();
        void onFail();
        void onStart();
        void onFinish();
    }

    @Override
    protected void onPreExecute() {
        if (configTransmitter != null)
            configTransmitter.onStart();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            InetSocketAddress address = new InetSocketAddress(IP, PORT);
            Socket socket = new Socket();
            socket.setSoTimeout(TIMEOUT);
            socket.connect(address);

            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

            Log.d(TAG, "got connection, sending " + red + "," + green + "," + blue);

            dOut.writeInt(red);
            dOut.writeInt(green);
            dOut.writeInt(blue);

            Log.d(TAG, "sent message");

            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            int input = dIn.readInt();

            Log.d(TAG, "got message " + input);

            socket.close();

            return true;

        } catch (IOException e) {
            Log.e(TAG, "exception", e);
        } catch (Exception e) {
            Log.e(TAG, "exception", e);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (configTransmitter != null) {
            configTransmitter.onFinish();

            if (aBoolean)
                configTransmitter.onSuccess();
            else
                configTransmitter.onFail();
        }
        else
            Log.d(TAG, "received null listener");
    }

}
