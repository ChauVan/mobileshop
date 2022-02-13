package com.example.mobileshop.adapter;

import static com.example.mobileshop.Others.ShowNotify.dismissProgressDialog;
import static com.example.mobileshop.Others.ShowNotify.showProgressDialog;
import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileshop.CartActivity;
import com.example.mobileshop.CheckOutActivity;
import com.example.mobileshop.MainActivity;
import com.example.mobileshop.Object.Cart.CartProduct;
import com.example.mobileshop.Object.CheckOut.CheckOutREQ;
import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Others.ClickListener;
import com.example.mobileshop.Others.ConvertMoney;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Others.GetNewToken;
import com.example.mobileshop.Others.ItemClickListener;
import com.example.mobileshop.ProductActivity;
import com.example.mobileshop.R;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {


    private ArrayList<CartProduct> alcartProduct;
    private ProductAdapter productAdapter;
    private ConvertMoney convertMoney = new ConvertMoney();

    //Này là để đem cái giá trị bị thay đổi trong Recycle ra ngoài Activity
    public ArrayList<CartProduct> getAlcartProduct() {
        return alcartProduct;
    }


    public void setAlcartProduct(ArrayList<CartProduct> alcartProduct) {
        this.alcartProduct = alcartProduct;
    }

    private static Context context;

    public  ProductAdapter(){

    }

    public ProductAdapter(ArrayList<CartProduct> alcartProduct, Context context) {
        this.alcartProduct = alcartProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_cart,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){

        int index = position;
        holder.ckbItemCartCheck.setChecked(Boolean.valueOf(alcartProduct.get(position).getProductStatus()));
        new DownloadImageFromInternet((ImageView) holder.imgItemCartPhoto).execute(String.valueOf(alcartProduct.get(position).getPic()));
        holder.txtItemCartProductName.setText((String.valueOf(alcartProduct.get(position).getProductName())));
        holder.txtItemCartProductPrice.setText((String.valueOf(convertMoney.StringToMoney(alcartProduct.get(position).getPrice()+""))));
        holder.edtItemCartAmount.setText(String.valueOf(alcartProduct.get(position).getAmount()));

        holder.txtplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int s = Integer.parseInt(String.valueOf(holder.edtItemCartAmount.getText())) + 1 ;
                holder.edtItemCartAmount.setText(s+"");
                alcartProduct.get(index).setAmount(s);
            }
        });

        holder.txtsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int s = Integer.parseInt(String.valueOf(holder.edtItemCartAmount.getText()));
                if(s>1)
                s -= 1;
                holder.edtItemCartAmount.setText(s+"");
                alcartProduct.get(index).setAmount(s);
            }
        });

        holder.ckbItemCartCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean bl = Boolean.valueOf(alcartProduct.get(index).getProductStatus());
                alcartProduct.get(index).setProductStatus(!bl+"");
            }
        });

        holder.txtItemCartProductDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallAPIDelCart(alcartProduct.get(index).getProductId()+"");
                delete(index);
            }
        });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ProductID",alcartProduct.get(position).getProductId()+"");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alcartProduct.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imgItemCartPhoto;
        public TextView txtItemCartProductName,txtItemCartProductPrice,txtItemCartProductDel;
        public CheckBox ckbItemCartCheck;
        public EditText edtItemCartAmount;
        public TextView txtplus,txtsub;
        public ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemCartPhoto = itemView.findViewById(R.id.imgItemCartPhoto);
            txtItemCartProductName = itemView.findViewById(R.id.txtItemCartProductName);
            txtItemCartProductPrice = itemView.findViewById(R.id.txtItemCartProductPrice);
            txtItemCartProductDel = itemView.findViewById(R.id.txtItemCartProductDel);
            ckbItemCartCheck = itemView.findViewById(R.id.ckbItemCartCheck);
            edtItemCartAmount = itemView.findViewById(R.id.edtItemCartAmount);
            txtplus = itemView.findViewById(R.id.txtplus);
            txtsub = itemView.findViewById(R.id.txtsub);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getAdapterPosition());
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    private  void CallAPIDelCart(String prodictId){
        String user;
        DataToken dataToken = new DataToken(context);

        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        user = preferences.getString("user","");

        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.DelCart(dataToken.getToken(),user,prodictId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(Message message) {
        if(message.getStatus()!=1)
        Toast.makeText(context, message.getNotification().toString(), Toast.LENGTH_SHORT).show();
    }

    private void handleError(Throwable error){
        Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }

    //Xóa vị trỉ chổ item đó xóa luôn cái hiển thị
    public void delete(int position) {
        alcartProduct.remove(position);
        notifyDataSetChanged();
    }


}
