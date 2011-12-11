package ntu.mpp.proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Choose extends Activity {
    /** Called when the activity is first created. 
     * @return */
	private Button Blogin,Bclean;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Blogin=(Button) findViewById(R.id.Chooselogin);
        Bclean=(Button) findViewById(R.id.Chooseclean);
        
        
       Blogin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Choose.this, PlayBookActivity.class);
				startActivity(intent);
				Choose.this.finish();
			}
		});
	}
}
