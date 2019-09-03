package com.example.justjava;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    int quantity = 1;

    /**
     * Functia de incrementare nu-l lasa sa treaca de 100
     * */
    public void increment(View view){
        if(quantity == 100) {
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }

    /**
     * Functia de decrementare nu-l lasa sa scada sub 1
     * */
    public void decrement(View view){
        if(quantity == 1) {
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }


    public void submitOrder(View view) {

        EditText nameField = (EditText) findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWippedCream = whippedCreamCheckBox.isChecked();
        Log.v("MainActivity", "Has whipped cream: " + hasWippedCream);

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolateCream = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasChocolateCream,hasWippedCream);
        String priceMessage = createOrderSummary(price, hasWippedCream, hasChocolateCream, name);
        displayMessage(priceMessage);

        /**
         *  EMAIL SENDER
         */
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("*/*");
        intent.setData(Uri.parse("mailto: "));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Coffee command " + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    private int calculatePrice(boolean addChocolate, boolean addWippedCream){
        int baseprice = 5;

        if(addWippedCream){
            baseprice = baseprice + 1;
        }
        if(addChocolate){
            baseprice = baseprice + 2;
        }

        return quantity * baseprice;
    }

    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolateCream, String name){
        String priceMessage = "Name: " + name;
        priceMessage = priceMessage + "\nAdd whipped cream?: " + hasWhippedCream;
        priceMessage = priceMessage + "\nAdd chocolate?: " + hasChocolateCream;
        priceMessage = priceMessage + "\nQuantity: " + quantity;
        priceMessage = priceMessage + "\nPrice: " + price + "$";
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setTextColor(Color.BLACK);
        orderSummaryTextView.setText(message);
    }
}