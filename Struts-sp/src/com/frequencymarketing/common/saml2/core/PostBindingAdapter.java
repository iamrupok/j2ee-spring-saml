package com.frequencymarketing.common.saml2.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.Configuration;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.binding.decoding.SAMLMessageDecoder;
import org.opensaml.common.binding.encoding.SAMLMessageEncoder;
import org.opensaml.saml2.binding.encoding.HTTPPostSimpleSignEncoder;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.opensaml.ws.transport.http.HttpServletResponseAdapter;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.binding.decoding.SAMLMessageDecoder;
import org.opensaml.common.binding.encoding.SAMLMessageEncoder;
import org.opensaml.saml2.binding.encoding.HTTPPostSimpleSignEncoder;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.opensaml.ws.transport.http.HttpServletResponseAdapter;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import com.frequencymarketing.common.saml.authentication.SamlProps;

public class PostBindingAdapter implements BindingAdapter{

	static final String SAML_REQUEST_POST_PARAM_NAME = "SAMLRequest";
	static final String SAML_RESPONSE_POST_PARAM_NAME = "SAMLResponse";

	//private VelocityEngine velocityEngine;

	private final SAMLMessageDecoder decoder;
	SAMLMessageEncoder encoder;	
	//private final String issuingEntityName;
	//private final SecurityPolicyResolver resolver;
	
	
	
	/*public PostBindingAdapter(SAMLMessageDecoder decoder,
			String issuingEntityName, SecurityPolicyResolver resolver) {
		super();
		this.decoder = decoder;
		this.issuingEntityName = issuingEntityName;
		this.resolver = resolver;
	}*/
	public PostBindingAdapter(SAMLMessageDecoder decoder,SAMLMessageEncoder encoder ) {
		super();
		this.decoder = decoder;
		this.encoder = encoder;
		//this.issuingEntityName = issuingEntityName;
		//this.resolver = resolver;
	}


	/*@Required
	public void setVelocityEngine(
			VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}*/
	

	@Override
	public SAMLMessageContext extractSAMLMessageContext(HttpServletRequest request, SecurityPolicyResolver resolver) throws MessageDecodingException, SecurityException {
		
		BasicSAMLMessageContext messageContext = new BasicSAMLMessageContext();
		
		messageContext.setInboundMessageTransport(new HttpServletRequestAdapter(request));
		messageContext.setSecurityPolicyResolver(resolver);

		decoder.decode(messageContext);
		
		return	messageContext;
		
		

	}

	@Override
	public void sendSAMLMessage(SignableSAMLObject samlMessage,
								Endpoint endpoint, 
								Credential signingCredential,
								HttpServletResponse response, SamlProps samlProps) throws MessageEncodingException {
		
		HttpServletResponseAdapter outTransport = new HttpServletResponseAdapter(response, false);
		
		BasicSAMLMessageContext messageContext = new BasicSAMLMessageContext();
		
		messageContext.setOutboundMessageTransport(outTransport);
		messageContext.setPeerEntityEndpoint(endpoint);
		messageContext.setOutboundSAMLMessage(samlMessage);
		
		
		/************************** Print the Response as XML String *****************************************/
		/*
		ResponseMarshaller marshaller = new ResponseMarshaller();
		Element plain;
		try {
			plain = marshaller.marshall(samlMessage);
			// 
			String samlResponse = XMLHelper.nodeToString(plain);
			System.out.println(samlResponse);
		} catch (MarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/*// init xml handling
				DOMImplementationRegistry registry;
				DOMImplementationLS domImpl;
				LSSerializer writer;
				try {
					registry = DOMImplementationRegistry.newInstance();
					domImpl =  (DOMImplementationLS) registry.getDOMImplementation("LS");
					 writer = domImpl.createLSSerializer();
						
						MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();
						Marshaller marshaller = marshallerFactory.getMarshaller(samlMessage);
					
						Element root;
						try {
							root = marshaller.marshall(samlMessage);
							System.out.println(writer.writeToString(root));
						} catch (MarshallingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
				} catch (ClassCastException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				*/
				
		
		/**************************************************************/
		
		messageContext.setOutboundMessageIssuer(samlProps.getIssuer());
		messageContext.setOutboundSAMLMessageSigningCredential(signingCredential);
		
		
		encoder.encode(messageContext);
		
	}


	@Override
	public String extractSAMLMessage(HttpServletRequest request) {
	    
		if(StringUtils.isNotBlank(request.getParameter(SAML_REQUEST_POST_PARAM_NAME)))
			return request.getParameter(SAML_REQUEST_POST_PARAM_NAME);
		else
			return request.getParameter(SAML_RESPONSE_POST_PARAM_NAME);
		
	}
	
	
}





