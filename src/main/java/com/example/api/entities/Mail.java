package com.example.api.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * A simple Entity to work with MailService
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 * 
 * @see {@link MailService}
 *
 */
public class Mail {

	public static final String QUEUED = "-1";
	public static final String NOT_SENT = "0";
	public static final String SENT = "1";

	private Long id;

	private String fromName;

	private String toName;

	private Date createdAt;

	private Date updatedAt;

	private String subject;

	private String body;

	private String status;

	private String templateName;
	
	private String actionSender;
	
	private String errorMessage;

	private Map<String, String> attributes;
	
	private Object object;

	
	public Mail() {
		this.attributes = new HashMap<String, String>();
		this.onCreate();
	}

	public Mail(String templateName) {
		this.templateName = templateName;
	}

	private void onCreate() {
		updatedAt = createdAt = new Date();
	}

	protected void onUpdate() {
		updatedAt = new Date();
	}


	public boolean isUnsentStatus(){
		return this.getStatus().equals(NOT_SENT);
	}

	public boolean isSentStatus(){
		return this.getStatus().equals(SENT);
	}


	public void addAttribute(Map<String, String> attribute) {
		this.getAttributes().putAll(attribute);
	}

	public void addAttribute(String key, String value) {
		this.getAttributes().put(key, StringUtils.substring(value, 0, 254));
	}


	/**
	 * @return the error_message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the error_message to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = StringUtils.substring(errorMessage, 0, 255);
	}

	/**
	 * 
	 */
	public void unsetErrorMessage() {
		
		this.errorMessage = null;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the fromName
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * @param fromName the fromName to set
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * @return the toName
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * @param toName the toName to set
	 */
	public void setToName(String toName) {
		this.toName = toName;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the actionSender
	 */
	public String getActionSender() {
		return actionSender;
	}

	/**
	 * @param actionSender the actionSender to set
	 */
	public void setActionSender(String actionSender) {
		this.actionSender = actionSender;
	}

	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	
}
