package com.example.lenovo.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;


import com.google.zxing.Result;

import java.util.ArrayList;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity {

    private TextView myContent;
    private ArrayList<ShoppingCart> shoppingList= new ArrayList<ShoppingCart>();
    private ShoppingCart shoppingCartAdd = null;
    private ListView itemListView;
    private double allTotal = 0.0f;
    private double textView3 = 0.0f;
    private TextView mTextView;
    private String message1 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(shoppingList.isEmpty()){
            Toast.makeText(this, "Cart is empty!", Toast.LENGTH_LONG).show();

        }
        itemListView = (ListView) findViewById(R.id.listView);
        mTextView = (TextView) findViewById(R.id.textView3);

    }

    public void onClickQR(View v)
    {
        try {
            Intent startScanner = new Intent(getApplicationContext(), Scanner.class);
            startActivityForResult(startScanner, 1);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onClickSms(View v){

        try{

            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("sms_body", messageReturn());
            startActivity(smsIntent);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void onClickEmail(View v){

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Shopping Cart");
        emailIntent.putExtra(Intent.EXTRA_TEXT, messageReturn());
        emailIntent.setType("message/rfc822");

        Intent emailChoose = Intent.createChooser(emailIntent, "Email");
        startActivity(emailChoose);
    }

    private String messageReturn() {

        ShoppingCart temp = null;

        String message = "";
        for(int i=0; i<shoppingList.size(); i++){
            temp = shoppingList.get(i);
            message += "Item: " + temp.getItemName() + System.getProperty("line.separator")
                    + "Quantity: " + temp.getQuantity() + System.getProperty("line.separator")
                    + "Price: " + temp.getPrice() + System.getProperty("line.separator")
                    + "Total Price by item: " + temp.getTotalPrice() + System.getProperty("line.separator") + "\n";

        }
        message += "Total price of all item: " + allTotal + "\n";
        return message;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    String mydata = data.getStringExtra("myData");
                    addToList(mydata);
                } catch (Exception e) {
                    AlertDialog.Builder diagBuilder = new AlertDialog.Builder(this);
                    diagBuilder.setTitle("Invalid");
                    diagBuilder.setMessage("Invalid QR Format");

                    AlertDialog myAlert = diagBuilder.create();
                    myAlert.show();
                    Thread myThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(2000);
                                Intent startScannerActivity = new Intent(getApplicationContext(), Scanner.class);
                                startActivityForResult(startScannerActivity, 1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    myThread.start();
                }


            }
        }
    }

    public void addToList(String mydata){

        int count = 0;
        ArrayList<String> stringArray = new ArrayList<String>();
        StringTokenizer myTokenizer = new StringTokenizer(mydata,"|||");
        while(myTokenizer.hasMoreTokens()){
            stringArray.add(myTokenizer.nextToken());
        }



        shoppingCartAdd = new ShoppingCart(stringArray.get(0), Integer.parseInt(stringArray.get(1)),
                Double.parseDouble(stringArray.get(2)), Integer.parseInt(stringArray.get(1))
                *Double.parseDouble(stringArray.get(2)));
        textView3 = textView3+ (Integer.parseInt(stringArray.get(1)) * Double.parseDouble(stringArray.get(2)));
        shoppingList.add(shoppingCartAdd);

        mTextView.setText(textView3 + "");

        allTotal += Double.parseDouble(String.valueOf(shoppingCartAdd.getTotalPrice()));

        ShoppingCartAdapter adapter = new ShoppingCartAdapter(this, R.layout.list_view, shoppingList);
        itemListView.setAdapter(adapter);


        }


    }






