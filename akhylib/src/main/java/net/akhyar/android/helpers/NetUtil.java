package net.akhyar.android.helpers;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetUtil {

	public static boolean isNetworkAvailable(Context context) {
		return ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo() != null;
	}

}
