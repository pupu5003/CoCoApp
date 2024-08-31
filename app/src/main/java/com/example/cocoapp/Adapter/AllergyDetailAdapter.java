package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocoapp.Object.AllergyDetail;
import com.example.cocoapp.R;
import java.util.List;

public class AllergyDetailAdapter extends RecyclerView.Adapter<AllergyDetailAdapter.AllergyDetailViewHolder> {

	private final List<AllergyDetail> allergies;
	private Context context;

	public AllergyDetailAdapter(Context context, List<AllergyDetail> allergies) {
		this.allergies = allergies;
		this.context = context;
	}

	@NonNull
	@Override
	public AllergyDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_allergy_medical_record, parent, false);
		return new AllergyDetailViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AllergyDetailViewHolder holder, int position) {
		AllergyDetail allergy = allergies.get(position);
		holder.nameTextView.setText(allergy.getName());
		holder.veterinarianTextView.setText(allergy.getVeterinarian());
		holder.dateTextView.setText(allergy.getDate());
	}

	@Override
	public int getItemCount() {
		return allergies.size();
	}

	static class AllergyDetailViewHolder extends RecyclerView.ViewHolder {
		TextView nameTextView;
		TextView petNameTextView;
		TextView veterinarianTextView;
		TextView dateTextView;

		public AllergyDetailViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.allergy_name);
			veterinarianTextView = itemView.findViewById(R.id.allergy_veterinarian);
			dateTextView = itemView.findViewById(R.id.allergy_date);
		}
	}
}
