package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.R;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

	private final List<Appointment> appointments;
	private final Context context;

	public AppointmentAdapter(Context context, List<Appointment> appointments) {
		this.context = context;
		this.appointments = appointments;
	}

	@NonNull
	@Override
	public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_appointment, parent, false);
		return new AppointmentViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
		Appointment appointment = appointments.get(position);
		holder.veterinarianTextView.setText(appointment.getVeterinarian());
		holder.dateTextView.setText(appointment.getDate());
	}

	@Override
	public int getItemCount() {
		return appointments.size();
	}

	static class AppointmentViewHolder extends RecyclerView.ViewHolder {
		TextView veterinarianTextView;
		TextView dateTextView;

		public AppointmentViewHolder(@NonNull View itemView) {
			super(itemView);
			veterinarianTextView = itemView.findViewById(R.id.appointment_veterinarian);
			dateTextView = itemView.findViewById(R.id.appointment_date);
		}
	}
}
