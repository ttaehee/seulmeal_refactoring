package shop.seulmeal.service.domain;

import java.sql.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Setter
@Getter
public class CustomProduct {
	private int customProductNo;
	private User user;
	private Product product;
	private int purchaseNo;
	private int count;
	private int price;
	private String cartStatus;
	private Date regDate;
	
	private List<CustomParts> minusParts; //제외재료 
	private List<CustomParts> plusParts; //추가재료 
	
	@Builder
	public CustomProduct(int count, int price, String cartStatus) {
		this.count = count;
		this.price = price;
		this.cartStatus = cartStatus;
	}
}
