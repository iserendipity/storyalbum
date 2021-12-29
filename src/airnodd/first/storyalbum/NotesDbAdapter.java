package airnodd.first.storyalbum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


//DbAapter의 역할: 
//1. Database의 파일이름을 "storyAlbum.db"로 생성
//2. Database의 테이블 생성, 삭제
//3. insert, update, delete 수행하여 cursor나 해당 albumId를  반납하게 함.

public class NotesDbAdapter {
	
	//DB파일 이름 선언
	public static final String DB_NAME = "storyAlbum.db";
	public static final int DB_VERSION = 3;
	public static final String TAG = "NotesDbAdapter";
	// TABLES: 앨범 리스, 스토리 리스트, 스토리 뷰 선
	public static final String TABLE_NAME_ALBUMLIST = "ALBUMLIST";
	public static final String TABLE_NAME_STORYLIST = "STORYLIST";
	public static final String TABLE_NAME_STORYVIEW = "STORYVIEW";

	// TABLE ALBUMLIST'S COLUMNS: 앨범리스트 컬럼 선언
	public static final String COLUMN_ALBUM_ID = "_id"; // Primary Key
	public static final String COLUMN_ALBUM_TITLE = "albumTitle";
	public static final String COLUMN_ALUBUM_DATE = "albumDate";

	// TABLE STORYLIST'S COLUMNS: 스토리리스트 컬럼 선언
	public static final String COLUMN_STORYLIST_ID = "_idStoryList"; // Primary Key
	public static final String COLUMN_STORYLIST_TITLE = "storyTitle";
	public static final String COLUMN_STORYLIST_DATE = "storyDate";
	// public static final String ALBUM_ID = "albumID"; //Foreign Key

	// TABLE STORYVIEW'S COLUMNS: 스토리 뷰어 컬럼 선언
	public static final String COLUMN_STORYVIEW_ID = "_idView"; // Primary Key
	public static final String COLUMN_STORYVIEW_TITLE = "viewTitle";
	public static final String COLUMN_STORYVIEW_DATE = "viewDate";
	// public static final String STORYLIST_ID = "storyId"; //Foreign Key

	// CREATE TABLES
	// 1. create table ALBUMLIST(id, title, date)
	public static final String CREATE_TABLE_ALBUMLIST = "create table if not exists "
			+ TABLE_NAME_ALBUMLIST+ " ("
			+ COLUMN_ALBUM_ID+ " integer primary key autoincrement, "
			+ COLUMN_ALBUM_TITLE+ " text, "
			+ COLUMN_ALUBUM_DATE+ " text " 
			+ ")";
/*
	// 2. create table STORYLIST(id, title, date, albumId);
	public static final String CREATE_TABLE_STORYLIST = "create table if not exists "
			+ TABLE_NAME_STORYLIST+ " ("
			+ COLUMN_STORYLIST_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_STORYLIST_TITLE+ ", "
			+ COLUMN_STORYLIST_DATE 
			+ COLUMN_ALBUM_ID // Foreign Key
			+ ")";

	// 3. create table STORYLIST(id, title, date, albumId);
	public static final String CREATE_TABLE_STORYVIEW = "create table if not exists "
			+ TABLE_NAME_STORYVIEW+ " ("
			+ COLUMN_STORYVIEW_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_STORYVIEW_TITLE+ ", "
			+ COLUMN_STORYVIEW_DATE 
			+ COLUMN_STORYLIST_ID // Foreign Key
			+ ")";
*/
	Context context; //Context 선언
	SQLiteOpenHelper openHelper = null; //SQL 도우미 선언
	SQLiteDatabase database = null; //Database 객체를 위한 선언
	
	
	public NotesDbAdapter(Context context){ //생성자 NotesDbAdapter
		
		super(); 				//여기서 super는 Object를 의미;
		this.context = context; //호출 시 보낸 객체를 받아 현재의 객체에 넣어 준다.
		Log.i ( TAG, "NotesDbAdapter 생성자 " );
	}
		
		//내부 클래스 선언, 하는 일: 테이블 생성 삭제
		class DBHelper extends SQLiteOpenHelper{ 
			public DBHelper(Context context){
				super(context, DB_NAME, null, DB_VERSION); //db file 생성( "storyAlbum.db" );
				Log.i(TAG, "DBHelper constructor");
			}
			public void onCreate(SQLiteDatabase database){
				//테이블 생성 (앨범 리스트, 스토리 리스트, 스토리 뷰어)
				Log.i(TAG, "DBHelper onCreate()");
				//database.execSQL("drop "+ TABLE_NAME_ALBUMLIST); 
				database.execSQL(CREATE_TABLE_ALBUMLIST); //(확장필요)만약 테이블이 있다면 생성시켜주지 않도록 바꿔주는 것이 필요. 
				
				Log.i(TAG, "ALBUMLIST 테이블 생성 : "+ database);
				
			//	database.execSQL(CREATE_TABLE_STORYLIST);
			//	database.execSQL(CREATE_TABLE_STORYVIEW);
				
				
			}
			public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
				Log.i(TAG, "DBHelper onUpgrade");
				//기존에 생성된 테이블이 있다면 삭제 명령
				database.execSQL("drop table if exists " + TABLE_NAME_ALBUMLIST); 
				//database.execSQL("drop table if exists " + TABLE_NAME_STORYLIST);
				//database.execSQL("drop table if exists " + TABLE_NAME_STORYVIEW);
				
				//테이블 새로 생성
				onCreate(database);
			}
		}
		//
	public void open(){ //하는일 : DBHelper 객체를 생성하고, database를 쓸수 있도록 객체를 받아온다.
		
		Log.i(TAG, "NotesDbAdpater.open()");
		openHelper = new DBHelper(context); //내부 클래스 DBHelper 객체 생성, 선언과 동시에 "storyAlbum.db" 파일을 만듬, 또한 Database를 쓸수 있는 getWritableDatase()를 호출 할 수 있다.
		database = openHelper.getWritableDatabase(); // NotesDbAdapter의 멤버 변수인 database를 쓸수 있게 하여 준다.			
	}
	public void close(){ //DBHelper의 객체를 반납한다.
		openHelper.close();
	}
	
	//Database에 저장된 모든 데이터를 이용
	public Cursor fetchAllDb(){
		//select * from ALBUMLIST order by albumId desc; 앨범리스트의 모든 내용을 불러와서 내림차순으로 정렬한다. 그리고 Cursor를 위치 시킨다.
		//일단 앨범 리스트만 ^^(확장 필요)
		Log.i(TAG, "NotesDbAdapter.fetchAllDb() 수행");
		
		return database.query(TABLE_NAME_ALBUMLIST, new String[]{COLUMN_ALBUM_ID, COLUMN_ALBUM_TITLE, COLUMN_ALUBUM_DATE}, //projection
				null, // selection
				null, //selectionArgs
				null, //group By
				null, //having
				COLUMN_ALBUM_ID + " DESC");// orderby
		
	}
	//Database에 albumId에 대한 특정 검색하여 값을 추출
	//확장 필요 선택적 table name 넣기, id 뿐만 아니라 다른 컬럼에 대한 검색 허용 하기 
	public Cursor check(long id){
		//select * from ALBUMLIST where albumId=id;  id는 호출 시 받아온 값 즉 특정 albumId에 대한 검색 
		
		String[] selectionArgs = {String.valueOf(id)};
		Cursor c = database.query(TABLE_NAME_ALBUMLIST,
				null, 
				COLUMN_ALBUM_ID + "=?",
				selectionArgs, 
				null, 
				null, 
				null); 
		return c;
	}
	
	//Database에 데이터 추가
	//(확장필요) insert into ALBUMLIST(title, date) values(title-받아온값, date-받아온값);
	//ContentValues 객체를 통해 위의 명령어를 짧고 쉽게 수행할 수 있다.
	//insert() 메소드는 현재 넣은 값의 id를 반환한다. 
	public long insertDb(String title, String date){
		ContentValues values = new ContentValues();
		values.put(COLUMN_ALBUM_TITLE, title);
		values.put(COLUMN_ALUBUM_DATE, date);
		return database.insert(TABLE_NAME_ALBUMLIST, null, values); //자동 생성된 albumId 반환
	}
	
	//Database의 내용 갱신
	public boolean updateDb(String title, String date, long id){
		ContentValues values = new ContentValues(); //ContentValues 객체를 통해 쉽게 값을 갱신 할 수있다.
		values.put(COLUMN_ALBUM_TITLE, title);
		values.put(COLUMN_ALUBUM_DATE, date);
		String[] selectionArgs = {String.valueOf(id)}; //갱신하고자 하는 해당 Index가 여러개일 수 있다. 그 대신 title과 date가 동일하게 된다.
		
		//0보다 클때는 true가 반납한다. 즉 update(갱신) 된 해당 albumId 컬럼에 대한 (title, date) 삽입 됬을 텐데, 갱신 된 albumId의 갯수를 의미한다. 
		//1개면 해당 id가 한개이며, 해당 id에 사용자가 입력한 title과 date로 갱신됨을 의미한다.
		return database.update(CREATE_TABLE_ALBUMLIST, values, COLUMN_ALBUM_ID + "=?", selectionArgs)>0; 
		
	}
	
	//지정한 id를 갖는 데이터 삭제
	public boolean deleteDb(long id){
		String[] selectionArgs = {String.valueOf(id)};
		return database.delete(TABLE_NAME_ALBUMLIST, COLUMN_ALBUM_ID + "=?", selectionArgs) >0; //해당 ID에 대한 삭제, id가 삭제 된다.
	}
}
	
	
	
	
	
	
	
	
	
	

