package com.zhibtzwz.cn.serialException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/** 
* @author  ZWZ 
* @version 2016��11��5�� ����6:33:41 
* @Function ���������Ϣ��װ�ַ������쳣
*/
public class ExceptionWriter {

	/**
	 * ��Exception�еĴ�����Ϣ��װ���ַ����в����ظ��ַ���
	 * @param e ���������Exception
	 * @return ������Ϣ�ַ���
	 */
	public static String getErrorInfoFromException(Exception e) { 
    	
    	StringWriter sw = null;
    	PrintWriter pw = null;
    	
        try {  
            sw = new StringWriter();  
            pw = new PrintWriter(sw);  
            e.printStackTrace(pw);  
            return "\r\n" + sw.toString() + "\r\n";  
            
        } catch (Exception e2) {  
            return "��������δ��ȡ��������Ϣ�����������!";  
        } finally {
        	try {
            	if (pw != null) {
            		pw.close();
            	}
            	if (sw != null) {
    				sw.close();
            	}
        	} catch (IOException e1) {
        		e1.printStackTrace();
        	}
        }
    }
}
