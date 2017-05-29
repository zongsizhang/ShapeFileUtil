package spatial;

import ShapeFileParse.ShpParseUtil;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by zongsizhang on 5/3/17.
 */
public class ShapeFileReader extends RecordReader<ShapeKey, BytesWritable> {

    private ShapeKey recordKey = null;

    private BytesWritable recordContent = null;

    private DataInputStream inputStream = null;

    public ShapeFileReader(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException
    {
        initialize(split, context);
    }

    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        ShpParseUtil.initializeGeometryFactory();
        FileSplit fileSplit = (FileSplit)split;
        long start = fileSplit.getStart();
        long end = start + fileSplit.getLength();
        int len = (int)fileSplit.getLength();
        Path filePath = fileSplit.getPath();
        FileSystem fileSys = filePath.getFileSystem(context.getConfiguration());
        FSDataInputStream inputStreamFS = fileSys.open(filePath);
        //byte[] wholeStream = new byte[len];
        inputStream = new DataInputStream(inputStreamFS);
        //IOUtils.readFully(inputStream, wholeStream, 0, len);
        //inputStream = new DataInputStream(new ByteArrayInputStream(wholeStream));
        ShpParseUtil.parseShapeFileHead(inputStream);
    }

    public boolean nextKeyValue() throws IOException, InterruptedException {
        if(ShpParseUtil.remainLength <= 0) return false;
        recordKey = new ShapeKey();
        recordContent = new BytesWritable();
        recordKey.setIndex(ShpParseUtil.parseRecordHeadID(inputStream));
        byte[] primitiveContent = ShpParseUtil.parseRecordPrimitiveContent(inputStream);
        recordContent.set(primitiveContent, 0, primitiveContent.length);
        return true;
    }

    public ShapeKey getCurrentKey() throws IOException, InterruptedException {
        return recordKey;
    }

    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return recordContent;
    }

    public float getProgress() throws IOException, InterruptedException {
        return (float)(ShpParseUtil.fileLength - ShpParseUtil.remainLength) / (float) ShpParseUtil.fileLength;
    }

    public void close() throws IOException {

    }
}
