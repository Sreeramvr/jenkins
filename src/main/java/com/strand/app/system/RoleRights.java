package com.strand.app.system;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.strand.app.config.Format;
import com.strand.app.config.ResponseDataGeneric;
@RestController
@RequestMapping("titpl/RoleRights")
public class RoleRights {
	@SuppressWarnings("unused")

	static Logger logger = Logger.getLogger(RoleRights.class);   
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	public RoleRights() {
		// TODO Auto-generated constructor stub
	}
	@RequestMapping("GetRoleNamesLov")
	public @ResponseBody String  GetRoleNamesLov() throws SQLException, ClassNotFoundException
	{
		 
		List<Map<String,Object>> query_data_obj=null;
		RoleRights obj=new RoleRights();

		try
		{
			String Sql = "select   role_name  as name,internal_code  as val	 from "+schema+".crm_user_roles  ";

			query_data_obj=jdbcTemplate.queryForList(Sql); 
		
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		}

		result.SetSuccess("success",query_data_obj);
		Gson gson = new Gson();	
		 return gson.toJson(result); 
 
	}
	
 @RequestMapping("GetModuleNamesforSelectedRoles")
	public  @ResponseBody String  GetModuleNamesforSelectedRoles(HttpServletRequest request)
	{

		 String app_type=request.getParameter("app_type");
		 String role_name=request.getParameter("role_name");
		 String menu_group=request.getParameter("menu_group");
		 
	 
		List<Map<String,Object>> query_data_obj=null;
	
		try
		{
			String menu_grp_cond="";
			if(!menu_group.isEmpty())
			{
				menu_grp_cond=" and menu_name='"+menu_group+"'" ;
			}
			String Sql = "select  mo.internal_code,menu_name,ifnull(sub_menu_name,'Others') as sub_menu_name,mo.display_name, ";
						Sql+= " ifnull(rr.ref_menu_code,0) as ref_menu_code  from  ";
						Sql+= schema+".fbk_ia_menu_groups mg inner join   ";
						Sql+= schema+".fbk_ia_menu_options mo on mg.internal_code=mo.menu_group and mg.app_type='"+app_type+"' ";
						Sql+= " left join "+schema+".fbk_ia_menu_sub_groups sg on sg.internal_code=menu_subgroup ";
						Sql+= " left join "+schema+".crm_role_rights rr on rr.ref_menu_code=mo.internal_code and rr.ref_role_code="+role_name;
						Sql+= " where 1=1 "+menu_grp_cond+" order by menu_name,sg.sequence,mo.display_name ";

						logger.info("role_rights Qry->"+Sql);
						System.out.println(Sql);
			
			query_data_obj=jdbcTemplate.queryForList(Sql); 
			


		}
		catch (Exception ex) {
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		}

		result.SetSuccess("success",query_data_obj);
		Gson gson = new Gson();	
		 return gson.toJson(result); 

	} 
 
 @RequestMapping("SaveRoleDetails")
	public  @ResponseBody String  SaveRoleDetails(HttpServletRequest request
			 )
	{
		 String checkedVals=request.getParameter("checkedVals");
		 String NoncheckedVals=request.getParameter("NoncheckedVals");
		 String role_name=request.getParameter("role_name");
		 

		try
		{
		
			if(!NoncheckedVals.isEmpty())
			{
			String[] str_arr=NoncheckedVals.split(",");
				String menu_ids="";
				for(int i=0;i<str_arr.length;i++)
				{
					String ref_menu_code=str_arr[i];
					menu_ids+=ref_menu_code+",";
				}
				menu_ids+="0";
				DeleteFromRoleRights(role_name,menu_ids);
			}
			
			if(!checkedVals.isEmpty())
			{
			String[] str_arr=checkedVals.split(",");
				for(int i=0;i<str_arr.length;i++)
				{
					String ref_menu_code=str_arr[i];
					DeleteFromRoleRights(role_name,ref_menu_code);
					InsertRoleRights(role_name,ref_menu_code);
				}
			}

		}
		catch (Exception ex) {
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		}

		result.SetSuccess("success","Mapped Successfully");
		Gson gson = new Gson();	
		 return gson.toJson(result); 

	}
	void InsertRoleRights(String ref_role_code,String ref_menu_code)
	{
	 
		 try
			{
				String insert_qry=" insert into "+schema+".crm_role_rights(ref_role_code,ref_menu_code) values ("
						+ "'"+ref_role_code+"',"
						+ "'"+ref_menu_code+"'"
						+ ")";
				logger.info("role_insert_qry=="+insert_qry);
				jdbcTemplate.update(insert_qry);
				 
				 

			}
			catch (Exception ex) {
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				if (cause instanceof SQLException) { }
				} 
	 
	}
	void DeleteFromRoleRights(String ref_role_code,String menu_ids)
	{
		 
		 try
			{
				String del_qry="delete from "+schema+".crm_role_rights where ref_role_code="+ref_role_code +" and ref_menu_code in ("+menu_ids+")";
				
				logger.info("role_del_qry=="+del_qry);
				jdbcTemplate.update(del_qry);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				Throwable cause = ex.getCause();
			 
				
				if (cause instanceof SQLException) { }
				} 
	 
	}
}
