package com.ilkaygunel.springintegration;

import com.ilkaygunel.domain.Invoice;
import com.ilkaygunel.domain.LineItem;
import com.ilkaygunel.domain.Order;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class ReceiverApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ReceiverApplication.class).web(WebApplicationType.NONE).run(args);
	}

	@Transformer(inputChannel = "orderChannel", outputChannel = "invoiceChannel")
	Invoice convertToInvoice(Order order) {

		System.out.println("*****************************");
		System.out.println("..... Received an order .....");
		System.out.println("Order id = " + order.getId());
		System.out.println("Generating Invoice ..........");
		Invoice invoice = new Invoice();
		invoice.setDate(new Date());
		invoice.setOrder_id(order.getId());

		Double value    = 0.0;
		Double amount   = 0.0;
		Double tax_rate = 0.0;
		Double tax      = 0.0;

		List<LineItem> lineItems = order.getItems();
		Double lineItemPrice;

		for (LineItem lineItem : lineItems) {
			lineItemPrice = (double) (lineItem.getProduct().getPrice() * lineItem.getQty());

			// Books are taxed at 5%
			if (lineItem.getProduct().getType() == 'B')
				tax_rate = 0.05;
			// Perfumes are taxed at 8%
			else if (lineItem.getProduct().getType() == 'P')
				tax_rate = 0.08;

			tax = lineItemPrice * tax_rate;

			value  += lineItemPrice;
			amount += lineItemPrice + tax;
		}
		invoice.setValue(value);
		invoice.setAmount(amount);

		return invoice;
	}

	@Bean
	@ServiceActivator(inputChannel = "invoiceChannel")
	public MessageHandler handler() {
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {

				Invoice invoice = (Invoice) message.getPayload();
				System.out.println("Received Invoice ............");
				System.out.println("Order Id = " + invoice.getOrder_id());
				System.out.println("Value = " + invoice.getValue());
				System.out.println("Invoice Amount = " + invoice.getAmount());
			}
		};
	}
}