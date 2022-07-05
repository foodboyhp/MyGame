
package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	GamePanel gp;
    
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, 
    attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage image, image2, image3;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    String dialouges[] = new String[20];
    int dialogueIndex = 0;
    //State
    public int worldX,worldY;
    public String direction = "down";
    public int spriteNum = 1;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean died = false;
    boolean hpBarOn = false;
    
    
    //Counter
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invicibleCounter = 0;
    public int isDyingCounter = 0;
    int hpBarCounter = 0;
    
    //Character Status
	public int type;
	public String name;
    public int maxLife;
    public int life;
    public int speed;
    public int level;
    public int strength;
    public int dexterity;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShiled;

    //item Attributes 
    public int attackValue;
    public int defenseValue;
    public Entity(GamePanel gp) {
    	this.gp = gp;
    }
    
    public void setAction() {
    	
    }
    public void damageReact() {
    	
    }
    public void speak() {

		if(dialouges[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gp.ui.currentDialogue = dialouges[dialogueIndex];
		dialogueIndex++;
		
		switch(gp.player.direction) {
		case "up": direction = "down"; break;
		case "down" : direction = "up"; break;
		case "left" : direction = "right"; break;
		case "right" : direction = "left"; break;
			
		}
    }
    public void update() {
    	setAction();
    	collisionOn = false;
    	gp.cChecker.checkTile(this);
    	gp.cChecker.checkObject(this, false);
    	gp.cChecker.checkEntity(this,gp.npc);
    	gp.cChecker.checkEntity(this, gp.monster);
    	boolean contactPlayer = gp.cChecker.checkPlayer(this);
    	
    	if(this.type == 2 && contactPlayer == true) {
    		if(gp.player.invincible==false) {
    			gp.playSE(6);
    			gp.player.life-=1;
    			gp.player.invincible = true;
    		}
    	}
    	//if collision is false, can move
    	if(collisionOn== false) {
			switch(direction) {
			case "up": worldY -= speed; break;
			case "down": worldY += speed; break;
			case "right": worldX += speed; break;
			case "left": worldX -= speed; break;
			}
		}
        
        spriteCounter++;
        if(spriteCounter > 10) {
        	if(spriteNum == 1) 
        		spriteNum = 2;
        	else if(spriteNum == 2)
        		spriteNum = 1;
        	spriteCounter = 0;
        }
        
        if(invincible == true) {
    		invicibleCounter++;
    		if(invicibleCounter>60) {
    			invincible = false;
    			invicibleCounter = 0;
    		}
    	}
    }
    public void draw(Graphics2D g2) {
    	
    	BufferedImage image = null;
    	
    	int screenX = worldX - gp.player.worldX + gp.player.screenX; //players position is always on the center on the screen -> 0-0 tile is really in different place since it's outside of our game window, so we need to do some offsetting.
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX + gp.tileSize>gp.player.worldX-gp.player.screenX&&
		   worldX - gp.tileSize<gp.player.worldX+gp.player.screenX&&
		   worldY + gp.tileSize>gp.player.worldY-gp.player.screenY&&
	       worldY - gp.tileSize<gp.player.worldY+gp.player.screenY) {
			switch (direction) {
            case "up":
            	if(spriteNum == 1){
            		image = up1;
            	}
            	if(spriteNum == 2){
            		image = up2;
            	}
                break;
            case "down":
            	if(spriteNum == 1){
            		image = down1;
            	}
            	if(spriteNum == 2){
            		image = down2;
            	}
                break;
            case "left":
            	if(spriteNum == 1){
            		image = left1;
            	}
            	if(spriteNum == 2){
            		image = left2;
            	}
                break;
            case "right":
            	if(spriteNum == 1){
            		image = right1;
            	}
            	if(spriteNum == 2){
            		image = right2;
            	}
                break;
			}
			
			//Monster HP bar
			if(type==2 && hpBarOn == true) {
				double oneScale = (double)gp.tileSize/maxLife;
				double hpBarValue = oneScale*life;
				
				
				g2.setColor(new Color(35,35,35));
				g2.fillRect(screenX-1, screenY-16, gp.tileSize+2, 12);
				
				g2.setColor(new Color(255,0,30));
				g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
			
				hpBarCounter++;
				
				if(hpBarCounter > 600) {
					hpBarCounter = 0;
					hpBarOn = false;
				}
			}
			
			
			if(invincible == true) {
				hpBarOn = true;
				hpBarCounter = 0;
				changeAlpha(g2, 0.4f);
			}
			if(died == true) {
				dyingAnimation(g2);
				
			}
			
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);	
			
			changeAlpha(g2, 1f);
		}
    }
    
    public void dyingAnimation(Graphics2D g2) {
    	isDyingCounter++;
    	
    	int i = 5;
    	
    	if(isDyingCounter<=i) {
    		changeAlpha(g2, 0f);
    	}
    	if(isDyingCounter>i&&isDyingCounter<=2*i) {
    		changeAlpha(g2, 1f);
    	}
    	if(isDyingCounter>2*i&&isDyingCounter<=3*i) {
    		changeAlpha(g2, 0f);
    	}
    	if(isDyingCounter>3*i&&isDyingCounter<=4*i) {
    		changeAlpha(g2, 1f);
    	}
    	if(isDyingCounter>4*i&&isDyingCounter<=5*i) {
    		changeAlpha(g2, 0f);
    	}
    	if(isDyingCounter>5*i&&isDyingCounter<=6*i) {
    		changeAlpha(g2, 1f);
    	}
    	if(isDyingCounter>6*i&&isDyingCounter<=7*i) {
    		changeAlpha(g2, 0f);
    	}
    	if(isDyingCounter>7*i&&isDyingCounter<=8*i) {
    		changeAlpha(g2, 1f);
    	}
    	if(isDyingCounter>8*i) {
    		died = true;
    		alive = false;
    	}
    }
    
    public void changeAlpha(Graphics2D g2, float alphaValue) {
    	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }
    
    public BufferedImage setup(String imgPath, int width, int height) {
    	UtilityTool uTool = new UtilityTool();
    	BufferedImage image = null;
    	
    	try {
    		image = ImageIO.read(getClass().getResourceAsStream(imgPath+".png"));
    		image = uTool.scaleImage(image, width, height);
    	} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
    	return image;
    }
}	
