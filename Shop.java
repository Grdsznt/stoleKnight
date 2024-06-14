import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player can buy a heal at this tile
 * 
 * @author Felix Zhao
 * @version 0.1
 */
public class Shop extends Tile
{
    
    private int type;
    private Label interactionLabel;
    private int cost;
    private boolean bought;
    /**
     * Creates a shop tile
     *
     * @param parent The room it belongs to
     * @param row The row it is in
     * @param col The col it is in
     */
    public Shop(RoomData parent, int row, int col) {
        super(parent, row, col);
        GreenfootImage image = new GreenfootImage("Tiles/notinbattle.png");
        image.scale(48, 48);
        
        setImage(image);
        
        GreenfootImage newImage = new GreenfootImage("healing_potion.png");
        newImage.scale(48, 48);
        image.drawImage(newImage, 0, 0);
        cost = Greenfoot.getRandomNumber(10)+10;
        interactionLabel = new Label("Press E to Heal - " + cost + " coins", 32);
        bought = false;
    }
    
    /**
     * Detects if player is nearby
     *
     */
    public void act() {
        if (getOneIntersectingObject(Hero.class) != null) {
            getWorld().addObject(interactionLabel, getX(), getY()-50);
        } else {
            getWorld().removeObject(interactionLabel);
        }
    }
    
    /**
     * Interact with the shop
     *
     *@param here The hero
     */
    public void interact(Hero hero) {
        if (bought) {
            return;
        }
        
        if (hero.getAmountOfGold() < cost) {
            return;
        }
        
        hero.removeGold(cost);
        hero.heal(5);
        bought = true;
        GreenfootImage image = new GreenfootImage("Tiles/notinbattle.png");
        image.scale(48, 48);
        
        setImage(image);
    }
}
