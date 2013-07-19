package com.frequencymarketing.common.saml.authentication;


/**
 * @author [Rajiv Verma]
 */
public interface Saml
{
    public byte[] getSAMLResponseAsBase64(Object memberData, String clientIp) throws Exception;

    public String getSAMLResponseAsString(Object memberData, String clientIp) throws Exception;
    
    
    public void setSamlProps(SamlProps a_samlProps);
}
