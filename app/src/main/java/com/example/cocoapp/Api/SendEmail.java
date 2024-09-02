package com.example.cocoapp.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	private static final String TAG = "SendEmail";

	public static void sendEmail(Context context, String recipientEmail, String subject, String messageBody) {
		final String senderEmail = "daobathanh2004@gmail.com";
		final String senderPassword = "desqsizuggqtvtlz";

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		new SendMailTask(context, session, senderEmail, recipientEmail, subject, messageBody).execute();
	}

	private static class SendMailTask extends AsyncTask<Void, Void, Boolean> {

		private Context context;
		private Session session;
		private String senderEmail;
		private String recipientEmail;
		private String subject;
		private String messageBody;

		public SendMailTask(Context context, Session session, String senderEmail, String recipientEmail, String subject, String messageBody) {
			this.context = context;
			this.session = session;
			this.senderEmail = senderEmail;
			this.recipientEmail = recipientEmail;
			this.subject = subject;
			this.messageBody = messageBody;
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(senderEmail));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
				message.setSubject(subject);
				message.setText(messageBody);

				Transport.send(message);
				return true;
			} catch (Exception e) {
				Log.e(TAG, "Error sending email", e);
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean success) {
			if (success) {
				Toast.makeText(context, "Email sent successfully!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "Failed to send email. Please try again.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
