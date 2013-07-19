package com.frequencymarketing.citi.saml2.idp.xml;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.AudienceBuilder;
import org.opensaml.saml2.core.impl.AudienceRestrictionBuilder;
import org.opensaml.saml2.core.impl.ConditionsBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;

import com.frequencymarketing.common.saml2.util.TimeService;

public class ConditionsGenerator {

	private final XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
	
	public ConditionsGenerator() {
		super();
		
	}

	public Conditions generateCondition(  int validForInMinutes, String audienceRestriction) {
		
		  DateTime now = new DateTime();
		  ConditionsBuilder conditionsBuilder = (ConditionsBuilder) builderFactory.getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
		  Conditions conditions = conditionsBuilder.buildObject();
		  conditions.setNotBefore(now);
		  conditions.setNotOnOrAfter(now.plusMinutes(validForInMinutes));
		  
		  if (audienceRestriction != null) {
			    AudienceBuilder audienceBuilder = (AudienceBuilder) builderFactory.getBuilder(Audience.DEFAULT_ELEMENT_NAME);
			    AudienceRestrictionBuilder audienceRestrictionBuilder = (AudienceRestrictionBuilder)builderFactory.getBuilder(AudienceRestriction.DEFAULT_ELEMENT_NAME);
	            Audience audience = audienceBuilder.buildObject();
	            audience.setAudienceURI(audienceRestriction);
	            AudienceRestriction audienceRestrictions = audienceRestrictionBuilder.buildObject();
	            audienceRestrictions.getAudiences().add(audience);

	            conditions.getAudienceRestrictions().add(audienceRestrictions);
	        }
		  
		return conditions;
		
	}
		
		
	
}
