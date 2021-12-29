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

	// �� ���� ���������
	int StoryListId; // StoryListId = �Ѱܹ޴� ��
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
	public String DB_NAME = "STORYALBUM.db"; // DB ������ �̸�
	public String TABLE_NAME = "STORYVIEW"; // Table�� �̸�
	public static final String TAG = "StoryView";
	LinearLayout textiv02;

	Thread th;

	// ������ �ڵ鷯, �޼����� ������ oncreateauto�޼ҵ� ����
	private Handler h = new Handler() {
		public void handleMessage(Message m) {
			
				
		//	Log.i(TAG, "�ڵ鷯 setContent()��");
				setContent();
			Log.i(TAG, "�ڵ鷯 setContent()��");
			
			cursor.moveToNext();
			Log.i(TAG, "�ڵ鷯 setContent()�� Ŀ���̵�");
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
		// Ÿ��Ʋ �޾Ƽ� �Է��� textview
		// ������ư ����

		ImageView editbtn01 = (ImageView) findViewById(R.id.EditBtn01);
		TextView textiv01 = (TextView) findViewById(R.id.TextIv01);
		textiv02 = (LinearLayout) findViewById(R.id.TextIv02);

		this.dBHandlingHelper = new DbAdapter(this);
		dBHandlingHelper.open();
		Log.i(TAG, "������ ����");
		m2();
		// intent���� �޾ƿ� fk�� �ο츦 �˻��ؼ� Ŀ���� ����
		Log.i(TAG, "����Ʈ���� �� : StoryListId = "+StoryListId+", albumId = "+albumId);
		cursor = dBHandlingHelper.whereIdStructure("STORYVIEW", 0, StoryListId);
		cursor2 = dBHandlingHelper.whereIdStructure("STORYLIST", 0, albumId);
		Log.i(TAG, "WhereIdStructure ����");
		cursor.moveToFirst();
		
		value = cursor.getCount();
		Log.i(TAG, "����� ����"+String.valueOf(value));
		cursor2.moveToFirst();
		title = cursor2.getString(1);
		Log.i(TAG, "���丮 Ÿ��Ʋ"+title);
		// textiv01.setTextColor(color.black);
		Log.i(TAG, "��");
		// textiv01.setTextSize(15);
		Log.i(TAG, "������");
		textiv01.setText(title);
		Log.i(TAG, "Ÿ��Ʋ �� ����");
		
		Log.i(TAG, "�� Ÿ��Ʋ");
		cursor.moveToFirst();
		th = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ������ �𸣳� post���� runnable. run������ ui�� ���������ϴٰ� ��.
				h.post(new Runnable() {
					@Override
					public void run() {
						// �� ������ ���̵� �������� Ȯ���ϰ� ����Ƽ ��ư���� ������ �ο��ϴ� id���� �������ش�.

						// value�� = Ŀ���� row �� = �ٹ� ����
						// value �� ��ŭ �ٹ����� �޼��� �ݺ� ����
						Log.i(TAG, "������ ���� ���� ��");
						for (int k = 0; k < value; k++) {
							Message m = h.obtainMessage();
							h.sendMessage(m);

						}

					}
				});
			}
		});
		// ������ ����
		th.start();
		
		editbtn01.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edit(); // ����
			}
		});

	}

	//
	//�޼ҵ��

	

	void setContent() {
		// settext
		// 1. text�䰡 �ڵ�����
		// 2. �Ķ���� ��, ���̵� ��
		// 3. ��� �߰�? textIv02?
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
		//�̹����� ���� ����
		//
		iv1.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize=4; // �̹����� 1/4�� ���
		orgImage = BitmapFactory.decodeFile(imagePath, options);
	//	image = Bitmap.createScaledBitmap(orgImage, 1024, 1024, true); // �̹�����
																		// 1024*1024��
																		// ����

		
		textiv02.addView(tv2);
		textiv02.addView(iv1);
		iv1.setImageBitmap(orgImage);
		Log.i(TAG, "��Ʈ�� �̹����� �̹����信 ����Ϸ�");
		textiv02.addView(tv1);
		
		Log.i(TAG, "�̹����н� : "+imagePath);
		//textiv02.addView(iv1);
	}

	void edit() {

	}

	void showMsg(String msg, int option) {
		Toast.makeText(this, msg, option).show();
	}

}
