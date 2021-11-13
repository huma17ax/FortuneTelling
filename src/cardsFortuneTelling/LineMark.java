package cardsFortuneTelling;

import java.awt.Graphics;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

public class LineMark {
	
	public int raw;
	public boolean isEmpty,onMouse;
	
	private static BufferedImage markImage;
	
	/* constructor */
	public LineMark(int _raw) {
		raw=_raw;
		
		try {
			markImage = ImageIO.read(new File("image/trump/z09.png"));
		} catch(IOException e) {
			
		}
	}
	
	/* call from CardManager.MoveCards */
	public void CheckEmpty() {
		isEmpty = true;
		for (Cards card:MainPanel.cardManager.card) {
			if (card.x==raw)  {
				isEmpty=false;
				break;
			}
		}
	}
	
	/* call from CardManager.MoveCards */
	public void AppMoveCards() {
		if (isEmpty && onMouse) MainPanel.cardManager.MoveCardsToEmpty(raw);
	}
	
	/* call from CardManager.Update */
	public void CheckOnMouse(int _mouseX,int _mouseY) {
		onMouse = false;
		if (_mouseX>Cards.baseX+raw*Cards.imageWidth*Cards.scale
			&& _mouseX<Cards.baseX+(raw+1)*Cards.imageWidth*Cards.scale
			&& _mouseY>Cards.baseY
			&& _mouseY<Cards.baseY+Cards.imageHeight*Cards.scale) {
			onMouse = true;
		}
	}
	
	/* call from CardManager.Draw */
	public void Draw(Graphics g) {
		g.drawImage(
    		markImage,
    		(int)(Cards.baseX+raw*Cards.imageWidth*Cards.scale), (int)(Cards.baseY),
    		(int)(Cards.baseX+(raw+1)*Cards.imageWidth*Cards.scale),(int)(Cards.baseY+Cards.imageHeight*Cards.scale),
    		0,0,Cards.imageWidth,Cards.imageHeight,
    		null);
		if (isEmpty && onMouse) g.drawImage(
    			CardManager.onMouseImage,
    			(int)(Cards.baseX+raw*Cards.imageWidth*Cards.scale), (int)(Cards.baseY),
    			(int)(Cards.baseX+(raw+1)*Cards.imageWidth*Cards.scale),(int)(Cards.baseY+Cards.imageHeight*Cards.scale),
    			0,0,Cards.imageWidth,Cards.imageHeight,
    			null);
	}
	
}
