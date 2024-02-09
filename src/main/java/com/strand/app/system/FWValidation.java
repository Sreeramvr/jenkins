package com.strand.app.system;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import com.strand.app.config.Format;
import com.strand.app.config.ResponseDataGeneric;

public  class FWValidation {
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	public FWValidation() {
		// TODO Auto-generated constructor stub
	}
	// will be called from popup grid display to check additional filters
	public static String GetPopUpFilerData(HttpServletRequest request,String calling_table_name,String pop_up_table_name,String all_data_obj_str, String clicked_id_system_name) throws ParseException
	{
		String enl_id=Format.GetSessonUserId(request);
		System.out.println("enl_id..."+enl_id);
		if(enl_id.isEmpty() || enl_id.equals(""))
		{
			try
			{
			enl_id=request.getParameter("FW_Login_User");
			System.out.println("User Id after session expiry..."+enl_id);
			}
			catch(Exception e)
			{
				System.out.println("User Id after Catch..."+e.getLocalizedMessage());
			}
		}

			String ext_pop_cond="";
			System.out.println("clicked_id_system_name=="+clicked_id_system_name+"---clicked_id_system_name "+clicked_id_system_name+"sss");
	 
			if(calling_table_name.equals("crm_employee_master") && clicked_id_system_name.equals("emp_department"))
			{
				 
				ext_pop_cond=" and crm_lookup_values.lookup_type='2' " ;
			}
			else if(calling_table_name.equals("crm_employee_master") && clicked_id_system_name.equals("emp_designation"))
			{
				 
				ext_pop_cond=" and crm_lookup_values.lookup_type='3' " ;
			}
			else if(calling_table_name.equals("crm_property_master") && clicked_id_system_name.equals("type"))
			{
//				 System.out.println("a");
				ext_pop_cond=" and crm_lookup_values.lookup_type='4' " ;
			}
//			else if(calling_table_name.equals("crm_employee_master") && clicked_id_system_name.equals("emp_reporting_to"))
//			{
//				 
//				ext_pop_cond="  " ;
//			}
//			else if(calling_table_name.equals("crm_employee_master") && clicked_id_system_name.equals("emp_territory"))
//			{
//				 
//				ext_pop_cond="  and crm_lookup_values.lookup_type='5' " ;
//			}
//			else if(calling_table_name.equals("crm_party_master") && clicked_id_system_name.equals("territory"))
//			{
//				 System.out.println("a");
//				 
//				ext_pop_cond="  and crm_lookup_values.lookup_type='5' " ;
//			}
			else if(calling_table_name.equals("crm_party_master") && clicked_id_system_name.equals("classification"))
			{
				 
				ext_pop_cond="  and crm_lookup_values.lookup_type='6' " ;
			}
			else if(calling_table_name.equals("crm_party_master") && clicked_id_system_name.equals("customer_type"))
			{
				 
				ext_pop_cond="  and crm_lookup_values.lookup_type='7' " ;
			}
			else if(calling_table_name.equals("crm_party_master") && clicked_id_system_name.equals("promotion_source"))
			{
				 
				ext_pop_cond="  and crm_lookup_values.lookup_type='8' " ;
			}
			else if(calling_table_name.equals("crm_party_master") && clicked_id_system_name.equals("designation"))
			{
				 
				ext_pop_cond="  and crm_lookup_values.lookup_type='9' " ;
			}
			else if(calling_table_name.equals("crm_party_master") && clicked_id_system_name.equals("designation_type"))
			{
				  
				ext_pop_cond="  and crm_lookup_values.lookup_type='10' " ;
			}
			else if(calling_table_name.equals("crm_prospect") && clicked_id_system_name.equals("prospect_status"))
			{
				 
				ext_pop_cond="  and crm_lookup_values.lookup_type='11' " ;
			}
			else if(calling_table_name.equals("crm_booking") && clicked_id_system_name.equals("id_type"))
			{
				 
				ext_pop_cond="  and crm_lookup_values.lookup_type='12' " ;
			}
			else if(calling_table_name.equals("crm_booking") && clicked_id_system_name.equals("property"))
			{
				 
				ext_pop_cond=" AND crm_branch_master.company IN ("
						+ " SELECT internal_code FROM crm.crm_company_master WHERE crm_company_master.user_type='Wonderla' "
						+ "  )" ;
			}
			else if(calling_table_name.equals("crm_booking") && clicked_id_system_name.equals("customer"))
			{
				String loggedinUserIntCode=request.getParameter("loggedinUserIntCode");
				
				ext_pop_cond=" AND  crm_party_master.internal_code IN (\r\n"
						+ "SELECT crm_party_master.internal_code FROM crm.crm_party_master\r\n"
						+ " INNER JOIN crm.crm_employee_master ON crm_employee_master.internal_code = crm_party_master.created_by\r\n"
						+ " WHERE crm_employee_master.emp_company = (SELECT crm_employee_master.emp_company FROM crm.crm_employee_master \r\n"
						+ " WHERE crm_employee_master.internal_code =  '"+loggedinUserIntCode
						+ "') )    " ;
			}
			else if(calling_table_name.equals("crm_booking") && clicked_id_system_name.equals("prospect"))
			{
				String loggedinUserIntCode=request.getParameter("loggedinUserIntCode");
				
				ext_pop_cond=" AND  crm_prospect.internal_code IN (\r\n"
						+ "SELECT crm_prospect.internal_code FROM crm.crm_prospect\r\n"
						+ " INNER JOIN crm.crm_employee_master ON crm_employee_master.internal_code = crm_prospect.created_by\r\n"
						+ " WHERE crm_employee_master.emp_company = (SELECT crm_employee_master.emp_company FROM crm.crm_employee_master \r\n"
						+ " WHERE crm_employee_master.internal_code =  '"+loggedinUserIntCode
						+ "') )    " ;
			}
 

		return ext_pop_cond;
	}
	


	

	
	
}