package com.ian.carassist;

import com.ian.carassist.service.AssistService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AssistReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent i = new Intent(context, AssistService.class);
            Log.d("ca", "boot completed.");
            context.startService(i);
		}
	}
}
