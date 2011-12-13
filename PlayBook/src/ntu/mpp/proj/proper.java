package ntu.mpp.proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class proper extends Activity {
    /** Called when the activity is first created. */
    Button bt1,bt2;
    boolean master = true;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proper);
        
        bt1=(Button)findViewById(R.id.button1);
        bt2=(Button)findViewById(R.id.button2);
        
        bt1.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(master){
					Intent intent=new Intent();
					intent.setClass(proper.this, PeopleCnt.class);
					startActivity(intent);
					proper.this.finish();	
				}
				else{
					Intent intent=new Intent();
					intent.setClass(proper.this, FreeTime.class);
					startActivity(intent);
					proper.this.finish();
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
				proper.this.finish();
			}
		});
        
        
    }
}