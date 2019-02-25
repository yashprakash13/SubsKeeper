package tech.pcreate.subscriptionkeeper.expenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import tech.pcreate.multichoicelayout.MultiChoiceLayout;
import tech.pcreate.subscriptionkeeper.R;
import tech.pcreate.subscriptionkeeper.database.SubsDatabase;
import tech.pcreate.subscriptionkeeper.database.Subscription;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyExpensesActivity extends AppCompatActivity implements MultiChoiceLayout.MultiChoiceOnclick {

    private ProgressBar progressBar;
    private SubsDatabase subsDatabase;
    private List<Subscription> subscriptionsList;
    private float monthlyExp, yearlyExp;
    private TextView amount;
    private boolean loadedSubs = false;
    private boolean isLoadedMonthly = false, isLoadedYearly =  false;
    private Map<String, Integer> frequencyMap;
    private Map<String, Float> frequencyWiseSumMonthly = new HashMap<>();
    private Map<String, Float> frequencyWiseSumYearly = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expenses);
        subsDatabase = SubsDatabase.getDatabase(this);
        amount = findViewById(R.id.amount);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.my_expenses));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);

        MultiChoiceLayout multiChoiceLayout = findViewById(R.id.multichoices);
        multiChoiceLayout.setOnClickListener(this);

        getAllSubs(new onCallbackSubscriptions() {
            @Override
            public void getAllSubscriptions(List<Subscription> subscriptions) {
                subscriptionsList = subscriptions;
                getFrequencyOfCurrencies();
            }
        });


    }

    private void getFrequencyOfCurrencies() {
        frequencyMap = new HashMap<>();
        for (Subscription subscription : subscriptionsList) {
            String s = subscription.getCurrency();
            Integer count = frequencyMap.get(s);
            if (count == null)
                count = 0;

            frequencyMap.put(s, count + 1);
        }
        //Log.e("Frequency = ", String.valueOf(frequencyMap));

        calculatefreuqncyWiseSum();

    }

    private void calculatefreuqncyWiseSum() {
        for (Subscription subscription : subscriptionsList){
            String currency = subscription.getCurrency();
            Float sum = frequencyWiseSumMonthly.get(currency);
            if (sum == null)
                sum = (float) 0;
            frequencyWiseSumMonthly.put(currency, sum + (float) subscription.getAmount()/(float) subscription.getRecurring());

            Float sumY =  frequencyWiseSumYearly.get(currency);
            if(sumY == null)
                sumY = (float) 0;
            frequencyWiseSumYearly.put(currency, (float) (sumY + ((float) subscription.getAmount() * (12.0 /  (float) subscription.getRecurring()))));
        }

        loadedSubs = true;
    }


    @Override
    public void onMultiChoiceLayoutClick() {
        progressBar.setVisibility(View.VISIBLE);

        if(MultiChoiceLayout.getSelection() ==  1){
            amount.setText("");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (Map.Entry <String, Float> entry : frequencyWiseSumMonthly.entrySet()){
                        amount.append(entry.getKey() + " = " + entry.getValue() + "\n\n");
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }, 3000);
        }else{
            amount.setText("");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (Map.Entry <String, Float> entry : frequencyWiseSumYearly.entrySet()){
                        amount.append(entry.getKey() + " = " + entry.getValue() + "\n\n");
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }, 3000);
        }

    }

    private void getAllSubs(onCallbackSubscriptions callbackSubscriptions){
        List<Subscription> list = subsDatabase.subsDao().getSubscriptions();
        callbackSubscriptions.getAllSubscriptions(list);
    }

}
