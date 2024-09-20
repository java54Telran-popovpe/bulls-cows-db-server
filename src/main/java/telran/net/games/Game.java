package telran.net.games;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "game")

public class Game {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="date_time", nullable = true)
	private LocalDateTime dateTime;
	
	@Column(name="is_finished", nullable = false)
	private boolean isFinished;
	
	private String sequence;
	public Game(LocalDateTime dateTime, boolean isFinished, String sequence) {
		this.dateTime = dateTime;
		this.isFinished = isFinished;
		this.sequence = sequence;
	}
	public Game() {
	}
	public LocalDateTime getDate() {
		return dateTime;
	}
	public void setDate(LocalDateTime date) {
		this.dateTime = date;
	}
	public boolean isfinished() {
		return isFinished;
	}
	public void setfinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	public long getId() {
		return id;
	}
	public String getSequence() {
		return sequence;
	}
}
