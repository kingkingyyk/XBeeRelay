package XBeeRelay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import com.digi.xbee.api.RemoteRaw802Device;

public class IPToXBee extends Thread {
	public int port;
	private BigInteger count=BigInteger.ZERO;
	
	public void run () {
		ServerSocket ss=null;
		try { ss=new ServerSocket(port); } catch (Exception e) {}
		while (true) {
			try {
				Socket sc=ss.accept();
				InputStream is=sc.getInputStream();
				DataInputStream br=new DataInputStream(is);
				OutputStream os=sc.getOutputStream();
				DataOutputStream pw=new DataOutputStream(os);
				
				byte [] read_data=new byte [100];
				br.readFully(read_data);
				String s=new String(read_data);
				String [] split=s.split(XBeeRelay.fieldDelimiter);
				if (split[0].equals("1")) { //maintain the connection!
					XBeeRelay.tcpRespondWaiting.put(split[1],true);
				}
				if (XBeeRelay.addressCache.containsKey(split[1])) {
					try {
						XBeeRelay.xbee.sendData(new RemoteRaw802Device(XBeeRelay.xbee,XBeeRelay.addressCache.get(split[1])),s.split(";!")[0].getBytes());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						XBeeRelay.xbee.sendBroadcastData(s.split(";!")[0].getBytes());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (split[0].equals("1")) {
					try { Thread.sleep(1000); } catch (InterruptedException e) {};
					for (int i=0;i<20 && !XBeeRelay.tcpRespondQueue.containsKey(split[1]);i++) {
						try { Thread.sleep(100); } catch (InterruptedException e) {};
					}
					if (XBeeRelay.tcpRespondQueue.containsKey(split[1])) {
						byte [] reply=XBeeRelay.tcpRespondQueue.remove(split[1]).split(XBeeRelay.fieldDelimiter)[3].getBytes();
						byte [] toSend=new byte [100];
						Arrays.fill(toSend,(byte)0);
						for (int i=0;i<reply.length;i++)
							toSend[i]=reply[i];
						
						pw.write(toSend);
					}
					pw.flush();
					pw.close();
					XBeeRelay.tcpRespondWaiting.remove(split[1]);
				}
				br.close();
				
				count=count.add(BigInteger.ONE);
				XBeeRelay.u.updateIPXBee(String.valueOf(count));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
