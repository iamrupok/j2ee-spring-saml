package com.frequencymarketing.citi.saml;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.util.XMLHelper;
import org.opensaml.xml.validation.ValidationException;
import org.w3c.dom.Element;

import com.frequencymarketing.common.model.dao.MemberSAMLSponsorAccountsData;
import com.frequencymarketing.common.saml.authentication.Saml;
import com.frequencymarketing.common.saml.authentication.SamlProps;
import com.frequencymarketing.common.saml2.core.PostBindingAdapter;
import com.frequencymarketing.common.saml2.util.SecurityPolicyResolverGenerator;
import com.frequencymarketing.common.saml2.util.SigningCredentialGenerator;
import com.frequencymarketing.common.saml2.util.SpringContext;

public class CitiPortalSaml2 implements Saml {
	
	protected final Log s_logger = LogFactory.getLog(CitiPortalSaml2.class);
    public CitiPortalProps m_samlProps;
    public CryptoXml m_cryptoXml;

    public static final String DEFAULT_ORIGINATOR = "CITILOGIN";
	
    //public MemberSAMLSponsorAccountsData processCitiLoginResponseSAML2UsingPostBindingAdapter(HttpServletRequest request) throws Exception
    public String processCitiLoginResponseSAML2UsingPostBindingAdapter(HttpServletRequest request) throws Exception
   	{
    	 PostBindingAdapter postBindingAdapter = (PostBindingAdapter) SpringContext.getBean("bindingAdpater");
         SecurityPolicyResolver securityPolicyResolver = SecurityPolicyResolverGenerator.generateSecurityPolicyResolver(getSamlProps(),"usm");
         SAMLMessageContext messageContext = null;
 		try {
 			messageContext = postBindingAdapter.extractSAMLMessageContext(request,securityPolicyResolver);
 		} catch (MessageDecodingException mde) {
 			s_logger.error("Exception decoding SAML message", mde);
 			throw mde;
 		} catch (SecurityException se) {
 			s_logger.error("Exception decoding SAML message", se);
 			throw se;
 		}
 		Response Saml2Response = (Response) messageContext.getInboundSAMLMessage();
 		
 		/*Credential signingCredential =  SigningCredentialGenerator.generateSigningCredential(getSamlProps());
 		//create SignatureValidator
        SignatureValidator signatureValidator = new SignatureValidator(signingCredential);

        //get the signature to validate from the response object
        Signature signature = Saml2Response.getSignature();

        //try to validate
        try
        {
            signatureValidator.validate(signature);
        }
        catch (ValidationException ve)
        {
            
        	s_logger.error("Signature Is Not Valid Cause:  ", ve);
            
            throw ve;
        }*/
        
        Assertion assertion = Saml2Response.getAssertions().get(0);
        
        /************************** Print the Response as XML String *****************************************/
        System.out.println(" ");
        System.err.println("Decoded Response: ");
        System.out.println(" ");
        ResponseMarshaller marshaller = new ResponseMarshaller();
		Element plain;
		String samlResponse =null;
		try {
			plain = marshaller.marshall(Saml2Response);
			// 
			samlResponse = XMLHelper.nodeToString(plain);
			System.out.println(samlResponse);
		} catch (MarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
      /* 	MemberSAMLSponsorAccountsData samlData = new MemberSAMLSponsorAccountsData();
       	
       	return samlData;*/
		return samlResponse;
   	}
    
    public MemberSAMLSponsorAccountsData processCitiLoginResponseSAML2(String loginSAMLResponse) throws Exception
   	{
       	MemberSAMLSponsorAccountsData samlData = new MemberSAMLSponsorAccountsData();
   		
       	/*
   		String strSamlResponse = m_cryptoXml.decrypt(loginSAMLResponse);
   		//s_logger.debug("Decrypted Login SAML Response: " + strSamlResponse);
   		
   		String customPayload = null;
   		try{
   			 //bootstrap the opensaml stuff
               //org.opensaml.DefaultBootstrap.bootstrap();
   			ByteArrayInputStream is = new ByteArrayInputStream(strSamlResponse.getBytes());
   			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
               documentBuilderFactory.setNamespaceAware(true);
               DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();

               Document document = docBuilder.parse(is);
               Element element = document.getDocumentElement();
               
               
               UnmarshallerFactory unmarshallerFactory = org.opensaml.xml.Configuration.getUnmarshallerFactory();
               org.opensaml.xml.io.Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
               XMLObject responseXmlObj = unmarshaller.unmarshall(element);
               
               
               Response samlResponse = (Response) responseXmlObj;
               
               //Signature sig = samlResponse.getSignature();
               // Validating the signature 
               //SignatureValidator validator = new SignatureValidator(credential);
               //validator.validate(sig);
               
               if (samlResponse != null)
               {
                   //response.verify(certificate);

                   if (samlResponse.getAssertions().iterator().hasNext())
                   {

                   	Assertion assertion = (Assertion) samlResponse.getAssertions().iterator().next();
                       //assertion.checkValidity();
                   	
                   	org.joda.time.DateTime notB = assertion.getConditions().getNotBefore();
                   	org.joda.time.DateTime notOnOrA = assertion.getConditions().getNotOnOrAfter();
                   	
                       Date notBefore = notB.toDate();
                       Date notOnOrAfter = notOnOrA.toDate();

                       if(isDateValid(notBefore, notOnOrAfter))
                       {

       	                Iterator aserItr = assertion.getStatements().iterator();
       	                if (aserItr.hasNext())
       	                {
       	                    AuthnStatement statement = (AuthnStatement) aserItr.next();
       	                    
       	                }
       	                if(aserItr.hasNext())
       	                {
       	                	AttributeStatement attribStmt = (AttributeStatement) aserItr.next();
       	                	
       	                	Iterator attribItr = attribStmt.getAttributes().iterator();
       	                	
       	                  	while(attribItr.hasNext())
       	                  	{
       	                  		Attribute samlAttr = (Attribute)attribItr.next();
       	                  		if(samlAttr.getName() != null && samlAttr.getName().equals("customPayload"))
       	                  		{
       	                  			Object obj = samlAttr.getAttributeValues().get(0);
       	                  			if(obj != null)
       	                  			{
       	                  				customPayload = obj.toString();
       	                  			}
       	                  		}
       	                  	}
       	                }

                       }
                       else
                       {
                       	s_logger.error("SAML Response expired. notBefore: "
                       			+ notBefore + "   notOnOrAfter: " + notOnOrAfter
                       			+ "     current time: " + Calendar.getInstance().getTime());
                       }
                   }

               }

   		}
   		 catch (Exception ex)
   	        {
   	            ex.printStackTrace();
   	        }
   		 String decodedCustomPayload = new String(Base64.decodeBase64(customPayload.getBytes()));
   	        
   			s_logger.debug(" Decoded Custom paylaod ---------> "
   					+ decodedCustomPayload);
   			Configuration config = Configuration.create();
   			String cmrPaylaodpersisValue = config.getcmrPayloadPersistValue();

   			if (null != cmrPaylaodpersisValue && cmrPaylaodpersisValue.equalsIgnoreCase("true")) 
   			{
   				Member member = (Member) BusinessFactory.create(Member.class);
   				try {
   					member.insertCMRData(decodedCustomPayload);
   				} catch (Exception e) {
   					// other than logging do not do anything like popping up the
   					// exception
   					s_logger.error(
   							" Failed to insert CML paylaod ------------- > ", e);
   				}
   			}
   	        
   	        
   	        //System.out.println(">>>>>>>> Decoded customPayload\n\n " + decodedCustomPayload);
   	        
   			String selectedAccount = null;
   			
   			JAXBContext jaxbContext = JAXBContext.newInstance("com.reardencommerce.schema.sso");
   			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
   			CustomPayload payload = (CustomPayload)unmarshaller.unmarshal(new StringReader(decodedCustomPayload));
   			
   			SessionInfo sessionInfo = payload.getSessionInfo();
   			samlData.setInvokingSource(DEFAULT_ORIGINATOR);
   			if(sessionInfo != null) {
   				CitiSessionInfo citiSessionInfo = (CitiSessionInfo)sessionInfo;
   				selectedAccount = citiSessionInfo.getSelectedAccount();
   				samlData.setSelectedAccount(selectedAccount);
   				//System.out.println("selectedAccount : " + selectedAccount);
   				
   				String originator = citiSessionInfo.getOriginator();
   				s_logger.debug("\n\n ====== > Originator: " + originator);
   				if(!StringUtils.isEmpty(originator)) {
   					samlData.setInvokingSource(originator);
   				}
   			}
   			
   			List<SuperAccount> accounts = payload.getSuperAccounts().getSuperAccount();
   			
   			if(accounts != null) {
   				
   				List<AccountNumberData> accountNumberList = new ArrayList<AccountNumberData>();
   				
   				for (SuperAccount superAccount : accounts) {
   					String superAccountNumber = superAccount.getNumber();
   					//System.out.println("super account Number: " + superAccountNumber);
   					
   					List<Account> accountList = superAccount.getAccounts().getAccount();
   					
   					for (Account account : accountList) {
   						CitiAccount citiAccount = (CitiAccount)account;
   						List<AcctInfo> custInfoList = citiAccount.getAcctRoleDetail().getAcctInfo();
   						if(custInfoList != null) {
   							for (AcctInfo acctInfo : custInfoList) {
   								
   								AccountNumberData accountNumberData = new AccountNumberData();
   								
   								String thankYouId = acctInfo.getThankYouID();
   								//System.out.println("ThankyouId: " + thankYouId);
   								accountNumberData.setMemberId(acctInfo.getThankYouID());
   								
   								String accountNumber = acctInfo.getAcctNumber();
   								accountNumberData.setAccountNumber(acctInfo.getAcctNumber());
   								String fimpCode = acctInfo.getFIMP();
   								accountNumberData.setFimpCode(acctInfo.getFIMP());
   								
   								//System.out.println("accountNumber: " + accountNumber);
   								//System.out.println("fimpCode: " + fimpCode);
   								
   								if("50".equals(acctInfo.getOwnershipType())) {
   									String businessAccountNumber = acctInfo.getRelatedAcctNumber();
   									//System.out.println("businessAccountNumber: " + businessAccountNumber);
   									accountNumberData.setAccountNumber(acctInfo.getRelatedAcctNumber());
   								}
   								//System.out.println("adding account to list: " + accountNumberData);
   								accountNumberList.add(accountNumberData);
   							}
   						}
   					}
   				}
   				
   				samlData.setAccountNumbers(accountNumberList);
   			}
   			*/
   			return samlData;
   	}
   	
	

	@Override
	public byte[] getSAMLResponseAsBase64(Object memberData, String clientIp)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSAMLResponseAsString(Object memberData, String clientIp)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSamlProps(SamlProps a_samlProps) {
		 m_samlProps = (CitiPortalProps)a_samlProps;

	}

	 private boolean isDateValid(Date notBefore, Date notOnOrAfter)
	    {
	    	Calendar c = Calendar.getInstance();
	    	Date currentDate = c.getTime();

	    	//return (currentDate.compareTo(notBefore) >= 0) && currentDate.before(notOnOrAfter);
	    	return currentDate.before(notOnOrAfter);
		}
	 
	 public CitiPortalProps getSamlProps()
	    {
	        return m_samlProps;
	    }

	    public void setSamlProps(CitiPortalProps a_samlProps)
	    {
	        m_samlProps = a_samlProps;
	    }

	   

	    public CryptoXml getCryptoXml()
		{
			return m_cryptoXml;
		}

		public void setCryptoXml(CryptoXml a_cryptoXml)
		{
			m_cryptoXml = a_cryptoXml;
		}


}
