package at.fhooe.mc.linzguide.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import at.fhooe.mc.linzguide.android.R;
import at.fhooe.mc.linzguide.android.actionbar.ActionBarListActivity;
import at.fhooe.mc.linzguide.android.adapter.CategoriesAdapter;
import at.fhooe.mc.linzguide.android.util.GeodataUpdater;
import at.fhooe.mc.linzguide.android.util.RawDataProvider;

public class POICategories extends ActionBarListActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_cats);
        
        setTitle("Kategorien");
        
        loadData();
    }
    
    
    @Override
    protected void onResume() {
    	
    	if(getIntent().hasExtra("update")){
    		checkUpdateProgress();
    		getIntent().removeExtra("update");
    	}
    	
    	super.onResume();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    public void loadData(){
    	
    	ListAdapter poiAdapter = new CategoriesAdapter(this,
				R.layout.list_element_1_line_and_icon,
				R.id.list_element_1_line_and_icon_title, RawDataProvider.catsNames);
    	
    	getListView().setAdapter(poiAdapter);
    }
    
    GeodataUpdater updater;
    boolean updateAvailable;
    
    private void checkUpdateProgress(){
		final ProgressDialog dialog = ProgressDialog.show(this, "",
				"Checking for updates ...", true, true);

		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				dialog.dismiss();
				
				if(updateAvailable){
					makeUpdateProgress();
				}else{
					Toast.makeText(getApplicationContext(), "Everything is Up2Date!", Toast.LENGTH_SHORT).show();
				}
			}
		};
		Thread startSaving = new Thread() {
			public void run() {
				
				updater = new GeodataUpdater(getApplicationContext());
		    	updateAvailable = updater.isUpdateAvailable();

				handler.sendEmptyMessage(0);
			}
		};
		startSaving.start();
	}
    
    int progressBarStatus = 0;
    ProgressDialog progressBar;
    private void makeUpdateProgress(){
    	progressBar = new ProgressDialog(this);
    	
		progressBar.setCancelable(false);
		progressBar.setMessage("Downloading Updates ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.show();
		
    	
    	final Handler handler = new Handler() {
    		public void handleMessage(Message msg) {
    			progressBar.dismiss();
    			
    			Toast.makeText(getApplicationContext(), "Update succsessful!", Toast.LENGTH_SHORT).show();
    			
    		}
    	};
    	Thread startSaving = new Thread() {
    		public void run() {
    			
    			updater.updateData(progressBar);
    			
    			try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    			
    			handler.sendEmptyMessage(0);
    		}
    	};
    	startSaving.start();
    }
    
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent i = new Intent(getApplicationContext(), POICategory.class);
    	i.putExtra("poiType", position);
    	startActivity(i);
    	super.onListItemClick(l, v, position, id);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.cats_opt, menu);
		
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	//Intent i;
		switch (item.getItemId()) {
		case R.id.cats_opt_update:
			checkUpdateProgress();
			break;
		case R.id.cats_opt_settings:
			//i = new Intent(this, SettingsMenu.class);
			//startActivity(i);
			break;
		}
    	return super.onOptionsItemSelected(item);
    }
   
}