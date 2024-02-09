package com.strand.app.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.strand.app.config.Format;
import com.strand.app.config.ResponseDataGeneric;
import com.strand.app.crm.SendMail;

/**
 * Servlet implementation class feedback
 */
@RestController
@RequestMapping("titpl/login")
public class Login  {
	static Logger logger = Logger.getLogger(Login.class);  
	
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@RequestMapping("ValidateLoginDetails_1")
	public   @ResponseBody String ValidateLoginDetails_1(HttpServletRequest request,ModelMap model) throws SQLException, ClassNotFoundException
	{
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		ResponseDataGeneric result=new ResponseDataGeneric();
		Gson gson = new Gson();	
		JSONObject row_data = new JSONObject();
		

		String internal_code="0";
		Integer branch_int_code=0;
		String usertype="";
		String branch="";
		int cnt=0;
		String empBranch="0";
		 try
			{
				String qry="select count(*) as cnt,um.internal_code,"
						+ "IFNULL(um.emp_type, 'user') as  user_type,ifnull(um.branch_cd,'0') as emp_branch"
						+ "  from crm_employee_master um "
						+ " INNER JOIN crm_company_master c ON um.emp_company = c.internal_code"
						+ "	where um.username='"+username+"' and um.password='"+password+"' "
						+ " AND um.status = 'A' AND c.status = 'A' "
					    + "group by um.internal_code, IFNULL(user_type, 'user') ";
				System.out.println("ValidateLoginDetails------"+qry);
			
						  List<Map<String,Object>> query_data_obj1=jdbcTemplate.queryForList(qry); 
						  for (Map<String, Object> map : query_data_obj1)
				  		    {
							  cnt=  Integer.parseInt(map.get("cnt").toString());
							   internal_code= map.get("internal_code").toString();
							   usertype=map.get("user_type").toString();
							   empBranch=map.get("emp_branch").toString();
				  		    }
				
		   		System.out.println("cnt------"+cnt);
		   		if(cnt > 0)
		   		{
		   			
				/*
				 * websession.setAttribute("s_username", username.toUpperCase());
				 * websession.setAttribute("s_password", password.toUpperCase());
				 * websession.setAttribute("s_usertype", usertype);
				 * websession.setAttribute("s_loginId", internal_code);
				 * websession.setAttribute("s_branch", branch);
				 * websession.setAttribute("s_branch_int_code", branch_int_code);
				 */
		   			Integer integer =(Integer) request.getSession().getAttribute("uid");
		   	   
		   	        if(integer==null){
		   	            integer=new Integer(0);
		   	            integer++;
		   	            request.getSession().setAttribute("uid",integer);  // it will write data to tables
		   	            request.getSession().setAttribute("s_loginId",internal_code);
		   	            request.getSession().setAttribute("empBranch",empBranch);
		   	        }else{ 
		   	        	
		   	            integer++;
		   	            request.getSession().setAttribute("uid",integer);  // it will write data to tables
		   	        }
		   			//System.out.println("s_username..."+websession.getAttribute("MY_SESSION_MESSAGES"));
		   			
		   			
		   			row_data.put("error_code", "0");
		   			row_data.put("username", username);
		   			row_data.put("password", password);
					row_data.put("internal_code", internal_code);
					row_data.put("s_loginId", internal_code);
					 
					
					row_data.put("usertype", usertype);
				
		   			result.SetError(0,"success",row_data); 
		   		}
		   		else
		   		{
		   			row_data.put("error_code", "-1");
		   			 
		   			row_data.put("data", "Invalid Login");
		   		}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				row_data.put("error_code", "-1");
				row_data.put("data", ex.getCause().toString());
				 
				if (cause instanceof SQLException) { }
				}
		 
		
		return gson.toJson(result);
		
	}
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@RequestMapping("TestAPI")
	public   @ResponseBody String TestAPI(HttpServletRequest request,ModelMap model) throws SQLException, ClassNotFoundException
	{
		ResponseDataGeneric result=new ResponseDataGeneric();
		
		 String qry ="select * from s_sysdb s ";
 
				System.out.println("ValidateLoginDetails------"+qry);
			
						  List<Map<String,Object>> query_data_obj1=jdbcTemplate.queryForList(qry); 
						  
		result.SetSuccess("OK", query_data_obj1);
		Gson gson = new Gson();	
		return gson.toJson(result);
	}
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@RequestMapping("ValidateLoginDetails")
	public   @ResponseBody String ValidateLoginDetails(HttpServletRequest request,ModelMap model) throws SQLException, ClassNotFoundException
	{
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		ResponseDataGeneric result=new ResponseDataGeneric();
		Gson gson = new Gson();	
		JSONObject row_data = new JSONObject();
		JSONObject return_data = new JSONObject();
		

		String internal_code="0";
		Integer branch_int_code=0;
		String due_days="";
		String usertype="";
		String real_name="";
		String branch="";
		String theme="";
		int cnt=0;
		String empBranch="0";
		 try
			{
//				String qry="select count(*) as cnt,um.internal_code,"
//						+ "IFNULL(um.emp_type, 'user') as  user_type,ifnull(um.branch_cd,'0') as emp_branch"
//						+ "  from crm_employee_master um "
//						+ " INNER JOIN crm_company_master c ON um.emp_company = c.internal_code"
//						+ "	where um.username='"+username+"' and um.password='"+password+"' "
//						+ " AND um.status = 'A' AND c.status = 'A' "
//					    + "group by um.internal_code, IFNULL(user_type, 'user') ";
			 
			 String qry ="select s.internal_code, ifnull(direct_login,'No') as direct_login, count(*) as cnt,'admin' as user_type,real_name, "
			 		+ "				ifnull(user_theme,'puerto') as theme ,email_id as fw_user_email,ifnull(password_changed,'No') as password_changed ,"
			 		+ "			IFNULL(uc.due_days,0) AS due_days"
			 		+ "				from s_sysdb s "
			 		+ "				 LEFT JOIN user_category uc ON uc.internal_code = s.category"
			 		+ "				where sysusr='"+username+"' and sysusrkey='"+password+"' and s.active='Yes' "
			 		+ "				group by s.internal_code";
				System.out.println("ValidateLoginDetails------"+qry);
			
						  List<Map<String,Object>> query_data_obj1=jdbcTemplate.queryForList(qry); 
						  for (Map<String, Object> map : query_data_obj1)
				  		    {
							  cnt=  Integer.parseInt(map.get("cnt").toString());
							   internal_code= map.get("internal_code").toString();
							   due_days= map.get("due_days").toString();
							   usertype=map.get("user_type").toString();
//							   empBranch=map.get("emp_branch").toString();
							  
//							  cnt=  Integer.parseInt(map.get("count").toString());
//							   internal_code= map.get("internal_code").toString();
//							   usertype=map.get("user_type").toString();
//							   real_name=map.get("real_name").toString();
//							   theme=map.get("theme").toString(); 
//							   empBranch=map.get("emp_branch").toString();
				  		    }
				
		   		System.out.println("cnt------"+cnt);
		   		if(cnt > 0)
		   		{
		   			
				/*
				 * websession.setAttribute("s_username", username.toUpperCase());
				 * websession.setAttribute("s_password", password.toUpperCase());
				 * websession.setAttribute("s_usertype", usertype);
				 * websession.setAttribute("s_loginId", internal_code);
				 * websession.setAttribute("s_branch", branch);
				 * websession.setAttribute("s_branch_int_code", branch_int_code);
				 */
		   			Integer integer =(Integer) request.getSession().getAttribute("uid");
		   	   
		   	        if(integer==null){
		   	            integer=new Integer(0);
		   	            integer++;
		   	            request.getSession().setAttribute("uid",integer);  // it will write data to tables
		   	            request.getSession().setAttribute("s_loginId",internal_code);
//		   	            request.getSession().setAttribute("empBranch",empBranch);
		   	        }else{ 
		   	        	
		   	            integer++;
		   	            request.getSession().setAttribute("uid",integer);  // it will write data to tables
		   	            request.getSession().setAttribute("s_loginId",internal_code);
		   	        }
		   			//System.out.println("s_username..."+websession.getAttribute("MY_SESSION_MESSAGES"));
		   			
		   	        request.getSession().setAttribute("usr",username);
		   	        request.getSession().setAttribute("pswd",password);
		   	        request.getSession().setAttribute("due_days",due_days);
			   	  
		   			row_data.put("error_code", "0");
		   			row_data.put("username", username);
		   			row_data.put("password", password);
					row_data.put("internal_code", internal_code);
					row_data.put("s_loginId", internal_code);
					 
					
					row_data.put("user_type", usertype);
					row_data.put("user_id", internal_code);
					row_data.put("real_name", real_name); 
					row_data.put("department", "");
					row_data.put("theme", theme);
					row_data.put("due_days", due_days);
					
		   			result.SetError(0,"success",row_data);  
		   		}
		   		else
		   		{
		   			row_data.put("error_code", "-1");
		   			 
		   			row_data.put("data", "Invalid Login");
		   		}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				row_data.put("error_code", "-1");
				row_data.put("data", ex.getCause().toString());
				 
				if (cause instanceof SQLException) { }
				}
		 
		
		return gson.toJson(row_data);
		
	}
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("logout")
	 public @ResponseBody String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	 {
		 ResponseDataGeneric result=new ResponseDataGeneric();
		 Gson gson = new Gson();	
		 JSONObject return_obj = new JSONObject();
			
        request.getSession().invalidate();
        
        return_obj.put("error_code", "0");
        result.SetSuccess("Logged Out Succesfully",return_obj);
        
        return gson.toJson(result);
     }
	 
	 @SuppressWarnings({ "unchecked", "deprecation" })
	public JSONObject GetMemberBasicDetails( String mem_phone_no)
	 {
		 JSONObject mem_details = new JSONObject();
		 mem_details.put("email", "");
		 mem_details.put("cnt", "0");
		 Integer cnt=0;
   		 String email_id = "";
		 
		 String qry="select count(*) as cnt,"
					+ " email_id"
					+ " from wm_member_enrollment"
					+ "	where mobile_no='"+mem_phone_no+"'"
					+ " GROUP BY internal_code";
			
			System.out.println("t_final_insert_qry------"+qry);
		
				  List<Map<String,Object>> query_data_obj1=jdbcTemplate.queryForList(qry);
				  for (Map<String, Object> map : query_data_obj1)
		  		    {
					  cnt= (Integer) map.get("cnt");
					  email_id= (String) map.get("email_id");
						 mem_details.put("email", email_id);
						 mem_details.put("cnt", cnt);
		  		    }
			 
	   		System.out.println("cnt------"+cnt);
			return mem_details;
	 }
		
	 @SuppressWarnings({ "unchecked" })
		@RequestMapping("ValidateMembLoginDetails")
		public   @ResponseBody String ValidateMembLoginDetails(HttpServletRequest request) throws SQLException, ClassNotFoundException, UnsupportedEncodingException
		{
			String mem_phone_no=request.getParameter("mem_phone_no");
			Gson gson = new Gson();	
			JSONObject row_data = new JSONObject();

	
			try
				{
				 JSONObject mem_details = new JSONObject();
				 mem_details = 	GetMemberBasicDetails(mem_phone_no);
				 
				 String count = mem_details.get("cnt").toString();
				 int cnt=Integer.parseInt(count);
				 
				 String email_id = mem_details.get("email").toString();
				 
			   		if(cnt > 0)
			   		{
			   			Random random = new Random();
						String otp_no = String.format("%04d", random.nextInt(10000));
						
						String cc = "";
						String subject = "Login OTP";
						String msg = "Dear Member, <br><br>";
							   msg += "Your WonderClub Login OTP is "+otp_no ;
							   
					    String otp_msg = "Your WonderClub Login OTP is "+otp_no;	   
						 
						
						row_data.put("error_code", "0");
			   		}
			   		else
			   		{
			   			row_data.put("error_code", "-1");
			   			 
			   			row_data.put("data", "Mobile no. not registered with us");
			   		}
				}
				catch (Exception ex) {
					Throwable cause = ex.getCause();
					row_data.put("error_code", "-1");
					row_data.put("data", ex.getCause().toString());
					 
					if (cause instanceof SQLException) { }
					}
			 
			
			return gson.toJson(row_data);
			
		}
	 
		
	
		

		@SuppressWarnings({ "unchecked", "deprecation" })
		public   @ResponseBody JSONObject ValidateMemberLoginDetails(HttpServletRequest request,String mem_phone_no) throws SQLException, ClassNotFoundException
		{
			JSONObject row_data = new JSONObject();
			
			Integer internal_code=0;
			Integer branch_int_code=0;
			String usertype="";
			String login_type="";
			String username="";
			String branch="";
			Integer cnt=0;
			 try
				{
					String qry="select count(*) as cnt, me.internal_code,"
							+ " 'user' as  user_type,"
							+ " 'customer' as login_type,"
							+ " member_name as username,"
							+ " bm.branch_name as branch,me.enrolled_branch as branch_int_code"
							+ " from wm_member_enrollment me "
							+ " inner join wm_branch_master bm on bm.internal_code=me.enrolled_branch"
							+ "	where mobile_no='"+mem_phone_no+"'"
							+ " group by me.internal_code";
					System.out.println("t_final_insert_qry------"+qry);
					
			   		 
			   		
							  List<Map<String,Object>> query_data_obj1=jdbcTemplate.queryForList(qry);
							  for (Map<String, Object> map : query_data_obj1)
					  		    {
								  cnt= (Integer) map.get("cnt");
								   internal_code= (Integer) map.get("internal_code");
								   usertype=map.get("user_type").toString();
								   login_type=map.get("login_type").toString();
								   username=map.get("username").toString();
								   branch=map.get("branch").toString();
								   branch_int_code= (Integer) map.get("branch_int_code");
					  		    }
					
			   		System.out.println("cnt------zzzz"+cnt);
			   		if(cnt > 0)
			   		{
			   			System.out.println("zzzzzzzzz");
			   			HttpSession websession = request.getSession(true);
			   			websession.setAttribute("s_username", username.toUpperCase());
			   			websession.setAttribute("fw_resort_login_type",login_type);
			   			websession.setAttribute("s_usertype", usertype); 
			   			websession.setAttribute("s_loginId", internal_code);
			   			websession.setAttribute("s_login_type", login_type);
			   			websession.setAttribute("s_branch", branch);
			   			websession.setAttribute("s_branch_int_code", branch_int_code);	
			   			
			   			System.out.println("s_username..."+websession.getAttribute("s_username"));
			   			row_data.put("error_code", "0");
			   			row_data.put("username", username);
						row_data.put("internal_code", internal_code);
						row_data.put("s_loginId", internal_code);
						row_data.put("login_type", login_type);
						row_data.put("fw_resort_login_type", login_type);
						row_data.put("usertype", usertype);
						row_data.put("branch", branch);
						row_data.put("s_branch_int_code", branch_int_code);
			   		}
			   		else
			   		{
			   			row_data.put("error_code", "-1");
			   			 
			   			row_data.put("data", "Invalid Login");
			   		}
				}
				catch (Exception ex) {
					ex.printStackTrace();
					Throwable cause = ex.getCause();
					row_data.put("error_code", "-1");
					row_data.put("data", ex.getCause().toString());
					 
					if (cause instanceof SQLException) { }
					}
			 
			
			return row_data;
			
		} 
	 

	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@RequestMapping("ChangeLoginUserPassword")
	public  @ResponseBody String ChangeLoginUserPassword(HttpServletRequest request) throws SQLException, ClassNotFoundException
	{
		Integer cnt=0;
		
		ResponseDataGeneric result=new ResponseDataGeneric();
		Gson gson = new Gson();
	    String loginId=Format.GetSessonUserId(request);
		String password=request.getParameter("password");
		String new_password=request.getParameter("newpassword");
		System.out.println(password);
	    System.out.println(new_password);
		String sql="select count(*) as cnt from "+schema+".wm_user_master "
				+ " u where u.internal_code='"+loginId+"' and u.password='"+password+"'";
		System.out.println(sql);
		
  	 
				  List<Map<String,Object>> query_data_obj1=jdbcTemplate.queryForList(sql); 
				  for (Map<String, Object> map : query_data_obj1)
		  		    {
					  cnt= (Integer) map.get("cnt");
					  System.out.println(cnt);
		            }
		
	 	if(cnt>0)
	 	{
		 		
		 	try
		 	{
				String qry ="update "+schema+".wm_user_master u "
						+ " set u.password='"+new_password+"'"+" where u.internal_code='"+loginId+"'";
				jdbcTemplate.update(qry);
		   }
		 	catch (Exception ex) 
		 	{
		 		ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
		 	}
		 	
		 	result.SetSuccess("Success","Updated Successfully");
	 	}
	 	else
	 	{
	 		result.SetSuccess("Error","Old Password is Wrong");	
	 	}
  	return gson.toJson(result);
	}
	
	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@RequestMapping("ForgotPassword")
	public   @ResponseBody String ForgotPassword(HttpServletRequest request) throws SQLException, ClassNotFoundException
	{
		String username=request.getParameter("fp_user_name");
		String email_id=request.getParameter("fp_email_id");
		ResponseDataGeneric result=new ResponseDataGeneric();
		Gson gson = new Gson();	
		JSONObject row_data = new JSONObject();
		

		String user_cnt="0";
		String user_email_cnt="0";
		String user_password="";  
		 try
			{
				String qry="SELECT count(internal_code) as user_count "
						+ "						FROM crm_employee_master "
						+ "						WHERE username='"+username+"'";
				System.out.println("ForgotPassword cnt------"+qry);
			
			  List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(qry);  
			  user_cnt = query_data_obj.get(0).get("user_count").toString(); 
			  
		   		System.out.println("cnt------"+user_cnt);
		   		if(Integer.parseInt(user_cnt) > 0)
		   		{
		   			String qry1="SELECT count(internal_code) as user_count "
		   					+ "						FROM crm_employee_master  "
		   					+ "						WHERE username = '"+username
		   					+ "'					AND emp_email = '"+email_id+"'";
					System.out.println("ForgotPassword mail cnt------"+qry1);
				
				  List<Map<String,Object>> query_data_obj1=jdbcTemplate.queryForList(qry1);  
				  user_email_cnt = query_data_obj1.get(0).get("user_count").toString(); 
				 
				  if(Integer.parseInt(user_email_cnt) > 0)
		   		{
					  String qry2="SELECT password AS user_password "
			   					+ "						FROM crm_employee_master  "
			   					+ "						WHERE username = '"+username
			   					+ "'					AND emp_email = '"+email_id+"'";
						System.out.println("ForgotPassword ------"+qry2);
					
					  List<Map<String,Object>> query_data_obj2=jdbcTemplate.queryForList(qry2);  
					  user_password = query_data_obj2.get(0).get("user_password").toString(); 
					  
					  
					  String subject = "Forgot Password Request";
					  String mail_html = "";
					  mail_html += "Dear "+username+", <br/><br/>";
					  mail_html += "Please find your existing password : <b>"+user_password+"</b><br/>";
					  mail_html += "kindly change the password once logged-in.";
					  
//					  email_id = "sambath@tarkasoft.com";
					  String mail_result = SendMail.SendManualMailNew( email_id, mail_html, subject);
					  
					  insertToMailHistory(email_id, subject, mail_result);
//					  SendForgotPasswordMails($aobj_context,$fp_user_name,$fp_email_id,$user_password);
					  
//					  result.SetError(0,"success","success"); 
					  
					  row_data.put("error_code", "0");
					  row_data.put("data", mail_result);
		   		}		   		
				  else {
						row_data.put("error_code", "-1");
						row_data.put("data", "Email ID doesn't match with the User Name");
					
				  } 
		   			
		   		}
		   		else
		   		{
		   			row_data.put("error_code", "-1"); 
		   			row_data.put("data", "User Name not found");
		   		}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				row_data.put("error_code", "-1");
				row_data.put("data", ex.getCause().toString());
				 
				if (cause instanceof SQLException) { }
				}
		 
		
		return gson.toJson(row_data);
		
	}
	
	private void insertToMailHistory(String email_id,String subject,String status) throws ClassNotFoundException, SQLException
	{
		JSONObject data_obj = new JSONObject();
		Gson gson = new Gson();	 
		   
		
		try {  
			 String sql="INSERT INTO crm_mail_history (subject, to_mail_id, cc, mail_status, date) "
			 		+ " VALUES ('"+subject+"','"+email_id+"','','"+status+"', now())  ";
			 
				System.out.println("insertToMailHistory..... "+sql);
				jdbcTemplate.execute(sql);
				 
			  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
	}
	
	@RequestMapping("isLoggedIn")
	public   @ResponseBody String isLoggedIn(HttpServletRequest request,ModelMap model) throws SQLException, ClassNotFoundException
	{ 
		JSONObject row_data = new JSONObject();
		Gson gson = new Gson();	
		String data="";
		 
		 try
			{
//				 
//		   			String usr = (String) request.getSession().getAttribute("usr");
//		   			String pswd = (String) request.getSession().getAttribute("pswd");
		   	    
		   			
//		   			if(!usr.isEmpty() && !pswd.isEmpty())
//		   			{
		   				data="1";
//		   			}
//		   			else
//		   				data="0";
				
		   			result.SetError(0,"success",data);  
		   		 
			}
			catch (Exception ex) {
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				row_data.put("error_code", "-1");
				row_data.put("data", ex.getCause().toString());
				 
				if (cause instanceof SQLException) { }
				}
		 
		
		return gson.toJson(result);
		
	}
	
}
