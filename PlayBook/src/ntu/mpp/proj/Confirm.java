package ntu.mpp.proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
	    eventID = PeopleData.getString("eventID");
	    days = PeopleData.getInt("days");
	    
	    switch(Index/(days+1)){
	    case 1:
	    	queryString = "FreeMorning";
	    	break;
	    case 2:
	    	queryString = "FreeNoon";
	    	break;
	    case 3:
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
								map.put("freeName", IDList.get(i).getString("phone"));
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