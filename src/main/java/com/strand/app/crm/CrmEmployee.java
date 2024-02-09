package com.strand.app.crm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.strand.app.config.ResponseDataGeneric;

@RestController
@RequestMapping("titpl/CrmEmployee")
public class CrmEmployee {
	
	static Logger logger = Logger.getLogger(CrmEmployee.class);   
	@Value("${app.schema:default}")
	String schema;
	
	@Value("${posSchema:default}")
	String posSchema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	public CrmEmployee() {
	// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("checkDuplicatePhoneNo")
	public  @ResponseBody String checkDuplicatePhoneNo(HttpServletRequest request,@RequestParam("phone") String phone) throws SQLException, ClassNotFoundException
	{ 
		JSONObject data_obj = new JSONObject();
		List<Map<String,Object>> dataList=null;
		Gson gson = new Gson();	

		String loggedinUserIntCode="";
		try {
			loggedinUserIntCode = request.getSession().getAttribute("s_loginId").toString();
			
			String sql="SELECT count(*) as cnt FROM crm_employee_master WHERE emp_mobile_no = '"+phone+"'";
			
			System.out.println("checkDuplicatePhoneNo...."+sql);
			dataList=jdbcTemplate.queryForList(sql);
			 
		} catch (Exception e) {
			loggedinUserIntCode="";
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}//---------To be removed
		
		System.out.println("loggedinUserIntCode..."+loggedinUserIntCode);
		if(loggedinUserIntCode.trim().isEmpty())
		{
			result.SetError(-9,"failure","Session Expired..!!");
			return gson.toJson(result);
		}
		
		  result.SetError(0,"success",dataList);
						 
		 
		return gson.toJson(result);
 
	}
	
	@RequestMapping("/updateEmployeeMaster")
	public  @ResponseBody String updateEmployeeMaster(HttpServletRequest request, @RequestParam("int_code") String int_code , @RequestParam("system_module_table_name")  String system_module_table_name ) throws ClassNotFoundException, SQLException
	{
		JSONObject data_obj = new JSONObject();
		Gson gson = new Gson();	
		
		String loggedinUserIntCode = request.getSession().getAttribute("s_loginId").toString(); 
		
		if(loggedinUserIntCode.trim().isEmpty())
		{
			result.SetError(-9,"failure","Session Expired..!!");
			return gson.toJson(result);
		}
		  
		String code  = CrmUtility.GenerateCodeID(jdbcTemplate, schema, system_module_table_name, "", "", "");
		
		try {  
			 String sql="UPDATE crm_employee_master SET password = 'changeme', emp_code = '"+code+"' "
					 + " WHERE internal_code  = "+int_code+" ";
			 
//			 String sql="UPDATE crm_employee_master SET password = 'changeme@123' "
//					 + " WHERE internal_code  = "+int_code+" ";
			 
				System.out.println(sql);
				jdbcTemplate.execute(sql);
				 
			  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		result.SetError(0,"success","success"); 
		  
		return gson.toJson(result);
	}
	
	@RequestMapping("/employeePostSave")
	public  @ResponseBody String employeePostSave(HttpServletRequest request, @RequestParam("int_code") String int_code , @RequestParam("system_module_table_name")  String system_module_table_name) throws ClassNotFoundException, SQLException
	{
		JSONObject data_obj = new JSONObject();
		Gson gson = new Gson();	
		
		String loggedinUserIntCode = request.getSession().getAttribute("s_loginId").toString(); 
		
		if(loggedinUserIntCode.trim().isEmpty())
		{
			result.SetError(-9,"failure","Session Expired..!!");
			return gson.toJson(result);
		}
		 
		deleteFromEmployeeGroupDetails(int_code);
		updateEmployeeSalesChannel(int_code);
		
		
		try {  
			
			 String sql="INSERT INTO crm_mapped_territory (employee,territory,territory_name) " + 
			 		"SELECT eg.ref_s_ref_data_code, eg.emp_territory, t.terr_name FROM crm_employee_master_territory_group eg  " + 
			 		"LEFT JOIN crm_territory_master t ON t.internal_code = eg.emp_territory " + 
			 		"WHERE ref_s_ref_data_code = '"+int_code+"' ";
			 
				System.out.println(sql);
				jdbcTemplate.execute(sql);
				 
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result.SetError(0,"success","success"); 
		  
		  return gson.toJson(result);
	}
	 
	public String updateEmployeeSalesChannel(String int_code) throws ClassNotFoundException, SQLException
	{
		JSONObject data_obj = new JSONObject();
		Gson gson = new Gson();	 
		   
		
		try {  
			 String sql="UPDATE crm_employee_master " + 
			 		"INNER JOIN crm_company_master c ON c.internal_code = crm_employee_master.emp_company  " + 
			 		"INNER JOIN crm_usr_type_mast ut ON ut.spg_desc = c.user_type " + 
			 		"SET  crm_employee_master.sales_channel_cd = ut.sales_channel_cd " + 
			 		"WHERE crm_employee_master.internal_code  ="+int_code+" ";
			 
				System.out.println(sql);
				jdbcTemplate.execute(sql);
				 
			  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		result.SetError(0,"success","success"); 
		return gson.toJson(result);
	}
	
	public  @ResponseBody String deleteFromEmployeeGroupDetails(String int_code ) throws SQLException, ClassNotFoundException
	{
		 
		Gson gson = new Gson();	
 
		try { 
			String sql = " DELETE FROM crm_mapped_territory WHERE employee = "+int_code+" "; 
			
			System.out.println(sql);
			jdbcTemplate.execute(sql);
			 
		} catch (Exception e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//---------To be removed
		 
		
		  result.SetError(0,"success","success");
						 
		 
		return gson.toJson(result);
 
	}
	@RequestMapping("checkTerritoryState")
	public  @ResponseBody String checkTerritoryState(HttpServletRequest request,@RequestParam("territory") String territory,@RequestParam("ex_territory") String ex_territory) throws SQLException, ClassNotFoundException
	{ 
		JSONObject data_obj = new JSONObject();
		List<Map<String,Object>> dataList1=null;
		List<Map<String,Object>> dataList2=null;
		Gson gson = new Gson();	
 
		String state = "";
//		String state_str = "";
		String res = "";
		String loggedinUserIntCode="";
		try {
			loggedinUserIntCode = request.getSession().getAttribute("s_loginId").toString();
			
//			String sql1=" SELECT GROUP_CONCAT(CAST(terr_state AS CHAR)) AS state_str  FROM crm_territory_master WHERE internal_code IN ("+ex_territory+") ";
			String sql1=" SELECT terr_state  AS state_str  FROM crm_territory_master WHERE internal_code IN ("+ex_territory+") ";
			String sql2=" SELECT terr_state AS state  FROM crm_territory_master WHERE internal_code = '"+territory+"' ";
			
			
			System.out.println("checkTerritoryState sql1...."+sql1);
			dataList1=jdbcTemplate.queryForList(sql1);
			
			System.out.println("checkTerritoryState sql2...."+sql2);
			dataList2=jdbcTemplate.queryForList(sql2);
			 
//			state_str = dataList1.get(0).get("state_str").toString();
			state = dataList2.get(0).get("state").toString();
			
			List<String> state_list=  new ArrayList<String>();
			for(Map m: dataList1)
			{
				String st = m.get("state_str").toString();
				state_list.add(st);
			}
			 
			if(!state_list.contains(state))
			{
				res = "warning";
			}
			
		} catch (Exception e) {
			loggedinUserIntCode="";
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}//---------To be removed
		
		System.out.println("loggedinUserIntCode..."+loggedinUserIntCode);
		if(loggedinUserIntCode.trim().isEmpty())
		{
			result.SetError(-9,"failure","Session Expired..!!");
			return gson.toJson(result);
		}
		
		 
		
		  result.SetError(0,"success",res);
						 
		 
		return gson.toJson(result);
 
	}
}
