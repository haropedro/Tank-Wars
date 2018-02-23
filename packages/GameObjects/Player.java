package GameObjects;

import BlockMap.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Player extends GameObject {

  //For removing green background
  
  private final int player;
  public final static int FIRST_PLAYER = 1;
  public final static int SECOND_PLAYER = 2;
  
  private final static int MAX_HEALTH = 200;
  private int health;
  private boolean dead;
    
  //attacking
  public final static int MAX_BULLETS = 5;
  private ArrayList<Bullet> bulletList;
  private int numBullets;
  private boolean flinching;
  private int bulletDamage;
  private long flinchTimer;
  
  public Player(BlockMap blockMap, int player)
  {
    super(blockMap);
    this.player = player;
    init();

    loadSprites();
    
    currentAction = 16;
  }
  
  private void init() {
    //tested values
    width = 68;
    height = 64;
    cWidth = 52;
    cHeight = 44;
    
    speed = 0;
    angleSpeed = 0;
    moveSpeed = 1;
    angleMoveSpeed = 1.5;
    
    angle = 180;
    
    maxSpeed = 1.5;
    maxAngleSpeed = 3;
    stopSpeed = 0.5;
    flinchTimer = 100000;
    
    health = 200;
    bulletDamage = 20;
    numBullets = 0;
    bulletList = new ArrayList<>();
    dead = false;
    
    rect = new Rectangle((int)x - cWidth / 2, (int)y - cHeight / 2, cWidth, cHeight);
    collisionBox = rect;
    
  }
  
  private void loadSprites() {
    if (player == 1) {
      loadFramesFromFolder("Resources/t1an64pxl");
    }
    if (player == 2) {
      loadFramesFromFolder("Resources/t2an64pxl");
    }
  }
  
  public int getHealth() { 
    return health;
  }
  
  public void setHealth(int k) {
        health = k;
    }
  
  public int getBulletDamage() {
        return bulletDamage;
    }
  
  private void getNextPosition() {
    //moving in positive x direction
    
     if (turnLeft) {
      angleSpeed += angleMoveSpeed;
      if (angleSpeed > maxAngleSpeed) {
        angleSpeed = maxAngleSpeed;
      }
      angle += angleSpeed;
      
      if (angle > 360) {
        angle = angle - 360 + angleSpeed;
      }
      
       if (forward)
       {
         speed += moveSpeed;
         if (speed > maxSpeed)
         {
           speed = maxSpeed;
         }
       } else if (backwards)
       {
         speed -= moveSpeed;
         if (speed < -maxSpeed)
         {
           speed = -maxSpeed;
         }
       }
    }
    //moving in negative x direction
     if (turnRight) {
      angleSpeed -= angleMoveSpeed;
      if (angleSpeed < -maxAngleSpeed) {
        angleSpeed = -maxAngleSpeed;
      }
      angle += angleSpeed;
      
      if (angle < -360) {
        angle = angle + 360 - angleSpeed;
      }
      
      if (backwards)
      {
        speed -= moveSpeed;
        if (speed < -maxSpeed)
        {
          speed = -maxSpeed;
        }
      } else if (forward)
      {
        speed += moveSpeed;
        if (speed > maxSpeed)
        {
          speed = maxSpeed;
        }
      }
    }
    
    //moving in positive y direction
    if (backwards) {
      speed += moveSpeed;
      if (speed > maxSpeed) {
        speed = maxSpeed;
      }
    }
    //moving in negative y direction
    if (forward) {
      speed -= moveSpeed;
      if (speed < -maxSpeed) {
        speed = -maxSpeed;
      }
    }
    
    //not moving forward or backwards direction
    if (isNotMoving()) {
      //stop moving in the x direction
      if (speed > 0) {
        speed -= stopSpeed;
        if (speed < 0) {
          speed = 0;
        }
      }
      else if (speed < 0) {
        speed += stopSpeed;
        if (speed > 0) {
          speed = 0;
        }
      }
      
     if (isNotTurning()) {
        //stop moving in the y direction
      if (angleSpeed > 0) {
        angleSpeed -= stopSpeed;
        if (angleSpeed < 0) {
          angleSpeed = 0;
        }
      }
      else if (angleSpeed < 0) {
        angleSpeed += stopSpeed;
        if (angleSpeed > 0) {
          angleSpeed = 0;
        }
      }
     }     
    }
  }
  
  private boolean isNotMoving() {
    return !forward && !backwards;
  }
  
  private boolean isNotTurning() {
    return !turnLeft && !turnRight;
  }
  
  private void checkPowerUp() {
    if (useShield) {
      health += 20;
      if (MAX_HEALTH < health) {
        health = MAX_HEALTH;
      }
    }
    setUseShield(false);
  }
  
  public synchronized void update() {
    getNextPosition();
    checkObjectCollision();
    checkBlockMapCollision();
    checkPowerUp();
    setPosition(xTemp, yTemp);
    setTransformation((int)xTemp, (int)yTemp);
    
    if(getHealth() <= 0) {
            setDead(true);
        }
  }
  
  public void setBlockMapPosition(double x, double y) {
    blockMap.setPosition(x, y);
  }
  
  public void setBlockMapArray(Block[][] bm) {
    blockMap.setBlockMapArray(bm);
  }
  
  public Block[][] getBlockMapArray() {
    return blockMap.getBlockMapArray();
  }
  
  public BlockMap getBlockMapObject() {
    return blockMap;
  }
  
  public void setBlockMapObject(BlockMap bm) {
    blockMap = bm;
  }
  
  public int getPlayerNumber() {
    return player;
  }
  
  public void setDead(boolean d) {
      dead = d;
  }

  public boolean getDead() {
      return dead;
  }
  
  public synchronized void draw(Graphics2D g) {
    setMapPosition();
    
    //draw player
    if (flinching) {
      long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
      if (elapsed / 100 % 2 == 0) {
        return;
      }
    }
    if (angle  < 0) {
      angle += 360;
    }
    
    int frame = (int)angle/6 - 1;
    
    if (frame < 0 || frame > 59) {
      frame = 0;
    }
    
    //accounts for the frame displacement in regards to the moving direction
    frame = (frame + 15) % 59;
    
    g.drawImage(frames[frame],
        (int)(x + xMap - width / 2),
        (int)(y + yMap - height / 2),
        null);
  }
  
  public void fire() {
      Bullet b = new Bullet(blockMap, angle, x, y);
      bulletList.add(b);
        setNumBullets(1);
      

      
  }
  
  public int getNumBullets() {
    return numBullets;
  }
  
  public void setNumBullets(int i) {
    numBullets += i;
  }
  
  public ArrayList<Bullet> getBulletList() {
    return bulletList;
  }
  
  public void playerHealth(Graphics2D hp, int x, int y) {
            hp.setColor(Color.RED);
            hp.fillRect(x, y, 200, 50);//(x, y, HP bar length, HP bar height)
            
            hp.setColor(Color.GREEN);
            hp.fillRect(x,
                    y,
                    getHealth(),
                    50);
            hp.setColor(Color.WHITE);
            hp.drawRect(x, y, 200, 50);
        
    }
}
