package main;

import java.awt.Rectangle;

public class EventHandler {

	GamePanel gp;
	Rectangle eventRect;
	int eventRectDefaultX, eventRectDefaultY;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		
		eventRect = new Rectangle();
		eventRect.x = 23;
		eventRect.y = 23;
		eventRect.width = 2;
		eventRect.height = 2;
		eventRectDefaultX = eventRect.x;
		eventRectDefaultY = eventRect.y;
				
	}
	
	public void checkEvent() {
		//if(hit(23, 40, "right")==true)damagePit(gp.gameState);
		//if(hit(23,12,"up")==true)healingPool(gp.gameState);
		if(hit(21,21,"any")==true)teleportPortal();
	}
	public boolean hit(int eventCol, int eventRow,String regDirection) {
		boolean hit = false;
		gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
		gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
		eventRect.x = eventCol*gp.tileSize+eventRect.x;
		eventRect.x = eventRow*gp.tileSize+eventRect.y;
		
		if(gp.player.solidArea.intersects(eventRect)) {
			if(gp.player.direction.contentEquals(regDirection)||regDirection.contentEquals("any"))
				hit = true;
		}
		
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect.x = eventRectDefaultX;
		eventRect.y = eventRectDefaultY;
		
		return hit;
	}
	public void damagePit(int gameState) {
		gp.gameState = gameState;
		gp.ui.showMessage("You fall into a pit");
		gp.player.life -=1;
	}
	public void healingPool(int gameState) {
		if(gp.keyH.enterPressed == true) {
			gp.player.attackCanceled = true;
			gp.gameState = gameState;
			gp.player.life = gp.player.maxLife;
		}
	}
	public void teleportPortal() {
		if(gp.keyH.enterPressed == true) {
			gp.player.worldX = gp.tileSize*37;
			gp.player.worldY = gp.tileSize*10;
		}
	}
}
