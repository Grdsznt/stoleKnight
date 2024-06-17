import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The portal brings the player to the next floor but also the BuffWorld
 * 
 * @author Felix Zhao
 * @version 0.1
 */
public class Portal extends Tile
{
    protected static GreenfootImage[] animationFrames = new GreenfootImage[6];
    private SimpleTimer animationTimer = new SimpleTimer();
    private int frame = 0;
    private Label interactionLabel;
    
    /**
     * Creates a portal to the next floor
     *
     * @param parent The room it belongs to
     * @param row The row it is in
     * @param col The col it is in
     */
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
    
    /**
     * Animates and tracks if the player is nearby
     *
     */
    public void act() {
        animate();
        
        if (getOneIntersectingObject(Hero.class) != null) {
            getWorld().addObject(interactionLabel, getX(), getY()-50);
        } else {
            getWorld().removeObject(interactionLabel);
        }
        
    }
    
    /**
     * Removes the label when interacted with
     *
     */
    public void interact() {
        getWorld().removeObject(interactionLabel);
    }
    
    
    private void animate() {
        if(animationTimer.millisElapsed() < 100) return;
        animationTimer.mark();
        
        setImage(animationFrames[frame % animationFrames.length]);
        frame++;
         
    }
}
