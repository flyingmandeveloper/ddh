package com.tonightstay.DDH.main;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class DailyCheck {

	SharedPreferences scratchData;

	public DailyCheck(Context context) {
		scratchData = context.getSharedPreferences("PREF_DEMO", 0);
	}

	public boolean isTodayScratch() {
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		int day = today.get(Calendar.DAY_OF_YEAR);

		int todayDate = year * 1000 + day;
		if (todayDate >= scratchData.getInt("scratchDate", 0))
			return true;
		else
			return false;
	}
	
	public void setTodayScratch()
	{
		int recordDate = scratchData.getInt("scratchDate", 0);
		
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		int day = today.get(Calendar.DAY_OF_YEAR);

		int todayDate = year * 1000 + day;
		if (todayDate>recordDate)
		{
			SharedPreferences.Editor editor = scratchData.edit();
			editor.putInt("scratchDate", todayDate);
			editor.commit();
		}
	}
	
}
