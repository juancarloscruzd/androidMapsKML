package ar.com.sicardi;

public class Style {
	
	String iconHref;
	private String idAtributte;

	public String getIconHref() {
		return iconHref;
	}

	public void setIconHref(String iconHref) {
		this.iconHref = iconHref;
	}

	public void setIdAtributte(String id) {
		this.idAtributte = id;
	}

	public String getIdAtributte() {
		return idAtributte;
	}

	public boolean isIdStyle(String idStyle) {
		return (this.idAtributte.equals(idStyle.substring(1, idStyle.length())));
	}

}
