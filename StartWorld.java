import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This is the StartWorld where the user will see first.
 * 
 * Cover image: https://www.vg247.com/soul-knight-codes
 * Characters: https://wholesomedev.itch.io/kingfree
 * 
 * @author Jean P
 * @version June 2024
 */
public class StartWorld extends World
{
    //Worlds
    private GameWorld gWorld;
    private InstructionWorld instructionsWorld;
    
    //Image
    private GreenfootImage cover = new GreenfootImage("soul-knight-cover.jpg");
    
    //Game title
    private SuperTextBox title = new SuperTextBox("Stole Knight", new Color(150,75,0,0), Color.BLACK, new Font(70), true, 400, 0, Color.WHITE);
    
    //Two boxes
    private SuperTextBox instructions = new SuperTextBox("Instructions", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    private SuperTextBox start = new SuperTextBox("Start", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    
    /**
     * Constructor for objects of class StartWorld.
     */
    public StartWorld()
    {    
        // Create a new world with 1200x720 cells with a cell size of 1x1 pixels.
        super(1200, 720, 1); 
        setBackground(cover);
        
        
        //Add title
        addObject(title, 600, 250);
        
        //Add instruction and start boxes
        addObject(instructions, 600, 400);
        addObject(start, 600, 500);
    }
    
    static {
        Hero.damageSoundPlayer();
    }
    
    public void act(){
        //Animate the options while hovering on them
        if(Greenfoot.mouseMoved(instructions)) {
            instructions.updateFont(new Font(40), 450);
        } else if(Greenfoot.mouseMoved(start)) {
            start.updateFont(new Font(40), 450);
        }
        
        //Return to normal if not hovering on them
        if(Greenfoot.mouseMoved(this)) {
            instructions.updateFont(new Font(30), 400);
            start.updateFont(new Font(30), 400);
        }
        
        //Go to each world if pressed
        if(Greenfoot.mousePressed(instructions)) {
            instructionsWorld = new InstructionWorld(this);
            Greenfoot.setWorld(instructionsWorld);
        }
        if(Greenfoot.mousePressed(start)) {
            gWorld = new GameWorld();
            Greenfoot.setWorld(gWorld);
        }
    }
}
