import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Write a description of class MainCharacter here.
 * 
 * @author Andy Feng, Felix Zhao
 * @version 1.0
 * Edited by Jean Pan
 * 
 * This is the main character of the game, the player can control control this character to pass the game
 * The main character will have various actions:
 * <ul>
 * <li> 
 * <ul>'
 */
public abstract class Hero extends SuperSmoothMover
{
    /**
     * Act - do whatever the MainCharacter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    //ArrayList<Power> powerList;
    protected ArrayList<Weapon> weaponInInventory = new ArrayList<Weapon>();
    private int Hp;
    private int shield;
    private double speed;
    private boolean mouseHold;
    protected boolean right;
    protected int energy;
    protected boolean attack;
    private double xMoveVel = 0;
    private double yMoveVel = 0;
    private double xAddedVel = 0;
    private double yAddedVel = 0;
    private double friction = 1;
    private int dashCooldown = 40;
    private int radius;
    //protected Weapon;
    protected Weapon currentWeapon;
    private MouseInfo mouse;
    
    public Hero(int Hp, int shieldValue, int speed, int initialEnergy, Weapon initialWeapon) {
        weaponInInventory.add(initialWeapon);
        this.Hp = Hp;
        this.shield = shieldValue;
        this.speed = speed;
        this.energy = initialEnergy;
        
        attack = false;
        right = true;
        mouseHold = false;
        
        /*GreenfootImage image = new GreenfootImage(50, 50);
        image.setColor(new Color(0, 0, 0));
        image.drawOval(0, 0, 49, 49);
        image.drawOval(30, 16, 18, 18);
        image.drawRect(0, 0, 49, 49);
        setImage(image);*/
        radius = getImage().getHeight()/2;
        
        currentWeapon = initialWeapon;
    }
    
    public void act()
    {
        control();
        takeDamage();

        Weapon weaponOnGround = (Weapon) getOneIntersectingObject(Weapon.class);
        if(weaponInInventory.size() < 2 && weaponOnGround != null) pickUpWeapon(weaponOnGround);
        else if(weaponInInventory.size() >= 2 && weaponOnGround != null) switchWeapon(weaponOnGround);
        
        updateFacingDirection();
    }
    
    private void control() {
        if (xAddedVel > 0) {
            xAddedVel -= friction*5;
            xAddedVel = Math.max(xAddedVel, 0);
        } else if (xAddedVel < 0) {
            xAddedVel += friction*5;
            xAddedVel = Math.min(xAddedVel, 0);
        }
        
        if (yAddedVel > 0) {
            yAddedVel -= friction*5;
            yAddedVel = Math.max(yAddedVel, 0);
        } else if (yAddedVel < 0) {
            yAddedVel += friction*5;
            yAddedVel = Math.min(yAddedVel, 0);
        }
        
        int directionX = 0;
        int directionY = 0;
        if (Greenfoot.isKeyDown("w")) {
            directionY--;
        }
        
        if (Greenfoot.isKeyDown("s")) {
            directionY++;
        }
        
        if (Greenfoot.isKeyDown("a")) {
            directionX--;
        }
        
        if (Greenfoot.isKeyDown("d")) {
            directionX++;
        }
        
        if (directionY != 0) {
            if (directionX != 0) {
                yMoveVel += directionY * speed * friction * Math.sqrt(2) / 2;
                yMoveVel = directionY * Math.min(directionY*yMoveVel, speed*Math.sqrt(2)/2);
            } else {
                yMoveVel += directionY * speed * friction;
                yMoveVel = directionY * Math.min(directionY*yMoveVel, speed);
            }
        } else if (yMoveVel < 0) {
            yMoveVel += speed*friction;
            yMoveVel = Math.min(yMoveVel, 0);
        } else if (yMoveVel > 0) {
            yMoveVel -= speed*friction;
            yMoveVel = Math.max(yMoveVel, 0);
        }
        
        if (directionX != 0) {
            if (directionY != 0) {
                xMoveVel += directionX * speed * friction * Math.sqrt(2) * 2;
                xMoveVel = directionX*Math.min(directionX*xMoveVel, speed*Math.sqrt(2)/2);
            } else {
                xMoveVel += directionX * speed * friction;
                xMoveVel = directionX*Math.min(directionX*xMoveVel, speed);
            }
        } else if (xMoveVel < 0) {
            xMoveVel += speed*friction;
            xMoveVel = Math.min(xMoveVel, 0);
        } else if (xMoveVel > 0) {
            xMoveVel -= speed*friction;
            xMoveVel = Math.max(xMoveVel, 0);
        }
        
        if (Greenfoot.isKeyDown("space") && dashCooldown == 0) {
            
            if (directionX != 0 && directionY == 0) {
                xAddedVel = directionX*30;
                dashCooldown = 30;
            } else if (directionY != 0 && directionX == 0) {
                yAddedVel = directionY*30;
                dashCooldown = 30;
            } else if (directionY != 0 && directionX != 0) {
                xAddedVel = directionX*30*Math.sqrt(2)/2;
                yAddedVel = directionY*30*Math.sqrt(2)/2;
                dashCooldown = 30;
            }
            
        } else if (dashCooldown > 0) {
            dashCooldown--;
        }
        // put inside later
        
        double totalMovement = Math.pow(xMoveVel+xAddedVel, 2) + Math.pow(yMoveVel+yAddedVel, 2);
        
        
        //System.out.println(xRatio);
        int times = (int)(totalMovement / 400)+1;
        double xDistance = (xMoveVel+xAddedVel)/times;
        double yDistance = (yMoveVel+yAddedVel)/times;
        for (int i = 0; i < times; i++) {
            setLocation(getPreciseX()+xDistance, getPreciseY());
            for (Wall wall : getIntersectingObjects(Wall.class)) {
                double left = wall.getX()-wall.getImage().getWidth()/2-getPreciseX();
                double right = wall.getX()+wall.getImage().getWidth()/2-getPreciseX();
                double top = wall.getY()-wall.getImage().getHeight()/2-getPreciseY();
                double bottom = wall.getY()+wall.getImage().getHeight()/2-getPreciseY();
                
                double edgeX = 0;
                double edgeY = 0;
                
                
                if (0 <left) {
                    edgeX = left;
                } else if (0 > right) {
                    edgeX = right;
                }
                
                if (0 < top) {
                    edgeY = top;
                } else if (0 > bottom) {
                    edgeY = bottom;
                }

                if (edgeX*edgeX + edgeY*edgeY <= radius*radius) {
                    if (top <= 0 && 0 <= bottom) {
                        if (Math.abs(left) < Math.abs(right)) {
                            setLocation(getPreciseX()+left-1-radius, getPreciseY());
                        } else {
                            setLocation(getPreciseX()+right+1+radius, getPreciseY());
                        }
                        xMoveVel = 0;
                        xAddedVel = 0;
                        break;
                    } else {
                        double yDiff = getPreciseY()-wall.getY();
                        double xDiff = getPreciseX()-wall.getX();
                        double halfLength = wall.getImage().getWidth()/2.0;
                        double ratioXY = Math.abs(xDiff) / Math.abs(yDiff);
                        double squareX = 0;
                        double squareY = 0;
                        
                        if (Math.abs(xDiff) >= Math.abs(yDiff)) {
                            squareX = halfLength * Math.signum(xDiff); // half length of sqaure
                            squareY = halfLength / ratioXY * Math.signum(yDiff);
                        } else {
                            squareY = halfLength * Math.signum(yDiff);
                            squareX = halfLength * ratioXY * Math.signum(xDiff);
                        }
                        
                        ratioXY = Math.abs(xDiff) / (Math.abs(yDiff) + Math.abs(xDiff));
                        // do radius of circle instead 25
                        double multiplier = radius / Math.sqrt((Math.pow(ratioXY, 2) + Math.pow(1-ratioXY, 2)));
                        double radiusX = Math.signum(xDiff)*multiplier*ratioXY + Math.signum(xDiff);
                        double radiusY = Math.signum(yDiff)*multiplier*(1-ratioXY) + Math.signum(yDiff);
                        System.out.println("Stuff: " + xDiff + " " + yDiff + " " + ratioXY);
                        System.out.println("Stuff2: " + squareX + " " + squareY);
                        System.out.println("Stuff3: " + radiusX + " " + radiusY + " " + multiplier);
                        System.out.println("Coords Wall: " + wall.getX() + " " + wall.getY());
                        setLocation(wall.getX()+squareX+radiusX, wall.getY()+radiusY+squareY);
                        System.out.println("Coords 2: " + getX() + " " + getY());
                    }
                    
                    //System.out.println(left + " " + right + " " + top + " " + bottom);
                    
                }
            }
            
            setLocation(getPreciseX(), getPreciseY()+yDistance);
            for (Wall wall : getIntersectingObjects(Wall.class)) {
                double left = wall.getX()-wall.getImage().getWidth()/2-getPreciseX();
                double right = wall.getX()+wall.getImage().getWidth()/2-getPreciseX();
                double top = wall.getY()-wall.getImage().getHeight()/2-getPreciseY();
                double bottom = wall.getY()+wall.getImage().getHeight()/2-getPreciseY();
                
                double edgeX = 0;
                double edgeY = 0;
                
                if (0 <left) {
                    edgeX = left;
                } else if (0 > right) {
                    edgeX = right;
                }
                
                if (0 < top) {
                    edgeY = top;
                } else if (0 > bottom) {
                    edgeY = bottom;
                }

                if (edgeX*edgeX + edgeY*edgeY <= radius*radius) {
                    if (left <= 0 && 0 <= right) {
                        if (Math.abs(top) < Math.abs(bottom)) {
                            setLocation(getPreciseX(), getPreciseY()+top-1-radius);
                        } else {
                            setLocation(getPreciseX(), getPreciseY()+bottom+1+radius);
                        }
                        yMoveVel = 0;
                        yAddedVel = 0;
                        break;
                    } else {
                        double yDiff = getPreciseY()-wall.getY();
                        double xDiff = getPreciseX()-wall.getX();
                        double halfLength = wall.getImage().getWidth()/2.0;
                        
                        double ratioXY = Math.abs(xDiff) / Math.abs(yDiff);
                        double squareX = 0;
                        double squareY = 0;
                        if (Math.abs(xDiff) >= Math.abs(yDiff)) {
                            squareX = halfLength * Math.signum(xDiff); // half length of sqaure
                            squareY = halfLength / ratioXY * Math.signum(yDiff);
                        } else {
                            squareY = halfLength * Math.signum(yDiff);
                            squareX = halfLength * ratioXY * Math.signum(xDiff);
                        }
                        
                        ratioXY = Math.abs(xDiff) / (Math.abs(yDiff) + Math.abs(xDiff));
                        // do radius of circle instead 25
                        double multiplier = radius / Math.sqrt((Math.pow(ratioXY, 2) + Math.pow(1-ratioXY, 2)));
                        double radiusX = Math.signum(xDiff)*multiplier*ratioXY + Math.signum(xDiff);
                        double radiusY = Math.signum(yDiff)*multiplier*(1-ratioXY) + Math.signum(yDiff);
                        
                        setLocation(wall.getX()+squareX+radiusX, wall.getY()+radiusY+squareY);
                    }
                    
                }
            }
        }
        
        
    }
    
    private void takeDamage() {
        // implement later
        
        if(Hp <= 0){
            //game over
        }
    }
    
    private void pickUpWeapon(Weapon newWeapon) {
        //pick up a new weapon if the Hero only has 1 weapon
        weaponInInventory.add(newWeapon);
    }
    
    private boolean switchWeapon(Weapon newWeapon) {
        //switch the current weapon the hero is using with another weapon
        weaponInInventory.remove(currentWeapon);
        weaponInInventory.add(newWeapon);
        currentWeapon = newWeapon;
        return true;
    }
    
    public abstract void ability();
    
    public abstract void animation();

    private boolean updateMouseHold() {
        GameWorld world = (GameWorld) getWorld();
        if(mouseHold && (world.hasMouseDragEnded(null) || world.isMouseClicked(null))) mouseHold = false;
        if(!mouseHold && world.isMousePressed(null)) mouseHold = true;
        return mouseHold;
    }
    
    private void updateFacingDirection(){
        GameWorld world = (GameWorld) getWorld();
        if (updateMouseHold()) {
            if(world.getMouseX() < getX()) right = false;
            else right = true;
        } else {
            if(Greenfoot.isKeyDown("a")) right = false;
            else right = true;
        }
    }
    
    public void resetXVelocity() {
        xMoveVel = 0;
        xAddedVel = 0;
    }
    
    public void resetYVelocity() {
        yMoveVel = 0;
        yAddedVel = 0;
    }
}
