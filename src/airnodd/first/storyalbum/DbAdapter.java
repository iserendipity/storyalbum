package airnodd.first.storyalbum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//DbAapter의 역할: 
//1. Database의 파일이름을 "storyAlbum.db"로 생성
//2. Database의 테이블 생성, 삭제
//3. insert, update, delete 수행하여 cursor나 해당 albumId를  반납하게 함.

public class DbAdapter {

	Context context;
	SQLiteOpenHelper openHelper = null;
	SQLiteDatabase database = null;
	
	//updateDb() table명, column명 보증해주기 위한 변수 
	String tableColumnTitle;
	String tableColumnThird;
	Cursor cursor;
	String columnIdFK;
	int lastIdValue;

	public static final String TAG = "DbAdapter";

	public DbAdapter(Context context) {

		super();
		this.context = context;
		Log.i(TAG, "DbAdapter  ");
	}

	class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context) {
			super(context, DtoQuery.DB_NAME, null, DtoQuery.DB_VERSION);
			Log.i(TAG, "DBHelper constructor");
		}

		public void onCreate(SQLiteDatabase database) {

			Log.i(TAG, "DBHelper onCreate()");

			database.execSQL(DtoQuery.CREATE_TABLE_ALBUMLIST);
			database.execSQL(DtoQuery.CREATE_TABLE_STORYLIST);
			database.execSQL(DtoQuery.CREATE_TABLE_STORYVIEW);
			Log.i(TAG, " : " + database);

		}

		public void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			Log.i(TAG, "DBHelper onUpgrade");
			
			//FOREIGN KEY 제약 때문에 하위 테이블 부터 지워준다. 
			database.execSQL("drop table if exists "
					+ DtoQuery.TABLE_NAME_STORYVIEW);
			
			database.execSQL("drop table if exists "
					+ DtoQuery.TABLE_NAME_STORYLIST);
			
			database.execSQL("drop table if exists "
					+ DtoQuery.TABLE_NAME_ALBUMLIST);
			Log.i(TAG, "DBHelper Drop Table: " + database);

		}
	}

	//
	public void open() { //

		Log.i(TAG, "DbAdapter.open()");

		this.openHelper = new DBHelper(context);

		this.database = openHelper.getWritableDatabase();
		Log.i(TAG, "DbAdapter.open().getWritableDatabase");
		this.database.execSQL("PRAGMA foreign_keys = ON;");
		
		Log.i(TAG, "database.execSQL PRAGMA foreign_keys = ON;");

		// openHelper.onUpgrade(database, DtoQuery.DB_VERSION,0);
		openHelper.onCreate(database);
	}

	public void close() { //
		Log.i(TAG, "DbAdapter.close()");
		openHelper.close();
	}

	// Database select* from table명 order by id desc;

	public Cursor fetchAllDb(String tableName) {
		// select * from ALBUMLIST order by albumId desc;

		Log.i(TAG, "DbAdapter.fetchAllDb() ");
		Log.i(TAG, "tableName : " + tableName);
		Cursor cursor;
		if (tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_ALBUMLIST)) {
			Log.i(TAG, "if  : " + tableName);
			cursor = agentFetchAllDb(DtoQuery.TABLE_NAME_ALBUMLIST,
					DtoQuery.COLUMN_ALBUM_ID, new String[] {
							DtoQuery.COLUMN_ALBUM_ID,
							DtoQuery.COLUMN_ALBUM_TITLE,
							DtoQuery.COLUMN_ALBUM_COLOR,
							DtoQuery.COLUMN_ALUBUM_DATE });

		} else if (tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_STORYLIST)) {
			Log.i(TAG, "else if  : " + tableName);
			cursor = agentFetchAllDb(DtoQuery.TABLE_NAME_STORYLIST,
					DtoQuery.COLUMN_STORYLIST_ID, new String[] {
							DtoQuery.COLUMN_STORYLIST_ID,
							DtoQuery.COLUMN_STORYLIST_TITLE,
							DtoQuery.COLUMN_STORYLIST_DATE,
							DtoQuery.COLUMN_STORYLIST_ALBUMID });

		} else {
			Log.i(TAG, "else  : " + tableName);
			cursor = agentFetchAllDb(DtoQuery.TABLE_NAME_STORYVIEW,
					DtoQuery.COLUMN_STORYVIEW_ID, new String[] {
							DtoQuery.COLUMN_STORYVIEW_ID,
							DtoQuery.COLUMN_STORYVIEW_TITLE,
							DtoQuery.COLUMN_STROYVIEW_ABSIMGPATH,
							DtoQuery.COLUMN_STORYVIEW_IMAGEPATH,
							DtoQuery.COLUMN_STORYVIEW_CONTENT,
							DtoQuery.COLUMN_STORYVIEW_DATE,
							DtoQuery.COLUMN_STORYVIEW_STORYLISTID });
		}
		Log.i(TAG, "cursor : " + cursor.toString());
		return cursor; // cursor 반납
	}
	
	

	public Cursor agentFetchAllDb(String tableName, String id, String[] columns) {
		Log.i(TAG, "DbAdapter.agentFetchAllDb()  ");
		Log.i(TAG, "tableName : " + tableName);
		Log.i(TAG, "id : " + id);
		
		Cursor cursor = database.query(tableName, columns, // projection
				null, // selection
				null, // selectionArgs
				null, // group By
				null, // having
				id );//+ " DESC");// orderby desc (최신순)
		
			
		//Log 찍어 보기
			for(int i =0; i<columns.length;i++){
				Log.i(TAG, "columns["+i+"] : " + columns[i] );
				
			}
		
		
		Log.i(TAG, "cursor : " + cursor);
		return cursor;
	}

	public StringBuffer selectWriteInBuffer(View v, Cursor cursor) {
		Log.i(TAG, "DbAdapter.selectWriteInBuffer()  ");

		StringBuffer sb = new StringBuffer();

		// String msg;
		// Editable edit;

		if (cursor.moveToFirst()) {
			do {
				// 컬럼 갯수 만큼 출력하기
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					sb.append(cursor.getString(i) + "\t : ");

				}
				sb.append("\n");
			} while (cursor.moveToNext());
		}

		return sb;

	}

	// 메소드 오버로딩 isnertDb에 대해 파라미터가 테이블 명에 따라 틀리므로 호출 시 파라미터에 따라 따라가게 만듦
	//// StoryList에 insert하는 메소드 오버로딩
	// ContentValue 객체를 통해 value에 해당하는 컬럼에 집어 넣고 저장해 둔다.
	// database.insert(DtoQuery.TABLE_NAME, null, values); 를 통해서 insert완료 할
	// 수 있다.
	
	//albumList와 storList의 매개변수는 개수와 타입이 같기에 정렬을 한번 하고 color인지 albumId 아닌지 확인한다.
	
	public void insertDb(String tableName, int id, String title, int color) { // 테이블을 위한 오버로딩
		Log.i(TAG, "insertDb, id==0 이면 Exception 걸림");
		try{
			if (id==0){
				throw new Exception();
			}
		
		
		if (tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_ALBUMLIST)){
			Log.i(TAG, "INSERT INTO TABLE 명령 : " + tableName);
			Log.i(TAG, tableName + ".ContentValues 객체 생성");
			ContentValues values = new ContentValues();
	
			Log.i(TAG, tableName + ".id 컬럼 insert to value");
			values.put(DtoQuery.COLUMN_ALBUM_ID, id);
	
			Log.i(TAG, tableName + ".title 컬럼 insert to value");
			values.put(DtoQuery.COLUMN_ALBUM_TITLE, title);
			
			Log.i(TAG, tableName + ".color 컬럼 insert to value");
			values.put(DtoQuery.COLUMN_ALBUM_COLOR, color);
	
			Log.i(TAG, tableName + ".date 컬럼 insert to value");
			values.put(DtoQuery.COLUMN_ALUBUM_DATE, getDateTime());
	
			Log.i(TAG, "database insert 명령");
			database.insert(DtoQuery.TABLE_NAME_ALBUMLIST, null, values);
			
		}else if(tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_STORYLIST)){
			
			int albumId = color;
			Log.i(TAG, "INSERT INTO TABLE 명령 : " + tableName);
			Log.i(TAG, "ContentValues 객체 생성");
			ContentValues values = new ContentValues();

			Log.i(TAG, tableName + ".id 컬럼 insert to value");
			values.put(DtoQuery.COLUMN_STORYLIST_ID, id);

			Log.i(TAG, tableName + ".title 컬럼 insert to value");
			values.put(DtoQuery.COLUMN_STORYLIST_TITLE, title);

			Log.i(TAG, tableName + ".date 컬럼 insert to value");
			values.put(DtoQuery.COLUMN_ALUBUM_DATE, getDateTime());

			Log.i(TAG, tableName + ".albumId 컬럼 insert to value");
			values.put(DtoQuery.COLUMN_STORYLIST_ALBUMID, albumId);

			Log.i(TAG, tableName + ".database insert 명령");
			database.insert(DtoQuery.TABLE_NAME_STORYLIST, null, values);
		}
		}catch(Exception e){
			Log.i(TAG, "Exception 걸림 : "+ e);
		}
		

	}

	// StoryView에 insert하는 메소드 오버로딩
	public void insertDb(String tableName, int id, String title, String absImgPath, 
			String imagePath, String contents, int storyListId) {
		Log.i(TAG, "insertDb, id==0 이면 Exception 걸림");
		try{
			if (id==0){
				throw new Exception();
			}
		
		Log.i(TAG, "INSERT INTO TABLE 명령 : " + tableName);
		// ContentValue 객체를 통해 value에 해당하는 컬럼에 집어 넣고 저장해 둔다.
		// database.insert(DtoQuery.TABLE_NAME, null, values); 를 통해서 insert완료 할
		// 수 있다.
		Log.i(TAG, "ContentValues 객체 생성");
		ContentValues values = new ContentValues();

		Log.i(TAG, tableName + ".id 컬럼 insert to value");
		values.put(DtoQuery.COLUMN_STORYVIEW_ID, id);

		Log.i(TAG, tableName + ".title 컬럼 insert to value");
		values.put(DtoQuery.COLUMN_STORYVIEW_TITLE, title);
		
		Log.i(TAG, tableName + ".absImgPath 컬럼 insert to value");
		values.put(DtoQuery.COLUMN_STROYVIEW_ABSIMGPATH, absImgPath);
		
		Log.i(TAG, tableName + ".imagePath 컬럼 insert to value");
		values.put(DtoQuery.COLUMN_STORYVIEW_IMAGEPATH, imagePath);

		Log.i(TAG, tableName + ".contents 컬럼 insert to value");
		values.put(DtoQuery.COLUMN_STORYVIEW_CONTENT, contents);

		Log.i(TAG, tableName + ".date 컬럼 insert to value");
		values.put(DtoQuery.COLUMN_STORYVIEW_DATE, getDateTime());

		Log.i(TAG, tableName + ".storyListId 컬럼 insert to value");
		values.put(DtoQuery.COLUMN_STORYVIEW_STORYLISTID, storyListId);

		Log.i(TAG, tableName + ".database insert 명령");
		database.insert(DtoQuery.TABLE_NAME_STORYVIEW, null, values);
		
		}catch(Exception e){
			Log.i(TAG, "Exception 걸림 : "+ e);
		}

	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"[yyyy.MM.dd HH:mm:ss]", Locale.getDefault());
		Date date = new Date();
		Log.i(TAG, "date : " + date);
		return dateFormat.format(date);
	}

	// Database에 ID에 대한 특정 검색하여 값을 추출
	// 확장 필요 선택적 table name 넣기, id 뿐만 아니라 다른 컬럼에 대한 검색 허용 하기
	// whereIdStructure(String tableName, int id, int idFK)
	//albumList일 경우 FK가 없기에 0으로 설정 한다. 
	//storylist, storyview일 경우 (id != 0) 은 id로 검색할 것을 말함. 그럴때는 idFK=0
	//storylist, storyview일 경우 (id = 0) 은 idFK로 검색할 것을 말함. 그럴때는 idFK != 0
	public Cursor whereIdStructure(String tableName, int id, int idFK) {

		// select * from ALBUMLIST where albumId=id; id는 호출 시 받아온 값 즉 특정
		// albumId에 대한 검색
		// tableName에 따라 테이블이 albumlist, storyList, sotryView로 달라지기 때문에 거기에 테이블을
		// 찾아 columnId를 where 조건에 넣어 준다.
		// ex) where columnId = id;
		String columnId;
		
		int tableNum;
		
		
		//albumList일 경우 1, storyList 2, storyView 3 for switch case.
		if (tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_ALBUMLIST)){
			tableNum = 1;
			columnId = DtoQuery.COLUMN_ALBUM_ID;
		}else if (tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_STORYLIST)){
			tableNum = 2;
			columnId = DtoQuery.COLUMN_STORYLIST_ID;
			columnIdFK = DtoQuery.COLUMN_STORYLIST_ALBUMID;
		}else{
			tableNum=3;
			columnId = DtoQuery.COLUMN_STORYVIEW_ID;
			columnIdFK = DtoQuery.COLUMN_STORYVIEW_STORYLISTID;
		}
		
		
		String[] selectionArgsId = { String.valueOf(id) };
		
		String[] selectionArgsFK = { String.valueOf(idFK) };
		//selectionArg 값 확인 차 출력
		for(int i=0;i<selectionArgsId.length;i++){
			Log.i(TAG, "whereIdStructure.selectionArgs["+i+ "] :" + selectionArgsId[i]);
		}
		Log.i(TAG, "talbeAlbum : ");
		
		switch(tableNum){
			case 1: cursor = database.query(tableName, null, columnId + "=?",
					selectionArgsId, null, null, null); break;
			case 2: if (id != 0){/*ID를 실행*/cursor = database.query(tableName, null, columnId + "=?",
					selectionArgsId, null, null, null); break; }
					else {/*FK실행*/cursor = database.query(tableName, null, columnIdFK + "=?",
							selectionArgsFK, null, null, null); 
					Log.i(TAG, "whereIDstructure, switch, tableNum : " +tableNum+" id : " +  id+" idFK : " + idFK+" columnIdFK : "+columnIdFK);
					break;}
			case 3: if (id != 0){/*ID를 실행*/cursor = database.query(tableName, null, columnId + "=?",
					selectionArgsId, null, null, null); break; }
					else {/*FK실행*/cursor = database.query(tableName, null, columnIdFK + "=?",
							selectionArgsFK, null, null, null); 
					Log.i(TAG, "whereIDstructure, switch, tableNum : " +tableNum+" id : " +  id+" idFK : " + idFK+" columnIdFK : "+columnIdFK);
					break;}
		}
		
		Log.i(TAG, "DbAdapter.whereIdStructure Cursor : " + cursor);
		return cursor;
	}

	public void dropDp(String tableName) {
		Log.i(TAG, "dropDb() 명령");

		/*
		 * database.execSQL("drop table if exists " +
		 * DtoQuery.TABLE_NAME_STORYVIEW);
		 */
		Log.i(TAG, "drop storyview 완료 : " + database);
		openHelper.onUpgrade(database, DtoQuery.DB_VERSION,
				DtoQuery.DB_NEW_VERSION);
		// Log.i(TAG, "dropDb() 수행완료");

	}
	
	//deleteDb 호출 시 수정할  컬럼명을 통일 시켰기에 _id로 대처한다. 
	public void deleteDb(String tableName, int id) {
		
		String delete = "DELETE from " + tableName + " WHERE _id=" + id+";";
		Log.i(TAG, "delete 쿼리 : " + delete);
		//Foreign Key 가 있으면 지우지 못하도록 하였고, Exception이 발생하기에 종료되지 않도록 하였다. 
		try{
			database.execSQL(delete);
		}catch(SQLiteException se){
			Log.i(TAG, "deleteDb 에러 : "+ se);
		}
	}
	//확장형으로 다수의 id를 지울 때 사용할 예정
	/*//지정한 id를 갖는 데이터 삭제
	public boolean deleteDb(long id){
		String[] selectionArgs = {String.valueOf(id)};
		return database.delete(TABLE_NAME_ALBUMLIST, COLUMN_ALBUM_ID + "=?", selectionArgs) >0; //해당 ID에 대한 삭제, id가 삭제 된다.
	}*/
	
	
	//메소드 오버라이딩
		//albumList 일때 변경시
		//newTitle과 newColorOrAlbum : int whatColumn = 3
		//newTitle을 변경 : int whatColumn = 1
		//newColor을 변경 : int whatColumn = 2
		//Example ) albumList or storyList를 위한 수정사항
		//1. newTitle 일때 	updateDb(albumList, 1 ,1, 새로운테이블명, 0);
		//2. newColor 일때 	updateDb(albumList, 2, 1, null, 	  1);
		//3. 둘다일 경우	  	updateDb(albumList, 3, 3, 새로운테이블명, 2);
		
	public void updateDb(String tableName, int whatColumn, int id, String newTitle, int newColor){ 
			
		try{
			if (id==0){
				throw new Exception();
			}
			
			Log.i(TAG, "updateDb() 수행");
			Log.i(TAG, "tableName : " + tableName);
			
			//updateWithOnConflict()의 매개변수 맞추기
			String whereClause = DtoQuery.COLUMN_ALBUM_ID+"=?";
			String[] whereArgs = { String.valueOf(id) };
			
			ContentValues values = new ContentValues();
					
			if(whatColumn == 3){
			//두개 다 변경 시에 적용함.
				values.put(DtoQuery.COLUMN_ALBUM_TITLE, newTitle);
				values.put(DtoQuery.COLUMN_ALBUM_COLOR, newColor);
				database.updateWithOnConflict(tableName, values, whereClause, whereArgs, 2);
				
				Log.i(TAG, "update 2개의 경우, newTitle : "+newTitle+"newColor : "+newColor);
			}else if (whatColumn == 1){
				//newTitle일 경우
				values.put(DtoQuery.COLUMN_ALBUM_TITLE, newTitle);
				database.updateWithOnConflict(tableName, values, whereClause, whereArgs, 1);
				Log.i(TAG, "update 1개 경우, newTitle : "+newTitle);
				
			}else if (whatColumn == 2){
				
					//newColor일 경우
				values.put(DtoQuery.COLUMN_ALBUM_COLOR, newColor);
				database.updateWithOnConflict(tableName, values, whereClause, whereArgs, 1);
				Log.i(TAG, "update 1개 경우, newColor : "+newColor);
			}
		}catch(Exception e){
			Log.i(TAG, "Exception 걸림 : "+ e);
		}
			
			Log.i(TAG, "updateDb() 수행완료");
			
	}
	//updateDb for storyList
	public void updateDb(String tableName, int id, String newTitle){ 
			
			Log.i(TAG, "updateDb() 수행");
			Log.i(TAG, "tableName : " + tableName);
			Log.i(TAG, "newTitle : "+newTitle);
			try{
				if (id==0){
					throw new Exception();
				}
			
			//updateWithOnConflict()의 매개변수 맞추기
			String whereClause = DtoQuery.COLUMN_STORYLIST_ID+"=?";
			String[] whereArgs = { String.valueOf(id) };
			
			ContentValues values = new ContentValues();
			
			values.put(DtoQuery.COLUMN_STORYLIST_TITLE, newTitle);
			database.updateWithOnConflict(tableName, values, whereClause, whereArgs, 2);
			
			}catch(Exception e){
				Log.i(TAG, "Exception 걸림 : "+ e);
			}
			Log.i(TAG, "updateDb() 수행완료");
			
		}
	//StoryView for update
	//메소드 오버라이딩
			//newTitle, newAbsImgPath, newImagePath, newContent 일때 : int whatColumn = 5
			//newTitle을 변경 : int whatColumn = 1
			//newAbsImgPath변경 : int whatColumn =2
			//newImagePath 변경 : int whatColumn = 3
			//newContent 변경 : int whatColumn = 4
			//Example ) albumList or storyList를 위한 수정사항
			//1. newTitle 일때    	updateDb(storyView, 1 ,1, 새로운내용, 기존내용, 기존내용, 기존경로);
			//2. newAbsImgPath 일때  updateDb(storyView, 2, 1, 기존내용, 새로운내용, 기존경로, 기존경로);
			//3. newImagePath일때 	updateDb(storyView, 3, 1, 기존내용, 기존내용, 새로운내용,  새로운내용);
			//4. newContent 일때  	updateDb(storyView, 4, 1, 기존내용, 새로운내용, 기존내용, 새로운내용);
			//5. 둘다일 경우	  		updateDb(storyView, 5 ,1, 새로운내용, 새로운내용, 새로운내용, 새로운내용);
	
	public void updateDb(String tableName, int whatColumn, int id, String newTitle, String newAbsImgPath,String newImagePath, String newContent){ 
		try{
			if (id==0){
				throw new Exception();
			}
		
		Log.i(TAG, "updateDb() 수행");
		Log.i(TAG, "tableName : " + tableName);
		
		//updateWithOnConflict()의 매개변수 맞추기
		String whereClause = DtoQuery.COLUMN_STORYVIEW_ID+"=?";
		String[] whereArgs = { String.valueOf(id) };
		
		ContentValues values = new ContentValues();
				
		if(whatColumn == 5){
		//두개 다 변경 시에 적용함.
			values.put(DtoQuery.COLUMN_STORYVIEW_TITLE, newTitle);
			values.put(DtoQuery.COLUMN_STROYVIEW_ABSIMGPATH, newAbsImgPath);
			values.put(DtoQuery.COLUMN_STORYVIEW_IMAGEPATH, newImagePath);
			values.put(DtoQuery.COLUMN_STORYVIEW_CONTENT, newContent);
			database.updateWithOnConflict(tableName, values, whereClause, whereArgs, 2);
			
			Log.i(TAG, "update 전부의 경우, newTitle : "+newTitle+" newAbsImgPath : "+newAbsImgPath+"newImagePath : "+newImagePath+"newContent : "+newContent ) ;
		}else if (whatColumn == 1){
			//newTitle일 경우
			values.put(DtoQuery.COLUMN_STORYVIEW_TITLE, newTitle);
			database.updateWithOnConflict(tableName, values, whereClause, whereArgs, 1);
			Log.i(TAG, "update 1개 경우, newTitle : "+newTitle);
			
		}else if (whatColumn == 2){
				
			//newAbsImagePath 경우
			values.put(DtoQuery.COLUMN_STORYVIEW_IMAGEPATH, newImagePath);
			database.updateWithOnConflict(tableName, values, whereClause, whereArgs, 1);
			Log.i(TAG, "update 1개 경우, newContent : "+newContent);
		}else if (whatColumn == 3){
			
			//newImagePaths인 경우
			values.put(DtoQuery.COLUMN_STORYVIEW_IMAGEPATH, newContent);
		database.updateWithOnConflict(tableName, values, whereClause, whereArgs, 1);
		Log.i(TAG, "update 1개 경우, newImagePath : "+newImagePath);
		
		}else if (whatColumn == 4){
			
			//newContents인 경우			
			values.put(DtoQuery.COLUMN_STORYVIEW_CONTENT, newContent);
		database.updateWithOnConflict(tableName, values, whereClause, whereArgs, 1);
		Log.i(TAG, "update 1개 경우, newImagePath : "+newImagePath);
		}
		}catch(Exception e){
			Log.i(TAG, "Exception 걸림 : "+ e);
		}
		Log.i(TAG, "updateDb() 수행완료");
		
	}
	/** 
	 * public int getLastId(String tableName){
	 * }
	 * @param tableName
	 * lastIdValue를 가지고 새로 생성시 +1로 insertDB에 추가 하면 효율적이므로 이 기능을 제공함. 
	 */

	public int getLastRowId(String tableName){
		
		Cursor cursor;
		cursor=fetchAllDb(tableName);
		cursor.moveToLast();
		this.lastIdValue = cursor.getInt(0);
	
		return lastIdValue;
	}
	private void showMsg(String msg, int option) {
		Toast.makeText(context, msg, option).show();

	}
}
/*
 * Cursor cursor;
		if(tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_ALBUMLIST)){
			 cursor=fetchAllDb(tableName);
			 cursor.moveToLast();
			 this.lastIdValue = cursor.getInt(0);
			  
		}else if(tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_STORYLIST)){
			cursor = whereIdStructure(tableName, 0, idFK);
			cursor.moveToLast();
			this.lastIdValue = cursor.getInt(0);
			
		}else if(tableName.equalsIgnoreCase(DtoQuery.TABLE_NAME_STORYVIEW)){
			cursor = whereIdStructure(tableName, 0, idFK);
			cursor.moveToLast();
			this.lastIdValue = cursor.getInt(0);
			
		}*/
