
package main;

import main.KeyHandler;
import tile.TileManager;
import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    //Screen Settings
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;//48*48
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    
    //World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize*maxWorldCol;
    public final int worldHeight = tileSize*maxWorldRow;
    
    //FPS
    int FPS = 60;
    
    //System
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound se = new Sound();
    Sound music = new Sound();
    public CollisionCheck cChecker = new CollisionCheck(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eventHandler = new EventHandler(this);
    Thread gameThread;
    
    //Entity and object
    public Player player = new Player(this,keyH);
    public Entity obj[] = new Entity[20];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    ArrayList<Entity> entities = new ArrayList<>();
    
    //Game State
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    
    
    //Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    
    public void setupGame() {
    	aSetter.setObject();
    	aSetter.setNPC();
    	aSetter.setMonster();
    	playMusic(0);
    	stopMusic();
    	gameState = titleState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        
        while(gameThread!=null){
            currentTime = System.nanoTime();
            delta+=(currentTime-lastTime)/drawInterval;
            timer+=(currentTime-lastTime);
            lastTime = currentTime;
            if(delta>=1){
                update();
                repaint();
                delta--;
                drawCount++;                
            }
            if(timer>=1000000000){
                drawCount = 0;
                timer = 0;
            }
        }
    }    

    public void update(){
    	if(gameState==playState) {
    		player.update();
    		for(int i = 0;  i < npc.length; i++) {
    			if(npc[i] != null) {
    				npc[i].update();
    			}
    		}
    		for(int i = 0;  i < monster.length; i++) {
    			if(monster[i] != null) {
    				if(monster[i].alive ==true && monster[i].died == false) {
    					monster[i].update();    					
    				}
    				if(monster[i].alive ==false) {
    					monster[i] = null;   					
    				}
    			}
    		}
    	}
    	if(gameState == pauseState) {
    		
    	}
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        //Title Screen
        if(gameState == titleState) {
        	ui.draw(g2);
        }
        //Others
        else {
        	//tile
            tileM.draw(g2);
           
            //Entities
            entities.add(player);
            for(int i = 0;i < npc.length; i++) {
            	if(npc[i]!=null) {
            		entities.add(npc[i]);
            	}
            }
            for(int i = 0;  i < obj.length; i++) {
            	if(obj[i]!=null) {
            		entities.add(obj[i]);
            	}
            }
            for(int i = 0;  i < monster.length; i++) {
            	if(monster[i]!=null) {
            		entities.add(monster[i]);
            	}
            }
            
            //Sort
            Collections.sort(entities, new Comparator<Entity>() {
            	public int compare(Entity o1, Entity o2) {
            		int res = Integer.compare(o1.worldY, o2.worldY);
            		return res;
            	}
			});
            //Draw entities
            for(int i = 0; i < entities.size(); i++) {
            	entities.get(i).draw(g2);
            }
            
            entities.clear();
            //UI
            ui.draw(g2);
            
        } 
        g2.dispose();
    }
    public void playMusic(int i) {
    	music.setFile(i);
    	music.play();
    	music.loop();
    }
    public void stopMusic() {
    	music.stop();
    }
    public void playSE(int i) {
    	se.setFile(i);
    	se.play();
    }
}
