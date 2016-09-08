package com.appdirect.util;

import com.appdirect.constant.Fields;
import com.appdirect.entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by abidk on 08/09/16.
 */
public class DataGeneratorUtil {

    public static  Subscription buildCreateSubscription(JSONObject   response){
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

        return  subscription;
    }

}
