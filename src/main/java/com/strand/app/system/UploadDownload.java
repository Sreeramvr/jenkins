package com.strand.app.system;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.log4j.Logger;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.strand.app.config.Format;
import com.strand.app.config.ResponseDataGeneric;

@RestController
@RequestMapping("titpl/system")
public class UploadDownload extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	static Logger logger = Logger.getLogger(UploadDownload.class); 
	String return_html = "";
	String module_id = "";
	String system_module_table_name = "";  
	String login_user_int_code = "";
	LinkedHashMap<String,LinkedHashMap<String,String>> full_element_details = new LinkedHashMap<String, LinkedHashMap<String,String>>();
	LinkedHashMap<String,LinkedHashMap<String,String>> ref_module_list_final = new LinkedHashMap<String, LinkedHashMap<String,String>>();
	Map<String, String> system_name_data_type_arr= new LinkedHashMap<String, String>();
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	public UploadDownload() {

	}
	public void ReAssignVariables()
	{
		return_html = "";
		login_user_int_code="0";
		full_element_details = new LinkedHashMap<String, LinkedHashMap<String,String>>();
		ref_module_list_final = new LinkedHashMap<String, LinkedHashMap<String,String>>();
		system_name_data_type_arr= new LinkedHashMap<String, String>();
	}

	@RequestMapping("DownloadExcelFormatForUpload")
	public @ResponseBody void DownloadFormat(@RequestParam("schema_id") String t_module_id,
			@RequestParam("system_module_table_name")  String s_system_module_table_name ,HttpServletRequest request, HttpServletResponse response) 
					throws IOException, BiffException, WriteException 
	{

		module_id = t_module_id;
		system_module_table_name = s_system_module_table_name;
		ReAssignVariables();
		
		response.setContentType("application/vnd.openxml"); 	
		response.setHeader("Content-Disposition", "attachment; filename="+system_module_table_name+".xls");
		List<Map<String,Object>> data_set=null;

		try
		{
			data_set= GetHeaderData("");
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		}
		

		WritableWorkbook wworkbook = Workbook.createWorkbook(response.getOutputStream());
		WritableSheet wsheet = wworkbook.createSheet("Sheet1", 0);


		int row=0;
		int col=0;

		if(data_set!=null)
		{
			logger.info("start for loop at="+Format.getCurrentTimeStamp());
			wsheet.addCell( new Label(col++, row,"internal_code"));
			for (Map<String, Object> map : data_set) 
			{
				try
				{
					wsheet.addCell( new Label(col++, row, map.get("name").toString()));
				}
				catch(NullPointerException e)
				{
					System.out.print("NullPointerException caught"+e.getMessage());
				}

			}
		}

		logger.info("Ended for loop at="+Format.getCurrentTimeStamp());
		wworkbook.write();
		wworkbook.close();
	}
	@RequestMapping("DownloadExcelFormatFullData")
	public @ResponseBody void DownloadExcelFormatFullData(@RequestParam("schema_id") String t_module_id,
			@RequestParam("system_module_table_name")  String s_system_module_table_name,
			@RequestParam("filter_cond")  String filter_cond,
			@RequestParam("selected_db_fields_arr")  String selected_db_fields_arr,
			@RequestParam("is_filter")  String is_filter,
			HttpServletRequest request, HttpServletResponse response) 
					throws IOException, BiffException, WriteException, ParseException 
	{
		JSONObject json =new JSONObject();
		String system_names="";
		String module_qry_cond="";
		String extra_cond="";
		String col_cond="";
		if(is_filter.equals("1"))
		{
			json = (JSONObject) new JSONParser().parse(filter_cond);

			Map<?,?> filter_cond_map=  json;
			logger.info("filter_cond_map: "+filter_cond_map);
			logger.info("selected_db_fields_arr: "+selected_db_fields_arr);
			logger.info("filter_cond: "+filter_cond);
			logger.info("is_filter: "+is_filter);
			logger.info("s_system_module_table_name: "+s_system_module_table_name);
			logger.info("t_module_id: "+t_module_id);

			String system_name_arr[]=selected_db_fields_arr.split(",");
			for(int i=0;i<system_name_arr.length;i++)
			{
				system_names+=" '"+system_name_arr[i]+"',";
			}

			logger.info("system_names: "+system_names);
			system_names=system_names.substring(0, system_names.length()-1);
			logger.info("system_names: "+system_names);

			if(!system_names.isEmpty())
				col_cond=" and su.system_name in ("+system_names+") ";

			int j=1;
			logger.info("filter_cond_map.size(): "+filter_cond_map.size());

			for(int k=0; k<filter_cond_map.size();k++)
			{
				Map value_map=(Map) filter_cond_map.get(Integer.toString(k));

				String db_filed =value_map.get("db_filed").toString().trim();
				String oper =value_map.get("oper").toString().trim();
				String val =value_map.get("val").toString().trim();
				String cond =value_map.get("cond").toString().trim();
				if(oper.equals("like"))
					val="%"+val+"%";
				module_qry_cond+= " "+db_filed+" "+ oper+" '"+val+"' ";
				if(filter_cond_map.size()!=j)
					module_qry_cond+=cond;
				j++;

			}

			if(!module_qry_cond.isEmpty())
				extra_cond=" and ("+module_qry_cond+") "; 
		}


		module_id = t_module_id;
		system_module_table_name = s_system_module_table_name;
		ReAssignVariables();
		

		response.setContentType("application/vnd.openxml"); 	
		response.setHeader("Content-Disposition", "attachment; filename="+system_module_table_name+".xls");
		List<Map<String,Object>> data_set=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> query_data_obj=new ArrayList<Map<String,Object>>();
		try
		{

			JSONObject qry_obj = new JSONObject();
			JSONObject s_field_obj = new JSONObject();
			ArrayList fields_arr = new ArrayList();
			DisplayGrid grid_obj=new DisplayGrid();

			data_set= GetHeaderData( col_cond); 
			qry_obj= grid_obj.GetGridQuery(  system_module_table_name, ""); 
			fields_arr=(ArrayList) qry_obj.get("fields_arr"); 
			String full_qry=(String) qry_obj.get("full_qry"); 
			s_field_obj=(JSONObject) qry_obj.get("s_field_obj");
			String sort_detail=" ORDER BY "+system_module_table_name+".internal_code desc" ;
			
			String e_qry="  SELECT  "+full_qry;
			e_qry+= " where 1=1  "+extra_cond;
			query_data_obj=jdbcTemplate.queryForList(e_qry);

		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		}
		

		WritableWorkbook wworkbook = Workbook.createWorkbook(response.getOutputStream());
		WritableSheet wsheet = wworkbook.createSheet("Sheet1", 0);


		int row=0;
		int col=0;
		Map<String, String> data_type_arr = new LinkedHashMap<String, String>();
		if(data_set.size()>0)
		{
			logger.info("start for loop at="+Format.getCurrentTimeStamp());
			wsheet.addCell( new Label(col++, row,"internal_code"));
			for (Map<String, Object> map : data_set) 
			{
				try
				{
					wsheet.addCell( new Label(col++, row, map.get("name").toString()));
					data_type_arr.put(map.get("system_name").toString(),map.get("data_type").toString());
				}
				catch(NullPointerException e)
				{
					logger.info("NullPointerException caught: "+e.getMessage());
				}

			}
		}
		row++;
		col=0;
		if(query_data_obj.size()>0)
		{
			for (Map<String, Object> map : query_data_obj)
			{
				col=0;
				String id=map.get("id").toString();
				wsheet.addCell( new Label(col++, row, id));

				for(String system_name : data_type_arr.keySet()) 
				{
					String data_type = data_type_arr.get(system_name);
					wsheet.addCell( new Label(col++, row, Format.GetObjectValueFromMap(map.get(system_name)).toString()));
				}
				row++;
			}
		}
		else
		{
			wsheet.mergeCells(0, 1,4, 2);
			wsheet.addCell( new Label(0, 1, "Data not found",getCellFormat(Colour.YELLOW2, Colour.BLACK, Alignment.CENTRE)));  
		}
		logger.info("Ended for loop at="+Format.getCurrentTimeStamp());
		wworkbook.write();
		wworkbook.close();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private List<Map<String, Object>> GetHeaderData(String col_cond) 
	{
		List<Map<String,Object>> query_data_obj=new ArrayList<Map<String,Object>>();

		try
		{		
			String Sql="select name,system_name,data_type from fbk_ia_s_user_schema_elements su where schema_code='"+module_id+"' "+col_cond+"  and is_multiple_group=0  order by sequence ";

			logger.info(" schema_elements query ="+Sql);
			 query_data_obj=jdbcTemplate.queryForList(Sql); 

		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		} 

		return query_data_obj;

	}	
	@RequestMapping("UploadModuleDetailsIntoDB")
	public @ResponseBody String UploadModuleDetailsIntoDB(@RequestParam("schema_id") String t_module_id,
			@RequestParam("system_module_table_name")  String s_system_module_table_name ,
			MultipartHttpServletRequest request, HttpServletResponse response) throws IOException, BiffException 
	{   
		ReAssignVariables();

		module_id = t_module_id;
		system_module_table_name = s_system_module_table_name;
		login_user_int_code=Format.GetSessonUserIntCode(request);;

		
		try
		{

			ReadExcelData(request,response);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		}


		//2. send it back to the client as <img> that calls get method
		//we are using getTimeInMillis to avoid server cached image 

		return return_html+"<br><span style='color:green'>Uploaded Successfully</span>";  

	}
	public void ReadExcelData(MultipartHttpServletRequest request, HttpServletResponse response ) throws IOException, BiffException
	{

		response.setContentType("application/vnd.openxml"); 	


		return_html="";
		String err_status="_error_while_uploading";
		Iterator<String> itr =  request.getFileNames(); 
		MultipartFile mpf = request.getFile(itr.next()); 
		InputStream uploadedStream = mpf.getInputStream(); 
		Workbook workbook = Workbook.getWorkbook(uploadedStream); 
		logger.info("File Name==>"+mpf.getOriginalFilename() );

		WritableWorkbook wworkbook = Workbook.createWorkbook(response.getOutputStream());
		WritableSheet wsheet = wworkbook.createSheet("Sheet1", 0);

		int row1=0;
		int col1=0;

		try {
			/*  byte[] bytes = mpf.getBytes();
			          String completeData = new String(bytes);
			          System.out.println(completeData);
			               //just temporary save file info into ufile
			       ufile.length = mpf.getBytes().length;
			       ufile.bytes= mpf.getBytes();
			       ufile.type = mpf.getContentType();
			       ufile.name = mpf.getOriginalFilename();*/

			Sheet sheet = workbook.getSheet(0);
			Cell rows = null;
			int columnsCount = sheet.getColumns();
			int rowsCount = sheet.getRows();
			Map<String, Integer> cell_index_arr_txt_num = new LinkedHashMap<String, Integer>();
			Map<Integer,String> cell_index_arr_num_txt = new LinkedHashMap<Integer,String> ();
			int first_row_indx=0;
			for (int first_row_col = 0;  first_row_col < columnsCount;  first_row_col++) 
			{	//Iterating sheet

				String cell_val = sheet.getCell(first_row_col, first_row_indx).getContents().trim();
				wsheet.addCell( new Label(col1++, row1, cell_val.toString()));
				cell_index_arr_txt_num.put(cell_val, first_row_col);
				cell_index_arr_num_txt.put(first_row_col,cell_val);

			}
			row1++;
			int is_internal_code_error=0;

			if(!cell_index_arr_txt_num.containsKey(("internal_code"))) 
			{
				is_internal_code_error=1;
				return_html=" <span class='upload_red'> Internal Code Field not present in excel file</>";
				wsheet.mergeCells(0, 1, columnsCount, 1);
				wsheet.addCell(new Label(0,1,"Internal Code Field not present in excel file", getCellFormat(Colour.IVORY, Colour.BLACK, Alignment.CENTRE)));

			}

			SimpleDateFormat year_month_fmt = new SimpleDateFormat("yyyy-MM-dd");
			if(is_internal_code_error==0)
			{
				GetSUSerSchemaElementDetailsData();
				for (int row = 1;  row <rowsCount;  row++) 
				{	
					col1=columnsCount;
					int is_error=0;
					String row_html="";

					String internal_code=sheet.getCell(cell_index_arr_txt_num.get("internal_code"), row).getContents().trim();
					Map<String, String> db_field_values_arr= new LinkedHashMap<String, String>();
					Map<String, String> db_field_values_arr_err= new LinkedHashMap<String, String>();
		 
					//db_field_values_arr.put("internal_code", internal_code);
					int i=1;
					for(String element_name : cell_index_arr_txt_num.keySet()) 
					{

						Integer cell_indx= cell_index_arr_txt_num.get(element_name);
						String cell_val = sheet.getCell(cell_indx, row).getContents().trim();

						Map<String, String> elements_list= new LinkedHashMap<String, String>();
						String final_cell_val=cell_val;
						if(full_element_details.containsKey(element_name))
						{
							elements_list=full_element_details.get(element_name);

							String system_name=elements_list.get("system_name").toString();
							String Type=elements_list.get("Type").toString();

							String optional=elements_list.get("optional").toString();
							String ref_module_code=elements_list.get("ref_module_code").toString();
							String ref_module_table_name=elements_list.get("ref_module_table_name").toString();
							final_cell_val=cell_val;
							db_field_values_arr_err.put(system_name, cell_val);
							if(optional.equals("0") && cell_val.isEmpty())
							{
								is_error=1;
								row_html+="<span  class='upload_orange'>"+element_name+" is Mandatory </span></br>";  
								wsheet.addCell(new Label(col1++, row1, element_name+" is Mandatory".toString() , getCellFormat(Colour.YELLOW, Colour.ORANGE, Alignment.CENTRE)));
							}

							if(Type.equals("Date") && !cell_val.isEmpty())
							{
								try
								{
									Cell cell1  = sheet.getCell(cell_indx, row);
									//									logger.info("cell1.getCellFormat : "+cell1.getCellFormat());
									//									logger.info("cell1.getType : "+cell1.getType());
									//									logger.info("cell1.getCellFeatures : "+cell1.getCellFeatures());
									//									logger.info("cell1.getContents : "+cell1.getContents());
									//									logger.info("cell1.toString().trim() : "+cell1.toString().trim());
									//									 
									DateCell dCell=null;
									dCell = (DateCell) cell1;
									final_cell_val= year_month_fmt.format(dCell.getDate());
									db_field_values_arr.put(system_name, final_cell_val);
								}
								catch(Exception e)
								{
									is_error=1; 
									wsheet.addCell(new Label(col1++, row1,  "Date exception - Cause: "+e.getCause()+" Msg: "+e.getMessage()+ "Date Format should be ex:1-Apr-2016 " , getCellFormat(Colour.YELLOW, Colour.ORANGE, Alignment.CENTRE)));
								}

							}
							else if(Type.equals("Reference Data") )
							{
								final_cell_val="0";
								//logger.info("ref_module_list_final.get(ref_module_table_name)"+ref_module_list_final.get(ref_module_table_name));
								if(!cell_val.isEmpty())
								{
									if(ref_module_list_final.containsKey(ref_module_table_name))
									{
										String check_cell_val=cell_val.toLowerCase();
										if(ref_module_list_final.get(ref_module_table_name).containsKey(check_cell_val))
										{
											final_cell_val=ref_module_list_final.get(ref_module_table_name).get(check_cell_val);

										}
										else
										{
											is_error=1;
											row_html+="<span  class='upload_orange'>"+element_name+"--"+cell_val+" is not found in "+ref_module_table_name+" </span></br>";
											wsheet.addCell(new Label(col1++, row1, element_name+": "+cell_val+" is not found in "+ref_module_table_name.toString() , getCellFormat(Colour.YELLOW, Colour.ORANGE, Alignment.CENTRE)));
										}
									}
								}
								db_field_values_arr.put(system_name, final_cell_val);

							}
							else if(Type.equals("Number") )
							{

								if(!cell_val.isEmpty())
								{
									try
									{
										Double.parseDouble(final_cell_val); 
										//final_cell_val=ref_module_list_final.get(ref_module_table_name).get(cell_val.trim());
										db_field_values_arr.put(system_name, final_cell_val);
									}
									catch(NumberFormatException n)
									{
										is_error=1;
										wsheet.addCell(new Label(col1++, row1, n.getMessage()+"-"+n.getCause() , getCellFormat(Colour.YELLOW, Colour.ORANGE, Alignment.CENTRE)));

									}
								}
							}
							else
							{
								db_field_values_arr.put(system_name, final_cell_val);
							}


						}
						i++;



					}//end of column loop 

					if(is_error==1)
					{
						String row_err="<span class='upload_red'> Error Found for Row No==>"+row +"</span></br>";
						return_html+="</br>"+row_err+row_html;

						wsheet.mergeCells(columnsCount, 0, --col1, 0);
						wsheet.addCell(new Label(columnsCount, 0 , "Error found for the following records" , getCellFormat(Colour.YELLOW, Colour.ORANGE, Alignment.LEFT)));

						col1=0;
						if(!db_field_values_arr_err.isEmpty())
						{
							wsheet.addCell(new Label(col1++, row1, internal_code.toString()));
							for (Object key : db_field_values_arr_err.keySet())
							{
								String keyStr = (String)key; 
								String keyvalue = (db_field_values_arr_err.get(keyStr).toString());
								wsheet.addCell(new Label(col1++, row1, keyvalue.toString()));  
							} 



						}
						row1++;
					}
					else
					{
						String sql_err="";
						if(!db_field_values_arr.isEmpty())
							sql_err=FrameWorkInsertUpdateData(row,internal_code,db_field_values_arr);
						if(!sql_err.isEmpty())
						{
							wsheet.addCell(new Label(columnsCount, row1 , sql_err , getCellFormat(Colour.YELLOW, Colour.ORANGE, Alignment.LEFT))); 
							wsheet.addCell(new Label(columnsCount, 0 , "Error found for the following records" , getCellFormat(Colour.YELLOW, Colour.ORANGE, Alignment.LEFT)));

							col1=0;
							if(!db_field_values_arr.isEmpty())
							{
								wsheet.addCell(new Label(col1++, row1, internal_code.toString()));
								for (Object key : db_field_values_arr.keySet())
								{
									String keyStr = (String)key; 
									String keyvalue = (db_field_values_arr_err.get(keyStr).toString());
									wsheet.addCell(new Label(col1++, row1, keyvalue.toString()));
								}
							}
							row1++;
						}
					}
				}//end of row loop

				if(return_html.isEmpty())
				{
					err_status="_succcessfully_uploaded";
					wsheet.mergeCells(0, 1, columnsCount, 1);
					wsheet.addCell(new Label(0,1,"Successfully Uploaded", getCellFormat(Colour.LIGHT_GREEN, Colour.DARK_GREEN, Alignment.CENTRE)));
				}
			}
			response.setHeader("Content-Disposition", "attachment; filename="+system_module_table_name+err_status+".xls");

			wworkbook.write();
			wworkbook.close();
			//System.out.println(cell_index_arr);

		}
		catch (Exception  e) {

			e.printStackTrace();
		}	


	}
	public String FrameWorkInsertUpdateData(int row_id,String internal_code,Map<String, String> db_field_values_arr)
	{
		String sql_err="";
		if(internal_code.isEmpty())
			internal_code="0";
		String insert_cols="";
		String  insert_col_values="";
		String update_cond=""; 
		logger.info("db_field_values_arr==>"+db_field_values_arr);

		for (Object key : db_field_values_arr.keySet()) {
			String keyStr = (String)key;
			String data_type=system_name_data_type_arr.get(keyStr).toString();
			logger.info("--"+db_field_values_arr.get(keyStr).toString());
			String keyvalue = (db_field_values_arr.get(keyStr).toString());
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
		String final_qry="";

		String insert_qry="insert into "+system_module_table_name;
		insert_qry+="(   "+insert_cols+",created_by, ";
		insert_qry+="created_date,modified_by,modified_date)  values ";
		insert_qry+=" ("+insert_col_values+","+login_user_int_code+",NOW(),"+login_user_int_code+",NOW())";	
		final_qry=insert_qry;

		update_cond+=" modified_by="+login_user_int_code+", modified_date=NOW() ";
		String update_qry=" update "+system_module_table_name+" set "+update_cond+" where internal_code="+internal_code;
		if(internal_code.equals("0"))
		{
			final_qry=insert_qry;
		}
		else
		{
			final_qry=update_qry;
		}
		logger.info("t_final_insert_update_qry------"+final_qry);
	

		try
		{
			jdbcTemplate.update(final_qry);



		}
		catch (Exception ex) {
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			return_html+=" <span class='upload_red'> Error while inserting/updating records for row id "+row_id+" </br>" +ex.getCause().toString()+"</span><br>";
			sql_err="Error while inserting/updating records "+ex.getCause().toString();
			if (cause instanceof SQLException) { }
		} 
		return sql_err;
	}
	public void GetSUSerSchemaElementDetailsData()
	{
		Map<String, String> ref_module_code_lists_int_code_arr = new LinkedHashMap<String, String>();
		try{


			String final_qry = "select  name,system_name,data_type as Type,";
			final_qry+=" ifnull(optional,'0') as optional, ";
			final_qry+=" ifnull(su.ref_module_code,0) as ref_module_code,ifnull(ref.file_name,'') as ref_module_table_name ";
			final_qry+=" from fbk_ia_s_user_schema_elements su inner join fbk_ia_menu_options s ";
			final_qry+=" on s.internal_code=su.schema_code ";
			final_qry+="  left join fbk_ia_menu_options ref on ref.internal_code=su.ref_module_code ";
			final_qry+=" where schema_code="+module_id +"  and is_multiple_group=0  order by su.sequence";

			//logger.info("********* Elements final_qry--------"+final_qry);
			@SuppressWarnings("rawtypes")
		 
				List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(final_qry); 

				for (Map<String, Object> map : query_data_obj) {
					Map<String, String> elements_list= new LinkedHashMap<String, String>();
					String name=map.get("name").toString();
					String system_name=map.get("system_name").toString();
					String Type=map.get("Type").toString();
					String optional=map.get("optional").toString();
					String ref_module_code=map.get("ref_module_code").toString();
					String ref_module_table_name=map.get("ref_module_table_name").toString();
					elements_list.put("system_name", system_name);
					elements_list.put("Type", Type);
					elements_list.put("optional", optional);
					elements_list.put("ref_module_code", ref_module_code);
					elements_list.put("ref_module_table_name", ref_module_table_name);
					system_name_data_type_arr.put(system_name,Type);
					full_element_details.put(name, (LinkedHashMap<String, String>) elements_list);
					if(!ref_module_table_name.isEmpty())
					{
						ref_module_code_lists_int_code_arr.put(ref_module_code, ref_module_table_name);
					}
				}

			

		}catch (Exception ex) {
			ex.printStackTrace();
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) {
				ex.printStackTrace();
			}
		}
		if(ref_module_code_lists_int_code_arr.size()>0)
		{
			System.out.println("coming to llopp");
			GetRefModuleDataSet(ref_module_code_lists_int_code_arr);
		}
		//logger.info("full_element_details="+full_element_details);
		//logger.info("full_element_details="+full_element_details);
		// logger.info("ref_module_code_lists_int_code_arr="+ref_module_code_lists_int_code_arr);
	}
	public void GetRefModuleDataSet(Map<String, String> ref_module_code_lists_int_code_arr)
	{
		AutoComplete ao_obj=new AutoComplete();
		for(String module_code : ref_module_code_lists_int_code_arr.keySet()) {
			String poup_module_name = ref_module_code_lists_int_code_arr.get(module_code);

			JSONObject meta_col_obj = new JSONObject();
			meta_col_obj=ao_obj.GetModuleMetaColumnsData(poup_module_name);

			String field_id=meta_col_obj.get("field_id").toString();
			String field_id1=meta_col_obj.get("field_id1").toString();
			String field_id2=meta_col_obj.get("field_id2").toString();
			String extra_columns=" '' as field_id1,'' as field_id2 ";
			if(!field_id2.isEmpty())
			{
				extra_columns=field_id1+" as field_id1,"+field_id2+" as field_id2 ";
			}

			try{

				String f_query="select internal_code, "+field_id+" as field_id, "+extra_columns +" from "+poup_module_name ;	
				logger.info("f_query=="+f_query);
				
				
					Map<String, String> local_ref_module_data_set = new LinkedHashMap<String, String>();
					
					List<Map<String,Object>> query_data_obj=jdbcTemplate.queryForList(f_query); 
					for (Map<String, Object> map : query_data_obj) {
						String internal_code=map.get("internal_code").toString();
						String qfield_id1=map.get("field_id1").toString().toLowerCase();
						String qfield_id2=map.get("field_id2").toString().toLowerCase();
						String qfield_id=map.get("field_id").toString().toLowerCase();
						local_ref_module_data_set.put(qfield_id,internal_code);
						if(!qfield_id1.isEmpty())
							local_ref_module_data_set.put(qfield_id1,internal_code);
						if(!qfield_id2.isEmpty())
							local_ref_module_data_set.put(qfield_id2,internal_code);

					}

					ref_module_list_final.put(poup_module_name,(LinkedHashMap<String, String>) local_ref_module_data_set);

					// logger.info("ref_module_list_final=="+ref_module_list_final);


				

			}catch (Exception ex) {
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				if (cause instanceof SQLException) {
					ex.printStackTrace();
				}
			}

		}

	}

	public static WritableCellFormat getCellFormat(Colour BgColour, Colour FontColour, Alignment textAlign) throws WriteException 
	{
		WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,false, UnderlineStyle.NO_UNDERLINE, FontColour);
		WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		cellFormat.setBackground(BgColour);
		cellFormat.setAlignment(textAlign);
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		return cellFormat;
	}
}
