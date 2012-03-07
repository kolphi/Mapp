package at.fhooe.mc.linzguide.android.util;

import at.fhooe.mc.linzguide.android.R;


public class RawDataProvider {

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
	
	public static String[] prefNames = new String[]{
			"horte",
			"sozialberatungsstellen",
			"seniorenzentren",
			"krankenanstalten",
			"jugendzentren",
			"hotspot",
			"veranstaltungen",
			"sportanlagen",
			"spielanlagen",
			"club_aktiv",
			"apotheken",
			"ambulatorien",
			"krabbelstuben",
			"kindergarten",
			};
	
	public static String[] catsNames = new String[]{
			"Horte",
			"Sozialberatungsstellen",
			"Seniorenzentren",
			"Krankenanstalten",
			"Jugendzentren",
			"Hotspots",
			"Veranstaltung",
			"Sportanlagen",
			"Spielanlagen",
			"Club-Aktiv",
			"Apotheken",
			"Ambulatorien",
			"Krabbelstuben",
			"Kindergärten",
			};
	
	public static int[] icons = new int[]{
		R.drawable.horte,
		R.drawable.sozialberatungsstellen,
		R.drawable.seniorenzentren,
		R.drawable.krankenanstalten,
		R.drawable.jugendzentren,
		R.drawable.hotspots,
		R.drawable.veranstaltung,
		R.drawable.sportanlagen,
		R.drawable.spielanlagen,
		R.drawable.clubaktiv,
		R.drawable.apotheken,
		R.drawable.ambulatorien,
		R.drawable.krabbelstuben,
		R.drawable.kindergarten,
		};
	
	public static String[] csvLinks = new String[]{
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
	
	public static String[] jsonLinks = new String[]{
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
	
}
