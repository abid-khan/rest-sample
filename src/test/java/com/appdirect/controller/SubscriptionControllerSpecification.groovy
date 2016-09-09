package com.appdirect.controller

import com.appdirect.entity.Subscription
import com.appdirect.repository.SubscriptionRepository
import com.appdirect.service.HttpServiceImpl
import com.appdirect.service.SubscriptionService
import com.appdirect.service.SubscriptionServiceImpl
import com.appdirect.util.CommonUtil
import com.appdirect.util.DataGeneratorUtil
import groovyx.net.http.RESTClient
import net.sf.json.xml.XMLSerializer
import org.mockito.InjectMocks
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.EnableTransactionManagement
import spock.lang.AutoCleanup
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import javax.ws.rs.core.MediaType

import static org.mockito.BDDMockito.*


/**
 * Created by abidk on 05/09/16.
 */


@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Stepwise
@EnableTransactionManagement
class SubscriptionControllerSpecification extends Specification {

    def createURI = "/subscriptions/notifications/create"
    def cancelURI = "/subscriptions/notifications/cancel"
    def dummyOrder = "https://www.appdirect.com/api/integration/v1/events/dummyOrder"
    def dummyCancel = "https://www.appdirect.com/api/integration/v1/events/dummyCancel"
    def inValidOrderResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><event xmlns:atom=\"http://www.w3.org/2005/Atom\"><type>SUBSCRIPTION_ORDER_INVALID</type><marketplace><baseUrl>https://acme.appdirect.com</baseUrl><partner>ACME</partner></marketplace><flag>STATELESS</flag><creator><email>test-email+creator@appdirect.com</email><firstName>DummyCreatorFirst</firstName><language>fr</language><lastName>DummyCreatorLast</lastName><openId>https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2</openId><uuid>ec5d8eda-5cec-444d-9e30-125b6e4b67e2</uuid></creator><payload><company><country>CA</country><email>company-email@example.com</email><name>Example Company Name</name><phoneNumber>415-555-1212</phoneNumber><uuid>d15bb36e-5fb5-11e0-8c3c-00262d2cda03</uuid><website>http://www.example.com</website></company><configuration><entry><key>domain</key><value>mydomain</value></entry></configuration><order><editionCode>BASIC</editionCode><pricingDuration>MONTHLY</pricingDuration><item><quantity>10</quantity><unit>USER</unit></item><item><quantity>15</quantity><unit>MEGABYTE</unit></item></order></payload><returnUrl>https://www.appdirect.com/finishprocure?token=dummyOrder</returnUrl></event>\t"
    def validOrderResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><event xmlns:atom=\"http://www.w3.org/2005/Atom\"><type>SUBSCRIPTION_ORDER</type><marketplace><baseUrl>https://acme.appdirect.com</baseUrl><partner>ACME</partner></marketplace><flag>STATELESS</flag><creator><email>test-email+creator@appdirect.com</email><firstName>DummyCreatorFirst</firstName><language>fr</language><lastName>DummyCreatorLast</lastName><openId>https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2</openId><uuid>ec5d8eda-5cec-444d-9e30-125b6e4b67e2</uuid></creator><payload><company><country>CA</country><email>company-email@example.com</email><name>Example Company Name</name><phoneNumber>415-555-1212</phoneNumber><uuid>d15bb36e-5fb5-11e0-8c3c-00262d2cda03</uuid><website>http://www.example.com</website></company><configuration><entry><key>domain</key><value>mydomain</value></entry></configuration><order><editionCode>BASIC</editionCode><pricingDuration>MONTHLY</pricingDuration><item><quantity>10</quantity><unit>USER</unit></item><item><quantity>15</quantity><unit>MEGABYTE</unit></item></order></payload><returnUrl>https://www.appdirect.com/finishprocure?token=dummyOrder</returnUrl></event>\t"
    def inValidCancelResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><event xmlns:atom=\"http://www.w3.org/2005/Atom\"><type>SUBSCRIPTION_CANCEL_INVALID</type><marketplace><baseUrl>https://acme.appdirect.com</baseUrl><partner>ACME</partner></marketplace><flag>STATELESS</flag><creator><email>test-email+creator@appdirect.com</email><firstName>DummyCreatorFirst</firstName><language>fr</language><lastName>DummyCreatorLast</lastName><openId>https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2</openId><uuid>ec5d8eda-5cec-444d-9e30-125b6e4b67e2</uuid></creator><payload><account><accountIdentifier>dummy-account</accountIdentifier><status>ACTIVE</status></account><configuration/></payload><returnUrl>https://www.appdirect.com/finishcancel?token=dummyCancel</returnUrl></event>"
    def validCancelResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><event xmlns:atom=\"http://www.w3.org/2005/Atom\"><type>SUBSCRIPTION_CANCEL</type><marketplace><baseUrl>https://acme.appdirect.com</baseUrl><partner>ACME</partner></marketplace><flag>STATELESS</flag><creator><email>test-email+creator@appdirect.com</email><firstName>DummyCreatorFirst</firstName><language>fr</language><lastName>DummyCreatorLast</lastName><openId>https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2</openId><uuid>ec5d8eda-5cec-444d-9e30-125b6e4b67e2</uuid></creator><payload><account><accountIdentifier>dummy-account</accountIdentifier><status>ACTIVE</status></account><configuration/></payload><returnUrl>https://www.appdirect.com/finishcancel?token=dummyCancel</returnUrl></event>"
    def xmlSerializer = new XMLSerializer()


    @Shared
    @AutoCleanup
    RESTClient restClient

    @MockBean
    HttpServiceImpl httpService


    @MockBean
    SubscriptionRepository subscriptionRepository

    @InjectMocks
    SubscriptionServiceImpl subscriptionService


    def setupSpec() {

        if (CommonUtil.isNull(restClient)) {
            restClient = new RESTClient("http://localhost:8080")
        }
    }

    def cleanupSpec() {
        restClient = null
    }


    def "Invalid  authorization header for create API"() {

        when:
        def response = restClient.get([
                path   : createURI,
                query  : [eventUrl: ""],
                headers: ["Authorization": ""]
        ])

        then:
        with(response) {
            status == HttpURLConnection.HTTP_OK
            contentType == MediaType.APPLICATION_JSON
        }

        with(response.data) {
            success == "false"
            accountIdentifier == ""
            errorCode == "ERROR_400"
            message == "Consumer key validation failed"
        }
    }


    def "Invalid order eventUrl"() {

        when:
        def response = restClient.get([
                path   : createURI,
                query  : [eventUrl: ""],
                headers: ["Authorization": "oauth_consumer_key=assignment-135827,oauth_nonce=8311386766772752470,oauth_signature=8EPpdSYxhozjabUA1n6TQn%2FvsDo%3D,oauth_signature_method=HMAC-SHA1,oauth_timestamp=1473272425,oauth_version=1.0"]
        ])

        then:
        with(response) {
            status == HttpURLConnection.HTTP_OK
            contentType == MediaType.APPLICATION_JSON
        }

        with(response.data) {
            success == "false"
            accountIdentifier == ""
            errorCode == "ERROR_400"
            message == "Empty event url"
        }
    }

    def "Order EventUrl  returns payload as null"() {
        given:
        given(this.httpService.getResponse(dummyOrder)).willReturn(null)
        when:
        def response = restClient.get([
                path   : createURI,
                query  : [eventUrl: dummyOrder],
                headers: ["Authorization": "oauth_consumer_key=assignment-135827,oauth_nonce=8311386766772752470,oauth_signature=8EPpdSYxhozjabUA1n6TQn%2FvsDo%3D,oauth_signature_method=HMAC-SHA1,oauth_timestamp=1473272425,oauth_version=1.0"]

        ])

        then:
        with(response) {
            status == HttpURLConnection.HTTP_OK
            contentType == MediaType.APPLICATION_JSON
        }

        with(response.data) {
            success == "false"
            accountIdentifier == ""
            errorCode == "ERROR_400"
            message == "Subscription response not received"
        }
    }

    def "Invalid subscription create type"() {
        given:
        //Mock API call
        def jsonResponse = xmlSerializer.read(inValidOrderResponse)
        given(this.httpService.getResponse(dummyOrder)).willReturn(jsonResponse)
        when:
        def response = restClient.get([
                path   : createURI,
                query  : [eventUrl: dummyOrder],
                headers: ["Authorization": "oauth_consumer_key=assignment-135827,oauth_nonce=8311386766772752470,oauth_signature=8EPpdSYxhozjabUA1n6TQn%2FvsDo%3D,oauth_signature_method=HMAC-SHA1,oauth_timestamp=1473272425,oauth_version=1.0"]

        ])

        then:
        with(response) {
            status == HttpURLConnection.HTTP_OK
            contentType == MediaType.APPLICATION_JSON
        }

        with(response.data) {
            success == "false"
            accountIdentifier == ""
            errorCode == "ERROR_400"
            message == "Invalid subscription type"
        }
    }

    def "Valid subscription create"() {
        given:
        //Mock API call
        def jsonResponse = xmlSerializer.read(validOrderResponse)
        given(this.httpService.getResponse(dummyOrder)).willReturn(jsonResponse)

        //Mock create call
        Subscription subscription = DataGeneratorUtil.buildCreateSubscription(jsonResponse);
        given(this.subscriptionRepository.saveAndFlush(subscription)).willReturn(subscription)

        when:
        def response = restClient.get([
                path   : createURI,
                query  : [eventUrl: dummyOrder],
                headers: ["Authorization": "oauth_consumer_key=assignment-135827,oauth_nonce=8311386766772752470,oauth_signature=8EPpdSYxhozjabUA1n6TQn%2FvsDo%3D,oauth_signature_method=HMAC-SHA1,oauth_timestamp=1473272425,oauth_version=1.0"]

        ])

        then:
        with(response) {
            status == HttpURLConnection.HTTP_OK
            contentType == MediaType.APPLICATION_JSON
        }

        with(response.data) {
            success == "true"
            accountIdentifier
            errorCode == "false"
            message == "success"
        }
    }

    //Cancel Order
    def "Invalid  authorization header for cancel API"() {

        when:
        def response = restClient.get([
                path   : cancelURI,
                query  : [eventUrl: ""],
                headers: ["Authorization": ""]
        ])

        then:
        with(response) {
            status == HttpURLConnection.HTTP_OK
            contentType == MediaType.APPLICATION_JSON
        }

        with(response.data) {
            success == "false"
            accountIdentifier == ""
            errorCode == "ERROR_400"
            message == "Consumer key validation failed"
        }
    }

    def "Invalid cancel order eventUrl"() {

        when:
        def response = restClient.get([
                path   : cancelURI,
                query  : [eventUrl: ""],
                headers: ["Authorization": "oauth_consumer_key=assignment-135827,oauth_nonce=8311386766772752470,oauth_signature=8EPpdSYxhozjabUA1n6TQn%2FvsDo%3D,oauth_signature_method=HMAC-SHA1,oauth_timestamp=1473272425,oauth_version=1.0"]

        ])

        then:
        with(response) {
            status == HttpURLConnection.HTTP_OK
            contentType == MediaType.APPLICATION_JSON
        }

        with(response.data) {
            success == "false"
            errorCode == "ERROR_400"
            message == "Empty event url"
        }
    }

    def "Cancel EventUrl  returns payload as null"() {
        given:

        //Mock API call
        given(this.httpService.getResponse(dummyCancel)).willReturn(null)
        when:
        def response = restClient.get([
                path   : cancelURI,
                query  : [eventUrl: dummyCancel],
                headers: ["Authorization": "oauth_consumer_key=assignment-135827,oauth_nonce=8311386766772752470,oauth_signature=8EPpdSYxhozjabUA1n6TQn%2FvsDo%3D,oauth_signature_method=HMAC-SHA1,oauth_timestamp=1473272425,oauth_version=1.0"]

        ])

        then:
        with(response) {
            status == HttpURLConnection.HTTP_OK
            contentType == MediaType.APPLICATION_JSON
        }

        with(response.data) {
            success == "false"
            errorCode == "ERROR_400"
            message == "Subscription response not received"
        }
    }


    def "Invalid subscription cancel type"() {
        given:
        //Mock API call
        def jsonResponse = xmlSerializer.read(inValidCancelResponse)
        given(this.httpService.getResponse(dummyCancel)).willReturn(jsonResponse)

        when:
        def response = restClient.get([
                path   : cancelURI,
                query  : [eventUrl: dummyCancel],
                headers: ["Authorization": "oauth_consumer_key=assignment-135827,oauth_nonce=8311386766772752470,oauth_signature=8EPpdSYxhozjabUA1n6TQn%2FvsDo%3D,oauth_signature_method=HMAC-SHA1,oauth_timestamp=1473272425,oauth_version=1.0"]

        ])

        then:
        with(response) {
            status == HttpURLConnection.HTTP_OK
            contentType == MediaType.APPLICATION_JSON
        }

        with(response.data) {
            success == "false"
            errorCode == "ERROR_400"
            message == "Invalid cancel order"
        }
    }

    @Ignore
    def "Valid subscription cancel order"() {
        given:
        //Mock API call
        def jsonResponse = xmlSerializer.read(validCancelResponse)
        given(this.httpService.getResponse(dummyCancel)).willReturn(jsonResponse)

        //Mock cancel call
        def createResponse = xmlSerializer.read(validOrderResponse)
        Subscription subscription = DataGeneratorUtil.buildCreateSubscription(createResponse);
        def identifier = "dummy-account"
        given(this.subscriptionRepository.findByIdentifier(identifier)).willReturn(subscription)

        given(this.subscriptionRepository.saveAndFlush(subscription)).willReturn(subscription)

        when:
        def response = restClient.get([
                path   : cancelURI,
                query  : [eventUrl: dummyCancel],
                headers: ["Authorization": "oauth_consumer_key=assignment-135827,oauth_nonce=8311386766772752470,oauth_signature=8EPpdSYxhozjabUA1n6TQn%2FvsDo%3D,oauth_signature_method=HMAC-SHA1,oauth_timestamp=1473272425,oauth_version=1.0"]

        ])

        then:
        with(response) {
            status == HttpURLConnection.HTTP_OK
            contentType == MediaType.APPLICATION_JSON
        }

        with(response.data) {
            success == "true"
            errorCode == "false"
            message == "success"
        }
    }
}
