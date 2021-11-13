package cardsFortuneTelling;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MainPanel extends JPanel implements Runnable{

	// パネルサイズ
    public static final int WIDTH = 1240;
    public static final int HEIGHT = 960;

    private Thread thread;

    public static CardManager cardManager;
    public static Input inputMouse;
    public static Effect effect;
    
    public MainPanel() {

        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        // 変数などの初期化

        cardManager = new CardManager();
        inputMouse = new Input();
        effect = new Effect();
        addMouseListener(inputMouse);
        addMouseMotionListener(inputMouse);
        addMouseWheelListener(inputMouse);
        
        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        cardManager.Draw(g);
        if (effect.active!=0) effect.Draw(g);

    }

    public void run() {
    	while (true) {

    		if (effect.active == 0) {
    			cardManager.Update();
    			inputMouse.stop=false;
    		} else {
    			effect.Update();
    			inputMouse.stop=true;
    		}
    		
    		repaint();
    		
    		try {
    			Thread.sleep(5);
    		}catch (InterruptedException e) {
    			e.printStackTrace();
    		}

    	}
    }
    public void Initialize() {
    	
    }

}
