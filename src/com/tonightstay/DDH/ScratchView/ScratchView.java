package com.tonightstay.DDH.ScratchView;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.example.com.tonightstay.ddh.R;
import com.tonightstay.DDH.ScratchView.FastMoveScratchView.OnFastMoveScratchListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ScratchView extends SurfaceView implements SurfaceHolder.Callback {
	// default value constants
	private final int DEFAULT_REVEAL_SIZE = 60;

	Paint bgPaint = new Paint();
	Paint coverPaint = new Paint();
	Paint maskPaint = new Paint();
	
	
	private Context mContext;
	private WScratchViewThread mThread;

	private Paint mOverlayPaint;
	private int mRevealSize;
	private boolean mIsAntiAlias = false;
	private Path path = new Path();
	int pathCount = 0;
	
	private boolean mScratchStart = false;

	Bitmap mIconBitmap;
	Bitmap mCoverBitmap = null;
	Canvas mCoverCanvas;

	int wid_mode = 60;


	Rect mFontBound = new Rect();

	
	PrizeField mPrize1 = new PrizeField();
	PrizeField mPrize2 = new PrizeField();
	PrizeField9x9 mPrize3 = new PrizeField9x9();
	
	int countOfCheck = 5;
	int chkX =0;
	int chkY =0;
	int coverImageW =0;
	int coverImageH =0;
	
	int prize1X = 0;
	int prize1Y = 0;
	int prize1GridW = 0;
	int prize1GridH = 0;
	int prize1OffX = 0;
	int prize1OffY = 0;
	
	int prize2X = 0;
	int prize2Y = 0;
	int prize2GridW = 0;
	int prize2GridH = 0;
	int prize2OffX = 0;
	int prize2OffY = 0;
	
	int prize3X = 0;
	int prize3Y = 0;
	int prize3GridW = 0;
	int prize3GridH = 0;
	int prize3OffX = 0;
	int prize3OffY = 0;
	
	boolean mCanShowPrize = false;
	
	public interface OnFilledPercentUpdateListener
	{
		public void onShowAllPrize();
	};
	
	OnFilledPercentUpdateListener mOnFilledPercentUpdateListener;
	
	public void setOnFilledPercentUpdateListener(OnFilledPercentUpdateListener listener)
	{
		mOnFilledPercentUpdateListener = listener;
	}
	
//	private Handler FilledPercentUpdateHandler =  new Handler()
//	{
//		@Override
//		 public void handleMessage(Message msg) 
//		{
//		  super.handleMessage(msg);
//		  String MsgString = (String)msg.obj;
//		  if (MsgString.equals("Update"))
//		  {
//			  
//			 if (mOnFilledPercentUpdateListener!=null)
//				 mOnFilledPercentUpdateListener.onShowAllPrize();
//		  } 
//		}
//	};
	
	public ScratchView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		init(ctx, attrs);
	}

	public ScratchView(Context context) {
		super(context);
		init(context, null);
	}

	boolean touth = false;
	private void init(Context context, AttributeSet attrs) {
		mContext = context;

		mRevealSize = DEFAULT_REVEAL_SIZE;

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.TRANSPARENT);

		mOverlayPaint = new Paint();
		mOverlayPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		mOverlayPaint.setStyle(Paint.Style.STROKE);
		mOverlayPaint.setStrokeCap(Paint.Cap.ROUND);
		mOverlayPaint.setStrokeJoin(Paint.Join.ROUND);
		
		maskPaint.setStyle(Paint.Style.STROKE);
		maskPaint.setStrokeCap(Paint.Cap.ROUND);
		maskPaint.setStrokeJoin(Paint.Join.ROUND);
		maskPaint.setAntiAlias(mIsAntiAlias);
		maskPaint.setStrokeWidth(mRevealSize);

		bgPaint.setAntiAlias(true);
		mIconBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.scratch_default_2);
	}
	
	public void setPrize1Rect(Rect rect)
	{
		mPrize1.setRect(rect);
		
		prize1X = mPrize1.getX();
		prize1Y = mPrize1.getY();
		prize1GridW = mPrize1.getWidth()/countOfCheck;
		prize1GridH = mPrize1.getHeight()/countOfCheck;
		prize1OffX = prize1GridW/2;
		prize1OffY = prize1GridH/2;
	}
	
	public void setPrize2Rect(Rect rect)
	{
		mPrize2.setRect(rect);
		
		prize2X = mPrize2.getX();
		prize2Y = mPrize2.getY();
		prize2GridW = mPrize2.getWidth()/countOfCheck;
		prize2GridH = mPrize2.getHeight()/countOfCheck;
		prize2OffX = prize2GridW/2;
		prize2OffY = prize2GridH/2;
	}
	
	public void setPrize3Rect(Rect rect)
	{
		mPrize3.setRect(rect);
		
		prize3X = mPrize2.getX();
		prize3Y = mPrize2.getY();
		prize3GridW = mPrize3.getWidth()/countOfCheck;
		prize3GridH = mPrize3.getHeight()/countOfCheck;
		prize3OffX = prize3GridW/2;
		prize3OffY = prize3GridH/2;
	}
	
	public void setPrizeMessage(String prize1,String prize2,String []prize3)
	{
		mPrize1.setMessage(prize1);
		int textSize = mPrize1.testTextSize();
		
		mPrize2.setMessage(prize2);
		textSize =Math.min(mPrize2.testTextSize(), textSize);
		
		mPrize3.setMessage(prize3);
		textSize =Math.min(mPrize3.testTextSize(), textSize);

		mPrize1.setTextSize(textSize);
		mPrize2.setTextSize(textSize);
		mPrize3.setTextSize(textSize);
	}
	

	
	public boolean countPercentOfFilled(int prize1ShowPercent,float prize2ShowPercent,float prize3ShowPercent)
	{
		if (mCoverBitmap == null)
			return false;
		
		int countP1 = 0;
		int countP2 = 0;
		int countP3 = 0;
		
		for (int y = 0;y<countOfCheck;++y)
		{
			for (int x = 0;x<countOfCheck;++x)
			{
				chkX = prize1X+prize1OffX+x*prize1GridW;
				chkY = prize1Y+prize1OffY+y*prize1GridH;
				if (chkX>=0 && chkX<coverImageW &&
					chkY>=0 && chkY<coverImageH	)
				{
					if (mCoverBitmap.getPixel(chkX, chkY) == 0)
						countP1++;
				}
				
				chkX = prize2X+prize2OffX+x*prize2GridW;
				chkY = prize2Y+prize2OffY+y*prize2GridH;
				if (chkX>=0 && chkX<coverImageW &&
					chkY>=0 && chkY<coverImageH	)
					if (mCoverBitmap.getPixel(chkX, chkY) == 0)
						countP2++;
				
				chkX = prize3X+prize3OffX+x*prize3GridW;
				chkY = prize3Y+prize3OffY+y*prize3GridH;
				if (chkX>=0 && chkX<coverImageW &&
					chkY>=0 && chkY<coverImageH	)
					if (mCoverBitmap.getPixel(chkX, chkY) == 0)
						countP3++;
				
			}
		}
		
		float sumOfCount =countOfCheck*countOfCheck;
		Log.e("debug","p1("+(100*((float)countP1/sumOfCount))+") p2("+(100*((float)countP2/sumOfCount))+") p3("+(100*((float)countP3/sumOfCount))+")");
		if (100*((float)countP1/sumOfCount)>=prize1ShowPercent &&
			100*((float)countP2/sumOfCount)>=prize2ShowPercent &&
			100*((float)countP3/sumOfCount)>=prize3ShowPercent)
			return true;
		else
			return false;


	}
	
	public void onSDraw(Canvas canvas) {

		canvas.drawBitmap(mIconBitmap, new Rect(0, 0, mIconBitmap.getWidth(),
				mIconBitmap.getHeight()), new Rect(0, 0, getWidth(),
				getHeight()), bgPaint);
		

		mPrize1.onDrawPrize(canvas);
		mPrize2.onDrawPrize(canvas);
		mPrize3.onDrawPrize(canvas);
		

		maskPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));

		mCoverCanvas.drawPath(path, maskPaint);


		maskPaint.setXfermode(null);

		canvas.drawBitmap(mCoverBitmap, new Rect(0, 0, mCoverBitmap.getWidth(),
				mCoverBitmap.getHeight()), new Rect(0, 0, getWidth(),
				getHeight()), bgPaint);

		mPrize1.onDrawLine(canvas);
		mPrize2.onDrawLine(canvas);
		mPrize3.onDrawLine(canvas);

		if (pathCount>15)
			clearPath();

	}
	


	public void clearPath() {
		mScratchStart = false;
		path.reset();
		pathCount = 0;
	}

	
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (mThread == null)
			return false;

		touth = true;

		lock.lock();
		try{
			
			switch(me.getAction()){
			case MotionEvent.ACTION_DOWN:
				path = new Path();
				path.moveTo(me.getX(), me.getY());
				pathCount++;
				mScratchStart = true;
				mThread.mCountScratchPercent = true;
			    break;
			case MotionEvent.ACTION_MOVE:
				if(mScratchStart){
					path.lineTo(me.getX(), me.getY());
					pathCount++;
					mThread.mCountScratchPercent = true;
				}else{
					mScratchStart = true;
					path.moveTo(me.getX(), me.getY());
					pathCount++;
					mThread.mCountScratchPercent = true;
				}
				break;
			case MotionEvent.ACTION_UP:
			default:
				clearPath();
				
				if (mCanShowPrize)
				{
					 if (mOnFilledPercentUpdateListener!=null)
						 mOnFilledPercentUpdateListener.onShowAllPrize();
				}
				break;
			}
		}
		finally
		{
			lock.unlock();
		}

			touth = false;
			return true;
		
	}

	

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

		mCoverBitmap = Bitmap.createBitmap(getWidth(), getHeight(),Bitmap.Config.ARGB_4444);

		coverImageW = getWidth();
		coverImageH = getHeight();
		
		mCoverCanvas = new Canvas(mCoverBitmap);
		
	
	    mCoverCanvas.drawBitmap(
					mIconBitmap,
					new Rect(0,0,mIconBitmap.getWidth(),mIconBitmap.getHeight()),
					new Rect(0,0,getWidth(),getHeight()),
					coverPaint);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		mThread = new WScratchViewThread(getHolder(), this);
		mThread.setRunning(true);
		mThread.start();
  
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		boolean retry = true;
		mThread.setRunning(false);
		while (retry) {
			try {
				mThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// do nothing but keep retry
			}
		}

	}
	Lock lock = new ReentrantLock();  
	
	class WScratchViewThread extends Thread {
		private SurfaceHolder mSurfaceHolder;
		private ScratchView mView;
		private boolean mRun = false;
		private boolean mCountScratchPercent = false;

		public WScratchViewThread(SurfaceHolder surfaceHolder, ScratchView view) {
			mSurfaceHolder = surfaceHolder;
			mView = view;
		}

		public void setRunning(boolean run) {
			mRun = run;
		}

		public SurfaceHolder getSurfaceHolder() {
			return mSurfaceHolder;
		}

		@Override
		public void run() {
			long startTime = 0L;
			long sleepTime = 0L;
			while (mRun) {
				Canvas c = null;
				try {
					c = mSurfaceHolder.lockCanvas();
					lock.lock();
					
					startTime = System.currentTimeMillis();
					
					try{
						if (c != null)
							mView.onSDraw(c);
					}
					finally
					{
						lock.unlock();
					}
					
					if (mCountScratchPercent == true)
					{
					
						
						mCountScratchPercent = false;
						
						

						if (countPercentOfFilled(40,40,60))
						{
							mCanShowPrize = true;
							
						}
						
					}
					
					sleepTime = 33 -(System.currentTimeMillis() - startTime);
					if (sleepTime<0L)
						sleepTime = 0L;
					
				
					this.sleep(sleepTime);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (c != null)
						mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}

	}

}
