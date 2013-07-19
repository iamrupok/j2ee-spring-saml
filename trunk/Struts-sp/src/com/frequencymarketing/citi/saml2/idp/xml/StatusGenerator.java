package com.frequencymarketing.citi.saml2.idp.xml;

import org.opensaml.Configuration;

import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.saml2.core.impl.StatusMessageBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;

public class StatusGenerator {

	private final XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
	
	
	public Status generateStatus( String value ) {
		
		StatusBuilder builder = (StatusBuilder) builderFactory.getBuilder(Status.DEFAULT_ELEMENT_NAME);
		
		Status status =  (Status) builder.buildObject();
		
		StatusCodeBuilder codeBuilder = (StatusCodeBuilder) builderFactory.getBuilder(StatusCode.DEFAULT_ELEMENT_NAME);
		
		StatusCode statusCode = (StatusCode) codeBuilder.buildObject();
		statusCode.setValue(value);
		status.setStatusCode(statusCode);
		
		return status;
	}
	
	public Status generateStatus( String value, String subStatus, String message ) {
		
		StatusBuilder builder = (StatusBuilder) builderFactory.getBuilder(Status.DEFAULT_ELEMENT_NAME);
		
		Status status =  (Status) builder.buildObject();
	

		
		StatusCodeBuilder codeBuilder = (StatusCodeBuilder) builderFactory.getBuilder(StatusCode.DEFAULT_ELEMENT_NAME);
		StatusCode statusCode = (StatusCode) codeBuilder.buildObject();
		statusCode.setValue(value);
		
		StatusCode subStatusCode = (StatusCode) codeBuilder.buildObject();
		subStatusCode.setValue(subStatus);
		statusCode.setStatusCode(subStatusCode);
		
		
		status.setStatusCode(statusCode);
		
		StatusMessageBuilder statusMessageBuilder = (StatusMessageBuilder) builderFactory.getBuilder(StatusMessage.DEFAULT_ELEMENT_NAME);
		
		StatusMessage statusMessage = statusMessageBuilder.buildObject();
		
		statusMessage.setMessage(message);
		status.setStatusMessage(statusMessage);
		
		return status;
	}
	
}
