package testsdcard.cai.maiyu.picassodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView ;
    private Button button ;
    private final String IMAGE_PATH = "http://www.leawo.cn/attachment/201404/15/1077476_1397532112ke37.png";
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView   =   (ImageView)findViewById(R.id.imageView);
        button      =   (Button)findViewById(R.id.load_image);
        Log.d(TAG , "初始化成功");


        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Picasso.with(MainActivity.this).load(IMAGE_PATH).placeholder(R.mipmap.ic_launcher).fit().into(mImageView);
                }
        });

    }
}
