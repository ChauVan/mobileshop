package com.example.mobileshop.Service;

import com.example.mobileshop.Object.AddCart.CartAdd;
import com.example.mobileshop.Object.Address.Address;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressDistrictData;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressDistrictRep;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressProvinceREP;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressWardsRep;
import com.example.mobileshop.Object.Address.AllAddressREP;
import com.example.mobileshop.Object.Cart.CartChange;
import com.example.mobileshop.Object.Cart.CartMessage;
import com.example.mobileshop.Object.CheckOut.CheckOutREQ;
import com.example.mobileshop.Object.HomeAllProduct.HomeAllProduct;
import com.example.mobileshop.Object.HomeAllProduct.HomeAllProductMessage;
import com.example.mobileshop.Object.Login.LoginMessage;
import com.example.mobileshop.Object.Login.LoginSocial;
import com.example.mobileshop.Object.Login.LoginUser;
import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Object.Message1;
import com.example.mobileshop.Object.Person.ChangePassword;
import com.example.mobileshop.Object.Person.InfoPerson;
import com.example.mobileshop.Object.Person.Person;
import com.example.mobileshop.Object.ProductDetail.PersonRating;
import com.example.mobileshop.Object.ProductDetail.ProductDetail;
import com.example.mobileshop.Object.ProductDetail.Rating;
import com.example.mobileshop.Object.ProductDetail.RatingREQ;
import com.example.mobileshop.Object.SignUp.SignUpUser;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface InterfaceAPIService {
    String BASE_Service = "https://api.covid21tsp.space/api/";

    @POST("login")
    Observable<LoginMessage> PostLogin(@Body LoginUser loginUser);

    @POST("loginSocial")
    Observable<LoginMessage> PostLoginSocial(@Body LoginSocial loginUser);

    @GET("getAllProductPaid")
    Observable<HomeAllProductMessage> GetProductPaid (@Header("Authorization") String token,@Query("user") String user);

    @GET("getRating")
    Observable<HomeAllProductMessage> GetRating (@Header("Authorization") String token,@Query("user") String user);

    @GET("refreshToken")
    Observable<LoginMessage> RefreshToken (@Header("Authorization") String token,@Query("user") String user);

    @POST("signup")
    Observable<Message> PostSignup(@Body SignUpUser signUpUser);

    @GET("sendEmail")
    Observable<Message> GetOTP(@Header("Authorization") String token,@Query("email") String email);

    @GET("getCart")
    Observable<CartMessage> GetCart(@Header("Authorization") String token, @Query("user") String user);

    @GET("allProduct")
    Observable<ArrayList<HomeAllProduct>> GetAllProduct();

    @GET("productDetail")
    Observable<HomeAllProduct> GetOneProduct(@Query("productId") String productId);

    @POST("addProductofCart")
    Observable<Message> AddToCart( @Header("Authorization") String token, @Body CartAdd cartAdd);

    @GET("getAccount")
    Observable<InfoPerson> GetPersonInfo(@Header("Authorization") String token, @Query("user") String user);

    @POST("updateCart1")
    Observable<Message> ChangeCart( @Header("Authorization") String token, @Body CartChange cartChange);

    @GET("deleteCart")
    Observable<Message> DelCart( @Header("Authorization") String token, @Query("user") String user,@Query("productId") String productId);

    @POST("createBill")
    Observable<Message> CreateBill( @Header("Authorization") String token, @Body CheckOutREQ checkOutREQ);

    @GET("getAllAddress")
    Observable<AllAddressREP> GetAllAddress(@Header("Authorization") String token, @Query("user") String user);

    @GET("province")
    Observable<AddressProvinceREP> GetProvince(@Header("token") String token);

    @GET("district")
    Observable<AddressDistrictRep> GetDistrict(@Header("token") String token, @Query("province_id") int provinceId);

    @GET("ward")
    Observable<AddressWardsRep> GetWards(@Header("token") String token, @Query("district_id") int districtId);

    @POST("updateAddress")
    Observable<Message> UpdateAddress(@Header("Authorization") String token,@Body Address address);

    @POST("addAddress")
    Observable<Message> AddAddress(@Header("Authorization") String token,@Body Address address);

    @GET("search")
    Observable<HomeAllProductMessage> SearchDevice(@Query("key") String name);

    @GET("allProductByType")
    Observable<ArrayList<HomeAllProduct>> TypeDevice(@Query("productTypeId") int name);

    @POST("changeInformation")
    Observable<Message> ChangePerson (@Header("Authorization") String token,@Body Person person);

    @POST("changePass")
    Observable<Message> ChangePassword (@Header("Authorization") String token,@Body ChangePassword changePassword);

    @GET("getFavorite")
    Observable<HomeAllProductMessage> GetFavorite (@Header("Authorization") String token,@Query("user") String user);

    @GET("addFavorite")
    Observable<Message> ChangeFavorite (@Header("Authorization") String token,@Query("user") String user, @Query("productId") String id);

    @GET("delFavorite")
    Observable<Message> DelFavorite (@Header("Authorization") String token,@Query("user") String user, @Query("productId") String id);

    @GET("productConfigs")
    Observable<ArrayList<ProductDetail>> GetProductConfig (@Query("productId") String id);

    @GET("productRatings")
    Observable<ArrayList<PersonRating>> GetRating (@Query("productId") String id);

    @POST("addRating")
    Observable<Message> AddRating (@Header("Authorization") String token,@Body RatingREQ ratingREQ);

    @POST("getBillsWithStatus")
    Observable<Message> GetBillStatus (@Header("Authorization") String token,@Query("user") String user,@Query("status") int status);

    @Multipart
    @POST("uploadPicAcc")
    Observable<Message1> Upload(@Header("Authorization") String token, @Part MultipartBody.Part photo, @Part("des") RequestBody des, @Query("user") String user);

    @GET("getLink")
    Observable<Message> getLink(@Header("Authorization") String token, @Query("user") String user);
}
