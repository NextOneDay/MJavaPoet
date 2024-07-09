package com.nextoneday.javapoet;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ButterKnifeActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);
        ButterKnife.bind(this);
        text.setText("test");
    }
    @OnClick
    public void testClick(View view){
        Toast.makeText(this, "ButterKnifeActivity testClick", Toast.LENGTH_SHORT).show();
    }
}