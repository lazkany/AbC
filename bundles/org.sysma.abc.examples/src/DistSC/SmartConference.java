/**
 * 
 */
package DistSC;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.exceptions.AbCPortException;
import org.sysma.abc.core.exceptions.DuplicateNameException;
import org.sysma.abc.core.topology.distributed.AbCClient;
import org.sysma.abc.core.topology.distributed.AbCPort;
import org.sysma.abc.core.topology.VirtualPort;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class SmartConference extends JFrame {

	private JTextField identity;
	private JTextField number;
	// private VirtualPort vp = new VirtualPort();
	private int port = 1221;
	JPanel inputPane = new JPanel();

	public JPanel getInputPane() {
		return inputPane;
	}

	public void setInputPane(JPanel inputPane) {
		this.inputPane = inputPane;
	}

	public SmartConference() throws HeadlessException, AbCAttributeTypeException {
		super("Smart Conference System");
		// this.topic = room.getValue(Environment.sessionAttribute);
		build();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

	public synchronized int getPort() {
		return ++port;
	}

	public AbCComponent createRoomComponent(int id, String topic, int dataPort, int signalPort, int subPort)
			throws DuplicateNameException, AbCPortException, AbCAttributeTypeException, IOException {
		AbCClient Client = new AbCClient(InetAddress.getLoopbackAddress(), dataPort, signalPort);
		Client.register(InetAddress.getLoopbackAddress(), subPort);
		AbCComponent c = new AbCComponent("Room " + id);
		c.setValue(Defs.nameAttribute, "Room " + id);
		c.setValue(Defs.relocateAttrivute, false);
		c.setValue(Defs.sessionAttribute, topic);
		c.setValue(Defs.roleAttribute, Defs.PROVIDER);
		c.addProcess(new RoomAgent());
		c.addProcess(new RelocationAgent());
		c.addProcess(new UpdatingAgent());
		c.setPort(Client);
		Client.start();
		RoomPanel rp = new RoomPanel(c);
		rp.setLocation(0, (id - 1) * rp.getHeight() + 40 * (id - 1));
		rp.setVisible(true);
		return c;
	}

	public AbCComponent createParticipantComponent(int id, String topic, int data, int signal, int subscription)
			throws DuplicateNameException, AbCPortException, AbCAttributeTypeException, IOException {
		AbCClient Client = new AbCClient(InetAddress.getLoopbackAddress(), data, signal);
		Client.register(InetAddress.getLoopbackAddress(), subscription);
		AbCComponent c = new AbCComponent("Participant " + id);
		c.setValue(Defs.idAttribute, id);
		c.addProcess(new ParticipantAgent("Participant " + id, topic));
		c.setPort(Client);
		Client.start();
		return c;
	}

	private void build() {
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(2, 1));

		// contentPane.setLayout(new GridLayout(2, 2));

		// this.setContentPane(inputPane);
		// contentPane.add(inputPane);
		JButton button1 = new JButton("Create Conference Venue");
		button1.addActionListener(new ActionListener() {
			private ArrayList<String> list = new ArrayList<String>(Arrays.asList("Expressiveness", "Session types", "Verification", "Testing", "Distributed Coordination", "Model Checking", "Process Algebra"));
			private ArrayList<Integer> sub_ports = new ArrayList<Integer>(
					Arrays.asList(9975, 9979, 9983, 9987, 9991, 9995, 9999));

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(SmartConference.this, "Create Venue! ");
				if (result == JOptionPane.OK_OPTION) {
					try {
						for (int i = 1; i <= 7; i++) {
							synchronized (this) {
								String x = (this.list.get(new Random().nextInt(this.list.size())));
								int subscription = (this.sub_ports.get(new Random().nextInt(this.sub_ports.size())));
								int data = getPort();
								int signal = getPort();
								createRoomComponent(i, x, data, signal, subscription).start();
								this.list.remove(x);
								// this.sub_ports.remove(subscription);
							}
						}
						button1.setEnabled(false);
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (DuplicateNameException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (AbCPortException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (AbCAttributeTypeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

		});
		JButton button2 = new JButton("New Participants");
		button2.addActionListener(new ActionListener() {
			private ArrayList<String> list = new ArrayList<String>(Arrays.asList("Expressiveness", "Session types", "Verification", "Testing", "Distributed Coordination", "Model Checking", "Process Algebra"));
			private ArrayList<Integer> sub_ports = new ArrayList<Integer>(
					Arrays.asList(9975, 9979, 9983, 9987, 9991, 9995, 9999));
			private Random rnd = new Random();
			private ArrayList<AbCComponent> comps = new ArrayList<AbCComponent>();

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(SmartConference.this, "New participants arrival ");
				if (result == JOptionPane.OK_OPTION) {
					try {

						for (int i = Integer.parseInt(identity.getText()); i <= (Integer.parseInt(number.getText())
								+ Integer.parseInt(identity.getText()) - 1); i++) {
							String x = (this.list.get(this.rnd.nextInt(7)));
							int data = getPort();
							int signal = getPort();
							int subscription = (this.sub_ports.get(new Random().nextInt(this.sub_ports.size())));

							
							// AbCClient Client = new
							// AbCClient(InetAddress.getLoopbackAddress(), data,
							// signal);
							// Client.register(InetAddress.getLoopbackAddress(),
							// subscription);
							comps.add(createParticipantComponent(i, x, data, signal, subscription));

						}
						Thread.sleep(5000);
						for (AbCComponent c : comps) {
						    c.start();
						}
						int n = Integer.parseInt(number.getText()) + Integer.parseInt(identity.getText());
						identity.setText(String.valueOf(n));
						identity.setEnabled(false);
						number.setText("");
					} catch (ConcurrentModificationException e1) {
						// TODO Auto-generated catch block
						System.out.println(e1.toString());
						e1.printStackTrace();
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (DuplicateNameException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (AbCPortException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (AbCAttributeTypeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}

		});
		inputPane.add(button1);

		this.setContentPane(contentPane);
		contentPane.add(inputPane);

		JPanel panel = new JPanel();
		panel.add(new JLabel("ID: "));
		identity = new JTextField(3);
		panel.add(identity);

		number = new JTextField(3);
		panel.add(new JLabel("Number: "));
		panel.add(number);
		this.setContentPane(contentPane);
		panel.add(button2);
		contentPane.add(panel);
		// inputPane.setBackground(Color.RED);
		pack();

	}

	public static void main(String[] argv) throws HeadlessException, AbCAttributeTypeException, InterruptedException {
		SmartConference rp = new SmartConference();
		rp.setLocationRelativeTo(null);
		// rp.getInputPane().setBackground(Color.darkGray);
		rp.setVisible(true);
		// Thread.currentThread().sleep(1000);
		// rp.getInputPane().setBackground(Color.magenta);
	}

}
