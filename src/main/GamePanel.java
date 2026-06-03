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

    // SYSTEM
    TileManager tileManager = new TileManager(this);
    InputHandler input = new InputHandler(this);
    Sound music = new Sound();
    Sound sound = new Sound();
    public UserInterface ui = new UserInterface(this);
    public Collision collision = new Collision(this);
    public Setter setter = new Setter(this);
    public EventHandler event = new EventHandler(this);
    Thread gameThread;

    // GAMEPLAY
    private final ArrayList<Sprite> render = new ArrayList<>(16);
    public Player player = new Player(this, input);
    public SuperObject[] objs = new SuperObject[8];
    public Entity[] npcs = new Entity[8];

    // GAME STATE
    public boolean debug = false;
    public enum GameState { TITLE, PLAY, PAUSE, DIALOGUE }
    public GameState gameState = GameState.TITLE;
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
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        int tick = 60;
        double drawInterval = 1_000_000_000D / tick;

        long lastTime = System.nanoTime();
        long currentTime;
        double delta = 0;

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
        if (gameState == GameState.PLAY) {
            player.update();

            for (Entity npc : npcs) {
                if (npc != null) {
                    npc.update();
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // DEBUG
        long start = System.nanoTime();

        // TITLE
        if (gameState == GameState.TITLE) {
            ui.draw(g2);
        }
        else {
            // TILES
            tileManager.draw(g2);

            // OBJECTS AND ENTITY
            render.clear();
            render.add(player);
            for (Entity npc : npcs) {
                if (npc != null) {
                    render.add(npc);
                }
            }
            for (SuperObject obj : objs) {
                if (obj != null) {
                    render.add(obj);
                }
            }

            render.sort((s1, s2) -> (Integer.compare(s1.getSpriteOrder(), s2.getSpriteOrder())));
            for (Sprite sprite : render) {
                sprite.draw(g2);
            }

            // UI
            ui.draw(g2);
            if (debug) {
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
