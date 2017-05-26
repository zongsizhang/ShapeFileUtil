package shapes;

import ShapeFileParse.ShapeTypeNotMatchException;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by zongsizhang on 5/11/17.
 */
public abstract class ShapeWritable implements Writable {

    /**
     *
     * @param dataOutput
     * @throws IOException
     */
    public void write(DataOutput dataOutput) throws IOException {

    }

    /**
     *
     * @param dataInput
     * @throws IOException
     */
    public void readFields(DataInput dataInput) throws IOException {

    }

    /**
     * for future feature, map shape to possible formats, for example, list of points
     */
    public abstract void flatmapShape();

    /**
     *
     * @param inputStream   input source from content sourse
     * @throws IOException
     */
    public abstract void parseShape(DataInputStream inputStream) throws IOException, ShapeTypeNotMatchException;

    /**
     * for testing correctness, print content of shape.
     */
    public abstract void printShape();
}
