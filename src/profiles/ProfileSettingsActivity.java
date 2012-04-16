package profiles;

import java.util.ArrayList;
import java.util.List;

import org.genshin.warehouse.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileSettingsActivity extends Activity {
	private int selectedProfile;
	private Profiles profiles;
	private Boolean creatingNew;
	
	Spinner spinner;
	Button newButton;
	Button saveButton;
	Button deleteButton;
	Button testButton;
	
    EditText server;
    EditText user;
    EditText password;
	
    private void hookupInterface() {
    	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                //Spinner spinner = (Spinner)parent;
                //int item = spinner.getSelectedItemPosition();
                selectProfile(position);
            }

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
		});
        
        
        deleteButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		profiles.deleteProfile(profiles.list.get(selectedProfile));
        		loadProfiles();
            }
		});
        
        
        newButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		setNew();
            }
		});
        
        saveButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		if (creatingNew)
        			createProfile();
        		else
        			updateProfile();
            }
		});
    }
    
    private void initViewElements() {
    	server = (EditText) findViewById(R.id.server_input);
        user = (EditText) findViewById(R.id.username_input);
        password = (EditText) findViewById(R.id.password_input);
        
        spinner = (Spinner) findViewById(R.id.profile_spinner);
        
        newButton = (Button) findViewById(R.id.profile_new_button);
        deleteButton = (Button) findViewById(R.id.profile_delete_button);
        saveButton = (Button) findViewById(R.id.profile_save_button);
        testButton = (Button) findViewById(R.id.profile_test_button);
    }
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);
        
        initViewElements();
        hookupInterface();
	}
	
	private void selectProfile(int position) {
		//TODO create new
        selectedProfile = position;
        
        //insert values into fields for view/edit
        server.setText(profiles.list.get(position).server);
        user.setText(profiles.list.get(position).user);
        password.setText(profiles.list.get(position).password);
    }
	
	private void loadProfiles() {
		creatingNew = false;
		
		profiles = new Profiles(this);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        for (int i = 0; i < profiles.list.size(); i++) {
	        	adapter.add(profiles.list.get(i).user + ":" + profiles.list.get(i).server);
	        }
	        
	        spinner.setAdapter(adapter);
		
		if (profiles.list.size() == 0)
		{
			setNew();
			return;
		}
		
		selectProfile(0);
	}
	
	private void setNew() {
		//set the creatingNew flag
		creatingNew = true;
		
		//TODO set a new entry in the spinner
		server.setText("");
		user.setText("");
		password.setText("");
	}
	
	private void createProfile() {
		profiles.createProfile(server.getText().toString(), user.getText().toString(), password.getText().toString());
		loadProfiles();
	}
	
	private void updateProfile() {
		profiles.updateProfile(profiles.list.get(selectedProfile));
		loadProfiles();
	}
}
