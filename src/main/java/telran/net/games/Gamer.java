package telran.net.games;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;
@Entity
@Table(name = "gamer")
public class Gamer {
	@Id
	private String username;
	private LocalDate birthdate;
	public Gamer(String username, LocalDate birthdate) {
		this.username = username;
		this.birthdate = birthdate;
	}
	public Gamer() {
	}
	public String getUsername() {
		return username;
	}
	public LocalDate getBirthdate() {
		return birthdate;
	}
	@Override
	public int hashCode() {
		return Objects.hash(username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gamer other = (Gamer) obj;
		return Objects.equals(username, other.username);
	}
	
	
}
