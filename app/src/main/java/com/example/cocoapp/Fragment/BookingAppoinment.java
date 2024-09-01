package com.example.cocoapp.Fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.cocoapp.R;

public class BookingAppoinment extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mParam1;
	private String mParam2;

	public BookingAppoinment() {
		// Required empty public constructor
	}

	public static BookingAppoinment newInstance(String param1, String param2) {
		BookingAppoinment fragment = new BookingAppoinment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	private Button lastSelectedButton = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_booking_appoinment, container, false);

		// Initialize UI elements
		ImageButton backButton = view.findViewById(R.id.back_button);
		TextView tvHeader = view.findViewById(R.id.tvHeader);
		TextView tvChooseDate = view.findViewById(R.id.tvChooseDate);
		CalendarView calendarView = view.findViewById(R.id.calendar_view);
		TextView tvPickTime = view.findViewById(R.id.tvPickTime);
		Button btnBookAppointment = view.findViewById(R.id.btn_book_appointment);

		// Time buttons
		Button btnTime930 = view.findViewById(R.id.btn_time_930);
		Button btnTime1030 = view.findViewById(R.id.btn_time_1030);
		Button btnTime1130 = view.findViewById(R.id.btn_time_1130);
		Button btnTime330 = view.findViewById(R.id.btn_time_330);
		Button btnTime430 = view.findViewById(R.id.btn_time_430);
		Button btnTime530 = view.findViewById(R.id.btn_time_530);

		// Set the CalendarView to start from today's date
		calendarView.setMinDate(System.currentTimeMillis());

		// Set header text from parameters if needed
		if (mParam1 != null) {
			tvHeader.setText(mParam1);
		}

		setUpButton(btnTime930);
		setUpButton(btnTime1030);
		setUpButton(btnTime1130);
		setUpButton(btnTime330);
		setUpButton(btnTime430);
		setUpButton(btnTime530);

		backButton.setOnClickListener(v -> {
			getParentFragmentManager().popBackStack();
		});

		btnBookAppointment.setOnClickListener(v -> {
			// Handle book appointment button click
			showBookingSuccessMessage();
		});

		return view;
	}

	private void setUpButton(Button button) {
		button.setOnClickListener(v -> {
			// Reset the previously selected button
			if (lastSelectedButton != null) {
				lastSelectedButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
				lastSelectedButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
			}

			// Update the current button to the selected state
			button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.Primary_color)));
			button.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

			lastSelectedButton = button;
		});
	}

	private void showBookingSuccessMessage() {
		Toast.makeText(getActivity(), "Booking successfully!", Toast.LENGTH_SHORT).show();
	}

}
