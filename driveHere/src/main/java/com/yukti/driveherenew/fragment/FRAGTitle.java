package com.yukti.driveherenew.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.AddNewCarDetailsActivity;
import com.yukti.driveherenew.ColorChoiceDialogFragment;
import com.yukti.driveherenew.LotcodeChoiceDialogFragment;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.photodir.AlbumStorageDirFactory;
import com.yukti.photodir.BaseAlbumDirFactory;
import com.yukti.photodir.FroyoAlbumDirFactory;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.ParamsKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by prashant on 21/1/16.
 */
public class FRAGTitle extends Fragment {
    CardView cv_TitleImages, cv_lotcode;
    RadioButton rbYes, rbNo;
    EditText edt_lotcode, edt_hastitle;
    ImageView imgView;
    CallbackAdd callbackAdd;
    private View mView;
    Button btn_next, photoAdd;
    private RadioGroup radioGroup;
    private DisplayImageOptions options;
    private static final int ACTION_TAKE_PHOTO = 1111; // License photo
    private static final int ACTION_SELECT_PHOTO = 1112;

    private static final int TITLE_REQUEST_CAMERA = 215;

    private LinearLayout linear_container;
    int screenWidth = 0, screenHeight = 0;
    private String pathSelectedFile, checkNoxValue = "";
    private String mCurrentPhotoPath;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    public static FRAGTitle newInstance() {
        FRAGTitle f = new FRAGTitle();
        return f;
    }

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_title, container, false);

        cv_lotcode = (CardView) mView.findViewById(R.id.card_lotcode);
        cv_TitleImages = (CardView) mView.findViewById(R.id.card_titleImages);

        edt_lotcode = (EditText) mView.findViewById(R.id.edt_lotcode);
        edt_hastitle = (EditText) mView.findViewById(R.id.edt_hastitle);

        linear_container = (LinearLayout) mView.findViewById(R.id.image_container);
        photoAdd = (Button) mView.findViewById(R.id.photoAdd);
        radioGroup = (RadioGroup) mView.findViewById(R.id.rg_title);
        rbYes = (RadioButton) mView.findViewById(R.id.rb_yes);
        rbNo = (RadioButton) mView.findViewById(R.id.rb_no);


        Log.e("ONCREATEVIEW", " " + AddNewCarActivity.addCarModelObject.getStrHasTitle().toString());
        Log.e("ONCREATEVIEW", " " + AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size());

        if (AddNewCarActivity.addCarModelObject.getStrHasTitle().toString().equalsIgnoreCase("Yes")) {
            rbYes.setChecked(true);
//            checkBoxYes.setChecked(true);
            cv_TitleImages.setVisibility(View.VISIBLE);
            cv_lotcode.setVisibility(View.VISIBLE);
        } else {
            rbNo.setChecked(true);
//            checkBoxNo.setChecked(true);
            cv_TitleImages.setVisibility(View.GONE);
            cv_lotcode.setVisibility(View.GONE);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_yes) {
                    cv_TitleImages.setVisibility(View.VISIBLE);
                    cv_lotcode.setVisibility(View.VISIBLE);
                    //edt_hastitle.setText(rbYes.getText().toString());

                } else if (checkedId == R.id.rb_no) {
                    cv_TitleImages.setVisibility(View.GONE);
                    cv_lotcode.setVisibility(View.GONE);
                    //AddNewCarActivity.addCarModelObject.setStrHasTitle(rbNo.getText().toString());
                }
            }

        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        initNextButton();
        initHasTitle();
        initLotCode();

        return mView;
    }

    void initHasTitle() {

        addImageView();

        if (AddNewCarActivity.addCarModelObject.arrayTitleImagePath != null && AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size() > 0) {
            for (int i = 0; i < AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size(); i++) {
                setPicUniversalImageLoader(AddNewCarActivity.addCarModelObject.arrayTitleImagePath.get(i), null, true, false);
            }
        }
        if (AddNewCarActivity.addCarModelObject.getStrTitleLot() != null &&
                AddNewCarActivity.addCarModelObject.getStrTitleLot().length() > 0) {
            edt_lotcode.setText(AddNewCarActivity.addCarModelObject.getStrTitleLot().toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("ONRESUME", " " + AddNewCarActivity.addCarModelObject.getStrHasTitle().toString());
        Log.e("ONCREATEVIEW", " " + AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size());
    }

    public void initNextButton() {
        btn_next = (Button) mView.findViewById(R.id.btn_ok);

        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            btn_next.setText("Next >> Remaining Info");
        } else {
            btn_next.setText("Back << All Info");
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_yes) {
                    if (AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size() < 1 ||
                            AddNewCarActivity.addCarModelObject.getStrTitleLot().toString() == null ||
                            AddNewCarActivity.addCarModelObject.getStrTitleLot().toString().length() == 0) {
                        Toast.makeText(getActivity(), "" + "Title image or lotcode not available", Toast.LENGTH_SHORT).show();
                    } else {
                        if (AddNewCarActivity.isedit && !AddNewCarActivity.addCarModelObject.getStrHasTitle().equalsIgnoreCase(rbYes.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_hasTitle)) {
                            AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_hasTitle);
                        }

                        AddNewCarActivity.addCarModelObject.setStrHasTitle(rbYes.getText().toString());
                        Log.e("ON NEXT BUTTON YES", " " + AddNewCarActivity.addCarModelObject.getStrHasTitle());
                        callbackAdd.onNextSecected(false, null);
                    }

                } else {
                    if (AddNewCarActivity.isedit && !AddNewCarActivity.addCarModelObject.getStrHasTitle().equalsIgnoreCase(rbNo.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_hasTitle)) {
                        AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_hasTitle);
                    }
                    AddNewCarActivity.addCarModelObject.setStrHasTitle(rbNo.getText().toString());
                    Log.e("ON NEXT BUTTON NO", " " + AddNewCarActivity.addCarModelObject.getStrHasTitle());
                    callbackAdd.onNextSecected(false, null);
                }


            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();

        if (radioGroup.getCheckedRadioButtonId() == R.id.rb_yes) {
            AddNewCarActivity.addCarModelObject.setStrHasTitle(rbYes.getText().toString());
//            Log.e("ON NEXT BUTTON YES"," "+AddNewCarActivity.addCarModelObject.getStrHasTitle());
        } else {
            AddNewCarActivity.addCarModelObject.setStrHasTitle(rbNo.getText().toString());
            //          Log.e("ON NEXT BUTTON NO"," "+AddNewCarActivity.addCarModelObject.getStrHasTitle());
        }

        Log.e("ONPAUSE", " " + AddNewCarActivity.addCarModelObject.getStrHasTitle().toString());
    }

    void getImg() {
        if (imgView != null) {
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenDialog(v);
                }
            });
        }
        photoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        View item = getActivity().getLayoutInflater().inflate(R.layout.item_add_photo, linear_container, false);

        imgView = (ImageView) item.findViewById(R.id.img);
        imgView.setVisibility(View.GONE);
        imgView.setTag(linear_container.getChildCount());

        if (AddNewCarActivity.imageViewWidth == 0) {
            ViewTreeObserver vto = imgView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    imgView.getViewTreeObserver().removeOnPreDrawListener(this);
                    AddNewCarActivity.imageViewHeight = imgView.getMeasuredHeight();
                    AddNewCarActivity.imageViewWidth = imgView.getMeasuredWidth();
                    Log.e("hilength", "imageViewHeight: " + AddNewCarActivity.imageViewHeight +
                            " imageViewWidth: " + AddNewCarActivity.imageViewWidth);
                    return true;
                }
            });
        }

//        imgView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });

        linear_container.addView(item);
    }

    public void addImageView() {

        if (AppSingleTon.VERSION_OS.checkVersion()) {
            // Marshmallow+
            if ((getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                    (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                getImg();

            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("");
                    builder.setMessage("Camera & Storage Permissiona Are Needed To Take Photos,Allow It?");
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
                                    TITLE_REQUEST_CAMERA);
                        }
                    });
                    builder.show();
                }


            }

        } else {
            // Pre-Marshmallow
            getImg();

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == TITLE_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getImg();
            } else {
                Toast.makeText(getActivity(), "Both Camera & Storage Permissions Are Needed", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void OpenDialog(final View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Want To Remove?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = v.getTag().toString();
                ((LinearLayout) v.getParent()).removeView(v);

                Log.e("tag", "index tag :: " + path);
                if (AddNewCarActivity.addCarModelObject.arrayTitleImagePath.contains(path)) {
                    AddNewCarActivity.addCarModelObject.arrayTitleImagePath.remove(path);
                }
            }
        });

        builder.setNegativeButton("Cancle",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog d = builder.create();
        d.show();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        // add items to the action Bar
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    // Open Camera using intent
                    dispatchTakePictureIntent();

                } else if (items[item].equals("Choose from Library")) {
                    dispatchSelectPictureIntent();
                    // } else if (items[item].equals("View full image")) {
                    // startZoomImageActivity();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void dispatchTakePictureIntent() {
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

    public void setDisplayOption() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_launcher)
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
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

                if (isCurrent)
                    mCurrentPhotoPath = image.getAbsolutePath();
            } catch (Exception e) {
                //Toast.makeText(getActivity(),"File Can not be created",Toast.LENGTH_SHORT).show();
            }
        }

        // Log.e("Create file", image.getAbsolutePath());

        return image;


    }


    private void dispatchSelectPictureIntent() {

        File photoFile = null;
        try {
            photoFile = createImageFile(true);
        } catch (IOException ex) {
            // Error occurred while creating the File
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), ACTION_SELECT_PHOTO);
        } else {
            Toast.makeText(getActivity(), "Opps! Storage is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case ACTION_TAKE_PHOTO:
                    handlePhoto(mCurrentPhotoPath);
                    break;
                case ACTION_SELECT_PHOTO:
                    Uri selectedImageUri = data.getData();
                    String selectedPath;
                    try {
                        String[] projection = {MediaStore.MediaColumns.DATA};
                        CursorLoader cursorLoader = new CursorLoader(getActivity().getBaseContext(), selectedImageUri, projection, null, null,
                                null);
                        Cursor cursor = cursorLoader.loadInBackground();
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        cursor.moveToFirst();
                        selectedPath = cursor.getString(column_index);
                    } catch (Exception e) {
                        e.printStackTrace();
                        selectedPath = "";
                    }

                    if (selectedPath == "")
                        try {
                            selectedPath = selectedImageUri.toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                            selectedPath = "";
                        }

                    Log.e("selectedImageUri", selectedPath.toString());
                    handlePhoto(selectedPath);
                    break;
            }
        }
    }

    public static boolean saveImage(Bitmap bitmap, String pathToSave) {
        boolean result = false;
//        pathToSave = mCurrentPhotoPath;
        String prefix = "file://";

        Log.e("pathToSave", pathToSave + "");
        if (pathToSave.contains(prefix))
            pathToSave = pathToSave.substring(prefix.length());
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

    private void handlePhoto(String path) {
        Log.e("Inside Handlephoto", path);
        showRotateImageDialog(path);
    }

    public void showRotateImageDialog(final String path) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Choose Action");
        LayoutInflater inflater = getActivity().getLayoutInflater().from(getActivity());
        View view = inflater.inflate(R.layout.activity_custom_dialog, null);

        dialog.setContentView(view);
        final ImageView dialogImage = (ImageView) view.findViewById(R.id.selectedImage);

        Log.e("hilength at", "Height: "
                + AddNewCarActivity.imageViewHeight + " Width: "
                + AddNewCarActivity.imageViewWidth + ":  path " + path);

        Button RotateButton = (Button) view.findViewById(R.id.RotateButton);
        Button DoneButton = (Button) view.findViewById(R.id.DoneButton);

        if (!setPicUniversalImageLoader(path, dialogImage, false, false)) {

            dialog.cancel();
        }

        DoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bitmap rotatedBitmap = ((BitmapDrawable) dialogImage.getDrawable()).getBitmap();

                if (saveImage(rotatedBitmap, mCurrentPhotoPath)) {
                    setPicUniversalImageLoader(mCurrentPhotoPath, null, true, true);
                } else {
                    Toast.makeText(getActivity().getBaseContext(),
                            "Image has not been saved, please try again!",
                            Toast.LENGTH_LONG).show();
                }
                rotatedBitmap = null;
                mCurrentPhotoPath = null;
                dialog.cancel();

            }
        });

        RotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap dialog_imageBitmap = ((BitmapDrawable) dialogImage.getDrawable()).getBitmap();
                int bitmapWidth = dialog_imageBitmap.getWidth();
                int bitmapHeight = dialog_imageBitmap.getHeight();
                Matrix matrix = new Matrix();
                matrix.preRotate(90);
                dialog_imageBitmap = Bitmap.createBitmap(dialog_imageBitmap, 0,
                        0, bitmapWidth, bitmapHeight, matrix, true);
                BitmapDrawable b = new BitmapDrawable(dialog_imageBitmap);
                dialogImage.setImageBitmap(dialog_imageBitmap);
                dialog_imageBitmap = null;
            }
        });
        dialog.show();
    }

    public boolean setPicUniversalImageLoader(String path, ImageView imageView, boolean needAddNewView, boolean needToAddPath) {
        setDisplayOption();
        if (imageView == null) {
            imageView = imgView;
            imgView.setVisibility(View.VISIBLE);
        }
        if (imageView != null) {
            if (path != null && path.length() > 0) {
                Log.e("setPicUnivermageLoader", " : " + path);
                if (!path.contains("file://")) {
                    path = "file://" + path;
                }
                imageView.setTag(path);
                try {
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(path, imageView, options);
                    if (needToAddPath && !AddNewCarActivity.addCarModelObject.arrayTitleImagePath.contains(path)) {
                        AddNewCarActivity.addCarModelObject.arrayTitleImagePath.add(path);
                        if (AddNewCarActivity.isedit && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_hasTitle)) {
                            AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_hasTitle);
                        }
                    }
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
        Log.e("Array Size After Add", AddNewCarActivity.addCarModelObject.arrayTitleImagePath.size() + "");
        return true;
    }

    void initLotCode() {
        final CharSequence[] lotList = getResources().getStringArray(
                R.array.Lotcode);

        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_lotcode.setText(lotList[position]);
                AddNewCarActivity.addCarModelObject.setStrTitleLot(edt_lotcode.getText().toString());
                if (AddNewCarActivity.isedit && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_hasTitle)) {
                    AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_hasTitle);
                }
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_lotcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String TAG_COLOR = "TAG_COLOR";
                LotcodeChoiceDialogFragment dialog1 = new LotcodeChoiceDialogFragment(listener);
                dialog1.show(getChildFragmentManager(), TAG_COLOR);

            }
        });

    }

}
