package TankGame;

import Main.GamePanel;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import javax.imageio.ImageIO;

public class Background
{
  private BufferedImage image;
  
  private double x;
  private double y;
  
  
  public Background(String s) {
    try {
      image = ImageIO.read(new File(s));
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void setPosition (double x, double y) {

    this.x = x % GamePanel.WIDTH;
    this.y = y % GamePanel.HEIGHT;
  }
  
  public void draw(Graphics2D g) {
    
    g.drawImage(image, (int)x, (int)y, null);
		if(x < 0) {
			g.drawImage(
				image,
				(int)x + GamePanel.WIDTH,
				(int)y,
				null
			);
		}
		if(x > 0) {
			g.drawImage(
				image,
				(int)x - GamePanel.WIDTH,
				(int)y,
				null
			);
		}
  }
  
}
