package main;

import java.awt.*;

public class Event {
    GamePanel gp;
    Rectangle eventHitbox;
    int eventDefaultX, eventDefaultY;

    public  Event(GamePanel gp) {
        this.gp = gp;

        eventHitbox = new Rectangle();
        eventHitbox.x = 23;
        eventHitbox.y = 23;
        eventHitbox.width = 2;
        eventHitbox.height = 2;
        eventDefaultX = eventHitbox.x;
        eventDefaultY = eventHitbox.y;
    }

    public void checkEvent() {
        if (hit(2, 4, "any")) {
            damage(gp.dialogueState);
        }
        if (hit(7,7,"any")) {
            heal(gp.dialogueState);
        }
    }

    public boolean hit(int col, int row, String direction) {
        boolean hit = false;

        gp.player.hitbox.x = gp.player.worldX + gp.player.hitbox.x;
        gp.player.hitbox.y = gp.player.worldY + gp.player.hitbox.y;
        eventHitbox.x = col * gp.tileSize + eventHitbox.x;
        eventHitbox.y = row * gp.tileSize + eventHitbox.y;

        if (gp.player.hitbox.intersects(eventHitbox)) {
            if (gp.player.direction.contentEquals(direction) || direction.contentEquals("any")) {
                hit = true;
            }
        }

        gp.player.hitbox.x = gp.player.hitboxDefaultX;
        gp.player.hitbox.y = gp.player.hitboxDefaultY;
        eventHitbox.x = eventDefaultX;
        eventHitbox.y = eventDefaultY;

        return hit;
    }

    public void damage(int gameState) {
        gp.gameState = gameState;
        gp.ui.dialogue = "You slipped.";
        gp.player.health -= 1;
    }

    public void heal(int gameState) {
        gp.gameState = gameState;
        gp.ui.dialogue = "You find respite.";
        gp.player.health = gp.player.maxHealth;
    }
}
