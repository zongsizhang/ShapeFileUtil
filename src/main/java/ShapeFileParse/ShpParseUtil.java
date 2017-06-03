package ShapeFileParse;

import com.vividsolutions.jts.geom.*;
import org.apache.commons.io.EndianUtils;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by zongsizhang on 5/3/17.
 */
public class ShpParseUtil implements ShapeFileConst{
    public static int currentTokenType = 0;
    public static long fileLength = 0;
    public static long remainLength = 0;

    public static GeometryFactory geometryFactory = null;

    public static void parseShapeFileHead(DataInputStream inputStream)
    throws IOException
    {
        int fileCode = inputStream.readInt();
        inputStream.skip(HEAD_EMPTY_NUM * INT_LENGTH);
        fileLength = 16 * inputStream.readInt() - HEAD_FILE_LENGTH_16BIT * 16;
        remainLength = fileLength;

        System.out.println("=============remainLength = " + remainLength);

        int fileVersion = EndianUtils.swapInteger(inputStream.readInt());

        currentTokenType = EndianUtils.swapInteger(inputStream.readInt());
        System.out.println("======token type===== " + currentTokenType);

        inputStream.skip(HEAD_BOX_NUM * DOUBLE_LENGTH);
    }

    public static void initializeGeometryFactory(){
        geometryFactory = new GeometryFactory();
    }

    public static byte[] parseRecordPrimitiveContent(DataInputStream inputStream) throws IOException{
        int contentLength = inputStream.readInt();
        long recordLength = 16 * (contentLength + 4);
        remainLength -= recordLength;
        byte[] contentArray = new byte[contentLength * 2];
        inputStream.readFully(contentArray,0,contentArray.length);
        return contentArray;
    }

    public static int parseRecordHeadID(DataInputStream inputStream) throws IOException{
        int id = inputStream.readInt();
        return id;
    }

    public static void validateShapeType(DataInputStream inputStream)
            throws IOException, ShapeTypeNotMatchException
    {
        int recordTokenType = EndianUtils.swapInteger(inputStream.readInt());
        if(recordTokenType != currentTokenType) throw new ShapeTypeNotMatchException();
    }

    public static Point parsePoint(DataInputStream inputStream)
            throws IOException
    {
        double x = EndianUtils.swapDouble(inputStream.readDouble());
        double y = EndianUtils.swapDouble(inputStream.readDouble());
        Point point = geometryFactory.createPoint(new Coordinate(x, y));
        return point;
    }


    /**
     * This is for parsing records with token type = 5(Polygon). It will return a Polygon object with a MBR as bounding box.
     * @param inputStream
     * @return
     * @throws IOException
     * @throws ShapeTypeNotMatchException
     */
    public static Polygon parsePolygon(DataInputStream inputStream)
            throws IOException, ShapeTypeNotMatchException
    {
        LinearRing boundBox = parseBoundingBox(inputStream);
        int numRings = EndianUtils.swapInteger(inputStream.readInt());
        int numPoints = EndianUtils.swapInteger(inputStream.readInt());
        int[] ringsOffsets = new int[numRings+1];
        LinearRing[] holes = new LinearRing[numRings];
        for(int i = 0;i < numRings; ++i){
            ringsOffsets[i] = EndianUtils.swapInteger(inputStream.readInt());
        }
        ringsOffsets[numRings] = numPoints;

        for(int i = 0;i < numRings; ++i){
            int readScale = ringsOffsets[i+1] - ringsOffsets[i];
            Coordinate[] coordinates = new Coordinate[readScale];
            for(int j = 0;j < readScale; ++j){
                coordinates[j] = parsePoint(inputStream).getCoordinate();
            }
            holes[i] = geometryFactory.createLinearRing(coordinates);
        }
        Polygon polygon = geometryFactory.createPolygon(boundBox, holes);
        return polygon;
    }

    /**
     * This is for parsing objects with shape type = MultiPoint
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static Point[] parseMultiPoints(DataInputStream inputStream)
            throws IOException
    {
        int numPoints = EndianUtils.swapInteger(inputStream.readInt());
        Point[] points = new Point[numPoints];
        for(int i = 0;i < numPoints; ++i){
            points[i] = parsePoint(inputStream);
        }
        return points;
    }

    /**
     * This is for parsing objects with shape type = PolyLine
     * @param inputStream
     * @return
     */
    public static LineString[] parsePolyLine(DataInputStream inputStream)
    throws IOException
    {
        int numParts = EndianUtils.swapInteger(inputStream.readInt());
        int numPoints = EndianUtils.swapInteger(inputStream.readInt());
        int[] stringOffsets = new int[numParts+1];
        for(int i = 0;i < numParts; ++i){
            stringOffsets[i] = EndianUtils.swapInteger(inputStream.readInt());
        }
        stringOffsets[numParts] = numPoints;
        LineString[] lines = new LineString[numParts];
        for(int i = 0;i < numParts; ++i){
            int readScale = stringOffsets[i+1] - stringOffsets[i];
            Coordinate[] coordinates = new Coordinate[readScale];
            for(int j = 0;j < readScale; ++j){
                coordinates[j] = parsePoint(inputStream).getCoordinate();
            }
            lines[i] = geometryFactory.createLineString(coordinates);
        }
        return lines;
    }
    /**
     * This is for parsing bounding box for shapes with Box.
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static LinearRing parseBoundingBox(DataInputStream inputStream)
            throws IOException
    {
        double xMin = EndianUtils.swapDouble(inputStream.readDouble());
        double yMin = EndianUtils.swapDouble(inputStream.readDouble());
        double xMax = EndianUtils.swapDouble(inputStream.readDouble());
        double yMax = EndianUtils.swapDouble(inputStream.readDouble());

        Coordinate[] boundArray = {
                new Coordinate(xMin, yMin),
                new Coordinate(xMin, yMax),
                new Coordinate(xMax, yMax),
                new Coordinate(xMax, yMin),
                new Coordinate(xMin, yMin)
        };
        return geometryFactory.createLinearRing(boundArray);
    }


}
