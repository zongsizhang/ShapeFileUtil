package shapes;

import ShapeFileParse.ShapeParseUtil;
import ShapeFileParse.ShapeTypeNotMatchException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by zongsizhang on 5/18/17.
 */
public class MultiPointWritable extends ShapeWritable {

    LinearRing boundBox = null; // Bounding box of current point set

    Point[] points = null; // specific points

    public void flatmapShape() {

    }

    public void parseShape(DataInputStream inputStream) throws IOException, ShapeTypeNotMatchException {
        ShapeParseUtil.validateShapeType(inputStream);
        boundBox = ShapeParseUtil.parseBoundingBox(inputStream);
        points = ShapeParseUtil.parseMultiPoints(inputStream);
    }

    public void printShape() {
        System.out.println("Bounding Box");
        for(Coordinate bound : boundBox.getCoordinates()){
            System.out.print("(" + bound.x + ", " + bound.y + ") ");
        }
        System.out.println("\nPoints: ");
        for(Point point : points){
            System.out.println(point.getX() + ", " + point.getY());
        }
    }
}
