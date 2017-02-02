import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * CS 4770 - Hw #5 - Animation in the Interface
 */
public class hw5 {

    private static LightTable table;
    private static LightTable tableForMagnets;
    private static PhotoComponent img;
    private static JFrame frame;
    public static JRadioButtonMenuItem photoView;
    public static JRadioButtonMenuItem gridView;
    public static JRadioButtonMenuItem splitView;

    public static JPanel contentArea;
    public static JPanel bottomStrip;
    public static JPanel centerGrid;

    public static JPanel panel;
    public static JPanel gridPanel;
    public static JPanel panelForMagnetThumbnails;

    public static JToggleButton vacation;
    public static JToggleButton family;
    public static JToggleButton school;
    public static JToggleButton work;


    //Animation Variables
    public static Timer timer;
    public static int newX = 0;
    public static int newY = 0;
    public static int prevX = 0;
    public static int prevY = 0;
    public static int targetLocX = 0;
    public static int targetLocY = 0;


    //Mouse Coordinates for PenStrokes
    private static int oldX, oldY;

    //Mouse Coordinates for Sticky Notes
    private static Point clickPoint;

    private static int i10 = 1;

    public static boolean isGesture = false;
    public static boolean isAnnotationMovement = false;
    public static boolean eraseOldAnnotations = false;

    private static int count = 1;

    public static boolean magnetModeOn = false;

    public static JMenu animationMenu;
    public static JMenu addMagnetMenu;
    public static JMenu removeMagnetMenu;

    public static JLabel vacationMag;
    public static JLabel familyMag;
    public static JLabel schoolMag;
    public static JLabel workMag;

    public static void main (String[] args) {
        frame = new JFrame("PhotoManip");
        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                for (int i = 0; i < tableForMagnets.photos.size(); i++) {
                    PhotoComponent curPhoto = tableForMagnets.photos.get(i);
                    ThumbnailComponent curThumbnail = curPhoto.getThumbnail();

                    //handles all the single magnets on the screen.
                    if (curPhoto.vacation && !curPhoto.family && !curPhoto.school && !curPhoto.work) {
                        System.out.println("in Vacation!");
                        moveSingleMagnet(curThumbnail, vacationMag);
                    } else if (!curPhoto.vacation && curPhoto.family && !curPhoto.school && !curPhoto.work) {
                        System.out.println("in Family!");
                        moveSingleMagnet(curThumbnail, familyMag);
                    } else if (!curPhoto.vacation && !curPhoto.family && curPhoto.school && !curPhoto.work) {
                        moveSingleMagnet(curThumbnail, schoolMag);
                    } else if (!curPhoto.vacation && !curPhoto.family && !curPhoto.school && curPhoto.work) {
                        moveSingleMagnet(curThumbnail, workMag);

                        //handles all the double magnet possibilities.
                    } else if (curPhoto.vacation && !curPhoto.family && curPhoto.school && !curPhoto.work) {
                        //vacation & school
                        moveMultipleMagnets(curThumbnail, vacationMag, schoolMag, null, null);
                    } else if (curPhoto.vacation && curPhoto.family && !curPhoto.school && !curPhoto.work) {
                        //vacation & family
                        moveMultipleMagnets(curThumbnail, vacationMag, familyMag, null, null);
                    } else if (curPhoto.vacation && !curPhoto.family && !curPhoto.school && curPhoto.work) {
                        //vacation & work
                        moveMultipleMagnets(curThumbnail, vacationMag, workMag, null, null);

                    } else if (!curPhoto.vacation && curPhoto.family && curPhoto.school && !curPhoto.work) {
                        //family & school
                        moveMultipleMagnets(curThumbnail, familyMag, schoolMag, null, null);
                    } else if (!curPhoto.vacation && curPhoto.family && !curPhoto.school && curPhoto.work) {
                        //family & work
                        moveMultipleMagnets(curThumbnail, familyMag, workMag, null, null);

                    } else if (!curPhoto.vacation && !curPhoto.family && curPhoto.school && curPhoto.work) {
                        //school & work
                        moveMultipleMagnets(curThumbnail, schoolMag, workMag, null, null);

                        //handles all the triple magnet possibilites.
                    } else if (curPhoto.vacation && curPhoto.family && curPhoto.school && !curPhoto.work) {
                        //vacation & family & school
                        moveMultipleMagnets(curThumbnail, vacationMag, schoolMag, workMag, null);
                    } else if (curPhoto.vacation && curPhoto.family && !curPhoto.school && curPhoto.work) {
                        //vacation & family & work
                        moveMultipleMagnets(curThumbnail, vacationMag, familyMag, workMag, null);
                    } else if (curPhoto.vacation && !curPhoto.family && curPhoto.school && curPhoto.work) {
                        //vacation & school & work
                        moveMultipleMagnets(curThumbnail, vacationMag, workMag, schoolMag, null);
                    } else if (!curPhoto.vacation && curPhoto.family && curPhoto.school && curPhoto.work) {
                        //family & school & work
                        moveMultipleMagnets(curThumbnail, familyMag, schoolMag, workMag, null);

                        //handles four magnet possibility.
                    } else if (curPhoto.vacation && curPhoto.family && curPhoto.school && curPhoto.work) {
                        moveMultipleMagnets(curThumbnail, familyMag, schoolMag, workMag, vacationMag);
                    }


                }

            }
        });
        img = null;
        contentArea = null;
        bottomStrip = null;
        table = new LightTable();
        tableForMagnets = new LightTable();
        panel = new JPanel();
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0,5));
        oldX = 0;
        oldY = 0;
        photoView = new JRadioButtonMenuItem();
        gridView = new JRadioButtonMenuItem();
        splitView = new JRadioButtonMenuItem();
        setupMenubar(frame);
        setupJFrame(frame);


    }

    private static void moveMultipleMagnets(ThumbnailComponent curThumbnail, JLabel magnet1, JLabel magnet2, JLabel magnet3, JLabel magnet4) {
        if (magnet3 == null && magnet4 == null) {
            targetLocX = (int)(magnet1.getLocation().getX() + magnet2.getLocation().getX()) / 2;
            targetLocY = (int)(magnet1.getLocation().getY() + magnet2.getLocation().getY()) / 2;
        } else if (magnet4 == null) {
            targetLocX = (int)(magnet1.getLocation().getX() + magnet2.getLocation().getX() + magnet3.getLocation().getX()) / 3;
            targetLocY = (int)(magnet1.getLocation().getY() + magnet2.getLocation().getY() + magnet3.getLocation().getY()) / 3;
        } else if (magnet1 != null && magnet2 != null && magnet3 != null && magnet4 != null) {
            targetLocX = (int)(magnet1.getLocation().getX() + magnet2.getLocation().getX() + magnet3.getLocation().getX() + magnet4.getLocation().getX()) / 4;
            targetLocY = (int)(magnet1.getLocation().getY() + magnet2.getLocation().getY() + magnet3.getLocation().getY() + magnet4.getLocation().getY()) / 4;
        }

        Point targetLoc = new Point(targetLocX, targetLocY);
        if (curThumbnail.getLocation() != targetLoc) {
            prevX = newX;
            prevY = newY;
            if (curThumbnail.getLocation().getX() != targetLocX) {
                double difference = curThumbnail.getLocation().getX() - targetLocX;
                if (difference <= 0.0) {
                    newX = (int)curThumbnail.getLocation().getX()+1;
                } else if (difference >= 0.0) {
                    newX = (int)curThumbnail.getLocation().getX()-1;
                }
            }
            if (curThumbnail.getLocation().getY() != targetLocY) {
                double difference = curThumbnail.getLocation().getY() - targetLocY;
                if (difference <= 0.0) {
                    newY = (int)curThumbnail.getLocation().getY()+1;
                } else if (difference >= 0.0) {
                    newY = (int)curThumbnail.getLocation().getY()-1;
                }
            }
            System.out.println("targetLoc: " + targetLoc);
            System.out.println("prevX, prevY: " + prevX + ", " + prevY);
            if (prevX == targetLocX && prevY == targetLocY) {
                timer.stop();
            } else {
                curThumbnail.setLocation(newX, newY);
                repack();
            }
        }
    }

    private static void moveSingleMagnet(ThumbnailComponent curThumbnail, JLabel magnet) {
        if (curThumbnail.getLocation() != magnet.getLocation()) {
            prevX = newX;
            prevY = newY;
            if (curThumbnail.getLocation().getX() != magnet.getLocation().getX()) {
                double difference = curThumbnail.getLocation().getX() - magnet.getLocation().getX();
                if (difference <= 0.0) {
                    newX = (int)curThumbnail.getLocation().getX()+1;
                } else if (difference >= 0.0) {
                    newX = (int)curThumbnail.getLocation().getX()-1;
                }
            }
            if (curThumbnail.getLocation().getY() != magnet.getLocation().getY()) {
                double difference = curThumbnail.getLocation().getY() - magnet.getLocation().getY();
                if (difference <= 0.0) {
                    newY = (int)curThumbnail.getLocation().getY()+1;
                } else if (difference >= 0.0) {
                    newY = (int)curThumbnail.getLocation().getY()-1;
                }
            }
            if (prevX == newX && prevY == newY) {
                timer.stop();
            } else {
                curThumbnail.setLocation(newX, newY);
                repack();
            }
        }
    }

    private static void addPhotoComponent(File file) {
        Image image = null;
        try {
            image = ImageIO.read(file);
        }  catch (IOException e2) {
            System.out.println("ERROR: Couldn't get image.");
        }
        img = new PhotoComponent(image, contentArea);
        img.setThumbnail(img);
        img.revalidate();

        attachDrawingListeners(img);

        vacation.setSelected(false);
        family.setSelected(false);
        school.setSelected(false);
        work.setSelected(false);

        if (img != null) {
            table.setCurrentPhoto(img);
            if (photoView.isSelected()) {

                table.addPhotoToTable(table.getCurrentPhoto());
                table.setMode(1);
                panel.removeAll();
                panel.add(img);
                contentArea.removeAll();
                contentArea.add(panel, BorderLayout.CENTER);

            } else if (gridView.isSelected()) {

                table.addPhotoToTable(img);
                table.setMode(2);
                removeThumbnailListeners();
                renderGrid();

            } else if (splitView.isSelected()) {

                table.addPhotoToTable(img);
                table.setMode(3);
                addElementsToBottomStrip();
                //Trick the computer into drawing PhotoView, even though its in SplitView.
                table.setMode(1);
                panel.removeAll();
                panel.add(table.getCurrentPhoto());
                contentArea.removeAll();
                contentArea.add(panel, BorderLayout.CENTER);
                table.setMode(3);

            }

            repack();
        }
    }

    private static void renderGrid() {
        gridPanel.removeAll();
        for (int i = 0; i < table.getTable().size(); i++) {
            PhotoComponent curPhoto = table.getTable().get(i);

            //Listeners are now re-added.
            curPhoto.getThumbnail().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        System.out.println("Thumbnail was clicked! BLAH BLAH");
                        table.removeAllBorders();
                        if(!magnetModeOn) {
                            curPhoto.getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                        }
                        table.setCurrentPhoto(curPhoto);
                    } else if (e.getClickCount() == 2) {
                        System.out.println("You double clicked the thumbnail in Grid mode!");
                        table.setCurrentPhoto(curPhoto);
                        gridPanel.removeAll();
                        panel.removeAll();
                        panel.add(curPhoto);
                        contentArea.removeAll();
                        contentArea.add(panel, BorderLayout.CENTER);
                        table.setMode(1);
                        photoView.setSelected(true);
                        removeDrawingListeners();
                        attachDrawingListeners(curPhoto);
                        table.repaint();
                        table.revalidate();
                        hw5.repack();
                    }
                }
            });
            gridPanel.add(curPhoto.getThumbnail());
        }
        contentArea.removeAll();
        contentArea.add(gridPanel, BorderLayout.CENTER);
    }

    public static void removeThumbnailListeners() {
        for (int i = 0; i < table.getTable().size(); i++) {
            ThumbnailComponent curThumbnail = table.getTable().get(i).getThumbnail();
            MouseListener[] mouseListeners = curThumbnail.getMouseListeners();
            for (MouseListener mouseListener : mouseListeners) {
                curThumbnail.removeMouseListener(mouseListener);
            }
        }

    }

    public static void removeDrawingListeners() {
        for (int i = 0; i < table.getTable().size(); i++) {
            PhotoComponent curPhoto = table.getTable().get(i);
            MouseListener[] mouseListeners = curPhoto.getMouseListeners();
            for (MouseListener mouseListener : mouseListeners) {
                curPhoto.removeMouseListener(mouseListener);
            }
            MouseMotionListener[] mouseMotionListeners = curPhoto.getMouseMotionListeners();
            for (MouseMotionListener mouseListener : mouseMotionListeners) {
                curPhoto.removeMouseMotionListener(mouseListener);
            }
            KeyListener[] keyListeners = curPhoto.getKeyListeners();
            for (KeyListener keyListener : keyListeners) {
                curPhoto.removeKeyListener(keyListener);
            }
        }

    }

    public static void attachDrawingListeners(PhotoComponent photo) {
        photo.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){
                    System.out.println("The image was clicked! Lol...");
                    flipImage();
                    photo.repaint();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){
                    isGesture = true;
                    System.out.println("Right click!");
                    if (photo.getDrawingMode()) {
                        if (withinRange(e.getX(), e.getY())) {
                            photo.setOval(e.getX(), e.getY());
                            photo.repaint();
                        }
                    }
                    if (photo.getTextMode()) {
                        if (withinRange(e.getX(), e.getY())) {
                            clickPoint = e.getPoint();
                        }
                    }
                }
                if(SwingUtilities.isLeftMouseButton(e)){
                    isGesture = false;
                    if (photo.getDrawingMode()) {
                        if (withinRange(e.getX(), e.getY())) {
                            photo.setOval(e.getX(), e.getY());
                            photo.repaint();
                        }
                    }
                    if (photo.getTextMode()) {
                        if (withinRange(e.getX(), e.getY())) {
                            clickPoint = e.getPoint();
                        }
                    }
                    if (photo.contains(e.getPoint())) {
                        photo.lastLocation = e.getPoint();
                        System.out.println("Last Location: " + photo.lastLocation);
                    }
                }



            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){
                    isGesture = true;
                    System.out.println("Erasing Stroke!");
                    List<Point> gestureStrokes = PhotoComponent.gestureStrokes;
                    String directionVector = Siger.produceDirectionVector(gestureStrokes);

                    Siger.PatternIndex templateIndex = Siger.matchToTemplates(directionVector);
                    count++;
                    System.out.println("TEMPLATE INDEX: " + templateIndex);
                    if (templateIndex == Siger.PatternIndex.NEXT || templateIndex == Siger.PatternIndex.NEXT2) {
                        table.moveForward();
                    }
                    if(templateIndex == Siger.PatternIndex.PREV || templateIndex == Siger.PatternIndex.PREV2) {
                        table.moveBackward();
                    }
                    if(templateIndex == Siger.PatternIndex.DELETE) {
                        deletePhoto();
                    }
                    if(templateIndex == Siger.PatternIndex.VACATION) {
                        if (hw5.vacation.isSelected()) {
                            hw5.vacation.setSelected(false);
                        } else if (!hw5.vacation.isSelected()) {
                            hw5.vacation.setSelected(true);
                        }

                    }
                    if(templateIndex == Siger.PatternIndex.FAMILY) {
                        if (hw5.family.isSelected()) {
                            hw5.family.setSelected(false);
                        } else if (!hw5.family.isSelected()) {
                            hw5.family.setSelected(true);
                        }
                    }
                    if(templateIndex == Siger.PatternIndex.SCHOOL) {
                        if (hw5.school.isSelected()) {
                            hw5.school.setSelected(false);
                        } else if (!hw5.school.isSelected()) {
                            hw5.school.setSelected(true);
                        }
                    }
                    if(templateIndex == Siger.PatternIndex.WORK) {
                        if (hw5.work.isSelected()) {
                            hw5.work.setSelected(false);
                        } else if (!hw5.work.isSelected()){
                            hw5.work.setSelected(true);
                        }
                    }

                    if(templateIndex == Siger.PatternIndex.ANNOTATIONS && photo.flipped) {
                        //loop through stroke points of circle
                        //create polygon with points and fill contained area with a color.
                        System.out.println("we are about to set polygonSelectedActivate...");
                        photo.polygonSelectedActivate = true;
                        photo.repaint();
                    }

                    if(templateIndex == Siger.PatternIndex.NONE && (count % 2 == 0)) {
                        JOptionPane.showMessageDialog(null, "ERROR: This gesture was not recognized!");
                    }

                    //Clear the visible gesture
                    if (templateIndex != Siger.PatternIndex.ANNOTATIONS) {
                        if (photo.getDrawingMode()) {
                            //photo.removeOval();
                            PhotoComponent.gestureStrokes.clear();
                            photo.repaint();
                            isGesture = false;
                            photo.polygonSelectedActivate = false;

                        }
                        if (photo.getTextMode()) {
                            clickPoint = null;
                        }
                    }

                }

                if(SwingUtilities.isLeftMouseButton(e)){
                    if (!isAnnotationMovement) {
                        isGesture = false;
                    }
                    isAnnotationMovement = false;
                    photo.shape = null;
                    photo.repaint();
                    if (photo.getDrawingMode()) {
                        //photo.removeOval();
                    }
                    if (photo.getTextMode()) {
                        clickPoint = null;
                    }
                }


            }
        });

        photo.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if(SwingUtilities.isRightMouseButton(e)){
                    isGesture = true;
                    System.out.println("Right click!");
                    //my code
                    if (photo.getDrawingMode()) {
                        if (withinRange(e.getX(), e.getY())) {
                            int[] toUpdate = photo.setOval(e.getX(), e.getY());
                            oldX = toUpdate[0];
                            oldY = toUpdate[1];
                            photo.repaint();
                        }
                    }
                    if (photo.getTextMode()) {
                        if (withinRange(e.getX(), e.getY())) {
                            Point dragPoint = e.getPoint();
                            int x = Math.min(clickPoint.x, dragPoint.x);
                            int y = Math.min(clickPoint.y, dragPoint.y);
                            int width = Math.max(clickPoint.x - dragPoint.x, dragPoint.x - clickPoint.x);
                            int height = Math.max(clickPoint.y - dragPoint.y, dragPoint.y - clickPoint.y);
                            photo.setRectangle(x, y, width, height);
                            photo.repaint();
                            photo.setFocusable(true);
                            photo.requestFocusInWindow();

                        }
                    }
                }

                if(SwingUtilities.isLeftMouseButton(e)){
                    isGesture = false;
                    if (photo.getDrawingMode()) {
                        if (withinRange(e.getX(), e.getY())) {
                            int[] toUpdate = photo.setOval(e.getX(), e.getY());
                            oldX = toUpdate[0];
                            oldY = toUpdate[1];
                            photo.repaint();
                        }
                    }
                    if (photo.getTextMode()) {
                        if (withinRange(e.getX(), e.getY())) {
                            Point dragPoint = e.getPoint();
                            int x = Math.min(clickPoint.x, dragPoint.x);
                            int y = Math.min(clickPoint.y, dragPoint.y);
                            int width = Math.max(clickPoint.x - dragPoint.x, dragPoint.x - clickPoint.x);
                            int height = Math.max(clickPoint.y - dragPoint.y, dragPoint.y - clickPoint.y);
                            photo.setRectangle(x, y, width, height);
                            photo.repaint();
                            photo.setFocusable(true);
                            photo.requestFocusInWindow();

                        }
                    }

                    System.out.println("The dragging is activated.");
                    if (photo.shape != null) {
                        int xcoor = (int)(e.getX() - photo.shape.getBounds().getX());
                        int ycoor = (int)(e.getY() - photo.shape.getBounds().getY());
                        System.out.println("MOUSE POINTER: " + e.getX() + ", " + e.getY());
                        System.out.println("POLYGON TOP LEFT: " + photo.shape.getBounds().getX() + ", " + photo.shape.getBounds().getY());
                        System.out.println("HOW MUCH TO SHIFT BY: " + xcoor + ", " + ycoor);

                        //Look for contents under and erase and redraw under selection
                        for (HashMap.Entry<Integer, Integer> entry : photo.annotationItems.entrySet()) {
                            if (photo.shape.contains(entry.getKey(), entry.getValue())) {
                                photo.shiftedAnnotationItems.put(entry.getKey(), entry.getValue());
                                photo.shiftedAnnotationItems2.put(xcoor + entry.getKey(), ycoor + entry.getValue());
                            }
                        }

                        photo.shape.translate(xcoor, ycoor);

                        photo.lastLocation = e.getPoint();
                        isAnnotationMovement = true;
                        eraseOldAnnotations = true;


                        photo.repaint();
                    }

                }



            }
        });

        photo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("Key was typed!" + e.getKeyChar());
                photo.concatString(e.getKeyChar());
                photo.repaint();

            }
        });
    }

    public static void addElementsToGrid() {
        System.out.println("centerGrid: " + centerGrid.toString());
        centerGrid = new JPanel();
        centerGrid.setLayout(new GridLayout(0,5));
        centerGrid = table.setGrid();
        contentArea.add(centerGrid, BorderLayout.CENTER);
    }

    private static void addElementsToBottomStrip() {
        bottomStrip.add(new JScrollPane().add(table.setSplit()), BorderLayout.SOUTH);
    }

    private static void initializeBottomStrip() {
        bottomStrip = new JPanel();
        bottomStrip.setLayout(new BoxLayout(bottomStrip, BoxLayout.X_AXIS));
        bottomStrip.setPreferredSize(new Dimension(100,100));
        bottomStrip.setMinimumSize(new Dimension(600, 400));
    }

    public static void repack() {
        if (i10 == 1) {
            frame.setPreferredSize(new Dimension(800, 600));
            i10 = 2;
        } else if (i10 == 2) {
            frame.setPreferredSize(new Dimension(800, 599));
            i10=1;
        }
        frame.pack();
        System.out.println("just packed the frame.");
    }

    private static boolean withinRange(int x, int y) {
        if (x > img.getX() && x < img.getX() + img.getWidth()) {
            if (y > img.getY() && y < img.getY() + img.getHeight()) {
                return true;
            }
        }
        return false;
    }

    private static void flipImage() {
        if (!img.isFlipped()) {
            img.setFlipped(true);
        } else if (img.isFlipped()) {
            img.setFlipped(false);
        }
    }

    private static void setupJFrame(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMinimumSize(new Dimension(500, 350));

        contentArea = new JPanel();
        contentArea.setLayout(new BorderLayout());
        contentArea.setPreferredSize(new Dimension(100,100));
        contentArea.setMinimumSize(new Dimension(600, 400));
        contentArea.setBackground(new Color(125, 134, 137));

        centerGrid = new JPanel();
        centerGrid.setLayout(new GridLayout(5,5));
        centerGrid.setBackground(Color.BLUE);

        initializeBottomStrip();

        panel.add(contentArea, BorderLayout.CENTER);
        panel.add(bottomStrip, BorderLayout.SOUTH);
        panel.setMinimumSize(new Dimension(300,300));


        JLabel statusBar = new JLabel("Status Label");
        statusBar.setHorizontalAlignment(SwingConstants.CENTER);
        //panel.add(statusBar, BorderLayout.SOUTH);


        vacation = new JToggleButton("Vacation");
        vacation.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    statusBar.setText("Vacation was selected as a tag!");
                    table.getCurrentPhoto().vacation = true;
                } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    statusBar.setText("Vacation was deselected as a tag!");
                    table.getCurrentPhoto().vacation = false;
                }
            }
        });

        family = new JToggleButton("Family");
        family.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    statusBar.setText("Family was selected as a tag!");
                    table.getCurrentPhoto().family = true;
                } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    statusBar.setText("Family was deselected as a tag!");
                    table.getCurrentPhoto().family = false;
                }
            }
        });

        school = new JToggleButton("School");
        school.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    statusBar.setText("School was selected as a tag!");
                    table.getCurrentPhoto().school = true;
                } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    statusBar.setText("School was deselected as a tag!");
                    table.getCurrentPhoto().school = false;
                }
            }
        });

        work = new JToggleButton("Work");
        work.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    statusBar.setText("Work was selected as a tag!");
                    table.getCurrentPhoto().work = true;
                } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    statusBar.setText("Work was deselected as a tag!");
                    table.getCurrentPhoto().work = false;
                }
            }
        });

        JToggleButton sports = new JToggleButton("Sports");
        sports.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    statusBar.setText("Sports was selected as a tag!");
                } else if (ev.getStateChange() == ItemEvent.DESELECTED) {
                    statusBar.setText("Sports was deselected as a tag!");
                }
            }
        });

        ButtonGroup group = new ButtonGroup();
        JRadioButton drawing = new JRadioButton("Drawing");
        drawing.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    if (img != null) {
                        img.setDrawingMode(true);
                        img.setTextMode(false);
                    }

                    statusBar.setText("Mode is set to drawing!");
                }
            }
        });
        drawing.setSelected(true);

        JRadioButton text = new JRadioButton("Text");
        text.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    if (img != null) {
                        img.setDrawingMode(false);
                        img.setTextMode(true);
                    }
                    statusBar.setText("Mode is set to text!");
                }
            }
        });
        group.add(drawing);
        group.add(text);

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));

        JLabel tags = new JLabel("Select Tags: ");
        Font f = tags.getFont();
        tags.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
        subPanel.add(tags);

        subPanel.add(vacation);
        subPanel.add(family);
        subPanel.add(school);
        subPanel.add(work);
        //subPanel.add(sports);

        JLabel mode = new JLabel("Select Mode: ");
        Font f2 = mode.getFont();
        mode.setFont(f2.deriveFont(f2.getStyle() ^ Font.BOLD));
        subPanel.add(mode);

        subPanel.add(drawing);
        subPanel.add(text);

        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                statusBar.setText("You just moved backwards by 1 photo.");
                table.moveBackward();
            }
        });

        JButton forward = new JButton("Forward");
        forward.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusBar.setText("You just moved forward by 1 photo.");
                table.moveForward();
            }
        });
        subPanel.add(back);
        subPanel.add(forward);
        panel.add(subPanel, BorderLayout.WEST);

        frame.add(panel);
        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(500, 350));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                img.contentAreaHeight = contentArea.getHeight();
                img.contentAreaWidth = contentArea.getWidth();
            }
        });

    }

    private static void deletePhoto() {
        System.out.println("A photo is being deleted... :)");
        if (photoView.isSelected()) {
            if (table.getTable().size() == 1) {
                table.getTable().remove(0);
                panel.removeAll();
                repack();
            } else {

                //Sets photo to the next one.
                for (int i = 0; i < table.getTable().size() - 1; i++) {
                    if (table.getTable().get(i) == table.getCurrentPhoto()) {
                        table.setCurrentPhoto(table.getTable().get(i+1));
                        table.getTable().remove(i);

                    }
                }
                //Delete current photo.
                panel.removeAll();
                panel.add(table.getCurrentPhoto());
                contentArea.removeAll();
                contentArea.add(panel, BorderLayout.CENTER);
                repack();
            }
        } else if (gridView.isSelected()) {
            if (table.getTable().size() == 1) {
                table.getTable().remove(0);
                gridPanel.removeAll();
                repack();
            } else {
                //Sets photo to the next one.
                for (int i = 0; i < table.getTable().size() - 1; i++) {
                    if (table.getTable().get(i) == table.getCurrentPhoto()) {
                        table.setCurrentPhoto(table.getTable().get(i+1));
                        table.removeAllBorders();
                        if(!magnetModeOn) {
                            table.getCurrentPhoto().getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                        }
                        table.getCurrentPhoto().getThumbnail().setSelected(true);
                        table.getTable().remove(i);
                    }
                }
                //Delete current photo.
                gridPanel.removeAll();
                removeThumbnailListeners();
                renderGrid();
                repack();
            }
        } else if (splitView.isSelected()) {
            if (table.getTable().size() == 1) {
                table.getTable().remove(0);
                bottomStrip.removeAll();
                panel.removeAll();
                repack();
            } else {
                //Sets photo to the next one.
                for (int i = 0; i < table.getTable().size() - 1; i++) {
                    if (table.getTable().get(i) == table.getCurrentPhoto()) {
                        table.setCurrentPhoto(table.getTable().get(i+1));
                        table.removeAllBorders();
                        if(!magnetModeOn) {
                            table.getCurrentPhoto().getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                        }
                        table.getCurrentPhoto().getThumbnail().setSelected(true);
                        table.getTable().remove(i);
                    }
                }
                //Delete current photo.
                bottomStrip.removeAll();
                panel.removeAll();
                addElementsToBottomStrip();
                panel.add(table.getCurrentPhoto());
                contentArea.removeAll();
                contentArea.add(panel, BorderLayout.CENTER);
                repack();
            }
        }
    }

    private static void generateMagnetizedThumbnails() {
        panelForMagnetThumbnails = new JPanel();
        panelForMagnetThumbnails.setLayout(null);
        panelForMagnetThumbnails.setSize(contentArea.getSize());
        Random rand = new Random();
        for (int i = 0; i < tableForMagnets.photos.size(); i++) {
            int randomX = rand.nextInt(((int)contentArea.getSize().getWidth()-100)-0) + 0;
            int randomY = rand.nextInt(((int)contentArea.getSize().getHeight()-100)-0) + 0;
            ThumbnailComponent curThumbnail = tableForMagnets.photos.get(i).getThumbnail();
            curThumbnail.setLocation(randomX,randomY);
            curThumbnail.setBorder(null);
            System.out.println("Thumbnail " + i + ": " + curThumbnail.getSize());
            System.out.println("Real Dim " + i + ": " + tableForMagnets.photos.get(i).getSize());
            panelForMagnetThumbnails.add(curThumbnail);
        }

    }

    private static void setupMenubar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        animationMenu = new JMenu("Magnet Mode");
        addMagnetMenu = new JMenu("Add Magnet");
        removeMagnetMenu = new JMenu("Remove Magnet");

        animationMenu.setEnabled(false);
        addMagnetMenu.setEnabled(false);
        removeMagnetMenu.setEnabled(false);

        JRadioButtonMenuItem animationOnItem = new JRadioButtonMenuItem();
        JRadioButtonMenuItem animationOffItem = new JRadioButtonMenuItem();

        animationOnItem.setText("On");
        animationOnItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                animationOnItem.setSelected(true);
                animationOffItem.setSelected(false);
                magnetModeOn = true;
                addMagnetMenu.setEnabled(true);
                removeMagnetMenu.setEnabled(true);
                tableForMagnets = table;
                //Clear the middle container of the gridlayout.
                //Add thumbnail images and display them right next to each other spaced out
                //generateMagnetizedThumbnails();
                contentArea.removeAll();
                gridPanel.removeAll();
                generateMagnetizedThumbnails();
                contentArea.add(panelForMagnetThumbnails, BorderLayout.CENTER);
                repack();
            }
        });


        animationOffItem.setText("Off");
        animationOffItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                animationOffItem.setSelected(true);
                animationOnItem.setSelected(false);
                magnetModeOn = false;
                addMagnetMenu.setEnabled(false);
                removeMagnetMenu.setEnabled(false);
                panelForMagnetThumbnails.removeAll();
                contentArea.removeAll();
                animationMenu.setEnabled(true);
                table.removeAllBorders();
                if(!magnetModeOn) {
                    table.getCurrentPhoto().getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                }
                gridView.setSelected(true);
                photoView.setSelected(false);
                splitView.setSelected(false);
                table.setMode(2);
                bottomStrip.removeAll();
                removeThumbnailListeners();
                renderGrid();
                if (table.getTable().isEmpty()) {
                    contentArea.removeAll();
                    panel.removeAll();
                    centerGrid.removeAll();
                    bottomStrip.removeAll();
                    gridPanel.removeAll();
                }
                repack();
            }
        });
        animationOffItem.setSelected(true);

        animationMenu.add(animationOffItem);
        animationMenu.add(animationOnItem);

        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();

        vacationMag = new JLabel("Vacation");
        familyMag = new JLabel("Family");
        schoolMag = new JLabel("School");
        workMag = new JLabel("Work");

        vacationMag.setOpaque(true);
        familyMag.setOpaque(true);
        schoolMag.setOpaque(true);
        workMag.setOpaque(true);

        vacationMag.setSize(new Dimension(60, 16));
        familyMag.setSize(new Dimension(60, 16));
        schoolMag.setSize(new Dimension(60, 16));
        workMag.setSize(new Dimension(60, 16));

        vacationMag.setVisible(false);
        familyMag.setVisible(false);
        schoolMag.setVisible(false);
        workMag.setVisible(false);

        vacationMag.addMouseListener(myMouseAdapter);
        vacationMag.addMouseMotionListener(myMouseAdapter);
        familyMag.addMouseListener(myMouseAdapter);
        familyMag.addMouseMotionListener(myMouseAdapter);
        schoolMag.addMouseListener(myMouseAdapter);
        schoolMag.addMouseMotionListener(myMouseAdapter);
        workMag.addMouseListener(myMouseAdapter);
        workMag.addMouseMotionListener(myMouseAdapter);

        JMenuItem addVacationMagnet = new JMenuItem();
        JMenuItem addFamilyMagnet = new JMenuItem();
        JMenuItem addSchoolMagnet = new JMenuItem();
        JMenuItem addWorkMagnet = new JMenuItem();

        addVacationMagnet.setText("Add Vacation");
        addVacationMagnet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //generateMagnetizedThumbnails();
                //Remove this later to combine the thumbnails with the magnets!!
                //contentArea.removeAll();
                //panelForMagnetThumbnails.removeAll();
                vacationMag.setVisible(true);
                vacationMag.setLocation(0,0);
                vacationMag.setBorder(new LineBorder(Color.YELLOW, 3));
                System.out.println("Magnet Size: " + vacationMag.getSize());
                panelForMagnetThumbnails.add(vacationMag);
                panelForMagnetThumbnails.setComponentZOrder(vacationMag, 0);
                contentArea.add(panelForMagnetThumbnails, BorderLayout.CENTER);
                myMouseAdapter.vacation = true;
                System.out.println("Adding the vacation magnet! ..supposedly. " + vacationMag.getText());
                repack();
            }
        });

        addFamilyMagnet.setText("Add Family");
        addFamilyMagnet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                familyMag.setVisible(true);
                familyMag.setLocation(80,0);
                familyMag.setBorder(new LineBorder(Color.ORANGE, 3));
                panelForMagnetThumbnails.add(familyMag);
                panelForMagnetThumbnails.setComponentZOrder(familyMag, 0);
                contentArea.add(panelForMagnetThumbnails, BorderLayout.CENTER);
                System.out.println("Adding the family magnet! ..supposedly. " + familyMag.getText());
                myMouseAdapter.family = true;
                repack();
            }
        });

        addSchoolMagnet.setText("Add School");
        addSchoolMagnet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                schoolMag.setVisible(true);
                schoolMag.setLocation(160,0);
                schoolMag.setBorder(new LineBorder(Color.GREEN, 3));
                panelForMagnetThumbnails.add(schoolMag);
                panelForMagnetThumbnails.setComponentZOrder(schoolMag, 0);
                contentArea.add(panelForMagnetThumbnails, BorderLayout.CENTER);
                System.out.println("Adding the school magnet! ..supposedly. " + schoolMag.getText());
                myMouseAdapter.school = true;
                repack();
            }
        });

        addWorkMagnet.setText("Add Work");
        addWorkMagnet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                workMag.setVisible(true);
                workMag.setLocation(240,0);
                workMag.setBorder(new LineBorder(Color.MAGENTA, 3));
                panelForMagnetThumbnails.add(workMag);
                panelForMagnetThumbnails.setComponentZOrder(workMag, 0);
                contentArea.add(panelForMagnetThumbnails, BorderLayout.CENTER);
                System.out.println("Adding the work magnet! ..supposedly. " + workMag.getText());
                myMouseAdapter.work = true;
                repack();
            }
        });

        addMagnetMenu.add(addVacationMagnet);
        addMagnetMenu.add(addFamilyMagnet);
        addMagnetMenu.add(addSchoolMagnet);
        addMagnetMenu.add(addWorkMagnet);


        JMenuItem removeVacationMagnet = new JMenuItem();
        JMenuItem removeFamilyMagnet = new JMenuItem();
        JMenuItem removeSchoolMagnet = new JMenuItem();
        JMenuItem removeWorkMagnet = new JMenuItem();

        removeVacationMagnet.setText("Remove Vacation");
        removeVacationMagnet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                vacationMag.setVisible(false);
                myMouseAdapter.vacation = false;
            }
        });

        removeFamilyMagnet.setText("Remove Family");
        removeFamilyMagnet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                familyMag.setVisible(false);
                myMouseAdapter.family = false;
            }
        });

        removeSchoolMagnet.setText("Remove School");
        removeSchoolMagnet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                schoolMag.setVisible(false);
                myMouseAdapter.school = false;
            }
        });

        removeWorkMagnet.setText("Remove Work");
        removeWorkMagnet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                workMag.setVisible(false);
                myMouseAdapter.work = false;
            }
        });

        removeMagnetMenu.add(removeVacationMagnet);
        removeMagnetMenu.add(removeFamilyMagnet);
        removeMagnetMenu.add(removeSchoolMagnet);
        removeMagnetMenu.add(removeWorkMagnet);


        JMenuItem importItem = new JMenuItem();
        importItem.setText("Import");
        importItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    addPhotoComponent(file);

                }
            }
        });

        JMenuItem deleteItem = new JMenuItem();
        deleteItem.setText("Delete");
        deleteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

                deletePhoto();
            }
        });

        JMenuItem exitItem = new JMenuItem();
        exitItem.setText("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileMenu.add(importItem);
        fileMenu.add(deleteItem);
        fileMenu.add(exitItem);

        ButtonGroup group = new ButtonGroup();

        photoView.setSelected(true);
        photoView.setText("Photo View");
        photoView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                animationMenu.setEnabled(false);
                removeDrawingListeners();
                attachDrawingListeners(table.getCurrentPhoto());
                photoView.setSelected(true);
                gridView.setSelected(false);
                splitView.setSelected(false);
                table.setMode(1);
                gridPanel.removeAll();
                bottomStrip.removeAll();
                centerGrid.removeAll();
                panel.removeAll();
                panel.add(table.getCurrentPhoto());
                contentArea.removeAll();
                contentArea.add(panel, BorderLayout.CENTER);
                if (table.getTable().isEmpty()) {
                    contentArea.removeAll();
                    panel.removeAll();
                    centerGrid.removeAll();
                    bottomStrip.removeAll();
                    gridPanel.removeAll();
                }
                repack();
            }
        });
        group.add(photoView);


        gridView.setText("Grid View");
        gridView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                animationMenu.setEnabled(true);
                table.removeAllBorders();
                if(!magnetModeOn) {
                    table.getCurrentPhoto().getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                }
                gridView.setSelected(true);
                photoView.setSelected(false);
                splitView.setSelected(false);
                table.setMode(2);
                bottomStrip.removeAll();
                removeThumbnailListeners();
                renderGrid();
                if (table.getTable().isEmpty()) {
                    contentArea.removeAll();
                    panel.removeAll();
                    centerGrid.removeAll();
                    bottomStrip.removeAll();
                    gridPanel.removeAll();
                }
                repack();

            }
        });
        group.add(gridView);

        splitView.setText("Split View");
        splitView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                animationMenu.setEnabled(false);
                removeThumbnailListeners();
                splitView.setSelected(true);
                photoView.setSelected(false);
                gridView.setSelected(false);
                table.setMode(3);
                centerGrid.removeAll();
                panel.removeAll();
                panel.add(table.getCurrentPhoto());
                attachDrawingListeners(table.getCurrentPhoto());
                contentArea.removeAll();
                contentArea.add(panel, BorderLayout.CENTER);
                addElementsToBottomStrip();
                table.removeAllBorders();
                table.getCurrentPhoto().getThumbnail().setBorder(new LineBorder(Color.RED, 12));
                table.getCurrentPhoto().getThumbnail().setSelected(true);
                if (table.getTable().isEmpty()) {
                    contentArea.removeAll();
                    panel.removeAll();
                    centerGrid.removeAll();
                    bottomStrip.removeAll();
                    gridPanel.removeAll();
                }
                repack();


            }
        });
        group.add(splitView);

        viewMenu.add(photoView);
        viewMenu.add(gridView);
        viewMenu.add(splitView);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(animationMenu);
        menuBar.add(addMagnetMenu);
        menuBar.add(removeMagnetMenu);
        frame.setJMenuBar(menuBar);

    }


    static class MyMouseAdapter extends MouseAdapter {

        private Point initialLoc;
        private Point initialLocOnScreen;
        public boolean vacation = false;
        public boolean family = false;
        public boolean school = false;
        public boolean work = false;

        @Override
        public void mousePressed(MouseEvent e) {
            Component comp = (Component)e.getSource();
            initialLoc = comp.getLocation();
            initialLocOnScreen = e.getLocationOnScreen();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Component comp = (Component)e.getSource();
            Point locOnScreen = e.getLocationOnScreen();

            int x = locOnScreen.x - initialLocOnScreen.x + initialLoc.x;
            int y = locOnScreen.y - initialLocOnScreen.y + initialLoc.y;
            comp.setLocation(x, y);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Component comp = (Component)e.getSource();
            Point locOnScreen = e.getLocationOnScreen();

            int x = locOnScreen.x - initialLocOnScreen.x + initialLoc.x;
            int y = locOnScreen.y - initialLocOnScreen.y + initialLoc.y;
            comp.setLocation(x, y);

            if(vacation || family || school || work) {
                //System.out.println("Location of Vacation Magnet!! " + vacationMag.getLocation());
                //Now, let's move the thumbnail to the magnet's position...
                //Remember, we have to check to see if this photo has multiple tags.
                timer.start();

            }
        }
    }

}
