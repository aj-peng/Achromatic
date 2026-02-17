package main;

import entity.NPC_Blue;
import object.OBJ_Dice;
import object.OBJ_Door;

public class Setter {
    GamePanel gp;

    public Setter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        gp.objs[0] = new OBJ_Dice(gp);
        gp.objs[0].worldX = gp.tileSize * 2;
        gp.objs[0].worldY = gp.tileSize * 2;

        gp.objs[1] = new OBJ_Door(gp);
        gp.objs[1].worldX = gp.tileSize * 2;
        gp.objs[1].worldY = gp.tileSize * 4;
    }

    public void setNPC() {
        gp.npcs[0] = new NPC_Blue(gp);
        gp.npcs[0].worldX = gp.tileSize * 12;
        gp.npcs[0].worldY = gp.tileSize * 2;
    }
}
