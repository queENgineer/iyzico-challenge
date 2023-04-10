package com.iyzico.challenge.service;

import com.iyzico.challenge.model.entity.Payment;
import com.iyzico.challenge.model.entity.SeatEntity;
import com.iyzico.challenge.repository.PaymentRepository;
import com.iyzico.challenge.repository.SeatRepository;
import com.iyzipay.Options;
import com.iyzipay.model.Address;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import com.iyzipay.model.Buyer;
import com.iyzipay.model.Currency;
import com.iyzipay.model.PaymentCard;
import com.iyzipay.request.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class IyzicoPaymentService {

    private static final String API_KEY="sandbox-9Nka4mr7A1DxsSkdRUtBQUyDkzbsdPar";
    private static final String SECRET_KEY="sandbox-V91I9PxV0KSG0vakYWChCEK1BcPqrhrF";
    private static final String URL="https://sandbox-api.iyzipay.com";
    
    private BankService bankService;
    private SeatRepository seatRepository;
    private PaymentRepository paymentRepository;

    public synchronized Boolean paymentForSeat(String transactionId,PaymentServiceRequest paymentServiceRequest) throws ServiceUnavailableException {
      log.info("IyzicoPaymentService - paymentForSeat() start.");
      SeatEntity seatEntity= seatRepository.findSeatById(paymentServiceRequest.getSeatId());
      if(!ObjectUtils.isEmpty(seatEntity)){
          if(ObjectUtils.isEmpty(seatEntity.getCustomerId())){
    
              iyzicoPaymentProcess(seatEntity.getPrice());
              pay(seatEntity.getPrice());
              seatEntity.setCustomerId(paymentServiceRequest.getCustomerId());
              seatRepository.save(seatEntity);
              log.info("{} IyzicoPaymentService - paymentForSeat() end.",transactionId);
              return true;
          }else{
              log.error("{} IyzicoPaymentService - paymentForSeat() The seat with id:{} is already booked .",transactionId,paymentServiceRequest.getSeatId());
              throw new InvalidParameterException();
          }
      }else{
          log.error("{} IyzicoPaymentService - paymentForSeat() The seat with id:{} is not available.",transactionId,paymentServiceRequest.getSeatId());
          throw new InvalidParameterException();
      }
    }
    
    public void pay(BigDecimal price) {
        log.info("{} IyzicoPaymentService - payWithBank() start.");
        //pay with bank
        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(price);
        BankPaymentResponse response = bankService.pay(request);
        log.info("{} IyzicoPaymentService - payWithBank() end.");
    
        //insert records
        saveBankPaymentResponse(response,price);
    
    }
    
    
    
    public void iyzicoPaymentProcess(BigDecimal price) throws ServiceUnavailableException {
    
        log.info("{} IyzicoPaymentService - payWithIyzico() start.");
        Options options = getOptions();
    
        CreatePaymentRequest request = getCreatePaymentRequest(price);
        PaymentCard paymentCard = getPaymentCard();
        request.setPaymentCard(paymentCard);
    
        Buyer buyer = getBuyer();
        request.setBuyer(buyer);
    
        Address shippingAddress = getAddress();
        request.setShippingAddress(shippingAddress);
        
        Address billingAddress = getAddress();
        request.setBillingAddress(billingAddress);
        
        List<BasketItem> basketItems = new ArrayList<BasketItem>();
        BasketItem firstBasketItem = getBasketItem(price);
        basketItems.add(firstBasketItem);
        
        request.setBasketItems(basketItems);
        com.iyzipay.model.Payment payment = com.iyzipay.model.Payment.create(request, options);
        if(!payment.getStatus().equals("success")){
            log.info("{} IyzicoPaymentService - payWithIyzico() has an error. ErrorCode: {}, ErrorMessage: {}, ErrorGroup: {} ", payment.getErrorCode(),payment.getErrorMessage(),payment.getErrorGroup());
            throw new ServiceUnavailableException();
        }
        log.info("{} IyzicoPaymentService - payWithIyzico() end.");
    }
    
    private Options getOptions() {
        Options options = new Options();
        options.setApiKey(API_KEY);
        options.setSecretKey(SECRET_KEY);
        options.setBaseUrl(URL);
        return options;
    }
    
    private BasketItem getBasketItem(BigDecimal price) {
        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId(UUID.randomUUID().toString());
        firstBasketItem.setName("TICKET");
        firstBasketItem.setCategory1("Services");
        firstBasketItem.setItemType(BasketItemType.VIRTUAL.name());
        firstBasketItem.setPrice(price);
        return firstBasketItem;
    }
    
    private Address getAddress() {
        Address shippingAddress = new Address();
        shippingAddress.setContactName("Jane Doe");
        shippingAddress.setCity("Istanbul");
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        return shippingAddress;
    }
    
    private Buyer getBuyer() {
        Buyer buyer = new Buyer();
        buyer.setId(UUID.randomUUID().toString());
        buyer.setName("John");
        buyer.setSurname("Doe");
        buyer.setEmail("email@email.com");
        buyer.setIdentityNumber("74300864791");
        buyer.setRegistrationAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        buyer.setIp("85.34.78.112");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        return buyer;
    }
    
    private PaymentCard getPaymentCard() {
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName("John Doe");
        paymentCard.setCardNumber("5528790000000008");
        paymentCard.setExpireMonth("12");
        paymentCard.setExpireYear("2030");
        paymentCard.setCvc("123");
        return paymentCard;
    }
    
    private CreatePaymentRequest getCreatePaymentRequest(BigDecimal price) {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setPrice(price);
        request.setPaidPrice(price);
        request.setCurrency(Currency.TRY.name());
        request.setInstallment(1);
        return request;
    }
    
    @Transactional
    public void saveBankPaymentResponse(BankPaymentResponse response,BigDecimal price){
        log.info("IyzicoPaymentService - saveBankPaymentResponse() start.");
        Payment payment = new Payment();
        payment.setBankResponse(response.getResultCode());
        payment.setPrice(price);
        paymentRepository.save(payment);
        log.info("IyzicoPaymentService - saveBankPaymentResponse() end.");
    }
}
