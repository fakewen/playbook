package ntu.mpp.proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class Register extends Activity {
    /** Called when the activity is first created. */
	EditText et1,et2,et3,et4,et5,et6;
	Button bt1,bt2;
	String account,password,password2,name,nick,email;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
        et1=(EditText)findViewById(R.id.EditAcct);
        et2=(EditText)findViewById(R.id.EditWord);
        et3=(EditText)findViewById(R.id.EditDblChk);
        et4=(EditText)findViewById(R.id.EditName);
        et5=(EditText)findViewById(R.id.EditNick);
        et6=(EditText)findViewById(R.id.EditMail);
        bt1=(Button)findViewById(R.id.Regsend);
        bt2=(Button)findViewById(R.id.Regclean);
        bt1.setOnClickListener(send);
        bt2.setOnClickListener(clean);
	}
	OnClickListener send=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.i("playbook","click send button!");
			// TODO Auto-generated method stub
			if(et1.getText().length()==0){
				Toast.makeText(v.getContext(), "請填完整資料", Toast.LENGTH_LONG).show();
			}
			else if(et2.getText().length()==0){
				Toast.makeText(v.getContext(), "請填完整資料", Toast.LENGTH_LONG).show();
			}
			else if(et3.getText().length()==0){
				Toast.makeText(v.getContext(), "請填完整資料", Toast.LENGTH_LONG).show();
			}
			else if(et4.getText().length()==0){
				Toast.makeText(v.getContext(), "請填完整資料", Toast.LENGTH_LONG).show();
			}
			else if(et5.getText().length()==0){
				Toast.makeText(v.getContext(), "請填完整資料", Toast.LENGTH_LONG).show();
			}
			else if(et6.getText().length()==0){
				Toast.makeText(v.getContext(), "請填完整資料", Toast.LENGTH_LONG).show();
			}else{
				//save user
				//account,password,password2,name,nick,email
				ParseObject testObject = new ParseObject("user_list");
				testObject.put("account", ""+et1.getText());
				testObject.put("password", ""+et2.getText());
				testObject.put("password2", ""+et3.getText());
				testObject.put("name", ""+et4.getText());
				testObject.put("nick", ""+et5.getText());
				testObject.put("email", ""+et6.getText());
				testObject.saveInBackground();
				Toast.makeText(v.getContext(), "申請成功!", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(Register.this, PlayBookActivity.class);
				startActivity(intent);
				Register.this.finish();
			}
			
		}
	};
	OnClickListener clean=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			et1.setText("");
			et2.setText("");
			et3.setText("");
			et4.setText("");
			et5.setText("");
			et6.setText("");

		}
	};
}