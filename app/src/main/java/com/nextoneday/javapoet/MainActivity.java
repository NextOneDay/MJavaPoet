package com.nextoneday.javapoet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nextoneday.annotation.BindView;
import com.nextoneday.annotation.OnClick;
import com.nextoneday.mybutterknife.MyButterknife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    public TextView tv;
    @BindView(R.id.btn)
    public Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyButterknife.bind(this);
        tv.setText("test");
    }
    @OnClick(R.id.btn)
    public void gogo(){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
