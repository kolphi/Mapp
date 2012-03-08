package at.fhooe.mc.linzguide.android.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import at.fhooe.mc.linzguide.android.R;
import at.fhooe.mc.linzguide.android.actionbar.ActionBarMapActivity;
import at.fhooe.mc.linzguide.android.db.DBGeopoints;
import at.fhooe.mc.linzguide.android.util.DataGeopoint;
import at.fhooe.mc.linzguide.android.util.RawDataProvider;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Map extends ActionBarMapActivity {

	MapView mMapView;
	MapController mMapController;

	GeoPoint mGeoPoint = new GeoPoint(48307747, 14286175);
	public LocationManager mLocationManager;
	private MyLocationOverlay mMyOverlay;
	
	DBGeopoints dbGeopoints;
	
	ItemizedOverlay itemizedoverlay;
	List<Overlay> mapOverlays;
	
	int poiType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setSatellite(false);
		
		mMapController = mMapView.getController();
		mMapController.setCenter(mGeoPoint);
		mMapController.setZoom(15);
		
		dbGeopoints = new DBGeopoints(getApplicationContext());

		mMyOverlay = new MyLocationOverlay(this, mMapView) {

			@Override
			public synchronized void onLocationChanged(Location location) {
				super.onLocationChanged(location);
				final GeoPoint meinePosition = new GeoPoint(
						(int) (location.getLatitude() * 1E6),
						(int) (location.getLongitude() * 1E6));
				// mMapController.animateTo(meinePosition);
			}

		};
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		mapOverlays = mMapView.getOverlays();
		
		
		which = new SparseBooleanArray();

		if(getIntent().hasExtra("points")){
			poiType = getIntent().getIntExtra("poiType", 0);
			ArrayList<DataGeopoint> items = (ArrayList<DataGeopoint>) getIntent().getSerializableExtra("points");
			
			itemizedoverlay = new ItemizedOverlay(getResources().getDrawable(RawDataProvider.icons[poiType]));
			
			which.append(poiType, true);
			
			GeoPoint myGeo;
			OverlayItem myItem;
			
			for (int i = 0; i < items.size(); i++) {
				myGeo = new GeoPoint(items.get(i).lat, items.get(i).lon);
				myItem = new OverlayItem(myGeo, items.get(i).name, items.get(i).address);
				itemizedoverlay.addOverlay(myItem);
			}
			mapOverlays.add(itemizedoverlay);
		}
		
		if(getIntent().hasExtra("point")){
			poiType = getIntent().getIntExtra("poiType", 0);
			DataGeopoint items = (DataGeopoint) getIntent().getSerializableExtra("point");
			
			itemizedoverlay = new ItemizedOverlay(getResources().getDrawable(RawDataProvider.icons[poiType]));
			
			GeoPoint myGeo;
			OverlayItem myItem;
			
			myGeo = new GeoPoint(items.lat, items.lon);
			myItem = new OverlayItem(myGeo, items.name, items.address);
			itemizedoverlay.addOverlay(myItem);
			
			mMapController.setCenter(myGeo);
			mMapController.setZoom(16);
			
			mapOverlays.add(itemizedoverlay);
		}
		
		setTitle("Map");

	}
	

	@Override
	protected void onPause() {
		dbGeopoints.closeOnPause();
		mMyOverlay.disableMyLocation();
		mMapView.getOverlays().remove(mMyOverlay);
		mLocationManager.removeUpdates(mMyOverlay);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000L, 20.0f, mMyOverlay);

		mMapView.getOverlays().add(mMyOverlay);
		mMyOverlay.enableMyLocation();

		
	}
	
	SparseBooleanArray which;
	public void selectPOIs(){

			final ListView list = new ListView(this);
			list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			list.setBackgroundColor(Color.WHITE);

			ArrayAdapter<String> poiAdapter = new ArrayAdapter<String>(
	    			getApplicationContext(), 
	    			android.R.layout.select_dialog_multichoice, 
	    			android.R.id.text1, 
	    			RawDataProvider.catsNames);
			
			list.setAdapter(poiAdapter);
			
			if(which != null){
				for(int i = 0; i<which.size(); i++){
					list.setItemChecked(which.keyAt(i), true);
				}
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select the POIs to view:");
			builder.setView(list);

			builder.setPositiveButton(getString(android.R.string.ok),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int arg1) {

							dialog.dismiss();
							
							mapOverlays.clear();
							
							which = new SparseBooleanArray();
							which = list.getCheckedItemPositions();
							
							for(int i = 0; i<14; i++){
								if(which.get(i)){
									
									ArrayList<DataGeopoint> items = dbGeopoints.getAllGeopointsOfType(i);
									
									itemizedoverlay = new ItemizedOverlay(getApplication().getResources().getDrawable(RawDataProvider.icons[i]));
									
									GeoPoint myGeo;
									OverlayItem myItem;
									
									for (int j = 0; j < items.size(); j++) {
										myGeo = new GeoPoint(items.get(j).lat, items.get(j).lon);
										myItem = new OverlayItem(myGeo, items.get(j).name, items.get(j).address);
										itemizedoverlay.addOverlay(myItem);
									}
									
									mapOverlays.add(itemizedoverlay);
									
								}
							}
							
							mMapView.invalidate();
						}

					});
			builder.setNegativeButton(getString(android.R.string.cancel),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_opt, menu);
		
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i;
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                i = new Intent(this, POICategory.class);
                i.putExtra("poiType", poiType);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            case R.id.map_opt_location:
            	mMyOverlay.runOnFirstFix(new Runnable() {
        			public void run() {
        				mMapController.animateTo(mMyOverlay.getMyLocation());
        			}
        		});
            	return true;
            case R.id.map_opt_select_poi:
            	selectPOIs();
            	return true;
            case R.id.map_opt_settings:
    			//i = new Intent(this, SettingsMenu.class);
    			//startActivity(i);
            	return true;
            	
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class ItemizedOverlay extends
			com.google.android.maps.ItemizedOverlay {

		Context mContext = Map.this;
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

		public ItemizedOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));

		}

		public ItemizedOverlay(Drawable defaultMarker, Context context) {
			super(defaultMarker);
			mContext = context;
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		    if (!shadow)
		        super.draw(canvas, mapView, shadow);
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);

		}

		@Override
		public int size() {
			return mOverlays.size();
		}

		public void addOverlay(OverlayItem overlay) {
			mOverlays.add(overlay);
			populate();
		}

		
		
		protected boolean onTap(int index) {
			final OverlayItem item = mOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			if(mMyOverlay.getMyLocation() != null){
				dialog.setMessage(item.getSnippet() + "\n\n" + "Entfernung: " + calcDistance(item.getPoint(), mMyOverlay.getMyLocation()));
			}else{
				dialog.setMessage(item.getSnippet());
			}
			
			dialog.setPositiveButton("Route", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + getNavigationString(item.getPoint()))); 
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + getNavigationString(item.getPoint()))); 
					startActivity(i);
				}
			});
			dialog.setNegativeButton("Cancel", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			
			dialog.show();
			return true;

		}

	}
	
	private String calcDistance(GeoPoint poi, GeoPoint myLoc){
		return calcDistance(poi.getLatitudeE6(), poi.getLongitudeE6(), myLoc.getLatitudeE6(), myLoc.getLongitudeE6());
	}
	
	private String calcDistance(int lat1E6, int lon1E6, int lat2E6, int lon2E6){
		
		double lat1 = ((double) lat1E6) / 1000000d;
		double lat2 = ((double) lat2E6) / 1000000d;
		double lon1 = ((double) lon1E6) / 1000000d;
		double lon2 = ((double) lon2E6) / 1000000d;
		
		double earthRadius = 6371d;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLog = Math.toRadians(lon2-lon1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLog/2) * Math.sin(dLog/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    DecimalFormat dez;
	    
	    if(dist > 10){
	    	dez = new DecimalFormat("# km");
	    }else if(dist > 1){
	    	dez = new DecimalFormat("#,# km");
	    }else{
	    	dist = dist*1000d;
	    	dez = new DecimalFormat("# m");
	    }
	    
	    return dez.format(dist);
	}
	
	private String getNavigationString(GeoPoint poi){
		double lat = poi.getLatitudeE6() / 1E6d;
		double lon = poi.getLongitudeE6() / 1E6d;
		
		return lat + "," + lon;
	}

}
