package BlockMap;

import BlockMap.Block;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import javax.imageio.ImageIO;

public class PowerUp extends Block{
  
  public static final int P1_SHIELD = 3;
  public static final int P2_SHIELD = 4;
  
  public PowerUp (int type, int x, int y, int width, int height) {
    super(type, x, y, width, height);
    loadImage();
  }
  
  public BufferedImage getImage() {
    return image;
  }
  
  private void loadImage()
  {
    try
    {
      if (type == P1_SHIELD)
      {
        image = makeBackgroundTransparent(ImageIO.read(new File("Resources/Shield1.gif")));
      }

      if (type == P2_SHIELD)
      {
        image =  makeBackgroundTransparent(ImageIO.read(new File("Resources/Shield2.gif")));
      }
    } catch (Exception e)
    {
      e.printStackTrace();
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
  
  public static BufferedImage makeBackgroundTransparent(BufferedImage image) {
    Image pic = makeColorTransparent(image, new Color(51, 128, 0));
    pic = makeColorTransparent(imageToBufferedImage(pic), new Color(51, 170, 0));
    return imageToBufferedImage(pic);
  }
}