package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int scale = 3;
    final int originalTileSize = 16; // 16x16
    public final int tileSize = originalTileSize * scale; // 48x48

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize; // 768 pixels
    public final int screenHeight = maxScreenRow * tileSize; // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 15;
    public final int maxWorldRow = 15;
    public final int maxWorldX = maxWorldCol * tileSize;
    public final int maxWorldY = maxWorldRow * tileSize;

    final int tick = 60;

    // SYSTEM
    TileManager tileManager = new TileManager(this);
    InputHandler input = new InputHandler(this);
    Sound music = new Sound();
    Sound sound = new Sound();
    public UserInterface ui = new UserInterface(this);
    public Collision collision = new Collision(this);
    public Setter setter = new Setter(this);
    Thread gameThread;

    // GAMEPLAY
    public Player player = new Player(this, input);
    public SuperObject objs[] = new SuperObject[10];
    public Entity npcs[] = new Entity[10];

    // GAMESTATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    // public final int optionsState = 5;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(input);
        this.setFocusable(true);
    }

    public void setupGame() {
        setter.setObject();
        setter.setNPC();
        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / tick;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();

            for (Entity npc : npcs) {
                if (npc != null) {
                    npc.update();
                }
            }
        }
        else if (gameState == pauseState) {
            // Pause Menu
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // DEBUG
        long start = 0;
        start = System.nanoTime();

        // TITLE
        if (gameState == titleState) {
            ui.draw(g2);
        }
        // GAME
        else {
            // TILES
            tileManager.draw(g2);

            // OBJECTS
            for (SuperObject obj : objs) {
                if (obj != null) {
                    obj.draw(g2, this);
                }
            }

            // Entities
            ArrayList<Entity> entities = new ArrayList<>();
            entities.add(player);
            for (Entity npc : npcs) {
                if (npc != null) {
                    entities.add(npc);
                }
            }

            entities.sort((e1, e2) -> (Integer.compare(e1.worldY, e2.worldY)));
            for (Entity entity : entities) {
                entity.draw(g2);
            }

            // UI
            ui.draw(g2);

            if (input.debug) {
                long elapsed = System.nanoTime() - start;
                ui.drawDelta(elapsed);
            }

            g2.dispose();
        }
    }

    public void playMusic(int index) {
        music.setFile(index);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSound(int index) {
        sound.setFile(index);
        sound.play();
    }
}
