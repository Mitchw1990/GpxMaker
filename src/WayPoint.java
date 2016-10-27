/**
 * Created by Mitch on 8/4/2016.
 */
public class WayPoint {

    String latitude;
    String longitude;
    String name;
    String desc;

    WayPoint(String latitude, String longitude, String name, String desc){
        this.name = name;
        this.latitude =latitude;
        this.longitude = longitude;
        this.desc = desc;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
