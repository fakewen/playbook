/*
package ntu.mpp.proj;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MyMap extends com.google.android.maps.MapActivity{
	
	private MapView map;			//宣告google map物件
	private MapController mapController;	//宣告google map控制物件
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymap);
 
		map = (MapView) findViewById(R.id.map);	//載入google map物件
		mapController = map.getController();	//設定控制的map物件
		setupMap();
	}
 
	private void setupMap() {
		setupMap(120.2126014372952, 22.99724179778664);	//設定地圖預設值
	}
	private void setupMap(double longitude, double latitude) {
		GeoPoint station = new GeoPoint(
					(int)(latitude * 1000000),
					(int)(longitude * 1000000)
		);			//設定地圖座標值:緯度,經度
 
		map.setTraffic(true);				//設定地圖檢示模式
		//.setTraffic(true)：一般地圖
		//.setSatellite(true)：衛星地圖
		//.setStreetView：街景圖
 
		mapController.setZoom(17);			//設定放大倍率1(地球)-21(街景)
		mapController.animateTo(station);	//指定地圖中央點
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