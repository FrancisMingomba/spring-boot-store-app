package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.PaymentStatus;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.repository.util.ClassUtils.ifPresent;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;



    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request){
        var cart = cartRepository.findById(request.getCartId()).orElse(null);
        if (cart == null) {
           throw new CartNotFoundException();
        }
        if (cart.isEmpty()) {
          throw new CartEmptyException();
        }

        var order =  Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        try {
            //Create a checkout session
          var session =  paymentGateway.createCheckoutSession(order);

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());

        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;

        }

    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
        .ifPresent( paymentResult -> {
            var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
            order.setStatus(paymentResult.getPaymentStatus());
            orderRepository.save(order);
        } );

    }
}
