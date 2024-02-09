package com.strand.app.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.gson.Gson;


public  class Format {
	
	@Autowired
	static
	JdbcTemplate jdbcTemplate;
	
	public Format() {
		// TODO Auto-generated constructor stub
	}
	public static String GetLogOutErrorCode()
	{
		return "{\"error_code\":-9}";
	}
	
	public static String GetObjectValueFromMap(Object msg_obj)
	{
		 
		 String msg_str="";
		 if(msg_obj!=null)
			 msg_str=msg_obj.toString().trim().replace("'","''");
		return msg_str;
	}
	public static Map<String,String> GetMonthArrayList()
	{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);


		Calendar calendar = new GregorianCalendar(year,month,01);
		SimpleDateFormat sdf = new SimpleDateFormat("MMM");
		SimpleDateFormat year_month_fmt = new SimpleDateFormat("yyyyMM");
	    Map<String,String>  month_name_list = new LinkedHashMap<String,String>();
		for(int i=0;i<=month;i++)
		{
 
			Calendar calendar2 = new GregorianCalendar(year,i,01);
			String yyyymm=year_month_fmt.format(calendar2.getTime());
			String mmmyy =sdf.format(calendar2.getTime()) ;
			month_name_list.put(yyyymm,mmmyy);
		}
		return month_name_list; 
		
	}
	public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
	public static Map<String,String> GetFeedBackMonthList(String year,String month,String day) throws ParseException
	{
	  System.out.println("year--------"+year);
	  System.out.println("month--------"+month);
	  System.out.println("day--------"+day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 
	    Map<String,String>  month_name_list = new LinkedHashMap<String,String>();
	    String sourceDate = year+"-"+month+"-"+day;
	    
	    SimpleDateFormat year_month_fmt = new SimpleDateFormat("dd-MMM");
	    SimpleDateFormat year_month_fmt1 = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    Date myDate = format.parse(sourceDate);
		for(int i=30;i>=0;i--)
		{
			int m=i*-1;
			Date new_date = addDays(myDate, m);
			String mmmyy =year_month_fmt.format(new_date.getTime()) ;
			String mmmyy1 =year_month_fmt1.format(new_date.getTime()) ;
			month_name_list.put(mmmyy,mmmyy1);
		}
		return month_name_list; 
		
	}
	public static String GetCurrentMonthNameYear()//Apr-2016
	{
		 int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH);
		Calendar calendar = new GregorianCalendar(year,month,01);

		SimpleDateFormat year_month_fmt = new SimpleDateFormat("MMM-yyyy");
		String curr_month_year_txt=year_month_fmt.format(calendar.getTime());
		
		return curr_month_year_txt;
	}
	public static String GetCurrentMonthNameText()
	{
	Calendar cal=Calendar.getInstance();
	SimpleDateFormat month_date = new SimpleDateFormat("MMM");
	String month_name = month_date.format(cal.getTime());
	return month_name;
	}
	public static int getCurrentDaysInMonth()
	{
		
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		month++;
			
		int no_of_days_in_current_month = getDaysInMonths(month,year);
		return no_of_days_in_current_month;
	} 
	public static String getSessionSupervisor(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		System.out.println("Supervisor: "+session.getAttribute("supervisor"));
		String supervisor="";
		if(null != session && session.getAttribute("supervisor")!=null) 
		{
			supervisor=session.getAttribute("supervisor").toString();
		}
		System.out.println("Supervisor: "+session.getAttribute("supervisor"));
		return supervisor;
	}

	public static int getDaysInMonths(int month, int year)
	{
		month--;
		
		int Year = year;
		int Month = month; 
		

		// Create a calendar object and set year and month
		Calendar mycal = new GregorianCalendar(Year, Month, 1);
	
		// Get the number of days in that month
		int no_of_days = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28

		return no_of_days;
	}
	public static String SplitByCommaAndGetQueryValue(String split_txt,String seperator)
	{
		String[] div_array =split_txt.split(seperator);
		String div_code="";
		for(int i=0;i<div_array.length;i++)
		{
			div_code+="'"+div_array[i]+"',";
		}
		
		div_code=div_code.substring(0,div_code.length()-1);
		return div_code;
	}
	public static String GetSessonUserId(HttpServletRequest request)
	{
		HttpSession session = request.getSession(true);
		String userId=""; 
		if(null != session && session.getAttribute("s_loginId")!=null) 
		{
			userId=session.getAttribute("s_loginId").toString();
		}
		System.out.println("userId..."+userId);
		System.out.println("s_username..."+session.getAttribute("MY_SESSION_MESSAGES"));
		return userId;
		
	}
 
	public static String GetSessonUserType(HttpServletRequest request)
	{
		HttpSession session = request.getSession(true);
		String usertype="";
		if(null != session && session.getAttribute("s_usertype")!=null) 
		{
			usertype=session.getAttribute("s_usertype").toString();
		}
		return usertype;
		
	}
	public static String GetSessonUserBranch(HttpServletRequest request)
	{
		HttpSession session = request.getSession(true);
		String userbranch="";
		if(null != session && session.getAttribute("s_branch")!=null) 
		{
			userbranch=session.getAttribute("s_branch").toString();
		}
		return userbranch;
		
	}
	public static String GetSessonUserBranchIntCode(HttpServletRequest request)
	{
		HttpSession session = request.getSession(true);
		String user_branch_intcode="";
		if(null != session && session.getAttribute("s_branch_int_code")!=null) 
		{
			user_branch_intcode=session.getAttribute("s_branch_int_code").toString();
		}
		return user_branch_intcode;
		
	}
	
	public static String GetSessonUserIntCode(HttpServletRequest request)
	{
		HttpSession session = request.getSession(true);
		String login_user_int_code="0";
		if(null != session && session.getAttribute("s_loginId")!=null) 
		{
			login_user_int_code=session.getAttribute("s_loginId").toString();
		}
		return login_user_int_code;
		
	}
	
	
	public static String GetMMMYY(String str)//input 201606 return Jun-16
	{
		Map<String,String> month_names = new HashMap<String,String>();  
		month_names.put("01","Jan");
		month_names.put("02","Feb");
		month_names.put("03","Mar");
		month_names.put("04","Apr");
		month_names.put("05","May");
		month_names.put("06","Jun");
		month_names.put("07","Jul");
		month_names.put("08","Aug");
		month_names.put("09","Sep");
		month_names.put("10","Oct");
		month_names.put("11","Nov");
		month_names.put("12","Dec");
		 System.out.println(str);
		 String year=str.substring(0, 4);
		
		 String month=str.substring(3, 2);
		
		 System.out.println(year);
		 System.out.println(month);
		return month_names.get(month)+"-"+year;

	}
	public static String GetMonth(String str)//input 201606 return Jun-16
	{
		Map<String,String> month_names = new HashMap<String,String>();  
		month_names.put("Jan","01");
		month_names.put("Feb","02");
		month_names.put("Mar","03");
		month_names.put("Apr","04");
		month_names.put("May","05");
		month_names.put("Jun","06");
		month_names.put("Jul","07");
		month_names.put("Aug","08");
		month_names.put("Sep","09");
		month_names.put("Oct","10");
		month_names.put("Nov","11");
		month_names.put("Dec","12");
		  
		return month_names.get(str);

	}

	
	public static Double TarkaRoundDouble(Double num)
	  {
		  
		  DecimalFormat df = new DecimalFormat("#.##");
		  df.setRoundingMode(RoundingMode.CEILING);
		  Double d = num.doubleValue();
		 return Double.parseDouble(df.format(d));
		// Double number = (double) Math.round(num * 100);
		 // number = number/100;
		 // return number;
	  }

	public static String addSlashes(String s) {
	    s = s.replaceAll("\\\\", "\\\\\\\\");
	    s = s.replaceAll("\\n", "\\\\n");
	    s = s.replaceAll("\\r", "\\\\r");
	    s = s.replaceAll("\\00", "\\\\0");
	    s = s.replaceAll("'", "\\\\'");
	    return s;
	}
	 

	public static Double TarkaRoundDoubleToAnyFormat(Double num,String format)
	  {
		  
		  DecimalFormat df = new DecimalFormat(format);
		  df.setRoundingMode(RoundingMode.CEILING);
		  Double d = num.doubleValue();
		 return Double.parseDouble(df.format(d));
		 
	  }
	public static String RoundBigDecimaltoWholeNoWithPerc(BigDecimal val)
	{
		return val.setScale(0, BigDecimal.ROUND_HALF_UP).toString() ;
	}
	public static  Double TarkaMin(Double v1, double v2)
		{
			if(v1<v2)
				return v1;
			else return v2;
		
		}

	public static  int TarkaMin(int v1, int v2)
		{
			if(v1<v2)
				return v1;
			else return v2;
		
		}
	public static  Double TarkaMax(Double v1, double v2)
		{
			if(v1>v2)
				return v1;
			else return v2;
		
		}
	public static String getCurrentTimeStamp()
	{
		
		 java.util.Date date= new java.util.Date();
		 return  (new Timestamp(date.getTime())).toString();
		
	}
	public static String GetCurrentYearMonth()
	{
		String year_month="";
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		String month_txt=("0"+month);
		
		year_month=year+month_txt.substring(month_txt.length() - 2);;
		return year_month;
	}
	public static String GetPreviousYearMonth()
	{
		String prev_year_month="";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		prev_year_month=format.format(cal.getTime());
		return prev_year_month;
	 
		
	}
	public static String GetPreviousYearMonthText()
	{
		String prev_year_month_txt="";
		SimpleDateFormat format = new SimpleDateFormat("MMM - yy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
	 
		prev_year_month_txt=format.format(cal.getTime());
		return prev_year_month_txt;
	 
		
	}
	public static String GetUserIntForEnlId( String fw_user_code) 
	{
		String internal_code="";
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();

		try
			{	
				String Sql = "select internal_code as internal_code from ia_user_master where login_id = '"+fw_user_code+"' ";
				System.out.println("  GetUserIntForEnlId ="+Sql);
			

					query_data_obj=jdbcTemplate.queryForList(Sql);  
					for(Map map2:query_data_obj)
					{
						internal_code=(String) map2.get("internal_code");
					}
								
			}
			catch (Exception ex) {
				ex.printStackTrace();
				Throwable cause = ex.getCause();
				if (cause instanceof SQLException) { }
			
			}
	
		
		
		return internal_code;
		
		
	}
		
}
