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
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
    private static String calanderURL = "";  
    private static String calanderEventURL = "";  
    private static String calanderRemiderURL = "";  

    static{  
        if(Integer.parseInt(Build.VERSION.SDK) >= 8){  
            calanderURL = "content://com.android.calendar/calendars";  
            calanderEventURL = "content://com.android.calendar/events";  
            calanderRemiderURL = "content://com.android.calendar/reminders";  
  
        }else{  
            calanderURL = "content://calendar/calendars";  
            calanderEventURL = "content://calendar/events";  
            calanderRemiderURL = "content://calendar/reminders";          
        }  
    } 
	/** Called when the activity is first created. */
	private GridView TimeTable;
	private SimpleAdapter listItemAdapter;
	private ArrayList<HashMap<String, Object>> listItem;
	private TextView tablename;
	private int[] TextViewID;
	private int days = 4,dataIndex;
	private String eventID;
	private boolean hadData = false;
	private Calendar cal = Calendar.getInstance();
	private Button Breturn,freetimesend,googleimport;
	private char queryMorning[],queryNoon[],queryNight[],queryAfternoon[];
	private String freeMorning ="",freeAfternoon="",freeNoon="",freeNight="";
	private String PhoneNumber = global.me; 
	private ProgressDialog ProgressD;
	private int yearFrom,monthFrom,dayFrom,yearTo,monthTo,dayTo,dayMax;
	Bundle EventData;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
		Breturn=(Button)findViewById(R.id.button1);
		tablename = (TextView) findViewById(R.id.TableName);
		tablename.setText("空閒時間表");
		freetimesend=(Button)findViewById(R.id.FreeTimeSend);
		googleimport=(Button)findViewById(R.id.GoogleImport);
		girdview();
		freetime();
		query();
		
		googleimport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				calendarImprot();
			}
		});
		freetimesend.setOnClickListener(new OnClickListener() {
		@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			//Breturn.setText(Integer.toString(days));
				for(int i= (days+2) ;i< (days+1)*5 ; i++){
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
							else if((days+1)*3 < i && i<(days+1)*4){
								freeAfternoon+=i%(days+1);/*date.get("ItemText1").toString();*/
								freeAfternoon+=",";
							}
							else if((days+1)*4 < i &&i<(days+1)*5){

								freeNight+=i%(days+1);/*date.get("ItemText1").toString();*/
								freeNight+=",";
							}
							
						}
					}
				}
				if(hadData){
					ParseQuery query = new ParseQuery("FreeTimeTable");
					query.whereEqualTo("eventID", eventID);
					
					query.findInBackground(new FindCallback() {
						
						@Override
						public void done(List<ParseObject> IDList, ParseException e) {
							// TODO Auto-generated method stub
							IDList.get(dataIndex).put("FreeMorning", freeMorning);
							IDList.get(dataIndex).put("FreeAfternoon", freeAfternoon);
							IDList.get(dataIndex).put("FreeNoon", freeNoon);
							IDList.get(dataIndex).put("FreeNight", freeNight);
							IDList.get(dataIndex).saveInBackground();
							
						}
					});
				}
				else{		
					ParseObject timeObject = new ParseObject("FreeTimeTable");
					timeObject.put("phone", PhoneNumber);
					timeObject.put("eventID", eventID);
					timeObject.put("FreeMorning", freeMorning);
					timeObject.put("FreeAfternoon", freeAfternoon);
					timeObject.put("FreeNoon", freeNoon);
					timeObject.put("FreeNight", freeNight);
					timeObject.saveInBackground();
				}
				FreeTime.this.finish();
				
			}
		});
		Breturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FreeTime.this.finish();
			}
		});
	}
	private void calendarImprot(){
		boolean NoTime[][] = new boolean[4][days];
		for(int i = 0 ; i < 4 ; i ++)
			for(int j = 0 ; j < days ; j++)
				NoTime[i][j] = false;
		Cursor eventCursor = getContentResolver().query(Uri.parse(calanderEventURL), null, null, null, null);
		eventCursor.moveToLast();
		/*int Noindex = 0;
        if(CalEnd.get(Calendar.DATE) - dayFrom < 0)
        	Noindex = dayMax - dayFrom  + CalEnd.get(Calendar.DATE);
        else if(dayTo - dayFrom == 0)
        	Noindex = 1;
        else*/
		do{
			Calendar CalStart = Calendar.getInstance();
			Calendar CalEnd = Calendar.getInstance();
			CalStart.setTimeInMillis(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex("dtstart"))));
			CalEnd.setTimeInMillis(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex("dtend"))));
			if(CalStart.get(Calendar.YEAR) == yearFrom ||CalEnd.get(Calendar.YEAR) == yearTo)
			if(getIntMonth(CalStart) == monthFrom || getIntMonth(CalEnd) == monthTo)
			if(monthFrom == monthTo){
			if(CalStart.get(Calendar.DATE) >= dayFrom && CalEnd.get(Calendar.DATE) <= dayTo)
			if(CalStart.get(Calendar.DATE) - CalEnd.get(Calendar.DATE) ==0){ 
			        int Noindex = CalEnd.get(Calendar.DATE) - dayFrom;
					if(CalStart.get(Calendar.HOUR_OF_DAY)<=11 && CalStart.get(Calendar.HOUR_OF_DAY) > 6){
						if(CalEnd.get(Calendar.HOUR_OF_DAY)<=11 && CalEnd.get(Calendar.HOUR_OF_DAY) > 6){
							NoTime[0][Noindex] = true;
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=14 && CalEnd.get(Calendar.HOUR_OF_DAY) > 11){
							NoTime[0][Noindex] = true;
							NoTime[1][Noindex] = true;
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=18 && CalEnd.get(Calendar.HOUR_OF_DAY) > 14){
							NoTime[0][Noindex] = true;
							NoTime[1][Noindex] = true;
							NoTime[2][Noindex] = true;
							
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=24 && CalEnd.get(Calendar.HOUR_OF_DAY) > 18){
							NoTime[0][Noindex] = true;
							NoTime[1][Noindex] = true;
							NoTime[2][Noindex] = true;
							NoTime[3][Noindex] = true;
						}
	
					}
					else if(CalStart.get(Calendar.HOUR_OF_DAY)<=14 && CalStart.get(Calendar.HOUR_OF_DAY) > 11){
						if(CalEnd.get(Calendar.HOUR_OF_DAY)<=14 && CalEnd.get(Calendar.HOUR_OF_DAY) > 11){
							NoTime[1][Noindex] = true;
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=18 && CalEnd.get(Calendar.HOUR_OF_DAY) > 14){
							NoTime[1][Noindex] = true;
							NoTime[2][Noindex] = true;
							
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=24 && CalEnd.get(Calendar.HOUR_OF_DAY) > 18){
							NoTime[1][Noindex] = true;
							NoTime[2][Noindex] = true;
							NoTime[3][Noindex] = true;
						}
						
					}
					else if(CalStart.get(Calendar.HOUR_OF_DAY)<=18 && CalStart.get(Calendar.HOUR_OF_DAY) > 14){
						if(CalEnd.get(Calendar.HOUR_OF_DAY)<=18 && CalEnd.get(Calendar.HOUR_OF_DAY) > 14){
							NoTime[2][Noindex] = true;
							
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=24 && CalEnd.get(Calendar.HOUR_OF_DAY) > 18){
							NoTime[2][Noindex] = true;
							NoTime[3][Noindex] = true;
						}
						
					}
					else if(CalStart.get(Calendar.HOUR_OF_DAY)<=24 && CalStart.get(Calendar.HOUR_OF_DAY) > 18){
						if(CalEnd.get(Calendar.HOUR_OF_DAY)<=24 && CalEnd.get(Calendar.HOUR_OF_DAY) > 18){
							NoTime[3][Noindex] = true;
						}
					}
				}
				
			}
			else{
				if(CalStart.get(Calendar.DATE) - CalEnd.get(Calendar.DATE) ==0)
				if(CalStart.get(Calendar.DATE) >= dayFrom || CalEnd.get(Calendar.DATE) <= dayTo){
			        int Noindex = dayMax - dayFrom  + CalEnd.get(Calendar.DATE);
					if(CalStart.get(Calendar.HOUR_OF_DAY)<=11 && CalStart.get(Calendar.HOUR_OF_DAY) > 6){
						if(CalEnd.get(Calendar.HOUR_OF_DAY)<=11 && CalEnd.get(Calendar.HOUR_OF_DAY) > 6){
							NoTime[0][Noindex] = true;
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=14 && CalEnd.get(Calendar.HOUR_OF_DAY) > 11){
							NoTime[0][Noindex] = true;
							NoTime[1][Noindex] = true;
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=18 && CalEnd.get(Calendar.HOUR_OF_DAY) > 14){
							NoTime[0][Noindex] = true;
							NoTime[1][Noindex] = true;
							NoTime[2][Noindex] = true;
							
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=24 && CalEnd.get(Calendar.HOUR_OF_DAY) > 18){
							NoTime[0][Noindex] = true;
							NoTime[1][Noindex] = true;
							NoTime[2][Noindex] = true;
							NoTime[3][Noindex] = true;
						}
	
					}
					else if(CalStart.get(Calendar.HOUR_OF_DAY)<=14 && CalStart.get(Calendar.HOUR_OF_DAY) > 11){
						if(CalEnd.get(Calendar.HOUR_OF_DAY)<=14 && CalEnd.get(Calendar.HOUR_OF_DAY) > 11){
							NoTime[1][Noindex] = true;
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=18 && CalEnd.get(Calendar.HOUR_OF_DAY) > 14){
							NoTime[1][Noindex] = true;
							NoTime[2][Noindex] = true;
							
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=24 && CalEnd.get(Calendar.HOUR_OF_DAY) > 18){
							NoTime[1][Noindex] = true;
							NoTime[2][Noindex] = true;
							NoTime[3][Noindex] = true;
						}
						
					}
					else if(CalStart.get(Calendar.HOUR_OF_DAY)<=18 && CalStart.get(Calendar.HOUR_OF_DAY) > 14){
						if(CalEnd.get(Calendar.HOUR_OF_DAY)<=18 && CalEnd.get(Calendar.HOUR_OF_DAY) > 14){
							NoTime[2][Noindex] = true;
							
						}
						else if(CalEnd.get(Calendar.HOUR_OF_DAY)<=24 && CalEnd.get(Calendar.HOUR_OF_DAY) > 18){
							NoTime[2][Noindex] = true;
							NoTime[3][Noindex] = true;
						}
						
					}
					else if(CalStart.get(Calendar.HOUR_OF_DAY)<=24 && CalStart.get(Calendar.HOUR_OF_DAY) > 18){
						if(CalEnd.get(Calendar.HOUR_OF_DAY)<=24 && CalEnd.get(Calendar.HOUR_OF_DAY) > 18){
							NoTime[3][Noindex] = true;
						}
					}
				}
			}
				
		}
		while(eventCursor.moveToPrevious());
		for(int i = 0 ; i < 4 ; i ++){
			for(int j = 0 ; j < days ; j++){
				if(NoTime[i][j] == false){
					HashMap<String, Object> map = (HashMap<String, Object>) TimeTable
							.getItemAtPosition((days+1)*(i+1)+(j+1));
					if (map.get("ItemText1").toString().equals("X")){
						HashMap<String, Object> map2 = new HashMap<String, Object>();
						map2.put("ItemText1", "O");
						listItem.set((days+1)*(i+1)+(j+1), map2);	
					}
				}
			}
		}
		TimeTable.setAdapter(listItemAdapter);
		listItemAdapter.notifyDataSetChanged();
		
	}
	private void query(){
		ParseQuery query = new ParseQuery("FreeTimeTable");
		query.whereEqualTo("eventID", eventID);
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> IDList, ParseException e) {
		        if (e == null) {
		        	for(int i = 0 ; i < IDList.size() ; i ++){
		        		if(IDList.get(i).getString("phone").equals(PhoneNumber)){
		        			dataIndex = i;
		        			queryMorning = IDList.get(i).getString("FreeMorning").toCharArray();
		        			queryNoon = IDList.get(i).getString("FreeNoon").toCharArray();
		        			queryAfternoon = IDList.get(i).getString("FreeAfternoon").toCharArray();
		        			queryNight = IDList.get(i).getString("FreeNight").toCharArray();
		        			hadData = true;
		        		}
		        	}
		        	if(hadData){
		        		freetimesend.setText("  更新  ");
						for(int i= 0 ;i < queryMorning.length ; i+=2){
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("ItemText1", "O");
							listItem.set((days+1)+Integer.parseInt(Character.toString(queryMorning[i])), map);
						}
						for(int i= 0 ;i < queryAfternoon.length ; i+=2){
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("ItemText1", "O");
							listItem.set((days+1)*2+Integer.parseInt(Character.toString(queryAfternoon[i])), map);
						}
						for(int i= 0 ;i < queryNoon.length ; i+=2){
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("ItemText1", "O");
							listItem.set((days+1)*3+Integer.parseInt(Character.toString(queryNoon[i])), map);
						}
						for(int i= 0 ;i < queryNight.length ; i+=2){
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("ItemText1", "O");
							listItem.set((days+1)*4+Integer.parseInt(Character.toString(queryNight[i])), map);
						}
						TimeTable.setAdapter(listItemAdapter);
		        	}
					listItemAdapter.notifyDataSetChanged();
					ProgressD.dismiss();
		        } else {
		        	ProgressD.dismiss();
		        }
		    }
		});
		ProgressD = ProgressDialog.show(this, "", "擷取資料中...", true, false);
	}
	private void girdview() {
		EventData = this.getIntent().getExtras();
		eventID = EventData.getString("eventid");
		String from_bundle = EventData.getString("from");
		String to_bundle = EventData.getString("to");
		
		String YMD_F[] = from_bundle.split("/");
		String YMD_T[] = to_bundle.split("/");
		yearFrom = Integer.parseInt(YMD_F[0]);
		yearTo = Integer.parseInt(YMD_T[0]);
		monthFrom = Integer.parseInt(YMD_F[1]);
		monthTo = Integer.parseInt(YMD_T[1]);
		dayFrom = Integer.parseInt(YMD_F[2]);
		dayTo = Integer.parseInt(YMD_T[2]);
		
       // Calendar cal = Calendar.getInstance();
        //cal.getTime();
        cal.set(yearFrom, getRealMonth(monthFrom),dayFrom);
        dayMax = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(dayTo - dayFrom < 0)
		days = cal.getActualMaximum(Calendar.DAY_OF_MONTH) - dayFrom + 1 + dayTo;
        else if(dayTo - dayFrom == 0)
        	days = 1;
        else
        	days = dayTo - dayFrom +1;
		ParseQuery query = new ParseQuery("FreeTimeTable");
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
		for (int i = 0; i < (days+1)*5; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (i == 0) {
				map.put("ItemText1", "");
				map.put("ItemText2", "");
				listItem.add(map);
			} else if (i > 0 && i < days+1) {
				
				String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
		        int w = (getChineseDayOfWeek(cal) + (i-1))%7;
		        if (w < 0)
		            w = 0;
		        int x =(cal.get(Calendar.DATE)+(i-1));
		        if(x > (cal.getActualMaximum(Calendar.DAY_OF_MONTH)))
		        	x = x%(cal.getActualMaximum(Calendar.DAY_OF_MONTH));

				map.put("ItemText1",  (Integer.toString(x)));
				map.put("ItemText2", weekDays[w]);
				listItem.add(map);
			} else if (i % (days+1) == 0 && i != 0) {
				
				if(i== (days+1)){
					map.put("ItemText1", "早上");
					map.put("ItemText2", "6~11");
					listItem.add(map);
				}
				else if(i== (days+1)*2){
					map.put("ItemText1", "中 午");
					map.put("ItemText2", "11~14");
					listItem.add(map);
				}
				else if(i== (days+1)*3){
					map.put("ItemText1", "下午");
					map.put("ItemText2", "14~18");
					listItem.add(map);
				}
				else if(i== (days+1)*4){
					map.put("ItemText1", "晚上");
					map.put("ItemText2", "18~24");
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
	public static int getIntMonth(Calendar rightNow) {
        int intMonth = 0;
        
        switch(rightNow.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
            	intMonth = 1;
                break;
            case Calendar.FEBRUARY:
            	intMonth = 2;
                break;
            case Calendar.MARCH:
            	intMonth = 3;
                break;
            case Calendar.APRIL:
            	intMonth = 4;
                break;
            case Calendar.MAY:
            	intMonth = 5;
                break;
            case Calendar.JUNE:
            	intMonth = 6;
                break;
            case Calendar.JULY:
            	intMonth = 7;
                break;
            case Calendar.AUGUST:
            	intMonth = 8;
                break;
            case Calendar.SEPTEMBER:
            	intMonth = 9;
                break;
            case Calendar.OCTOBER:
            	intMonth = 10;
                break;
            case Calendar.NOVEMBER:
            	intMonth = 11;
                break;
            case Calendar.DECEMBER:
            	intMonth = 12;
                break;                
        }
        
        return intMonth;
    }
	public static int getRealMonth(int month) {
        int intMonth = 0;
        
        switch(month) {
            case 1:
            	intMonth = Calendar.JANUARY;
                break;
            case 2:
            	intMonth = Calendar.FEBRUARY;
                break;
            case 3:
            	intMonth = Calendar.MARCH;
                break;
            case 4:
            	intMonth = Calendar.APRIL;
                break;
            case 5:
            	intMonth = Calendar.MAY;
                break;
            case 6:
            	intMonth = Calendar.JUNE;
                break;
            case 7:
            	intMonth = Calendar.JULY;
                break;
            case 8:
            	intMonth = Calendar.AUGUST;
                break;
            case 9:
            	intMonth = Calendar.SEPTEMBER;
                break;
            case 10:
            	intMonth = Calendar.OCTOBER;
                break;
            case 11:
            	intMonth = Calendar.NOVEMBER;
                break;
            case 12:
            	intMonth = Calendar.DECEMBER;
                break;                
        }
        
        return intMonth;
    }
	public static int getChineseDayOfWeek(Calendar rightNow) {
		int chineseDayOfWeek = 0;

		switch (rightNow.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			chineseDayOfWeek = 0;
			break;
		case Calendar.MONDAY:
			chineseDayOfWeek = 1;
			break;
		case Calendar.TUESDAY:
			chineseDayOfWeek = 2;
			break;
		case Calendar.WEDNESDAY:
			chineseDayOfWeek = 3;
			break;
		case Calendar.THURSDAY:
			chineseDayOfWeek = 4;
			break;
		case Calendar.FRIDAY:
			chineseDayOfWeek = 5;
			break;
		case Calendar.SATURDAY:
			chineseDayOfWeek = 6;
			break;
		}

		return chineseDayOfWeek;
	}

}
