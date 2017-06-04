package shapes;

import ShapeFileParse.DbfParseUtil;
import ShapeFileParse.FieldDescriptor;
import ShapeFileParse.ShapeTypeNotMatchException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import javax.management.Descriptor;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by zongsizhang on 5/11/17.
 */
public abstract class ShapeWritable implements Writable {

    private Text attributes = null;

    private int id = 0;

    /**
     *
     * @param dataOutput
     * @throws IOException
     */
    public void write(DataOutput dataOutput) throws IOException {

    }

    public void readFields(DataInput dataInput) throws IOException {

    }

    public void parseAttributes(DataInputStream inputStream) throws IOException{
        byte[] delimiter = {','};
        attributes = new Text();
        for(FieldDescriptor descriptor : DbfParseUtil.infoBundle.fieldDescriptors){
            byte[] fldBytes = new byte[descriptor.getFieldLength()];
            inputStream.readFully(fldBytes);
            switch(descriptor.getFieldType()){
                case 'C':
                {
                    attributes.append(fldBytes,0,fldBytes.length);
                    attributes.append(delimiter,0,1);
                    break;
                }
                case 'N' | 'F':
                {
                    attributes.append(fldBytes,0,fldBytes.length);
                    attributes.append(delimiter,0,1);
                    break;
                }
            }

        }
    }

    public void printAttribute(){
        System.out.println(attributes.toString());
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
