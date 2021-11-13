package cardsFortuneTelling;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.*;

public class ScreenCapture {

	BufferedImage ScrImage;
	Robot robot = null;
	
	public ScreenCapture(){
		try {
			robot = new Robot();
		} catch (AWTException e) {
		}
	}
	
	public void Capture() {
		try {
			ScrImage = robot.createScreenCapture(new Rectangle(MainPanel.WIDTH,MainPanel.HEIGHT));
		} catch (SecurityException e){
		} catch (IllegalArgumentException e) {
		}
	}
	
	public void Draw(Graphics g) {
		g.drawImage(ScrImage, 0, 0, null);
	}
}
