/*
 * @(#) UsmSaml.java Aug 04, 2008
 * Copyright 2005 Frequency Marketing, Inc. All rights reserved.
 * Frequency Marketing, Inc. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.frequencymarketing.citi.saml;

import java.security.KeyPair;
import java.security.KeyStore;
import java.util.Collections;
import java.util.Date;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignature;

import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLAudienceRestrictionCondition;
import org.opensaml.SAMLAuthenticationStatement;
import org.opensaml.SAMLIdentifier;
import org.opensaml.SAMLIdentifierFactory;
import org.opensaml.SAMLNameIdentifier;
import org.opensaml.SAMLResponse;
import org.opensaml.SAMLSubject;
import org.opensaml.XML;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

import com.frequencymarketing.common.model.TyUsmSamlProfileData;
import com.frequencymarketing.common.saml.authentication.Saml;
import com.frequencymarketing.common.saml.authentication.SamlProps;
import com.frequencymarketing.common.saml.misc.CryptoKeystoreUtil;

public class UsmSaml implements Saml
{
    public String m_usmURL;
    public SamlProps m_samlProps;
    public CryptoXml cryptoXml;
    
    protected final Log s_logger = LogFactory.getLog(UsmSaml.class);

    public byte[] getSAMLResponseAsBase64(Object a_memberData, String a_clientIp) throws Exception
    {
        return getSAMLResponse((TyUsmSamlProfileData)a_memberData, a_clientIp).toBase64();
    }

    public String getSAMLResponseAsString(Object a_memberData, String a_clientIp) throws Exception
    {   
    	String resp = getSAMLResponse((TyUsmSamlProfileData)a_memberData, a_clientIp).toString();
        return cryptoXml.encrypt(resp);
    }

    private SAMLResponse getSAMLResponse(TyUsmSamlProfileData memberData, String a_clientIp) throws Exception
    {
        String memberId = memberData.getMemberId();
        SAMLIdentifier idgen = SAMLIdentifierFactory.getInstance();
        SAMLResponse samlResponse = new SAMLResponse();
        SAMLAssertion samlAssertion = new SAMLAssertion();
        SAMLAuthenticationStatement samlAuthenticationStatement = new SAMLAuthenticationStatement();
        SAMLSubject samlSubject = new SAMLSubject(new SAMLNameIdentifier(
                memberId, null, null), Collections
            .singleton(SAMLSubject.CONF_BEARER), null, null);

        samlAuthenticationStatement.setSubjectIP(a_clientIp);
        samlAuthenticationStatement.setSubject(samlSubject);
        samlAuthenticationStatement.setAuthInstant(new Date());
        samlAuthenticationStatement
            .setAuthMethod(SAMLAuthenticationStatement.AuthenticationMethod_Password);
        samlAssertion.addStatement(samlAuthenticationStatement);
        samlAssertion.setId(idgen.getIdentifier());
        samlAssertion.setIssuer(getSamlProps().getIssuer());
        samlAssertion.setNotBefore(new Date(System.currentTimeMillis() - 30000));
        samlAssertion.setNotOnOrAfter(new Date(
                System.currentTimeMillis() + 90000));//2 minutes
        samlAssertion
            .addCondition(new SAMLAudienceRestrictionCondition(Collections
                .singleton(getSamlProps().getAudienceRestriction())));
        
        //NameIdentifier is the Member id.
        SAMLAttributeStatement samlsaStatement = new SAMLAttributeStatement();
        SAMLSubject l_subject2 = new SAMLSubject(new SAMLNameIdentifier(
                memberId, null, null), Collections
            .singleton(SAMLSubject.CONF_BEARER), null, null);

        samlsaStatement.setSubject(l_subject2);
        samlsaStatement = addToAttributeStatement(samlsaStatement, "member_id",
                memberId, null, XML.SAML_NS);

        samlsaStatement = addToAttributeStatement(samlsaStatement, "agent_id",
                memberData.getAgentId(), null,
                XML.SAML_NS);

        samlsaStatement = addToAttributeStatement(samlsaStatement, "mbr_name_first",
                memberData.getFirstName(), null,
                XML.SAML_NS);
        samlsaStatement = addToAttributeStatement(samlsaStatement, "mbr_name_last",
                memberData.getLastName(), null,
                XML.SAML_NS);

        samlsaStatement = addToAttributeStatement(samlsaStatement, "point_balance",
                memberData.getPointBalance(), null, XML.SAML_NS);
        samlAssertion.addStatement(samlsaStatement);
        samlResponse.addAssertion(samlAssertion);
        samlResponse.setId(idgen.getIdentifier());

        //Load the KeyStore
        KeyStore keystore = CryptoKeystoreUtil.getKeyStore(getSamlProps()
            .getKeystore(), getSamlProps().getKeystorePass().toCharArray());

        KeyPair keyPair = CryptoKeystoreUtil.getKeyPair(keystore, getSamlProps().getKeystoreAlias(),
                getSamlProps().getKeystorePass().toCharArray());

        samlResponse.sign(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1, keyPair
            .getPrivate(), null);

        s_logger.debug(samlResponse.toString());
        
       
		System.out.println(samlResponse);
		
        return samlResponse;
    }

    private static SAMLAttributeStatement addToAttributeStatement(
        SAMLAttributeStatement a_saStatement, String a_attrName,
        String a_attrValue, QName a_attrType, String a_attrNamespace)
            throws Exception
    {
        SAMLAttribute samlAttr = new SAMLAttribute();
        samlAttr.setName(a_attrName);
        samlAttr.addValue(a_attrValue);
        samlAttr.setType(a_attrType);
        samlAttr.setNamespace(a_attrNamespace);
        a_saStatement.addAttribute(samlAttr);
        return a_saStatement;
    }

    public String getPartnerURL()
    {
        return m_usmURL;
    }

    public void setPartnerURL(String a_partnerURL)
    {
        m_usmURL = a_partnerURL;
    }

    public SamlProps getSamlProps()
    {
        return m_samlProps;
    }

    public void setSamlProps(SamlProps a_samlProps)
    {
        m_samlProps = a_samlProps;
    }
    public CryptoXml getCryptoXml()
   	{
   		return cryptoXml;
   	}

   	public void setCryptoXml(CryptoXml a_cryptoXml)
   	{
   		cryptoXml = a_cryptoXml;
   	}
}
