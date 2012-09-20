package com.jmuenster.poi;

import java.text.DateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jmuenster.extras.Constants;

public class ViewNote extends Activity {
	
	private String title, body, category, visibility, owner;
	private int alt = 0, lat = 0, lng = 0;
	private long creationTime = 0, validFor = 0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grabbingValues();
        setContentView(R.layout.view);
        setTitle(R.string.view);
        fillingForm();
    }
    
    private void fillingForm() {
		((TextView) findViewById (R.id.view_title)).setText(title);
		((TextView) findViewById (R.id.view_body)).setText(body);
		((TextView) findViewById (R.id.view_owner)).setText(owner);
		((TextView) findViewById (R.id.view_category)).setText(category);
		((TextView) findViewById (R.id.view_visibility)).setText(visibility);
		((TextView) findViewById (R.id.view_latitude)).setText((lat/1E6)+"");
		((TextView) findViewById (R.id.view_longitude)).setText((lng/1E6)+"");
		((TextView) findViewById (R.id.view_altitude)).setText(alt+"");
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		String dateString = dateFormat.format(creationTime);
		((TextView) findViewById (R.id.view_creationTime)).setText(dateString);
		((TextView) findViewById (R.id.view_validFor)).setText(validFor+"");

		Button cancelButton = (Button) findViewById (R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
    }
    
	private void grabbingValues() {
        Bundle extras = getIntent().getExtras();
        owner = extras.getString(Constants.OI_OWNER);
        title = extras.getString(Constants.OI_TITLE);
        body = extras.getString(Constants.OI_DESCRIPTION);
        alt = extras.getInt(Constants.OI_ALT);
        lat = extras.getInt(Constants.OI_LAT);
        lng = extras.getInt(Constants.OI_LNG);
        validFor = extras.getLong(Constants.OI_VALID_FOR);
        creationTime = extras.getLong(Constants.OI_CREATION_TIME);
        category = extras.getString(Constants.OI_CATEGORY);
        visibility = extras.getString(Constants.OI_VISIBILITY);   
	}
}
