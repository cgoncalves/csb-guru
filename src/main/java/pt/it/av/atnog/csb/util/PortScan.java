package pt.it.av.atnog.csb.util;

import java.io.IOException;
import java.net.Socket;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 *
 */
public class PortScan {
	
	public static boolean isPortOpen(String host, int port) {
		try {
			Socket socket = new Socket(host, port);
			socket.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
