package com.example.cocoapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cocoapp.Adapter.NotificationAdapter;
import com.example.cocoapp.Object.NotificationItem;
import com.example.cocoapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notification extends Fragment {
	private RecyclerView recyclerView;
	private NotificationAdapter notificationAdapter;
	private List<NotificationItem> notificationList;

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public Notification() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment Notification.
	 */
	// TODO: Rename and change types and number of parameters
	public static Notification newInstance(String param1, String param2) {
		Notification fragment = new Notification();
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_notification, container, false);

		// Initialize RecyclerView
		recyclerView = view.findViewById(R.id.nearby_recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		String pic1 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.vet1;
		String pic2 = "android.resource://" + getContext().getPackageName() + "/" + R.drawable.vet2;

		// Initialize data
		notificationList = new ArrayList<>();
		notificationList.add(new NotificationItem(
				"Dr. Smith",
				"Upcoming appointment at 10 AM",
				"10m ago",
				pic1
		));
		notificationList.add(new NotificationItem(
				"Dr. Johnson",
				"Follow-up required",
				"1h ago",
				pic2
		));
		// Initialize adapter and set it to RecyclerView
		notificationAdapter = new NotificationAdapter(getContext(), notificationList);
		recyclerView.setAdapter(notificationAdapter);
		return view;
	}
}