package com.example.weather;

import com.example.weather.api.WeatherEndpoints;
import com.example.weather.api.WeatherServiceBuilder;
import com.example.weather.data.GeocodingResult;
import com.example.weather.data.WeatherResult;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {
    private final String coordsSanJoseJson = "[{\n" +
            "    \"name\": \"San Jose\",\n" +
            "    \"local_names\": {\n" +
            "        \"eo\": \"San-Joseo\",\n" +
            "        \"ru\": \"Сан-Хосе\",\n" +
            "        \"gl\": \"San Xosé\",\n" +
            "        \"vi\": \"San Jose\",\n" +
            "        \"es\": \"San José\",\n" +
            "        \"uk\": \"Сан-Хосе\",\n" +
            "        \"en\": \"San Jose\",\n" +
            "        \"am\": \"ሳን ሆዜ\",\n" +
            "        \"pt\": \"San José\",\n" +
            "        \"ar\": \"سان خوسيه\",\n" +
            "        \"zh\": \"聖荷西\",\n" +
            "        \"oc\": \"San José\"\n" +
            "    },\n" +
            "    \"lat\": 37.3361663,\n" +
            "    \"lon\": -121.890591,\n" +
            "    \"country\": \"US\",";

    private final String weatherSanJoseJson = "{\n" +
            "    \"coord\": {\n" +
            "        \"lon\": -121.8906,\n" +
            "        \"lat\": 37.3362\n" +
            "    },\n" +
            "    \"weather\": [{\n" +
            "        \"id\": 801,\n" +
            "        \"main\": \"Clouds\",\n" +
            "        \"description\": \"few clouds\",\n" +
            "        \"icon\": \"02d\"\n" +
            "    }],\n" +
            "    \"base\": \"stations\",\n" +
            "    \"main\": {\n" +
            "        \"temp\": 293.56,\n" +
            "        \"feels_like\": 293.22,\n" +
            "        \"temp_min\": 287.27,\n" +
            "        \"temp_max\": 297.04,\n" +
            "        \"pressure\": 1013,\n" +
            "        \"humidity\": 60\n" +
            "    },\n" +
            "    \"visibility\": 10000,\n" +
            "    \"wind\": {\n" +
            "        \"speed\": 9.26,\n" +
            "        \"deg\": 340\n" +
            "    },\n" +
            "    \"clouds\": {\n" +
            "        \"all\": 20\n" +
            "    },\n" +
            "    \"dt\": 1682292712,\n" +
            "    \"sys\": {\n" +
            "        \"type\": 2,\n" +
            "        \"id\": 2004102,\n" +
            "        \"country\": \"US\",\n" +
            "        \"sunrise\": 1682256137,\n" +
            "        \"sunset\": 1682304549\n" +
            "    },\n" +
            "    \"timezone\": -25200,\n" +
            "    \"id\": 5392171,\n" +
            "    \"name\": \"San Jose\",\n" +
            "    \"cod\": 200\n" +
            "}";

    WeatherEndpoints weatherService;

    @Before
    public void setUp() {
        weatherService = new WeatherServiceBuilder().buildService(WeatherEndpoints.class);
    }

    @After
    public void tearDown() {
        weatherService = null;
    }

    @Test
    public void testFetchWeather() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody(weatherSanJoseJson));
        mockWebServer.start();

        Response<WeatherResult> response = weatherService.getWeather(Constants.WEATHER_API_KEY, "37.336166", "-121.890594", "imperial").execute();
        Assert.assertEquals("wrong coords", 37.3362f, response.body().getCoord().getLat(), 0);
        Assert.assertEquals("wrong coords", -121.8906f, response.body().getCoord().getLon(), 0);

        Assert.assertEquals("wrong weather id", 801, response.body().getWeather()[0].getId());
        Assert.assertEquals("wrong weather main description", "Clouds", response.body().getWeather()[0].getMain());
        Assert.assertEquals("wrong weather description", "few clouds", response.body().getWeather()[0].getDescription());
        Assert.assertEquals("wrong weather icon", "02d", response.body().getWeather()[0].getIcon());

        Assert.assertEquals("wrong weather temperature", 68.49f, response.body().getMain().getTemp(), 0);
        Assert.assertEquals("wrong weather feels like temperature", 67.86f, response.body().getMain().getFeels_like(), 0);
        Assert.assertEquals("wrong weather max temperature", 73.92f, response.body().getMain().getTemp_max(), 0);
        Assert.assertEquals("wrong weather minimum temperature", 56.98f, response.body().getMain().getTemp_min(), 0);
        Assert.assertEquals("wrong weather pressure", 1013.0f, response.body().getMain().getPressure(), 0);
        Assert.assertEquals("wrong weather humidity", 60.0f, response.body().getMain().getHumidity(), 0);
    }

    @Test
    public void testGeocoding() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody(coordsSanJoseJson));
        mockWebServer.start();

        Response<GeocodingResult[]> response = weatherService.getCoordinates("san jose", Constants.WEATHER_API_KEY).execute();
        Assert.assertEquals("Incorrect city name", "San Jose", response.body()[0].getName());
        Assert.assertEquals("Incorrect latitude", 37.336166f, response.body()[0].getLat(), 0);
        Assert.assertEquals("Incorrect longitude", -121.890594f, response.body()[0].getLon(), 0);
        Assert.assertEquals("Incorrect country", "US", response.body()[0].getCountry());
    }
}
