import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Weapon here.
 * 
 * @author Andy Feng
 * @version 1.0
 */
public abstract class Weapon extends Actor
{
    /**
     * Act - do whatever the Weapon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    protected boolean isAttacking;
    
    public Weapon(){
        isAttacking = false;
    }
    
    public void act()
    {
        // Add your action code here.
        updateAttack();
    }
    
    private void updateAttack() {
        GameWorld world = (GameWorld) getWorld();
        if(isAttacking && (world.hasMouseDragEnded(null) || world.isMouseClicked(null))) isAttacking = false;
        if(!isAttacking && world.isMousePressed(null)) isAttacking = true;
    }
    
    public abstract void attack();
}
