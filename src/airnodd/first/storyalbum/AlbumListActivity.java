package airnodd.first.storyalbum;

//import airnodd.first.storyalbum.DbAdapter;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Color;

import android.util.Log;

import android.view.Gravity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumListActivity extends Activity {

	private LinearLayout testlayout01, testlayout02, testlayout03,
			testlayout04, testlayout05, testlayout06, delbutton01, delbutton02,
			delbutton03, delbutton04, delbutton05, delbutton06;;

	RelativeLayout relativelayout01;
	Button btn1;
	int color;
	Cursor cursor;
	String tableName;
	StringBuffer sb;
	
	String title;
	String imagePath;
	String contents;
	int StoryListId;
	int albumId;
	String columnId;

	private DbAdapter dBHandlingHelper;
	public int DB_MODE = Context.MODE_PRIVATE;
	public String DB_NAME = "STORYALBUM.db"; // DB ������ �̸�
	public String TABLE_NAME = "ALBUMLIST"; // Table�� �̸�
	public static final String TAG = "AlbumListActivity";
	static int j = 1;
	
	static int init=1;
	static int i=1;
	// i, db��
	// value, count��
	int value;

	Thread th;

	//������ �ڵ鷯, �޼����� ������ oncreateauto�޼ҵ� ����
	private Handler h = new Handler() {
		public void handleMessage(Message m) {
			//
			oncreateauto();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.albumlist);
		delbutton01 = (LinearLayout) findViewById(R.id.delbutton01);
		delbutton02 = (LinearLayout) findViewById(R.id.delbutton02);
		delbutton03 = (LinearLayout) findViewById(R.id.delbutton03);
		delbutton04 = (LinearLayout) findViewById(R.id.delbutton04);

		testlayout01 = (LinearLayout) findViewById(R.id.testlayout01);
		testlayout02 = (LinearLayout) findViewById(R.id.testlayout02);
		testlayout03 = (LinearLayout) findViewById(R.id.testlayout03);
		testlayout04 = (LinearLayout) findViewById(R.id.testlayout04);

		btn1 = (Button) findViewById(R.id.btn01);

		//��� ����
		this.dBHandlingHelper = new DbAdapter(this);
		dBHandlingHelper.open();

		//fetchalldb - ���̺��� ��� ������ select
		cursor = dBHandlingHelper.fetchAllDb("ALBUMLIST");
		cursor.moveToFirst();

		//Ŀ���� ��� row ���� Ȯ��
		value = cursor.getCount();
		Log.i(TAG, "��� Ȯ��"+String.valueOf(value));
		
		//db�� �ƹ� �����Ͱ� ���� ���� �ִ� ����� �̺�Ʈ ó��
		if(value != 0){
			cursor.moveToLast();
			i = cursor.getInt(0)+1;
			Log.i(TAG, "���������� �߰��Ǿ�� �ϴ� ���̵� ��=���� �� �Ʒ� Row�� id��  + 1 : "+i);
		}else{
			
		}
		
		//������ ����
		th = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//������ �𸣳� post���� runnable. run������ ui�� ���������ϴٰ� ��.
				h.post(new Runnable() {
					@Override
					public void run() {
						//�� ������ ���̵� �������� Ȯ���ϰ� ����Ƽ ��ư���� ������ �ο��ϴ� id���� �������ش�.
					
						cursor.moveToFirst();
						//value�� = Ŀ���� row �� = �ٹ� ����
						//value �� ��ŭ �ٹ����� �޼��� �ݺ� ����
						for (int k = 0; k < value; k++) {
							Message m = h.obtainMessage();
							h.sendMessage(m);

						}

					}
				});
			}
		});
		//������ ����
		th.start();

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//btn1 = ����Ƽ Ŭ���� Ÿ��Ʋ �Է� ���̾�α� ���� 
				titledialog();
				// colordialog();

			}
		});

	}

	//��񿡼� ���� value�� ��ŭ �ڵ��������ִ� �޼ҵ�
	void oncreateauto() {
		init = cursor.getInt(0);
		title = cursor.getString(1);
		color = cursor.getInt(2);
		switch (color) {
		case 1:
			initred();
			break;
		case 2:
			initorange();
			break;
		case 3:
			inityellow();
			break;
		case 4:
			initgreen();
			break;
		case 5:
			initblue();
			break;
		case 6:
			initnavy();
			break;
		case 7:
			initviolet();
		}
		cursor.moveToNext();
	}

	void pagedialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Ȯ��");
		builder.setMessage("���� �������� �Ѿ���?");
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_POSITIVE) {
					Toast.makeText(AlbumListActivity.this, "����", 0).show();
				} else { // DialogInterface.BUTTON_NEGATIVE
					Toast.makeText(AlbumListActivity.this, "�ƴϿ�", 0).show();

				}
			}
		};
		builder.setNegativeButton("�ƴϿ�", listener);
		builder.setPositiveButton("��", listener);

		builder.show();
	}

	private String colors[] = { "����", "��Ȳ", "���", "�ʷ�", "�Ķ�", "����", "����" };

	void colordialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("�ٹ��� ������ ������ �ּ���");
		builder.setSingleChoiceItems( // Radio ����
				colors, 0, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							red();
						} else if (which == 1) {
							orange();// orange();
						} else if (which == 2) {
							yellow();// yellow();
						} else if (which == 3) {
							green();// green();
						} else if (which == 4) {
							blue();// blue();
						} else if (which == 5) {
							navy();// navy();
						} else {
							violet();// violet();
						}
						// dialog.cancel();
						dialog.dismiss();

					}
				});
		builder.show();
	}

	void titledialog() {
		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.albumlistcustomdialog,
				(ViewGroup) findViewById(R.id.layout_root));

		// ��ó:[�ڵ�] �ȵ���̵� AlertDialog�� �α���â�����
		AlertDialog.Builder builder = new AlertDialog.Builder(
				AlbumListActivity.this);
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
					showMsg("������ �Է��ϼ���", 0);
					return;
				} else {

					AlbumListActivity.this.title = title;
					showMsg(title, 0);
					colordialog();
				}
			}
		});
		builder.show();
	}
	//db���� �޾ƿ� �ڵ����� �ٹ��� �������ִ� �޼ҵ�->insertdb�� ���� i++����
	void initred() {

		TextView txTest = new TextView(AlbumListActivity.this);

		txTest.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest.setBackgroundResource(R.drawable.c_01);
		txTest.setTextColor(Color.BLACK);
		txTest.setGravity(Gravity.CENTER);

		txTest.setId(init);
		txTest.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest);

		} else if (j == 5) {
			testlayout01.addView(txTest);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest);

		} else if (j == 10) {
			testlayout02.addView(txTest);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest);

		} else if (j == 15) {
			testlayout03.addView(txTest);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest);

		} else if (j == 20) {
			testlayout04.addView(txTest);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		// dBHandlingHelper.insertDb(TABLE_NAME, txTest.getId(), title, 1);
		// } catch (SQLException e) {

		// }

		
		j++;
		// else�� ��ȣ

		txTest.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId()); 

			}
		});

	}
	//empty��ư�� ������ �����ϴ� �޼ҵ�
	void red() {
		//���ο� txTest��� ��ü ����
		TextView txTest = new TextView(AlbumListActivity.this);
		//�Ķ���� ����
		txTest.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest.setBackgroundResource(R.drawable.c_01);
		txTest.setTextColor(Color.BLACK);
		txTest.setGravity(Gravity.CENTER);
		//id�ο�
		txTest.setId(i);
		txTest.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest);

		} else if (j == 5) {
			testlayout01.addView(txTest);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest);

		} else if (j == 10) {
			testlayout02.addView(txTest);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest);

		} else if (j == 15) {
			testlayout03.addView(txTest);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest);

		} else if (j == 20) {
			testlayout04.addView(txTest);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		dBHandlingHelper.insertDb(TABLE_NAME, txTest.getId(), title, 1);
		// } catch (SQLException e) {

		// }

		i++;
		j++;
		// else�� ��ȣ

		txTest.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());

			}
		});

	}

	void initorange() {

		TextView txTest1 = new TextView(AlbumListActivity.this);

		txTest1.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest1.setBackgroundResource(R.drawable.c_02);
		txTest1.setTextColor(Color.BLACK);
		txTest1.setGravity(Gravity.CENTER);

		txTest1.setId(init);
		txTest1.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest1);

		} else if (j == 5) {
			testlayout01.addView(txTest1);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest1);

		} else if (j == 10) {
			testlayout02.addView(txTest1);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest1);

		} else if (j == 15) {
			testlayout03.addView(txTest1);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest1);

		} else if (j == 20) {
			testlayout04.addView(txTest1);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		// dBHandlingHelper.insertDb(TABLE_NAME, txTest1.getId(), title, 1);
		// } catch (SQLException e) {

		// }

		
		j++;
		// else�� ��ȣ

		txTest1.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue
				Log.i(TAG, "��Ȳ�� �ڵ����� ��Ŭ�� ���̵� :"+String.valueOf(v.getId()));
				intent(v.getId());
				
			}
		});

	}

	void orange() {

		TextView txTest1 = new TextView(AlbumListActivity.this);

		txTest1.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest1.setBackgroundResource(R.drawable.c_02);
		txTest1.setTextColor(Color.BLACK);
		txTest1.setGravity(Gravity.CENTER);

		txTest1.setId(i);
		txTest1.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest1);

		} else if (j == 5) {
			testlayout01.addView(txTest1);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest1);

		} else if (j == 10) {
			testlayout02.addView(txTest1);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest1);

		} else if (j == 15) {
			testlayout03.addView(txTest1);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest1);

		} else if (j == 20) {
			testlayout04.addView(txTest1);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		dBHandlingHelper.insertDb(TABLE_NAME, txTest1.getId(), title, 2);
		// } catch (SQLException e) {

		// }

		i++;
		j++;
		// else�� ��ȣ

		txTest1.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());
			}
		});

	}

	void inityellow() {

		TextView txTest2 = new TextView(AlbumListActivity.this);

		txTest2.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest2.setBackgroundResource(R.drawable.c_03);
		txTest2.setTextColor(Color.BLACK);
		txTest2.setGravity(Gravity.CENTER);

		txTest2.setId(init);
		txTest2.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest2);

		} else if (j == 5) {
			testlayout01.addView(txTest2);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest2);

		} else if (j == 10) {
			testlayout02.addView(txTest2);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest2);

		} else if (j == 15) {
			testlayout03.addView(txTest2);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest2);

		} else if (j == 20) {
			testlayout04.addView(txTest2);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		// dBHandlingHelper.insertDb(TABLE_NAME, txTest21.getId(), title, 1);
		// } catch (SQLException e) {

		// }

		
		j++;
		// else�� ��ȣ

		txTest2.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());

			}
		});

	}

	void yellow() {

		TextView txTest2 = new TextView(AlbumListActivity.this);

		txTest2.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest2.setBackgroundResource(R.drawable.c_03);
		txTest2.setTextColor(Color.BLACK);
		txTest2.setGravity(Gravity.CENTER);

		txTest2.setId(i);
		txTest2.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest2);

		} else if (j == 5) {
			testlayout01.addView(txTest2);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest2);

		} else if (j == 10) {
			testlayout02.addView(txTest2);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest2);

		} else if (j == 15) {
			testlayout03.addView(txTest2);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest2);

		} else if (j == 20) {
			testlayout04.addView(txTest2);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		dBHandlingHelper.insertDb(TABLE_NAME, txTest2.getId(), title, 3);
		// } catch (SQLException e) {

		// }

		i++;
		j++;
		// else�� ��ȣ

		txTest2.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());
			}
		});

	}

	void initgreen() {

		TextView txTest3 = new TextView(AlbumListActivity.this);

		txTest3.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest3.setBackgroundResource(R.drawable.c_04);
		txTest3.setTextColor(Color.BLACK);
		txTest3.setGravity(Gravity.CENTER);

		txTest3.setId(init);
		txTest3.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest3);

		} else if (j == 5) {
			testlayout01.addView(txTest3);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest3);

		} else if (j == 10) {
			testlayout02.addView(txTest3);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest3);

		} else if (j == 15) {
			testlayout03.addView(txTest3);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest3);

		} else if (j == 20) {
			testlayout04.addView(txTest3);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		// dBHandlingHelper.insertDb(TABLE_NAME, txTest.getId(), title, 1);
		// } catch (SQLException e) {

		// }

		
		j++;
		// else�� ��ȣ

		txTest3.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());

			}
		});

	}

	void green() {

		TextView txTest3 = new TextView(AlbumListActivity.this);

		txTest3.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest3.setBackgroundResource(R.drawable.c_04);
		txTest3.setTextColor(Color.BLACK);
		txTest3.setGravity(Gravity.CENTER);

		txTest3.setId(i);
		txTest3.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest3);

		} else if (j == 5) {
			testlayout01.addView(txTest3);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest3);

		} else if (j == 10) {
			testlayout02.addView(txTest3);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest3);

		} else if (j == 15) {
			testlayout03.addView(txTest3);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest3);

		} else if (j == 20) {
			testlayout04.addView(txTest3);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		dBHandlingHelper.insertDb(TABLE_NAME, txTest3.getId(), title, 4);
		// } catch (SQLException e) {

		// }

		i++;
		j++;
		// else�� ��ȣ

		txTest3.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());
			}
		});

	}

	void initblue() {

		TextView txTest4 = new TextView(AlbumListActivity.this);

		txTest4.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest4.setBackgroundResource(R.drawable.c_05);
		txTest4.setTextColor(Color.BLACK);
		txTest4.setGravity(Gravity.CENTER);

		txTest4.setId(init);
		txTest4.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest4);

		} else if (j == 5) {
			testlayout01.addView(txTest4);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest4);

		} else if (j == 10) {
			testlayout02.addView(txTest4);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest4);

		} else if (j == 15) {
			testlayout03.addView(txTest4);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest4);

		} else if (j == 20) {
			testlayout04.addView(txTest4);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		// dBHandlingHelper.insertDb(TABLE_NAME, txTest.getId(), title, 1);
		// } catch (SQLException e) {

		// }

		
		j++;
		// else�� ��ȣ

		txTest4.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());
			}
		});

	}

	void blue() {

		TextView txTest4 = new TextView(AlbumListActivity.this);

		txTest4.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest4.setBackgroundResource(R.drawable.c_05);
		txTest4.setTextColor(Color.BLACK);
		txTest4.setGravity(Gravity.CENTER);

		txTest4.setId(i);
		txTest4.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest4);

		} else if (j == 5) {
			testlayout01.addView(txTest4);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest4);

		} else if (j == 10) {
			testlayout02.addView(txTest4);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest4);

		} else if (j == 15) {
			testlayout03.addView(txTest4);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest4);

		} else if (j == 20) {
			testlayout04.addView(txTest4);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		dBHandlingHelper.insertDb(TABLE_NAME, txTest4.getId(), title, 5);
		// } catch (SQLException e) {

		// }

		i++;
		j++;
		// else�� ��ȣ

		txTest4.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());

			}
		});

	}

	void initnavy() {

		TextView txTest5 = new TextView(AlbumListActivity.this);

		txTest5.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest5.setBackgroundResource(R.drawable.c_06);
		txTest5.setTextColor(Color.BLACK);
		txTest5.setGravity(Gravity.CENTER);

		txTest5.setId(init);
		txTest5.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest5);

		} else if (j == 5) {
			testlayout01.addView(txTest5);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest5);

		} else if (j == 10) {
			testlayout02.addView(txTest5);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest5);

		} else if (j == 15) {
			testlayout03.addView(txTest5);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest5);

		} else if (j == 20) {
			testlayout04.addView(txTest5);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		// dBHandlingHelper.insertDb(TABLE_NAME, txTest5.getId(), title, 1);
		// } catch (SQLException e) {

		// }

		
		j++;
		// else�� ��ȣ

		txTest5.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());

			}
		});

	}

	void navy() {

		TextView txTest5 = new TextView(AlbumListActivity.this);

		txTest5.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest5.setBackgroundResource(R.drawable.c_06);
		txTest5.setTextColor(Color.BLACK);
		txTest5.setGravity(Gravity.CENTER);

		txTest5.setId(i);
		txTest5.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest5);

		} else if (j == 5) {
			testlayout01.addView(txTest5);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest5);

		} else if (j == 10) {
			testlayout02.addView(txTest5);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest5);

		} else if (j == 15) {
			testlayout03.addView(txTest5);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest5);

		} else if (j == 20) {
			testlayout04.addView(txTest5);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		dBHandlingHelper.insertDb(TABLE_NAME, txTest5.getId(), title, 6);
		// } catch (SQLException e) {

		// }

		i++;
		j++;
		// else�� ��ȣ

		txTest5.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());

			}
		});

	}

	void initviolet() {

		TextView txTest6 = new TextView(AlbumListActivity.this);

		txTest6.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest6.setBackgroundResource(R.drawable.c_07);
		txTest6.setTextColor(Color.BLACK);
		txTest6.setGravity(Gravity.CENTER);

		txTest6.setId(init);
		txTest6.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest6);

		} else if (j == 5) {
			testlayout01.addView(txTest6);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest6);

		} else if (j == 10) {
			testlayout02.addView(txTest6);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest6);

		} else if (j == 15) {
			testlayout03.addView(txTest6);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest6);

		} else if (j == 20) {
			testlayout04.addView(txTest6);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		// dBHandlingHelper.insertDb(TABLE_NAME, txTest.getId(), title, 1);
		// } catch (SQLException e) {

		// }

		
		j++;
		// else�� ��ȣ

		txTest6.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue

				intent(v.getId());

			}
		});

	}

	void violet() {

		TextView txTest6 = new TextView(AlbumListActivity.this);

		txTest6.setLayoutParams(new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));

		txTest6.setBackgroundResource(R.drawable.c_07);
		txTest6.setTextColor(Color.BLACK);
		txTest6.setGravity(Gravity.CENTER);

		txTest6.setId(i);
		txTest6.setText(title);
		// showMsg(title, 0);

		if (j < 5) {

			testlayout01.addView(txTest6);

		} else if (j == 5) {
			testlayout01.addView(txTest6);
			delbutton01.removeView(btn1);
			delbutton02.addView(btn1);

		} else if (j < 10) {

			testlayout02.addView(txTest6);

		} else if (j == 10) {
			testlayout02.addView(txTest6);
			delbutton02.removeView(btn1);
			delbutton03.addView(btn1);

		} else if (j < 15) {
			testlayout03.addView(txTest6);

		} else if (j == 15) {
			testlayout03.addView(txTest6);
			delbutton03.removeView(btn1);
			delbutton04.addView(btn1);

		}

		else if (j < 20) {
			testlayout04.addView(txTest6);

		} else if (j == 20) {
			testlayout04.addView(txTest6);
			delbutton04.removeView(btn1);
			pagedialog();
		}

		// try {
		dBHandlingHelper.insertDb(TABLE_NAME, txTest6.getId(), title, 7);
		// } catch (SQLException e) {

		// }

		i++;
		j++;
		// else�� ��ȣ

		txTest6.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showMsg("����ġ", 0);
				if (j < 6) {

					testlayout01.removeView(v);

				} else if (j == 6) {
					testlayout01.removeView(v);
					delbutton02.removeView(btn1);
					delbutton01.addView(btn1);
				} else if (j < 11) {
					testlayout02.removeView(v);
				} else if (j == 11) {
					testlayout02.removeView(v);
					delbutton03.removeView(btn1);
					delbutton02.addView(btn1);

				} else if (j < 16) {
					testlayout03.removeView(v);
				} else if (j == 16) {
					testlayout03.removeView(v);
					delbutton04.removeView(btn1);
					delbutton03.addView(btn1);
				} else if (j < 21) {
					testlayout04.removeView(v);
				} else {

					showMsg("21�̻�", 0);

				}
				showMsg("������ id : " + v.getId(), 0);
				dBHandlingHelper.deleteDb(TABLE_NAME, v.getId());
				j--;

				showMsg("i : " + String.valueOf(i) + ", j : "
						+ String.valueOf(j), 0);
				return true;
			}
		});

		txTest6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {// ����Ʈvalue
					
				intent(v.getId());

			}
		});

	}
	void intent(int k){
		Intent intent = new Intent(this, StoryListActivity.class);
		intent.putExtra("albumId", k);
		Log.i(TAG, "albumId�� �� : "+k);
		startActivity(intent);	
	}
	void showMsg(String msg, int option) {
		Toast.makeText(this, msg, option).show();
	}

}
