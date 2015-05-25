package br.com.flygowmobile.enums;


public enum OrderItemStatusEnum {
	OPENED(1),
	SENDED(2)
	;

	private int id;
	
	OrderItemStatusEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public OrderItemStatusEnum fromId(Long id){
		for(OrderItemStatusEnum orderItemStatusEnum : values()){
            if(orderItemStatusEnum.getId() == id){
                return orderItemStatusEnum;
            }
        }
		return null;
	}
}