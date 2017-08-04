import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;


public class Game implements Runnable{
	
	private Random rand;
	
	private JFrame frame;
	private Canvas canvas;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
	private Key key;
	int px=20,py=20,mx=0,my=0,ax=40,ay=40;
	int grid = 10,dim = 50;
	
	int[][] snake = new int[256][2];
	int length=2;
	public Game(){
		rand = new Random();
		frame = new JFrame("SNAKE");
		frame.setSize(500,500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		key = new Key();
		frame.addKeyListener(key);
		canvas = new Canvas();
		
		canvas.setPreferredSize(new Dimension(500,500));
		canvas.setMaximumSize(new Dimension(500,500));
		canvas.setMinimumSize(new Dimension(500,500));
		canvas.setFocusable(false);
		frame.add(canvas);
		frame.pack();
		
		for(int i=0;i<snake.length;i++){
			snake[i] = new int[]{px,py};
		}
		
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
	}
	
	public void run(){
		
		long now,lastTime = System.nanoTime();
		double delta = 0,nsPerTick = 1000000000/15;
		
		while(true){
			
			now = System.nanoTime();
			delta +=(now-lastTime)/nsPerTick;
			lastTime=now;
			
			if(delta>=1){
				tick();
				render();
				delta--;
				
			}
			
		}
		
	}
	
	
	public void tick(){
		px+=mx;
		py+=my;
		
		snake[0] = new int[]{px,py};
		
		if(px==ax &&py==ay){
			ax = rand.nextInt(dim);
			ay=rand.nextInt(dim);
			length++;
		}
		if(px<-1){
			px=dim-1;
		}
		if(px>dim-1){
			px=0;
		}
		if(py<-1){
			py=dim-1;
		}
		if(py>dim-1){
			py=0;
		}
		
		for(int i =length;i>=1;i--){
			snake[i] = snake[i-1];
		}
		
	}
	
	public void render(){
		bs = canvas.getBufferStrategy();
		
		if(bs==null){
			canvas.createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, 500, 500);
		
		g.setColor(Color.red);
		g.fillRect(ax*grid, ay*grid, grid, grid);
		g.setColor(Color.gray);
		g.drawRect(ax*grid, ay*grid, grid, grid);
		
		for(int i=0;i<length;i++){
			g.setColor(Color.blue);
			g.fillRect(snake[i][0]*grid, snake[i][1]*grid, grid, grid);
			g.setColor(Color.gray);
			g.drawRect(snake[i][0]*grid, snake[i][1]*grid, grid, grid);
		}
		
		bs.show();g.dispose();
	}
	
	public synchronized void stop(){
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		Game g=new Game();
		g.start();
	}
	
	class Key implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_UP && !(my==1)){
				mx=0;
				my=-1;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN&&!(my==-1)){
				mx=0;
				my=1;
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT&&!(mx==1)){
				mx=-1;
				my=0;
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT&&!(mx==-1)){
				mx=1;
				my=0;
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
