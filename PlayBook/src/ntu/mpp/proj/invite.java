package ntu.mpp.proj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class invite extends Activity {
	/** Called when the activity is first created. */
	String me = "0922261111";
	Button bt1, bt2, bt3 ;
	EditText et1, et2, et3;
	TextView tv1, tv2, tv3;
	ImageButton ib1;
	int myYear, myMonth, myDay;
	int startYear, startMonth, startDay;
	int endYear, endMonth, endDay;
	int dueYear, dueMonth, dueDay;
	static final int START = 0;
	static final int END = 1;
	static final int DUE = 2;
	private ProgressDialog ProgressD;
	String string = "";
	String PhoneNumber;
	int counter = 0;
	String[] nameList = new String[1000];
	// String[] phoneList = new String[1000];
	String[] testList = new String[] { "0912606622", "0932228445",
			"0972523939", "0922263232", "0921319786", "0912345678" };
	// String[] userList = new String[5];
	ArrayList<String> globalUserList = new ArrayList<String>();
	ArrayList<String> globalPhoneList = new ArrayList<String>();
	ArrayList<String> localPhoneList = new ArrayList<String>();
	ArrayList<String> selectedPhoneList = new ArrayList<String>();
	ArrayList<String> selectedNameList = new ArrayList<String>();

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
		bt3 = (Button) findViewById(R.id.button3);
		ib1 = (ImageButton)findViewById(R.id.imageButton1);
		
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
		bt3.setOnClickListener(select_friend);

		ib1.setOnClickListener(map_listener);
		
		ProgressD = ProgressDialog.show(this, "",
				"朋友資料讀取中...\n你的朋友超多耶,\n請稍等一下下哦!!", true, false);

		// 得到ContentResolver對像

		new Thread() {
			public void run() {
				// Looper.prepare();
				ContentResolver cr = getContentResolver();
				// 取的電話部一開始的pointer
				Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
						null, null, null, null);
				// 向後移動pointer
				while (cursor.moveToNext() /*&& counter < 10*/) {
					// 取得連絡人名字
					int nameFieldColumnIndex = cursor
							.getColumnIndex(PhoneLookup.DISPLAY_NAME);
					String contact = cursor.getString(nameFieldColumnIndex);
					// Log.i("playbook", "name " + contact);

					// 取得電話號碼
					// 先清除上一個人的號碼
					PhoneNumber = null;
					String ContactId = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts._ID));
					// 取得聯絡人所有的行動電話
					Cursor phone = cr
							.query(
									ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
									null,
									ContactsContract.CommonDataKinds.Phone.CONTACT_ID
											+ "="
											+ ContactId
											+ " AND "
											+ ContactsContract.CommonDataKinds.Phone.TYPE
											+ "="
											+ ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
									null, null);
					while (phone.moveToNext()) {
						PhoneNumber = phone
								.getString(phone
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					phone.close();
					// 

					// 排除沒有行動電話的聯絡人
					if (PhoneNumber != null) {
						// nameList[counter] = contact;
						localPhoneList.add(PhoneNumber);
						counter++;
						Log.i("playbook", contact + " "
								+ localPhoneList.get(counter - 1));
					}
					// }

				}
				cursor.close();
				Log.i("counter", Integer.toString(counter));
				ProgressD.dismiss();
				// Looper.loop();

			}
		}.start();

		// ProgressD.dismiss();

	}

	OnClickListener select_friend = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final View v_ = v;
			// TODO Auto-generated method stub
			ProgressD = ProgressDialog.show(invite.this, "", "搜尋朋友中...", true,
					false);
			ParseQuery query = new ParseQuery("user_list");

			// 將手機內有的電話(PhoneList)丟到parse去查詢
			// ****不知道PhoneList裡面是不是有怪字元,丟PhoneList上去會exception,丟測試用的testList就不會
			query.whereContainedIn("account", Arrays.asList(localPhoneList
					.toArray()));

			query.findInBackground(new FindCallback() {
				public void done(List<ParseObject> commentList, ParseException e) {

					globalUserList.clear();
					globalPhoneList.clear();
					if (e != null) {
						Log.i("exception", "exception" + e.getMessage());
					} else {
						Log.i("user", "size " + commentList.size());
						// commentList now has the comments for myPost
						for (int i = 0; i < commentList.size(); i++) {

							Log.i("user", commentList.get(i).getString("name"));
							// userList[i] =
							// commentList.get(i).getString("name");
							globalUserList.add(commentList.get(i).getString(
									"name"));
							globalPhoneList.add(commentList.get(i).getString(
									"account"));
						}
						// **這一段現在執行不到
						Bundle bundle = new Bundle();
						// bundle.putStringArray("friend_list", userList);//
						// 實際上應改成userList
						bundle
								.putStringArrayList("friend_list",
										globalUserList);
						Intent intent = new Intent(v_.getContext(),
								FriendList.class);
						intent.putExtras(bundle);
						startActivityForResult(intent, 0);
						// **

					}
					ProgressD.dismiss();
				}
			});

			// **這一段之後要刪掉
			// Bundle bundle = new Bundle();
			// bundle.putStringArray("friend_list", testList);//實際上應改成userList
			// Intent intent = new Intent(v.getContext(), FriendList.class);
			// intent.putExtras(bundle);
			// startActivityForResult(intent, 0);
			// **

		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// selectedPhoneList.clear();

		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Log.i("extras", extras.getString("selectedIndex"));
					String[] temp = extras.getString("selectedIndex")
							.split(",");
					Log.i("extras", "length " + temp.length);
					for (int i = 0; i < temp.length; i++) {
						int index = Integer.parseInt(temp[i]);
						selectedPhoneList.add(globalPhoneList.get(index));
						selectedNameList.add(globalUserList.get(index));
						Log.i("selectPhone", globalPhoneList.get(index));
					}
				}
			}
		}

	};

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
	String today = null;
	OnClickListener select_end_date = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();
			myYear = c.get(Calendar.YEAR);
			myMonth = c.get(Calendar.MONTH);
			myDay = c.get(Calendar.DAY_OF_MONTH);
			today = String.valueOf(myYear) + String.format("%02d", myMonth + 1)
					+ String.format("%02d", myDay);
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

	String date_from = null;
	String date_from_ = null;
	private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			date_from = String.valueOf(year) + "/"
					+ String.valueOf(monthOfYear + 1) + "/"
					+ String.valueOf(dayOfMonth);
			date_from_ = String.valueOf(year)
					+ String.format("%02d", monthOfYear + 1)
					+ String.format("%02d", dayOfMonth);
			Log.i("date", "//" + String.format("%02d", monthOfYear + 1));
			startDay = dayOfMonth;
			startMonth = monthOfYear + 1;
			startYear = year;

			tv1.setText(date_from);
			// Toast.makeText(AndroidDatePicker.this, date,
			// Toast.LENGTH_LONG).show();
		}
	};
	String date_to = null;
	String date_to_ = null;
	private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			date_to = String.valueOf(year) + "/"
					+ String.valueOf(monthOfYear + 1) + "/"
					+ String.valueOf(dayOfMonth);
			date_to_ = String.valueOf(year)
					+ String.format("%02d", monthOfYear + 1)
					+ String.format("%02d", dayOfMonth);
			endDay = dayOfMonth;
			endMonth = monthOfYear + 1;
			endYear = year;

			tv2.setText(date_to);
			// Toast.makeText(AndroidDatePicker.this, date,
			// Toast.LENGTH_LONG).show();
		}
	};

	String date_dl = null;
	String date_dl_ = null;
	private DatePickerDialog.OnDateSetListener dueDateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			date_dl = String.valueOf(year) + "/"
					+ String.valueOf(monthOfYear + 1) + "/"
					+ String.valueOf(dayOfMonth);
			date_dl_ = String.valueOf(year)
					+ String.format("%02d", monthOfYear + 1)
					+ String.format("%02d", dayOfMonth);
			dueDay = dayOfMonth;
			dueMonth = monthOfYear + 1;
			dueYear = year;

			tv3.setText(date_dl);
			// Toast.makeText(AndroidDatePicker.this, date,
			// Toast.LENGTH_LONG).show();
		}
	};

	OnClickListener submit_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			DateFormat df;
			// 這裡要驗證三個日期都有選了 還有朋友有選了
			// 還有驗證不可以早於今天
			if (date_from == null || date_to == null || date_dl == null) {

				Toast.makeText(v.getContext(), "時間還沒填完!", Toast.LENGTH_LONG)
						.show();
			} else if (Integer.valueOf(today) >= Integer.valueOf(date_from_)) {
				Toast.makeText(v.getContext(), "活動開始日期不可以早於今天哦",
						Toast.LENGTH_LONG).show();
			} else if (Integer.valueOf(date_from_) > Integer.valueOf(date_to_)) {
				Toast.makeText(v.getContext(), "活動開始日期不可以晚於活動截止日期哦",
						Toast.LENGTH_LONG).show();
			} else if (Integer.valueOf(date_dl_) >= Integer.valueOf(date_from_)) {
				Toast.makeText(v.getContext(), "回報截止日期不可以晚於活動開始日期哦",
						Toast.LENGTH_LONG).show();
			}else if (Integer.valueOf(today) >= Integer.valueOf(date_dl_)) {
				Toast.makeText(v.getContext(), "回報截止日期不可以早於今天哦",
						Toast.LENGTH_LONG).show();
			} else if (selectedPhoneList.size() == 0) {
				Toast.makeText(v.getContext(), "還沒選擇好友哦", Toast.LENGTH_LONG)
						.show();
			} else if (et1.getText().equals("")) {
				Toast.makeText(v.getContext(), "活動內容還沒填寫哦", Toast.LENGTH_LONG)
						.show();
			} else {

				ParseObject testObject = new ParseObject("event_list");
				// testObject.put("state", "@submit button!");
				Log.i("playbook", "from=" + date_from + "to" + date_to
						+ "deadline" + date_dl);
				testObject.put("from", date_from);
				testObject.put("to", date_to);
				testObject.put("deadline", date_dl);
				//if flag_mark x,y
				if(global.flag_mark){
					testObject.put("x", global.p_.getLatitudeE6() / 1E6);
					testObject.put("y", global.p_.getLongitudeE6() /1E6);
				}
				// 用invite table 紀錄人跟活動的關係

				String time = new Date().toString();
				for (int i = 0; i < selectedPhoneList.size(); i++) {
					ParseObject invite = new ParseObject("invite");// 這要放裡面
					invite.put("event", "" + et1.getText());

					invite.put("friends", selectedPhoneList.get(i).toString());
					invite.put("friendName", selectedNameList.get(i).toString());
					invite.put("founder", global.me);// 開團者
					invite.put("foundername", global.my_name);// 開團者
					//if flag_mark x,y
					if(global.flag_mark){
						invite.put("x", global.p_.getLatitudeE6() / 1E6);
						invite.put("y", global.p_.getLongitudeE6() /1E6);
					}
					invite.put("status", "0");// 0:調查中 1:成團!
					invite.put("eventid", global.me + time);// eventid
					invite.saveInBackground();
				}

				//
				testObject.put("event", "" + et1.getText());
				testObject.put("location", "" + et2.getText());
				testObject.put("note", "" + et3.getText());
				testObject.put("founder", global.me);// 開團者
				testObject.put("foundername", global.my_name);// 開團者
				testObject.put("status", "0");// 0:調查中 1:成團!

				testObject.put("eventid", global.me + time);// eventid
				testObject.saveInBackground();

				Intent intent = new Intent();
				intent.setClass(invite.this, PlayBookActivity.class);
				startActivity(intent);
				invite.this.finish();
			}
		}
	};
	OnClickListener back_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Intent intent = new Intent();
			//intent.setClass(invite.this, PlayBookActivity.class);
			//startActivity(intent);
			invite.this.finish();
		}
	};
	OnClickListener map_listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(invite.this, MyMap.class);
			startActivity(intent);
			// invite.this.finish();
		}
	};
}
