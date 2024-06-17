import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The ending world after losing or beating 10 floors
 * 
 * @author Felix Zhao, Jean Pan
 * @version 0.1
 */
public class EndWorld extends World
{
    private Label returnButton;
    private int state, floor, health, energy, slot1, slot2;
    
    //Images
    private GreenfootImage win = new GreenfootImage("win1.png");
    private GreenfootImage die = new GreenfootImage("die1.png");
    
    //Game info
    private Label floorAcheived;
    private Label healthRemained;
    
    /**
     * Creates the end world. What is displayed depends on the state
     *
     * @param state State 0 - Loss | State 1 - Win
     */
    public EndWorld(int state, int floor, int health, int energy, int slot1, int slot2)
    {    
        super(1200, 720, 1);
        this.state = state;
        this.floor = floor;
        this.health = health;
        this.energy = energy;
        this.slot1 = slot1;
        this.slot2 = slot2;
        if (state == 0) {
            //addObject(new Label("You Died :(", 64, new Color(200, 0, 0), new Color(255, 0, 0)), 600, 120);
            setBackground("death_screen.png");
            getBackground().drawImage(die, (getWidth()-die.getWidth())/2, 200);
        } else {
            //addObject(new Label("Victory!!!", 48, new Color(0, 200, 0), new Color(0, 255, 0)), 600, 120);
            setBackground("victory_background.png");
            getBackground().drawImage(win, (getWidth()-win.getWidth())/2, 250);
        }
        
        //Game info
        floorAcheived = new Label("Floor Achieved: " + floor + "/10", 50, new Color(255, 255, 255), new Color(255, 255, 255));
        addObject(floorAcheived, 600, 450);
        healthRemained = new Label("Health Remained: " + health + "/10", 50, new Color(255, 255, 255), new Color(255, 255, 255));
        addObject(healthRemained, 600, 500);
        
        //Return
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
