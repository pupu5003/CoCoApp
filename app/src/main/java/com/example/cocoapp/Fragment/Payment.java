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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
	private Integer mParam1;
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
	public static Payment newInstance(Integer param1) {
		Payment fragment = new Payment();
		Bundle args = new Bundle();
		args.putInt(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getInt(ARG_PARAM1);

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_payment, container, false);
		Button btn_pay = view.findViewById(R.id.btn_pay_now);
		TextView amount = view.findViewById(R.id.tv_amount_value);
		amount.setText(String.valueOf(mParam1));
		ImageButton backbtn = view.findViewById(R.id.back_button);
		RadioButton rb_momo = view.findViewById(R.id.rb_momo);
		RadioButton rb_zalopay = view.findViewById(R.id.rb_zalopay);

		rb_momo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "MoMo is not supported yet", Toast.LENGTH_SHORT).show();
			}
		});

		backbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		rb_zalopay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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