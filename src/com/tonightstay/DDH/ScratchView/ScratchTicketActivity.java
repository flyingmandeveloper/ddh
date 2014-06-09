package com.tonightstay.DDH.ScratchView;

import java.io.InputStream;
import java.util.ArrayList;

import com.example.com.tonightstay.ddh.R;
import com.tonightstay.DDH.ScratchView.FastMoveScratchView.OnFastMoveScratchListener;
import com.tonightstay.DDH.ScratchView.ScratchView.OnFilledPercentUpdateListener;

import flyingman.utility.DeviceTool.DeviceTools;
import flyingman.utility.DeviceTool.DeviceTools.ScreenResolution;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScratchTicketActivity extends Activity  implements 
OnFastMoveScratchListener,
OnFilledPercentUpdateListener{
	ScratchView mScratchView;
	FastMoveScratchView mFastMoveScratchView;

	
	final float topWeight = 1.0f;
	final float bottomWeight = 2.0f;
	final float totalWeight = topWeight+bottomWeight;
	
	//上面第1個獎項位置佔全螢幕高的比例
	final float startYWeight = 0.15f;
	//上面2個獎項的間距佔上部剩餘高的比例
	final float TopTwoPrizeGapWeight = 0.15f;
	//上面獎項短邊佔全螢幕寬的比例
	final float TopTwoPrizeWidthShortWeight = 0.05f;
	//上面獎項長邊佔全螢幕寬的比例
	final float TopTwoPrizeWidthLongWeight = 0.3f;
	
	final float BottomPrizePaddingLeft = TopTwoPrizeWidthShortWeight;
	final float BottomPrizePaddingRight = TopTwoPrizeWidthShortWeight;
	final float BottomPrizePaddingTop = 0.0f;
	final float BottomPrizePaddingBottom = 0.35f;
	
	Button mScratchBtn;
	RelativeLayout mStartPage;
	RelativeLayout mScratchResultGetit;
	RelativeLayout mScratchResultNothing;
	ImageView mScratchExit;
	
	TextView mResultMsg1;
	TextView mResultMsg2;
	TextView mResultMsg3;
	TextView mResultMsg4;
	
	TextView mInfoLang1;
	TextView mInfoLang2;
	
	LinearLayout mInfolayout;
	
	TextView mDDHRule;
	final int mIconCount = 9;
//	BitmapDrawable[] ddhSmallPicture = new BitmapDrawable[mIconCount];
	Bitmap[] ddhSmallPicture = new Bitmap[mIconCount];
	
	int ddhSmallPictureSize = 0;
	
	int mBingo = 0;
	int mTypePrize = 0;
	int mResultPrize = -1;
	
	public final static String SB_TAKE_IT = "com.tonightstay.DDH.ScratchView.takeit";
	
	final int mBGCount = 6;
   // BitmapDrawable[] tBgImg = new BitmapDrawable[mBGCount];
	Bitmap[] tBgImg = new Bitmap[mBGCount];
    
	
	public void onEndScratch()
	{
		finish();
	};
	
	public void onExitScratch()
	{
		finish();
	};
	
	public void onGiveUp()
	{
		finish();
	};
	
	public void onTakeIt()
	{
		
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // lock screen orientation (stops screen clearing when rotating phone)
        setRequestedOrientation(getResources().getConfiguration().orientation);
        
        initUI();

    }
    
    public boolean repeat_click(final View v, double sec){
		if(!v.isClickable()) return true;
		v.setClickable(false);
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				v.setClickable(true);
			}
		},(long) (sec*1000));
		return false;
	}
    
    
    
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        
        Log.e("debug","onDestroy");
        
        for (int i =0;i<mBGCount;++i)
        {
            tBgImg[i].recycle();
            tBgImg[i] = null;
        }
        
        for (int i =0;i<mIconCount;++i)
        {
            ddhSmallPicture[i].recycle();
            ddhSmallPicture[i] = null;
        }
        
        
        
        
        mFastMoveScratchView = null;
        mScratchView = null;
        
    }
    
    Bitmap loadImageFromResource(int resId)
    {
        return loadImageFromResource(resId,1);
    }
    
    Bitmap loadImageFromResource(int resId,int inSampleSize)
    {
        InputStream is = this.getResources().openRawResource(resId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
     // width，height設為原來的1/inSampleSize
        options.inSampleSize = inSampleSize;   
        return BitmapFactory.decodeStream(is, null, options);
    }

    void initUI()
    {
    	ddhSmallPictureSize = DeviceTools.getPixelFromDip(this, 50);
    	
    	Log.e("debug","initUI");
    	
    	
        ddhSmallPicture[0] = loadImageFromResource(R.drawable.ddh_p1);
        ddhSmallPicture[1] = loadImageFromResource(R.drawable.ddh_p2);
        ddhSmallPicture[2] = loadImageFromResource(R.drawable.ddh_p3);
        ddhSmallPicture[3] = loadImageFromResource(R.drawable.ddh_p4);
        ddhSmallPicture[4] = loadImageFromResource(R.drawable.ddh_p5);
        ddhSmallPicture[5] = loadImageFromResource(R.drawable.ddh_p6);
        ddhSmallPicture[6] = loadImageFromResource(R.drawable.ddh_p7);
        ddhSmallPicture[7] = loadImageFromResource(R.drawable.ddh_p8);
        ddhSmallPicture[8] = loadImageFromResource(R.drawable.ddh_p9);
        
        
        ScreenResolution sr = DeviceTools.getScreenResolution(this);
        

        mScratchView = new ScratchView(this);
        addContentView(mScratchView,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        mScratchView.setVisibility(View.INVISIBLE);
        mFastMoveScratchView = new FastMoveScratchView(this);
    	addContentView(mFastMoveScratchView,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
    	
    	LayoutInflater inflater = LayoutInflater.from(this);
    	final View startConver = inflater.inflate(R.layout.scratchview, null);
        addContentView(startConver,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        
        mStartPage = (RelativeLayout) startConver.findViewById(R.id.StartPage);
        
        
        mScratchBtn = (Button)startConver.findViewById(R.id.start_scratch_btn);
        
        mScratchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mStartPage.setVisibility(View.INVISIBLE);
				mFastMoveScratchView.startScratch();
				
			}
		});
        
        mScratchResultGetit = (RelativeLayout) startConver.findViewById(R.id.scratch_result_getit);
        mScratchResultGetit.setVisibility(View.INVISIBLE);
        
        mScratchResultNothing = (RelativeLayout) startConver.findViewById(R.id.scratch_result_nothing);
        mScratchResultNothing.setVisibility(View.INVISIBLE);
        
    	mResultMsg1 = (TextView) startConver.findViewById(R.id.result_msg1);
    	mResultMsg2 = (TextView) startConver.findViewById(R.id.result_msg2);
    	mResultMsg3 = (TextView) startConver.findViewById(R.id.result_msg3);
    	mResultMsg4 = (TextView) startConver.findViewById(R.id.result_msg4);
    	
    	mInfolayout = (LinearLayout) startConver.findViewById(R.id.infolayout);
    	mInfolayout.setVisibility(View.INVISIBLE);
    	mInfoLang1 =  (TextView) startConver.findViewById(R.id.infoLang1);
    	mInfoLang2 =  (TextView) startConver.findViewById(R.id.infoLang2);
    	
    	
    	
    	
        mScratchExit =  (ImageView)startConver.findViewById(R.id.scratch_exit);
        mScratchExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onExitScratch();
			}
		});
        
        Button giveUpBtn = (Button)startConver.findViewById(R.id.giveup);
        giveUpBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onGiveUp();
			}
		});
        
        Button takeItBtn = (Button)startConver.findViewById(R.id.takeit);
        takeItBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (repeat_click(v, 5))	return ;
				onTakeIt();

			}
		});
        
        Button closeBtn = (Button)startConver.findViewById(R.id.ddh_close);
        closeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onEndScratch();
			}
		});
        
        
        int startY1 = (int) (sr.height*startYWeight);
        int restTopH = (int) (sr.height*topWeight/totalWeight - startY1);
        int topTwoPrizeGap = (int) (restTopH*TopTwoPrizeGapWeight);
        int topPrizeH = (restTopH - topTwoPrizeGap-topTwoPrizeGap)/2;
        
        int startY2 = startY1+topPrizeH+topTwoPrizeGap;
        
        
        int shortWidth = (int) (sr.width*TopTwoPrizeWidthShortWeight);
        int longWidth = (int) (sr.width*TopTwoPrizeWidthLongWeight);
        
        mScratchView.setPrize1Rect(new Rect(longWidth,(int) startY1,sr.width-shortWidth,startY1+topPrizeH));
        mScratchView.setPrize2Rect(new Rect(shortWidth,(int) startY2,sr.width-longWidth,startY2+topPrizeH));
        
        
        
        int bottomPrizeLeft = (int) (sr.width*BottomPrizePaddingLeft);
        int bottomPrizeRight = (int) (sr.width - sr.width*BottomPrizePaddingRight);
        
        int bottomPrizeTop = (int) ((sr.height*bottomWeight/totalWeight)*BottomPrizePaddingTop+(sr.height*topWeight/totalWeight));
        int bottomPrizeBottom = (int) (sr.height - (sr.height*bottomWeight/totalWeight)*BottomPrizePaddingBottom);
        
        mScratchView.setPrize3Rect(new Rect(bottomPrizeLeft,bottomPrizeTop,bottomPrizeRight,bottomPrizeBottom),ddhSmallPictureSize);
        
        mDDHRule = (TextView) startConver.findViewById(R.id.ddh_rule);
        
        if (mDDHRule!=null)
        {
	        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mDDHRule.getLayoutParams();
	        
	        lp.setMargins(0, (int) (bottomPrizeBottom+mDDHRule.getTextSize()*2), 0, 0);
	        mDDHRule.setLayoutParams(lp);
	        mDDHRule.setVisibility(View.INVISIBLE);
        }
        else {
			Log.e("debug","mDDHRule==null");
		}
        
        Intent i = getIntent();
        
        int ticket1 = i.getIntExtra("FastMovePage1", 1);
        int ticket2 = i.getIntExtra("FastMovePage2", 2);
        int ticket3 = i.getIntExtra("FastMovePage3", 3);
        int scratchTicket = i.getIntExtra("ScatchTicket", 4);
        
        String[] infoLang1Arr = getResources().getStringArray(R.array.ddh_info_lang1);
        String[] infoLang2Arr = getResources().getStringArray(R.array.ddh_info_lang2);
        
        int infoIdx = scratchTicket-1;
        if (infoLang1Arr!=null && infoIdx<infoLang1Arr.length && infoIdx>=0)
            mInfoLang1.setText(infoLang1Arr[infoIdx]);
        else
            mInfoLang1.setText(null);
        
        
        if (infoLang2Arr!=null && infoIdx<infoLang2Arr.length && infoIdx>=0)
            mInfoLang2.setText(infoLang2Arr[infoIdx]);
        else
            mInfoLang2.setText(null);
        
        mBingo = i.getIntExtra("Bingo", 0);
        mTypePrize = i.getIntExtra("ScatchType", 0);
        mResultPrize = i.getIntExtra("ScatchPrice", 0);
        
                
        if (mBingo > 0)
        {
    	 	String startDateFmt = getString(R.string.get_ddh_ticket_startdate_fmt);
    		String expireDateFmt = getString(R.string.get_ddh_ticket_expiredate_fmt);
    		String priceFmt = null;
        	mResultMsg1.setText(i.getStringExtra("ScatchMsg1"));

        	if (mTypePrize == ScratchTicketActivity.CashTypePrize)
        		priceFmt = getString(R.string.get_ddh_ticket_price_fmt);
        	else if (mTypePrize == ScratchTicketActivity.FreeTicketTypePrize)
        		priceFmt = getString(R.string.get_ddh_ticket_count_fmt);

        	
    		mResultMsg2.setText(String.format("%s"+priceFmt, i.getStringExtra("ScatchMsg2"),mResultPrize));
    		mResultMsg3.setText(String.format(startDateFmt, i.getStringExtra("Startdate")));
        	mResultMsg4.setText(String.format(expireDateFmt, i.getStringExtra("Expiredate")));
        }
     
     	Bitmap[] pricesImg = getRandomImage(mBingo);
        String msg2 = null;
     	if (mTypePrize == CashTypePrize)
     		msg2 = String.format(getString(R.string.ddh_cash_title), i.getStringExtra("ScatchMsg2"),mResultPrize);
     	else if (mTypePrize == FreeTicketTypePrize)
     		msg2 = String.format(getString(R.string.ddh_free_title), i.getStringExtra("ScatchMsg2"),mResultPrize);
        
        mScratchView.setPrizeMessage(i.getStringExtra("ScatchMsg1"),msg2, pricesImg);
        mScratchView.setOnFilledPercentUpdateListener(this);
        mScratchView.setStrokeWidth(DeviceTools.getPixelFromDip(this,5));
    	
        
        mFastMoveScratchView.setOnFastMoveScratchListener(this);
        
        
        int ticket1Id = getResources().getIdentifier("scratch_default_"+ticket1, "drawable", getPackageName());
        int ticket2Id = getResources().getIdentifier("scratch_default_"+ticket2, "drawable", getPackageName());
        int ticket3Id = getResources().getIdentifier("scratch_default_"+ticket3, "drawable", getPackageName());
        int ticket4Id = getResources().getIdentifier("scratch_default_"+scratchTicket, "drawable", getPackageName());
        
        
          tBgImg[0] = loadImageFromResource(R.drawable.scratch_logo);
          tBgImg[1] = loadImageFromResource(ticket1Id,2);
          tBgImg[2] = loadImageFromResource(ticket2Id,2);
          tBgImg[3] = loadImageFromResource(ticket3Id,2);
          tBgImg[4] = loadImageFromResource(ticket4Id,2);        
          tBgImg[5] = loadImageFromResource(ticket4Id);
        
        
        FrameLayout.LayoutParams exitLP = (FrameLayout.LayoutParams) mScratchExit.getLayoutParams();
        
        
        mFastMoveScratchView.setStratchTicket(
        		exitLP.width+exitLP.leftMargin+exitLP.rightMargin,
        		exitLP.topMargin,
        		tBgImg[0],
        		tBgImg[1],
        		tBgImg[2],
        		tBgImg[3],
        		tBgImg[4]);
        
        mScratchView.setScratchImage(
        		exitLP.width+exitLP.leftMargin+exitLP.rightMargin,
        		exitLP.topMargin,
        		tBgImg[0],tBgImg[5]);
        
 
    }
    
    public void showScrathTicket()
    {
        mFastMoveScratchView.setVisibility(View.INVISIBLE);  
        mScratchView.setVisibility(View.VISIBLE); 	
        mInfolayout.setVisibility(View.VISIBLE);
    }
    
    public boolean isCoverBitmapNull()
    {
        return mScratchView == null?true:mScratchView.isCoverBitmapNull();
    }
    
    int[] fixPriceArr = new int[]{50,100};
    
    public static final int CashTypePrize = 1;
    public static final int FreeTicketTypePrize = 2;
    
    String[] getRandomPrice(int type, int price)
    {
    	String[] ret = new String[mIconCount];
    	
		int randomPrice = 0;
		
		if (type == CashTypePrize)
		{
			
	    	if (price > 0)
	    	{
	    		for (int i = 0;i<3;++i)
	    			ret[i] = String.valueOf(price);
	    		
	    		for (int i = 3;i<mIconCount;++i)
	    		{
	    			int idx = (int) (Math.random()*fixPriceArr.length);
	    			randomPrice+=fixPriceArr[idx];
	    			if (randomPrice == price)
	    				randomPrice+=fixPriceArr[idx];
	    			
	    			ret[i] = String.valueOf(randomPrice);
	    			
	    			if ( (int) (Math.random()*2) == 0 && i<mIconCount-1)
	    			{
	    				ret[i+1]=ret[i];
	    				++i;
	    			}
	    		}
	    	}
	    	else
	    	{
	    		for (int i = 0;i<mIconCount;++i)
	    		{
	    			int idx = (int) (Math.random()*fixPriceArr.length);
	    			randomPrice+=fixPriceArr[idx];
	      			
	    			ret[i] = String.valueOf(randomPrice);
	    			
	    			if ( (int) (Math.random()*2) == 0 && i<mIconCount-1)
	    			{
	    				ret[i+1]=ret[i];
	    				++i;
	    			}
	    		}
	    	}
		}
		else if (type == FreeTicketTypePrize)
		{
			if (price > 0)
	    	{
	    		for (int i = 0;i<3;++i)
	    		{
	    			ret[i] = getString(R.string.get_ddh_ticket_free);
	    		}
	    		

	    		for (int i = 3;i<mIconCount;++i)
	    		{
	    			int idx = (int) (Math.random()*fixPriceArr.length);
	    			randomPrice+=fixPriceArr[idx];
	    			if (randomPrice == price)
	    				randomPrice+=fixPriceArr[idx];
	    			
	    			ret[i] = String.valueOf(randomPrice);
	    			
	    			if ( (int) (Math.random()*2) == 0 && i<mIconCount-1)
	    			{
	    				ret[i+1]=ret[i];
	    				++i;
	    			}
	    		}
	    	}
	       	else
	    	{

	       		int freeCount = (int) (Math.random()*3);
	       		for (int i = 0;i<freeCount;++i)
	       		{
	       			ret[i]  =  getString(R.string.get_ddh_ticket_free);
	       		}
	       		
	    		for (int i = freeCount;i<mIconCount;++i)
	    		{
	    			int idx = (int) (Math.random()*fixPriceArr.length);
	    			randomPrice+=fixPriceArr[idx];
	    			if (randomPrice == price)
	    				randomPrice+=fixPriceArr[idx];
	    			
	    			ret[i] = String.valueOf(randomPrice);
	    			
	    			if ( (int) (Math.random()*2) == 0 && i<mIconCount-1)
	    			{
	    				ret[i+1]=ret[i];
	    				++i;
	    			}
	    		}
	    	}
		}
		
    	
    	
    	
    	int idx1 = 0;
		int idx2 = 0;
		String swap;
		
		for (int i=0;i<50;++i)
		{
			idx1 = (int) (Math.random()*ret.length);
			idx2= (int) (Math.random()*ret.length);
			swap = ret[idx1];
			ret[idx1] = ret[idx2];
			ret[idx2] = swap;

		}
		
    	
    	return ret;
    }
    
    Bitmap[] getRandomImage( int bingo)
    {

    	Bitmap[] ret = new Bitmap[mIconCount];
    	
		
		ArrayList<Integer> resId = new ArrayList<Integer>();
		for (int i = 0;i<mIconCount;++i)
			resId.add(Integer.valueOf(i));
		

		if (bingo > 0)
        {
            int getItIdx = (int) (Math.random()*mIconCount);
            int picIdx = resId.get(getItIdx);
            resId.remove(getItIdx);
            
            
            for (int i = 0;i<3;++i)
                ret[i] =  ddhSmallPicture[picIdx];
            
            for (int i = 3;i<mIconCount;++i)
            {
                int rIdx = (int) (Math.random()*resId.size());
                picIdx = resId.get(rIdx);
                if ( (int) (Math.random()*2) == 0 && i<mIconCount-1)
                {
                    ret[i] =  ddhSmallPicture[picIdx];
                    ++i;
                    ret[i] =  ddhSmallPicture[picIdx];
                    resId.remove(rIdx);
                }
                else
                {
                    ret[i] =  ddhSmallPicture[picIdx];
                    resId.remove(rIdx);
                }
            }
        }
        else
        {
            for (int i = 0;i<mIconCount;++i)
            {
                int  rIdx= (int) (Math.random()*resId.size());
                
                Integer picIdx = resId.get(rIdx);
                
                if ( (int) (Math.random()*2) == 0 && i<mIconCount-1)
                {
                    ret[i] =  ddhSmallPicture[picIdx];
                    ++i;
                    ret[i] =  ddhSmallPicture[picIdx];
                    resId.remove(rIdx);
                }
                else
                {
                    ret[i] =  ddhSmallPicture[picIdx];
                    resId.remove(rIdx);
                }
            }
        }
    	int idx1 = 0;
		int idx2 = 0;
		Bitmap swap;
		
		for (int i=0;i<50;++i)
		{
			idx1 = (int) (Math.random()*ret.length);
			idx2= (int) (Math.random()*ret.length);
			swap = ret[idx1];
			ret[idx1] = ret[idx2];
			ret[idx2] = swap;

		}
		
    	
    	return ret;
    }
    
	@Override
	public void OnPickScratchFinish() {
		
		mScratchView.setVisibility(View.VISIBLE);
        mInfolayout.setVisibility(View.VISIBLE);
		mDDHRule.setVisibility(View.VISIBLE);
		mScratchView.post(new Runnable() {
			
			@Override
			public void run() {
				mFastMoveScratchView.setVisibility(View.INVISIBLE);
				
			}
		});
		
		
	}

	@Override
	public void onShowAllPrize() {

		if (mBingo > 0)
		{
			mScratchResultGetit.setVisibility(View.VISIBLE);
			mScratchResultGetit.requestLayout();
		}
		else
		{
			mScratchResultNothing.setVisibility(View.VISIBLE);
			mScratchResultNothing.requestLayout();
		}

		mScratchExit.setVisibility(View.INVISIBLE);
		mScratchView.setShowScratchCover(false);
		
		

	}
}
