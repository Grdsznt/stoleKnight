import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Instructions of how to play the game.
 * 
 * Background image: https://wallpapercave.com/soul-knight-wallpapers
 * 
 * @author Jean P
 * @version June 2024
 */
public class InstructionWorld extends World
{
    //Worlds
    private StartWorld sw;
    private GameWorld gw;
    
    //Image
    private static GreenfootImage bgdImage = new GreenfootImage("soul-knight-instruction-bgd.jpg");
    
    //Title
    private Label title = new Label("Instructions", 50, Color.WHITE, Color.WHITE);
    
    //Story
    private SuperTextBox story = new SuperTextBox(new String[]{"You are locked in a hell...", "Try to save yourself by climbing floors, entered rooms, and killed all monsters in order to exit the room!"}, new Color(150,75,0), Color.WHITE, new Font(20), true, 1100, 0, Color.WHITE);
    
    //How to play
    private SuperTextBox a = new SuperTextBox("<a>: move left", new Color(150,75,0,0), Color.WHITE, new Font(20), false, 1000, 0, Color.WHITE);
    private SuperTextBox d = new SuperTextBox("<d>: move right", new Color(150,75,0,0), Color.WHITE, new Font(20), false, 1000, 0, Color.WHITE);
    private SuperTextBox w = new SuperTextBox("<w>: move up", new Color(150,75,0,0), Color.WHITE, new Font(20), false, 1000, 0, Color.WHITE);
    private SuperTextBox s = new SuperTextBox("<s>: move down", new Color(150,75,0,0), Color.WHITE, new Font(20), false, 1000, 0, Color.WHITE);
    
    private SuperTextBox e = new SuperTextBox("<e>: pick weapon up", new Color(150,75,0,0), Color.WHITE, new Font(20), false, 1000, 0, Color.WHITE);
    private SuperTextBox num = new SuperTextBox("1 & 2: switch weapon shown on the left in the 2 boxes", new Color(150,75,0,0), Color.WHITE, new Font(20), false, 1000, 0, Color.WHITE);
    private SuperTextBox click = new SuperTextBox("Click screen: attack", new Color(150,75,0,0), Color.WHITE, new Font(20), false, 1000, 0, Color.WHITE);
    
    
    //Two boxes
    private SuperTextBox back = new SuperTextBox("Back", new Color(150,75,0), Color.WHITE, new Font(20), true, 80, 0, Color.WHITE);
    private SuperTextBox start = new SuperTextBox("Start", new Color(150,75,0), Color.WHITE, new Font(20), true, 80, 0, Color.WHITE);
    
    /**
     * Constructor for objects of class InstructionWorld.
     */
    public InstructionWorld(StartWorld sw)
    {    
        // Create a new world with 1200x720 cells with a cell size of 1x1 pixels.
        super(1200, 720, 1); 
        
        this.sw = sw;
        
        //Set image
        setBackground(bgdImage);
        
        //Add title
        addObject(title, 600, 100);
        
        //Add story
        addObject(story, 600, 180);
        
        //Add how to play
        addObject(a, 600, 250);
        addObject(d, 600, 280);
        addObject(w, 600, 310);
        addObject(s, 600, 340);
        
        addObject(e, 600, 390);
        addObject(num, 600, 420);
        addObject(click, 600, 450);
        
        //Add back and start boxes
        addObject(back, 100, 650);
        addObject(start, 1100, 650);
    }
    
    public void act(){
        //Animate the options while hovering on them
        if(Greenfoot.mouseMoved(back)) {
            back.updateFont(new Font(25), 100);
        } else if(Greenfoot.mouseMoved(start)) {
            start.updateFont(new Font(25), 100);
        }
        
        //Return to normal if not hovering on them
        if(Greenfoot.mouseMoved(this)) {
            back.updateFont(new Font(20), 80);
            start.updateFont(new Font(20), 80);
        }
        
        //Go to each world if pressed
        if(Greenfoot.mousePressed(back)) {
            Greenfoot.setWorld(sw);
        }
        if(Greenfoot.mousePressed(start)) {
            gw = new GameWorld();
            Greenfoot.setWorld(gw);
        }
    }
}
