package BlockMap;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Block{
  
  protected BufferedImage image;
  protected Rectangle rect;
  protected int type;
  
  public static final int EMPTY_TILE = 0;
  public static final int BREAKABLE = 1;
  public static final int UNBREAKABLE = 2;
  
  public Block(int type, int x, int y, int width, int height) {
    this.type = type;
    rect = new Rectangle(x, y, width, height);
    loadImage();
  }
  
  public BufferedImage getImage() {
    return image;
  }
  
  private void loadImage()
  {
    try
    {
      if (type == BREAKABLE)
      {
        image = ImageIO.read(new File("Resources/Wall2.gif"));
      }

      if (type == UNBREAKABLE)
      {
        image = ImageIO.read(new File("Resources/Wall1.gif"));

      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public int getType() {
    return type;
  }
  
   public void setType(int k) {
    type = k;
  }
  
  public Rectangle getRectangle() {
    return rect;
  }
}
