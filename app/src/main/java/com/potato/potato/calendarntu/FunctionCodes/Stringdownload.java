package com.potato.potato.calendarntu.FunctionCodes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Stringdownload {
	public String getstring(String course){
		try {
			//Catch the exception and give a popwindow
			String coursecode = course;
			String urlstr = "https://wish.wis.ntu.edu.sg/webexe/owa/aus_schedule.main_display1?r_search_type=F&boption=Search&staff_access=false&acadsem=2015;2&r_subj_code=" + coursecode;
			URL url = new URL(urlstr);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			InputStream in = conn.getInputStream();
			String str = null;
			String str1 = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while ((str = br.readLine()) != null) {
				str1 = str1 + str;
			}
			//System.out.println(str1);
			System.out.println(str1);
			int b = str1.indexOf("<TR><TH>");
			int a = str1.lastIndexOf("</TR>");
			str1 = str1.substring(b, a);
			str1 = "<HTML>" + str1 + "</TR></HTML>";

			return str1;
		}catch (Exception e){
			System.out.println("Reading error:"+e);
			return null;
		}
	
	}
}