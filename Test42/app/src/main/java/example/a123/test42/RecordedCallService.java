package example.a123.test42;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

public class RecordedCallService extends Service {

    private NotificationManager nm;
    private final int NOTIFICATION_ID = 127;

    public RecordedCallService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onCreate() {
        super.onCreate();
        Intent shareIntent = new Intent(this, ShareActivity.class);
        Intent editIntent = new Intent(this, PreviewEditorActivity.class);
        PendingIntent sharePendingIntent = PendingIntent.getActivity(this, 0, shareIntent, 0);
        PendingIntent editPendingIntent = PendingIntent.getActivity(this, 0, editIntent, 0);
        nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Action share = new Notification.Action(R.drawable.s, "Share", sharePendingIntent);
        Notification.Action edit = new Notification.Action(R.drawable.e, "Edit text file", editPendingIntent);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        System.out.println("Activity started");
        builder
                .setSmallIcon(R.mipmap.phone)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_launcher_background))
                .setTicker("Call logger")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("New call recorded")
                .addAction(edit)
                .addAction(share);

        Notification notification = builder.build();
        nm.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nm.cancel(NOTIFICATION_ID);
    }
}
