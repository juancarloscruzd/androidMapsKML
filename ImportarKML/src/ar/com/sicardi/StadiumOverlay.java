package ar.com.sicardi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
 
public class StadiumOverlay extends Overlay {  
 
private GeoPoint gp; 
private int mRadius=6; 
private int mode=0; 
private int defaultColor; 
private String text=""; 
private Bitmap img = null;
private String equipo; 
 
 
public StadiumOverlay(GeoPoint gp1,int mode, int defaultColor, String texTo, String urlIcon, long modoArchivo) {
	int slashIndex = urlIcon.lastIndexOf('/');
	this.equipo = urlIcon.substring(slashIndex + 1);
	if(modoArchivo==1L) this.img =  getBitmapFromFile(this.equipo);
	else {
		this.img = getBitmapFromURL(urlIcon);
        setImagen(this.img,this.equipo);
	}
    this.gp = gp1;
    this.text = texTo;
    this.mode = mode; 
    this.defaultColor = defaultColor; 
    this.text = " ";
} 
 
private Bitmap getBitmapFromFile(String filename) {
	FileInputStream is;
    File f = null;
    Bitmap bitmap = null;
    f = new File(Parametros.PATH_MEMORIA+filename);
    if (f.canRead()) {
      Log.d("FileStorage", "reading file " + f.getAbsolutePath() + " - "
          + filename);
    } else {
      Log.d("FileStorage", "Can't read file " + filename);
    }
  try {
      is = new FileInputStream(f);
      if (is != null && is.available() > 0) {
		        bitmap = BitmapFactory.decodeStream(is);
	  } else {
		        Log.w("soFurryApp", "Can't load from external storage");
	  }
	} catch (Exception e) {
		      Log.e("soFurryApp", "error in loadIcon", e);
   }
   return bitmap;
}

public void setText(String t) { 
    this.text = t; 
} 
 
public void setBitmap(Bitmap bitmap) {  
    this.img = bitmap; 
} 
 
public int getMode() { 
    return mode; 
} 
 
@Override 
public boolean draw (Canvas canvas, MapView mapView, boolean shadow, long when) { 
    Projection projection = mapView.getProjection(); 
    if (shadow == false) { 
        Paint paint = new Paint(); 
        paint.setAntiAlias(true); 
        Point point = new Point(); 
        projection.toPixels(gp, point); 
        // mode=1 -> unico modo, ya habra mas
        if(mode==1) { 
            if(defaultColor==999) {
            	paint.setColor(Color.BLACK); // Color.BLUE
	            paint.setColor(defaultColor); 
	            RectF oval=new RectF(point.x - mRadius, point.y - mRadius, 
	            point.x + mRadius, point.y + mRadius); 
	            canvas.drawOval(oval, paint); } 
            else {
                //Definimos el pincel de dibujo
                Paint p = new Paint();
                p.setColor(defaultColor);
                

            	if(this.img == null)   {
                	//Marca Ejemplo 2: Bitmap
                	Bitmap bm = BitmapFactory.decodeResource(
                	        mapView.getResources(),
                	        R.drawable.marcador_google_maps);
                	this.img = bm;
            	}
            	else{
            		this.img = Bitmap.createScaledBitmap(this.img, 25, 25, false); 
            	}
            	//canvas.drawBitmap(this.img, point.x - this.img.getWidth(),point.y - this.img.getHeight(), p);
            	canvas.drawBitmap(this.img, point.x,point.y , p);
            }
        } 
    } 
    return super.draw(canvas, mapView, shadow, when); 
} 

public static Bitmap getBitmapFromURL(String src) {
  try {
		URL url = new URL(src);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.connect();
		InputStream input = connection.getInputStream();
		Bitmap myBitmap = BitmapFactory.decodeStream(input);
		return myBitmap;
  } catch (IOException e) {
			e.printStackTrace();
		return null;
	}
}

public void setImagen(Bitmap avatar,String nombre) {
    
    try {
        FileOutputStream out=new FileOutputStream(Parametros.PATH_MEMORIA+nombre);
        avatar.compress(Bitmap.CompressFormat.PNG, 90, out);
        try {
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    finally {
    	
    }
}

public GeoPoint getGp(){
   return this.gp;	
}

@Override
public boolean onTap(GeoPoint point, MapView mapView) 
{
	Context contexto = mapView.getContext();
	String msg = "Lat: " + point.getLatitudeE6()/1E6 + " - " + 
	             "Lon: " + point.getLongitudeE6()/1E6;
	
	Toast toast = Toast.makeText(contexto, msg, Toast.LENGTH_SHORT);
	toast.show();
	
	return true;
}
    
} 

