package SalamanderManiacs.blockminerClient.Graph;

import SalamanderManiacs.blockminerClient.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Image {

    /*static field*/
    public static void registerTextures(Main game) {
    URL mURL = game.getClass().getResource("/defaultAssets/mask.png");
    BufferedImage mask = null;
    game.atlas = new BufferedImage(256,game.tiles*16,BufferedImage.TYPE_INT_ARGB);
    try {
        mask = ImageIO.read(new File(mURL.toURI()));
    } catch (IOException e) {
        e.printStackTrace();
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }
    game.log(game.getClass().getResource("/")+"");
    for (int i = 0; i < game.tiles; i++) {
        URL tURL = game.getClass().getResource("/defaultAssets/tile_"+(i+1)+".png");
        BufferedImage texture = null;
        try {
            texture = ImageIO.read(new File(tURL.toURI()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        for (int n = 0; n < 16; n++) {
            for (int y = 0; y < 15; y++) {
                for (int x = 0; x < 15; x++) {
                    game.atlas.setRGB(n*16+x, i*16+y, (mask.getRGB(n*16+x, 0+y) & texture.getRGB(0+x, 0+y)));
                }
            }
            if (((boolean) game.tileData.get(i)[1]) == false) {
                n = 16;
            }
        }
    }
}
}
