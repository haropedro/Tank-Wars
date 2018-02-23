package GameObjects;

import BlockMap.PowerUp;
import BlockMap.Block;
import BlockMap.BlockMap;
import Main.GamePanel;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FilenameFilter;
import javax.imageio.ImageIO;

public abstract class GameObject {

  protected File dir;
  protected String[] EXTENSIONS;
  protected FilenameFilter IMAGE_FILTER;

  protected Image image;
  protected BlockMap blockMap;
  protected Rectangle rect;
  protected Shape collisionBox;
  protected Player otherPlayer;
  protected int blockSize;

  protected boolean intersected;
  protected boolean topInter, botInter, rightInter, leftInter;
  protected boolean useShield;

  //dimensions
  protected int width, height;

  //position
  protected double x, y;
  protected double xMap, yMap;

  //speed
  protected double xSpeed, ySpeed;

  //collision box
  protected int cWidth, cHeight;

  protected double angle;
  protected double angleMoveSpeed;
  protected double angleSpeed;
  protected double maxAngleSpeed;

  protected double speed;

  //collision
  protected int curRow, curCol;
  protected double xDest, yDest, xTemp, yTemp;
  protected boolean topLeft, topRight, bottomLeft, bottomRight;
  protected boolean tLShield1, tRShield1, bLShield1, bRShield1;
  protected boolean tLShield2, tRShield2, bLShield2, bRShield2;
  private double xDif, yDif;

  protected int currentAction, prevAction;

  //movement
  protected boolean forward, turnLeft, backwards, turnRight;
  protected double moveSpeed, maxSpeed, stopSpeed;
  protected BufferedImage[] frames;

  public GameObject(BlockMap blockMap) {
    this.blockMap = blockMap;
    blockSize = blockMap.getBlockSize();
  }

  public boolean bulletIntersects(GameObject obj) {
    Rectangle r1 = getRectangle();
    Rectangle r2 = obj.getRectangle();
    return r1.intersects(r2);
  }

  public boolean intersects(GameObject obj) {
    return collisionBox.intersects(obj.getCollisionBox().getBounds2D());
  }

  public Rectangle getRectangle() {
    return new Rectangle((int) x - cWidth/2, (int) y - cHeight/2, cWidth, cHeight);
  }

  public void setTransformation(int x, int y) {
    AffineTransform at = new AffineTransform();
    rect.setBounds(x - cWidth / 2, y - cHeight / 2, cWidth, cHeight);
    at.rotate(Math.toRadians(-angle), rect.getCenterX(), rect.getCenterY());
    collisionBox = at.createTransformedShape(rect);
  }

  public void calculateCorners(double x, double y) {

    //surrounding tiles for 64pxl
    int leftTile = (int) (x - cWidth / 2) / blockSize;
    int rightTile = (int) (x + cWidth / 2 - 1) / blockSize;
    int topTile = (int) (y - cHeight / 2) / blockSize;
    int bottomTile = (int) (y + cHeight / 2 - 1) / blockSize;

    //four corners for 64pxl
    int tL = blockMap.getType(topTile, leftTile);
    int tR = blockMap.getType(topTile, rightTile);
    int bL = blockMap.getType(bottomTile, leftTile);
    int bR = blockMap.getType(bottomTile, rightTile);

    //handles objects larger than 32pxl
    if (cWidth > 32 && height > 32) {
      int cWidthX = 32;
      int cHeightX = 32;
      int xBuffer = 10;
      int leftTileX = (int) (x - xBuffer - blockSize + cWidthX / 2) / blockSize;
      int rightTileX = (int) (x + xBuffer + cWidthX / 2 - 1) / blockSize;
      int topTileX = (int) (y - cHeightX / 2) / blockSize;
      int bottomTileX = (int) (y + cHeightX / 2 - 1) / blockSize;
      int tLX = blockMap.getType(topTileX, leftTileX);
      int tRX = blockMap.getType(topTileX, rightTileX);
      int bLX = blockMap.getType(bottomTileX, leftTileX);
      int bRX = blockMap.getType(bottomTileX, rightTileX);

      int cWidthY = 32;
      int cHeightY = 44;
      int yBuffer = 10;
      int leftTileY = (int) (x - yBuffer + cWidthY / 2) / blockSize;
      int rightTileY = (int) (x + yBuffer + cWidthY / 2 - 1) / blockSize;
      int topTileY = (int) (y - cHeightY / 2) / blockSize;
      int bottomTileY = (int) (y + cHeightY / 2 - 1) / blockSize;
      int tLY = blockMap.getType(topTileY, leftTileY);
      int tRY = blockMap.getType(topTileY, rightTileY);
      int bLY = blockMap.getType(bottomTileY, leftTileY);
      int bRY = blockMap.getType(bottomTileY, rightTileY);

      //set boolean blocks
      topLeft = (tL != Block.EMPTY_TILE) || (tLX != Block.EMPTY_TILE) || (tLY != Block.EMPTY_TILE);
      topRight = (tR != Block.EMPTY_TILE) || (tRX != Block.EMPTY_TILE) || (tRY != Block.EMPTY_TILE);
      bottomLeft = (bL != Block.EMPTY_TILE) || (bLX != Block.EMPTY_TILE) || (bLY != Block.EMPTY_TILE);
      bottomRight = (bR != Block.EMPTY_TILE) || (bRX != Block.EMPTY_TILE) || (bRY != Block.EMPTY_TILE);

      //set PowerUps
      //player 1
      tLShield1 = (tL == PowerUp.P1_SHIELD) || (tLX == PowerUp.P1_SHIELD) || (tLY == PowerUp.P1_SHIELD);
      tRShield1 = (tR == PowerUp.P1_SHIELD) || (tRX == PowerUp.P1_SHIELD) || (tRY == PowerUp.P1_SHIELD);
      bLShield1 = (bL == PowerUp.P1_SHIELD) || (bLX == PowerUp.P1_SHIELD) || (bLY == PowerUp.P1_SHIELD);
      bRShield1 = (bR == PowerUp.P1_SHIELD) || (bRX == PowerUp.P1_SHIELD) || (bRY == PowerUp.P1_SHIELD);
      //player 2
      tLShield2 = (tL == PowerUp.P2_SHIELD) || (tLX == PowerUp.P2_SHIELD) || (tLY == PowerUp.P2_SHIELD);
      tRShield2 = (tR == PowerUp.P2_SHIELD) || (tRX == PowerUp.P2_SHIELD) || (tRY == PowerUp.P2_SHIELD);
      bLShield2 = (bL == PowerUp.P2_SHIELD) || (bLX == PowerUp.P2_SHIELD) || (bLY == PowerUp.P2_SHIELD);
      bRShield2 = (bR == PowerUp.P2_SHIELD) || (bRX == PowerUp.P2_SHIELD) || (bRY == PowerUp.P2_SHIELD);
    } else {
      topLeft = (tL != Block.EMPTY_TILE);
      topRight = (tR != Block.EMPTY_TILE);
      bottomLeft = (bL != Block.EMPTY_TILE);
      bottomRight = (bR != Block.EMPTY_TILE);

      //power ups
      //player 1
      tLShield1 = (tL == PowerUp.P1_SHIELD);
      tRShield1 = (tR == PowerUp.P1_SHIELD);
      bLShield1 = (bL == PowerUp.P1_SHIELD);
      bRShield1 = (bR == PowerUp.P1_SHIELD);
      //player 2
      tLShield2 = (tL == PowerUp.P2_SHIELD);
      tRShield2 = (tR == PowerUp.P2_SHIELD);
      bLShield2 = (bL == PowerUp.P2_SHIELD);
      bRShield2 = (bR == PowerUp.P2_SHIELD);
    }
  }

  /*
   if moving in a direction, check the corners of the
   next tile in that direction to see if its an EMPTY_TILE or not.
   if it is not an EMPTY_TILE, then stop moving in that direction,
   else continue moving
   */
  public void checkObjectCollision() {

    if (intersects(otherPlayer)) {
      xDif = getX() - otherPlayer.getX();
      yDif = getY() - otherPlayer.getY();

      //locate sides of intersection
      //moving forward
//        if (speed < 0)
      if (forward) {
        //other player is below them
        if (yDif < 0) {
          botInter = true;
        } //other player is above them
        else if (yDif > 0) {
          topInter = true;
        }
        //other player is on the right
        if (xDif < 0) {
          rightInter = true;
        } //other player is on the left
        else if (xDif > 0) {
          leftInter = true;
        }
      }

//        if (speed > 0)
      if (backwards) {
        //other player is below them
        if (yDif < 0) {
          botInter = true;
        } //other player is above them
        else if (yDif > 0) {
          topInter = true;
        }

        //other player is on the right
        if (xDif < 0) {
          rightInter = true;
        } //other player is on the left
        else if (xDif > 0) {
          leftInter = true;
        }
      }
    } else {
      botInter = topInter = rightInter = leftInter = false;
    }
  }

  public void checkBlockMapCollision() {

    boolean speedBoosted = false;
    curCol = (int) x / blockSize;
    curRow = (int) y / blockSize;

    xDest = x + speed * Math.sin(Math.toRadians(angle));
    yDest = y + speed * Math.cos(Math.toRadians(angle));

    xTemp = x;
    yTemp = y;

    int player;
    if (otherPlayer.getPlayerNumber() == 1) {
      player = 2;
    } else {
      player = 1;
    }
    //tank direction is relative to the front of the tank
    //y direction movement
    calculateCorners(x, yDest);

    //tank moving forwards
    if (speed < 0) {
      //if there is a block above, stop the speed and place
      //the object right below the block

      if (topLeft || topRight || topInter) {

        if ((leftInter || rightInter) && ((angle >= 0 && angle <= 90) || (angle > 270))) { //tank facing up, moving up
          //yTemp = (curRow + 1) * blockSize + cHeight / 2;
          speed = 0;
          yTemp++;
        } else if ((player == 1 && tRShield1) || (player == 1 && tLShield1)
            || (player == 2 && tRShield2) || (player == 2 && tLShield2)) {
          if (!speedBoosted) {
            xTemp += speed * Math.sin(Math.toRadians(angle));
            yTemp += speed * Math.cos(Math.toRadians(angle));
            speedBoosted = true;
          }
          blockMap.setBlockType(Block.EMPTY_TILE, (int) yTemp / blockSize - 1, (int) xTemp / blockSize);
          useShield = true;
        } else {
          speed = 0;
          yTemp = curRow * blockSize + cHeight / 2 + 1;
        }
      } else if (bottomLeft || bottomRight || botInter) {

        if ((leftInter && rightInter) && (angle > 90 && angle <= 270)) { //tank facing down, moving down
          speed = 0;
          yTemp--;
        } else if ((player == 1 && bRShield1) || (player == 1 && bLShield1)
            || (player == 2 && bRShield2) || (player == 2 && bLShield2)) {
          if (!speedBoosted) {
            xTemp += speed * Math.sin(Math.toRadians(angle));
            yTemp += speed * Math.cos(Math.toRadians(angle));
            speedBoosted = true;
          }
          blockMap.setBlockType(Block.EMPTY_TILE, (int) yTemp / blockSize + 1, (int) xTemp / blockSize);
          useShield = true;
        } else {
          speed = 0;
          yTemp = (curRow + 1) * blockSize - cHeight / 2;
        }
      } else {
        if (!speedBoosted) {
          xTemp += speed * Math.sin(Math.toRadians(angle));
          yTemp += speed * Math.cos(Math.toRadians(angle));
          speedBoosted = true;
        }
      }
    }
    //tank going backwards
    if (speed > 0) {
      if (bottomLeft || bottomRight || botInter) {

        if (((leftInter || rightInter) && (angle > 90 && angle <= 270))) {//tank facing up, moving down
          speed = 0;
          yTemp--;
        } else if ((player == 1 && bRShield1) || (player == 1 && bLShield1)
            || (player == 2 && bRShield2) || (player == 2 && bLShield2)) {
          if (!speedBoosted) {
            xTemp += speed * Math.sin(Math.toRadians(angle));
            yTemp += speed * Math.cos(Math.toRadians(angle));
            speedBoosted = true;
          }
          blockMap.setBlockType(Block.EMPTY_TILE, (int) yTemp / blockSize + 1, (int) xTemp / blockSize);
          useShield = true;
        } else {
          speed = 0;
          yTemp = (curRow + 1) * blockSize - cHeight / 2;
        }
      } else if (topLeft || topRight || topInter) {

        if (((leftInter || rightInter) && ((angle >= 0 && angle <= 90) || (angle > 270)))) {//tank facing down, moving up
          speed = 0;
          yTemp++;
        } else if ((player == 1 && tRShield1) || (player == 1 && tLShield1)
            || (player == 2 && tRShield2) || (player == 2 && tLShield2)) {
          if (!speedBoosted) {
            xTemp += speed * Math.sin(Math.toRadians(angle));
            yTemp += speed * Math.cos(Math.toRadians(angle));
            speedBoosted = true;
          }
          blockMap.setBlockType(Block.EMPTY_TILE, (int) yTemp / blockSize - 1, (int) xTemp / blockSize);
          useShield = true;
        } else {
          speed = 0;
          yTemp = curRow * blockSize + cHeight / 2 + 1;
        }
      } else {
        if (!speedBoosted) {

          xTemp += speed * Math.sin(Math.toRadians(angle));
          yTemp += speed * Math.cos(Math.toRadians(angle));
          speedBoosted = true;
        }
      }
    }

    
//    //x direction movement
    calculateCorners(xDest, y);
    //tank moving forward
    if (speed < 0) {
      if (topLeft || bottomLeft || leftInter) {

        if (((topInter || botInter) && (angle >= 0 && angle <= 180))) {//tank facing left, moving left
          xTemp++;
        } else if ((player == 1 && tLShield1) || (player == 1 && bLShield1)
            || (player == 2 && tLShield2) || (player == 2 && bLShield2)) {
          if (!speedBoosted) {
            xTemp += speed * Math.sin(Math.toRadians(angle));
            yTemp += speed * Math.cos(Math.toRadians(angle));
            speedBoosted = true;
          }
          blockMap.setBlockType(Block.EMPTY_TILE, (int) yTemp / blockSize, (int) xTemp / blockSize - 1);
          useShield = true;
        } else {
          speed = 0;
          xTemp = curCol * blockSize + cWidth / 2;
        }
      } else if (topRight || bottomRight || rightInter) {

        if (((topInter || botInter) && angle > 180)) { //tank facing right, moving right
          xTemp--;
        } else if ((player == 1 && tRShield1) || (player == 1 && bRShield1)
            || (player == 2 && tRShield2) || (player == 2 && bRShield2)) {
          if (!speedBoosted) {
            xTemp += speed * Math.sin(Math.toRadians(angle));
            yTemp += speed * Math.cos(Math.toRadians(angle));
            speedBoosted = true; 
          }
          blockMap.setBlockType(Block.EMPTY_TILE, (int) yTemp / blockSize, (int) xTemp / blockSize + 1);
          useShield = true;
        } else {
          speed = 0;
          xTemp = (curCol + 1) * blockSize - cWidth / 2;
        }
      } else {
        if (!speedBoosted) {

          xTemp += speed * Math.sin(Math.toRadians(angle));
          yTemp += speed * Math.cos(Math.toRadians(angle));
          speedBoosted = true;
        }
      }
    }
    //tank moving backwards
    if (speed > 0) {
      if (topRight || bottomRight || rightInter) {

        if (((topInter || botInter) && angle > 180)) {//tank facing left, moving right
          xTemp--;
        } else if ((player == 1 && tRShield1) || (player == 1 && bRShield1)
            || (player == 2 && tRShield2) || (player == 2 && bRShield2)) {
          if (!speedBoosted) {
            xTemp += speed * Math.sin(Math.toRadians(angle));
            yTemp += speed * Math.cos(Math.toRadians(angle));
            speedBoosted = true;
          }
          blockMap.setBlockType(Block.EMPTY_TILE, (int) yTemp / blockSize, (int) xTemp / blockSize + 1);
          useShield = true;
        } else {
          speed = 0;
          xTemp = (curCol + 1) * blockSize - cWidth / 2;
        }
      } else if (topLeft || bottomLeft || leftInter) {

        if (((topInter || botInter) && (angle >= 0 && angle <= 180))) {//tank facing right, moving left
          xTemp++;
        } else if ((player == 1 && tLShield1) || (player == 1 && bLShield1)
            || (player == 2 && tLShield2) || (player == 2 && bLShield2)) {
          if (!speedBoosted) {
            xTemp += speed * Math.sin(Math.toRadians(angle));
            yTemp += speed * Math.cos(Math.toRadians(angle));
            speedBoosted = true;
          }
          blockMap.setBlockType(Block.EMPTY_TILE, (int) yTemp / blockSize, (int) xTemp / blockSize - 1);
          useShield = true;
        } else {
          speed = 0;
          xTemp = curCol * blockSize + cWidth / 2;
        }
      } else {
        if (!speedBoosted) {
          xTemp += speed * Math.sin(Math.toRadians(angle));
          yTemp += speed * Math.cos(Math.toRadians(angle));
          speedBoosted = true;
        }
      }
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

    Image pic;
    //if it is a shell
    if (width == 24 && height == 24) {
      pic = makeColorTransparent(image, Color.BLACK);
    } //if it is a tank
    else {
      pic = makeColorTransparent(image, Color.GREEN);
    }
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
    frames = new BufferedImage[60];
    int count = 0;

    if (dir.isDirectory()) {
      for (final File f : dir.listFiles(IMAGE_FILTER)) {
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

  public int getX() {
    return (int) x;
  }

  public int getY() {
    return (int) y;
  }

  public int getXMap() {
    return (int) xMap;
  }

  public int getYMap() {
    return (int) yMap;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getCHeight() {
    return cHeight;
  }

  public int getCWidth() {
    return cWidth;
  }

  public Shape getCollisionBox() {
    return collisionBox;
  }

  public void setOtherPlayer(Player otherPlayer) {
    this.otherPlayer = otherPlayer;
  }

  public void setPosition(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void setSpeeds(double xSpeed, double ySpeed) {
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
  }

  public void setAngle(int a) {
    angle = a;
  }

  public void setMapPosition() {
    xMap = blockMap.getX();
    yMap = blockMap.getY();
  }

  public boolean notOnScreen() {
    return (x + xMap + width < 0)
        || (x + xMap - width > GamePanel.WIDTH)
        || (y + yMap + height < 0)
        || (y + yMap - height > GamePanel.HEIGHT);
  }

  public void setTurnRight(boolean b) {
    turnRight = b;
  }

  public void setTurnLeft(boolean b) {
    turnLeft = b;
  }

  public void setForward(boolean b) {
    forward = b;
  }

  public void setBackwards(boolean b) {
    backwards = b;
  }

  public void setCurrentAction(int k) {
    currentAction = k;
  }

  public void setPrivousAction(int k) {
    prevAction = k;
  }

  public void setUseShield(boolean b) {
    useShield = b;
  }
}
