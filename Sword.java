import greenfoot.*;  
import java.util.ArrayList;

/**
 * One of the weapons: bow. This is coupled with an arrow.
 * 
 * @author Andy Feng
 * @version June 10th, 2024
 */
public class Sword extends Weapon {
    private static final String IMAGE_PATH = "images/sword/sword";
    private static final int DAMAGE = 1;
    private static final int RECOVER_TIME = 30;

    protected GreenfootImage[] swordRightFrames = new GreenfootImage[6];
    protected GreenfootImage[] swordLeftFrames = new GreenfootImage[6];
    private int frameNumber = 0;
    private boolean isSwinging;
    private int recoverCounter = 0;
    private SimpleTimer animationTimer = new SimpleTimer();
    
    public Sword() {
        super(DAMAGE);
        loadImages();
        isSwinging = false;
        setImage(swordRightFrames[0]);
    }

    private void loadImages() {
        for (int i = 0; i < swordRightFrames.length; i++) {
            swordRightFrames[i] = new GreenfootImage(IMAGE_PATH + i + ".png");
            swordRightFrames[i].scale(50, 50);
            swordLeftFrames[i] = new GreenfootImage(IMAGE_PATH + i + ".png");
            swordLeftFrames[i].mirrorHorizontally();
            swordLeftFrames[i].scale(50, 50);
        }
    }

    public void act() {
        super.act();
        Actor holder = getHolder();
        
        if (recoverCounter > 0) {
            recoverCounter--;
        }
        
        if (holder instanceof Hero || holder instanceof Enemy) {
            boolean right = holder instanceof Hero ? ((Hero) holder).right : ((Enemy) holder).right;
            animateSword(right);
        }
    }

    private void animateSword(boolean right) {
        if (isAttacking && beingUsed && recoverCounter == 0) {
            isSwinging = true;
            swing(right ? swordRightFrames : swordLeftFrames);
            attack();
        } else if (beingUsed && !isSwinging && recoverCounter != 0) {
            setImage(right ? swordRightFrames[0] : swordLeftFrames[0]);
        }
    }

    private void swing(GreenfootImage[] swingFrames) {
        while(frameNumber < swingFrames.length) {
            if(animationTimer.millisElapsed() > 500) {
                setImage(swingFrames[frameNumber]);
                frameNumber++;
            } else {
                animationTimer.mark();
            }
        }
        if(frameNumber >= swingFrames.length) {
            frameNumber = 0;
            recoverCounter = RECOVER_TIME;
            isSwinging = false;
        }
    }

    public void attack() {
        Actor holder = getHolder();
        if (holder instanceof Hero) {
            ArrayList<Enemy> enemies = (ArrayList<Enemy>) getIntersectingObjects(Enemy.class);
            for (Enemy enemy : enemies) {
                enemy.health -= DAMAGE;
            }
        } else if (holder instanceof Enemy) {
            Hero hero = (Hero) getOneIntersectingObject(Hero.class);
            hero.hp -= DAMAGE;
        }
    }
}
