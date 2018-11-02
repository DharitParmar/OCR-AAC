package com.example.dparm.ocrscanneraac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dparm.ocrscanneraac.database.AppDatabase;
import com.example.dparm.ocrscanneraac.database.CardEntry;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class CardScannerActivity extends AppCompatActivity {

    private static final int MY_SCAN_REQUEST_CODE = 1;
    private AppDatabase mDB;
    private TextView mTextCardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_scanner);

        mDB = AppDatabase.getInstance(getApplicationContext());

        mTextCardNumber = findViewById(R.id.textCardNumber);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onScanPress();
    }

    private void onScanPress() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        // false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, true);
        // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);

        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);


        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case MY_SCAN_REQUEST_CODE:
                String resultStr;

                if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {

                    // storing into parcelable object type credit card
                    CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                    saveCardDetails(scanResult);

                    resultStr = "Card Scanned Successfully" + "\n";
                    // if necessary can use getFormattedCardNumber()
                    resultStr += "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

//                    we can perform something here:
//                    or needed can extract more data from a card
//                    eg:
//                    myService.setCardNumber( scanResult.cardNumber );
//
//                    if (scanResult.isExpiryValid()) {
//                        resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
//                    }
//
//
//                    if (scanResult.postalCode != null) {
//                        resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
//                    }
//
                    mTextCardNumber.setText(resultStr);
                } else {
                    resultStr = "Scan was canceled!" + "\n" + "Please try it again!";
                }

        }
    }

    public void saveCardDetails(CreditCard scanResult){
        // read card number string
        String mCardNumber;
        mCardNumber = scanResult.getRedactedCardNumber();

        // Puts the string into CardEntry object
        final CardEntry cardEntry = new CardEntry(mCardNumber);

        // Inserts the Card Entry object into database using runnable within diskIO from AppExecutor
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // makes database call logic into runnable thread
                mDB.cardDao().insertCard(cardEntry);
                finish();
            }
        });

    }

}
