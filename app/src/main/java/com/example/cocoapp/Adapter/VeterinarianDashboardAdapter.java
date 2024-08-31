package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocoapp.R;
import com.example.cocoapp.Object.Veterinarian;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VeterinarianDashboardAdapter extends RecyclerView.Adapter<VeterinarianDashboardAdapter.VeterinarianDashboardViewHolder> {

	private List<Veterinarian> veterinarianList;
	private Context context;

	public VeterinarianDashboardAdapter(Context context, List<Veterinarian> veterinarianList) {
		this.context = context;
		this.veterinarianList = veterinarianList;
	}

	@NonNull
	@Override
	public VeterinarianDashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_veterinarian_dashboard, parent, false);
		return new VeterinarianDashboardViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull VeterinarianDashboardViewHolder holder, int position) {
		Veterinarian veterinarian = veterinarianList.get(position);
		// Bind your data to the view here
		holder.bind(veterinarian);
	}

	@Override
	public int getItemCount() {
		return Math.min(veterinarianList.size(), 2); // Limit to 2 items
	}

	public static class VeterinarianDashboardViewHolder extends RecyclerView.ViewHolder {
		private ImageView profileImage;
		private TextView doctorName;
		private TextView doctorQualification;
		private RatingBar ratingBar;
		private TextView reviewText;
		private TextView experienceText;
		private TextView distanceText;
		private TextView priceText;
		private TextView availabilityText;
		private TextView lastVisitText;
		private TextView lastVisitDate;
		private TextView bookAppointment;

		public VeterinarianDashboardViewHolder(@NonNull View itemView) {
			super(itemView);
			profileImage = itemView.findViewById(R.id.profile_image);
			doctorName = itemView.findViewById(R.id.doctor_name);
			doctorQualification = itemView.findViewById(R.id.doctor_qualification);
			ratingBar = itemView.findViewById(R.id.rating_layout);
			reviewText = itemView.findViewById(R.id.textView);
			distanceText = itemView.findViewById(R.id.distance);
			priceText = itemView.findViewById(R.id.price);
			lastVisitText = itemView.findViewById(R.id.last_visit_text);
			lastVisitDate = itemView.findViewById(R.id.last_visit_date);
			bookAppointment = itemView.findViewById(R.id.bookAppointment);
		}

		public void bind(Veterinarian veterinarian) {
			doctorName.setText(veterinarian.getName());
			doctorQualification.setText(veterinarian.getQualification());
			ratingBar.setRating(veterinarian.getRating());
			reviewText.setText(String.format("%.1f (%d reviews)", veterinarian.getRating(), veterinarian.getReviews()));
			distanceText.setText(veterinarian.getDistance());
			priceText.setText(veterinarian.getPrice());
			// Set the image from ImageView
			profileImage.setImageDrawable(veterinarian.getProfileImage().getDrawable());
			lastVisitDate.setText(veterinarian.getLastVisit());
		}
	}
}
