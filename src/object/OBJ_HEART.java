package object;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_HEART extends Entity{
	
	
	public OBJ_HEART(GamePanel gp) {
		super(gp);
		name = "Boots";
		image = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
		image2 = setup("/objects/heart_half", gp.tileSize, gp.tileSize);
		image3 = setup("/objects/heart_blank", gp.tileSize, gp.tileSize);
		
		
	}
}
