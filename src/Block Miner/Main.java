package BlockMiner;

import java.io.*;
import java.lang.reflect.Array;
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

public class Main extends JPanel implements Runnable {
	
	public int tiles = 0;
	public long vsyncRate = (long)((1/(double)(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getRefreshRate()))*1000000000);
	public boolean vsync = true;
	public BufferedImage[] textures;
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
		textures = new BufferedImage[tiles];
		for (int i = 0; i < tiles; i++) {
			URL tURL = getClass().getResource("/BlockMiner/defaultAssets/tile_"+(i+1)+".png");
			try {
				textures[i] = ImageIO.read(new File(tURL.toURI()));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
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
	
	private Mouse m = new Mouse();
	
	public JFrame jFrame;
	
	public int fps = 0;
	
	public int frames = 0;
	
	public long lastSecTime = System.currentTimeMillis();
	
	public int mapwidth = 20;
	public int mapheight = 20;
	
	public int tileSize = 30;
	
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
			return true;
		}
	}
	
	public BufferedImage getSubImage(int id, int mx, int my) {
		if (id == 1) {
			boolean above = blockTest(mx,my-1);
			boolean below = blockTest(mx,my+1);
			boolean left = blockTest(mx-1,my);
			boolean right = blockTest(mx+1,my);
			if (above == true) {
				if (below == true) {
					if (left == true) {
						if (right == true) {
							return textures[0].getSubimage(64, 0, 15, 15);
						} else {
							return textures[0].getSubimage(0, 0, 15, 15);
						}
					} else if (right == true) {
						return textures[0].getSubimage(16, 0, 15, 15);
					} else {
						return textures[0].getSubimage(64, 32, 15, 15);
					}
				} else if (left == true) {
					if (right == true) {
						return textures[0].getSubimage(32, 0, 15, 15);
					} else {
						return textures[0].getSubimage(0, 16, 15, 15);
					}
				} else if (right == true) {
					return textures[0].getSubimage(16, 16, 15, 15);
				} else {
					return textures[0].getSubimage(16, 32, 15, 15);
				}
			} else if (below == true) {
				if (left == true) {
					if (right == true) {
						return textures[0].getSubimage(48, 0, 15, 15);
					} else {
						return textures[0].getSubimage(32, 16, 15, 15);
					}
				} else if (right == true) {
					return textures[0].getSubimage(48, 16, 15, 15);
				} else {
					return textures[0].getSubimage(0, 32, 15, 15);
				}
			} else if (left == true) {
				if (right == true) {
					return textures[0].getSubimage(64, 16, 15, 15);
				} else {
					return textures[0].getSubimage(32, 32, 15, 15);
				}
			} else if (right == true) {
				return textures[0].getSubimage(48, 32, 15, 15);
			} else {
				return textures[0].getSubimage(64, 64, 15, 15);
			}
	    } else {
	    	return textures[0];
	    }
	}
	
	public void renderID(Graphics g, int id, int x, int y) {
		if (id == 1) {
			if (textures != null) {
				g.drawImage(textures[0].getSubimage(64, 0, 15, 15), x, y, 15, 15, null);
			}
	    } else if (id == 2) {
	    	Color c = new Color(163, 253, 255);
	    	g.setColor(Color.white);
	    	g.setColor(c);
	    	g.fillRect(x,y,15,15);
	    }
	}
	
	public void renderID(Graphics g, int mx, int my) {
		int id = getBlockAt(mx,my);
		int x = mx*tileSize;
		int y = my*tileSize;
		if (id == -1) {
			g.setColor(Color.black);
			g.fillRect(x,y,tileSize,tileSize);
		} else if (id == 0) {
			nothing();
		} else if (id == 1) {
			if (textures != null) {
				g.drawImage(getSubImage(id,mx,my), x, y, tileSize, tileSize, null);
			}
	    } else if (id == 2) {
	    	Color c = new Color(163, 253, 255);
	    	g.setColor(Color.white);
	    	g.setColor(c);
	    	g.fillRect(x,y,tileSize,tileSize);
	    }
	}
	
	public Main() {
		log("Starting BlockMiner "+version[0]+"."+version[1]+"."+version[2]);
		registerTiles();
		log("Registered "+tiles+" tiles");
		registerTextures();
		log("Registered "+tiles+" textures");
		this.setDoubleBuffered(true);
		this.setMinimumSize(new Dimension(768, 540));
		this.setPreferredSize(new Dimension(768, 540));
		this.setSize(new Dimension(768, 540));
		this.setBackground(new Color(190, 237, 247));
		
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
		
		jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setMinimumSize(new Dimension(768, 540));
        jFrame.setSize(new Dimension(768, 540));
        jFrame.setTitle("BlockMiner");
        URL iconURL = getClass().getResource("/BlockMiner/defaultAssets/bmuicon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        jFrame.setIconImage(icon.getImage());
        jFrame.setContentPane(this);
        jFrame.setVisible(true);
        Thread thread = new Thread(this);
        thread.start();
        
    }
	
	public void Update() {
        repaint();
    }
	
	public void paint(Graphics g){
		super.paintComponent(g);
        for (int y = 0; y < mapheight; y++) {
            for (int x = 0; x < mapwidth; x++) {
            	
            	//renderID(g,(int)Array.get(map,y*20+x),x*15,y*15);
            	renderID(g,x,y);
            	
            }
        }
        long fmem = runtime.freeMemory();
        long mmem = runtime.maxMemory();
        long amem = runtime.totalMemory();
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
	
	public void run() {
		System.getProperties().list(System.out);
        addMouseListener(m);
        addMouseMotionListener(m);
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
		            	fps = frames;
		            	frames = 0;
		            	lastSecTime = millis;
		            }
	        	}
        	} else {
        		
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
