package org.axel.dataakansalle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.axel.dataakansalle.SearchHistoryAdapter;


public class MainActivity extends AppCompatActivity {

    private EditText searchBar;
    private Button searchBTN;

    private RecyclerView recyclerView;

    private SearchHistoryAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.municipalityInput);
        searchBTN = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recentSearchesRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchHistoryAdapter(SearchHistoryStorage.getInstance().getSearchHistory());

        adapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                searchBar.setText(item);
            }
        });

        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void switchActivity(View view){
        Intent intent = new Intent(this, MunicipalityActivity.class);
        String municipalityName = searchBar.getText().toString().trim();
        SearchHistoryStorage.getInstance().addSearch(municipalityName);
        adapter.notifyDataSetChanged();
        intent.putExtra("valittuKunta", municipalityName);
        startActivity(intent);
    }
}