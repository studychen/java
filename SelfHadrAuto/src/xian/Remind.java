package xian;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Remind implements ActionListener {

	private Container con;
	private JPanel jp;
	private JFrame jf;
	private JLabel lab2;

	public Remind() {
		jf = new JFrame();
		jf.setTitle("Task end");
		jf.setBounds(400, 400, 600, 400);

		con = jf.getContentPane();
		con.setLayout(null);
		jp = new JPanel();
		jp.setLayout(null);
		jp.setBounds(10, 10, 280, 180);
		con.add(jp);


		jf.setVisible(true);
		jf.setAlwaysOnTop(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}



	public static void main(String[] args) {
		new Remind();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
