package com.frequencymarketing.common.saml2.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.common.SignableSAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;

import com.frequencymarketing.common.saml.authentication.SamlProps;

/**
 * 
 * Abstracts the SAML Binding used to send/receive messages.
 * 
 * @author 
 *
 */
public interface BindingAdapter {

	public void sendSAMLMessage(SignableSAMLObject samlMessage, Endpoint endpoint, 	Credential signingCredential, HttpServletResponse response, SamlProps samlProps) throws MessageEncodingException;
	
	public SAMLMessageContext extractSAMLMessageContext(HttpServletRequest request,SecurityPolicyResolver resolver) throws MessageDecodingException, SecurityException;

	public String extractSAMLMessage(HttpServletRequest request);
	
}
