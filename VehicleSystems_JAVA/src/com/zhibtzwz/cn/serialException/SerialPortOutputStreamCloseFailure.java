package com.zhibtzwz.cn.serialException;
/** 
* @author  ZWZ 
* @version 2016��11��5�� ����6:42:38 
* @Function �����ڶ���������쳣
*/
public class SerialPortOutputStreamCloseFailure extends Exception{

	private static final long serialVersionUID = 1L;

	public SerialPortOutputStreamCloseFailure() {}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "�رմ��ڶ�����������OutputStream��ʱ����";
	}
}
