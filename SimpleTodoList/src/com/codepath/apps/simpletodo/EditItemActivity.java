package com.codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditItemActivity extends Activity {

	private Button btnSave;
	private int index;
	private Button btnTakePicture;
	
	private String audioFileName;
	private MediaRecorder mediaRecorder;

	public final String APP_TAG = "MyCustomApp";
	public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
	// PICK_PHOTO_CODE is a constant integer
	public final static int PICK_PHOTO_CODE = 1046;
	public String photoFileName = "photo.jpg";
	private View btnChosePicture;
	private TodoEntry value;
	private Button btnRecordAudio;
	private Button btnStopRecording;
	private Button btnPlaybackRecording;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText etValue = (EditText) findViewById(R.id.etEditItemValue);
				value.value = etValue.getText().toString();
				// Prepare data intent
				Intent data = new Intent();
				// Pass relevant data back as a result
				data.putExtra("value", value);
				data.putExtra("index", index);
				// Activity finished ok, return the data
				setResult(RESULT_OK, data); // set result code and bundle data
											// for response
				finish(); // closes the activity, pass data to parent
			}
			
		});

		btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
		btnTakePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// create Intent to take a picture and return control to the calling application
			    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name
			    // Start the image capture intent to take photo
			    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
			
		});
		
		btnChosePicture = (Button) findViewById(R.id.btnChoosePicture);
		btnChosePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Create intent for picking a photo from the gallery
			    Intent intent = new Intent(Intent.ACTION_PICK,
			        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			    // Bring up gallery to select a photo
			    startActivityForResult(intent, PICK_PHOTO_CODE);
			}
			
		});
		
		btnRecordAudio = (Button) findViewById(R.id.btnStartRecording);
		btnRecordAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Verify that the device has a mic first
				PackageManager pmanager = EditItemActivity.this.getPackageManager();
				if (pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
				    // Set the file location for the audio
				    audioFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
				    audioFileName += "/simpletodolist-" + System.currentTimeMillis() + ".3gp";
				    // Create the recorder
				    mediaRecorder = new MediaRecorder();
				    // Set the audio format and encoder
				    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				    // Setup the output location
				    mediaRecorder.setOutputFile(audioFileName);
				    try {
				    	Log.i(getClass().getName(), "Recording started...");
				    	
						// Start the recording
						mediaRecorder.prepare();

						Toast.makeText(EditItemActivity.this, "Recording... Talk!", Toast.LENGTH_LONG).show();
						mediaRecorder.start();
						
						btnRecordAudio.setEnabled(false);
						btnStopRecording.setEnabled(true);
					} catch (IOException e) {
						Log.e(getClass().getName(), e.getMessage(), e);
						Toast.makeText(EditItemActivity.this, "Could not record audio!", Toast.LENGTH_LONG).show();
					}
				} else { // no mic on device
				    Toast.makeText(EditItemActivity.this, "This device doesn't have a mic!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		btnStopRecording = (Button) findViewById(R.id.btnStopRecording);
		btnStopRecording.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mediaRecorder != null) {
					mediaRecorder.stop();
					
					value.audio = audioFileName;
					
					Log.i(getClass().getName(), "Recording stopped. Saved recorded audio to " + audioFileName);
					
					btnRecordAudio.setEnabled(true);
					btnStopRecording.setEnabled(false);
					btnPlaybackRecording.setEnabled(true);
				}
			}
		});
		
		btnPlaybackRecording = (Button) findViewById(R.id.btnPlay);
		btnPlaybackRecording.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					MediaPlayer mediaPlayer = new MediaPlayer();
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaPlayer.setDataSource(value.audio);
					mediaPlayer.prepare(); // must call prepare first
					mediaPlayer.start(); // then start
				} catch(Throwable th) {
					Log.e(getClass().getName(), th.getMessage(), th);
					Toast.makeText(EditItemActivity.this, "Playback failed!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		value = (TodoEntry) getIntent().getSerializableExtra("value");
		index = getIntent().getIntExtra("index", -1);
		
		btnPlaybackRecording.setEnabled(!StringUtils.isEmpty(value.audio));
		
		EditText etValue = (EditText) findViewById(R.id.etEditItemValue);
		etValue.setText(value.value);
		
		Uri takenPhotoUri = getPhotoFileUri(value.image);
		ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
        ivPreview.setImageBitmap(BitmapFactory.decodeFile(takenPhotoUri.getPath()));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
		    switch(requestCode) {
		    case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
		       if (resultCode == RESULT_OK) {
		         Uri takenPhotoUri = getPhotoFileUri(photoFileName);
		         // by this point we have the camera photo on disk
		         Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
		         // Load the taken image into a preview
		         ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
		         ivPreview.setImageBitmap(takenImage);
		         value.image = photoFileName;
		       } else { // Result was a failure
		    	   Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
		       }
		       break;
		    
			case PICK_PHOTO_CODE:
		    	if (data != null) {
		            try {
						Uri photoUri = data.getData();
						// Do something with the photo based on Uri
						Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
						// Load the selected image into a preview
						ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
						ivPreview.setImageBitmap(selectedImage);
						
						value.image = photoUri.toString();
					} catch (IOException e) {
						Log.e(this.getClass().getName(), e.getMessage(), e);
						Toast.makeText(this, "Could not access picture!", Toast.LENGTH_SHORT).show();
					}
		        } else {
		        	Toast.makeText(this, "No picture selected", Toast.LENGTH_SHORT).show();
		        }
		    	break;
		    }
		}
	}
	
	// Returns the Uri for a photo stored on disk given the fileName
	public Uri getPhotoFileUri(String fileName) {
	    // Get safe storage directory for photos
	    File mediaStorageDir = new File(
	        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_TAG);

	    // Create the storage directory if it does not exist
	    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
	        Log.d(APP_TAG, "failed to create directory");
	    }

	    // Return the file target for the photo based on filename
	    return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
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
