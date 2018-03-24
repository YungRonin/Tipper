package io.tipper.tipper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TipperHome extends AppCompatActivity {
    RecieveActivity recieveActivity;
    SendActivity sendActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipper_home);
        recieveActivity = new RecieveActivity(this);
        sendActivity = new SendActivity(this);

        Button recieveButton = findViewById(R.id.reveive_button);
        recieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(recieveActivity.intent());
            }
        });

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(sendActivity.intent());
            }
        });
    }
}
