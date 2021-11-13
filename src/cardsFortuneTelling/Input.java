package cardsFortuneTelling;

import java.awt.event.*;

public class Input implements MouseListener,MouseMotionListener,MouseWheelListener {

	public int mouseX = 0,mouseY = 0;
	public boolean stop=false;
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if (!stop) {
			for (Cards card : MainPanel.cardManager.card) {
				card.CheckClick(mouseX, mouseY);
			}
			MainPanel.cardManager.CheckSelectCard();
			MainPanel.cardManager.MoveRemCard(mouseX,mouseY);
		}
	}
	public void mouseReleased(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if (!stop) {
			MainPanel.cardManager.dragging=false;
			MainPanel.cardManager.moving=true;
			MainPanel.cardManager.MoveCards();
			MainPanel.cardManager.moving=false;
			for (Cards card:MainPanel.cardManager.card) {
				card.isSelect=false;
			}
		}
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if (!stop) MainPanel.cardManager.dragging=true;
	}
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!stop) {
			if (e.getWheelRotation()>0
				&& Cards.baseY+(MainPanel.cardManager.rawLength-1)*Cards.imageHeight*Cards.scale*Cards.gap+Cards.imageHeight*Cards.scale
				>MainPanel.HEIGHT) {
				Cards.baseY-=e.getWheelRotation()*20;
			}
			if (e.getWheelRotation()<0) Cards.baseY-=e.getWheelRotation()*20;
			if (Cards.baseY>10) Cards.baseY=10;
		}
	}

}
