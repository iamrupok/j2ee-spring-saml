package com.frequencymarketing.common.saml2.util;

import java.util.UUID;

public class IDService {

	public String generateID() {
		return UUID.randomUUID().toString();
	}
	
}