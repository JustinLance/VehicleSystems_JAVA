package com.zhbitzwz.cn.MyView;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.zhbitzwz.cn.MyUtil.SerialTool;
import com.zhbitzwz.cn.StaticData.StaticData;
import com.zhibtzwz.cn.serialException.ExceptionWriter;
import com.zhibtzwz.cn.serialException.NoSuchPort;
import com.zhibtzwz.cn.serialException.NotASerialPort;
import com.zhibtzwz.cn.serialException.PortInUse;
import com.zhibtzwz.cn.serialException.ReadDataFromSerialPortFailure;
import com.zhibtzwz.cn.serialException.SerialPortInputStreamCloseFailure;
import com.zhibtzwz.cn.serialException.SerialPortParameterFailure;
import com.zhibtzwz.cn.serialException.TooManyListeners;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @author ZWZ
 * @version 2016��12��11�� ����2:31:44
 * @Function ���������ʾ��
 */
public class MyDataView extends Frame {

	MyClient myClient = null;

	private List<String> commList = null; // ������ö˿ں�
	private SerialPort serialPort = null; // ���洮�ڶ���

	JPanel jPanelMapView = new JPanel();
	Browser browser = new Browser();
	BrowserView browserView = new BrowserView(browser);

	private Font font = new Font("΢���ź�", Font.BOLD, 25);

	private Label tem = new Label("��������", Label.CENTER); // �¶�
	private Label hum = new Label("��������", Label.CENTER); // ʪ��

	private Choice commChoice = new Choice(); // ����ѡ��������
	private Choice bpsChoice = new Choice(); // ������ѡ��

	private Button openSerialButton = new Button("�򿪴���");

	Image offScreen = null; // �ػ�ʱ�Ļ���

	// ����window��icon
	Toolkit toolKit = getToolkit();
	Image icon = toolKit.getImage(MyDataView.class.getResource("computer.png"));

	public MyDataView(MyClient myClient) {
		this.myClient = myClient;
		commList = SerialTool.findPort(); // �����ʼ��ʱ��ɨ��һ����Ч����
	}

	/**
	 * ���˵�������ʾ�� ���Label����ť��������������¼�������
	 */
	public void dataFrame() {
		this.setBounds(StaticData.LOC_X, StaticData.LOC_Y, StaticData.WIDTH, StaticData.HEIGHT);
		this.setTitle("�������ն�ϵͳ");
		this.setIconImage(icon);
		this.setBackground(Color.white);
		this.setLayout(null);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (serialPort != null) {
					// �����˳�ʱ�رմ����ͷ���Դ
					SerialTool.closePort(serialPort);
				}
				System.exit(0);
			}
		});

		jPanelMapView.setBounds(10, 10, 800, 400);
		jPanelMapView.setBorder(BorderFactory.createEtchedBorder());
		jPanelMapView.setLayout(new BorderLayout(0, 0));
		add(jPanelMapView);
		jPanelMapView.add(browserView, BorderLayout.CENTER);
		browser.loadURL("http://uri.amap.com/marker");

		tem.setBounds(140, 480, 225, 50);
		tem.setBackground(Color.black);
		tem.setFont(font);
		tem.setForeground(Color.white);
		add(tem);

		hum.setBounds(520, 480, 225, 50);
		hum.setBackground(Color.black);
		hum.setFont(font);
		hum.setForeground(Color.white);
		add(hum);

		// ��Ӵ���ѡ��ѡ��
		commChoice.setBounds(160, 536, 200, 200);
		// ����Ƿ��п��ô��ڣ��������ѡ����
		if (commList == null || commList.size() < 1) {
			JOptionPane.showMessageDialog(null, "û����������Ч���ڣ�", "����", JOptionPane.INFORMATION_MESSAGE);
		} else {
			for (String s : commList) {
				commChoice.add(s);
			}
		}
		add(commChoice);

		// ��Ӳ�����ѡ��
		bpsChoice.setBounds(526, 536, 200, 200);
		bpsChoice.add("1200");
		bpsChoice.add("2400");
		bpsChoice.add("4800");
		bpsChoice.add("9600");
		bpsChoice.add("14400");
		bpsChoice.add("19200");
		bpsChoice.add("115200");
		add(bpsChoice);

		// ��Ӵ򿪴��ڰ�ť
		openSerialButton.setBounds(250, 590, 300, 50);
		openSerialButton.setBackground(Color.lightGray);
		openSerialButton.setFont(new Font("΢���ź�", Font.BOLD, 20));
		openSerialButton.setForeground(Color.darkGray);
		add(openSerialButton);
		// ��Ӵ򿪴��ڰ�ť���¼�����
		openSerialButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ��ȡ��������
				String commName = commChoice.getSelectedItem();
				// ��ȡ������
				String bpsStr = bpsChoice.getSelectedItem();

				// ��鴮�������Ƿ��ȡ��ȷ
				if (commName == null || commName.equals("")) {
					JOptionPane.showMessageDialog(null, "û����������Ч���ڣ�", "����", JOptionPane.INFORMATION_MESSAGE);
				} else {
					// ��鲨�����Ƿ��ȡ��ȷ
					if (bpsStr == null || bpsStr.equals("")) {
						JOptionPane.showMessageDialog(null, "�����ʻ�ȡ����", "����", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// �������������ʾ���ȡ��ȷʱ
						int bps = Integer.parseInt(bpsStr);
						try {

							// ��ȡָ���˿����������ʵĴ��ڶ���
							serialPort = SerialTool.openPort(commName, bps);
							// �ڸô��ڶ�������Ӽ�����
							SerialTool.addListener(serialPort, new SerialListener());
							// �����ɹ�������ʾ
							JOptionPane.showMessageDialog(null, "�����ɹ����Ժ���ʾ������ݣ�", "��ʾ",
									JOptionPane.INFORMATION_MESSAGE);

						} catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort | PortInUse
								| TooManyListeners e1) {
							// ��������ʱʹ��һ��Dialog��ʾ����Ĵ�����Ϣ
							JOptionPane.showMessageDialog(null, e1, "����", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
		});

		this.setResizable(false);
		new Thread(new RepaintThread()).start(); // �����ػ��߳�
	}

	/**
	 * �������������Ԫ��
	 */
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();

		g.setColor(Color.black);
		g.setFont(new Font("΢���ź�", Font.BOLD, 25));
		g.drawString(" �¶ȣ� ", 45, 510);

		g.setColor(Color.black);
		g.setFont(new Font("΢���ź�", Font.BOLD, 25));
		g.drawString(" ʪ�ȣ� ", 425, 510);

		g.setColor(Color.gray);
		g.setFont(new Font("΢���ź�", Font.BOLD, 20));
		g.drawString(" ����ѡ�� ", 45, 550);

		g.setColor(Color.gray);
		g.setFont(new Font("΢���ź�", Font.BOLD, 20));
		g.drawString(" �����ʣ� ", 425, 550);
	}

	/**
	 * ˫���巽ʽ�ػ������Ԫ�����
	 */
	@Override
	public void update(Graphics g) {
		if (offScreen == null)
			offScreen = this.createImage(StaticData.WIDTH, StaticData.HEIGHT);
		Graphics gOffScreen = offScreen.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.white);
		gOffScreen.fillRect(0, 0, StaticData.WIDTH, StaticData.HEIGHT); // �ػ���������
		this.paint(gOffScreen); // �ػ�����Ԫ��
		gOffScreen.setColor(c);
		g.drawImage(offScreen, 0, 0, null); // ���»��õĻ�����������ԭ������
	}

	/*
	 * �ػ��̣߳�ÿ��30�����ػ�һ�Σ�
	 */
	private class RepaintThread implements Runnable {

		@Override
		public void run() {

			while (true) {
				repaint();
				// ɨ����ô���
				commList = SerialTool.findPort();
				if (commList != null && commList.size() > 0) {
					// �����ɨ�赽�Ŀ��ô���
					for (String s : commList) {
						// �ô������Ƿ��Ѵ��ڣ���ʼĬ��Ϊ�����ڣ���commList����ڵ���commChoice�ﲻ���ڣ�������ӣ�
						boolean commExist = false;
						for (int i = 0; i < commChoice.getItemCount(); i++) {
							if (s.equals(commChoice.getItem(i))) {
								// ��ǰɨ�赽�Ĵ������Ѿ��ڳ�ʼɨ��ʱ����
								commExist = true;
								break;
							}
						}
						if (commExist) {
							// ��ǰɨ�赽�Ĵ������Ѿ��ڳ�ʼɨ��ʱ���ڣ�ֱ�ӽ�����һ��ѭ��
							continue;
						} else {
							// ��������������´����������ô��������б�
							commChoice.add(s);
						}
					}
					// �Ƴ��Ѿ������õĴ���
					for (int i = 0; i < commChoice.getItemCount(); i++) {
						// �ô����Ƿ���ʧЧ����ʼĬ��Ϊ�Ѿ�ʧЧ����commChoice����ڵ���commList�ﲻ���ڣ����Ѿ�ʧЧ��
						boolean commNotExist = true;
						for (String s : commList) {
							if (s.equals(commChoice.getItem(i))) {
								commNotExist = false;
								break;
							}
						}
						if (commNotExist) {
							// System.out.println("remove" +
							// commChoice.getItem(i));
							commChoice.remove(i);
						} else {
							continue;
						}
					}
				} else {
					// ���ɨ�赽��commListΪ�գ����Ƴ��������д���
					commChoice.removeAll();
				}

				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					String err = ExceptionWriter.getErrorInfoFromException(e);
					JOptionPane.showMessageDialog(null, err, "����", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
			}
		}

	}

	/**
	 * ���ڲ�����ʽ����һ�����ڼ�����
	 *
	 */
	private class SerialListener implements SerialPortEventListener {

		@Override
		public void serialEvent(SerialPortEvent arg0) {
			switch (arg0.getEventType()) {

			case SerialPortEvent.BI: // 10 ͨѶ�ж�
				JOptionPane.showMessageDialog(null, "�봮���豸ͨѶ�ж�", "����", JOptionPane.INFORMATION_MESSAGE);
				break;

			case SerialPortEvent.OE: // 7 ��λ�����������

			case SerialPortEvent.FE: // 9 ֡����

			case SerialPortEvent.PE: // 8 ��żУ�����

			case SerialPortEvent.CD: // 6 �ز����

			case SerialPortEvent.CTS: // 3 �������������

			case SerialPortEvent.DSR: // 4 ����������׼������

			case SerialPortEvent.RI: // 5 ����ָʾ

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 ��������������
				break;

			case SerialPortEvent.DATA_AVAILABLE: // 1 ���ڴ��ڿ�������

				// System.out.println("found data");
				byte[] data = null;

				try {
					if (serialPort == null) {
						JOptionPane.showMessageDialog(null, "���ڶ���Ϊ�գ�����ʧ�ܣ�", "����", JOptionPane.INFORMATION_MESSAGE);
					} else {
						data = SerialTool.readFromPort(serialPort); // ��ȡ���ݣ������ֽ�����
						// System.out.println(new String(data));

						// �Զ����������
						if (data == null || data.length < 1) { // ��������Ƿ��ȡ��ȷ
							JOptionPane.showMessageDialog(null, "��ȡ���ݹ�����δ��ȡ����Ч���ݣ������豸�����", "����",
									JOptionPane.INFORMATION_MESSAGE);
							System.exit(0);
						} else {
							String dataOriginal = new String(data); // ���ֽ���������ת��λΪ������ԭʼ���ݵ��ַ���
							String dataValid = ""; // ��Ч���ݣ���������ԭʼ�����ַ���ȥ���ͷ*���Ժ���ַ�����
							String[] elements = null; // �������水�ո���ԭʼ�ַ�����õ����ַ�������
							// ��������
							if (dataOriginal.charAt(0) == '*') { // �����ݵĵ�һ���ַ���*��ʱ��ʾ���ݽ�����ɣ���ʼ����
								dataValid = dataOriginal.substring(1);
								elements = dataValid.split(" ");
								if (elements == null || elements.length < 1) { // ��������Ƿ������ȷ
									JOptionPane.showMessageDialog(null, "���ݽ������̳��������豸�����", "����",
											JOptionPane.INFORMATION_MESSAGE);
									System.exit(0);
								} else {
									try {
										// ���½���Labelֵ
										/*
										 * for (int i=0; i<elements.length; i++)
										 * { System.out.println(elements[i]); }
										 */
										// System.out.println("win_dir: " +
										// elements[5]);
										tem.setText(elements[0] + " ��");
										hum.setText(elements[1] + " %");
									} catch (ArrayIndexOutOfBoundsException e) {
										JOptionPane.showMessageDialog(null, "���ݽ������̳������½�������ʧ�ܣ������豸�����", "����",
												JOptionPane.INFORMATION_MESSAGE);
										System.exit(0);
									}
								}
							}
						}

					}

				} catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
					JOptionPane.showMessageDialog(null, e, "����", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0); // ������ȡ����ʱ��ʾ������Ϣ���˳�ϵͳ
				}

				break;

			}
		}

	}
}
