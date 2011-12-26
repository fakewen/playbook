package ntu.mpp.proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Confirm extends Activity {
    /** Called when the activity is first created. */
	private Button CReturn,CConfirm;
	private ProgressDialog ProgressD;
	private String queryString = "";
	private int Index,days;
	private String eventID;
	private ListView freeNameList;
	private SimpleAdapter listItemAdapter;
	private ArrayList<HashMap<String, Object>> listItem;
	private int[] TextViewID;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);
        Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
        CReturn = (Button) findViewById(R.id.ConfirmReturn);
        CConfirm = (Button) findViewById(R.id.Confirmed);
        listview();
        CReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Confirm.this.finish();
			}
		});
        CConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Bundle bundle = new Bundle();
//				bundle.putString("eventid", eventID);
//				Intent intent = new Intent();
//				intent.setClass(Confirm.this, ?.class);
//				intent.putExtras(bundle);
//				startActivity(intent);
				ParseQuery query = new ParseQuery("event_list");
				  query.whereEqualTo("founder", global.me);//找出自己的活動!
				  query.findInBackground(new FindCallback(){
					  @Override
						public void done(List<ParseObject> IDList, ParseException e) {
						  }
					  }
				  );
				Confirm.this.finish();
			}
		});
	}
	
	private void listview(){
		freeNameList = (ListView) findViewById(R.id.FreeNameList);
		TextViewID = new int[] { R.id.freeName};
		listItem = new ArrayList<HashMap<String, Object>>();
		listItemAdapter = new SimpleAdapter(this, listItem, R.layout.name,
				new String[] {"freeName"}, TextViewID);


		Bundle PeopleData = this.getIntent().getExtras();
	    Index = PeopleData.getInt("Index");
	    eventID = PeopleData.getString("eventID");Log.i("playbook","really?"+eventID);
	    days = PeopleData.getInt("days");
	    
	    switch(Index/(days+1)){
	    case 1:
	    	queryString = "FreeMorning";
	    	break;
	    case 2:
	    	queryString = "FreeNoon";
	    	break;
	    case 3:
	    	queryString = "FreeAfternoon";
	    	break;
	    case 4:
	    	queryString = "FreeNight";
	    	break;
	    }
	    //CConfirm.setText(Integer.toString(Index));
	    ParseQuery query = new ParseQuery("FreeTimeTable");
		query.whereEqualTo("eventID", eventID);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> IDList, ParseException e) {
				// TODO Auto-generated method stub
				if(e==null){
					for(int i = 0 ; i < IDList.size(); i++){
						char freeDay[] = IDList.get(i).getString(queryString).toCharArray();
						for(int j = 0 ; j < freeDay.length; j+=2){
							if(Integer.parseInt((Character.toString(freeDay[j])))== Index%(days+1) ){
								//CConfirm.setText(IDList.get(i).getString("phone"));
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("freeName", IDList.get(i).getString("name"));
								//map.put("ItemText2", "");
								listItem.add(map);
							}
						}
					}
					freeNameList.setAdapter(listItemAdapter);
					ProgressD.dismiss();
				}else{
					ProgressD.dismiss();
				}
			}
		});
		ProgressD = ProgressDialog.show(this, "", "擷取資料中...", true, false);
		
	}
}
