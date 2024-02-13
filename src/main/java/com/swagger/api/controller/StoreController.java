package com.swagger.api.controller;
import com.swagger.petstore.models.Order;
import io.restassured.response.Response;

public class StoreController extends BaseController{
    public Response createOrder(Order targetOrder){
        return petStoreApiClient("/store")
                .body(targetOrder)
                .post("/");
    }
}
