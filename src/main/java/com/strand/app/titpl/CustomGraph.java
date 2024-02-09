package com.strand.app.titpl;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.strand.app.config.Format;
import com.strand.app.config.ResponseDataGeneric;
@RestController
@RequestMapping("titpl/CustomGraph")

public class CustomGraph {
	  private static final int String = 0;
	
		static Logger logger = Logger.getLogger(CustomGraph.class);   
		
		String loginId="";
		String calling_table_name="";
		String pop_up_table_name="";
		String system_name="";
		String value="";
		
		@Value("${app.schema:default}")
		String schema;
		
		@Autowired
	    JdbcTemplate jdbcTemplate;
		
		@Autowired
		ResponseDataGeneric result;
		
	public CustomGraph() {
		// TODO Auto-generated constructor stub
	}
	@RequestMapping("GetStateDetails")
	public @ResponseBody String GetStateDetails (HttpServletRequest request
			 
			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
			Date run_date=null;
			String dis_run_date="";
			String html="";
			List<Map<String,Object>> query_data_obj1=null;
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	  String e_qry="SELECT internal_code,IFNULL(name,'Unknown') AS name "
	    	  		+ "FROM state order by name";
	    	  System.out.println("GetStateDetails------"+e_qry);
	    	 
			  query_data_obj1=jdbcTemplate.queryForList(e_qry);
			  
	      }
	      catch (Exception ex) {
    			ex.printStackTrace();
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) { }
		 }
  

	result.SetSuccess("success", query_data_obj1);
	 return gson.toJson(result);
	}	
	@RequestMapping("GetAgeGroupDetails")
	public @ResponseBody String GetAgeGroupDetails (HttpServletRequest request
			 
			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
			Date run_date=null;
			String dis_run_date="";
			String html="";
			List<Map<String,Object>> query_data_obj1=null;
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	  String e_qry=" SELECT internal_code,age_group from agegroup order by age_group";
	    	  System.out.println("GetAgeGroupDetails------"+e_qry);
	    	 
			  query_data_obj1=jdbcTemplate.queryForList(e_qry);
			  
	      }
	      catch (Exception ex) {
    			ex.printStackTrace();
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) { }
		 }
  

	result.SetSuccess("success", query_data_obj1);
	 return gson.toJson(result);
	}	
	
	
	@RequestMapping("GetLocationHTMLDetails")
	public @ResponseBody String GetLocationHTMLDetails (HttpServletRequest request
			 
			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
			Date run_date=null;
			String dis_run_date="";
			String html="";
			List<Map<String,Object>> query_data_obj1=null;
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	  String e_qry="SELECT internal_code,name FROM location_master";
	    	  System.out.println("GetStateDetails------"+e_qry);
	    	 
			  query_data_obj1=jdbcTemplate.queryForList(e_qry);
			  
	      }
	      catch (Exception ex) {
    			ex.printStackTrace();
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) { }
		 }
  

	result.SetSuccess("success", query_data_obj1);
	 return gson.toJson(result);
	}	
	@RequestMapping("GetDistrictDetails")
	public @ResponseBody String GetDistrictDetails (HttpServletRequest request,
			@RequestParam("state")  String state
			 
			 
			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
			Date run_date=null;
			String dis_run_date="";
			String html="";
			List<Map<String,Object>> query_data_obj1=null;
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	  String state_cond="";
	    	  if(!state.equals(""))
	    		  	state_cond="and state="+state+"";
	    	  else
	    		  state_cond="";
	    	  String e_qry="SELECT internal_code, IFNULL(name,'Unknown') AS name FROM district"
	    	  		+ "			  where 1=1 "+state_cond+""
	    	  		+ "			 order by name";
	    	  System.out.println("GetStateDetails------"+e_qry);
	    	 
			  query_data_obj1=jdbcTemplate.queryForList(e_qry);
			  
	      }
	      catch (Exception ex) {
    			ex.printStackTrace();
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) { }
		 }
  

	result.SetSuccess("success", query_data_obj1);
	 return gson.toJson(result);
	}
	@RequestMapping("GetCityDetails")
	public @ResponseBody String GetCityDetails (HttpServletRequest request,
			@RequestParam("state")  String state
			 
			 
			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
			Date run_date=null;
			String dis_run_date="";
			String html="";
			List<Map<String,Object>> query_data_obj1=null;
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	  String state_cond="";
	    	  if(!state.equals(""))
	    		  	state_cond="and state="+state+"";
	    	  else
	    		  state_cond="";
	    	  String e_qry="SELECT internal_code, IFNULL(name,'Unknown') AS name FROM city_master "
	    	  		+ "			  where 1=1 "+state_cond+""
	    	  		+ "			 order by name";
	    	  System.out.println("GetCityDetails------"+e_qry);
	    	 
			  query_data_obj1=jdbcTemplate.queryForList(e_qry);
			  
	      }
	      catch (Exception ex) {
    			ex.printStackTrace();
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) { }
		 }
  

	result.SetSuccess("success", query_data_obj1);
	 return gson.toJson(result);
	}
	
	@RequestMapping("GetCompanyDetails")
	public @ResponseBody String GetCompanyDetails (HttpServletRequest request
			 
			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
			Date run_date=null;
			String dis_run_date="";
			String html="";
			List<Map<String,Object>> query_data_obj1=null;
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	  String e_qry=" SELECT internal_code,name FROM company_master \r\n"
	    	  		+ "			where 1=1 ";
	    	  System.out.println("GetCompanyDetails------"+e_qry);
	    	 
			  query_data_obj1=jdbcTemplate.queryForList(e_qry);
			  
	      }
	      catch (Exception ex) {
    			ex.printStackTrace();
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) { }
		 }
  

	result.SetSuccess("success", query_data_obj1);
	 return gson.toJson(result);
	}	
	@RequestMapping("GetHospitalLOV")
	public @ResponseBody String GetHospitalLOV (HttpServletRequest request,
			@RequestParam("company")  String company
			 
			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
			Date run_date=null;
			String dis_run_date="";
			String html="";
			List<Map<String,Object>> query_data_obj1=null;
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	  String e_qry="SELECT internal_code, name FROM hospital_master "
	    	  		+ "			  where company="+company+"";
	    	  System.out.println("GetHospitalLOV------"+e_qry);
	    	 
			  query_data_obj1=jdbcTemplate.queryForList(e_qry);
			  
	      }
	      catch (Exception ex) {
    			ex.printStackTrace();
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) { }
		 }
  

	result.SetSuccess("success", query_data_obj1);
	 return gson.toJson(result);
	}	
	@RequestMapping("GetRunDates")
	public @ResponseBody String GetRunDates (HttpServletRequest request
			 
			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
			Date run_date=null;
			String dis_run_date="";
			String html="";
			List<Map<String,Object>> query_data_obj1=null;
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	  String e_qry=" SELECT DISTINCT date_format(run_date,'%Y-%m-%d') as run_date, "
	    	  		+ "DATE_FORMAT(run_date,'%d-%b-%Y') AS dis_run_date "
	    	  		+ "FROM sample_master WHERE run_date IS NOT NULL order by run_date";
	    	  System.out.println("run date query------"+e_qry);
	    	  html="<option id='0000-00-00' value='0000-00-00' Selected>None</option>";
				 
			  query_data_obj1=jdbcTemplate.queryForList(e_qry);
			  
	      }
	      catch (Exception ex) {
    			ex.printStackTrace();
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) { }
		 }
  

	result.SetSuccess("success", query_data_obj1);
	 return gson.toJson(result);
	}
	 @RequestMapping("GetCityNames")
		public @ResponseBody String GetCityNames (HttpServletRequest request
	 
				 ) throws SQLException, ClassNotFoundException
		{
			List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
			Gson gson = new Gson();	
				String name=null;
				String html="";
				List<Map<String,Object>> query_data_obj1=null;
				 JSONObject final_data_obj = new JSONObject();
		      try{
		    	  String e_qry="SELECT  cm.internal_code as city_int_code,name as city_name"
		    	  		+ " FROM city_master cm"
		    	  		+ " LEFT JOIN user_category_sub_group ucs ON cm.internal_code=ucs.city"
		    	  		+ " LEFT JOIN s_sysdb s ON s.category=ucs.ref_s_ref_data_code"
		    	  		+ " WHERE MAP='YES'  ";
		    	  System.out.println("city query------"+e_qry);
		    	  html="<option id='city' value=''  Selected>None</option>";
					 
				  query_data_obj1=jdbcTemplate.queryForList(e_qry);
				  
		      }
		      catch (Exception ex) {
	    			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
	  

		result.SetSuccess("success", query_data_obj1);
		 return gson.toJson(result);
		}
	@RequestMapping("GetWHOLineages")
	public @ResponseBody String GetWHOLineages (HttpServletRequest request,
			@RequestParam("from_date")  String from_date,	
			@RequestParam("to_date")  String to_date,	
			@RequestParam("run_date")  String run_date,
			@RequestParam("who_name")  String who_name
			 
			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
			String html="";
			 String date_cond="";
			 JSONObject data_obj = new JSONObject();
			List<Map<String,Object>> query_data_obj1=null;
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	
		    	 
	    	  if(run_date.equals("0000-00-00"))
	    		  date_cond="and (s.sample_col_date between '"+from_date+"' and '"+to_date+"')";
	  		  else 
	  			  date_cond="and (s.run_date = '"+run_date+"')";
	  		
	    	  String e_qry=" SELECT DISTINCT p.lineage AS lineage , l.internal_code "
	    	  		+ "FROM pangolin_data p "
	    	  		+ "INNER JOIN sample_master s ON s.sample_id = p.sample_id "
	    	  		+ " LEFT JOIN lineage_master l ON l.pangolin_lineage = p.lineage"
	    	  		+ " WHERE 1=1 "+date_cond+""
	    	  		+ " and l.who_common_name = '"+who_name+"'";
	    	  System.out.println("who lin query------"+e_qry);
	    	  html="<div class='' style='float:left;'> "
	    	  		+ " <label class='control-label' style='margin-right:10px'> </label></div> "
	    	  		+ " <div class='form-group' style='float:left;'> "
	    	  		+ "<div class='checkbox-list'>";
			  query_data_obj1=jdbcTemplate.queryForList(e_qry);
			  for (Map<String, Object> map : query_data_obj1)
	  		    {
				  html+="<label class='checkbox-inline'>	"
				  		+ " <input type='checkbox'  name='lineage_type' "
				  		+ "id='lin_"+map.get("internal_code")+"'"
				  				+ " onclick='DG.RefreshGraph();'>"+map.get("lineage")+"</input></label>";
	  		    }
			  html+="</div></div> </div>";
			  data_obj.put("lin",html);
			  
	      }
	      catch (Exception ex) {
    			ex.printStackTrace();
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) { }
		 }
  

	result.SetSuccess("success", data_obj);
	 return gson.toJson(result);
	}
	@RequestMapping("GetActualLineages")
	public @ResponseBody String GetActualLineages (HttpServletRequest request,
			@RequestParam("from_date")  String from_date,	
			@RequestParam("to_date")  String to_date,	
			@RequestParam("run_date")  String run_date
			 
			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
		JSONObject data_obj = new JSONObject();
		List<Map<String,Object>> query_data_obj2 = null;
		 String lin_html="";
		 String common_html="";
		 String common_radio_html="";
			List<Map<String,Object>> query_data_obj1=null;
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	  String date_cond="";
	    	 
	    	  if(run_date.equals("0000-00-00"))
	  			date_cond="and (s.sample_col_date between '"+from_date+"' and '"+to_date+"')";
	  		else 
	  			date_cond="and (s.run_date = '"+run_date+"')";
	  		
	  		String qry="SELECT DISTINCT p.lineage AS lineage , l.internal_code  "
	  				+ " FROM pangolin_data p"
	  				+ " INNER JOIN sample_master s ON s.sample_id = p.sample_id"
	  				+ " LEFT JOIN lineage_master l ON l.pangolin_lineage = p.lineage"
	  				+ " WHERE 1=1 "+date_cond+"";
	    	 
	    	  System.out.println("get lin query------"+qry);
	    	  lin_html="<div class='' style='float:left;'> "
	    	  		+ "<label class='control-label' style='margin-right:10px'> </label>"
	    	  		+ "</div>"
	    	  		+ "<div class='form-group' style='float:left;'>"
	    	  		+ "<div class='checkbox-list'>";
	    	  
			  query_data_obj1=jdbcTemplate.queryForList(qry);
			  for (Map<String, Object> map : query_data_obj1)
	  		    {
				  lin_html+="<label class='checkbox-inline'>	"
				  		+ "<input type='checkbox'  name='lineage_type'  "
				  		+ "id='lin_"+map.get("internal_code")+"' onclick='DG.RefreshGraph();'>"
				  		+map.get("lineage")+"</input>"
				  		+ "</label>";
	  		    }
			  lin_html+="</div> </div> </div>";
			  data_obj.put("lin",lin_html);
			  
			  
			  qry="SELECT DISTINCT DISTINCT IFNULL(l.who_common_name,'Other') AS who_common_name"
			  		+ " FROM pangolin_data p "
			  		+ "INNER JOIN sample_master s ON s.sample_id = p.sample_id "
			  		+ "LEFT JOIN lineage_master l ON l.pangolin_lineage = p.lineage "
			  		+ "WHERE 1=1 "+date_cond+"";
			  System.out.println("get common query------"+qry);
			  common_html="<div class='' style='float:left;'> "
			  		+ " <label class='control-label' style='margin-right:10px'> </label>"
			  		+ "</div> "
			  		+ "<div class='form-group' style='float:left;'>"
			  		+ "<div class='checkbox-list'>";
			  query_data_obj1=jdbcTemplate.queryForList(qry);
			  for (Map<String, Object> map : query_data_obj1)
	  		    {
				  common_html+="<label class='checkbox-inline'>	"
				  		+ " <input type='checkbox'  name='common_name'"
				  		+ "	id='common_name_"+map.get("who_common_name")+"' "
				  				+ " onclick='DG.RefreshGraph();'>"+map.get("who_common_name")+"</input></label>";
	  		    }
			  common_html+="</div> </div> </div>";
			  data_obj.put("common",common_html);
			  
			  
			  
			  qry="SELECT DISTINCT DISTINCT IFNULL(l.who_common_name,'Other') AS who_common_name "
			  		+ "FROM pangolin_data p "
			  		+ "INNER JOIN sample_master s ON s.sample_id = p.sample_id "
			  		+ "LEFT JOIN lineage_master l ON l.pangolin_lineage = p.lineage"
			  		+ " WHERE 1=1 "+date_cond+"";
			  System.out.println("get common radio query------"+qry);
			  query_data_obj1=jdbcTemplate.queryForList(qry);
			  
			  
			  common_radio_html="<div class='' style='float:left;'> "
			  		+ "<label class='control-label' style='margin-right:10px'> </label></div>"
			  		+ "	<div class='form-group' style='float:left;'>														\r\n"
			  		+ "	<div class='checkbox-list'>";
			  for (Map<String, Object> map : query_data_obj1)
	  		    {
				  common_radio_html+="<label class='radio-inline'>	"
				  		+ "<input type='radio'  name='common_name_radio'"
				  		+ "	id='common_name_radio_"+map.get("who_common_name")+"'"
				  				+ " onclick='DG.GetWHOLineages(\""+map.get("who_common_name")+"\");'>"
				  						+ ""+map.get("who_common_name")+"</input></label>";
	  		    }
			  common_radio_html+="</div> </div> </div>";
			  data_obj.put("common_radio",common_radio_html);
			 
			  String sub_html="";
			  qry="SELECT DISTINCT gene FROM next_clade_data_substitution_group";
				  System.out.println("get substitution query------"+qry);
				  query_data_obj1=jdbcTemplate.queryForList(qry);
				 sub_html="<div class='form-group' style='float:left;'>	"
				 		+ "<div class='checkbox-list'>";
				  for (Map<String, Object> map : query_data_obj1)
		  		    {
					  String gene="";
					  String selected="";
					  if(map.get("gene").equals(""))
						  gene="None";
					  else
						  gene=(java.lang.String) map.get("gene");
					  if(map.get("gene").equals("S"))
						 selected="checked";
					  else
						 selected="";
					  sub_html+="<label class='radio-inline'>	"
					  		+ "<input type='radio' "+selected+" name='dash_gene'"
					  		+ "	id='"+gene+"' val='"+gene+"'" 
					  				+ " onclick='DG.RefreshGraph();'>"
					  						+ ""+gene+"</input></label>";
		  		    }
				  sub_html+="<label class='radio-inline'>	"
					  		+ "<input type='radio'  name='dash_gene'"
					  		+ "	id='All' value='All'"
					  				+ " onclick='DG.RefreshGraph();'>All</input></label></div>";
				  data_obj.put("common_radio",common_radio_html);
				  data_obj.put("sub_html",sub_html);
				  
			  
	      }
	      
	      catch (Exception ex) {
    			ex.printStackTrace();
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) { }
		 }
	      finally
	      {
	    	  
	      }
  

	result.SetSuccess("success", data_obj);
	 return gson.toJson(result);
	}
	@RequestMapping("RefreshGraph")
	public @ResponseBody String RefreshGraph (HttpServletRequest request,
			@RequestParam("y_type")  String y_type,	
			@RequestParam("x_type")  String x_type,
			@RequestParam("from_date")  String from_date,
			@RequestParam("run_date")  String run_date,
			@RequestParam("to_date")  String to_date,
			@RequestParam("work_place")  String work_place,
			@RequestParam("hospital_admission")  String hospital_admission,
			@RequestParam("icu_admission")  String icu_admission,
			@RequestParam("infected_after_vaccine")  String infected_after_vaccine,
			@RequestParam("gender")  String gender,
			@RequestParam("vaccine_status")  String vaccine_status,
			@RequestParam("vaccine")  String vaccine,
			@RequestParam("age_group")  String age_group,
			@RequestParam("mucormycosis")  String mucormycosis,
			@RequestParam("state")  String state,
			@RequestParam("district")  String district,
			@RequestParam("city")  String city,
			@RequestParam("location")  String location,
			@RequestParam("re_infection")  String re_infection,
			@RequestParam("common_name")  String common_name,
			@RequestParam("lineage_type")  String lineage_type,
			@RequestParam("lin_type")  String lin_type,
			@RequestParam("combor")  String combor,
			@RequestParam("div")  String div,
			@RequestParam("perc_div")  String perc_div,
			@RequestParam("company")  String company ,
			@RequestParam("hospital")  String hospital,
			@RequestParam("from")  String from,
			@RequestParam("due_days")  String due_days
			 ) throws SQLException, ClassNotFoundException
	{
		System.out.println("hospital------"+hospital);
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
		List<Map<String,Object>> query_data_obj2 = null;	
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	   
	    	 
	    	 // String due_days =(java.lang.String) request.getSession().getAttribute("due_days");
	    	//  String usr =request.getSession().getAttribute("usr").toString();
	    	   System.out.println("due_days------"+due_days);
	    	  //System.out.println("usr------"+usr);
	   		 String company_cond="";
	   		if(!company.equals("''"))
	   		 {
		    	  String e_qry=" select IFNULL(GROUP_CONCAT(internal_code),0) as hosp_int_codes from "
		    	  		+ "		hospital_master where company="+company;
		    	  query_data_obj=jdbcTemplate.queryForList(e_qry); 
		    	  for (Map<String, Object> map : query_data_obj)
				  {
		    		  String hosp_int_codes= map.get("hosp_int_codes").toString().trim();
		    		  company_cond="and s.hospital in ("+hosp_int_codes+")";
				  }
		    			    		  
		    					
	   		 }
	   		 
	   		String hospital_cond="";
	   		if(from.equals("custom"))
	   		{
		   		if(!hospital.equals("0"))
		   		 {
		   			hospital_cond="and s.hospital in ("+hospital+")";			
		   		 }
	   		}
	   		System.out.println("hospital_cond------"+hospital_cond);
	   		String state_cond="";
			if(!state.equals("0"))
				state_cond="and s.state = "+state+"";
			
			String district_cond="";
			if(!district.equals("0"))
				district_cond="and s.district = "+district+"";
			
			String city_cond="";
			if(!city.equals("0"))
				city_cond="and s.city = "+city+"";
			
			String location_cond="";
			if(!location.equals("0"))
			{
				if(location=="Unknown")
					location_cond="and ifnull(s.location,'') = ''";
				else
					location_cond="and s.location = '"+location+"'";
			}
			age_group=age_group.substring(0, age_group.length() - 1);  
			String[] age_arr = age_group.split(",");
			 
			String age_grp_cond="";
			 
			for(int i=0;i<age_arr.length;i++)
			{
				if(!age_arr[i].equals("0"))
				{
					String age_int_code=age_arr[i].substring(4,1);
					if(age_grp_cond.equals(""))	
						age_grp_cond="and (a.internal_code = '"+age_int_code+"'";
					else
						age_grp_cond+=" or a.internal_code = '"+age_int_code+"'";
				}
			}
			if(!age_grp_cond.equals(""))
				age_grp_cond+=")";
			gender = gender.replaceAll("\\s+", "");
			 System.out.println("age_grp_cond------"+age_grp_cond);
			gender=gender.substring(0, gender.length() - 1);  
			String[] gender_arr = gender.split(",");
			 
			String gender_cond="";
			 
			for(int i=0;i<gender_arr.length;i++)
			{
				//System.out.println("gender_arr[i]------"+gender_arr[i]);
				if(!gender_arr[i].toString().equals("0"))
				{
					//System.out.println("after if cond gender_arr[i]------"+gender_arr[i]);
					if(gender_cond.equals(""))	
						gender_cond="and (s.ender = '"+gender_arr[i]+"'";
					else
						gender_cond+=" or s.gender = '"+gender_arr[i]+"'";
				}
			}
			if(!gender_cond.equals(""))
				gender_cond+=")";
			  
			System.out.println("gender_cond------"+gender_cond);
			vaccine=vaccine.substring(0, vaccine.length() - 1);  
			String[] vaccine_arr = vaccine.split(",");
			 
			String vaccine_cond="";
			 
			for(int i=0;i<vaccine_arr.length;i++)
			{
				if(!vaccine_arr[i].equals("0"))
				{
					String vac_int_code=vaccine_arr[i].substring(4,1);
					if(vaccine_cond.equals(""))	
						vaccine_cond="and (s.vaccine_1 = "+vac_int_code+" or s.vaccine_2 = "+vac_int_code+"";
					else
						vaccine_cond+=" or  (s.vaccine_1 = "+vac_int_code+" or s.vaccine_2 = "+vac_int_code+"";
				}
			}
			if(!vaccine_cond.equals(""))
				vaccine_cond+=")";
			
			String vac_status_cond=""; 
			if(!vaccine_status.equals(""))
			{
				vaccine_status=vaccine_status.substring(0, vaccine_status.length() - 1);  
				String[] vaccine_status_arr = vaccine_status.split(",");
				if(vaccine_status_arr[0].equals("first_dose"))
					vac_status_cond+=" and ifnull(s.vaccine_1_date,'0000-00-00')!='0000-00-00'";
				if(vaccine_status_arr[1].equals("sec_dose"))
					vac_status_cond+=" and ifnull(s.vaccine_2_date,'0000-00-00')!='0000-00-00'";
			}
			String hosp_adm_cond="";
			if(!hospital_admission.equals(""))
			{ 
				hospital_admission=hospital_admission.substring(0, hospital_admission.length() - 1);
				String[] hospital_admission_arr = hospital_admission.split(",");
				if(hospital_admission_arr.equals("h_ad_yes") && hospital_admission_arr.equals("h_ad_no"))
					hosp_adm_cond="";
				else if(hospital_admission_arr.equals("h_ad_yes"))
					hosp_adm_cond="and ifnull(s.admission_date,'0000-00-00')!='0000-00-00'";
				else if(hospital_admission_arr.equals("h_ad_no"))
					hosp_adm_cond="and ifnull(s.admission_date,'0000-00-00')='0000-00-00'";
			}
			
			String icu_adm_cond="";
			if(!icu_admission.equals(""))
			{
				icu_admission=icu_admission.substring(0, icu_admission.length() - 1);
				String[] icu_admission_arr = icu_admission.split(",");
				
				if(icu_admission_arr[0].equals("i_ad_yes") && icu_admission_arr[1].equals("i_ad_no"))
					icu_adm_cond="";
				else if(icu_admission_arr[0].equals("i_ad_yes"))
					icu_adm_cond="and ifnull(s.icu_date,'0000-00-00')!='0000-00-00'";
				else if(icu_admission_arr[1].equals("i_ad_no"))
					icu_adm_cond="and ifnull(s.icu_date,'0000-00-00')='0000-00-00'";
			}
			String afr_covid_cond="";
			if(!infected_after_vaccine.equals(""))
			{
				infected_after_vaccine=infected_after_vaccine.substring(0, infected_after_vaccine.length() - 1);
				String[] infected_after_vaccine_arr = infected_after_vaccine.split(",");
				 
				if(infected_after_vaccine_arr[0].equals("afr_yes") && infected_after_vaccine_arr[1].equals("afr_no"))
					afr_covid_cond="";
				else if(infected_after_vaccine_arr[0].equals("afr_yes"))
					afr_covid_cond="and (ifnull(s.vaccine_1_date,'0000-00-00')!='0000-00-00' or ifnull(s.vaccine_2_date,'0000-00-00')!='0000-00-00')";
				else if(infected_after_vaccine_arr[1].equals("afr_no"))
					afr_covid_cond="and (ifnull(s.vaccine_1_date,'0000-00-00')!='0000-00-00' and  ifnull(s.vaccine_2_date,'0000-00-00')!='0000-00-00')";
			}
			String muc_cond="";
			if(!mucormycosis.equals(""))
			{
				mucormycosis=mucormycosis.substring(0, mucormycosis.length() - 1);
				String[] mucormycosis_arr = mucormycosis.split(",");
			 
				if(mucormycosis_arr[0].equals("m_yes") &&  mucormycosis_arr[1].equals("m_no"))
					muc_cond="";
				else if(mucormycosis_arr[0].equals("m_yes"))
					muc_cond="and ifnull(s.mucormycosis,'0000-00-00')!='0000-00-00'";
				else if(mucormycosis_arr[1].equals("m_no"))
					muc_cond="and ifnull(s.mucormycosis,'0000-00-00')='0000-00-00'";
			}
			String re_inf_cond="";
			if(!re_infection.equals(""))
			{
				re_infection=re_infection.substring(0, re_infection.length() - 1);
				String[] re_infection_arr = re_infection.split(",");
				  
				if(re_infection_arr[0].equals("e_inf_yes") && re_infection_arr[1].equals("re_inf_no"))
					re_inf_cond="";
				else if(re_infection_arr[0].equals("re_inf_yes"))
					re_inf_cond="and ifnull(s.reinfection,'No')='Yes'";
				else if(re_infection_arr[1].equals("re_inf_no"))
					re_inf_cond="and ifnull(s.reinfection,'No')='No'";
			}
			String wp_cond="";
			if(!work_place.equals(""))
			{
				work_place=work_place.substring(0, work_place.length() - 1);
				String[] work_place_arr = work_place.split(",");
				 
				if(work_place_arr[0].equals("Home") && work_place_arr[1].equals("Office"))
					wp_cond="";
				else if(work_place_arr[0].equals("Home"))
					wp_cond="and ifnull(s.workplace,'')='Home'";
				else if(work_place_arr[1].equals("Office"))
					wp_cond="and ifnull(s.workplace,'')='Office'";
			}
			String comp_cond="";
			if(!combor.equals(""))
			{
				work_place=combor.substring(0, combor.length() - 1);
				String[] combor_arr = combor.split(",");
				
				 
				for(int i=0;i<combor_arr.length;i++)
				{
					if(!combor_arr[i].equals("0"))
					{
						//String com_int_code=combor_arr[i].substring(4,1);
						//if(comp_cond.equals(""))	
						//	comp_cond="( com_name = '"+com_int_code+"'";
						//else
							//comp_cond+=" and  com_name = '"+com_int_code+"'";
					}
				}
			}
			String com_string=GetComName(common_name,lin_type);
			String lin_string=GetLinName(lineage_type);
			String lineage_cond="";
			String common_name_cond="";
			if(!com_string.equals(""))
				common_name_cond="and "+com_string+"";
			else
				common_name_cond="";
			if(!lin_string.equals(""))
				lineage_cond="and "+lin_string+"";
			else
				lineage_cond="";
			String date_cond="";
			String hosp_cond="";
			String vac_cond="";
			String user_date_cond="";
			
			if(run_date.equals("0000-00-00"))
				date_cond="and (s.sample_col_date between '"+from_date+"' and '"+to_date+"')";
			else 
				date_cond="and (s.run_date = '"+run_date+"')";
			if(!due_days.equals("0"))
				user_date_cond=" AND s.sample_col_date < (SELECT DATE_SUB(CURRENT_DATE(), INTERVAL "+due_days+" DAY)) ";
			else
				user_date_cond="";
			String common_filer_cond=date_cond+""+user_date_cond+""+company_cond+""+hospital_cond+""+hosp_cond+""+
						wp_cond+""+hosp_adm_cond+""+icu_adm_cond+""+afr_covid_cond+""+muc_cond+""+re_inf_cond+""+wp_cond+""+
					age_grp_cond+""+gender_cond+""+vac_cond+""+vac_status_cond+""+
						state_cond+""+district_cond+""+city_cond+""+location_cond+""+
					lineage_cond+""+common_name_cond;
				 
			System.out.println("cdsfsdf"+x_type);
			System.out.println("cdsfsdf"+y_type);
			String select_field="";
			if(x_type.equals("x_month") && y_type.equals("y_lineage"))
			{	
				if(lin_type.equals("lineage"))
					select_field="p.lineage";
				else
					select_field="ifnull(who_common_name,'Other')";
			 
				String qry="SELECT a.mon as mon,sample_col_date as sample_col_date, GROUP_CONCAT(lineage,'~',cnt) as data,"
						+ "SUM(cnt) AS tot FROM ("
						+ " SELECT "+select_field+" as lineage, DATE_FORMAT(s.sample_col_date, '%y%m') as  sample_col_date,DATE_FORMAT(s.sample_col_date,'%b-%y') AS mon,"
						+ " SUM(1) as cnt"
						+ " FROM pangolin_data p"
						+ " INNER JOIN sample_master s ON s.sample_id = p.sample_id"
						+ " "
						+ " LEFT JOIN lineage_master l ON l.pangolin_lineage = p.lineage"
						 
						+ " where 1=1 "+common_filer_cond+""
						+ " GROUP BY DATE_FORMAT(s.sample_col_date,'%b-%y'),"
						+ " "+select_field+",DATE_FORMAT(s.sample_col_date, '%y%m')"
						+ " )a  GROUP BY mon,sample_col_date"
						+ " order by sample_col_date  ";
			
						//echo "<pre>".$qry; die(); 
				System.out.println("cdsfsdf"+qry);
				query_data_obj2 =jdbcTemplate.queryForList(qry);
				 
				  
			}
			else if(x_type.equals("x_month") && y_type.equals("y_gender"))
			{	
				 
				String qry="SELECT a.mon,sample_col_date, GROUP_CONCAT(gender,'~',cnt) AS data,"
						+ "SUM(cnt) AS tot FROM ("
						+ " SELECT IF(IFNULL(s.gender,'Others')='','Others',s.gender) as gender, "
						+ " DATE_FORMAT(s.sample_col_date, '%y%m') as  sample_col_date,DATE_FORMAT(s.sample_col_date,'%b-%y') AS mon,"
						+ " SUM(1) as cnt"
						+ " FROM pangolin_data p"
						+ " INNER JOIN sample_master s ON s.sample_id = p.sample_id"
						+ " "
						+ " LEFT JOIN lineage_master l ON l.pangolin_lineage = p.lineage"
						 
						+ " where 1=1 "+common_filer_cond+""
						+ " GROUP BY DATE_FORMAT(s.sample_col_date,'%b-%y'),"
						+ "  IF(IFNULL(s.gender,'Others')='','Others',s.gender),DATE_FORMAT(s.sample_col_date, '%y%m')"
						+ " )a  GROUP BY mon,sample_col_date"
						+ " order by sample_col_date  ";
			
						// echo "<pre>".$qry; die(); 
				System.out.println("cdsfsdf"+qry);
				query_data_obj2 =jdbcTemplate.queryForList(qry);
				 
				  
			}
			else if(x_type.equals("x_month") && y_type.equals("y_age"))
			{	
				 
				String qry="SELECT a.mon,sample_col_date, GROUP_CONCAT(age_group,'~',cnt) AS data,"
						+ "SUM(cnt) AS tot FROM ("
						+ " SELECT a.age_group, "
						+ " DATE_FORMAT(s.sample_col_date, '%y%m') as  sample_col_date,DATE_FORMAT(s.sample_col_date,'%b-%y') AS mon,"
						+ " SUM(1) as cnt"
						+ " FROM pangolin_data p"
						+ " INNER JOIN sample_master s ON s.sample_id = p.sample_id"
						+ " INNER JOIN agegroup a ON s.age BETWEEN a.st_age AND a.en_age "
						+ " LEFT JOIN lineage_master l ON l.pangolin_lineage = p.lineage"
						 
						+ " where 1=1 "+common_filer_cond+""
						+ " GROUP BY "
						+ "  a.age_group,DATE_FORMAT(s.sample_col_date, '%y%m')"
						+ " )a  GROUP BY mon,sample_col_date"
						+ " order by sample_col_date  ";
			
						// echo "<pre>".$qry; die(); 
				System.out.println("cdsfsdf"+qry);
				query_data_obj2 =jdbcTemplate.queryForList(qry);
				 
				  
			}
	      }
			
			  
	      		catch (Exception ex) {
	      			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
	    

	  	result.SetSuccess("success", query_data_obj2);
		 return gson.toJson(result);
 
	}
	@RequestMapping("GetSummaryData")
	public @ResponseBody String GetSummaryData (HttpServletRequest request,
			@RequestParam("from_date")  String from_date,
			@RequestParam("to_date")  String to_date,
			@RequestParam("samples_collected")  String samples_collected,
			@RequestParam("deaths")  String deaths,
			@RequestParam("critical")  String critical,
			@RequestParam("discharged")  String discharged,
			@RequestParam("ventilator")  String ventilator,
			@RequestParam("progressive_samples_collected")  String progressive_samples_collected,
			@RequestParam("covid_patients")  String covid_patients,
			@RequestParam("active_cases")  String active_cases,
			@RequestParam("progressive_positive")  String progressive_positive,
			@RequestParam("progressive_nof_deaths")  String progressive_nof_deaths,
			@RequestParam("nof_house_surey")  String nof_house_surey,
			@RequestParam("population_coverd")  String population_coverd,
			@RequestParam("houses_covered")  String houses_covered,
			@RequestParam("patients_with_flu")  String patients_with_flu,
			@RequestParam("cases_in_hospital")  String cases_in_hospital,
			@RequestParam("cases_in_home_isolation")  String cases_in_home_isolation

			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
		List<Map<String,Object>> query_data_obj2 = null;	
			 JSONObject full_data_obj = new JSONObject();
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	  String e_qry="SELECT	SUM(samples_collected) as samples_collected,"
	    	  		+ "SUM(discharged) as discharged,SUM(deaths) as deaths,SUM(critical) as critical,"
	    	  		+ "SUM(ventilator) as ventilator,SUM(progressive_samples_collected) as progressive_samples_collected,"
	    	  		+ "SUM(covid_patients) as covid_patients,SUM(active_cases) as active_cases,SUM(progressive_positive) as progressive_positive,"
	    	  		+ "SUM(progressive_nof_deaths) progressive_nof_deaths ,SUM(nof_house_surey) as nof_house_surey,"
	    	  		+ "SUM(population_coverd) as population_coverd,SUM(houses_covered) as houses_covered,"
	    	  		+ "SUM(patients_with_flu) as patients_with_flu,"
	    	  		+ "SUM(cases_in_hospital) as cases_in_hospital,"
	    	  		+ "SUM(cases_in_home_isolation) as cases_in_home_isolation"
                    +" FROM summary_data_set "
                    + "WHERE DATE BETWEEN '"+from_date+"' AND '"+to_date+"' ";
		    	  System.out.println("GetSummaryData------"+e_qry);
		    	  query_data_obj2=jdbcTemplate.queryForList(e_qry);
		  for (Map<String, Object> map : query_data_obj2)
		    {
			  final_data_obj.put("samples_collected",map.get("samples_collected"));
			  final_data_obj.put("discharged",map.get("discharged"));
			  final_data_obj.put("deaths",map.get("deaths"));
			  final_data_obj.put("critical",map.get("critical"));
			  final_data_obj.put("ventilator",map.get("ventilator"));
			  final_data_obj.put("progressive_samples_collected",map.get("progressive_samples_collected"));
			  final_data_obj.put("covid_patients",map.get("covid_patients"));
			  final_data_obj.put("active_cases",map.get("active_cases"));
			  final_data_obj.put("progressive_positive",map.get("progressive_positive"));
			  final_data_obj.put("progressive_nof_deaths",map.get("progressive_nof_deaths"));
			  final_data_obj.put("nof_house_surey",map.get("nof_house_surey"));
			  final_data_obj.put("population_coverd",map.get("population_coverd"));
			  final_data_obj.put("houses_covered",map.get("houses_covered"));
			  final_data_obj.put("patients_with_flu",map.get("patients_with_flu"));
			  final_data_obj.put("cases_in_hospital",map.get("cases_in_hospital"));
			  final_data_obj.put("cases_in_home_isolation",map.get("cases_in_home_isolation"));

		    }
	      }
			
	      catch (Exception ex) {
	      			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
	    full_data_obj.put("final_data_obj",final_data_obj);

	  	
			List<Map<String,Object>> query_data_obj1= new ArrayList<Map<String,Object>>();;
		
			List<Map<String,Object>> query_data_obj3 = null;	
				 JSONObject full_data_obj1 = new JSONObject();
				 JSONObject final_data_obj1= new JSONObject();
				 JSONArray final_data_array = new JSONArray();

		      try{
		    	  String e_qrys="SELECT	DATE_FORMAT(date, '%e-%m-%Y') as Date,sum(samples_collected) as samples_collected,"
		    	  		+ "sum(discharged) as discharged,"
		    	  		+ "sum(deaths) as deaths,"
		    	  		+ "sum(critical) as critical,"
		    	  		+ "sum(ventilator) as ventilator,"
		    	  		+ "sum(progressive_samples_collected) as progressive_samples_collected,"
		    	  		+ "sum(covid_patients) as covid_patients,"
		    	  		+ "sum(active_cases) as active_cases,"
		    	  		+ "sum(progressive_positive) as progressive_positive,"
		    	  		+ "sum(progressive_nof_deaths) progressive_nof_deaths ,"
		    	  		+ "sum(nof_house_surey) as nof_house_surey,"
		    	  		+ "sum(population_coverd) as population_coverd,"
		    	  		+ "sum(houses_covered) as houses_covered,"
		    	  		+ "sum(patients_with_flu) as patients_with_flu,"
		    	  		+ "sum(cases_in_hospital) as cases_in_hospital,"
		    	  		+ "sum(cases_in_home_isolation) as cases_in_home_isolation"
	                    +" FROM summary_data_set "
	                    + "WHERE DATE BETWEEN '"+from_date+"' AND '"+to_date+"'"
	                   + "GROUP BY DATE";
			    	  System.out.println("GetSummaryData------"+e_qrys);
			    	  query_data_obj3=jdbcTemplate.queryForList(e_qrys);
			  for (Map<String, Object> map : query_data_obj3)
			    {
	              JSONObject data_obj = new JSONObject();
	              data_obj.put("date",map.get("date"));
	              data_obj.put("samples_collected",map.get("samples_collected"));
	              data_obj.put("discharged",map.get("discharged"));
	              data_obj.put("deaths",map.get("deaths"));
	              data_obj.put("critical",map.get("critical"));
	              data_obj.put("ventilator",map.get("ventilator"));
	              data_obj.put("progressive_samples_collected",map.get("progressive_samples_collected"));
	              data_obj.put("covid_patients",map.get("covid_patients"));
	              data_obj.put("active_cases",map.get("active_cases"));
	              data_obj.put("progressive_positive",map.get("progressive_positive"));
				  data_obj.put("progressive_nof_deaths",map.get("progressive_nof_deaths"));
				  data_obj.put("nof_house_surey",map.get("nof_house_surey"));
				  data_obj.put("population_coverd",map.get("population_coverd"));
				  data_obj.put("houses_covered",map.get("houses_covered"));
				  data_obj.put("patients_with_flu",map.get("patients_with_flu"));
				  data_obj.put("cases_in_hospital",map.get("cases_in_hospital"));
				  data_obj.put("cases_in_home_isolation",map.get("cases_in_home_isolation"));
				  final_data_array.add(data_obj);
			    }
			  System.out.println(final_data_array);
		      }
				
		      catch (Exception ex) {
		      			ex.printStackTrace();
				        Throwable cause = ex.getCause();
				        if (cause instanceof SQLException) { }
				 }
		    full_data_obj.put("final_data_obj1",final_data_array);
		    full_data_obj.put("final_data_obj",final_data_obj);


		  result.SetSuccess("success", final_data_array);
			// return gson.toJson(result);
			 result.SetSuccess("success", full_data_obj);
			 return gson.toJson(result);
			 
 
	}
	@RequestMapping("GetSummaryDataPopUpDetails")
	public @ResponseBody String GetSummaryDataPopUpDetails (HttpServletRequest request,
			@RequestParam("from_date")  String from_date,
			@RequestParam("to_date")  String to_date,
			@RequestParam("clicked_feild")  String clicked_feild
		


			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
		List<Map<String,Object>> query_data_obj2 = null;	
			 JSONObject full_data_obj = new JSONObject();
			 JSONObject final_data_obj = new JSONObject();
			 JSONArray final_data_array = new JSONArray();
	      try{
	    	//  String clicked_field="";
	    	  String field = "";

	    	  if (clicked_feild.equals("Samples collected")) {
	    	      field = "samples_collected";
	    	  } else if (clicked_feild.equals("Discharged")) {
	    	      field = "discharged";
	    	  } else if (clicked_feild.equals("Deaths")) {
	    	      field = "deaths";
	    	  } else if (clicked_feild.equals("Critical")) {
	    	      field = "critical";
	    	  } else if (clicked_feild.equals("Ventilator")) {
	    	      field = "ventilator";
	    	  } else if (clicked_feild.equals("Progressive Samples Collected")) {
	    	      field = "progressive_samples_collected";
	    	  } else if (clicked_feild.equals("Covid Patients")) {
	    	      field = "covid_patients";
	    	  } else if (clicked_feild.equals("Active Cases")) {
	    	      field = "active_cases";
	    	  } else if (clicked_feild.equals("Progressive Positive")) {
	    	      field = "progressive_positive";
	    	  } else if (clicked_feild.equals("Progressive Nof Deaths")) {
	    	      field = "progressive_nof_deaths";
	    	  } else if (clicked_feild.equals("No of House Surey")) {
	    	      field = "nof_house_surey";
	    	  }else if (clicked_feild.equals("Houses Covered")) {
	    	      field = "houses_covered";
	    	  } else if (clicked_feild.equals("Population Coverd")) {
	    	      field = "population_coverd";
	    	  } else if (clicked_feild.equals("Patients With Flu")) {
	    	      field = "patients_with_flu";
	    	  }else if (clicked_feild.equals("Active Cases In Hospital")) {
	    	      field = "cases_in_hospital";
	    	  }else if (clicked_feild.equals("Active Cases In Home isolation")) {
	    	      field = "cases_in_home_isolation";
	    	  }
	          
	    	  System.out.println("clicked_feild------"+clicked_feild);
	    	  System.out.println("field------"+field);
	    	  String e_qry="SELECT  date," + field + " as val "
	    	  		+ "FROM summary_data_set "
	    	  		+ "WHERE DATE BETWEEN '"+from_date+"' AND '"+to_date+"'"
	    	  		 +"GROUP BY date ";
	        //	echo "<pre>".$e_qry; die(); 
	                  
		    	  System.out.println("GetSummaryDataPopUpDetails------"+e_qry);
		    	  query_data_obj2=jdbcTemplate.queryForList(e_qry);
		    	  for (Map<String, Object> map : query_data_obj2) {
		              JSONObject data_obj = new JSONObject();
		              data_obj.put("date", map.get("date"));
		              data_obj.put("val", map.get("val"));
		              final_data_array.add(data_obj);
		          }
	      }
			
	      catch (Exception ex) {
	      			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
		    //full_data_obj.put("data_obj",data_obj);

			 result.SetSuccess("success", final_data_array);
			 return gson.toJson(result);
			 
 
	}
	@RequestMapping("GetSummaryDataPopUpDetailsTrends")
	public @ResponseBody String GetSummaryDataPopUpDetailsTrends (HttpServletRequest request,
			@RequestParam("from_date")  String from_date,
			@RequestParam("to_date")  String to_date,
			@RequestParam("clicked_feild")  String clicked_feild
		


			 ) throws SQLException, ClassNotFoundException
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
		List<Map<String,Object>> query_data_obj2 = null;	
			 JSONObject full_data_obj = new JSONObject();
			 JSONObject final_data_obj = new JSONObject();
			 JSONArray final_data_array = new JSONArray();
	      try{
	    	//  String clicked_field="";
	    	  String field = "";

	    	  if (clicked_feild.equals("Samples collected")) {
	    	      field = "samples_collected";
	    	  } else if (clicked_feild.equals("Discharged")) {
	    	      field = "discharged";
	    	  } else if (clicked_feild.equals("Deaths")) {
	    	      field = "deaths";
	    	  } else if (clicked_feild.equals("Critical")) {
	    	      field = "critical";
	    	  } else if (clicked_feild.equals("Ventilator")) {
	    	      field = "ventilator";
	    	  } else if (clicked_feild.equals("Progressive Samples Collected")) {
	    	      field = "progressive_samples_collected";
	    	  } else if (clicked_feild.equals("Covid Patients")) {
	    	      field = "covid_patients";
	    	  } else if (clicked_feild.equals("Active Cases")) {
	    	      field = "active_cases";
	    	  } else if (clicked_feild.equals("Progressive Positive")) {
	    	      field = "progressive_positive";
	    	  } else if (clicked_feild.equals("Progressive Nof Deaths")) {
	    	      field = "progressive_nof_deaths";
	    	  } else if (clicked_feild.equals("No of House Surey")) {
	    	      field = "nof_house_surey";
	    	  }else if (clicked_feild.equals("Houses Covered")) {
	    	      field = "houses_covered";
	    	  } else if (clicked_feild.equals("Population Coverd")) {
	    	      field = "population_coverd";
	    	  } else if (clicked_feild.equals("Patients With Flu")) {
	    	      field = "patients_with_flu";
	    	  }else if (clicked_feild.equals("Active Cases In Hospital")) {
	    	      field = "cases_in_hospital";
	    	  }else if (clicked_feild.equals("Active Cases In Home isolation")) {
	    	      field = "cases_in_home_isolation";
	    	  }
	          
	          
	    	  System.out.println("clicked_feild------"+clicked_feild);
	    	  System.out.println("field------"+field);
	    	  String e_qry="SELECT  date," + field + " as val "
	    	  		+ "FROM summary_data_set "
	    	  		+ "WHERE DATE BETWEEN '"+from_date+"' AND '"+to_date+"'"
	    	  		 +"ORDER BY date ";
	        //	echo "<pre>".$e_qry; die(); 
	                  
		    	  System.out.println("GetSummaryDataPopUpDetails------"+e_qry);
		    	  query_data_obj2=jdbcTemplate.queryForList(e_qry);
		    	  for (Map<String, Object> map : query_data_obj2) {
		              JSONObject data_obj = new JSONObject();
		              data_obj.put("date", map.get("date"));
		              data_obj.put("val", map.get("val"));
		              final_data_array.add(data_obj);
		          }
	      }
			
	      catch (Exception ex) {
	      			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
		    //full_data_obj.put("data_obj",data_obj);

			 result.SetSuccess("success", final_data_array);
			 return gson.toJson(result);
			 
 
	}
	public String GetComName(String com_name,String lin_type)
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		String com_cond="";
		String com_string="";
		com_name=com_name.substring(0, com_name.length() - 1);
		if(lin_type.equals("lineage"))
			com_string =  com_name.replaceAll("common_name_radio_","");
		else
			com_string =  com_name.replaceAll("common_name_","");
		com_string="'"+com_string.replaceAll(",","','")+"'";
		System.out.println("com_string--"+com_string);
		String qry="select ifnull(group_concat(QUOTE(pangolin_lineage)),'') as lineage "
				+ "from lineage_master where who_common_name in ("+com_string+")";
		System.out.println("com_string--"+qry);
		query_data_obj=jdbcTemplate.queryForList(qry);
		  for (Map<String, Object> map : query_data_obj)
		    {
			  String lin=map.get("lineage").toString();
			  if(!lin.equals(""))
				  com_cond="(lineage = "+lin.replaceAll(","," OR lineage = ")+")";
		    }
		  System.out.println("com_string--"+com_cond);
	//	if(!empty($lobj_get_lin_mame['lineage']))
			//$com_cond="(lineage = ".str_replace(","," OR lineage = ",$lobj_get_lin_mame['lineage']).")";
		return com_cond;
	}
	public String GetLinName(String lineage_type)
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		String lin_cond="";
		if(!lineage_type.equals(""))
		{
			lineage_type=lineage_type.substring(0, lineage_type.length() - 1);
			lineage_type =  lineage_type.replaceAll("lin_","");
			System.out.println("lineage_type--"+lineage_type);
			//com_name=com_name.substring(0, com_name.length() - 1);
			String qry="select ifnull(group_concat(QUOTE(pangolin_lineage)),'') as lineage \r\n"
					+ " from lineage_master where internal_code in ("+lineage_type+")";
			System.out.println("com_string--"+qry);
			query_data_obj=jdbcTemplate.queryForList(qry);
			  for (Map<String, Object> map : query_data_obj)
			    {
				  String lin=map.get("lineage").toString();
				  if(!lin.equals(""))
					  lin_cond="(lineage = "+lin.replaceAll(","," OR lineage = ")+")";
			    }
			  System.out.println("lin string--"+lin_cond); 
		}
		return lin_cond;
	}

	private int replace(java.lang.String string2, java.lang.String string3, java.lang.String com_name) {
		// TODO Auto-generated method stub
		return 0;
	}
	 
}
