package com.yukti.driveherenew.search;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yukti.dataone.model.TitleHistoryModel;
import com.yukti.driveherenew.BaseActivity;
import com.yukti.driveherenew.LotcodeChoiceDialogFragment;
import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.MessageDialogFragment;
import com.yukti.driveherenew.MyApplication;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.jsonparser.AddTitleResponse;
import com.yukti.photodir.AlbumStorageDirFactory;
import com.yukti.photodir.BaseAlbumDirFactory;
import com.yukti.photodir.FroyoAlbumDirFactory;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by prashant on 16/1/16.
 */
public class TitleActivity extends BaseActivity implements View.OnClickListener {
    ImageView imgView;
    private LinearLayout linear_container;
    public static int imageViewWidth = 0;
    public static int imageViewHeight = 0;
    public static ArrayList<String> arrayImageAllPath;
    int screenWidth = 0, screenHeight = 0;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private static final int ACTION_TAKE_PHOTO = 1111; // License photo
    private static final int ACTION_SELECT_PHOTO = 1112;
    private String mCurrentPhotoPath;
    private DisplayImageOptions options;
    private EditText edt_lotcode;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    public static String strLotcode;
    public static String carid;


    public interface OnTagSelectedListener {
        public void onTagSelected(int tagPosition, String strTag);

        public void onTagLongPress(int tagPosition, String strTag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        initToolbar();

        linear_container = (LinearLayout) findViewById(R.id.image_container);
        edt_lotcode = (EditText) findViewById(R.id.edt_lotcode_title);
        Button mButtonSubmit = (Button) findViewById(R.id.btn_submit);
        mButtonSubmit.setOnClickListener(this);

        initLotCode();
        //strLotcode = edt_lotcode.getText().toString();
        arrayImageAllPath = new ArrayList<String>();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        carid = getIntent().getExtras().getString("carid");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        Log.e("hilength at out side", "screenHeight: " + screenHeight
                + " screenWidth: " + screenWidth);


        addImageView();
        Button addNewPhoto = (Button) findViewById(R.id.addphoto_title);
        addNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

        if (arrayImageAllPath != null
                && arrayImageAllPath.size() > 0) {
            for (int i = 0; i < arrayImageAllPath.size(); i++) {
                setPicUniversalImageLoader(arrayImageAllPath.get(i), null, true, false);
            }
        }

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_title_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("image", mCurrentPhotoPath);

        super.onSaveInstanceState(outState);
        Log.e("ON SAVED INSTANCE STATE", "fsdfsfs");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null)
            mCurrentPhotoPath = savedInstanceState.getString("image");

        Log.e("ON RESTORE INSTANCE STATE", "fsdfsfs");
    }

    public void addImageView() {

        if (imgView != null) {
            imgView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    OpenDialog(v);
                    // if
                    // (AddNewCarActivity.arrayListGpsSerial.contains(gps_temp))
                    // {
                    // AddNewCarActivity.arrayListGpsSerial.remove(gps_temp);
                    // }
                }
            });
        }

        View item = getLayoutInflater().inflate(R.layout.item_add_photo, linear_container, false);
        imgView = (ImageView) item.findViewById(R.id.img);
        imgView.setVisibility(View.GONE);
        imgView.setTag(linear_container.getChildCount());
        if (imageViewWidth == 0) {
            ViewTreeObserver vto = imgView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    imgView.getViewTreeObserver().removeOnPreDrawListener(this);
                    imageViewHeight = imgView.getMeasuredHeight();
                    imageViewWidth = imgView.getMeasuredWidth();
                    Log.e("hilength", "imageViewHeight: " + imageViewHeight +
                            " imageViewWidth: " + imageViewWidth);
                    return true;
                }
            });
        }


        linear_container.addView(item);
    }

    public void OpenDialog(final View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(TitleActivity.this);
        builder.setTitle("Want To Remove?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = v.getTag().toString();
                ((LinearLayout) v.getParent()).removeView(v);

                Log.e("tag", "index tag :: " + path);
                if (arrayImageAllPath.contains(path)) {
                    arrayImageAllPath.remove(path);
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

        // Log.e("tag", "tag :: "+tag);

        final CharSequence[] items = {"Take Photo", "Choose from Library",/*
                                                                             * "View full image"
																			 * ,
																			 */
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(TitleActivity.this);
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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
                Toast.makeText(getApplicationContext(), "Opps! Storage is not available.", Toast.LENGTH_SHORT).show();
            }
        }
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
            Toast.makeText(getApplicationContext(), "Opps! Storage is not available.", Toast.LENGTH_SHORT).show();
        }
    }

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

            }
        }

        // Log.e("Create file", image.getAbsolutePath());

        return image;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_TAKE_PHOTO:
                    handlePhoto(mCurrentPhotoPath);
                    break;
                case ACTION_SELECT_PHOTO:
                    Uri selectedImageUri = data.getData();
                    String selectedPath;
                    try {
                        String[] projection = {MediaStore.MediaColumns.DATA};
                        CursorLoader cursorLoader = new CursorLoader(getBaseContext(), selectedImageUri, projection, null, null,
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

    private void handlePhoto(String path) {
        Log.e("Inside Handlephoto", path);
        showRotateImageDialog(path);

    }

    public void showRotateImageDialog(final String path) {
        final Dialog dialog = new Dialog(TitleActivity.this);
        dialog.setTitle("Choose Action");
        LayoutInflater inflater = getLayoutInflater().from(TitleActivity.this);
        View view = inflater.inflate(R.layout.activity_custom_dialog, null);

        dialog.setContentView(view);
        // dialog.setCancelable(false);
        final ImageView dialogImage = (ImageView) view.findViewById(R.id.selectedImage);

        dialogImage.setImageURI(Uri.parse(path));


        Log.e("hilength at", "Height: "
                + imageViewHeight + " Width: "
                + imageViewWidth + ":  path " + path);

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
                    Log.e("Done click ", " : " + mCurrentPhotoPath);
                    setPicUniversalImageLoader(mCurrentPhotoPath, null, true, true);
                } else {
                    Toast.makeText(getBaseContext(),
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
                // imageView.setImageURI(Uri.parse(path));
                try {
                    ImageLoader.getInstance().displayImage(path, imageView, options);
                    if (needToAddPath && !arrayImageAllPath.contains(path)) {
                        arrayImageAllPath.add(path);
                    }
                    if (needAddNewView) {
                        addImageView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this.getBaseContext(), "Please select proper image file.", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(this.getBaseContext(), "Please select proper image file.", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            return false;
        }
        Log.e("Array Size After Add", arrayImageAllPath.size() + "");
        return true;
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

    void initLotCode() {
        final String title = "Choose Lot Code";
        final CharSequence[] lotList = getResources().getStringArray(R.array.Lotcode);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_lotcode.setText(lotList[position]);
                // lotCode.setTag(colorValueList);
                strLotcode = lotList[position].toString();
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_lotcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String TAG_COLOR = "TAG_COLOR";
                LotcodeChoiceDialogFragment dialog1 = new LotcodeChoiceDialogFragment(
                        listener);
                dialog1.show(getSupportFragmentManager(), TAG_COLOR);

            }
        });

    }

    void sendTitleData() {

        Log.e("SendTitleData....", "SendTitleData");
        final String TAG_PUSH_RESULT = "TAG_PUSH_RESULT";
        final Dialog dialog = new Dialog(TitleActivity.this);
        dialog.setContentView(R.layout.addtitle);
        dialog.setCancelable(false);
        dialog.setTitle("Please Wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String url = AppSingleTon.APP_URL.URL_ADD_TITLEPHOTO;
        MultipartRequestTitle multipartRequestTitle = new MultipartRequestTitle(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final String TAG_PUSH_RESULT = "TAG_PUSH_RESULT";

                try {
                    AddTitleResponse addTitleRes = AppSingleTon.APP_JSON_PARSER.addTitleResponse(response);
                    if (addTitleRes.status_code == 1) {
                        Log.e("Success...", "Title Addedd successfully..");

                        Toast.makeText(getApplicationContext(), "Title Added Successfully..", Toast.LENGTH_SHORT).show();
                        TitleHistoryActivity.IS_FROM_TITLE_PIC = true;
                        finish();
                    } else if (addTitleRes.status_code == 2) {
                        Log.e("Failure", "fail to add Title of car");
                        MessageDialogFragment fragment = new MessageDialogFragment(
                                "Info", addTitleRes.message, true, "Ok", false,
                                "", false, "", null);
                        fragment.show(getSupportFragmentManager(), "");
                    } else if (addTitleRes.status_code == 4) {
                        Toast.makeText(getApplicationContext(), addTitleRes.message, Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(getApplicationContext());
                    } else {
                        MessageDialogFragment fragment = new MessageDialogFragment(
                                "Failed", "Failed to add Title.", true, "Ok",
                                false, "", false, "", null);
                        fragment.show(getSupportFragmentManager(), TAG_PUSH_RESULT);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(TitleActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                sendTitleData();
                            else
                                Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Common.dismissProgressDialog();
                Log.e("Volley Error..", error.toString());
                Log.e("error", "error");
                dialog.dismiss();
            }
        });
        multipartRequestTitle.setRetryPolicy(new DefaultRetryPolicy(
                Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(multipartRequestTitle);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:

                boolean needfocus = false;

                if (arrayImageAllPath.size() > 0) {
                    needfocus = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Please select title image", Toast.LENGTH_SHORT).show();
                }

                if (edt_lotcode.getText().toString().length() > 0) {
                    needfocus = true;
                    edt_lotcode.setError(null);
                } else {
                    edt_lotcode.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please select lotcode", Toast.LENGTH_SHORT).show();
                }
                if (!edt_lotcode.getText().toString().equals("") && arrayImageAllPath != null && arrayImageAllPath.size() > 0) {
                    sendTitleData();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
