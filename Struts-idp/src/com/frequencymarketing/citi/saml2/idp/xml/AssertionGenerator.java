package com.frequencymarketing.citi.saml2.idp.xml;



import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;

import com.frequencymarketing.common.model.TySamlProfileData;
import com.frequencymarketing.common.model.TyUsmSamlProfileData;
import com.frequencymarketing.common.saml2.util.IDService;
import com.frequencymarketing.common.saml2.util.TimeService;
import com.frequencymarketing.common.saml2.xml.IssuerGenerator;


public class AssertionGenerator {

	private final XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

	private final IssuerGenerator issuerGenerator;
	private final SubjectGenerator subjectGenerator;
	private final IDService idService;
	private final TimeService timeService;
	private final AuthnStatementGenerator authnStatementGenerator = new AuthnStatementGenerator();
	private final AttributeStatementGenerator attributeStatementGenerator = new AttributeStatementGenerator(); 
	
	
	public AssertionGenerator(String issuingEntityName, TimeService timeService, IDService idService) {
		super();
	
		this.timeService = timeService;
		this.idService = idService;
		issuerGenerator = new IssuerGenerator(issuingEntityName);
		subjectGenerator = new SubjectGenerator(timeService);
	}
	
	public  Assertion generateAssertionTyUsm (TyUsmSamlProfileData memberData, String recepientAssertionConsumerURL, int validForInMinutes,  String inResponseTo,String clientIp ,DateTime authnInstant) {
		
		// To  Do
		/*
		UserDetails principal =	(UserDetails) authToken.getPrincipal();
		WebAuthenticationDetails details = (WebAuthenticationDetails) authToken.getDetails();
		*/
		AssertionBuilder assertionBuilder = (AssertionBuilder)builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
		Assertion assertion = assertionBuilder.buildObject();
		
		Subject subject = subjectGenerator.generateSubject(recepientAssertionConsumerURL, validForInMinutes, memberData.getMemberId(), inResponseTo, clientIp);
		
		Issuer issuer = issuerGenerator.generateIssuer();
		
		AuthnStatement authnStatement = authnStatementGenerator.generateAuthnStatement(authnInstant);
		
		assertion.setIssuer(issuer);
		assertion.getAuthnStatements().add(authnStatement);
		assertion.setSubject(subject);

		assertion.getAttributeStatements().add(attributeStatementGenerator.generateAttributeStatementTyUsm(memberData));
		
		assertion.setID(idService.generateID());
		assertion.setIssueInstant(timeService.getCurrentDateTime());
		return assertion;
	}
	
public  Assertion generateAssertionTy (TySamlProfileData memberData, String recepientAssertionConsumerURL, int validForInMinutes,  String inResponseTo,String clientIp, DateTime authnInstant) {
		
		// To  Do
		/*
		UserDetails principal =	(UserDetails) authToken.getPrincipal();
		WebAuthenticationDetails details = (WebAuthenticationDetails) authToken.getDetails();
		*/
		AssertionBuilder assertionBuilder = (AssertionBuilder)builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
		Assertion assertion = assertionBuilder.buildObject();
		
		Subject subject = subjectGenerator.generateSubject(recepientAssertionConsumerURL, validForInMinutes, memberData.getMemberId(), inResponseTo, clientIp);
		
		Issuer issuer = issuerGenerator.generateIssuer();
		
		AuthnStatement authnStatement = authnStatementGenerator.generateAuthnStatement(authnInstant);
		
		assertion.setIssuer(issuer);
		assertion.getAuthnStatements().add(authnStatement);
		assertion.setSubject(subject);

		assertion.getAttributeStatements().add(attributeStatementGenerator.generateAttributeStatementTy(memberData));
		
		assertion.setID(idService.generateID());
		assertion.setIssueInstant(timeService.getCurrentDateTime());
		return assertion;
	}
}
