package me.dalt.pushexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ScrollView;

import com.growthpush.GrowthPush;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ScrollView scrollView = new ScrollView(this);
		setContentView(scrollView);

		GrowthPush.getInstance().initialize(getApplicationContext(), YOUR_APP_ID, "YOUR_APP_SECRET").register("YOUR_GOOGLE_API_SENDER_ID");
		GrowthPush.getInstance().trackEvent("Launch");
		GrowthPush.getInstance().setDeviceTags();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
