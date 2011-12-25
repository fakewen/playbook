package ntu.mpp.proj;

import java.util.ArrayList;
import java.util.Arrays;

import android.R.string;
import android.app.ListActivity;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FriendList extends ListActivity {

	
	private ListView mainListView = null;
	

	private ArrayList<string> selectedItems = new ArrayList<string>();

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list);

		// 資料陣列
		//String[] vData = { "足球", "棒球", "籃球" };
		Bundle bData = getIntent().getExtras();
		
		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						"儲存受邀好友", Toast.LENGTH_SHORT).show();

				SaveSelections();
				//這裡要把所有preference丟回去invite.java
			}
		});

		Button btnClear = (Button) findViewById(R.id.btnClear);
		btnClear.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Toast.makeText(getApplicationContext(),
						"清除所有選擇", Toast.LENGTH_SHORT).show();

				ClearSelections();
			}
		});

		// Prepare an ArrayList of todo items
		// ArrayList<string> listTODO = PrepareListFromXml();

		this.mainListView = getListView();

		//mainListView.setCacheColorHint(0);

		// Bind the data with the list
		// lv_arr = (String[]) listTODO.toArray(new String[0]);
		mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mainListView.setAdapter(new ArrayAdapter(FriendList.this,
				android.R.layout.simple_list_item_multiple_choice, bData.getStringArrayList("friend_list")));
		
		//new ArrayAdapter<string>(FriendList.this, android.R.layout.simple_list_item_multiple_choice, vData);

		mainListView.setItemsCanFocus(false);
		//mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

//		LoadSelections();

//		// 建立 ListView 物件
//		ListView lv = this.getListView();
//
//		// 設定 ListView 選擇的方式 :
//		// 單選 : ListView.CHOICE_MODE_SINGLE
//		// 多選 : ListView.CHOICE_MODE_MULTIPLE
//		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//
//		// ListView 的選項來源由 陣列 提供
//		// RadioButton Layout 樣式 :
//		// android.R.layout.simple_list_item_single_choice
//		// CheckBox Layout 樣式 :
//		// android.R.layout.simple_list_item_multiple_choice
//		this.setListAdapter(new ArrayAdapter(this,
//				android.R.layout.simple_list_item_multiple_choice, vData));
	}
	
	@Override
	protected void onPause() {
		// always handle the onPause to make sure selections are saved if user clicks back button
		SaveSelections();

		super.onPause();
	}
	
	private void SaveSelections() {

		// save the selections in the shared preference in private mode for the user

		SharedPreferences settingsActivity = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = settingsActivity.edit();

		String savedItems = getSavedItems();
		String savedIndex = getSavedIndex();
		Log.i("selected", savedIndex);

		prefEditor.putString("select_items", savedItems);
		prefEditor.putString("select_index", savedIndex);

		prefEditor.commit();
	}
	private String getSavedIndex() {
		String savedItems = "";

		int count = this.mainListView.getAdapter().getCount();

		for (int i = 0; i < count; i++) {

			if (this.mainListView.isItemChecked(i)) {
				if (savedItems.length() > 0) {
					savedItems += "," + i;
				} else {
					savedItems += i;
				}
			}

		}
		return savedItems;
	}
	
	private String getSavedItems() {
		String savedItems = "";

		int count = this.mainListView.getAdapter().getCount();

		for (int i = 0; i < count; i++) {

			if (this.mainListView.isItemChecked(i)) {
				if (savedItems.length() > 0) {
					savedItems += "," + this.mainListView.getItemAtPosition(i);
				} else {
					savedItems += this.mainListView.getItemAtPosition(i);
				}
			}

		}
		return savedItems;
	}
	
	private void ClearSelections() {

		// user has clicked clear button so uncheck all the items

		int count = this.mainListView.getAdapter().getCount();

		for (int i = 0; i < count; i++) {
			this.mainListView.setItemChecked(i, false);
		}

		// also clear the saved selections
		SaveSelections();

	}
	
	/*private void LoadSelections() {
		// if the selections were previously saved load them

		SharedPreferences settingsActivity = getPreferences(MODE_PRIVATE);

		if (settingsActivity.contains(SETTING_TODOLIST)) {
			String savedItems = settingsActivity
					.getString(SETTING_TODOLIST, "");

			this.selectedItems.addAll(Arrays.asList(savedItems.split(",")));
			int count = this.mainListView.getAdapter().getCount();

			for (int i = 0; i < count; i++) {
				String currentItem = (String) this.mainListView.getAdapter()
						.getItem(i);
				if (this.selectedItems.contains(currentItem)) {
					this.mainListView.setItemChecked(i, true);
				}

			}

		}
	}*/
	
}
