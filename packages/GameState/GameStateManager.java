package GameState;

import java.awt.Graphics2D;
import java.util.ArrayList;

/*
Switches between different Game States
*/
public class GameStateManager
{
  private ArrayList<GameState> gameStates;
  private int currentState;
  
  public static final int MENUSTATE = 0;
  public static final int LEVELSTATE = 1;
  public static final int CONTROLSTATE = 2;
  public static final int ENDSTATE = 3;
  
  public GameStateManager() {
    
    gameStates = new ArrayList();
    
    currentState = MENUSTATE;
try {
    gameStates.add(new MenuState(this));
    gameStates.add(new LevelState(this));
    gameStates.add(new ControlState(this));
    gameStates.add(new EndState(this));
} catch (Exception e) {
  e.printStackTrace();
}

  }
  
  public void setState(int state) {
    currentState = state;
    if (currentState == MENUSTATE) {
      gameStates.get(currentState).init();
    }
  }
  
  public void update() {
    gameStates.get(currentState).update();
  }

  public void draw(Graphics2D gLeftScreen, Graphics2D gRightScreen, Graphics2D gBackgroundScreen) {
    gameStates.get(currentState).draw(gLeftScreen, gRightScreen, gBackgroundScreen);
  }
  
  public void keyPressed(int k) {
    gameStates.get(currentState).keyPressed(k);
  }
  
  public void keyReleased(int k) {
    gameStates.get(currentState).keyReleased(k);
  }
  
  public int getCurrentState() {
    return currentState;
  }
  
  public ArrayList getGameState() {
      return gameStates;
  }
  
  public void setWinner(int k) {
    gameStates.get(currentState).setWinner(k);
  }
}
