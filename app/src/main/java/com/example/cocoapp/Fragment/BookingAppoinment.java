package com.example.cocoapp.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	private Button btnTime930, btnTime1030, btnTime1130, btnTime330, btnTime430, btnTime530;


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

	public BookingAppoinment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_booking_appoinment, container, false);
		apiService = ApiClient.getClient(requireActivity(), false).create(ApiService.class);
		SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
		token = prefs.getString("jwt_token", null);

		ImageButton backButton = view.findViewById(R.id.back_button);
		TextView tvHeader = view.findViewById(R.id.tvHeader);
		TextView tvChooseDate = view.findViewById(R.id.tvChooseDate);
		CalendarView calendarView = view.findViewById(R.id.calendar_view);
		TextView tvPickTime = view.findViewById(R.id.tvPickTime);
		Button btnBookAppointment = view.findViewById(R.id.btn_book_appointment);
		RadioGroup radioGroupSort = view.findViewById(R.id.radioGroupSort);
		EditText name = view.findViewById(R.id.etEnterName);

		btnTime930 = view.findViewById(R.id.btn_time_930);
		btnTime1030 = view.findViewById(R.id.btn_time_1030);
		btnTime1130 = view.findViewById(R.id.btn_time_1130);
		btnTime330 = view.findViewById(R.id.btn_time_330);
		btnTime430 = view.findViewById(R.id.btn_time_430);
		btnTime530 = view.findViewById(R.id.btn_time_530);

		calendarView.setMinDate(System.currentTimeMillis());
		calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
			month+=1;
			selectedDate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth);
			validateTimeSlots(selectedDate);
		});

		setUpTimeButtons(new Button[]{btnTime930, btnTime1030, btnTime1130, btnTime330, btnTime430, btnTime530});

		backButton.setOnClickListener(v -> { getParentFragmentManager().popBackStack(); });

		if (veterinarian.getVetName() != null) { tvHeader.setText("Booking with " + veterinarian.getVetName()); }

		radioGroupSort.setOnCheckedChangeListener((group, checkedId) -> { selectedCategory = checkedId == R.id.radioDefault ? "Vaccination" : "Disease"; });

		selectedCategory = "Vaccination";

		btnBookAppointment.setOnClickListener(v -> {
			String selectedDate = getSelectedDate();
			String selectedTime = getSelectedTime();
			String nameText = name.getText().toString().trim();

			if (nameText.isEmpty()) {
				Toast.makeText(getActivity(), "Please enter the type name.", Toast.LENGTH_SHORT).show();
				return;
			}

			if (selectedTime == null || selectedTime.isEmpty()) {
				Toast.makeText(getActivity(), "Please select time.", Toast.LENGTH_SHORT).show();
				return;
			}

			if (selectedDate == null || selectedDate.isEmpty()) {
				Toast.makeText(getActivity(), "Please select date.", Toast.LENGTH_SHORT).show();
				return;
			}

			if (selectedCategory == null || selectedCategory.isEmpty()) {
				Toast.makeText(getActivity(), "Please select a category type.", Toast.LENGTH_SHORT).show();
				return;
			}

			long timeInMillis = convertToMilliseconds(selectedDate, selectedTime);
			long currentTimeInMillis = System.currentTimeMillis();
			if (timeInMillis < currentTimeInMillis) {
				Toast.makeText(getActivity(), "Cannot book an appointment in the past.", Toast.LENGTH_SHORT).show();
				return;
			}

			Appointment appointment = new Appointment(timeInMillis, veterinarian.getVetId(), selectedCategory, String.valueOf(name.getText()));
			addAppointment(appointment);

		});

		return view;
	}

	private void validateTimeSlots(String selectedDate) {

		List<Button> timeButtons = Arrays.asList(
				btnTime930, btnTime1030, btnTime1130, btnTime330, btnTime430, btnTime530
		);

		resetButtonsToDefault(timeButtons);

		Set<DayOfWeek> workDays = parseWorkDays(veterinarian.getWorkTime());
		DayOfWeek selectedDay = LocalDate.parse(selectedDate).getDayOfWeek();

		Log.d("BookingAppoinment", "Selected Date: " + selectedDate);
		Log.d("BookingAppoinment", "Veterinarian Work Days: " + workDays.toString());
		Log.d("BookingAppoinment", "Selected Day: " + selectedDay);

		if (!workDays.contains(selectedDay)) {
			for (Button button : timeButtons) {
				disableButton(button);
			}
		} else {
			setUpTimeButtons(timeButtons.toArray(new Button[0]));
		}
	}

	private void resetButtonsToDefault(List<Button> buttons) {
		for (Button button : buttons) {
			button.setEnabled(true);
			button.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
			button.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
			button.setOnClickListener(null);
		}
	}


	private long convertToMilliseconds(String selectedDate, String selectedTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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

	private Set<LocalTime> getWorkingHours(String workTime) {
		Set<LocalTime> validTimes = new HashSet<>();

		try {
			String[] parts = workTime.split(" at ");
			if (parts.length != 2) {
				throw new IllegalArgumentException("Invalid format for workTime: " + workTime);
			}

			String daysPart = parts[0].trim();
			String timesPart = parts[1].trim();

			String[] timeRange = timesPart.split(" - ");
			if (timeRange.length != 2) {
				throw new IllegalArgumentException("Invalid time range in workTime: " + workTime);
			}

			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
			LocalTime startTime = LocalTime.parse(timeRange[0].trim(), timeFormatter);
			LocalTime endTime = LocalTime.parse(timeRange[1].trim(), timeFormatter);

			LocalTime timeSlot = startTime;
			while (!timeSlot.isAfter(endTime)) {
				validTimes.add(timeSlot);
				timeSlot = timeSlot.plusMinutes(30);
			}

		} catch (DateTimeParseException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return validTimes;
	}

	private void setUpTimeButtons(Button[] buttons) {
		Set<LocalTime> workingHours = getWorkingHours(veterinarian.getWorkTime());
		DateTimeFormatter flexibleFormatter = DateTimeFormatter.ofPattern("[H:mm][HH:mm]");

		for (Button button : buttons) {
			String buttonTime = button.getText().toString();

			try {
				LocalTime time = LocalTime.parse(buttonTime, flexibleFormatter);

				if (!workingHours.contains(time)) {
					disableButton(button);
				} else {
					enableButton(button, time);
				}
			} catch (DateTimeParseException e) {
				Log.e("Time Parsing", "Failed to parse time: " + buttonTime, e);
				disableButton(button);
			}
		}
	}
	private void disableButton(Button button) {
		button.setEnabled(false);
		button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D3D3D3")));
		button.setOnClickListener(v -> showInvalidTimeDialog());
	}

	private void enableButton(Button button, LocalTime time) {
		button.setEnabled(true);
		button.setOnClickListener(v -> selectTime(button, time.toString()));
	}

	private void selectTime(Button button, String time) {
		if (lastSelectedButton != null) {
			lastSelectedButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
			lastSelectedButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
		}
		button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.Primary_color)));
		button.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
		selectedTime = time;
		lastSelectedButton = button;
	}

	private void showInvalidTimeDialog() {
		new AlertDialog.Builder(getContext())
				.setTitle("Invalid Time")
				.setMessage("This time slot is outside the veterinarian's working hours.")
				.setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
				.show();
	}

	private boolean isWithinWorkingHours(String selectedDate, String selectedTime, String workTime) {
		try {
			String[] parts = workTime.split("at");
			String daysPart = parts[0].trim();
			String timePart = parts[1].trim();

			Set<DayOfWeek> workDays = parseWorkDays(daysPart);
			DayOfWeek selectedDay = LocalDate.parse(selectedDate).getDayOfWeek();

			if (!workDays.contains(selectedDay)) {
				return false;
			}

			String[] times = timePart.split("-");
			LocalTime startWorkTime = LocalTime.parse(times[0].replace('.', ':'), DateTimeFormatter.ofPattern("H:mm"));
			LocalTime endWorkTime = LocalTime.parse(times[1].replace('.', ':'), DateTimeFormatter.ofPattern("H:mm"));
			LocalTime selectedLocalTime = LocalTime.parse(selectedTime, DateTimeFormatter.ofPattern("H:mm"));

			return !selectedLocalTime.isBefore(startWorkTime) && !selectedLocalTime.isAfter(endWorkTime);
		} catch (Exception e) {
			Log.e("WorkTimeParsing", "Error parsing work time: " + workTime, e);
			return false;
		}
	}

	private Set<DayOfWeek> parseWorkDays(String daysPart) {
		Set<DayOfWeek> days = new HashSet<>();
		daysPart = daysPart.toLowerCase().trim();

		List<DayOfWeek> allDays = Arrays.asList(
				DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
				DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
		);

		for (int i = 0; i < allDays.size(); i++) {
			for (int j = i + 1; j < allDays.size(); j++) {
				String range = allDays.get(i).name().toLowerCase() + " - " + allDays.get(j).name().toLowerCase();
				if (daysPart.contains(range)) {
					for (int k = i; k <= j; k++) {
						days.add(allDays.get(k));
					}
					return days;
				}
			}
		}

		for (DayOfWeek day : allDays) {
			if (daysPart.contains(day.name().toLowerCase())) {
				days.add(day);
			}
		}

		Log.d("BookingAppoinment", "Parsed Work Days: " + days);

		return days;
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
