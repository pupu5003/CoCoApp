package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocoapp.Object.VaccinationDetail;
import com.example.cocoapp.R;
import java.util.List;

public class VaccinationDetailAdapter extends RecyclerView.Adapter<VaccinationDetailAdapter.VaccinationDetailViewHolder> {

	private final List<VaccinationDetail> vaccinations;
	private Context context;

	public VaccinationDetailAdapter(Context context, List<VaccinationDetail> vaccinations) {
		this.vaccinations = vaccinations;
		this.context = context;
	}

	@NonNull
	@Override
	public VaccinationDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_vaccination, parent, false);
		return new VaccinationDetailViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull VaccinationDetailViewHolder holder, int position) {
		VaccinationDetail vaccination = vaccinations.get(position);
		holder.nameTextView.setText(vaccination.getName());
		holder.dateTextView.setText(vaccination.getDate());
		holder.veterinarianTextView.setText(vaccination.getVeterinarian());
	}

	@Override
	public int getItemCount() {
		return vaccinations.size();
	}

	static class VaccinationDetailViewHolder extends RecyclerView.ViewHolder {
		TextView nameTextView;
		TextView dateTextView;
		TextView veterinarianTextView;
		TextView petNameTextView;

		public VaccinationDetailViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.vaccination_name);
			dateTextView = itemView.findViewById(R.id.vaccination_date);
			veterinarianTextView = itemView.findViewById(R.id.vaccination_veterinarian);
		}
	}
}
