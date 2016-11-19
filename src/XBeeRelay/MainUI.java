package XBeeRelay;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;

public class MainUI extends JFrame {
	private static final long serialVersionUID = -8500115240406676616L;
	private JPanel contentPane;
	private JTextField textFieldXBeePortName;
	private JTextField textFieldBaudRate;
	private JTextField textFieldForwardIp;
	private JTextField textFieldForwardPort;
	private JTextField textFieldListenPort;
	private JButton btnStart;
	private JLabel lblXBeeIP;
	private JLabel lblIPXBee;
	private JLabel lblXBeeDevices;
	private JPanel panel_1;

	public MainUI() {
		setTitle("XBee Relay");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {}

			@Override
			public void windowClosed(WindowEvent arg0) {}

			@Override public void windowClosing(WindowEvent arg0) {
				if (XBeeRelay.xbee!=null && XBeeRelay.xbee.isOpen()) XBeeRelay.xbee.close();
			}

			@Override public void windowDeactivated(WindowEvent arg0) {}

			@Override public void windowDeiconified(WindowEvent arg0) {}

			@Override public void windowIconified(WindowEvent arg0) {}

			@Override public void windowOpened(WindowEvent arg0) {}
			
		});
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 414, 239);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Setup", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblXBeePortName = new JLabel("XBee's Port :");
		lblXBeePortName.setBounds(10, 11, 97, 14);
		panel.add(lblXBeePortName);
		lblXBeePortName.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textFieldXBeePortName = new JTextField();
		textFieldXBeePortName.setBounds(117, 8, 282, 20);
		panel.add(textFieldXBeePortName);
		textFieldXBeePortName.setText("COM7");
		textFieldXBeePortName.setColumns(10);
		
		JLabel lblBaudRate = new JLabel("Baud Rate :");
		lblBaudRate.setBounds(0, 42, 107, 14);
		panel.add(lblBaudRate);
		lblBaudRate.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textFieldBaudRate = new JTextField();
		textFieldBaudRate.setBounds(117, 39, 282, 20);
		panel.add(textFieldBaudRate);
		textFieldBaudRate.setText("9600");
		textFieldBaudRate.setColumns(10);
		
		JLabel lblForwardIp = new JLabel("Forward IP :");
		lblForwardIp.setBounds(0, 72, 107, 14);
		panel.add(lblForwardIp);
		lblForwardIp.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textFieldForwardIp = new JTextField();
		textFieldForwardIp.setBounds(117, 70, 282, 20);
		panel.add(textFieldForwardIp);
		textFieldForwardIp.setText("192.168.1.175");
		textFieldForwardIp.setColumns(10);
		
		JLabel lblForwardPort = new JLabel("Forward Port :");
		lblForwardPort.setBounds(10, 104, 97, 14);
		panel.add(lblForwardPort);
		lblForwardPort.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textFieldForwardPort = new JTextField();
		textFieldForwardPort.setBounds(117, 101, 282, 20);
		panel.add(textFieldForwardPort);
		textFieldForwardPort.setText("40000");
		textFieldForwardPort.setColumns(10);
		
		textFieldListenPort = new JTextField();
		textFieldListenPort.setBounds(117, 132, 282, 20);
		panel.add(textFieldListenPort);
		textFieldListenPort.setText("40002");
		textFieldListenPort.setColumns(10);
		
		JLabel lblListenPort = new JLabel("Listen Port :");
		lblListenPort.setBounds(10, 135, 97, 14);
		panel.add(lblListenPort);
		lblListenPort.setHorizontalAlignment(SwingConstants.RIGHT);
		
		btnStart = new JButton("Start");
		btnStart.setBounds(310, 177, 89, 23);
		panel.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				XBeeRelay.createProcess(textFieldXBeePortName.getText(),Integer.parseInt(textFieldBaudRate.getText()),
										textFieldForwardIp.getText(), Integer.parseInt(textFieldForwardPort.getText()),
										Integer.parseInt(textFieldListenPort.getText()));
				btnStart.setEnabled(false);
			}
		});
		
		panel_1 = new JPanel();
		tabbedPane.addTab("Statistics", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("XBee -> IP :");
		lblNewLabel.setBounds(10, 11, 97, 14);
		panel_1.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblIPXBee = new JLabel("0");
		lblIPXBee.setBounds(117, 36, 139, 14);
		panel_1.add(lblIPXBee);
		
		lblXBeeIP = new JLabel("0");
		lblXBeeIP.setBounds(117, 11, 139, 14);
		panel_1.add(lblXBeeIP);
		
		JLabel lblNewLabel_1 = new JLabel("IP -> XBee :");
		lblNewLabel_1.setBounds(10, 36, 97, 14);
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel labelXBeeDevicez = new JLabel("Device Count :");
		labelXBeeDevicez.setBounds(0, 61, 107, 14);
		panel_1.add(labelXBeeDevicez);
		labelXBeeDevicez.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblXBeeDevices = new JLabel("0");
		lblXBeeDevices.setBounds(117, 61, 139, 14);
		panel_1.add(lblXBeeDevices);
	}
	
	public void updateXBeeIP (String s) {
		lblXBeeIP.setText(s);
	}
	
	public void updateIPXBee (String s) {
		lblIPXBee.setText(s);
	}
	
	public void updateXBeeDevices (int i) {
		lblXBeeDevices.setText(String.valueOf(i));
	}
}
