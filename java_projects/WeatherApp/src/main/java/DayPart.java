//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author valeriali
 * @project WeatherApp
 */

public class DayPart {

    // Аннотация помогает сопоставить поле temp_avg из JSON с tempAvg в Java
    @JsonProperty("temp_avg")
    private int tempAvg;

    public int getTempAvg() {
        return tempAvg;
    }

    public void setTempAvg(int tempAvg) {
        this.tempAvg = tempAvg;
    }
}
