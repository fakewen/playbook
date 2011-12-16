package ntu.mpp.proj;
//fakewen is fool
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
public class ElistCBox extends ExpandableListActivity
{
	String me="0922261111";
	private ProgressDialog ProgressD;
    private static final String LOG_TAG = "ElistCBox";
//這是有幾類
    static final String colors[] = {
	  "已確定",
	  "調查中",
	
	};
//這裡類別裡的活動們
	static final String shades[][] = {
// Shades of 已確定
	  {
		"已確定1","by1",
		"已確定2","by2",
		"已確定3","by3"
	  },
// Shades of 調查中
	  {
		"調查中1","by1",
		"調查中2","by2",
		"調查中3","by3"
	  },

    };
	Button bt1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.list);
        Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
        //ProgressD = ProgressDialog.show(this, "", "擷取資料中...", true, false);//
        bt1=(Button)findViewById(R.id.button1);
        //這裡不用findview! expand會自己找 android:list
		SimpleExpandableListAdapter expListAdapter =
			new SimpleExpandableListAdapter(
				this,
				createGroupList(),	// groupData describes the first-level entries
				R.layout.group_row,	// Layout for the first-level entries
				new String[] { "colorName" },	// Key in the groupData maps to display
				new int[] { R.id.childname },		// Data under "colorName" key goes into this TextView
				createChildList(),	// childData describes second-level entries
				R.layout.child_row,	// Layout for second-level entries
				new String[] { "shadeName", "rgb" },	// Keys in childData maps to display
				new int[] { R.id.childname, R.id.rgb }	// Data under the keys above go into these TextViews
			);
		setListAdapter( expListAdapter );
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(ElistCBox.this, PlayBookActivity.class);
				startActivity(intent);
				ElistCBox.this.finish();
			}
		});
    }

    public void  onContentChanged  () {
        super.onContentChanged();
        Log.d( LOG_TAG, "onContentChanged" );
    }

    public boolean onChildClick(
            ExpandableListView parent, 
            View v, 
            int groupPosition,
            int childPosition,
            long id) {
        Log.d( LOG_TAG, "onChildClick: "+childPosition );
        final String event_name_=event_[groupPosition][childPosition];
        Log.i("playbook", "bundle:"+event_name_+" x:"+Integer.toString(groupPosition)+" y:"+Integer.toString(childPosition));
        ElistCBox.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String event_name=event_name_;Log.i("playbook","@run event_name:"+event_name);
				Bundle bData = new Bundle();
				bData.putString("event_name", event_name);
				//bData.putString("ck", "ok");
				Intent intent=new Intent();
				intent.setClass(ElistCBox.this, proper.class);
				intent.putExtras(bData);
				startActivity(intent);
				ElistCBox.this.finish();
			}
		});
        
        /*
        CheckBox cb = (CheckBox)v.findViewById( R.id.check1 );
        if( cb != null )
            cb.toggle();*/
        return false;
    }

    public void  onGroupExpand  (int groupPosition) {
        Log.d( LOG_TAG,"onGroupExpand: "+groupPosition );
    }

/**
  * Creates the group list out of the colors[] array according to
  * the structure required by SimpleExpandableListAdapter. The resulting
  * List contains Maps. Each Map contains one entry with key "colorName" and
  * value of an entry in the colors[] array.
  */
	private List createGroupList() {
	  ArrayList result = new ArrayList();
	  for( int i = 0 ; i < colors.length ; ++i ) {
		HashMap m = new HashMap();
	    m.put( "colorName",colors[i] );
		result.add( m );
	  }
	  return (List)result;
    }

/**
  * Creates the child list out of the shades[] array according to the
  * structure required by SimpleExpandableListAdapter. The resulting List
  * contains one list for each group. Each such second-level group contains
  * Maps. Each such Map contains two keys: "shadeName" is the name of the
  * shade and "rgb" is the RGB value for the shade.
  */
	ArrayList result;// = new ArrayList();
	
	// Second-level lists
		  ArrayList secList1;// = new ArrayList();
		  ArrayList secList2;// = new ArrayList();
		  String event_[][];int cnt1=0;
		  String event0[];int cnt0=0;
  private List createChildList() {
	  event_=new String[2][1000];
	  result = new ArrayList();
	  secList1 = new ArrayList();
	  secList2 = new ArrayList();
	  ProgressD = ProgressDialog.show(this, "", "擷取資料中...", true, false);
	  ParseQuery query = new ParseQuery("invite");
	  query.whereEqualTo("friends", me);//找出自己有被邀請的活動
	  query.findInBackground(new FindCallback(){
		  @Override
			public void done(List<ParseObject> IDList, ParseException e) {
			  if (e == null) {
				  ArrayList secList1_ = new ArrayList();
				  ArrayList secList2_ = new ArrayList();
				  for(int i=0;i<IDList.size();i++){
					  if(IDList.get(i).getString("status").equals("1")){
						  HashMap child = new HashMap();
							child.put( "shadeName", IDList.get(i).getString("event") );Log.i("playbook","1"+IDList.get(i).getString("event"));
						    child.put( "rgb", IDList.get(i).getString("founder") );Log.i("playbook","1"+IDList.get(i).getString("founder"));
							secList1.add( child );
							event_[0][cnt1]=IDList.get(i).getString("event");cnt1++;
					  }
					  else{
						  HashMap child = new HashMap();
							child.put( "shadeName", IDList.get(i).getString("event") );Log.i("playbook","1"+IDList.get(i).getString("event"));
						    child.put( "rgb", IDList.get(i).getString("founder") );Log.i("playbook","1"+IDList.get(i).getString("founder"));
							secList2.add( child );
							event_[1][cnt0]=IDList.get(i).getString("event");cnt0++;
					  }
					  
				  }
			  }
			  ProgressD.dismiss();
		  }
		  
	  });
	  result.add( secList1 );
	  result.add( secList2 );
	  //ProgressD.dismiss();
	
	return result;
  }

}
