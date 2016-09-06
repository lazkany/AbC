/**
 * 
 */
package org.sysma.abc.examples.smartconference;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.sysma.abc.core.topology.distributed.AbCPort;
import org.sysma.abc.core.topology.VirtualPort;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class SmartConference extends JFrame {

	private JTextField identity;
	private JTextField number;
	private VirtualPort vp = new VirtualPort();
	JPanel inputPane = new JPanel();

	public JPanel getInputPane() {
		return inputPane;
	}

	public void setInputPane(JPanel inputPane) {
		this.inputPane = inputPane;
	}

	public SmartConference(AbCComponent c) throws HeadlessException, AbCAttributeTypeException {
		super("Smart Conference System");
		// this.topic = room.getValue(Environment.sessionAttribute);
		build();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}

	public AbCComponent createRoomComponent(int id, String topic, AbCPort port)
			throws DuplicateNameException, AbCPortException, AbCAttributeTypeException {
		AbCComponent c = new AbCComponent("Room " + id);
		c.setValue(Defs.nameAttribute, "Room " + id);
		c.setValue(Defs.relocateAttrivute, false);
		c.setValue(Defs.sessionAttribute, topic);
		c.setValue(Defs.roleAttribute, Defs.PROVIDER);
		port.start();
		c.setPort(port);
		c.addProcess(new RoomAgent());
		c.addProcess(new RelocationAgent());
		c.addProcess(new UpdatingAgent());
		RoomPanel rp = new RoomPanel(c);
		rp.setLocation(0, (id - 1) * rp.getHeight() + 40 * (id - 1));
		rp.setVisible(true);
		return c;
	}

	public AbCComponent createParticipantComponent(int id, String topic, AbCPort port)
			throws DuplicateNameException, AbCPortException, AbCAttributeTypeException {
		AbCComponent c = new AbCComponent("Participant " + id);
		c.setValue(Defs.idAttribute, id);
		port.start();
		c.setPort(port);
		c.addProcess(new ParticipantAgent("Participant " + id, topic));
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
			private ArrayList<String> list = new ArrayList<String>(Arrays.asList("A", "B", "C", "D", "E", "F", "J"));

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(SmartConference.this, "Create Venue! ");
				if (result == JOptionPane.OK_OPTION) {
					try {
						for (int i = 1; i <= 7; i++) {
							synchronized (this) {
								String x = (this.list.get(new Random().nextInt(this.list.size())));

								createRoomComponent(i, x, vp.getPort()).start();
								this.list.remove(x);
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
					}
				}
			}

		});
		JButton button2 = new JButton("New Participants");
		button2.addActionListener(new ActionListener() {
			private ArrayList<String> list = new ArrayList<String>(Arrays.asList("A", "B", "C", "D", "E", "F", "J"));
			private Random rnd=new Random();
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(SmartConference.this, "New participants arrival ");
				if (result == JOptionPane.OK_OPTION) {
					try {
						
						for (int i = Integer.parseInt(identity.getText()); i <= (Integer
								.parseInt(number.getText())+Integer.parseInt(identity.getText())-1); i++) {
							String x = (this.list.get(this.rnd.nextInt(7)));
							Thread.sleep(20);
							createParticipantComponent(i, x, vp.getPort()).start();

						}
						int n = Integer
								.parseInt(number.getText())+Integer.parseInt(identity.getText());
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
		SmartConference rp = new SmartConference(new AbCComponent("test"));
		rp.setLocationRelativeTo(null);
		// rp.getInputPane().setBackground(Color.darkGray);
		rp.setVisible(true);
		// Thread.currentThread().sleep(1000);
		// rp.getInputPane().setBackground(Color.magenta);
	}

}
