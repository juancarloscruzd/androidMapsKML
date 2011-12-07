package ar.com.sicardi;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Intent;
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
import com.google.android.maps.Overlay;

public class DirectionMapActivity extends MapActivity { 
	 
	private Button btnSatelite = null;
	private Button btnCentrar = null;
	private Button btnAnimar = null;
	private Button btnMover = null;
	private Button btnRecorrer = null;
	private MapController controlMapa = null;
	private MapView mapView = null; 
	private long modoLocalOURL = 0L; //inicia en URL  y 1L local
	
    public long getModoLocalOURL() {
		return modoLocalOURL;
	}

	public void setModoLocalOURL(long modoLocalOURL) {
		this.modoLocalOURL = modoLocalOURL;
	}

	@Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.main); 
 
        mapView = (MapView) findViewById(R.id.mapview); 
        mapView.setBuiltInZoomControls(true); 
        parseandoLocal(false);
        //parseandoPorAPI();
 
    } 

    /***
     * 
     */
    public void parseandoLocal(boolean rutas){
        cargarBotones();

        GeoPoint currentPoint = DrawService.puntoCentralMap(Parametros.LATITUD_CORDOBA,Parametros.LONGITUD_CORDOBA,this);
       
        InputStream archivo = null; 
        try{
        	archivo = FileHandler.recuperarArchivo(Parametros.PATH_MEMORIA+Parametros.ARCHIVO_KML,Parametros.ARCHIVO_KML_URL,this);
        	NavigationDataSet ds = parsearArchivo(archivo);
            DrawService.dibujaEnMapa(mapView,ds,rutas,currentPoint,currentPoint,this);
            // centrar y acomodar zoom en el mapa
            controlMapa = mapView.getController();
            controlMapa.setZoom(Parametros.ZOOM_DEFECTO);
            controlMapa.zoomToSpan(currentPoint.getLatitudeE6()*2,currentPoint.getLongitudeE6()*2); 
            //controlMapa.animateTo(new GeoPoint((currentPoint.getLatitudeE6() + currentPoint.getLatitudeE6()) / 2 
            //        , (currentPoint.getLongitudeE6() + currentPoint.getLongitudeE6()) / 2));
 
        } catch(Exception e) { 
            Log.d("DirectionMap","Exception parsing kml."); 
        }    	
    }
    


	private NavigationDataSet parsearArchivo(InputStream archivo) throws ParserConfigurationException, SAXException, IOException {
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
        InputSource is = new InputSource(archivo); 
        // ACA SE PARSEA EL KML            
        xmlreader.parse(is); 
        // Retorna los datos generados del parseo en un DATASET
        NavigationDataSet ds = navSaxHandler.getParsedData();
		return ds;
	}



	private void cargarBotones() {
    	btnSatelite = (Button)findViewById(R.id.BtnSatelite);
        btnSatelite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mapView.isSatellite())
					mapView.setSatellite(false);
				else
					mapView.setSatellite(true);
			}
		});

        btnCentrar = (Button)findViewById(R.id.BtnCentrar);
        btnCentrar.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View arg0) {
				GeoPoint loc = DrawService.puntoCentralMap(Parametros.LATITUD_CORDOBA, Parametros.LONGITUD_CORDOBA);
				controlMapa.setCenter(loc);
				controlMapa.setZoom(Parametros.ZOOM_DEFECTO);
			}
		});

        btnAnimar = (Button)findViewById(R.id.BtnAnimar);
        btnAnimar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GeoPoint loc = DrawService.puntoCentralMap(Parametros.LATITUD_CORDOBA, Parametros.LONGITUD_CORDOBA);
				controlMapa.animateTo(loc);
				int zoomActual = mapView.getZoomLevel();
				for(int i=zoomActual; i<10; i++)
				{
					controlMapa.zoomIn();
				}
			}
		});

        btnMover = (Button)findViewById(R.id.BtnMover);
        btnMover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				controlMapa.scrollBy(40, 40);
			}
		});
        
        btnRecorrer = (Button)findViewById(R.id.BtnRecorrer);
        btnRecorrer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				recorrerEstadios();
			}

			private void recorrerEstadios() {
				controlMapa.animateTo(DrawService.puntoCentralMap(Parametros.LATITUD_CORDOBA, Parametros.LONGITUD_CORDOBA));
				int zoomActual = mapView.getZoomLevel();
				for(Overlay overl : mapView.getOverlays()){
					controlMapa.animateTo(((StadiumOverlay) overl).getGp());
					zoomActual = Parametros.ZOOM_DEFECTO;
					for(int i=zoomActual; i<10; i++) controlMapa.zoomIn();
				}
			}
		});
		
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
    
    @Override
    protected boolean isRouteDisplayed() {
    	return true;
    }
    
}
    // and the rest of the methods in activity, e.g. drawPath() etc... 
