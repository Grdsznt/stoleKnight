import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.HashSet;
/**
 * <p>
 * This is the superclass of the actors the player takes contorl of. The player can attack, move and also dash
 * </p>
 * 
 * Edited by Jean Pan, Edwin Dong
 * 
 * @author Andy Feng, Felix Zhao
 * @version 1.0
 * 
 * 
 */
public abstract class Hero extends SuperSmoothMover
{
    /**
     * Act - do whatever the MainCharacter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    protected HashSet<String> powerList = new HashSet<String>();
    protected ArrayList<Weapon> weaponsInInventory = new ArrayList<Weapon>();
    protected int hp;
    protected int maxHP;
    protected int shield;
    protected int maxShield;
    protected int shieldRecoverTime;
    protected double speed;
    protected int gold;
    private boolean mouseHold;
    protected boolean right;
    protected int energy;
    protected int maxEnergy;
    protected int slot1, slot2;
    protected boolean attack;
    protected boolean isInvincible;
    protected long invincibleStart;
    protected int invincibleDuration;
    protected boolean isTransparent;
    protected int lastHitCounter = 0;
    
    
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
    protected Label energyNumber;
    
    protected Label goldLabel;
    protected Image goldCoin;
    protected Image weaponLabelOne;
    protected Image weaponLabelTwo;
    protected Image weaponOne;
    protected Image weaponTwo;
    protected Image energyCostOne;
    protected Image energyCostTwo;
    
    private static GreenfootSound[] damageSounds;
    private static int damageSoundsIndex;
    protected SimpleHitbox hitbox;
    protected Overlay overlay;

    public Hero(int hp, int shieldValue, int speed, int initialEnergy, int slot1, int slot2) {
        
        this.slot1 = slot1;
        this.slot2 = slot2;
        if (slot2 != 3) {
            if (slot1 == 0) {
                if (slot2 == 0) {
                    weaponsInInventory.add(new Sword());
                    weaponsInInventory.add(new Sword());
                } else if (slot2 == 1) {
                    weaponsInInventory.add(new Sword());
                    weaponsInInventory.add(new Bow());
                } else {
                    weaponsInInventory.add(new Sword());
                    weaponsInInventory.add(new Wand(5));
                }
            } else if (slot1 == 1) {
                if (slot2 == 0) {
                    weaponsInInventory.add(new Bow());
                    weaponsInInventory.add(new Sword());
                } else if (slot2 == 1) {
                    weaponsInInventory.add(new Bow());
                    weaponsInInventory.add(new Bow());
                } else {
                    weaponsInInventory.add(new Bow());
                    weaponsInInventory.add(new Wand(5));
                }
            } else {
                if (slot2 == 0) {
                    weaponsInInventory.add(new Wand(5));
                    weaponsInInventory.add(new Sword());
                } else if (slot2 == 1) {
                    weaponsInInventory.add(new Wand(5));
                    weaponsInInventory.add(new Bow());
                } else {
                    weaponsInInventory.add(new Wand(5));
                    weaponsInInventory.add(new Wand(5));
                }
            }
        } else {
            if (slot1 == 0) {
                weaponsInInventory.add(new Sword());
            } else if (slot1 == 1) {
                weaponsInInventory.add(new Bow());
            } else {
                weaponsInInventory.add(new Wand(5));
            }
        }
        if (weaponsInInventory.size() > 1) {
            weaponOne = new Image(weaponsInInventory.get(0).getImage());
            weaponTwo = new Image(weaponsInInventory.get(1).getImage());
            if (weaponOne.getImage().getWidth() > weaponOne.getImage().getWidth()) {
                double multiplier = 48.0 / weaponOne.getImage().getWidth();
                weaponOne.getImage().scale((int)(multiplier * weaponOne.getImage().getWidth()), (int)(multiplier * weaponOne.getImage().getHeight()));
            } else {
                double multiplier = 48.0 / weaponOne.getImage().getHeight();
                weaponOne.getImage().scale((int)(multiplier * weaponOne.getImage().getWidth()), (int)(multiplier * weaponOne.getImage().getHeight()));
            }
            if (weaponTwo.getImage().getWidth() > weaponTwo.getImage().getWidth()) {
                double multiplier = 48.0 / weaponTwo.getImage().getWidth();
                weaponTwo.getImage().scale((int)(multiplier * weaponTwo.getImage().getWidth()), (int)(multiplier * weaponTwo.getImage().getHeight()));
            } else {
                double multiplier = 48.0 / weaponTwo.getImage().getHeight();
                weaponTwo.getImage().scale((int)(multiplier * weaponTwo.getImage().getWidth()), (int)(multiplier * weaponTwo.getImage().getHeight()));
            }
            energyCostOne = new Image(10, 10);
            energyCostOne.setImage(new GreenfootImage(" " + weaponsInInventory.get(0).getEnergyUsage()+" ", 20, Color.WHITE, new Color(135, 206, 235)));
            energyCostTwo = new Image(10, 10);
            energyCostTwo.setImage(new GreenfootImage(" " + weaponsInInventory.get(1).getEnergyUsage()+" ", 20, Color.WHITE, new Color(135, 206, 235)));
        } else {
            weaponOne = new Image(weaponsInInventory.get(0).getImage());
            if (weaponOne.getImage().getWidth() > weaponOne.getImage().getWidth()) {
                double multiplier = 48.0 / weaponOne.getImage().getWidth();
                weaponOne.getImage().scale((int)(multiplier * weaponOne.getImage().getWidth()), (int)(multiplier * weaponOne.getImage().getHeight()));
            } else {
                double multiplier = 48.0 / weaponOne.getImage().getHeight();
                weaponOne.getImage().scale((int)(multiplier * weaponOne.getImage().getWidth()), (int)(multiplier * weaponOne.getImage().getHeight()));
            }
            energyCostOne = new Image(10, 10);
            energyCostOne.setImage(new GreenfootImage(" " + weaponsInInventory.get(0).getEnergyUsage()+" ", 20, Color.WHITE, new Color(135, 206, 235)));
            energyCostTwo = new Image(10, 10);
            weaponTwo = null;
        }
        
        
        
        currentWeapon = weaponsInInventory.get(0);
        currentWeapon.beingUsed = true;
        this.hp = hp;
        maxHP = hp;
        this.shield = shieldValue;
        maxShield = shieldValue;
        this.speed = speed;
        this.energy = initialEnergy;
        maxEnergy = 100;
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
        energyBar = new SuperStatBar(maxEnergy, energy, null, 105, 18, 0, new Color(135, 206, 235), Color.BLACK);
        energyNumber = new Label(energy + "/" + maxEnergy, 24);
        gold = 10;
        goldLabel = new Label("10", 32);
        goldCoin = new Image("coins/coin0.png");
        goldCoin.getImage().scale(32, 32);
        weaponLabelOne = new Image("weaponslot.png");
        weaponLabelTwo = new Image("weaponslot.png");
        
        
        hitboxList = new ArrayList<Class<?>>();
        hitboxList.add(Wall.class);
        hitboxList.add(Void.class);
        hitboxList.add(Chest.class);
        shieldRecoverTime = 300;
        
    }
    
    public void addedToWorld(World world) {
        for (Weapon w : weaponsInInventory) {
            world.addObject(w, getX(), getY());
            
        }

        world.addObject(hpBar, 118, 38);
        world.addObject(hpNumber, 118, 38);
        world.addObject(shieldBar, 118, 69);
        world.addObject(shieldNumber, 118, 69);
        world.addObject(energyBar, 118, 100);
        world.addObject(energyNumber, 118, 100);
        world.addObject(goldLabel, 88, 160);
        world.addObject(goldCoin, 36, 160);
        // world.addObject(weaponLabelOne, 64, 210);
        // world.addObject(weaponLabelTwo, 144, 210);
        world.addObject(weaponLabelOne, 56, 230);
        world.addObject(energyCostOne, 56, 270);
        world.addObject(weaponLabelTwo, 152, 230);
        world.addObject(energyCostTwo, 152, 270);
        if (weaponsInInventory.size() > 1) {
            world.addObject(weaponOne, 56, 230);
            world.addObject(weaponTwo, 152, 230);
        } else {
            world.addObject(weaponOne, 56, 230);
        }
        
        world.addObject(new Label("Buffs", 32), 56, 300);
    }
    
    public void act() {
        control();
        if (isInvincible) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - invincibleStart > invincibleDuration) {
                isInvincible = false;
            }
        }
        if (lastHitCounter >= shieldRecoverTime && shield < maxShield) {
            if (lastHitCounter % 60 == 0) {
                shield++;
                shieldBar.update(shield);
                shieldNumber.setValue(shield + "/" + maxShield);
            }
        }
        if (powerList.contains("Energy Regen")) {
            
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
                    setLocation(342, getPreciseY());
                }
            }
        }
    }
    
    /**
     * Causes the player to take damage
     *
     * @param damage The damage to take
     */
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
            playDamageSound();
            lastHitCounter = 0;
            isInvincible = true;
            invincibleStart = System.currentTimeMillis();
        }
        
        if(hp <= 0){
            //game over
            GameWorld gw = (GameWorld) getWorld();
            if (weaponsInInventory.size() == 1) {
                Weapon w = weaponsInInventory.get(0);
                if (w instanceof Sword) {
                    Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 0, 3)); // 3 means no weapon
                } else if (w instanceof Bow) {
                    Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 1, 3));
                } else {
                    Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 2, 3));
                }
            } else {
                Weapon w = weaponsInInventory.get(0), w2 = weaponsInInventory.get(1);
                if (w instanceof Sword) {
                    if (w2 instanceof Sword) {
                        Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 0, 0));
                    } else if (w2 instanceof Bow) {
                        Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 0, 1));
                    } else {
                        Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 0, 2));
                    }
                    
                } else if (w instanceof Bow) {
                    if (w2 instanceof Sword) {
                        Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 1, 0));
                    } else if (w2 instanceof Bow) {
                        Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 1, 1));
                    } else {
                        Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 1, 2));
                    }
                } else {
                    if (w2 instanceof Sword) {
                        Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 2, 0));
                    } else if (w2 instanceof Bow) {
                        Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 2, 1));
                    } else {
                        Greenfoot.setWorld(new EndWorld(0, gw.getFloor(), 10, energy, 2, 2));
                    }
                }
            }
            
            getWorld().removeObject(this);
            return;
        }
    }
    
    /**
     * Gives gold to the player
     *
     * @param amount The gold gained
     */
    public void getGold(int amount) {
        gold += amount;
        if (powerList.contains("Better Loot")) {
            gold += amount;
        }
        goldLabel.setValue(gold);
    }
    
    /**
     * Removes gold from the player
     *
     * @param amount The amount of gold to take away
     */
    public void removeGold(int amount) {
        gold -= amount;
        goldLabel.setValue(gold);
    }
    
    /**
     * Returns how much gold the player has
     */
    public int getAmountOfGold() {
        return gold;
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
    
    /**
     * Updates the facing direction of the hero based on user input or mouse position.
     */
    private void updateFacingDirection() {
        if (GameWorld.isMouseHolding()) {
            // If mouse is being held, determine facing direction based on mouse position
            right = GameWorld.getMouseX() >= getX();
        } else {
            // If keys 'a' or 'd' are pressed, set facing direction accordingly
            if (Greenfoot.isKeyDown("a")) right = false;
            if (Greenfoot.isKeyDown("d")) right = true;
        }
    }
    
    /**
     * Updates the position and orientation of the current weapon.
     * Adjusts offsets based on the type of weapon equipped.
     */
    private void updateWeaponPosition() {
        if(currentWeapon != null) {
            int offsetX;
            int offsetY;
    
            // Determine offsets based on facing direction and weapon type
            if(right) {
                offsetX = currentWeapon instanceof Sword ? 15 : 5; // Adjusted offset for right-facing
                offsetY = currentWeapon instanceof Sword ? 5 : 5;
                currentWeapon.setRotation(0);
            } else {
                offsetX = currentWeapon instanceof Sword ? -10 : -5; // Adjusted offset
                offsetY = currentWeapon instanceof Sword ? 5 : 5;
                currentWeapon.setRotation(0);
            }
    
            // Set weapon location relative to hero
            currentWeapon.setLocation(getX() + offsetX, getY() + offsetY);
    
            // Set weapon image based on facing direction, only if not attacking
            if (!attack) {
                if(currentWeapon instanceof Sword) {
                    currentWeapon.setImage(right ? ((Sword) currentWeapon).swordRightFrames[0] : ((Sword) currentWeapon).swordLeftFrames[0]);
                }
                if(currentWeapon instanceof Bow) {
                    currentWeapon.setImage(right ? ((Bow) currentWeapon).bowRightFrames[0] : ((Bow) currentWeapon).bowLeftFrames[0]);
                }
            }
        }
    
        // Position other weapons in inventory
        for (Weapon weapon : weaponsInInventory) {
            if (weapon != currentWeapon) {
                weapon.setLocation(getX() + (right ? -10 : 10), getY());
                weapon.beingUsed = false;
                if(weapon instanceof Sword) {
                    weapon.setImage(right ? ((Sword) weapon).swordRightFrames[0] : ((Sword) weapon).swordLeftFrames[0]);
                }
            }
        }
    }
    
    /**
     * Switches the current weapon in the hero's inventory based on key inputs.
     * Provides cooldown to avoid rapid switching.
     */
    private void switchWeaponInInventory() {
        if (weaponActionCooldown == 0) {
            try {
                // Switch weapon based on key presses ('1' for first weapon, '2' for second)
                if(Greenfoot.isKeyDown("1")){
                    currentWeapon = weaponsInInventory.get(0);
                    currentWeapon.beingUsed = true;
                    weaponActionCooldown = 5; // Cooldown period to prevent rapid switches
                }
                if(Greenfoot.isKeyDown("2")){
                    currentWeapon = weaponsInInventory.get(1);
                    currentWeapon.beingUsed = true;
                    weaponActionCooldown = 5; // Cooldown period to prevent rapid switches
                }
            } catch (IndexOutOfBoundsException e) {
                // System.out.println("No weapon there.");
            }
        }
    }
    
    /**
     * Drops the current weapon from the hero's inventory onto the game world.
     */
    private void dropWeapon() {
        currentWeapon.beingUsed = false;
        getWorld().addObject(currentWeapon, getX(), getY());
    }
    
    /**
     * Picks up a weapon from the ground and switches it with the current weapon in inventory.
     * Maintains the weapon's state and updates inventory accordingly.
     * @param weaponOnGround The weapon object to be picked up.
     */
    private void pickUpAndSwitchCurrentWeapon(Weapon weaponOnGround) {
        int currentWeaponIndex = weaponsInInventory.indexOf(currentWeapon);
        dropWeapon();
        weaponsInInventory.set(currentWeaponIndex, weaponOnGround);
        currentWeapon = weaponOnGround;
        currentWeapon.beingUsed = true;
    }
    
    /**
     * Handles actions related to weapons, such as picking up new weapons from the ground.
     * Manages cooldowns to prevent rapid actions.
     */
    private void handleWeaponActions() {
        ArrayList<Weapon> weaponsOnGround = (ArrayList<Weapon>) getIntersectingObjects(Weapon.class);
        for(Weapon weaponOnGround : weaponsOnGround) {
            if(weaponsInInventory.indexOf(weaponOnGround) == -1) {
                handleWeaponPickup(weaponOnGround);
            }
        }
    }
    
    /**
     * Handles the specific action of picking up a weapon from the ground.
     * Determines whether to add the weapon to inventory or switch it with the current weapon.
     * @param weaponOnGround The weapon object to be picked up.
     */
    private void handleWeaponPickup(Weapon weaponOnGround) {
        if (weaponActionCooldown == 0) {
            boolean pickedUp = false;
            if (weaponsInInventory.size() < 2 && Greenfoot.isKeyDown("e")) {
                weaponsInInventory.add(weaponOnGround);
                weaponActionCooldown = 20; // Cooldown period to prevent rapid pickups
                pickedUp = true;
            } else if (weaponsInInventory.size() >= 2 && Greenfoot.isKeyDown("e")) {
                pickUpAndSwitchCurrentWeapon(weaponOnGround);
                weaponActionCooldown = 20; // Cooldown period to prevent rapid pickups
                pickedUp = true;
            }
            if (!pickedUp) {
                return;
            }
    
            // Update UI representation of inventory weapons
            // Assuming weaponOne and weaponTwo are UI elements representing inventory weapons
            // Update their images and positions based on the new inventory state
            updateWeaponUIRepresentation();
        }
    }
    
    /**
     * Interacts with specific tiles on the map, such as portals or chests.
     * Executes actions based on user input.
     */
    private void tileInteraction() {
        Portal portal = (Portal)getOneIntersectingObject(Portal.class);
        if (portal != null) {
            if (Greenfoot.isKeyDown("e")) {
                // Interact with portal (e.g., transition to next map)
                getWorldOfType(GameWorld.class).nextMap(this);
            }
        }
        Chest chest = (Chest)getOneIntersectingObject(Chest.class);
        if (chest != null) {
            if (Greenfoot.isKeyDown("e")) {
                // Interact with chest (e.g., open chest and obtain rewards)
                chest.interact(this);
            }
        }
        Shop shop = (Shop)getOneIntersectingObject(Shop.class);
        if (shop != null) {
            if (Greenfoot.isKeyDown("e")) {
                shop.interact(this);
            }
        }
    }
    
    /**
     * Updates the UI representation of the hero's weapons in the game world.
     * Assumes existence of UI elements weaponOne and weaponTwo.
     */
    private void updateWeaponUIRepresentation() {
        // Remove existing UI representations
        getWorld().removeObject(weaponOne);
        getWorld().removeObject(weaponTwo);
        
    
        // Update UI representation for weapon one
        weaponOne = new Image(weaponsInInventory.get(0).getImage());
        scaleImageToFit(weaponOne);
        energyCostOne.setImage(new GreenfootImage(" " + weaponsInInventory.get(0).getEnergyUsage()+" ", 20, Color.WHITE, new Color(135, 206, 235)));
        // Add weapon one to the world at appropriate location
        getWorld().addObject(weaponOne, weaponLabelOne.getX(), weaponLabelOne.getY());
        
        // If only one weapon in inventory, return as no need to update UI for second weapon
        if (weaponsInInventory.size() < 2) {
            return;
        }
    
        // Update UI representation for weapon two
        weaponTwo = new Image(weaponsInInventory.get(1).getImage());
        scaleImageToFit(weaponTwo);
        energyCostTwo.setImage(new GreenfootImage(" " + weaponsInInventory.get(1).getEnergyUsage()+" ", 20, Color.WHITE, new Color(135, 206, 235)));
        // Add weapon two to the world at appropriate location
        getWorld().addObject(weaponTwo, weaponLabelTwo.getX(), weaponLabelTwo.getY());
    }
    
    /**
     * Scales the given image to fit within a fixed size (60x60 pixels).
     * @param image The image to be scaled.
     */
    private void scaleImageToFit(Image image) {
        if (image.getImage().getWidth() > image.getImage().getWidth()) {
            double multiplier = 48.0 / image.getImage().getWidth();
            image.getImage().scale((int)(multiplier * image.getImage().getWidth()), (int)(multiplier * image.getImage().getHeight()));
        } else {
            double multiplier = 48.0 / image.getImage().getHeight();
            image.getImage().scale((int)(multiplier * image.getImage().getWidth()), (int)(multiplier * image.getImage().getHeight()));
        }
    }
    /**
     * Reset the X velocity
     */
    public void resetXVelocity() {
        xMoveVel = 0;
        xAddedVel = 0;
    }
    
    
    /**
     * Reset the Y Velocity
     *
     */
    public void resetYVelocity() {
        yMoveVel = 0;
        yAddedVel = 0;
    }
    
    /**
     * Plays the damage sound
     *
     */
    public void playDamageSound(){
        damageSounds[damageSoundsIndex].setVolume(70);
        damageSounds[damageSoundsIndex].play();
        damageSoundsIndex++; 
        if (damageSoundsIndex >= damageSounds.length){
            damageSoundsIndex = 0;
        }
    }
    
    /**
     * Loads the sounds
     *
     */
    public static void damageSoundPlayer(){
        damageSoundsIndex = 0;
        damageSounds = new GreenfootSound[5]; 
        for (int i = 0; i < damageSounds.length; i++){
            damageSounds[i] = new GreenfootSound("heroTakeDamage.mp3");
            damageSounds[i].play();
            Greenfoot.delay(1);
            damageSounds[i].stop();
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
    
    
    /**
     * Adds a power to the player
     *
     * @param power The power
     */
    public void addPower(String power) {
        Image buffImage = new Image("buffs/"+power+".png");
        int powerSize = powerList.size();
        buffImage.getImage().scale(48, 48);
        getWorld().addObject(buffImage, 36 + (powerSize % 3) * 64, 350 + (powerSize/3)*64);
        powerList.add(power);
        // Some power ups are handled in different spots
        switch (power) {
            case "Extra HP":
                maxHP += 2;
                hp += 2;
                hpBar.setMaxVal(maxHP);
                hpBar.update(hp);
                hpNumber.setValue(hp + "/" + maxHP);
                break;
            case "Swiftness":
                speed += 2;
                break;
            case "More Shield":
                shield+=2;
                maxShield+=2;
                shieldBar.setMaxVal(maxHP);
                shieldBar.update(hp);
                shieldNumber.setValue(shield + "/" + maxShield);
                break;
            case "Faster Shield Recover":
                shieldRecoverTime /= 2;
                break;
            case "Longer Immunity":
                invincibleDuration *= 2;
                break;
            case "More Energy":
                maxEnergy += 100;
                energyBar.setMaxVal(maxEnergy);
                gainEnergy(100);
        }
        
        
    }
    
    
    /**
     * Returns the hero's current powers
     *
     * @return Returns the powerList
     */
    public HashSet<String> getPowerList() {
        return powerList;
    }
    
    /**
     * Returns the weapons the user has
     *
     * @return Returns the weapons the user has
     */
    public ArrayList<Weapon> getWeapons() {
        return weaponsInInventory;
    }
    
    /**
     * Heals the player a certain amount
     *
     * @param amount The amount to heal
     */
    public void heal(int amount) {
        hp += amount;
        hp = Math.min(hp, maxHP);
        hpBar.update(hp);
        hpNumber.setValue(hp + "/" + maxHP);
    }
    
    /**
     * Takes energy away from the player
     *
     * @param amount The amount to take away
     */
    public void useEnergy(int amount) {
        energy -= amount;
        energy = Math.max(energy, 0);
        energyBar.update(energy);
        energyNumber.setValue(energy + "/" + maxEnergy);
    }
    
    /**
     * Gives the player energy
     *
     * @param amount The amount to gain
     */
    public void gainEnergy(int amount) {
        energy += amount;
        energy = Math.min(energy, maxEnergy);
        energyBar.update(energy);
        energyNumber.setValue(energy + "/" + maxEnergy);
    }
    
    
    /**
     * Returns the amount of energy the player/hero has
     *
     * @return Returns how much energy the player has
     */
    public int getEnergyAmount() {
        return energy;
    }
    
    
}
