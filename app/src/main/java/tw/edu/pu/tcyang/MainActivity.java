package tw.edu.pu.tcyang;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //設定全螢幕顯示
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //設定螢幕為橫式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_main);
    }

    public void StartGame(View v){
        //讀取燈號秒數
        EditText edt = (EditText) findViewById(R.id.editTextGreen);
        String stringG = edt.getText().toString();

        edt = (EditText) findViewById(R.id.editTextYellow);
        String stringY = edt.getText().toString();

        edt = (EditText) findViewById(R.id.editTextRed);
        String stringR = edt.getText().toString();

        //判斷是否有燈號為0或空白
        if  (stringG.isEmpty() || stringY.isEmpty() || stringR.isEmpty()){
            Toast.makeText(this, "燈號的秒數不能為空白", Toast.LENGTH_LONG).show();
        }
        else{
            int SecG = Integer.valueOf(stringG);
            int SecY = Integer.valueOf(stringY);
            int SecR = Integer.valueOf(stringR);
            if ((SecG == 0) || (SecY == 0) || (SecR == 0)){
                Toast.makeText(this, "燈號的秒數不能為0", Toast.LENGTH_LONG).show();
            }
            else{

                Intent it = new Intent();
                it.setClass(this, GameActivity.class);

                //傳值
                it.putExtra("SecG", SecG);
                it.putExtra("SecY", SecY);
                it.putExtra("SecR", SecR);
                startActivity(it);
                finish();
            }
        }

    }

    public void EndApp(View v){
        finish();
    }

}
