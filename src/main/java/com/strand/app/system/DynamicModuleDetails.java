package com.strand.app.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.strand.app.config.Format;
import com.strand.app.config.ResponseDataGeneric;


@RestController
@RequestMapping("titpl/DynamicModuleDetails")
public class DynamicModuleDetails {
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
 
		static Logger logger = Logger.getLogger(DynamicModuleDetails.class);  
		String final_insert_qry="";
		String final_update_qry="";
		String login_user_int_code="0";
		String internal_code="0";
		String system_module_table_name="";
		String save_type="New";
	
		JSONObject group_details_arr= new JSONObject();
		
		@RequestMapping("AddUserSchemaGeneralDetailsNew")
	public @ResponseBody String AddUserSchemaGeneralDetailsNew(HttpServletRequest request) throws ParseException, IOException
	{
		 
		 BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF8"));
	        String json = "";
	        if(br != null){
	            json = br.readLine();
	        }
			System.out.println("json......."+json);

	        
		 ObjectMapper mapper = new ObjectMapper();
		 DynamicMasterJsonData dyn_json_data = mapper.readValue(json,DynamicMasterJsonData.class);
		 System.out.println("--------------dyn_json_data.save_schema_details_arr"+dyn_json_data.save_schema_details_arr);
		 final_insert_qry="";
			group_details_arr = null;
			//login_user_int_code="1";
			JSONObject return_obj = new JSONObject();
			
			  system_module_table_name=dyn_json_data.system_module_table_name;
			  String FW_login_Id=dyn_json_data.FW_login_Id;
			  internal_code=dyn_json_data.internal_code;		 
			  FormInsertUpdateCommands(dyn_json_data.save_schema_details_arr,dyn_json_data.header_data_type_arr);
			 
			  group_details_arr=dyn_json_data.group_details_arr;
			  System.out.println("group_details_arr--"+group_details_arr);
			  Gson gson = new Gson();	
			 
	
				try {
					login_user_int_code = request.getSession().getAttribute("s_loginId").toString();
					
				} catch (Exception e) {
					
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//---------To be removed
				
				System.out.println("login_user_int_code..."+login_user_int_code);
				if(login_user_int_code.trim().isEmpty())
				{
					result.SetError(-9,"failure","Session Expired..!!");
					return gson.toJson(result);
				}
				
				  if(login_user_int_code.equals("0")|| login_user_int_code.isEmpty())
				  {
					  login_user_int_code= Format.GetUserIntForEnlId(FW_login_Id);
					  System.out.println("login_user_int_code......."+login_user_int_code);
				  }
		 
				try
				{
					 if(internal_code.equals("0"))
					  {
						 save_type="New";
						  return_obj=InsertIntoHeaderTable(final_insert_qry);
					  }
					 else 
					  {
						 save_type="Edit";
						  return_obj=UpdateHeaderTable(final_update_qry);
						  HandleGroupDetails(internal_code);
					  }
					 
					 String last_id_to_str=return_obj.get("last_id").toString();
				//POST SAVE	 
			/*
			 * if(!last_id_to_str.equals("0")) {
			 * FWValidation.PostSaveModuleDetails(system_module_table_name, internal_code,
			 * last_id_to_str); }
			 */
				}
				catch (Exception ex) {
					ex.printStackTrace();
					Throwable cause = ex.getCause();
					if (cause instanceof SQLException) { }
				}
			
				String last_id=(String) return_obj.get("last_id");
				if(last_id.equals("0"))
					result.SetError(-1,"success",return_obj);
				else
				result.SetSuccess("success",return_obj);
			
				
				 return gson.toJson(result); 
			 
		 
	}
	
	public JSONObject InsertIntoHeaderTable(String t_final_insert_qry)
	{
		 JSONObject return_obj = new JSONObject();
		
		    
		 try
			{
				
				System.out.println("t_final_insert_qry------"+t_final_insert_qry);
				jdbcTemplate.update(t_final_insert_qry);
				
		
				String last_id_qry=" SELECT LAST_INSERT_ID() as lastId ";
				
				 List<Map<String,Object>>  query_data_obj=jdbcTemplate.queryForList(last_id_qry);
			 	
				 String lastId =  query_data_obj.get(0).get("lastId").toString();
			
			 
				//String lastId=GetLastInsertedId(session);
				 System.out.println("lastId-"+lastId);
				
				 
				if(lastId.equals("0"))
				{
					return_obj.put("msg", "Insertion Failed");
					return_obj.put("last_id", lastId);
					
				}
				else
				{
					HandleGroupDetails(lastId);
					return_obj.put("msg", "inserted successfully");
					return_obj.put("last_id", lastId);
				}

			}
			catch (Exception ex) {
				Throwable cause = ex.getCause();
				return_obj.put("msg", ex.getCause().toString());
				return_obj.put("last_id", "0");
				
				if (cause instanceof SQLException) { }
				} 
		  
		return return_obj;
	}
		
	
	ArrayList  GetExistingGroupIntCodeForGroupTable(String group_table_name)
	{
		ArrayList existing_group_int_codes = new ArrayList();
		String sql=" select internal_code from "+group_table_name+"  where ref_s_ref_data_code="+internal_code;
		logger.info(" get_existing_grp_int_codes_qry==>"+sql);
		try
		{
			List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(sql);
			
	    		  for (Map<String, Object> map : query_data_obj) {
	    			    String internal_code=map.get("internal_code").toString();
	    			    existing_group_int_codes.add(internal_code);
	    		  }
			
		}catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) {}
		}
		logger.info(" get_existing_grp_int_codes list==>"+existing_group_int_codes);
		return existing_group_int_codes;
	}
	void  HandleGroupDetails(String lastId)
	{
		String json_string=group_details_arr.toJSONString();
		JSONObject grp_json_data_obj= new JSONObject();
		 JSONParser parser = new JSONParser();
		  try {
			grp_json_data_obj=(JSONObject) parser.parse(json_string);
			
			
			System.out.println("grp_json_data_obj........"+grp_json_data_obj);
			if(grp_json_data_obj.size()>0)
			  {
				System.out.println("grp_json_data_obj--"+grp_json_data_obj);
					//gor each group_table_name
				  for (Object key : grp_json_data_obj.keySet()) 
				  {
						 String group_table_name = (String)key;
						 
						 JSONObject tmp_group_details_arr = new JSONObject();
						 tmp_group_details_arr=(JSONObject) grp_json_data_obj.get(group_table_name);
						 ArrayList existing_group_int_codes = new ArrayList();
						 ArrayList ui_group_int_codes = new ArrayList();
						 if(save_type.equals("Edit"))
						 {
							 existing_group_int_codes=GetExistingGroupIntCodeForGroupTable(group_table_name);
						 }
						 System.out.println("tmp_group_details_arr--"+tmp_group_details_arr);
						  //loop each row in group
						 for (Object row_id_key : tmp_group_details_arr.keySet()) {
							 String row_id = (String)row_id_key;
							 JSONObject group_row_obj = new JSONObject();
						 
							 group_row_obj = (JSONObject) tmp_group_details_arr.get(row_id);
							 System.out.println("group_row_obj--"+group_row_obj);
							 String group_int_code=group_row_obj.get("group_int_code").toString();
							 ui_group_int_codes.add(group_int_code);
							 
							 ForGroupInsertUpdateCommands(group_table_name,lastId,group_row_obj);
							 //System.out.println(keyvalue);
						 } 
						 if(save_type.equals("Edit") && existing_group_int_codes.size()>0)
						 DeleteMissingGroupRowDetails( group_table_name, existing_group_int_codes, ui_group_int_codes);
						 
					}
					        
			  }
			
			
			 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		
		   
		  
	}
	public void DeleteMissingGroupRowDetails(String group_table_name,ArrayList existing_group_code,ArrayList ui_group_code)
	{
		
		 Set<String> set1 = new HashSet<String>();
		    Set<String> set2 = new HashSet<String>();

		    set1.addAll(existing_group_code);
		    set2.addAll(ui_group_code);

		    set1.removeAll(set2);
		  
		    Iterator iter = set1.iterator();
		    String int_codes="";
		    while (iter.hasNext()) {
		    	int_codes+=iter.next()+",";
		    }
		    if(!int_codes.isEmpty())
		    {
		    	int_codes=int_codes.substring(0,int_codes.length()-1);
		    	 String db_qry="delete from "+group_table_name+" where ref_s_ref_data_code="+internal_code;
		    	 db_qry+=" and internal_code in("+int_codes+")";
				 logger.info("delete group qryyyyyyyyy->"+db_qry);
				
				 try
					{
					 jdbcTemplate.update(db_qry);	
				}
					catch (Exception ex) {
						ex.printStackTrace();
						Throwable cause = ex.getCause();
						logger.info(" ERROR WHILE In Delete Group Qry="+ex.getCause()+"==>"+db_qry);
						
						if (cause instanceof SQLException) { }
						}
				
		    }
		    
	}
	public void ForGroupInsertUpdateCommands(String group_table_name,String ref_s_ref_data_code,JSONObject group_row_obj)
	{
		 
	//this will be called for each group row
		String insert_cols="";
		String  insert_col_values="";
		String update_cond=""; 
		String group_int_code=group_row_obj.get("group_int_code").toString();
	 
	 System.out.println("group_int_code--"+group_int_code);
		 for (Object key : group_row_obj.keySet()) {
			 String keyStr = (String)key;
			 String keyvalue = java.net.URLDecoder.decode(group_row_obj.get(keyStr).toString());
			 keyvalue = keyvalue.replace("'","''");
			 
			 if(keyStr.equals("group_int_code")) continue;
			 insert_cols+=keyStr+",";
			 String cell_val="'"+keyvalue+"'";
			  
			 insert_col_values+=cell_val+",";		
			 update_cond+=keyStr+"= "+cell_val+",";
				
		        
		 }
		 insert_cols=insert_cols.substring(0,insert_cols.length()-1);
		 insert_col_values=insert_col_values.substring(0,insert_col_values.length()-1);
		 String db_qry="";
		 if(group_int_code.equals("0"))
		 {
			 db_qry="insert into "+schema+"."+group_table_name;
			 db_qry+="(   ref_s_ref_data_code,"+insert_cols+",created_by, ";
			 db_qry+="created_date,modified_by,modified_date)  values ";
			 db_qry+=" ("+ref_s_ref_data_code+","+insert_col_values+","+login_user_int_code+",CURRENT_TIMESTAMP,"+login_user_int_code+",CURRENT_TIMESTAMP)";	
			 System.out.println(" grp insert qry=="+db_qry);
			 
		 }
		 else
		 {
			 	update_cond+=" modified_by="+login_user_int_code+", modified_date=CURRENT_TIMESTAMP ";
			 	db_qry=" update "+schema+"."+group_table_name+" set "+update_cond+" where internal_code="+group_int_code;
			 	System.out.println(" grp update qry=="+db_qry);
		 
		 }
		 
		
		 try
			{
			 jdbcTemplate.update(db_qry);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				logger.info(" ERROR WHILE INSERT/UPDATE GROUP QRY="+ex.getCause()+"==>"+db_qry);
				
				if (cause instanceof SQLException) { }
				}
			 
			
	}
	public JSONObject UpdateHeaderTable(String t_final_update_qry)
	{
		 JSONObject return_obj = new JSONObject();
	
		logger.info("final_update_qry---"+final_update_qry);
		 
	        
		 try
			{
			 jdbcTemplate.update(t_final_update_qry);	

				 
					return_obj.put("msg", "Updated Successfully");
					return_obj.put("last_id", internal_code);
				 

			}
			catch (Exception ex) {
				Throwable cause = ex.getCause();
				return_obj.put("msg", ex.getCause().toString());
				return_obj.put("last_id", "0");
				
				if (cause instanceof SQLException) { }
				} 
		 
		 
		
		 
		return return_obj;
	}
	public void  FormInsertUpdateCommands(JSONObject save_schema_details_arr ,JSONObject header_data_type_arr )
	{
		String insert_cols="";
		String  insert_col_values="";
		String update_cond="";
		System.out.println("save_schema_details_arr......."+save_schema_details_arr);
		 logger.info("save_schema_details_arr==>"+save_schema_details_arr);
		 for (Object key : save_schema_details_arr.keySet()) {
			 String keyStr = (String)key;
			 String data_type=header_data_type_arr.get(keyStr).toString();
			 String keyvalue = java.net.URLDecoder.decode(save_schema_details_arr.get(keyStr).toString());
			 keyvalue = keyvalue.replace("'","''");
			 
			 insert_cols+=keyStr+",";
			 String cell_val="'"+keyvalue+"'";
			 if(data_type.equals("Date") && keyvalue.isEmpty())
				 cell_val="NULL"; 
			
			  
			 insert_col_values+=cell_val+",";		
			 update_cond+=keyStr+"= "+cell_val+",";
				
		        
		 }
		 insert_cols=insert_cols.substring(0,insert_cols.length()-1);
		 insert_col_values=insert_col_values.substring(0,insert_col_values.length()-1);
		 
		 String insert_qry="insert into "+schema+"."+system_module_table_name;
				 insert_qry+="(   "+insert_cols+",created_by, ";
				 insert_qry+="created_date,modified_by,modified_date)  values ";
				 insert_qry+=" ("+insert_col_values+","+login_user_int_code+",CURRENT_TIMESTAMP,"+login_user_int_code+",CURRENT_TIMESTAMP)";	
		 final_insert_qry=insert_qry;
		 
		 update_cond+=" modified_by="+login_user_int_code+", modified_date=CURRENT_TIMESTAMP ";
			final_update_qry=" update "+schema+"."+system_module_table_name+" set "+update_cond+" where internal_code="+internal_code;
			
			
					 

	}
	
	@RequestMapping(value="GetSchemaModuleDetailsForUpdateNew" ,produces = "application/json; charset=utf-8")
	public  @ResponseBody String  GetSchemaModuleDetailsForUpdateNew(HttpServletRequest request,HttpServletResponse response)  throws ParseException, UnsupportedEncodingException
	 {
		 
		String internal_code=request.getParameter("internal_code");
		String table_name=request.getParameter("table_name");
	 
		 
			JSONObject return_obj = new JSONObject();
			return_obj.put("internal_code",internal_code);
			return_obj.put("details",""); 
			
				try
				{
			
			        
						JSONObject qry_obj = new JSONObject();
					  
						qry_obj=GetEditQuery(table_name,internal_code);
						JSONObject fields_arr = new JSONObject();
						String full_qry=(String) qry_obj.get("full_qry");
						fields_arr= (JSONObject) qry_obj.get("fields_arr");
						List<Map<String,Object>> query_data_obj=null;
						
						query_data_obj=GetDetailsRowDataSet(full_qry,fields_arr); 
												
						return_obj.put("details",query_data_obj); 
						JSONObject grp_details = new JSONObject();
						grp_details=GetGroupDetailsForUpdate(table_name,internal_code);
						return_obj.put("grp_details",grp_details); 
						
						request.setCharacterEncoding("UTF8"); 
				        response.setCharacterEncoding("UTF8");
				}
				catch( Exception ex) {
					ex.printStackTrace();
					Throwable cause = ex.getCause();
					if (cause instanceof SQLException) { }
				}
				ResponseDataGeneric result=new ResponseDataGeneric();
				result.SetSuccess("success",return_obj);
				Gson gson = new Gson();	
				
		        
				 return gson.toJson(result); 
			 
	 }
	
	List<Map<String,Object>> GetDetailsRowDataSet(String full_qry,JSONObject fields_arr)
	{
		List<Map<String,Object>> query_data_obj=null;
		  try{
		         
	    	  System.out.println(" Edit Qry =>"+full_qry);
	    
	    		query_data_obj=jdbcTemplate.queryForList(full_qry);
			   }
	      		catch (Exception ex) {
	      			ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) { }
			 }
		return query_data_obj;
		
	}
	JSONObject GetEditQuery(String table_name,String internal_code)
	{
		String join_qry= GetRefModuleJoinQueryForEdit(table_name);
 
		JSONObject qry_obj = new JSONObject();
		JSONObject s_field_obj = new JSONObject();
		JSONObject fields_arr = new JSONObject();
		 qry_obj.put("full_qry","");
		 qry_obj.put("fields_arr",fields_arr);
		 
			String qry=" SELECT 	data_type,	CASE WHEN data_type='Reference Data' THEN  	 CONCAT('ifnull(concat(',f1,f2,'),'' '') as txt_ref_schema_',field_name,',ifnull(',table_name,'.',field_name,',0) as ', field_name)  "
					+ "WHEN data_type='Date' THEN CONCAT('date_format(',table_name,'.',field_name, ',''%d/%m/%Y'') as ', field_name) "
					+ "WHEN data_type='Number' THEN CONCAT('ifnull(',table_name,'.',field_name,',''0'') as ', field_name) "
					+ "ELSE CONCAT('ifnull(',table_name,'.',field_name,','' '') as ', field_name ) "
					+ "END AS field_name,field_name AS u_field_name   FROM   (	"
					+ "SELECT  su.sequence su_seq,s.internal_code, su.internal_code AS ele_id, "
					+ "ref_module_code, s.file_name AS table_name ,s.display_name,  field_id, "
					+ "CASE WHEN IFNULL(field_id1,' ')<>' ' THEN CONCAT(table_name,'_',su.internal_code,'.',field_id1) ELSE '' END AS f1,"
					+ "CASE WHEN IFNULL(field_id2,' ')<>' ' THEN CONCAT(','' - '',',table_name,'_',su.internal_code,'.',field_id2) ELSE '' END  AS f2,"
					+ "IFNULL(is_multiple_group,0) AS multiple ,  system_name AS field_name , data_type"
					+ " from "+schema+".fbk_ia_menu_options s inner join "+schema+".fbk_ia_s_user_schema_elements su on  su.schema_code=s.internal_code  "
					+ " left join "+schema+".fbk_ia_module_meta_columns mmc on mmc.module_id=su.ref_module_code ";
			qry+=" where s.file_name='"+table_name+"' "; 
			qry+=" and su.is_multiple_group=0 ";
			qry+=" ) l  ";
			qry+=" where   multiple=0  order by su_seq ";
			
			
	 

		 logger.info("*******Edit Display Field s Qry==>"+qry);
	    System.out.println("Edit Display Field s Qry "+qry);
	    	
	    	 String field_name="";
			try
			{
				
				List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(qry);
				
		   			 
		    		  for (Map<String, Object> map : query_data_obj) {
		    			    String t_field_name=map.get("field_name").toString();
		    			    field_name+=t_field_name +",";
		    			    fields_arr.put(map.get("u_field_name").toString(),map.get("data_type").toString());
		    			   
		    		  }
		    		  field_name=field_name.substring(0,field_name.length()-1);
		    		  
				
			}catch (Exception ex) 
			{
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				if (cause instanceof SQLException) {}
			}
			
			 		
					String full_qry =  "select "+table_name +".internal_code as id,"+field_name; 
					full_qry+=" from "+schema+".";
					full_qry+= table_name + join_qry;
					full_qry+="	where "+table_name+".internal_code="+internal_code;
					logger.info("FInal Edit Qry=="+full_qry);
				 
						 
 
					qry_obj.put("full_qry",full_qry);
					qry_obj.put("fields_arr",fields_arr);

		 		 
		 
			
		return qry_obj;
	}
	 
	String GetRefModuleJoinQueryForEdit(String table_name)
	{
		String join_qry=" ";
 
	  
		String qry=" select CONCAT(' left join ', '"+schema+".',s.file_name, '   ',s.file_name,'_',sus.internal_code,' on ',s.file_name,'_',sus.internal_code,'.internal_code =',sch.file_name,'.', system_name)  as join_table_name  ";
		qry+="	from "+schema+".fbk_ia_s_user_schema_elements  sus";
		qry+=" inner join "+schema+".fbk_ia_menu_options sch on sus.schema_code=sch.internal_code";
		qry+=" inner join "+schema+".fbk_ia_menu_options s on sus.ref_module_code=s.internal_code ";
		
		qry+=" where sch.file_name='"+table_name+"' ";
		qry+=" and is_multiple_group=0 and ref_module_code>0 ";
		
		System.out.println("*******GetRefModuleJoinQuery s Qry==>"+qry);
	     //System.out.println(Sql);
	    	 
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
	//Group Details for Update
	JSONObject GetGroupDetailsForUpdate(String table_name,String header_internal_code)
	{
		JSONObject group_details = new JSONObject();
		 
		try{
	        
	   
	         String Sql = "select name as group_name,sug.internal_code, "
	         	   		+ " CONCAT(s.file_name,'_',system_group_table_name,'_group') as table_name  "
	         		 + " from fbk_ia_s_user_schema_group sug inner join  fbk_ia_menu_options s on s.internal_code=sug.schema_code"
	         		+ " where multiple=1 and  s.file_name='"+table_name+"'";
		  
	     	System.out.println("********* GetGroupDetailsForUpdate -------"+Sql);
	    		
		    		  List<Map<String,Object>> query_data_obj=	jdbcTemplate.queryForList(Sql);
		    		  for (Map<String, Object> map : query_data_obj)
			  		    {
			    			   	 
		    			  String group_internal_code= map.get("internal_code").toString().trim();
		    		 
		    			  String group_table_name= map.get("table_name").toString().trim();
		    			  
		    			  List<Map<String,Object>> grp_query_data_obj=null;
		    			  grp_query_data_obj=GetGroupTableDataForEachGroup(table_name,header_internal_code,group_table_name,group_internal_code);
		    			  
		    			  group_details.put(group_table_name,grp_query_data_obj);
		    			  
		    			  
			  		    }
	    		
	    					  
			   }catch (Exception ex) {
				   ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) {
			        	 ex.printStackTrace();
			        }
			   } 
		
		return group_details;
	}
	List<Map<String,Object>> GetGroupTableDataForEachGroup(String table_name,String header_internal_code,String  group_table_name,
			String  group_internal_code)
	{
		  List<Map<String,Object>> grp_query_data_obj=null;
		
		  
		  JSONObject qry_obj = new JSONObject();
		  
			qry_obj=  GetEditQueryForEachGroup(group_table_name,header_internal_code,group_internal_code);
			JSONObject fields_arr = new JSONObject();
			String full_qry=(String) qry_obj.get("full_qry");
			fields_arr= (JSONObject) qry_obj.get("fields_arr");
			  try{
		    
				  grp_query_data_obj=jdbcTemplate.queryForList(full_qry);
					
				   }
		      		catch (Exception ex) {
		      			ex.printStackTrace();
				        Throwable cause = ex.getCause();
				        if (cause instanceof SQLException) { }
				 }
			 
		  return grp_query_data_obj;
	}
	JSONObject GetEditQueryForEachGroup(String group_table_name,String header_internal_code,String group_internal_code)
	{
		 
	 
		 String join_qry=GetRefModuleJoinQueryForGroupId(group_internal_code);
		JSONObject qry_obj = new JSONObject();
		JSONObject s_field_obj = new JSONObject();
		JSONObject fields_arr = new JSONObject();
		 
		String qry="select data_type,";
		qry+="CASE WHEN data_type='Reference Data' THEN CONCAT('ifnull(concat(',f1,f2 ,'),'''') as txt_ref_schema_',field_name,',ifnull(',table_name,'.',field_name,',0) as ',field_name)";
		qry+="WHEN data_type='Number' THEN CONCAT('ifnull(',table_name,'.',field_name,',''0'') as ', field_name)";
		qry+="ELSE CONCAT('ifnull(',table_name,'.',field_name,','''') as ', field_name)  ";
		qry+="END AS field_name,field_name AS u_field_name   FROM ";
		 
		qry+="(SELECT  su.sequence su_seq,s.internal_code, su.internal_code AS ele_id,  ref_module_code,CONCAT( s.file_name,'_',g.system_group_table_name,'_group') AS table_name ,";
		qry+=" s.display_name,  field_id, CASE WHEN IFNULL(field_id1,' ')<>' ' THEN CONCAT(table_name,'_',su.internal_code,'.',field_id1) ELSE '' END AS f1,";
		qry+=" CASE WHEN IFNULL(field_id2,' ')<>' ' THEN CONCAT(','' -'', ',table_name,'_',su.internal_code,'.',field_id2) ELSE '' END  AS f2,";
		
		 
		qry+=" IFNULL(is_multiple_group,0) AS multiple ,  system_name AS field_name , data_type ";
		qry+=" from  fbk_ia_menu_options s inner join fbk_ia_s_user_schema_elements su on ";
		qry+=" su.schema_code=s.internal_code ";
		qry+="  inner join  fbk_ia_s_user_schema_group g on g.internal_code=su.group_code ";
		qry+="  left join fbk_ia_module_meta_columns mmc on mmc.module_id=su.ref_module_code ";
		qry+=" where g.internal_code='"+group_internal_code+"'   ";
		qry+=" and su.is_multiple_group=1";
		qry+=" ) l  ";
		qry+=" where   multiple=1  order by su_seq ";

		 System.out.println("*******GRoup Edit Display Field s Qry==>"+qry);
	    
	    	 String field_name="";
			try
			{
					List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(qry); 
			 
		   			 
		    		  for (Map<String, Object> map : query_data_obj) {
		    			    String t_field_name=map.get("field_name").toString();
		    			    field_name+=t_field_name +",";
		    			    fields_arr.put(map.get("u_field_name").toString(),map.get("data_type").toString());
		    			   
		    		  }
		    		  field_name=field_name.substring(0,field_name.length()-1);
		    		  
				
			}catch (Exception ex) 
			{
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				if (cause instanceof SQLException) {}
			}
			
			 		
					String full_qry =  "select "+group_table_name +".internal_code as group_int_code, "+field_name; 
					full_qry+=" from "+schema+".";
					full_qry+= group_table_name + join_qry;
					full_qry+="	where "+schema+"."+group_table_name+".ref_s_ref_data_code="+header_internal_code+ " order by  "+group_table_name +".row_sequence" ;
					System.out.println("FInal Group Edit Qry=="+full_qry);
				 
						 
 
					qry_obj.put("full_qry",full_qry);
					qry_obj.put("fields_arr",fields_arr);

		 		 
		 
			
		return qry_obj;
	}
	
	String GetRefModuleJoinQueryForGroupId(String group_int_code)
	{
		String join_qry=" ";
		String qry="select concat(' left join "+schema+".',s.file_name, '  ',s.file_name,'_',sus.internal_code,' on ',s.file_name,'_',sus.internal_code,'.internal_code =', sch.file_name,'_',system_group_table_name,'_group' ,'.', system_name)  as join_table_name "; 
					qry+=" from "+schema+".fbk_ia_s_user_schema_elements  sus ";
					qry+=" inner join "+schema+".fbk_ia_s_user_schema_group g on g.internal_code=sus.group_code ";
					qry+=" inner join "+schema+".fbk_ia_menu_options sch on sus.schema_code=sch.internal_code "; 
					qry+=" inner join "+schema+".fbk_ia_menu_options s on sus.ref_module_code=s.internal_code ";
					qry+=" where g.internal_code="+group_int_code;
					qry+=" and is_multiple_group=1 and ref_module_code>0 ";
	  
					System.out.println("*******GetRefModuleJoinQueryForGroupId s Qry==>"+qry);
	     //System.out.println(Sql);
	    	 
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
}
