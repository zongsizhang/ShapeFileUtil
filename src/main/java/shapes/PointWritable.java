package shapes;

import ShapeFileParse.ShapeFileConst;
import ShapeFileParse.ShapeParseUtil;
import ShapeFileParse.ShapeTypeNotMatchException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.io.EndianUtils;
import org.geotools.data.shapefile.shp.ShapeType;
import shapes.ShapeWritable;

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
        ShapeParseUtil.validateShapeType(inputStream);
        point = ShapeParseUtil.parsePoint(inputStream);
    }

    public void flatmapShape() {

    }

    public void printShape() {
        System.out.println(point.getX() + " , " + point.getY());
    }
}
