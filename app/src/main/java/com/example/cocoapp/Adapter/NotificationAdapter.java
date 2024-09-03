package com.example.cocoapp.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Object.NotificationItem;
import com.example.cocoapp.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

	private List<NotificationItem> notificationList;
	private Context context;

	public NotificationAdapter(Context context, List<NotificationItem> notificationList) {
		this.context = context;
		this.notificationList = notificationList;
	}

	@NonNull
	@Override
	public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
		return new NotificationViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
		NotificationItem item = notificationList.get(position);
		Glide.with(context)
				.load(item.getIconResId())
				.error(R.drawable.dog1)
				.into(holder.notificationIcon);

		holder.doctorName.setText(item.getDoctorName());
		holder.notificationMessage.setText(item.getMessage());
		holder.notificationTime.setText(item.getTime());

	}

	@Override
	public int getItemCount() {
		return notificationList.size();
	}

	// Inner ViewHolder class
	public class NotificationViewHolder extends RecyclerView.ViewHolder {
		ImageView notificationIcon;
		TextView doctorName;
		TextView notificationMessage;
		TextView notificationTime;
		ImageView notificationArrow;

		public NotificationViewHolder(@NonNull View itemView) {
			super(itemView);
			notificationIcon = itemView.findViewById(R.id.notification_icon_1);
			doctorName = itemView.findViewById(R.id.Doctor_name);
			notificationMessage = itemView.findViewById(R.id.notification_message_1);
			notificationTime = itemView.findViewById(R.id.notification_time);
			notificationArrow = itemView.findViewById(R.id.notification_arrow_1);
		}
	}
}
