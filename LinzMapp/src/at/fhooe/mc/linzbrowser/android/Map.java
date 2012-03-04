package at.fhooe.mc.linzbrowser.android;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Map extends MapActivity {

	MapView mMapView;
	MapController mMapController;

	GeoPoint mGeoPoint = new GeoPoint(48210907, 13487763);
	public LocationManager mLocationManager;
	private MyLocationOverlay mMyOverlay;

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
		List<Overlay> mapOverlays = mMapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.hort);
		
		ItemizedOverlay itemizedoverlay = new ItemizedOverlay(drawable) {
			@Override
			public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			    if (!shadow)
			        super.draw(canvas, mapView, shadow);
			}
		};

		
		ArrayList<DataHort> items = (ArrayList<DataHort>) getIntent().getSerializableExtra("data");
		
		GeoPoint myGeo;
		OverlayItem myItem;
		
		for (int i = 0; i < items.size(); i++) {
			myGeo = new GeoPoint(items.get(i).lat, items.get(i).lon);
			myItem = new OverlayItem(myGeo, items.get(i).name, items.get(i).address);
			itemizedoverlay.addOverlay(myItem);
		}
		mapOverlays.add(itemizedoverlay);

	}
	

	@Override
	protected void onPause() {
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

		mMyOverlay.runOnFirstFix(new Runnable() {

			public void run() {

				//mMapController.animateTo(mMyOverlay.getMyLocation());

			}
		});
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

		@Override
		protected boolean onTap(int index) {
			OverlayItem item = mOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();
			return true;

		}

	}

}
