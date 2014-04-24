package com.tonightstay.DDH.dialog;

import com.example.com.tonightstay.ddh.R;

import android.app.Activity;
import android.os.Bundle;

public class GetDDHTicketDialog extends Activity {


	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.get_ddh_ticket_dialog);
		
		setTitle(R.string.get_ddh_ticket_title);
	}
	
}
