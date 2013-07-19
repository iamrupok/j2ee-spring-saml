package com.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
 
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
 
public class SignAssertion
{
	/*
   private final static Logger logger = LoggerFactory.getLogger(SignAssertion.class);
   private static Credential signingCredential = null;
   final static Signature signature = null;
   final static String password = "secret";
   final static String certificateAliasName = "selfsigned";
   final static String fileName = "C:\\Users\\Narendra\\Desktop\\SAML\\idpcert.jks";
 
   @SuppressWarnings("static-access")
   private void intializeCredentials()
   {
      KeyStore ks = null;
      FileInputStream fis = null;
      char[] password = this.password.toCharArray();
 
      // Get Default Instance of KeyStore
      try
      {
         ks = KeyStore.getInstance(KeyStore.getDefaultType());
      }
      catch (KeyStoreException e)
      {
         logger.error("Error while Intializing Keystore", e);
      }
 
      // Read Ketstore as file Input Stream
      try
      {
         fis = new FileInputStream(fileName);
      }
      catch (FileNotFoundException e)
      {
         logger.error("Unable to found KeyStore with the given keystoere name ::" + fileName, e);
      }
 
      // Load KeyStore
      try
      {
         ks.load(fis, password);
      }
      catch (NoSuchAlgorithmException e)
      {
         logger.error("Failed to Load the KeyStore:: ", e);
      }
      catch (CertificateException e)
      {
         logger.error("Failed to Load the KeyStore:: ", e);
      }
      catch (IOException e)
      {
         logger.error("Failed to Load the KeyStore:: ", e);
      }
 
      // Close InputFileStream
      try
      {
         fis.close();
      }
      catch (IOException e)
      {
         logger.error("Failed to close file stream:: ", e);
      }
 
      // Get Private Key Entry From Certificate
      KeyStore.PrivateKeyEntry pkEntry = null;
      try
      {
         pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(this.certificateAliasName, new KeyStore.PasswordProtection(
                  this.password.toCharArray()));
      }
      catch (NoSuchAlgorithmException e)
      {
         logger.error("Failed to Get Private Entry From the keystore:: " + this.fileName, e);
      }
      catch (UnrecoverableEntryException e)
      {
         logger.error("Failed to Get Private Entry From the keystore:: " + this.fileName, e);
      }
      catch (KeyStoreException e)
      {
         logger.error("Failed to Get Private Entry From the keystore:: " + this.fileName, e);
      }
      PrivateKey pk = pkEntry.getPrivateKey();
 
      X509Certificate certificate = (X509Certificate) pkEntry.getCertificate();
      BasicX509Credential credential = new BasicX509Credential();
      credential.setEntityCertificate(certificate);
      credential.setPrivateKey(pk);
      signingCredential = credential;
 
      logger.info("Private Key" + pk.toString());
 
   }
 
   public static void main(String args[]) throws Exception
   {
      SignAssertion sign = new SignAssertion();
      sign.intializeCredentials();
      Signature signature = null;
      try
      {
         DefaultBootstrap.bootstrap();
      }
      catch (ConfigurationException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      signature = (Signature) Configuration.getBuilderFactory().getBuilder(Signature.DEFAULT_ELEMENT_NAME)
               .buildObject(Signature.DEFAULT_ELEMENT_NAME);
 
      signature.setSigningCredential(signingCredential);
 
      // This is also the default if a null SecurityConfiguration is specified
      SecurityConfiguration secConfig = Configuration.getGlobalSecurityConfiguration();
      // If null this would result in the default KeyInfoGenerator being used
      String keyInfoGeneratorProfile = "XMLSignature";
 
      try
      {
         SecurityHelper.prepareSignatureParams(signature, signingCredential, secConfig, null);
      }
      catch (SecurityException e)
      {
         e.printStackTrace();
      }
      catch (org.opensaml.xml.security.SecurityException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
 
      Response resp = SAMLWriter.getSamlAssertion();
 
      resp.setSignature(signature);
 
      try
      {
         Configuration.getMarshallerFactory().getMarshaller(resp).marshall(resp);
      }
      catch (MarshallingException e)
      {
         e.printStackTrace();
      }
 
      try
      {
         Signer.signObject(signature);
      }
      catch (SignatureException e)
      {
         e.printStackTrace();
      }
 
      ResponseMarshaller marshaller = new ResponseMarshaller();
      Element plain = marshaller.marshall(resp);
      // response.setSignature(sign);
      String samlResponse = XMLHelper.nodeToString(plain);
      logger.info("********************\n*\n***********::" + samlResponse);
 
   }*/
}