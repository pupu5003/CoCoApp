package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocoapp.Object.Vaccination;
import com.example.cocoapp.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VaccinationAdapter extends RecyclerView.Adapter<VaccinationAdapter.VaccinationViewHolder> {

	private final List<Vaccination> vaccinations;
	private Context context;

	public VaccinationAdapter(Context context, List<Vaccination> vaccinations) {
		this.vaccinations = vaccinations;
		this.context = context;
	}

	@NonNull
	@Override
	public VaccinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_vaccination, parent, false);
		return new VaccinationViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull VaccinationViewHolder holder, int position) {
		Vaccination vaccination = vaccinations.get(position);
		holder.nameTextView.setText(vaccination.getName());
		holder.dateTextView.setText(vaccination.getDate());
		holder.veterinarianTextView.setText(vaccination.getVeterinarian());
	}

	@Override
	public int getItemCount() {
		return vaccinations.size();
	}

	static class VaccinationViewHolder extends RecyclerView.ViewHolder {
		TextView nameTextView;
		TextView dateTextView;
		TextView veterinarianTextView;

		public VaccinationViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.vaccination_name);
			dateTextView = itemView.findViewById(R.id.vaccination_date);
			veterinarianTextView = itemView.findViewById(R.id.vaccination_veterinarian);
		}
	}
}
