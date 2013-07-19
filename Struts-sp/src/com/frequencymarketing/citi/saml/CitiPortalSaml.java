package com.frequencymarketing.citi.saml;

import java.io.ByteArrayInputStream;

import org.opensaml.SAMLResponse;

import com.frequencymarketing.common.model.dao.MemberSAMLSponsorAccountsData;
import com.frequencymarketing.common.saml.authentication.Saml;
import com.frequencymarketing.common.saml.authentication.SamlProps;

public class CitiPortalSaml implements Saml {

	
	
    public CitiPortalProps m_samlProps;
    public CryptoXml m_cryptoXml;
	
	//public MemberSAMLSponsorAccountsData processCitiLoginResponse(String loginSAMLResponse) throws Exception
    public SAMLResponse processCitiLoginResponse(String loginSAMLResponse) throws Exception
	{
	MemberSAMLSponsorAccountsData samlData = new MemberSAMLSponsorAccountsData();
	
	String strSamlResponse = m_cryptoXml.decrypt(loginSAMLResponse);
	//s_logger.debug("Decrypted Login SAML Response: " + strSamlResponse);
	
	String customPayload = null;
	
	SAMLResponse response = new SAMLResponse(new ByteArrayInputStream(strSamlResponse.getBytes()),1);
	System.out.println(response);
	return response;
	}

	@Override
	public byte[] getSAMLResponseAsBase64(Object memberData, String clientIp)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSAMLResponseAsString(Object memberData, String clientIp)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSamlProps(SamlProps a_samlProps) {
		  m_samlProps = (CitiPortalProps)a_samlProps;
		
	}
	 public CitiPortalProps getSamlProps()
	    {
	        return m_samlProps;
	    }

	    public void setSamlProps(CitiPortalProps a_samlProps)
	    {
	        m_samlProps = a_samlProps;
	    }

	    

	    public CryptoXml getCryptoXml()
		{
			return m_cryptoXml;
		}

		public void setCryptoXml(CryptoXml a_cryptoXml)
		{
			m_cryptoXml = a_cryptoXml;
		}
}
