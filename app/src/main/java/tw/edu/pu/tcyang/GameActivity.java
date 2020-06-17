package tw.edu.pu.tcyang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class GameActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    GameSurfaceView GameSV;
    Handler handler;

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


        setContentView(R.layout.activity_game);

        GameSV = (GameSurfaceView) findViewById(R.id.GameSV);
        //設定初始測試之燈號秒數
        //GameSV.SetLightSec(6,2,3);
        //讀取使用者輸入之燈號秒數
        Intent it = getIntent();
        GameSV.SetLightSec(it.getIntExtra("SecG",0),
                it.getIntExtra("SecY",0),
                it.getIntExtra("SecR",0));

        handler= new Handler();
    }


    float LastX, CurrentX;  //手指觸控X座標

    //利用手指觸控，控制小男孩走路
    public boolean onTouchEvent (MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            //GameSV.BoyMoving = true;
            LastX = event.getX();
            handler.post(runnable);
        }
        else if (event.getAction() == MotionEvent.ACTION_UP){
            GameSV.BoyMoving =  false;
            handler.removeCallbacks(runnable);  //銷毀執行緒
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE){
            CurrentX = event.getX();
            if (CurrentX>LastX){
                GameSV.BoyMoving = true;
            }
            else{
                GameSV.BoyMoving =  false;
            }
            LastX = CurrentX;
        }

        return true;
    }

    //處理小男孩走路
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Canvas canvas = GameSV.getHolder().lockCanvas();
            GameSV.drawSomething(canvas);
            GameSV.getHolder().unlockCanvasAndPost(canvas);
            //handler.postDelayed(runnable, 50);
            //判斷是否闖紅燈
            if (GameSV.BoyMoving && (GameSV.CurrentLight == "Red")){  //闖紅燈
                handler.removeCallbacks(runnable);  //銷毀執行緒
                GameSV.handlerLight.removeCallbacks(GameSV.runnableLight);  //銷毀燈號倒數執行緒
                GameOver();  //遊戲結束處理
            }
            else{
                handler.postDelayed(runnable, 50);
            }

        }
    };


    //遊戲結束處理
    public void GameOver(){
        //遊戲結束處理
        new AlertDialog.Builder(this)
                .setTitle("遊戲結束")
                .setMessage("您此次的成績是" + String.valueOf(GameSV.score) + "分，不可以闖紅燈喔！")
                .setIcon(R.drawable.boy8)
                .setPositiveButton("再玩一次", this)
                .setNegativeButton("結束系統", this)
                .setNeutralButton("設定燈號秒數", this)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == DialogInterface.BUTTON_POSITIVE){
            //再玩一次，設定遊戲初始值
            GameSV.CurrentLight = "Yellow";
            GameSV.CurrentCountDown = GameSV.YellowLightSec + 1; //因為一開始執行就會-1
            GameSV.step = 1;
            GameSV.score = 0;
            GameSV.post(GameSV.runnableLight);

            //設定全螢幕顯示
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        else if (i == DialogInterface.BUTTON_NEGATIVE){
            //結束系統
            finish();
        }else{
            //設定燈號秒數 (回到MainActivity)
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
            finish();
        }
    }
}
