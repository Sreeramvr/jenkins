package com.strand.app.system;

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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.strand.app.config.Format;
import com.strand.app.config.ResponseDataGeneric;

@RestController
@RequestMapping("titpl/SystemSetup")
public class SystemSetup {
	 
		static Logger logger = Logger.getLogger(SystemSetup.class);   
		@Value("${app.schema:default}")
		String schema;
		
		@Autowired
	    JdbcTemplate jdbcTemplate;
		
		@Autowired
		ResponseDataGeneric result;
	public SystemSetup() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("CheckSessionAndGetMenus")
	public  @ResponseBody String CheckSessionAndGetMenus(HttpServletRequest request) throws SQLException, ClassNotFoundException
	{
		String app_type=request.getParameter("app_type");
		JSONObject data_obj = new JSONObject();
		
		
		Gson gson = new Gson();	
		//HttpSession session = request.getSession(true);
		
		
		
		String userId="73";
		try {
			//userId = request.getSession().getAttribute("s_loginId").toString();
		} catch (Exception e) {
			userId="";
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//---------To be removed
		
		System.out.println("userId..."+userId);
		if(userId.trim().isEmpty())
		{
			result.SetError(-1,"failure","Session Expired..!!");
			return gson.toJson(result);
		}
		
		  List<Map<String,Object>> branch_list=null;
		  
		  //String userId="admin";
		  
		  data_obj.put("logged_in_user", ""); data_obj.put("loginUserName", "");
		  
		  
//		  branch_list=getBranchList(); System.out.println("branch_list="+branch_list);
		  
		  try {
		  
		  
		  String menu_html=GetMenuList(userId,app_type); List<Map<String,Object>>
		  application_menus=null;
		  
		  data_obj.put("menu_html", menu_html); data_obj.put("cur_mmm_yyyy",
		  Format.GetCurrentMonthNameYear()); data_obj.put("branch_list", branch_list);
		  
		  } catch (Exception ex) { ex.printStackTrace(); Throwable cause =
		  ex.getCause(); if (cause instanceof SQLException) { } }
		 
		
		result.SetError(0,"success",data_obj);
						 
		 
			 
		
		return gson.toJson(result);
 
	}
//	public List<Map<String,Object>> getBranchList()
//	{
//		List<Map<String,Object>> branch_list=null;
//		try
//		{
//			String sql="select internal_code, branch_name from crm_branch_master ";
//			System.out.println(sql);
//			branch_list=jdbcTemplate.queryForList(sql);
//	 
//	   		
//		}
//		catch (Exception ex) {
//			ex.printStackTrace();
//	        Throwable cause = ex.getCause();
//	        System.out.println("Error at GetQuesMastBasicDetails "+ex.getMessage());
//	        if (cause instanceof SQLException) { }
//	 }
//		
//		
//		
//		return branch_list;
//	}
	public  String GetMenuList(String login_id,String app_type)
	{
	   
		String html="";
	 
		 html="";
	 
		 String menu_icons="";
		 String sys_icons="";
		 String menu_icons_col="";
		 String sys_icons_col="";
		 
	      try{
	        
	         Map<String,String>  main_menu_list = new LinkedHashMap<String,String>();
	        
	     
	         LinkedHashMap<String,LinkedHashMap<String,String>> full_single_menu_set = new LinkedHashMap<String, LinkedHashMap<String,String>>();
	         LinkedHashMap<String,LinkedHashMap<String,String>> full_direct_menu_set = new LinkedHashMap<String, LinkedHashMap<String,String>>();
	         LinkedHashMap<String,LinkedHashMap<String,LinkedHashMap<String,String>>> full_multiple_menu_set = new LinkedHashMap<String, LinkedHashMap<String,LinkedHashMap<String,String>>>();
	         
	        Map<String,String> module_wise_a_list = new HashMap<String,String>();
	    	Map<String,String> module_wise_icon_list = new HashMap<String,String>();
	    	Map<String,String> module_wise_icon_color = new HashMap<String,String>();
	         
	         String Sql = "select  mo.internal_code,mg.menu_name,mg.menu_type,ifnull(sg.sub_menu_name,'Others') as sub_menu_name,mo.module_name,ifnull(mo.icons,'hand-o-right') as menu_icons,";
     				Sql += " ifnull(mg.icons,'desktop') as sys_icons,ifnull(mg.icons_color,'blue') as sys_icons_col,ifnull(mo.icons_color,'blue') as menu_icons_col,";
 					Sql +=" ifnull(mo.file_name,'') as file_name, mo.display_name,ifnull(js_path,'0') as js_path,ifnull(onclick,'0') as  onclick, "
 							+ " ifnull(load_type,'Internal') as  load_type ,ifnull(menu_tag,'0') as menu_tag,ifnull(custom_links,'0') as custom_links ";
			 		Sql +=" from "+schema+".fbk_ia_menu_groups mg ";
			 		Sql +=" inner join "+schema+".fbk_ia_menu_options mo on mo.menu_group=mg.internal_code";
			 		Sql +=" left join "+schema+".fbk_ia_menu_sub_groups sg on sg.internal_code=mo.menu_subgroup";
			 		Sql +=" inner join "+schema+".crm_role_rights rr on rr.ref_menu_code=mo.internal_code";
			 		Sql +=" inner join "+schema+".crm_user_master_roles_group ur on ur.role=rr.ref_role_code";
			 		Sql +=" inner join "+schema+".s_sysdb um on ur.ref_s_ref_data_code=um.internal_code  and um.internal_code='"+login_id+"' "; 
			 		Sql +=" where mg.app_type='"+app_type+"' and mg.active='Yes' and mo.active='Yes' ";//     
			 		Sql +=" order by mg.sequence,ifnull(sg.sequence,0),ifnull(mo.sequence,0) ";
			 		
//			 		String Sql = "select  mo.internal_code,mg.menu_name,mg.menu_type,ifnull(sg.sub_menu_name,'Others') as sub_menu_name,mo.module_name,ifnull(mo.icons,'hand-o-right') as menu_icons,";
//     				Sql += " ifnull(mg.icons,'desktop') as sys_icons,ifnull(mg.icons_color,'blue') as sys_icons_col,ifnull(mo.icons_color,'blue') as menu_icons_col,";
// 					Sql +=" ifnull(mo.file_name,'') as file_name, mo.display_name,ifnull(js_path,'0') as js_path,ifnull(onclick,'0') as  onclick, "
// 							+ " ifnull(load_type,'Internal') as  load_type ,ifnull(menu_tag,'0') as menu_tag,ifnull(custom_links,'0') as custom_links ";
//			 		Sql +=" from "+schema+".fbk_ia_menu_groups mg ";
//			 		Sql +=" inner join "+schema+".fbk_ia_menu_options mo on mo.menu_group=mg.internal_code";
//			 		Sql +=" left join "+schema+".fbk_ia_menu_sub_groups sg on sg.internal_code=mo.menu_subgroup";
//			 		Sql +=" inner join "+schema+".crm_role_rights rr on rr.ref_menu_code=mo.internal_code";
//			 		Sql +=" inner join "+schema+".crm_user_master_roles_group ur on ur.role=rr.ref_role_code";
//			 		Sql +=" inner join "+schema+".crm_employee_master um on ur.ref_s_ref_data_code=um.internal_code  and um.internal_code='"+login_id+"' "; 
//			 		Sql +=" where mg.app_type='"+app_type+"' and mg.active='Yes' and mo.active='Yes' ";//     
//			 		Sql +=" order by mg.sequence,ifnull(sg.sequence,0),ifnull(mo.sequence,0) ";
					 		logger.info("******* User Rights Qry==>"+Sql);
	    System.out.println("menus--->"+Sql);
	    		 
				
	    		  
	    		
	    			 String a_link="";
	    		
	    		  List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(Sql); 
	    		System.out.println("query_data_obj=="+query_data_obj);  
	    		 
	    		  new ArrayList<String>();
    			 
	    		  for (Map<String, Object> map : query_data_obj) {
	    			  String internal_code=map.get("internal_code").toString();
	    			  String menu_type=map.get("menu_type").toString();
	    			 
	    			  String menu_name=map.get("menu_name").toString();
						String sub_menu_name= map.get("sub_menu_name").toString();
						 String module_name=map.get("module_name").toString();
						 String display_name=map.get("display_name").toString();
						 String file_name=map.get("file_name").toString();
						 String load_type=map.get("load_type").toString();
						 String menu_tag=map.get("menu_tag").toString();
						 
						 String custom_links=map.get("custom_links").toString();
						 String js_path= map.get("js_path").toString();
						 String onclick=map.get("onclick").toString();
						  System.out.println(load_type+"--"+module_name);
						  
						 menu_icons=map.get("menu_icons").toString();
						 sys_icons=map.get("sys_icons").toString();
						 menu_icons_col=map.get("menu_icons_col").toString();
						 sys_icons_col=map.get("sys_icons_col").toString();
						 
						 module_wise_icon_list.put(menu_name, sys_icons);
						 module_wise_icon_color.put(menu_name, sys_icons_col);
							 
						 String  a_sub_menu_name="";
						 if(menu_type.equals("Multiple"))
							 a_sub_menu_name=sub_menu_name;
						 if(menu_icons.isEmpty())
							 menu_icons = "road";
						 
						 if(load_type.equals("Internal") && !menu_tag.equals("ComingSoon") && !menu_tag.equals("New"))
						 {
							 a_link="<li><a href='javascript:void(0);' id='"+file_name+"'  js_path='"+js_path+"' custom_links='"+custom_links+"' main_menu_name='"+menu_name+"'  sub_menu_name='"+a_sub_menu_name+"' onclick=\"LoadHtmlFiles('"+file_name+"','"+module_name+"','"+display_name+"','"+internal_code+"')\" on_click_attr=\""+onclick+"\" ><i   class='fa fa-"+menu_icons+"'></i> <span class='hide-menu'>"+display_name+"</span></a> </li> ";
						 }
						 else if(load_type.equals("NEW") || menu_tag.equals("New"))
						 {
							 a_link="<li><a href='javascript:void(0);' id='"+file_name+"'  js_path='"+js_path+"' custom_links='"+custom_links+"' main_menu_name='"+menu_name+"'  sub_menu_name='"+a_sub_menu_name+"' onclick=\"LoadHtmlFiles('"+file_name+"','"+module_name+"','"+display_name+"','"+internal_code+"')\" on_click_attr=\""+onclick+"\" ><i   class='fa fa-"+menu_icons+"'></i> <span class='hide-menu'>"+display_name+"</span></a> </li> ";
						 }
						 else if(load_type.equals("COMINGSOON") || menu_tag.equals("ComingSoon"))
						 {
							 a_link="<li><a href='javascript:void(0);'><i   class='fa fa-"+menu_icons+"'></i> <span class='hide-menu'>"+display_name+"</span></a> </li> ";
						 }
						 else if(load_type.equals("FrameWork"))
						 {
							 a_link="<li><a  href='javascript:void(0);' id='"+file_name+"'  js_path='"+js_path+"' custom_links='"+custom_links+"' main_menu_name='"+menu_name+"'  sub_menu_name='"+a_sub_menu_name+"' onclick=\"getModulesDetailsNew('"+file_name+"','"+module_name+"','"+internal_code+"','New','"+display_name+"',0)\" on_click_attr=\""+onclick+"\" ><i   class='fa fa-"+menu_icons+"'></i> <span class='hide-menu'>"+display_name+"</span></a> </li> ";
						 }
						
						 else if(load_type.equals("Application"))
						 {
							 a_link="<li><a href='javascript:void(0);' id='"+file_name+"'   js_path='"+js_path+"' custom_links='"+custom_links+"' main_menu_name='"+menu_name+"'  sub_menu_name='"+a_sub_menu_name+"'  onclick=\""+onclick+"\" ><i   class='fa fa-"+menu_icons+"'></i> <span class='hide-menu'>"+display_name+"</span></a> </li> ";
						 }
						 else
						 {
							//q System.out.println(onclick);
							 a_link="<li><a   id='"+file_name+"'  js_path='"+js_path+"' custom_links='"+custom_links+"'  href='"+onclick+"' target='_new' ><i   class='fa fa-"+menu_icons+"'></i> <span class='hide-menu'>"+display_name+"</span></a> </li> ";
						 }
						 
						 module_wise_a_list.put(module_name, a_link);
						 if(!main_menu_list.containsKey(menu_name))
							 main_menu_list.put(menu_name,menu_type);
						 
						 
						 		if(menu_type.equals("Single"))
						 		{
						 			 LinkedHashMap<String, String> module_list = new LinkedHashMap<String,String>();
									 module_list.put(internal_code, module_name);
										if(full_single_menu_set.containsKey(menu_name))
										{

											full_single_menu_set.get(menu_name).put(internal_code, module_name);
										}
										else
										{
											full_single_menu_set.put(menu_name, module_list);
										}
						 			
						 		}
						 		else if(menu_type.equals("Direct"))
						 		{
						 			 LinkedHashMap<String, String> module_list = new LinkedHashMap<String,String>();
									 module_list.put(internal_code, module_name);
										if(full_direct_menu_set.containsKey(menu_name))
										{

											full_direct_menu_set.get(menu_name).put(internal_code, module_name);
										}
										else
										{
											full_direct_menu_set.put(menu_name, module_list);
										}
						 			
						 		}
						 		else if(menu_type.equals("Multiple"))
						 		{
						 			 
						 			 LinkedHashMap<String, String> module_list_multi = new LinkedHashMap<String,String>();
						 			module_list_multi.put(internal_code, module_name);
						 			 
										if(full_multiple_menu_set.containsKey(menu_name))
										{

											if(full_multiple_menu_set.get(menu_name).containsKey(sub_menu_name))
											{
												full_multiple_menu_set.get(menu_name).get(sub_menu_name).put(internal_code, module_name);
											}
											else
											{
												full_multiple_menu_set.get(menu_name).put(sub_menu_name, module_list_multi);
											}
												
										}
										else
										{
											 LinkedHashMap<String,  LinkedHashMap<String, String> > sub_menu_list_multi = new LinkedHashMap<String, LinkedHashMap<String, String> >();
											 sub_menu_list_multi.put(sub_menu_name, module_list_multi);
											full_multiple_menu_set.put(menu_name, sub_menu_list_multi);
										 
										}
						 			
						 		}
						 
	    		  	}//end of for loop
	    		 
	    		  int row=0;
	    		  for(String menu_name : main_menu_list.keySet()) {
	    			  row++;
	    			  
	    			  String menu_type = main_menu_list.get(menu_name);
	    			  
	    			  String menu_icon_name=  module_wise_icon_list.get(menu_name);
	    			  String menu_icon_color=  module_wise_icon_color.get(menu_name);
	    			  
	    			  if(menu_type.equals("Single"))
	    			  {
	    				  html+="<li>";
	    				  //html+="<a href='#' data-close-others='true' data-delay='0' data-hover='' data-toggle='' class='dropdown-toggle'>";
	    				  html+="<a  href='index.html' class='waves-effect' onclick='System.EnableMenuActive("+row+")' id='ad_admin_"+row+"' ><i class='fa fa-"+menu_icon_name+"' data-icon='v'></i> ";
	    				  html+=" <span class='hide-menu'> "+menu_name+" <span class='fa arrow'></span> </span></a>  ";
	    				  html+=" <ul class='nav nav-second-level'>";
		    			  LinkedHashMap<String, String> module_list = new LinkedHashMap<String,String>();
		    			  module_list =full_single_menu_set.get(menu_name);
		    			  for(String int_code : module_list.keySet()) {
		    				  String module_name = module_list.get(int_code);
		    				  html+=module_wise_a_list.get(module_name);
		    				 
		    			  }
		    			  html+=" </ul>	</li>";
	    			  }
	    			  else if(menu_type.equals("Direct"))
	    			  {
		    			  LinkedHashMap<String, String> module_list = new LinkedHashMap<String,String>();
		    			  module_list =full_direct_menu_set.get(menu_name);
		    			  for(String int_code : module_list.keySet()) {
		    				  String module_name = module_list.get(int_code);
		    				  html+=module_wise_a_list.get(module_name);
		    			  }
	    			  }
	    			  else if (menu_type.equals("Multiple"))
	    			  {
	    				  LinkedHashMap<String,  LinkedHashMap<String, String> > sub_menu_list_multi_temp = new LinkedHashMap<String, LinkedHashMap<String, String> >();
	    				  sub_menu_list_multi_temp=full_multiple_menu_set.get(menu_name);
	    				  html+="<li>";
	    				  //html+="<a href='#' data-close-others='true' data-delay='0' data-hover='' data-toggle='' class='dropdown-toggle'>";
	    				  html+="<a   href='javascript:void(0)  class='waves-effect'  onclick='System.EnableMenuActive("+row+")' id='ad_admin_"+row+"'><i   class='fa fa-"+menu_icon_name+"'></i> ";
	    				  html+=" <span class='hide-menu'>"+menu_name+"<span class='fa arrow'></span></span></a>";
	    				  html+=" <ul class='nav nav-second-level'>";
	    				  //
	    				  for( String sub_menu_name : sub_menu_list_multi_temp.keySet() )
	    				  {
	    					  html+=" <li> <a href='javascript:void(0)' class='waves-effect'><i data-icon='&#xe008;' class='linea-icon linea-basic fa-fw'></i><span class='hide-menu'> "+sub_menu_name+" </span><span class='fa arrow'></span></a>";
	    					  html+=" <ul class='nav nav-third-level'>";
	    					  LinkedHashMap<String, String> module_list = new LinkedHashMap<String,String>();
	    					  module_list =full_multiple_menu_set.get(menu_name).get(sub_menu_name);
	    					  for(String int_code : module_list.keySet()) {
			    				  String module_name = module_list.get(int_code);
			    				  html+=module_wise_a_list.get(module_name);
			    			  }
	    					  
	    					  html+=" </ul></li> ";
	    					  
	    				  }
	    				  html+=" </ul>	</li>";
	    				  
	    			  }
	    		         
	    		    }
	    		   
	    		
	    		 
				  
			   }catch (Exception ex) {
				   ex.printStackTrace();
			        Throwable cause = ex.getCause();
			        if (cause instanceof SQLException) {
			        	 
			        }
			 }
	      
	      return html;
	       
	    
	}
	

}
