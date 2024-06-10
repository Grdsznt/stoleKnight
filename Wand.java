import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Wand here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wand extends Weapon
{
    /**
     * Act - do whatever the Wand wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    GreenfootImage wand = new GreenfootImage("weapon_red_magic_staff.png");
    private int damage;
    
    public void act()
    {
        super.act();
        animate();
    }
    
    public Wand(int damage) {
        super(damage);
        this.damage = damage;
        setImage(wand);
    }
    
    public void attack() {
        
    }
    
    private void animate(){
        if(isAttacking)  {
            setRotation(20); 
        }
        else {
            setRotation(0);
        }
    }
}
