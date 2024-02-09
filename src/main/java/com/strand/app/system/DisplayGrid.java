package com.strand.app.system;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.strand.app.config.Format;
import com.strand.app.config.ResponseDataGeneric;


@RestController
@RequestMapping("titpl/DisplayGrid")

public class DisplayGrid {
	 
		static Logger logger = Logger.getLogger(DisplayGrid.class);   
		String loginId="";
		@Value("${app.schema:default}")
		String schema;
		
		@Autowired
	    JdbcTemplate jdbcTemplate;
		
		@Autowired
		ResponseDataGeneric result;
		
	public DisplayGrid() {
		// TODO Auto-generated constructor stub
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("viewUserSchemaModuleDetails")
	public @ResponseBody String viewUserSchemaModuleDetails(HttpServletRequest request) throws SQLException, ClassNotFoundException, UnsupportedEncodingException
	{
		String rows_text=request.getParameter("rows");
		int limit=Integer.parseInt(rows_text);
		String page_text=request.getParameter("page");
		int page=Integer.parseInt(page_text);
		
		String sidx=request.getParameter("sidx");
	 
		
		String sord=request.getParameter("sord");
		String search=request.getParameter("_search");
		String table_name=request.getParameter("table_name");
	 
		//request.setCharacterEncoding("UTF8"); 
//		String loggedinUserIntCode = "";
	 
		 loginId=Format.GetSessonUserId(request);
		 ArrayList fields_arr = new ArrayList();
		 String filter_cond="";
		 int count=0;
		 int total_pages=0;
		 List<Map<String,Object>> query_data_obj=null;
		Gson gson = new Gson();	
		 
	      try{
//	    	  loggedinUserIntCode = request.getSession().getAttribute("s_loginId").toString();
	    	  
	    	  JSONObject qry_obj = new JSONObject();
	    	  JSONObject s_field_obj = new JSONObject();
	    	  qry_obj= GetGridQuery(table_name,"include_in_grid");
	    	  
	    	  fields_arr=(ArrayList) qry_obj.get("fields_arr");
	    	 
	    	  
	    	  String full_qry=(String) qry_obj.get("full_qry");
	    	  String cnt_qry=(String) qry_obj.get("cnt_qry");
	    	  s_field_obj=(JSONObject) qry_obj.get("s_field_obj");
	    	  String sort_detail=" ORDER BY "+table_name+".internal_code "+sord;
	    	  if(!sidx.equals("id") && s_field_obj.containsKey(sidx))
	    	  {
	    		  sort_detail=" ORDER BY "+s_field_obj.get(sidx) +" "+ sord;
	    	  }
	    	  if(search.equals("true"))
	    	  {
	    		  filter_cond=GetGridFilterConditions(request,s_field_obj);
	    	  }
	    	  
	    	  String custom_cond = " ";
	    	  if(table_name.equals("crm_booking") || table_name.equals("crm_party_master") || table_name.equals("crm_prospect"))
	    	  {
	    		  custom_cond += " AND "+table_name+".created_by = '"+request.getParameter("loggedinUserIntCode")+"' ";
	    	  }
	    	  if(table_name.equals("crm_prospect"))
	    	  {
	    		  custom_cond += "AND  crm_prospect.internal_code IN ( "
  								+"SELECT crm_prospect.internal_code FROM crm.crm_prospect "
	  							+" INNER JOIN crm.crm_employee_master ON crm_employee_master.internal_code = crm_prospect.created_by "
	  							+" WHERE crm_employee_master.emp_company = (SELECT crm_employee_master.emp_company FROM crm.crm_employee_master "
	  							+" WHERE crm_employee_master.internal_code =  '"+request.getParameter("loggedinUserIntCode")+"') )   ";
	    	  }
	    	  
	    	  cnt_qry+=" where 1=1 "+custom_cond+filter_cond;
	    	    count=GetCountFromQry(cnt_qry);
	    	    total_pages=0;
	    	  
	    	// calculation of total pages for the query
	    	  if( count >0 )
	    	  {
	    	  total_pages = (int) Math.ceil((double)count/limit);
	    	  }
	    	  
	    	  	if(total_pages==0) total_pages=1;
	     
	    	  if (page > total_pages)
	    	  page=total_pages;
	    	  int start = limit*page - limit;
	    	  if(start <0)
	    	  start = 0;
	    	 int max_limit =limit * page;
	    	  String e_qry= " SELECT  "+full_qry;
	    	   	  	e_qry+= " where 1=1 "+custom_cond+filter_cond+" "+sort_detail+" LIMIT "+start+","+limit;;
	    	   	  	
	    	   	 System.out.println("*******Final s Qry==>"+e_qry);
	    	   	query_data_obj=jdbcTemplate.queryForList(e_qry);
	    					
	    	   	System.out.println("query_data_obj-"+query_data_obj);		 
	    					
	    					
	    					
			   }
	      		catch (Exception ex) {
	      			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
	      JSONObject grid_data_obj = new JSONObject();    
	      grid_data_obj.put("page", page);
	      grid_data_obj.put("total", total_pages);
	      grid_data_obj.put("records", count);
	      grid_data_obj.put("rows", query_data_obj);
	      
	
		 
	      return gson.toJson(grid_data_obj);
	 
	}
 
	@SuppressWarnings("unchecked")
	@RequestMapping("viewUserSchemaSrefDataDetails")
	public @ResponseBody String viewUserSchemaSrefDataDetails(HttpServletRequest request ,
			
//			@RequestParam("rows") int limit,@RequestParam("page") int page,
//			@RequestParam("sidx")  String sidx,@RequestParam("sord") String sord,
//			@RequestParam("_search")  String search,@RequestParam("pop_up_table_name") String table_name,
//			@RequestParam("calling_table_name") String calling_table_name,@RequestParam("clicked_id_system_name") String clicked_id_system_name,
			@RequestParam Map<String, String> param_map

			
			
			) throws SQLException, ClassNotFoundException, ParseException
	{
		
		String rows_text=request.getParameter("rows");
		int limit=Integer.parseInt(rows_text);
		String page_text=request.getParameter("page");
		int page=Integer.parseInt(page_text);
		
		String sidx=request.getParameter("sidx");
	 
		
		String sord=request.getParameter("sord");
		String search=request.getParameter("_search");
		String table_name=request.getParameter("pop_up_table_name");
		String calling_table_name=request.getParameter("calling_table_name");
		String clicked_id_system_name=request.getParameter("clicked_id_system_name");
		
		System.out.println("--param_map-------"+param_map);
		
		
		 loginId=Format.GetSessonUserId(request);
		 ArrayList fields_arr = new ArrayList();
		 String filter_cond="";
		 int count=0;
		 int total_pages=0;
		 List<Map<String,Object>> query_data_obj=null;
		Gson gson = new Gson();	
		
	      try{
	         
	    	  JSONObject qry_obj = new JSONObject();
	    	  JSONObject s_field_obj = new JSONObject();
	    	  qry_obj= GetGridQuery(  table_name,"include_in_popup");
	    	  
	    	  fields_arr=(ArrayList) qry_obj.get("fields_arr");
	    	   
	    	  
	    	  
	    	  String full_qry=(String) qry_obj.get("full_qry");
	    	  String cnt_qry=(String) qry_obj.get("cnt_qry");
	    	  s_field_obj=(JSONObject) qry_obj.get("s_field_obj");
	    	  String sort_detail=" ORDER BY "+table_name+".internal_code "+sord;
	    	  
//	    	  System.out.println("t1");
	    	  String new_filter_cond="";
	    	  String pop_up_cond="";
	    	  
	    	 
	    	
//	    	  if(param_map.containsKey("all_data_obj"))
//	    	  {
//	    		  System.out.println("t2");
	    		  String all_data_obj_str=param_map.get("all_data_obj");
	    		  if(calling_table_name.equals("crm_employee_master") || calling_table_name.equals("crm_property_master") || 
	    				  calling_table_name.equals("crm_party_master")  || calling_table_name.equals("crm_prospect") || calling_table_name.equals("crm_booking"))
	    		  {
					 System.out.println("--[object Object]-------");
					 pop_up_cond=FWValidation.GetPopUpFilerData(request, calling_table_name, table_name, all_data_obj_str,clicked_id_system_name);	
	    		  }	
	    		  

				
	    	  	
	    	  
	    	  
	    	  if(!sidx.equals("id") && s_field_obj.containsKey(sidx))
	    	  {
	    		  sort_detail=" ORDER BY "+s_field_obj.get(sidx) +" "+ sord;
	    	  }
	    	  if(search.equals("true"))
	    	  {
	    		  filter_cond=GetGridFilterConditions(request,s_field_obj);
	    	  }
	    	  cnt_qry+=" where 1=1 "+filter_cond+new_filter_cond+pop_up_cond;
	    	    count=GetCountFromQry(cnt_qry);
	    	    total_pages=0;
	    	  
	    	// calculation of total pages for the query
	    	  if( count >0 )
	    	  {
	    	  
	    	  total_pages = (int) Math.ceil((double)count/limit);
	    	  }
	    	  	if(total_pages==0) total_pages=1;
	     
	    	  if (page > total_pages)
	    	  page=total_pages;
	    	  int start = limit*page - limit;
	    	  if(start <0)
	    	  start = 0;
	    	   
	    	  
	    	  int max_limit =limit * page;
	    	  String e_qry="   SELECT  "+full_qry;
    	   	  	e_qry+= " where 1=1 "+filter_cond+new_filter_cond+pop_up_cond+" "+sort_detail+" LIMIT  "+start+", "+limit ;
    	   	  	
    	   	  	System.out.println("e_qry....."+e_qry);
	    	  

    	   	 query_data_obj=jdbcTemplate.queryForList(e_qry);
			   }
	      		catch (Exception ex) {
	      			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
	      JSONObject grid_data_obj = new JSONObject();    
	      grid_data_obj.put("page", page);
	      grid_data_obj.put("total", total_pages);
	      grid_data_obj.put("records", count);
	      grid_data_obj.put("rows", query_data_obj);
	      
		 
		
	      return gson.toJson(grid_data_obj);
 
	}
	String GetGridFilterConditions(HttpServletRequest request, JSONObject field_arr)
	{
	 
		 String filter_cond="";
		 Enumeration<String> parameterNames = request.getParameterNames();
		 
	        while (parameterNames.hasMoreElements()) {
	            String paramName = parameterNames.nextElement();
	          
	            String[] paramValues = request.getParameterValues(paramName);
	            for (int i = 0; i < paramValues.length; i++) {
	                String paramValue = paramValues[i];
	                 
	                if(field_arr.containsKey(paramName))
	 			   {
	 				   filter_cond+=" and "+field_arr.get(paramName)+" like '"+paramValue+"%'";
	 			   }
	                

	            }
	        }
		  
			 
		  logger.info("Grid Filter cond=="+filter_cond);
		  return filter_cond;
	}
	int GetCountFromQry(String cnt_qry)
	{
		String cnt="0";
		System.out.println(cnt_qry);
			try
			{
				
				List<Map<String,Object>> query_data_obj =jdbcTemplate.queryForList(cnt_qry);
				cnt=query_data_obj.get(0).get("cnt").toString();
				 
			}catch (Exception ex) 
			{
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				if (cause instanceof SQLException) {}
			}
			
			return Integer.parseInt(cnt);
			
	}
	 
	JSONObject GetGridQuery(String table_name,String type)
	{
		String type_cond=" ";
		if(!type.isEmpty())
		type_cond=" and  su."+type+"=1 ";
		String join_qry= GetRefModuleJoinQuery(table_name,type);
		System.out.println(" grid join qry-->"+join_qry);;
		JSONObject qry_obj = new JSONObject();
		JSONObject s_field_obj = new JSONObject();
		 ArrayList fields_arr = new ArrayList();
		 qry_obj.put("full_qry","");
		qry_obj.put("cnt_qry","");
		qry_obj.put("fields_arr",fields_arr);
		qry_obj.put("s_field_obj","");
		
		String qry="SELECT CASE WHEN data_type='Reference Data' THEN CONCAT('ifnull(concat(',f1,f2,'),'' '') as ',field_name)"
				+ "WHEN data_type='Date' THEN CONCAT('date_format(',table_name,'.',field_name, ',''%d/%m/%Y'') as ', field_name)"
				+ "WHEN data_type='Number' THEN CONCAT('ifnull(',table_name,'.',field_name,',''0'') as ', field_name) "
				+ "ELSE CONCAT('ifnull(',table_name,'.',field_name,','' '') as ', field_name) "
				+ "END AS field_name,field_name AS u_field_name,  CASE WHEN data_type='Reference Data'"
				+ "THEN CONCAT('ifnull(concat(',f1,f2,'),'' '') ') WHEN data_type='Date'  "
				+ "THEN CONCAT('date_format(',table_name,'.',field_name, ',''%d/%m/%Y'')')"
				+ "WHEN data_type='Number'  THEN CONCAT('ifnull(',table_name,'.',field_name,',''0'') ') ELSE CONCAT('ifnull(',table_name,'.',field_name,','' '') ')"
				+ "END AS s_field_name FROM (	"
				+ "SELECT  su.sequence su_seq,s.internal_code, su.internal_code AS ele_id, "
				+ "ref_module_code, s.file_name AS table_name ,s.display_name,  field_id,   CASE WHEN IFNULL(field_id1,' ')<>' '"
				+ "THEN CONCAT(table_name,'_',su.internal_code,'.',field_id1) ELSE '' END AS f1,   CASE WHEN IFNULL(field_id2,' ')<>' '"
				+ "THEN CONCAT(','' - '',',table_name,'_',su.internal_code,'.',field_id2) ELSE '' END  AS f2,  IFNULL(is_multiple_group,0) AS multiple ,"
				+ "system_name AS field_name , data_type "
				+ " from  fbk_ia_menu_options s inner join fbk_ia_s_user_schema_elements su on  su.schema_code=s.internal_code  "
				+ " left join  fbk_ia_module_meta_columns mmc on mmc.module_id=su.ref_module_code ";
		qry+=" where s.file_name='"+table_name+"' "+type_cond;  
		qry+=" and su.is_multiple_group=0 ";
		qry+=" ) l  ";
		qry+=" where   multiple=0  order by su_seq ";

		 System.out.println("*******Display Field s Qry==>"+qry);
	    // System.out.println(qry);
	    	 int field_records_found=0;
	    	 String field_name="";
			try
			{
				
				
				List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(qry);
			 if(query_data_obj.size()>0)
				 field_records_found=1;
		   			 
		    		  for (Map<String, Object> map : query_data_obj) {
		    			    String t_field_name=map.get("field_name").toString();
		    			    field_name+=t_field_name +",";
		    			    
		    			    fields_arr.add(map.get("u_field_name").toString());
		    			    s_field_obj.put(map.get("u_field_name").toString(),map.get("s_field_name").toString());
		    		  }
		    		  field_name=field_name.substring(0,field_name.length()-1);
		    		  
				
			}catch (Exception ex) 
			{
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				if (cause instanceof SQLException) {}
			}
			
			if(field_records_found==1)
			{
				 		
					String full_qry =  table_name +".internal_code as id,"+field_name; 
					full_qry+="  from "+schema+".";
					full_qry+= table_name + join_qry;
					

					String cnt_qry = " select count(1) as cnt ";
					cnt_qry+="  from "+schema+".";
						cnt_qry+= table_name  ;
						 
 
					qry_obj.put("full_qry",full_qry);
					qry_obj.put("cnt_qry",cnt_qry);
					qry_obj.put("fields_arr",fields_arr);
					qry_obj.put("s_field_obj",s_field_obj);
 
		 		 
			}
			
		return qry_obj;
	}
	 
	String GetRefModuleJoinQuery(String table_name,String type)
	{
		String type_cond=" ";
		if(!type.isEmpty())
		type_cond=" and  sus."+type+"=1 ";
		
		String join_qry=" ";
		String qry=" select concat(' left join ','"+schema+".',s.file_name, '   ',s.file_name,'_',sus.internal_code,' on ',s.file_name,'_',sus.internal_code,'.internal_code =',sch.file_name,'.', system_name)  as join_table_name  ";
		qry+="	from "+schema+".fbk_ia_s_user_schema_elements  sus";
		qry+=" inner join "+schema+".fbk_ia_menu_options sch on sus.schema_code=sch.internal_code";
		qry+=" inner join "+schema+".fbk_ia_menu_options s on sus.ref_module_code=s.internal_code ";
		
		qry+=" where sch.file_name='"+table_name+"' "+type_cond;
		qry+=" and is_multiple_group=0 and ref_module_code>0 ";
	  
		//logger.info("*******GetRefModuleJoinQuery s Qry==>"+qry);
	  System.out.println(" GetRefModuleJoinQuery "+qry);
	    	 
			try
			{
				List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(qry);
		 
	    		  for (Map<String, Object> map : query_data_obj) {
	    			  String join_table_name=map.get("join_table_name").toString();
	    			  join_qry+=join_table_name +" ";
	    		  }
		    		  
				
			}catch (Exception ex) 
			{
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				if (cause instanceof SQLException) {}
			}
			        
		return join_qry;
	}
	
	@RequestMapping("deleteUserSchemaModuleDetails")
	public @ResponseBody String deleteUserSchemaModuleDetails(HttpServletRequest request 	) throws SQLException, ClassNotFoundException
	{
		String id=request.getParameter("id");
		String table_name=request.getParameter("table_name");
		Gson gson = new Gson();	
		JSONObject qry_obj = new JSONObject();
		 try
			{
				
				String del_qry="delete from "+schema+"."+table_name +" where internal_code="+id;
				jdbcTemplate.update(del_qry);	
				qry_obj.put("msg", "deleted Successfully");
				qry_obj.put("internal_code", id);
				 

			}
			catch (Exception ex) {
				Throwable cause = ex.getCause();
				qry_obj.put("msg", ex.getCause().toString());
				qry_obj.put("internal_code", id);
				
				if (cause instanceof SQLException) { }
				}
		
		 
		
		 return gson.toJson(qry_obj);
	 
	}
	
}
