package SalamanderManiacs.blockminerClient.Graph;

import SalamanderManiacs.blockminerClient.Main;
import SalamanderManiacs.blockminerServer.entities.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.BinaryOperator;

public class Renderer {
    private final Main game;
    public float offset=0.f;

    public Renderer(Main game) {
        this.game = game;
    }


    public void renderID(Graphics g, int id, int x, int y) {
//        if (id == 1) {
//            if (game.textures != null) {
//                g.drawImage(game.textures[0].getSubimage(64, 0, 15, 15), x, y, 15, 15, null);
//            }
//        } else if (id == 2) {
//            Color c = new Color(163, 253, 255);
//            g.setColor(Color.white);
//            g.setColor(c);
//            g.fillRect(x,y,15,15);
//        }
    }
    public void renderID(Graphics g, int mx, int my) {
        int id = game.getBlockAt(mx,my);
        int x = (int)offset+(mx*game.tileSize);
        int y = my*game.tileSize;
        if (id == -1) {
            g.setColor(Color.black);
            g.fillRect(x,y,game.tileSize,game.tileSize);
        } else if (id == 0) {
            game.nothing();
        } else {
            if (game.atlas != null) {
                g.drawImage(getSubImage(id,mx,my), x, y, game.tileSize, game.tileSize, null);
            }
        }
    }
    public BufferedImage getSubImage(int id, int mx, int my) {
            int amount=0;
            if (((boolean) game.tileData.get(id-1)[1]) != false) {
	            if (!game.blockTest(mx+1,my)){amount+=1;} //right weight=1
	            if (!game.blockTest(mx-1,my)){amount+=2;} //left  weight=2
	            if (!game.blockTest(mx,my+1)){amount+=4;} //below weight=4
	            if (!game.blockTest(mx,my-1)){amount+=8;} //above weight=8
			}
            return game.atlas.getSubimage(amount*16, (id-1)*16, 15, 15);
    }
}
