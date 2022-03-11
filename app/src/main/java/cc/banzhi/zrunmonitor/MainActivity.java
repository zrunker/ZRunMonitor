package cc.banzhi.zrunmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cc.banzhi.runmonitor.RunMonitor;
import cc.banzhi.zrunmonitor.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RunMonitor.init(this);
    }
}
