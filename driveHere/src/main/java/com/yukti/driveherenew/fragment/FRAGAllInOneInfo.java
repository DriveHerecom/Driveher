package com.yukti.driveherenew.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;

import com.yukti.utils.AppSingleTon;
import com.yukti.utils.CamOperation;
import com.yukti.utils.CamOperation.CameraResponse;
import com.yukti.utils.Common;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.SDCardManager;

public class FRAGAllInOneInfo extends Fragment {

    View view_main;

    EditText edt_stockno, edt_problem, edt_note, edt_company, edt_purchaseform,
            edt_inspectiondate, edt_registrationdate, edt_insurancedate,
            edt_gastank, edt_hastitle, edt_locationtitle, edt_mechanic,
            edt_salesprice;

    CallbackAdd callbackAdd;
    Button buttonInsurancePhoto;
    private DisplayImageOptions options;
    ImageView img_companyinsurance;
    private static final int ACTION_TAKE_PHOTO = 1111;

    private static final int ALL_IN_ONE_REQUEST_CAMERA = 214;

    //    private static String mCurrentPhotoPath;
    String TAG_PROBLEM = "TAG_PROBLEM";
    int click;
    File currentImageFile;

    CameraResponse cameraResponse;

    int screenWidth = 0, screenHeight = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbackAdd = (CallbackAdd) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CallbackAdd");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view_main = (inflater.inflate(R.layout.fragment_all_inone, container,
                false));
        init();
        initProblem();
        //inithasTitle();
        initregistrationdate();
        initinsurancedate();
        initinspectiondate();
        initGasTank();
        initLocationtitle();
        initrotateimage();
        initnext();

        getDetail();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        return view_main;
    }

    public static FRAGAllInOneInfo newInstance() {
        FRAGAllInOneInfo f = new FRAGAllInOneInfo();
        return f;
    }

    void init() {
        edt_stockno = (EditText) view_main.findViewById(R.id.edt_stock_number);
        edt_problem = (EditText) view_main
                .findViewById(R.id.edt_vehicleproblem);
        edt_note = (EditText) view_main.findViewById(R.id.edt_vehiclenote);
        edt_company = (EditText) view_main.findViewById(R.id.edt_company);
        edt_purchaseform = (EditText) view_main
                .findViewById(R.id.edt_vehiclepurchaseform);
        edt_inspectiondate = (EditText) view_main
                .findViewById(R.id.edt_inspectiondate);
        edt_registrationdate = (EditText) view_main
                .findViewById(R.id.edt_registrationdate);
        edt_insurancedate = (EditText) view_main
                .findViewById(R.id.edt_insurance_date);
        edt_gastank = (EditText) view_main.findViewById(R.id.edt_gasetank);
        edt_hastitle = (EditText) view_main.findViewById(R.id.edt_hastitle);
        edt_locationtitle = (EditText) view_main
                .findViewById(R.id.edt_locationtitle);

        img_companyinsurance = (ImageView) view_main
                .findViewById(R.id.iv_companyinsurance);

        buttonInsurancePhoto = (Button) view_main.findViewById(R.id.btnInsuraancePhoto);
        edt_salesprice = (EditText) view_main
                .findViewById(R.id.edt_vehiclesalesprice);
        edt_mechanic = (EditText) view_main.findViewById(R.id.edt_mechanic);
    }

    void initnext() {

        Button btn_next = (Button) view_main.findViewById(R.id.btn_nextallinfo);

        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            btn_next.setText("Next >> DataOne");
        } else {
            btn_next.setText("Back << All Info");
        }

        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

//                if(AddNewCarActivity.arryFragments.contains(AddNewCarActivity.Fragments.AddTitle)){
//                    AddNewCarActivity.arryFragments.remove(AddNewCarActivity.Fragments.AddTitle);
//                }
                setDetail();

                callbackAdd.onNextSecected(false, null);
            }
        });
    }

    void getInsurance() {
        if (AddNewCarActivity.addCarModelObject.companyInsuranceFile != null) {
            Log.e("AddNewCarActivity.addCarModelObject.companyInsuranceFile", " is null");
            OpenDialog();
        } else {
            startCameraIntent(true);
        }
    }

    void initrotateimage() {
        buttonInsurancePhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppSingleTon.VERSION_OS.checkVersion()) {
                    // Marshmallow+
                    if ((getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                            (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                        getInsurance();

                    } else {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            Builder builder = new Builder(getActivity());

                            builder.setTitle("");
                            builder.setMessage("Camera & Storage Permissions Are Needed To Take Photos,Allow It?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final Intent i = new Intent();
                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    i.addCategory(Intent.CATEGORY_DEFAULT);
                                    i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(i);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            ALL_IN_ONE_REQUEST_CAMERA);
                                }
                            });
                            builder.show();
                        }

                    }

                } else {
                    // Pre-Marshmallow
                    getInsurance();

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ALL_IN_ONE_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getInsurance();
            } else {
                Toast.makeText(getActivity(), "Both Camera & Storage Permission Needed", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void OpenDialog() {

        Builder builder = new Builder(getActivity());
        builder.setTitle("Photo");
        builder.setPositiveButton("Take New ",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startCameraIntent(true);
                    }
                });
        builder.setNegativeButton("Rotate ",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showRotateImageDialog(AddNewCarActivity.addCarModelObject.getStrCompanyInsurance());
                    }
                });
        Dialog d = builder.create();
        d.show();
    }

    void initProblem() {

        final String title = "Choose Problem";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.Problem);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_problem.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_problem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getChildFragmentManager(), TAG_PROBLEM);
            }
        });
    }

    void initLocationtitle() {

        // edt_location.setText("No");
        final String title = "Choose Location Title";
        final String TAG_TITLE = "Location Title";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.location_title);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_locationtitle.setText(driveTypeList[position]);

            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_locationtitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getActivity().getSupportFragmentManager(),
                        TAG_TITLE);
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    public class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            DecimalFormat formatter = new DecimalFormat("00");
            String date = String.valueOf(year) + "-"
                    + String.valueOf(formatter.format(month + 1)) + "-"
                    + String.valueOf(formatter.format(day));
            if (click == 1) {
                edt_inspectiondate.setText(date);
            } else if (click == 2) {
                edt_registrationdate.setText(date);
            } else {
                // String.valueOf(year)
                //
                // String.valueOf(formatter.format(month + 1))
                //
                // String.valueOf(formatter.format(day))
                edt_insurancedate.setText(date);

            }
        }

    }

    void initinspectiondate() {
        edt_inspectiondate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                click = 1;
                showDatePickerDialog(v);
            }
        });
    }

    void initregistrationdate() {
        edt_registrationdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                click = 2;
                showDatePickerDialog(v);
            }
        });
    }

    void initinsurancedate() {
        edt_insurancedate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                click = 3;
                showDatePickerDialog(v);
            }
        });
    }

    void initGasTank() {

        final String title = "Select GasTank";
        final String TAG_GAS_TANK = "GAS TANK";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.GasTankList);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_gastank.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_gastank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getActivity().getSupportFragmentManager(),
                        TAG_GAS_TANK);
            }
        });
    }

    public void startCameraIntent(final boolean isFromCompanyInsurance) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(true);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO);
            } else {
                Toast.makeText(getActivity(), "Opps! Storage is not available.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private File createImageFile(boolean isCurrent) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";

        File storageDir = null;

//        storageDir = cacheManager.getAppCacheDir();

        if (storageDir == null)
            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = null;
        if (storageDir.exists()) {
            try {
                image = new File(storageDir, imageFileName);
                // Save a file: path for use with ACTION_VIEW intents

                if (isCurrent) {
                    //mCurrentPhotoPath = image.getAbsolutePath();
                    AddNewCarActivity.addCarModelObject.setStrCompanyInsurance(image.getAbsolutePath());
                }
            } catch (Exception e) {

            }
        }

        //Log.e("Create file", image.getAbsolutePath());

        return image;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == ACTION_TAKE_PHOTO) {
                handlePhoto(AddNewCarActivity.addCarModelObject.getStrCompanyInsurance());
            }
        }
        /*if ((requestCode == CamOperation.ACTION_TAKE_PHOTO)) {

			if (resultCode == Activity.RESULT_OK) {
				cameraResponse.onSuccess();
			} else if (resultCode == Activity.RESULT_CANCELED) {

			}
		}*/
    }

    private void handlePhoto(String path) {
//        Log.e("Inside Handlephoto", path);
        showRotateImageDialog(path);
    }

    public void showRotateImageDialog(final String path) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Choose Action");
        LayoutInflater inflater = getActivity().getLayoutInflater().from(getActivity());
        View view = inflater.inflate(R.layout.activity_custom_dialog, null);

        dialog.setContentView(view);
        // dialog.setCancelable(false);
        final ImageView dialogImage = (ImageView) view.findViewById(R.id.selectedImage);

        Log.e("hilength at showRotateImageDialog side", "Height: " + AddNewCarActivity.imageViewHeight + " Width: "
                + AddNewCarActivity.imageViewWidth + ":  path " + path);

        Button RotateButton = (Button) view.findViewById(R.id.RotateButton);
        Button DoneButton = (Button) view.findViewById(R.id.DoneButton);
//        if (path.contains("http:")) {
//            setPicUniversalImageLoader(path, dialogImage, false);
//        } else
        if (!setPicUniversalImageLoader(path, dialogImage, false)) {
            dialog.cancel();
        }

        DoneButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bitmap rotatedBitmap = ((BitmapDrawable) dialogImage.getDrawable()).getBitmap();
               /* if (path.contains("http:") && saveImage(rotatedBitmap, AddNewCarActivity.addCarModelObject.getStrCompanyInsurance())) {
                    File file;
                    file = Common.bitmapToFilePath(resizedBitmap);
                    if (file != null) {
                        AddNewCarActivity.addCarModelObject.companyInsuranceFile = file;
                        addImageView();
                    }

                    setPicUniversalImageLoader(AddNewCarActivity.addCarModelObject.getStrCompanyInsurance(), null, true);
                } else if (saveImage(rotatedBitmap, AddNewCarActivity.addCarModelObject.getStrCompanyInsurance())) {
                    setPicUniversalImageLoader(AddNewCarActivity.addCarModelObject.getStrCompanyInsurance(), null, true);
                } else {
                    Toast.makeText(getActivity().getBaseContext(),
                            "Image has not been saved, please try again!",
                            Toast.LENGTH_LONG).show();
                }*/
                if (resizedBitmap != null) {

//                    ImageView img = (ImageView) ll_container.findViewWithTag(pos);
                    img_companyinsurance.setImageBitmap(resizedBitmap);
                    File file;
                    file = Common.bitmapToFilePath(resizedBitmap);
                    if (file != null) {
                        if (AddNewCarActivity.isedit) {
                            if (AddNewCarActivity.addCarModelObject.companyInsuranceFile != null && !AddNewCarActivity.addCarModelObject.companyInsuranceFile.equals(path) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_companyInsurancePhoto)) {
                                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_companyInsurancePhoto);
                            }
                        }
                        AddNewCarActivity.addCarModelObject.companyInsuranceFile = file;
//                        AddNewCarActivity.addCarModelObject.setStrCompanyInsurance(String.valueOf(file));
//                        EditCarActivity.photoList.set(pos, file);
                        Log.e("rotate image", file.getPath());
                    }
                }
                rotatedBitmap = null;
                //mCurrentPhotoPath = null;
                dialog.cancel();
            }
        });

        RotateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap dialog_imageBitmap = ((BitmapDrawable) dialogImage.getDrawable()).getBitmap();
                int bitmapWidth = dialog_imageBitmap.getWidth();
                int bitmapHeight = dialog_imageBitmap.getHeight();
                Matrix matrix = new Matrix();
                matrix.preRotate(90);
                resizedBitmap = Bitmap.createBitmap(dialog_imageBitmap, 0,
                        0, bitmapWidth, bitmapHeight, matrix, true);
                BitmapDrawable b = new BitmapDrawable(dialog_imageBitmap);
                dialogImage.setImageBitmap(resizedBitmap);
//                dialog_imageBitmap = null;
//                drawMatrix(dialog_imageBitmap);
            }
        });
        dialog.show();
    }

    public boolean setPicUniversalImageLoader(String path, ImageView imageView, boolean needAddNewView) {
        setDisplayOption();
        if (imageView == null) {
            imageView = img_companyinsurance;
        }
        if (imageView != null) {
            if (path != null && path.length() > 0) {
                Log.e("setPicUnivermageLoader", " : " + path);
                if (!path.contains("http:")) {
                    if (!path.contains("file://")) {
                        path = "file://" + path;
                    }
                }
                try {
//                    if (AddNewCarActivity.isedit && path.contains("file://http:"))
//                        AddNewCarActivity.addCarModelObject.setStrCompanyInsurance(path);
//                    if (path.contains("file://http:")) {
//                        String path2 = path.replace("file://", "");
//                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(path2, imageView, options);
//                    } else {
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(path, imageView, options);
//                    }
//                    if (!AddNewCarActivity.arrayImagePath.contains(path)) {
//                        AddNewCarActivity.arrayImagePath.add(path);
//                    }
//
                    String final_path;
                    if (path.contains("file:")) {
                        final_path = path.replace("file:", "");
                    } else {
                        final_path = path;
                    }
                    Log.e("Final path", final_path);

                    AddNewCarActivity.addCarModelObject.companyInsuranceFile = new File(final_path);
                    if (needAddNewView)
                        addImageView();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getBaseContext(), "Please select proper image file.", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(getActivity().getBaseContext(), "Please select proper image file.", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public void setDisplayOption() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_default_car)
                    .showImageForEmptyUri(R.drawable.ic_default_car)
                    .showImageOnFail(R.drawable.ic_default_car)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
    }

    public void addImageView() {

        if (img_companyinsurance != null) {
            img_companyinsurance.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    OpenDialog();
                    // if
                    // (AddNewCarActivity.arrayListGpsSerial.contains(gps_temp))
                    // {
                    // AddNewCarActivity.arrayListGpsSerial.remove(gps_temp);
                    // }
                }
            });
        }
    }

    public static boolean saveImage(Bitmap bitmap, String pathToSave) {
        boolean result = false;
        String prefix = "";
//        pathToSave = mCurrentPhotoPath;
//        if (!AddNewCarActivity.isedit) {
        prefix = "file://";
        if (pathToSave.contains(prefix))
            pathToSave = pathToSave.substring(prefix.length());
//        }
        File file = new File(pathToSave);
        Log.e("saveImage", "pathToSave : " + pathToSave);
        //if (file.exists())
        // file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    Bitmap resizedBitmap = null;
    int bitmapWidth;
    int bitmapHeight;
    ImageView dialogImage;


    private void drawMatrix(Bitmap dialog_imageBitmap) {
        Matrix matrix = new Matrix();
        matrix.preRotate(90);
        resizedBitmap = Bitmap.createBitmap(dialog_imageBitmap, 0, 0,
                bitmapWidth, bitmapHeight, matrix, true);
        BitmapDrawable b = new BitmapDrawable(resizedBitmap);
        dialogImage.setImageBitmap(resizedBitmap);
        // dialogImage.setRotation(angle);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(AddNewCarActivity.arrayTitleImagePath.size() > 0)
        {
            edt_hastitle.setText("Yes");
        }*/
        initonResume();

    }

    void initonResume() {
        //inithasTitle();
        getDetail();

    }

    void getDetail() {
        if (AddNewCarActivity.addCarModelObject.getStrStockNumber() != null
                && AddNewCarActivity.addCarModelObject.getStrStockNumber().length() != 0) {
            edt_stockno.setText(AddNewCarActivity.addCarModelObject.getStrStockNumber());
        }
        if (AddNewCarActivity.addCarModelObject.getStrVehicleProblem() != null
                && AddNewCarActivity.addCarModelObject.getStrVehicleProblem().length() != 0) {
            edt_problem.setText(AddNewCarActivity.addCarModelObject.getStrVehicleProblem());
        }
        if (AddNewCarActivity.addCarModelObject.getStrVehicleNote() != null
                && AddNewCarActivity.addCarModelObject.getStrVehicleNote().length() != 0) {
            edt_note.setText(AddNewCarActivity.addCarModelObject.getStrVehicleNote());
        }
        if (AddNewCarActivity.addCarModelObject.getStrCompany() != null
                && AddNewCarActivity.addCarModelObject.getStrCompany().length() != 0) {
            edt_company.setText(AddNewCarActivity.addCarModelObject.getStrCompany());
        }
        if (AddNewCarActivity.addCarModelObject.getStrPurchaseForm() != null
                && AddNewCarActivity.addCarModelObject.getStrPurchaseForm().length() != 0) {
            edt_purchaseform.setText(AddNewCarActivity.addCarModelObject.getStrPurchaseForm());
        }
        if (AddNewCarActivity.addCarModelObject.getStrInspectionDate() != null
                && AddNewCarActivity.addCarModelObject.getStrInspectionDate().length() != 0) {
            edt_inspectiondate.setText(AddNewCarActivity.addCarModelObject.getStrInspectionDate());
        }
        if (AddNewCarActivity.addCarModelObject.getStrRegistrationDate() != null
                && AddNewCarActivity.addCarModelObject.getStrRegistrationDate().length() != 0) {
            edt_registrationdate.setText(AddNewCarActivity.addCarModelObject.getStrRegistrationDate());
        }
        if (AddNewCarActivity.addCarModelObject.getStrInsuranceDate() != null
                && AddNewCarActivity.addCarModelObject.getStrInsuranceDate().length() != 0) {
            edt_insurancedate.setText(AddNewCarActivity.addCarModelObject.getStrInsuranceDate());
        }
        if (AddNewCarActivity.addCarModelObject.getStrGasTank() != null
                && AddNewCarActivity.addCarModelObject.getStrGasTank().length() != 0) {
            edt_gastank.setText(AddNewCarActivity.addCarModelObject.getStrGasTank());
        }

        if (AddNewCarActivity.addCarModelObject.getStrLocationTitle() != null
                && AddNewCarActivity.addCarModelObject.getStrLocationTitle().length() != 0) {
            edt_locationtitle.setText(AddNewCarActivity.addCarModelObject.getStrLocationTitle());
        }
        if (AddNewCarActivity.addCarModelObject.getStrMechanic() != null
                && AddNewCarActivity.addCarModelObject.getStrMechanic().length() != 0) {
            edt_mechanic.setText(AddNewCarActivity.addCarModelObject.getStrMechanic());
        }
        if (AddNewCarActivity.addCarModelObject.getStrSalesPrice() != null
                && AddNewCarActivity.addCarModelObject.getStrSalesPrice().length() != 0) {
            edt_salesprice.setText(AddNewCarActivity.addCarModelObject.getStrSalesPrice());
        }
        if (AddNewCarActivity.addCarModelObject.companyInsuranceFile != null
                && AddNewCarActivity.addCarModelObject.companyInsuranceFile.length() != 0) {
            img_companyinsurance.setVisibility(View.VISIBLE);
            buttonInsurancePhoto.setVisibility(View.GONE);
            img_companyinsurance.setImageBitmap(Common
                    .filepathTobitmap(AddNewCarActivity.addCarModelObject.companyInsuranceFile.getAbsoluteFile()
                            .toString()));
            addImageView();
        } else if (AddNewCarActivity.addCarModelObject.getStrCompanyInsurance().length() > 2 && AddNewCarActivity.isedit) {
            img_companyinsurance.setVisibility(View.VISIBLE);
            buttonInsurancePhoto.setVisibility(View.GONE);
            loadImageLoader(AddNewCarActivity.addCarModelObject.getStrCompanyInsurance(), img_companyinsurance);
            addImageView();
        }
    }

    public void loadImageLoader(String path, ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_car)
                .showImageForEmptyUri(R.drawable.ic_default_car)
                .showImageOnFail(R.drawable.ic_default_car)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader.getInstance().displayImage(path, imageView, options);
    }

    void setDetail() {
        if (AddNewCarActivity.isedit) {
            if (!AddNewCarActivity.addCarModelObject.getStrStockNumber().equalsIgnoreCase(edt_stockno.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_stockNumber)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_stockNumber);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrVehicleProblem().equalsIgnoreCase(edt_problem.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_problem)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_problem);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrVehicleNote().equalsIgnoreCase(edt_note.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_note)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_note);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrCompany().equalsIgnoreCase(edt_company.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_company)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_company);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrPurchaseForm().equalsIgnoreCase(edt_purchaseform.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_purchaseFrom)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_purchaseFrom);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrInspectionDate().equalsIgnoreCase(edt_inspectiondate.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_inspectionDate)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_inspectionDate);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrRegistrationDate().equalsIgnoreCase(edt_registrationdate.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_registrationDate)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_registrationDate);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrInsuranceDate().equalsIgnoreCase(edt_insurancedate.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_insuranceDate)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_insuranceDate);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrGasTank().equalsIgnoreCase(edt_gastank.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_gasTank)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_gasTank);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrLocationTitle().equalsIgnoreCase(edt_locationtitle.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_titleLocation)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_titleLocation);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrMechanic().equalsIgnoreCase(edt_mechanic.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_mechanic)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_mechanic);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrSalesPrice().equalsIgnoreCase(edt_salesprice.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_salesPrice)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_salesPrice);
            }
        }
        AddNewCarActivity.addCarModelObject.setStrStockNumber(edt_stockno.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrVehicleProblem(edt_problem.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrVehicleNote(edt_note.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrCompany(edt_company.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrPurchaseForm(edt_purchaseform.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrInspectionDate(edt_inspectiondate.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrRegistrationDate(edt_registrationdate.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrInsuranceDate(edt_insurancedate.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrGasTank(edt_gastank.getText().toString());

                /*AddNewCarActivity.addCarModelObject.setStrHasTitle(edt_hastitle.getText()
                        .toString());*/
        AddNewCarActivity.addCarModelObject.setStrLocationTitle(edt_locationtitle
                .getText().toString());
        AddNewCarActivity.addCarModelObject.setStrMechanic(edt_mechanic.getText()
                .toString());
        AddNewCarActivity.addCarModelObject.setStrSalesPrice(edt_salesprice.getText()
                .toString());
    }
    /*public void changeFragmentarray(String hasTitle){
        if(hasTitle.equalsIgnoreCase("Yes")){

            if(!AddNewCarActivity.arryFragments.contains(AddNewCarActivity.Fragments.AddTitle)){
                AddNewCarActivity.arryFragments.add(10, AddNewCarActivity.Fragments.AddTitle);
            }

            Log.e("getset", AddNewCarActivity.addCarModelObject.getStrHasTitle());

        }else if(hasTitle.equalsIgnoreCase("No")){
            AddNewCarActivity.arrayTitleImagePath.clear();
            if(AddNewCarActivity.arryFragments.contains(AddNewCarActivity.Fragments.AddTitle)){
                AddNewCarActivity.arryFragments.remove(AddNewCarActivity.Fragments.AddTitle);
            }
        }
        initnext();
    }*/
}
