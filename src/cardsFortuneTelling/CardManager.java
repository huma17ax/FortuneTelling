package cardsFortuneTelling;

import java.awt.*;
//import java.awt.BasicStroke;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.image.*;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CardManager {

	public Cards[] card = new Cards[52];
    public OutputString outputString = new OutputString();
    public LineMark[] lineMark = new LineMark[7];
    public CollectMark[] collectMark = new CollectMark[4];
	
	public static BufferedImage backImage;
	public static BufferedImage selectImage;
	public static BufferedImage onMouseImage;
	public static BufferedImage openImage;;
	
	public int selectID,onMouseID;
	public boolean moving=false;
	public int rawLength=7;
	public boolean dragging=false;
	
	public boolean finish = false;
	
	/* constructor */
	public CardManager() {
		
		try {
			backImage = ImageIO.read(new File("image/trump/z01.png"));
			selectImage = ImageIO.read(new File("image/trump/z04.png"));
			onMouseImage = ImageIO.read(new File("image/trump/z05.png"));
			openImage = ImageIO.read(new File("image/trump/open.png"));
		} catch(IOException e) {
			
		}
		
		//
		for (int i=0;i<7;i++) {
			lineMark[i] = new LineMark(i);
		}
		//
		collectMark[0] = new CollectMark(cardType.diamond);
		collectMark[1] = new CollectMark(cardType.heart);
		collectMark[2] = new CollectMark(cardType.club);
		collectMark[3] = new CollectMark(cardType.spade);
		
		
		//カードの初期化
        for (int i=0;i<4;i++) {
        	for (int j=0;j<13;j++) {
            	if (i==0) card[i*13+j] = new Cards(cardType.diamond,j+1);
            	if (i==1) card[i*13+j] = new Cards(cardType.heart,j+1);
            	if (i==2) card[i*13+j] = new Cards(cardType.club,j+1);
            	if (i==3) card[i*13+j] = new Cards(cardType.spade,j+1);
        	}
        }
        
        //カードのシャッフル
        
        for (int i=0;i<3;i++) {
    		int num = (int)(Math.random()*52);
    		if (card[num].x==0) {
    			card[num].x=-1;card[num].y=-1;
    		}
    	}
        
        for (int i=0;i<52;i++) {
        	if (card[i].x!=-1) {
        	
        		card[i].x=(int)(Math.random()*7);// 0.0~6.9を切り捨て
        		card[i].y=(int)(Math.random()*7);

        		for (int j=0;j<i;j++) {
        			if (card[i].x==card[j].x
        					&& card[i].y==card[j].y) {
        				j=-1;
        				if (card[i].x==6) {
        					card[i].x=0;card[i].y++;
        					if (card[i].y==7) card[i].y=0;
        				} else {
        					card[i].x++;
        				}
        			}
        		}
        		
        	}
        }/*
        int xxx=0;
        int yyy=0;
        for (int i=51;i>=3;i--,xxx++) {
        	card[i].x=xxx;
        	card[i].y=yyy;
        	if (xxx==6) {xxx=-1;yyy++;}
        }
        
        card[2].x=-1;card[2].y=-1;
        card[1].x=-1;card[1].y=-1;
        card[0].x=-1;card[0].y=-1;
        */
        
        //指定カードの裏返し
        for (Cards card:card) {
        	if (card.x>=2 && card.x<=4
            	&& card.y>=2 && card.y<=4) {
            	card.isOpen=false;
            	card.openingEffect=11;
            }
        }
        
        //最下カードを手前設定
        for (Cards card:card) {
        	if (card.y==6) card.isLast=true;
        }
        
	}
	
	/* call from MainPanel.run */
	public void Update() {
		
		if (finish) return;
		
		for (Cards card : card ) {
			card.Update();
		}
		
		if (moving==false) {
			onMouseID=-1;
			for (int i=0;i<52;i++) {
				if (card[i].CheckOnMouse(MainPanel.inputMouse.mouseX,MainPanel.inputMouse.mouseY)) {
					onMouseID=i;
				}
			}
		}
		
		for (LineMark mark:lineMark) {
			mark.CheckOnMouse(MainPanel.inputMouse.mouseX, MainPanel.inputMouse.mouseY);
		}
		for (CollectMark mark : collectMark) {
			mark.CheckOnMouse(MainPanel.inputMouse.mouseX, MainPanel.inputMouse.mouseY);
		}
		
		//終了処理
		boolean flag = true;
		for (CollectMark mark : collectMark) {
			if (mark.collectedNum!=13) flag=false;
		}
		if (flag) finish =true;
		
		//余りカードを自動追加
		/*
		for (int i=0;i<52;i++) {
			if (card[i].x==-1 && card[i].isCollected==false) {
	        	
				boolean flag=false;
				for (int j=0;j<52;j++) {
					if (card[j].isLast && card[j].isOpen && card[j].type==card[i].type
						&& card[j].number==card[i].number+1) {
						card[i].x=card[j].x;
						card[i].y=card[j].y+1;
						card[j].isLast=false;
						card[i].isLast=true;
						flag=true;
						//長さ更新
						rawLength = 0;
						for (Cards card:card) {
							if (card.y > rawLength) rawLength=card.y;
						}
						rawLength++;
						//
						break;
					}
				}
				if (flag) break;
				
				for (CollectMark mark : collectMark) {
					if (mark.type == card[i].type && mark.collectedNum == card[i].number-1) {
						card[i].isCollected=true;
						mark.collectedNum++;
						break;
					}
				}
				
			}
		}*/
	}
	
	/* call from Input.mousePressed */
	public void MoveRemCard(int _mouseX, int _mouseY) {
		
		int surplus = 0;
		
		for (int i=0;i<52;i++) {
			if (card[i].x==-1 && card[i].isCollected==false) {
				
				if (_mouseX > (int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100)
					&& _mouseX < (int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100+100)
					&& _mouseY > 335
					&& _mouseY < 485) {
					
					//13以外の盤面出し
					boolean flag=false;
					for (int j=0;j<52;j++) {
						if (card[i].number!=13
							&& card[j].isLast && card[j].isOpen && card[j].type==card[i].type
							&& card[j].number==card[i].number+1) {
							MainPanel.effect.AddElementRem(card[i].type, card[i].number,(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100-50),260, card[j].x, card[j].y+1);
							card[i].x=card[j].x;
							card[i].y=card[j].y+1;
							card[j].isLast=false;
							card[i].isLast=true;
							card[i].invisible=true;
							flag=true;
							//長さ更新
							rawLength = 0;
							for (Cards card:card) {
								if (card.y > rawLength) rawLength=card.y;
							}
							rawLength++;
							//
							break;
						}
					}
					if (flag) break;
					
					//13の盤面出し
					flag=false;
					for (LineMark mark : lineMark) {
						if (card[i].number==13
							&& mark.isEmpty) {
							MainPanel.effect.AddElementRem(card[i].type, card[i].number,(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100-50),260, mark.raw, 0);
							card[i].x=mark.raw;
							card[i].y=0;
							card[i].isLast=true;
							card[i].invisible=true;
							flag=true;
							//長さ更新
							rawLength = 0;
							for (Cards card:card) {
								if (card.y > rawLength) rawLength=card.y;
							}
							rawLength++;
							//
							break;
						}
					}
					if (flag) break;
					
					//山札への移動
					for (CollectMark mark : collectMark) {
						if (mark.type == card[i].type && mark.collectedNum == card[i].number-1) {
							MainPanel.effect.AddElementColFromRem(card[i].type, card[i].number,(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100-50),260);
							card[i].isCollected=true;
							card[i].invisible=true;
							mark.collectedNum++;
							break;
						}
					}
					
				}
				
				
				surplus++;
			}
		}
		
	}
	
	/* call from anywhere in this project */
	public Cards IdentifyCard(cardType _type,int _num) {
		
		for (Cards card : card) {
			
			if (card.number == _num
				&& card.type == _type) {
				return card;
			}
			
		}
		
		return null;
	}
	
	/*call from Input.mouseReleaseed*/
	public void MoveCards() {
		//移動可能判定
		if (selectID>=0 && selectID<13*4
			&& onMouseID>=0 && onMouseID<13*4
			&& selectID!=onMouseID
			&& card[selectID].x!=card[onMouseID].x
			&& card[selectID].x!=-1 && card[selectID].y!=-1
			&& card[selectID].type==card[onMouseID].type
			&& card[selectID].number==card[onMouseID].number-1
			&& card[onMouseID].isLast==true) {

			//後ろに裏カードが続いているか判定
			for (int i=0;i<52;i++) {
				if (card[i].x==card[selectID].x && card[i].y>card[selectID].y && card[i].isOpen==false) return;
			}

			//末端判定変更
			card[onMouseID].isLast = false;
			for (int i=0;i<52;i++) {
				if (card[i].x==card[selectID].x && card[i].y==card[selectID].y-1) {
					card[i].isLast=true;
					break;
				}
			}
			//移動
			int ID = selectID;
			for (int i=1;;i++) {
				if (card[ID].isLast) {
					MainPanel.effect.AddElementBod(card[ID].type, card[ID].number, card[ID].x, card[ID].y, card[onMouseID].x, card[onMouseID].y+i);
					//System.out.println("ID: "+ID+" onMouseID: "+onMouseID);
					card[ID].x=card[onMouseID].x;
					card[ID].y=card[onMouseID].y+i;
					card[ID].invisible=true;
					break;
				}
				for (int j=0;j<52;j++) {
					if (card[j].x==card[ID].x && card[j].y==card[ID].y+1) {
						MainPanel.effect.AddElementBod(card[ID].type, card[ID].number, card[ID].x, card[ID].y, card[onMouseID].x, card[onMouseID].y+i);
						card[ID].x=card[onMouseID].x;
						card[ID].y=card[onMouseID].y+i;
						card[ID].invisible=true;
						ID=j;
						break;
					}
				}
			}


			//長さ更新
			rawLength = 0;
			for (Cards card:card) {
				if (card.y > rawLength) rawLength=card.y;
			}
			rawLength++;
			
			for (LineMark mark : lineMark) {
				mark.CheckEmpty();
			}
			
		} else {
			for (LineMark mark:lineMark) {
				mark.AppMoveCards();
			}
			for (CollectMark mark:collectMark) {
				mark.AppCollectCards();
			}
			outputString.AddMes("ここへは動かせません");
		}
		
	}
	
	/* call from LineMark.AppMoveCards */
	public void MoveCardsToEmpty(int _raw) {
		if (selectID==-1) return;
		if (card[selectID].number==13) {
			
			//後ろに裏カードが続いているか判定
			for (int i=0;i<52;i++) {
				if (card[i].x==card[selectID].x && card[i].y>card[selectID].y && card[i].isOpen==false) return;
			}
			
			//末端判定変更
			for (int i=0;i<52;i++) {
				if (card[i].x==card[selectID].x && card[i].y==card[selectID].y-1) {
					card[i].isLast=true;
					break;
				}
			}
			//移動
			int ID = selectID;
			for (int i=0;;i++) {
				if (card[ID].isLast) {
					MainPanel.effect.AddElementBod(card[ID].type, card[ID].number, card[ID].x, card[ID].y, _raw, i);
					card[ID].x=_raw;
					card[ID].y=i;
					card[ID].invisible=true;
					break;
				}
				for (int j=0;j<52;j++) {
					if (card[j].x==card[ID].x && card[j].y==card[ID].y+1) {
						MainPanel.effect.AddElementBod(card[ID].type, card[ID].number, card[ID].x, card[ID].y, _raw, i);
						card[ID].x=_raw;
						card[ID].y=i;
						card[ID].invisible=true;
						ID=j;
						break;
					}
				}
			}
			//長さ更新
			rawLength = 0;
			for (Cards card:card) {
				if (card.y > rawLength) rawLength=card.y;
			}
			rawLength++;
			
			for (LineMark mark : lineMark) {
				mark.CheckEmpty();
			}
			
		}
	}
	
	/* call from CollectMark.AppCollectCards */
	public int CollectCards(cardType _type,int _num) {
		if (selectID==-1) return 0;
		int ID = selectID;
		int add=0;
		boolean breakFlag=false;
		for (int i=0 ; !breakFlag ; i++){
			
			if (card[ID].type==_type
				&& card[ID].number==_num+1+i
				&& card[ID].isOpen==true
				&& card[ID].isLast==true) {
				for (int j=0;j<52;j++) {
					if (card[j].x==card[ID].x && card[j].y==card[ID].y-1) {
						MainPanel.effect.AddElementCol(card[ID].type, card[ID].number, card[ID].x, card[ID].y);
						card[ID].x=-1;
						card[ID].y=-1;
						card[ID].invisible=true;
						card[ID].isCollected=true;
						add++;
						card[j].isLast=true;
						ID=j;
						break;
					} else if (j==51) {
						MainPanel.effect.AddElementCol(card[ID].type, card[ID].number, card[ID].x, card[ID].y);
						card[ID].x=-1;
						card[ID].y=-1;
						card[ID].invisible=true;
						card[ID].isCollected=true;
						add++;
						breakFlag=true;
					}
				}
			} else {
				break;
			}
			
		}
		
		//長さ更新
		rawLength = 0;
		for (Cards card:card) {
			if (card.y > rawLength) rawLength=card.y;
		}
		rawLength++;
		
		for (LineMark mark : lineMark) {
			mark.CheckEmpty();
		}
		
		return add;
	}
	
	/* call from Input.mousePressed */
	public void CheckSelectCard() {
		selectID=-1;
		for (int i=0;i<52;i++) {
			if (card[i].isSelect) {
				selectID=i;
			}
		}
	}
	
	/* call from MainPanel.paintComponent */
	public void Draw(Graphics g) {
		
		for (LineMark mark : lineMark) {
			mark.Draw(g);
		}
		for (CollectMark mark : collectMark) {
			mark.Draw(g);
		}
		
		AllCardsDraw(g);
		
		outputString.Write(g);
		
		if (dragging && selectID!=-1 && card[selectID].x!=-1) {

			Graphics2D g2 = (Graphics2D)g;
			BasicStroke lineStroke = new BasicStroke(4.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER);
			g2.setStroke(lineStroke);
			g2.setColor(new Color(113,255,255));
			
			g2.draw(new Line2D.Double(
					Cards.baseX+(card[selectID].x+0.5)*Cards.imageWidth*Cards.scale,
					Cards.baseY+(card[selectID].y+0.5)*Cards.imageHeight*Cards.scale*Cards.gap,
					MainPanel.inputMouse.mouseX,
					MainPanel.inputMouse.mouseY));
			
		}
		
		if (finish) {
			Graphics2D g2 = (Graphics2D)g;
		
			g2.setFont(new Font("ＭＳ Ｐゴシック",Font.PLAIN,230));
			g2.setColor(new Color(255,0,0));
			g2.drawString("Clear!!", 175, 300);
		}
	}
	
	/* call from CardManager.Draw */
	public void AllCardsDraw(Graphics g) {
		
		for (int i=0;i<rawLength;i++) {
			for (Cards card:card) {
				if (card.y==i) {
					card.Draw(g);
				}
			}
		}
		
		
		int surplus=0;
		for (int i=0;i<52;i++) {
			if (card[i].x==-1 && card[i].isCollected==false) {
	        	g.drawImage(
    				card[i].frontImage,
    				(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100), 335,
    				(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100+100),485,
    				0,0,Cards.imageWidth,Cards.imageHeight,
    				null);
	        	
	        	for (int j=0;j<52;j++) {
					if (card[i].number!=13
						&& card[j].isLast && card[j].isOpen && card[j].type==card[i].type
						&& card[j].number==card[i].number+1) {
					
						g.drawImage(
			    				selectImage,
			    				(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100), 335,
			    				(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100+100),485,
			    				0,0,Cards.imageWidth,Cards.imageHeight,
			    				null);
						
					}
				}
	        	
	        	for (LineMark mark : lineMark) {
	        		if (card[i].number==13 && mark.isEmpty) {
	        			g.drawImage(
			    				selectImage,
			    				(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100), 335,
			    				(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100+100),485,
			    				0,0,Cards.imageWidth,Cards.imageHeight,
			    				null);
	        			break;
	        		}
	        	}
	        	
	        	for (CollectMark mark : collectMark) {
					if (mark.type == card[i].type && mark.collectedNum == card[i].number-1) {
						g.drawImage(
			    				selectImage,
			    				(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100), 335,
			    				(int)(Cards.baseX+20+Cards.imageWidth*Cards.scale*7+surplus*100+100),485,
			    				0,0,Cards.imageWidth,Cards.imageHeight,
			    				null);
						break;
					}
				}
	        	
				surplus++;
			}
		}
		
	}

}
