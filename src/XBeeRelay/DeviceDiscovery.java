package XBeeRelay;

public class DeviceDiscovery extends Thread {
	public void run () {
		XBeeRelay.xbee.getNetwork().startDiscoveryProcess();
		while (true) {
			XBeeRelay.u.updateXBeeDevices(XBeeRelay.xbee.getNetwork().getNumberOfDevices());
			
			try {Thread.sleep(1000); } catch (InterruptedException e) {}
		}
	}
}
