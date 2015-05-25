package br.com.flygowmobile.enums;



public enum PositionsEnum {
	PRODUCT_DETAILS(120)
	;

	private int margin;

	PositionsEnum(int margin) {
		this.margin = margin;
	}

	public int getMargin() {
		return margin;
	}
}
