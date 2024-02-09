package com.strand.app.controller;
import java.util.Arrays;

 
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.strand.app.config.*;
 

import io.swagger.annotations.Api;

@RestController
@Api(value="CustomerMaster", description="Upload of Customer to centralized database") 
public class SpringCustomerController {

	@Autowired
	ResponseDataGeneric result;

	 

	@Autowired
	private AppFunc appFunc;

	 
	public boolean checkCustomerPhone(String customerPhone) {

		JSONObject return_obj = new JSONObject();

		boolean phoneFlag = true;

		int ph_len = customerPhone.length();

		boolean phoneIntFlag = appFunc.isNumeric(customerPhone);

		if (ph_len != 10) {
			phoneFlag = false;
		}
		if (phoneIntFlag == false) {
			phoneFlag = false;
		}

		return phoneFlag;
	}

	public boolean checkCustomerName(String customerName) {

		boolean nameFlag = true;

		int name_len = customerName.length();

		if (name_len < 4) {
			nameFlag = false;
		}

		return nameFlag;
	}

}
