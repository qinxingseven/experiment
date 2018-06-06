package com.project.zero.intent;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button but1 = findViewById(R.id.but1);
        but1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView tv1 = findViewById(R.id.tv1);
                String s = tv1.getText().toString();
                //Intent intent = new Intent();
                //intent.setAction(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse(s));//设置一个URI地址
                //startActivity(intent);
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, MyWebview.class);
                intent2.putExtra("url", s);
                startActivity(intent2);

            }
        });
    }



}
