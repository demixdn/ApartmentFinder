package com.github.apartmentfinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends Activity {

    private EditText etApartmentNumber;
    private EditText etFloorCount;
    private EditText etApartmentOnFloor;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etApartmentNumber = (EditText) findViewById(R.id.etApartmentNumber);
        etFloorCount = (EditText) findViewById(R.id.etFloorCount);
        etApartmentOnFloor = (EditText) findViewById(R.id.etApartmentsOnFloorCount);
        findViewById(R.id.btCalculation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
                calc();
            }
        });
        tvResult = (TextView) findViewById(R.id.tvResult);
    }

    private void calc() {
        String apartNum = etApartmentNumber.getText().toString();
        String floorCount = etFloorCount.getText().toString();
        String apartOnFloor = etApartmentOnFloor.getText().toString();
        if (isEmpty(apartNum) || isEmpty(floorCount) || isEmpty(apartOnFloor)) {
            Toast.makeText(this, R.string.error_field_not_filled, Toast.LENGTH_SHORT).show();
        } else {
            parseData(apartNum, floorCount, apartOnFloor);
        }
    }

    private void parseData(String apartNum, String floorCount, String apartOnFloor) {
        try {
            int apartNumInt = getApartNumInt(apartNum);
            int floorCountInt = getFloorCountInt(floorCount);
            int apartOnFloorInt = getApartOnFloorInt(apartOnFloor);
            calcAndShow(apartNumInt, floorCountInt, apartOnFloorInt);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_parsing, Toast.LENGTH_SHORT).show();
        }
    }

    private int getApartOnFloorInt(String apartOnFloor) {
        int apartOnFloorInt = Integer.parseInt(apartOnFloor);
        if (apartOnFloorInt <= 0) {
            apartOnFloorInt = 1;
            etApartmentOnFloor.setText(String.valueOf(apartOnFloorInt));
        }
        return apartOnFloorInt;
    }

    private int getFloorCountInt(String floorCount) {
        int floorCountInt = Integer.parseInt(floorCount);
        if (floorCountInt <= 0) {
            floorCountInt = 1;
            etFloorCount.setText(String.valueOf(floorCountInt));
        }
        return floorCountInt;
    }

    private int getApartNumInt(String apartNum) {
        int apartNumInt = Integer.parseInt(apartNum);
        if (apartNumInt <= 0) {
            apartNumInt = 1;
            etApartmentNumber.setText(String.valueOf(apartNumInt));
        }
        return apartNumInt;
    }

    private void calcAndShow(int apartNum, int floorCount, int apartOnFloor) {
        int floorResult = getFloorBy(apartNum, floorCount, apartOnFloor);
        int doorResult = getDoorBy(apartNum, floorCount, apartOnFloor);
        showResult(floorResult, doorResult);
    }

    private void showResult(int floorResult, int doorResult) {
        String result = getString(R.string.result_string, floorResult, doorResult);
        tvResult.setText(result);
    }

    private int getFloorBy(int apartmentNumber, int floorsCount, int apartmentsOnFloor) {
        int numberInDoors = apartmentNumber % (floorsCount * apartmentsOnFloor);
        if (numberInDoors == 0) {
            numberInDoors = floorsCount * apartmentsOnFloor;
        }
        return (int) Math.ceil(numberInDoors * 1.0d / apartmentsOnFloor * 1.0d);
    }

    private int getDoorBy(int apartmentNumber, int floorsCount, int apartmentsOnFloor) {
        return (int) Math.ceil(apartmentNumber * 1.0d / (floorsCount * apartmentsOnFloor * 1.0d));
    }

    public void hideSoftKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
