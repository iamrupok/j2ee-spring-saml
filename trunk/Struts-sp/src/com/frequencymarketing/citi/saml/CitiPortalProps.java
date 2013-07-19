package com.frequencymarketing.citi.saml;

import com.frequencymarketing.common.saml.authentication.SamlProps;

public class CitiPortalProps extends SamlProps {

	private String applicationId;
    private String username;
	private String password;
	private String apiName;
	private String version;
	private String keepAliveURL;
    private String citiLoginUrl;
    private String tyPostURL;


    public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getKeepAliveURL() {
		return keepAliveURL;
	}

	public void setKeepAliveURL(String keepAliveURL) {
		this.keepAliveURL = keepAliveURL;
	}

	public String getCitiLoginUrl() {
		return citiLoginUrl;
	}

	public void setCitiLoginUrl(String citiLoginUrl) {
		this.citiLoginUrl = citiLoginUrl;
	}

	public String getTyPostURL() {
		return tyPostURL;
	}

	public void setTyPostURL(String tyPostURL) {
		this.tyPostURL = tyPostURL;
	}

}
