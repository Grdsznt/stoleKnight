import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * One of the weapons: bow. This is couples with an arrow.
 * 
 * @author Jean Pan
 * @version May 2024
 * 
 * Edited by Andy Feng
 */
public class Bow extends Weapon
{
    private int actCount = 0;
    //Attacking frames
    private static GreenfootImage[] bowFrames = {new GreenfootImage("weapon_bow0.png"), new GreenfootImage("weapon_bow1.png")};
    private boolean shootOneArrow;
    
    /**
     * Constructor of Bow to set its original image.
     */
    public Bow(int damage){
        super(damage);
        setImage(bowFrames[0]);
        shootOneArrow = true;
    }
    
    public void act()
    {
        super.act();
        animation();
        //turnTowards(GameWorld.getMouseX(), GameWorld.getMouseY());
        attack();
    }
    
    /**
     * Animate it to attack.
     */
    public void attack(){
        //Attack by pulling the bow
        if(isAttacking && shootOneArrow){
            getWorld().addObject(new Projectile(GameWorld.getMouseX(), GameWorld.getMouseY(), damage), getX(), getY());
            shootOneArrow = false;
        } else if(!isAttacking) {
            shootOneArrow = true;
        }
    }
    
    private void animation(){
        if(isAttacking) setImage(bowFrames[1]);
        else setImage(bowFrames[0]);
    }
}
