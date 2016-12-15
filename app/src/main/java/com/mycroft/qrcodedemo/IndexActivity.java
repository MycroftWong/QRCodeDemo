package com.mycroft.qrcodedemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class IndexActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0x1000;
    private TextView mInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mInfoTextView = (TextView) findViewById(R.id.info_text_view);
    }

    public void scan(View view) {
        startActivityForResult(new Intent(this, MainActivity.class), REQUEST_CODE);
    }

    public void test(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE) {
            mInfoTextView.setText(data.getStringExtra(MainActivity.RESULT_CODE));
        }
    }

    public void weixin(View view) {
//        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://weixin.qq.com/r/dm32309890jdsfjal"));
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://com.mycroft.qrcode/dajdklfj"))
//        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("weixin://1231321"))
//        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://weixin.qq.com/r/dm16Yg-EolLdrUsB9zhk"))
                .addCategory(Intent.CATEGORY_DEFAULT)
                .addCategory(Intent.CATEGORY_BROWSABLE);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.setData(Uri.parse("weixin://qr/1231321"));
//        intent.setData(Uri.parse("http://weixin.qq.com/r/dm32309890jdsfjal"));
        startActivity(intent);
    }

}
