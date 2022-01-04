package ru.bstu.it41.service;

import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.AllForUser;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.models.MessageStatus;
import ru.bstu.it41.service.models.OfferWithTask;
import ru.bstu.it41.service.models.OffersWithUserinfo;
import ru.bstu.it41.service.models.OtherTender;
import ru.bstu.it41.service.models.ResultAuthorizationInfo;
import ru.bstu.it41.service.models.ReviewAndUserinfo;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.models.StatusOffer;
import ru.bstu.it41.service.models.StatusPhoto;
import ru.bstu.it41.service.models.StatusTask;
import ru.bstu.it41.service.models.StatusTender;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.TaskAndTenderWithOffers;
import ru.bstu.it41.service.models.Tasks;

import static ru.bstu.it41.service.any.UserRequest.PARAM_API;

/**
 * Created by NorD on 16.11.2017.
 */

public interface serviceAPI {

    @FormUrlEncoded
    @POST(UserRequest.URL_LOGIN + "?mobile")
    Call<Status>loginTest(@Field(UserRequest.PARAM_EMAIL) String email,
                          @Field(UserRequest.PARAM_PASSWORD) String password);

    @FormUrlEncoded
    @POST(UserRequest.URL_LOGIN)
    Call<ResultAuthorizationInfo>login(@Field(UserRequest.PARAM_EMAIL) String email,
                                       @Field(UserRequest.PARAM_PASSWORD) String password);

    @FormUrlEncoded
    @POST(UserRequest.URL_REGISTER)
    Call<Status>register(@Field(UserRequest.PARAM_FIRSTNAME) String firstname,
                         @Field(UserRequest.PARAM_LASTNAME) String lastname,
                         @Field(UserRequest.PARAM_MALE) byte male,
                         @Field(UserRequest.PARAM_EMAIL) String email,
                         @Field(UserRequest.PARAM_TELEPHONE) String phone,
                         @Field(UserRequest.PARAM_PASSWORD) String password);

    @FormUrlEncoded
    @POST(UserRequest.URL_REGISTER + "?mobile")
    Call<String>registerTest(@Field(UserRequest.PARAM_FIRSTNAME) String firstname,
                         @Field(UserRequest.PARAM_LASTNAME) String lastname,
                         @Field(UserRequest.PARAM_MALE) byte male,
                         @Field(UserRequest.PARAM_EMAIL) String email,
                         @Field(UserRequest.PARAM_TELEPHONE) String phone,
                         @Field(UserRequest.PARAM_PASSWORD) String password);

    @GET(UserRequest.URL_PROFILE)
    Call<AllForUser> getProfileView();

    @FormUrlEncoded
    @POST(UserRequest.URL_PROFILE)
    Call<Status> changeProfileView(@Field(UserRequest.PARAM_FIRSTNAME) String firstname,
                                   @Field(UserRequest.PARAM_LASTNAME) String lastname,
                                   @Field(UserRequest.PARAM_PATRONYMIC) String patronymic,
                                   @Field(UserRequest.PARAM_BIRTHDAY) Date birthday,
                                   @Field(UserRequest.PARAM_MALE) int male,
                                   @Field(UserRequest.PARAM_ADDRESS) String address);

    @GET(UserRequest.URL_REVIEWS)
    Call<List<ReviewAndUserinfo>> getReviews(@Path(UserRequest.PARAM_USER_ID) int userId);

    @FormUrlEncoded
    @POST(UserRequest.URL_ABOUT)
    Call<Status> changeProfileAbout(@Field(UserRequest.PARAM_ABOUT) String about);

    @Multipart
    @POST(UserRequest.URL_LOAD_PHOTO)
    Call<StatusPhoto> loadPhoto(@Part() MultipartBody.Part file);

    @FormUrlEncoded
    @POST(UserRequest.URL_SETTINGS)
    Call<Status> changeSettings(@Field(UserRequest.PARAM_NOTIFICATION_EMAIL) int notifyEmail,
                                @Field(UserRequest.PARAM_NOTIFICATION_SMS) int notifySms,
                                @Field(UserRequest.PARAM_NOTIFICATION_PUSH) int notifyPush);

    @FormUrlEncoded
    @POST(UserRequest.URL_LOGOUT)
    Call<String> logout(@Field("mobile") int mobile);

    @GET(UserRequest.URL_TENDERS)
    Call<List<OtherTender>> getTenders();

    @GET(UserRequest.URL_TASKS)
    Call<List<TaskAndTenderWithOffers>> getTasks();

    @GET("/tasksAPI/getAllTasks")
    Call<List<Tasks>> getAllTasksTest();

    @FormUrlEncoded
    @PUT(UserRequest.URL_TASKS)
    Call<StatusTask> addTask(@Field(UserRequest.PARAM_NAME) String taskName,
                             @Field(UserRequest.PARAM_DESCRIPTION) String taskDescription,
                             @Field(UserRequest.PARAM_CATEGORY) int taskCategoryId,
                             @Field(UserRequest.PARAM_DEADLINE) Date taskDeadline,
                             @Field(UserRequest.PARAM_PRICE) int taskPrice,
                             @Field(UserRequest.PARAM_LATITUDE) Double latitude,
                             @Field(UserRequest.PARAM_LONGITUDE) Double longitude,
                             @Field(UserRequest.PARAM_ADDRESS) String address);

    @DELETE(UserRequest.URL_TASK_DELETE)
    Call<Status> deleteTask(@Path(UserRequest.PARAM_TASK_ID) int taskId);

    @FormUrlEncoded
    @POST(UserRequest.URL_TASK_CHANGE)
    Call<Status> changeTask(@Field(UserRequest.PARAM_TASK_ID) int taskId,
                            @Field(UserRequest.PARAM_NAME) String taskName,
                            @Field(UserRequest.PARAM_DESCRIPTION) String taskDescription,
                            @Field(UserRequest.PARAM_CATEGORY) int taskCategoryId,
                            @Field(UserRequest.PARAM_DEADLINE) Date taskDeadline,
                            @Field(UserRequest.PARAM_PRICE) int taskPrice,
                            @Field(UserRequest.PARAM_LATITUDE) Double latitude,
                            @Field(UserRequest.PARAM_LONGITUDE) Double longitude,
                            @Field(UserRequest.PARAM_ADDRESS) String address);

    @FormUrlEncoded
    @PUT(UserRequest.URL_TENDERS)
    Call<StatusTender> addTender(@Field(UserRequest.PARAM_TASK_ID) int taskID,
                                 @Field(UserRequest.PARAM_DATE_END) Date tenderDateEnd);

    @DELETE(UserRequest.URL_TENDER_DELETE)
    Call<Status> deleteTender(@Path(UserRequest.PARAM_TENDER_ID) int tenderID);

    @POST(UserRequest.URL_CATEGORIES)
    Call<List<Category>> getCategories();

    @GET(UserRequest.URL_OFFERS_FOR_TENDER)
    Call<OffersWithUserinfo> getOffers(@Path(UserRequest.PARAM_TENDER_ID) int tenderID);

    @GET(UserRequest.URL_OFFERS_FOR_USER)
    Call<List<OfferWithTask>> getOffersForUser();

    @DELETE(UserRequest.URL_OFFERS_DELETE)
    Call<Status> deleteOffer(@Path(UserRequest.PARAM_OFFER_ID) int offerID);

    @FormUrlEncoded
    @PUT(UserRequest.URL_ADD_OFFER)
    Call<StatusOffer> addOffer(@Field(UserRequest.PARAM_TENDER_ID) int tenderID,
                               @Field(UserRequest.PARAM_DEADLINE) Date deadline,
                               @Field(UserRequest.PARAM_PRICE) int price,
                               @Field(UserRequest.PARAM_DESCRIPTION) String description);

    @GET(UserRequest.URL_GET_ALL_MESSAGES)
    Call<MessageStatus> getAllMessages(@Path(UserRequest.PARAM_AUCTION_ID) int auctionID);

    @FormUrlEncoded
    @PUT(UserRequest.URL_SEND_MESSAGE)
    Call<Status> sendMessage(@Field(UserRequest.PARAM_TENDER_ID) int tenderID,
                               @Field(UserRequest.PARAM_MESSAGE) String message);

    @FormUrlEncoded
    @POST(UserRequest.URL_SEND_TOKEN_ID)
    Call<Status> sendToken(@Field(UserRequest.PARAM_TOKEN_ID) String token);

    @POST(UserRequest.URL_SEND_CLEAR_TOKENS)
    Call<Status> clearTokens();

    @FormUrlEncoded
    @POST(UserRequest.URL_COMPLETE_TASK)
    Call<Status> completeTask(@Field(UserRequest.PARAM_TENDER_ID) int tenderId);

    @FormUrlEncoded
    @POST(UserRequest.URL_FINISH_TASK)
    Call<Status> finishTask(@Field(UserRequest.PARAM_TENDER_ID) int tenderId);

    @FormUrlEncoded
    @POST(UserRequest.URL_SELECT_OFFER)
    Call<Status> selectOffer(@Field(UserRequest.PARAM_AUCTION_ID) int tenderId,
                             @Field(UserRequest.PARAM_OFFER_ID) int offerId);

    @FormUrlEncoded
    @POST(UserRequest.URL_ADD_REVIEW)
    Call<Status> addReview(@Field(UserRequest.PARAM_TENDER_ID) int tenderId,
                           @Field(UserRequest.PARAM_RATING) int rating,
                           @Field(UserRequest.PARAM_TEXT_REVIEW) String textReview);
}
