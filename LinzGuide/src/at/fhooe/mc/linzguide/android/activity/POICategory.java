package at.fhooe.mc.linzguide.android.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.ClipData.Item;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import at.fhooe.mc.linzguide.android.R;
import at.fhooe.mc.linzguide.android.actionbar.ActionBarListActivity;
import at.fhooe.mc.linzguide.android.adapter.CategoryAdapter;
import at.fhooe.mc.linzguide.android.db.DBGeopoints;
import at.fhooe.mc.linzguide.android.util.DataGeopoint;
import at.fhooe.mc.linzguide.android.util.RawDataProvider;

public class POICategory extends ActionBarListActivity {
	
	DBGeopoints dbGeopoints;
	ArrayList<DataGeopoint> items;
	int poiType;
	SharedPreferences preferences;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_cat);
        
        
        // Initialize preferences
   		preferences = PreferenceManager.getDefaultSharedPreferences(this);
        dbGeopoints = new DBGeopoints(getApplicationContext());
        
        poiType = getIntent().getIntExtra("poiType", 0);
        
        loadData();
    }
    
    
    @Override
    protected void onResume() {
    	
    	setTitle(RawDataProvider.catsNames[poiType]);
    	
    	super.onResume();
    }
    
    @Override
    protected void onPause() {
    	dbGeopoints.closeOnPause();
    	super.onPause();
    }
    
    public void loadData(){
    	
    	items = dbGeopoints.getAllGeopointsOfType(poiType);
    	
    	ListAdapter gpAdapter = new CategoryAdapter(this,
				R.layout.list_element_2_lines,
				R.id.list_element_2_lines_title, items);
    	
    	getListView().setAdapter(gpAdapter);
    }
    
    public void searchData(String query){
    	
    	items = dbGeopoints.searchAllGeopointsOfType(poiType, query);
    	
    	ListAdapter gpAdapter = new CategoryAdapter(this,
				R.layout.list_element_2_lines,
				R.id.list_element_2_lines_title, items);
    	
    	getListView().setAdapter(gpAdapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent i = new Intent(getApplicationContext(), Map.class);
    	i.putExtra("point", items.get(position));
    	i.putExtra("poiType", getIntent().getIntExtra("poiType", 0));
    	startActivity(i);
    	super.onListItemClick(l, v, position, id);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.cat_opt, menu);
    	
    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
    		SearchView searchView = (SearchView) menu.findItem(R.id.cat_opt_search).getActionView();
    		searchView.setOnQueryTextListener(new OnQueryTextListener() {
				
				public boolean onQueryTextSubmit(String query) {
					Toast.makeText(getApplicationContext(), "submit", Toast.LENGTH_SHORT).show();
					return false;
				}
				
				public boolean onQueryTextChange(String newText) {
					searchData(newText);
					return false;
				}
			});
    		
    		if(getListView().getCount() == 0){
        		menu.findItem(R.id.cat_opt_update).setVisible(true);
        		menu.findItem(R.id.cat_opt_update).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        		menu.findItem(R.id.cat_opt_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        		menu.findItem(R.id.cat_opt_map).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        	}
    	}
    	
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i;
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                i = new Intent(this, POICategories.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            case R.id.cat_opt_map:
            	i = new Intent(getApplicationContext(), Map.class);
            	i.putExtra("points", items);
            	i.putExtra("poiType", getIntent().getIntExtra("poiType", 0));
            	startActivity(i);
            	return true;
            case R.id.cat_opt_search:
            	return true;
            case R.id.cat_opt_update:
            	i = new Intent(this, POICategories.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("update", true);
                startActivity(i);
            	return true;
            case R.id.cat_opt_settings:
    			i = new Intent(this, SettingsMenu.class);
    			startActivity(i);
            	return true;
            	
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}