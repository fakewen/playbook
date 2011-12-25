package ntu.mpp.proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PlayBookActivity extends Activity {
    /** Called when the activity is first created. */
    Button bt1,bt2;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        bt1=(Button)findViewById(R.id.button2);
        bt2=(Button)findViewById(R.id.button3);
        
        bt1.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(PlayBookActivity.this, invite.class);
				//intent.setClass(PlayBookActivity.this, MyMap.class);
				startActivity(intent);
				//PlayBookActivity.this.finish();
			}
		});
        bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(PlayBookActivity.this, ElistCBox.class);
				startActivity(intent);
				//PlayBookActivity.this.finish();
			}
		});
        
        
    }
}