package object;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_BOOTS extends Entity {
	
	public OBJ_BOOTS(GamePanel gp) {
		super(gp);
		name = "Boots";
		down1 = setup("/objects/boots", gp.tileSize, gp.tileSize);
	}
}
