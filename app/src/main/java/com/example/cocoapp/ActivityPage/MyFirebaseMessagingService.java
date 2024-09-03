package com.example.cocoapp.ActivityPage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.cocoapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
	private static final String CHANNEL_ID = "notify";
	private static final int NOTIFICATION_ID = 102;

	@Override
	public void onCreate() {
		super.onCreate();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(
					CHANNEL_ID,
					"Your Channel Name",
					NotificationManager.IMPORTANCE_DEFAULT);
			channel.setDescription("Your Channel Description");
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	@Override
	public void onMessageReceived(@NonNull RemoteMessage message) {
		super.onMessageReceived(message);
		Log.d("FCM", "Message received from: " + message.getFrom());

		if (message.getNotification() != null) {
			getFirebaseMessage(message.getNotification().getTitle(), message.getNotification().getBody());
		}
	}

	private void getFirebaseMessage(String title, String body) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.noti_ic)
				.setContentTitle(title)
				.setContentText(body)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setAutoCancel(true);

		NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
			return;
		}

		managerCompat.notify(NOTIFICATION_ID, builder.build());
	}
}
