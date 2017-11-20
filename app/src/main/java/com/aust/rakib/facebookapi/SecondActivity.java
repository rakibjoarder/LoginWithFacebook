package com.aust.rakib.facebookapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

public class SecondActivity extends AppCompatActivity {
    private String first_name,name,id,gender;
    private TextView firstnameTV,lastnameTV,fullnameTV;
    private ImageView imageView;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        first_name=getIntent().getExtras().getString("first_name");
        id =getIntent().getExtras().getString("id");
        name=getIntent().getExtras().getString("name");
        gender=getIntent().getExtras().getString("gender");

        button=(Button)findViewById(R.id.logout);

        fullnameTV=(TextView) findViewById(R.id.textView3);
        lastnameTV=(TextView) findViewById(R.id.textView2);
        firstnameTV=(TextView) findViewById(R.id.textView);
        imageView=(ImageView) findViewById(R.id.imageView2);

        firstnameTV.setText(name);
        lastnameTV.setText(gender);
        fullnameTV.setText(id);
        Picasso.with(SecondActivity.this).load("https://graph.facebook.com/"+id+"/picture?type=large").into(imageView);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
                Intent main = new Intent( SecondActivity.this, MainActivity.class );
                startActivity( main );
                finish();
            }
        });
    }
}
