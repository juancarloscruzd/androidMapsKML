package ar.com.sicardi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.List;

import android.location.Location;
import android.util.Log;

public class FileHandler {

	public static InputStream recuperarArchivo(String string, String urls, DirectionMapActivity directionMapActivity) throws IOException {
     	 StringBuilder urlString = new StringBuilder(); 
        //urlString.append("http://maps.google.com/maps?f=d&hl=es&saddr=-34.9134721,-57.9614658&daddr=-34.9134721,-57.9614658&ie=UTF8&0&om=0&output=kml");
     	 InputStream archivo = null;
     	try
     	{   
     		archivo  = new FileInputStream(string); 
     		directionMapActivity.setModoLocalOURL(1L);
     	}
     	catch (Exception ex)
     	{
     	    Log.e("Ficheros", "Error al leer fichero desde memoria interna");
       	//urlString
           urlString.append(urls);
           //setea la url
           URL url = new URL(urlString.toString());
           archivo = url.openStream();
           OutputStreamWriter fout=
               new OutputStreamWriter(castInputStreamToFileOutputStream(string,archivo));
           fout.close();
           directionMapActivity.setModoLocalOURL(0L);
     	}
       return archivo;

	}

	public static FileOutputStream castInputStreamToFileOutputStream(String string, InputStream archivo) {
		FileOutputStream out = null; 
		try {
			  
		     out = new FileOutputStream(string);
			 int read = 0;
			 byte[] bytes = new byte[1024];
			 while ((read = archivo.read(bytes)) != -1) {
					out.write(bytes, 0, read);
			 }
			 out.flush();
			 out.close();
		     System.out.println("New file created!");
			} 
		   catch (IOException e) {
				System.out.println(e.getMessage());
	     }
		return out;
	}
	
    public static StringBuilder tomandoKMZdesdeMaps(Location lastKnownLocation,
			List<Double> destD) {
    	StringBuilder urlString = new StringBuilder(); 
        urlString.append("http://maps.google.com/maps?f=d&hl=en"); 
        urlString.append("&saddr=");//from 
        urlString.append( Double.toString(lastKnownLocation.getLatitude() )); 
        urlString.append(","); 
        urlString.append( Double.toString(lastKnownLocation.getLongitude() )); 
        urlString.append("&daddr=");//to 
        urlString.append( Double.toString(destD.get(0).doubleValue())); 
        urlString.append(","); 
        urlString.append( Double.toString(destD.get(1).doubleValue())); 
        urlString.append("&ie=UTF8&0&om=0&output=kml"); 
		return urlString;
	}
}
