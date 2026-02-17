package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Heart extends SuperObject{
    GamePanel gp;

    public OBJ_Heart(GamePanel gp) {
        this.gp = gp;

        name = "Heart";
        collision = false;
        try {
            image = utility.scaleImage(
                    ImageIO.read(Objects.requireNonNull(getClass().getResource("/object/heart.png"))),
                    gp.tileSize / 2, gp.tileSize / 2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
