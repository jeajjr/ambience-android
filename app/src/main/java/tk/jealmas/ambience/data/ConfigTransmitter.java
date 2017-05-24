package tk.jealmas.ambience.data;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConfigTransmitter extends AsyncTask<Void, Void, Boolean> {

    private ConfigTransmitterListener configTransmitter;
    private String IP;
    private int red;
    private int green;
    private int blue;

    public ConfigTransmitter(ConfigTransmitterListener configTransmitter, String IP, int red, int green, int blue) {
        this.configTransmitter = configTransmitter;
        this.IP = IP;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public interface ConfigTransmitterListener {
        void onSuccess();
        void onFail();
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Socket socket = new Socket(IP, 45225);
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

            Log.d("ConfigTransmitter", "got connection, sending " + red + "," + green + "," + blue);

            dOut.writeInt(red);
            dOut.writeInt(green);
            dOut.writeInt(blue);

            Log.d("ConfigTransmitter", "sent message");

            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            int input = dIn.readInt();

            Log.d("ConfigTransmitter", "got message " + input);

            socket.close();

            return true;

        } catch (IOException e) {
            Log.e("ConfigTransmitter", "exception", e);
        } catch (Exception e) {
            Log.e("ConfigTransmitter", "exception", e);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean)
            configTransmitter.onSuccess();
        else
            configTransmitter.onFail();

    }

}
