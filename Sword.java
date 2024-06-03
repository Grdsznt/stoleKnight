import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Sword here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Sword extends Weapon
{
    /**
     * Act - do whatever the Sword wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GreenfootImage rustySword = new GreenfootImage("images/0x72_DungeonTilesetII_v1.7/frames/weapon_rusty_sword.png"); 
    private static int damage = 1;
    private int range;
    
    public Sword() {
        super(damage);
    }
    
    public void act()
    {
        // Add your action code here.
        super.act();
    }
    
    public void attack() {
        
    }
}
