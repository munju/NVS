package com.jmuenster.poi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.jmuenster.extras.Constants;
import com.jmuenster.extras.Syncronisation;
import com.jmuenster.map.MyItemizedOverlay;

public class AddEditDelNote extends Activity{
	
	private String title = "", description = "", mode="", unique_id = "";
	private int lat = 0, lng = 0, cat_id = 0;
	private EditText titleField, descriptionField;
	private Spinner cat_spinner;
	private Syncronisation sync;
	private boolean inputError = false;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grabbingValues();
        setContentView(R.layout.point);
        
        fillingForm();
        sync = new Syncronisation(this);
        MyItemizedOverlay.setLongPressed(false);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	standardTest();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    private void standardTest() {
    	if (!Syncronisation.checkConnectivity(this)) {
    		sync.setGotInternet(false);
    	} else {
    		sync.setGotInternet(true);
    	}
    }
    
    private void fillingForm() {
    	((TextView) findViewById (R.id.editMainTitle)).setText(mode);
		titleField = (EditText) findViewById (R.id.title);
		titleField.setText(title);
		descriptionField = (EditText) findViewById (R.id.description);
		descriptionField.setText(description);
		cat_spinner = (Spinner) findViewById(R.id.category_spinner);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.category_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    cat_spinner.setAdapter(adapter);
		cat_spinner.setPromptId(R.string.categories);
	    cat_spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
	    
		((TextView) findViewById (R.id.lat)).setText(lat+"");
		((TextView) findViewById (R.id.lng)).setText(lng+"");
		
		Button saveButton = (Button) findViewById (R.id.saveButton);
		Button cancelButton = (Button) findViewById (R.id.cancelButton);
		Button deleteButton = (Button) findViewById (R.id.deleteButton);
		
		if (mode.compareTo(Constants.DELETE)==0) {
			saveButton.setVisibility(Button.GONE);
			deleteButton.setVisibility(Button.VISIBLE);
			cat_spinner.setSelection(cat_id);
			cat_spinner.setEnabled(false);
			titleField.setFocusable(false);
			descriptionField.setFocusable(false);
			deleteButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					deleteItem(unique_id);
					loadMapView();
				}
			});
		} else if (mode.compareTo(Constants.ADD)==0){
			deleteButton.setVisibility(Button.GONE);
			saveButton.setVisibility(Button.VISIBLE);		
			saveButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					saveItem();
					loadMapView();
				}
			});
		} else if (mode.compareTo(Constants.EDIT)==0) {
			deleteButton.setVisibility(Button.GONE);
			saveButton.setVisibility(Button.VISIBLE);
			cat_spinner.setSelection(cat_id);
			saveButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					saveItem();
					loadMapView();
				}
			});			
		} else {
			deleteButton.setVisibility(Button.GONE);
			saveButton.setVisibility(Button.GONE);
		}

		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void grabbingValues() {
        mode = getIntent().getStringExtra(Constants.MODE);
        Bundle extras = getIntent().getExtras();
        if (mode.compareTo(Constants.EDIT)==0 ||
        		mode.compareTo(Constants.DELETE)==0) {
            title = extras.getString(Constants.OI_TITLE);
            description = extras.getString(Constants.OI_DESCRIPTION); 
            cat_id = turnCatToInt(extras.getString(Constants.OI_CATEGORY));
            unique_id = extras.getString(Constants.OI_ID);
        }
        
        lat = extras.getInt(Constants.OI_LAT);
        lng = extras.getInt(Constants.OI_LNG);	
    }
	
	private int turnCatToInt(String cat) {
		if (cat.compareTo(Constants.CAT_GENERAL)==0) {return 0;}
		else if (cat.compareTo(Constants.CAT_ENTERTAINMENT)==0) {return 1;}
		else if (cat.compareTo(Constants.CAT_FOOD)==0) {return 2;}
		else if (cat.compareTo(Constants.CAT_MEDICAL)==0){return 3;}
		else {return 0;}
	}
	
	private boolean testInput() {
		if (inputError) {
			return true;
		} else {
			return false;
		}
	}
	
	private void loadMapView() {
		if (!testInput()){
			this.finish();
		}
		inputError = false;
	}
	
	private void deleteItem(String id) {
		sync.deleteNote(id);
	}
	
	private void saveItem() {
		String newTitle = titleField.getText().toString();
		if (newTitle.compareTo("")==0) {
			Toast.makeText(this, "Please enter a title!", Toast.LENGTH_LONG).show();
			inputError = true;
			return;
		}
		String newDescription = descriptionField.getText().toString();
		if (mode.compareTo(Constants.EDIT)==0) {
			if (newTitle.compareTo(title)!=0  || 
					newDescription.compareTo(description)!=0) {
//				myDB.edit(title, newTitle, newDescription, lat, lng);
			}
		} else if (mode.compareTo(Constants.ADD)==0){
//			if (myDB.insert(newTitle, newDescription, lat, lng) == 2) {
//				Toast.makeText(this, "This title already exists!", Toast.LENGTH_LONG).show();
//				inputError = true;
//			}
		} else {
			Log.i("AddEditPoint", "Somewhere in saveItem unsuccesful");
		}
	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    	
	    }
	}
}