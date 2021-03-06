package fr.escape.android;

import java.io.File;
import java.text.DecimalFormat;

import fr.escape.android.R;
import fr.escape.app.Engine;
import fr.escape.game.entity.CoordinateConverter;
import fr.escape.game.scenario.ScenarioBuilder;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class BuilderActivity extends Activity {
	
	final ScenarioBuilder builder = new ScenarioBuilder();
	
	private FileAsyncTask fAsyncTask;
	
	private MenuItem changeButton;
	private MenuItem removeButton;
	private MenuItem saveButton;
	
	boolean registerMovement = false;
	private String backgroundName = "";
	private int step = 1;
	
	int scenarioId = -1;
	int previous = 0;
	int bossId = 0;
	int imgId = 0;
	int type = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_builder_options_1);
		
		new ListAsyncTask(this, new int[] {R.id.scenario_list,1}).execute(new String[] {"EscapeIR/Scenario"});
		fAsyncTask = new FileAsyncTask(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.builder, menu);
		
		changeButton = menu.findItem(R.id.change_type);
		removeButton = menu.findItem(R.id.remove_ship);
		saveButton   = menu.findItem(R.id.save_button);
		
		saveButton.setEnabled(false);
		hideShipMenu();
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.change_type : switchType(); break;
			case R.id.remove_ship : removeShip(); break;
			case R.id.save_button : builder.saveData();
				Toast.makeText(getBaseContext(), "Scénario sauvegardé.", Toast.LENGTH_SHORT).show();
				break;
			default : break;
		}
		return true;
	}
	
	public void next(View view) {
		switch(step) {
			case 1 : ListView scnList = (ListView) findViewById(R.id.scenario_list);
				String scenarioName = ((EditText) findViewById(R.id.scenario_name)).getText().toString();
				String scenarioTime = ((EditText) findViewById(R.id.scenario_time)).getText().toString();
				Log.i("NEXT","Scenario ID : " + scenarioId);
				if((scenarioName.equals("")  || scenarioTime.equals("")) && scenarioId == -1) {
					Toast.makeText(getBaseContext(), "Remplissez les champs ou sélectionnez un scénario.", Toast.LENGTH_LONG).show();
				} else {
					if(scenarioId != -1) {
						setContentView(R.layout.activity_builder);
						saveButton.setEnabled(true);
						
						RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
						bLayout.setOnTouchListener(getOnTouchListener());
						
						fAsyncTask.execute((String) scnList.getItemAtPosition(scenarioId));
						return;
					}
					
					builder.name = scenarioName;
					builder.time = Integer.parseInt(scenarioTime);
					
					setContentView(R.layout.activity_builder_options_2);
					new ListAsyncTask(this, new int[] {R.id.images_list,0}).execute(new String[] {Environment.DIRECTORY_PICTURES, "Jupiter Background", "Moon Background", "Earth Background"});
					++step;
				}
				
				break;
			case 2 : ListView imgList = (ListView) findViewById(R.id.images_list);
				setContentView(R.layout.activity_builder_options_3);
				new ListAsyncTask(this, new int[] {R.id.boss_list,2}).execute(new String[] {null, "Jupiter Boss", "Moon Boss", "Earth Boss"});
				
				backgroundName = (String) imgList.getItemAtPosition(imgId);
				++step;
				break;
			case 3 : setContentView(R.layout.activity_builder);
				saveButton.setEnabled(true);
				
				builder.bossId = bossId;
				
				RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
				bLayout.setOnTouchListener(getOnTouchListener());
				
				int backgroundId = -1;
				switch(imgId) {
					case 0 : backgroundId = R.drawable.bjupiter; break;
					case 1 : backgroundId = R.drawable.bmoon; break;
					case 2 : backgroundId = R.drawable.bearth; break;
					default: break;
				}
				setBackground(backgroundId, backgroundName);
				
				++step;
				break;
			default: step = 1;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void addShip(int time, float x, float y, int type, float xWorld) {
		RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
		
		Bitmap bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sraptor);
		CoordinateConverter converter = new CoordinateConverter(bLayout.getWidth(), getWindowManager().getDefaultDisplay().getHeight(), 10);
		
		float wx = (xWorld == -15) ? converter.toMeterX((int) x) : xWorld;
		builder.currentShip = builder.createShip(x,wx,y,0.0f,type,time,bImage.getWidth() / 2,bImage.getHeight() / 2);
		
		builder.currentShip.image = new ImageView(getBaseContext());
		builder.currentShip.image.setImageBitmap(bImage);
		builder.currentShip.image.setPadding((int) (x - (bImage.getWidth() / 2)), (int) (y - (bImage.getHeight() / 2)), 0, 0);
		bLayout.addView(builder.currentShip.image);
		
		registerMovement = true;
		showShipMenu();
		this.type = (type == 0) ? 2 : type - 1;
		switchType();
	}
	
	public void removeShip() {
		builder.currentShip.image.setVisibility(View.GONE);
		builder.currentShip.image.setEnabled(false);
		builder.ships.remove(builder.currentShip);
		builder.currentShip = null;
		registerMovement = false;
		hideShipMenu();
		
		Toast.makeText(getBaseContext(), "Vaisseau supprimé.", Toast.LENGTH_SHORT).show();
	}
	
	public void switchType() {
		Bitmap bImage = null;
		type = (type + 1) % 3;
		switch(type) {
			case 0 : bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sraptor); break;
			case 1 : bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sfalcon); break;
			case 2 : bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sviper); break;
			default : bImage = BitmapFactory.decodeResource(getResources(), R.drawable.sraptor); break;
		}
		builder.currentShip.image.setImageBitmap(bImage);
		builder.currentShip.type = type;
	}
	
	float distanceX = 0, distanceY = 0;
	int nbMovement = 0;
	
	@SuppressWarnings("deprecation")
	public void registerMovement(MotionEvent event) {
		++nbMovement;
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			distanceX = builder.currentShip.x - event.getX();
			distanceY = builder.currentShip.y - event.getY();
		}
		
		if(nbMovement % 20 == 0 || event.getAction() == MotionEvent.ACTION_UP) {
			DecimalFormat f1 = new DecimalFormat("0.0");
			DecimalFormat f2 = new DecimalFormat("00.0");
			
			ScrollView scroll = (ScrollView) findViewById(R.id.builderscroll);
			CoordinateConverter converter = new CoordinateConverter(scroll.getWidth(), getWindowManager().getDefaultDisplay().getHeight(), 10);
			scroll.requestDisallowInterceptTouchEvent(true);
			
			float x = converter.toMeterX((int) (event.getX() + distanceX));
			float y = converter.toMeterY((int) ((event.getY() + distanceY) - builder.currentShip.y));
			
			builder.currentShip.movements.add(((x < 10) ? f1.format(x) : f2.format(x)) + " " + ((y < 10) ? f1.format(y) : f2.format(y)));
			
			if(event.getAction() == MotionEvent.ACTION_UP) {
				hideShipMenu();
				registerMovement = false;
				builder.currentShip = null;
				scroll.requestDisallowInterceptTouchEvent(false);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void setBackground(int backgroundId, String backgroundName) {
		RelativeLayout bLayout = (RelativeLayout) findViewById(R.id.builderlayout);
		
		if(backgroundId != -1) {
			bLayout.setBackgroundResource(backgroundId);
			builder.background = Integer.toString(backgroundId);
		} else {
			File directory = Engine.getImageStorage();
			File image = new File(directory, backgroundName);
			
			BitmapDrawable background = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(image.getAbsolutePath()));
			if(Build.VERSION.SDK_INT >= 16) {
				bLayout.setBackground(background);
			} else {
				bLayout.setBackgroundDrawable(background);
			}
			
			builder.background = backgroundName;
		}
	}
	
	private OnTouchListener getOnTouchListener() {
		return new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP && builder.currentShip == null) {
					builder.currentShip = builder.selectShip(event.getX(),event.getY());
					if(builder.currentShip != null) {
						Toast.makeText(getBaseContext(), "Vaisseau sélectionné : vous pouvez tracer ces déplacements.", Toast.LENGTH_SHORT).show();
						registerMovement = true;
						showShipMenu();
						return true;
					}
				}
				
				if(registerMovement) {
					registerMovement(event);
					return true;
				} else if(event.getAction() == MotionEvent.ACTION_UP && previous < 10) {
					int height = findViewById(R.id.builderlayout).getHeight();
					int realTime = (int) Math.floor((builder.time - (event.getY() / (height / builder.time))));
					addShip(realTime, event.getX(), event.getY(), type, -15);
					Toast.makeText(getBaseContext(), "Vaisseau ajouté : vous pouvez tracer ces déplacements.", Toast.LENGTH_SHORT).show();
				}
				
				if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
					previous = 0;
				} else {
					++previous;
				}
				
				return true;
			}
		};
	}
	
	public void showShipMenu() {
		changeButton.setEnabled(true);
		removeButton.setEnabled(true);
	}
	
	public void hideShipMenu() {
		changeButton.setEnabled(false);
		removeButton.setEnabled(false);
	}
	
	public void setListId(int id, int position) {
		switch(id) {
			case 0 : imgId = position; break;
			case 1 : scenarioId = position; break;
			case 2 : bossId = position; break;
			default: break;
		}
	}
	
}