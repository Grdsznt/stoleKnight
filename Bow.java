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
    private static GreenfootImage[] bowFramesRight = {new GreenfootImage("weapon_bow0.png"), new GreenfootImage("weapon_bow1.png")};
    private static GreenfootImage[] bowFramesLeft = {new GreenfootImage("weapon_bow0.png"), new GreenfootImage("weapon_bow1.png")};;
    private boolean shootOneArrow;
    private Hero hero;
    
    /**
     * Constructor of Bow to set its original image.
     */
    public Bow(int damage){
        super(damage);
        setImage(bowFramesRight[0]);
        shootOneArrow = true;
        
        for(int i = 0; i < bowFramesLeft.length; i++){
            bowFramesLeft[i].mirrorHorizontally();
        }
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
        if(hero == null) return;
        if(isAttacking && shootOneArrow){
            getWorld().addObject(new Projectile(GameWorld.getMouseX(), GameWorld.getMouseY(), damage), getX(), getY());
            shootOneArrow = false;
        } else if(!isAttacking) {
            shootOneArrow = true;
        }
    }
    
    private void animation(){
        if(hero == null) return;
        if(hero.right){
            if(isAttacking) setImage(bowFramesRight[1]);
            else setImage(bowFramesRight[0]);
        } else {
            if(isAttacking) setImage(bowFramesLeft[1]);
            else setImage(bowFramesLeft[0]);
        }
    }
}
