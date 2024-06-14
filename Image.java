import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Just an actor that is used for displaying images that can move and change
 * 
 * @author Felix Zhao
 * @version 0.1
 */
public class Image extends Actor
{
    /**
     * Creates an image using a file name 
     *
     * @param fileName The file path / file name
     */
    public Image(String fileName) {
        setImage(fileName);
    }
    
    /**
     * Creates an image using a file name 
     *
     * @param fileName The file path / file name
     */
    public Image(GreenfootImage image) {
        setImage(new GreenfootImage(image));
    }
    
    /**
     * Creates empty image with a width and a height
     *
     * @param width The width of the image
     * @param height The height of the image
     */
    public Image(int width, int height) {
        setImage(new GreenfootImage(width, height));
    }
}
