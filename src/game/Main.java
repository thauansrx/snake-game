package game;

import java.awt.Dimension;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("GAME - SNAKE");
		
		frame.setContentPane(new GamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setResizable(false);
		frame.pack();
		
		frame.setPreferredSize(new Dimension(GamePanel.LARGURA, GamePanel.ALTURA));
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);

	}
}
