package shapes;

import ShapeFileParse.ShapeTypeNotMatchException;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by zongsizhang on 5/18/17.
 */
public class PolyLineWritable extends ShapeWritable {

    LinearRing boundBox = null;

    LineString[] lines = null;

    public void flatmapShape() {

    }

    public void parseShape(DataInputStream inputStream) throws IOException, ShapeTypeNotMatchException {

    }

    public void printShape() {

    }
}
