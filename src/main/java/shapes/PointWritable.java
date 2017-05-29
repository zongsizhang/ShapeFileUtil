package shapes;

import ShapeFileParse.ShpParseUtil;
import ShapeFileParse.ShapeTypeNotMatchException;
import com.vividsolutions.jts.geom.Point;

import java.io.DataInputStream;
import java.io.IOException;


/**
 * Created by zongsizhang on 5/11/17.
 */
public class PointWritable extends ShapeWritable {

    Point point = null;

    public PointWritable(){

    }

    public void mapPoints() {

    }

    /**
     *
     * @param inputStream   the input   stream from ShapeFile
     * @throws IOException
     */
    public void parseShape(DataInputStream inputStream)
            throws IOException, ShapeTypeNotMatchException
    {
        ShpParseUtil.validateShapeType(inputStream);
        point = ShpParseUtil.parsePoint(inputStream);
    }

    public void flatmapShape() {

    }

    public void printShape() {
        System.out.println(point.getX() + " , " + point.getY());
    }
}
