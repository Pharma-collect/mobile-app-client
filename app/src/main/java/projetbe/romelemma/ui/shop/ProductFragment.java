package projetbe.romelemma.ui.shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import projetbe.romelemma.R;


public class ProductFragment extends Fragment {
    String listProduit;
    int positionProduit;
    JSONArray produits;
    JSONObject selectedProduct;
    int _quantity;
    View _view;

    public static  ProductFragment newInstance(String produit, int positionProduit) {
        Bundle args = new Bundle();
        args.putInt("position", positionProduit);
        args.putString("produit", produit);

        ProductFragment fragment = new ProductFragment();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shop_product_details, container, false);
        positionProduit = this.getArguments().getInt("position");
        listProduit = this.getArguments().getString("produit");

        setView(view);

        return view;
    }

    private void setView(View view){
        _view = view;
        changeQuantity();
        try {
            JSONObject provisoire  = new JSONObject(listProduit);
            produits = provisoire.getJSONArray("result");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView title = view.findViewById(R.id.titleProduct);
        ImageView image = view.findViewById(R.id.imageProduct);
        TextView description = view.findViewById(R.id.descriptionProduct);
        TextView price = view.findViewById(R.id.priceProduct);

        try {
            selectedProduct = produits.getJSONObject(positionProduit);
            title.setText(selectedProduct.getString("title"));
            description.setText(selectedProduct.getString("description"));
            Bitmap bt = AccueilShop.getBitmapFromURL(selectedProduct.getString("image_url"));

            String priceS = selectedProduct.getString("price") + "€";
            price.setText("Prix à l'unité :   " + priceS);
            price.setTextSize(20);
            image.setImageBitmap(bt);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final LottieAnimationView check = view.findViewById(R.id.lottie_check);
        check.setVisibility(View.INVISIBLE);

        final TextView checkText = view.findViewById(R.id.checkText);
        checkText.setVisibility(View.INVISIBLE);

        final Button addToCart = view.findViewById(R.id.AddToCart);

        final RelativeLayout commande = view.findViewById(R.id.commande);

        addToCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long time = check.getDuration();
                commande.setVisibility(View.INVISIBLE);
                checkText.setVisibility(View.VISIBLE);
                check.setVisibility(View.VISIBLE);
                check.playAnimation();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        check.setVisibility(View.INVISIBLE);
                        checkText.setVisibility(View.INVISIBLE);
                        commande.setVisibility(View.VISIBLE);



                    }
                }, time);

                Panier _panier = new Panier();
                _panier.addProductToList(selectedProduct, _quantity);
            }
        });

        Button back = view.findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AccueilShop fragment = AccueilShop.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.act_choice, fragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    private void changeQuantity(){
        _quantity = 1;
        Button less = _view.findViewById(R.id.less);
        Button more = _view.findViewById(R.id.more);


        less.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(_quantity > 1){
                    _quantity = _quantity-1;
                    TextView quantityLabel = _view.findViewById(R.id.quantityLabel);
                    quantityLabel.setText(String.valueOf(_quantity));

                    // Calcul prix du lot
                    try {
                        TextView priceProducts = _view.findViewById(R.id.priceProducts);
                        Double priceUn = Double.valueOf(selectedProduct.getString("price"));
                        Double priceLot = priceUn * _quantity;
                        priceProducts.setText("Prix du lot : " + String.valueOf(priceLot) + "€");

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _quantity = _quantity + 1;
                TextView quantityLabel = _view.findViewById(R.id.quantityLabel);
                quantityLabel.setText(String.valueOf(_quantity));
                // Calcul prix du lot
                try {
                    TextView priceProducts = _view.findViewById(R.id.priceProducts);
                    Double priceUn = Double.valueOf(selectedProduct.getString("price"));
                    Double priceLot = priceUn * _quantity;
                    priceProducts.setText("Prix du lot : " + String.valueOf(priceLot) + "€");

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });


    }
}
