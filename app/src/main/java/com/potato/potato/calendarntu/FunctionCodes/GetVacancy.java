package com.potato.potato.calendarntu.FunctionCodes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class GetVacancy {
	
	public String getStr(String course) throws Exception{
		String urlstr = "https://wish.wis.ntu.edu.sg/webexe/owa/aus_vacancy.check_vacancy2?subj="+course; 
		URL url = new URL(urlstr);
		HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
		InputStreamReader rd = new InputStreamReader(conn.getInputStream());
		BufferedReader br = new BufferedReader(rd);
		
		String result="" , buff="";
		while ((buff = br.readLine())!=null){
			result += buff;
		}
		int start = result.indexOf("<TR BGCOLOR=\"YELLOW\">");
		int end = result.indexOf("</TABLE>");
		result = result.substring(start,end);
		result = result.replace(" BGCOLOR=\"YELLOW\"", "");
		result = result.replace("&nbsp;", "");
		result = result.replace("<TR>", "");
		result = result.replace("</TR>", "");
		
		return result;
	}
	
	public ArrayList<String> main(String course) throws Exception {
		String str = getStr(course);
		System.out.println(str);
		ArrayList<String> res = getindList(str,true);
		for(int i=0;i<res.size();i++){
			System.out.println(res.get(i));
		}
		return res;
	}
	
	public ArrayList<String> getindList(String str,boolean reverse){
		int counter = 0;
		ArrayList<String> result = new ArrayList<String>();
		Pattern ptn = Pattern.compile("<TD>[^<>]*</TD>");
		java.util.regex.Matcher mtc = ptn.matcher(str);
		while (mtc.find()){
			String matched = mtc.group();
			//System.out.println(matched);
			matched = matched.replace("<TD>", "");
			matched = matched.replace("</TD>", "");
			if(counter%8==0&&!matched.equals("")){
				result.add(matched);
			}
			if(!matched.equals("")&&counter%8==1&&(matched.equals("0")!=reverse)){
				System.out.println("delete:"+result.get(result.size()-1));
				result.remove(result.size()-1);
			}
			counter ++;
		}
		return result;
	}

}