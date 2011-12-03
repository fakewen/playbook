package ntu.mpp.proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class invite extends Activity {
    /** Called when the activity is first created. */
    Button bt1,bt2;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite);
        
        bt1=(Button)findViewById(R.id.button1);
        bt2=(Button)findViewById(R.id.button2);
        
        bt1.setOnClickListener(submit_listener);
        bt2.setOnClickListener(submit_listener);
    }
	OnClickListener submit_listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(invite.this, PlayBookActivity.class);
			startActivity(intent);
			invite.this.finish();
		}
	};
}