package com.tonightstay.DDH.dialog;

import com.example.com.tonightstay.ddh.R;
import com.tonightstay.DDH.ScratchView.ScratchTicketActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class GetDDHTicketDialog extends Activity {

	Button mNBtn;
	Button mPBtn;
	int[]StratchIdx=new int[]{1,2,3,4,5,6,7,8,9,10};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 LayoutParams params = getWindow().getAttributes(); 
         params.height = LayoutParams.MATCH_PARENT;
         params.width  = LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
//        getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL, LayoutParams.FLAG_NOT_TOUCH_MODAL);
//        getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        
        
        setFinishOnTouchOutside(false);
        
		setContentView(R.layout.get_ddh_ticket_dialog);
		
		setTitle(R.string.get_ddh_ticket_title);
		
		mNBtn = (Button)findViewById(R.id.ddh_dialog_n_btn);
		
		mNBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					finish();
			}
		});
		
		mPBtn = (Button)findViewById(R.id.ddh_dialog_p_btn);
		
		mPBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				
				randomIdx();
				
				Intent i = new Intent();
				i.setClass(GetDDHTicketDialog.this, ScratchTicketActivity.class);
				
				int TypePrize = (int) (Math.random()*2)+1;
				i.putExtra("FastMovePage1", StratchIdx[0]);
				i.putExtra("FastMovePage2", StratchIdx[1]);
				i.putExtra("FastMovePage3", StratchIdx[3]);
				i.putExtra("ScatchTicket", StratchIdx[4]);
				i.putExtra("ScatchType", TypePrize);
				i.putExtra("ScatchMsg1", "知本老爺大酒店");
				
				int rrr = (int) (Math.random()*3);
				
				rrr = 0;
				Log.e("debug","rrrr:"+rrr);
				if (rrr == 0)
				{
					if (TypePrize == ScratchTicketActivity.CashTypePrize)
					{
						int[] fixPriceArr = new int[]{50,100,150,200,250,300};
						int idx = (int) (Math.random()*fixPriceArr.length);
						i.putExtra("ScatchMsg2", getString(R.string.get_ddh_ticket_type_cash));
						i.putExtra("ScatchPrice", fixPriceArr[idx]);
						i.putExtra("Startdate", "2014/08/30");
						i.putExtra("Expiredate", "2014/10/30");
					}
					else if (TypePrize == ScratchTicketActivity.FreeTicketTypePrize)
					{
						i.putExtra("ScatchMsg2", getString(R.string.get_ddh_ticket_type_free_room));
						i.putExtra("ScatchPrice", 1);
						i.putExtra("Startdate", "2014/08/30");
						i.putExtra("Expiredate", "2014/10/30");
						
					}
				}
				else
				{
					if (TypePrize == ScratchTicketActivity.CashTypePrize)
						i.putExtra("ScatchMsg2", getString(R.string.get_ddh_ticket_type_cash));
					else if  (TypePrize == ScratchTicketActivity.FreeTicketTypePrize)
						i.putExtra("ScatchMsg2", getString(R.string.get_ddh_ticket_type_free_room));
						
					i.putExtra("ScatchPrice", -1);
						
				}
				
				startActivity(i);
				
	
				
			}
		});
	}
	
	void randomIdx()
	{
	
		int idx1 = 0;
		int idx2 = 0;
		int swap = 0;
		
		for (int i=0;i<10;++i)
		{
			idx1 = (int) (Math.random()*10);
			idx2= (int) (Math.random()*10);
			swap = StratchIdx[idx1];
			StratchIdx[idx1] = StratchIdx[idx2];
			StratchIdx[idx2] = swap;
		}
	}
	
//	@Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//
//        	Log.e("debug","ACTION_OUTSIDE");
//            return false;
//        }
//
//        return false;
//    }
	
}
