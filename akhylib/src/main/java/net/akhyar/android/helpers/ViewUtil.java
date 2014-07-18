package net.akhyar.android.helpers;

import android.view.View;

public class ViewUtil {
	public static void toggleVisibility(View view) {
		if (view.getVisibility() == View.INVISIBLE)
			view.setVisibility(View.VISIBLE);
		else
			view.setVisibility(View.INVISIBLE);
	}

	public static void togglePresence(View view) {
		if (view.getVisibility() == View.GONE)
			view.setVisibility(View.VISIBLE);
		else
			view.setVisibility(View.GONE);
	}

	public static void toggleSelected(View view) {
		view.setSelected(!view.isSelected());
	}

	public static void setPresence(View view, boolean isVisible) {
		if (isVisible)
			view.setVisibility(View.VISIBLE);
		else
			view.setVisibility(View.GONE);
	}

	public static void setVisibility(View view, boolean isVisible) {
		if (isVisible)
			view.setVisibility(View.VISIBLE);
		else
			view.setVisibility(View.INVISIBLE);
	}

}
