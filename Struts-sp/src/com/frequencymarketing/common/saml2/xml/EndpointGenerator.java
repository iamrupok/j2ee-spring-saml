package com.frequencymarketing.common.saml2.xml;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.xml.XMLObjectBuilderFactory;


public class EndpointGenerator {

	
	private static final Logger s_log = Logger.getLogger(EndpointGenerator.class);
	
	private XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
	
	public Endpoint generateEndpoint(QName service, String location, String responseLocation) {
		
		s_log.debug("end point service: {} "+ service);
		s_log.debug("end point location: {} "+ location);
		s_log.debug("end point responseLocation: {} "+ responseLocation);
		
		SAMLObjectBuilder<Endpoint> endpointBuilder = (SAMLObjectBuilder<Endpoint>) builderFactory.getBuilder(service);
		Endpoint samlEndpoint = endpointBuilder.buildObject();
		
        samlEndpoint.setLocation(location);
        
        //this does not have to be set
        if( StringUtils.isNotEmpty(responseLocation))
        	samlEndpoint.setResponseLocation(responseLocation);
        
        return samlEndpoint;
	}

}
