package com.example.cocoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocoapp.Fragment.VeterinarianProfile;
import com.example.cocoapp.R;
import com.example.cocoapp.Object.Veterinarian;

import java.util.List;

public class VeterinarianDashboardAdapter extends RecyclerView.Adapter<VeterinarianDashboardAdapter.VeterinarianDashboardViewHolder> {

	private List<Veterinarian> veterinarianList;
	private Context context;
	private boolean showAll;

	public VeterinarianDashboardAdapter(Context context, List<Veterinarian> veterinarianList, boolean showAll) {
		this.context = context;
		this.veterinarianList = veterinarianList;
		this.showAll = showAll;
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
		holder.bind(veterinarian);

		holder.bookAppointment.setOnClickListener(view -> {
			FragmentActivity activity = (FragmentActivity) context;
			FragmentManager fragmentManager = activity.getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();

			VeterinarianProfile profileFragment = VeterinarianProfile.newInstance(veterinarian);

			transaction.replace(R.id.fragment_container, profileFragment); // R.id.fragment_container is the ID of your container layout
			transaction.addToBackStack(null); // add to backstack so the user can navigate back
			transaction.commit();
		});
	}

	@Override
	public int getItemCount() {
		return showAll ? veterinarianList.size() : Math.min(veterinarianList.size(), 3); // Show all if showAll is true
	}

	public static class VeterinarianDashboardViewHolder extends RecyclerView.ViewHolder {
		private ImageView profileImage;
		private TextView doctorName;
		private TextView doctorQualification;
		private RatingBar ratingBar;
		private TextView reviewText;
		private TextView distanceText;
		private TextView priceText;
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
			doctorName.setText(veterinarian.getVetName());
			doctorQualification.setText(veterinarian.getDegree());
			ratingBar.setRating(veterinarian.getRating());
			reviewText.setText(String.format("%.1f (%d reviews)", veterinarian.getRating(), veterinarian.getReviews()));
			distanceText.setText(String.valueOf(veterinarian.getDistance()));
			priceText.setText(String.valueOf(veterinarian.getPrice()));
			int size = veterinarian.getReviews().size();
			String lastAppointment = veterinarian.getReviews().get(size-1).getTime();
			lastVisitDate.setText(lastAppointment);

			// Load the image using Glide
			Glide.with(profileImage.getContext())
					.load(veterinarian.getImageUrl())
					.error(R.drawable.vet1)
					.into(profileImage);
		}
	}
}
