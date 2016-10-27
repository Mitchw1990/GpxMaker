import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Mitch on 8/4/2016.
 */
public class GpxMaker {
    public static void main(String[] args){
        GpxMaker gpx = new GpxMaker();
        ArrayList<WayPoint> list = new ArrayList<>();
        try {
            list = gpx.importSpreadsheet();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gpx.writeGpx(list);
    }
    public ArrayList<WayPoint> importSpreadsheet() throws IOException {
        ArrayList<WayPoint> wayPoints = new ArrayList<>();
        String spreadsheetPath = System.getProperty("user.dir") + File.separatorChar + "Properties.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(spreadsheetPath));

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet spreadsheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = spreadsheet.iterator();
        rowIterator.next();
        XSSFRow currentRow = null;
        while (rowIterator.hasNext()){
            currentRow = (XSSFRow)rowIterator.next();

            String id = currentRow.getCell(0).getRawValue();
            String name = currentRow.getCell(1).toString().trim().replace("&", " ")
                    + " / "  +
                    id;
            String fullName = currentRow.getCell(2).toString().trim().replace("&", " ");
            String lat = currentRow.getCell(3).toString().trim();
            String lon = currentRow.getCell(4).toString().trim();
            WayPoint wp = new WayPoint(lat, lon, name, fullName);
            wayPoints.add(wp);
        }
        inputStream.close();
        return wayPoints;
    }

    public void writeGpx(ArrayList<WayPoint> points) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("Route.gpx"), "UTF-8"))) {
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n" +
                    "\n" +
                    "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"InspectorADE\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">\n" +
                    "<metadata>\n" +
                    "\t\t<name>"+ dateString +"</name>\n" +
                    "\t\t<link href=\"http://www.garmin.com\">\n" +
                    "\t\t  <text>Garmin International</text>\n" +
                    "\t\t</link>\n" +
                    "\t\t<time>2016-10-21T01:59:18-05:00</time>\n" +
                    "   </metadata>");
            for (WayPoint p : points) {
                out.write("<wpt lat=\"" + p.getLatitude() + "\" lon=\"" + p.getLongitude() + "\">\n" +
                        "  <name>" + p.getName() + "</name>\n" +
                        "<desc>" + p.getDesc() + "</desc>\n" +
                        "\t\t\t\t<type>2</type>" +
                        "</wpt>\n");
            }
                out.write("<rte>\n" +
                        "<name>Route</name>\n\n");
            for(WayPoint p : points){
                out.write("<rtept lat=\"" + p.getLatitude() + "\" lon=\"" + p.getLongitude() + "\">\n" +
                        "  <name>" + p.getName() + "</name>\n" +
                        "</rtept>\n");
            }
            out.write("</rte>\n" +
                    "\t\t\t</gpx>");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
