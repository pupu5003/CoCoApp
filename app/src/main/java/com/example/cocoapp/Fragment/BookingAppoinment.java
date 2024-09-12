package com.example.cocoapp.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cocoapp.Api.ApiClient;
import com.example.cocoapp.Api.ApiService;
import com.example.cocoapp.Object.Appointment;
import com.example.cocoapp.Object.Veterinarian;
import com.example.cocoapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingAppoinment extends Fragment {
	private static final String ARG_VETERINARIAN = "veterinarian";
	private Veterinarian veterinarian;
	private Button lastSelectedButton = null;
	private String selectedCategory;
	private String token;
	private ApiService apiService;
	String selectedDate;
	String selectedTime ;


	public static BookingAppoinment newInstance(Veterinarian veterinarian) {
		BookingAppoinment fragment = new BookingAppoinment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_VETERINARIAN, veterinarian);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			veterinarian = (Veterinarian) getArguments().getSerializable(ARG_VETERINARIAN);
		};
	}


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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_booking_appoinment, container, false);
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		// Initialize UI elements
		ImageButton backButton = view.findViewById(R.id.back_button);
		TextView tvHeader = view.findViewById(R.id.tvHeader);
		TextView tvChooseDate = view.findViewById(R.id.tvChooseDate);
		CalendarView calendarView = view.findViewById(R.id.calendar_view);
		TextView tvPickTime = view.findViewById(R.id.tvPickTime);
		Button btnBookAppointment = view.findViewById(R.id.btn_book_appointment);
		RadioGroup radioGroupSort = view.findViewById(R.id.radioGroupSort);
		EditText name = view.findViewById(R.id.etEnterName);

		calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
			month+=1;
			selectedDate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth);
		});


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

		setUpButton(btnTime930, "09:30");
		setUpButton(btnTime1030, "10:30");
		setUpButton(btnTime1130, "11:30");
		setUpButton(btnTime330, "15:30");
		setUpButton(btnTime430, "16:30");
		setUpButton(btnTime530, "17:30");


		backButton.setOnClickListener(v -> {
			getParentFragmentManager().popBackStack();
		});

		if (veterinarian.getVetName() != null) {
			tvHeader.setText("Booking with " + veterinarian.getVetName());
		}

		backButton.setOnClickListener(v -> {
			getParentFragmentManager().popBackStack();
		});

		radioGroupSort.setOnCheckedChangeListener((group, checkedId) -> {
			if (checkedId == R.id.radioDefault) {
				selectedCategory = "Vaccination";
			} else if (checkedId == R.id.radioSortByWeight) {
				selectedCategory = "Disease";
			}
		});

		selectedCategory = "Vaccination";

		btnBookAppointment.setOnClickListener(v -> {
			String selectedDate = getSelectedDate();
			String selectedTime = getSelectedTime();

			if (selectedTime.isEmpty()) {
				Toast.makeText(getActivity(), "Please select time.", Toast.LENGTH_SHORT).show();
				return;
			}

			long timeInMillis = convertToMilliseconds(selectedDate, selectedTime);
			Appointment appointment = new Appointment(timeInMillis, veterinarian.getVetId(), selectedCategory, String.valueOf(name.getText()));

			addAppointment(appointment);

			Toast.makeText(getActivity(), "Booking successfully!", Toast.LENGTH_SHORT).show();
			getParentFragmentManager().popBackStack();
		});



		return view;
	}

	private long convertToMilliseconds(String selectedDate, String selectedTime) {
		selectedDate = selectedDate.trim();
		selectedTime = selectedTime.trim();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
			throw new IllegalArgumentException("Date or time cannot be empty");
		}

		String dateTimeString = selectedDate + " " + selectedTime;

		try {
			ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
			LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
			ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, vietnamZoneId);
			return zonedDateTime.toInstant().toEpochMilli();
		} catch (DateTimeParseException e) {
			Log.e("DateTimeParseException", "Error parsing date and time: " + dateTimeString, e);
			throw e;
		}
	}

	private void setUpButton(Button button, String time) {
		button.setOnClickListener(v -> {
			if (lastSelectedButton != null) {
				lastSelectedButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
				lastSelectedButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
			}
			button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.Primary_color)));
			button.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

			selectedTime = time;
			lastSelectedButton = button;
		});
	}


	private String getSelectedDate() {
		return selectedDate;
	}


	private String getSelectedTime() {
		return selectedTime;
	}

	private void showBookingSuccessMessage() {
		Toast.makeText(getActivity(), "Booking successfully!", Toast.LENGTH_SHORT).show();
	}

	private void addAppointment(Appointment appointment) {
		apiService.addAppointment("Bearer " + token, appointment).enqueue(new Callback<String>() {
			@Override
			public void onResponse(@NonNull Call<String> call, Response<String> response) {
				if (response.isSuccessful()) {
					Toast.makeText(getActivity(), "Appointment added successfully!", Toast.LENGTH_SHORT).show();
				} else {
					Log.e("API Error", "Response code: " + response.code() + " Message: " + response.message());
					if (getContext()!=null)
						Toast.makeText(getContext(), "Failed to add appointment", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
				Log.e("API Error", t.getMessage());
				if (getContext()!=null)
					Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}


}
