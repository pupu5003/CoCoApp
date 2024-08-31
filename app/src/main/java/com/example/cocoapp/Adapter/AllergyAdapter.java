package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocoapp.Object.Allergy;
import com.example.cocoapp.R;
import java.util.List;

public class AllergyAdapter extends RecyclerView.Adapter<AllergyAdapter.AllergyViewHolder> {

	private final List<Allergy> allergies;
	private Context context;

	public AllergyAdapter(Context context, List<Allergy> allergies) {
		this.context =context;
		this.allergies = allergies;
	}

	@NonNull
	@Override
	public AllergyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_allergy_wellness, parent, false);
		return new AllergyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AllergyViewHolder holder, int position) {
		Allergy allergy = allergies.get(position);
		holder.nameTextView.setText(allergy.getName());
		holder.descriptionTextView.setText(allergy.getDescription());
		holder.veterinarianTextView.setText(allergy.getVeterinarian());
	}

	@Override
	public int getItemCount() {
		return allergies.size();
	}

	static class AllergyViewHolder extends RecyclerView.ViewHolder {
		TextView nameTextView;
		TextView descriptionTextView;
		TextView veterinarianTextView;

		public AllergyViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.allergy_name);
			descriptionTextView = itemView.findViewById(R.id.allergy_description);
			veterinarianTextView = itemView.findViewById(R.id.allergy_veterinarian);
		}
	}
}
