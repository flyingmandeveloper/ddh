package com.tonightstay.DDH.ScratchView;

import com.example.com.tonightstay.ddh.R;
import com.tonightstay.DDH.tools.DeviceTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FastMoveScratchView extends  SurfaceView implements SurfaceHolder.Callback {

	Bitmap mStartBitmap1;
	Bitmap mStartBitmap2;
	Bitmap mStartBitmap3;
	Bitmap mStartBitmap4;
	Bitmap mBackgroundBitmap;
	Bitmap mFinger0;
	Bitmap mFinger1;
	Paint mBgPaint = new Paint();
	FastMoveScratchViewThread mThread;
	double mOffY = 0;
	double mSpeed = 50;
	
	final double  mSlowToFastSpeedDefault = 0.1;
	double mMillsDiffTime = 0;
	double mSlowToFastOffY1 = 0;
	double mSlowToFastOffY2 = 0;
	double mSlowToFastSpeed = mSlowToFastSpeedDefault;
	double mSlowToFastAcceleration = 0.005;
	double mSlowDiffDistance = 0;
	
	final double  mFastToSlowSpeedDefault = 3.0;
	final double  mFastToSlowSpeedStopDefault = 0.5;
	double mFastToSlowOffY = 0;
	double mFastToSlowDiffDistance = 0;
	double mFastToSlowDuring = 0;
	double mFastToSlowAcceleration = 0;
	double mFastToSlowNow = 0;
	int fingerSize = 0;
	double swapFingerTime = 150;
	double swapFingerDuring = 0;
	int fingerAniIdx = 0;
	
	public interface OnFastMoveScratchListener
	{
		public void OnPickScratchFinish();
	};
	
	OnFastMoveScratchListener mOnFastMoveScratchListener;
	
	public void startScratch()
	{
		if (mThread!=null)
			mThread.aniState = eAnimState.SlowToFast;
	}
	
	public void setOnFastMoveScratchListener(OnFastMoveScratchListener listener)
	{
		mOnFastMoveScratchListener = listener;
	}
	
	private Handler FinishPickScratch =  new Handler()
	{
		@Override
		 public void handleMessage(Message msg) 
		{
		  super.handleMessage(msg);
		  String MsgString = (String)msg.obj;
		  if (MsgString.equals("Finish"))
		  {
			 if (mOnFastMoveScratchListener!=null)
				 mOnFastMoveScratchListener.OnPickScratchFinish();
		  } 
		}
	};
	
	public FastMoveScratchView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		init(ctx, attrs);
	}

	public FastMoveScratchView(Context context) {
		super(context);
		init(context, null);
	}
	
	private void init(Context context, AttributeSet attrs) 
	{
		//setZOrderOnTop(true);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.TRANSPARENT);
		
		fingerSize = DeviceTools.getPixelFromDip(context, 64);
		mBackgroundBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.fastmove_scratch_bg)).getBitmap();
		mFinger0 = ((BitmapDrawable)getResources().getDrawable(R.drawable.finger_touch0)).getBitmap();
		mFinger1 = ((BitmapDrawable)getResources().getDrawable(R.drawable.finger_touch1)).getBitmap();

		mBgPaint.setAntiAlias(true);
	}
	
	public void setStratchTicket(Bitmap ticket1,Bitmap ticket2,Bitmap ticket3,Bitmap ticket4)
	{
		mStartBitmap1 = ticket1;
		mStartBitmap2 = ticket2;
		mStartBitmap3 = ticket3;
		mStartBitmap4 = ticket4;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (mThread == null)
			return false;
			
		switch(me.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				switch(mThread.aniState)
				{
				case VeryFast:
					mFastToSlowDuring = getWidth()*2/(mFastToSlowSpeedDefault+mFastToSlowSpeedStopDefault);
					mFastToSlowAcceleration = - (mFastToSlowSpeedDefault-mFastToSlowSpeedStopDefault)/mFastToSlowDuring;
					mThread.aniState = eAnimState.FastToSlow;
					break;
				}
				break;
		}
		
		return false;
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mThread = new FastMoveScratchViewThread(getHolder(), this);
		mThread.setRunning(true);
		mThread.start();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
	
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
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
	
	public void onHoldDraw(Canvas canvas)
	{

		canvas.drawBitmap(
				mStartBitmap1, 
				new Rect(0,0,mStartBitmap1.getWidth(),mStartBitmap1.getHeight()),
				new Rect(0,0,getWidth(),getHeight()),
				mBgPaint);
	}
	
	public void onSlowToFastDraw(Canvas canvas,long offsetTime) {

		mMillsDiffTime = ((double)offsetTime/1000000);
		int wholeWidth = getWidth();
		int leftImageX1 = wholeWidth;
		int leftImageX2 = wholeWidth+wholeWidth;
		
		
		mSlowToFastSpeed += mMillsDiffTime*mSlowToFastAcceleration;
		mSlowDiffDistance = mMillsDiffTime*mSlowToFastSpeed;
		
		mSlowToFastOffY1-=mSlowDiffDistance;
		mSlowToFastOffY2-=mSlowDiffDistance;
				
		canvas.drawBitmap(
				mStartBitmap1, 
				new Rect(0,0,mStartBitmap1.getWidth(),mStartBitmap1.getHeight()),
				new Rect((int)mSlowToFastOffY1,0,(int)mSlowToFastOffY1+wholeWidth,getHeight()),
				mBgPaint);

		canvas.drawBitmap(
				mStartBitmap2, 
				new Rect(0,0,mStartBitmap2.getWidth(),mStartBitmap2.getHeight()),
				new Rect((int)mSlowToFastOffY2+leftImageX1,0,(int)mSlowToFastOffY2+leftImageX2,getHeight()),
				mBgPaint);
		
		if (mSlowToFastOffY1<=-leftImageX1)
		{
			mSlowToFastOffY1 = leftImageX2 + mSlowToFastOffY1;
			mThread.aniState = eAnimState.VeryFast;
		}
		
		if (mSlowToFastOffY2<=-leftImageX2)
		{
			mSlowToFastOffY2 = leftImageX2 + mSlowToFastOffY2;
			
		}

	}
	
	public void onSDraw(Canvas canvas,long offsetTime) {

		mMillsDiffTime = ((double)offsetTime/1000000);
		int wholeWidth = getWidth()*3;
		
		canvas.drawBitmap(
				mBackgroundBitmap, 
				new Rect(0,0,mBackgroundBitmap.getWidth(),mBackgroundBitmap.getHeight()),
				new Rect((int)mOffY,0,(int)mOffY+wholeWidth,getHeight()),
				mBgPaint);
		
		canvas.drawBitmap(
				mBackgroundBitmap, 
				new Rect(0,0,mBackgroundBitmap.getWidth(),mBackgroundBitmap.getHeight()),
				new Rect((int)mOffY+wholeWidth,0,(int)mOffY+wholeWidth+wholeWidth,getHeight()),
				mBgPaint);
		
		if (fingerAniIdx%2==0)
		{
			canvas.drawBitmap(
					mFinger0, 
					new Rect(0,0,mFinger0.getWidth(),mFinger0.getHeight()),
					new Rect(getWidth()/2-fingerSize/2,(getHeight()*2/3),getWidth()/2+fingerSize/2,(getHeight()*2/3)+fingerSize),
					mBgPaint);
		}
		else
		{
			canvas.drawBitmap(
					mFinger1, 
					new Rect(0,0,mFinger1.getWidth(),mFinger1.getHeight()),
					new Rect(getWidth()/2-fingerSize/2,(getHeight()*2/3),getWidth()/2+fingerSize/2,(getHeight()*2/3)+fingerSize),
					mBgPaint);
		}
		
		
		swapFingerDuring+=mMillsDiffTime;
		if (swapFingerDuring>=swapFingerTime)
		{
			swapFingerDuring %= swapFingerTime;
			fingerAniIdx++;
		}
		
		mOffY-=mMillsDiffTime*mSpeed;

		
		if (mOffY<=-wholeWidth)
			mOffY = mOffY%(-wholeWidth);
	}
	
	public void onFastToSlowDraw(Canvas canvas,long offsetTime) {

		mMillsDiffTime = ((double)offsetTime/1000000);
		int wholeWidth = getWidth();
		int wholeWidthX2 = wholeWidth+wholeWidth;
		
		mFastToSlowNow +=mMillsDiffTime;

		if (mFastToSlowNow<=mFastToSlowDuring)
			mFastToSlowOffY = -(mFastToSlowSpeedDefault*mFastToSlowNow+(mFastToSlowAcceleration*(mFastToSlowNow*mFastToSlowNow))/2);
		else
		{
			mFastToSlowOffY = -wholeWidth;
			if (mThread.finishPick == false)
			{
				mThread.finishPick = true;
				Message message = FinishPickScratch.obtainMessage(1,"Finish");
				FinishPickScratch.sendMessage(message);
			}
		}
		
				
		canvas.drawBitmap(
				mStartBitmap3, 
				new Rect(0,0,mStartBitmap1.getWidth(),mStartBitmap1.getHeight()),
				new Rect((int)mFastToSlowOffY,0,(int)mFastToSlowOffY+wholeWidth,getHeight()),
				mBgPaint);

		canvas.drawBitmap(
				mStartBitmap4, 
				new Rect(0,0,mStartBitmap2.getWidth(),mStartBitmap2.getHeight()),
				new Rect((int)mFastToSlowOffY+wholeWidth,0,(int)mFastToSlowOffY+wholeWidthX2,getHeight()),
				mBgPaint);
		
		
	}

	public enum eAnimState
	{
		HoldDraw,
		SlowToFast,
		VeryFast,
		FastToSlow
	}
	
	class FastMoveScratchViewThread extends Thread {
		private SurfaceHolder mSurfaceHolder;
		private FastMoveScratchView mView;
		private boolean mRun = false;
		boolean finishPick = false;
		
		eAnimState aniState = eAnimState.HoldDraw;

		public FastMoveScratchViewThread(SurfaceHolder surfaceHolder, FastMoveScratchView view) {
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
		public void run() 
		{
			long nowTime = System.nanoTime();
			long startTm = 0L;
			long sleepTime = 0L;
			long drawTime = 0L;
			
			while (mRun) 
			{
				Canvas c = null;
				try 
				{
					startTm = System.nanoTime();
					
					c = mSurfaceHolder.lockCanvas();
		
					if (c != null)
					{
						switch(aniState)
						{
							case HoldDraw:
								mView.onHoldDraw(c);
							break;
							case SlowToFast:
								mView.onSlowToFastDraw(c,System.nanoTime() - nowTime);
							break;
							case VeryFast:
								mView.onSDraw(c,System.nanoTime() - nowTime);
							break;
							case FastToSlow:
								mView.onFastToSlowDraw(c,System.nanoTime() - nowTime);
							break;
							
						}
					}
					nowTime = System.nanoTime();
					drawTime = nowTime - startTm;
					startTm = nowTime;
	
				} finally {
					if (c != null)
						mSurfaceHolder.unlockCanvasAndPost(c);
				}
				
				
				sleepTime = 33 -(drawTime/1000000L);
				if (sleepTime<0L)
					sleepTime = 0L;
				
				
				try {

					this.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
