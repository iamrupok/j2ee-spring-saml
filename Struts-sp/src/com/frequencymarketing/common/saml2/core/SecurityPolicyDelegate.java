

package com.frequencymarketing.common.saml2.core;

import java.util.Collections;
import java.util.List;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.security.SecurityPolicy;
import org.opensaml.ws.security.SecurityPolicyException;
import org.opensaml.ws.security.SecurityPolicyRule;
import org.opensaml.ws.security.provider.BasicSecurityPolicy;

public class SecurityPolicyDelegate implements SecurityPolicy  {

	private final BasicSecurityPolicy basicSecurityPolicy;
	
	public SecurityPolicyDelegate(List<SecurityPolicyRule> securityPolicyRules) {
		super();
		basicSecurityPolicy = new BasicSecurityPolicy();
		basicSecurityPolicy.getPolicyRules().addAll(securityPolicyRules);
	}

	@Override
	public void evaluate(MessageContext messageContext) throws SecurityPolicyException {
		basicSecurityPolicy.evaluate(messageContext);
	}

	@Override
	public List<SecurityPolicyRule> getPolicyRules() {
		return basicSecurityPolicy.getPolicyRules();
	}

}
