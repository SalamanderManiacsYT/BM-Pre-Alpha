package SalamanderManiacs.blockminerClient.Graph;

import SalamanderManiacs.blockminerClient.Main;
import SalamanderManiacs.blockminerClient.Mouse;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JPanel {
    private final Main game;
    public Mouse m = new Mouse();
    public int fps = 0;

    public JFrame jFrame;
    public GameWindow (int w, int h, int x, int y, Main game){
        this.game = game;
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setMinimumSize(new Dimension(768, 540));
        jFrame.setTitle("BlockMiner");

    }

    public GameWindow (Main game){
        this(768,540,0,0,game);
    }
    public GameWindow (int w, int h,Main game){
        this(w,h,0,0,game);
    }
    public void setIcon(ImageIcon icon){

        jFrame.setIconImage(icon.getImage());
        jFrame.setContentPane(this);
    }
    public void setVisible(boolean b){
        jFrame.setVisible(b);}
    public void paint(Graphics g){
        super.paintComponent(g);
        for (int y = 0; y < game.mapheight; y++) {
            for (int x = 0; x < game.mapwidth; x++) {

                //renderer.renderID(g,(int)Array.get(map,y*20+x),x*15,y*15);
                game.renderer.renderID(g,x,y);

            }
        }
        long fmem = game.runtime.freeMemory();
        long mmem = game.runtime.maxMemory();
        long amem = game.runtime.totalMemory();
        g.setFont(new Font("sans serif", Font.PLAIN, 20));
        g.setColor(new Color(0,0,0,125));
        g.drawString(fps+" FPS", 12, 26);
        g.drawString((amem-fmem) / 1048576+"MB / "+amem / 1048576+"MB used ("+Math.round((((float)amem-(float)fmem)/(float)amem)*100)+"%), "+(fmem / 1048576)+"MB free", 12, 52);
        g.drawString(mmem / 1048576+"MB max, "+((fmem + (mmem - amem)) / 1048576)+"MB total free", 12, 78);
        g.setColor(new Color(255,255,255));
        g.drawString(fps+" FPS", 10, 24);
        g.drawString((amem-fmem) / 1048576+"MB / "+amem / 1048576+"MB used ("+Math.round((((float)amem-(float)fmem)/(float)amem)*100)+"%), "+(fmem / 1048576)+"MB free", 10, 50);
        g.drawString(mmem / 1048576+"MB max, "+((fmem + (mmem - amem)) / 1048576)+"MB total free", 10, 76);
    }
}
