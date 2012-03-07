package at.fhooe.mc.linzguide.android.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import at.fhooe.mc.linzguide.android.R;
import at.fhooe.mc.linzguide.android.adapter.CategoriesAdapter;
import at.fhooe.mc.linzguide.android.adapter.CategoryAdapter;
import at.fhooe.mc.linzguide.android.db.DBGeopoints;
import at.fhooe.mc.linzguide.android.util.GeodataUpdater;
import at.fhooe.mc.linzguide.android.util.RawDataProvider;

public class POICategories extends ListActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_cats);
        
    }
    
    
    @Override
    protected void onResume() {
    	loadData();
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
    
    public void checkForUpdate(final View view){
    	checkUpdateProgress();
    }
    
    GeodataUpdater updater;
    boolean updateAvailable;
    
    
    private void checkUpdateProgress(){
		final ProgressDialog dialog = ProgressDialog.show(this, "",
				"Check for updates ...", true, true);

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
		progressBar.setMessage("Download Updates ...");
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
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
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
   
}