package at.fhooe.mc.linzbrowser.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

public class GeodataUpdater {
	
	public ArrayList<Integer> needUpdate;
	public ArrayList<String> newRevisions;
	
	private Context ctx;
	
	public class PoiTypes {
	    public final static int HORTE = 0;
	    public final static int SOZIALBERATUNGSSTELLEN = 1;
	    public final static int SENIORENZENTREN = 2;
	    public final static int KRANKENANSTALTEN = 3;
	    public final static int JUGENDZENTREN = 4;
	    public final static int HOTSPOT = 5;
	    public final static int VERANSTALTUNG = 6;
	    public final static int SPORTANLAGEN = 7;
	    public final static int SPIELANLAGEN = 8;
	    public final static int CLUB_AKTIV = 9;
	    public final static int APOTHEKEN = 10;
	    public final static int AMBULATORIEN = 11;
	    public final static int KRABBELSTUBEN = 12;
	    public final static int KINDERGARTEN = 13;
	}
	
	private String[] prefNames = new String[]{
			"horte",
			"sozialberatungsstellen",
			"seniorenzentren",
			"krankenanstalten",
			"jugendzentren",
			"hotspot",
			"veranstaltung",
			"sportanlagen",
			"spielanlagen",
			"club-aktiv",
			"apotheken",
			"ambulatorien",
			"krabbelstuben",
			"kindergarten",
			};
	
	private String[] csvLinks = new String[]{
			"http://data.linz.gv.at/katalog/soziales_gesellschaft/kinder/kinderbetreuung/horte/POIS_Horte.csv",
			"http://data.linz.gv.at/katalog/soziales_gesellschaft/beratung/POIS_Sozialberatungsstellen.csv",
			"http://data.linz.gv.at/katalog/soziales_gesellschaft/senior/Beratung_Pflege_Betreuung/POIS_Seniorenzentren.csv",
			"http://data.linz.gv.at/katalog/gesundheit/krankenanstalten/krankenhaeuser/POIS_Krankenhaeuser.csv",
			"http://data.linz.gv.at/katalog/soziales_gesellschaft/jugend/jugendzentren/POIS_Jugendzentren.csv",
			"http://data.linz.gv.at/katalog/Freizeit/hotspot/Hotspot_Geodaten.csv",
			"http://data.linz.gv.at/katalog/Freizeit/veranstaltungsorte/POIS_Veranstaltungsort.csv",
			"http://data.linz.gv.at/katalog/Freizeit/sport_baeder/sportanlagen/POIS_Sportanlagen.csv",
			"http://data.linz.gv.at/katalog/soziales_gesellschaft/jugend/freizeit/POIS_Spielanlagen.csv",
			"http://data.linz.gv.at/katalog/soziales_gesellschaft/senior/freizeit/POIS_Club_Aktiv.csv",
			"http://data.linz.gv.at/katalog/linz_service/apotheken/POIS_Apotheke.csv",
			"http://data.linz.gv.at/katalog/gesundheit/krankenanstalten/krankenhaeuser/POIS_Ambulatorien.csv",
			"http://data.linz.gv.at/katalog/soziales_gesellschaft/kinder/kinderbetreuung/krabbelstuben/POIS_Krabbelstube.csv",
			"http://data.linz.gv.at/katalog/soziales_gesellschaft/kinder/kinderbetreuung/kindergaerten/POIS_Kindergarten.csv",
			};
	
	private String[] jsonLinks = new String[]{
			"http://ckan.data.linz.gv.at/api/rest/package/horte-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/sozialberatungsstellen-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/seniorenzentren-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/krankenanstalten-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/jugendzentren-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/hotspot--standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/veranstaltung-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/sportanlagen-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/spielanlagen-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/club-aktiv-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/apotheken-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/ambulatorien-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/krabbelstuben-standorte",
			"http://ckan.data.linz.gv.at/api/rest/package/kindergarten-standorte",
			};
	
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
		
		for(int i = 0; i < jsonLinks.length; i++){
			
			currRevisionId = RevisionPrefs.getString(prefNames[i], "");
			
			if(currRevisionId.equals("")){
				
				needUpdate.add(i);
				newRevisions.add(getRevisionId(jsonLinks[i]));
				updateAvailable = true;
				
			}else{
				newRevisionId = getRevisionId(jsonLinks[i]);
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
	
	public void updateData(){
		
		SharedPreferences RevisionPrefs = ctx.getSharedPreferences("GeodataRevisions", 0);
		SharedPreferences.Editor editor = RevisionPrefs.edit();
		
		DBGeopoints dbGeopoints = new DBGeopoints(ctx);
		
		for(int i = 0; i<needUpdate.size(); i++){
			
			
				
			switch (needUpdate.get(i)) {
			case PoiTypes.HORTE:
				
				ArrayList<String[]> list = getData(csvLinks[needUpdate.get(i)]);
				dbGeopoints.deleteAllHort();
				
				for(int j = 1; j < list.size(); j++){
					dbGeopoints.saveHort(new DataHort(list.get(j)[0], list.get(j)[1], list.get(j)[2], list.get(j)[3], list.get(j)[4], list.get(j)[5], list.get(j)[6], list.get(j)[7], list.get(j)[8]));
				}
				break;
			}
				
			
			editor.putString(prefNames[needUpdate.get(i)], newRevisions.get(i));
			
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
