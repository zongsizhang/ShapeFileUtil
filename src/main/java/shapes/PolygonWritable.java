package shapes;

import ShapeFileParse.ShpParseUtil;
import ShapeFileParse.ShapeTypeNotMatchException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by zongsizhang on 5/12/17.
 */
public class PolygonWritable extends ShapeWritable {

    private Polygon polygon;

    public void flatmapShape() {

    }

    public void parseShape(DataInputStream inputStream)
            throws IOException, ShapeTypeNotMatchException
    {
        ShpParseUtil.validateShapeType(inputStream);
        polygon = ShpParseUtil.parsePolygon(inputStream);
    }

    public void printShape() {
        System.out.println("==============polygon===========");
        Coordinate[] coordinates = polygon.getCoordinates();
        for(int i = 5;i < coordinates.length; ++i){
            System.out.print(coordinates[i].x + " " + coordinates[i].y + ", ");
        }
        System.out.println();
    }
}
