package shop.seulmeal.web.purchase.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import shop.seulmeal.service.domain.Purchase;

@ToString
@Getter
@Setter
public class PurchaseDto {
	
	private String userId;
	private String name;
	private String address;
	private String phone;
	private String email;
	private String message;
	private int price;
	private String paymentCondition;
	private int usePoint;
	
	private List<Integer> customProductNo;
	
	public Purchase toDomain() {	
        return Purchase.builder()
                .name(name)
                .address(address)
                .phone(phone)
                .email(email)
                .message(message)
                .price(price)
                .paymentCondition(paymentCondition)
                .usePoint(usePoint)
                .build();
    }

}