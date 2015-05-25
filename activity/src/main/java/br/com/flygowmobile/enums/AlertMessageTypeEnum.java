package br.com.flygowmobile.enums;



public enum AlertMessageTypeEnum {
	TO_PAYMENT(1L, "To Payment", "Attendant being called to request service on the table"),
	TO_ATTENDANCE(2L, "To Attendance", "Attendant being called to request payment on the table")
	;

	private Long id;
	private String name;
	private String description;
	
	AlertMessageTypeEnum(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public static AlertMessageTypeEnum fromId(Long id){
		for(AlertMessageTypeEnum type : values()){
			if(type.getId().equals(id)){
				return type;
			}
		}
		return null;
	}
}
