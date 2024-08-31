package com.example.cocoapp.Adapter;

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

	public VaccinationAdapter(List<Vaccination> vaccinations) {
		this.vaccinations = vaccinations;
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

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
		String date = vaccination.getDate() != null ? sdf.format(vaccination.getDate()) : "No visit data";
		holder.dateTextView.setText(date);
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
