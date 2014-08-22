package com.codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
	private final int REQUEST_CODE = 20;
	
	private ArrayList<TodoEntry> items;
	private ArrayAdapter<TodoEntry> itemsAdapter;
	private ListView lvItems;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<TodoEntry>();
        readItems();
        itemsAdapter = new ArrayAdapter<TodoEntry>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        
        setupListViewListener();
    }
    
    public void addTodoItem(View v) {
    	EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
    	itemsAdapter.add(new TodoEntry(etNewItem.getText().toString()));
    	etNewItem.setText("");
    	saveItems();
    }
    
    private void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				saveItems();
				return true;
			}
		});
    	
    	lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
				i.putExtra("value", items.get(position));
				i.putExtra("index", position);
				startActivityForResult(i, REQUEST_CODE);
				
			}
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // REQUEST_CODE is defined above
      if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
         TodoEntry value = (TodoEntry) data.getSerializableExtra("value");
         int index = data.getExtras().getInt("index");
         
         items.set(index, value);
         itemsAdapter.notifyDataSetChanged();
         saveItems();
      }
    } 
    
    private void readItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    
    	Log.w(getClass().getName(), "Data loading not working yet");
    	
//    	try {
//    		items = new ArrayList<TodoEntry>(FileUtils.readLines(todoFile));
//    	} catch(IOException e) {
    		items = new ArrayList<TodoEntry>();
//    		e.printStackTrace();
//    	}
    }
    
    private void saveItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try {
    		FileUtils.writeLines(todoFile, items);
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
}
