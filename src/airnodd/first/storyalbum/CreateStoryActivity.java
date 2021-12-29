package airnodd.first.storyalbum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateStoryActivity extends Activity {
	Button button01, button02, button03;
	TextView tv01;
	EditText et01,et02;
	Uri ImageViewUri;
	int getLastId;
	int StoryListId;
	Cursor c, cursor;
	String tableName;
	StringBuffer sb;
	int id;
	String title;
	int color;
	String imagePath;
	String contents;
	String absolutePath;
	int storyListId;
	int albumId;
	String columnId;
	int whatColumn;
	Button btn1;
	int StoryViewId;
	String str;
	private DbAdapter dBHandlingHelper;
	public int DB_MODE = Context.MODE_PRIVATE;
	public String DB_NAME = "STORYALBUM.db"; // DB ������ �̸�
	public String TABLE_NAME = "STORYLIST"; // Table�� �̸�
	public static final String TAG = "CreateStoryActivity";
	// DatabaseOpenHelper openHelper;
	SQLiteDatabase db;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createstory);

		this.dBHandlingHelper = new DbAdapter(this);
		dBHandlingHelper.open();
		et01 = (EditText) findViewById(R.id.EditTextTitle);
		et02=(EditText)findViewById(R.id.et02);
		m3();
		Log.i(TAG, "����Ʈ���� �ٹ����̵� : " + albumId);
		Log.i(TAG, "storylistid : " + StoryListId);
		button01 = (Button) findViewById(R.id.ButtonForPlusImage01);
		button01.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				getImage(); // ���������� �̹��� �������� �޼ҵ�
			}
		});
		button02 = (Button) findViewById(R.id.ButtonForEnter02);
		tv01 = (TextView) findViewById(R.id.TextView_Content_StoryView);

		button02.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inputText(); // EditText���� �Է¹��� ���� TextView�� ����
			}
		});
		button03 = (Button) findViewById(R.id.ButtonForEnter03);
		button03.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent�ϴ� �޼ҵ�
				move();
			}
		});
		
		Log.i(TAG, "������� �����");
	//	getLastId = dBHandlingHelper.getLastRowId("STORYVIEW");
		//Log.i(TAG, "���丮�� ���̺��� getlastrowid : "+String.valueOf(getLastId));
		//getLastId = getLastId + 1;
		cursor = dBHandlingHelper.fetchAllDb("STORYVIEW");
		if(cursor.getCount() != 0){
			cursor.moveToLast();
			this.getLastId=cursor.getInt(0)+1;
			Log.i(TAG, "getLastId : "+String.valueOf(getLastId));
			
		}else{
			this.getLastId = 1;
		}
	}
		void m3(){
			Intent intent = getIntent();
			StoryListId = intent.getIntExtra("StoryListId", StoryListId);
			albumId=intent.getIntExtra("albumId", albumId);
			Log.i(TAG, "����Ʈ���� �ٹ����̵� : " + albumId);
			Log.i(TAG, "storylistid : " + StoryListId);
		}
		
	

	void move() {// Intent �ϴ� �޼ҵ�
		imagePath = ImageViewUri.toString();
		Log.i(TAG, "imagePath : "+imagePath);
		title = et01.getText().toString();
		Log.i(TAG, "getLastId : "+getLastId);
		Log.i(TAG, "StoryListId"+StoryListId);
		dBHandlingHelper.insertDb("STORYVIEW", getLastId, title, absolutePath, imagePath, contents, StoryListId);
		 // FK
		Log.i(TAG, "�μ�Ʈ ��� ����");
		Intent intent = new Intent(this, StoryListActivity.class);
		intent.putExtra("StoryListId", StoryListId);
		intent.putExtra("albumId", albumId);
		Log.i(TAG, "StoryListId�� �� : " + StoryListId);
		startActivity(intent);
		//�Է¾����� ���� �߻���		
	}

	void inputText() { // EditText���� �Է¹��� ���� TextView�� �����ϴ� �޼ҵ�
		this.contents = et02.getText().toString();
		if(contents.length()!=0){
		tv01.setText(contents);
		} else{
		Toast.makeText(CreateStoryActivity.this, "������ �Է��ϼ���", 0).show();
	}
	}

	void getImage() { // ���������� �̹��� �������� �޼ҵ�
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, 0);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == Activity.RESULT_OK) {
				ImageView img = (ImageView) findViewById(R.id.ImageView01_Center_StoryView);
				ImageViewUri = data.getData();

				c = getContentResolver().query(
						Uri.parse(ImageViewUri.toString()), null, null, null,
						null);
				c.moveToNext();
				this.absolutePath = c.getString(c
						.getColumnIndex(MediaStore.MediaColumns.DATA));

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap orgImage = BitmapFactory.decodeFile(absolutePath,
						options);
				Bitmap bitmap = Bitmap.createScaledBitmap(orgImage, 1024, 1024,
						true);

				img.setImageBitmap(bitmap);
			}
		}
	}
}
