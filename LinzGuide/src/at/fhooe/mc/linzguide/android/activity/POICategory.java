package at.fhooe.mc.linzguide.android.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import at.fhooe.mc.linzguide.android.R;
import at.fhooe.mc.linzguide.android.adapter.CategoryAdapter;
import at.fhooe.mc.linzguide.android.db.DBGeopoints;
import at.fhooe.mc.linzguide.android.util.DataGeopoint;

public class POICategory extends ListActivity {
	
	DBGeopoints dbGeopoints;
	ArrayList<DataGeopoint> items;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_cat);
        
        dbGeopoints = new DBGeopoints(getApplicationContext());
        
    }
    
    
    @Override
    protected void onResume() {
    	loadData();
    	super.onResume();
    }
    
    @Override
    protected void onPause() {
    	dbGeopoints.closeOnPause();
    	super.onPause();
    }
    
    public void loadData(){
    	
    	items = dbGeopoints.getAllGeopointsOfType(getIntent().getIntExtra("poiType", 0));
    	
    	ListAdapter gpAdapter = new CategoryAdapter(this,
				R.layout.list_element_2_lines,
				R.id.list_element_2_lines_title, items);
    	
    	getListView().setAdapter(gpAdapter);
    }
    
    public void showMap(final View view){
    	Intent i = new Intent(getApplicationContext(), Map.class);
    	i.putExtra("points", items);
    	i.putExtra("poiType", getIntent().getIntExtra("poiType", 0));
    	startActivity(i);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent i = new Intent(getApplicationContext(), Map.class);
    	i.putExtra("point", items.get(position));
    	i.putExtra("poiType", getIntent().getIntExtra("poiType", 0));
    	startActivity(i);
    	super.onListItemClick(l, v, position, id);
    }
}