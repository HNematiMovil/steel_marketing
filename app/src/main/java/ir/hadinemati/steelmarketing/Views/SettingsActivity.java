package ir.hadinemati.steelmarketing.Views;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;
import java.util.List;

import ir.hadinemati.steelmarketing.Lib.Adapters.IgnoreContactListRecyclerAdapter;
import ir.hadinemati.steelmarketing.Lib.Adapters.ProductListRecyclerAdapter;
import ir.hadinemati.steelmarketing.Lib.ViewAnimation;
import ir.hadinemati.steelmarketing.Models.Entity.ContactDO;
import ir.hadinemati.steelmarketing.Presenters.SettingsPresenter;
import ir.hadinemati.steelmarketing.R;
import ir.hadinemati.steelmarketing.Views.Interfaces.ISettingView;

public class SettingsActivity extends AppCompatActivity implements ISettingView, View.OnClickListener {


    SettingsPresenter _settingsPresenter;


    NestedScrollView nested_scroll_view;

    /// ignore contacts
    Button BtnHideIgnoreContactsSection;
    View IgnoreContactsSection;
    ImageButton IBToggleIgnoreContactsSection, IBAddNewIgnoringContact;
    TextView tvSettingsIgnoreContactsSection;
    RecyclerView IgnoreContactsRecycler;

    BottomSheetBehavior mIGbottomSheetBehavior;
    BottomSheetDialog mIGbottomSheetDialog;
    View IGBottomSheet;


    /// products

    Button BtnHideProductsSection;
    View ProductsSection;
    ImageButton IBToggleProductsSection, IBAddNewProduct;
    TextView tvSettingsProductsSection;
    RecyclerView ProductsRecycler;

    BottomSheetBehavior mPbottomSheetBehavior;
    BottomSheetDialog mPbottomSheetDialog;
    View PBottomSheet;

    /// sheet

    EditText etNewContactName, etNewContactNumber , etNewProductName;


    int REQUEST_CODE_CONTACTS = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();

        _settingsPresenter = new SettingsPresenter(this, getApplicationContext());

        _settingsPresenter.getAllIgnoredContacts();
        _settingsPresenter.getAllProducts();


    }

    private void initView() {


        IGBottomSheet = findViewById(R.id.BottomSheetAddNewContact);
        mIGbottomSheetBehavior = BottomSheetBehavior.from(IGBottomSheet);


        PBottomSheet = findViewById(R.id.BottomSheetAddNewProduct);
        mPbottomSheetBehavior = BottomSheetBehavior.from(PBottomSheet);


        nested_scroll_view = findViewById(R.id.nested_scroll_view);

        // Ignore Contact section
        IBToggleIgnoreContactsSection = findViewById(R.id.SettingsIBToggleIgnoreContactsSection);
        BtnHideIgnoreContactsSection = findViewById(R.id.SettingsBtnIgnoreContactsSectionClose);
        IgnoreContactsSection = findViewById(R.id.IgnoreContactsSection);
        tvSettingsIgnoreContactsSection = findViewById(R.id.SettingsTVIgnoreContactsSectionTitle);
        IBAddNewIgnoringContact = findViewById(R.id.SettingsIBIgnoreContactsSectionAddNew);
        IgnoreContactsRecycler = findViewById(R.id.SettingsRecyclerIgnoredContacts);


        /// products section
        IBToggleProductsSection = findViewById(R.id.SettingsIBToggleProductSection);
        BtnHideProductsSection = findViewById(R.id.SettingsBtnProductSectionClose);
        ProductsSection = findViewById(R.id.ProductSection);
        tvSettingsProductsSection = findViewById(R.id.SettingsTVProductSectionTitle);
        IBAddNewProduct = findViewById(R.id.SettingsIBProductSectionAddNew);
        ProductsRecycler = findViewById(R.id.SettingsRecyclerProducts);


        // initial state closed
        IgnoreContactsSection.setVisibility(View.GONE);
        IBAddNewIgnoringContact.animate().setDuration(10).alpha(0);

        // initial state closed
        ProductsSection.setVisibility(View.GONE);
        IBAddNewProduct.animate().setDuration(10).alpha(0);


        // click
        IBToggleIgnoreContactsSection.setOnClickListener(this);
        BtnHideIgnoreContactsSection.setOnClickListener(this);
        IBAddNewIgnoringContact.setOnClickListener(this);

        IBToggleProductsSection.setOnClickListener(this);
        BtnHideProductsSection.setOnClickListener(this);
        IBAddNewProduct.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SettingsIBToggleIgnoreContactsSection:
            case R.id.SettingsBtnIgnoreContactsSectionClose:
                ToggleIgnoreContactsSection(IBToggleIgnoreContactsSection);
                break;
            case R.id.SettingsIBToggleProductSection:
            case R.id.SettingsBtnProductSectionClose:
                ToggleProductsSection(IBToggleProductsSection);
                break;
            case R.id.SettingsIBProductSectionAddNew:
                ShowAddNewProductBottomSheet();
                break;
            case R.id.SettingsIBIgnoreContactsSectionAddNew:
                ShowAddNewIgnoreContactBottomSheet();
                break;
        }
    }

    private void ShowAddNewIgnoreContactBottomSheet() {


        if (mIGbottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            mIGbottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        View v = getLayoutInflater().inflate(R.layout.sheet_add_new_ignore_contact, null);

        mIGbottomSheetDialog = new BottomSheetDialog(this);
        mIGbottomSheetDialog.setContentView(v);

        etNewContactName = v.findViewById(R.id.SheetAddIgnoreContactETName);
        etNewContactNumber = v.findViewById(R.id.SheetAddIgnoreContactETPhoneNumber);

        mIGbottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        v.findViewById(R.id.SheetAddIgnoreContactBtnChooseFromContacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactIntent, REQUEST_CODE_CONTACTS);

            }
        });


        v.findViewById(R.id.SheetAddIgnoreContactBtnChooseSaveContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _settingsPresenter.AddNewIgnoredContact(etNewContactNumber.getText().toString(), etNewContactName.getText().toString());

            }
        });


        mIGbottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mIGbottomSheetDialog = null;
            }
        });
        mIGbottomSheetDialog.show();

    }

    private void ShowAddNewProductBottomSheet() {


        if (mPbottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            mPbottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        View v = getLayoutInflater().inflate(R.layout.sheet_add_new_product, null);

        mPbottomSheetDialog = new BottomSheetDialog(this);
        mPbottomSheetDialog.setContentView(v);

        etNewProductName = v.findViewById(R.id.SheetAddProductName);

        mPbottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        
        v.findViewById(R.id.SheetAddProductBtnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _settingsPresenter.AddNewProduct(etNewProductName.getText().toString());
            }
        });
        
        mPbottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mPbottomSheetDialog = null;
            }
        });
        mPbottomSheetDialog.show();

    }


    private void ToggleIgnoreContactsSection(View view) {
        boolean show = toggleArrow(view);
        IBAddNewIgnoringContact.animate().setDuration(200).alpha(show ? 1 : 0);
        if (show) {
            ViewAnimation.expand(IgnoreContactsSection, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                   // Tools.nestedScrollTo(nested_scroll_view, IgnoreContactsSection);
                }
            });
        } else {
            ViewAnimation.collapse(IgnoreContactsSection);
        }
    }

    private void ToggleProductsSection(View view) {
        boolean show = toggleArrow(view);
        IBAddNewProduct.animate().setDuration(200).alpha(show ? 1 : 0);
        if (show) {
            ViewAnimation.expand(ProductsSection, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    //Tools.nestedScrollTo(nested_scroll_view, ProductsSection);
                }
            });
        } else {
            ViewAnimation.collapse(ProductsSection);
        }
    }


    public boolean toggleArrow(View view) {

        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }

    }


    @Override
    public void OnIgnoreContactsListGenerated(List<ContactDO> ignoredContactList) {

        IgnoreContactsRecycler.setLayoutManager(new LinearLayoutManager(this));
        IgnoreContactsRecycler.setAdapter(new IgnoreContactListRecyclerAdapter(ignoredContactList, this, _settingsPresenter));

        if(mIGbottomSheetDialog == null)
            return;


        if(mIGbottomSheetDialog.isShowing()){
            mIGbottomSheetDialog.dismiss();
        }

    }

    @Override
    public void OnIgnoreContactsListItemRemoved(List<ContactDO> ignoredContactList) {
        IgnoreContactsRecycler.setLayoutManager(new LinearLayoutManager(this));
        IgnoreContactsRecycler.setAdapter(new IgnoreContactListRecyclerAdapter(ignoredContactList, this, _settingsPresenter));
    }

    @Override
    public void OnNewContactExists() {
        Toast.makeText(this, "این شماره قبلا افزوده شده است", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnProductListGenerated(List<String> products) {

        ProductsRecycler.setLayoutManager(new LinearLayoutManager(this));
        ProductsRecycler.setAdapter(new ProductListRecyclerAdapter(this,products, Arrays.asList(getResources().getStringArray(R.array.products)),_settingsPresenter));

        if(mPbottomSheetDialog == null)
            return;

        mPbottomSheetDialog.dismiss();


    }

    @Override
    public void OnNewProductExists() {
        Toast.makeText(this, "محصول مورد نظر قبلا اضافه شده است", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Toast.makeText(this, "on", Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_CODE_CONTACTS) {
            //  Toast.makeText(this, "on2:" + requestCode + ":" + resultCode, Toast.LENGTH_SHORT).show();
            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();
                ContentResolver contentResolver = getContentResolver();
                Cursor contentCursor = contentResolver.query(uri, null, null, null, null);

                if (contentCursor.moveToFirst()) {
                    String id = contentCursor.getString(contentCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone =
                            contentCursor.getString(contentCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                        phones.moveToFirst();
                        String contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Toast.makeText(this, "" + contactNumber, Toast.LENGTH_SHORT).show();

                        etNewContactName.setText(contentCursor.getString(contentCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        etNewContactNumber.setText(contactNumber);
                    }
                }


            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}