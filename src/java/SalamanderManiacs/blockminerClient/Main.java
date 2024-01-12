package SalamanderManiacs.blockminerClient;

import SalamanderManiacs.blockminerClient.Graph.GameWindow;
import SalamanderManiacs.blockminerClient.Graph.Renderer;
import SalamanderManiacs.blockminerServer.entities.Entity;

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.*;

import static SalamanderManiacs.blockminerClient.Graph.Image.registerTextures;

public class Main implements Runnable {

	public int tiles = 0;
	public BufferedImage atlas;
	public ArrayList<Object[]> tileData = new ArrayList<Object[]>();
	public String detectedOS = "";
	public void nothing() {

	}

	public void registerTile(String name, boolean outlines) {
		tileData.add(new Object[] {(Object) name, (Object) outlines});
		tiles++;
	}

	public void registerTiles() {
		registerTile("Dirt",true);
		registerTile("Grass",true);
	}



	public String timeString() {
		return "Z";
	}

	public Runtime runtime = Runtime.getRuntime();

	public int[] version = new int[] {0, 1, 0};

	public void log(String s) {
		System.out.println(LocalDateTime.now()+"Z [LOG] - "+s);
	}

	public void warn(String s) {
		System.out.println(LocalDateTime.now()+"Z [WARN] - "+s);
	}

	public void error(String s) {
		System.out.println(LocalDateTime.now()+"Z [ERROR] - "+s);
	}

	public Entity player=new Entity("a",this);


	public int mapwidth = 20;
	public int mapheight = 20;

	public int tileSize = 30;
	public Renderer renderer = new Renderer(this);
	public GameWindow gameWindow = new GameWindow(this);
	public int[] map = new int[]{

		1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
        1,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,1,1,
        1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,
        1,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,0,1,
        1,0,1,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
        1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
        1,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,1,1,0,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,
        1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,
        1,1,1,1,2,2,2,2,2,2,2,1,1,1,1,1,1,1,1,1
	};

	public int getBlockAt(int x, int y) {
		if (x >= 0 && x < mapwidth && y >= 0 && y < mapheight) {
			return map[y*mapwidth+x];
		} else {
			return -1;
		}
	}

	public boolean blockTest(int mx, int my) {
		int block = getBlockAt(mx,my);
		if (block == -1 || block == 0) {
			return false;
		} else {
			return ((boolean) tileData.get(block-1)[1]);
		}
	}




	public Main() {

		log("Starting BlockMiner "+version[0]+"."+version[1]+"."+version[2]);
		registerTiles();
		log("Registered "+tiles+" tiles");
		registerTextures(this);
		log("Registered "+tiles+" textures");
		gameWindow.setDoubleBuffered(true);
		gameWindow.setMinimumSize(new Dimension(768, 540));
		gameWindow.setPreferredSize(new Dimension(768, 540));
		gameWindow.setSize(new Dimension(768, 540));
		gameWindow.setBackground(new Color(190, 237, 247));
		log("Max Memory: "+runtime.maxMemory() / 1048576+"MB");
		log("Refresh Rate: "+GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getRefreshRate()+"Hz");
		//log("Allocated Memory: "+runtime.totalMemory() / 1048576+"MB");
		log("Running "+System.getProperty("os.name")+System.getProperty("os.arch"));

		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
			detectedOS = "MaxOS";
		} else if (OS.indexOf("win") >= 0) {
			detectedOS = "Windows";
		} else if (OS.indexOf("nux") >= 0 || OS.indexOf("nix") >= 0 || OS.indexOf("aix") >= 0) {
			detectedOS = "Unix";
		} else {
			detectedOS = "Unknown";
		}

        URL iconURL = getClass().getResource("/defaultAssets/bm-icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
		gameWindow.setIcon(icon);
		gameWindow.setVisible(true);
        Thread thread = new Thread(this);
        thread.start();
        
    }

	public void Update() {
		gameWindow.repaint();
		player.px+=gameWindow.moveIndex[0];
		player.py+=gameWindow.moveIndex[1];
    }


	public void run() {
		gameWindow.addMouseListener(gameWindow.m);
		gameWindow.addMouseMotionListener(gameWindow.m);
		double tickrate=1000000000/40;
		double nextTick=System.nanoTime()+tickrate;
        while (!Thread.currentThread().isInterrupted()) {
			if (System.nanoTime()>=nextTick){
			Update();
			nextTick=System.nanoTime()+tickrate;
			}
			gameWindow.update();
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {}
        }
    }

	public static void main(String[] args) {

		new Main();

	}

}
