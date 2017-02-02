import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by danielansher on 10/18/16.
 */
public class LightTable extends JComponent {

    public ArrayList<PhotoComponent> photos;
    public ArrayList<ThumbnailComponent> thumbnails;
    private Graphics2D g2;

    private int mode;
    private PhotoComponent currentPhoto;

    private JPanel grid;

    public LightTable() {
        photos = new ArrayList<PhotoComponent>();
        thumbnails = new ArrayList<ThumbnailComponent>();
        currentPhoto = null;
        mode = 1;
    }

    public JPanel setGrid() {
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(0,5));

        for (int i = 0; i < photos.size(); i++) {
            PhotoComponent curPhoto = photos.get(i);
            System.out.println(i + ", " + curPhoto.getWidth() + "x" + curPhoto.getHeight());
            jp.add(curPhoto.getThumbnail());

        }
        return jp;
    }

    public JPanel setSplit() {
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
        for (int i = 0; i < photos.size(); i++) {
            PhotoComponent curPhoto = photos.get(i);

            if (hw5.splitView.isSelected()) {
                curPhoto.getThumbnail().addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if(e.getClickCount()==1){
                            System.out.println("Photo was clicked! Huzzuah");
                            //loop through all photos and remove borders from every other one.
                            removeAllBorders();
                            //add a border to current image.
                            curPhoto.getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                            curPhoto.getThumbnail().setSelected(true);
                            currentPhoto = curPhoto;
                            hw5.panel.removeAll();
                            hw5.panel.add(curPhoto);
                            repaint();
                            revalidate();
                            hw5.repack();
                        } else if (e.getClickCount()==2) {
                            System.out.println("Thumbnail was double clicked! Huzzah!");
                            currentPhoto = curPhoto;
                            hw5.bottomStrip.removeAll();
                            hw5.panel.removeAll();
                            hw5.panel.add(curPhoto);
                            setMode(1);
                            hw5.photoView.setSelected(true);
                            hw5.removeDrawingListeners();
                            hw5.attachDrawingListeners(curPhoto);
                            repaint();
                            revalidate();
                            hw5.repack();
                        }

                    }
                });
            }
            jp.add(curPhoto.getThumbnail());
        }

        return jp;
    }

    public void removeAllBorders() {
        for (int i = 0; i < photos.size(); i++) {
            photos.get(i).getThumbnail().setBorder(null);
            photos.get(i).getThumbnail().setSelected(false);
        }

    }

    public void moveForward() {

        if (hw5.photoView.isSelected()) {
            for (int i = photos.size() - 2; i >= 0; i--) {
                if (photos.get(i) == currentPhoto) {
                    System.out.println("I'm here and the next photo is " + photos.get(i+1).getWidth());
                    currentPhoto = photos.get(i + 1);
                    hw5.panel.removeAll();
                    hw5.panel.add(currentPhoto);
                    hw5.attachDrawingListeners(currentPhoto);
                    repaint();
                    revalidate();
                    hw5.repack();

                }
            }
        } else if (hw5.gridView.isSelected()) {

            for (int i = photos.size() - 2; i >= 0; i--) {
                if (photos.get(i).getThumbnail().getBorder() != null && !(i >= photos.size())) {
                    removeAllBorders();
                    photos.get(i + 1).getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                    currentPhoto = photos.get(i + 1);
                    repaint();
                    revalidate();
                    hw5.repack();
                }
            }

        } else if (hw5.splitView.isSelected()) {

            for (int i = photos.size() - 2; i >= 0; i--) {
                if (photos.get(i).getThumbnail().getBorder() != null) {
                    removeAllBorders();
                    photos.get(i + 1).getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                    currentPhoto = photos.get(i + 1);
                    hw5.panel.removeAll();
                    hw5.panel.add(currentPhoto);
                    hw5.contentArea.removeAll();
                    hw5.contentArea.add(hw5.panel, BorderLayout.CENTER);
                    repaint();
                    revalidate();
                    hw5.repack();
                }
            }

        }

    }

    public void moveBackward() {
        if (hw5.photoView.isSelected()) {
            for (int i = 0; i < photos.size(); i++) {
                if (photos.get(i) == currentPhoto && i > 0) {
                    System.out.println("I'm here and the previous photo is " + photos.get(i-1).getWidth());
                    currentPhoto = photos.get(i - 1);
                    hw5.panel.removeAll();
                    hw5.panel.add(currentPhoto);
                    hw5.attachDrawingListeners(currentPhoto);
                    repaint();
                    revalidate();
                    hw5.repack();
                }
            }
        } else if (hw5.gridView.isSelected()) {
            for (int i = 0; i < photos.size(); i++) {
                System.out.println("Currently trying to move backwards..");
                if (photos.get(i).getThumbnail().getBorder() != null && i-1 >= 0) {
                    //remove all borders
                    removeAllBorders();
                    //add a border to the next photo's thumbnail
                    photos.get(i - 1).getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                    currentPhoto = photos.get(i - 1);
                    repaint();
                    revalidate();
                    hw5.repack();
                }
            }
        } else if (hw5.splitView.isSelected()) {

            for (int i = 0; i < photos.size(); i++) {
                if (photos.get(i).getThumbnail().getBorder() != null && i-1 >= 0 ) {
                    removeAllBorders();
                    photos.get(i - 1).getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                    currentPhoto = photos.get(i - 1);
                    hw5.panel.removeAll();
                    hw5.panel.add(currentPhoto);
                    hw5.contentArea.removeAll();
                    hw5.contentArea.add(hw5.panel, BorderLayout.CENTER);
                    repaint();
                    revalidate();
                    hw5.repack();
                }
            }

        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        System.out.println("we are in the light component's paintcomponent.");
        if (mode == 1) {
            System.out.println("we are in mode 1! with image table of length: " + photos.size());

            currentPhoto.paintComponent(g2);

        }
        else if (mode == 2) {
            System.out.println("we are in mode 2! but we aren't drawing anything!");
            hw5.addElementsToGrid();
        }
        else if (mode == 3) {
            currentPhoto.paintComponent(g2);
        }

    }

    public void addPhotoToTable(PhotoComponent toAdd) {
        photos.add(toAdd);
        thumbnails.add(new ThumbnailComponent(toAdd));
        currentPhoto = toAdd;
        System.out.println("THE CURRENT TABLE HOLDS: " + photos);
    }

    public PhotoComponent removePhotoFromTable(int index) {
        return photos.remove(index);
    }

    public ArrayList<PhotoComponent> getTable() {
        return photos;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int m) {
        mode = m;
    }

    public PhotoComponent getCurrentPhoto() {
        return currentPhoto;
    }

    public void setCurrentPhoto(PhotoComponent p) {
        currentPhoto = p;
    }



}
