import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {
	
	static final int SCREEN_WIDTH = 650;
	static final int SCREEN_HEIGHT = 650;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int delay = 115;
	int x[] = new int[GAME_UNITS];
	int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten, appleX, appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame()
	{
		newApple();
		running = true;
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) 
		{
			/*
			//grid lines
			for(int i = 0; i <= SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
			//populating apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			//snake
			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) {
						g.setColor(new Color(53, 156, 80));
						g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);}
				else {
					//g.setColor(new Color(19, 110, 43));
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);}
			}
			g.setColor(Color.white);
			g.setFont(new Font("Monospace", Font.BOLD, 30));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("SCORE: "+applesEaten, (SCREEN_WIDTH-metrics.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize());
		}
		else
		{
			gameOver(g);
		}
	}	
	
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY  = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
	}
	
	public void move()
	{
		//shifting array
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		
		//action performed works according to this 
		switch(direction) {
		case 'U' : 
			y[0] = y[0] - UNIT_SIZE;
			break;
		
		case 'D' :
			y[0] = y[0] + UNIT_SIZE;
			break;
			
		case 'L' :
			x[0] = x[0] - UNIT_SIZE;
			break;
			
		case 'R' :
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApples() {
		if(x[0] == appleX && y[0] == appleY) {
			applesEaten++;
			bodyParts++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		
		//checking collision with its own body
		for(int i = bodyParts; i > 0; i--) {
			if(x[0] == x[i] && y[0] == y[i]) {
				running = false;
			}
		}
		//checking if head collides with left or right boundaries
		if(x[0] < 0) {
			running = false;
		}
		if(x[0] >= SCREEN_WIDTH) {
			running = false;
		}
		//checking if head collides with top or bottom boundaries
		if(y[0] < 0) {
			running = false;
		}
		if(y[0] >= SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		//display score on final screen
		g.setColor(Color.white);
		g.setFont(new Font("Monospace", Font.BOLD, 30));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("SCORE: "+applesEaten, (SCREEN_WIDTH-metrics1.stringWidth("SCORE: "+applesEaten))/2, (SCREEN_HEIGHT/2)+2*g.getFont().getSize());
		//game over message
		g.setColor(Color.white);
		g.setFont(new Font("Monospace", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH-metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApples();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
			break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
			break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
			break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
			break;
			}
		}
	}
}
