package cardsFortuneTelling;

import java.awt.Graphics;

class Element{
	cardType type;
	int number;
	double x,y;
	int initPosX,initPosY;
	int endPosX,endPosY;
	int speed;
	double angle;
	double distance;
	
	Element(){
		type=cardType.heart;//何でも
		number=-1;
		x=0;y=0;initPosX=0;initPosY=0;endPosX=0;endPosY=0;
		speed=4;
		angle=0;
		distance=0;
	}
	void Subst(Element _elm) {
		type=_elm.type;
		number=_elm.number;
		x=_elm.x;y=_elm.y;
		initPosX=_elm.initPosX;initPosY=_elm.initPosY;
		endPosX=_elm.endPosX;endPosY=_elm.endPosY;
		speed=_elm.speed;angle=_elm.angle;
		distance=_elm.distance;
	}
}

public class Effect {

	Element[] element = new Element[52];
	
	public int active = 0;
	
	/* constructor */
	public Effect() {
		for (int i=0;i<52;i++) {
			element[i] = new Element(); 
		}
	}
	
	/* call from MainPanel.run */
	public void Update() {
		
		for (int i=0;i<active;i++) {
			element[i].x+=element[i].speed*Math.cos(element[i].angle);
			element[i].y+=element[i].speed*Math.sin(element[i].angle);
			
			if (Math.sqrt((element[i].endPosX-element[i].x)*(element[i].endPosX-element[i].x)+(element[i].endPosY-element[i].y)*(element[i].endPosY-element[i].y))>element[i].distance) {
				ReleaseElm(i);
			} else {
				element[i].distance=Math.sqrt((element[i].endPosX-element[i].x)*(element[i].endPosX-element[i].x)+(element[i].endPosY-element[i].y)*(element[i].endPosY-element[i].y));
			}
			
		}
		
	}
	
	/* call from Effect.Update */
	public void ReleaseElm(int _num) {
		for (int i=_num;i<active;i++) {
			if (i==51) break;
			element[i].Subst(element[i+1]);
		}
		
		for (Cards card:MainPanel.cardManager.card) {
			if (card.number==element[_num].number && card.type==element[_num].type) {
				card.invisible=false;
			}
		}
		
		active--;
	}
	
	public void Draw(Graphics g) {
		for (int i=0;i<active;i++) {
			
			for (Cards card:MainPanel.cardManager.card) {
				if (card.number==element[i].number && card.type==element[i].type) {
					card.DrawToEffect(g, (int)element[i].x, (int)element[i].y);
				}
			}
			
		}
	}
	
	//回収用
	public void AddElementCol(cardType _type,int _num,int board_stX,int board_stY) {
		element[active].number=_num;
		element[active].type=_type;
		element[active].initPosX=(int)(Cards.baseX+board_stX*Cards.imageWidth*Cards.scale);
		element[active].initPosY=(int)(Cards.baseY+board_stY*Cards.imageHeight*Cards.scale*Cards.gap);
		
		if (_type==cardType.club) {
			element[active].endPosX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*7.5);
			element[active].endPosY=500;
		}
		if (_type==cardType.diamond) {
			element[active].endPosX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*8.5)+10;
			element[active].endPosY=500;
		}
		if (_type==cardType.heart) {
			element[active].endPosX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*7.5);
			element[active].endPosY=(int)(500+Cards.imageHeight*Cards.scale)+10;
		}
		if (_type==cardType.spade) {
			element[active].endPosX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*8.5)+10;
			element[active].endPosY=(int)(500+Cards.imageHeight*Cards.scale)+10;
		}
		
		element[active].angle = Math.atan2(element[active].endPosY-element[active].initPosY, element[active].endPosX-element[active].initPosX);
		
		element[active].x = element[active].initPosX;
		element[active].y = element[active].initPosY;
		element[active].distance=Math.sqrt((element[active].endPosX-element[active].initPosX)*(element[active].endPosX-element[active].initPosX)+(element[active].endPosY-element[active].initPosY)*(element[active].endPosY-element[active].initPosY));
		active++;
	}
	public void AddElementColFromRem(cardType _type,int _num,int coor_stX,int coor_stY) {
		element[active].number=_num;
		element[active].type=_type;
		element[active].initPosX=coor_stX;
		element[active].initPosY=coor_stY;
		
		if (_type==cardType.club) {
			element[active].endPosX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*7.5);
			element[active].endPosY=500;
		}
		if (_type==cardType.diamond) {
			element[active].endPosX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*8.5)+10;
			element[active].endPosY=500;
		}
		if (_type==cardType.heart) {
			element[active].endPosX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*7.5);
			element[active].endPosY=(int)(500+Cards.imageHeight*Cards.scale)+10;
		}
		if (_type==cardType.spade) {
			element[active].endPosX=(int)(Cards.baseX+Cards.imageWidth*Cards.scale*8.5)+10;
			element[active].endPosY=(int)(500+Cards.imageHeight*Cards.scale)+10;
		}
		
		element[active].angle = Math.atan2(element[active].endPosY-element[active].initPosY, element[active].endPosX-element[active].initPosX);
		
		element[active].x = element[active].initPosX;
		element[active].y = element[active].initPosY;
		element[active].distance=Math.sqrt((element[active].endPosX-element[active].initPosX)*(element[active].endPosX-element[active].initPosX)+(element[active].endPosY-element[active].initPosY)*(element[active].endPosY-element[active].initPosY));
		active++;
	}
	
	//盤面内移動用
	public void AddElementBod(cardType _type,int _num,int board_stX,int board_stY,int board_edX,int board_edY) {
		element[active].number=_num;
		element[active].type=_type;
		element[active].initPosX=(int)(Cards.baseX+board_stX*Cards.imageWidth*Cards.scale);
		element[active].initPosY=(int)(Cards.baseY+board_stY*Cards.imageHeight*Cards.scale*Cards.gap);
		
		
		element[active].endPosX=(int)(Cards.baseX+board_edX*Cards.imageWidth*Cards.scale);
		element[active].endPosY=(int)(Cards.baseY+board_edY*Cards.imageHeight*Cards.scale*Cards.gap);
		
		element[active].angle = Math.atan2(element[active].endPosY-element[active].initPosY, element[active].endPosX-element[active].initPosX);
		
		element[active].x = element[active].initPosX;
		element[active].y = element[active].initPosY;
		element[active].distance=Math.sqrt((element[active].endPosX-element[active].initPosX)*(element[active].endPosX-element[active].initPosX)+(element[active].endPosY-element[active].initPosY)*(element[active].endPosY-element[active].initPosY));
		active++;
	}
	//余りカード用
	public void AddElementRem(cardType _type,int _num,int coor_stX,int coor_stY,int board_edX,int board_edY) {
		element[active].number=_num;
		element[active].type=_type;
		element[active].initPosX=coor_stX;
		element[active].initPosY=coor_stY;
		
		
		element[active].endPosX=(int)(Cards.baseX+board_edX*Cards.imageWidth*Cards.scale);
		element[active].endPosY=(int)(Cards.baseY+board_edY*Cards.imageHeight*Cards.scale*Cards.gap);
		
		element[active].angle = Math.atan2(element[active].endPosY-element[active].initPosY, element[active].endPosX-element[active].initPosX);
		
		element[active].x = element[active].initPosX;
		element[active].y = element[active].initPosY;
		element[active].distance=Math.sqrt((element[active].endPosX-element[active].initPosX)*(element[active].endPosX-element[active].initPosX)+(element[active].endPosY-element[active].initPosY)*(element[active].endPosY-element[active].initPosY));
		active++;
	}
	
	
}
