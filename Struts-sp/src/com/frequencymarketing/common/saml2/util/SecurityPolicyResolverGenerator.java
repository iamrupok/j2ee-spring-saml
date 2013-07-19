package com.frequencymarketing.common.saml2.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.ws.security.SecurityPolicyRule;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.CredentialResolver;
import org.opensaml.xml.security.credential.KeyStoreCredentialResolver;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.criteria.EntityIDCriteria;
import org.opensaml.xml.security.criteria.UsageCriteria;

import com.frequencymarketing.common.saml.authentication.SamlProps;
import com.frequencymarketing.common.saml.misc.CryptoKeystoreUtil;
import com.frequencymarketing.common.saml2.core.KeyStoreCredentialResolverDelegate;
import com.frequencymarketing.common.saml2.core.SecurityPolicyDelegate;
import com.frequencymarketing.common.saml2.core.SignatureSecurityPolicyRule;
import org.opensaml.ws.security.provider.StaticSecurityPolicyResolver;

public class SecurityPolicyResolverGenerator {

	private static Log s_logger = LogFactory.getLog(SecurityPolicyResolverGenerator.class);

	public static SecurityPolicyResolver generateSecurityPolicyResolver(SamlProps props, String entityIdCriteriaAlias) {
		
		StaticSecurityPolicyResolver staticSecurityPolicyResolver = null;

		try{
			
			// we could use a different adapter to send the response based on
			// request issuer...
			KeyStoreCredentialResolverDelegate keyStoreCredentialResolverDelegate = new KeyStoreCredentialResolverDelegate();
			keyStoreCredentialResolverDelegate.setKeystorePassword(props
					.getKeystorePass());

			// get private key from key store
			KeyStore keyStore = CryptoKeystoreUtil.getKeyStore(
					props.getKeystore(), props.getKeystorePass().toCharArray());
			//KeyPair keyPair = CryptoKeystoreUtil.getKeyPair(keyStore, props.getKeystoreAlias(), props.getKeystorePass().toCharArray());
			InputStream is = SecurityPolicyResolver.class.getClassLoader()
					.getResourceAsStream(props.getKeystore());
			
			keyStore.load(is, props.getKeystorePass().toCharArray());
			
			is.close();

			/*
			 * KeyStore.PrivateKeyEntry pkEntry = null; pkEntry =
			 * (KeyStore.PrivateKeyEntry)
			 * keyStore.getEntry(props.getKeystoreAlias(), new
			 * KeyStore.PasswordProtection
			 * (props.getKeystorePass().toCharArray())); PrivateKey pk =
			 * pkEntry.getPrivateKey(); System.out.println("Private Key" +
			 * pk.toString());
			 */

			Map<String, String> privateKeyPasswordsByAlias = new HashMap<String, String>();
			privateKeyPasswordsByAlias.put(entityIdCriteriaAlias,props.getKeystorePass());
			//privateKeyPasswordsByAlias.put(props.getIssuer(),props.getKeystorePass());

			keyStoreCredentialResolverDelegate
					.setPrivateKeyPasswordsByAlias(privateKeyPasswordsByAlias);

			KeyStoreCredentialResolver keyStoreCredentialResolver = new KeyStoreCredentialResolver(keyStore, privateKeyPasswordsByAlias);

			keyStoreCredentialResolverDelegate
					.setKeyStoreCredentialResolver(keyStoreCredentialResolver);
			CredentialResolver credentialResolver = keyStoreCredentialResolverDelegate;
			List<SecurityPolicyRule> securityPolicyRuleList = new ArrayList<SecurityPolicyRule>();
			CriteriaSet criteriaSet = new CriteriaSet();
			criteriaSet.add( new EntityIDCriteria(entityIdCriteriaAlias));
			criteriaSet.add( new UsageCriteria(UsageType.SIGNING) );
			
			SignatureSecurityPolicyRule  signatureSecurityPolicyRule = new SignatureSecurityPolicyRule(credentialResolver, new SAMLSignatureProfileValidator(),criteriaSet);
			securityPolicyRuleList.add(signatureSecurityPolicyRule);
			SecurityPolicyDelegate securityPolicyDelegate = new SecurityPolicyDelegate(securityPolicyRuleList);
			
			staticSecurityPolicyResolver = new StaticSecurityPolicyResolver(securityPolicyDelegate);
			
	}
			catch (NoSuchAlgorithmException e) {
				s_logger.error("Exception Occured while Generating SecurityPolicyResolver: "
						+ e.getMessage());
				//e.printStackTrace();
			} catch (CertificateException e) {
				s_logger.error("Exception Occured while Generating SecurityPolicyResolver: "
						+ e.getMessage());
				//e.printStackTrace();
			} catch (IOException e) {
				s_logger.error("Exception Occured while Generating SecurityPolicyResolver: "
						+ e.getMessage());
				//e.printStackTrace();
			}
		
		 
		
		return staticSecurityPolicyResolver;
		
	}


}
