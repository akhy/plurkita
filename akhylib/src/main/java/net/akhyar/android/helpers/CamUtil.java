package net.akhyar.android.helpers;

import android.content.Context;
import android.content.pm.PackageManager;

public class CamUtil {
	public static boolean hasCamera(Context context) {
		return context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA);
	}
}
