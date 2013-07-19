package com.frequencymarketing.citi.saml2.idp.xml;

import java.util.Collection;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringBuilder;


import com.frequencymarketing.common.model.TySamlProfileData;
import com.frequencymarketing.common.model.TyUsmSamlProfileData;


public class AttributeStatementGenerator {

	private final XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
	private final String[] tyUsmAtrributeProperty = {"member_id", "agent_id","mbr_name_first", "mbr_name_last", "point_balance"};
	private final String[] tyAtrributeProperty = {"member_id", "agent_id","mbr_name_first", "mbr_name_last", "point_balance","email_address"};
	// To  Do
	public AttributeStatement generateAttributeStatementTyUsm(TyUsmSamlProfileData memberProfile) {
		
		AttributeStatementBuilder attributeStatementBuilder = (AttributeStatementBuilder) builderFactory.getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
		AttributeStatement attributeStatement = attributeStatementBuilder.buildObject();

		for (String tyUsmAtrributeName : tyUsmAtrributeProperty) {
			

			//Response/Assertion/AttributeStatement/Attribute
			AttributeBuilder attributeBuilder = (AttributeBuilder)  builderFactory.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
			Attribute attribute = attributeBuilder.buildObject();
			attribute.setName(tyUsmAtrributeName);
			
			//urn:oasis:names:tc:SAML:2.0:attrname-format:basic
			attribute.setNameFormat(Attribute.BASIC);
			//this was convoluted to figure out
			//Response/Assertion/AttributeStatement/Attribute/AttributeValue
			XSStringBuilder stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
			XSString stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
			if(tyUsmAtrributeName.equals("member_id")){
				stringValue.setValue(memberProfile.getMemberId());
			}
			else if(tyUsmAtrributeName.equals("agent_id")){
				stringValue.setValue(memberProfile.getAgentId());
			}
			else if(tyUsmAtrributeName.equals("mbr_name_first")){
				stringValue.setValue(memberProfile.getFirstName());
			}
			else if(tyUsmAtrributeName.equals("mbr_name_last")){
				stringValue.setValue(memberProfile.getLastName());
			}
			else if(tyUsmAtrributeName.equals("point_balance")){
				stringValue.setValue(memberProfile.getPointBalance());
			}
			

			attribute.getAttributeValues().add(stringValue);
			attributeStatement.getAttributes().add(attribute);
		}
		
		
		
		return attributeStatement;
	}
	
	
	
public AttributeStatement generateAttributeStatementTy(TySamlProfileData memberProfile) {
		
		AttributeStatementBuilder attributeStatementBuilder = (AttributeStatementBuilder) builderFactory.getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
		AttributeStatement attributeStatement = attributeStatementBuilder.buildObject();

		for (String tyAtrributeName : tyAtrributeProperty) {
			

			//Response/Assertion/AttributeStatement/Attribute
			AttributeBuilder attributeBuilder = (AttributeBuilder)  builderFactory.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
			Attribute attribute = attributeBuilder.buildObject();
			attribute.setName(tyAtrributeName);
			
			//urn:oasis:names:tc:SAML:2.0:attrname-format:basic
			attribute.setNameFormat(Attribute.BASIC);
			//this was convoluted to figure out
			//Response/Assertion/AttributeStatement/Attribute/AttributeValue
			XSStringBuilder stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
			XSString stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
			if(tyAtrributeName.equals("member_id")){
				stringValue.setValue(memberProfile.getMemberId());
			}
			else if(tyAtrributeName.equals("agent_id")){
				stringValue.setValue(memberProfile.getAgentId());
			}
			else if(tyAtrributeName.equals("mbr_name_first")){
				stringValue.setValue(memberProfile.getFirstName());
			}
			else if(tyAtrributeName.equals("mbr_name_last")){
				stringValue.setValue(memberProfile.getLastName());
			}
			else if(tyAtrributeName.equals("point_balance")){
				stringValue.setValue(memberProfile.getPointBalance());
			}
			else if(tyAtrributeName.equals("email_address")){
				stringValue.setValue(memberProfile.getEmailAddress());
			}
			

			attribute.getAttributeValues().add(stringValue);
			attributeStatement.getAttributes().add(attribute);
		}
		
		return attributeStatement;
	}
	
}