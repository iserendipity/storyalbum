package airnodd.first.storyalbum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CopyOfStoryListActivity extends Activity implements OnItemClickListener {

	private ListView listView, lv;
	private static final String TAG = "StoryListActivity";
	private String text, img, path, alid;
	private static int check = 0;
	private Intent intent;
	private LinearLayout readyforstorylist01, storylist02;
	ArrayList<String> imgs = new ArrayList<String>();
	ArrayList<String> content = new ArrayList<String>();
	ArrayList<String> paths = new ArrayList<String>();
	List<HashMap<String, String>> aList;
	HashMap<String, String> hm;
	SimpleAdapter adapter;

	// ---------------------------------------------------------

	Cursor cursor;
	String tableName;
	StringBuffer sb;
	int id;
	String title;
	int color;
	String imagePath;
	String contents;
	int storyListId;
	int albumId;
	String columnId;
	int whatColumn;
	SQLiteDatabase db;
	private DbAdapter dBHandlingHelper;
	public String TABLE_NAME = "STORYLIST";
	int value;

	static int i = 1;

	Thread th;
	// fetchalldb - 테이블의 모든 데이터 select

	private Handler h = new Handler() {
		public void handleMessage(Message m) {
			//
			oncreateauto();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storylist);
		Button btn = (Button)findViewById(R.id.createButton01);
		btn.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				create();
			}
		});

		// fetchalldb - 테이블의 모든 데이터 select
		cursor = dBHandlingHelper.fetchAllDb("ALBUMLIST");
		cursor.moveToFirst();
		
		//커서의 모든 row 개수 확인
				value = cursor.getCount();

		m3(); // AlbumListActivity로부터 값을 받음
		Log.i(TAG, "albumId " + albumId);

		th = new Thread(new Runnable() {

			@Override
			public void run() {
				h.post(new Runnable() {

					@Override
					public void run() {
						cursor.moveToFirst();

						for (int k = 1; k <= value; k++) {
							Message m = h.obtainMessage();
							h.sendMessage(m);

						}

					}
				});

			}
		});

		cursor = dBHandlingHelper.whereIdStructure(TABLE_NAME, 0, albumId);

		if (cursor.moveToFirst()) {

			if (cursor.getCount() == 0) {
				// 초기값
				imgs.add(Integer.toString(R.drawable.startimage));
				content.add("클릭하면 이동합니다");
				paths.add("a");
			} else {
				do {
					// 값이 있는 만큼 story 생성 해주기
					// for(int i=0;i<cursor.getColumnCount();i++){
					// column은 0부터
					cursor.getInt(0);
					cursor.getString(1);
					cursor.getString(2);
					cursor.getInt(4);
					// }
					cursor.moveToNext();

				} while (cursor.moveToNext());
			}
		}
		Log.i(TAG, "실행 확인1");
		cursor.moveToFirst();
		Log.i(TAG, "실행 확인2");
		int value = cursor.getCount();
		Log.i(TAG, "실행 확인3");

		Button btn1 = (Button) findViewById(R.id.addview);
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addlayout();
			}
		});

		this.aList = new ArrayList<HashMap<String, String>>();

		basic(); // 기본값 입력 1

		repeat(); // for문

		String[] from = { "img", "text", "path" };
		int[] to = { R.id.img, R.id.text, R.id.path };

		this.adapter = new SimpleAdapter(getBaseContext(), this.aList,
				R.layout.listviewstorylist, from, to);

		listView = (ListView) findViewById(R.id.listviewStoryList01);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this); // 이벤트발생
	}
	void oncreateauto(){
		
	}
	void addlayout() { // 레이아웃, 리스트 추가
		storylist02 = new LinearLayout(CopyOfStoryListActivity.this);
		storylist02.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
	//	storylist02.setBackgroundResource(R.drawable.middle);

		readyforstorylist01 = (LinearLayout) findViewById(R.id.readyforstorylist01);
		readyforstorylist01.addView(storylist02);

		// ((ViewGroup)storylist03.getParent()).removeView(storylist03);

		lv = new ListView(CopyOfStoryListActivity.this);
		lv.setLayoutParams(new ListView.LayoutParams(
				android.widget.ListView.LayoutParams.MATCH_PARENT,
				android.widget.ListView.LayoutParams.WRAP_CONTENT));
	//	lv.setBackgroundResource(R.drawable.middle);
		lv.setCacheColorHint(Color.TRANSPARENT);

		storylist02.addView(lv);
		i++;
		lv.setId(i);
		// Log.i(TAG, "i : "+lv.getId());

		dBHandlingHelper.insertDb(TABLE_NAME, lv.getId(), title, albumId);
		// DB에 입력

	}

	void repeat() {
		for (int i = 0; i < imgs.size(); i++) {
			hm = new HashMap<String, String>();
			hm.put("text", content.get(i));
			hm.put("img", imgs.get(i));
			hm.put("path", paths.get(i));
		}
		this.aList.add(hm);
		Log.i(TAG, "hm.put 확인" + hm);

		check++; // 반복횟수확인
		Log.i(TAG, "repeat 호출 횟수 check : " + check);
	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// String idx = "Story1 - " + id;
		// showMsg("count 확인 : " + idx, 0);
		if (adapter.getItem(position) == aList.get(0)) {
			Intent intent = new Intent(this, CreateStoryActivity.class);
			startActivityForResult(intent, 0);

		} else {
			Intent intent = new Intent(this, StoryViewActivity.class);
			String datatext = aList.get(position).get("text");
			String dataimg = aList.get(position).get("img");
			String datapath = aList.get(position).get("path");
			intent.putExtra("datatext", datatext);
			intent.putExtra("dataflag", dataimg);
			intent.putExtra("datapath", datapath);

			Log.i(TAG, "datatext" + datatext);
			Log.i(TAG, "dataflag" + dataimg);
			Log.i(TAG, "datapath" + datapath);

			startActivity(intent);
		}
	}

	void m3() { // AlbumListActivity로부터 값을 받음
		Intent intent3 = getIntent();
		this.albumId = Integer.valueOf(intent3.getStringExtra("albumid"));
		Log.i(TAG, "AlbumId : " + this.albumId);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) { // StoryListActivity와 CreateAlbumActivity 소통
			if (resultCode == RESULT_OK) {
				text = intent.getStringExtra("text");
				img = intent.getStringExtra("img");
				path = intent.getStringExtra("absolute1Path");

				Log.i(TAG, "img : " + img);
				Log.i(TAG, "text : " + text);

				if (img != null) {
					Log.i(TAG, "img != 이고 if문 안쪽");
					imgs.add(img);
					Log.i(TAG, "flags.add 실행확인 ");
					content.add(text);
					paths.add(path);
					Log.i(TAG, "text.add 실행확인");
					repeat();
					Log.i(TAG, "이벤트안 repeat()실행완료");
					adapter.notifyDataSetChanged(); // ListView의 값이 변경되었음을 알림
				}
			}
		} else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				alid = intent.getStringExtra("alid");

				Log.i(TAG, "alid" + alid);
			}

		}
	}

	void basic() {
		imgs.add(Integer.toString(R.drawable.startimage));
		content.add("클릭하면 이동합니다");
		paths.add("a");

	}

	void showMsg(String msg, int option) {
		Toast.makeText(this, msg, option).show();
	}
	void create(){
		LinearLayout frame = new LinearLayout(CopyOfStoryListActivity.this);
		LinearLayout top = new LinearLayout(CopyOfStoryListActivity.this);
		LinearLayout middle01 = new LinearLayout(CopyOfStoryListActivity.this);
		LinearLayout middle02 = new LinearLayout(CopyOfStoryListActivity.this);
		LinearLayout bottom = new LinearLayout(CopyOfStoryListActivity.this);
		LinearLayout side01 = new LinearLayout(CopyOfStoryListActivity.this);
		LinearLayout side02 = new LinearLayout(CopyOfStoryListActivity.this);
		LinearLayout list = new LinearLayout(CopyOfStoryListActivity.this);
		ListView listview01 = new ListView(CopyOfStoryListActivity.this);
		ImageView imageview01 = new ImageView(CopyOfStoryListActivity.this);
		TextView textview01 = new TextView(CopyOfStoryListActivity.this);
		TextView path01 = new TextView(CopyOfStoryListActivity.this);
		
		LinearLayout readyforstorylist =(LinearLayout)findViewById(R.id.readyforstorylist01);
		
		frame.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		frame.setBackgroundResource(R.drawable.storylist);
		frame.setOrientation(LinearLayout.VERTICAL);

		top.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				0, 0.2360F));
		
		middle01.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				0, 0.6482F));
		middle01.setOrientation(LinearLayout.HORIZONTAL);

		bottom.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				0, 0.1158F));
				
		side01.setLayoutParams(new LinearLayout.LayoutParams(0, 
				LayoutParams.MATCH_PARENT, 0.1F));
		
		middle02.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT, 0.8F));
		middle02.setOrientation(LinearLayout.VERTICAL);
		middle02.setGravity(Gravity.CENTER);
		
		side02.setLayoutParams(new LinearLayout.LayoutParams(0, 
				LayoutParams.MATCH_PARENT, 0.1F));
				
		imageview01.setLayoutParams(new LinearLayout.LayoutParams(200, 
				200));
		
		textview01.setLayoutParams(new LinearLayout.LayoutParams(200, 
				200));
		
		path01.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT));
		path01.setVisibility(View.INVISIBLE);
		
		readyforstorylist.addView(frame);
		frame.addView(top);
		frame.addView(middle01);
		frame.addView(bottom);
		middle01.addView(side01);
		middle01.addView(middle02);
		middle01.addView(side02);
		middle02.addView(imageview01);
		
	}
}
