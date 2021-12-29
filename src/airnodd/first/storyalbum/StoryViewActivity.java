package airnodd.first.storyalbum;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.util.Log;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StoryViewActivity extends Activity {

	// 수 많은 멤버변수들
	int StoryListId; // StoryListId = 넘겨받는 값
	int value;
	static int i;
	Cursor cursor, cursor2, cursor3;
	String imagePath;
	String contents;
	String title;
	String dates;
	int albumId;
	Uri ImageView01Uri;
	Bitmap orgImage, image;
	
	private DbAdapter dBHandlingHelper;
	public int DB_MODE = Context.MODE_PRIVATE;
	public String DB_NAME = "STORYALBUM.db"; // DB 생성시 이름
	public String TABLE_NAME = "STORYVIEW"; // Table의 이름
	public static final String TAG = "StoryView";
	LinearLayout textiv02;

	Thread th;

	// 쓰레드 핸들러, 메세지를 받으면 oncreateauto메소드 실행
	private Handler h = new Handler() {
		public void handleMessage(Message m) {
			
				
		//	Log.i(TAG, "핸들러 setContent()전");
				setContent();
			Log.i(TAG, "핸들러 setContent()후");
			
			cursor.moveToNext();
			Log.i(TAG, "핸들러 setContent()후 커서이동");
		}
	};
	void m2(){
		Intent intent = getIntent();
		StoryListId = intent.getIntExtra("StoryListId", StoryListId);
		albumId = intent.getIntExtra("albumId", albumId);
		Log.i(TAG, String.valueOf(StoryListId));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storyview);
		// 타이틀 받아서 입력할 textview
		// 수정버튼 생성

		ImageView editbtn01 = (ImageView) findViewById(R.id.EditBtn01);
		TextView textiv01 = (TextView) findViewById(R.id.TextIv01);
		textiv02 = (LinearLayout) findViewById(R.id.TextIv02);

		this.dBHandlingHelper = new DbAdapter(this);
		dBHandlingHelper.open();
		Log.i(TAG, "디비오픈 성공");
		m2();
		// intent에서 받아온 fk의 로우를 검색해서 커서에 저장
		Log.i(TAG, "인텐트받은 값 : StoryListId = "+StoryListId+", albumId = "+albumId);
		cursor = dBHandlingHelper.whereIdStructure("STORYVIEW", 0, StoryListId);
		cursor2 = dBHandlingHelper.whereIdStructure("STORYLIST", 0, albumId);
		Log.i(TAG, "WhereIdStructure 성공");
		cursor.moveToFirst();
		
		value = cursor.getCount();
		Log.i(TAG, "밸류값 저장"+String.valueOf(value));
		cursor2.moveToFirst();
		title = cursor2.getString(1);
		Log.i(TAG, "스토리 타이틀"+title);
		// textiv01.setTextColor(color.black);
		Log.i(TAG, "색");
		// textiv01.setTextSize(15);
		Log.i(TAG, "사이즈");
		textiv01.setText(title);
		Log.i(TAG, "타이틀 값 저장");
		
		Log.i(TAG, "셋 타이틀");
		cursor.moveToFirst();
		th = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 이유는 모르나 post안의 runnable. run에서만 ui가 생성가능하다고 함.
				h.post(new Runnable() {
					@Override
					public void run() {
						// 맨 마지막 아이디가 무엇인지 확인하고 엠프티 버튼으로 생성시 부여하는 id값을 변경해준다.

						// value값 = 커서의 row 수 = 앨범 갯수
						// value 수 만큼 앨범생성 메세지 반복 전송
						Log.i(TAG, "쓰레드 포문 실행 전");
						for (int k = 0; k < value; k++) {
							Message m = h.obtainMessage();
							h.sendMessage(m);

						}

					}
				});
			}
		});
		// 쓰레드 시작
		th.start();
		
		editbtn01.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edit(); // 수정
			}
		});

	}

	//
	//메소드들

	

	void setContent() {
		// settext
		// 1. text뷰가 자동생성
		// 2. 파라메터 셋, 아이디 셋
		// 3. 어디에 추가? textIv02?
		contents = cursor.getString(4);
		dates=cursor.getString(5);
		imagePath=cursor.getString(2);
		TextView tv1 = new TextView(StoryViewActivity.this);
		TextView tv2 = new TextView(StoryViewActivity.this);
		ImageView iv1 = new ImageView(StoryViewActivity.this);
		tv1.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
		//tv1.setBackgroundResource(R.drawable.c_01);
		tv1.setTextColor(Color.DKGRAY);
		tv1.setText(contents);
		//tv1.setId(i);
		tv2.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
		tv2.setTextColor(Color.DKGRAY);
		tv2.setText(dates);
		//이미지를 만들어서 넣자
		//
		iv1.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize=4; // 이미지를 1/4로 축소
		orgImage = BitmapFactory.decodeFile(imagePath, options);
	//	image = Bitmap.createScaledBitmap(orgImage, 1024, 1024, true); // 이미지를
																		// 1024*1024로
																		// 고정

		
		textiv02.addView(tv2);
		textiv02.addView(iv1);
		iv1.setImageBitmap(orgImage);
		Log.i(TAG, "비트맵 이미지를 이미지뷰에 적용완료");
		textiv02.addView(tv1);
		
		Log.i(TAG, "이미지패스 : "+imagePath);
		//textiv02.addView(iv1);
	}

	void edit() {

	}

	void showMsg(String msg, int option) {
		Toast.makeText(this, msg, option).show();
	}

}
