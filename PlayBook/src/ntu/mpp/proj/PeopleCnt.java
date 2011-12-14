package ntu.mpp.proj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
	private int days = 4;
	private ArrayList<HashMap<String, Object>> listItem;
	private int[] TextViewID;
	Calendar cal = Calendar.getInstance();
	Button Breturn,freetimesend;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
		Breturn=(Button)findViewById(R.id.button1);
		freetimesend=(Button)findViewById(R.id.FreeTimeSend);
		freetimesend.setVisibility(View.GONE);
		tablename = (TextView) findViewById(R.id.TableName);
		tablename.setText("人數統計");
		Breturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(PeopleCnt.this, proper.class);
				startActivity(intent);
				PeopleCnt.this.finish();
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
		ParseQuery query = new ParseQuery("FreeTimeTable");
		query.whereEqualTo("eventID", "778899");
		query.findInBackground(new FindCallback() {
			@Override
			public void done(List<ParseObject> IDList, ParseException e) {
		        if (e == null) {
		        	
		            Log.d("score", "Retrieved " + IDList.get(0).getObjectId() + " scores");
		            Breturn.setText(/*Integer.toString*/( IDList.get(2).getString("FreeMorning")/*.getObjectId()*/));
		           // IDList.get(0).put("FreeMorning", "123");
		            //IDList.get(0).saveInBackground();
		            
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		}); 
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
				// HashMap<String, Object> map = new HashMap<String, Object>();

				//if (map.get("ItemText1").toString().equals("O"))
				//	map.put("ItemText1", "X");
				/*else if (map.get("ItemText1").toString().equals("X"))
					map.put("ItemText1", "?");
				else if (map.get("ItemText1").toString().equals("?"))
					map.put("ItemText1", "O");*/

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
				map.put("ItemText1", "1");
				map.put("ItemText2", "");
				listItem.add(map);
			}

		}
		TimeTable.setAdapter(listItemAdapter);
	}


}