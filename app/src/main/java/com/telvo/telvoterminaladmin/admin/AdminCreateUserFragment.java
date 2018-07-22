package com.telvo.telvoterminaladmin.admin;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.model.admin.action.CreateUserResponse;
import com.telvo.telvoterminaladmin.model.admin.login.LoginResponseAdmin;
import com.telvo.telvoterminaladmin.model.currency.Currency;
import com.telvo.telvoterminaladmin.model.currency.CurrencyData;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.ButtonClick;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;
import com.telvo.telvoterminaladmin.util.DatePicker;
import com.telvo.telvoterminaladmin.util.PermissionManager;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminCreateUserFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CreateUserFragment";
    private static int REQUEST_IMAGE = 1;
    private static int REQUEST_CAMERA = 0;
    private static int IMAGE_TYPE = -1;

    private Context context;
    private Intent intent;
    private FragmentManager fragmentManager;
    private TextView[] textViews = new TextView[7];
    // Personal info
    private CircularImageView userPhotoImageView;
    private ImageView uploadPhotoImageView;
    private EditText userNameEditText;
    private EditText userPhoneEditText;
    private EditText userEmailEditText;
    private EditText fatherNameEditText;
    private EditText motherNameEditText;
    private EditText spouseNameEditText;
    private Spinner nationalitySpinner;
    private DatePicker dateOfBirthEditText;
    private ImageView calendarImageView;
    private RadioGroup genderRadioGroup;
    private RadioGroup authenticationTypeRadioGroup;
    private EditText authenticationNumberEditText;
    private ImageView signatureImageView;
    // Present address
    private EditText presentAddressEditText;
    private EditText presentCityEditText;
    private EditText presentPostalCodeEditText;
    private Spinner presentCountrySpinner;
    // Permanent address
    private EditText permanentAddressEditText;
    private EditText permanentCityEditText;
    private EditText permanentPostalCodeEditText;
    private Spinner permanentCountrySpinner;
    // Currency
    private Spinner currencySpinner;
    // Introducer
    private EditText introducerNameEditText;
    private CountryCodePicker countryCodePicker;
    private EditText introducerPhoneEditText;
    private EditText introducerRelationEditText;

    private Button submitButton;

    private CustomAlertDialog alertDialog;
    private ProgressDialog progress;

    private String selectedItem = null;
    private Uri imageFileUri;
    private Uri signImageFileUri;
    private String userPicturePath;
    private String signPicturePath;
    private String name;
    private String mobileNumber;
    private String email;
    private String fatherName;
    private String motherName;
    private String spouseName;
    private String nationality;
    private String dob;
    private String gender;
    private String nid;
    private String nidType;
    private String presentAddress;
    private String presentCity;
    private String presentPostalCode;
    private String presentCountry;
    private String permanentAddress;
    private String permanentCity;
    private String permanentPostalCode;
    private String permanentCountry;
    private String currency;
    private String introducerName;
    private String introducerCountryCode;
    private String introducerMobileNumber;
    private String introducerRelation;
    private File imageFile;
    private File signImageFile;
    private Map<String, RequestBody> params = new HashMap<>();
    private MultipartBody.Part image;
    private MultipartBody.Part signImage;

    private String token;
    private ApiInterface apiInterface;
    private LoginResponseAdmin loginResponseAdmin;
    private CreateUserResponse createUserResponse = new CreateUserResponse();

    public AdminCreateUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_create_user, container, false);

        context = getActivity();
        loginResponseAdmin = ((AdminMainActivity) getActivity()).getLoginResponseAdmin();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        token = "Bearer " + loginResponseAdmin.getToken();
        Log.i(TAG, "token: " + token);

        initializeViews(rootView);
        setupNationalitySpinner();
        setupPresentCountrySpinner();
        setupPermanentCountrySpinner();
        setupCurrencySpinner();

        return rootView;
    }

    private void initializeViews(View view) {
        // Personal
        userPhotoImageView = view.findViewById(R.id.circularImageView);
        uploadPhotoImageView = view.findViewById(R.id.uploadPhotoImageView);
        uploadPhotoImageView.setOnClickListener(this);
        userNameEditText = view.findViewById(R.id.userNameEditText);
        userPhoneEditText = view.findViewById(R.id.userPhoneEditText);
        userEmailEditText = view.findViewById(R.id.emailEditText);
        fatherNameEditText = view.findViewById(R.id.fatherNameEditText);
        motherNameEditText = view.findViewById(R.id.motherNameEditText);
        spouseNameEditText = view.findViewById(R.id.spouseNameEditText);
        nationalitySpinner = view.findViewById(R.id.nationalitySpinner);
        dateOfBirthEditText = view.findViewById(R.id.dateOfBirthEditText);
        calendarImageView = view.findViewById(R.id.calendarImageView);
        calendarImageView.setOnClickListener(this);
        genderRadioGroup = view.findViewById(R.id.genderRadioGroup);
        authenticationTypeRadioGroup = view.findViewById(R.id.authenticationTypeRadioGroup);
        authenticationTypeRadioGroup.setOnClickListener(this);
        authenticationNumberEditText = view.findViewById(R.id.authenticationNumberEditText);
        signatureImageView = view.findViewById(R.id.signatureImageView);
        signatureImageView.setOnClickListener(this);
        // Present address
        presentAddressEditText = view.findViewById(R.id.presentAddressEditText);
        presentCityEditText = view.findViewById(R.id.presentCityEditText);
        presentPostalCodeEditText = view.findViewById(R.id.presentPostalCodeEditText);
        presentCountrySpinner = view.findViewById(R.id.presentCountrySpinner);
        // Permanent
        permanentAddressEditText = view.findViewById(R.id.permanentAddressEditText);
        permanentCityEditText = view.findViewById(R.id.permanentCityEditText);
        permanentPostalCodeEditText = view.findViewById(R.id.permanentPostalCodeEditText);
        permanentCountrySpinner = view.findViewById(R.id.permanentCountrySpinner);
        // Currency
        currencySpinner = view.findViewById(R.id.currencySpinner);
        // Introducer
        introducerNameEditText = view.findViewById(R.id.introducerNameEditText);
        countryCodePicker = view.findViewById(R.id.countryCodePicker);
        introducerPhoneEditText = view.findViewById(R.id.introducerPhoneNumberEditText);
        introducerRelationEditText = view.findViewById(R.id.introducerRelationEditText);

        submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        alertDialog = new CustomAlertDialog(context);
        progress = new ProgressDialog(context);
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadPhotoImageView:
                IMAGE_TYPE = 0;
                selectImage();
                break;
            case R.id.calendarImageView:
                dateOfBirthEditText.requestFocus();
                Toast.makeText(context, "calendarImageView click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.authenticationTypeRadioGroup:
                setAuthenticationTypeHint();
                break;
            case R.id.signatureImageView:
                alertDialog.showDialogWithYes(getString(R.string.signature_upload_message), new ButtonClick() {
                    @Override
                    public void Do() {
                        IMAGE_TYPE = 1;
                        selectImage();
                    }
                });
                break;
            case R.id.submitButton:
                // personal info
                name = userNameEditText.getText().toString().trim();
                mobileNumber = "88" + userPhoneEditText.getText().toString().trim();
                email = userEmailEditText.getText().toString().trim();
                fatherName = fatherNameEditText.getText().toString().trim();
                motherName = motherNameEditText.getText().toString().trim();
                spouseName = spouseNameEditText.getText().toString().trim();
                nationality = nationalitySpinner.getSelectedItem().toString().trim();
                dob = dateOfBirthEditText.getText().toString().trim();
                if (genderRadioGroup.getCheckedRadioButtonId() == R.id.maleRadioButton) {
                    gender = "male";
                } else if (genderRadioGroup.getCheckedRadioButtonId() == R.id.femaleRadioButton) {
                    gender = "female";
                }
                if (authenticationTypeRadioGroup.getCheckedRadioButtonId() == R.id.nationalIdRadioButton) {
                    nidType = "0";
                } else if (authenticationTypeRadioGroup.getCheckedRadioButtonId() == R.id.passportNoRadioButton) {
                    nidType = "1";
                }
                nid = authenticationNumberEditText.getText().toString().trim();
                // present address
                presentAddress = presentAddressEditText.getText().toString().trim();
                presentCity = presentCityEditText.getText().toString().trim();
                presentPostalCode = presentPostalCodeEditText.getText().toString().trim();
                presentCountry = presentCountrySpinner.getSelectedItem().toString().trim();
                //permanent address
                permanentAddress = permanentAddressEditText.getText().toString().trim();
                permanentCity = permanentCityEditText.getText().toString().trim();
                permanentPostalCode = permanentPostalCodeEditText.getText().toString().trim();
                permanentCountry = permanentCountrySpinner.getSelectedItem().toString().trim();
                // currency
                currency = currencySpinner.getSelectedItem().toString().trim();
                // introducer
                introducerName = introducerNameEditText.getText().toString().trim();
                introducerCountryCode = countryCodePicker.getSelectedCountryCode();
                introducerMobileNumber = introducerPhoneEditText.getText().toString().trim();
                introducerRelation = introducerRelationEditText.getText().toString().trim();

                if (TextUtils.isEmpty(userPicturePath)) {
                    Toast.makeText(context, getString(R.string.please_add_user_image), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name)) {
                    Toast.makeText(context, getString(R.string.please_enter_user_name), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(context, getString(R.string.please_enter_user_phone), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(context, getString(R.string.please_enter_user_email), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(fatherName)) {
                    Toast.makeText(context, getString(R.string.please_enter_father_name), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(motherName)) {
                    Toast.makeText(context, getString(R.string.please_enter_mother_name), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(spouseName)) {
                    Toast.makeText(context, getString(R.string.please_enter_spouse_name), Toast.LENGTH_SHORT).show();
                } else if (nationalitySpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, getString(R.string.please_select_nationality), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(dob)) {
                    Toast.makeText(context, getString(R.string.please_enter_date_of_birth), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(gender)) {
                    Toast.makeText(context, getString(R.string.please_select_gender), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(nid)) {
                    Toast.makeText(context, getString(R.string.please_enter_nid_or_passport_no), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(signPicturePath)) {
                    Toast.makeText(context, getString(R.string.please_add_signature_image), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(presentAddress)) {
                    Toast.makeText(context, getString(R.string.please_enter_present_address), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(presentCity)) {
                    Toast.makeText(context, getString(R.string.please_enter_present_city), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(presentPostalCode)) {
                    Toast.makeText(context, getString(R.string.please_enter_present_postal_code), Toast.LENGTH_SHORT).show();
                } else if (presentCountrySpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, getString(R.string.please_select_present_country), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(permanentAddress)) {
                    Toast.makeText(context, getString(R.string.please_enter_permanent_address), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(permanentCity)) {
                    Toast.makeText(context, getString(R.string.please_enter_permanent_city), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(permanentPostalCode)) {
                    Toast.makeText(context, getString(R.string.please_enter_permanent_postal_code), Toast.LENGTH_SHORT).show();
                } else if (permanentCountrySpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(context, getString(R.string.please_select_permanent_country), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(introducerName)) {
                    Toast.makeText(context, getString(R.string.please_enter_introducer_name), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(introducerMobileNumber)) {
                    Toast.makeText(context, getString(R.string.please_enter_introducer_phone_no), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(introducerRelation)) {
                    Toast.makeText(context, getString(R.string.please_enter_introducer_relation), Toast.LENGTH_SHORT).show();
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        showProgressDialog();
                        createUser();
                    } else {
                        alertDialog.showDialog(context.getResources().getString(R.string.no_internet_connection));
                    }
                }
                break;
            default:
        }
    }

    private void setAuthenticationTypeHint() {
        if (authenticationTypeRadioGroup.getCheckedRadioButtonId() == R.id.nationalIdRadioButton) {
            authenticationNumberEditText.setHint(getString(R.string.national_id));
        } else {
            authenticationNumberEditText.setHint(getString(R.string.passport_number));
        }
    }

    private void createUser() {
        prepareCreateUserData();

        //Log.e(TAG, "createUser: info - " + info.toString());
        Log.i(TAG, "createUser: image - " + image.body().contentType());
        Log.i(TAG, "createUser: signImage - " + signImage.body().contentType());

        Call<CreateUserResponse> responseCall = apiInterface.createUser(token, params, image, signImage);
        responseCall.enqueue(new Callback<CreateUserResponse>() {
            @Override
            public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                hideProgressDialog();

                createUserResponse = response.body();
                if (createUserResponse != null) {
                    if (createUserResponse.getStatus().equals(Constants.SUCCESS)) {
                        alertDialog.showDialog(createUserResponse.getMessage());
                    } else {
                        alertDialog.showDialog(createUserResponse.getMessage());
                    }
                } else {
                    hideProgressDialog();
                    alertDialog.showDialog(getString(R.string.unable_to_connect));
                }
            }

            @Override
            public void onFailure(Call<CreateUserResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void prepareCreateUserData() {
        params.put("name", createPartFromString(name));
        params.put("mobileNumber", createPartFromString(mobileNumber));
        params.put("email", createPartFromString(email));
        params.put("currency", createPartFromString(currency));
        params.put("fatherName", createPartFromString(fatherName));
        params.put("motherName", createPartFromString(motherName));
        params.put("nationality", createPartFromString(nationality));
        params.put("dob", createPartFromString(dob));
        params.put("gender", createPartFromString(gender));
        params.put("spouseName", createPartFromString(spouseName));
        params.put("nid", createPartFromString(nid));
        params.put("nidType", createPartFromString(nidType));
        params.put("presentAddress", createPartFromString(presentAddress));
        params.put("presentCity", createPartFromString(presentCity));
        params.put("presentPostalCode", createPartFromString(presentPostalCode));
        params.put("presentCountry", createPartFromString(presentCountry));
        params.put("permanentAddress", createPartFromString(permanentAddress));
        params.put("permanentCity", createPartFromString(permanentCity));
        params.put("permanentPostalCode", createPartFromString(permanentPostalCode));
        params.put("permanentCountry", createPartFromString(permanentCountry));
        params.put("introducerName", createPartFromString(introducerName));
        params.put("introducerMobileNumber", createPartFromString(introducerMobileNumber));
        params.put("introducerRelation", createPartFromString(introducerRelation));
        params.put("introducerCountryCode", createPartFromString(introducerCountryCode));

        image = prepareFilePart("image", imageFile);
        signImage = prepareFilePart("signImage", signImageFile);
    }

    private RequestBody createPartFromString(String str) {
        return RequestBody.create(MultipartBody.FORM, str);
    }

    private MultipartBody.Part prepareFilePart(String partName, File file) {
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void showProgressDialog() {
        progress.setMessage(getString(R.string.please_wait));
        progress.show();
    }

    private void hideProgressDialog() {
        progress.dismiss();
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.add_photo);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                selectedItem = items[item].toString();
                if (selectedItem.equals(getString(R.string.take_photo))) {
                    if (PermissionManager.checkPermission(context)) {
                        cameraIntent();
                    }
                } else if (selectedItem.equals(getString(R.string.choose_from_gallery))) {
                    if (PermissionManager.checkPermission(context)) {
                        imagePickerIntent();
                    }
                } else if (selectedItem.equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        CropImage.activity().start(getContext(), this);
    }

    private void imagePickerIntent() {
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ");

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && null != data) {
            Log.i(TAG, "onActivityResult: if");
            Uri selectedImage = data.getData();
            CropImage.activity(selectedImage).start(getActivity());
        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && null != data) {
            Log.i(TAG, "onActivityResult: 1st else if");
            Uri selectedImage = data.getData();
//            CropImage.activity(selectedImage).start(getActivity());

            CropImage.activity(selectedImage).start(context, AdminCreateUserFragment.this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.i(TAG, "onActivityResult: 2nd else if");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    if (IMAGE_TYPE == 0) {
                        Log.i(TAG, "onActivityResult: IMAGE_TYPE 0");
                        imageFileUri = result.getUri();
                        Log.i(TAG, "onActivityResult: imageFileUri" + imageFileUri);
                        userPhotoImageView.setImageURI(imageFileUri);
                        imageFile = new File(getRealPathFromURI(imageFileUri));
                        imageFile = new Compressor(context).setQuality(80).compressToFile(imageFile);
                        userPicturePath = imageFile.getAbsolutePath();
                        Log.i(TAG, "User image file path: " + userPicturePath);
                    } else if (IMAGE_TYPE == 1) {
                        Log.i(TAG, "onActivityResult: IMAGE_TYPE 1");
                        signImageFileUri = result.getUri();
                        Log.i(TAG, "onActivityResult: imageFileUri" + signImageFileUri);
                        signatureImageView.setImageURI(signImageFileUri);
                        signImageFile = new File(getRealPathFromURI(signImageFileUri));
                        signImageFile = new Compressor(context).setQuality(80).compressToFile(signImageFile);
                        signPicturePath = signImageFile.getAbsolutePath();
                        Log.i(TAG,"Sign image file path: " + signImageFile.getAbsolutePath());
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        Log.i(TAG, "user picture path: " + userPicturePath);
        Log.i(TAG, "sign picture path: " + signPicturePath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (selectedItem.equals(getString(R.string.take_photo))) {
                        if (PermissionManager.checkPermission(context)) {
                            cameraIntent();
                        }
                    } else if (selectedItem.equals(getString(R.string.choose_from_gallery))) {
                        if (PermissionManager.checkPermission(context)) {
                            imagePickerIntent();
                        }
                    }
                }
                break;
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void setupNationalitySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.nationality_array, R.layout.spinner_item_create_user);
        adapter.setDropDownViewResource(R.layout.spinner_item_create_user);
        nationalitySpinner.setAdapter(adapter);
    }

    private void setupPresentCountrySpinner() {
        List<String> countries = getCountryList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item_create_user, countries);
        adapter.setDropDownViewResource(R.layout.spinner_item_create_user);
        presentCountrySpinner.setAdapter(adapter);
    }

    private void setupPermanentCountrySpinner() {
        List<String> countries = getCountryList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item_create_user, countries);
        adapter.setDropDownViewResource(R.layout.spinner_item_create_user);
        permanentCountrySpinner.setAdapter(adapter);
    }

    private void setupCurrencySpinner() {
        List<String> currencies = getCurrencyList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item_create_user, currencies);
        adapter.setDropDownViewResource(R.layout.spinner_item_create_user);
        currencySpinner.setAdapter(adapter);
    }

    private List<String> getCountryList() {
        List<String> countries = new ArrayList<>();
        String countryString = AppManager.loadJSONFromAsset(context, "currency");
        Currency currency = new Gson().fromJson(countryString, Currency.class);
        countries.add(getString(R.string.select_country));

        for (CurrencyData currencyData : currency.getCcyNtry()) {
            String result = currencyData.getCtryNm();
            countries.add(result);
        }
        return countries;
    }

    private List<String> getCurrencyList() {
        List<String> currencies = new ArrayList<>();
        String currencyString = AppManager.loadJSONFromAsset(context, "currency");
        Currency currency = new Gson().fromJson(currencyString, Currency.class);

        currencies.add(getString(R.string.select_currency));
        for (CurrencyData currencyData : currency.getCcyNtry()) {
            String result = currencyData.getCcy();
            currencies.add(result);
        }
        return currencies;
    }
}
