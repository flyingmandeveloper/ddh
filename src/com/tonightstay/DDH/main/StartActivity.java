package com.tonightstay.DDH.main;


import com.example.com.tonightstay.ddh.R;
import com.tonightstay.DDH.ScratchView.ScratchTicketActivity;
import com.tonightstay.DDH.dialog.GetDDHTicketDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity implements OnClickListener{

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		Button btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener(this);
		
		Intent i = new Intent(this,GetDDHTicketDialog.class);
		startActivity(i);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		i.setClass(this, ScratchTicketActivity.class);
		startActivity(i);
		
	}

	
}
