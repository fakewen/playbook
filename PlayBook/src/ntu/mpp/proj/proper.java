package ntu.mpp.proj;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
public class proper extends Activity {
    /** Called when the activity is first created. */
    Button bt1,bt2;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
    boolean master = false;
    private ProgressDialog ProgressD;
    String eventid_bundle;
    String from_bundle;
    String to_bundle;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proper);
        tv1=(TextView)findViewById(R.id.textView1);
        tv2=(TextView)findViewById(R.id.textView2);
        tv3=(TextView)findViewById(R.id.textView3);
        tv4=(TextView)findViewById(R.id.textView4);
        tv5=(TextView)findViewById(R.id.textView5);
        tv6=(TextView)findViewById(R.id.textView6);
        tv7=(TextView)findViewById(R.id.textView7);
        tv8=(TextView)findViewById(R.id.textView8);
        
        Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
        Bundle bData = getIntent().getExtras();
        //Log.i("playbook", "got event:"+bData.getString("event_name"));
        //Log.i("playbook", bData.getString("ck"));
        ProgressD = ProgressDialog.show(this, "", "擷取資料中...", true, false);
  	  	ParseQuery query = new ParseQuery("event_list");
  	  	//query.whereEqualTo("event", bData.getString("event_name"));//找出自己有被邀請的活動
  	  	query.whereEqualTo("eventid", bData.getString("event_id"));
  	  eventid_bundle=bData.getString("event_id");
  	  Log.i("playbook", "eventid="+bData.getString("event_id"));
  	  	query.findInBackground(new FindCallback(){
  		  @Override
  			public void done(List<ParseObject> IDList, ParseException e) {
  			  if (e == null) {
  				  Log.i("playbook", "list size="+Integer.toString(IDList.size()));
  				tv1.setText("活動內容:"+IDList.get(0).getString("event"));
  				tv2.setText("活動地點:"+IDList.get(0).getString("location"));
  				tv3.setText("發起人:"+IDList.get(0).getString("founder"));
  				tv4.setText("活動日期從:"+IDList.get(0).getString("from"));
  				from_bundle=IDList.get(0).getString("from");
  				tv5.setText("活動日期到:"+IDList.get(0).getString("to"));
  				to_bundle=IDList.get(0).getString("to");
  				tv6.setText("活動截止日期:"+IDList.get(0).getString("deadline"));
  				tv7.setText(IDList.get(0).getString("status").equals("1")?"已成團":"調查中");
  				tv8.setText(IDList.get(0).getString("note"));
  			  }
  			ProgressD.dismiss();
  		  	}
  		});
        bt1=(Button)findViewById(R.id.button1);
        bt2=(Button)findViewById(R.id.button2);
        
        bt1.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("eventid", eventid_bundle);
				bundle.putString("from", from_bundle);
				bundle.putString("to", to_bundle);
				if(master){
					Intent intent=new Intent();
					intent.setClass(proper.this, PeopleCnt.class);
					intent.putExtras(bundle);
					startActivity(intent);
					//proper.this.finish();	
				}
				else{
					Intent intent=new Intent();
					intent.setClass(proper.this, FreeTime.class);
					intent.putExtras(bundle);
					startActivity(intent);
					//proper.this.finish();
				}
			}
		});
        bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(proper.this, ElistCBox.class);
				startActivity(intent);
				//proper.this.finish();
			}
		});
        
        
    }
}