import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Write a description of class MainCharacter here.
 * 
 * @author Andy Feng, Felix Zhao
 * @version 1.0
 * Edited by Jean Pan, Edwin Dong
 * 
 * This is the main character of the game, the player can control control this character to pass the game
 * The main character will have various actions:
 * <ul>
 * <li> moving around
 * <li> attack, pickup and switch weapon 
 * <ul>
 */
public abstract class Hero extends SuperSmoothMover
{
    /**
     * Act - do whatever the MainCharacter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    //ArrayList<Power> powerList;
    protected ArrayList<Weapon> weaponsInInventory = new ArrayList<Weapon>();
    protected int hp;
    protected int maxHP;
    protected int shield;
    protected int maxShield;
    protected double speed;
    protected int gold;
    private boolean mouseHold;
    protected boolean right;
    protected int energy;
    protected boolean attack;
    protected boolean isInvincible;
    protected long invincibleStart;
    protected int invincibleDuration;
    protected boolean isTransparent;
    protected int lastHitCounter = 0;
    protected int timeForShieldToHeal;
    
    protected boolean canMove = true;
    protected double xMoveVel = 0;
    protected double yMoveVel = 0;
    protected double xAddedVel = 0;
    protected double yAddedVel = 0;
    protected double friction = 1;
    protected int dashCooldown = 40;
    // will always be some subclass of actor
    protected ArrayList<Class<?>> hitboxList;
    
    protected int radius;
    protected Weapon currentWeapon;
    protected int weaponActionCooldown = 0;
    
    protected SuperStatBar hpBar;
    protected Label hpNumber;
    protected SuperStatBar shieldBar;
    protected Label shieldNumber;
    protected SuperStatBar energyBar;
    protected Label goldLabel;
    protected Image goldCoin;
    protected Image weaponLabelOne;
    protected Image weaponLabelTwo;
    protected Image weaponOne;
    protected Image weaponTwo;
    
    private GreenfootSound damageSound;
    private static GreenfootSound[] damageSounds;
    private static int damageSoundsIndex;
    protected SimpleHitbox hitbox;
    protected Overlay overlay;

    public Hero(int hp, int shieldValue, int speed, int initialEnergy, Weapon initialWeapon) {
        weaponsInInventory.add(initialWeapon);
        
        weaponOne = new Image(weaponsInInventory.get(0).getImage());
        if (weaponOne.getImage().getWidth() > weaponOne.getImage().getWidth()) {
            double multiplier = 60.0 / weaponOne.getImage().getWidth();
            weaponOne.getImage().scale((int)(multiplier * weaponOne.getImage().getWidth()), (int)(multiplier * weaponOne.getImage().getHeight()));
        } else {
            double multiplier = 60.0 / weaponOne.getImage().getHeight();
            weaponOne.getImage().scale((int)(multiplier * weaponOne.getImage().getWidth()), (int)(multiplier * weaponOne.getImage().getHeight()));
        }
        weaponTwo = null;
        
        currentWeapon = initialWeapon;
        currentWeapon.beingUsed = true;
        this.hp = hp;
        maxHP = hp;
        this.shield = shieldValue;
        maxShield = shieldValue;
        this.speed = speed;
        this.energy = initialEnergy;

        attack = false;
        right = true;
        mouseHold = false;
        isInvincible = false;
        invincibleDuration = 1000;
        
        radius = 16;
        hpBar = new SuperStatBar(hp, hp, null, 105, 18, 0, Color.RED, Color.BLACK);
        hpNumber = new Label(hp + "/" + hp, 24);
        shieldBar = new SuperStatBar(shield, shield, null, 105, 18, 0, Color.GRAY, Color.BLACK);
        shieldNumber = new Label(shield + "/" + shield, 24);
        gold = 10;
        goldLabel = new Label("10", 32);
        goldCoin = new Image("coins/coin0.png");
        goldCoin.getImage().scale(32, 32);
        weaponLabelOne = new Image("weaponslot.png");
        weaponLabelTwo = new Image("weaponslot.png");
        
        hitboxList = new ArrayList<Class<?>>();
        hitboxList.add(Wall.class);
        hitboxList.add(Chest.class);
        
        damageSoundPlayer();
    }
    
    public void addedToWorld(World world) {
        world.addObject(currentWeapon, getX(), getY());
        world.addObject(hpBar, 118, 38);
        world.addObject(hpNumber, 118, 38);
        world.addObject(shieldBar, 118, 69);
        world.addObject(shieldNumber, 118, 69);
        world.addObject(goldLabel, 88, 160);
        world.addObject(goldCoin, 36, 160);
        // world.addObject(weaponLabelOne, 64, 210);
        // world.addObject(weaponLabelTwo, 144, 210);
        world.addObject(weaponLabelOne, 56, 230);
        world.addObject(weaponLabelTwo, 152, 230);
        world.addObject(weaponOne, 56, 230);
    }
    
    public void act() {
        control();
        if (isInvincible) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - invincibleStart > invincibleDuration) {
                isInvincible = false;
            }
        }
        if (lastHitCounter >= 300 && shield < maxShield) {
            if (lastHitCounter % 60 == 0) {
                shield++;
                shieldBar.update(shield);
                shieldNumber.setValue(shield + "/" + maxShield);
            }
        }
        lastHitCounter++;
        renderHero();
        handleWeaponActions();
        switchWeaponInInventory();
        updateFacingDirection();
        updateWeaponPosition();
        
        tileInteraction();
        
        if (weaponActionCooldown > 0) {
            weaponActionCooldown--;
        }
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
            for (Class cls : hitboxList) {
                for (Actor wall : (ArrayList<Actor>)getIntersectingObjects(cls)) {
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
                            // System.out.println("Stuff: " + xDiff + " " + yDiff + " " + ratioXY);
                            // System.out.println("Stuff2: " + squareX + " " + squareY);
                            // System.out.println("Stuff3: " + radiusX + " " + radiusY + " " + multiplier);
                            // System.out.println("Coords Wall: " + wall.getX() + " " + wall.getY());
                            setLocation(wall.getX()+squareX+radiusX, wall.getY()+radiusY+squareY);
                            // System.out.println("Coords 2: " + getX() + " " + getY());
                        }
                        
                        //System.out.println(left + " " + right + " " + top + " " + bottom);
                        
                    }
                }
            }
            
            setLocation(getPreciseX(), getPreciseY()+yDistance);
            for (Class cls : hitboxList) {
                for (Actor wall : (ArrayList<Actor>)getIntersectingObjects(cls)) {
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
        
        ArrayList<RoomExit> exits = (ArrayList<RoomExit>)getIntersectingObjects(RoomExit.class);
        if (exits.size() > 0) {
            String direction = exits.get(0).activate();
            if (direction != null) {
                if (direction.equals("up")) {
                    setLocation(getPreciseX(), 600);
                } else if (direction.equals("down")) {
                    setLocation(getPreciseX(), 110);
                } else if (direction.equals("left")) {
                    setLocation(1090, getPreciseY());
                } else if (direction.equals("right")) {
                    setLocation(352, getPreciseY());
                }
            }
        }
    }
    
    public void takeDamage(int damage) {
        // implement later
        if (!isInvincible) {
            if (shield > 0) {
                shield -= damage;
                shield = Math.max(shield, 0);
                shieldBar.update(shield);
                shieldNumber.setValue(shield + "/" + maxShield);
            } else {
                hp -= damage;
                hpBar.update(hp);
                hpNumber.setValue(hp + "/" + maxHP);
            }
            //playDamageSound();
            lastHitCounter = 0;
            isInvincible = true;
            invincibleStart = System.currentTimeMillis();
        }
        
        if(hp <= 0){
            //game over
            Greenfoot.setWorld(new StartWorld());
            getWorld().removeObject(this);
            return;
        }
        
    }
    
    public void getGold(int amount) {
        gold += amount;
        goldLabel.setValue(gold);
    }
    
    public void renderHero() {
        if (isInvincible) {
            getImage().setTransparency(128); // 50% transparency
        } else {
            getImage().setTransparency(255); // 100% visibility
        }
    }

    
    public abstract void ability();
    
    public abstract void animation();
    
    private void updateFacingDirection() {
        if (GameWorld.isMouseHolding()) {
            right = GameWorld.getMouseX() >= getX();
        } else {
            if (Greenfoot.isKeyDown("a")) right = false;
            if (Greenfoot.isKeyDown("d")) right = true;
        }
    }

    private void updateWeaponPosition() {
        if(currentWeapon != null) {
            int offsetX;
            int offsetY;

            if(right) {
                offsetX = currentWeapon instanceof Sword ? 15 : 5; // Adjusted offset
                offsetY = currentWeapon instanceof Sword ? 10 : 5;
                currentWeapon.setRotation(0);
            } else {
                offsetX = currentWeapon instanceof Sword ? -15 : -5; // Adjusted offset
                offsetY = currentWeapon instanceof Sword ? 10 : 5;
                currentWeapon.setRotation(0);
            }
            currentWeapon.setLocation(getX() + offsetX, getY() + offsetY);

            if (!attack) {
                if(currentWeapon instanceof Sword) {
                    currentWeapon.setImage(right ? ((Sword) currentWeapon).swordRightFrames[0] : ((Sword) currentWeapon).swordLeftFrames[0]);
                }
                if(currentWeapon instanceof Bow) {
                    currentWeapon.setImage(right ? ((Bow) currentWeapon).bowRightFrames[0] : ((Bow) currentWeapon).bowLeftFrames[0]);
                }
            }
        }

        for (Weapon weapon : weaponsInInventory) {
            if (weapon != currentWeapon) {
                weapon.setLocation(getX() + (right ? -10 : 10), getY());
                weapon.beingUsed = false;
            }
        }
    }


    private void switchWeaponInInventory() {
        if (weaponActionCooldown == 0) {
            try {
                if(Greenfoot.isKeyDown("1")){
                    currentWeapon = weaponsInInventory.get(0);
                    currentWeapon.beingUsed = true;
                    weaponActionCooldown = 5; // Adding cooldown to avoid multiple detections
                }
                if(Greenfoot.isKeyDown("2")){
                    currentWeapon = weaponsInInventory.get(1);
                    currentWeapon.beingUsed = true;
                    weaponActionCooldown = 5; // Adding cooldown to avoid multiple detections
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("No weapon there.");
            }
        }
    }

    private void dropWeapon() {
        currentWeapon.beingUsed = false;
        getWorld().addObject(currentWeapon, getX(), getY());
    }

    private void pickUpAndSwitchCurrentWeapon(Weapon weaponOnGround) {
        int currentWeaponIndex = weaponsInInventory.indexOf(currentWeapon);
        dropWeapon();
        weaponsInInventory.set(currentWeaponIndex, weaponOnGround);
        currentWeapon = weaponOnGround;
        currentWeapon.beingUsed = true;
    }

    private void handleWeaponActions() {
        ArrayList<Weapon> weaponsOnGround = (ArrayList<Weapon>) getIntersectingObjects(Weapon.class);
        for(Weapon weaponOnGround : weaponsOnGround) {
            if(weaponsInInventory.indexOf(weaponOnGround) == -1) {
                handleWeaponPickup(weaponOnGround);
            }
        }
    }

    private void handleWeaponPickup(Weapon weaponOnGround) {
        if (weaponActionCooldown == 0) {
            boolean pickedUp = false;
            if (weaponsInInventory.size() < 2 && Greenfoot.isKeyDown("e")) {
                System.out.println("Weapon picked up");
                weaponsInInventory.add(weaponOnGround);
                weaponActionCooldown = 20; // Adding cooldown to avoid multiple detections
                pickedUp = true;
            } else if (weaponsInInventory.size() >= 2 && Greenfoot.isKeyDown("e")) {
                System.out.println("Weapon switched");
                pickUpAndSwitchCurrentWeapon(weaponOnGround);
                weaponActionCooldown = 20; // Adding cooldown to avoid multiple detections
                pickedUp = true;
            }
            if (!pickedUp) {
                return;
            }
            getWorld().removeObject(weaponOne);
            getWorld().removeObject(weaponTwo);
            weaponOne = new Image(weaponsInInventory.get(0).getImage());
            if (weaponOne.getImage().getWidth() > weaponOne.getImage().getWidth()) {
                double multiplier = 60.0 / weaponOne.getImage().getWidth();
                weaponOne.getImage().scale((int)(multiplier * weaponOne.getImage().getWidth()), (int)(multiplier * weaponOne.getImage().getHeight()));
            } else {
                double multiplier = 60.0 / weaponOne.getImage().getHeight();
                weaponOne.getImage().scale((int)(multiplier * weaponOne.getImage().getWidth()), (int)(multiplier * weaponOne.getImage().getHeight()));
            }
            if (weaponsInInventory.size() < 2) {
                return;
            }
            weaponTwo = new Image(weaponsInInventory.get(1).getImage());
            if (weaponTwo.getImage().getWidth() > weaponTwo.getImage().getWidth()) {
                double multiplier = 60.0 / weaponTwo.getImage().getWidth();
                weaponTwo.getImage().scale((int)(multiplier * weaponTwo.getImage().getWidth()), (int)(multiplier * weaponTwo.getImage().getHeight()));
            } else {
                double multiplier = 60.0 / weaponTwo.getImage().getHeight();
                weaponTwo.getImage().scale((int)(multiplier * weaponTwo.getImage().getWidth()), (int)(multiplier * weaponTwo.getImage().getHeight()));
            }
            getWorld().addObject(weaponOne, weaponLabelOne.getX(), weaponLabelOne.getY());
            getWorld().addObject(weaponTwo, weaponLabelTwo.getX(), weaponLabelTwo.getY());
        }
    }
    
    private void tileInteraction() {
        Portal portal = (Portal)getOneIntersectingObject(Portal.class);
        if (portal != null) {
            if (Greenfoot.isKeyDown("e")) {
                getWorldOfType(GameWorld.class).nextMap(this);
            }
        }
        Chest chest = (Chest)getOneIntersectingObject(Chest.class);
        if (chest != null) {
            if (Greenfoot.isKeyDown("e")) {
                chest.interact(this);
            }
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
    
    public void playDamageSound(){
        damageSounds[damageSoundsIndex].setVolume(80);
        damageSounds[damageSoundsIndex].play();
        damageSoundsIndex++; 
        if (damageSoundsIndex >= damageSounds.length){
            damageSoundsIndex = 0;
        }
    }
    public static void damageSoundPlayer(){
        damageSoundsIndex = 0;
        damageSounds = new GreenfootSound[20]; 
        for (int i = 0; i < damageSounds.length; i++){
            damageSounds[i] = new GreenfootSound("");
        }   

    }
    
    
    /**
     * Adds to the hitboxList
     *
     * @param cls The class to add (Has to be a sublcass of Actor)
     */
    public void addHitboxList(Class<?> cls) {
        if (!(Actor.class).isAssignableFrom(cls)) return;
        hitboxList.add(cls);
    }
    
    /**
     * Remove from hitbox list
     *
     * @param cls The class to remove
     */
    public void removeHitboxList(Class<?> cls) {
        hitboxList.remove(cls);
    }
}
