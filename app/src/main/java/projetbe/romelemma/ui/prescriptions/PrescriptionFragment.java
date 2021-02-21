package projetbe.romelemma.ui.prescriptions;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import projetbe.romelemma.MainActivity;
import projetbe.romelemma.R;
import projetbe.romelemma.dataClass.User;
import projetbe.romelemma.repository.MyRepository;
import projetbe.romelemma.services.FileService;

import static android.app.Activity.RESULT_OK;

public class PrescriptionFragment  extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMAGE = 2 ;

    Button sendPrescription;
    ImageView prescriptionThumbnail;
    Bitmap prescriptionBtp;
    String imagePath;
    public static PrescriptionFragment newInstance() {
        PrescriptionFragment fragment = new PrescriptionFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prescriptions_fragment, container, false);
        prescriptionThumbnail =view.findViewById(R.id.prescriptionPicture);
        prescriptionThumbnail.setVisibility(View.INVISIBLE);
        Button camera = view.findViewById(R.id.importCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(prescriptionThumbnail.getVisibility() == View.VISIBLE){
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Prescription already import");
                    alertDialog.setMessage("Voulez vous supprimer la prescription existente et en importer une nouvelle ? ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Supprimer et Importer",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dispatchTakePictureIntent();
                                }
                            });
                    alertDialog.show();
                } else if(prescriptionThumbnail.getVisibility() == View.INVISIBLE){
                    prescriptionThumbnail.setVisibility(View.VISIBLE);
                    dispatchTakePictureIntent();
                }

            }
        });

        Button gallery = view.findViewById(R.id.importGallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prescriptionThumbnail.getVisibility() == View.VISIBLE){
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Prescription already import");
                    alertDialog.setMessage("Voulez vous supprimer la prescription existente et en importer une nouvelle ? ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Supprimer et Importer",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    importFromGallery();                               }
                            });
                    alertDialog.show();
                } else if(prescriptionThumbnail.getVisibility() == View.INVISIBLE){
                    importFromGallery();
                    prescriptionThumbnail.setVisibility(View.VISIBLE);
                }
            }
        });


        prescriptionThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Alert delete Prescription");
                alertDialog.setMessage("Voulez vous supprimer cette image ?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Valider",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                prescriptionThumbnail.setImageBitmap(null);
                                prescriptionThumbnail.setVisibility(View.INVISIBLE);
                                sendPrescription.setVisibility(View.INVISIBLE);
                            }
                        });
                alertDialog.show();
            }
        });



        sendPrescription = view.findViewById(R.id.sendPrescription);
        sendPrescription.setVisibility(View.INVISIBLE);
        sendPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileService fileService = new FileService();
                User user = fileService.getData(getContext());
                MyRepository repo = new MyRepository();
                repo.createPrescription(user.getId(), "1", imagePath, getContext());
            }
        });


        return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.mydomain.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

            //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "1mind_" + timeStamp + ".jpg";
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),  imageFileName);
        //File photo = new File("/storage/emulated/0/DCIM/Camera/",imageFileName);
        imagePath = photo.getAbsolutePath();

        return photo;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            prescriptionBtp = (Bitmap) extras.get("data");
            if(prescriptionBtp !=null) {
                prescriptionThumbnail.setImageBitmap(prescriptionBtp);
                sendPrescription.setVisibility(View.VISIBLE);
            }

        }
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Uri imageUri = data.getData();
                imagePath = getPath(getContext(), imageUri);
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                prescriptionBtp = BitmapFactory.decodeStream(imageStream);
                if(prescriptionBtp != null){
                    prescriptionThumbnail.setImageBitmap(prescriptionBtp);
                    sendPrescription.setVisibility(View.VISIBLE);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error when importing the photo", Toast.LENGTH_LONG).show();
            }

            /*
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();*/

        }


    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    private void importFromGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


}
