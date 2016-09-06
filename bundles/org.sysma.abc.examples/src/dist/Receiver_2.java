/**
 * 
 */
package dist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCEnvironment;
import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Tuple;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.FalsePredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.TruePredicate;
import org.sysma.abc.core.topology.distributed.AbCClient;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class Receiver_2 {
	public static class Process_1 extends AbCProcess {

		/**
		 * @param name
		 */
		public Process_1(String name) {
			super(name);
			// TODO Auto-generated constructor stub

		}

		@Override
		protected void doRun() throws InterruptedException, AbCAttributeTypeException {

			System.out.println(this.name + " => received: " + receive(o -> channel(o)));

		}

		public AbCPredicate channel(Object msg) {
			if (msg instanceof Tuple) {
				Tuple t = (Tuple) msg;
				if (t.get(0).equals("c")) {
					return new TruePredicate();
				}
			}
			return new FalsePredicate();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws DuplicateNameException
	 * @throws AbCAttributeTypeException
	 * @throws AbCPortException 
	 */
	public static void main(String[] args) throws IOException, DuplicateNameException, AbCAttributeTypeException, AbCPortException {
		// TODO Auto-generated method stub
		System.out.println("Enter port number : ");
		int port = 0;
		int sig_port=0;
		int sub_port=0;
		try {
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			port = Integer.parseInt(bufferRead.readLine());
			System.out.println("Enter signal port number : ");
			sig_port=Integer.parseInt(bufferRead.readLine());
			System.out.println("Enter subscribe port number : ");
			sub_port=Integer.parseInt(bufferRead.readLine());

		} catch (IOException e) {
			e.printStackTrace();
		}
		AbCClient cPortClient = new AbCClient(InetAddress.getLoopbackAddress(), port,sig_port);
		cPortClient.register( InetAddress.getLoopbackAddress() , sub_port );
		AbCEnvironment store1 = new AbCEnvironment();
		AbCComponent c1 = new AbCComponent("C1", store1);
		Process_1 rcv2 = new Process_1("rcv_2");
		c1.addProcess(rcv2);
		c1.setPort(cPortClient);
		cPortClient.start();
		c1.start();
//		System.out.println(cPortClient.getLocalAddress().getLocalSocketAddress());

	}

}
