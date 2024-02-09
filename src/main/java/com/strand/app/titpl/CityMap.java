package com.strand.app.titpl;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.strand.app.config.ResponseDataGeneric;
@RestController
@RequestMapping("titpl/CityMap")
public class CityMap {

	private static final int String = 0;
	
	static Logger logger = Logger.getLogger(CityMap.class);   
	
	String loginId="";
	String calling_table_name="";
	String pop_up_table_name="";
	String system_name="";
	String value="";
	
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	ResponseDataGeneric result;
	
	public CityMap() {
	// TODO Auto-generated constructor stub
		SessionFactory factory;  
	}
	@RequestMapping("GetCityMap")
	public @ResponseBody String GetCityMap (HttpServletRequest request,
			@RequestParam("y_type")  String y_type,	
			@RequestParam("x_type")  String x_type,
			@RequestParam("from_date")  String from_date,
			@RequestParam("run_date")  String run_date,
			@RequestParam("to_date")  String to_date,
			@RequestParam("work_place")  String work_place,
			@RequestParam("hospital_admission")  String hospital_admission,
			@RequestParam("icu_admission")  String icu_admission,
			@RequestParam("infected_after_vaccine")  String infected_after_vaccine,
			@RequestParam("gender")  String gender,
			@RequestParam("vaccine_status")  String vaccine_status,
			@RequestParam("vaccine")  String vaccine,
			@RequestParam("age_group")  String age_group,
			@RequestParam("mucormycosis")  String mucormycosis,
			@RequestParam("state")  String state,
			@RequestParam("district")  String district,
			@RequestParam("city")  String city,
			@RequestParam("location")  String location,
			@RequestParam("re_infection")  String re_infection,
			@RequestParam("common_name")  String common_name,
			@RequestParam("lineage_type")  String lineage_type,
			@RequestParam("lin_type")  String lin_type,
			@RequestParam("combor")  String combor,
			@RequestParam("source")  String source,
			 
			@RequestParam("company")  String company ,
			@RequestParam("hospital")  String hospital,
			@RequestParam("substitution_val")  String substitution_val,
			@RequestParam("position_val")  String position_val,
			@RequestParam("min_occ")  String min_occ,
			@RequestParam("gene")  String gene,
			@RequestParam("due_days")  String due_days
			 
			
			 ) throws SQLException, ClassNotFoundException
			{
		String return_html = "";
		String error = "0";
		JSONObject return_obj = new JSONObject();
		  ResponseDataGeneric result=new ResponseDataGeneric();
		  JSONObject data_obj = new JSONObject();
			Gson gson = new Gson();	
			int rowsCount=0;
			//System.out.println("login_user_name---");
			   return_html = "";
			   return_obj = new JSONObject(); 
			   List<Map<String,Object>> query_data_obj=null;
			   
		try
		{
			String company_cond="";
			System.out.println("gender------"+gender);
			
	   		if(!company.equals("''"))
	   		 {
		    	  String e_qry=" select IFNULL(GROUP_CONCAT(internal_code),0) as hosp_int_codes from "
		    	  		+ "		hospital_master where company="+company;
		    	  query_data_obj=jdbcTemplate.queryForList(e_qry); 
		    	  for (Map<String, Object> map : query_data_obj)
				  {
		    		  String hosp_int_codes= map.get("hosp_int_codes").toString().trim();
		    		  company_cond="and s.hospital in ("+hosp_int_codes+")";
				  }
		    			    		  
		    					
	   		 }
	   		System.out.println("gender------"+gender);
			
	   		String hospital_cond="";
	   		if(!hospital.equals("''"))
	   		 {
	   			hospital_cond="and s.hospital in ("+hospital+")";			
	   		 }
	   	
	   		System.out.println("hospital_cond------"+hospital_cond);
	   		String state_cond="";
			if(!state.equals("0"))
				state_cond="and s.state = "+state+"";
			
			String district_cond="";
			if(!district.equals("0"))
				district_cond="and s.district = "+district+"";
			
			String city_cond="";
			if(!city.equals("0"))
				city_cond="and s.city = "+city+"";
			
			String location_cond="";
			if(!location.equals("0"))
			{
				if(location=="Unknown")
					location_cond="and ifnull(s.location,'') = ''";
				else
					location_cond="and s.location = '"+location+"'";
			}
			age_group=age_group.substring(0, age_group.length() - 1);  
			String[] age_arr = age_group.split(",");
			System.out.println("age_arr"+age_arr);
			String age_grp_cond="";
			
			for(int i=0;i<age_arr.length;i++)
			{
				if(!age_arr[i].equals("0"))
				{
					String age_int_code=age_arr[i].substring(4,1);
					if(age_grp_cond.equals(""))	
						age_grp_cond="and (a.internal_code = '"+age_int_code+"'";
					else
						age_grp_cond+=" or a.internal_code = '"+age_int_code+"'";
				}
			}
			System.out.println("age_grp_cond------"+age_grp_cond);
			
			if(!age_grp_cond.equals(""))
				age_grp_cond+=")";
			gender = gender.replaceAll("\\s+", "");
			gender=gender.substring(0, gender.length() - 1);  
			String[] gender_arr = gender.split(",");
			 
			String gender_cond="";
			 
			for(int i=0;i<gender_arr.length;i++)
			{
				//System.out.println("gender_arr[i]------"+gender_arr[i]);
				if(!gender_arr[i].toString().equals("0"))
				{
					//System.out.println("after if cond gender_arr[i]------"+gender_arr[i]);
					if(gender_cond.equals(""))	
						gender_cond="and (s.ender = '"+gender_arr[i]+"'";
					else
						gender_cond+=" or s.gender = '"+gender_arr[i]+"'";
				}
			}
			if(!gender_cond.equals(""))
				gender_cond+=")";
			  
			System.out.println("gender_cond------"+gender_cond);
			vaccine=vaccine.substring(0, vaccine.length() - 1);  
			String[] vaccine_arr = vaccine.split(",");
			 
			String vaccine_cond="";
			 
			for(int i=0;i<vaccine_arr.length;i++)
			{
				if(!vaccine_arr[i].equals("0"))
				{
					String vac_int_code=vaccine_arr[i].substring(4,1);
					if(vaccine_cond.equals(""))	
						vaccine_cond="and (s.vaccine_1 = "+vac_int_code+" or s.vaccine_2 = "+vac_int_code+"";
					else
						vaccine_cond+=" or  (s.vaccine_1 = "+vac_int_code+" or s.vaccine_2 = "+vac_int_code+"";
				}
			}
			if(!vaccine_cond.equals(""))
				vaccine_cond+=")";
			
			String vac_status_cond=""; 
			if(!vaccine_status.equals(""))
			{
				vaccine_status=vaccine_status.substring(0, vaccine_status.length() - 1);  
				String[] vaccine_status_arr = vaccine_status.split(",");
				if(vaccine_status_arr[0].equals("first_dose"))
					vac_status_cond+=" and ifnull(s.vaccine_1_date,'0000-00-00')!='0000-00-00'";
				if(vaccine_status_arr[1].equals("sec_dose"))
					vac_status_cond+=" and ifnull(s.vaccine_2_date,'0000-00-00')!='0000-00-00'";
			}
			String hosp_adm_cond="";
			if(!hospital_admission.equals(""))
			{ 
				hospital_admission=hospital_admission.substring(0, hospital_admission.length() - 1);
				String[] hospital_admission_arr = hospital_admission.split(",");
				if(hospital_admission_arr.equals("h_ad_yes") && hospital_admission_arr.equals("h_ad_no"))
					hosp_adm_cond="";
				else if(hospital_admission_arr.equals("h_ad_yes"))
					hosp_adm_cond="and ifnull(s.admission_date,'0000-00-00')!='0000-00-00'";
				else if(hospital_admission_arr.equals("h_ad_no"))
					hosp_adm_cond="and ifnull(s.admission_date,'0000-00-00')='0000-00-00'";
			}
			
			String icu_adm_cond="";
			if(!icu_admission.equals(""))
			{
				icu_admission=icu_admission.substring(0, icu_admission.length() - 1);
				String[] icu_admission_arr = icu_admission.split(",");
				
				if(icu_admission_arr[0].equals("i_ad_yes") && icu_admission_arr[1].equals("i_ad_no"))
					icu_adm_cond="";
				else if(icu_admission_arr[0].equals("i_ad_yes"))
					icu_adm_cond="and ifnull(s.icu_date,'0000-00-00')!='0000-00-00'";
				else if(icu_admission_arr[1].equals("i_ad_no"))
					icu_adm_cond="and ifnull(s.icu_date,'0000-00-00')='0000-00-00'";
			}
			String afr_covid_cond="";
			if(!infected_after_vaccine.equals(""))
			{
				infected_after_vaccine=infected_after_vaccine.substring(0, infected_after_vaccine.length() - 1);
				String[] infected_after_vaccine_arr = infected_after_vaccine.split(",");
				 
				if(infected_after_vaccine_arr[0].equals("afr_yes") && infected_after_vaccine_arr[1].equals("afr_no"))
					afr_covid_cond="";
				else if(infected_after_vaccine_arr[0].equals("afr_yes"))
					afr_covid_cond="and (ifnull(s.vaccine_1_date,'0000-00-00')!='0000-00-00' or ifnull(s.vaccine_2_date,'0000-00-00')!='0000-00-00')";
				else if(infected_after_vaccine_arr[1].equals("afr_no"))
					afr_covid_cond="and (ifnull(s.vaccine_1_date,'0000-00-00')!='0000-00-00' and  ifnull(s.vaccine_2_date,'0000-00-00')!='0000-00-00')";
			}
			String muc_cond="";
			if(!mucormycosis.equals(""))
			{
				mucormycosis=mucormycosis.substring(0, mucormycosis.length() - 1);
				String[] mucormycosis_arr = mucormycosis.split(",");
			 
				if(mucormycosis_arr[0].equals("m_yes") &&  mucormycosis_arr[1].equals("m_no"))
					muc_cond="";
				else if(mucormycosis_arr[0].equals("m_yes"))
					muc_cond="and ifnull(s.mucormycosis,'0000-00-00')!='0000-00-00'";
				else if(mucormycosis_arr[1].equals("m_no"))
					muc_cond="and ifnull(s.mucormycosis,'0000-00-00')='0000-00-00'";
			}
			String re_inf_cond="";
			if(!re_infection.equals(""))
			{
				re_infection=re_infection.substring(0, re_infection.length() - 1);
				String[] re_infection_arr = re_infection.split(",");
				  
				if(re_infection_arr[0].equals("e_inf_yes") && re_infection_arr[1].equals("re_inf_no"))
					re_inf_cond="";
				else if(re_infection_arr[0].equals("re_inf_yes"))
					re_inf_cond="and ifnull(s.reinfection,'No')='Yes'";
				else if(re_infection_arr[1].equals("re_inf_no"))
					re_inf_cond="and ifnull(s.reinfection,'No')='No'";
			}
			String wp_cond="";
			if(!work_place.equals(""))
			{
				work_place=work_place.substring(0, work_place.length() - 1);
				String[] work_place_arr = work_place.split(",");
				 
				if(work_place_arr[0].equals("Home") && work_place_arr[1].equals("Office"))
					wp_cond="";
				else if(work_place_arr[0].equals("Home"))
					wp_cond="and ifnull(s.workplace,'')='Home'";
				else if(work_place_arr[1].equals("Office"))
					wp_cond="and ifnull(s.workplace,'')='Office'";
			}
			String comp_cond="";
			if(!combor.equals(""))
			{
				work_place=combor.substring(0, combor.length() - 1);
				String[] combor_arr = combor.split(",");
				
				 
				for(int i=0;i<combor_arr.length;i++)
				{
					if(!combor_arr[i].equals("0"))
					{
						//String com_int_code=combor_arr[i].substring(4,1);
						//if(comp_cond.equals(""))	
						//	comp_cond="( com_name = '"+com_int_code+"'";
						//else
							//comp_cond+=" and  com_name = '"+com_int_code+"'";
					}
				}
			}
			String com_string=GetComName(common_name,lin_type);
			String lin_string=GetLinName(lineage_type);
			String lineage_cond="";
			String common_name_cond="";
			if(!com_string.equals(""))
				common_name_cond="and "+com_string+"";
			else
				common_name_cond="";
			if(!lin_string.equals(""))
				lineage_cond="and "+lin_string+"";
			else
				lineage_cond="";
			
			String date_cond="";
			String hosp_cond="";
			String vac_cond="";
			String user_date_cond="";
			if(run_date.equals("0000-00-00"))
				date_cond="and (s.sample_col_date between '"+from_date+"' and '"+to_date+"')";
			else 
				date_cond="and (s.run_date = '"+run_date+"')";
			if(!due_days.equals("0"))
				user_date_cond=" AND s.sample_col_date < (SELECT DATE_SUB(CURRENT_DATE(), INTERVAL "+due_days+" DAY)) ";
			else
				user_date_cond="";
			String common_filer_cond=date_cond+" "+user_date_cond+" "+company_cond+" "+hospital_cond+" "+hosp_cond+" "+
						wp_cond+""+hosp_adm_cond+" "+icu_adm_cond+" "+afr_covid_cond+""+muc_cond+""+re_inf_cond+""+wp_cond+""+
					age_grp_cond+""+gender_cond+" "+vac_cond+" "+vac_status_cond+" "+ 
						state_cond+" "+district_cond+" "+city_cond+" "+location_cond+" "+
					lineage_cond+" "+common_name_cond;
			String qry="";
			if(source.equals("mutation"))
			{
				String gene_cond="";
				/*if(gene.equals("All"))
					gene_cond="";
				else
					gene_cond="AND nc.gene="+gene+"'";*/
				String pos_cond="";
				if(!position_val.equals(""))
					pos_cond="and nc.gene_pos in('"+position_val+"')";
				String sub_cond="";
				if(!substitution_val.equals(""))
					sub_cond="and nc.substitution in('"+substitution_val+"')";
				
				 qry="SELECT ll.name,ll.map_id,IFNULL(aaa.cnt,'None~0') AS cnt,IFNULL(aaa.cntt,0) AS cntt FROM location_master ll  "
				 		+ "left join ( SELECT a.name,a.map_id,ifnull(GROUP_CONCAT(a.substitution,'~',a.cnt ORDER BY a.cnt DESC),'None~0') AS cnt, "
				 		+ "SUM(a.cnt) AS cntt FROM "
						+ "	(SELECT l.name,l.map_id,substitution,IFNULL(COUNT(nc.internal_code),0) AS cnt  "
						+ "FROM location_master l "
						+ "LEFT JOIN sample_master s ON s.location=l.internal_code "
						+ "LEFT JOIN next_clade_data p ON p.sample_id=s.sample_id  "
						+ "LEFT JOIN next_clade_data_substitution_group nc ON nc.ref_s_ref_data_code = p.internal_code "
						+ "	WHERE 1=1 "+common_filer_cond+" "+gene_cond+" "+pos_cond+" "+sub_cond+" "
						+ "	GROUP BY l.name,l.map_id,substitution) a GROUP BY a.name,a.map_id "
						+ ") aaa ON aaa.map_id=ll.map_id";
			}
			else
			{
				 qry="SELECT ll.name,ll.map_id,IFNULL(aaa.cnt,'None~0') AS cnt,IFNULL(aaa.cntt,0) AS cntt FROM location_master ll  "
					+ "LEFT JOIN( "
					+ "SELECT a.name,a.map_id, IFNULL(GROUP_CONCAT(a.lineage,'~',a.cnt ORDER BY a.cnt DESC),'None~0') "
					+ "	 AS cnt, SUM(a.cnt) AS cntt FROM "
					+ "(SELECT l.name,l.map_id,lineage,IFNULL(COUNT(s.internal_code),0) AS cnt   "
					+ "	FROM location_master l "
					+ "LEFT JOIN sample_master s ON s.location=l.internal_code "
					+ "LEFT JOIN pangolin_data p ON p.sample_id=s.sample_id "
					+ "  where 1=1 "+common_filer_cond+"  "
					+ "GROUP BY l.name,l.map_id,lineage) a GROUP BY a.name,a.map_id "
					+ ") aaa ON aaa.map_id=ll.map_id";
			}
			 System.out.println("map qry dvfv"+qry);
			 query_data_obj =jdbcTemplate.queryForList(qry);
			 data_obj.put("data",query_data_obj);
			 qry="SELECT map_id,name FROM location_master order by map_id";
			 query_data_obj =jdbcTemplate.queryForList(qry);
			 data_obj.put("location_details",query_data_obj);
			 
			}
			catch (Exception ex) 
			{
				Throwable cause = ex.getCause();
				if (cause instanceof SQLException) { }
			}
		 
		result.SetSuccess("success", data_obj);
		 return gson.toJson(result);
	}
	public String GetComName(String com_name,String lin_type)
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		String com_cond="";
		String com_string="";
		com_name=com_name.substring(0, com_name.length() - 1);
		if(lin_type.equals("lineage"))
			com_string =  com_name.replaceAll("common_name_radio_","");
		else
			com_string =  com_name.replaceAll("common_name_","");
		com_string="'"+com_string.replaceAll(",","','")+"'";
		System.out.println("com_string--"+com_string);
		String qry="select ifnull(group_concat(QUOTE(pangolin_lineage)),'') as lineage "
				+ "from lineage_master where who_common_name in ("+com_string+")";
		System.out.println("com_string--"+qry);
		query_data_obj=jdbcTemplate.queryForList(qry);
		  for (Map<String, Object> map : query_data_obj)
		    {
			  String lin=map.get("lineage").toString();
			  if(!lin.equals(""))
				  com_cond="(lineage = "+lin.replaceAll(","," OR lineage = ")+")";
		    }
		  System.out.println("com_string--"+com_cond);
	//	if(!empty($lobj_get_lin_mame['lineage']))
			//$com_cond="(lineage = ".str_replace(","," OR lineage = ",$lobj_get_lin_mame['lineage']).")";
		return com_cond;
	}
	public String GetLinName(String lineage_type)
	{
		List<Map<String,Object>> query_data_obj= new ArrayList<Map<String,Object>>();;
		String lin_cond="";
		if(!lineage_type.equals(""))
		{
			lineage_type=lineage_type.substring(0, lineage_type.length() - 1);
			lineage_type =  lineage_type.replaceAll("lin_","");
			System.out.println("lineage_type--"+lineage_type);
			//com_name=com_name.substring(0, com_name.length() - 1);
			String qry="select ifnull(group_concat(QUOTE(pangolin_lineage)),'') as lineage \r\n"
					+ " from lineage_master where internal_code in ("+lineage_type+")";
			System.out.println("com_string--"+qry);
			query_data_obj=jdbcTemplate.queryForList(qry);
			  for (Map<String, Object> map : query_data_obj)
			    {
				  String lin=map.get("lineage").toString();
				  if(!lin.equals(""))
					  lin_cond="(lineage = "+lin.replaceAll(","," OR lineage = ")+")";
			    }
			  System.out.println("lin string--"+lin_cond); 
		}
		return lin_cond;
	}
	 
}
