package at.fhooe.mc.linzbrowser.android;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

public class LinzBrowserActivity extends ListActivity {
	
	DBGeopoints dbGeopoints;
	ArrayList<DataHort> items;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
    	
    	items = dbGeopoints.getAllHort();
    	
    	ListAdapter optionAdapter = new HortAdapter(this,
				R.layout.list_element_2_lines,
				R.id.list_element_2_lines_title, items);
    	
    	getListView().setAdapter(optionAdapter);
    }
    
    public void checkForUpdate(final View view){
    	
    	GeodataUpdater updater = new GeodataUpdater(getApplicationContext());
    	if(updater.isUpdateAvailable()){
    		updater.updateData();
    		loadData();
    	}else{
    		Toast.makeText(getApplicationContext(), "Everything is Up2Date!", Toast.LENGTH_SHORT).show();
    	}
    	
    }
    
    public void showMap(final View view){
    	Intent i = new Intent(getApplicationContext(), Map.class);
    	i.putExtra("data", items);
    	startActivity(i);
    }
}