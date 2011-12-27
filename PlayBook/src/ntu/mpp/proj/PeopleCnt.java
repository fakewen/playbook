package ntu.mpp.proj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class PeopleCnt extends Activity {
	/** Called when the activity is first created. */
	private GridView TimeTable;
	private SimpleAdapter listItemAdapter;
	private TextView tablename;
	private int days = 8;
	private String eventID;
	private ArrayList<HashMap<String, Object>> listItem;
	private int[] TextViewID;
	private Calendar cal = Calendar.getInstance();
	private Button Breturn,freetimesend,googleimport;
	private int freeTime[][];
	private ProgressDialog ProgressD;
	Bundle EventData;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		Bundle bData = getIntent().getExtras();
		Log.i("playbook","got eventid="+bData.getString("eventid"));
		Log.i("playbook","got from="+bData.getString("from"));
		Log.i("playbook","got to="+bData.getString("to"));
		Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
		Breturn=(Button)findViewById(R.id.button1);
		freetimesend=(Button)findViewById(R.id.FreeTimeSend);
		googleimport=(Button)findViewById(R.id.GoogleImport);
		freetimesend.setVisibility(View.GONE);
		googleimport.setVisibility(View.GONE);
		tablename = (TextView) findViewById(R.id.TableName);
		tablename.setText("人數統計");

		Breturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent=new Intent();
				//intent.setClass(PeopleCnt.this, proper.class);
				//startActivity(intent);
				PeopleCnt.this.finish();
			}
		});
		
		girdview();
		freetime();

	}

	private void girdview() {
		EventData = this.getIntent().getExtras();
		eventID = EventData.getString("eventid");
		EventData = this.getIntent().getExtras();
		String from_bundle = EventData.getString("from");
		String to_bundle = EventData.getString("to");
		int yearFrom,monthFrom,dayFrom,yearTo,monthTo,dayTo;
		String YMD_F[] = from_bundle.split("/");
		String YMD_T[] = to_bundle.split("/");
		yearFrom = Integer.parseInt(YMD_F[0]);
		yearTo = Integer.parseInt(YMD_T[0]);
		monthFrom = Integer.parseInt(YMD_F[1]);
		monthTo = Integer.parseInt(YMD_T[1]);
		dayFrom = Integer.parseInt(YMD_F[2]);
		dayTo = Integer.parseInt(YMD_T[2]);
		
        //Calendar cal = Calendar.getInstance();
        //cal.getTime();
		cal.set(yearFrom, getRealMonth(monthFrom),dayFrom);
        if(dayTo - dayFrom < 0)
		days = cal.getActualMaximum(Calendar.DAY_OF_MONTH) - dayFrom + 1 + dayTo;
        else if(dayTo - dayFrom == 0)
        	days = 1;
        else
        	days = dayTo - dayFrom +1;
        //Breturn.setText(Integer.toString(dayTo - dayFrom));
		freeTime = new int [4][30];
		for(int i = 0 ; i<3 ; i++){
			for(int j = 0 ; j<3 ; j++){
				freeTime[i][j] = 0;
			}
		}
		ParseQuery query = new ParseQuery("FreeTimeTable");
		query.whereEqualTo("eventID", eventID);
		
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> IDList, ParseException e) {
		        if (e == null) {
		        	for(int i = 0 ; i < IDList.size() ;i++ ){
		        		char Morning[],Noon[],Afternoon[],Night[];
		        		Morning = IDList.get(i).getString("FreeMorning").toCharArray();
		        		Noon = IDList.get(i).getString("FreeNoon").toCharArray();
		        		Afternoon = IDList.get(i).getString("FreeAfternoon").toCharArray();
		        		Night = IDList.get(i).getString("FreeNight").toCharArray();
		        		for(int j = 0 ;j<Morning.length ; j+=2){
		        			freeTime[0][Integer.parseInt(Character.toString(Morning[j]))-1]++;
		        		}
		        		for(int j = 0 ;j<Noon.length ; j+=2){
		        			freeTime[1][Integer.parseInt(Character.toString(Noon[j]))-1]++;
		        		}
		        		for(int j = 0 ;j<Afternoon.length ; j+=2){
		        			freeTime[2][Integer.parseInt(Character.toString(Afternoon[j]))-1]++;
		        		}
		        		for(int j = 0 ;j<Night.length ; j+=2){
		        			freeTime[3][Integer.parseInt(Character.toString(Night[j]))-1]++;
		        		}
		        	}
		        	for(int i= 1 ;i <= 4 ; i++){
			        	for(int j= 1 ;j <= days ; j++){
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("ItemText1", Integer.toString(freeTime[i-1][j-1]));
							listItem.set((days+1)*i+j,map);
			        	
						}
		        	}
		        	TimeTable.setAdapter(listItemAdapter);
		        	listItemAdapter.notifyDataSetChanged();
		        	ProgressD.dismiss();
		        } else {
		        	Breturn.setText("Error");
		        	ProgressD.dismiss();
		        }
		    }
		});
		ProgressD = ProgressDialog.show(this, "", "擷取資料中...", true, false);
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

				HashMap<String, Object> date = (HashMap<String, Object>) TimeTable
						.getItemAtPosition(arg2%(days+1));
				Bundle PeopleData = new Bundle();
				PeopleData.putInt("Index",arg2);
				PeopleData.putString("eventID",eventID);
				PeopleData.putInt("days",days);
				Intent intent=new Intent();
				intent.setClass(PeopleCnt.this, Confirm.class);
				intent.putExtras(PeopleData);
				startActivity(intent);
				
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
					map.put("ItemText1", "早");
					map.put("ItemText2", "上");
					listItem.add(map);
				}
				else if(i== (days+1)*2){
					map.put("ItemText1", "中");
					map.put("ItemText2", "午");
					listItem.add(map);
				}
				else if(i== (days+1)*3){
					map.put("ItemText1", "下");
					map.put("ItemText2", "午");
					listItem.add(map);
				}
				else if(i== (days+1)*4){
					map.put("ItemText1", "晚");
					map.put("ItemText2", "上");
					listItem.add(map);
				}
				

			} else {
				map.put("ItemText1", "0");
				map.put("ItemText2", "");
				listItem.add(map);
			}

		}
		TimeTable.setAdapter(listItemAdapter);
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