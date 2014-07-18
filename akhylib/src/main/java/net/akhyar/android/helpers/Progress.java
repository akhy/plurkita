package net.akhyar.android.helpers;

import android.app.ProgressDialog;
import android.content.Context;

public class Progress {
	public static ProgressDialog makeBlocking(Context context, String message) {
		ProgressDialog dlg = new ProgressDialog(context);
		dlg.setMessage(message);
		dlg.setCancelable(false);
		return dlg;
	}
	
	public static ProgressDialog makeCancelable(Context context, String message) {
		ProgressDialog dlg = new ProgressDialog(context);
		dlg.setMessage(message);
		dlg.setCancelable(true);
		return dlg;
	}
}
