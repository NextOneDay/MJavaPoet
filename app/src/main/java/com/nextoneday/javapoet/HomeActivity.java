package com.nextoneday.javapoet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nextoneday.annotation.BindView;
import com.nextoneday.annotation.OnClick;
import com.nextoneday.mybutterknife.MyButterknife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    public TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MyButterknife.bind(this);

        tv.setText("wo 是 home page");
    }


    @OnClick(R.id.bbb)
    public void lll(View view){

        Toast.makeText(this, "llll", Toast.LENGTH_SHORT).show();
    }
}
