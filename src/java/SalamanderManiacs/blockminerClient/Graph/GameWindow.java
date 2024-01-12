package SalamanderManiacs.blockminerClient.Graph;

import SalamanderManiacs.blockminerClient.Main;
import SalamanderManiacs.blockminerClient.Mouse;
import SalamanderManiacs.blockminerServer.entities.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JPanel  implements KeyListener {
    public long vsyncRate = (long)((1/(double)(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getRefreshRate()))*1000000000);
    public boolean vsync = false;
    public long prevRenderTime = 0;
    public long ct = 0;
    public int frames = 0;

    public long lastSecTime = System.currentTimeMillis();
	
	private static final long serialVersionUID = 8599544972938038452L;
	private final Main game;
    public Mouse m = new Mouse();
    public int fps = 0;

    public int []moveIndex={0,0};

    public JFrame jFrame;
    public GameWindow (int w, int h, int x, int y, Main game){
        this.game = game;
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setMinimumSize(new Dimension(768, 540));
        jFrame.setTitle("BlockMiner");
        jFrame.addKeyListener(this);
        

    }
    public void update(){
        if (vsync) {
            if (ct > (prevRenderTime + vsyncRate)) {
                prevRenderTime = prevRenderTime + vsyncRate;
                if (ct > (prevRenderTime + vsyncRate)) {
                    prevRenderTime = ct;
                }


                long millis = System.currentTimeMillis();
                if (lastSecTime+1000 > millis) {
                    frames++;
                } else {
                    fps = frames;
                    frames = 0;
                    lastSecTime = millis;
                }
            }
        } else {

            long millis = System.currentTimeMillis();
            if (lastSecTime+1000 > millis) {
                frames++;
            } else {
                fps = frames;
                frames = 0;
                lastSecTime = millis;
            }
    }
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
        game.player.render(g);
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        switch(key){
            case KeyEvent.VK_D:
                moveIndex[0]=moveIndex[0]==0?1:0;
                // Other operations
                break;
            case KeyEvent.VK_A:
                moveIndex[0]=moveIndex[0]==0?-1:0;
                // Other operations
                break;
            case KeyEvent.VK_W:
                moveIndex[1]=moveIndex[1]==0?-1:0;
                // Other operations
                break;
            case KeyEvent.VK_S:
                moveIndex[1]=moveIndex[1]==0?1:0;
                // Other operations
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch(key){
            case KeyEvent.VK_D:
                moveIndex[0]=0;
                // Other operations
                break;
            case KeyEvent.VK_A:
                moveIndex[0]=0;
                // Other operations
                break;
            case KeyEvent.VK_W:
                moveIndex[1]=0;
                // Other operations
                break;
            case KeyEvent.VK_S:
                moveIndex[1]=0;
                // Other operations
                break;
        }

    }
}
