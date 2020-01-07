package cf.sidward35.foodlog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    boolean mealEntry = false, sumCalculated = false;
    int calsSum=0, protSum=0, carbsSum=0, fatSum=0;
    String mealType = "";
    final String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
    final String FOLDERNAME = "FoodLogApp", FILENAME = "FoodLogData.txt";

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

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Please enable storage read/write permission!", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        File folder = new File(extStorageDirectory, FOLDERNAME);
        folder.mkdir();
        final File file = new File(folder, FILENAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            updateHomePage(file);
        }catch(Exception e){}

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.meal_entry);
                mealEntry = true;

                Toolbar toolbar2 = findViewById(R.id.toolbar2);
                toolbar2.setTitle("Food Log");

                final RadioButton bfChoice = findViewById(R.id.radioButton);
                final RadioButton lChoice = findViewById(R.id.radioButton2);
                final RadioButton dChoice = findViewById(R.id.radioButton3);

                bfChoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mealType = "Breakfast";
                    }
                });

                lChoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mealType = "Lunch";
                    }
                });

                dChoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mealType = "Dinner";
                    }
                });

                final Button finishButton = findViewById(R.id.button);
                finishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(sumCalculated && !mealType.equals("")){
                            String date = getCurrentTimeStamp();
                            writeToFile(date+" | "+mealType+" | "+calsSum+" | "+protSum+" | "+carbsSum+" | "+fatSum+"\n", file);
                            onBackPressed();
                        }else if(mealType.equals("")){
                            Toast.makeText(getApplicationContext(), "Please specify meal type!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Press 'Calculate' first!", Toast.LENGTH_LONG).show();
                        }
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
                        sumCalculated = true;

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

                        calsSum = Integer.parseInt(cals1.getText().toString())+Integer.parseInt(cals2.getText().toString())+Integer.parseInt(cals3.getText().toString())+Integer.parseInt(cals4.getText().toString());
                        protSum = Integer.parseInt(prot1.getText().toString())+Integer.parseInt(prot2.getText().toString())+Integer.parseInt(prot3.getText().toString())+Integer.parseInt(prot4.getText().toString());
                        carbsSum = Integer.parseInt(carbs1.getText().toString())+Integer.parseInt(carbs2.getText().toString())+Integer.parseInt(carbs3.getText().toString())+Integer.parseInt(carbs4.getText().toString());
                        fatSum = Integer.parseInt(fat1.getText().toString())+Integer.parseInt(fat2.getText().toString())+Integer.parseInt(fat3.getText().toString())+Integer.parseInt(fat4.getText().toString());

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
            sumCalculated = false;
            mealType = "";
            onResume();
        }
        else System.exit(0);
    }

    private void writeToFile(String data, File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file, true);
            fileOut.write(data.getBytes());
            fileOut.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(File file) {

        String ret = "";

        try {
            InputStream inputStream = new FileInputStream(file);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                ArrayList<String> lines = new ArrayList<String>();

                while ( (receiveString = bufferedReader.readLine()) != null )
                    lines.add(receiveString);

                for(int x=lines.size()-1; x>=0; x--)
                    if(lines.get(x).substring(0, 10).equals(getCurrentTimeStamp().substring(0, 10)))
                        stringBuilder.append(lines.get(x)+"\n");

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    private void updateHomePage(File file) {
        TextView hw = findViewById(R.id.helloworld);
        ProgressBar calsBar = findViewById(R.id.progressBar);
        calsBar.setMax(2452);
        ProgressBar protBar = findViewById(R.id.progressBar2);
        protBar.setMax(158);
        ProgressBar carbsBar = findViewById(R.id.progressBar3);
        carbsBar.setMax(165);
        ProgressBar fatBar = findViewById(R.id.progressBar4);
        fatBar.setMax(60);

        String todayData = readFromFile(file);
        String[] todayLines = todayData.split("\n");
        int todayCals = 0, todayProt = 0, todayCarbs = 0, todayFat = 0;
        for(String str:todayLines){
            todayCals+=Integer.parseInt(str.substring(ordinalIndexOf(str, "|", 2)+2, ordinalIndexOf(str, "|", 3)-1));
            todayProt+=Integer.parseInt(str.substring(ordinalIndexOf(str, "|", 3)+2, ordinalIndexOf(str, "|", 4)-1));
            todayCarbs+=Integer.parseInt(str.substring(ordinalIndexOf(str, "|", 4)+2, ordinalIndexOf(str, "|", 5)-1));
            todayFat+=Integer.parseInt(str.substring(ordinalIndexOf(str, "|", 5)+2));
        }
        hw.setText("Cals: "+todayCals+"/2452 | Protein: "+todayProt+"/158g\nCarbs: "+todayCarbs+"/165g | Fat: "+todayFat+"/60g");

        calsBar.setProgress(todayCals);
        protBar.setProgress(todayProt);
        carbsBar.setProgress(todayCarbs);
        fatBar.setProgress(todayFat);
    }

    public int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }
}
