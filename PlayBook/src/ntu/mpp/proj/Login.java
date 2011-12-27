package ntu.mpp.proj;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

class global {
	static String me = "";
	static GeoPoint p_;
	static boolean flag_mark=false;
	static String my_name="";
}

public class Login extends Activity {
	/**
	 * Called when the activity is first created.
	 * 
	 * @return
	 */
	boolean flag_sv = false;
	// boolean flag_in=false;
	private Button Blogin, Bclean;
	private TextView RegisterNew;
	EditText et1, et2;
	CheckBox cb1;
	boolean account_ck = true;
	private ProgressDialog ProgressD;
	private CheckBox.OnCheckedChangeListener listener = new CheckBox.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			if (cb1.isChecked() == true) {
				flag_sv = true;
				
				/*
				Toast.makeText(Login.this, "save ur account!",
						Toast.LENGTH_SHORT).show();*/
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		cb1 = (CheckBox) findViewById(R.id.checkBox1);
		et1 = (EditText) findViewById(R.id.Editaccount);
		et2 = (EditText) findViewById(R.id.Editpassword);
		Blogin = (Button) findViewById(R.id.Chooselogin);
		Bclean = (Button) findViewById(R.id.Chooseclean);
		RegisterNew = (TextView) findViewById(R.id.TextRegist);
		// cb1
		cb1.setOnCheckedChangeListener(listener);
		//
		SharedPreferences shp = getSharedPreferences("prefer", 0);
		// et1.setText(shp.getString("account", ""));
		// et2.setText(shp.getString("password", ""));
		if (shp.getBoolean("flag_in", false)) {
			global.me=shp.getString("account", "0922263232");
			global.my_name=shp.getString("my_name", "陳群元");
			Intent intent = new Intent();
			intent.setClass(Login.this, PlayBookActivity.class);
			startActivity(intent);
			Login.this.finish();
		}
		Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS",
				"tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
		RegisterNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Login.this, Register.class);
				startActivity(intent);
				Login.this.finish();
			}
		});
		Bclean.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et1.setText("");
				et2.setText("");
			}
		});
		Blogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent=new Intent();
				// intent.setClass(Login.this, PlayBookActivity.class);
				// startActivity(intent);

				ProgressD = ProgressDialog.show(Login.this, "", "擷取資料中...",
						true, false);
				// TODO Auto-generated method stub
				ParseQuery query = new ParseQuery("user_list");
				query.whereEqualTo("account", "" + et1.getText());
				query.whereEqualTo("password", "" + et2.getText());
				final View v_ = v;
				query.findInBackground(new FindCallback() {
					@Override
					public void done(List<ParseObject> IDList, ParseException e) {
						if (e == null) {

							if (IDList.size() != 0) {
								if (flag_sv) {
									// prefer
									SharedPreferences shp = getSharedPreferences(
											"prefer", 0);
									SharedPreferences.Editor editor = shp
											.edit();
									editor.putString("account",
											"" + et1.getText());
									editor.putString("password",
											"" + et2.getText());
									editor.putString("my_name",
											"" + IDList.get(0).getString("name"));
									editor.putBoolean("flag_in", true);
									editor.commit();// prefer end
								}
								global.my_name=""+IDList.get(0).getString("name");
								global.me = "" + et1.getText();
								account_ck = true;
								Intent intent = new Intent();
								intent.setClass(Login.this,
										PlayBookActivity.class);
								startActivity(intent);
								Login.this.finish();
							} else {
								// Toast.makeText(v.getContext(), "請填完整資料",
								// Toast.LENGTH_LONG).show();
								Login.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(v_.getContext(),
												"帳密不合法!", Toast.LENGTH_LONG)
												.show();
									}
								});
								account_ck = false;
							}
						}

						ProgressD.dismiss();
					}
				});
				if (account_ck == false) {
					// Toast.makeText(v.getContext(), "帳密不合法!",
					// Toast.LENGTH_LONG).show();
				}
				// Login.this.finish();
			}
		});
	}
}
