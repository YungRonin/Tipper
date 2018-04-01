package io.tipper.tipper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gani.lib.http.GParams;
import com.gani.lib.http.GRestCallback;
import com.gani.lib.http.GRestResponse;
import com.gani.lib.http.HttpMethod;
import com.gani.lib.json.GJsonArray;
import com.gani.lib.json.GJsonObject;
import com.gani.lib.logging.GLog;
import com.gani.lib.ui.ProgressIndicator;
import com.gani.lib.ui.Ui;
import com.gani.lib.ui.view.GButton;
import com.gani.lib.ui.view.GTextView;
import com.google.android.gms.vision.text.Line;

import org.json.JSONException;
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

        GParams params = GParams.create()
            .put("apikey", "8XY5G7CC8CYMAJ267UBE58QNWDG1H49JHT")
            .put("module", "account")
            .put("action", "txlist")
            .put("address", "0xab86ca6c0e64092c4f444af47a2bebba67f6cd7b")
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
                    String txHash = transaction.getString("hash");
                    GLog.t(getClass(), "TX: " + transaction.getString("hash"));
                    container.addView(new GTextView(TransactionListActivity.this).text(txHash));
                }
            }
        }).execute();
    }

//    @Override
//    public void onPostCreate(@Nullable Bundle savedinstaceState) {
//        super.onPostCreate(savedinstaceState);
//        scanner = new QrScanner(this);
//        scanner.init();
//        layout.addView(scanner.getView());
//    }

}
