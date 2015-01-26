package hackathonbootcamp.needumbrella;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


public class GeoSetting extends ActionBarActivity {

    public static void setSelection(Spinner spinner, String item) {
        SpinnerAdapter adapter = spinner.getAdapter();
        int index = 0;
        String[] strAry = null;
        String str = null;
        for (int i = 0; i < adapter.getCount(); i++) {
            str = (String) adapter.getItem(i);
            if (str.split("：")[0].equals(item)) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_setting);

        Button saveButton = (Button) findViewById(R.id.geoSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                saveGeoClick();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // アイテムを追加します
        adapter.add("00063270397：大阪府大阪市西成区花園北");
        adapter.add("00063293288：奈良県奈良市南紀寺町");
        adapter.add("00064287051：兵庫県神戸市北区山田町下谷上");
        adapter.add("00064264407：京都府京都市山科区東野八反畑町");
        adapter.add("00064253830：滋賀県大津市梅林");
        adapter.add("00064303967：和歌山県和歌山市湊");
        Spinner spinner = null;
        spinner = (Spinner) findViewById(R.id.spnHome);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        setSelection(spinner, sp.getString("GeoHomeString", null));
        spinner = (Spinner) findViewById(R.id.spnBiz);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        setSelection(spinner, sp.getString("GeoBizString", null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geo_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_settings) {
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveGeoClick() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        Spinner spinner = null;
        String item = null;
        String[] strAry = null;
        spinner = (Spinner) findViewById(R.id.spnHome);
        item = (String) spinner.getSelectedItem();
        strAry = item.split("：");
        sp.edit().putString("GeoHomeString", strAry[0]).commit();
        spinner = (Spinner) findViewById(R.id.spnBiz);
        item = (String) spinner.getSelectedItem();
        strAry = item.split("：");
        sp.edit().putString("GeoBizString", strAry[0]).commit();

        Toast.makeText(GeoSetting.this, "保存しました", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, Main.class);
        startActivity(intent);

    }
}
