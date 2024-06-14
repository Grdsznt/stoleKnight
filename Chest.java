import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * <p>
 * The chest tile is openable by the player and can give good loot
 * </p>
 *  
 * @author Felix Zhao
 * @version 0.1
 */
public class Chest extends Tile
{
    private Label interactionLabel;
    private boolean opened;
    /**
     * Creates a new chest
     *
     * @param parent The room it belongs to
     * @param row The row it is in
     * @param col The col it is in
     */
    public Chest(RoomData parent, int row, int col) {
        super(parent, row, col);
        GreenfootImage image = new GreenfootImage("Tiles/chest1.png");
        
        opened = false;
        setImage(image);
        getImage().scale(48, 48);
        interactionLabel = new Label("E to Open", 32);
    }
    
    
    /**
     * Checks if player is nearby to display some text
     *
     */
    public void act() {
        if (getOneIntersectingObject(Hero.class) != null && !opened) {
            getWorld().addObject(interactionLabel, getX(), getY()-50);
        } else {
            getWorld().removeObject(interactionLabel);
        }
    }
    
    /**
     * Interact with the chest - gives gold or a weapon
     *
     * @param hero The hero
     */
    public void interact(Hero hero) {
        if (opened) {
            return;
        }
        opened = true;
        if (Greenfoot.getRandomNumber(3) == 0) {
            int choice = Greenfoot.getRandomNumber(3);
            Weapon newWeapon = new Sword();
            switch (choice) {
                case 0:
                    newWeapon = new Sword();
                    break;
                case 1:
                    newWeapon = new Bow();
                    break;
                case 2:
                    newWeapon = new Wand(5);
                    break;
            }
            getWorld().addObject(newWeapon, getX(), getY()+50);
        } else {
            hero.getGold(5);
        }
       
        GreenfootImage image = new GreenfootImage("Tiles/chest3.png");
        
        setImage(image);
        getImage().scale(48, 48);
    }
}
