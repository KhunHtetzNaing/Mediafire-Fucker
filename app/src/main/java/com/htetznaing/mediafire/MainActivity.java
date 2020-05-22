package com.htetznaing.mediafire;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText edit_query;
    private TextView tv_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Finding...");

        edit_query = findViewById(R.id.edit_query);
        tv_result = findViewById(R.id.tv_result);
        edit_query.setText("https://www.mediafire.com/file/kfw1swo4mhiips9/Htun_Win.mp4/file");
    }

    public void getDirectLink(View view) {
        final String inputURL = edit_query.getText().toString();
        new MFireFucker(inputURL, new MFireFucker.OnFinished() {
            @Override
            public void done(String src, String size) {
                progressDialog.dismiss();
                //If OK
                tv_result.setText("Input => "+inputURL+"\n\nOutput => "+src+"\n\nSize => "+size);
            }

            @Override
            public void error(String message) {
                progressDialog.dismiss();
                //If error
                tv_result.setText(message);
            }
        }).find();
        progressDialog.show();
    }
}
