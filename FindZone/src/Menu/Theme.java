package Menu;

public class Theme {

	private String nom , url , auteur ;
	int id;

	public Theme(String nom, String url ,String auteur, String string) {
		this.nom = nom;
		this.url = url;
		this.auteur = auteur;
		this.id = Integer.parseInt(string);
		
	}
	public int getId() {
		return id;
	}
	public String getNom() {
		return nom;
	}
	public String getUrl() {
		return url;
	}

	public String getAuteur() {
		return auteur;
	}
	
}
