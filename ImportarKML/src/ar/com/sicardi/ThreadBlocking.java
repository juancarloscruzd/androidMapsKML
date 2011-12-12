package ar.com.sicardi;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ThreadBlocking extends Thread {
	
	public Handler showAdressResults;
    public Factorythreads myFactory;
    public void setFactoryT(Factorythreads myFactor){
    	this.myFactory = myFactor;
    };
    
	public void run(){  
		Looper.prepare();
		showAdressResults = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	        	myFactory.cerrandoPopUpProcessDialog();
	        }
	    };	
	    try {
			Thread.sleep(1500);
		} catch (Exception e) {
			// @todo: Show error message
		}
		showAdressResults.sendEmptyMessage(0);		
		  Looper.loop();
	}
};

