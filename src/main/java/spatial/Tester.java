package spatial;

import ShapeFileParse.ShapeParseUtil;
import shapes.ShapeWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;


/**
 * Created by zongsizhang on 4/28/17.
 */
public class Tester {
    public static void main(String[] args){

        final SparkConf sparkConf = new SparkConf().setAppName("spatialshapefile");
        final JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        ShapeRDD shapeRDD = new ShapeRDD(args[0], sparkContext);
    }
}

//maven based



