package ntu.mpp.proj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	Calendar cal = Calendar.getInstance();
	Button bt1,freetimesend;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
		bt1=(Button)findViewById(R.id.button1);
		tablename = (TextView) findViewById(R.id.TableName);
		tablename.setText("空閒時間表");
		freetimesend=(Button)findViewById(R.id.FreeTimeSend);
		freetimesend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String freeMorning ="",freeNoon="",freeNight="";
				//freeMorning+=",2";
				for(int i= 9 ;i<32 ; i++){
					if (i % 8 != 0){
						HashMap<String, Object> map = (HashMap<String, Object>) TimeTable
								.getItemAtPosition(i);
						HashMap<String, Object> date = (HashMap<String, Object>) TimeTable
								.getItemAtPosition(i%8);
						if (map.get("ItemText1").toString().equals("O")){
							if(i<16){
								//freetimesend.setText(date.get("ItemText1").toString());
								freeMorning+=date.get("ItemText1").toString();
								freeMorning+=",";
							}
							else if(16 < i && i<24){
								//freetimesend.setText(date.get("ItemText1").toString());
								//date.get("ItemText1").toString();
								freeNoon+=date.get("ItemText1").toString();
								freeNoon+=",";
							}
							else if(24 < i &&i<32){
								//freetimesend.setText(date.get("ItemText1").toString());
								//date.get("ItemText1").toString();
								freeNight+=date.get("ItemText1").toString();
								freeNight+=",";
							}
							
						}

						/*else if (map.get("ItemText1").toString().equals("X")){
							
						}

						/*else if (map.get("ItemText1").toString().equals("?")){
							
						}*/


					}
				}
				//HashMap<String, Object> date = (HashMap<String, Object>) TimeTable
						//.getItemAtPosition(1);
				//freetimesend.setText(freeMorning);
				
				ParseObject timeObject = new ParseObject("FreeTimeTable");
		        //testObject.put("state", "@submit button!");
				timeObject.put("phone", "0900123456");
				timeObject.put("eventID", "778899");
				timeObject.put("FreeMorning", freeMorning);
				timeObject.put("FreeNoon", freeNoon);
				timeObject.put("FreeNight", freeNight);
				//timeObject.put("May_Free", "friends");

				timeObject.saveInBackground();
				
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			
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
		/*
		 * for (int i = 0; i < 32; i++) { HashMap<String, Object> map = new
		 * HashMap<String, Object>(); map.put("ItemText1", "");
		 * map.put("ItemText2", ""); listItem.add(map); }
		 * Calendar.setAdapter(listItemAdapter);
		 */
	}

	private void girdview() {
		TimeTable = (GridView) findViewById(R.id.gridView1);
		TimeTable.setNumColumns(8);
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
				// HashMap<String, Object> map = new HashMap<String, Object>();
				//freetimesend.setText(Integer.toString(arg2));
				if (map.get("ItemText1").toString().equals("O"))
					map.put("ItemText1", "X");
				else if (map.get("ItemText1").toString().equals("X"))
					map.put("ItemText1", "O");
				//else if (map.get("ItemText1").toString().equals("?"))
					//map.put("ItemText1", "O");
       
				// map.put("ItemText1", "O");
				// map.put("ItemText2", "X");
				listItem.set(arg2, map);
				// setTitle(Integer.toString(arg2)/* "選取了"+ map.get("ItemText")
				// */);
				listItemAdapter.notifyDataSetChanged();

			}
		});
	}
       
	private void freetime() {
		for (int i = 0; i < 32; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (i == 0) {
				map.put("ItemText1", "");
				map.put("ItemText2", "");
				listItem.add(map);
			} else if (i > 0 && i < 8) {
				
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
			} else if (i % 8 == 0 && i != 0) {
				switch (i) {
				case 8:
					map.put("ItemText1", "早");
					map.put("ItemText2", "上");
					listItem.add(map);
					break;
				case 16:
					map.put("ItemText1", "中");
					map.put("ItemText2", "午");
					listItem.add(map);
					break;
				case 24:
					map.put("ItemText1", "晚");
					map.put("ItemText2", "上");
					listItem.add(map);
					break;
				}

			} else {
				map.put("ItemText1", "O");
				map.put("ItemText2", "");
				listItem.add(map);
			}

		}
		TimeTable.setAdapter(listItemAdapter);
	}


}
