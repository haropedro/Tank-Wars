package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FilenameFilter;
import javax.imageio.ImageIO;

public class Explosion{
  protected File dir;
  protected String[] EXTENSIONS;
  protected FilenameFilter IMAGE_FILTER;

  public final static int SMALL_EXPLOSION = 0;
  public final static int LARGE_EXPLOSION = 1;
  public final static int NO_EXPLOSION = -1;
  
  private int type;
  private int p1x, p1y, p2x, p2y;
  
  private BufferedImage[] frames;
  private int currentFrame;
  private long startTime;
  private long delay;
  private boolean playedOnce;

  public Explosion(int type, int p1x, int p1y, int p2x, int p2y) {
    this.type = type;
    this.p1x = p1x;
    this.p2x = p2x;
    this.p1y = p1y;
    this.p2y = p2y;
    init();
    loadSprites();
  }
  
  private void init() {
    if (type == SMALL_EXPLOSION) {
    }
    
    if (type == LARGE_EXPLOSION) {
    }
    
    delay = 10;
    startTime = System.nanoTime();
    currentFrame = 0;
    
    playedOnce = type == NO_EXPLOSION;
  }
  
  public synchronized void update() {
    long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if(currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
  }
  
  public int getp1X() {
    return p1x;
  }
  
  public int getp1Y() {
    return p1y;
  }
  
  public int getFrame() {
    return currentFrame;
  }

  public BufferedImage getImage() {
    return frames[currentFrame];
  }

  public boolean hasPlayedOnce() {
    return playedOnce;
  }
  
  public void setPlayedOnce(boolean b) {
    playedOnce = b;
  }
  
  private void loadSprites() {
    if (type == SMALL_EXPLOSION) {
      loadFramesFromFolder("Resources/ExplosionSmall32x32");
    }
    
    if (type == LARGE_EXPLOSION) {
      loadFramesFromFolder("Resources/ExplosionLarge64x64");
    }
  }
  
  public synchronized void draw(Graphics2D gRightScreen, Graphics2D gLeftScreen) {
    
    if (!playedOnce) {
      gLeftScreen.drawImage(frames[currentFrame], 
          p1x, 
          p1y,
          null);
      gRightScreen.drawImage(frames[currentFrame], 
          p2x, 
          p2y,
          null);
      update();
    }
  }
  
  public static Image makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
  
  private static BufferedImage imageToBufferedImage(Image image) {

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return bufferedImage;

    }
  
  public BufferedImage makeBackgroundTransparent(BufferedImage image) {
    Image pic = makeColorTransparent(image, Color.BLACK);
    return imageToBufferedImage(pic);
  }
  
  public void loadFramesFromFolder(String file) {
    
    File dir = new File(file);

    EXTENSIONS = new String[]{
        "gif", "png", "jpg"
    };
    
    IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
    
    //frames rotating 45 degrees counter clockwise
    if (type == SMALL_EXPLOSION) {
      frames = new BufferedImage[7];
    }
    
    if (type == LARGE_EXPLOSION) {
      frames = new BufferedImage[8];
    }
    int count = 0;
    
    if (dir.isDirectory())
    { 
      for (final File f : dir.listFiles(IMAGE_FILTER))
      {
        try {
          BufferedImage image = ImageIO.read(f);
          image = makeBackgroundTransparent(image);
          
          frames[count++] = image;

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
  
}
