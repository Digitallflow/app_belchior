package com.digitalflow.belchior.app_belchior.Helper;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by MarllonS on 25/01/2018.
 */

public class HelperAux  extends AppCompatActivity {

   public void getSupportActionB() {
       if (getSupportActionBar() != null) {
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setDisplayShowHomeEnabled(true);
       }
   }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//            finish();
//        return super.onOptionsItemSelected(item);
//    }
}
