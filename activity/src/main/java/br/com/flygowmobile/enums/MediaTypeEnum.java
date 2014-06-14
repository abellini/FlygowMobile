package br.com.flygowmobile.enums;



public enum MediaTypeEnum {
	PHOTO((byte)1, "PHOTO"),
	VIDEO((byte)2, "VIDEO")
	;

	private Byte id;
	private String name;
	
	MediaTypeEnum(Byte id, String name) {
		this.id = id;
		this.name = name;
	}

	public Byte getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public static MediaTypeEnum fromId(byte id){
		if(MediaTypeEnum.PHOTO.getId().equals(id)){
			return MediaTypeEnum.PHOTO;
		}else if(MediaTypeEnum.VIDEO.getId().equals(id)){
			return MediaTypeEnum.VIDEO;
		}
		return null;
	}
	
	public static MediaTypeEnum fromName(String code){
		if(MediaTypeEnum.PHOTO.getName().toUpperCase().equals(code.toUpperCase())){
			return MediaTypeEnum.PHOTO;
		}else if(MediaTypeEnum.VIDEO.getName().toUpperCase().equals(code.toUpperCase())){
			return MediaTypeEnum.VIDEO;
		}
		return null;
	}
}
