package cardsFortuneTelling;

import java.awt.*;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.Graphics2D;

public class OutputString {
	
	String[] message = new String[3];
	
	/* constructor */
	public OutputString() {
		message[0]="";
		message[1]="";
		message[2]="";
	}
	
	/* call from Cards.CheckClick */
	public void AddMes(String str) {
		message[2] = message[1];
		message[1] = message[0];
		message[0] = str;
	}
	
	/* call from CardManager.Draw */
	public void Write(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setFont(new Font("ＭＳ Ｐゴシック",Font.PLAIN,30));
		g2.setColor(new Color(0,0,0));
		//g2.drawString(message[0], 70, 900);
		
		g2.setFont(new Font("ＭＳ Ｐゴシック",Font.PLAIN,28));
		g2.setColor(new Color(20,30,15));
		//g2.drawString(message[1], 70, 928);
		
		g2.setFont(new Font("ＭＳ Ｐゴシック",Font.PLAIN,26));
		g2.setColor(new Color(40,60,30));
		//g2.drawString(message[2], 70, 954);
	}
	

}
