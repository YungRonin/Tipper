package io.tipper.tipper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.gani.lib.http.GParams;
import com.gani.lib.http.GRestCallback;
import com.gani.lib.http.GRestResponse;
import com.gani.lib.http.HttpMethod;
import com.gani.lib.json.GJsonArray;
import com.gani.lib.json.GJsonObject;
import com.gani.lib.logging.GLog;
import com.gani.lib.ui.ProgressIndicator;
import com.gani.lib.ui.Ui;
import com.gani.lib.ui.view.GTextView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.IOException;
import java.math.BigInteger;

import io.tipper.tipper.app.database.MyDbValue;

import static io.tipper.tipper.app.constants.Keys.DB_FILE_PATH;

public class TransactionListActivity extends Activity {
//    private Context context;
//    private QrScanner scanner;
//    private LinearLayout layout;

    public static Intent intent() {
        return new Intent(Ui.context(), TransactionListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_layout);

        final LinearLayout container = findViewById(R.id.container);

        container.addView(new GTextView(this).text("Transaction History").bold());

        String pubKey;
        String WalletFilePath;
        WalletFilePath = MyDbValue.get(DB_FILE_PATH, new TypeToken<String>() {});
        if (WalletFilePath != null) {
            try {
                Credentials creds = WalletUtils.loadCredentials("atestpasswordhere", WalletFilePath);

                pubKey = creds.getAddress();

                if(pubKey != null){
                    GParams params = GParams.create()
                            .put("apikey", "8XY5G7CC8CYMAJ267UBE58QNWDG1H49JHT")
                            .put("module", "account")
                            .put("action", "txlist")
                            .put("address", pubKey)
                            .put("startblock", "0")
                            .put("endblock", "99999999")
                            .put("sort", "desc")
                            .put("page", "1")
                            .put("offset", "50");

                    HttpMethod.GET.async("https://rinkeby.etherscan.io/api", params.toImmutable(), new GRestCallback(this, ProgressIndicator.NULL) {
                        @Override
                        protected void onRestResponse(GRestResponse r) throws JSONException {
                            super.onRestResponse(r);

                            GJsonObject result = r.getResult();
                            GJsonArray<GJsonObject> transactions = result.getArray("result");
                            GLog.t(getClass(), "RESULT: " + transactions);
                            for (GJsonObject transaction : transactions) {
                                String from = transaction.getString("from");
                                String to = transaction.getString("to");
                                String value = transaction.getString("value");
                                GLog.t(getClass(), "TX: " + transaction.toString());
                                container.addView(new GTextView(TransactionListActivity.this).text("from: " + from));
                                container.addView(new GTextView(TransactionListActivity.this).text("to: " + to));
                                container.addView(new GTextView(TransactionListActivity.this).text("value: " + new BalanceHelper().convertWeiToEth(new BigInteger(value))));
                            }
                        }
                    }).execute();
                }
            } catch (CipherException e) {
                Log.e("fail", "exception " + e);
            } catch (IOException e) {
                Log.e("fail", "exception " + e);
            }
        }
        else{
            container.addView(new GTextView(TransactionListActivity.this).text("Unable to obtain tx history."));
        }
    }

//    @Override
//    public void onPostCreate(@Nullable Bundle savedinstaceState) {
//        super.onPostCreate(savedinstaceState);
//        scanner = new QrScanner(this);
//        scanner.init();
//        layout.addView(scanner.getView());
//    }

}
