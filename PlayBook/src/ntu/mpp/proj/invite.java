package ntu.mpp.proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;
public class invite extends Activity {
    /** Called when the activity is first created. */
    Button bt1,bt2;
    EditText et1,et2,et3;
    TextView tv1,tv2,tv3;
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;
    int stopYear, stopMonth, stopDay;
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
        tv1=(TextView) findViewById(R.id.textView_start);
        tv2=(TextView) findViewById(R.id.textView_end);
        tv3=(TextView) findViewById(R.id.textView_stop);
        bt1.setOnClickListener(submit_listener);
        bt2.setOnClickListener(back_listener);
    }
	
	OnClickListener select_start_date = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	OnClickListener submit_listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			ParseObject testObject = new ParseObject("event_list");
	        //testObject.put("state", "@submit button!");
			testObject.put("from", "2011/12/12");
			testObject.put("to", "2011/12/19");
			testObject.put("deadline", "2011/12/25");
			//用invite table 紀錄人跟活動的關係
			String friends[]={"0922262222" , "0922261111"};//被邀請的人們 
			
			for(int i=0 ; i<friends.length ; i++){
				ParseObject invite = new ParseObject("invite");//這要放裡面
				invite.put("event", ""+et1.getText());
				invite.put("friends", friends[i]);
				invite.put("founder", "0922263232");//開團者
		        invite.put("status", "1");//0:調查中  1:成團!
				invite.saveInBackground();
			}
			
			//
	        testObject.put("event", ""+et1.getText());
	        testObject.put("location", ""+et2.getText());
	        testObject.put("note", ""+et3.getText());
	        testObject.put("founder", "0922263232");//開團者
	        testObject.put("status", "1");//0:調查中  1:成團!
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