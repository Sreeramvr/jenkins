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
@RequestMapping("titpl/pangolin")
public class pangolin {

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
	
	public pangolin() {
	// TODO Auto-generated constructor stub
		SessionFactory factory;  
	}
	@RequestMapping("MoveFilesToFtp")
	public @ResponseBody String MoveFilesToFtp (MultipartHttpServletRequest request
			 
			
			 ) throws SQLException, ClassNotFoundException
			{
		String return_html = "";
		String error = "0";
		JSONObject return_obj = new JSONObject();
		  ResponseDataGeneric result=new ResponseDataGeneric();
			Gson gson = new Gson();	
			int rowsCount=0;
			System.out.println("schema---"+schema);
			 System.out.println("file_path---"+file_path);
			   return_html = "";
			   return_obj = new JSONObject(); 
			   
		try
		{ 
			return_html=ReadExcelData(request);
		}
		catch (Exception ex) 
		{
			Throwable cause = ex.getCause();
			if (cause instanceof SQLException) { }
		}
		return gson.toJson(result);
	}
	public String ReadExcelData(MultipartHttpServletRequest request) throws IOException, BiffException
	{
		Iterator<String> itr =  request.getFileNames();
		MultipartFile mpf = request.getFile(itr.next());
		InputStream uploadedStream = mpf.getInputStream();
		System.out.println("file name---"+mpf.getOriginalFilename());
		 
		// File targetFile = new File("src/main/webapp/upload_files/"+mpf.getOriginalFilename());
		 File targetFile = new File(file_path+mpf.getOriginalFilename());
		 System.out.println("Full file_path---"+file_path+mpf.getOriginalFilename());
		    OutputStream outStream = new FileOutputStream(targetFile);

		    byte[] buffer = new byte[8 * 1024];
		    int bytesRead;
		    while ((bytesRead = uploadedStream.read(buffer)) != -1) {
		        outStream.write(buffer, 0, bytesRead);
		    }
		    IOUtils.closeQuietly(uploadedStream);
		    IOUtils.closeQuietly(outStream);
		String return_html="sdfdsg";
		return return_html;
	}
	
	@RequestMapping("GetFileDetailsInFTP")
	public @ResponseBody String GetFileDetailsInFTP (HttpServletRequest request
			 
			
			 ) throws SQLException, ClassNotFoundException
			{
		String return_html = "";
		String error = "0";
		JSONObject return_obj = new JSONObject();
		final File folder = new File(file_path);
		 System.out.println("Full folder file_path---"+folder);
		  ResponseDataGeneric result=new ResponseDataGeneric();
			Gson gson = new Gson();	
			String html="";
			JSONObject data_obj = new JSONObject();
			int i=1;
			for (final File fileEntry : folder.listFiles()) {
		        
		            System.out.println(fileEntry.getName());

		            
		          
		           // html+="<h3>"+fileEntry.getName()+" was last modified: " +fileEntry.lastModified()+"</h3>";
		            html+="<h3>"+i+" "+fileEntry.getName()+"</h3>";
		             
		            i=i+1;
		    } 
			data_obj.put("files",html);
			   return_obj = new JSONObject(); 
			   
		try
		{
			 
			 
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
