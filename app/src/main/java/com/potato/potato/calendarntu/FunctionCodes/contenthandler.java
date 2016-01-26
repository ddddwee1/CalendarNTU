package com.potato.potato.calendarntu.FunctionCodes;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class contenthandler extends DefaultHandler {
	int i,swi,buffer;
	Index inf;
	functions func=new functions();
	int dayy=0;
	ArrayList<Index> list = new ArrayList<Index>();
	boolean cando = false;
	Section sct;
	ArrayList<Section> sctlist;
    String exceptionHandleString = "";
	
	//The return type is a list of info, in other words, it is the list in "course" class
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch,start,length);
        //System.out.println(s);
		if (i==0 && length!=0){
			if(cando){
				list.add(inf);
			}
			cando = true;
			inf = new Index();
			sctlist = new ArrayList<Section>();
			String ind = new String(ch,start,length);
			inf.setIndex(ind);
			inf.setSctlist(sctlist);
			//System.out.println(ind);
		}
		if(i==1&&length!=0){
				sct = new Section();
				sct.setWeeks(new ArrayList<String>());
				// 1:lec  2:tut  3:lab
				sct.setStrtype(s);
				if (s.contains("TUT")) {
					sct.setType(2);
				} else if (s.contains("LAB")) {
					sct.setType(3);
				} else if (s.contains("LEC")) {
					sct.setType(1);
					sct.setStrtype("LEC");
				} else {
					sct.setType(0);
				}
			//System.out.println(s+"   type:"+sct.getType());
		}
		if (i==3 && length!=0){
				switch (s) {
					case "MON":
						dayy = 0;
						break;
					case "TUE":
						dayy = 1;
						break;
					case "WED":
						dayy = 2;
						break;
					case "THU":
						dayy = 3;
						break;
					case "FRI":
						dayy = 4;
						break;
				}
		}
		if (i==4 && length!=0){
            try {
                if (!exceptionHandleString.equals("")){
                    s = exceptionHandleString + s;
                    exceptionHandleString = "";
                }
                buffer = s.indexOf("-");
                //System.out.println(s);
                String str1 = s.substring(0, 2);
                String str11 = s.substring(2, buffer);
                String str2 = s.substring(buffer + 1, buffer + 3);
                String str22 = s.substring(buffer + 3);
                int bufa = Integer.parseInt(str1);
                int bufaa = Integer.parseInt(str11);
                bufaa = bufaa / 30;
                int bufb = Integer.parseInt(str2);
                int bufbb = Integer.parseInt(str22);
                bufbb = bufbb / 30;
                bufa = (bufa - 8) * 2 - 1 + bufaa + (100 * dayy);
                bufb = (bufb - 8) * 2 - 1 + bufbb + (100 * dayy);
                sct.setStarttime(bufa);
                sct.setEndtime(bufb);
                //System.out.println("start:"+bufa+"\nend:"+bufb);
                sctlist.add(sct);
            }catch (StringIndexOutOfBoundsException e){
                if (e instanceof StringIndexOutOfBoundsException){
                    exceptionHandleString = s;
                }else{
                    System.out.println("i=4 error");
                }
            }
		}
		if (i==5 && length!=0){
			//Place of this section
			//System.out.println("5: "+s);
			sct.setPlace(s);
		}
		if (i==6 && length!=0){
		        //Get week
				sct.setStrWeeks(s);
				s = s.replace("Wk", "");
				String[] buf = s.split(",");
				ArrayList<String> buffer = new ArrayList<String>();
				for (int i = 0; i < buf.length; i++) {
					//System.out.println("buffer: "+buf[i]);
					if (buf[i].contains("-")) {
						String[] aa = buf[i].split("-");
						int start1 = Integer.valueOf(aa[0]);
						int end1 = Integer.valueOf(aa[1]);
						for (int j = start1; j <= end1; j++) {
							buffer.add(String.valueOf(j));
						}
					} else {
						buffer.add(buf[i]);
					}
				}

            sct.setWeeks(buffer);
			for (int i=0;i<sctlist.size();i++){
                Section bufff = sctlist.get(i);
                if (bufff!=sct && bufff.getType()==sct.getType() && bufff.getStarttime()==sct.getStarttime() && bufff.getEndtime()==sct.getEndtime() /*&& bufff.getPlace().equals(sct.getPlace())*/){
                    //System.out.println("combine scts");
                    bufff.getWeeks().addAll(sct.getWeeks());
                    bufff.setStrWeeks(bufff.getStrWeeks()+","+sct.getStrWeeks().replace("Wk",""));
                    sctlist.remove(sct);
                    break;
                }
            }
			//System.out.println("6: "+s);
		}
	}

	public ArrayList<Index> getList() {
		return list;
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		if(cando){
			list.add(inf);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName=="TR"){
			i=-1;
		}
		if (qName=="TD"){
			swi=1;
		}
		if (qName=="B"&&swi==1){
			swi=0;
			i++;
		}
	}
}
