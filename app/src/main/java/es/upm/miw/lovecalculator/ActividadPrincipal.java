package es.upm.miw.lovecalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import es.upm.miw.lovecalculator.models.LoveCalculator;
import es.upm.miw.lovereporter.LoveAlertActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActividadPrincipal extends Activity {

    private static final String API_BASE_URL = "https://love-calculator.p.rapidapi.com";
    private static final String LOG_TAG = "MiW";
    private static final int RC_SIGN_IN = 2018;

    private LoveCalculatorRESTAPIService apiService;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLoveCalculatorDatabaseReference;

    private int percentage;

    private TextView tvRespuesta;
    private EditText etFirstLover;
    private EditText etSecondLover;
    private SeekBar sbPercentage;

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

        // btb Get instance of Firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mLoveCalculatorDatabaseReference = mFirebaseDatabase.getReference().child("lovecalculator");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    CharSequence username = user.getDisplayName();
                    Toast.makeText(ActividadPrincipal.this, getString(R.string.firebase_user_fmt, username), Toast.LENGTH_LONG).show();
                    Log.i(LOG_TAG, "onAuthStateChanged() " + getString(R.string.firebase_user_fmt, username));
                } else {
                    // user is signed out
                    startActivityForResult(
                            // Get an instance of AuthUI based on the default app
                            AuthUI.getInstance().
                                    createSignInIntentBuilder().
                                    setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    )).
                                    setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                                    build(),
                            RC_SIGN_IN
                    );
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loveHistoric:
                startActivity(new Intent(this, ActividadLoveHistoric.class));
                Log.i(LOG_TAG, getString(R.string.loveHistoric));
                Toast.makeText(this, R.string.loveHistoric, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sendReport:
                Intent intent = new Intent(this, LoveAlertActivity.class);
                intent.putExtra("percentage", percentage);
                startService(intent);
                Log.i(LOG_TAG, getString(R.string.report_sent));
                Toast.makeText(this, R.string.report_sent, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                mFirebaseAuth.signOut();
                Log.i(LOG_TAG, getString(R.string.signed_out));
                Toast.makeText(this, R.string.signed_out, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    percentage = Integer.parseInt(loveCalculator.getPercentage());
                    tvRespuesta.append(loveCalculator.getFname()
                            + " & " + loveCalculator.getSname()
                            + " have \n" + loveCalculator.getPercentage()
                            + "% of compatibility.\n"
                            + loveCalculator.getResult() + "\n\n");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        sbPercentage.setProgress(percentage, true);
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
                    mLoveCalculatorDatabaseReference.push().setValue(loveCalculator);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.signed_in, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_in));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.signed_cancelled, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_cancelled));
                finish();
            }
        }
    }
}