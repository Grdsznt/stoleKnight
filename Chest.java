import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Chest here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Chest extends Tile
{
    private Label interactionLabel;
    public Chest(RoomData parent, int row, int col) {
        super(parent, row, col);
        GreenfootImage image = new GreenfootImage("Tiles/chest1.png");
        
        
        setImage(image);
        getImage().scale(48, 48);
        interactionLabel = new Label("E to Open", 32);
    }
    
    
    public void act() {
        if (getOneIntersectingObject(Hero.class) != null) {
            getWorld().addObject(interactionLabel, getX(), getY()-50);
        } else {
            getWorld().removeObject(interactionLabel);
        }
    }
}
