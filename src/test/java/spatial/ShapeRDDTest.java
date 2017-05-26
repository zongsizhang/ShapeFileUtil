package spatial;

import junit.framework.TestCase;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zongsizhang on 5/23/17.
 */
public class ShapeRDDTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testLoadShapeFile() throws IOException{
        String InputLocation = "/Users/zongsizhang/IdeaProjects/operation/src/main/resources/map.shp";
        File file = new File(InputLocation);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("url", file.toURI().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore
                .getFeatureSource(typeName);
        Filter filter = Filter.INCLUDE;
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
        FeatureIterator<SimpleFeature> features = collection.features();
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            System.out.print(feature.getID());
            System.out.print(": ");
            System.out.println(feature.getDefaultGeometryProperty().getValue());
        }
    }

    public void tearDown() throws Exception {
    }

}