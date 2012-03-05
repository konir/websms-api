package de.ub0r.android.websms.connector.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class MmsUtils {

	private static final String TAG = "MmsUtils";

	private static final Uri MMS_URI_PARTS = Uri.parse("content://mms/part/");
	/** Cursor's projection for parts. */
	private static final String[] PROJECTION_PARTS = { //
	"_id", // 0
			"mid", // 1
			"ct", // 2
	};
	/** INDEX: id. */
	private static final int INDEX_ID = 0;
	/** INDEX: mid. */
	public static final int INDEX_MID = 1;
	/** INDEX: content type. */
	public static final int INDEX_CT = 2;

	/**
	 * Returns the contents of the file as a byte array
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(final File file) throws IOException {
		InputStream is = new FileInputStream(file);
		Log.d(TAG, "getBytesFromFile(....)");

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
			Log.d(TAG, "File is too large: " + length);
			return null;
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file: "
					+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		Log.d(TAG, "File size=" + bytes.length);
		return bytes;
	}

	/**
	 * Resize an image to the target size
	 * 
	 * @param byteArray
	 * @param targetSize
	 * @return
	 */
	public static byte[] resizeImage(final byte[] byteArray,
			final long targetSize) {
		Log.d(TAG, "****++++ resizeImage");
		byte[] fileByteArray = null;

		Bitmap yourSelectedImage = BitmapFactory.decodeByteArray(byteArray, 0,
				byteArray.length);

		float scale = (((200 * 1000) / (float) byteArray.length) * 100);
		Log.d(TAG, "scale float= " + scale);
		Log.d(TAG, "scale int  = " + (int) scale);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		if (scale > 90) {
			scale = 90; // sicherstellen, dass scale nie > 100 wird.
		}
		do {
			scale = scale + 10;
			yourSelectedImage.compress(CompressFormat.JPEG, (int) scale, bos);
			fileByteArray = bos.toByteArray();
			Log.d(TAG, "new filesize=" + fileByteArray.length);
			scale = scale - 20; // falls file immer noch grösser "targetSize"
								// scale verkleinern
		} while (fileByteArray.length >= targetSize);

		return fileByteArray;
	}

	/**
	 * Save MMS parts. Image, etc....
	 * 
	 * @param context
	 *            {@link Context}
	 */
	public void saveMmsParts(final Context context) {
		final ContentResolver cr = context.getContentResolver();

		// // query (Uri uri, String[] projection, String selection, String[]
		// // selectionArgs, String sortOrder)
		// Cursor cursor = cr.query(MMS_URI_PARTS, PROJECTION_PARTS, null, null,
		// null);
		//
		// if (cursor == null || !cursor.moveToFirst()) {
		// return;
		// }
		// final int iID = cursor.getColumnIndex(PROJECTION_PARTS[INDEX_ID]);
		// final int iCT = cursor.getColumnIndex(PROJECTION_PARTS[INDEX_CT]);
		// final int iText = cursor.getColumnIndex("text");
		//
		// ContentValues initialValues = this.createContentValues(stationIcon,
		// stationName);
		// cr.insert(MMS_URI_PARTS, initialValues);
		//
		// final int pid = cursor.getInt(iID);
		// final String ct = cursor.getString(iCT);
		// Log.d(TAG, "part: " + pid + " " + ct);
		//
		// // get part
		// InputStream is = null;
		//
		// final Uri uri = ContentUris.withAppendedId(MMS_URI_PARTS, pid);
		// try {
		// is = cr.openInputStream(uri);
		// } catch (IOException e) {
		// Log.e(TAG, "Failed to load part data", e);
		// }
		// if (is == null) {
		// Log.i(TAG, "InputStream for part " + pid + " is null");
		// if (iText >= 0 && ct.startsWith("text/")) {
		// this.body = cursor.getString(iText);
		// }
		// continue;
		// }
		// if (ct == null) {
		// continue;
		// }
		// if (ct.startsWith("image/")) {
		// this.picture = BitmapFactory.decodeStream(is);
		// final Intent i = new Intent(Intent.ACTION_VIEW);
		// i.setDataAndType(uri, ct);
		// i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		// this.contentIntent = i;
		// continue; // skip the rest
		// }
		//
		// if (is != null) {
		// try {
		// is.close();
		// } catch (IOException e) {
		// Log.e(TAG, "Failed to close stream", e);
		// } // Ignore
		// }
	}

	private ContentValues createContentValues(final int stationIcon,
			final String stationName) {
		ContentValues values = new ContentValues();
		// values.put(KEY_STATION_ICON, stationIcon);
		// values.put(KEY_STATION_NAME, stationName);
		return values;
	}

}
