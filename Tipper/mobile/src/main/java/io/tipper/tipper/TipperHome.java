package io.tipper.tipper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gani.lib.logging.GLog;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import io.tipper.tipper.app.database.MyDbValue;

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

//        Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
//        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
//        String clientVersion = web3ClientVersion.getWeb3ClientVersion();

//        Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
//        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
//        String clientVersion = web3ClientVersion.getWeb3ClientVersion();


        Web3j web3 = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/tQmR2iidoG7pjW1hCcCf"));  // defaults to http://localhost:8545/
        try {
            Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();

//            Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            GLog.t(getClass(), "CLIENT: " + clientVersion);

        }
//        catch (IOException e) {
//            GLog.e(getClass(), "Failed", e);
//        }
        catch (InterruptedException e) {
            GLog.e(getClass(), "Failed", e);
        }
        catch (ExecutionException e) {
            GLog.e(getClass(), "Failed", e);
        }
    }
}
