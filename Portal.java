import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Portal here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Portal extends Tile
{
    protected static GreenfootImage[] animationFrames = new GreenfootImage[6];
    private SimpleTimer animationTimer = new SimpleTimer();
    private int frame = 0;
    private Label interactionLabel;
    
    public Portal(RoomData parent, int row, int col) {
        super(parent, row, col);
        
        interactionLabel = new Label("E to Enter", 32);
        
        // loading the frames
        for(int i = 0; i < animationFrames.length; i++){
            animationFrames[i] = new GreenfootImage("endportal/portal"+i+".png");
            animationFrames[i].scale(128, 128);
        }
        setImage(animationFrames[0]);
    }
    
    public void act() {
        animate();
        
        if (getOneIntersectingObject(Hero.class) != null) {
            getWorld().addObject(interactionLabel, getX(), getY()-50);
        } else {
            getWorld().removeObject(interactionLabel);
        }
        
    }
    
    public void interact() {
        
    }
    
    
    
    
    private void animate() {
        if(animationTimer.millisElapsed() < 100) return;
        animationTimer.mark();
        
        setImage(animationFrames[frame % animationFrames.length]);
        frame++;
         
    }
}
