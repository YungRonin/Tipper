package io.tipper.tipper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;

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
        setContentView(R.layout.activity_send_layout);
        layout = findViewById(R.id.qr_scanner_layout);
//        layout.addView(new GButton(this).text("Send").onClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new AsyncSendTask().execute();
//            }
//        }));
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedinstaceState) {
        super.onPostCreate(savedinstaceState);
        scanner = new QrScanner(this);
        scanner.init();
        layout.addView(scanner.getView());
    }

    private void handleTransactionReceipt(final TransactionReceipt transactionReceipt){
        Ui.run(new Runnable() {
            @Override
            public void run() {
                String hash = String.valueOf("Hash : " + transactionReceipt.getTransactionHash());
                String from = String.valueOf("From : " + transactionReceipt.getFrom() + "\n");
                String to = String.valueOf("To : " + transactionReceipt.getTo() + "\n");
                AlertDialog.Builder builder = new AlertDialog.Builder(SendActivity.this);
                builder.setTitle("Transaction Receipt");
                builder.setMessage(String.valueOf(from + to + hash));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

    }


    public static class AsyncSendTask extends AsyncTask<String, String, Exception> {
        SendActivity context;

        public AsyncSendTask(SendActivity context){
            super();
            this.context = context;
        }

        @Override
        protected Exception doInBackground(String... params) {


            return send(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Exception e) {
            if(e != null && context != null) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        public Exception send(String address, String amount) {

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
                                web3, credentials, address,
                                new BigDecimal(amount), Convert.Unit.ETHER)
                                .send();

                        context.handleTransactionReceipt(transactionReceipt);
                        GLog.t(getClass(), "TxHash: " + transactionReceipt.getTransactionHash());
                    } catch (Exception e) {
                        GLog.e(getClass(), "Failed", e);
                        return e;
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
            return null;
        }
    }
}
