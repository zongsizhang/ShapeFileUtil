package spatial;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import shapes.PrimitiveShapeWritable;
import shapes.ShapeKey;

import java.io.IOException;

/**
 * Created by zongsizhang on 5/3/17.
 */
public class ShapeInputFormat extends CombineFileInputFormat<ShapeKey, PrimitiveShapeWritable> {
    public RecordReader<ShapeKey, PrimitiveShapeWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException {
        return new CombineShapeReader();
    }

    @Override
    protected boolean isSplitable(JobContext context, Path file) {
        return false;
    }
}

