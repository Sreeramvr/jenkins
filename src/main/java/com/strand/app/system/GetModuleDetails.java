package com.strand.app.system;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.strand.app.config.ResponseDataGeneric; 

@RestController
@RequestMapping("titpl/GetModuleDetails")
public class GetModuleDetails {
	@SuppressWarnings("unused")
	static Logger logger = Logger.getLogger(GetModuleDetails.class);   
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	JSONObject jsn_obj = new JSONObject();
	public GetModuleDetails() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("getGeneralUserSchemaDetails")
	public @ResponseBody String getGeneralUserSchemaDetails(HttpServletRequest request ) throws UnsupportedEncodingException
	{
		String module_id=request.getParameter("module_id");
		 jsn_obj = new JSONObject();
		
		try
		{
			logger.info("*****getGeneralUserSchemaDetails"); 
			GetSUSerSchemaElementDetailsData(module_id);
			GetSUSerSchemaHeaderDetails(module_id);
			GetSUSerSchemaGroupDetails(module_id);
			 
		}
	  	catch (Exception ex) {
	        Throwable cause = ex.getCause();
	        if (cause instanceof SQLException) { }
		 }
		result.SetSuccess("success",jsn_obj); 
		Gson gson = new Gson();	
		 return gson.toJson(result); 
		 
		
	}
	public void GetSUSerSchemaElementDetailsData(String module_id)
	{
		 try{
	        
	   
	       
	         String final_qry = "select  name,system_name,data_type as Type,ifnull(list_of_values,'') as list_of_values,";
	        			final_qry+=" ifnull(list_of_values,'') as list_of_values, ";
	        			final_qry+=" ifnull(group_code,'0') as ref_s_user_schema_group_code, ";
	        			final_qry+=" ifnull(default_value,'0') as default_value, ";
	        			final_qry+=" ifnull(optional,'0') as optional, ";
	        			
	        			final_qry+=" ifnull(is_ref_module_code_lov,'0') as is_ref_module_code_lov  ,";
	        			final_qry+=" ifnull(su.ref_module_code,0) as ref_module_code,ifnull(ref.file_name,'') as ref_module_table_name ";
	        			final_qry+=" from "+schema+".fbk_ia_s_user_schema_elements su inner join "+schema+".fbk_ia_menu_options s ";
	        			final_qry+=" on s.internal_code=su.schema_code ";
	        			final_qry+="  left join "+schema+".fbk_ia_menu_options ref on ref.internal_code=su.ref_module_code ";
	        			final_qry+=" where schema_code="+module_id +"  and is_multiple_group=0  order by su.sequence";
	          
	     	System.out.println("********* Elements final_qry--------"+final_qry);
	    		 
			
		    		  List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(final_qry);
		    		  jsn_obj.put("1", query_data_obj);
	    		 
	    					  
			   }catch (Exception ex) {
				   ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) {
			        	 ex.printStackTrace();
			        }
			   }
	}
	public void GetSUSerSchemaHeaderDetails(String module_id)
	{
		
		 try{
	        
	    
	         String Sql = "select   ifnull(mo.file_name,'') as system_module_table_name,mo.internal_code,mg.menu_name,mg.menu_type,ifnull(sg.sub_menu_name,'') as sub_menu_name,mo.module_name,";
				Sql +="  mo.display_name, ifnull(load_type,'Internal') as  load_type,ifnull(view_type,'EntryScreen') as  view_type ,ifnull(js_path,'') as js_path ";
				Sql +="  ,ifnull(include_download,'0') as include_download ,ifnull(include_save,0) as include_save,ifnull(include_view,0) as include_view,ifnull(include_close,0) as include_close,ifnull(include_clear,0) as include_clear,ifnull(custom_links,'') as custom_links";
		 		Sql +=" from "+schema+".fbk_ia_menu_groups mg ";
		 		Sql +=" inner join "+schema+".fbk_ia_menu_options mo on mo.menu_group=mg.internal_code";
		 		Sql +=" left join "+schema+".fbk_ia_menu_sub_groups sg on sg.internal_code=mo.menu_subgroup";
		   		Sql +=" where mo.internal_code="+module_id;
		//  System.out.println(Sql);
	     	logger.info("********* GetSUSerSchemaHeaderDetails -------"+Sql);
	    		 
				
		    		  List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(Sql);
		    		  jsn_obj.put("header_details", query_data_obj);
	    		
	    					  
			   }catch (Exception ex) {
				   ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) {
			        	 ex.printStackTrace();
			        }
			   }
	}
	public void GetSUSerSchemaGroupDetails(String module_id)
	{
		JSONObject multi_group = new JSONObject();
		 try{
	        
	   
	         String Sql = "select name as group_name,sug.internal_code, group_id as div_group_id,"
	         		+ "concat("+schema+ ".s.file_name , '_' , system_group_table_name , '_group') as table_name  "
	         		 
	         		+ " from "+schema+".fbk_ia_s_user_schema_group sug inner join "+schema+".fbk_ia_menu_options s on s.internal_code=sug.schema_code"
	         		+ " where multiple=1 and  schema_code="+module_id;
		  
	     	System.out.println("********* GetSUSerSchemaGroupDetails -------"+Sql);
	    		 
		    		  List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(Sql); 
		    		  for (Map<String, Object> map : query_data_obj)
			  		    {
			    			   	 
		    			  String internal_code= map.get("internal_code").toString().trim();
		    			  String div_group_id= map.get("div_group_id").toString().trim();
		    			  String table_name= map.get("table_name").toString().trim();
		    			  String group_name= map.get("group_name").toString().trim();
		    			  JSONObject grp_local_obj = new JSONObject();
		    			  grp_local_obj.put("div_group_id",div_group_id);
		    			  grp_local_obj.put("table_name",table_name);
		    			  grp_local_obj.put("group_id",internal_code);
		    			  grp_local_obj.put("is_add_row",1);
		    			  grp_local_obj.put("is_delete_row",1);
		    			  grp_local_obj.put("parent_group",0);
		    			  List<Map<String,Object>> query_data_elements=null;
		    			  
		    			  query_data_elements=GetSUSerSchemaGroupElementsForGroup(module_id,internal_code);
		    			  
		    			  grp_local_obj.put("elements",query_data_elements);
		    			  multi_group.put(table_name,grp_local_obj);
		    			  
			  		    }
	    		
	    					  
			   }catch (Exception ex) {
				   ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) {
			        	 ex.printStackTrace();
			        }
			   }
		  jsn_obj.put("multi_group", multi_group);
	}
	public  List<Map<String,Object>> GetSUSerSchemaGroupElementsForGroup(String module_id,String group_id)
	{
		  List<Map<String,Object>> query_data_obj=null;
		 try{
	       
	   
	         String Sql = "select su.internal_code,su.name,su.system_name, ";
					 Sql+= " ifnull(us.file_name,'') AS ref_module_table, ";
					 Sql+= " ifnull(us.display_name,'') as ref_module_dis_name,  ";
					 Sql+= " su.data_type as Type,ifnull(su.on_enter,'') as on_enter,'Meta' as meta_data, ";
					 Sql+= " ifnull(su.text_area_rows,2) as text_area_rows, ifnull(su.grid_width,0) as grid_width,";
					 Sql+= " su.is_read_only,ifnull(su.is_ref_module_code_lov,0) as is_ref_module_code_lov, ";
					 Sql+= " ifnull(su.optional,0) as optional ,ifnull(su.ref_module_code,0) as ref_module_code, ";
					 Sql+= " ifnull(su.list_of_values,0) as list_of_values,su.system_name as db_field,ifnull(su.on_exit,'') as  on_exit ";
					 Sql+= " from "+schema+".fbk_ia_s_user_schema_elements su left join  "+schema+".fbk_ia_menu_options us on us.internal_code=su.ref_module_code "; 
					 Sql+= " where su.schema_code="+module_id+" and su.group_code="+group_id+" ";									  
					 Sql+= " order by su.sequence";
					  
					 System.out.println("********* GetSUSerSchemaGroupDetails -------"+Sql);
	    		 
				 
	    		
				
		    	 query_data_obj=jdbcTemplate.queryForList(Sql);  
		    		  
	    		 
	    					  
			   }catch (Exception ex) {
				   ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) {
			        	 ex.printStackTrace();
			        }
			   }
		 return query_data_obj;
	}
}
