package com.tonightstay.DDH.ScratchView;


import com.example.com.tonightstay.ddh.R;
import com.tonightstay.DDH.ScratchView.FastMoveScratchView.OnFastMoveScratchListener;
import com.tonightstay.DDH.ScratchView.ScratchView.OnFilledPercentUpdateListener;
import com.tonightstay.DDH.tools.DeviceTools;
import com.tonightstay.DDH.tools.DeviceTools.ScreenResolution;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ScratchTicketActivity extends Activity  implements 
OnFastMoveScratchListener,
OnFilledPercentUpdateListener{
	ScratchView mScratchView;
	FastMoveScratchView mFastMoveScratchView;
	
	//上面第1個獎項位置佔全螢幕高的比例
	final float startYWeight = 0.15f;
	//上面2個獎項的間距佔上部剩餘高的比例
	final float TopTwoPrizeGapWeight = 0.2f;
	//上面獎項短邊佔全螢幕寬的比例
	final float TopTwoPrizeWidthShortWeight = 0.05f;
	//上面獎項長邊佔全螢幕寬的比例
	final float TopTwoPrizeWidthLongWeight = 0.3f;
	
	final float BottomPrizePaddingLeft = TopTwoPrizeWidthShortWeight;
	final float BottomPrizePaddingRight = TopTwoPrizeWidthShortWeight;
	final float BottomPrizePaddingTop = 0.0f;
	final float BottomPrizePaddingBottom = 0.2f;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // lock screen orientation (stops screen clearing when rotating phone)
        setRequestedOrientation(getResources().getConfiguration().orientation);
       
        ScreenResolution sr = DeviceTools.getScreenResolution(this);
        
        setContentView(R.layout.scratchview);
        
        mScratchView = (ScratchView)findViewById(R.id.ScratchView);
        
        int startY1 = (int) (sr.height*startYWeight);
        int restTopH = sr.height/2 - startY1;
        int topTwoPrizeGap = (int) (restTopH*TopTwoPrizeGapWeight);
        int topPrizeH = (restTopH - topTwoPrizeGap-topTwoPrizeGap)/2;
        
        int startY2 = startY1+topPrizeH+topTwoPrizeGap;
        
        
        int shortWidth = (int) (sr.width*TopTwoPrizeWidthShortWeight);
        int longWidth = (int) (sr.width*TopTwoPrizeWidthLongWeight);
        
        mScratchView.setPrize1Rect(new Rect(longWidth,(int) startY1,sr.width-shortWidth,startY1+topPrizeH));
        mScratchView.setPrize2Rect(new Rect(shortWidth,(int) startY2,sr.width-longWidth,startY2+topPrizeH));
        
        
        
        int bottomPrizeLeft = (int) (sr.width*BottomPrizePaddingLeft);
        int bottomPrizeRight = (int) (sr.width - sr.width*BottomPrizePaddingRight);
        
        int bottomPrizeTop = (int) ((sr.height/2)*BottomPrizePaddingTop+sr.height/2);
        int bottomPrizeBottom = (int) (sr.height - (sr.height/2)*BottomPrizePaddingBottom);
        
        mScratchView.setPrize3Rect(new Rect(bottomPrizeLeft,bottomPrizeTop,bottomPrizeRight,bottomPrizeBottom));
        
        mScratchView.setPrizeMessage("Get it11", "Good", new String[]{"10000","free","3545","4123","5232","6123","7123","812321","9123"});
        mScratchView.setOnFilledPercentUpdateListener(this);
    	
        mFastMoveScratchView = (FastMoveScratchView)findViewById(R.id.FastMoveScratchView);
        
        mFastMoveScratchView.setOnFastMoveScratchListener(this);
        
        
       // mFastMoveScratchView.setVisibility(View.INVISIBLE);

    }
    
	@Override
	public void OnPickScratchFinish() {
		
		mFastMoveScratchView.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onShowAllPrize() {
		Log.e("debug","onShowAllPrize");
		
	}
}
