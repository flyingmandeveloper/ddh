package com.tonightstay.DDH.ScratchView;

import java.io.InputStream;
import java.util.ArrayList;

import com.example.com.tonightstay.ddh.R;
import com.tonightstay.DDH.ScratchView.FastMoveScratchView.OnFastMoveScratchListener;
import com.tonightstay.DDH.ScratchView.ScratchView.OnFilledPercentUpdateListener;

import flyingman.utility.DeviceTool.DeviceTools;
import flyingman.utility.DeviceTool.DeviceTools.ScreenResolution;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class BaseScratchTicketActivity extends Activity  implements 
OnFastMoveScratchListener,
OnFilledPercentUpdateListener{
    
    
    public static final int LOADING = 1;                //等待api 呼叫回應
    public static final int GET_IT_NOT_LOGIN = 2;       //中獎後還沒登入的狀態
    public static final int GET_IT_NOT_AUTH = 3;        //中獎後還沒認證的狀態
    public static final int GET_IT_LOGINED_AUTHED = 4;  //中獎後已登入已認證
    public static final int NOTHING = 5;                //沒有中獎
    
    
	ScratchView mScratchView;
	FastMoveScratchView mFastMoveScratchView;
	
	final float topWeight = 2.0f;
	final float bottomWeight = 2.0f;
	final float totalWeight = topWeight+bottomWeight;
	
	//上面第1個獎項位置佔全螢幕高的比例
	final float startYWeight = 0.20f;
	//上面2個獎項的間距佔上部剩餘高的比例
	final float TopTwoPrizeGapWeight = 0.1f;
	//上面獎項短邊佔全螢幕寬的比例
	final float TopTwoPrizeWidthShortWeight = 0.05f;
	//上面獎項長邊佔全螢幕寬的比例
	final float TopTwoPrizeWidthLongWeight = 0.3f;
	
	final float BottomPrizePaddingLeft = TopTwoPrizeWidthShortWeight;
	final float BottomPrizePaddingRight = TopTwoPrizeWidthShortWeight;
	final float BottomPrizePaddingTop = 0.0f;
	final float BottomPrizePaddingBottom = 0.10f;
	
	FrameLayout mStartPageLayout;
	FrameLayout mBeforeScratchedLayout;
	FrameLayout mAfterScratchedLayout;

	final int mIconCount = 9;
	Bitmap[] ddhSmallPicture = new Bitmap[mIconCount];
	
	int ddhSmallPictureSize = 0;
	
	
	public final static String MEMBER_STATUS = "MemberStatus";
    public final static String BINGO = "Bingo";
    
  //前面動畫三張圖的索引
    public final static String FAST_MOVE_PAGE_1 ="FastMovePage1";
    public final static String FAST_MOVE_PAGE_2 ="FastMovePage2";
    public final static String FAST_MOVE_PAGE_3 ="FastMovePage3";
    //最後要刮圖的索引    
	public final static String SCRATCH_TICKET ="ScatchTicket";
	
    //是否要刮的開頭兩個訊息    
	public final static String START_SCRATCH_MSG0 ="StartScratchMsg0";
	public final static String START_SCRATCH_MSG1 ="StartScratchMsg1";
    //是否要刮的開頭 刮一下的button title
	public final static String START_SCRATCH_BUTTON ="StartScratchBtn";

    //要刮的當下 開頭兩個訊息
	public final static String SCRATCH_MSG_1 = "ScratchMsg1";
    public final static String SCRATCH_MSG_2 = "ScratchMsg2";
    
    public final static String SCRATCH_HINT_1 = "ScratchHint1";
    public final static String SCRATCH_HINT_2 = "ScratchHint2";
    public final static String SCRATCH_HINT_3 = "ScratchHint3";
    
    public final static String SCRATCH_SUPPORT = "ScratchSupport";
    public final static String SCRATCH_PRIZE = "ScratchPrize";
    public final static String SCRATCH_ADDRESS = "ScratchAddress";
    public final static String SCRATCH_TAKEIT_MSG0 = "ScratchTakeItMsg0";
    public final static String SCRATCH_TAKEIT_MSG1 = "ScratchTakeItMsg1";
    public final static String SCRATCH_TAKEIT_MSG2 = "ScratchTakeItMsg2";
    
    public final static String SCRATCH_TAKEIT_MSG3 = "ScratchTakeItMsg3";
    public final static String SCRATCH_TAKEIT_MSG4 = "ScratchTakeItMsg4";
    public final static String SCRATCH_TAKEIT_MSG5 = "ScratchTakeItMsg5";
    public final static String SCRATCH_TAKEIT_MSG6 = "ScratchTakeItMsg6";
    public final static String SCRATCH_TAKEIT_BUTTON = "ScratchTakeItBtn";
    public final static String SCRATCH_NOT_LOGIN_BUTTON = "ScratchNotLoginBtn";
    public final static String SCRATCH_LOGIN_BUTTON = "ScratchLoginBtn";
    public final static String SCRATCH_NOT_AUTH_BUTTON = "ScratchNotAuthBtn";
    public final static String SCRATCH_AUTH_BUTTON = "ScratchAuthBtn";
    
    public final static String SCRATCH_NOTHING_MSG1 = "ScratchNothingMsg1";
    public final static String SCRATCH_NOTHING_MSG2 = "ScratchNothingMsg2";
    public final static String SCRATCH_NOTHING_BUTTON = "ScratchNothingBtn";
	
    String mStartScratchMsg0 = "";
    String mStartScratchMsg1 = "";
    String mStartScratchBtnTitle = "";
    
	String mScratchMsg1 = "";
	String mScratchMsg2 = "";
	
	String mScratchTakeItMsg0 = "";
	String mScratchSupport = "";
	String mScratchPrize = "";
	String mScratchTakeItMsg1 = "";
	String mScratchTakeItMsg2 = "";
	String mScratchTakeItMsg3 = "";
	String mScratchTakeItMsg4 = "";
	String mScratchTakeItMsg5 = "";
	String mScratchTakeItMsg6 = "";
	String mScratchTakeItButton = "";
	String mScratchNotLoginButton = "";
	String mScratchLoginButton = "";
	String mScratchNotAuthButton = "";
	String mScratchAuthButton = "";
	
	
	String mScratchNothingMsg1 = "";
	String mScratchNothingMsg2 = "";
	String mScratchNothingBtn = "";
    
	int mStartpageResId = 0;
	int mBeforeScrathedResId = 0;
	int mAfterScratchedResId = 0;
	
	public final static String SB_TAKE_IT = "com.tonightstay.DDH.ScratchView.takeit";
	
	final int mBGCount = 6;
	Bitmap[] tBgImg = new Bitmap[mBGCount];
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
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
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        
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
    	final View startConver = inflater.inflate(R.layout.base_scratchview, null);
        addContentView(startConver,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        
        
        mStartPageLayout = (FrameLayout) startConver.findViewById(R.id.startpage_layout);
        mBeforeScratchedLayout = (FrameLayout) startConver.findViewById(R.id.before_scratched_layout);
        mBeforeScratchedLayout.setVisibility(View.INVISIBLE);
        mAfterScratchedLayout = (FrameLayout) startConver.findViewById(R.id.after_scratched_layout);
        mAfterScratchedLayout.setVisibility(View.INVISIBLE);
         
        int startY1 = (int) (sr.height*startYWeight);
        int restTopH = (int) (sr.height*topWeight/totalWeight - startY1);
        int topTwoPrizeGap = (int) (restTopH*TopTwoPrizeGapWeight);
        int restBlockH = restTopH - topTwoPrizeGap-topTwoPrizeGap;
        float restaurantTitleH  = 0.9f;
        float restaurantAddressH  = restaurantTitleH/3.0f;
        float prizeH  = 1.0f;
        float totalH  = restaurantTitleH+restaurantAddressH+prizeH;
        
        int topBlock1H = (int) (restBlockH*(restaurantTitleH/totalH));
        int topBlock2H = (int) (restBlockH*(restaurantAddressH/totalH));
        int topBlock3H = (int) (restBlockH*(prizeH/totalH));
        
        
        
        int startY1_1 = startY1+topBlock1H;
        int startY2 = startY1_1+topBlock2H+topTwoPrizeGap;
        
        
//        int shortWidth = (int) (sr.width*TopTwoPrizeWidthShortWeight);
//        int longWidth = (int) (sr.width*TopTwoPrizeWidthLongWeight);
        
//        mScratchView.setPrize1Rect(new Rect(longWidth,(int) startY1,sr.width-shortWidth,startY1+topPrizeH));
//        mScratchView.setPrize1_1Rect(new Rect(longWidth,(int) startY1+topPrizeH,sr.width-shortWidth,startY1+topPrizeH+topPrizeH/2));
//        mScratchView.setPrize2Rect(new Rect(shortWidth,(int) startY2,sr.width-longWidth,startY2+topPrizeH));
//        
        
        
        int bottomPrizeLeft = (int) (sr.width*BottomPrizePaddingLeft);
        int bottomPrizeRight = (int) (sr.width - sr.width*BottomPrizePaddingRight);
        
        int bottomPrizeTop = (int) ((sr.height*bottomWeight/totalWeight)*BottomPrizePaddingTop+(sr.height*topWeight/totalWeight));
        int bottomPrizeBottom = (int) (sr.height - (sr.height*bottomWeight/totalWeight)*BottomPrizePaddingBottom);
        
        mScratchView.setPrize1Rect(new Rect(bottomPrizeLeft,(int) startY1,bottomPrizeRight,startY1+topBlock1H));
        mScratchView.setPrize1_1Rect(new Rect(bottomPrizeLeft,(int) startY1_1,bottomPrizeRight,startY1_1+topBlock2H));
        mScratchView.setPrize2Rect(new Rect(bottomPrizeLeft,(int) startY2,bottomPrizeRight,startY2+topBlock3H));
        
        mScratchView.setPrize3Rect(new Rect(bottomPrizeLeft,bottomPrizeTop,bottomPrizeRight,bottomPrizeBottom),ddhSmallPictureSize);

        mScratchView.setHintField1Rect(new Rect(bottomPrizeLeft,(int) startY1,bottomPrizeRight,startY1+topBlock1H+topBlock2H));
        mScratchView.setHintField2Rect(new Rect(bottomPrizeLeft,(int) startY2,bottomPrizeRight,startY2+topBlock3H));
        mScratchView.setHintField3Rect(new Rect(bottomPrizeLeft,bottomPrizeTop,bottomPrizeRight,bottomPrizeBottom));
        
        mScratchView.setOnFilledPercentUpdateListener(this);
        mScratchView.setStrokeWidth(DeviceTools.getPixelFromDip(this,5));
    	
        mFastMoveScratchView.setOnFastMoveScratchListener(this);
    }
    
    public void setScratchCustomUI(View startpage,View beforeScrathed,View afterScratched)
    {
        if (mStartPageLayout!=null)
            mStartPageLayout.addView(startpage,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        
        if (mBeforeScratchedLayout!=null)
            mBeforeScratchedLayout.addView(beforeScrathed,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        
        if (mAfterScratchedLayout!=null)
            mAfterScratchedLayout.addView(afterScratched,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        
    }
    
    public void setDDHBG(int ticket1,int ticket2,int ticket3,int scratchTicket)
    {
        int ticket1Id = getResources().getIdentifier("scratch_default_"+ticket1, "drawable", getPackageName());
        int ticket2Id = getResources().getIdentifier("scratch_default_"+ticket2, "drawable", getPackageName());
        int ticket3Id = getResources().getIdentifier("scratch_default_"+ticket3, "drawable", getPackageName());
        int ticket4Id = getResources().getIdentifier("scratch_default_"+scratchTicket, "drawable", getPackageName());
        
        
          tBgImg[0] = loadImageFromResource(R.drawable.scratch_logo);
          tBgImg[1] = loadImageFromResource(ticket1Id);
          tBgImg[2] = loadImageFromResource(ticket2Id);
          tBgImg[3] = loadImageFromResource(ticket3Id);
          tBgImg[4] = loadImageFromResource(ticket4Id);        
          tBgImg[5] = loadImageFromResource(ticket4Id);
        
        
          
        mFastMoveScratchView.setStratchTicket(
                0,
                0,
                tBgImg[0],
                tBgImg[1],
                tBgImg[2],
                tBgImg[3],
                tBgImg[4]);
        
        mScratchView.setScratchImage(
                0,
                0,
                tBgImg[0],tBgImg[5]);
    }
    
    public void resetScratch()
    {
        mStartPageLayout.setVisibility(View.VISIBLE);
        mFastMoveScratchView.setVisibility(View.VISIBLE);
        mFastMoveScratchView.resetScratch();
    }
    
     
    
    public void setScratchImage(Bitmap[] prize3)
    {
        mScratchView.setScratchImage(prize3);
    }
    
    public void setScratchData(String title,String address,String msg,String hint1,String hint2,String hint3,int hintFontSize)
    {
        mScratchView.setPrizeMessage(title,address,msg, hint1,hint2,hint3,hintFontSize);
    }
    
    //開始進行選刮刮券
    public void showRandomAnimation()
    {
        mStartPageLayout.setVisibility(View.INVISIBLE);
        mFastMoveScratchView.startScratch();
    }
    
    //秀未刮開的刮刮樂
    public void showScrathTicket()
    {
        mFastMoveScratchView.setVisibility(View.INVISIBLE);  
        mScratchView.setVisibility(View.VISIBLE); 	
        mBeforeScratchedLayout.setVisibility(View.VISIBLE);
        mAfterScratchedLayout.setVisibility(View.INVISIBLE);
    }
    
    //當快速移動時，觸碰螢幕後抽中一張卡時
    @Override
    public void OnPickScratchFinish() {
        
        mScratchView.setVisibility(View.VISIBLE);
        mBeforeScratchedLayout.setVisibility(View.VISIBLE);
        mAfterScratchedLayout.setVisibility(View.INVISIBLE);
        mScratchView.post(new Runnable() {
            
            @Override
            public void run() {
                mFastMoveScratchView.setVisibility(View.INVISIBLE);
                
            }
        });
    }

    //秀已刮開的刮刮樂
    @Override
    public void onShowAllPrize() {

        mBeforeScratchedLayout.setVisibility(View.INVISIBLE);
        mAfterScratchedLayout.setVisibility(View.VISIBLE);
        mScratchView.setShowScratchCover(false);
    }
    
    
    public boolean isCoverBitmapNull()
    {
        return mScratchView == null?true:mScratchView.isCoverBitmapNull();
    }
    
    int[] fixPriceArr = new int[]{50,100};
    
    public static final int CashTypePrize = 1;
    public static final int FreeTicketTypePrize = 2;
    
    public String[] getRandomPrice(int type, int price)
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
    
    public Bitmap[] getRandomImage( int bingo)
    {

        Bitmap[] ret = new Bitmap[mIconCount];
        
        
        ArrayList<Integer> resId = new ArrayList<Integer>();
        for (int i = 0;i<mIconCount;++i)
            resId.add(Integer.valueOf(i));
        

        if (bingo > 0)
        {
            
            int picIdx = 0;
            if (false)
            {
                //隨機任1圖案為中獎圖案
                int getItIdx = (int) (Math.random()*mIconCount);
                picIdx = resId.get(getItIdx);
                resId.remove(getItIdx);
                
                for (int i = 0;i<3;++i)
                    ret[i] =  ddhSmallPicture[picIdx];
            }
            else
            {
                //指定Logo為中獎圖案
                for (int i = 0;i<3;++i)
                    ret[i] =  ddhSmallPicture[0];
                resId.remove(0);
            
            }
            
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
    
	

}
