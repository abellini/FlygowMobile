package br.com.flygowmobile.enums;


public enum TabletStatusEnum {
	IN_ATTENDANCE(1, "In attendance", "In Attendance"),
	AVALIABLE(2, "Available", "Available"),
	UNAVALIABLE(3, "Unavailable", "Unavailable")
	;

	private int id;
	private String name;
	private String description;
	
	TabletStatusEnum(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public static TabletStatusEnum fromId(int id){
		if(TabletStatusEnum.IN_ATTENDANCE.getId()== id){
			return TabletStatusEnum.IN_ATTENDANCE;
		}else if (TabletStatusEnum.AVALIABLE.getId() == id){
			return TabletStatusEnum.AVALIABLE;
		}else if (TabletStatusEnum.UNAVALIABLE.getId() == id){
			return TabletStatusEnum.UNAVALIABLE;
		}
		return null;
	}
}
