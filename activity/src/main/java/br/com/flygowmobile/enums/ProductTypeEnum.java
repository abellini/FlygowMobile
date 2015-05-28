package br.com.flygowmobile.enums;



public enum ProductTypeEnum {
	FOOD("FOOD"),
	PROMOTION("PROMOTION")
	;

	private String name;

	ProductTypeEnum(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}
	
	public static ProductTypeEnum fromName(String name){
		if(ProductTypeEnum.FOOD.getName().toUpperCase().equals(name.toUpperCase())){
			return ProductTypeEnum.FOOD;
		}else if(ProductTypeEnum.PROMOTION.getName().toUpperCase().equals(name.toUpperCase())){
			return ProductTypeEnum.PROMOTION;
		}
		return null;
	}
}
