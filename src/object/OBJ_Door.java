package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Door extends SuperObject{
    GamePanel gp;

    public OBJ_Door(GamePanel gp) {
        this.gp = gp;

        name = "Door";
        collision = true;
        try {
            image = utility.scaleImage(
                    ImageIO.read(Objects.requireNonNull(getClass().getResource("/object/door.png"))),
                    gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
