package com.zhibtzwz.cn.serialException;
/** 
* @author  ZWZ 
* @version 2016��11��5�� ����6:37:03 
* @Function ����Ǵ��������쳣
*/
public class NotASerialPort extends Exception{

	private static final long serialVersionUID = 1L;

	public NotASerialPort() {}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "�˿�ָ���豸���Ǵ������ͣ��򿪴��ڲ���ʧ�ܣ�";
	}
}
