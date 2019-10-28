package es.upm.miw.lovecalculator;

import es.upm.miw.lovecalculator.models.LoveCalculator;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


@SuppressWarnings("Unused")
interface LoveCalculatorRESTAPIService {
    @Headers({"x-rapidapi-host: love-calculator.p.rapidapi.com",
            "x-rapidapi-key: 3d20a0ee03msh8bae264b7389687p185ac6jsn03674d22bf8a"})

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    // https://love-calculator.p.rapidapi.com/getPercentage
    @GET("/getPercentage")
    Call<LoveCalculator> getPercentage(@Query("fname") String fname, @Query("sname") String sname);
}
