package com.tll.mail;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;

/**
 * The abstract mail context. Holds routing, encoding and attachment data.
 * Concrete sub-classes must provide subject and message content.
 * @author jpk
 */
abstract class AbstractMailContext implements IMailContext {

	private final MailRouting routing;

	private final String encoding;

	private final List<Attachment> attachments = new ArrayList<Attachment>();

	boolean sent;

	/**
	 * Constructor
	 * @param routing
	 * @param encoding
	 */
	protected AbstractMailContext(MailRouting routing, String encoding) {
		super();
		this.routing = routing;
		this.encoding = encoding;
	}

	@Override
	public MailRouting getRouting() {
		return routing;
	}

	@Override
	public String getEncoding() {
		return encoding;
	}

	@Override
	public List<Attachment> getAttachments() {
		return attachments;
	}

	@Override
	public void addAttachment(String attachmentName, DataSource dataSource) {
		attachments.add(new Attachment(attachmentName, dataSource));
	}

	@Override
	public void addAttachment(String attachmentName, String data, String contentType) {
		final DataSource dataSource = new StringDataSource(attachmentName, data, contentType);
		addAttachment(attachmentName, dataSource);
	}

	@Override
	public void markSent() {
		this.sent = true;
	}

	@Override
	public boolean wasSent() {
		return sent;
	}

}