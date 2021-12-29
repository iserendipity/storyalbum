package airnodd.first.storyalbum;

import android.app.Activity;
import java.lang.String.*;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.SyncStateContract.Columns;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button buttonSelectAll, buttonDropTable, buttonInsertDb, buttonUpdateDb, buttonDeleteDb,
			buttonWhereId, buttonGetLastId;
	EditText et1TableName;
	EditText et2ColumnId;
	EditText et3ColumnTitle;
	EditText et4ColumnImagePath;
	EditText et5ColumnContents;
	EditText et6ColumnColor;
	//EditText et6ColumnDate;
	EditText et7ColumnAlbumId_FK;
	EditText et8ColumnStoryListId_FK;
	EditText et9whatColumn;
	EditText et10ColumnAbsImgPath;
	TextView tvResultView;
	public static final String TAG = "MainActivity";
	
	//꼭선언해 줘야 하는 멤버변수
		Cursor cursor;
		String tableName;
		StringBuffer sb;
		int id;
		String title;
		int color;
		String absImgPath;
		String imagePath;
		String contents;
		int storyListId;
		int albumId;
		String columnId;
		int whatColumn;
		// DatabaseOpenHelper openHelper;
		SQLiteDatabase db;
		private DbAdapter dBHandlingHelper;		
	//여기까지는 꼭 선언해 줘야 함. 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// storyAlbum.db를 생성 및 오픈하고, Create문을 만들어준다.
		Log.i(TAG, "DbAdapter dBHandling 생성 및 open() 호출");
		this.dBHandlingHelper = new DbAdapter(this);
		dBHandlingHelper.open();
		Log.i(TAG, "DbAdapter dBHandling.open() 완료");
		
		// insert Test
		// dBHandlingHelper.insertDb("앨범의 제목 컬럼");
		//findViewById를 실행한다.
		Log.i(TAG, "FindViewById() 호출");
		callFindViewById();
		Log.i(TAG, "FindViewById() 완료");
		
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sb = new StringBuffer();
				
				
				

				Log.i(TAG, "MainActivity.onClick()");
			
				// 사용자가 버튼 클릭 시 발생하는 이벤트
				// 1.실행 버튼을 누를 시
				// 1) TextView tvResultView에 적은 값을 읽는다. 읽은 값은 table의 이름이다.
				// 2) 입력 받은 table의 모든 정보를 출력한다.

				// EditText etvResultView에 있는 값을 읽어 들일때 String 형으로 변환이 필요하여,
				// 삽입함:
				// String editableConvert2String(EditText e1);
				MainActivity.this.tableName = editableConvert2String(et1TableName);
				
				
				//getValuesFromView의 하는 일
				//1. 각 vaule들을 View에서 받아오고 멤버변수에 저장하여 놓는다.
				getValuesFromView();
				
				// checkTableName의 하는 일
				//1. tableName이 albumlist, storyList, storyView인지 확인하여 준다.
				//2. return 값은 int 형이며, 1은 albumlist, 2은 storyList, 3은 storyView를 사용한다.
				int table = checkTableName(tableName);
				
				
				Log.i(TAG, "int table : " + table);
				if (v == buttonSelectAll) {

					selectAll(v, tableName, sb);
				}
				// 2.Db삭제를 누를 시
				// 1) TextView tvResultView에 적은 값을 읽는다. 읽은 값은 table의 이름이다.
				// 2) 입력 받은 table을 삭제한다.
				else if (v == buttonDropTable) {

					dropTable(v, tableName, sb);

				} else if (v == buttonInsertDb) {
					// Insert

					Log.i(TAG, "buttonInsertDb : " + buttonInsertDb);
					Log.i(TAG, "checkTableName 호출");
					//int table = checkTableName(tableName); 를 통해서 table의 따라서 index를 받아온다. 
						switch(table){
							//1. albumList, 2. storyList, 3. storyView 
							case 1: dBHandlingHelper.insertDb(tableName, id, title, color); break;
							case 2: dBHandlingHelper.insertDb(tableName, id, title, albumId); break;
							case 3: dBHandlingHelper.insertDb(tableName, id, title, absImgPath, imagePath, contents, storyListId); break;	
						
						}
						Log.i(TAG, "dBHandlingHelper.insertDb() 완료");
						// 전체 부름
						selectAll(v, tableName, sb);
						
					}else if (v == buttonUpdateDb){
						
						//해당 테이블과 Id를 수정함.
						//update for albumlist
						//public void updateDb(String tableName, int whatColumn, int id, String newTitle, int newColor){}
						//메소드 오버라이딩
						//albumList or storyList 일때 변경시
						//newTitle과 newColorOrAlbum : int whatColumn = 3
						//newTitle을 변경 : int whatColumn = 1
						//newColor을 변경 : int whatColumn = 2
						//Example ) albumList or storyList를 위한 수정사항
						//1. newTitle 일때 	updateDb(albumList, 1 ,1, 새로운테이블명, 0);
						//2. newColor 일때 	updateDb(albumList, 2, 1, null, 	  1);
						//3. 둘다일 경우	  	updateDb(albumList, 3, 3, 새로운테이블명, 2);
						Log.i(TAG, "dBHandlingHelper.buttonUpdateDb() 수행");
						
						
						//updateDb for storyList
						//updateDb(String tableName, int id, String newTitle)
						//title만 고침 int whatColumn 안씀 	
						
						switch(table){
							//1. albumList, 2. storyList, 3. storyView 
							//public void updateDb(String tableName, int whatColumn, int id, String newTitle, int newColorOrAlbumId){}
							case 1: dBHandlingHelper.updateDb(tableName, whatColumn, id, title, color); break;
							case 2: dBHandlingHelper.updateDb(tableName, id, title); break;
							case 3: dBHandlingHelper.updateDb(tableName, whatColumn, id, title, absImgPath, imagePath, contents); break;	
						
							
							//메소드 오버라이딩
							//update for storyview
							//newTitle, newContent, newImagePath 일때 : int whatColumn = 4
							//newTitle을 변경 : int whatColumn = 1
							//newContent 변경 : int whatColumn = 2
							//newImagePath 변경 : int whatColumn = 3
							//Example ) albumList or storyList를 위한 수정사항
							//1. newTitle 일때    updateDb(storyView, 1 ,1, 새로운테이블명, 기존내용, 기존경로);
							//2. newContent 일때  updateDb(storyView, 1, 1, 기존테이블명, 	새로운 내용, 기존경로);
							//3. newImagePath일때 updateDb(storyView, 2, 1, 기존테이블명, 	기존내용,  새로운 경로);
							//4. 둘다일 경우	  	updateDb(storyView, 4 ,1, 새로운테이블명, 새로운 내용, 새 이미지 경로);
						//	public void updateDb(String tableName, int whatColumn, int id, String newTitle, String newContent, String newImagePath)
						}
						Log.i(TAG, "dBHandlingHelper.buttonUpdateDb() 완료");
						
						//전체 부름 select * from tableName;
						selectAll(v, tableName, sb);
						
						
					}else if (v == buttonDeleteDb){
						//columnId = null;
							Log.i(TAG, "buttonDeleteDb, int table : "+ table );
							/*
							switch(table){
							//1. albumList, 2. storyList, 3. storyView 
								case 1: columnId = DtoQuery.COLUMN_ALBUM_ID;  break;
								case 2: columnId = DtoQuery.COLUMN_STORYLIST_ID; break;
								case 3: columnId = DtoQuery.COLUMN_STORYVIEW_ID; break;	
							}*/
							//Log.i(TAG, "buttonDeleteDb, columnId : "+columnId);
							Log.i(TAG, "buttonDeleteDb, deleteDb() 호출");
							//delete 호출
							dBHandlingHelper.deleteDb(tableName, id);
							Log.i(TAG, "buttonDeleteDb, deleteDb() 수행완료");
							Log.i(TAG, "buttonDelete id : "+id);
							
							// 전체 부름
							selectAll(v, tableName, sb);
							
					}else if (v == buttonWhereId){
						Log.i(TAG, "buttonWhereId.whereIdStructure 호출");
						switch(table){
						//1. albumList, 2. storyList, 3. storyView 
							case 1: cursor = dBHandlingHelper.whereIdStructure(tableName, id, 0); break;
							case 2: cursor = dBHandlingHelper.whereIdStructure(tableName, id, albumId); break;
							case 3: cursor = dBHandlingHelper.whereIdStructure(tableName, id, storyListId); break;
							}
						
						//cursor의 있는 값을 String Buffer sb로 쓰고, sb값을 TextView tvResultView로 출력한다. 
						sb = dBHandlingHelper.selectWriteInBuffer(v, cursor);
						tvResultView.setText(sb.toString());
						Log.i(TAG, "buttonWhereId.whereIdStructure 완료");
						
					}else if (v == buttonGetLastId){
						Log.i(TAG, "buttonGetLastId 완료");
						int lastValue;
						lastValue = dBHandlingHelper.getLastRowId(tableName); 
						tvResultView.setText("해당 마지막 id 값은 : "+lastValue);
						Log.i(TAG, "buttonGetLastId 완료");
					}			
				}
		};
		
		Log.i(TAG, "setOnClickListener() 호출");
		// setOnClickListener(listener)를 메소드로 묶어서 호출 하였음. 
		callSetOnClickListener(listener);
		Log.i(TAG, "setOnClickListener() 완료");
	}
	
	//getValuesFromView의 하는 일
	//1. 각 vaule들을 View에서 받아오고 멤버변수에 저장하여 놓는다.
	private void getValuesFromView() {
		this.id = editableConvert2Integer(et2ColumnId);
		this.title = editableConvert2String(et3ColumnTitle);
		this.absImgPath = editableConvert2String(et10ColumnAbsImgPath);
		this.color = editableConvert2Integer(et6ColumnColor);
		this.imagePath =editableConvert2String(et4ColumnImagePath);
		this.contents = editableConvert2String(et5ColumnContents);
		this.albumId = editableConvert2Integer(et7ColumnAlbumId_FK);
		this.storyListId = editableConvert2Integer(et8ColumnStoryListId_FK);
		this.whatColumn = editableConvert2Integer(et9whatColumn);
		
		Log.i(TAG, "EditText에서 각 컬럼에 대한 값을 받아옴.");
		Log.i(TAG, "table 명은 : " + tableName);
		Log.i(TAG, "id : "+id);
		Log.i(TAG, "title : "+title);
		Log.i(TAG, "color : "+color);
		Log.i(TAG, "absImgPate : "+absImgPath);
		Log.i(TAG, "imagePate : "+imagePath);
		Log.i(TAG, "contents : "+contents);
		Log.i(TAG, "albumId : "+albumId);
		Log.i(TAG, "storyListId : "+storyListId);
		Log.i(TAG, "whatColumn : "+whatColumn);
	}
	// checkTableName의 하는 일
	//1. tableName이 albumlist, storyList, storyView인지 확인하여 준다.
	//2. return 값은 int 형이며, 1은 albumlist, 2은 storyList, 3은 storyView를 사용한다.
	private int checkTableName(String tableName) {
		int table;
		Log.i(TAG, "checkTableNameAndInsertToTable : " + tableName);
		if (tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_ALBUMLIST)) {
			Log.i(TAG, "if  : " + tableName);
			table = 1;

		} else if (tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_STORYLIST)) {
			Log.i(TAG, "else if  : " + tableName);
			table = 2;

		} else {
			Log.i(TAG, "else  : " + tableName);
			table = 3;
		}
		
		return table;
	}

	private void callSetOnClickListener(View.OnClickListener listener) {
		
		Log.i(TAG, "buttonSelectAll.setOnClickListener : ");
		Log.i(TAG, "buttonDropTable.setOnClickListener : ");
		Log.i(TAG, "buttonInsertDb.setOnClickListener : ");
		Log.i(TAG, "buttonUpdateDb.setOnClickListener : ");
		Log.i(TAG, "buttonDeleteDb.setOnClickListener : ");
		Log.i(TAG, "buttonWhereId.setOnClickListener : ");
		Log.i(TAG, "buttonGetLastId.setOnClickListener : ");
	
		this.buttonSelectAll.setOnClickListener(listener);
		this.buttonDropTable.setOnClickListener(listener);
		this.buttonInsertDb.setOnClickListener(listener);
		this.buttonUpdateDb.setOnClickListener(listener);
		this.buttonDeleteDb.setOnClickListener(listener);
		this.buttonWhereId .setOnClickListener(listener);
		this.buttonGetLastId.setOnClickListener(listener);
		
		Log.i(TAG, "et1TableName.setOnClickListener");
		Log.i(TAG, "et2ColumnId.setOnClickListener");
		Log.i(TAG, "et3ColumnTitle.setOnClickListener");
		Log.i(TAG, "et6ColumnColor.setOnClickListener");
		Log.i(TAG, "et10ColumnAbsImgPath.setOnClickListener");
		Log.i(TAG, "et4ColumnImagePath.setOnClickListener");
		Log.i(TAG, "et5ColumnContents.setOnClickListener");
		Log.i(TAG, "et7ColumnAlbumId_FK.setOnClickListener");
		Log.i(TAG, "et8ColumnStoryListId_FK.setOnClickListener");
		Log.i(TAG, "et9whatColumn.setOnClickListener");
		
		this.et1TableName.setOnClickListener(listener);
		this.et2ColumnId.setOnClickListener(listener);
		this.et3ColumnTitle.setOnClickListener(listener);
		this.et6ColumnColor.setOnClickListener(listener);
		this.et10ColumnAbsImgPath.setOnClickListener(listener);
		this.et4ColumnImagePath.setOnClickListener(listener);
		this.et5ColumnContents.setOnClickListener(listener);
		this.et7ColumnAlbumId_FK.setOnClickListener(listener);
		this.et8ColumnStoryListId_FK.setOnClickListener(listener);
		this.et9whatColumn.setOnClickListener(listener);
		

		Log.i(TAG, "tvResultView.setOnClickListener : ");
		this.tvResultView.setOnClickListener(listener);
		
	}

	// EditText에 입력한 값을 String으로 변환해주는 역할을 담당.
	public String editableConvert2String(EditText e1) {

		String str = null;
		// tableName,columnId 가공
		str = e1.getText() + "";
		// columnId = et2.getText()+"";

		str = str.trim();
		// columnId=columnId.trim();
		
		Log.i(TAG, "editableConvert2String str : " + str);

		// test

		return str;
	}
	// EditText에 입력한 값을 INTEGER으로 변환해주는 역할을 담당. 
	public int editableConvert2Integer(EditText e) {
	
		int in;
		String str;
		
		str = e.getText()+"";
		str = str.trim();
		
		try {
			in = Integer.parseInt(str);
			Log.i(TAG, "editableConvert2Integer in : " + in);
			return in;
			
		}catch(NumberFormatException nfe){
			Log.i(TAG, "editableConvert2Integer in 은 Exception에 걸림");
			in = 0;
			return in;
		}
		
		
	}
	

	// selectAll은 DbAdapter.fetchAllDb()에서 반환받은 cursor을 이용하여 TextView에 뿌려주는 역할을
	// 한다.
	void selectAll(View v, String tableName, StringBuffer sb) {

		// fetchAllDb는 select * from talbeName order by columnId desc; 를 뜻한다.
		Log.i(TAG, "MainActivity.onClick().dBHandlingHelper.fetchAllDb() : " + tableName);
		cursor = dBHandlingHelper.fetchAllDb(tableName);

		Log.i(TAG,
				"MainActivity.onClick().dBHandlingHelper.selectWriteInBuffer()");
		sb = dBHandlingHelper.selectWriteInBuffer(v, cursor);
		tvResultView.setText(sb.toString());

	}

	// dropTable은 해당 table을 삭제 및 재 생성 하는 역할을 한다.
	void dropTable(View v, String tableName, StringBuffer sb) {
		// tableName

		Log.i(TAG, "MainActivity.onClick().dBHandlingHelper.dropDb()");

		try {
			dBHandlingHelper.dropDp(tableName);
			sb.append("테이블들이 삭제 되었습니다.\n");
			tvResultView.setText(sb);
			
			//table 재생성
			dBHandlingHelper.open();
			
			sb.append("테이블들이 재 생성되었습니다.\n");
			tvResultView.setText(sb);
			
		} catch (NullPointerException npe) {
			Log.i(TAG, "DropTable  후 재 생성 NullPointException" + npe);
		}
	}

	private void showMsg(String msg, int option) {
		Toast.makeText(getBaseContext(), msg, option).show();

	}


	void callFindViewById() {
		Log.i(TAG, "buttonSelectAll findByViewId");
		Log.i(TAG, "buttonDropTable findByViewId");
		Log.i(TAG, "buttonInsertDb findByViewId");
		Log.i(TAG, "buttonUpdateDb findByViewId");
		Log.i(TAG, "buttonDeleteDb findByViewId");
		Log.i(TAG, "buttonWhereId findByViewId");
		Log.i(TAG, "buttonGetLastId findByViewId");
		this.buttonSelectAll = (Button) findViewById(R.id.ButtonAdd);
		this.buttonDropTable = (Button) findViewById(R.id.ButtonDeleteDb);
		this.buttonInsertDb = (Button) findViewById(R.id.ButtonInsertDb);
		this.buttonUpdateDb = (Button) findViewById(R.id.ButtonUpdateDb);
		this.buttonDeleteDb = (Button) findViewById(R.id.ButtonDeleteId);
		this.buttonWhereId = (Button) findViewById(R.id.ButtonWhereId);
		this.buttonGetLastId=(Button) findViewById(R.id.ButtonGetLastIdValue);
		

		Log.i(TAG, "et1TableName findByViewId");
		Log.i(TAG, "et2ColumnId findByViewId");
		Log.i(TAG, "et3ColumnTitle findByViewId");
		Log.i(TAG, "et6ColumnColor findByViewId");
		Log.i(TAG, "EditText10ColumnAbsImagePath findByViewId");
		Log.i(TAG, "et4ColumnImagePath findByViewId");
		Log.i(TAG, "et5ColumnContents findByViewId");
		Log.i(TAG, "et7ColumnAlbumId_FK findByViewId");
		Log.i(TAG, "et8ColumnStoryListId_FK findByViewId");
		Log.i(TAG, "et9ColumnDate findByViewId");
		
		
		this.et1TableName = (EditText) findViewById(R.id.EditText1TableName);
		this.et2ColumnId = (EditText) findViewById(R.id.EditText2ColumnId);
		this.et3ColumnTitle = (EditText) findViewById(R.id.EditText3ColumnTitle);
		this.et6ColumnColor = (EditText) findViewById(R.id.EditText6ColumnColor);
		this.et10ColumnAbsImgPath = (EditText) findViewById(R.id.EditText10ColumnAbsImagePath);
		this.et4ColumnImagePath = (EditText) findViewById(R.id.EditText4ColumnImagePath);
		this.et5ColumnContents = (EditText) findViewById(R.id.EditText5ColumnContents);
		this.et7ColumnAlbumId_FK = (EditText) findViewById(R.id.EditText7ColumnAlbumId_For_FK);
		this.et8ColumnStoryListId_FK = (EditText) findViewById(R.id.EditText8ColumnStoryList_For_FK);
		this.et9whatColumn = (EditText) findViewById(R.id.EditText9whatColumn);
		
		
		Log.i(TAG, "tvResultView findByViewId");
		this.tvResultView = (TextView) findViewById(R.id.TextViewData01);
	}
		
}