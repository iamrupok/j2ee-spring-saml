/*
 * @(#) VerifyMemberData.java Apr 3, 2006
 * Copyright 2005 Frequency Marketing, Inc. All rights reserved.
 * Frequency Marketing, Inc. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.frequencymarketing.common.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/*
import com.frequencymarketing.common.ListTools;
import com.frequencymarketing.common.StringUtils;*/


/**
* @author Srinivas Purma
*/
public class PortalSsoData
{
	private String applicationId;
	private String username;
	private String password;
	private String userToken;
	private String apiName;
	private String version;
	private String partnerId;
	private String partnerSystemId;
	private String partnerServerId;
	private String partnerSessionId;
	private String requestId;
	private String conversationId;
	private String serverId;
	private String sessionId;
	private String m_ccsId;
    private String m_langCode;
    private String m_last4Ssn;
    private String m_nameFirst;
    private String m_nameLast;
    private String m_accountNumbers;
    private String m_selectedAccount;
    private String m_destinationUrl;
    private String m_hbxCode;
    private int registeredMemberCount;
    private int primaryMemberCount;
    private String isUserPrimaryOnSelectedAccount;
    private String selectedAccountProductName;
	private String selectedAccountMemberId;
	private String selectedAccountMemberStatus;
	private List memberSponsorAccountsList;
	private String m_errorCode;
    private String m_errorMsg;

    public static final String SECONDARY_USER_ERROR_CODE = "-20101";
    public static final String NO_ACCOUNT_FOUND_ERROR_CODE = "-20102";
    public static final String SELECTED_ACCOUNT_NOT_FOUND_ERROR_CODE = "-20103";
    public static final String DUPLICATE_REQUEST_ERROR_CODE = "-20110";

    public PortalSsoData()
    {
    }


    public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getUserToken() {
		return userToken;
	}


	public void setUserToken(String userToken) {
		this.userToken = userToken;
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


	public String getPartnerId() {
		return partnerId;
	}


	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}


	public String getPartnerSystemId() {
		return partnerSystemId;
	}


	public void setPartnerSystemId(String partnerSystemId) {
		this.partnerSystemId = partnerSystemId;
	}


	public String getPartnerServerId() {
		return partnerServerId;
	}


	public void setPartnerServerId(String partnerServerId) {
		this.partnerServerId = partnerServerId;
	}


	public String getPartnerSessionId() {
		return partnerSessionId;
	}


	public void setPartnerSessionId(String partnerSessionId) {
		this.partnerSessionId = partnerSessionId;
	}

    public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getRegisteredMemberCount() {
		return registeredMemberCount;
	}

	public void setRegisteredMemberCount(int registeredMemberCount) {
		this.registeredMemberCount = registeredMemberCount;
	}

	public int getPrimaryMemberCount() {
		return primaryMemberCount;
	}

	public void setPrimaryMemberCount(int primaryMemberCount) {
		this.primaryMemberCount = primaryMemberCount;
	}

	public String getIsUserPrimaryOnSelectedAccount() {
		return isUserPrimaryOnSelectedAccount;
	}

	public void setIsUserPrimaryOnSelectedAccount(
			String isUserPrimaryOnSelectedAccount) {
		this.isUserPrimaryOnSelectedAccount = isUserPrimaryOnSelectedAccount;
	}

	public String getSelectedAccountProductName() {
		return selectedAccountProductName;
	}

	public void setSelectedAccountProductName(String selectedAccountProductName) {
		this.selectedAccountProductName = selectedAccountProductName;
	}

    /**
     * @return Returns the nameLast.
     */
    public String getNameLast()
    {
        return m_nameLast;
    }

    /**
     * @param a_nameLast The nameLast to set.
     */
    public void setNameLast(String a_nameLast)
    {
        m_nameLast = a_nameLast;
    }

    /**
     * @return Returns the memberId.
     */
    public String getSelectedAccountMemberId()
    {
        return selectedAccountMemberId;
    }

    /**
     * @param a_memberId The memberId to set.
     */
    public void setSelectedAccountMemberId(String a_memberId)
    {
        selectedAccountMemberId = a_memberId;
    }

	public String getCcsId()
	{
		return m_ccsId;
	}

	public void setCcsId(String a_ccsId)
	{
		m_ccsId = a_ccsId;
	}

	public String getLangCode()
	{
		return m_langCode;
	}

	public void setLangCode(String a_langCode)
	{
		m_langCode = a_langCode;
	}

	public String getNameFirst()
	{
		return m_nameFirst;
	}

	public void setNameFirst(String a_nameFirst)
	{
		m_nameFirst = a_nameFirst;
	}

	public String getAccountNumbers()
	{
		return m_accountNumbers;
	}

	public void setAccountNumbers(String a_accountNumbers)
	{
		m_accountNumbers = a_accountNumbers;
	}

	public String getSelectedAccount()
	{
		return m_selectedAccount;
	}

	public void setSelectedAccount(String a_selectedAccount)
	{
		m_selectedAccount = a_selectedAccount;
	}

	public String getDestinationUrl()
	{
		return m_destinationUrl;
	}

	public void setDestinationUrl(String a_destinationUrl)
	{
		m_destinationUrl = a_destinationUrl;
	}

	public String getHbxCode()
	{
		return m_hbxCode;
	}

	public void setHbxCode(String a_hbxCode)
	{
		m_hbxCode = a_hbxCode;
	}

	public String getErrorCode()
	{
		return m_errorCode;
	}

	public void setErrorCode(String a_errorCode)
	{
		m_errorCode = a_errorCode;
	}

	public String getErrorMsg() {
		return m_errorMsg;
	}

	public void setErrorMsg(String a_errorMsg)
	{
		m_errorMsg = a_errorMsg;
	}

	public String getLast4Ssn()
	{
		return m_last4Ssn;
	}

	public void setLast4Ssn(String a_last4Ssn)
	{
		m_last4Ssn = a_last4Ssn;
	}

	/*public boolean isSecondary()
	{
		boolean retVal = m_errorCode == null
				&& ("A".equals(getSelectedAccountMemberStatus()) || "P".equals(getSelectedAccountMemberStatus()))
				&& (getPrimaryMemberCount() <= 0 || ListTools.isEmpty(getPrimaryAccounts()));

		return retVal;
	}

	public boolean isSearchAccountsFailed()
	{
		return (m_errorCode != null && (m_errorCode.equals(NO_ACCOUNT_FOUND_ERROR_CODE)
				|| m_errorCode.equals(SELECTED_ACCOUNT_NOT_FOUND_ERROR_CODE)))
				|| memberSponsorAccountsList == null || memberSponsorAccountsList.size() == 0
				|| ListTools.isEmpty(getPrimaryAccounts())
				|| isInvalidMemberStatus();
	}

	public boolean isInvalidMemberStatus()
	{
		return !StringUtils.isEmpty(getSelectedAccount())  &&
				(getSelectedAccountMemberStatus() == null
				|| "C".equals(getSelectedAccountMemberStatus())
				|| "I".equals(getSelectedAccountMemberStatus()));
	}*/

	public boolean isDuplicateRequest()
	{
		return (m_errorCode != null && m_errorCode.equals(DUPLICATE_REQUEST_ERROR_CODE));
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getPassword() {
		return password;
	}


	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}


	public String getApplicationId() {
		return applicationId;
	}

	public List getMemberSponsorAccountsList() {
		return memberSponsorAccountsList;
	}


	/*public void setMemberSponsorAccountsList(List memberSponsorAccountsList) {

		/// Filter out all cancelled account if the member Id's are the same.
		Map accountMap = new HashMap();
		for (Iterator iterator = memberSponsorAccountsList.iterator(); iterator.hasNext();)
		{
			PortalMbrSponsorAcct acct = (PortalMbrSponsorAcct) iterator.next();

			PortalMbrSponsorAcct existingAcct = (PortalMbrSponsorAcct)accountMap.get(acct.getMemberId());
			if(existingAcct == null || "C".equals(existingAcct.getAcctStatus()))
			{
				accountMap.put(acct.getAffinityId(), acct);
			}

		}

		this.memberSponsorAccountsList = new ArrayList();
		this.memberSponsorAccountsList.addAll(accountMap.values());
	}*/


	public String getSelectedAccountMemberStatus() {
		return selectedAccountMemberStatus;
	}


	public void setSelectedAccountMemberStatus(String selectedAccountMemberStatus) {
		this.selectedAccountMemberStatus = selectedAccountMemberStatus;
	}

/*	public List getPrimaryRegisteredAccounts() {
		return ListTools.findByProperty(getMemberSponsorAccountsList(), "mbrStatus", "A");

	}

	public List getPrimaryPendingAccounts() {
		return ListTools.findByProperty(getMemberSponsorAccountsList(), "mbrStatus", "P");
	}

	public List getPrimaryAccounts() {
		return ListTools.findByProperty(getMemberSponsorAccountsList(), "primaryMbrInd", "Y");
	}

	public String getLast4SelectedAccountNumber() {

		String retVal = getSelectedAccount();
		if(!StringUtils.isEmpty(getSelectedAccount()) && getSelectedAccount().length() >= 4) {
			retVal = getSelectedAccount().substring(getSelectedAccount().length() - 4);
		}
		return retVal;
	}
	
	public String toString()
	{
		return StringUtils.toStringObject(this);
	}*/
}


