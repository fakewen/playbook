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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
public class ElistCBoxmaster extends ExpandableListActivity
{
	String me="0922261111";
	private ProgressDialog ProgressD;
	TextView tv1;
    private static final String LOG_TAG = "ElistCBox";
//�o�O���X��
    static final String colors[] = {
	  "�w�T�w",
	  "�լd��",
	
	};
//�o�����O�̪����ʭ�
	static final String shades[][] = {
// Shades of �w�T�w
	  {
		"�w�T�w1","by1",
		"�w�T�w2","by2",
		"�w�T�w3","by3"
	  },
// Shades of �լd��
	  {
		"�լd��1","by1",
		"�լd��2","by2",
		"�լd��3","by3"
	  },

    };
	Button bt1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.list);
        tv1=(TextView)findViewById(R.id.textView1);
        tv1.setText("�ڪ�����");
        
        Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS", "tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
        //ProgressD = ProgressDialog.show(this, "", "�^����Ƥ�...", true, false);//
        bt1=(Button)findViewById(R.id.button1);
        //�o�̤���findview! expand�|�ۤv�� android:list
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
				intent.setClass(ElistCBoxmaster.this, PlayBookActivity.class);
				startActivity(intent);
				ElistCBoxmaster.this.finish();
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
        final String event_id_=eventid_[groupPosition][childPosition];
        Log.i("playbook", "bundle:"+event_name_+" x:"+Integer.toString(groupPosition)+" y:"+Integer.toString(childPosition));
        ElistCBoxmaster.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String event_name=event_name_;Log.i("playbook","@run event_name:"+event_name);
				String event_id=event_id_;
				Bundle bData = new Bundle();
				bData.putString("event_name", event_name);
				bData.putString("event_id", event_id);Log.i("playbook","event_id@list="+event_id);
				//bData.putString("ck", "ok");
				Intent intent=new Intent();
				intent.setClass(ElistCBoxmaster.this, proper.class);
				intent.putExtras(bData);
				startActivity(intent);
				ElistCBoxmaster.this.finish();
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
		  String event_[][];/*�O���Ҧ�����*/int cnt1=0;
		  String event0[];int cnt0=0;
		  String eventid_[][];
		  
  private List createChildList() {
	  event_=new String[2][1000];
	  eventid_=new String[2][1000];
	  result = new ArrayList();
	  secList1 = new ArrayList();
	  secList2 = new ArrayList();
	  ProgressD = ProgressDialog.show(this, "", "�^����Ƥ�...", true, false);
	  ParseQuery query = new ParseQuery("event_list");
	  query.whereEqualTo("founder", global.me);//��X�ۤv������!
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
							event_[0][cnt1]=IDList.get(i).getString("event");
							eventid_[0][cnt1]=IDList.get(i).getString("eventid");
							Log.i("playbook", "id@listget="+IDList.get(i).getString("eventid"));
							cnt1++;
					  }
					  else{
						  HashMap child = new HashMap();
							child.put( "shadeName", IDList.get(i).getString("event") );Log.i("playbook","1"+IDList.get(i).getString("event"));
						    child.put( "rgb", IDList.get(i).getString("founder") );Log.i("playbook","1"+IDList.get(i).getString("founder"));
							secList2.add( child );
							event_[1][cnt0]=IDList.get(i).getString("event");
							eventid_[1][cnt0]=IDList.get(i).getString("eventid");
							Log.i("playbook", "id@listget="+IDList.get(i).getString("eventid"));
							cnt0++;
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