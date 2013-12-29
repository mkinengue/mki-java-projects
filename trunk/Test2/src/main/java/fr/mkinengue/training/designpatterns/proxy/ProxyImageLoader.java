package fr.mkinengue.training.designpatterns.proxy;

public class ProxyImageLoader implements Image {

    private RealImageLoader image = null;
    private String filename = null;

    /**
     * Constructor
     * 
     * @param theFileName
     */
    public ProxyImageLoader(final String theFileName) {
        filename = theFileName;
    }

    /**
     * Displays the image
     */
    public void displayImage() {
        if (image == null) {
            image = new RealImageLoader(filename);
        }
        image.displayImage();
    }

}
