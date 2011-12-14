package ntu.mpp.proj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FreeTime extends Activity {
	/** Called when the activity is first created. */
	private GridView TimeTable;
	private SimpleAdapter listItemAdapter;
	private ArrayList<HashMap<String, Object>> listItem;
	private TextView tablename;
	private int[] TextViewID;
	private int days = 4,dataIndex;
	private boolean hadData = false;
	private Calendar cal = Calendar.getInstance();
	private Button Breturn,freetimesend;
	private char queryMorning[],queryNoon[],queryNight[];
	private String freeMorning ="",freeNoon="",freeNight="";
	private String PhoneNumber = "0917723346"; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
		Breturn=(Button)findViewById(R.id.button1);
		tablename = (TextView) findViewById(R.id.TableName);
		tablename.setText("空閒時間表");
		freetimesend=(Button)findViewById(R.id.FreeTimeSend);
		freetimesend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

				for(int i= (days+2) ;i< (days+1)*4 ; i++){
					if (i % days+1 != 0){
						HashMap<String, Object> map = (HashMap<String, Object>) TimeTable
								.getItemAtPosition(i);
						HashMap<String, Object> date = (HashMap<String, Object>) TimeTable
								.getItemAtPosition(i%(days+1));
						if (map.get("ItemText1").toString().equals("O")){
							if(i<(days+1)*2){
								freeMorning+=i%(days+1);/*date.get("ItemText1").toString();*/
								freeMorning+=",";
							}
							else if((days+1)*2 < i && i<(days+1)*3){
								freeNoon+=i%(days+1);/*date.get("ItemText1").toString();*/
								freeNoon+=",";
							}
							else if((days+1)*3 < i &&i<(days+1)*4){

								freeNight+=i%(days+1);/*date.get("ItemText1").toString();*/
								freeNight+=",";
							}
							
						}
					}
				}
				if(hadData){
					ParseQuery query = new ParseQuery("FreeTimeTable");
					query.whereEqualTo("eventID", "778899");
					query.findInBackground(new FindCallback() {
						
						@Override
						public void done(List<ParseObject> IDList, ParseException e) {
							// TODO Auto-generated method stub
							IDList.get(dataIndex).put("FreeMorning", freeMorning);
							IDList.get(dataIndex).put("FreeNoon", freeNoon);
							IDList.get(dataIndex).put("FreeNight", freeNight);
							IDList.get(dataIndex).saveInBackground();
							
						}
					});
				}
				else{		
					ParseObject timeObject = new ParseObject("FreeTimeTable");
					timeObject.put("phone", PhoneNumber);
					timeObject.put("eventID", "778899");
					timeObject.put("FreeMorning", freeMorning);
					timeObject.put("FreeNoon", freeNoon);
					timeObject.put("FreeNight", freeNight);
					//timeObject.put("May_Free", "friends");
	
					timeObject.saveInBackground();
				}
				
			}
		});
		Breturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(FreeTime.this, proper.class);
				startActivity(intent);
				FreeTime.this.finish();
			}
		});
		
		girdview();
		freetime();
		query();
	}
	private void query(){
		ParseQuery query = new ParseQuery("FreeTimeTable");
		query.whereEqualTo("eventID", "778899");
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> IDList, ParseException e) {
		        if (e == null) {
		        	for(int i = 0 ; i < IDList.size() ; i ++){
		        		if(IDList.get(i).getString("phone").equals(PhoneNumber)){
		        			dataIndex = i;
		        			queryMorning = IDList.get(i).getString("FreeMorning").toCharArray();
		        			queryNoon = IDList.get(i).getString("FreeNoon").toCharArray();
		        			queryNight = IDList.get(i).getString("FreeNight").toCharArray();
		        			hadData = true;
		        			//Breturn.setText(Integer.toString(queryMorning.length));
		        			//Breturn.setText(Character.toString(queryNoon[1]));
		        		}
		        	}
		        	if(hadData){
						for(int i= 0 ;i < queryMorning.length ; i+=2){
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("ItemText1", "O");
							listItem.set((days+1)+Integer.parseInt(Character.toString(queryMorning[i])), map);
						}
						for(int i= 0 ;i < queryNoon.length ; i+=2){
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("ItemText1", "O");
							listItem.set((days+1)*2+Integer.parseInt(Character.toString(queryNoon[i])), map);
						}
						for(int i= 0 ;i < queryNight.length ; i+=2){
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("ItemText1", "O");
							listItem.set((days+1)*3+Integer.parseInt(Character.toString(queryNight[i])), map);
						}
						TimeTable.setAdapter(listItemAdapter);
		        	}
					listItemAdapter.notifyDataSetChanged();
		        } else {
		            //Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		}); 
	}
	private void girdview() {

		TimeTable = (GridView) findViewById(R.id.gridView1);
		TimeTable.setNumColumns(days+1);
		TextViewID = new int[] { R.id.ItemText1, R.id.ItemText2 };
		listItem = new ArrayList<HashMap<String, Object>>();
		listItemAdapter = new SimpleAdapter(this, listItem, R.layout.items,
				new String[] { "ItemText1", "ItemText2" }, TextViewID);
		TimeTable.setOnItemClickListener(new GridView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				HashMap<String, Object> map = (HashMap<String, Object>) TimeTable
						.getItemAtPosition(arg2);

				if (map.get("ItemText1").toString().equals("O"))
					map.put("ItemText1", "X");
				else if (map.get("ItemText1").toString().equals("X"))
					map.put("ItemText1", "O");
				listItem.set(arg2, map);
				listItemAdapter.notifyDataSetChanged();

			}
		});
	}
       
	private void freetime() {
		for (int i = 0; i < (days+1)*4; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (i == 0) {
				map.put("ItemText1", "");
				map.put("ItemText2", "");
				listItem.add(map);
			} else if (i > 0 && i < days+1) {
				
				String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
		        Calendar cal = Calendar.getInstance();
		        cal.getTime();
		        int w = (cal.get(Calendar.DAY_OF_WEEK) - 1 +(i-1))%7;
		        if (w < 0)
		            w = 0;
		        int x =(cal.get(Calendar.DATE)+(i-1));
		        x = x%((cal.getActualMaximum(Calendar.DAY_OF_MONTH))+1);
		        if(x==0)
		        	x=1;
				map.put("ItemText1",  (Integer.toString(x)));
				map.put("ItemText2", weekDays[w]);
				listItem.add(map);
			} else if (i % (days+1) == 0 && i != 0) {
				
				if(i== (days+1)){
					map.put("ItemText1", "早");
					map.put("ItemText2", "上");
					listItem.add(map);
				}
				else if(i== (days+1)*2){
					map.put("ItemText1", "下");
					map.put("ItemText2", "午");
					listItem.add(map);
				}
				else if(i== (days+1)*3){
					map.put("ItemText1", "晚");
					map.put("ItemText2", "上");
					listItem.add(map);
				}
				

			} else {
				map.put("ItemText1", "X");
				map.put("ItemText2", "");
				listItem.add(map);
			}

		}
		TimeTable.setAdapter(listItemAdapter);
	}


}
