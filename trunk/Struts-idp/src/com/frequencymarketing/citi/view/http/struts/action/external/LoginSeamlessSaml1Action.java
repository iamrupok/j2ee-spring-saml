package com.frequencymarketing.citi.view.http.struts.action.external;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

import com.frequencymarketing.common.model.TyUsmSamlProfileData;
import com.frequencymarketing.common.saml.authentication.Saml;
import com.frequencymarketing.common.saml.authentication.SamlProps;
import com.frequencymarketing.common.saml2.core.PostBindingAdapter;
import com.frequencymarketing.common.saml2.util.IDService;
import com.frequencymarketing.common.saml2.util.SpringContext;
import com.frequencymarketing.common.saml2.util.TimeService;
import com.frequencymarketing.common.saml2.xml.EndpointGenerator;

public class LoginSeamlessSaml1Action extends Action {

	public static String ACTION_NAME = "loginSeamless2";
	private static Log s_logger = LogFactory
			.getLog(LoginSeamlessSaml2Action.class);

	public static final String EXPEDIA_PARTNER_ID = "5021";
	public static final String USM_PARTNER_ID = "5030";
	public static final String CARTERA_PARTNER_ID = "CART";
	public static final String CARTERA_SAML_PARTNER_CODE = "CART";
	public static final String CONNEXIONS_PARTNER_ID = "FV_CLTS";
	public static final String CONNEXIONS_PARTNER_ID_2 = "170201";
	public static final String CONNEXIONS_SAML_PARTNER_CODE = "FV_CLTS";
	public static final String REARDEN_SAML_PARTNER_CODE = "FV_RDEN";
	public static final String LIVENATION_PARTNER_ID = "FV_SWP";

	public static String FORWARD_VESDIA_BONUS = "vesdiaBonusAwarded";
	public static String FORWARD_EXPEDIA_BONUS = "expediaBonusAwarded";
	public static String PARAMETER_REF_NAME = "refrenceName";
	public static final String[] SPECIAL_CHAR = { ">", "<", "\"" };

	public static final String FORWARD_FAILURE = "failure";
	public static final String FORWARD_SUCCESS = "success";

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String forward = FORWARD_FAILURE;
		String refrenceName = request.getParameter(PARAMETER_REF_NAME);
		request.setAttribute(PARAMETER_REF_NAME, refrenceName);
		String memberId = "123";
		String partnerId = "5030";
		String cbUrl = request.getParameter("cbUrl");

		String clientIp = request.getRemoteAddr();

		if (!partnerId.isEmpty()
				&& (partnerId.equals(USM_PARTNER_ID) || partnerId
						.equals(LIVENATION_PARTNER_ID))) {
			// TyMbrProfile member =
			// (TyMbrProfile)BusinessFactory.create(TyMbrProfile.class);
			TyUsmSamlProfileData usmSamlData = new TyUsmSamlProfileData();
			usmSamlData.setMemberId("123456789");
			usmSamlData.setAgentId("agent-01");
			usmSamlData.setErrorCode("Error-1");
			usmSamlData.setErrorMessage("Error Message-1");
			usmSamlData.setFirstName("Alam");
			usmSamlData.setLastName("Nur");
			usmSamlData.setPointBalance("2586974");
			
			 Saml partnerSaml = (Saml)SpringContext.getBean("usmSaml");
		        SamlProps props = (SamlProps)SpringContext.getBean("usmSamlProps2");
		        request.setAttribute("SAMLURL", props.getSamlPostURL());
		        String usmSamlResponse = partnerSaml.getSAMLResponseAsString(usmSamlData, clientIp);
		        
		       
		        
		        //System.out.println("before mapping.findForward(forward);"+usmSamlResponse);
		        s_logger.debug(">>>>>> saml response to USM: " + usmSamlResponse);
		        
		       request.setAttribute("SAMLResponse", usmSamlResponse); 
           
		        //setUsmSaml(usmSamlData, clientIp, USM_PARTNER_ID);
            
            forward = FORWARD_SUCCESS;

		}
		return mapping.findForward(forward);
	}
	/* protected void setUsmSaml(TyUsmSamlProfileData memberData, String clientIp, String partnerId )throws Exception
	    {
	        Saml partnerSaml = (Saml)SpringContext.getBean("usmSaml");
	        SamlProps props = (SamlProps)SpringContext.getBean("usmSamlProps");
	        re.setAttribute("SAMLURL", props.getSamlPostURL());
	        String usmSamlResponse = new String(partnerSaml.getSAMLResponseAsBase64(memberData, clientIp));
	        s_logger.debug(">>>>>> saml response to USM: " + usmSamlResponse);
	        getRequest().setAttribute("SAMLResponse", usmSamlResponse); 
	    }*/

}
