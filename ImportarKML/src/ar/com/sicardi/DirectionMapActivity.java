package ar.com.sicardi;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class DirectionMapActivity extends MapActivity { 
	 
	private Button btnSatelite = null;
	private Button btnCentrar = null;
	private Button btnAnimar = null;
	private Button btnMover = null;
	private MapController controlMapa = null;
	private MapView mapView = null; 
	
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.main); 
 
        mapView = (MapView) findViewById(R.id.mapview); 
        mapView.setBuiltInZoomControls(true); 
        parseandoLocal();
        //parseandoPorAPI();
 
    } 

    /***
     * 
     */
    public void parseandoLocal(){
        btnSatelite = (Button)findViewById(R.id.BtnSatelite);
        btnCentrar = (Button)findViewById(R.id.BtnCentrar);
        btnAnimar = (Button)findViewById(R.id.BtnAnimar);
        btnMover = (Button)findViewById(R.id.BtnMover);
        //Controlador del mapa
        controlMapa = mapView.getController();
        btnSatelite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mapView.isSatellite())
					mapView.setSatellite(false);
				else
					mapView.setSatellite(true);
			}
		});
        btnCentrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Double latitud = 37.40*1E6;
				Double longitud = -5.99*1E6;
				GeoPoint loc = 
					new GeoPoint(latitud.intValue(), longitud.intValue());
				controlMapa.setCenter(loc);
				controlMapa.setZoom(10);
			}
		});
        btnAnimar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Double latitud = 37.40*1E6;
				Double longitud = -5.99*1E6;
				GeoPoint loc = 
					new GeoPoint(latitud.intValue(), longitud.intValue());
				controlMapa.animateTo(loc);
				int zoomActual = mapView.getZoomLevel();
				for(int i=zoomActual; i<10; i++)
				{
					controlMapa.zoomIn();
				}
			}
		});
        btnMover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				controlMapa.scrollBy(40, 40);
			}
		});

        // Acquire a reference to the system Location Manager 
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE); 
 
        String locationProvider = LocationManager.NETWORK_PROVIDER; 
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider); 
		//Double latitud = 37.40*1E6;
		//Double longitud = -5.99*1E6;
        Double latitud = -14.91 +2.0000001;
		Double longitud = -57.96 +1.0000001;
        List<Double> destD = new ArrayList<Double>();
        destD.add(latitud);
        destD.add(longitud);
        
        
       StringBuilder urlString = new StringBuilder(); 
      /* urlString.append("http://maps.google.com/maps?f=d&hl=en"); 
        urlString.append("&saddr=");//from 
        urlString.append( Double.toString(lastKnownLocation.getLatitude() )); 
        urlString.append(","); 
        urlString.append( Double.toString(lastKnownLocation.getLongitude() )); 
        urlString.append("&daddr=");//to 
        urlString.append( Double.toString(destD.get(0).doubleValue())); 
        urlString.append(","); 
        urlString.append( Double.toString(destD.get(1).doubleValue())); 
        urlString.append("&ie=UTF8&0&om=0&output=kml"); */
       urlString.append("http://www.prolab.unlp.edu.ar/prolabBeta/images/convenioAFA/PartidosFinal.kml");
       //commit
   
       Intent mapIntent = new Intent(Intent.ACTION_VIEW, null);
       //Uri uri1 = Uri.parse("geo:0,0?q=http://code.google.com/apis/kml/documentation/KML_Samples.kml");
       Uri uri1 = Uri.parse("geo:-34.9134721,-57.9614658?q=http://maps.google.com.ar/maps/ms?authuser=0&vps=8&hl=es&ie=UTF8&msa=0&output=kml");
       mapIntent.setData(uri1);
       startActivity(Intent.createChooser(mapIntent, "Sample Map ")); 
        
        urlString.append("http://maps.google.com/maps?f=d&hl=en&saddr=-34.9134721,-57.9614658&daddr=-14.9134721,-57.9614658&ie=UTF8&0&om=0&output=kml");
        
        try{ 
            //setea la url
            URL url = new URL(urlString.toString()); 
            // crea el factory del parser 
            SAXParserFactory factory = SAXParserFactory.newInstance(); 
            // crea un parser
            SAXParser parser = factory.newSAXParser(); 
            // crea un lector del parser 
            XMLReader xmlreader = parser.getXMLReader(); 
            // instancia un manejador de la navegacion del parseo // DECIDE COMO TRATAR LOS TAGS XML 
            NavigationSaxHandler navSaxHandler = new NavigationSaxHandler(); 
            // le asigna el manejador al lector xml
            xmlreader.setContentHandler(navSaxHandler); 
            // toma el KML de la URL pasada
            InputSource is = new InputSource(url.openStream()); 
            // ACA SE PARSEA EL KML            
            xmlreader.parse(is); 
            // Retorna los datos generados del parseo en un DATASET
            NavigationDataSet ds = navSaxHandler.getParsedData(); 
 
            // dibuja la RUTA/puntos
             drawPath(ds, Color.parseColor("#add331"), mapView ); 
 
            // encontrar los l�mites usando itemized overlay 
            GeoPoint destPoint = new GeoPoint(destD.get(0).intValue(),destD.get(1).intValue()); 
            GeoPoint currentPoint = new GeoPoint( new Double(lastKnownLocation.getLatitude()*1E6).intValue() 
                                                ,new Double(lastKnownLocation.getLongitude()*1E6).intValue() ); 
 
            Drawable dot = this.getResources().getDrawable(R.drawable.pixel); 
            MapItemizedOverlay bgItemizedOverlay = new MapItemizedOverlay(dot,this); 
            OverlayItem currentPixel = new OverlayItem(destPoint, null, null ); 
            OverlayItem destPixel = new OverlayItem(currentPoint, null, null ); 
            bgItemizedOverlay.addOverlay(currentPixel); 
            bgItemizedOverlay.addOverlay(destPixel);
 
            // centrar y acomodar zoom en el mapa 
            MapController mc = mapView.getController(); 
            mc.zoomToSpan(bgItemizedOverlay.getLatSpanE6()*2,bgItemizedOverlay.getLonSpanE6()*2); 
            mc.animateTo(new GeoPoint( 
                    (currentPoint.getLatitudeE6() + destPoint.getLatitudeE6()) / 2 
                    , (currentPoint.getLongitudeE6() + destPoint.getLongitudeE6()) / 2));
 
        } catch(Exception e) { 
            Log.d("DirectionMap","Exception parsing kml."); 
        }    	
    }
    
    /***
     * 
     */
    public void parseandoPorAPI(){
    	final HttpClient client = new DefaultHttpClient();
    	final HttpGet get = new HttpGet("http://maps.google.com/maps?f=d&hl=en&saddr=-34.9134721,-57.9614658&daddr=-14.9134721,-57.9614658&ie=UTF8&0&om=0&output=kml");
    	try {
    	    final HttpResponse resp = client.execute(get);
    	    android.util.Log.e("MapOverlays", resp.toString());
    	} catch (Throwable t) {
    	    android.util.Log.e("MapOverlays", "Exception", t);
    	}
    	final Intent intent = new Intent(
    			android.content.Intent.ACTION_VIEW, 
    			Uri.parse("geo:0,0?q=http://www.prolab.unlp.edu.ar/prolabBeta/images/convenioAFA/PartidosFinal.kml"));
    	startActivity(intent);
    	//"http://maps.google.com.ar/maps/ms?authuser=0&vps=2&hl=es&ie=UTF8&msa=0&output=kml&msid=210826482800711502689.0004aa13f8cbd69bf1b2c"
        //startActivity(Intent.createChooser(mapIntent, "Sample Map ")); 

    }
    
    /** 
     * Does the actual drawing of the route, based on the geo points provided in the nav set 
     * 
     * @param navSet     Navigation set bean that holds the route information, incl. geo pos 
     * @param color      Color in which to draw the lines 
     * @param mMapView01 Map view to draw onto 
     */ 
    public void drawPath(NavigationDataSet navSet, int color, MapView mMapView01) { 
     
        Log.d(myapp.APP, "map color before: " + color);         
     
        // color correction for dining, make it darker 
        if (color == Color.parseColor("#add331")) color = Color.parseColor("#6C8715"); 
        Log.d(myapp.APP, "map color after: " + color); 
     
        Collection overlaysToAddAgain = new ArrayList(); 
        for (Iterator iter = mMapView01.getOverlays().iterator(); iter.hasNext();) { 
            Object o = iter.next(); 
            Log.d(myapp.APP, "overlay type: " + o.getClass().getName()); 
            if (!RouteOverlay.class.getName().equals(o.getClass().getName())) { 
                // mMapView01.getOverlays().remove(o); 
                overlaysToAddAgain.add(o); 
            } 
        } 
        mMapView01.getOverlays().clear(); 
        mMapView01.getOverlays().addAll(overlaysToAddAgain); 
     
        String path = navSet.getRoutePlacemark().getCoordinates(); 
        Log.d(myapp.APP, "path=" + path); 
        if (path != null && path.trim().length() > 0) { 
            String[] pairs = path.trim().split(" "); 
     
            Log.d(myapp.APP, "pairs.length=" + pairs.length); 
     
            String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude lngLat[1]=latitude lngLat[2]=height 
     
            Log.d(myapp.APP, "lnglat =" + lngLat + ", length: " + lngLat.length); 
     
            if (lngLat.length<3) lngLat = pairs[1].split(","); // if first pair is not transferred completely, take seconds pair //TODO  
     
            try { 
                GeoPoint startGP = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6)); 
                mMapView01.getOverlays().add(new RouteOverlay(startGP, startGP, 1)); 
                GeoPoint gp1; 
                GeoPoint gp2 = startGP; 
     
                for (int i = 1; i < pairs.length; i++) // the last one would be crash 
                { 
                    lngLat = pairs[i].split(","); 
     
                    gp1 = gp2; 
     
                    //if (lngLat.length >= 2 && gp1.getLatitudeE6() > 0 && gp1.getLongitudeE6() > 0 
                           // && gp2.getLatitudeE6() > 0 && gp2.getLongitudeE6() > 0) { 
     
                        // for GeoPoint, first:latitude, second:longitude 
                        gp2 = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6)); 
     
                        if (gp2.getLatitudeE6() != 22200000) {  
                            mMapView01.getOverlays().add(new RouteOverlay(gp1, gp2, 2, color)); 
                            Log.d(myapp.APP, "draw:" + gp1.getLatitudeE6() + "/" + gp1.getLongitudeE6() + " TO " + gp2.getLatitudeE6() + "/" + gp2.getLongitudeE6()); 
                        } 
                   // } 
                    // Log.d(myapp.APP,"pair:" + pairs[i]); 
                } 
                //routeOverlays.add(new RouteOverlay(gp2,gp2, 3)); 
                mMapView01.getOverlays().add(new RouteOverlay(gp2, gp2, 3)); 
            } catch (NumberFormatException e) { 
                Log.e(myapp.APP, "Cannot draw route.", e); 
            } 
        } 
        // mMapView01.getOverlays().addAll(routeOverlays); // use the default color 
        mMapView01.setEnabled(true); 
    } 

    @Override
    protected boolean isRouteDisplayed() {
    	return true;
    }
    
}
    // and the rest of the methods in activity, e.g. drawPath() etc... 
