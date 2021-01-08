package projetbe.romelemma.ui.shop;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Panier {

    static private JSONArray _productsCart;
    static JSONObject commande = new JSONObject();

    static JSONArray productsCommande = new JSONArray();

    static Double priceTotal;

    static Boolean hasProduct = false;

    void addProductToList(JSONObject producttoAdd, int quantity){
        if(_productsCart == null) {
            try {
                _productsCart = new JSONArray();
                producttoAdd.put("quantity",quantity);
                _productsCart.put(producttoAdd);

                JSONObject product = new JSONObject();
                product.put("id_product", producttoAdd.get("id"));
                product.put("quantity", quantity);
                productsCommande.put(product);


                Double priceU = producttoAdd.getDouble("price");
                priceTotal = priceU*quantity;


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
                try {
                    for(int i = 0; i < productsCommande.length() ; i++) {
                        JSONObject pro = productsCommande.getJSONObject(i);

                        if (pro.get("id_product").equals(producttoAdd.get("id"))) {
                            uploadProductToOrder( producttoAdd, pro,  quantity,  i);
                            hasProduct = true;
                        }
                    }
                    if(!hasProduct){
                        addProductToOrder(producttoAdd, quantity);
                    }
                    hasProduct = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        try {
            commande.put("id_client", "23");
            commande.put("id_pharmacy", "1");
            commande.put("total_price", priceTotal);
            commande.put("products", productsCommande);
            commande.put("detail", "RAS");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONArray getListProducts(){
        if(_productsCart == null){
            _productsCart = new JSONArray();
            return _productsCart;
        }else{
            return _productsCart;
        }
    }

    private void addProductToOrder(JSONObject producttoAdd,int quantity){

        JSONObject product2 = new JSONObject();
        try {
            product2.put("id_product", producttoAdd.get("id"));
            product2.put("quantity", quantity);
            productsCommande.put(product2);


            producttoAdd.put("quantity",quantity);
            _productsCart.put(producttoAdd);

            Double priceU = producttoAdd.getDouble("price");
            priceTotal = priceTotal+ (priceU*quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void uploadProductToOrder(JSONObject producttoAdd,JSONObject pro, int quantity, int i){
        //Si jamais le produit est déjà dans la liste
        int oldQuantity = 0;
        try {
            oldQuantity = pro.getInt("quantity");
            int newQuantity = oldQuantity + quantity;
            pro.put("quantity", newQuantity);

            JSONObject proCard = _productsCart.getJSONObject(i);
            if(proCard.getString("id").equals(String.valueOf(producttoAdd.get("id")))){
                proCard.put("quantity", newQuantity);
            }
            Double priceU = producttoAdd.getDouble("price");
            priceTotal = priceTotal+ (priceU*quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void confirmOrder(Context context) {
        try {
            createOrder(context, commande.getString("id_client"), commande.getString("id_pharmacy"), commande.getString("total_price"), commande.getJSONArray("products"), commande.getString("detail"));
            commande = new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void createOrder(Context context, final String id_client, final String id_pharmacy, final String total_price, final JSONArray products, final String details) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "https://88-122-235-110.traefik.me:61001/api/order/createOrder";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AccueilShop.orderIsCreate(response);

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
                params.put("id_client", id_client);
                params.put("id_pharmacy", id_pharmacy);
                params.put("total_price", total_price);
                params.put("products", String.valueOf(productsCommande));
                params.put("detail", details);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
