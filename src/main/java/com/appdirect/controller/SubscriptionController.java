package com.appdirect.controller;

import com.appdirect.constant.ErrorCodes;
import com.appdirect.constant.Fields;
import com.appdirect.entity.*;
import com.appdirect.exception.SubscriptionNotPresentServiceException;
import com.appdirect.service.HttpService;
import com.appdirect.service.SubscriptionService;
import com.appdirect.util.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by abidk on 03/09/16.
 * Defines APIs for subscription event notifications
 */
@Component
@Path("/notifications")
public class SubscriptionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private HttpService httpService;

    @Value("${consumerKey}")
    private String consumerKey;

    @GET
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Context HttpServletRequest request, @QueryParam("eventUrl") String eventUrl) {


        try {
            logger.info("Notification received on create subscription with eventUrl{}", eventUrl);


           //Validate request
            if(!RequestValidatorUtil.isValid(request,consumerKey)){
                return Response.ok().entity(ResponseUtil.buildResponse("false", "", ErrorCodes.ERROR_400.toString(), "Consumer key validation failed")).type(MediaType.APPLICATION_JSON)
                        .build();
            }


            if (CommonUtil.isEmpty(eventUrl)) {
                logger.error("eventUrl can't be empty");
                return Response.ok().entity(ResponseUtil.buildResponse("false", "", ErrorCodes.ERROR_400.toString(), "Empty event url")).type(MediaType.APPLICATION_JSON)
                        .build();
            }


            //Get subscription data
            JSONObject response = httpService.getResponse(eventUrl);
            if (CommonUtil.isNull(response)) {
                logger.error("No payload received using eventUrl {}", eventUrl);
                return Response.ok().entity(ResponseUtil.buildResponse("false", "", ErrorCodes.ERROR_400.toString(), "Subscription response not received")).type(MediaType.APPLICATION_JSON)
                        .build();
            }

            //Validate subscripiton type
            String type = JSONUtil.getString(response, Fields.TYPE);
            if (!Fields.SUBSCRIPTION_ORDER.equals(type)) {
                logger.error("Invalid create subscription type received using eventUrl {}", eventUrl);
                return Response.ok().entity(ResponseUtil.buildResponse("false", "", ErrorCodes.ERROR_400.toString(), "Invalid subscription type")).type(MediaType.APPLICATION_JSON)
                        .build();
            }


            //Create subscription entry
            Subscription subscription = DataGeneratorUtil.buildCreateSubscription(response);

            //Create subscription entry
            subscription = subscriptionService.create(subscription);

            return Response.ok().entity(ResponseUtil.buildResponse("true", subscription.getIdentifier(), "false", "success")).build();

        } catch (Exception ex) {
            logger.error("Failed handle create subscription notification due to ", ex);
            return Response.ok().entity(ResponseUtil.buildResponse("false", "", ErrorCodes.ERROR_400.toString(), ex.getMessage())).type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }


    @GET
    @Path("/cancel")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelSubscription(@Context HttpServletRequest request, @QueryParam("eventUrl") String eventUrl) {


        try {
            logger.info("Notification received on cancel subscription with eventUrl {}", eventUrl);


            //Validate request
            if(!RequestValidatorUtil.isValid(request,consumerKey)){
                return Response.ok().entity(ResponseUtil.buildResponse("false", "", ErrorCodes.ERROR_400.toString(), "Consumer key validation failed")).type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (CommonUtil.isEmpty(eventUrl)) {
                logger.error("eventUrl can't be empty");
                return Response.ok().entity(ResponseUtil.buildResponse("false", "ERROR_400", "Empty event url")).type(MediaType.APPLICATION_JSON)
                        .build();
            }


            //Get subscription data
            JSONObject response = httpService.getResponse(eventUrl);
            if (CommonUtil.isNull(response)) {
                logger.error("No payload received using eventUrl {}", eventUrl);
                return Response.ok().entity(ResponseUtil.buildResponse("false", "ERROR_400", "Subscription response not received")).type(MediaType.APPLICATION_JSON)
                        .build();
            }

            //Validate subscripiton type
            String type = JSONUtil.getString(response, Fields.TYPE);
            if (!Fields.SUBSCRIPTION_CANCEL.equals(type)) {
                logger.error("Invalid cancel subscription type received using eventUrl {}", eventUrl);
                return Response.ok().entity(ResponseUtil.buildResponse("false", "", ErrorCodes.ERROR_400.toString(), "Invalid cancel order")).type(MediaType.APPLICATION_JSON)
                        .build();
            }

            //Get payload data
            JSONObject payloadObject = JSONUtil.getJSONObject(response, Fields.PAYLOAD);
            if (CommonUtil.isNull(payloadObject)) {
                logger.error("No payload received using eventUrl {}", eventUrl);
                return Response.ok().entity(ResponseUtil.buildResponse("false", "ERROR_400", "Subscription response not received")).type(MediaType.APPLICATION_JSON)
                        .build();
            }
            JSONObject accountObject = JSONUtil.getJSONObject(payloadObject, Fields.ACCOUNT);
            if (CommonUtil.isNull(accountObject)) {
                logger.error("No account details received using eventUrl {}", eventUrl);
                return Response.ok().entity(ResponseUtil.buildResponse("false", "ERROR_400", "Account details not received")).type(MediaType.APPLICATION_JSON)
                        .build();

            }
            String accountIdentifier = JSONUtil.getString(accountObject, Fields.ACCOUNT_IDENTIFIER);

            if (CommonUtil.isEmpty(accountIdentifier)) {
                logger.error("No account id received using eventUrl {}", eventUrl);
                return Response.ok().entity(ResponseUtil.buildResponse("false", "ERROR_400", "Account id n missing")).type(MediaType.APPLICATION_JSON)
                        .build();
            }


            //Cancel subscription
            Subscription subscription = subscriptionService.findByIdentifier(accountIdentifier);

            return Response.ok().entity(ResponseUtil.buildResponse("true", "false", "success")).build();

        } catch (SubscriptionNotPresentServiceException ex) {
            logger.error("Failed  cancel subscription notification due to ", ex);

            return Response.ok().entity(ResponseUtil.buildResponse("false", "true", ex.getMessage())).build();

        } catch (Exception ex) {
            logger.error("Failed  cancel subscription notification due to ", ex);
            return Response.ok().entity(ResponseUtil.buildResponse("false", "true", ex.getMessage())).build();

        }
    }


}
