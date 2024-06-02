import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * One of the weapons: bow. This is couples with an arrow.
 * 
 * @author Jean Pan
 * @version May 2024
 */
public class Bow extends Weapon
{
    //Attacking variables
    private boolean isAttacking = false;
    private int actCount = 0;
    //Attacking frames
    private static GreenfootImage[] bowFrames = {new GreenfootImage("weapon_bow0.png"), new GreenfootImage("weapon_bow1.png")};
    
    /**
     * Constructor of Bow to set its original image.
     */
    public Bow(){
        setImage(bowFrames[0]);
    }
    
    public void act()
    {
        if(isAttacking){
            //Animate it
            attack();
            actCount++;
        } else {
            actCount = 0;
        }
    }
    
    /**
     * Animate it to attack.
     */
    public void attack(){
        //Attack by pulling the bow
        if(actCount==0){
            setImage(bowFrames[1]);
            return;
        }
        
        //If pass 0.5 s, finish attacking by releasing
        if(actCount==30){
            setImage(bowFrames[0]);
            isAttacking = false;
            return;
        }
    }
}
