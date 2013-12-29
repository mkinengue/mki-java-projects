package fr.mkinengue.training.designpatterns.proxy;

public class RealImageLoader implements Image {

    private String filename = null;

    /**
     * Constructor
     * 
     * @param theFileName
     */
    public RealImageLoader(final String theFileName) {
        filename = theFileName;
        loadImageFromDisk();
    }

    /**
     * Loads the image from the disk
     */
    private void loadImageFromDisk() {
        System.out.println("Loading   " + filename);
    }

    /**
     * Displays the image
     */
    public void displayImage() {
        System.out.println("Displaying " + filename);
    }
}
