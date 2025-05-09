package me.miki.shindo.features.profile;

public enum ProfileType {
	ALL(0, "ALL"), FAVORITE(1, "FAVORITE");
	
	private int id;
	private String name;
	
	private ProfileType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	public static ProfileType getTypeById(int id) {
		
		for(ProfileType type : ProfileType.values()) {
			if(type.getId() == id) {
				return type;
			}
		}
		
		return ProfileType.ALL;
	}
}
