package cf.sidward35.foodlog;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean mealEntry = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onResume();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.meal_entry);
                mealEntry = true;

                final Button finishButton = findViewById(R.id.button);
                finishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });

                final TextView calsTotal = findViewById(R.id.textView10);
                final TextView protTotal = findViewById(R.id.textView11);
                final TextView carbsTotal = findViewById(R.id.textView12);
                final TextView fatTotal = findViewById(R.id.textView13);

                calsTotal.setVisibility(View.GONE);
                protTotal.setVisibility(View.GONE);
                carbsTotal.setVisibility(View.GONE);
                fatTotal.setVisibility(View.GONE);

                final EditText cals1 = findViewById(R.id.editText2);
                final EditText cals2 = findViewById(R.id.editText7);
                final EditText cals3 = findViewById(R.id.editText12);
                final EditText cals4 = findViewById(R.id.editText17);

                final EditText prot1 = findViewById(R.id.editText3);
                final EditText prot2 = findViewById(R.id.editText8);
                final EditText prot3 = findViewById(R.id.editText13);
                final EditText prot4 = findViewById(R.id.editText18);

                final EditText carbs1 = findViewById(R.id.editText4);
                final EditText carbs2 = findViewById(R.id.editText9);
                final EditText carbs3 = findViewById(R.id.editText14);
                final EditText carbs4 = findViewById(R.id.editText19);

                final EditText fat1 = findViewById(R.id.editText5);
                final EditText fat2 = findViewById(R.id.editText10);
                final EditText fat3 = findViewById(R.id.editText15);
                final EditText fat4 = findViewById(R.id.editText20);

                final Button calcButton = findViewById(R.id.button2);
                calcButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(cals1.getText().toString().isEmpty()) cals1.setText("0");
                        if(cals2.getText().toString().isEmpty()) cals2.setText("0");
                        if(cals3.getText().toString().isEmpty()) cals3.setText("0");
                        if(cals4.getText().toString().isEmpty()) cals4.setText("0");

                        if(prot1.getText().toString().isEmpty()) prot1.setText("0");
                        if(prot2.getText().toString().isEmpty()) prot2.setText("0");
                        if(prot3.getText().toString().isEmpty()) prot3.setText("0");
                        if(prot4.getText().toString().isEmpty()) prot4.setText("0");

                        if(carbs1.getText().toString().isEmpty()) carbs1.setText("0");
                        if(carbs2.getText().toString().isEmpty()) carbs2.setText("0");
                        if(carbs3.getText().toString().isEmpty()) carbs3.setText("0");
                        if(carbs4.getText().toString().isEmpty()) carbs4.setText("0");

                        if(fat1.getText().toString().isEmpty()) fat1.setText("0");
                        if(fat2.getText().toString().isEmpty()) fat2.setText("0");
                        if(fat3.getText().toString().isEmpty()) fat3.setText("0");
                        if(fat4.getText().toString().isEmpty()) fat4.setText("0");

                        int calsSum = Integer.parseInt(cals1.getText().toString())+Integer.parseInt(cals2.getText().toString())+Integer.parseInt(cals3.getText().toString())+Integer.parseInt(cals4.getText().toString());
                        int protSum = Integer.parseInt(prot1.getText().toString())+Integer.parseInt(prot2.getText().toString())+Integer.parseInt(prot3.getText().toString())+Integer.parseInt(prot4.getText().toString());
                        int carbsSum = Integer.parseInt(carbs1.getText().toString())+Integer.parseInt(carbs2.getText().toString())+Integer.parseInt(carbs3.getText().toString())+Integer.parseInt(carbs4.getText().toString());
                        int fatSum = Integer.parseInt(fat1.getText().toString())+Integer.parseInt(fat2.getText().toString())+Integer.parseInt(fat3.getText().toString())+Integer.parseInt(fat4.getText().toString());

                        calcButton.setVisibility(View.GONE);

                        calsTotal.setVisibility(View.VISIBLE);
                        protTotal.setVisibility(View.VISIBLE);
                        carbsTotal.setVisibility(View.VISIBLE);
                        fatTotal.setVisibility(View.VISIBLE);

                        calsTotal.setText(calsSum+"");
                        protTotal.setText(protSum+"");
                        carbsTotal.setText(carbsSum+"");
                        fatTotal.setText(fatSum+"");
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(mealEntry){
            mealEntry = false;
            onResume();
        }
        else System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
