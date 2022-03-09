package odev;

public class superSinif {
	private String sinifAdi;
	private int adet;
	
	public superSinif(String sinifAdi,int adet) {
		this.sinifAdi=sinifAdi;
		this.adet=adet;
	}
	
	public void setSinifAdi(String sinifAdi) {
		this.sinifAdi=sinifAdi;
	}
	public void setAdet(int adet) {
		this.adet=adet;
	}
	public String getSinifAdi() {
		return sinifAdi;
	}
	public int getAdet() {
		return adet;
	}
}