package SalamanderManiacs.blockminerClient;

import SalamanderManiacs.blockminerClient.Graph.GameWindow;
import SalamanderManiacs.blockminerClient.Graph.Renderer;

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

public class Main implements Runnable {

	public int tiles = 0;
	public long vsyncRate = (long)((1/(double)(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getRefreshRate()))*1000000000);
	public boolean vsync = true;
	public BufferedImage atlas;
	public ArrayList<Object[]> tileData = new ArrayList<Object[]>();
	public long prevRenderTime = 0;
	public long ct = 0;
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

	public void registerTextures() {
		URL mURL = getClass().getResource("/defaultAssets/mask.png");
		BufferedImage mask = null;
		atlas = new BufferedImage(256,tiles*16,BufferedImage.TYPE_INT_ARGB);
		try {
			mask = ImageIO.read(new File(mURL.toURI()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		log(getClass().getResource("/")+"");
		for (int i = 0; i < tiles; i++) {
			URL tURL = getClass().getResource("/defaultAssets/tile_"+(i+1)+".png");
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
						atlas.setRGB(n*16+x, i*16+y, (mask.getRGB(n*16+x, 0+y) & texture.getRGB(0+x, 0+y)));
					}
				}
				if (((boolean) tileData.get(i)[1]) == false) {
					n = 16;
				}
			}
		}
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



	public int frames = 0;

	public long lastSecTime = System.currentTimeMillis();

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
		registerTextures();
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
    }


	public void run() {
		gameWindow.addMouseListener(gameWindow.m);
		gameWindow.addMouseMotionListener(gameWindow.m);
        while (!Thread.currentThread().isInterrupted()) {
			Instant inst = Instant.now().plusNanos(1);
			ct = TimeUnit.SECONDS.toNanos(inst.getEpochSecond()) + inst.getNano();
			if (vsync == true) {
				if (ct > (prevRenderTime + vsyncRate)) {
					prevRenderTime = prevRenderTime + vsyncRate;
					if (ct > (prevRenderTime + vsyncRate)) {
						prevRenderTime = ct;
					}

					Update();

					long millis = System.currentTimeMillis();
					if (lastSecTime+1000 > millis) {
						frames++;
					} else {
						gameWindow.fps = frames;
						frames = 0;
						lastSecTime = millis;
					}
				}
			} else {
				Update();

				long millis = System.currentTimeMillis();
				if (lastSecTime+1000 > millis) {
					frames++;
				} else {
					gameWindow.fps = frames;
					frames = 0;
					lastSecTime = millis;
				}

			}
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {}
        }
    }

	public static void main(String[] args) {

		new Main();

	}

}
