package ir.hadinemati.steelmarketing.Views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ir.hadinemati.steelmarketing.R;
import ir.hadinemati.steelmarketing.Views.Interfaces.IStatiticsView;

public class StatisticsActivity extends AppCompatActivity implements IStatiticsView {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
