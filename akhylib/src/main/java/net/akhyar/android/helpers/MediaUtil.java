package net.akhyar.android.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore.Images;

import java.io.File;
import java.util.Locale;

public class MediaUtil {
	public static Uri saveJpegEntry(Context context, String imagePath, String title,
			String description, long dateTaken, Location loc) {
		ContentValues v = new ContentValues();
		v.put(Images.Media.TITLE, title);
		v.put(Images.Media.DISPLAY_NAME, title);
		v.put(Images.Media.DESCRIPTION, description);
		v.put(Images.Media.DATE_ADDED, dateTaken);
		v.put(Images.Media.DATE_TAKEN, dateTaken);
		v.put(Images.Media.DATE_MODIFIED, dateTaken);
		v.put(Images.Media.MIME_TYPE, "image/jpeg");

		File f = new File(imagePath);
		File parent = f.getParentFile();
		String path = parent.toString().toLowerCase(Locale.ENGLISH);
		String name = parent.getName().toLowerCase(Locale.ENGLISH);
		v.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
		v.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
		v.put(Images.Media.SIZE, f.length());
		f = null;

		if (loc != null) {
			v.put(Images.Media.LATITUDE, loc.getLatitude());
			v.put(Images.Media.LONGITUDE, loc.getLongitude());
		}
		v.put("_data", imagePath);
		ContentResolver c = context.getContentResolver();
		return c.insert(Images.Media.EXTERNAL_CONTENT_URI, v);
	}
}
