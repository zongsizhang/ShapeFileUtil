package spatial;

import ShapeFileParse.ShapeFileConst;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;
import shapes.PrimitiveShapeWritable;
import shapes.ShapeKey;
import shapes.ShapeWritable;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.Serializable;

import static ShapeFileParse.ShpParseUtil.currentTokenType;

/**
 * Created by zongsizhang on 5/23/17.
 */
public class ShapeRDD implements Serializable{

    JavaRDD<ShapeWritable> shapeWritableRDD;

    JavaPairRDD<ShapeKey, BytesWritable> primitiveShapeRDD;

    public ShapeRDD(String filePath, JavaSparkContext sparkContext){
        JavaPairRDD<ShapeKey, PrimitiveShapeWritable> shapePrimitiveRdd = sparkContext.newAPIHadoopFile(
                filePath,
                ShapeInputFormat.class,
                ShapeKey.class,
                PrimitiveShapeWritable.class,
                new Configuration()
        );

        System.out.println("============shape count================" + shapePrimitiveRdd.count());
        JavaRDD<ShapeWritable> shapeRDD = shapePrimitiveRdd.map(PrimitiveToShape);
        //System.out.println("============shapeWritable count================" + shapeRDD.count());
        //shapeRDD.foreach(PrintShape);

    }

    public static final Function<Tuple2<ShapeKey, PrimitiveShapeWritable>, ShapeWritable> PrimitiveToShape
            = new Function<Tuple2<ShapeKey, PrimitiveShapeWritable>, ShapeWritable>(){
        public ShapeWritable call(Tuple2<ShapeKey, PrimitiveShapeWritable> primitiveTuple) throws Exception {
            Class<?> shapeClass = Class.forName(ShapeFileConst.typeClassNamePairs.get(currentTokenType));
            ShapeWritable shape = (ShapeWritable) shapeClass.newInstance();
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(primitiveTuple._2().getPrimitiveRecord().getBytes()));
            shape.parseShape(inputStream);
            return shape;
        }
    };

    public static final VoidFunction<ShapeWritable> PrintShape = new VoidFunction<ShapeWritable>() {
        public void call(ShapeWritable shapeWritable) throws Exception {
            shapeWritable.printShape();
        }
    };

}
