package tk.jealmas.ambience.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

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

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContent(contentView);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(1, notification);
    }

    @Override
    public void onColorPicked(int r, int g, int b, ConfigTransmitter.ConfigTransmitterListener resultListener) {
        (new ConfigTransmitter(resultListener, r, g, b)).execute();
    }
}
