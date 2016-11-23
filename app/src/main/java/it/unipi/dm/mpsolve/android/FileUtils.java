package it.unipi.dm.mpsolve.android;

import java.net.URISyntaxException;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * @brief Utility to get the Path of a Uri obtained through a call to 
 * an Intent.ACTION_GET_CONTENT. 
 * 
 * Taken from {@url http://stackoverflow.com/questions/7856959/android-file-chooser}. 
 */
public class FileUtils {

	/**
	 * @brief Obtain the Path of a resource obtained through an Intent.ACTION_GET_CONTENT
	 * @param context The Android Context. 
	 * @param uri The Uri obtained from the call to the Intent. 
	 * @return A String with a local path on the device, or null if the data
	 * was invalid. 
	 * 
	 * @throws URISyntaxException
	 */
	public static String getPath(Context context, Uri uri) throws URISyntaxException {
	    if ("content".equalsIgnoreCase(uri.getScheme())) {
	        String[] projection = { "_data" };
	        Cursor cursor = null;

	        try {
	            cursor = context.getContentResolver().query(uri, projection, null, null, null);
	            int column_index = cursor.getColumnIndexOrThrow("_data");
	            if (cursor.moveToFirst()) {
	                return cursor.getString(column_index);
	            }
	        } catch (Exception e) {
	        	// TODO: Do something here
	        }
	    }
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}
	
}
