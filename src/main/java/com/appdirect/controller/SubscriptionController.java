package com.appdirect.controller;

import com.appdirect.constant.ErrorCodes;
import com.appdirect.constant.Fields;
import com.appdirect.entity.*;
import com.appdirect.exception.SubscriptionNotPresentServiceException;
import com.appdirect.service.HttpService;
import com.appdirect.service.SubscriptionService;
import com.appdirect.util.CommonUtil;
import com.appdirect.util.JSONUtil;
import com.appdirect.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @GET
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Context HttpServletRequest request, @QueryParam("eventUrl") String eventUrl) {


        try {
            logger.info("Notification received on create subscription with eventUrl{}", eventUrl);

            if (CommonUtil.isEmpty(eventUrl)) {
                logger.error("eventUrl can't be empty");
                return Response.ok().entity(ResponseUtil.buildResponse("false", "", ErrorCodes.ERROR_400.toString(), "Empty event url")).type(MediaType.APPLICATION_JSON)
                        .build();
            }

            //TODO  validate request  headers


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
            Subscription subscription = new Subscription();

            //Get market place data
            JSONObject marketPlace = JSONUtil.getJSONObject(response, Fields.MARKET_PLACE);
            Marketplace marketplace = new Marketplace();
            marketplace.setBaseUrl(JSONUtil.getString(marketPlace, Fields.BASE_URL));
            marketplace.setPartner(JSONUtil.getString(marketPlace, Fields.PARTNER));
            subscription.setMarketplace(marketplace);


            //Get creator data
            JSONObject creatorObject = JSONUtil.getJSONObject(response, Fields.CREATOR);

            Creator creator = new Creator();
            creator.setEmail(JSONUtil.getString(creatorObject, Fields.EMAIL));
            creator.setFirstName(JSONUtil.getString(creatorObject, Fields.FIRAT_NAME));
            creator.setLastName(JSONUtil.getString(creatorObject, Fields.LAST_NAME));
            creator.setLanguage(JSONUtil.getString(creatorObject, Fields.LANGUAGE));
            creator.setOpenId(JSONUtil.getString(creatorObject, Fields.OPEN_ID));
            creator.setUuid(JSONUtil.getString(creatorObject, Fields.UUID));
            subscription.setCreator(creator);

            //Get payload data
            JSONObject payloadObject = JSONUtil.getJSONObject(response, Fields.PAYLOAD);


            JSONObject companyObject = JSONUtil.getJSONObject(payloadObject, Fields.COMPANY);
            Company company = new Company();
            company.setCountry(JSONUtil.getString(companyObject, Fields.COUNTRY));
            company.setName(JSONUtil.getString(companyObject, Fields.NAME));
            company.setPhNo(JSONUtil.getString(companyObject, Fields.PH));
            company.setUuid(JSONUtil.getString(companyObject, Fields.UUID));
            company.setWebsite(JSONUtil.getString(companyObject, Fields.WEB_SITE));

            subscription.setCompany(company);

            //Get order data
            JSONObject orderObject = JSONUtil.getJSONObject(payloadObject, Fields.ORDER);
            Order order = new Order();
            order.setEditionCode(JSONUtil.getString(orderObject, Fields.EDITION_CODE));
            order.setPricingDuration(JSONUtil.getString(orderObject, Fields.PRICING_DURATION));

            List<Item> itemsList = new ArrayList<Item>();
            JSONArray itemObjects = JSONUtil.getJSONArray(orderObject, Fields.ITEM);

            for (int i = 0; i < itemObjects.size(); i++) {
                JSONObject itemObject = (JSONObject) itemObjects.get(i);
                Item item = new Item();
                item.setQuantity(JSONUtil.getLong(itemObject, Fields.QUANTITY));
                item.setUnit(JSONUtil.getString(itemObject, Fields.UNIT));
                itemsList.add(item);
            }
            order.setItems(itemsList);

            subscription.setOrder(order);

            subscription.setIdentifier(UUID.randomUUID().toString());
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


            if (CommonUtil.isEmpty(eventUrl)) {
                logger.error("eventUrl can't be empty");
                return Response.ok().entity(ResponseUtil.buildResponse("false", "ERROR_400", "Empty event url")).type(MediaType.APPLICATION_JSON)
                        .build();
            }

            //TODO  validate request from aPPDirect

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
