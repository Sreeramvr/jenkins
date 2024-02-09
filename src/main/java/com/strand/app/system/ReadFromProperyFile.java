package com.strand.app.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.simple.JSONObject;

public class ReadFromProperyFile {

	public static JSONObject GetDataFromPropFile()  {
		Properties prop = new Properties();
        InputStream input = null;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	    input = classLoader.getResourceAsStream("/application.properties");
	
			 try {
				prop.load(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
         
          String PAYU_GATEWAY_URL=prop.getProperty("PAYU_GATEWAY_URL").trim();
          String PAYU_SUCCESS_URL=prop.getProperty("PAYU_SUCCESS_URL").trim();
          String PAYU_FAILURE_URL=prop.getProperty("PAYU_FAILURE_URL").trim();
          String PAYTM_MID=prop.getProperty("PAYTM_MID").trim();
          String PAYTM_MERCHANT_KEY=prop.getProperty("PAYTM_MERCHANT_KEY").trim();
          String PAYTM_INDUSTRY_TYPE_ID=prop.getProperty("PAYTM_INDUSTRY_TYPE_ID").trim();
          String PAYTM_CHANNEL_ID=prop.getProperty("PAYTM_CHANNEL_ID").trim();
          String PAYTM_URL=prop.getProperty("PAYTM_URL").trim();
          String PAYTM_CALLBACK_URL=prop.getProperty("PAYTM_CALLBACK_URL").trim();
           String PAYTM_WEBSITE=prop.getProperty("PAYTM_WEBSITE").trim();
          String SUCCESS_HTML_BASE_TAG_URL=prop.getProperty("SUCCESS_HTML_BASE_TAG_URL").trim();
          String MAIL_USERNAME=prop.getProperty("MAIL_USERNAME").trim();
          String MAIL_PASSWORD=prop.getProperty("MAIL_PASSWORD").trim();
          String MAIL_SMTP=prop.getProperty("MAIL_SMTP").trim();
          String MAIL_PORT=prop.getProperty("MAIL_PORT").trim();
          
     
          String TARGET_API_1=prop.getProperty("TARGET_API_1").trim();
          String GST_USERNAME=prop.getProperty("GST_USERNAME").trim();
          String GST_PASSWORD=prop.getProperty("GST_PASSWORD").trim();
          String GST_CLIENT_ID=prop.getProperty("GST_CLIENT_ID").trim();
          String GST_CLIENT_SECRET=prop.getProperty("GST_CLIENT_SECRET").trim();
          String GST_GRANT_TYPE=prop.getProperty("GST_GRANT_TYPE").trim();
          String TARGET_API_2=prop.getProperty("TARGET_API_2").trim();
       
          
          
          JSONObject propObj=new JSONObject();
          propObj.put("PAYU_GATEWAY_URL", PAYU_GATEWAY_URL);
          propObj.put("PAYU_SUCCESS_URL", PAYU_SUCCESS_URL);
          propObj.put("PAYU_FAILURE_URL", PAYU_FAILURE_URL);
          propObj.put("PAYTM_MID", PAYTM_MID);
          propObj.put("PAYTM_MERCHANT_KEY", PAYTM_MERCHANT_KEY);
          propObj.put("PAYTM_INDUSTRY_TYPE_ID", PAYTM_INDUSTRY_TYPE_ID);
          propObj.put("PAYTM_CHANNEL_ID", PAYTM_CHANNEL_ID);
          propObj.put("PAYTM_URL", PAYTM_URL);
          propObj.put("PAYTM_CALLBACK_URL", PAYTM_CALLBACK_URL);
          propObj.put("PAYTM_WEBSITE", PAYTM_WEBSITE);
          propObj.put("SUCCESS_HTML_BASE_TAG_URL", SUCCESS_HTML_BASE_TAG_URL);
          propObj.put("MAIL_USERNAME", MAIL_USERNAME);
          propObj.put("MAIL_PASSWORD", MAIL_PASSWORD);
          propObj.put("MAIL_SMTP", MAIL_SMTP);
          propObj.put("MAIL_PORT", MAIL_PORT);
          propObj.put("TARGET_API_1", TARGET_API_1);
          propObj.put("GST_USERNAME", GST_USERNAME);
          propObj.put("GST_PASSWORD", GST_PASSWORD);
          propObj.put("GST_CLIENT_ID", GST_CLIENT_ID);
          propObj.put("GST_CLIENT_SECRET", GST_CLIENT_SECRET);
          propObj.put("GST_GRANT_TYPE", GST_GRANT_TYPE);
          propObj.put("TARGET_API_2", TARGET_API_2);
          
          return propObj;
	}

}






