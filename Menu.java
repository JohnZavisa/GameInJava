package com.tutorial.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import org.lwjgl.openal.AL;

import com.tutorial.main.Game.STATE;

public class Menu extends MouseAdapter
{
	private Game game1;
	private Random r = new Random();
	Handler handler;
	private HUD hud;
	
	public Menu(Game game1, Handler handler, HUD hud)
	{
		this.game1 = game1;
		this.hud = hud;
		this.handler = handler;
	}
	public void mousePressed(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		if(game1.gameState == STATE.Menu)
		{
			//Play Button
			if(mouseOver(mx, my, 210, 150, 200, 64))
			{
				//game1.gameState = STATE.Game;
				//handler.addObject(new Player(Game.WIDTH/2-32,Game.HEIGHT/2-32, ID.Player, handler));
				//handler.clearEnemies();
				//handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH), r.nextInt(Game.HEIGHT), ID.BasicEnemy, handler));
				Game.gameState = STATE.Select;
				return;
			}

			//help button
			if(mouseOver(mx, my, 210, 250, 200, 64))
			{
				game1.gameState = STATE.Help;
			}
			//Quit Button
			if(mouseOver(mx, my, 210, 350, 200, 64))
			{
				AL.destroy();
				System.exit(1);
			}
			//try again
			if(mouseOver(mx, my, 210, 350, 200, 64))
			{
				handler.clearEnemies();
				System.exit(1);
			}	
		}
		if(game1.gameState == STATE.Select)
		{
			//Normal Button
			if(mouseOver(mx, my, 210, 150, 200, 64))
			{
				game1.gameState = STATE.Game;
				handler.addObject(new Player(Game.WIDTH/2-32,Game.HEIGHT/2-32, ID.Player, handler));
				handler.clearEnemies();
				handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemy, handler));
				game1.diff = 0;
			}

			//Hard button
			if(mouseOver(mx, my, 210, 250, 200, 64))
			{
				game1.gameState = STATE.Game;
				handler.addObject(new Player(Game.WIDTH/2-32,Game.HEIGHT/2-32, ID.Player, handler));
				handler.clearEnemies();
				handler.addObject(new HardEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemy, handler));
				game1.diff = 1;
			}
			//Back Button
			if(mouseOver(mx, my, 210, 350, 200, 64))
			{
				game1.gameState = STATE.Menu;
				return;
			}
		}
	
		
		
		//Back button for help
		if(game1.gameState == STATE.Help)
		{
			if(mouseOver(mx, my, 210, 350, 200, 64))
			{
				game1.gameState = STATE.Menu;
				return;
			}
		}
		//button for back
		if(game1.gameState == STATE.End)
		{
			if(mouseOver(mx, my, 210, 350, 200, 64))
			{
				game1.gameState = STATE.Menu;
				hud.setLevel(1);
				hud.setScore(0);
				
			}
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{
		
	}
	
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height)
	{
		if(mx > x && mx < x + width)
		{
			if(my > y && my < y + height)
			{
				return true;
			}
			else{
				return false;
			}
		}else
		{
			return false;
		}
	}
	
	public void tick()
	{
		
	}
	
	public void render(Graphics g)
	{
		if(game1.gameState == STATE.Menu)
		{
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			
			g.setFont(fnt);
			g.setColor(Color.magenta);
			g.drawString("CHON", 225, 70);
			g.setColor(Color.cyan);
			
			g.setFont(fnt2);
			g.drawRect(210, 150, 200, 64);
			g.drawString("Play", 275, 190);
			
			g.setColor(Color.cyan);
			g.drawRect(210, 250, 200, 64);
			g.drawString("Homey", 260, 290);
			
			g.setColor(Color.cyan);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Quit",  270,  390);
		}
		else if(game1.gameState == STATE.Help)
		{
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("Tracks", 240, 70);
			
			g.setFont(fnt3);
			String newLine = System.lineSeparator();
			g.drawString("Use WASD keys to avoid collision", 50, 95);
			g.drawString("01 – Sleepy Tea", 50, 120);
			g.drawString("02– Waterslide", 50, 140);
			g.drawString("03 – Chonxgoyama: Berry Streets", 50, 160);
			g.drawString("04 – No Signal", 50, 180);
			g.drawString("05 - Checkpoint", 50, 200);
			g.drawString("06 – Chonxlophile: Nayhoo (feat. Masego)", 50, 220);
			g.drawString("07 – Here And There", 50, 240);
			g.drawString("08 – The Space", 50, 260);
			g.drawString("09 – Chonxgiraffage: Feel This Way", 50, 280);
			g.drawString("10 – Continue?", 50, 300);
			g.drawString("11 – Chonxrom: Glitch", 50, 320);
			g.drawString("12 – “Wave Bounce", 50, 340);
		
			g.setFont(fnt2);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Back",  270,  390);
		}else if(game1.gameState == STATE.End)
		{
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			Font fnt3 = new Font("arial", 1, 20);
			
			g.setFont(fnt);
			g.setColor(Color.white);
			g.drawString("Game Over", 180, 70);
			
			g.setFont(fnt3);
			g.drawString("You lost with a score of: " + hud.getScore(), 175, 200);
			
			g.setFont(fnt2);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Try Again",  245,  390);
		}
		else if(game1.gameState == STATE.Select)
		{
			Font fnt = new Font("arial", 1, 50);
			Font fnt2 = new Font("arial", 1, 30);
			
			g.setFont(fnt);
			g.setColor(Color.magenta);
			g.drawString("Select Difficulty", 168, 70);
			g.setColor(Color.cyan);
			
			g.setFont(fnt2);
			g.drawRect(210, 150, 200, 64);
			g.drawString("Normal", 260, 190);
			
			g.setColor(Color.cyan);
			g.drawRect(210, 250, 200, 64);
			g.drawString("Hard", 260, 290);
			
			g.setColor(Color.cyan);
			g.drawRect(210, 350, 200, 64);
			g.drawString("Back",  270,  390);
		}
		
	}
}
