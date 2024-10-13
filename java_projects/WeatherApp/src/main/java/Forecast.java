//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author valeriali
 * @project WeatherApp
 */

public class Forecast {

    private String date;
    private Parts parts;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Parts getParts() {
        return parts;
    }

    public void setParts(Parts parts) {
        this.parts = parts;
    }
}
