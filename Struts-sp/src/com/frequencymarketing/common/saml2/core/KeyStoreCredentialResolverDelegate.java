package com.frequencymarketing.common.saml2.core;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.CredentialResolver;
import org.opensaml.xml.security.credential.KeyStoreCredentialResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * 
 * Trivial implementation of CredentialResolver.  Not recommended for production use as
 * it is not secure.
 * 
 * This class loads a Java keystore from spring config and instantiates 
 * an Open SAML KeyStoreCredentialResolver.  All calls are then delegated to the
 * KeyStoreCredentialResolver.
 * 
 * 
 * @author 
 *
 */
public class KeyStoreCredentialResolverDelegate implements CredentialResolver  {

	
	private KeyStoreCredentialResolver  keyStoreCredentialResolver;

	private String keystorePassword;
	private Map<String,String> privateKeyPasswordsByAlias;


	
	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	
	public void setPrivateKeyPasswordsByAlias(
			Map<String, String> privateKeyPasswordsByAlias) {
		this.privateKeyPasswordsByAlias = privateKeyPasswordsByAlias;
	}

	public Iterable<Credential> resolve(CriteriaSet criteriaSet)
			throws SecurityException {
		return keyStoreCredentialResolver.resolve(criteriaSet);
	}

	public Credential resolveSingle(CriteriaSet criteriaSet) throws SecurityException {
		return keyStoreCredentialResolver.resolveSingle(criteriaSet);
	}

	/*public void afterPropertiesSet() throws Exception {
		
		KeyStore ks = KeyStore.getInstance("JKS");
		
		ks.load(new ByteArrayInputStream(Base64.decodeBase64(b64EncodedKeystore.getBytes())), keystorePassword.toCharArray());

		keyStoreCredentialResolver = new KeyStoreCredentialResolver(ks, privateKeyPasswordsByAlias);
	}*/

	public void setKeyStoreCredentialResolver(
			KeyStoreCredentialResolver keyStoreCredentialResolver) {
		this.keyStoreCredentialResolver = keyStoreCredentialResolver;
	}

}
