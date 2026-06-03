package main;

import entity.Entity;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EventHandler {
    GamePanel gp;
    Rectangle eventHitbox;
    int eventDefaultX, eventDefaultY;

    private final Map<Long, Node> events = new HashMap<>();

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventDefaultX = 23;
        eventDefaultY = 23;
        eventHitbox = new Rectangle(eventDefaultX,eventDefaultY,2,2);

        setEvents();
    }

    public void checkEvent() {
        switch (gp.gameState) {
            case PLAY -> {
                Node node = events.get(stepped());
                if (node != null && hit(node)) {
                    node.action().run();
                }
            }
            case DIALOGUE -> gp.gameState = GamePanel.GameState.PLAY;
        }
    }

    public void gamble() {
        boolean success = Math.random() < 0.3;
        gp.ui.addMessage(success ? "+1 Health" : "-1 Health");
        gp.player.adjustHealth(success ? 1 : -1);
    }

    public void respite() {
        gp.gameState = GamePanel.GameState.DIALOGUE;
        gp.ui.setDialogue("You find respite.");
        gp.player.adjustHealth(gp.player.maxHealth);
    }

    private void setEvents() {
        addEvent(7,7, Entity.Direction.NONE, this::respite);
    }

    private void addEvent(int x, int y, Entity.Direction direction, Runnable action) {
        events.put(index(x, y), new Node(x, y, direction, action));
    }

    private boolean hit(Node node) {
        gp.player.hitbox.x = gp.player.worldX + gp.player.hitboxDefaultX;
        gp.player.hitbox.y = gp.player.worldY + gp.player.hitboxDefaultY;
        eventHitbox.x = node.x() * gp.tileSize + eventDefaultX;
        eventHitbox.y = node.y() * gp.tileSize + eventDefaultY;

        boolean hit = (gp.player.hitbox.intersects(eventHitbox)) &&
                (gp.player.direction == node.direction() || node.direction() == Entity.Direction.NONE);

        gp.player.hitbox.x = gp.player.hitboxDefaultX;
        gp.player.hitbox.y = gp.player.hitboxDefaultY;
        eventHitbox.x = eventDefaultX;
        eventHitbox.y = eventDefaultY;

        return hit;
    }

    private long stepped() {
        int col = (gp.player.worldX + gp.player.hitbox.x + gp.player.hitbox.width / 2) / gp.tileSize;
        int row = (gp.player.worldY + gp.player.hitbox.y + gp.player.hitbox.height / 2) / gp.tileSize;
        return index(col, row);
    }

    private long index(int x, int y) {
        return ((long) x << 32) | (y & 0xffffffffL);
    }

    private record Node(int x, int y, Entity.Direction direction, Runnable action) {}
}
