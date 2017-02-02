import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by danielansher on 11/13/16.
 */
public class Siger {

    //Ordinal directions
    private static final String NORTH = "N";
    private static final String SOUTH = "S";
    private static final String EAST = "E";
    private static final String WEST = "W";
    private static final String NORTHEAST = "B";
    private static final String NORTHWEST = "A";
    private static final String SOUTHEAST = "C";
    private static final String SOUTHWEST = "D";

    //General ordinal directional constants
    private static final String NORTHISH = NORTHWEST + NORTH + NORTHEAST;
    private static final String SOUTHISH = SOUTHWEST + SOUTH + SOUTHEAST;
    private static final String EASTISH = NORTHEAST + EAST + SOUTHEAST;
    private static final String WESTISH = NORTHWEST + WEST + SOUTHWEST;

    private static final String NORTHEASTISH = NORTH + NORTHEAST + EAST;
    private static final String SOUTHEASTISH = EAST + SOUTHEAST + SOUTH;
    private static final String NORTHWESTISH = NORTH + NORTHWEST + WEST;
    private static final String SOUTHWESTISH = WEST + SOUTHWEST + SOUTH;

    //Gestures
    private static final String[] NEXT_PHOTO = { SOUTHEAST, SOUTHWEST}; //>
    private static final String[] NEXT_PHOTO2 = { SOUTHEASTISH, SOUTHWESTISH };
    private static final String[] PREV_PHOTO = { SOUTHWEST, SOUTHEAST}; //<
    private static final String[] PREV_PHOTO2 = { SOUTHWESTISH, SOUTHEASTISH };
    private static final String[] DELETE_TEMPLATE = { SOUTHISH, SOUTHEASTISH, EASTISH, NORTHEASTISH, NORTHISH, WESTISH, SOUTHWESTISH, SOUTHISH };
    private static final String[] VACATION_TEMPLATE = { NORTHEASTISH, SOUTHEASTISH }; //^
    private static final String[] FAMILY_TEMPLATE = { SOUTHEASTISH, NORTHEASTISH }; //^ upside down
    private static final String[] SCHOOL_TEMPLATE = { NORTHEASTISH, SOUTHEASTISH, WESTISH }; // Triangle
    private static final String[] WORK_TEMPLATE = { EASTISH, SOUTHISH, WESTISH, NORTHISH }; // Square
    private static final String[] ANNOTATIONS_TEMPLATE = { SOUTHWESTISH, SOUTHISH, SOUTHEASTISH, EASTISH, NORTHEASTISH, NORTHISH, NORTHWESTISH}; // O, drawing counterclockwise

    private static final Pattern[] patternList = {
        Pattern.compile(producePatternString(NEXT_PHOTO)),
        Pattern.compile(producePatternString(NEXT_PHOTO2)),
        Pattern.compile(producePatternString(PREV_PHOTO)),
        Pattern.compile(producePatternString(PREV_PHOTO2)),
        Pattern.compile(producePatternString(DELETE_TEMPLATE)),
        Pattern.compile(producePatternString(VACATION_TEMPLATE)),
        Pattern.compile(producePatternString(FAMILY_TEMPLATE)),
        Pattern.compile(producePatternString(SCHOOL_TEMPLATE)),
        Pattern.compile(producePatternString(WORK_TEMPLATE)),
        Pattern.compile(producePatternString(ANNOTATIONS_TEMPLATE))
    };

    public enum PatternIndex {
        NEXT,
        NEXT2,
        PREV,
        PREV2,
        DELETE,
        VACATION,
        FAMILY,
        SCHOOL,
        WORK,
        ANNOTATIONS,
        NONE
    }

    public static PatternIndex matchToTemplates(String patternString) {
        for (int i = 0; i < patternList.length; i++) {
            System.out.println("Comparing Template w/ Pattern: " + patternString + ", " + patternList[i]);
            if (patternList[i].matcher(patternString).find()) {
                return PatternIndex.values()[i];
            }
        }
        return PatternIndex.NONE;
    }

    private static String producePatternString(String[] vector) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("^");
        buffer.append(".{0,2}+");
        for (int i = 0; i < vector.length; i++) {
            switch (vector[i]) {
                case EAST: buffer.append("E+"); break;
                case NORTH: buffer.append("N+"); break;
                case WEST: buffer.append("W+"); break;
                case SOUTH: buffer.append("S+"); break;
                case NORTHEAST: buffer.append("B+"); break;
                case NORTHWEST: buffer.append("A+"); break;
                case SOUTHWEST: buffer.append("D+"); break;
                case SOUTHEAST: buffer.append("C+"); break;
                case EASTISH: buffer.append("[BEC]+"); break;
                case NORTHISH: buffer.append("[ANB]+"); break;
                case WESTISH: buffer.append("[AWD]+"); break;
                case SOUTHISH: buffer.append("[DSC]+"); break;
                case SOUTHEASTISH: buffer.append("[ECS]+"); break;
                case SOUTHWESTISH: buffer.append("[WDS]+"); break;
                case NORTHEASTISH: buffer.append("[NBE]+"); break;
                case NORTHWESTISH: buffer.append("[NAW]+"); break;
                default:
                    break;
            }
        }

        buffer.append(".{0,2}+");
        buffer.append("$");
        return buffer.toString();
    }

    public static String produceDirectionVector(List<Point> stroke) {

        StringBuffer directionBuffer = new StringBuffer();

        for (int i = 1; i < stroke.size(); i++) {
            double x1 = stroke.get(i-1).getX();
            double x2 = stroke.get(i).getX();
            double y1 = stroke.get(i-1).getY();
            double y2 = stroke.get(i).getY();

            if (x1 - x2 < 0 && y1 - y2 > 0) {
                directionBuffer.append(NORTHEAST);
            } else if (x1 - x2 < 0 && y1 - y2 == 0) {
                directionBuffer.append(EAST);
            } else if (x1 - x2 < 0 && y1 - y2 < 0) {
                directionBuffer.append(SOUTHEAST);
            } else if (x1 - x2 == 0 && y1 - y2 < 0) {
                directionBuffer.append(SOUTH);
            } else if (x1 - x2 > 0 && y1 - y2 < 0) {
                directionBuffer.append(SOUTHWEST);
            } else if (x1 - x2 > 0 && y1 - y2 == 0) {
                directionBuffer.append(WEST);
            } else if (x1 - x2 > 0 && y1 - y2 > 0) {
                directionBuffer.append(NORTHWEST);
            } else if (x1 - x2 == 0 && y1 - y2 > 0) {
                directionBuffer.append(NORTH);
            }
        }

        return directionBuffer.toString();
    }


}
