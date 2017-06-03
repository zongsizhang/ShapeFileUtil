package spatial;

import org.apache.commons.io.FilenameUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import shapes.PrimitiveShapeWritable;
import shapes.ShapeKey;

import java.io.IOException;

/**
 * Created by zongsizhang on 6/1/17.
 */
public class CombineShapeReader extends RecordReader<ShapeKey, PrimitiveShapeWritable> {

    /** id of input path of .shp file */
    private FileSplit shpSplit = null;

    /** id of input path of .shx file */
    private FileSplit shxSplit = null;

    /** id of input path of .dbf file */
    private FileSplit dbfSplit = null;

    /** RecordReader for .shp file */
    private ShapeFileReader shapeFileReader = null;

    /** RecordReader for .dbf file */
    private DbfFileReader dbfFileReader = null;

    /** suffix of attribute file */
    private final static String DBF_SUFFIX = "dbf";

    /** suffix of shape record file */
    private final static String SHP_SUFFIX = "shp";

    /** suffix of index file */
    private final static String SHX_SUFFIX = "shx";

    /** value of current record */
    private PrimitiveShapeWritable value = null;

    /** flag of whether .dbf exists */
    private boolean hasDbf = false;

    /** flag of whether having next .dbf record */
    private boolean hasNextDbf = false;

    /**
     * cut the combined split into FileSplit for .shp, .shx and .dbf
     * @param split
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    public void initialize(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException
    {
        CombineFileSplit fileSplit = (CombineFileSplit)split;
        Path[] paths = fileSplit.getPaths();
        for(int i = 0;i < paths.length; ++i){
            String suffix = FilenameUtils.getExtension(paths[i].toString());
            if(suffix.equals(SHP_SUFFIX)) shpSplit = new FileSplit(paths[i], fileSplit.getOffset(i), fileSplit.getLength(i), fileSplit.getLocations());
            else if(suffix.equals(SHX_SUFFIX)) shxSplit = new FileSplit(paths[i], fileSplit.getOffset(i), fileSplit.getLength(i), fileSplit.getLocations());
            else if(suffix.equals(DBF_SUFFIX)) dbfSplit = new FileSplit(paths[i], fileSplit.getOffset(i), fileSplit.getLength(i), fileSplit.getLocations());
        }
        // if shape file doesn't exists, throw an IOException
        if(shpSplit == null) throw new IOException("Can't find .shp file.");
        else{
            shapeFileReader = new ShapeFileReader();
            shapeFileReader.initialize(shpSplit, context);
        }
        if(dbfSplit != null){
            dbfFileReader = new DbfFileReader();
            dbfFileReader.initialize(dbfSplit, context);
            hasDbf = true;
        }else hasDbf = false;

    }

    public boolean nextKeyValue() throws IOException, InterruptedException {
        //only .shp mode
        if(!hasDbf) return shapeFileReader.nextKeyValue();
        // with dbf, check consistency first
        boolean hasNextShp = shapeFileReader.nextKeyValue();
        hasNextDbf = dbfFileReader.nextKeyValue();
        //some dbf records lost
        if(hasNextShp && !hasNextDbf){
            Exception e = new Exception("shape record loses attributes in .dbf file at ID=" + shapeFileReader.getCurrentKey().getIndex());
            e.printStackTrace();
        }else if(!hasNextShp && hasNextDbf){
            Exception e = new Exception("Redundant attributes in .dbf exists");
            e.printStackTrace();
        }
        return hasNextShp;
    }

    public ShapeKey getCurrentKey() throws IOException, InterruptedException {
        return shapeFileReader.getCurrentKey();
    }

    public PrimitiveShapeWritable getCurrentValue() throws IOException, InterruptedException {
        value = new PrimitiveShapeWritable();
        value.setPrimitiveRecord(shapeFileReader.getCurrentValue());
        if(hasDbf && hasNextDbf) value.setPrimitiveAttribute(dbfFileReader.getCurrentValue());
        return value;
    }

    public float getProgress() throws IOException, InterruptedException {
        return shapeFileReader.getProgress();
    }

    public void close() throws IOException {
        shapeFileReader.close();
    }
}
