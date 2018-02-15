package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	public static final int LARGURA = 600;
	public static final int ALTURA = 600;
	
	//Render
	private Graphics2D g2d;
	private BufferedImage image;
	
	//Game Loop
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	//Game Coisas
	private final int SIZE = 10;
	private Entity head, apple;
	private ArrayList<Entity> snake;
	private int ponto;
        private int level;
        private boolean gameover;
	
	//Movimento
	private int dx,dy;
	//key input
	private boolean up, down, right,left,start;
	
	public GamePanel() {
		setPreferredSize(new Dimension(LARGURA, ALTURA));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	
	public void addNotify() {
		super.addNotify();
		thread = new Thread(this);
		thread.start();
	}
	
	private void setFPS(int fps) {
		targetTime = 800/ fps;
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int x = e.getKeyCode();
		
		if(x == KeyEvent.VK_UP) up = true;
		if(x == KeyEvent.VK_DOWN) down = true;
		if(x == KeyEvent.VK_LEFT) left = true;
		if(x == KeyEvent.VK_RIGHT) right = true;
		if(x == KeyEvent.VK_ENTER) start = true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int x = e.getKeyCode();
		
		if(x == KeyEvent.VK_UP) up = false;
		if(x == KeyEvent.VK_DOWN) down = false;
		if(x == KeyEvent.VK_LEFT) left = false;
		if(x == KeyEvent.VK_RIGHT) right = false;
		if(x == KeyEvent.VK_ENTER) start = false;
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void run() {
		
		if(running) return;
		
		init();
		
		long startTime;
		long elapsed;
		long wait;
		
		while (running) {
			
			startTime = System.nanoTime();
			
			update() ;
			requestRender();
			
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			if(wait > 0) {
				try {
					Thread.sleep(wait);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void init() {

		image = new BufferedImage(LARGURA, ALTURA, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		
		running = true;
		setUplevel();
                
                gameover = false;
                level = 1;
                
		setFPS(level * 10);
 
	}
	
	private void setUplevel() {
		
		snake = new ArrayList<Entity>();
		
		head = new Entity(SIZE);
		head.setPosition(LARGURA / 2, ALTURA / 2);
		snake.add(head);
		
		for (int i = 1; i < 3; i++) {
			
			Entity e = new Entity(SIZE);
			e.setPosition(head.getX() +( i * SIZE),head.getY());
			snake.add(e);
		}
		apple = new Entity(SIZE);
		setApple();
		ponto = 0;
		
	}
	public void setApple() {
		int x = (int) (Math.random() * (LARGURA - SIZE));
		int y = (int) (Math.random() * (ALTURA - SIZE));
		
		x = x - (x % SIZE);
		y = y - (y % SIZE);
		
		apple.setPosition(x, y);
	}

	private void requestRender() {
	 
		render(g2d);	
		Graphics g = getGraphics();
		g.setColor(Color.white);
		
		g.drawImage(image, 0,0,null);
		g.dispose();
	}

	private void update() {
		if(up && dy == 0) {
			dy = -SIZE;
			dx = 0;
		}
		
		if(down && dy == 0) {
			dy = SIZE;
			dx = 0;
		  }
		if(left && dx == 0) {
			dy = 0;
			dx =  -SIZE;
		  }
		if(right && dx == 0) {
			dy = 0;
			dx =  SIZE;
		  }
		
		if(dx != 0 || dy != 0) {
			for (int i = snake.size() -1 ; i > 0; i--) {
				snake.get(i).setPosition(snake.get(i - 1).getX(), snake.get(i - 1).getY());
				
			}
			head.move(dx, dy);
		}
		
		if(apple.isCollsion(head)) {
			ponto++;
			setApple();
			
			Entity e = new Entity(SIZE);
			e.setPosition(-100,-100);
			snake.add(e);
			
		}
		
		if(head.getX() < 0) head.setX(LARGURA); 
		if(head.getY() < 0) head.setY(ALTURA); 
		if(head.getX() > LARGURA) head.setX(0); 
		if(head.getY() > ALTURA) head.setY(0);
	
	}
	
	public void render(Graphics2D g2d) {
		g2d.clearRect(0, 0, LARGURA, ALTURA);
		
		g2d.setColor(Color.green);
		for(Entity e : snake) {
			e.render(g2d);
		}
		
		g2d.setColor(Color.RED);
		apple.render(g2d) ;
		
		g2d.setColor(Color.white);
		g2d.drawString("Score : " + ponto , 10, 10);
	}

	

}
