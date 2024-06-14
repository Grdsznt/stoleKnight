import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The ending world after losing or beating 10 floors
 * 
 * @author Felix Zhao
 * @version 0.1
 */
public class EndWorld extends World
{
    private Label returnButton;
    private int state;
    
    /**
     * Creates the end world. What is displayed depends on the state
     *
     * @param state State 0 - Loss | State 1 - Win
     */
    public EndWorld(int state)
    {    
        super(1200, 720, 1);
        this.state = state;
        if (state == 0) {
            addObject(new Label("You Died :(", 64, new Color(200, 0, 0), new Color(255, 0, 0)), 600, 120);
            setBackground("death_screen.png");
        } else {
            addObject(new Label("Victory!!!", 48, new Color(0, 200, 0), new Color(0, 255, 0)), 600, 120);
            setBackground("victory_background.png");
        }
        returnButton = new Label("Return To Home", 48);
        addObject(returnButton, 600, 600);
        
    }
    
    /**
     * Checks if return button is pressed
     *
     */
    public void act() {
        if (Greenfoot.mouseMoved(returnButton)) {
            returnButton.setFillColor(new Color(200, 200, 200));
        } else if (Greenfoot.mouseMoved(null)) {
            returnButton.setFillColor(new Color(255, 255, 255));
        }
        if (Greenfoot.mouseClicked(returnButton)) {
            Greenfoot.setWorld(new StartWorld());
        }
    }
}
