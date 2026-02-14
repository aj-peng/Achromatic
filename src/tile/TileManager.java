package tile;

import main.GamePanel;
import main.Utility;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {
    GamePanel gp;
    Utility utility = new Utility();

    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[20];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {
        // 0-9 PLACEHOLDER
        setup(0, "void", true);
        setup(10, "stone", false);
        setup(11, "path", false);
        setup(12, "dirt", false);
        setup(13, "wall", false);
        setup(14, "tree", true);
        setup(15, "water", true);
    }

    public void setup(int index, String imageName, boolean collision) {
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/tiles/" + imageName + ".png")));
            tile[index].image = utility.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadMap() {
        try {
            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/maps/map01.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }

                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int index = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (gp.player.onScreen(worldX, worldY)) {
                g2.drawImage(tile[index].image, screenX, screenY, null);
            }
            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public int getTileNum(int worldX,int worldY) {
        int boundary = 4; // 4 Pixel Boundary
        if (worldX < boundary || worldX > gp.maxWorldX - boundary ||
                worldY < boundary || worldY > gp.maxWorldY - boundary) {
            return 0;
        }

        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;
        return mapTileNum[col][row];
    }
}
