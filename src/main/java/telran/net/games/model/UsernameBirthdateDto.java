package telran.net.games.model;

import java.time.LocalDate;

import org.json.JSONObject;

public record UsernameBirthdateDto(String username, LocalDate birthdate) {
	
	private static final String USERNAME_FIELD_NAME = "username";
	private static final String BIRTHDATE_FIELD_NAME = "birthdate";

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(USERNAME_FIELD_NAME, username);
		jsonObject.put(BIRTHDATE_FIELD_NAME, birthdate);
		return jsonObject.toString();
	}
	
	public UsernameBirthdateDto(JSONObject jsonObject) {
		this(jsonObject.getString(USERNAME_FIELD_NAME),
				LocalDate.parse( jsonObject.getString(BIRTHDATE_FIELD_NAME) ));
	}
}
