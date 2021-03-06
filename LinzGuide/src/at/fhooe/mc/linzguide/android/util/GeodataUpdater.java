package at.fhooe.mc.linzguide.android.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import at.fhooe.mc.linzguide.android.db.DBGeopoints;

public class GeodataUpdater {
	
	public ArrayList<Integer> needUpdate;
	public ArrayList<String> newRevisions;
	
	private Context ctx;
	
	
	public GeodataUpdater(Context ctx) {
		this.ctx = ctx;
		needUpdate = new ArrayList<Integer>();
		newRevisions = new ArrayList<String>();
	}
	
	public boolean isUpdateAvailable(){
		
		SharedPreferences RevisionPrefs = ctx.getSharedPreferences("GeodataRevisions", 0);
		
		boolean updateAvailable = false;
		
		String currRevisionId;
		String newRevisionId;
		
		for(int i = 0; i < RawDataProvider.jsonLinks.length; i++){
			
			currRevisionId = RevisionPrefs.getString(RawDataProvider.prefNames[i], "");
			
			if(currRevisionId.equals("")){
				
				needUpdate.add(i);
				newRevisions.add(getRevisionId(RawDataProvider.jsonLinks[i]));
				updateAvailable = true;
				
			}else{
				newRevisionId = getRevisionId(RawDataProvider.jsonLinks[i]);
				if(!currRevisionId.equals(newRevisionId)){
					needUpdate.add(i);
					newRevisions.add(newRevisionId);
					updateAvailable = true;
				}
			}

		}
		
		return updateAvailable;
	}
	
	private String getRevisionId(String url){
		
		try {
			
			URI uri = new URI(url);
    	
	    	HttpClient httpClient = new DefaultHttpClient();
	    	HttpContext localContext = new BasicHttpContext();
	        HttpGet httpGet = new HttpGet(uri);
	        HttpResponse response;
	        response = httpClient.execute(httpGet, localContext);
	
	        BufferedReader reader2 = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "ISO-8859-1"));
	        JSONObject json = new JSONObject(reader2.readLine());
	    	
	        return json.getString("revision_id");
		
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public void updateData(ProgressDialog progressBar){
		
		SharedPreferences RevisionPrefs = ctx.getSharedPreferences("GeodataRevisions", 0);
		SharedPreferences.Editor editor = RevisionPrefs.edit();
		
		DBGeopoints dbGeopoints = new DBGeopoints(ctx);
		
		progressBar.setMax(needUpdate.size());
		
		for(int i = 0; i<needUpdate.size(); i++){
			
			int id = needUpdate.get(i);
			
			ArrayList<String[]> list = getData(RawDataProvider.csvLinks[id]);
			dbGeopoints.deleteAllGeopointsOfType(id);
			
			switch(id){
				//Einrichtung;Adresse;Internet_Adresse;Kategorie;eindeutige_Adresse;X;Y;Lat;Lon
			case RawDataProvider.PoiTypes.HORTE:
				for(int j = 1; j < list.size(); j++){
					dbGeopoints.saveGeoPoint(id, new DataGeopoint(list.get(j)[0], list.get(j)[1], list.get(j)[2], list.get(j)[3], list.get(j)[4], list.get(j)[5], list.get(j)[6], list.get(j)[7], list.get(j)[8], "","","","",""));
				}
				break;
				//Nummer;Latitude;Longitude;Name;Kurztext;Start im Jahr;Ende im Jahr;Stadt;Postleitzahl;Stra�e;Homepage
			case RawDataProvider.PoiTypes.HOTSPOT:
				for(int j = 1; j < list.size(); j++){
					dbGeopoints.saveGeoPoint(id, new DataGeopoint(list.get(j)[3], list.get(j)[9] + ", " + list.get(j)[8] + " " + list.get(j)[7], list.get(j)[10], "Hotspot", list.get(j)[9], "0", "0", list.get(j)[1], list.get(j)[2], "","","","",""));
				}
				break;
				//Einrichtung;Adresse;Kategorie;Internet_Adresse;X;Y;eindeutige_Adresse;Lat;Lon
			case RawDataProvider.PoiTypes.VERANSTALTUNG:
				for(int j = 1; j < list.size(); j++){
					dbGeopoints.saveGeoPoint(id, new DataGeopoint(list.get(j)[0], list.get(j)[1], list.get(j)[3], list.get(j)[2], list.get(j)[6], list.get(j)[4], list.get(j)[5], list.get(j)[7], list.get(j)[8], "","","","",""));
				}
				break;
				//Einrichtung;Adresse;Internet_Adresse;Kategorie;X;Y;Lat;Lon
			case RawDataProvider.PoiTypes.SPIELANLAGEN:
			case RawDataProvider.PoiTypes.APOTHEKEN:
			case RawDataProvider.PoiTypes.AMBULATORIEN:
				for(int j = 1; j < list.size(); j++){
					dbGeopoints.saveGeoPoint(id, new DataGeopoint(list.get(j)[0], list.get(j)[1], list.get(j)[2], list.get(j)[3], "", list.get(j)[4], list.get(j)[5], list.get(j)[6], list.get(j)[7], "","","","",""));
				}
				break;
				
				//Einrichtung;Beschreibung;Zimmer;SinglePrice;DoublePrice;lat;lon;eindeutige_adresse;website
			case RawDataProvider.PoiTypes.HOTELS:
				for(int j = 1; j < list.size(); j++){
					dbGeopoints.saveGeoPoint(id, new DataGeopoint(list.get(j)[0], list.get(j)[8], list.get(j)[10], list.get(j)[2], list.get(j)[8],"","", list.get(j)[6], list.get(j)[7], list.get(j)[3],list.get(j)[5],list.get(j)[4],list.get(j)[1], list.get(j)[9]));
				}
				break;
				
				//Einrichtung;Beschreibung;Kategorie;Eintritt Vollpreis(doubleprice);Ermae�igt(singleprice);Lat;Lon;eindeutige_Adresse;Telefon;Internet_Adresse;
			case RawDataProvider.PoiTypes.SEHENSWUERDIGKEITEN:
				for(int j = 1; j < list.size(); j++){
					dbGeopoints.saveGeoPoint(id, new DataGeopoint(list.get(j)[0], list.get(j)[7], list.get(j)[9], list.get(j)[2], list.get(j)[7],"","", list.get(j)[5], list.get(j)[6],"", list.get(j)[3], list.get(j)[4],list.get(j)[1],list.get(j)[8]));
				}
				break;
				
				//Einrichtung;Adresse;Internet_Adresse;Kategorie;X;Y;eindeutige_Adresse;Lat;Lon
			default:
				for(int j = 1; j < list.size(); j++){
					dbGeopoints.saveGeoPoint(id, new DataGeopoint(list.get(j)[0], list.get(j)[1], list.get(j)[2], list.get(j)[3], list.get(j)[6], list.get(j)[4], list.get(j)[5], list.get(j)[7], list.get(j)[8], "","","","",""));
				}
				break;	
			}
				
			editor.putString(RawDataProvider.prefNames[id], newRevisions.get(i));
			
			progressBar.setProgress(i+1);
		}
		
		editor.commit();
		dbGeopoints.closeOnPause();
	}
	
	private ArrayList<String[]> getData(String url){
		
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		try {
			URI uri = new URI(url);
	    	
	    	HttpClient httpClient = new DefaultHttpClient();
	    	HttpContext localContext = new BasicHttpContext();
	        HttpGet httpGet = new HttpGet(uri);
	        HttpResponse response;
	        response = httpClient.execute(httpGet, localContext);
	
	        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "ISO-8859-1"));
	    	
	    	String next[] = {};
	
	        CSVReader reader1 = new CSVReader(reader);
	        for(;;) {
	            next = reader1.readNext();
	            if(next != null) {
	                list.add(next);
	            } else {
	                break;
	            }
	        }
        
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
}
