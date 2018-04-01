package io.tipper.tipper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.gani.lib.http.GHttpResponse;
import com.gani.lib.logging.GLog;
import com.gani.lib.ui.view.GButton;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import io.tipper.tipper.app.database.MyDbValue;
import io.tipper.tipper.components.QrScanner;

import static io.tipper.tipper.app.constants.Keys.DB_FILE_PATH;

public class SendActivity extends Activity {
    private Context context;
    private QrScanner scanner;
    private LinearLayout layout;

    public SendActivity() {
    }

    public SendActivity(Context context) {
        this.context = context;
    }

    public Intent intent() {
        return new Intent(context, this.getClass());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_laayout);
        layout = findViewById(R.id.qr_scanner_layout);
        layout.addView(new GButton(this).text("Send").onClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncSendTask().execute();
            }
        }));
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedinstaceState) {
        super.onPostCreate(savedinstaceState);
        scanner = new QrScanner(this);
        scanner.init();
        layout.addView(scanner.getView());
    }

    private void send() {

        Web3j web3 = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/tQmR2iidoG7pjW1hCcCf"));  // defaults to http://localhost:8545/
        try {
            Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
//            Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            GLog.t(getClass(), "CLIENT: " + clientVersion);

//            if (true) {
//                throw new RuntimeException("TEST");
//            }

            try {
                String walletPath = MyDbValue.getString(DB_FILE_PATH);

                Credentials credentials = WalletUtils.loadCredentials("atestpasswordhere", walletPath);

                try {
                    TransactionReceipt transactionReceipt = Transfer.sendFunds(
                        web3, credentials, "0x8d8f0d309723d21fc36b005f4177ea02d9d65a71",
                        BigDecimal.valueOf(0.1), Convert.Unit.ETHER)
                        .send();

                    GLog.t(getClass(), "TxHash: " + transactionReceipt.getTransactionHash());
                } catch (Exception e) {
                    GLog.e(getClass(), "Failed", e);
                }
//                catch (IOException e) {
//                    GLog.e(getClass(), "Failed", e);
//                }
//                catch (TransactionException e) {
//                    GLog.e(getClass(), "Failed", e);
//                }
            } catch (IOException e) {
                GLog.e(getClass(), "Failed", e);
            } catch (CipherException e) {
                GLog.e(getClass(), "Failed", e);
            }

        }
//        catch (IOException e) {
//            GLog.e(getClass(), "Failed", e);
//        }
        catch (InterruptedException e) {
            GLog.e(getClass(), "Failed", e);
        } catch (ExecutionException e) {
            GLog.e(getClass(), "Failed", e);
        }
    }

    class AsyncSendTask extends AsyncTask<Void, Void, Exception> {
        @Override
        protected Exception doInBackground(Void... params) {
            send();

            return null;
        }

        @Override
        protected void onPostExecute(Exception e) {

        }
    }
}
