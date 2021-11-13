package cardsFortuneTelling;

import java.awt.Graphics;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CollectMark {

	public cardType type;
	public int collectedNum=0;
	public int coorX,coorY;
	public boolean onMouse;
	
	private BufferedImage markImage;
	
	/* constructor */
	public CollectMark(cardType _type) {
		type=_type;
		
		try {
			switch(type) {
			case club:
				markImage = ImageIO.read(new File("image/trump/z06c.png"));
				coorY=500;
				coorX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*7.5);
				break;
			case diamond:
				markImage = ImageIO.read(new File("image/trump/z06d.png"));
				coorY=500;
				coorX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*8.5)+10;
				break;
			case heart:
				markImage = ImageIO.read(new File("image/trump/z06h.png"));
				coorY=(int)(500+Cards.imageHeight*Cards.scale)+10;
				coorX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*7.5);
				break;
			case spade:
				markImage = ImageIO.read(new File("image/trump/z06s.png"));
				coorY=(int)(500+Cards.imageHeight*Cards.scale)+10;
				coorX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*8.5)+10;
				break;
			default:
				break;
			}
		} catch (IOException e){
		}
		
	}
	
	/* call from CardManager.Update */
	public void CheckOnMouse(int _mouseX,int _mouseY) {
		onMouse = false;
		if (_mouseX>coorX
			&& _mouseX<coorX+Cards.imageWidth*Cards.scale
			&& _mouseY>coorY
			&& _mouseY<coorY+Cards.imageHeight*Cards.scale) {
			onMouse = true;
		}
	}
	
	/* call from CardManager.MoveCards */
	public void AppCollectCards(){
		if (onMouse) {
			collectedNum+=MainPanel.cardManager.CollectCards(type,collectedNum);
		}
	}
	
	// club  | diamond
	// heart | spade
	//
	//盤から横 半カード離す
	//各10px離す
	/* call from CardManager.Draw */
	public void Draw(Graphics g) {

		boolean breakFlag=false;
		for (int i=0 ; i<=collectedNum && !breakFlag ; i++) {
			
			if (i==collectedNum) {
				g.drawImage(
						markImage,
						coorX,coorY,
		    			(int)(coorX+Cards.imageWidth*Cards.scale),(int)(coorY+Cards.imageHeight*Cards.scale),
		    			0,0,Cards.imageWidth,Cards.imageHeight,
		    			null);
				break;
			}
			for (Cards card:MainPanel.cardManager.card) {
				if (card.number==collectedNum-i && card.type==type) {
					if (card.DrawToCMark(g, coorX, coorY)==true) breakFlag=true;
				}
			}
			
		}
		
		if (onMouse) g.drawImage(
    			CardManager.onMouseImage,
    			coorX,coorY,
        		(int)(coorX+Cards.imageWidth*Cards.scale),(int)(coorY+Cards.imageHeight*Cards.scale),
    			0,0,Cards.imageWidth,Cards.imageHeight,
    			null);
	}
	
}
