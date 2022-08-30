package shop.seulmeal.web.purchase.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomProductDto {
	
	private int count;
	private int price;
	private String cartStatus;
	
	List<Integer> minusNo;
	List<String> minusName;
	
	List<Integer> plusPartsNo;
	List<Integer> plusPrice;
	List<Integer> plusGram;

}
