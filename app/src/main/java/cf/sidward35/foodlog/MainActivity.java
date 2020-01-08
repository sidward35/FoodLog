package cf.sidward35.foodlog;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    boolean mealEntry = false, sumCalculated = false, settingsOpen = false;
    int calsSum=0, protSum=0, carbsSum=0, fatSum=0;
    String mealType = "";
    final String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
    final String FOLDERNAME = "FoodLogApp", FILENAME = "FoodLogData.txt";
    int CALSMAX = 2452, PROTEINMAX = 158, CARBSMAX = 165, FATMAX = 60;

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
        }else if(settingsOpen){
            settingsOpen = false;
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            setContentView(R.layout.app_settings);
            settingsOpen = true;
            Toolbar toolbar3 = findViewById(R.id.toolbar3);
            toolbar3.setTitle("Food Log");

            final EditText calsSetting = findViewById(R.id.editTextCalsSetting);
            final EditText protSetting = findViewById(R.id.editTextProtSetting);
            final EditText carbsSetting = findViewById(R.id.editTextCarbsSetting);
            final EditText fatSetting = findViewById(R.id.editTextFatSetting);
            calsSetting.setHint(CALSMAX+"");
            protSetting.setHint(PROTEINMAX+"");
            carbsSetting.setHint(CARBSMAX+"");
            fatSetting.setHint(FATMAX+"");

            Button resetSettings = findViewById(R.id.button4);
            resetSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calsSetting.setText("2452");
                    protSetting.setText("158");
                    carbsSetting.setText("165");
                    fatSetting.setText("60");
                }
            });

            Button saveSettings = findViewById(R.id.button3);
            saveSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Integer> newSettings = new ArrayList<Integer>();
                    if(!calsSetting.getText().toString().isEmpty()) CALSMAX = Integer.parseInt(calsSetting.getText().toString());
                    if(!protSetting.getText().toString().isEmpty()) PROTEINMAX = Integer.parseInt(protSetting.getText().toString());
                    if(!carbsSetting.getText().toString().isEmpty()) CARBSMAX = Integer.parseInt(carbsSetting.getText().toString());
                    if(!fatSetting.getText().toString().isEmpty()) FATMAX = Integer.parseInt(fatSetting.getText().toString());
                    newSettings.add(CALSMAX);
                    newSettings.add(PROTEINMAX);
                    newSettings.add(CARBSMAX);
                    newSettings.add(FATMAX);
                    writeSettings(newSettings);
                    onBackPressed();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        TextView calsText = findViewById(R.id.cals_text);
        TextView protText = findViewById(R.id.prot_text);
        TextView carbsText = findViewById(R.id.carbs_text);
        TextView fatText = findViewById(R.id.fat_text);

        readSettings();

        calsText.setText("Cals: 0/"+CALSMAX);
        protText.setText("Protein: 0/"+PROTEINMAX+"g");
        carbsText.setText("Carbs: 0/"+CARBSMAX+"g");
        fatText.setText("Fat: 0/"+FATMAX+"g");

        ProgressBar calsBar = findViewById(R.id.progressBar);
        calsBar.setMax(CALSMAX);
        ProgressBar protBar = findViewById(R.id.progressBar2);
        protBar.setMax(PROTEINMAX);
        ProgressBar carbsBar = findViewById(R.id.progressBar3);
        carbsBar.setMax(CARBSMAX);
        ProgressBar fatBar = findViewById(R.id.progressBar4);
        fatBar.setMax(FATMAX);

        String todayData = readFromFile(file);
        String[] todayLines = todayData.split("\n");
        int todayCals = 0, todayProt = 0, todayCarbs = 0, todayFat = 0;
        for(String str:todayLines){
            todayCals+=Integer.parseInt(str.substring(ordinalIndexOf(str, "|", 2)+2, ordinalIndexOf(str, "|", 3)-1));
            todayProt+=Integer.parseInt(str.substring(ordinalIndexOf(str, "|", 3)+2, ordinalIndexOf(str, "|", 4)-1));
            todayCarbs+=Integer.parseInt(str.substring(ordinalIndexOf(str, "|", 4)+2, ordinalIndexOf(str, "|", 5)-1));
            todayFat+=Integer.parseInt(str.substring(ordinalIndexOf(str, "|", 5)+2));
        }

        calsText.setText("Cals: "+todayCals+"/"+CALSMAX);
        protText.setText("Protein: "+todayProt+"/"+PROTEINMAX+"g");
        carbsText.setText("Carbs: "+todayCarbs+"/"+CARBSMAX+"g");
        fatText.setText("Fat: "+todayFat+"/"+FATMAX+"g");

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

    private void writeSettings(ArrayList<Integer> goals){
        String data = "";
        for(int n:goals)
            data+=(n+"\n");
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("settings", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void readSettings(){
        try {
            InputStream inputStream = openFileInput("settings");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                ArrayList<String> lines = new ArrayList<String>();

                while ((receiveString = bufferedReader.readLine()) != null)
                    lines.add(receiveString);

                inputStream.close();
                CALSMAX = Integer.parseInt(lines.get(0));
                PROTEINMAX = Integer.parseInt(lines.get(1));
                CARBSMAX = Integer.parseInt(lines.get(2));
                FATMAX = Integer.parseInt(lines.get(3));
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}
