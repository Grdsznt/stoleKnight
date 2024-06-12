import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Wand here.
 * 
 * @author Edwin Dong, Andy Feng
 * @version (a version number or a date)
 */
public class Wand extends Weapon
{
    /**
     * Act - do whatever the Wand wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    GreenfootImage wand = new GreenfootImage("weapon_red_magic_staff.png");
    private int damage, actNum;
    private BallProjectile proj;
    private boolean attackOnce;
    
    public void act()
    {
        super.act();
        animate();
        if (getHolder() instanceof Enemy && isAttacking == true && actNum % 22 == 0) isAttacking = false;
        if (getHolder() instanceof Hero) isAttacking = GameWorld.isMouseHolding();
        if(!GameWorld.isMouseHolding()) attackOnce = true;
        attack();
        actNum++;
    }
    
    public Wand(int damage) {
        super(damage);
        this.damage = damage;
        setImage(wand);
        actNum = 0;
        attackOnce = true;
    }
    
    public void shoot(int dx, int dy) {
        isAttacking = true;
        double clockwiseAngle = 0;
        // Calculate angle in radians from enemy to hero
        double angleRadians = Math.atan2(dy, dx);
    
        // Convert radians to degrees
        double angleDegrees = Math.toDegrees(angleRadians);
    
        // Adjust for Greenfoot's coordinate system (clockwise and 0-360)
        clockwiseAngle = (angleDegrees + 360) % 360;
        proj = new BallProjectile(20, 3, (int)clockwiseAngle); 
        getWorld().addObject(proj, getX(), getY()-15);
    }
    
    public void attack() {
        if(getHolder() instanceof Hero && beingUsed && isAttacking && attackOnce) {
            attackOnce = false;
            shoot(GameWorld.getMouseX() - getX(), GameWorld.getMouseY() - getY());
        }
    }
    
    private void animate(){
        if(isAttacking)  {
            if(getHolder() instanceof Hero) setRotation(((Hero)getHolder()).right ? 20 : -20); 
            else setRotation(20);
        } else {
            setRotation(0);
        }
    }
    
    public void followWizard(Wizard w){
        setLocation(w.getX()-4, w.getY()+17);
    }
}