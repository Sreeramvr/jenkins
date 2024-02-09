package com.strand.app.system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
@RequestMapping("titpl/AutoComplete")

public class AutoComplete {
	  private static final int String = 0;
	
		static Logger logger = Logger.getLogger(AutoComplete.class);   
		
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
		
	public AutoComplete() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("AssignRefModuleLovValues")
	public @ResponseBody String AssignRefModuleLovValues (HttpServletRequest request,
			@RequestParam("schema_id")  String schema_id,@RequestParam("system_module_table_name") String system_module_table_name 
			 
			) throws SQLException, ClassNotFoundException
	{
		  
	
		 
		 List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		Gson gson = new Gson();	
			
			 JSONObject final_data_obj = new JSONObject();
	      try{
	    	   
	    	 
	   		 
	   		 
	    	  String e_qry=" select su.internal_code,su.ref_module_code, su.system_name, ";
					  e_qry+=" ifnull(su.populate_lov,1) as populate_lov  ";
					  e_qry+=" from fbk_ia_s_user_schema_elements su";
					  e_qry+=" inner join fbk_ia_menu_options s on s.internal_code=su.ref_module_code";
					  e_qry+=" where su.is_ref_module_code_lov=1 ";
					  e_qry+=" and su.schema_code="+schema_id;
					  e_qry+="  and su.ref_module_code<>0 and su.is_multiple_group=0 ";
	    	 
	    	  
	    						query_data_obj=jdbcTemplate.queryForList(e_qry); 
	    						  for (Map<String, Object> map : query_data_obj)
	    				  		    {
	    				    			   	 
	    			    			  String internal_code= map.get("internal_code").toString().trim();
	    			    			  String ref_module_code= map.get("ref_module_code").toString().trim();
	    			    			  String system_name= map.get("system_name").toString().trim();
	    			    			  String populate_lov= map.get("populate_lov").toString().trim();
	    			    			  JSONObject meta_colmn_obj = new JSONObject();
	    			    			  meta_colmn_obj=GetValuesFromMetaColumns(ref_module_code);
	    			    			  List<Map<String,Object>> query_data_obj1= new ArrayList<Map<String,Object>>();;
	    			    			  if(!meta_colmn_obj.get("field_id").toString().equals(""))
	    			    			  {
		    			    			  query_data_obj1=GetRefModuleLoveValues(meta_colmn_obj.get("field_id").toString(), meta_colmn_obj.get("table_name").toString());
		    			    			  
		    			    			  final_data_obj.put(system_name, query_data_obj1);
	    			    			  }
	    				  		    }
	    			    		  
	    					
			   }
	      		catch (Exception ex) {
	      			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
	    

	  	result.SetSuccess("success", final_data_obj);
		 return gson.toJson(result);
 
	}
	
	private JSONObject GetValuesFromMetaColumns(String ref_module_code)
	{
		JSONObject return_obj = new JSONObject();
		return_obj.put("table_name", "");
		return_obj.put("field_id", "");
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();
		String query= "select  field_id,table_name from fbk_ia_module_meta_columns	where module_id="+ref_module_code;
				 
		System.out.println("GetValuesFromMetaColumns Sql ="+query);
		try
		{
			

				query_data_obj=jdbcTemplate.queryForList(query);
				
				for(Map map2:query_data_obj)
				{
					return_obj.put("field_id",map2.get("field_id").toString());
					return_obj.put("table_name",map2.get("table_name").toString());
					 
				}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.info("Error inGetAlreadyExistscount Sql="+query);
		}
		
		return return_obj;
	}
	private List<Map<String,Object>> GetRefModuleLoveValues(String field_id,String table_name)
	{
		JSONObject return_obj = new JSONObject();
		return_obj.put("table_name", "");
		return_obj.put("field_id", "");
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();
		String query= "select  internal_code,"+field_id+" as value from "+table_name;
				 
		System.out.println("GetRefModuleLoveValues Sql ="+query);
		try
		{
				query_data_obj=jdbcTemplate.queryForList(query); 	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.info("Error inGetAlreadyExistscount Sql="+query);
		}
		
		return query_data_obj;
	}
	 
	@RequestMapping("GetModuleKeyDataForElement")
	public  @ResponseBody String GetModuleKeyDataForElement(HttpServletRequest request) throws SQLException, ClassNotFoundException
	{
		String t_calling_table_name=request.getParameter("calling_table_name");
		String internal_code=request.getParameter("internal_code");
		String t_pop_up_table_name=request.getParameter("pop_up_table_name");
		String t_system_name=request.getParameter("system_name");
		
		  calling_table_name=t_calling_table_name;
		  pop_up_table_name=t_pop_up_table_name;
		   system_name=t_system_name;
		 
	
		 
		 List<Map<String,Object>> query_data_obj=null;
		Gson gson = new Gson();	
			
	      try{
	    	   
	    		JSONObject meta_col_obj = new JSONObject();
	   		 meta_col_obj=GetModuleMetaColumnsData(t_pop_up_table_name);
	   		 String field_id=meta_col_obj.get("field_id").toString();
	   		 

			 if(t_pop_up_table_name.contains("cloud_pos_master.ITEM_MASTER")) 
				 field_id= "ITEM_CODE";
	   		 
	   		 System.out.println("here");
	    	  String e_qry="select  internal_code,"+field_id+" as name ";
	    	  e_qry+="	from  "+t_pop_up_table_name+"  where internal_code="+internal_code;
	    		 System.out.println("*******GetModuleKeyDataForElement Final s Qry==>"+e_qry);
	    	   	  	
	    			query_data_obj=jdbcTemplate.queryForList(e_qry);
	    			    		  
	    					
			   }
	      		catch (Exception ex) {
	      			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
	    
	  
	  	result.SetSuccess("success", query_data_obj);
		 return gson.toJson(result);
 
	}
	
	@RequestMapping("getAutoCompleteFromFrameWorkNew")
	public @ResponseBody String getAutoCompleteFromFrameWorkNew(HttpServletRequest request
			) throws SQLException, ClassNotFoundException, IOException
	{
		String t_calling_table_name=request.getParameter("calling_table_name");
		String t_pop_up_table_name=request.getParameter("pop_up_table_name");
		String t_system_name=request.getParameter("system_name");
		String t_value=request.getParameter("value");
		
		 
		  calling_table_name=t_calling_table_name;
		  pop_up_table_name=t_pop_up_table_name;
		  value=t_value.trim();
		  system_name=t_system_name;
			ResponseDataGeneric result=new ResponseDataGeneric();
			Gson gson = new Gson();	
			List<Map<String,Object>> query_data_obj=null;
		 
			 
	      try{
	         
	    	 
	    	  
	    	  query_data_obj=  GetAutoCompleteQrySet();
	    	 
	    	 
			   }
	      		catch (Exception ex) {
	      			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
	      result.SetSuccess("success", query_data_obj);
	      return gson.toJson(result); 
	     
	}
 
	 List<Map<String, Object>> GetAutoCompleteQrySet()
	{
		 List<Map<String,Object>> query_data_obj=null;
		 JSONObject meta_col_obj = new JSONObject();
		 meta_col_obj=GetModuleMetaColumnsData(pop_up_table_name);
		 String field_id1=meta_col_obj.get("field_id1").toString();
		 String field_id2=meta_col_obj.get("field_id2").toString();
		  
		 String field_id1_val= field_id1;
			if(!(field_id2).isEmpty())
				field_id1_val="   concat(concat( ifnull("+field_id1+",'') ,' - '), ifnull("+field_id2+",''))";
		 
			 String field_id2_val= field_id2;
				if(!(field_id1).isEmpty())
					field_id2_val="   concat(concat( ifnull("+field_id2+",'') ,' - '), ifnull("+field_id1+",''))";
			String qry1=" select   internal_code,"+field_id1_val+" as value from ";
					qry1+=pop_up_table_name+" where "+field_id1+" like '"+value+"%' ";
					qry1+= " order by "+field_id1+" limit 20";
			
			String qry2=" select  internal_code,"+field_id2_val+" as value from ";
			qry2+=pop_up_table_name+" where "+field_id2+" like '"+value+"%' ";
			qry2+= " order by "+field_id2+" limit 20";	
			System.out.println("qry1.."+qry1);
			System.out.println("qry2.."+qry2);
			JSONObject query_data_obj1 = new JSONObject();
			query_data_obj1=	GetResultSet(qry1);
					int list_size= Integer.parseInt(query_data_obj1.get("list_size").toString());
					if(list_size==0)
					{
						query_data_obj1=	GetResultSet(qry2);
					}
					 
					list_size= Integer.parseInt(query_data_obj1.get("list_size").toString());
					if(list_size>0)
		 return (List<Map<java.lang.String, Object>>) query_data_obj1.get("query_data_obj");
					else return query_data_obj;
		 
	}
	 
	 JSONObject GetResultSet(String Sql)
	 {
		 JSONObject data_set = new JSONObject();
		 
		 List<Map<String,Object>> query_data_obj=null ;
		 int list_size=0;
		 data_set.put("list_size", 0);
		 data_set.put("query_data_obj","");
		 try{
		        
			 logger.info("Sql----"+Sql);	
			 
			  query_data_obj=jdbcTemplate.queryForList(Sql); 
			  list_size=query_data_obj.size();
			  data_set.put("query_data_obj", query_data_obj);
			  data_set.put("list_size",list_size);
			  
    		System.out.println("query_data_obj...."+query_data_obj);
    					  
		   }catch (Exception ex) {
		        Throwable cause = ex.getCause();
		        if (cause instanceof SQLException) {
		        	 ex.printStackTrace();
		        }
		   }
	 	 	
		 return data_set;
	 }
	 
	 @RequestMapping("GetModuleMetaColumnsData")
	 @ResponseBody JSONObject  GetModuleMetaColumnsData(String t_pop_up_table_name)
	{
		JSONObject meta_col_obj = new JSONObject();
		 meta_col_obj.put("field_id1","");
		 meta_col_obj.put("field_id2","");
		 meta_col_obj.put("field_id","");
		 
		 try{
	        
		     String Sql =  " select field_id,field_id1,ifnull(field_id2,' ') as field_id2 from "+schema+".fbk_ia_module_meta_columns where table_name='"+t_pop_up_table_name+"'";
		   	 System.out.println("GetModuleMetaColumnsData..."+Sql);
	    		 
					 List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(Sql); 
					 for (Map<String, Object> map : query_data_obj) {
		    			    String field_id1=map.get("field_id1").toString().trim();
		    			    String field_id2=map.get("field_id2").toString().trim();
		    			    String field_id=map.get("field_id").toString().trim();
		    			    meta_col_obj.put("field_id1",field_id1);
		    			    meta_col_obj.put("field_id2",field_id2);
		    			    meta_col_obj.put("field_id",field_id);
		    			    System.out.println("meta_col_obj..."+meta_col_obj);
		    		  }
				  
			   }catch (Exception ex) {
				   ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) {
			        	 ex.printStackTrace();
			        }
			   }
		 	return meta_col_obj;	
	}
}
