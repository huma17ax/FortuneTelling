package cardsFortuneTelling;

import java.awt.Graphics;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

enum cardType{
	heart,
	spade,
	diamond,
	club
};

public class Cards{

	int x,y,number;
	cardType type;
	boolean isSelect = false;
	boolean isOpen = true;
	boolean isLast = false;
	boolean onMouse = false;
	boolean isCollected = false;
	boolean invisible = false;//移動エフェクト有効時の透明化
	int openingEffect = 0;

	public static final int imageWidth=200,imageHeight=300;
	public static final double scale = 3.0/5.0 ,gap = 1.0/3.0;
	public static int baseX = 60,baseY = 10;
	
	public BufferedImage frontImage;
	
	String imagePath = "image/trump/";

	/* constructor */
	public Cards(cardType _type,int _num) {
		
		x=0;y=0;
		type = _type;
		number = _num;
		
		if (type==cardType.heart) imagePath = imagePath.concat("h");
		if (type==cardType.spade) imagePath = imagePath.concat("s");
		if (type==cardType.diamond) imagePath = imagePath.concat("d");
		if (type==cardType.club) imagePath = imagePath.concat("c");
		imagePath = imagePath.concat(String.format("%02d", number) + ".png");
		try {
			frontImage = ImageIO.read(new File(imagePath));
		} catch(IOException e) {
			
		}
		
	}

	public void Update() {
		invisible = false;
	}
	
	/* call from Input.mousePressed */
	public void CheckClick(int _mouseX,int _mouseY) {
		if ((!isLast
			&& _mouseX>baseX+x*imageWidth*scale
			&& _mouseX<baseX+(x+1)*imageWidth*scale
			&& _mouseY>baseY+y*imageHeight*scale*gap
			&& _mouseY<baseY+(y+1)*imageHeight*scale*gap) ||
			(isLast
			&& _mouseX>baseX+x*imageWidth*scale
			&& _mouseX<baseX+(x+1)*imageWidth*scale
			&& _mouseY>baseY+y*imageHeight*scale*gap
			&& _mouseY<baseY+y*imageHeight*scale*gap+imageHeight*scale)) {
			//this card is clicked
			if (isOpen) isSelect = true;
			
			if (!isOpen) {
				if (isLast) {
					isOpen=true;
				} else {
					MainPanel.cardManager.outputString.AddMes("このカードは開けません");
				}
			}
			
		} else {
			//this card is NOT clicled
			isSelect=false;
		}
		
		/*
		if (!isLast) {
			if (_mouseX>baseX+x*imageWidth*scale
				&& _mouseX<baseX+(x+1)*imageWidth*scale
				&& _mouseY>baseY+y*imageHeight*scale*gap
				&& _mouseY<baseY+(y+1)*imageHeight*scale*gap) {
				isSelect = !isSelect;
			} else {
				isSelect = false;
			}
		} else {
			if (_mouseX>baseX+x*imageWidth*scale
				&& _mouseX<baseX+(x+1)*imageWidth*scale
				&& _mouseY>baseY+y*imageHeight*scale*gap
				&& _mouseY<baseY+y*imageHeight*scale*gap+imageHeight*scale) {
				isSelect = !isSelect;
			} else {
				isSelect = false;
			}
		}*/
		
	}
	
	/* call from CardManager.Update */
	public boolean CheckOnMouse(int _mouseX,int _mouseY) {
		onMouse = false;
		if (!isLast) {
			if (_mouseX>baseX+x*imageWidth*scale
				&& _mouseX<baseX+(x+1)*imageWidth*scale
				&& _mouseY>baseY+y*imageHeight*scale*gap
				&& _mouseY<baseY+(y+1)*imageHeight*scale*gap) {
				onMouse = true;
			}
		} else {
			if (_mouseX>baseX+x*imageWidth*scale
				&& _mouseX<baseX+(x+1)*imageWidth*scale
				&& _mouseY>baseY+y*imageHeight*scale*gap
				&& _mouseY<baseY+y*imageHeight*scale*gap+imageHeight*scale) {
				onMouse = true;
			}
		}
		return onMouse;
	}
	
	/* call from CardManager.AllCardsDraw */
	public void Draw(Graphics g) {
		
		if (invisible) return;
		
		//盤面上にある
        if (x!=-1) {
            g.drawImage(
    			(isOpen ? frontImage : CardManager.backImage),
    			(int)(baseX+x*imageWidth*scale), (int)(baseY+y*imageHeight*scale*gap),
    			(int)(baseX+(x+1)*imageWidth*scale),(int)(baseY+y*imageHeight*scale*gap+imageHeight*scale),
    			0,0,imageWidth,imageHeight,
    			null);
        }
        if (isSelect) {
            g.drawImage(
    			CardManager.selectImage,
    			(int)(baseX+x*imageWidth*scale), (int)(baseY+y*imageHeight*scale*gap),
    			(int)(baseX+(x+1)*imageWidth*scale),(int)(baseY+y*imageHeight*scale*gap+imageHeight*scale),
    			0,0,imageWidth,imageHeight,
    			null);
        } else if (onMouse) {
        	g.drawImage(
        		CardManager.onMouseImage,
        		(int)(baseX+x*imageWidth*scale), (int)(baseY+y*imageHeight*scale*gap),
        		(int)(baseX+(x+1)*imageWidth*scale),(int)(baseY+y*imageHeight*scale*gap+imageHeight*scale),
        		0,0,imageWidth,imageHeight,
        		null);
        }
        if (onMouse) {
        	g.drawImage(
        		(isOpen ? frontImage : CardManager.backImage),
        		(int)(baseX+imageWidth*scale*7.5), 20,
        		(int)(baseX+imageWidth*scale*7.5+imageWidth),20+imageHeight,
        		0,0,imageWidth,imageHeight,
        		null);
        }
        
        if (isOpen && openingEffect>0) {
        	openingEffect--;
        }
        if (openingEffect==10 || openingEffect==9) {
        	g.drawImage(
            		CardManager.openImage,
            		(int)(baseX+x*imageWidth*scale), (int)(baseY+y*imageHeight*scale*gap),
            		(int)(baseX+(x+1)*imageWidth*scale),(int)(baseY+y*imageHeight*scale*gap+imageHeight*scale),
            		imageWidth,imageHeight,imageWidth*2,imageHeight*2,
            		null);
        }
        if (openingEffect==8 || openingEffect==7) {
        	g.drawImage(
            		CardManager.openImage,
            		(int)(baseX+x*imageWidth*scale), (int)(baseY+y*imageHeight*scale*gap),
            		(int)(baseX+(x+1)*imageWidth*scale),(int)(baseY+y*imageHeight*scale*gap+imageHeight*scale),
            		0,imageHeight,imageWidth,imageHeight*2,
            		null);
        }
        if (openingEffect==6 || openingEffect==5) {
        	g.drawImage(
            		CardManager.openImage,
            		(int)(baseX+x*imageWidth*scale), (int)(baseY+y*imageHeight*scale*gap),
            		(int)(baseX+(x+1)*imageWidth*scale),(int)(baseY+y*imageHeight*scale*gap+imageHeight*scale),
            		imageWidth*2,0,imageWidth*3,imageHeight,
            		null);
        }
        if (openingEffect==4 || openingEffect==3) {
        	g.drawImage(
            		CardManager.openImage,
            		(int)(baseX+x*imageWidth*scale), (int)(baseY+y*imageHeight*scale*gap),
            		(int)(baseX+(x+1)*imageWidth*scale),(int)(baseY+y*imageHeight*scale*gap+imageHeight*scale),
            		imageWidth,0,imageWidth*2,imageHeight,
            		null);
        }
        if (openingEffect==2 || openingEffect==1) {
        	g.drawImage(
            		CardManager.openImage,
            		(int)(baseX+x*imageWidth*scale), (int)(baseY+y*imageHeight*scale*gap),
            		(int)(baseX+(x+1)*imageWidth*scale),(int)(baseY+y*imageHeight*scale*gap+imageHeight*scale),
            		0,0,imageWidth,imageHeight,
            		null);
        }
        
	}

	/* call from CollectMark.Draw */
	public boolean DrawToCMark(Graphics g,int _x,int _y) {
		
		if (invisible) return false;
		
		g.drawImage(
    		frontImage,
    		_x, _y,
    		(int)(_x+imageWidth*scale),(int)(_y+imageHeight*scale),
    		0,0,imageWidth,imageHeight,
    		null);
		
		return true;
	}
	
	/*call from Effect.Draw */
	public void DrawToEffect(Graphics g,int _x,int _y) {
		g.drawImage(
    		frontImage,
    		_x, _y,
    		(int)(_x+imageWidth*scale),(int)(_y+imageHeight*scale),
    		0,0,imageWidth,imageHeight,
    		null);
	}
}
