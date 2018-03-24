package io.tipper.tipper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import io.tipper.tipper.components.QrScanner;

public class SendActivity extends Activity {
    private Context context;
    private QrScanner scanner;
    private LinearLayout layout;

    public SendActivity(){}

    public SendActivity(Context context){
        this.context = context;
    }

    public Intent intent(){
        return new Intent(context, this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_laayout);
        layout = findViewById(R.id.qr_scanner_layout);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedinstaceState){
        super.onPostCreate(savedinstaceState);
        scanner = new QrScanner(this);
        scanner.init();
        layout.addView(scanner.getView());
    }
}
