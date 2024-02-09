package com.strand.app.system;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.google.gson.Gson;
import com.strand.app.config.Format;
import com.strand.app.config.ResponseDataGeneric;

public class CodeGenerator {
	
	static Logger logger = Logger.getLogger(CodeGenerator.class);   
	@Value("${app.schema:default}")
	String schema;
	
	@Value("${posSchema:default}")
	String posSchema;
	 
	@Autowired 
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	public CodeGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	private String GenerateLeadID(String module_name, String prefix, String seperator, String padding ) throws SQLException, ClassNotFoundException
	{
		String code = "";
		String last_number = "0";
		String auto_gen_number = "0"; 
		try
		{
			
			List<Map<String,Object>> query_code_obj= new ArrayList<Map<String,Object>>();
			query_code_obj=GenerateAutoLeadID(module_name,prefix,seperator,padding);
			 
			//if(query_code_obj.size() > 0)
			System.out.println("a");
			if(!query_code_obj.isEmpty())	
			{
				System.out.println("q");
				code = query_code_obj.get(0).get("code").toString();
				System.out.println("code-------"+code.trim());
				code = code.trim();
			}
			System.out.println("code : "+code);
			if(code.isEmpty() || code.equals(""))
			{
				System.out.println("a2");
				InsertIntoAutoGeneratorLeadID(module_name,prefix,seperator,padding);
				
				query_code_obj=GenerateAutoLeadID(module_name,prefix,seperator,padding);
				if(query_code_obj.size() > 0)
				{
					code = query_code_obj.get(0).get("code").toString();
					System.out.println("code-------"+code.trim());
					code = code.trim();
				}
			}
			
			UpdateGeneratedLeadID(module_name,prefix,seperator,padding);
			
			if(padding.isEmpty())
			{
				padding = "0";
			}
			
			last_number = str_pad(code, 4, padding,"STR_PAD_LEFT");
			
			System.out.println("last_number-----------"+last_number); 
			/*if(seperator.isEmpty())
			{
				seperator = "-";
			}*/
			
			auto_gen_number = prefix+seperator+last_number;
			
			System.out.println("auto_gen_number-----------"+auto_gen_number);
		}
		catch (Exception ex) {
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		}

		return auto_gen_number;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> GenerateAutoLeadID( String module_name, String prefix, String seperator, String padding) 
	{
		JSONObject data_obj = new JSONObject();
		List<Map<String,Object>> DataList=null;
		Gson gson = new Gson();	
		try
		{
			String sql = " select"
					  +" last_number+1 as code"
					  +" FROM crm_lead_id_generator"
					  +" WHERE prefix='"+prefix+"' and module_id='"+module_name+"'";
		 
			System.out.println("-------------GenerateAutoLeadID Sql ="+sql);
 
			DataList=jdbcTemplate.queryForList(sql);

			System.out.println("1");
			System.out.println("DataList : "+DataList.toString());
			System.out.println("2");
		}
		catch (Exception e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return DataList;
	}
	
	public String InsertIntoAutoGeneratorLeadID(String module_name, String prefix, String seperator, String padding) throws SQLException, ClassNotFoundException
	{
		String return_obj = "1";
		
		String qry="";
		 try
			{
				//Transaction tx = session.beginTransaction();
				qry="INSERT INTO crm_lead_id_generator "
                   +"(internal_code,prefix,last_number,module_id)"
				   +" VALUES"
				   +"(0,'"+prefix+"',0,'"+module_name+"')";
				System.out.println("InsertIntoAutoGeneratorLeadID_qry------"+qry);
				jdbcTemplate.execute(qry);
			}
			catch (Exception ex) 
		 	{
				Throwable cause = ex.getCause();
				System.out.println("qqqqqqqqqqqqqqqqq------"+qry);
				if (cause instanceof SQLException) { }
			} 
		 return return_obj;
	}
	
	public  String UpdateGeneratedLeadID(String module_name, String prefix, String seperator, String padding ) throws SQLException, ClassNotFoundException
	{
		String update_gen_number = "0";
		
		try
		{
			UpdateGeneratedLeadIDDetails(module_name,prefix);
		}
		catch (Exception ex) {
	        Throwable cause = ex.getCause();
	        System.out.println("Error at UpdateGeneratedLeadID"+ex.getMessage());
	        if (cause instanceof SQLException) { }
		}

		return update_gen_number;
	}
	
	public String UpdateGeneratedLeadIDDetails(String module_name, String prefix) throws SQLException, ClassNotFoundException
	{
		String return_obj = "1";
		
		String qry="";
		 try
			{
//				Transaction tx = session.beginTransaction();
				qry="update crm_lead_id_generator"
						+ " set last_number = last_number+1"
						+ " where prefix = '"+prefix+"' and module_id = '"+module_name+"'";
				
				System.out.println("UpdateGeneratedLeadIDDetails------"+qry);
				jdbcTemplate.execute(qry);
			}
			catch (Exception ex) 
		 	{
				Throwable cause = ex.getCause();
				System.out.println("qqqqqqqqqqqqqqqqq------"+qry);
				if (cause instanceof SQLException) { }
			} 
		 return return_obj;
	}
	
	public static String str_pad(String input, int length, String pad, String sense)
	{
	   int resto_pad = length - input.length();
	   String padded = "";

	   if (resto_pad <= 0){ return input; }

	   if(sense.equals("STR_PAD_RIGHT"))
	   {
	       padded  = input;
	       padded += _fill_string(pad,resto_pad);
	   }
	   else if(sense.equals("STR_PAD_LEFT"))
	   {
	       padded  = _fill_string(pad, resto_pad);
	       padded += input;
	   }
	   else // STR_PAD_BOTH
	   {
	       int pad_left  = (int) Math.ceil(resto_pad/2);
	       int pad_right = resto_pad - pad_left;

	       padded  = _fill_string(pad, pad_left);
	       padded += input;
	       padded += _fill_string(pad, pad_right);
	   }
	   return padded;
	}
	
	protected static String _fill_string(String pad, int resto )
	{
	   boolean first = true;
	   String padded = "";

	   if (resto >= pad.length())
	   {
	      for (int i = resto; i >= 0; i = i - pad.length())
	      {
	          if (i  >= pad.length())
	          {
	              if (first){ padded = pad; } else { padded += pad; }
	          }
	          else
	          {
	              if (first){ padded = pad.substring(0, i); } else { padded += pad.substring(0, i); }
	          }
	          first = false;
	      }
	  }
	  else
	  {
	      padded = pad.substring(0,resto);
	  }
	  return padded;
	}

}
