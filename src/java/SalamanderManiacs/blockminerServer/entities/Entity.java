package SalamanderManiacs.blockminerServer.entities;

import SalamanderManiacs.blockminerClient.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Entity {
    public float px=20.f, py=20.f;
    public float ax, ay;

    public int UUID;
    public Image texture;

    public static Entity [] entities=new Entity[256];

    public Entity(String img,Main game) {
        URL mURL = game.getClass().getResource("/defaultAssets/bm-icon.png");
        try {
            texture= ImageIO.read(new File(mURL.toURI()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(Graphics g){
        g.drawImage(texture,(int)px,(int)py,32,64,null);
    }
}
