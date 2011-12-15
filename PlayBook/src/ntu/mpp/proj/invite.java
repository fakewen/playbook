package ntu.mpp.proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseObject;
public class invite extends Activity {
    /** Called when the activity is first created. */
    Button bt1,bt2;
    EditText et1,et2,et3;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite);
        //parse SQL init here
        Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
        //need push to button
        bt1=(Button)findViewById(R.id.button1);
        bt2=(Button)findViewById(R.id.button2);
        et1=(EditText)findViewById(R.id.editText1);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText3);
        bt1.setOnClickListener(submit_listener);
        bt2.setOnClickListener(back_listener);
    }
	OnClickListener submit_listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			/*
			invite.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ParseObject testObject = new ParseObject("TestObject");
			        //testObject.put("state", "@submit button!");
			        testObject.put("event", et1.getText());
			        testObject.put("location", et2.getText());
			        testObject.put("note", et3.getText());
			        testObject.saveInBackground();
				}
			});*/
			ParseObject testObject = new ParseObject("TestObject");
	        //testObject.put("state", "@submit button!");
			testObject.put("from", "from");
			testObject.put("to", "to");
			testObject.put("deadline", "deadline");
			testObject.put("friends", "friends");
			
	        testObject.put("event", ""+et1.getText());
	        testObject.put("location", ""+et2.getText());
	        testObject.put("note", ""+et3.getText());
	        testObject.saveInBackground();
			
			Intent intent=new Intent();
			intent.setClass(invite.this, PlayBookActivity.class);
			startActivity(intent);
			invite.this.finish();
		}
	};
	OnClickListener back_listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(invite.this, PlayBookActivity.class);
			startActivity(intent);
			//invite.this.finish();
		}
	};
}