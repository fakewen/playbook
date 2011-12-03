package ntu.mpp.proj;
//push to die
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class ElistCBox extends ExpandableListActivity
{
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
		"已確定2","by2",
		"已確定2","by2",
		"已確定2","by2",
		"已確定2","by2",
		"已確定2","by2",
		"已確定2","by2",
		"已確定2","by2",
		"已確定2","by2",
		"已確定2","by2",
		"已確定3","by3"
	  },
// Shades of 調查中
	  {
		"調查中1","by1",
		"調查中2","by2",
		"調查中2","by2",
		"調查中2","by2",
		"調查中2","by2",
		"調查中2","by2",
		"調查中2","by2",
		"調查中2","by2",
		"調查中2","by2",
		"調查中2","by2",
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
        ElistCBox.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(ElistCBox.this, proper.class);
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
  private List createChildList() {
	ArrayList result = new ArrayList();
	for( int i = 0 ; i < shades.length ; ++i ) {
// Second-level lists
	  ArrayList secList = new ArrayList();
	  for( int n = 0 ; n < shades[i].length ; n += 2 ) {
	    HashMap child = new HashMap();
		child.put( "shadeName", shades[i][n] );
	    child.put( "rgb", shades[i][n+1] );
		secList.add( child );
	  }
	  result.add( secList );
	}
	return result;
  }

}
