package br.com.flygowmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import br.com.flygowmobile.database.RepositoryScript;


public class SplashScreen extends Activity implements Runnable {

    private final int DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Toast.makeText(this, "Aguarde...", Toast.LENGTH_SHORT).show();
        Handler h = new Handler();
        h.postDelayed(this, DELAY);

    }

    @Override
    public void run() {
        RepositoryScript repository = new RepositoryScript(this);
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }




}
