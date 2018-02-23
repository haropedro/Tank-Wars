package GameState;

import TankGame.Background;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ControlState extends GameState {

    private Background bg;
    private Font font;
    private String[] controls = {"Forward", "Backward", "Turn Left", "Turn Right", "Shoot"};
    private String[] player2Controls = {"Up Arrow Key", "Down Arrow Key", "Left Arrow Key", "Right Arrow Key", "Enter"};
    private String[] player1Controls = {"W Key", "S Key", "A Key", "D Key", "Space"};

    public ControlState(GameStateManager gsm) {
        this.gsm = gsm;

        try {
            bg = new Background("Resources/TankGameMenu1080x480.jpg");
            font = new Font("Arial", Font.PLAIN, 28);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public synchronized void draw(Graphics2D gControlScreen, Graphics2D nullValue, Graphics2D nullValue2) {
        bg.draw(gControlScreen);
        gControlScreen.setColor(Color.WHITE);
        gControlScreen.drawString("Player 1", 125, 65);
        gControlScreen.drawString("Player 2", 565, 65);

        gControlScreen.setFont(new Font("Arial", Font.PLAIN, 20));
        //player one
        for (int i = 0; i < 5; i++) {
            gControlScreen.drawString(controls[i] + ": ", 100, 100 + i * 35);
            gControlScreen.drawString(player2Controls[i], 200, 100 + i * 35);
            
            gControlScreen.drawString(controls[i] + ": ", 560, 100 + i * 35);
            gControlScreen.drawString(player1Controls[i], 660, 100 + i * 35);
        }
        
        //back button settings

        gControlScreen.setFont(font);
        gControlScreen.setColor(Color.RED);
        gControlScreen.drawString("Back", 345, 300);
        
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    @Override
    public void keyReleased(int k) {
    }

}
