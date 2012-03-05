package de.ub0r.android.websms.connector.common;

/**
 * 
 */
import java.io.InputStream;

import org.apache.http.entity.mime.content.InputStreamBody;

class InputSreamKnownFileSize extends InputStreamBody {
	private int lenght;

	public InputSreamKnownFileSize(final InputStream in, final int lenght,
			final String mimeType, final String filename) {
		super(in, mimeType, filename);
		this.lenght = lenght;
	}

	@Override
	public long getContentLength() {
		return this.lenght;
	}
}
