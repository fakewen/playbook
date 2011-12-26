
package ntu.mpp.proj;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.parse.Parse;

public class MyMap extends MapActivity
{
	private MapView mapView;
	private MapController mapController;
	private MyLocationOverlay mylayer;
	boolean flag_draw=true;
	//MapView mapView; 
    MapController mc;
    GeoPoint p,p_tmp;
    Button bt1,bt2;
	/*
	@Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) 
    {   
        //---when user lifts his finger---
        if (event.getAction() == 1) {                
            GeoPoint p = mapView.getProjection().fromPixels(
                (int) event.getX(),
                (int) event.getY());
                Toast.makeText(getBaseContext(), 
                    p.getLatitudeE6() / 1E6 + "," + 
                    p.getLongitudeE6() /1E6 , 
                    Toast.LENGTH_SHORT).show();
        }                            
        return false;
    } */ 
	class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, 
        boolean shadow, long when) 
        {
            if(global.flag_mark){
        	super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(global.p_, screenPts);//這裡要給位置
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(
                getResources(), R.drawable.darkreddot);            
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
            
            }return true;
        }
        @Override
        public boolean onTouchEvent(MotionEvent event, MapView mapView) 
        {   
            //---when user lifts his finger---
            if (event.getAction() == 1) {                
                GeoPoint p = mapView.getProjection().fromPixels(
                    (int) event.getX(),
                    (int) event.getY());
                	p_tmp=p;
                    Toast.makeText(getBaseContext(), 
                        p.getLatitudeE6() / 1E6 + "," + 
                        p.getLongitudeE6() /1E6 , 
                        Toast.LENGTH_SHORT).show();
            }                            
            return false;
        }      
    } 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymap);
		Parse.initialize(this, "97PXpE7X3RaVJJ8saoXqJ4k3MBlMAVaFgtarAXKS",
				"tFXZlErWqrJ2rRY8IOn2N0riC1vURsSL7ea3VH9a");
		findControl();
		bt1=(Button)findViewById(R.id.button1);
		bt2=(Button)findViewById(R.id.button2);
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(MyMap.this, invite.class);
//				startActivity(intent);
				MyMap.this.finish();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				global.p_=p_tmp;
				global.flag_mark=true;
			}
		});
	}

	private void findControl()
	{
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
		mapController.setZoom(16);
		/*
		//定位點
		List<Overlay> overlays = mapView.getOverlays();
		mylayer = new MyLocationOverlay(this, mapView);
		//顯示羅盤
		mylayer.enableCompass();
		//啟動更新(如果坐標有變動會跟著移動)
		mylayer.enableMyLocation();
		mylayer.runOnFirstFix(new Runnable()
		{

			public void run()
			{
				mapController.animateTo(mylayer.getMyLocation());
				p=mylayer.getMyLocation();
				if(flag_draw){
					MapOverlay mapOverlay = new MapOverlay();
					List<Overlay> listOfOverlays = mapView.getOverlays();
					listOfOverlays.clear();
					listOfOverlays.add(mapOverlay);
				}
			}
		});
		*/
		if(flag_draw){
			MapOverlay mapOverlay = new MapOverlay();
			List<Overlay> listOfOverlays = mapView.getOverlays();
			listOfOverlays.clear();
			listOfOverlays.add(mapOverlay);
		}
		//mylayer.draw(canvas, mapView, shadow)
		//overlays.add(mylayer);
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}
}