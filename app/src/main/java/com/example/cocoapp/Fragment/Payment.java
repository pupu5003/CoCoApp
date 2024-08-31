package com.example.cocoapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cocoapp.Api.CreateOrder;
import com.example.cocoapp.MainActivity;
import com.example.cocoapp.R;

import org.json.JSONObject;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Payment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Payment extends Fragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public Payment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment Payment.
	 */
	// TODO: Rename and change types and number of parameters
	public static Payment newInstance(String param1, String param2) {
		Payment fragment = new Payment();
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
		View view = inflater.inflate(R.layout.fragment_payment, container, false);
		Button btn_pay = view.findViewById(R.id.btn_pay_now);
		TextView amount = view.findViewById(R.id.tv_amount_value);

		StrictMode.ThreadPolicy policy = new
				StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// ZaloPay SDK Init
		ZaloPaySDK.init(2553, Environment.SANDBOX);
		btn_pay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CreateOrder orderApi = new CreateOrder();
				try {
					JSONObject data = orderApi.createOrder(amount.getText().toString());
					String code = data.getString("return_code");
					Log.d("data", code);

					if (code.equals("1")) {
						String token = data.getString("zp_trans_token");
						ZaloPaySDK.getInstance().payOrder(requireActivity(), token, "demozpdk://app", new PayOrderListener() {

							@Override
							public void onPaymentSucceeded(String s, String s1, String s2) {
							}

							@Override
							public void onPaymentCanceled(String s, String s1) {

							}

							@Override
							public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

							}
						});
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return view;
	}
	public void handleNewIntent(Intent intent) {
		// Handle the intent data
		if (intent != null) {
			ZaloPaySDK.getInstance().onResult(intent);
			// Handle the response logic, for example, update the UI based on payment result
			// You can pass specific data from the intent to methods here if needed
		}
	}


}