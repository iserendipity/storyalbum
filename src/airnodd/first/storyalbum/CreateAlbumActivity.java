package airnodd.first.storyalbum;

import airnodd.first.storyalbum.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.os.Bundle;

public class CreateAlbumActivity extends Activity {
	private Gallery g;
	public Button btngoAlbumList01;
	public EditText et1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createalbum);

		g = (Gallery) findViewById(R.id.GalleryCreateAlbum01);// View
		g.setAdapter(new UserAdapter(this));
		et1 = (EditText) findViewById(R.id.EditTextCreateAlbum01);
		et1.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_ENTER) {

					}
				}
				return false;
			}
		});


		btngoAlbumList01 = (Button) findViewById(R.id.ButtonCreateAlbum01);
		btngoAlbumList01.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goAlbumListd();
			}
		});
		
	}

	public void goAlbumListd() {
		AlertDialog.Builder goalbumlistBuilder = new AlertDialog.Builder(this);
		goalbumlistBuilder.setTitle("실행확인");
		goalbumlistBuilder.setMessage("작성완료하셨나요?");

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (which == DialogInterface.BUTTON_POSITIVE) {
					EditText eAlbumName = (EditText) findViewById(R.id.EditTextCreateAlbum01);
					String value = eAlbumName.getText().toString();
					
					if (value.length() > 0) {
						Toast.makeText(CreateAlbumActivity.this,
								"album : " + value, 1).show();
						goAlbumList();
					} else {
						Toast.makeText(CreateAlbumActivity.this,
								"앨범명은 1글자 이상 입력하세요", Toast.LENGTH_SHORT).show();
					}
				} else {
				}
			}
		};
		goalbumlistBuilder.setPositiveButton("예", listener);
		goalbumlistBuilder.setNegativeButton("아니오", listener);
		goalbumlistBuilder.show();
	}

	public void goAlbumList() {
		Intent i2 = new Intent(this, AlbumListActivity.class);
		EditText eAlbumName = (EditText) findViewById(R.id.EditTextCreateAlbum01);
		String value = eAlbumName.getText().toString();
		
		i2.putExtra("key1", value);
		startActivityForResult(i2, 0);
		

	}
	
}

class Model {
	static final int IMGS[] = { R.drawable.green, R.drawable.blue,
			R.drawable.red, R.drawable.pupple, R.drawable.orange,
			R.drawable.violet, R.drawable.yellow };
}

class UserAdapter extends BaseAdapter { // Controller
	private Context c;
	private ImageView iv;

	UserAdapter() {
	}

	UserAdapter(CreateAlbumActivity c) {
		this.c = c;
	}

	public int getCount() {
		return Model.IMGS.length;
	}

	public Object getItem(int position) {
		return Model.IMGS[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			iv = new ImageView(c);

			// iv.setLayoutParams(new Gallery.LayoutParams(360, 450));//직접 크기 조절
			iv.setScaleType(ImageView.ScaleType.CENTER_CROP); // 비율 유지 옵션
			// iv.setScaleType(ImageView.ScaleType.FIT_XY); // 크기 고정 옵션
			iv.setPadding(15, 15, 45, 15);
		} else {
			iv = (ImageView) convertView;
		}
		iv.setImageResource(Model.IMGS[position]);

		return iv;
	}
}

class FeaturedSelectListener implements AdapterView.OnItemSelectedListener {

	private Animation grow = null;
	private View lastView = null;

	public FeaturedSelectListener(Context c) {
		grow = AnimationUtils.loadAnimation(c, R.anim.featured_selected);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// Shrink the view that was zoomed
		try {
			if (null != lastView)
				lastView.clearAnimation();
		} catch (Exception clear) {
		}

		// Zoom the new selected view
		try {
			view.startAnimation(grow);
		} catch (Exception animate) {
		}

		// Set the last view so we can clear the animation
		lastView = view;

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}