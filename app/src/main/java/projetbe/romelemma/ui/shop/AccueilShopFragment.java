package projetbe.romelemma.ui.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import projetbe.romelemma.MainActivity;
import projetbe.romelemma.R;

public class AccueilShopFragment extends Fragment {

    public JSONArray _produits;
    public Adapter adapter;
    public ListView _produitListe;
    BottomNavigationView bottomNavigationView;
    private String _response;
    private static View _view;

    private static Button confirmOrder;


    public static AccueilShopFragment newInstance() {
        AccueilShopFragment fragment = new AccueilShopFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shop, container, false);

        _view = view;
        setView(view);
        handleSSLHandshake();
        getProductsByPharmacy("1");

        return view;
    }


    private void setView(View root){
        final TextView titleShop = (TextView) root.findViewById(R.id.title);
       // setFont(titleShop, "QUICKSAND");

        Button panier = root.findViewById(R.id.card);


        TextView emptyCard = _view.findViewById(R.id.emptyCard);
        emptyCard.setVisibility(View.INVISIBLE);

        Button back = root.findViewById(R.id.back);
        back.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                back.setVisibility(View.INVISIBLE);
                panier.setVisibility(View.VISIBLE);
                confirmOrder.setVisibility(View.INVISIBLE);
                titleShop.setText("Tous les produits");
                _produitListe.setVisibility(View.VISIBLE);
                try {
                    displayProduct(_produits);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                emptyCard.setVisibility(View.INVISIBLE);

            }
        });

        panier.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showCardView(titleShop, emptyCard);
                panier.setVisibility(View.INVISIBLE);
                back.setVisibility(View.VISIBLE);

            }
        });


        _produitListe = root.findViewById(R.id.list);
        _produitListe.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                panier.setVisibility(View.INVISIBLE);
                ProductFragment fragment = ProductFragment.newInstance(_response, position);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.accueilShop, fragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });
        confirmOrder = root.findViewById(R.id.confirmOrder);

    }

    private void showCardView(TextView titleShop, TextView emptyCard) {

        titleShop.setText("Panier");

        final PanierClass panier= new PanierClass();
        JSONArray _productsCart = panier.getListProducts();

        if(_productsCart.length() == 0){
            confirmOrder.setVisibility(View.INVISIBLE);
            emptyCard.setVisibility(View.VISIBLE);

            try {
                displayProduct(_productsCart);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            confirmOrder.setVisibility(View.VISIBLE);
            try {
                displayProduct(_productsCart);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            confirmOrder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    panier.confirmOrder(getContext());
                    _produitListe.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public void setFont(TextView textView, String fontName) {
        if(fontName != null){
            try {
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                textView.setTypeface(typeface);
            } catch (Exception e) {
                Log.e("FONT", fontName + " not found", e);
            }
        }
    }


    public void displayProduct(JSONArray p) throws JSONException {
        ArrayList<String> pr = new ArrayList<String>();
        ArrayList<String> price = new ArrayList<String>();
        ArrayList<String> des = new ArrayList<String>();
        ArrayList<String> qty = new ArrayList<String>();
        ArrayList<Bitmap> im = new ArrayList<Bitmap>();

        for( int i = 0; i<p.length(); i++) {
            JSONObject produit = p.getJSONObject(Integer.parseInt(String.valueOf(i)));
            String name = produit.getString("title");
            String prix = produit.getString("price");
            String description = produit.getString("description");
            Bitmap bt = getBitmapFromURL(produit.getString("image_url"));
            pr.add(name);
            des.add(description);
            im.add(bt);
            price.add(prix);

            if(produit.isNull("quantity")){
                qty = null;
            }else{
                String quantity = produit.getString("quantity");
                qty.add(quantity);
            }
        }

        CustomListProduct listAdapter = new CustomListProduct(getActivity(), pr, im, price, des, qty);
        _produitListe.setAdapter(listAdapter);

    }

    public static Bitmap getBitmapFromURL(String image_url) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(image_url);
            Bitmap test = BitmapFactory.decodeStream((InputStream)url.getContent());
            return test;
        } catch (IOException e) {
            //Log.e(TAG, e.getMessage());
        }
        return  null;
    }


    public void getProductsByPharmacy(final String pharmacy_id) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = "https://88-122-235-110.traefik.me:61001/api/product/getProductsByPharmacy";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    _response = response;
                    JSONObject test  = new JSONObject(response);
                    _produits = test.getJSONArray("result");
                    displayProduct(_produits);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int a = 1;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Host", "node");
                return params;
            }

            @Override
            protected Map<String, String> getParams()  throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pharmacy_id", pharmacy_id);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private ArrayList createArrayListItem(int position){
        try {
            JSONObject jObject = _produits.getJSONObject(position);
            JSONArray jArray = new JSONArray();
            jArray.put(jObject);

            ArrayList<String> listdata = new ArrayList<String>();
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    listdata.add(jArray.getString(i));
                }
            }

            return listdata;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

        @SuppressLint("TrulyRandom")
        public static void handleSSLHandshake() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception ignored) {
            }
        }


    public static void orderIsCreate(String response){
        final LottieAnimationView check = _view.findViewById(R.id.lottie_check);
        confirmOrder.setVisibility(View.INVISIBLE);
        check.setVisibility(View.VISIBLE);
        check.playAnimation();
        final Handler handler = new Handler();

        long time = check.getDuration();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                check.setVisibility(View.INVISIBLE);
            }
        }, time);


    }
}
