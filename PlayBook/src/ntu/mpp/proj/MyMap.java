/*
package ntu.mpp.proj;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MyMap extends com.google.android.maps.MapActivity{
	
	private MapView map;			//�ŧigoogle map����
	private MapController mapController;	//�ŧigoogle map�����
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymap);
 
		map = (MapView) findViewById(R.id.map);	//���Jgoogle map����
		mapController = map.getController();	//�]�w���map����
		setupMap();
	}
 
	private void setupMap() {
		setupMap(120.2126014372952, 22.99724179778664);	//�]�w�a�Ϲw�]��
	}
	private void setupMap(double longitude, double latitude) {
		GeoPoint station = new GeoPoint(
					(int)(latitude * 1000000),
					(int)(longitude * 1000000)
		);			//�]�w�a�Ϯy�Э�:�n��,�g��
 
		map.setTraffic(true);				//�]�w�a���˥ܼҦ�
		//.setTraffic(true)�G�@��a��
		//.setSatellite(true)�G�ìP�a��
		//.setStreetView�G�󴺹�
 
		mapController.setZoom(17);			//�]�w��j���v1(�a�y)-21(��)
		mapController.animateTo(station);	//���w�a�Ϥ����I
	}
	
	 @Override
	    protected boolean isRouteDisplayed() {
	        // TODO Auto-generated method stub
	        return false;
	    }
}
*/
package ntu.mpp.proj;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;


import android.os.Bundle;

public class MyMap extends MapActivity
{
	private MapView mapView;
	private MapController mapController;
	private MyLocationOverlay mylayer;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymap);
		findControl();
	}

	private void findControl()
	{
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
		mapController.setZoom(16);
		
		//�w���I
		List<Overlay> overlays = mapView.getOverlays();
		mylayer = new MyLocationOverlay(this, mapView);
		//���ù�L
		mylayer.enableCompass();
		//�Ұʧ�s(�p�G���Ц��ܰʷ|��۲���)
		mylayer.enableMyLocation();
		mylayer.runOnFirstFix(new Runnable()
		{

			public void run()
			{
				mapController.animateTo(mylayer.getMyLocation());
			}
		});
		overlays.add(mylayer);
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}
}