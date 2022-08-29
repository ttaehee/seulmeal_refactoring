package shop.seulmeal.service.domain;

import java.sql.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel
public class Purchase {
	@ApiModelProperty(example="682")
	private int purchaseNo;
	private User user;
	private int price;
	private String address;
	private String name;
	private String phone;
	private String email;
	private String message;
	@ApiModelProperty(example="4")
	private String purchaseStatus;
	private String status;
	private Date regDate;
	private String paymentCondition;
	@ApiModelProperty(example="imp_514829371298")
	private String imp_uid;
	@ApiModelProperty(example="200")
	private int amount;
	private int usePoint;
	
	private List<CustomProduct> customProduct;

}
