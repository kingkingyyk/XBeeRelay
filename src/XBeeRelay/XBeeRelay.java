package XBeeRelay;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.listeners.IPacketReceiveListener;
import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.packet.XBeePacket;

public class XBeeRelay {

	public static XBeeDevice xbee;
	public static final String fieldDelimiter=";";
	public static ConcurrentHashMap<String,XBee16BitAddress> addressCache=new ConcurrentHashMap<>();
	public static ConcurrentLinkedQueue<String> queue=new ConcurrentLinkedQueue<>();
	public static ConcurrentHashMap<String,Boolean> tcpRespondWaiting=new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String,String> tcpRespondQueue=new ConcurrentHashMap<>();
	public static MainUI u;
	
	public static void main (String [] args) {
		u=new MainUI();
		u.setLocationRelativeTo(null);
		u.setVisible(true);
	}
	
	public static void createProcess (String portName, int baudrate, String forwardIP, int forwardPort, int listeningPort) {
		xbee=new XBeeDevice(portName,baudrate);
		try {
			xbee.open();
			xbee.addPacketListener(new IPacketReceiveListener() {
	
				@Override
				public void packetReceived(XBeePacket arg0) {
					String [] split=arg0.getParameters().get("RF data").split(" ");
					StringBuilder sb=new StringBuilder();
					for (String s : split) {
						sb.append((char)Integer.parseInt(s,16));
					}
					queue.offer(sb.toString());
					
					String senderAddress=arg0.getParameters().get("16-bit source address");
					if (!addressCache.containsValue(senderAddress)) {
						split=sb.toString().split(fieldDelimiter);
						addressCache.put(split[1],new XBee16BitAddress(senderAddress));
					}
				}
				
			});
		} catch (XBeeException e) {
			e.printStackTrace();
		}
		
		while (!xbee.isOpen()) {}
		XBeeToIP xi=new XBeeToIP(); xi.forwardIP=forwardIP; xi.port=forwardPort;
		xi.start();
		
		IPToXBee ix=new IPToXBee(); ix.port=listeningPort;
		ix.start();
		
		DeviceDiscovery dd=new DeviceDiscovery(); dd.start();
	}
	
}
