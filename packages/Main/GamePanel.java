package Main;

import GameState.GameStateManager;
import TankGame.LSBackground;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import java.awt.event.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

  //dimensions
  public static final int WIDTH = 540;
  public static final int HEIGHT = 480;
  public static final int WIDTH_SCALE = 2;
  public static final double HEIGHT_SCALE = 2.5;
  public static final int FULL_WIDTH = 1080;
  public static final int FULL_HEIGHT = 480;

  //game thread
  private Thread thread;
  private boolean running;
  private final int FPS = 60;
  private final long targetTime = 1000/FPS;

  //image
  private BufferedImage leftScreen;
  private BufferedImage rightScreen;
  private BufferedImage menuScreen;
  private BufferedImage controlScreen;
  private BufferedImage backgroundScreen;
  private BufferedImage HUDScreen;
  private BufferedImage endScreen;
  private BufferedImage miniMapScreen;
  private Graphics2D gLeftScreen;
  private Graphics2D gRightScreen;
  private Graphics2D gMenuScreen;
  private Graphics2D gControlScreen;
  private Graphics2D gBackgroundScreen;
  private Graphics2D gHUDScreen;
  private Graphics2D gEndScreen;
  
  private boolean alreadyDrawnBackground;
  
  //game state manager
  private GameStateManager gsm;
  
  private LSBackground lsBg;

  public GamePanel() {
    super();
    setPreferredSize(new Dimension(FULL_WIDTH * WIDTH_SCALE, (int)(FULL_HEIGHT * HEIGHT_SCALE)));
    setFocusable(true);
    requestFocus();
  }
  
  @Override
  public synchronized void addNotify() {
    super.addNotify();
    
    if(thread == null) {
      thread = new Thread(this);
      addKeyListener(this);
      thread.start();
    }
  }
  
  private void init() {
    leftScreen = new BufferedImage (WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    rightScreen = new BufferedImage (WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    menuScreen = new BufferedImage (FULL_WIDTH, FULL_HEIGHT, BufferedImage.TYPE_INT_RGB);
    controlScreen = new BufferedImage (FULL_WIDTH, FULL_HEIGHT, BufferedImage.TYPE_INT_RGB);
    backgroundScreen = new BufferedImage (FULL_WIDTH, (int)(FULL_HEIGHT * HEIGHT_SCALE), BufferedImage.TYPE_INT_RGB);
    HUDScreen = new BufferedImage (FULL_WIDTH, (int)(FULL_HEIGHT * (.5)), BufferedImage.TYPE_INT_ARGB);
    endScreen = new BufferedImage (FULL_WIDTH, FULL_HEIGHT, BufferedImage.TYPE_INT_RGB);
    miniMapScreen = new BufferedImage(FULL_WIDTH, (int) (FULL_HEIGHT * 0.5), BufferedImage.TYPE_INT_RGB);
    
    gLeftScreen = (Graphics2D) leftScreen.getGraphics();
    gRightScreen = (Graphics2D) rightScreen.getGraphics();
    gMenuScreen = (Graphics2D) menuScreen.getGraphics();
    gControlScreen = (Graphics2D) controlScreen.getGraphics();
    gBackgroundScreen = (Graphics2D) backgroundScreen.getGraphics();
    gHUDScreen = (Graphics2D) HUDScreen.getGraphics();
    gEndScreen = (Graphics2D) endScreen.getGraphics();
    
    alreadyDrawnBackground = false;
    running = true;
    
    lsBg = new LSBackground("Resources/Desert-Camo.jpg");

    gsm = new GameStateManager();
  }
  
  //game loop
  @Override
  public synchronized void run() {
    init();
    
    long start;
    long elapsed;
    long wait;
    
    while(running) {
      
      start = System.nanoTime();
      update();
      draw();
      drawToScreen();
      
      elapsed = System.nanoTime() - start;
      wait = targetTime - elapsed/1000000;
      
      try {
        if (wait < 0) {
          wait = 5;
        }
        Thread.sleep(wait);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  private synchronized void update() {
    gsm.update();
    if (gsm.getCurrentState() == GameStateManager.MENUSTATE) {
      alreadyDrawnBackground = false;
    }
  }
  
  private synchronized void draw() {
    if (gsm.getCurrentState() == GameStateManager.LEVELSTATE) {
      gsm.draw(gLeftScreen, gRightScreen, gHUDScreen);
    } 
    else if (gsm.getCurrentState() == GameStateManager.MENUSTATE) {
      gsm.draw(gMenuScreen, null , null);
    } 
    else if (gsm.getCurrentState() == GameStateManager.CONTROLSTATE) {
      gsm.draw(gControlScreen, null, null);
    }
    else if(gsm.getCurrentState() == GameStateManager.ENDSTATE) {
        gsm.draw(gEndScreen, null, null);
    }
  }
  
  private synchronized void drawToScreen() {
    Graphics g2 = getGraphics();

    if (gsm.getCurrentState() == GameStateManager.LEVELSTATE) {
      if (!alreadyDrawnBackground) {
        lsBg.draw(gBackgroundScreen);

        g2.drawImage(backgroundScreen, 0, 0, FULL_WIDTH * WIDTH_SCALE, (int)(FULL_HEIGHT * HEIGHT_SCALE), null);
        alreadyDrawnBackground = true;
      }
      
      g2.drawImage(leftScreen, 20, 20, WIDTH * WIDTH_SCALE - 40, HEIGHT * WIDTH_SCALE - 20, null);
      g2.drawImage(rightScreen, FULL_WIDTH +20, 20, WIDTH * WIDTH_SCALE - 40, HEIGHT * WIDTH_SCALE - 20, null);
      g2.drawImage(HUDScreen, 0, FULL_HEIGHT * 2, FULL_WIDTH * WIDTH_SCALE, (int)(FULL_HEIGHT * (.5)), null);
    } 
    else if (gsm.getCurrentState() == GameStateManager.MENUSTATE){
      g2.drawImage(menuScreen, 0, 0, FULL_WIDTH * WIDTH_SCALE, (int)(FULL_HEIGHT * HEIGHT_SCALE), null);
    } 
    else if (gsm.getCurrentState() == GameStateManager.CONTROLSTATE) {
      g2.drawImage(controlScreen, 0, 0, FULL_WIDTH * WIDTH_SCALE, (int)(FULL_HEIGHT * HEIGHT_SCALE), null);
    }
    else if(gsm.getCurrentState() == GameStateManager.ENDSTATE) {
        g2.drawImage(endScreen, 0, 0, FULL_WIDTH * WIDTH_SCALE, (int) (FULL_HEIGHT * HEIGHT_SCALE), null);
    }
    g2.dispose();

  }


  @Override
  public void keyTyped(KeyEvent key)
  {
    
  }

  @Override
  public void keyPressed(KeyEvent key)
  {
    gsm.keyPressed(key.getKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent key)
  {
    gsm.keyReleased(key.getKeyCode());
  }
}
