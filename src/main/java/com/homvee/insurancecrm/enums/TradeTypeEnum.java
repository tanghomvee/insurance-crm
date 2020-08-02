package com.homvee.insurancecrm.enums;

/**
 * 有效标志
 *
 * @author ddyunf
 */
public enum TradeTypeEnum {
    CHARGE(1,"充值"),
	MATCH(2,"匹配消费");

	private Integer val;
	private String desc;

	public Integer getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	TradeTypeEnum(Integer val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static TradeTypeEnum getByVal(Integer val){
		for (TradeTypeEnum tmp : TradeTypeEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
