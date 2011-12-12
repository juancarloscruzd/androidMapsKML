package ar.com.sicardi;

import android.app.ProgressDialog;

import com.google.android.maps.MapActivity;

public class Factorythreads {
	private ProgressDialog pdWaitingPopUp;
	
	public void abriendoPopUpProcessDialog(ProgressDialog pd, MapActivity activity,String title, String text, boolean indeterminate, boolean canceleable){
		pd = ProgressDialog.show(activity, title, text, true, false);
		pdWaitingPopUp = pd;
	}
	
	public void cerrandoPopUpProcessDialog(){
		pdWaitingPopUp.dismiss();
	}
	
	public ThreadBlocking getHiloPopUpProcessDialog(){
		ThreadBlocking hilo = new ThreadBlocking();
		hilo.setFactoryT(this);
		return hilo;
	}

}
