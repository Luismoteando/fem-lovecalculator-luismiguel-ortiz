package es.upm.miw.lovecalculator;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import es.upm.miw.lovecalculator.models.LoveCalculator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActividadPrincipal extends Activity {

    private static final String API_BASE_URL = "https://love-calculator.p.rapidapi.com";

    private static final String LOG_TAG = "MiW";

    private TextView tvRespuesta;
    private EditText etFirstLover;
    private EditText etSecondLover;
    private SeekBar sbPercentage;

    private LoveCalculatorRESTAPIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        tvRespuesta = findViewById(R.id.tvRespuesta);
        etFirstLover = findViewById(R.id.etFirstLover);
        etSecondLover = findViewById(R.id.etSecondLover);
        sbPercentage = findViewById(R.id.sbPercentage);

        // btb added for retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(LoveCalculatorRESTAPIService.class);
    }


    //
    // A este método se llama cada vez que se hace click sobre el corazón
    // Ver  activity_actividad_principal.xml
    //
    public void getPercentage(View v) {
        String firstName = etFirstLover.getText().toString();
        String secondName = etSecondLover.getText().toString();
        Log.i(LOG_TAG, "getPercentage => fname=" + firstName + ", sname=" + secondName);
        tvRespuesta.setText("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sbPercentage.setProgress(0, true);
            sbPercentage.setThumb(getDrawable(R.mipmap.ic_question_foreground));
        }

        // Realiza la llamada por nombre
        Call<LoveCalculator> call_async = apiService.getPercentage(firstName, secondName);

        // Asíncrona
        call_async.enqueue(new Callback<LoveCalculator>() {


            @Override
            public void onResponse(Call<LoveCalculator> call, Response<LoveCalculator> response) {
                LoveCalculator loveCalculator = response.body();
                if (null != loveCalculator) {
                    tvRespuesta.append(loveCalculator.getFname()
                            + " & " + loveCalculator.getSname()
                            + " have \n" + loveCalculator.getPercentage()
                            + "% of compatibility.\n"
                            + loveCalculator.getResult() + "\n\n");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        sbPercentage.setProgress(Integer.parseInt(loveCalculator.getPercentage()), true);
                        int percentage = Integer.parseInt(loveCalculator.getPercentage());
                        if (percentage >= 0 && percentage < 25) {
                            sbPercentage.setThumb(getDrawable(R.mipmap.ic_broken_heart_foreground));
                        } else if (percentage >= 25 && percentage < 50) {
                            sbPercentage.setThumb(getDrawable(R.mipmap.ic_handshake_foreground));
                        } else if (percentage >= 50 && percentage < 75) {
                            sbPercentage.setThumb(getDrawable(R.mipmap.ic_heart_foreground));
                        } else if (percentage >= 75 && percentage < 100) {
                            sbPercentage.setThumb(getDrawable(R.mipmap.ic_wedding_foreground));
                        }
                    }
                    Log.i(LOG_TAG, "getPercentage => respuesta=" + loveCalculator);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        sbPercentage.setProgress(0, true);
                        sbPercentage.setThumb(getDrawable(R.mipmap.ic_question_foreground));
                    }
                    tvRespuesta.setText(getString(R.string.strError));
                    Log.i(LOG_TAG, getString(R.string.strError));
                }
            }


            @Override
            public void onFailure(Call<LoveCalculator> call, Throwable t) {
                Toast.makeText(
                        getApplicationContext(),
                        "ERROR: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                Log.e(LOG_TAG, t.getMessage());
            }
        });


        // Síncrona... no aquí => NetworkOnMainThreadException
//        Call<Country> call_sync = apiService.getCountryByName("spain");
//        try {
//            Country country = call_sync.execute().body();
//            Log.i(LOG_TAG, "SYNC => " + country.toString());
//        } catch (IOException e) {
//            Log.e(LOG_TAG, e.getMessage());
//        }
    }
}