package com.zhbitzwz.cn.MyView;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import com.zhbitzwz.cn.StaticData.StaticData;
import com.zhibtzwz.cn.serialException.ExceptionWriter;

/**
 * @author ZWZ
 * @version 2016��12��11�� ����2:27:49
 * @Function ���������
 */
public class MyClient extends Frame {

	Color color = Color.WHITE;
	Image offScreen = null; // ����˫����

	// ����window��icon
	Toolkit toolKit = getToolkit();
	Image icon = toolKit.getImage(MyClient.class.getResource("computer.png"));

	// �����������
	MyDataView myDataView = new MyDataView(this); // ��ʾ������������

	public static void main(String[] args) {
		new MyClient().launchFrame();
	}

	/**
	 * ��ʾ��ҳ��
	 */
	public void launchFrame() {
		initView();

		new Thread(new RepaintThread()).start();
	}

	/**
	 * Initial����
	 */
	public void initView() {
		this.setBounds(StaticData.LOC_X, StaticData.LOC_Y, StaticData.WIDTH, StaticData.HEIGHT);
		this.setTitle("�������ն�ϵͳ");
		this.setIconImage(icon);
		this.setBackground(Color.white);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.addKeyListener(new KeyMonitor());
		this.setResizable(false);
		this.setVisible(true);
	}

	/**
	 * �����¼��ļ����ڲ���
	 */
	private class KeyMonitor extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_ENTER) {
				setVisible(false);
				myDataView.setVisible(true);
				myDataView.dataFrame();
			}
		}
	}

	/**
	 * ���������������Ԫ��
	 */
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();

		g.setFont(new Font("΢���ź�", Font.BOLD, 40));
		g.setColor(Color.black);
		g.drawString("��ӭʹ�ó���ϵͳ", 45, 190);

		g.setFont(new Font("΢���ź�", Font.ITALIC, 26));
		g.setColor(Color.BLACK);
		g.drawString("Version��1.0   Powered By��ZWZ", 280, 260);

		g.setFont(new Font("΢���ź�", Font.BOLD, 30));
		g.setColor(color);
		g.drawString("�����������Enter�����������桪������", 100, 480);
		// ʹ���� "�����������Enter�����������桪������" �ڰ���˸
		if (color == Color.WHITE)
			color = Color.black;
		else if (color == color.BLACK)
			color = Color.white;
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
		gOffScreen.fillRect(0, 0, StaticData.WIDTH, StaticData.HEIGHT);
		this.paint(gOffScreen); // �ػ�����Ԫ��
		gOffScreen.setColor(c);
		g.drawImage(offScreen, 0, 0, null); // ���»��õĻ�����������ԭ������
	}

	/**
	 * �ػ��߳��ڲ��ࣨÿ��250�����ػ�һ�Σ�
	 */
	private class RepaintThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					String err = ExceptionWriter.getErrorInfoFromException(e);
					JOptionPane.showMessageDialog(null, err, "����", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
			}
		}

	}
}
