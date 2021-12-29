package airnodd.first.storyalbum;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class StoryListActivity extends Activity {

	private static final String TAG = "StoryListActivity";
	private String text, img, path, alid;

	private Intent intent;
	private LinearLayout readyforstorylist01, storylist02;

	// ---------------------------------------------------------

	Cursor cursor, cursor2;
	String tableName;
	int id;
	String title;
	int color, StoryListId;

	int albumId;
	String columnId;
	int whatColumn;
	SQLiteDatabase db;
	private DbAdapter dBHandlingHelper;
	public String TABLE_NAME = "STORYLIST";
	int value, value2;
	String contents, imagePath, dates;
	Bitmap orgImage;
	ImageView imageview01;

	static int i;
	Thread th;
	private Handler h = new Handler() {
		public void handleMessage(Message m) {
			//
			create();
			// Toast.makeText(StoryListActivity.this,
			// "�ڵ����� �����尡 �����ϴ� �޼ҵ� ����"+value+"ȸ", 0).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storylist);

		Button btn = (Button) findViewById(R.id.createButton01);
		
		this.dBHandlingHelper = new DbAdapter(this);
		dBHandlingHelper.open();

		// fetchalldb - ���̺��� ��� ������ select
		cursor = dBHandlingHelper.whereIdStructure("STORYVIEW", 0, StoryListId);
		cursor2 = dBHandlingHelper.whereIdStructure("STORYLIST", 0, albumId);
		// cursor2.moveToFirst();
		// StoryListId = cursor2.getInt(0);
		Log.i(TAG, "storylistid : " + StoryListId);
		cursor2.moveToFirst();
		cursor.moveToFirst();
		value2 = cursor2.getCount();
		value = cursor.getCount();
		Log.i(TAG, "��� Ȯ��! value : " + String.valueOf(value));
		Log.i(TAG, "��� Ȯ��! value2 :" + String.valueOf(value2));
		if (value2 != 0) {
			cursor2.moveToLast();
			i = cursor2.getInt(0) + 1;
			Log.i(TAG, "���������� �߰��Ǿ�� �ϴ� ���̵� ��=���� �� �Ʒ� Row�� id��  + 1 : " + i);
		} else {
			i = 1;
		}

		// ������ ����
		th = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ������ �𸣳� post���� runnable. run������ ui�� ���������ϴٰ� ��.
				h.post(new Runnable() {
					@Override
					public void run() {
						// �� ������ ���̵� �������� Ȯ���ϰ� ����Ƽ ��ư���� ������ �ο��ϴ� id���� �������ش�.
						Log.i(TAG, "������ ��");
						cursor.moveToFirst();
						// value�� = Ŀ���� row �� = �ٹ� ����
						// value �� ��ŭ �ٹ����� �޼��� �ݺ� ����
						for (int l = 0; l < value2; l++) {

							Message m = h.obtainMessage();
							h.sendMessage(m);

						}

					}
				});
			}
		});
		// ������ ����
		th.start();
		m1();
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				titledialog();
			}
		});

	}

	// �޼ҵ�
	void m1() {
		Intent intent = getIntent();
		albumId = intent.getIntExtra("albumId", albumId);
		StoryListId = intent.getIntExtra("StoryListId", StoryListId);
		Log.i(TAG, "����Ʈ���� �ٹ����̵� : " + albumId);
		Log.i(TAG, "storylistid : " + StoryListId);
	}

	void create() {// ����
		LinearLayout frame = new LinearLayout(StoryListActivity.this);
		LinearLayout top = new LinearLayout(StoryListActivity.this);
		LinearLayout middle01 = new LinearLayout(StoryListActivity.this);
		LinearLayout middle02 = new LinearLayout(StoryListActivity.this);
		LinearLayout bottom = new LinearLayout(StoryListActivity.this);
		LinearLayout side01 = new LinearLayout(StoryListActivity.this);
		LinearLayout side02 = new LinearLayout(StoryListActivity.this);
		ImageView imageview01 = new ImageView(StoryListActivity.this);
		TextView textview01 = new TextView(StoryListActivity.this);
		TextView path01 = new TextView(StoryListActivity.this);
		LinearLayout readyforstorylist = (LinearLayout) findViewById(R.id.readyforstorylist01);

		frame.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		frame.setBackgroundResource(R.drawable.storylist);
		frame.setOrientation(LinearLayout.VERTICAL);

		top.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 0.2360F));

		middle01.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 0.6482F));
		middle01.setOrientation(LinearLayout.HORIZONTAL);

		bottom.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 0.1158F));

		side01.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, 0.1F));

		middle02.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.8F));
		middle02.setOrientation(LinearLayout.VERTICAL);
		middle02.setGravity(Gravity.CENTER);
		side02.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, 0.1F));

		imageview01.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
		imageview01.setBackgroundColor(Color.BLACK);
		textview01.setLayoutParams(new LinearLayout.LayoutParams(100, 200));

		path01.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		path01.setVisibility(View.INVISIBLE);

		readyforstorylist.addView(frame);
		frame.addView(top);
		frame.addView(middle01);
		frame.addView(bottom);

		middle01.addView(side01);
		middle01.addView(middle02);
		middle01.addView(side02);
	//	middle02.addView(btntocreate);
		middle02.addView(imageview01);
		middle02.addView(textview01);
		imageview01.setId(i);
		Log.i(TAG, "����Ű �ٹ� id : " + albumId);

		imageview01.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "v.getid : " + String.valueOf(v.getId()));
				dBHandlingHelper.insertDb(TABLE_NAME, v.getId(), title, albumId);
				intent(v.getId());

			}
		});

	}

/*	void autocreate() {

		// image = Bitmap.createScaledBitmap(orgImage, 1024, 1024, true); //
		// �̹�����
		// 1024*1024��
		// ����
		LinearLayout frame = new LinearLayout(StoryListActivity.this);
		LinearLayout top = new LinearLayout(StoryListActivity.this);
		LinearLayout middle01 = new LinearLayout(StoryListActivity.this);
		LinearLayout middle02 = new LinearLayout(StoryListActivity.this);
		LinearLayout bottom = new LinearLayout(StoryListActivity.this);
		LinearLayout side01 = new LinearLayout(StoryListActivity.this);
		LinearLayout side02 = new LinearLayout(StoryListActivity.this);
		LinearLayout list = new LinearLayout(StoryListActivity.this);
		ListView listview01 = new ListView(StoryListActivity.this);
		ImageView imageview01 = new ImageView(StoryListActivity.this);
		TextView textview01 = new TextView(StoryListActivity.this);
		TextView path01 = new TextView(StoryListActivity.this);
		Button btntocreate = new Button(StoryListActivity.this);
		LinearLayout readyforstorylist = (LinearLayout) findViewById(R.id.readyforstorylist01);

		frame.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		frame.setBackgroundResource(R.drawable.storylist);
		frame.setOrientation(LinearLayout.VERTICAL);

		top.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 0.2360F));

		middle01.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 0.6482F));
		middle01.setOrientation(LinearLayout.HORIZONTAL);

		bottom.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 0.1158F));

		side01.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, 0.1F));

		middle02.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.8F));
		middle02.setOrientation(LinearLayout.VERTICAL);
		middle02.setGravity(Gravity.CENTER);
		btntocreate.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
		btntocreate.setBackgroundResource(R.drawable.enterbuttons);
		side02.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, 0.1F));

		imageview01.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

		textview01.setLayoutParams(new LinearLayout.LayoutParams(100, 200));

		path01.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		path01.setVisibility(View.INVISIBLE);

		readyforstorylist.addView(frame);
		frame.addView(top);
		frame.addView(middle01);
		frame.addView(bottom);
		Log.i(TAG, "������ �Դ�");
		middle01.addView(side01);
		middle01.addView(middle02);
		middle01.addView(side02);
		middle02.addView(btntocreate);
		middle02.addView(imageview01);
		middle02.addView(textview01);
		btntocreate.setId(i);
		Log.i(TAG, "����Ű �ٹ� id : " + albumId);

		contents = cursor.getString(4);
		imagePath = cursor.getString(2);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4; // �̹����� 1/4�� ���
		orgImage = BitmapFactory.decodeFile(imagePath, options);

		imageview01.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
		imageview01.setImageBitmap(orgImage);
		textview01.setLayoutParams(new LinearLayout.LayoutParams(100, 200));
		textview01.setText(contents);
		btntocreate.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
		btntocreate.setBackgroundResource(R.drawable.enterbuttons);

		btntocreate.setId(i);
		middle02.addView(btntocreate);
		middle02.addView(imageview01);
		middle02.addView(textview01);

		btntocreate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "v.getid : " + String.valueOf(v.getId()));
				dBHandlingHelper.insertDb(TABLE_NAME, v.getId(), title, albumId);
				intent(v.getId());

			}
		});

	}
*/
	void titledialog() {
		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.albumlistcustomdialog,
				(ViewGroup) findViewById(R.id.layout_root));

		// ��ó:[�ڵ�] �ȵ���̵� AlertDialog�� �α���â�����
		AlertDialog.Builder builder = new AlertDialog.Builder(
				StoryListActivity.this);
		builder.setTitle("������ �Է����ּ���");
		builder.setView(layout);

		builder.setNegativeButton("���", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		builder.setPositiveButton("�Ϸ�", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				EditText dialoget = (EditText) ((AlertDialog) dialog)
						.findViewById(R.id.dialoget01);
				title = dialoget.getText().toString();
				if (title.length() == 0) {
					Toast.makeText(StoryListActivity.this, "������ �Է��ϼ���", 0).show();
					return;
				} else {

					StoryListActivity.this.title = title;
					StoryListId ++;
					intent();
					
				}
			}
		});
		builder.show();
	}

	void intent() {
		Intent intent = new Intent(this, CreateStoryActivity.class);
		intent.putExtra("StoryListId", StoryListId);
		intent.putExtra("albumId", albumId);
		Log.i(TAG, "StoryListId�� �� : " + StoryListId);
		startActivity(intent);
	}
	void intent(int k){
		Intent intent = new Intent(this, StoryListActivity.class);
		intent.putExtra("StoryListId", k);
		intent.putExtra("albumId", albumId);
		Log.i(TAG, "StoryListId�� �� : " + StoryListId);
		startActivity(intent);
	}
}
