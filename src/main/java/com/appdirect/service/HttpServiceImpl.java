package com.appdirect.service;

import com.appdirect.exception.HttpServiceException;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.QueryStringSigningStrategy;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abidk on 04/09/16.
 */
@Service
public class HttpServiceImpl implements HttpService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${consumerKey}")
    private String consumerKey;

    @Value("${consumerSecret}")
    private String consumerSecret;

    private OAuthConsumer consumer;

    @PostConstruct
    public void init() {
        consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
    }

    @Override
    public JSONObject getResponse(String requestUrl) throws HttpServiceException {

        logger.info("Getting response for url {}", requestUrl);

        InputStream inputStream = null;
        JSONObject jsonObject = null;

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            consumer.sign(request);
            request.setRequestProperty("Content-Type", "application/json");
            request.setRequestMethod("GET");
            request.connect();

            if (request.getResponseCode() == HttpURLConnection.HTTP_OK) {
                logger.info("Success response for  url {}", requestUrl);
                inputStream = request.getInputStream();
                String body = IOUtils.toString(inputStream, "UTF-8");
                XMLSerializer xmlSerializer = new XMLSerializer();
                jsonObject  =  (JSONObject)xmlSerializer.read(body);
                return jsonObject;
            } else {
                logger.info("Error response for  url {}", requestUrl);
                inputStream = request.getErrorStream();
                //TODO handle failure
                return null;
            }

        } catch (Exception ex) {
            throw new HttpServiceException("Failed to get response due to {}", ex);
        }
    }

    @Override
    public String validate(String url) throws HttpServiceException {
        try {
            logger.info("Signing return url {}", url);
            consumer.setSigningStrategy(new QueryStringSigningStrategy());
            String signedUrl = consumer.sign(url);
            return signedUrl;
        } catch (Exception ex) {
            logger.error("Failed to sign return url");
            throw new HttpServiceException("Failed to  validate due to {}", ex);
        }
    }


}
