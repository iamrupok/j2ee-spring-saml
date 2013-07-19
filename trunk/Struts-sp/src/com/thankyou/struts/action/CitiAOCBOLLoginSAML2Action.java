package com.thankyou.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.thankyou.struts.form.LoginForm;
 
public class CitiAOCBOLLoginSAML2Action extends Action {
 
	public static String ACTION_NAME = "SAMLAssertionSSOLogin";
    private static Log s_logger = LogFactory.getLog(CitiAOCBOLLoginSAML2Action.class);

    private static final String SSO_FAILURE="ssoFailure";
    private static final String FORWARD_ACCOUNT_LIST = "accountList";
    private static final String FORWARD_CONDENSED_REGISTRATION = "condensedRegistration";
    private static final String FORWARD_SSO_FAILURE_ACCOUNT_LOCKED = "accountLocked";

    public static final String CBOL_KEEP_ALIVE_URL = "cbolKeepAliveURL";
    public static final String CBOL_KEEP_ALIVE_URL_PARAM = "KAL_URL";
    public static final String RELAY_STATE = "relayState";
    

    public static final String REQUEST_SSO_ACTIVE_ACCOUNTS ="portalSsoActiveAccounts";
    public static final String REQUEST_SSO_PENDING_ACCOUNTS ="portalSsoPendingAccounts";

    private String forward;
    public static final String FORWARD_FAILURE = "failure";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	
    	
        forward = FORWARD_FAILURE;
        
        String loginSAMLResponse = request.getParameter("SAMLResponse");
        
        
        return mapping.findForward(forward);
    }
}