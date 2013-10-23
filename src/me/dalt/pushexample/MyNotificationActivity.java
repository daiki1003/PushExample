package me.dalt.pushexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.growthpush.GrowthPush;

public class MyNotificationActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GrowthPush.getInstance().trackEvent("Launch via push notification");
		this.startActivity(this.getPackageManager().getLaunchIntentForPackage(this.getPackageName())
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}
}