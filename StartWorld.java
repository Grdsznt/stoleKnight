import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This is the StartWorld where the user will see first.
 * 
 * @author Jean P
 * @version June 2024
 */
public class StartWorld extends World
{
    private GameWorld gWorld;
    
    //Two boxes
    private static SuperTextBox instructions = new SuperTextBox("Instructions", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    private static SuperTextBox start = new SuperTextBox("Start", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    
    /**
     * Constructor for objects of class StartWorld.
     */
    public StartWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 720, 1); 
        
        //Add instruction and start boxes
        addObject(instructions, 600, 300);
        addObject(start, 600, 400);
    }
    
    public void act(){
        //Animate the options while hovering on them
        if(Greenfoot.mouseMoved(instructions)) {
            instructions.updateFont(new Font(40));
        } else if(Greenfoot.mouseMoved(start)) {
            start.updateFont(new Font(40));
        }
        
        //Return to normal if not hovering on them
        if(Greenfoot.mouseMoved(this)) {
            instructions.updateFont(new Font(30));
            start.updateFont(new Font(30));
        } 
        
        //Go to each world if pressed
        /*if(Greenfoot.mousePressed(instructions))
        {
            instructionsWorld = new Instructions(this);
            Greenfoot.setWorld(instructionsWorld);
        }*/
        if(Greenfoot.mousePressed(start)) {
            gWorld = new GameWorld();
            Greenfoot.setWorld(gWorld);
        }
    }
}
