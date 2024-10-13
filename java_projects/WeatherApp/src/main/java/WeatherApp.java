import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author valeriali
 * @project WeatherApp
 */
public class WeatherApp {

    static double lat = 52.37125;
    static double lon = 4.89388;
    static int limit = 7;

    private static final String API_URL = "https://api.weather.yandex.ru/v2/forecast?lat="
            + lat + "&lon=" + lon + "&limit=" + limit;
    private static final String API_KEY = "3b38470f-bc19-4cbf-9d15-425de4a42fe9";

    public static void main(String[] args) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Yandex-API-Key", API_KEY);

        // Создаем HttpEntity с заголовками
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        // Отправляем запрос и получаем ответ в формате JSON
        ResponseEntity<String> rawResponse = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);
        String jsonResponse = rawResponse.getBody();
        System.out.println(jsonResponse);

        ResponseEntity<WeatherResponse> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, WeatherResponse.class);

        // Получаем данные о погоде
        WeatherResponse weatherResponse = response.getBody();
        if (weatherResponse != null) {
            // Отдельно выводим текущую температуру
            int currentTemp = weatherResponse.getFact().getTemp();
            System.out.println("\nТекущая температура: " + currentTemp + "°C");

            // Вычисляем среднюю температуру за указанный период
            List<Forecast> forecasts = weatherResponse.getForecasts();
            double sumTemp = 0;
            int count = 0;

            for (Forecast forecast : forecasts) {
                int tempAvg = forecast.getParts().getDay().getTempAvg();
                sumTemp += tempAvg;
                count++;
                System.out.println("День " + count + " - Средняя температура: " + tempAvg + "°C");
            }

            if (count > 0) {
                double averageTemp = sumTemp / count;
                System.out.printf("\nСредняя температура за %d дней: %.2f°C\n", count, averageTemp);
            }
        } else {
            System.out.println("Не удалось получить данные о погоде.");
        }
    }

}
