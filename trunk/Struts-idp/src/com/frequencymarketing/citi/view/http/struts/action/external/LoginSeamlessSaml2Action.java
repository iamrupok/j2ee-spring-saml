package com.frequencymarketing.citi.view.http.struts.action.external;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.security.cert.X509Certificate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.CredentialResolver;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.criteria.EntityIDCriteria;
import org.opensaml.xml.security.criteria.UsageCriteria;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorFactory;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorManager;
import org.opensaml.xml.security.keyinfo.NamedKeyInfoGeneratorManager;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import sun.misc.BASE64Encoder;

import com.frequencymarketing.citi.saml2.idp.xml.AuthnResponseGenerator;
import com.frequencymarketing.citi.saml2.idp.xml.ConditionsGenerator;
import com.frequencymarketing.common.model.TyUsmSamlProfileData;
import com.frequencymarketing.common.saml.authentication.SamlProps;
import com.frequencymarketing.common.saml.misc.CryptoKeystoreUtil;
import com.frequencymarketing.common.saml2.core.KeyStoreCredentialResolverDelegate;
import com.frequencymarketing.common.saml2.core.PostBindingAdapter;
import com.frequencymarketing.common.saml2.util.IDService;
import com.frequencymarketing.common.saml2.util.SignatureGenerator;
import com.frequencymarketing.common.saml2.util.SigningCredentialGenerator;
import com.frequencymarketing.common.saml2.util.SpringContext;
import com.frequencymarketing.common.saml2.util.TimeService;
import com.frequencymarketing.common.saml2.xml.EndpointGenerator;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;

public class LoginSeamlessSaml2Action extends Action {

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

		/**************************************************************/

		TimeService timeService = (TimeService) SpringContext
				.getBean("timeService");
		IDService idService = (IDService) SpringContext.getBean("idService");
		EndpointGenerator endpointGenerator = (EndpointGenerator) SpringContext
				.getBean("endpointGenerator");
		PostBindingAdapter postBindingAdapter = (PostBindingAdapter) SpringContext
				.getBean("bindingAdpater");
		/*****************************************************************************/

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

			SamlProps props = (SamlProps) SpringContext.getBean("usmSamlProps");

			int responseValidityTimeInMinutes = 2;
			String inResponseTo = null;
			DateTime authnInstant = new DateTime(request.getSession()
					.getCreationTime());
			AuthnResponseGenerator authnResponseGenerator = new AuthnResponseGenerator(
					props.getIssuer(), timeService, idService);

			Response authResponse = authnResponseGenerator
					.generateAuthnResponseTyUsm(usmSamlData,
							props.getRecipientURL(),
							props.getAudienceRestriction(),
							responseValidityTimeInMinutes, inResponseTo,
							clientIp, authnInstant);

			Endpoint endpoint = endpointGenerator
					.generateEndpoint(
							org.opensaml.saml2.metadata.AssertionConsumerService.DEFAULT_ELEMENT_NAME,
							props.getRecipientURL(), null);

			Credential signingCredential = SigningCredentialGenerator.generateSigningCredential(props);

			Signature signature = SignatureGenerator.generateSignature(signingCredential);

			authResponse.setSignature(signature);
			try {
				Configuration.getMarshallerFactory()
						.getMarshaller(authResponse).marshall(authResponse);

				Signer.signObject(signature);
			} catch (MarshallingException e) {
				e.printStackTrace();
			} catch (SignatureException e) {
				e.printStackTrace();
			}

			/************ End Of Signing ******************/
			
			/************************** Print the Response as XML String *****************************************/
			
			ResponseMarshaller marshaller = new ResponseMarshaller();
			Element plain;
			try {
				plain = marshaller.marshall(authResponse);
				// 
				String samlResponse = XMLHelper.nodeToString(plain);
				System.out.println(samlResponse);
			} catch (MarshallingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			

			try {
				postBindingAdapter.sendSAMLMessage(authResponse, endpoint,
						signingCredential, response, props);

				forward = null;
			} catch (MessageEncodingException mee) {
				s_logger.error("Exception encoding SAML message", mee);
				forward = FORWARD_FAILURE;
			}

			/*
			 * request.setAttribute("SAMLURL", props.getSamlPostURL());
			 * BASE64Encoder encoder = new BASE64Encoder();
			 * s_logger.debug(">>>>>> saml response to USM: " +
			 * authResponse.toString());
			 * request.setAttribute("SAMLResponse",encoder
			 * .encodeBuffer(authResponse.toString().getBytes()) ); forward =
			 * FORWARD_SUCCESS;
			 */
		}
		return mapping.findForward(forward);

	}
}
