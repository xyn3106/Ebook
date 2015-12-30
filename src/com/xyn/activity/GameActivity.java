package com.xyn.activity;


import java.util.Random;

import com.xyn.ebook.R;
import com.xyn.ebook.game1;
import com.xyn.ebook.game2;
import com.xyn.ebook.game3;
import com.xyn.ebook.game4;
import com.xyn.ebook.game5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class GameActivity extends Activity {

	private TextView a,b,c,d,mTitle,levelText;
	private int level=1,round,score;
	private int[] randomList;
	Toast mToast;
	String[] game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game);
		initView();
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		levelText = (TextView) findViewById(R.id.level);
		a = (TextView) findViewById(R.id.L1);
		b = (TextView) findViewById(R.id.L2);
		c = (TextView) findViewById(R.id.L3);
		d = (TextView) findViewById(R.id.L4);
		MyOnClickLietener myonclick = new MyOnClickLietener();
		a.setOnClickListener(myonclick);
		b.setOnClickListener(myonclick);
		c.setOnClickListener(myonclick);
		d.setOnClickListener(myonclick);
		randomList = NewRandomList(game1.count-1);
		game = game1.list[randomList[round]];
		refresh();
	}

	private class MyOnClickLietener implements View.OnClickListener {
		public void onClick(View arg0) {
			int mID = arg0.getId();
			switch (mID) {
			case R.id.L1:
				choose("A");
				break;
			case R.id.L2:
				choose("B");
				break;
			case R.id.L3:
				choose("C");
				break;
			case R.id.L4:
				choose("D");
				break;
			}
		}
	}
	
	private void choose(String i){
		String right = game[game.length-1];
		if(i.equals(right)){
			score++;
			if (mToast == null)
		            mToast = Toast.makeText(this, "恭喜回答正确~", Toast.LENGTH_SHORT);  
			else {  
		            mToast.setText("恭喜回答正确~");  
		            mToast.setDuration(Toast.LENGTH_SHORT);  
		            }  
		} else {
			if (mToast == null)
	            mToast = Toast.makeText(this, "回答错误了哦", Toast.LENGTH_SHORT);  
	        else {
	            mToast.setText("回答错误了哦");  
	            mToast.setDuration(Toast.LENGTH_SHORT);  
	            }  
		}
        mToast.show();
		round++;
		if(round<5){
			switch (level){
				case 1:
					game = game1.list[randomList[round]];
					break;
				case 2:
					game = game2.list[randomList[round]];
					break;
				case 3:
					game = game3.list[randomList[round]];
					break;
				case 4:
					game = game4.list[randomList[round]];
					break;
				case 5:
					game = game5.list[randomList[round]];
					break;
			}
			refresh();
		} else{
			level++;
			if(level==6)
				finishGame();
			round=0;
			switch (level){
			case 2:
//				this.getWindow().setBackgroundDrawableResource(R.drawable.gamebg2);
				levelText.setText("第二关");
				randomList = NewRandomList(game2.count-1);
				game = game2.list[randomList[round]];
				break;
			case 3:
//				this.getWindow().setBackgroundDrawableResource(R.drawable.gamebg3);
				levelText.setText("第三关");
				randomList = NewRandomList(game3.count-1);
				game = game3.list[randomList[round]];
				break;
			case 4:
//				this.getWindow().setBackgroundDrawableResource(R.drawable.gamebg4);
				levelText.setText("第四关");
				randomList = NewRandomList(game4.count-1);
				game = game4.list[randomList[round]];
				break;
			case 5:
//				this.getWindow().setBackgroundDrawableResource(R.drawable.gamebg5);
				levelText.setText("第五关");
				randomList = NewRandomList(game5.count-1);
				game = game5.list[randomList[round]];
				break;
				}
			refresh();
		}
	}
	
	private void finishGame() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("游戏结束，25道题目中，你答对了"+score+"道题~");
		builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		AlertDialog dialog = builder.create();
//		dialog.setCanceledOnTouchOutside(false);//对话框以外的地方不起作用。返回键还起作用
		dialog.setCancelable(false);//对话框以外的地方不起作用。返回键也不起作用
		dialog.show();
//		new Thread(){
//			public void run(){
//				try {
//					sleep(3000);
//					finish();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
	}

	private void refresh(){
		mTitle.setText(game[0]);
		a.setText(game[1]);
		b.setText(game[2]);
		if(game.length>5){
			c.setVisibility(View.VISIBLE);
			c.setText(game[3]);
			d.setVisibility(View.VISIBLE);
			d.setText(game[4]);
		}
		else if(game.length>4){
			c.setVisibility(View.VISIBLE);
			c.setText(game[3]);
			d.setVisibility(View.GONE);
		}
		else{
			c.setVisibility(View.GONE);
			d.setVisibility(View.GONE);
		}
	}
	
	private int[] NewRandomList(int max){
		 int[] intRet = new int[5]; 
         int intRd = 0; 
         int count = 0;
         int flag = 0;
         Random rdm = new Random(System.currentTimeMillis());
         while(count<5){
              intRd = rdm.nextInt(max);
              for(int i=0;i<count;i++){
                  if(intRet[i]==intRd){
                      flag = 1;
                      break;
                  }else{
                      flag = 0;
                      }
                  }
              if(flag==0){
                  intRet[count] = intRd;
                  count++;
                  }
              }
         return intRet;
	}
	
	public void onBackPressed(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("确定要退出游戏吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				GameActivity.super.onBackPressed();
			}
		});
		builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {}
			});
		builder.create().show();
	}
}
