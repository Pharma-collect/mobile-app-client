package projetbe.romelemma.ui.shop;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import projetbe.romelemma.R;

public class CustomListProduct extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> web;
    private final ArrayList<String> price;
    private final ArrayList<String> description;
    private final ArrayList<Bitmap> imageId;
    private final ArrayList<String> quantity;
            ;
    public CustomListProduct(Activity context,
                             ArrayList<String> web, ArrayList<Bitmap> imageId, ArrayList<String> price, ArrayList<String> description,ArrayList<String> quantity) {
        super(context, R.layout.list_product, web);
        this.context = context;
        this.web = web;
        this.price= price;
        this.imageId = imageId;
        this.description = description;
        this.quantity = quantity;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_product, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        TextView txtPrice = (TextView) rowView.findViewById(R.id.price);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.des);
        TextView txtQuantity = (TextView) rowView.findViewById(R.id.quantitytxt);

        txtDescription.setTextSize(15);


        txtTitle.setText(web.get(position));
        imageView.setImageBitmap(imageId.get(position));
        txtPrice.setText(price.get(position));
        txtDescription.setText((description.get(position)));
        if(quantity != null){
            txtQuantity.setText(quantity.get(position));
            txtQuantity.setVisibility(View.VISIBLE);

        }else{
            txtQuantity.setVisibility(View.GONE);
        }
        return rowView;
    }

}