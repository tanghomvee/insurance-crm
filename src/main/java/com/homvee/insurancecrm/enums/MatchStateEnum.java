package com.homvee.insurancecrm.enums;

/**
 * 有效标志
 *
 * @author ddyunf
 */
public enum MatchStateEnum {
	MATCHED("MATCHED","已经匹配"),
	MISMATCH("MISMATCH","不匹配");

	private String val;
	private String desc;

	public String getVal() {
		return val;
	}

	public String getDesc() {
		return desc;
	}

	MatchStateEnum(String val, String desc) {
		this.val = val;
		this.desc = desc;
	}


	public  static MatchStateEnum getByVal(Integer val){
		for (MatchStateEnum tmp : MatchStateEnum.values()){
			if(tmp.val.equals(val)){
				return tmp;
			}
		}
		return null;
	}

}
