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
    private boolean opened;
    public Chest(RoomData parent, int row, int col) {
        super(parent, row, col);
        GreenfootImage image = new GreenfootImage("Tiles/chest1.png");
        
        opened = false;
        setImage(image);
        getImage().scale(48, 48);
        interactionLabel = new Label("E to Open", 32);
    }
    
    
    public void act() {
        if (getOneIntersectingObject(Hero.class) != null && !opened) {
            getWorld().addObject(interactionLabel, getX(), getY()-50);
        } else {
            getWorld().removeObject(interactionLabel);
        }
    }
    
    public void interact(Hero hero) {
        if (opened) {
            return;
        }
        opened = true;
        hero.getGold(5);
        GreenfootImage image = new GreenfootImage("Tiles/chest3.png");
        

        setImage(image);
        getImage().scale(48, 48);
    }
}
