package ar.com.sicardi;


import android.util.Log; 
import ar.com.sicardi.myapp; 
import org.xml.sax.Attributes; 
import org.xml.sax.SAXException; 
import org.xml.sax.helpers.DefaultHandler; 

import ar.com.sicardi.NavigationDataSet; 
import ar.com.sicardi.Placemark; 


public class NavigationSaxHandler extends DefaultHandler{ 

// =========================================================== 
// Fields 
// =========================================================== 

private boolean in_kmltag = false; 
private boolean in_placemarktag = false; 
private boolean in_nametag = false; 
private boolean in_descriptiontag = false; 
private boolean in_geometrycollectiontag = false; 
private boolean in_linestringtag = false; 
private boolean in_pointtag = false; 
private boolean in_coordinatestag = false; 
private boolean in_styleUrl = false;

private StringBuffer buffer; 

private NavigationDataSet navigationDataSet = new NavigationDataSet();
private boolean in_style = false;
private boolean in_iconHref =  false; 

// =========================================================== 
// Getter & Setter 
// =========================================================== 

public NavigationDataSet getParsedData() { 
navigationDataSet.getCurrentPlacemark().setCoordinates(buffer.toString().trim()); 
	return this.navigationDataSet; 
} 

// =========================================================== 
// Methods 
// =========================================================== 
@Override 
public void startDocument() throws SAXException { 
	this.navigationDataSet = new NavigationDataSet(); 
} 

@Override 
public void endDocument() throws SAXException { 
	// Nothing to do 
} 

/** Gets be called on opening tags like: 
* <tag> 
* Can provide attribute(s), when xml was like: 
* <tag attribute="attributeValue">*/ 
@Override 
public void startElement(String namespaceURI, String localName, 
String qName, Attributes atts) throws SAXException { 
	if (localName.equals("kml")) { 
		this.in_kmltag = true; 
	} else if (localName.equals("Placemark")) { 
		this.in_placemarktag = true; 
		navigationDataSet.setCurrentPlacemark(new Placemark()); 
	} else if (localName.equals("name")) { 
		this.in_nametag = true; 
	} else if (localName.equals("description")) { 
		this.in_descriptiontag = true; 
	} else if (localName.equals("GeometryCollection")) { 
		this.in_geometrycollectiontag = true; 
	} else if (localName.equals("LineString")) { 
		this.in_linestringtag = true; 
	} else if (localName.equals("point")) { 
		this.in_pointtag = true; 
	} else if (localName.equals("coordinates")) { 
		buffer = new StringBuffer(); 
		this.in_coordinatestag = true; 
    } else if (localName.equals("styleUrl")) { 
    	this.in_styleUrl = true;  
	} else if (localName.equals("Style")) { 
		this.in_style  = true;  
	} else if (localName.equals("href")) { 
		this.in_iconHref = true; 
	}
} 

/** Gets be called on closing tags like: 
* </tag> */ 
@Override 
public void endElement(String namespaceURI, String localName, String qName) 
throws SAXException { 
if (localName.equals("kml")) { 
	this.in_kmltag = false; 
} else if (localName.equals("Placemark")) { 
	this.in_placemarktag = false; 

	if ("Route".equals(navigationDataSet.getCurrentPlacemark().getTitle())) 
	navigationDataSet.setRoutePlacemark(navigationDataSet.getCurrentPlacemark()); 
	else navigationDataSet.addCurrentPlacemark(); 

} else if (localName.equals("name")) { 
	this.in_nametag = false; 
} else if (localName.equals("description")) { 
	this.in_descriptiontag = false; 
} else if (localName.equals("GeometryCollection")) { 
	this.in_geometrycollectiontag = false; 
} else if (localName.equals("LineString")) { 
	this.in_linestringtag = false; 
} else if (localName.equals("point")) { 
	this.in_pointtag = false; 
} else if (localName.equals("coordinates")) { 
	this.in_coordinatestag = false; 
} else if (localName.equals("styleUrl")) { 
	this.in_styleUrl = false; 
} else if (localName.equals("Style")) { 
	this.in_style = false; 
	navigationDataSet.addCurrentStyle(); 
}  else if (localName.equals("href")) { 
	this.in_iconHref = false; 
}  
} 

/** Gets be called on the following structure: 
* <tag>characters</tag> */ 
@Override 
public void characters(char ch[], int start, int length) { 
if(this.in_nametag){ 
	if (navigationDataSet.getCurrentPlacemark()==null) navigationDataSet.setCurrentPlacemark(new Placemark()); 
		navigationDataSet.getCurrentPlacemark().setTitle(new String(ch, start, length)); 
	} else 
		if(this.in_descriptiontag){ 
			if (navigationDataSet.getCurrentPlacemark()==null) navigationDataSet.setCurrentPlacemark(new Placemark()); 
				navigationDataSet.getCurrentPlacemark().setDescription(new String(ch, start, length)); 
} else 
	if(this.in_coordinatestag){ 
		if (navigationDataSet.getCurrentPlacemark()==null) navigationDataSet.setCurrentPlacemark(new Placemark()); 
		    navigationDataSet.getCurrentPlacemark().setCoordinates(new String(ch, start, length)); 
			buffer.append(ch, start, length); 
	} 
	else 
	if(this.in_styleUrl){ 
		if (navigationDataSet.getCurrentPlacemark()==null) navigationDataSet.setCurrentPlacemark(new Placemark()); 
		    navigationDataSet.getCurrentPlacemark().setStyleUrl(new String(ch, start, length).toString()); 
	} 
	else 
		if(this.in_iconHref ){ 
			if (navigationDataSet.getCurrentPlacemark()==null) navigationDataSet.setCurrentPlacemark(new Placemark()); 
			    navigationDataSet.getCurrentStyle().setIconHref(new String(ch, start, length).toString()); 
		} 
} 
}
