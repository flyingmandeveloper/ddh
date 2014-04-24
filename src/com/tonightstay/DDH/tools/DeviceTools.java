package com.tonightstay.DDH.tools;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;


public class DeviceTools {
	
	public static class ScreenResolution
	{
		public int width = 0;
		public int height = 0;
		public float widthDP = 0.0f;
		public float heightDP = 0.0f;
		public float density = 0.0f;
	}
	
	public static ScreenResolution getScreenResolution(Activity activity) {
		if (activity ==null)
			return null;
		
		ScreenResolution sr = new ScreenResolution();
		Display display = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		//display.getWidth() is deprecated
		//display.getHeight() is deprecated
		sr.density = activity.getResources().getDisplayMetrics().density;
		sr.width = outMetrics.widthPixels;
		sr.height = outMetrics.heightPixels;
		sr.widthDP = outMetrics.widthPixels / sr.density;
		sr.heightDP = outMetrics.heightPixels / sr.density;
		
		return sr;
	}
	
	public static int getPixelFromDip(Context context, int dip) {
		if (context == null)
			return 0;

		Resources r = context.getResources();

		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, r.getDisplayMetrics());
	}

	public static int convertDensityPixel(Context context, int dip) {
		if (context == null)
			return 0;
		else
			return (int) (dip * context.getResources().getDisplayMetrics().density);
	}

	public static int getScreenOrientation(Activity activity)
	{
		if (activity == null)
			return Configuration.ORIENTATION_UNDEFINED;
		
	    Display getOrient = activity.getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}
	
	public static  InputMethodManager getInputMethodManager(Activity activity)
	{
		if (activity == null)
			return null;
		return (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	public static void logHeap(String st) {
        Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576));
        Double available = new Double(Debug.getNativeHeapSize())/1048576.0;
        Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        
        Double totalM = new Double(Runtime.getRuntime().totalMemory())/new Double((1048576));
        Double freeM = new Double(Runtime.getRuntime().freeMemory())/new Double((1048576));
        Double maxM = new Double(Runtime.getRuntime().maxMemory())/new Double((1048576));
        
        Log.d("debug", "debug. ==============="+st+"==================");
        Log.d("debug", "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
        Log.d("debug", "debug.memory: allocated: " + df.format(totalM) + "/"+ df.format(maxM) +" MB "+" used: "+df.format(totalM-freeM)+"MB Free: " + df.format(freeM) +"MB");
        Log.d("debug", "total: "+(allocated+(totalM-freeM)));
        
    }
}
