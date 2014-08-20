package com.codepath.apps.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends Activity {

	private Button btnSave;
	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText etValue = (EditText) findViewById(R.id.etEditItemValue);
				  // Prepare data intent 
				  Intent data = new Intent();
				  // Pass relevant data back as a result
				  data.putExtra("value", etValue.getText().toString());
				  data.putExtra("index", index);
				  // Activity finished ok, return the data
				  setResult(RESULT_OK, data); // set result code and bundle data for response
				  finish(); // closes the activity, pass data to parent				
			}
			
		});

		String value = getIntent().getStringExtra("value");
		index = getIntent().getIntExtra("index", -1);
		
		EditText etValue = (EditText) findViewById(R.id.etEditItemValue);
		etValue.setText(value);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
