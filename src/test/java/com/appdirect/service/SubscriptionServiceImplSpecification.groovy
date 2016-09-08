package com.appdirect.service

import com.appdirect.AssignemntApplication
import com.appdirect.entity.Creator
import com.appdirect.entity.Subscription
import com.appdirect.exception.SubscriptionNotPresentServiceException
import com.appdirect.exception.SubscriptionServiceException
import com.appdirect.repository.SubscriptionRepository
import com.appdirect.util.CommonUtil
import groovyx.net.http.RESTClient
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.EnableTransactionManagement
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import javax.ws.rs.core.MediaType

import static org.mockito.BDDMockito.given

/**
 * Created by abidk on 05/09/16.
 */
@ContextConfiguration
@SpringBootTest
@Stepwise
@EnableTransactionManagement
class SubscriptionServiceImplSpecification extends  Specification {

    @Mock
    SubscriptionRepository  subscriptionRepository

    @InjectMocks
    SubscriptionServiceImpl subscriptionService



    def "Find by account identifier"() {

        given:
        def accountIdentifier = UUID.randomUUID().toString()
        Subscription subscription  =  new Subscription()
        subscription.setIdentifier(accountIdentifier)
        subscription.setVersion(0)
        given(this.subscriptionRepository.findByIdentifier(accountIdentifier)).willReturn(subscription)
        when:
        def response = subscriptionService.findByIdentifier(accountIdentifier)

        then:
        with(response) {
           response.getIdentifier() == subscription.getIdentifier()
        }


    }

    def "Subscription does not  exists with account identifier"() {

        given:

        given(this.subscriptionRepository.findByIdentifier("abc")).willReturn(null)
        when:
         subscriptionService.findByIdentifier("abc")

        then:
        thrown(SubscriptionServiceException)


    }

    def "Create subscription entry"() {

        given:
        Subscription subscription  =  new Subscription()
        Creator creator = new Creator()
        creator.setEmail("demo")
        creator.setUuid("demo")
        subscription.setCreator(creator)
        subscription.setIdentifier(UUID.randomUUID().toString())
        given(this.subscriptionRepository.saveAndFlush(subscription)).willReturn(subscription)

        when:
        def response =  subscriptionService.create(subscription)

        then:
        response.getIdentifier() ==  subscription.getIdentifier()


    }

    def "Cancel subscription entry"() {

        given:
        Subscription subscription  =  new Subscription()
        Creator creator = new Creator()
        creator.setEmail("demo")
        creator.setUuid("demo")
        subscription.setCreator(creator)
        subscription.setIdentifier(UUID.randomUUID().toString())
        given(this.subscriptionRepository.saveAndFlush(subscription)).willReturn(subscription)

        when:
        def response =  subscriptionService.cancel(subscription)

        then:
        response.getStatus().toString() ==  "CANCELED"

    }

}
