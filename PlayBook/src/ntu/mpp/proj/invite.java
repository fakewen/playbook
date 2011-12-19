package ntu.mpp.proj;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class invite extends Activity {
	/** Called when the activity is first created. */
	String me="0922261111";
	Button bt1, bt2;
	EditText et1, et2, et3;
	TextView tv1, tv2, tv3;
	int myYear, myMonth, myDay;
	int startYear, startMonth, startDay;
	int endYear, endMonth, endDay;
	int dueYear, dueMonth, dueDay;
	static final int START = 0;
	static final int END = 1;
	static final int DUE = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite);
		// parse SQL init here
		Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS",
				"tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
		// need push to button
		bt1 = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);
		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.editText2);
		et3 = (EditText) findViewById(R.id.editText3);
		tv1 = (TextView) findViewById(R.id.textView_start);
		tv2 = (TextView) findViewById(R.id.textView_end);
		tv3 = (TextView) findViewById(R.id.textView_stop);
		bt1.setOnClickListener(submit_listener);
		bt2.setOnClickListener(back_listener);
		tv1.setOnClickListener(select_start_date);
		tv2.setOnClickListener(select_end_date);
		tv3.setOnClickListener(select_due_date);
	}

	OnClickListener select_start_date = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();
			myYear = c.get(Calendar.YEAR);
			myMonth = c.get(Calendar.MONTH);
			myDay = c.get(Calendar.DAY_OF_MONTH);
			showDialog(START);
		}
	};

	OnClickListener select_end_date = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();
			myYear = c.get(Calendar.YEAR);
			myMonth = c.get(Calendar.MONTH);
			myDay = c.get(Calendar.DAY_OF_MONTH);
			showDialog(END);
		}
	};

	OnClickListener select_due_date = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();
			myYear = c.get(Calendar.YEAR);
			myMonth = c.get(Calendar.MONTH);
			myDay = c.get(Calendar.DAY_OF_MONTH);
			showDialog(DUE);
		}
	};

	protected android.app.Dialog onCreateDialog(int id) {
		switch (id) {
		case START:
			// Toast.makeText(DatePicker.this,
			// "- onCreateDialog(ID_DATEPICKER) -", Toast.LENGTH_LONG)
			// .show();
			return new DatePickerDialog(this, startDateListener, myYear,
					myMonth, myDay);
		case END:
			return new DatePickerDialog(this, endDateListener, myYear, myMonth,
					myDay);
		case DUE:
			return new DatePickerDialog(this, dueDateListener, myYear, myMonth,
					myDay);

		default:
			return null;
		}

	};

	private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			String date =  String.valueOf(year)  + "/"
					+ String.valueOf(monthOfYear + 1) + "/"
					+ String.valueOf(dayOfMonth);
			startDay = dayOfMonth;
			startMonth = monthOfYear + 1;
			startYear = year;

			tv1.setText(date);
			// Toast.makeText(AndroidDatePicker.this, date,
			// Toast.LENGTH_LONG).show();
		}
	};
	
	private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			String date =  String.valueOf(year)  + "/"
			+ String.valueOf(monthOfYear + 1) + "/"
			+ String.valueOf(dayOfMonth);
			endDay = dayOfMonth;
			endMonth = monthOfYear + 1;
			endYear = year;

			tv2.setText(date);
			// Toast.makeText(AndroidDatePicker.this, date,
			// Toast.LENGTH_LONG).show();
		}
	};
	
	private DatePickerDialog.OnDateSetListener dueDateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			String date =  String.valueOf(year)  + "/"
			+ String.valueOf(monthOfYear + 1) + "/"
			+ String.valueOf(dayOfMonth);
			dueDay = dayOfMonth;
			dueMonth = monthOfYear + 1;
			dueYear = year;

			tv3.setText(date);
			// Toast.makeText(AndroidDatePicker.this, date,
			// Toast.LENGTH_LONG).show();
		}
	};

	OnClickListener submit_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//這裡要驗證三個日期都有選了 還有朋友有選了
			ParseObject testObject = new ParseObject("event_list");
			// testObject.put("state", "@submit button!");
			testObject.put("from", "2011/12/12");
			testObject.put("to", "2011/12/19");
			testObject.put("deadline", "2011/12/25");
			// 用invite table 紀錄人跟活動的關係
			String friends[] = { "0922262222", "0922261111" };// 被邀請的人們
			String time=new Date().toString();
			for (int i = 0; i < friends.length; i++) {
				ParseObject invite = new ParseObject("invite");// 這要放裡面
				invite.put("event", "" + et1.getText());
				invite.put("friends", friends[i]);
				invite.put("founder", "0922263232");// 開團者
				invite.put("status", "0");// 0:調查中 1:成團!
				invite.put("eventid", gl.me+time);//eventid
				invite.saveInBackground();
			}

			//
			testObject.put("event", "" + et1.getText());
			testObject.put("location", "" + et2.getText());
			testObject.put("note", "" + et3.getText());
			testObject.put("founder", "0922263232");// 開團者
			testObject.put("status", "0");// 0:調查中 1:成團!
			
			testObject.put("eventid", gl.me+time);//eventid
			testObject.saveInBackground();

			Intent intent = new Intent();
			intent.setClass(invite.this, PlayBookActivity.class);
			startActivity(intent);
			invite.this.finish();
		}
	};
	OnClickListener back_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(invite.this, PlayBookActivity.class);
			startActivity(intent);
			// invite.this.finish();
		}
	};
}