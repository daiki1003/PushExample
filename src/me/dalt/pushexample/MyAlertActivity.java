package me.dalt.pushexample;

import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.growthpush.GrowthPush;
import com.growthpush.utils.SystemUtils;
import com.growthpush.view.AlertFragment;

public class MyAlertActivity extends FragmentActivity {

	private static final int WAKE_LOCK_TIMEROUT = 10 * 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(android.R.style.Theme_Translucent);

		manageKeyguard();
		managePower();

		AlertFragment fragment = new MyAlertFragment(getIntent().getExtras().getString("message"));
		fragment.show(getSupportFragmentManager(), getClass().getName());

	}

	@Override
	public void onDestroy() {

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		super.onDestroy();

	}

	private void manageKeyguard() {

		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		if (!keyguardManager.inKeyguardRestrictedInputMode())
			return;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			return;
		}

		if (keyguardManager.isKeyguardSecure())
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		else if (keyguardManager.isKeyguardLocked())
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

	}

	private void managePower() {

		PowerManager powerManager = SystemUtils.getPowerManager(getApplicationContext());
		if (powerManager == null)
			return;

		@SuppressWarnings("deprecation")
		final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, getClass().getName());
		wakeLock.acquire();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				wakeLock.release();
			}

		}, WAKE_LOCK_TIMEROUT);

	}

	class MyAlertFragment extends AlertFragment {

		public MyAlertFragment(String message) {
			super(message);
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {

			if (which == DialogInterface.BUTTON_POSITIVE)
				GrowthPush.getInstance().trackEvent("Launch via push notification");

			super.onClick(dialog, which);

		}
	}

}