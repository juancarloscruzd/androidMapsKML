package ar.com.sicardi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap; 
import android.graphics.BitmapFactory;
import android.graphics.Canvas; 
import android.graphics.Color; 
import android.graphics.Paint; 
import android.graphics.Point; 
import android.graphics.RectF; 
 
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
 
 
public StadiumOverlay(GeoPoint gp1,int mode, int defaultColor, String texTo, String urlIcon) { 
    this.gp = gp;
    this.text = texTo;
    this.mode = mode; 
    this.defaultColor = defaultColor; 
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
                
            	//Marca Ejemplo 2: Bitmap
            	Bitmap bm = BitmapFactory.decodeResource(
            	        mapView.getResources(),
            	        R.drawable.marcador_google_maps);
            	 
            	canvas.drawBitmap(bm, point.x - bm.getWidth(),
            	        point.y - bm.getHeight(), p);
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
 
} 

