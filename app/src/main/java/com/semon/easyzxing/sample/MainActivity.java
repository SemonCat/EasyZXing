package com.semon.easyzxing.sample;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.semon.easyzxing.QRCodeReaderView;


public class MainActivity extends ActionBarActivity {

    private QRCodeReaderView qrCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrCodeView = (QRCodeReaderView) findViewById(R.id.QRCodeView);

        qrCodeView.setListener(new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void OnQRCodeRead(final String text) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void cameraNotFound() {

            }

            @Override
            public void QRCodeNotFoundOnCamImage() {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            new AlertDialog.Builder(this).setTitle("test").setMessage("test").show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
