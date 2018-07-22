package com.telvo.telvoterminaladmin.shop;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.model.shop.action.QrCode;
import com.telvo.telvoterminaladmin.model.shop.action.QrRequest;
import com.telvo.telvoterminaladmin.model.shop.action.QrResponse;
import com.telvo.telvoterminaladmin.model.shop.action.ShopBalanceResponse;
import com.telvo.telvoterminaladmin.model.shop.login.LoginResponseShop;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.sharedpreference.TerminalPreferences;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;
import com.telvo.telvoterminaladmin.util.NumberFormatUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopHomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ShopHomeFragment";
    private TerminalPreferences terminalPreferences;

    private Context context;

    private TextView shopTotalBalanceTextView;
    private EditText etShopAmount;
    private AppCompatButton btnGenerateQRCode;
    private AppCompatButton btnDone;
    private ImageView ivQRCode;
    private TextView[] textViews;
    private ProgressDialog progress;
    private CustomAlertDialog alertDialog;

    private LoginResponseShop loginResponseShop;
    private String shopId;
    private String amount;
    private String token;

    private ApiInterface apiInterface;
    private QrRequest qrRequest = new QrRequest();
    private QrResponse qrResponse;
    private QrCode qrCode;

    private ShopBalanceResponse shopBalanceResponse;

    public ShopHomeFragment() {
        // Required empty public constructor
    }

    public void setTextViews(TextView[] textViews) {
        this.textViews = textViews;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shop_home, container, false);

        context = getActivity();
        loginResponseShop = ((ShopMainActivity) getActivity()).getLoginResponseShop();
        terminalPreferences = new TerminalPreferences(context);
        token = "Bearer " + loginResponseShop.getToken();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        initializeViews(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setAmountView();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setAmountView() {
        shopTotalBalanceTextView.setText(terminalPreferences.getShopTotalBalance());
    }

    private void initializeViews(View view) {
        shopTotalBalanceTextView = view.findViewById(R.id.shopTotalBalanceTextView);
        etShopAmount = view.findViewById(R.id.edit_text_shop_amount);
        btnGenerateQRCode = view.findViewById(R.id.btn_generate_qr_code);
        btnGenerateQRCode.setOnClickListener(this);
        btnDone = view.findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);
        ivQRCode = view.findViewById(R.id.image_view_qr_code);
        alertDialog = new CustomAlertDialog(context);
        progress = new ProgressDialog(context);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_generate_qr_code:
                hideKeyboard(view);

                shopId = loginResponseShop.getShop().getId();
                amount = etShopAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount)) {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_amount), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        showProgressDialog();
                        generateQr();
                    } else {
                        alertDialog.showDialog(context.getResources().getString(R.string.no_internet_connection));
                    }
                }
                break;
            case R.id.btn_done:
                showProgressDialog();
                requestUpdatedBalance();
                etShopAmount.setText("");
                ivQRCode.setVisibility(View.GONE);
                btnDone.setVisibility(View.GONE);
                qrCode = null;
            default:
                return;
        }
    }

    private void requestUpdatedBalance() {
        String id = shopId;
        Call<ShopBalanceResponse> shopBalanceResponseCall = apiInterface.getShopBalance(token, id);
        shopBalanceResponseCall.enqueue(new Callback<ShopBalanceResponse>() {
            @Override
            public void onResponse(Call<ShopBalanceResponse> call, Response<ShopBalanceResponse> response) {
                hideProgressDialog();

                shopBalanceResponse = response.body();

                if (shopBalanceResponse.getStatus().equals(Constants.SUCCESS)) {
                    saveShopBalance();
                    updateShopBalance();
                } else {
                    alertDialog.showDialog(shopBalanceResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ShopBalanceResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void updateShopBalance() {
        shopTotalBalanceTextView.setText(terminalPreferences.getShopTotalBalance());
    }

    private void saveShopBalance() {
        terminalPreferences.putShopTotalBalance(NumberFormatUtils.getFormattedDouble(shopBalanceResponse.getBalance()));
    }

    private void showProgressDialog() {
        progress.setMessage(getString(R.string.please_wait));
        progress.show();
    }

    private void hideProgressDialog() {
        progress.dismiss();
    }

    private void generateQr() {
        setQrRequestValues();

        Call<QrResponse> qrResponseCall = apiInterface.generateQR(token, qrRequest);
        qrResponseCall.enqueue(new Callback<QrResponse>() {
            @Override
            public void onResponse(Call<QrResponse> call, Response<QrResponse> response) {
                hideProgressDialog();
                qrResponse = response.body();

                if (qrResponse.getStatus().equals(Constants.SUCCESS)) {
                    qrCode = qrResponse.getQrCode();
                    String qrString = convertToString(qrCode);
                    Bitmap qrBitmap = convertToQrImage(qrString);
                    ivQRCode.setImageBitmap(qrBitmap);
                    ivQRCode.setVisibility(View.VISIBLE);
                    btnDone.setVisibility(View.VISIBLE);
                } else {
                    alertDialog.showDialog(qrResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<QrResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void setQrRequestValues() {
        qrRequest.setShopId(shopId);
        qrRequest.setAmount(amount);
    }

    private Bitmap convertToQrImage(String qrString) {
        Bitmap bmp = null;
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(qrString, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bmp;
    }

    private String convertToString(QrCode qrCode) {
        Gson gson = new Gson();
        return gson.toJson(qrCode);
    }

    private void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
