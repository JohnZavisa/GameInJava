package com.tutorial.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable
{
	private Menu menu;
	private static final long serialVersionUID = 1550691097823471818L;
	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
	private Thread thread;
	private boolean running = false;
	public static boolean paused = false;
	public int diff = 0; // 0 = norm, 1 = hard
	private Random r; 
	private Handler handler;
	private HUD hud;
	private Spawn spawner;
	
	
	public enum STATE 
	{
		Menu,
		Select,
		Help,
		Game,
		End
	};
	public static STATE gameState = STATE.Menu;
	
	public Game()
	{
		handler = new Handler();
		hud = new HUD();
		menu = new Menu(this, handler, hud);
		this.addKeyListener(new KeyInput(handler, this));
		this.addMouseListener(menu);
		
		AudioPlayer.load();
		
		AudioPlayer.getMusic("music").loop();
		new Window(WIDTH, HEIGHT, "Let's Build a Game!", this);
		
		
		spawner = new Spawn(handler, hud, this);
		
		r = new Random();		
		if(gameState == STATE.Game)
		{
			handler.addObject(new Player(WIDTH/2-32,HEIGHT/2-32, ID.Player, handler));
			handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH), r.nextInt(Game.HEIGHT), ID.BasicEnemy, handler));
		}
		else
		{
			for(int i = 0; i <10; i++)
			{
				handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.MenuParticle, handler));
			}
		}
		
		
	}
	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	public synchronized void stop()
	{
		try 
		{
			thread.join();
			running = false;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run() 
	{
		this.requestFocus();
		long lastTime = System.nanoTime(); // get current time to the nanosecond
		double amountOfTicks = 60.0; // set the number of ticks 
		double ns = 1000000000 / amountOfTicks; // this determines how many times we can devide 60 into 1e9 of nano seconds or about 1 second
		double delta = 0; // change in time (delta always means a change like delta v is change in velocity)
		long timer = System.currentTimeMillis(); // get current time
		int frames = 0; // set frame variable
		while(running)
		{ 
		   long now = System.nanoTime(); // get current time in nonoseconds during current loop
		   delta += (now - lastTime) / ns; // add the amount of change since the last loop
		   lastTime = now;  // set lastTime to now to prepare for next loop
		   while(delta >= 1){
		    // one tick of time has passed in the game this 
		    //ensures that we have a steady pause in our game loop 
		    //so we don't have a game that runs way too fast 
		    //basically we are waiting for  enough time to pass so we 
		    // have about 60 frames per one second interval determined to the nanosecond (accuracy)
		    // once this pause is done we render the next frame
		    tick();  
		    delta--;  // lower our delta back to 0 to start our next frame wait
		   }
		   if(running)
		   {
		    render(); // render the visuals of the game
		   }
		   frames++; // note that a frame has passed
		   if(System.currentTimeMillis() - timer > 1000 )
		   { // if one second has passed
		    timer+= 1000; // add a thousand to our timer for next time
		    //System.out.println("FPS: " + frames); // print out how many frames have happened in the last second
		    frames = 0; // reset the frame count for the next second
		   }
		 }
		  stop(); // no longer running stop the thread
	}
	
	private void tick()
	{
		if(gameState == STATE.Help)
		{
			handler.tick();
			
		}
		if(gameState == STATE.Game)
		{
			if(!paused)
			{
				hud.tick();
				spawner.tick();
				handler.tick();
				
				
				if(HUD.HEALTH <= 0)
				{
					HUD.HEALTH = 100;
					
					gameState = STATE.End;
					
					handler.clearEnemies();
					for(int i = 0; i <10; i++)
					{
						handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.MenuParticle, handler));
					}
					
				}
			}
			
		}
		else if(gameState == STATE.Menu || gameState == STATE.End || gameState == STATE.Select)
		{
			menu.tick();
			handler.tick();
		}
	}
	
	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		if(paused)
		{
			g.setColor(Color.white);
			g.drawString("PAUSED", 100, 100);
		}
		
		if(gameState == STATE.Game)
		{
			hud.render(g);
		}
		
		else if(gameState == STATE.Menu || gameState == STATE.Help || gameState == STATE.End || gameState == STATE.Select)
		{
			menu.render(g);
		}
		//hud.render(g);
		g.dispose();
		bs.show();
	}
	
	public static float clamp(float var, float min, float max)
	{
		if(var >= max) return var = max;
		else if (var <= min) return var = min;
		else return var;
		
	}
	public static void main(String[] args)
	{
		new Game();
	}
}
