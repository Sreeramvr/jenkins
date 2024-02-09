package com.strand.app.system;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@RequestMapping("titpl/CreatePopupFiles")

 
public class CreatePopupFiles {
	static Logger logger = Logger.getLogger(CreatePopupFiles.class);  
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	 
	@RequestMapping("create_popup_files")
	public @ResponseBody String create_popup_files(HttpServletRequest request) throws IOException
	{   
		
		
		List<Map<String,Object>> popup_grid_file_list=null;
		List<Map<String,Object>> get_display_cols=null;
		BufferedWriter writer = null;
		try
		{
			
			popup_grid_file_list = getPopupGridFilesData(request);	
			
			logger.info("Started for loop-PopupGrid_file_list");
			for(Map<String,Object> map :popup_grid_file_list)
			{
				String display_name=(String) map.get("display_name");
				String app_type=(String) map.get("app_type");
				String system_module_table_name=(String) map.get("system_module_table_name");
				String internal_code= (String) map.get("internal_code");
				
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
						String label_field_name=(String) disp_col_map.get("name");
						String field_name=(String) disp_col_map.get("field_name");
						
						if(disp_col_map.get("data_type")=="Number") 
							align= "align:'right',  "; 
						else 
							align= " align:'left', ";
						int grid_width = (Integer) disp_col_map.get("grid_width");
						if(grid_width==0)
							grid_width=85;
						grid_header+="\n\t '"+label_field_name+"',";
						grid_header_val+="\n\t {name:'"+field_name+"',index:'"+field_name+"',"+align+" width:'"+grid_width+"'},";
					}
					logger.info("Ended for loop-get_display_cols");
					
					grid_header=grid_header.substring(0,grid_header.length()-1);
					grid_header_val=grid_header_val.substring(0,grid_header_val.length()-1);
				
					String filename = "E:\\Work\\crm_workspace\\wla-crm\\WebContent\\grid_display_files\\"+app_type+"\\pop_up_"+grid_table_name+".js";
					System.out.println("filename="+filename);
				 
					String writeJsContent ="var pg_"+grid_table_name+" = "
							+ " jQuery('#table_grid1').jqGrid "
							+ "\n({"
							+ "\n\turl:$host_url+\"DisplayGrid/viewUserSchemaSrefDataDetails?pop_up_table_name=\"+pop_up_table_name+\"&calling_table_name=\"+calling_table_name+\"&clicked_id_system_name=\"+clicked_id_system_name,"
							+ "\n\tdatatype: 'json', "
							+ "\n\tcolNames:["+grid_header+"],\t"
							+ "\n\tcolModel:["+grid_header_val+"],\t"
							+ "\n\trowNum:50, "
							+ "\n\trowList:[50,100,200,500], "
							+ "\n\tpager: jQuery('#sub_grid_div1'), "
							+ "\n\tsortname: 'id', "
							+ "\n\theight:'250',"
					
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
						
				
							+ "\n\t},"
							+ "\n\tshrinkToFit: true,"
							
							+ "\n})"
							+ "\n.navGrid('#sub_grid_div1',{edit:false,add:false,del:false,search:false},{},{},{},{multipleSearch:false})"
							+ "\n.navButtonAdd('#sub_grid_div1',{caption:'Search', buttonicon :'ui-icon-pin-s', onClickButton:function(){pg_"+grid_table_name+"[0].toggleToolbar() } }) ; "
							+ "\n"
							+ "\npg_"+grid_table_name+".filterToolbar(); "
							+ "\npg_"+grid_table_name+"[0].toggleToolbar();"
	
							+ "\n$('.ui-search-toolbar').bind('keydown',function(evt)"
							+ "\n{"
							+ "\n\tvar kC  = (evt.which) ? evt.which : evt.keyCode;"
							+ "\n\tif(kC==40)"
							+ "\n\t\tjQuery('#table_grid1').editCell(1,1,false);"
							+ "\n\tif(kC==39) "
							+ "\n\t\t$('#'+evt.target.id).focusNextInputField(); "
							+ "\n\tif(kC==37) "
							+ "\n\t\t$('#'+evt.target.id).focusPreviousInputField(); "
							+ "\n});"
							+ "\n$('#ref_display_popup_title').html('"+display_name+"');";
					logger.info("Ended for loop-get_display_cols");
					
					logger.info("writePopupJsContent ="+writeJsContent);
					
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
		
		Gson gson = new Gson();	
		result.SetError(0,"success","Done");
		return gson.toJson(result);
	}	
	

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Map<String,Object>> getPopupGridFilesData( HttpServletRequest request) 
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
					+ " inner join "+schema+".fbk_ia_menu_options mo on mo.menu_group=mg.internal_code "
					+ " left join "+schema+".fbk_ia_menu_sub_groups sg on sg.internal_code=mo.menu_subgroup WHERE load_type='FrameWork'  ";

				logger.info("getGridFilesData---"+final_qry);
		
				query_data_obj=jdbcTemplate.queryForList(final_qry);
			
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
	public List<Map<String, Object>> getDisplayCols(HttpServletRequest request, String internal_code) 
	{
		List<Map<String,Object>> query_data_obj=null;
		
		try
		{
			String final_qry ="select s.file_name as table_name, su.name, su.data_type, ifnull(su.grid_width,'85') as grid_width, system_name as field_name  "
					+ "from "+schema+".fbk_ia_menu_options s "
					+ "inner join "+schema+".fbk_ia_s_user_schema_elements su on su.schema_code=s.internal_code  "
					+ "where s.internal_code="+internal_code+" and include_in_popup=1 "
					+ "order by su.sequence";

			System.out.println("getDisplayCols---"+final_qry);
	
			query_data_obj=jdbcTemplate.queryForList(final_qry);
				

		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException){}
		}
		return query_data_obj;
	}
	
}