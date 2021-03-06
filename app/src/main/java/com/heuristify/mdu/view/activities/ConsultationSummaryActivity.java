package com.heuristify.mdu.view.activities;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.heuristify.mdu.R;
import com.heuristify.mdu.adapter.BluetoothDeviceAdapter;
import com.heuristify.mdu.adapter.ConsultationSummaryAdapter;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.databinding.ActivityConsultationSummaryBinding;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.helper.UnicodeFormatter;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;
import com.heuristify.mdu.interfaces.OnItemClickPosition;
import com.heuristify.mdu.mvvm.viewmodel.ConsultationViewModel;
import com.heuristify.mdu.pojo.Bluetooth;
import com.heuristify.mdu.pojo.PatientPrescribedMedicine;
import com.heuristify.mdu.pojo.PatientPrescribedMedicineAndDiagnosis;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConsultationSummaryActivity extends BindingBaseActivity<ActivityConsultationSummaryBinding> implements OnClickHandlerInterface, LifecycleOwner, OnItemClickPosition {
    public String TAG = "ConsultationSummaryActivity";
    private int consultation_id;
    private List<PatientPrescribedMedicine> patientPrescribedMedicinesList;
    private ConsultationSummaryAdapter consultationSummaryAdapter;

    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    private BluetoothDevice mBluetoothDevice;
    private final UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Dialog mDialog;
    private BluetoothDeviceAdapter bluetoothDeviceAdapter;
    private List<Bluetooth> bluetoothList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothList = new ArrayList<>();
        LifecycleOwner lifecycleOwner = this;
        Observer observer;
        getDataBinding().setClickHandler(this);
        initializeRecycleView();

        ConsultationViewModel consultationViewModel = ViewModelProviders.of(this).get(ConsultationViewModel.class);

        if (getIntent().getExtras() != null) {
            Patient patient = (Patient) getIntent().getSerializableExtra("patient");
            consultationViewModel.getPatientImagePath(patient.getId());
            DisplayLog.showLog(TAG, " patientID " + patient.getId());
            getDataBinding().editTextTextFullName.setText(patient.getFullName());
            getDataBinding().editTextTextAge.setText(String.valueOf(patient.getAge()));
            getDataBinding().editTextGender.setText(patient.getGender());
            consultation_id = getIntent().getExtras().getInt("consultation_id");
        }

        observer = (Observer<PatientPrescribedMedicineAndDiagnosis>) patientPrescribedMedicineAndDiagnosis -> {

            if (patientPrescribedMedicineAndDiagnosis.getDiagnosisAndMedicine() != null) {
                getDataBinding().editTextTextPatientDiagnosis.setText(patientPrescribedMedicineAndDiagnosis.getDiagnosisAndMedicine().getPatientDiagnosis());
                getDataBinding().editTextTextPatientDescription.setText(patientPrescribedMedicineAndDiagnosis.getDiagnosisAndMedicine().getDescription());
            }

            if (patientPrescribedMedicineAndDiagnosis.getList() != null) {
                DisplayLog.showLog(TAG, "prescribed_med_list " + patientPrescribedMedicineAndDiagnosis.getList().size());
                patientPrescribedMedicinesList.addAll(patientPrescribedMedicineAndDiagnosis.getList());
                consultationSummaryAdapter.notifyDataSetChanged();
            }

        };

        consultationViewModel.getPatientPrescribedMedicineList(consultation_id).observe(this, observer);

        consultationViewModel.patientImageMutableLiveDate().observe(this, s -> {
            Log.e("image", "" + s);
            if (s != null) {
                if (s.contains("http") || s.contains("https")) {

                    Glide.with(this)
                            .load(s)
                            .into(getDataBinding().imageView3);

                } else {
                    File file = new File(s);
                    String filePath = file.getPath();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    getDataBinding().imageView3.setImageBitmap(bitmap);
                }

            }
        });

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_consultation_summary;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                gotoDashBoard();
                break;
            case R.id.textViewBack:
                gotoDashBoard();
                break;
            case R.id.buttonPrint:
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    listPairedDevices();
                }

                break;
            case R.id.buttonDone:
                Intent intent = new Intent(ConsultationSummaryActivity.this, AddNewConsultationActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    private void gotoDashBoard() {
//        Intent intent = new Intent(ConsultationSummaryActivity.this, DashboardActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        finish();
    }

    private void initializeRecycleView() {
        patientPrescribedMedicinesList = new ArrayList<>();
        getDataBinding().recyclerViewConsultation.setHasFixedSize(true);
        getDataBinding().recyclerViewConsultation.setItemAnimator(new DefaultItemAnimator());
        getDataBinding().recyclerViewConsultation.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        consultationSummaryAdapter = new ConsultationSummaryAdapter(this, patientPrescribedMedicinesList);
        getDataBinding().recyclerViewConsultation.setAdapter(consultationSummaryAdapter);
        getDataBinding().recyclerViewConsultation.setItemAnimator(null);
    }

    private void showDevicesDialog() {

        mDialog = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_MinWidth);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_bluetooth_device_dialog_view);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(false);
        mDialog.show();
        RecyclerView recyclerView = mDialog.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bluetoothDeviceAdapter = new BluetoothDeviceAdapter(bluetoothList, mContext);
        recyclerView.setAdapter(bluetoothDeviceAdapter);
        recyclerView.setItemAnimator(null);
        bluetoothDeviceAdapter.setOnItemClickPosition(this);

        Button btnDismiss = mDialog.findViewById(R.id.buttonDismiss);
        btnDismiss.setOnClickListener(v -> mDialog.dismiss());

    }

    private void listPairedDevices() {
        if (bluetoothList != null) {
            bluetoothList.clear();
        }
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.e(TAG, "PairedDevices: " + " Name " + mDevice.getName() + " Address " + mDevice.getAddress());
                bluetoothList.add(new Bluetooth(mDevice.getName(), mDevice.getAddress()));

            }
            if (bluetoothList != null) {
                showDevicesDialog();
            }
        }
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            if (nOpenSocket.isConnected()) {
                nOpenSocket.close();
            }
            Log.e(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    public byte[] printPhoto() {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    R.drawable.assignment_dark_24px);
            if (bmp != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                return stream.toByteArray();
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
        return new byte[0];
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(ConsultationSummaryActivity.this, "Device Connected", Toast.LENGTH_SHORT).show();
        }
    };

    public void run() {

        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
//            print();
            print2();

        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            mHandler.sendEmptyMessage(0);
        }

    }

    public void print() {

        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket.getOutputStream();

                    String BILL;
                    BILL = "Patient Name: " + getDataBinding().editTextTextFullName.getText().toString() + "\n"
                            + "Age:  " + getDataBinding().editTextTextAge.getText().toString() +
                            " \t" + "Gender:  " + getDataBinding().editTextGender.getText().toString() +
                            "\n--------------------------------\n" + "Patient Diagnosis \n" + getDataBinding().editTextTextPatientDiagnosis.getText().toString()
                            + "\n" +
                            "Patient Description \n" + getDataBinding().editTextTextPatientDescription.getText().toString() + "\n--------------------------------\n";


                    BILL = BILL + "\nIssued Medicine\n";

                    BILL = BILL + "--------------------------------\n" +
                            "Med Name            Freq" +
                            "    " +
                            "Days\n";
                    BILL = BILL + "--------------------------------\n";

                    for (int i = 0; i < patientPrescribedMedicinesList.size(); i++) {
                        String medNmae = patientPrescribedMedicinesList.get(i).getMedicineName();

                        if (medNmae.length() > 15) {
                            medNmae = medNmae.substring(0, 15) + "...";
                        }

                        int spaces = 20 - medNmae.length();
                        StringBuilder sp = new StringBuilder();
                        for (int j = 0; j < spaces; j++) {
                            sp.append(" ");
                        }

                        BILL = BILL + "" + medNmae + sp +
                                "" + patientPrescribedMedicinesList.get(i).getFrequency() + "    " + patientPrescribedMedicinesList.get(i).getDays() + "\n";
                    }

                    BILL = BILL + "--------------------------------\n";
                    BILL = BILL + "\n\n ";

//                    Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.assignment_dark_24px);
//                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
//                    byte[] image=stream.toByteArray();
//                    os.write(image);


//                    Drawable d = ContextCompat.getDrawable(mContext, R.drawable.add_circle);
//                    Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    PrintPic printPic = PrintPic.getInstance();
//                    printPic.init(bitmap);
//                    byte[] bitmapdata = printPic.printDraw();
//                    os.write(bitmapdata);

                    os.write(BILL.getBytes());

                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 104;
                    os.write(intToByteArray(h));
                    int n = 162;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));
                    closeSocket(mBluetoothSocket);
                    gotoDashBoard();

                } catch (Exception e) {
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        t.start();

    }

    public void print2() {
//        closeSocket(mBluetoothSocket);
        String BILL;
        EscPosPrinter printer;


        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
//            BILL = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.doctors_pana1, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
            BILL = "[L]<b>Print Date: </b>" + formatter.format(date) + "\n" +
                    "[L]\n" +
                    "[C]<u><font size='big'>Patient Receipt</font></u>\n" +
                    "[C]================================\n" +
                    "[L]<b>Patient Name: </b>" + getDataBinding().editTextTextFullName.getText().toString() + "\n" +
                    "[L]<b>Age:</b>" + getDataBinding().editTextTextAge.getText().toString() + "\n" +
                    "[L]<b>Gender:</b>" + getDataBinding().editTextGender.getText().toString() + "\n" +
                    "[C]--------------------------------\n" +
                    "[L]<b>Patient Diagnosis:</b>" +
                    "\n"+
                    "[L]" + getDataBinding().editTextTextPatientDiagnosis.getText().toString() + "\n" +
                    "[L]<b>Patient Description + Prescribed medicine:</b>" +
                    "\n"+
                    "[L]" + getDataBinding().editTextTextPatientDescription.getText().toString() + "\n" +
                    "[C]================================\n" +
                    "[L]<b><font size='normal'>Issued Medicine</font></b>" + "\n" +
                    "[C]================================\n" +
                    "[L]<b>Medicine Name[R]Days[R]Freq </b>" + "\n";

            for (int i = 0; i < patientPrescribedMedicinesList.size(); i++) {
                String medName = patientPrescribedMedicinesList.get(i).getMedicineName();
                int medNameLength = medName.length();
                int remainingLength = medNameLength;
                String medNameToPrint = medName;
                if (medNameLength > 16) {
                    medNameToPrint = medName.substring(0, 16);
                    remainingLength = medNameLength - 16;
                }

                BILL = BILL + "[L]" + medNameToPrint +
                        "[R]" + patientPrescribedMedicinesList.get(i).getDays() + "[R]" + patientPrescribedMedicinesList.get(i).getFrequency() + "\n";

                if (medNameLength > 16) {
                    int length = Integer.parseInt(String.valueOf(medName.length() / 16));
                    int medNameLengthParts = (int) Math.floor(length);
                    for (int j=1; j<=medNameLengthParts; j++) {
                        if (remainingLength > 16) {
                            BILL = BILL + "[L]" + medName.substring(16 * j, (16 * j)+16) + "\n";
                            remainingLength -= 16;
                        }
                        else {
                            BILL = BILL + "[L]" + medName.substring(16 * j, (16 * j)+remainingLength) + "\n";
                            remainingLength -= remainingLength;
                        }
                    }
                }
                BILL = BILL+"[L]\n";
            }
            BILL = BILL + "[C]================================\n";

            printer.printFormattedText(BILL);
            gotoDashBoard();

        } catch (EscPosConnectionException e) {
            e.printStackTrace();
        } catch (EscPosBarcodeException e) {
            e.printStackTrace();
        } catch (EscPosEncodingException e) {
            e.printStackTrace();
        } catch (EscPosParserException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onRecyclerViewItemClick(int position) {
        mDialog.dismiss();
        print2();
//        String mDeviceAddress = bluetoothList.get(position).getDevice_address();
//        Log.e(TAG, "Coming incoming address " + mDeviceAddress);
//        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
//        mBluetoothConnectProgressDialog = ProgressDialog.show(this, "", mBluetoothDevice.getName() + " : " + mBluetoothDevice.getAddress(), true, false);
//        Thread mBlutoothConnectThread = new Thread(this::run);
//        mBlutoothConnectThread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        closeSocketConnection();
        gotoDashBoard();
        finish();
    }

    private void closeSocketConnection() {

        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);
        if (mRequestCode == REQUEST_ENABLE_BT) {
            if (mResultCode == Activity.RESULT_OK) {
                listPairedDevices();
            }
        }
    }


}