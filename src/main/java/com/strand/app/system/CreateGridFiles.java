package com.strand.app.system;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.strand.app.config.ResponseDataGeneric;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value="titpl/CreateGridFiles",method = RequestMethod.POST)
@Api(value="Create Grid Files", description="Create Grid Files") 

public class CreateGridFiles {
	
	static Logger logger = Logger.getLogger(CreateGridFiles.class);  
	
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	
	 @RequestMapping("create_grid_files")
	public @ResponseBody String create_grid_files(HttpServletRequest request) throws IOException
	{   
		
		
		List<Map<String,Object>> grid_file_list=null;
		List<Map<String,Object>> get_display_cols=null;
		BufferedWriter writer = null;
		try
		{
			
			grid_file_list = getGridFilesData(request);	
			logger.info("Started for loop-grid_file_list");
			for(Map<String,Object> map :grid_file_list)
			{
				
				String app_type=(String) map.get("app_type");
				
				String system_module_table_name=(String) map.get("system_module_table_name");
				String internal_code= map.get("internal_code").toString();
				
				
				get_display_cols=getDisplayCols(request,internal_code);
				System.out.println("internal_code-------"+internal_code);
				if(get_display_cols!=null && get_display_cols.size()>0)
				{
					String grid_header="";
					String grid_header_val="";
					String grid_table_name= system_module_table_name;
					String align="";
				 
					
					int m=1;
					logger.info("Started for loop-get_display_cols");
					for(Map<String,Object> disp_col_map: get_display_cols)
					{
						String lable_field_name=(String) disp_col_map.get("name");
						String field_name=(String) disp_col_map.get("field_name");
						
						if(disp_col_map.get("data_type")=="Number") 
							align= "align:'right',  "; 
						else 
							align= " align:'left', ";
						int grid_width = Integer.parseInt(disp_col_map.get("grid_width").toString());
						if(grid_width==0)
							grid_width=85;
						grid_header+="\n\t '"+lable_field_name+"',";
						grid_header_val+="\n\t {name:'"+field_name+"',index:'"+field_name+"',"+align+" width:'"+grid_width+"'},";
										 
					}
					logger.info("Ended for loop-get_display_cols");
					grid_header=grid_header.substring(0,grid_header.length()-1);
					grid_header_val=grid_header_val.substring(0,grid_header_val.length()-1);
				 
					String filename = "E:\\Work\\crm_workspace\\wla-crm\\WebContent\\grid_display_files\\"+app_type+"\\"+grid_table_name+".js";
					logger.info("filename="+filename);
					
					String writeJsContent ="var table_grid_"+grid_table_name+" = "
							+ " jQuery('#table_grid_"+grid_table_name+"').jqGrid "
							+ "\n({"
							+ "\n\t	url:$host_url+'DisplayGrid/viewUserSchemaModuleDetails?table_name="+grid_table_name+"',"
							+ "\n\tdatatype: 'json', "
							+ "\n\tcolNames:["+grid_header+"],\t"
							+ "\n\tcolModel:["+grid_header_val+"],\t"
							+ "\n\trowNum:50, "
							+ "\n\trowList:[50,100,200,500], "
							+ "\n\tpager: jQuery('#sub_grid_div_"+grid_table_name+"'), "
							+ "\n\tsortname: 'id', "
							+ "\n\theight:'250',autowidth:true, "
							+ "\n\tviewrecords: true, "
							+ "\n\tsortorder: 'desc', "
							+ "\n\tmultiselect: false, "
							+ "\n\tautowidth: true ,"
							+ "\n\tcellsubmit: 'clientArray', "
							+ "\n\tcellEdit: true,"
							+ "\n\tloadComplete:function() "
							+ "\n\t{"
							+ "\n\t\t$('.ui-jqgrid-htable').css('border-collapse','separate');"
							+ "\n\t},"
							+ "\n\tondblClickRow: function(rowid)"
							+ "\n\t{"
							+ "\n\t\tall_obj.EditRecords(rowid,'"+grid_table_name+"');"
							+ "\n\t\treturn;"
							+ "\n\t},"
							+ "\n\t shrinkToFit: true,"
							+ "\n\t 	editurl:$host_url+'DisplayGrid/deleteUserSchemaModuleDetails?table_name="+grid_table_name+"'"
							+ "\n})"
							+ "\n.navGrid('#sub_grid_div_"+grid_table_name+"',{edit:false,add:false,del:true,search:false},{},{},{},{multipleSearch:true})"
							+ "\n.navButtonAdd('#sub_grid_div_"+grid_table_name+"',{caption:'Search', buttonicon :'ui-icon-pin-s', onClickButton:function(){table_grid_"+grid_table_name+"[0].toggleToolbar() } }) ; "
							+ "\n"
							+ "\ntable_grid_"+grid_table_name+".filterToolbar(); "
							+ "\ntable_grid_"+grid_table_name+"[0].toggleToolbar();"
	
							+ "\n$('.ui-search-toolbar').bind('keydown',function(evt)"
							+ "\n{"
							+ "\n\tvar kC  = (evt.which) ? evt.which : evt.keyCode;"
							+ "\n\tif(kC==40)"
							+ "\n\t\tjQuery('#table_grid').editCell(1,1,false);"
							+ "\n\tif(kC==39) "
							+ "\n\t\t$('#'+evt.target.id).focusNextInputField(); "
							+ "\n\tif(kC==37) "
							+ "\n\t\t$('#'+evt.target.id).focusPreviousInputField(); "
							+ "\n});";
					logger.info("Ended for loop-get_display_cols");
					
					logger.info("somecontent="+writeJsContent);
					
					 writer = new BufferedWriter( new FileWriter( filename));
					 writer.write( writeJsContent); writer.close( );
				}
			}
			
			
		}
	
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException){}
		}
		
		ResponseDataGeneric result=new ResponseDataGeneric();
		Gson gson = new Gson();	
		result.SetError(0,"success","Done");
		return gson.toJson(result);
 
	}	
	

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Map<String,Object>> getGridFilesData( HttpServletRequest request) 
	{
		List<Map<String,Object>> query_data_obj=null;
		
		try
		{
			String final_qry ="select mg.app_type, ifnull(mo.file_name,'') as system_module_table_name,"
					+ "mo.internal_code,mg.menu_name,mg.menu_type,ifnull(sg.sub_menu_name,'') as sub_menu_name,mo.module_name, "
					+ "mo.display_name, ifnull(load_type,'Internal') as  load_type,ifnull(view_type,'EntryScreen') as  view_type "
					+ " ,ifnull(include_save,0) as include_save,ifnull(include_view,0) as include_view,ifnull(include_close,0) as include_close,"
					+ "ifnull(include_clear,0) as include_clear,ifnull(custom_links,'') as custom_links"
					+ " from "+schema+".fbk_ia_menu_groups mg "
					+ " inner join  "+schema+".fbk_ia_menu_options mo on mo.menu_group=mg.internal_code "
					+ " left join  "+schema+".fbk_ia_menu_sub_groups sg on sg.internal_code=mo.menu_subgroup WHERE load_type='FrameWork' and mo.file_name!='fbk_feedback_master'  ";

			System.out.println("getGridFilesData---"+final_qry);
			query_data_obj=jdbcTemplate.queryForList(final_qry);
			
			System.out.println("query_data_obj...."+query_data_obj);
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException){}
		}
		return query_data_obj;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Map<String, Object>> getDisplayCols( HttpServletRequest request, String internal_code) 
	{
		List<Map<String,Object>> query_data_obj=null;
		
		try
		{
			String final_qry ="select s.file_name as table_name, su.name, su.data_type, ifnull(su.grid_width,'85') as grid_width, system_name as field_name  "
					+ "from "+schema+".fbk_ia_menu_options s "
					+ "inner join "+schema+".fbk_ia_s_user_schema_elements su on su.schema_code=s.internal_code  "
					+ "where s.internal_code="+internal_code+" and include_in_grid=1 "
					+ "order by su.sequence";
			System.out.println("getDisplayCols---"+final_qry);
			 
			query_data_obj=jdbcTemplate.queryForList(final_qry);
			
		}
		catch (Exception ex) 
		{
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException){}
		}
		return query_data_obj;
	}
	
}


