package ar.com.sicardi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class DrawService {
	
	   /** 
     * Does the actual drawing of the route, based on the geo points provided in the nav set 
     * 
     * @param navSet     Navigation set bean that holds the route information, incl. geo pos 
     * @param color      Color in which to draw the lines 
     * @param mMapView01 Map view to draw onto 
     */ 
    public static void drawStadium(NavigationDataSet navSet, int color, MapView mMapView01, long modoArchivo) { 
     
        Log.d(myapp.APP, "map color before: " + color);         
     
        // color correction for dining, make it darker 
        if (color == Color.parseColor("#add331")) color = Color.parseColor("#6C8715"); 
        Log.d(myapp.APP, "map color after: " + color); 
     
        Collection overlaysToAddAgain = new ArrayList(); 
        for (Iterator iter = mMapView01.getOverlays().iterator(); iter.hasNext();) { 
            Object o = iter.next(); 
            Log.d(myapp.APP, "overlay type: " + o.getClass().getName()); 
            if (!StadiumOverlay.class.getName().equals(o.getClass().getName())) { 
                // mMapView01.getOverlays().remove(o); 
                overlaysToAddAgain.add(o); 
            } 
        } 
        mMapView01.getOverlays().clear(); 
        mMapView01.getOverlays().addAll(overlaysToAddAgain); 
     
        //String path = navSet.getRoutePlacemark().getCoordinates();
        for (Placemark tempPlace : navSet.getPlacemarks()){
        String path = tempPlace.getCoordinates();
        Log.d(myapp.APP, "path=" + path); 
        if (path != null && path.trim().length() > 0) { 
            String[] pairs = path.trim().split(" "); 
     
            Log.d(myapp.APP, "pairs.length=" + pairs.length); 
            String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude lngLat[1]=latitude lngLat[2]=height 
            Log.d(myapp.APP, "lnglat =" + lngLat + ", length: " + lngLat.length); 
     
            if (lngLat.length<3) lngLat = pairs[1].split(","); // if first pair is not transferred completely, take seconds pair //TODO  
            try { 
            	Style styleTmpe = navSet.getStyleById(tempPlace.getStyleUrl());
            	String styleTmp = null;
            	if(styleTmpe!=null)  styleTmp = styleTmpe.iconHref;
                GeoPoint startGP = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6)); 
                mMapView01.getOverlays().add(new StadiumOverlay(startGP, 1,color,tempPlace.getDescription(),styleTmp,modoArchivo)); 
                Log.d(myapp.APP, "draw STADIUM IN: " + startGP.getLatitudeE6() + "/" + startGP.getLongitudeE6()); 
            } catch (NumberFormatException e) { 
                Log.e(myapp.APP, "Cannot draw route.", e); 
            } 
        } 
        }  
        // mMapView01.getOverlays().addAll(routeOverlays); // use the default color 
        mMapView01.setEnabled(true); 
    } 
    
    /** 
     * Does the actual drawing of the route, based on the geo points provided in the nav set 
     * 
     * @param navSet     Navigation set bean that holds the route information, incl. geo pos 
     * @param color      Color in which to draw the lines 
     * @param mMapView01 Map view to draw onto 
     */ 
    public static void drawPath(NavigationDataSet navSet, int color, MapView mMapView01) { 
     
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

	public static void dibujaEnMapa(MapView mapView2, NavigationDataSet ds, boolean rutas, GeoPoint destPoint, GeoPoint currentPoint, DirectionMapActivity actividad) {
        if (rutas) {
            // dibuja la RUTA/puntos
            drawPath(ds, Color.parseColor("#add331"), mapView2 ); //drawPath dibuja ruta
            
            // encontrar los l�mites usando itemized overlay 
            Drawable dot = actividad.getResources().getDrawable(R.drawable.pixel); 
            MapItemizedOverlay bgItemizedOverlay = new MapItemizedOverlay(dot,actividad); 
            OverlayItem currentPixel = new OverlayItem(destPoint, null, null ); 
            OverlayItem destPixel = new OverlayItem(currentPoint, null, null ); 
            bgItemizedOverlay.addOverlay(currentPixel); 
            bgItemizedOverlay.addOverlay(destPixel);
        }
        else {
            // dibuja la RUTA/puntos
            drawStadium(ds, Color.parseColor("#add331"), mapView2, actividad.getModoLocalOURL()); //drawPath dibuja ruta
        }
	}
	
	public static GeoPoint puntoCentralMap(Double latitud, Double longitud, DirectionMapActivity actividad){
		GeoPoint currentPoint = null;
			
		LocationManager locationManager = (LocationManager) actividad.getSystemService(Context.LOCATION_SERVICE); 
         
		List<Double> destD = new ArrayList<Double>();
        destD.add(latitud);
        destD.add(longitud);
        GeoPoint destPoint = new GeoPoint(destD.get(0).intValue(),destD.get(1).intValue());  
        
        String locationProvider = LocationManager.NETWORK_PROVIDER; 
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider); 
        
        if (lastKnownLocation!=null) {
            currentPoint = new GeoPoint( new Double(lastKnownLocation.getLatitude()*1E6).intValue() 
                                                ,new Double(lastKnownLocation.getLongitude()*1E6).intValue() );	
        } 
        else {
            currentPoint = destPoint;	       	
        }
        return currentPoint;
	}

	public static GeoPoint puntoCentralMap(Double latitud,
			Double longitud) {
		List<Double> destD = new ArrayList<Double>();
        destD.add(latitud);
        destD.add(longitud);
        GeoPoint destPoint = new GeoPoint(destD.get(0).intValue(),destD.get(1).intValue());  
        return destPoint;
	}
}
