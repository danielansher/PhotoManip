import javax.swing.*;
import java.awt.*;

/**
 * Created by danielansher on 10/19/16.
 */
public class ThumbnailComponent extends JComponent {

    private PhotoComponent photo;
    private boolean selected;
    public ThumbnailComponent(PhotoComponent p) {
        Dimension pre = new Dimension();
        pre.setSize(600, 400);
        setPreferredSize(pre);
        setSize(pre);

        photo = p;
        selected = false;

        Dimension d = new Dimension();
        d.setSize(photo.getImage().getWidth(null), photo.getImage().getHeight(null));
        setPreferredSize(d);
        setSize(d);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics g1 = g.create();
        Graphics2D g2 = (Graphics2D) g1;
        //20% of the normal size
        g2.scale(0.2, 0.2);

        photo.setFlipped(false);
        photo.paintComponent(g2);

    }

    public void setSelected(boolean b) {
        selected = b;
    }
    public boolean isSelected() {
        return selected;
    }

}
