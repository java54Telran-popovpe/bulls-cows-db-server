package telran.net.games;
import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;
@Entity
@Table(name="game_gamer")
@IdClass(GameGamer.EmbeddedCPK.class)
public class GameGamer {
	
	@Column(name="is_winner", nullable = false)
	private boolean isWinner;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "game_id")
	private Game game;
	@Id
	@ManyToOne
	@JoinColumn(name = "gamer_id")
	private Gamer gamer;
	
	
	public GameGamer() {
		
	}
	
	public GameGamer(boolean isWinner, Game game, Gamer gamer) {
		this.isWinner = isWinner;
		this.game = game;
		this.gamer = gamer;
	}
	
	@SuppressWarnings("serial")
	static class EmbeddedCPK implements Serializable {
		private Game game;
		private Gamer gamer;
		
		public Game getGame() {
			return game;
		}
		public void setGame(Game game) {
			this.game = game;
		}
		public Gamer getGamer() {
			return gamer;
		}
		public void setGamer(Gamer gamer) {
			this.gamer = gamer;
		}
		@Override
		public int hashCode() {
			return Objects.hash(game, gamer);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EmbeddedCPK other = (EmbeddedCPK) obj;
			return Objects.equals(game, other.game) && Objects.equals(gamer, other.gamer);
		}
		
	}

	public boolean isWinner() {
		return isWinner;
	}
	public Game getGame() {
		return game;
	}
	public Gamer getGamer() {
		return gamer;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}
	
	
}
