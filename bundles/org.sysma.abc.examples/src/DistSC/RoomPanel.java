/**
 * 
 */
package DistSC;

import java.awt.Color;
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
	private JPanel inputPane = new JPanel();
	public RoomPanel( AbCComponent room ) throws HeadlessException, AbCAttributeTypeException {
		super( room.getValue(Defs.nameAttribute) );
		this.room = room;
		this.topic = room.getValue(Defs.sessionAttribute);
		build();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		setClr();
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while( true ) {
					try {
						room.waitUntil(new Not(new HasValue(Defs.SESSION_ATTRIBUTE_NAME, topic)));
						setClr();
						String newTopic = room.getValue(Defs.sessionAttribute);
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
							room.setValue(Defs.sessionAttribute, topic);
						} else {
							room.setValue(Defs.newSessionAttribute, topic);
							room.setValue(Defs.relocateAttrivute, true);
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
	
	public void setClr() throws AbCAttributeTypeException{
		switch(room.getValue(Defs.sessionAttribute)){
		case "A":
			inputPane.setBackground(Color.red);
			break;
		case "B":
			inputPane.setBackground(Color.blue);
			break;
		case "C":
			inputPane.setBackground(Color.GRAY);
			break;
		case "D":
			inputPane.setBackground(Color.GREEN);
			break;
		case "E":
			inputPane.setBackground(Color.cyan);
			break;
		case "F":
			inputPane.setBackground(Color.magenta);
			break;
		case "J":
			inputPane.setBackground(Color.pink);
			break;
			
		}
	}
	public static void main(String[] argv) throws HeadlessException, AbCAttributeTypeException {
		RoomPanel rp = new RoomPanel(new AbCComponent("test") );
		rp.setVisible(true);
	}

}
