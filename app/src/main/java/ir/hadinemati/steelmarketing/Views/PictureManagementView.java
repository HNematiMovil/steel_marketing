package ir.hadinemati.steelmarketing.Views;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Lib.Http;
import ir.hadinemati.steelmarketing.Lib.UriHelper;
import ir.hadinemati.steelmarketing.Models.Entity.PictureDO;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.IPictureManagementPresenter;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.IProductsSelectionResult;
import ir.hadinemati.steelmarketing.Presenters.PictureManagementPresenter;
import ir.hadinemati.steelmarketing.R;
import ir.hadinemati.steelmarketing.Services.UploadImagesService;
import ir.hadinemati.steelmarketing.Services.Uploading;
import ir.hadinemati.steelmarketing.Views.Interfaces.IPictureManagementView;
import ir.hadinemati.steelmarketing.Views.Widgets.TextViewPersian;

public class PictureManagementView extends AppCompatActivity implements IPictureManagementView {
    String TAG = "PictureManagementView";
    int PICK_IMAGE_CODE = 1;

    List<PictureDO> SelectedImagesPaths;
    List<String> ProductList;
    int UploadingIndex = 0;

    IPictureManagementPresenter pictureManagementPresenter;
    LayoutInflater inflater;
    FlexboxLayout flex;

    String selectedCategory = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);

        pictureManagementPresenter = new PictureManagementPresenter(this, getApplicationContext());
        pictureManagementPresenter.getProductList();
        SelectedImagesPaths = new ArrayList<>();

        inflater = LayoutInflater.from(this);

        initView();
    }

    private void initView() {
        flex = findViewById(R.id.flexPictures);
        findViewById(R.id.btnChooseImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                getIntent.setType("image/*");
//
//                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                Intent chooserIntent = Intent.createChooser(getIntent, "انتخاب تصویر");
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
//
//                startActivityForResult(chooserIntent, PICK_IMAGE_CODE);


                DialogProducts dlgProd = new DialogProducts(
                        PictureManagementView.this,
                        ProductList,
                        new IProductsSelectionResult() {
                            @Override
                            public void selected(String name) {
                                selectedCategory = name;
                                ImagePicker.create(PictureManagementView.this).multi().limit(10).imageTitle(name).start(PICK_IMAGE_CODE);

                            }
                        }
                );
                dlgProd.show();


            }
        });

        findViewById(R.id.btnSync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent UploadIntent = new Intent(PictureManagementView.this, Uploading.class);
                UploadIntent.putExtra("ImagesPath", new Gson().toJson(SelectedImagesPaths));
                startService(UploadIntent);
                Toast.makeText(PictureManagementView.this, "درحال ارسال تصاویر لطفا تا اتمام ارسال تصاویر نرم افزار را نبندید", Toast.LENGTH_LONG).show();
                PictureManagementView.this.finish();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
                for (Image image : images) {
                    SelectedImagesPaths.add(new PictureDO(selectedCategory, image.getPath()));

                    try {
                        final int removeIndex = SelectedImagesPaths.size() - 1;
                        View v = inflater.inflate(R.layout.item_picture, null, false);
                        ((TextView) v.findViewById(R.id.tvName)).setText(selectedCategory);
                        BitmapFactory.Options BitmapOption = new BitmapFactory.Options();
//
                        BitmapOption.inSampleSize = 16;
                        Bitmap imageBitmap = BitmapFactory.decodeFile(image.getPath(),BitmapOption);

                        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 150, 150, true).copy(Bitmap.Config.ARGB_8888, true);
                        ((ImageView) v.findViewById(R.id.ivPicture)).setImageBitmap(imageBitmap);
                        // ((ImageView) v.findViewById(R.id.ivPicture)).setImageURI(data.getData());
                        v.findViewById(R.id.ibDelete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                flex.removeView(v);
                                SelectedImagesPaths.remove(removeIndex);
                            }
                        });


                        data.setData(null);
                        flex.addView(v);
                    } catch (Exception ex) {
                        ex.getStackTrace();
                    }


                }


            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnGenerateProductList(List<String> products) {
        this.ProductList = products;
    }




}
