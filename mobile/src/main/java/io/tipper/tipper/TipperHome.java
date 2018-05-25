package io.tipper.tipper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletUtils;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import io.tipper.tipper.app.database.Database;
import io.tipper.tipper.app.database.Wallet;
import io.tipper.tipper.components.WalletPath;

public class TipperHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipper_home);


        String WalletFilePath;
        WalletFilePath = new WalletPath().getPath(this);
        if(WalletFilePath == null) {
            WalletFilePath = getFilesDir().getPath().concat("/" + createWallet());

            final String finalPath = WalletFilePath;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Wallet wallet =new Wallet();
                    wallet.setWalletFilePath(finalPath);
                    Database.getDatabase(TipperHome.this.getApplication()).dao().insertSingleWallet(wallet);
                }
            }) .start();
        }


        Button recieveButton = findViewById(R.id.reveive_button);
        recieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new RecieveActivity().intent(TipperHome.this));
            }
        });

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new SendActivity().intent(TipperHome.this));
            }
        });

        Button historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new TransactionListActivity().intent(TipperHome.this));
            }
        });



    }

    private String createWallet(){
        try {
            return WalletUtils.generateNewWalletFile("atestpasswordhere", getFilesDir(), false);
        }
        catch (IOException e){
            Log.e(getClass().getName(), "exception " + e);
        }
        catch(InvalidAlgorithmParameterException e){
            Log.e(getClass().getName(), "exception " + e);
        }
        catch (NoSuchAlgorithmException e){
            Log.e(getClass().getName(), "exception " + e);
        }
        catch (NoSuchProviderException e){
            Log.e(getClass().getName(), "exception " + e);
        }
        catch (CipherException e){
            Log.e(getClass().getName(), "exception " + e);
        }
        return null;
    }
}
