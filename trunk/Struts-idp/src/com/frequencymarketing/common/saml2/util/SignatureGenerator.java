package com.frequencymarketing.common.saml2.util;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.Configuration;

import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;

import org.opensaml.xml.signature.Signature;


import com.sun.org.apache.xml.internal.security.signature.XMLSignature;

public class SignatureGenerator {

	private static Log s_logger = LogFactory.getLog(SignatureGenerator.class);

	public static Signature generateSignature(Credential signingCredential) {

		Signature signature = null;

		/************ For Signing the SAML2 Response ******************/

		signature = (Signature) Configuration.getBuilderFactory()
				.getBuilder(Signature.DEFAULT_ELEMENT_NAME)
				.buildObject(Signature.DEFAULT_ELEMENT_NAME);

		signature.setSigningCredential(signingCredential);
		signature
				.setSignatureAlgorithm(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);

		// This is also the default if a null SecurityConfiguration is
		// specified
		SecurityConfiguration secConfig = Configuration
				.getGlobalSecurityConfiguration();
		// If null this would result in the default KeyInfoGenerator being
		// used
		String keyInfoGeneratorProfile = "XMLSignature";

		try {
			SecurityHelper.prepareSignatureParams(signature, signingCredential,
					secConfig, null);
		} catch (SecurityException e) {
			s_logger.error("Exception Occured while Generating Credential: "
					+ e.getMessage());
			// e.printStackTrace();
		}

		return signature;

	}

}
