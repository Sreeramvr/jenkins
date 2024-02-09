package com.strand.app.system;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.strand.app.config.ResponseDataGeneric;
@RestController
@RequestMapping("titpl/metacolumns")

public class CreateModuleMetaColumns 
{

	static Logger logger = Logger.getLogger(CreateModuleMetaColumns.class);  
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	
	@RequestMapping("createModuleMetaColumns")
	public @ResponseBody String createModuleMetaColumns(HttpServletRequest request) throws IOException
	{

		try
		{
			List<Map<String,Object>> schema_id_list = null;
			schema_id_list=get_schema_id(request);
			
			List<Map<String,Object>> display_cols_list = null;
			List<Map<String,Object>> get_field_names_list=null;
			
			for(Map<String,Object> schema_id_map : schema_id_list)
			{
				String schema_id =  schema_id_map.get("schema_id").toString();
				
				display_cols_list = get_display_cols_WK( schema_id); //get_display_cols_With Key
				
				if(display_cols_list==null || display_cols_list.isEmpty())
				{
					display_cols_list = get_display_cols_WOK(schema_id); //get_display_cols_With Out Key
				}
				if(display_cols_list==null || display_cols_list.isEmpty()) continue;
				
				String module_id = "";
				String module_name = "";
				String table_name = "";
				String field_name = "";
				String field_id = "";
				
				if(display_cols_list.size()==1)
				{
					module_id= (String) display_cols_list.get(0).get("module_id").toString();
					module_name= (String) display_cols_list.get(0).get("module_name");
					table_name= (String) display_cols_list.get(0).get("table_name");
					field_name= (String) display_cols_list.get(0).get("field_name");
					field_id= (String) display_cols_list.get(0).get("field_id");
				}
				else
				{
				
					module_id= (String) display_cols_list.get(0).get("module_id").toString();
					module_name= (String) display_cols_list.get(0).get("module_name");
					table_name= (String) display_cols_list.get(0).get("table_name");
					field_name= (String) display_cols_list.get(0).get("field_name")+"-ele-"+display_cols_list.get(1).get("field_name");
					field_id= " concat( "+(String) display_cols_list.get(0).get("field_id")+",''-'',"+display_cols_list.get(1).get("field_id")+")";
	
				}
				System.out.println(module_id+"\t"+module_name+"\t"+table_name+"\t"+field_name+"\t"+field_id);
				
				deleteMetaColumns(module_id);
				insertIntoMetaColumn(module_id, module_name, table_name, field_name, field_id);
				
				String extra_cond="";
				extra_cond=" and is_key_field=1 ";
				
				get_field_names_list = get_field_names( schema_id, extra_cond);
				if(get_field_names_list==null ||get_field_names_list.isEmpty())
				{
					extra_cond=" "; 
					get_field_names_list = get_field_names(schema_id,extra_cond);
				}
				if(get_field_names_list==null ||get_field_names_list.isEmpty()) continue;
				
				String field_name1 = "";
				String field_name2 = "";
				String field_id1 = "";
				String field_id2 = "";
				
				if(get_field_names_list.size()==1)
				{
					field_name1 = (String) get_field_names_list.get(0).get("field");
					field_id1 = (String) get_field_names_list.get(0).get("field_name");
				}
				else
				{
					field_name1 = (String) get_field_names_list.get(0).get("field");
					field_name2 = (String) get_field_names_list.get(1).get("field");
					field_id1 = (String) get_field_names_list.get(0).get("field_name");
					field_id2 = (String) get_field_names_list.get(1).get("field_name");
				}
				updateMetaColumns(field_name1, field_name2, field_id1, field_id2, schema_id);
								
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException){}
		}
		
		
		Gson gson = new Gson();	
		result.SetError(0,"success","Done");
		return gson.toJson(result);
	}
	public void updateMetaColumns(String field_name1, String field_name2, String field_id1, String field_id2, String schema_id) 
	{
		 
		 
		String update_qry = "update "+schema+".fbk_ia_module_meta_columns set field_name1='"+field_name1+"', field_name2='"+field_name2+"', field_id1='"+field_id1+"', field_id2='"+field_id2+"' "
				+ "where module_id="+schema_id; 
		
		System.out.println("Update into Meta columns------"+update_qry);
		
		try
		{				
			jdbcTemplate.update(update_qry);

		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			System.out.println(" ERROR WHILE Updating MetaColumns QRY="+ex.getCause()+"==>"+update_qry);
				
			if (cause instanceof SQLException) { }
		}
		 
		
		
	}
	public List<Map<String, Object>> get_field_names(String schema_id,String extra_cond) 
	{
		List<Map<String,Object>> data_list=null;
		try
		{
			String qry = "select   schema_code  as schema_id, module_name, name as field, system_name as field_name "
					+ "from "+schema+".fbk_ia_s_user_schema_elements  s "
					+ "inner join "+schema+".fbk_ia_menu_options su on su.internal_code=schema_code "
					+ "where  schema_code="+schema_id+""+extra_cond+"  "
					+ "order by  ifnull(s.sequence,0), su.internal_code desc limit 2";
			System.out.println("get_field_names------"+qry);
		
		   data_list=jdbcTemplate.queryForList(qry);
			
		}
	
		catch(Exception ex)
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException){}
		}
		
		return data_list;
	}

	public void insertIntoMetaColumn(String module_id, String module_name, String table_name, String field_name, String field_id)
	{	 
		String insert_qry = "insert into "+schema+".fbk_ia_module_meta_columns ("
				+ "module_id, "
				+ "module_name, "
				+ "table_name, "
				+ "field_name, "
				+ "field_id) values "
				+ "("+module_id+",'"+module_name+"','"+table_name+"','"+field_name+"','"+field_id+"')"; 
		
		System.out.println("insert into Meta columns ---------"+insert_qry);
		
		try
		{				
			jdbcTemplate.update(insert_qry);	

		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			System.out.println(" ERROR WHILE Inserting MetaColumns QRY="+ex.getCause()+"==>"+insert_qry);
				
			if (cause instanceof SQLException) { }
		}
		 
	}
	public void deleteMetaColumns(String module_id)
	{
	 
		 
		String delete_qry = "delete from "+schema+".fbk_ia_module_meta_columns where module_id="+module_id; 
		
		System.out.println("delete--qry------"+delete_qry);
		
		try
		{
			jdbcTemplate.update(delete_qry);	

		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			System.out.println(" ERROR WHILE deleting MetaColumns QRY="+ex.getCause()+"==>"+delete_qry);
			
			if (cause instanceof SQLException) { }
		}
		System.out.println("Done--qry------"+delete_qry);
		
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> get_schema_id( HttpServletRequest request) 
	{
		List<Map<String,Object>> query_data_obj= null;
		try
		{
			String sql="select  distinct  internal_code as schema_id from "+schema+".fbk_ia_menu_options  WHERE load_type='FrameWork' ";
			System.out.println("getModuleMetaColumnsDataList------"+sql);
			
			query_data_obj=jdbcTemplate.queryForList(sql);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException){}
		}
		
		return query_data_obj;
	}
	
	// get_display_cols_WK==> get_display_cols_With Key
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Map<String, Object>> get_display_cols_WK( String schema_id) 
	{
		List<Map<String,Object>> query_data_obj= null;
		try
		{
			String sql="select  schema_code  as module_id, module_name, name as field_name, system_name as field_id, "
					+ "su.file_name as table_name, ifnull(s.sequence,0) as s, su.internal_code  as b "
					+ "from "+schema+".fbk_ia_s_user_schema_elements  s "
					+ "inner join "+schema+".fbk_ia_menu_options su on su.internal_code=schema_code "
					+ "where is_key_field=1 and schema_code="+schema_id+"    "
					+ "order by ifnull(s.sequence,0),su.internal_code limit 2";
			
			System.out.println("get_display_cols_WK------"+sql);
			
			query_data_obj=jdbcTemplate.queryForList(sql);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException){}
		}
		
		return query_data_obj;
	}
	
	// get_display_cols_WOK==> get_display_cols_With out Key
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Map<String, Object>> get_display_cols_WOK(String schema_id) 
	{
		List<Map<String,Object>> query_data_obj= null;
		try
		{
			String sql="select  schema_code  as module_id, module_name, name as field_name, system_name as field_id, "
					+ "su.file_name as table_name, ifnull(s.sequence,0) as s, su.internal_code  as b "
					+ "from "+schema+".fbk_ia_s_user_schema_elements  s "
					+ "inner join "+schema+".fbk_ia_menu_options su on su.internal_code=schema_code "
					+ "where schema_code="+schema_id+"   "
					+ "order by  ifnull(s.sequence,0),su.internal_code limit 2 ";
			
			System.out.println("get_display_cols_WOK------"+sql);
			
			query_data_obj=jdbcTemplate.queryForList(sql);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException){}
		}
		
		return query_data_obj;
	}
		

}
