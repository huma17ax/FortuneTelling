package cardsFortuneTelling;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame{

	public MainFrame() {

		setTitle("占い");
		
		/*
        JMenuBar menubar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        menubar.add(gameMenu);
        JMenuItem resetItem = new JMenuItem("Reset");
        gameMenu.add(resetItem);
        
        setJMenuBar(menubar);
        */
		
		MainPanel panel = new MainPanel();
		panel.setBackground(new Color(70,100,50));
		//resetItem.addActionListener(panel);
		
		Container contentPane = getContentPane();
		contentPane.add(panel);
		
		pack();
	}
	
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
	}
	
}
