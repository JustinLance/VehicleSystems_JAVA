package com.zhibtzwz.cn.serialException;
/** 
* @author  ZWZ 
* @version 2016��11��5�� ����6:44:07 
* @Function �����ڼ������������쳣
*/
public class TooManyListeners extends Exception{
	
	private static final long serialVersionUID = 1L;

	public TooManyListeners() {}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "���ڼ������������࣡��Ӳ���ʧ�ܣ�";
	}

}
