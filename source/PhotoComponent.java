import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by danielansher on 10/18/16.
 */
public class PhotoComponent extends JComponent {

    private Image pic;
    private ThumbnailComponent thumbnail;
    public boolean flipped;

    public int contentAreaWidth;
    public int contentAreaHeight;
    public static Graphics2D g2;

    private List<Point> penStrokes;
    public static List<Point> gestureStrokes;
    private boolean drawOval = false;
    private int ovalX = 0;
    private int ovalY = 0;

    private ArrayList<Integer> rectDimensions;
    private boolean drawRect = false;
    private int rectX = 0;
    private int rectY = 0;
    private int rectWidth = 0;
    private int rectHeight = 0;
    public  boolean polygonSelectedActivate = false;

    private boolean draw;

    private boolean drawingMode = true;
    private boolean textMode = false;

    private String annotations = null;
    private ArrayList<String> texts = new ArrayList<String>();
    private ArrayList<Integer> yComp = new ArrayList<Integer>();
    private int newLineHeight = 12;
    public static boolean[] tags;

    public Polygon shape;
    public Point lastLocation;

    public boolean vacation = false;
    public boolean family = false;
    public boolean school = false;
    public boolean work = false;

    public HashMap<Integer, Integer> annotationItems = new HashMap<Integer, Integer>();
    public HashMap<Integer, Integer> shiftedAnnotationItems = new HashMap<Integer, Integer>();
    public HashMap<Integer, Integer> shiftedAnnotationItems2 = new HashMap<Integer, Integer>();
    public PhotoComponent(Image image, JPanel contentArea) {


        //Set the sizes before we load the image.
        Dimension pre = new Dimension();
        pre.setSize(600, 400);
        setPreferredSize(pre);
        setSize(pre);

        draw = true;
        penStrokes = new ArrayList<Point>();
        gestureStrokes = new ArrayList<Point>();
        rectDimensions = new ArrayList<Integer>();

        pic = image;
        flipped = false;
        tags = new boolean[4];
        tags[0] = false;
        tags[1] = false;
        tags[2] = false;
        tags[3] = false;

        contentAreaWidth = contentArea.getWidth();
        contentAreaHeight = contentArea.getHeight();

        //Setting the default sizes after we load the image.
        Dimension d = new Dimension();
        d.setSize(pic.getWidth(null), pic.getHeight(null));
        setPreferredSize(d);
        setSize(d);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (draw && pic != null) {
            super.paintComponent(g);
            g2 = (Graphics2D) g;
            int x = (contentAreaWidth - pic.getWidth(null)) / 2;
            int y = (contentAreaHeight - pic.getHeight(null)) / 2;
            if (!flipped) {
                g2.drawImage(pic, x, y, null);
                if (drawOval && hw5.isGesture) {
                    gestureStrokes.add(new Point(ovalX, ovalY));
                    if (gestureStrokes != null) {
                        for (Point p : gestureStrokes) {
                            g2.setPaint(Color.BLUE);
                            int brushSize = 10;
                            int xCoor = (int) (p.getX());
                            int yCoor = (int) (p.getY());
                            g2.fillOval((xCoor - (brushSize / 2)), (yCoor - (brushSize / 2)), brushSize, brushSize);
                        }
                    }
                }
            } else if (flipped) {
                if (hw5.isAnnotationMovement == false) {
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x, y, pic.getWidth(null), pic.getHeight(null));
                    g2.drawRect(x, y, pic.getWidth(null), pic.getHeight(null));
                }
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if(hw5.isGesture && hw5.isAnnotationMovement == false) {

                    //MAKE A BOOLEAN AND THAT DIFFERENTIATES IF IT IS A GESTURE OR MOVEMENT OF THE ANNOTATIONS.

                    System.out.println("I should be printing blue points");
                    gestureStrokes.add(new Point(ovalX, ovalY));
                    if (gestureStrokes != null) {
                        for (Point p : gestureStrokes) {
                            g2.setPaint(Color.BLUE);
                            int brushSize = 10;
                            int xCoor = (int) (p.getX());
                            int yCoor = (int) (p.getY());
                            g2.fillOval((xCoor - (brushSize / 2)), (yCoor - (brushSize / 2)), brushSize, brushSize);
                        }
                    }
                    System.out.println("polygonSelectedActivate: " + polygonSelectedActivate);
                    //This handles the annotations selection!
                    if(polygonSelectedActivate) {
                        int alpha = 127; // 50% transparent
                        Color myColor = new Color(0, 0, 255, alpha);
                        g.setColor(myColor);
                        int[] xPoints = new int[gestureStrokes.size()];
                        int[] yPoints = new int[gestureStrokes.size()];
                        for (int i = 0; i < gestureStrokes.size(); i++) {
                            xPoints[i] = gestureStrokes.get(i).x;
                            yPoints[i] = gestureStrokes.get(i).y;
                        }
                        shape = new Polygon(xPoints, yPoints, gestureStrokes.size());
                        g2.fillPolygon(shape);
                        polygonSelectedActivate = false;

                    }

                } else if (hw5.isAnnotationMovement) {
                    System.out.println("Redrawing polygon with dimensions: " + shape.getBounds());
                    g2.setColor(Color.BLUE);
                    g2.drawPolygon(shape);
                    g2.fillPolygon(shape);

                    if(hw5.eraseOldAnnotations) {
                        //erases old points.
                        for (HashMap.Entry<Integer, Integer> entry : shiftedAnnotationItems.entrySet()) {
                            g2.clearRect(entry.getKey(), entry.getValue(), 5, 5);
                        }
                        //draws new points
                        g2.setColor(Color.RED);
                        for (HashMap.Entry<Integer, Integer> entry : shiftedAnnotationItems2.entrySet()) {
                            g2.drawRect(entry.getKey(), entry.getValue(), 5, 5);
                        }

                    }

                } else {
                        System.out.println("I should be printing the red points.");
                        if (drawingMode) {
                            g2.setPaint(Color.RED);
                            System.out.println("drawOval, penStrokes: " + drawOval + ", " + penStrokes);
                            if (drawOval) {
                                penStrokes.add(new Point(ovalX, ovalY));
                                if (penStrokes != null) {
                                    for (Point p : penStrokes) {
                                        g2.setPaint(Color.RED);
                                        int brushSize = 5;
                                        int xCoor = (int) (p.getX());
                                        int yCoor = (int) (p.getY());
                                        g2.fillOval((xCoor - (brushSize / 2)), (yCoor - (brushSize / 2)), brushSize, brushSize);

                                        annotationItems.put((xCoor - (brushSize / 2)), (yCoor - (brushSize / 2)));
                                    }
                                }
                            }
                        } else if (textMode) {
                            g2.setPaint(Color.YELLOW);
                            if (drawRect) {
                                rectDimensions.add(rectX);
                                rectDimensions.add(rectY);
                                rectDimensions.add(rectWidth);
                                rectDimensions.add(rectHeight);

                                for (int i = 0; i < rectDimensions.size(); i += 4) {
                                    g2.fillRect(rectDimensions.get(i), rectDimensions.get(i + 1), rectDimensions.get(i + 2), rectDimensions.get(i + 3));
                                    g2.drawRect(rectDimensions.get(i), rectDimensions.get(i + 1), rectDimensions.get(i + 2), rectDimensions.get(i + 3));
                                    annotationItems.put(rectDimensions.get(i), rectDimensions.get(i + 1));
                                }
                                System.out.println("Annotations: " + annotations);
                                if (annotations != null) {
                                    //We are typing text on the sticky note.
                                    if (rectDimensions.size() > 0) {
                                        g2.setColor(Color.BLACK);
                                        int curStringWidth = g.getFontMetrics().stringWidth(annotations);
                                        int textX = rectDimensions.get(rectDimensions.size() - 4) + 2;
                                        int textY = rectDimensions.get(rectDimensions.size() - 3) + newLineHeight;
                                        int stickyX = rectDimensions.get(rectDimensions.size() - 4);
                                        int stickyY = rectDimensions.get(rectDimensions.size() - 3);
                                        int stickyWidth = rectDimensions.get(rectDimensions.size() - 2);
                                        int stickyHeight = rectDimensions.get(rectDimensions.size() - 1);

                                        //the character can be displayed on the sticky note, we haven't reached the end of
                                        //the line yet.
                                        if (textX + curStringWidth < (stickyX + stickyWidth - 4)) {
                                            if (texts != null) {
                                                texts.add(annotations);
                                                yComp.add(textY);
                                                for (int i = 0; i < texts.size(); i++) {
                                                    g2.drawString(texts.get(i), textX, yComp.get(i));
                                                    annotationItems.put(textX, yComp.get(i));

                                                }
                                            } else {
                                                g2.drawString(annotations, textX, textY);
                                                annotationItems.put(textX, textY);
                                            }

                                            //we must word wrap because the string goes passed the sticky's width.
                                        } else {
                                            texts.add(annotations);
                                            yComp.add(textY);
                                            for (int i = 0; i < texts.size(); i++) {
                                                g2.drawString(texts.get(i), textX, yComp.get(i));
                                                annotationItems.put(textX, yComp.get(i));
                                            }
                                            annotations = annotations.substring(annotations.length() - 1);
                                            newLineHeight += 12;
                                            g2.drawString(annotations, textX, textY + newLineHeight);
                                            annotationItems.put(textX, textY + newLineHeight);
                                        }

                                        //when we reach the end of the sticky, adjust its height to fit the text.
//                                if (textY > stickyY + stickyHeight) {
//                                    g2.setColor(Color.YELLOW);
//                                    g2.fillRect(stickyX, stickyY + stickyHeight, stickyWidth, newLineHeight);
//                                    g2.drawRect(stickyX, stickyY + stickyHeight, stickyWidth, newLineHeight);
//                                    stickyY = stickyHeight + newLineHeight;
//
//                                  }
//                                }

                                    }
                                }
                            }

                        }
                    }
                }

            }

        }



    public Image getImage() {
        return pic;
    }

    public void setImage(Image i) {
        pic = i;
    }

    public ThumbnailComponent getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(PhotoComponent p) {
        thumbnail = new ThumbnailComponent(p);
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean b) {
        flipped = b;
    }

    public void setDraw(boolean d) {
        draw = d;
    }

    public boolean getDraw() {
        return draw;
    }

    public void setDrawingMode(boolean d) {
        drawingMode = d;
    }

    public boolean getDrawingMode() {
        return drawingMode;
    }

    public void setTextMode(boolean d) {
        textMode = d;
    }

    public boolean getTextMode() {
        return textMode;
    }

    public void setRectangle(int x, int y, int width, int height) {
        drawRect = true;
        rectX = x;
        rectY = y;
        rectWidth = width;
        rectHeight = height;
    }

    public void removeRectangle() {
        drawRect = false;
    }

    public int[] setOval(int currentX, int currentY) {
        drawOval = true;
        int[] toReturn = {ovalX, ovalY};
        ovalX = currentX;
        ovalY = currentY;
        return toReturn;
    }

    public void removeOval() {
        drawOval = false;
    }

    public void concatString(char c) {
        if (annotations == null) {
            annotations = c + "";
        } else {
            annotations = annotations + c;
        }
    }


}
