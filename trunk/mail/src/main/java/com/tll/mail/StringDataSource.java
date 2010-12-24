package com.tll.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * Simple string-based implementation of javax.activation.DataSource interface.
 * @author jpk
 */
public class StringDataSource implements DataSource {

	private String name;
	private String data;
	private String contentType;

	/**
	 * 
	 */
	public StringDataSource() {
		super();
	}

	public StringDataSource(String name, String data, String contentType) {
		super();
		this.name = name;
		this.data = data;
		this.contentType = contentType;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public InputStream getInputStream() {
		// Reader reader = new StringReader(data);
		return new ByteArrayInputStream(data.getBytes());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		throw new IOException("No output streams available for this type of data source.");
	}

}
