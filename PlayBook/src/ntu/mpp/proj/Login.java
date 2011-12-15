package ntu.mpp.proj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Login extends Activity {
    /** Called when the activity is first created. 
     * @return */
	private Button Blogin,Bclean;
	private TextView RegisterNew;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Blogin=(Button) findViewById(R.id.Chooselogin);
        Bclean=(Button) findViewById(R.id.Chooseclean);
        RegisterNew = (TextView) findViewById(R.id.TextRegist);
        
        RegisterNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Login.this, Register.class);
				startActivity(intent);
				//Login.this.finish();
			}
		});
       Blogin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Login.this, PlayBookActivity.class);
				startActivity(intent);
				//Login.this.finish();
			}
		});
	}
}
