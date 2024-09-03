package com.example.cocoapp.ActivityPage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.cocoapp.R;

public class AlarmReceiver extends BroadcastReceiver {
	private static final String CHANNEL_ID = "notify";
	private static final int NOTIFICATION_ID = 102;

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
				.setSmallIcon(R.drawable.noti_ic)
				.setContentTitle("Scheduled Notification")
				.setContentText("This is your scheduled notification.")
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setAutoCancel(true);

		// Display the notification
		NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);

		if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
			return;
		}

		managerCompat.notify(NOTIFICATION_ID, builder.build());
	}
}
