package spatial;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;


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



