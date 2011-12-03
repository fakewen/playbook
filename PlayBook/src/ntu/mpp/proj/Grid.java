package ntu.mpp.proj;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class Grid extends Activity {
	/*private GridView Calendar;
	private SimpleAdapter listItemAdapter;
	private ArrayList<HashMap<String, Object>> listItem;
	private int[] TextViewID;
	final String day[]={"","早上","中午","下午"};
	final String count[]={"Mon","5","2","4",
			"Tue","6","3","10",
			"Wed","7","2","4",
			"Thu","6","4","3",
			"Fri","5","3","6",
			"Sat","1","4","6",
			"Sun","4","6","3",
			};
	Button bt1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		bt1=(Button)findViewById(R.id.button1);
		
		girdview();
		Calendar.setNumColumns(4);
		for(int i = 0 ; i<4 ; i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemText", day[i]);
            listItem.add(map);  
		}
		for(int i = 0 ; i<count.length ; i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemText", count[i]);
            listItem.add(map);  
		}
		Calendar.setAdapter(listItemAdapter);
		bt1.setOnClickListener(ck_listener);
	}  
	private void girdview(){
		Calendar = (GridView) findViewById(R.id.gridView1);
		TextViewID = new int[] { R.id.ItemText};
		listItem = new ArrayList<HashMap<String, Object>>();  
        listItemAdapter = new SimpleAdapter(this,listItem,R.layout.items,  
                new String[] {"ItemText"},TextViewID);
	}
	OnClickListener ck_listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(Grid.this, proper.class);
			startActivity(intent);
			Grid.this.finish();
		}
	};*/
	/** Called when the activity is first created. */
	private GridView TimeTable;
	private SimpleAdapter listItemAdapter;
	private ArrayList<HashMap<String, Object>> listItem;
	private int[] TextViewID;
	Calendar cal = Calendar.getInstance();
	Button bt1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		bt1=(Button)findViewById(R.id.button1);
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Grid.this, proper.class);
				startActivity(intent);
				Grid.this.finish();
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

				if (map.get("ItemText1").toString().equals("O"))
					map.put("ItemText1", "X");
				else if (map.get("ItemText1").toString().equals("X"))
					map.put("ItemText1", "?");
				else if (map.get("ItemText1").toString().equals("?"))
					map.put("ItemText1", "O");

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
