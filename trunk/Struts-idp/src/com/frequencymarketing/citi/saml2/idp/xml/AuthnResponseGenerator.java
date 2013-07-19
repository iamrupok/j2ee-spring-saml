package com.frequencymarketing.citi.saml2.idp.xml;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.impl.ResponseBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;



import com.frequencymarketing.common.model.TySamlProfileData;
import com.frequencymarketing.common.model.TyUsmSamlProfileData;
import com.frequencymarketing.common.saml2.util.IDService;
import com.frequencymarketing.common.saml2.util.TimeService;
import com.frequencymarketing.common.saml2.xml.IssuerGenerator;


public class AuthnResponseGenerator {
	
	private final XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

	private final String issuingEntityName;
	
	private final IssuerGenerator issuerGenerator;
	private final AssertionGenerator assertionGenerator;
	private final IDService idService;
	private final TimeService timeService;
	private final ConditionsGenerator conditionsGenerator;
	
	StatusGenerator statusGenerator;
	
	public AuthnResponseGenerator(String issuingEntityName, TimeService timeService, IDService idService) {
		super();
		this.issuingEntityName = issuingEntityName;
		this.idService = idService;
		this.timeService = timeService;
		issuerGenerator = new IssuerGenerator(issuingEntityName);
		assertionGenerator = new AssertionGenerator(issuingEntityName, timeService, idService);
		statusGenerator = new StatusGenerator();
		conditionsGenerator = new ConditionsGenerator();
	}


	public Response generateAuthnResponseTyUsm(TyUsmSamlProfileData memberData, String recepientAssertionConsumerURL,String audienceRestriction , int validForInMinutes, String inResponseTo, String clientIp, DateTime authnInstant){
		
		//UserDetails userDetails = (UserDetails) authToken.getPrincipal();
		
		ResponseBuilder responseBuilder = (ResponseBuilder) builderFactory.getBuilder(Response.DEFAULT_ELEMENT_NAME);
		Response authResponse = responseBuilder.buildObject();

		Issuer responseIssuer = issuerGenerator.generateIssuer();
		
		Conditions conditions = conditionsGenerator.generateCondition(validForInMinutes,audienceRestriction);
		Assertion assertion = assertionGenerator.generateAssertionTyUsm(memberData, recepientAssertionConsumerURL, validForInMinutes, inResponseTo, clientIp, authnInstant);
		assertion.setConditions(conditions);
		
		authResponse.setIssuer(responseIssuer);
		authResponse.setID(idService.generateID());
		authResponse.setIssueInstant(timeService.getCurrentDateTime());
		authResponse.setInResponseTo(inResponseTo);
		authResponse.getAssertions().add(assertion);
		authResponse.setDestination(recepientAssertionConsumerURL);
		authResponse.setStatus(statusGenerator.generateStatus(StatusCode.SUCCESS_URI));
		return authResponse;
	}
	public Response generateAuthnResponseTy(TySamlProfileData memberData, String recepientAssertionConsumerURL, int validForInMinutes, String inResponseTo, String clientIp, DateTime authnInstant){
		
		//UserDetails userDetails = (UserDetails) authToken.getPrincipal();
		
		ResponseBuilder responseBuilder = (ResponseBuilder) builderFactory.getBuilder(Response.DEFAULT_ELEMENT_NAME);
		Response authResponse = responseBuilder.buildObject();

		Issuer responseIssuer = issuerGenerator.generateIssuer();
		
		Assertion assertion = assertionGenerator.generateAssertionTy(memberData, recepientAssertionConsumerURL, validForInMinutes, inResponseTo, clientIp, authnInstant);
		
		
		authResponse.setIssuer(responseIssuer);
		authResponse.setID(idService.generateID());
		authResponse.setIssueInstant(timeService.getCurrentDateTime());
		authResponse.setInResponseTo(inResponseTo);
		authResponse.getAssertions().add(assertion);
		authResponse.setDestination(recepientAssertionConsumerURL);
		authResponse.setStatus(statusGenerator.generateStatus(StatusCode.SUCCESS_URI));
		return authResponse;
	}
	
	
}
	