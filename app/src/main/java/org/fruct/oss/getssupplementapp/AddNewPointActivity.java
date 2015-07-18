package org.fruct.oss.getssupplementapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Andrey on 18.07.2015.
 */
public class AddNewPointActivity extends Activity {
    Button btCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewpoint);

        btCategory = (Button) findViewById(R.id.activity_addpoint_category);
        btCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
                startActivityForResult(i, Const.INTENT_RESULT_CATEGORY);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addnewpoint, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = new Intent();

        if (id == R.id.action_done){
            //TODO: handler
            setResult(Const.INTENT_RESULT_CODE_OK, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: handler
    }
}
