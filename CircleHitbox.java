import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CircleHitbox here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CircleHitbox extends Hitbox
{
    private double radius;
    public CircleHitbox(SuperSmoothMover parent, double radius, int xOffset, int yOffset) {
        super(parent, xOffset, yOffset);
        this.radius = radius;
        setImage(new GreenfootImage((int)radius*2, (int)radius*2));
    }
    
    /**
     * Checks if it hits something solid
     *
     * @return Returns 0, 1 or 2 depending how it contacts a block
     */
    public int checkBlockHitX() {
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
                        setLocation(getPreciseX()+left-1-getImage().getWidth()/2, getPreciseY());
                    } else {
                        setLocation(getPreciseX()+right+1+getImage().getWidth()/2, getPreciseY());
                    }
                    parent.setLocation(getPreciseX()-xOffset, getPreciseY()-yOffset);
                    return 2;
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
                    parent.setLocation(getPreciseX()-xOffset, getPreciseY()-yOffset);
                    return 1;
                }
                
                //System.out.println(left + " " + right + " " + top + " " + bottom);
                
            }
        }
        
        return 0;
    }
    
    /**
     * Checks if it hits something solid
     *
     * @return Returns 0, 1 or 2 depending how it contacts a block
     */
    public int checkBlockHitY() {
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
                        setLocation(getPreciseX(), getPreciseY()+top-1-getImage().getHeight()/2);
                    } else {
                        setLocation(getPreciseX(), getPreciseY()+bottom+1+getImage().getHeight()/2);
                    }
                    parent.setLocation(getPreciseX()-xOffset, getPreciseY()-yOffset);
                    return 2;
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
                    parent.setLocation(getPreciseX()-xOffset, getPreciseY()-yOffset);
                    return 1;
                }
                
            }
        }
        return 0;
    }
}
