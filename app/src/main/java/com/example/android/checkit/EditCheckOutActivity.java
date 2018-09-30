package com.example.android.checkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditCheckOutActivity extends AppCompatActivity {

    @BindView(R.id.accommodation_edit_text)
    EditText mAccomEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_check_out);

        // Bind views
        ButterKnife.bind(this);
    }

    public void saveCheckOut(View view) {
        Toast.makeText(this, "CheckOut saved.", Toast.LENGTH_SHORT).show();

        // Get Accommodation Name
        String accommodationName = mAccomEditText.getText().toString().trim();

        // Check to see that the user input a name
        if (TextUtils.isEmpty(accommodationName)) {
            Toast.makeText(this, "Please enter valid accommodation name.", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
