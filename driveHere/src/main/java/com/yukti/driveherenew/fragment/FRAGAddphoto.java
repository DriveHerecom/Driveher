package com.yukti.driveherenew.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
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
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.R;
import com.yukti.photodir.AlbumStorageDirFactory;
import com.yukti.photodir.BaseAlbumDirFactory;
import com.yukti.photodir.FroyoAlbumDirFactory;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.ParamsKey;

public class FRAGAddphoto extends Fragment {

    private View mView;
    CallbackAdd callbackAdd;
    ImageView imgView;

    private String mCurrentPhotoPath;
    private DisplayImageOptions options;

    private static final int ACTION_TAKE_PHOTO = 1111; // License photo
    private static final int ACTION_SELECT_PHOTO = 1112;

    private static final int ADD_PHOTO_REQUEST_CAMERA = 213;

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private String pathSelectedFile;
    private LinearLayout linear_container;

    int screenWidth = 0, screenHeight = 0;

    public static FRAGAddphoto newInstance() {
        FRAGAddphoto f = new FRAGAddphoto();
        return f;
    }

    public interface OnTagSelectedListener {
        public void onTagSelected(int tagPosition, String strTag);

        public void onTagLongPress(int tagPosition, String strTag);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        //setActionBar();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_add_photo, container, false);

        Button btn_next = (Button) mView.findViewById(R.id.btn_nextPhotos);
        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            btn_next.setText("Next >> Add Titles");
        } else {
            btn_next.setText("Back << All Info");
        }
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                callbackAdd.onNextSecected(false, null);

            }
        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        Log.e("hilength at out side", "screenHeight: " + screenHeight
                + " screenWidth: " + screenWidth);

        linear_container = (LinearLayout) mView.findViewById(R.id.llImageContainer);
        Button btnAddPhoto = (Button) mView.findViewById(R.id.btnAddphoto);
        btnAddPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        addImageView();

        if (AddNewCarActivity.addCarModelObject.arrayImagePath != null
                && AddNewCarActivity.addCarModelObject.arrayImagePath.size() > 0) {
            for (int i = 0; i < AddNewCarActivity.addCarModelObject.arrayImagePath.size(); i++) {
                setPicUniversalImageLoader(AddNewCarActivity.addCarModelObject.arrayImagePath.get(i), null, true, false);
            }
        }
        return mView;
    }

    public void OpenDialog(final View v) {

        Builder builder = new Builder(getActivity());
        builder.setTitle("Want To Remove?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path = v.getTag().toString();
                ((LinearLayout) v.getParent()).removeView(v);

                if (AddNewCarActivity.addCarModelObject.arrayImagePath.contains(path)) {
                    AddNewCarActivity.addCarModelObject.arrayImagePath.remove(path);
                    AddNewCarActivity.addCarModelObject.deleteImagePath.add(path);
                    if (AddNewCarActivity.isedit) {
                        if (AddNewCarActivity.addCarModelObject.deleteImagePath.size() > 0 && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_deletePhotoLink)) {
                            AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_deletePhotoLink);
                        }
                    }
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

    void addimg() {
        if (imgView != null) {
            imgView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    OpenDialog(v);
                }
            });
        }

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
                    return true;
                }
            });
        }

        imgView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                selectImage();
            }
        });

        linear_container.addView(item);
    }

    public void addImageView() {

        if (AppSingleTon.VERSION_OS.checkVersion()) {
            // Marshmallow+
            if ((getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                    (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                addimg();

            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Builder builder = new Builder(getActivity());

                    builder.setTitle("");
                    builder.setMessage("Camera & Storage Permissions are Needed To Take Photos,Allow It?");
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
                                    ADD_PHOTO_REQUEST_CAMERA);
                        }
                    });
                    builder.show();
                }
            }
        } else {
            // Pre-Marshmallow
            addimg();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ADD_PHOTO_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                addimg();
            } else {
                Toast.makeText(getActivity(), "Both Camera & Storage Permissions Are Needed", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
                if (!path.contains("http:")) {
                    if (!path.contains("file://")) {
                        path = "file://" + path;
                    }
                }
                imageView.setTag(path);
                try {
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(path, imageView, options);
                    if (needToAddPath && !AddNewCarActivity.addCarModelObject.arrayImagePath.contains(path)) {
                        if (AddNewCarActivity.isedit) {
                            if (!AddNewCarActivity.addCarModelObject.arrayImagePath.contains(path) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_photo)) {
                                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_photo);
                            }
                        }
                        AddNewCarActivity.addCarModelObject.arrayImagePath.add(path);
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
        Log.e("Array Size After Add", AddNewCarActivity.addCarModelObject.arrayImagePath.size() + "");
        return true;
    }

    public void setDisplayOption() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.car_demo)
                    .showImageForEmptyUri(R.drawable.car_demo)
                    .showImageOnFail(R.drawable.car_demo)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .displayer(new RoundedBitmapDisplayer(20))
                    .build();
    }

    private void selectImage() {

        // Log.e("tag", "tag :: "+tag);

        final CharSequence[] items = {"Take Photo", "Choose from Library",/*
                                                                             * "View full image"
																			 * ,
																			 */
                "Cancel"};

        Builder builder = new Builder(getActivity());
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
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
                //image = null;
                if (isCurrent)
                    mCurrentPhotoPath = image.getAbsolutePath();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return image;
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
        showRotateImageDialog(path);
    }

    public void copySelectedFileToCache(File sourceLocation, File targetLocation) throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copySelectedFileToCache(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    public void showRotateImageDialog(final String path) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Choose Action");
        LayoutInflater inflater = getActivity().getLayoutInflater().from(getActivity());
        View view = inflater.inflate(R.layout.activity_custom_dialog, null);

        dialog.setContentView(view);
        final ImageView dialogImage = (ImageView) view.findViewById(R.id.selectedImage);

        Button RotateButton = (Button) view.findViewById(R.id.RotateButton);
        Button DoneButton = (Button) view.findViewById(R.id.DoneButton);
        if (!setPicUniversalImageLoader(path, dialogImage, false, false)) {
            dialog.cancel();
        }

        DoneButton.setOnClickListener(new OnClickListener() {

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

				/*if (saveImage(rotatedBitmap, pathSelectedFile))
                {
					setImage(pathSelectedFile);
				} else {
					Toast.makeText(getActivity().getBaseContext(),
							"Image has not been saved, please try again!",
							Toast.LENGTH_LONG).show();
				}
				rotatedBitmap = null;
				pathSelectedFile = null;
				dialog.cancel();*/
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
                dialog_imageBitmap = Bitmap.createBitmap(dialog_imageBitmap, 0,
                        0, bitmapWidth, bitmapHeight, matrix, true);
                BitmapDrawable b = new BitmapDrawable(dialog_imageBitmap);
                dialogImage.setImageBitmap(dialog_imageBitmap);
                dialog_imageBitmap = null;
            }
        });
        dialog.show();
    }

    public boolean copySelectedImageToDriveHerePath(String sourceLocation) {

        boolean result = false;
        File sourceFile = new File(sourceLocation);
        // make sure your target location folder exists!
        File targetFile = null;
        if (pathSelectedFile != null && pathSelectedFile.length() > 0) {
            try {
                targetFile = new File(pathSelectedFile);
            } catch (Exception e) {
                e.printStackTrace();
                targetFile = null;
            }

        }

        if (sourceFile.exists() && targetFile != null) {
            try {
                InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(targetFile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();

                result = true;
                Log.v("", "Copy file successful.");
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        } else {
            Log.v("", "Copy file failed. Source file missing.");
            result = false;
        }
        return result;
    }

}
