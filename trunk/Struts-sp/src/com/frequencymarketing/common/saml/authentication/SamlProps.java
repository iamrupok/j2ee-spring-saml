package com.frequencymarketing.common.saml.authentication;

/**
 * All the properties are Configured in Spring.
 */
public class SamlProps
{
    public String m_keystore;
    public String m_issuer;
    public String m_audienceRestriction;
    public String m_samlPostURL;
    public String m_keystorePass;
    public String m_keystoreAlias;
    public String m_sessionStateURL;
    public String m_logoutURL;
    public String m_recipientURL;
    
	public String getSamlPostURL()
    {
        return m_samlPostURL;
    }

    public void setSamlPostURL(String a_samlPostURL)
    {
        m_samlPostURL = a_samlPostURL;
    }

    public String getAudienceRestriction()
    {
        return m_audienceRestriction;
    }

    public void setAudienceRestriction(String a_audienceRestriction)
    {
        m_audienceRestriction = a_audienceRestriction;
    }

    public String getIssuer()
    {
        return m_issuer;
    }

    public void setIssuer(String a_issuer)
    {
        m_issuer = a_issuer;
    }

    public String getKeystore()
    {
        return m_keystore;
    }

    public void setKeystore(String a_keystore)
    {
        m_keystore = a_keystore;
    }

    /**
     * @return Returns the keystorePass.
     */
    public String getKeystorePass()
    {
        return m_keystorePass;
    }

    /**
     * @param a_keystorePass The keystorePass to set.
     */
    public void setKeystorePass(String a_keystorePass)
    {
        m_keystorePass = a_keystorePass;
    }

    public String getSessionStateURL()
    {
        return m_sessionStateURL;
    }

    public void setSessionStateURL(String stateURL)
    {
        m_sessionStateURL = stateURL;
    }

	public String getKeystoreAlias() 
	{
		return m_keystoreAlias;
	}

	public void setKeystoreAlias(String keystoreAlias) 
	{
		this.m_keystoreAlias = keystoreAlias;
	}

	public String getLogoutURL() {
		return m_logoutURL;
	}

	public void setLogoutURL(String logoutURL) {
		this.m_logoutURL = logoutURL;
	}

    public String getRecipientURL() {
		return m_recipientURL;
	}

	public void setRecipientURL(String recipientURL) {
		this.m_recipientURL = recipientURL;
	}
}
