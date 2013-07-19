package com.frequencymarketing.common.saml2.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.CredentialResolver;
import org.opensaml.xml.security.credential.KeyStoreCredentialResolver;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.criteria.EntityIDCriteria;
import org.opensaml.xml.security.criteria.UsageCriteria;
import org.opensaml.xml.signature.Signature;


import com.frequencymarketing.common.saml.authentication.SamlProps;
import com.frequencymarketing.common.saml.misc.CryptoKeystoreUtil;
import com.frequencymarketing.common.saml2.core.KeyStoreCredentialResolverDelegate;

public class SigningCredentialGenerator {

	private static Log s_logger = LogFactory.getLog(SigningCredentialGenerator.class);

	public static Credential generateSigningCredential(SamlProps props) {

		Credential signingCredential = null;

		try {

			// we could use a different adapter to send the response based on
			// request issuer...
			KeyStoreCredentialResolverDelegate keyStoreCredentialResolverDelegate = new KeyStoreCredentialResolverDelegate();
			keyStoreCredentialResolverDelegate.setKeystorePassword(props
					.getKeystorePass());

			// get private key from key store
			KeyStore keyStore = CryptoKeystoreUtil.getKeyStore(
					props.getKeystore(), props.getKeystorePass().toCharArray());
			KeyPair keyPair = CryptoKeystoreUtil.getKeyPair(keyStore, props
					.getKeystoreAlias(), props.getKeystorePass().toCharArray());
			InputStream is = SigningCredentialGenerator.class.getClassLoader()
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
			privateKeyPasswordsByAlias.put(props.getKeystoreAlias(),
					props.getKeystorePass());

			keyStoreCredentialResolverDelegate
					.setPrivateKeyPasswordsByAlias(privateKeyPasswordsByAlias);

			KeyStoreCredentialResolver keyStoreCredentialResolver = new KeyStoreCredentialResolver(keyStore, privateKeyPasswordsByAlias);

			keyStoreCredentialResolverDelegate
					.setKeyStoreCredentialResolver(keyStoreCredentialResolver);
			CredentialResolver credentialResolver = keyStoreCredentialResolverDelegate;

			CriteriaSet criteriaSet = new CriteriaSet();
			criteriaSet.add(new EntityIDCriteria(props.getKeystoreAlias()));
			criteriaSet.add(new UsageCriteria(UsageType.SIGNING));

			signingCredential = credentialResolver.resolveSingle(criteriaSet);
		} catch (SecurityException e) {
			s_logger.error("Exception Occured while Generating Credential: "
					+ e.getMessage());
			// e.printStackTrace();
		} catch (org.opensaml.xml.security.SecurityException e) {
			s_logger.error("Exception Occured while Generating Credential: "
					+ e.getMessage());
			// e.printStackTrace();
		}

		catch (NoSuchAlgorithmException e1) {
			s_logger.error("Exception Occured while Generating Credential: "
					+ e1.getMessage());
			// e1.printStackTrace();
		} catch (CertificateException e1) {
			s_logger.error("Exception Occured while Generating Credential: "
					+ e1.getMessage());
			// e1.printStackTrace();
		} catch (IOException e1) {
			s_logger.error("Exception Occured while Generating Credential: "
					+ e1.getMessage());
			// e1.printStackTrace();
		}

		return signingCredential;

	}

}
