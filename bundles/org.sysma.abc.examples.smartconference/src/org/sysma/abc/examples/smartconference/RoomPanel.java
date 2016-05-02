/**
 * 
 */
package org.sysma.abc.examples.smartconference;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.JarOutputStream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.StyleContext.SmallAttributeSet;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.Not;

/**
 * @author loreti
 *
 */
public class RoomPanel extends JFrame {
	
	private AbCComponent room;
	private String topic;
	private DefaultListModel<String> history;
	private JTextField field;

	public RoomPanel( AbCComponent room ) throws HeadlessException, AbCAttributeTypeException {
		super( room.getValue(Environment.nameAttribute) );
		this.room = room;
		this.topic = room.getValue(Environment.sessionAttribute);
		build();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while( true ) {
					try {
						room.waitUntil(new Not(new HasValue(Environment.SESSION_ATTRIBUTE_NAME, topic)));
						String newTopic = room.getValue(Environment.sessionAttribute);
						if (!newTopic.equals(topic)) {
							history.add(0, newTopic);
							topic = newTopic;
							JOptionPane.showMessageDialog(RoomPanel.this, "Topic changed!");
						}
					} catch (AbCAttributeTypeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		t.start();
	}

	private void build() {
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(2, 1));
		
		JPanel inputPane = new JPanel();
//		contentPane.setLayout(new GridLayout(2, 2));
		inputPane.add(new JLabel("Topic: "));
		field = new JTextField(50);
		inputPane.add( field );
		JButton button = new JButton("Set");
		button.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String topic = field.getText();
				int result = JOptionPane.showConfirmDialog(
					RoomPanel.this, 
					"Set room topic to: "+field.getText()
				);
				if (result == JOptionPane.OK_OPTION) {
					try {
						RoomPanel.this.topic = topic;
						if (history.isEmpty()) {
							room.setValue(Environment.sessionAttribute, topic);
						} else {
							room.setValue(Environment.newSessionAttribute, topic);
							room.setValue(Environment.relocateAttrivute, true);
						}
						history.add(0, topic);
						field.setText("");
					} catch (AbCAttributeTypeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		});
		inputPane.add( button );
		this.setContentPane(contentPane);
		contentPane.add(inputPane);
		history = new DefaultListModel<>();		
		history.add(0, topic);
		contentPane.add(new JList<>(history));
		pack();
		
	}
	
	
	public static void main(String[] argv) throws HeadlessException, AbCAttributeTypeException {
		RoomPanel rp = new RoomPanel(new AbCComponent("test") );
		rp.setVisible(true);
	}

}
