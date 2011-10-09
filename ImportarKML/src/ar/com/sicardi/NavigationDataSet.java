package ar.com.sicardi;

import java.util.ArrayList;
import java.util.Iterator;


public class NavigationDataSet { 

private ArrayList<Placemark> placemarks = new ArrayList<Placemark>(); 
private ArrayList<Style> styles = new ArrayList<Style>(); 
private Placemark currentPlacemark; 
private Placemark routePlacemark;
private Style currentStyle; 

public ArrayList<Style> getStyles() {
	return styles;
}

public void setStyles(ArrayList<Style> styles) {
	this.styles = styles;
}

public void setCurrentStyle(Style currentStyle) {
	this.currentStyle = currentStyle;
}

public String toString() { 
String s= ""; 
for (Iterator<Placemark> iter=placemarks.iterator();iter.hasNext();) { 
	Placemark p = (Placemark)iter.next(); 
	s += p.getTitle() + "\n" + p.getDescription() + "\n\n"; 
} 
return s; 
} 

public void addCurrentPlacemark() { 
	placemarks.add(currentPlacemark); 
} 

public ArrayList<Placemark> getPlacemarks() { 
	return placemarks; 
} 

public void setPlacemarks(ArrayList<Placemark> placemarks) { 
	this.placemarks = placemarks; 
} 

public Placemark getCurrentPlacemark() { 
	return currentPlacemark; 
} 

public void setCurrentPlacemark(Placemark currentPlacemark) { 
	this.currentPlacemark = currentPlacemark; 
} 

public Placemark getRoutePlacemark() { 
	return routePlacemark; 
}
 
public void setRoutePlacemark(Placemark routePlacemark) {     
	this.routePlacemark = routePlacemark; }

public Style getCurrentStyle() {
	return currentStyle;
}

public void addCurrentStyle() {
	styles.add(currentStyle); 
}

public Style getStyleById(String idStyle) {
	for(Style styleTmp : this.styles)  {
		if(styleTmp.isIdStyle(idStyle)) return styleTmp;
	} 
	return null;
}

}