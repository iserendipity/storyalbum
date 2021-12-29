package airnodd.first.storyalbum;

public class DtoQuery {

	static final String DB = "storyalbum.db";

	static final String SELECT_ALBUM = "select * from ?";
	
	
	
	

	//DB파일 이름 선언
	public static final String DB_NAME = "storyAlbum.db";
	public static final int DB_VERSION = 3;
	public static final int DB_NEW_VERSION = 4;
	public static final String TAG = "NotesDbAdapter";
	// TABLES: 앨범 리스, 스토리 리스트, 스토리 뷰 선
	public static final String TABLE_NAME_ALBUMLIST = "ALBUMLIST";
	public static final String TABLE_NAME_STORYLIST = "STORYLIST";
	public static final String TABLE_NAME_STORYVIEW = "STORYVIEW";

	// TABLE ALBUMLIST'S COLUMNS: 앨범리스트 컬럼 선언
	public static final String COLUMN_ALBUM_ID = "_id"; // Primary Key
	public static final String COLUMN_ALBUM_TITLE = "title";
	public static final String COLUMN_ALBUM_COLOR = "color";
	public static final String COLUMN_ALUBUM_DATE = "date";

	// TABLE STORYLIST'S COLUMNS: 스토리리스트 컬럼 선언
	public static final String COLUMN_STORYLIST_ID = "_id"; // Primary Key
	public static final String COLUMN_STORYLIST_TITLE = "title";
	public static final String COLUMN_STORYLIST_DATE = "date";
	public static final String COLUMN_STORYLIST_ALBUMID = "_idAlbum";//Foreign Key

	// TABLE STORYVIEW'S COLUMNS: 스토리 뷰어 컬럼 선언
	public static final String COLUMN_STORYVIEW_ID = "_id"; // Primary Key
	public static final String COLUMN_STORYVIEW_TITLE = "title";
	public static final String COLUMN_STROYVIEW_ABSIMGPATH = "absImagPath";
	public static final String COLUMN_STORYVIEW_IMAGEPATH = "imagePath";
	public static final String COLUMN_STORYVIEW_CONTENT = "content";
	public static final String COLUMN_STORYVIEW_DATE = "date";
	public static final String COLUMN_STORYVIEW_STORYLISTID = "_idStoryList";//Foreign Key
	

	// CREATE TABLES
	// 1. create table ALBUMLIST(id, title, date)
	//integer primary key autoincrement,
	public static final String CREATE_TABLE_ALBUMLIST = "create table if not exists "
			+ TABLE_NAME_ALBUMLIST+ " ("
			+ COLUMN_ALBUM_ID+ " integer primary key, "
			+ COLUMN_ALBUM_TITLE+ " varchar2, "
			+ COLUMN_ALBUM_COLOR+" integer, " 
			+ COLUMN_ALUBUM_DATE+ " datetime " 
			+ ")";
	
	public static final String CREATE_TABLE_STORYLIST = "create table if not exists "
			+ TABLE_NAME_STORYLIST+ " ("
			+ COLUMN_STORYLIST_ID+ " integer primary key, "
			+ COLUMN_STORYLIST_TITLE+ " varchar2, "
			+ COLUMN_STORYLIST_DATE+ " datetime, "
			+ COLUMN_STORYLIST_ALBUMID + " integer, "
			+ "FOREIGN KEY ("+COLUMN_STORYLIST_ALBUMID+") REFERENCES "+TABLE_NAME_ALBUMLIST+" ("+COLUMN_ALBUM_ID+")" //Foreign Key 선언
			+ ")";
			
	public static final String CREATE_TABLE_STORYVIEW = "create table if not exists "
			+ TABLE_NAME_STORYVIEW+ " ("
			+ COLUMN_STORYVIEW_ID+ " integer primary key, "
			+ COLUMN_STORYVIEW_TITLE+ " varchar2, "
			+ COLUMN_STROYVIEW_ABSIMGPATH+ " varchar2, "
			+ COLUMN_STORYVIEW_IMAGEPATH+ " varchar2, "
			+ COLUMN_STORYVIEW_CONTENT+ " varchar2, "
			+ COLUMN_STORYVIEW_DATE+ " datetime, "
			+ COLUMN_STORYVIEW_STORYLISTID+ " integer, "
			+ "FOREIGN KEY ("+COLUMN_STORYVIEW_STORYLISTID+") REFERENCES "+TABLE_NAME_STORYLIST+" ("+COLUMN_STORYLIST_ID+")" //Foreign Key 선언
			+ ")";
	
	//DROP TABLE
	public static final String DROP_TABLE_ALBUMLIST = "DROP TABLE IF EXISTS "+TABLE_NAME_ALBUMLIST+")";
	public static final String DROP_TALBE_STORYLIST = "DROP TABLE IF EXISTS "+TABLE_NAME_STORYLIST+")";
	public static final String DROP_TABLE_STORYVIEW = "DROP TABLE IF EXISTS "+TABLE_NAME_STORYVIEW+")";
	

}
