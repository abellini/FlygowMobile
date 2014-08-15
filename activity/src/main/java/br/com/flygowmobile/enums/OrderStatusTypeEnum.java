package br.com.flygowmobile.enums;



public enum OrderStatusTypeEnum {
	OPENED(1),
	CLOSED(2)
	;

	private int id;

	OrderStatusTypeEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}


	public static OrderStatusTypeEnum fromId(int id){
		if(OrderStatusTypeEnum.OPENED.getId() == id){
			return OrderStatusTypeEnum.OPENED;
		}else if(OrderStatusTypeEnum.CLOSED.getId() == id){
			return OrderStatusTypeEnum.CLOSED;
		}
		return null;
	}
}
