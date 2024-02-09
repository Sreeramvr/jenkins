package com.strand.app.titpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.strand.app.config.ResponseDataGeneric;

import jxl.read.biff.BiffException;
@RestController
@RequestMapping("titpl/VarSer")
public class VarSer {

	private static final int String = 0;
	
	static Logger logger = Logger.getLogger(pangolin.class);   
	
	String loginId="";
	String calling_table_name="";
	String pop_up_table_name="";
	String system_name="";
	String value="";
	
	@Value("${app.schema:default}")
	String schema;
	
	@Value("${app.file_path:default}")
	String file_path;
	 
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	
	public VarSer() {
	// TODO Auto-generated constructor stub
		SessionFactory factory;  
	}
	@RequestMapping("GetVarientTableDetails")
	public @ResponseBody String GetVarientTableDetails (HttpServletRequest request,
			 
			@RequestParam("as_on_date")  String as_on_date,	
			@RequestParam("past_days")  String past_days
			 ) throws SQLException, ClassNotFoundException
			{
		String return_html = "";
		String error = "0";
		JSONObject return_obj = new JSONObject();
		  ResponseDataGeneric result=new ResponseDataGeneric();
		  List<Map<String,Object>> query_data_obj=null;
		  JSONObject data_obj = new JSONObject();
			Gson gson = new Gson();	
			int rowsCount=0;
		 
			return_html = "";
			return_obj = new JSONObject(); 
			 String qry="";
		try
		{ 
			  qry="SELECT who_common_name, "
			 		+ "GROUP_CONCAT(lin) AS lin, "
			 		+ "SUM(sample_cnt) AS sample_cnt, "
			 		+ "	who_status "
			 		+ "FROM (SELECT who_common_name,GROUP_CONCAT(DISTINCT ' ',pangolin_lineage) AS lin, "
			 		+ "who_status, "
			 		+ "	COUNT(s.internal_code) AS sample_cnt  "
			 		+ "FROM lineage_master l "
			 		+ "INNER JOIN pangolin_data p ON p.lineage = l.pangolin_lineage "
			 		+ "INNER   JOIN sample_master s ON s.sample_id=p.sample_id "
			 		+ "WHERE s.sample_col_date <='"+as_on_date+"' "
			 		
			 		+ "GROUP BY pangolin_lineage,who_common_name,who_status "
			 		+ ")a "
			 		+ "GROUP BY who_common_name,who_status";
			 System.out.println("var serv"+qry);
			 query_data_obj =jdbcTemplate.queryForList(qry);
			 data_obj.put("first_set",query_data_obj);
			 
			 qry="SELECT DATE_SUB('"+as_on_date+"',INTERVAL  "+past_days+"-1 DAY) AS from_date, "
			 		+ "DATE_FORMAT(DATE_SUB('"+as_on_date+"',INTERVAL  "+past_days+"-1 DAY),'%D %b')  AS ui_from_date, "
			 		+ "DATE_FORMAT('"+as_on_date+"','%D %b')  AS ui_as_on_date, "
			 		+ " DATE_SUB('"+as_on_date+"',INTERVAL "+past_days+" DAY) AS past_to_date, "
			 		+ "DATE_SUB('"+as_on_date+"',INTERVAL  ("+past_days+"*2)-1 DAY) AS past_from_date,"
			 		+ " DATE_FORMAT( DATE_SUB('"+as_on_date+"',INTERVAL "+past_days+" DAY),'%D %b') AS ui_past_to_date, "
			 		+ " DATE_FORMAT( DATE_SUB('"+as_on_date+"',INTERVAL ("+past_days+"*2)-1 DAY),'%D %b') AS ui_past_from_date";
			 System.out.println("var serv"+qry);
			 query_data_obj =jdbcTemplate.queryForList(qry);
			  String from_date="";
			  String ui_from_date= "";
			  String ui_as_on_date= "";
			  String past_to_date= "";
			  String past_from_date= "";
			  String ui_past_to_date= "";
			  String ui_past_from_date= "";
			 for (Map<String, Object> map : query_data_obj)
			  {
	    		   from_date= map.get("from_date").toString().trim();
	    		   ui_from_date= map.get("ui_from_date").toString().trim();
	    		   ui_as_on_date= map.get("ui_as_on_date").toString().trim();
	    		   past_to_date= map.get("past_to_date").toString().trim();
	    		   past_from_date= map.get("past_from_date").toString().trim();
	    		   ui_past_to_date= map.get("ui_past_to_date").toString().trim();
	    		   ui_past_from_date= map.get("ui_past_from_date").toString().trim();
	    		  
			  }
			 qry="SELECT who_common_name, "
			 		+ "GROUP_CONCAT(lin) AS lin, "
			 		+ "SUM(sample_cnt) AS sample_cnt, "
			 		+ "who_status "
			 		+ "FROM (SELECT who_common_name,GROUP_CONCAT(DISTINCT ' ',pangolin_lineage) AS lin, "
			 		+ "who_status, "
			 		+ "COUNT(s.internal_code) AS sample_cnt "
			 		+ "	FROM lineage_master l "
			 		+ "INNER JOIN pangolin_data p ON p.lineage = l.pangolin_lineage "
			 		+ "INNER   JOIN sample_master s ON s.sample_id=p.sample_id "
			 		+ "WHERE s.sample_col_date between '"+from_date+"' and '"+as_on_date+"' "
			 		+ "GROUP BY pangolin_lineage,who_common_name,who_status "
			 		+ ")a "
			 		+ "GROUP BY who_common_name,who_status";
			 query_data_obj =jdbcTemplate.queryForList(qry);
			 data_obj.put("second_set",query_data_obj);
			 
			 qry="SELECT who_common_name, "
				 		+ "GROUP_CONCAT(lin) AS lin, "
				 		+ "SUM(sample_cnt) AS sample_cnt, "
				 		+ "who_status "
				 		+ "FROM (SELECT who_common_name,GROUP_CONCAT(DISTINCT ' ',pangolin_lineage) AS lin, "
				 		+ "who_status, "
				 		+ "COUNT(s.internal_code) AS sample_cnt "
				 		+ "	FROM lineage_master l "
				 		+ "INNER JOIN pangolin_data p ON p.lineage = l.pangolin_lineage "
				 		+ "INNER   JOIN sample_master s ON s.sample_id=p.sample_id "
				 		+ "WHERE s.sample_col_date between '"+past_from_date+"' and '"+past_to_date+"' "
				 		+ "GROUP BY pangolin_lineage,who_common_name,who_status "
				 		+ ")a "
				 		+ "GROUP BY who_common_name,who_status";
				 query_data_obj =jdbcTemplate.queryForList(qry);
				 data_obj.put("third_set",query_data_obj);
			qry="SELECT who_common_name,GROUP_CONCAT(DISTINCT \" \",pangolin_lineage) AS lin, "
					+ "	who_status "
					+ "	FROM lineage_master l "
					+ "GROUP BY who_common_name,who_status";
			query_data_obj =jdbcTemplate.queryForList(qry);
			 data_obj.put("base_data",query_data_obj);
		}
		catch (Exception ex) 
		{
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		}
		result.SetSuccess("success", data_obj);
		 return gson.toJson(result);
	}
	
	@RequestMapping("GetVarientAreaChart")
	public @ResponseBody String GetVarientAreaChart (HttpServletRequest request ,
			
			@RequestParam("from_date")  String from_date,	
			@RequestParam("to_date")  String to_date,	
			@RequestParam("lin_type")  String lin_type,	
			@RequestParam("common_name")  String common_name,	
			@RequestParam("lineage_type")  String lineage_type 
			 ) throws SQLException, ClassNotFoundException
			{
		String return_html = "";
		String error = "0";
		JSONObject return_obj = new JSONObject();
		  ResponseDataGeneric result=new ResponseDataGeneric();
		  List<Map<String,Object>> query_data_obj=null;
		  JSONObject data_obj = new JSONObject();
			Gson gson = new Gson();	
			int rowsCount=0;
		 
			return_html = "";
			return_obj = new JSONObject(); 
			 String qry="";
		try
		{ 
			String select_field="";
			if(lin_type.equals("lineage"))
				select_field="p.lineage";
			else
				select_field="who_common_name";	
		// echo $lineage_cond; die();	
		  qry="SELECT aa,StartOfWeek AS wk1,GROUP_CONCAT(lineage,'~',cnt) AS data "
				+ "FROM ( SELECT  DATE(DATE_ADD(s.sample_col_date, INTERVAL(1-DAYOFWEEK(s.sample_col_date)) DAY)) AS aa, "
				+ "DATE_FORMAT(DATE(DATE_ADD(s.sample_col_date, INTERVAL(1-DAYOFWEEK(s.sample_col_date)) DAY)),'%d-%m-%Y') AS StartOfWeek, "
				+ ""+select_field+" as lineage,COUNT(DISTINCT s.sample_id) AS cnt  "
				+ "FROM sample_master s  "
				+ "INNER JOIN pangolin_data p ON p.sample_id=s.sample_id "
				+ "LEFT JOIN lineage_master l ON l.pangolin_lineage = p.lineage "
				+ "WHERE s.sample_col_date BETWEEN '"+from_date+"' AND '"+to_date+"' "
				+ "GROUP BY sample_col_date,aa,StartOfWeek,"+select_field+") a "
				+ "GROUP BY StartOfWeek,aa "
				+ "order by aa";
			 System.out.println("var serv area"+qry);
			 query_data_obj =jdbcTemplate.queryForList(qry);
			 data_obj.put("first_set",query_data_obj);
			 
			  
		}
		catch (Exception ex) 
		{
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		}
		result.SetSuccess("success", data_obj);
		 return gson.toJson(result);
	}
	 
}
