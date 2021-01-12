package projetbe.romelemma.ui.prescriptions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import projetbe.romelemma.R;
import projetbe.romelemma.ui.shop.AccueilShopFragment;

import static android.app.Activity.RESULT_OK;

public class PrescriptionFragment  extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMAGE = 2 ;

    ImageView prescriptionThumbnail;
    public static PrescriptionFragment newInstance() {
        PrescriptionFragment fragment = new PrescriptionFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prescriptions_fragment, container, false);

        Button scan = view.findViewById(R.id.importCamera);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });

        Button gallery = view.findViewById(R.id.importGallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                importFromGallery();
            }
        });

        prescriptionThumbnail =view.findViewById(R.id.prescriptionPicture);

        return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            prescriptionThumbnail.setImageBitmap(imageBitmap);
        }
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                prescriptionThumbnail.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error when importing the photo", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void importFromGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
}
