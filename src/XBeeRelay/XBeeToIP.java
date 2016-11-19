package XBeeRelay;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;

public class XBeeToIP extends Thread {
	public String forwardIP;
	public int port;
	private BigInteger count=BigInteger.ZERO;
	
	public void run () {
		while (true) {
			while (XBeeRelay.queue.peek()!=null) {
				try {
					Socket sc=new Socket(forwardIP,port);
					sc.setSoTimeout(5000);
					OutputStream os=sc.getOutputStream();
					DataOutputStream pw=new DataOutputStream(os);
					String s=XBeeRelay.queue.poll();
					pw.write(s.getBytes());
					pw.close();
					os.close();
					
					//For control.
					String [] split=s.split(XBeeRelay.fieldDelimiter);
					if (XBeeRelay.tcpRespondWaiting.containsKey(split[1]) && split[0].equals("2")) {
						XBeeRelay.tcpRespondQueue.put(split[1],s);
					}
					
					count=count.add(BigInteger.ONE);
					XBeeRelay.u.updateXBeeIP(count.toString());
					sc.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			try {Thread.sleep(300); } catch (InterruptedException e) {}
		}
	}
	
}
