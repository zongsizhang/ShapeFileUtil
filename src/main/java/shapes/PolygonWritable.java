package shapes;

import ShapeFileParse.ShapeParseUtil;
import ShapeFileParse.ShapeTypeNotMatchException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import java.io.DataInputStream;
import java.io.IOException;

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
        ShapeParseUtil.validateShapeType(inputStream);
        polygon = ShapeParseUtil.parsePolygon(inputStream);
    }

    public void printShape() {
        System.out.println("==============polygon===========");
        Coordinate[] coordinates = polygon.getCoordinates();
        for(Coordinate coordinate : coordinates){
            System.out.println(coordinate.x + " " + coordinate.y);
        }
    }
}
