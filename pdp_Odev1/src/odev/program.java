package odev;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;

import java.util.List;

public class program {

	public static void main(String[] args) throws IOException {
		
	
		BufferedReader inputStream = null;

		List<String> satirlar = new ArrayList<>();
		List<String> classAdi = new ArrayList<>();
		List<superSinif> superSiniflar=new ArrayList<>();
		

		
		int classBasi = 0;
		int classSonu = 0;
		int publicBasi = 0;
		int publicSonu = 0;

		String satir="";
		String fileName = "src/program.cpp";

		try {

			inputStream = new BufferedReader(new FileReader(fileName));

			while ((satir = inputStream.readLine()) != null) {
				satirlar.add(satir);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} finally {

			if (inputStream != null) {
				inputStream.close();
			}
		}

		for (int i = 0; i < satirlar.size(); i++) {
			String suankiSatir = satirlar.get(i).replaceAll(" ","");

			if (suankiSatir.contains("class")) {
				classBasi = i;//class kelimesi olan sat�r class ba��d�r
				String classAd;

				// kal�t�m almayan s�n�flar
				if (!suankiSatir.contains(":")) {
					classAd = suankiSatir.substring(suankiSatir.indexOf("class") + 5);
					classAd = classAd.replace("{", "");
					classAdi.add(classAd);
					System.out.println("S�n�f: " + classAd);

				} else {// satirda class varsa ve : i�eriyorsa kal�t�m alm��t�r demektir

					// class kelimesinin sonundan : ya kadar olan k�s�m s�n�f ad�m�zd�r. Onu
					// al�yoruz ve al�rken t�m bo�luklar� temizliyoruz.
					classAd = suankiSatir.substring(suankiSatir.indexOf("class") + 5, suankiSatir.indexOf(':'));							
					classAdi.add(classAd);
					System.out.println("S�n�f: " + classAd);
					// satirin : dan sonra gelen k�sm� ald�k ve bu k�s�mdan public eri�im
					// belirtecini ve { karakterini ��kard�k

					String ikiNoktaSonrasi = suankiSatir.substring(suankiSatir.indexOf(':') + 1);

					ikiNoktaSonrasi = ikiNoktaSonrasi.replaceAll("public", "");
					ikiNoktaSonrasi = ikiNoktaSonrasi.replace("{", "");
					
					//: dan sonras�n� d�zenledikten sonra , a g�re b�ld�k
					String[] siniflar = ikiNoktaSonrasi.split(",");
					//bu dizideki s�per s�n�flar� listeye ekleyece�im.Eklerken bu s�per s�n�f listede e�er �nceden varsa, onun sadece adetini 1 art�raca��m.
					//Yoksa bir superSinif nesnesi olu�turup bu nesnenin ad�na dizideki eleman� verece�im adetini de 1 yapaca��m
					//Liste bo� ise dizideki iilk eleman� direkt nesne olu�turup listeye ekleyece�im
					for(int t=0;t<siniflar.length;t++) {
						
						if(superSiniflar.size()==0) {
							superSinif sS=new superSinif(siniflar[t],1);	
							superSiniflar.add(sS);
							continue;

						}else {
						for(int p=0;p<superSiniflar.size();p++) {						
							if(superSiniflar.get(p).getSinifAdi().matches(siniflar[t])) {
								superSiniflar.get(p).setAdet(superSiniflar.get(p).getAdet()+1);
								break;
								
							}else if(!superSiniflar.get(p).getSinifAdi().matches(siniflar[t])&&p!=superSiniflar.size()-1) {
								continue;
							}
							else {
								superSinif SS=new superSinif(siniflar[t],1);
								superSiniflar.add(SS);
								break;
							}
														
							}
							
						}
					}
				

				}

				
				// enum yada struct varsa onlar�n da sonu }; ile ve class sonu da }; bitti�inden class sonu ile kar��mamas� i�in
				// sat�rda enum yada struct kelimesi varsa okumaya devam ettik ve }; g�d���m�zde continue diyerek sat�r okumaya devam ettik ��nk� bu }; class de�il enum yada struct sonudur
				for (int g = i; g < satirlar.size(); g++) {
					satirlar.get(g).replaceAll(" ", "");
					if (satirlar.get(g).contains("enum")||satirlar.get(g).contains("struct")) {
						for (int f = g; f < satirlar.size(); f++) {
							if (satirlar.get(f).contains("};")) {
								g = f + 1;
								break;
							}
						}
					}
					if (satirlar.get(g).contains("};")&&satirlar.get(g).contains("class")) {
						classSonu = g;
						break;
					}else if(satirlar.get(g).contains("};")&&!satirlar.get(g).contains("class")) {
						classSonu = g;
						break;
					}
				}

				// public: alan tespiti. 
				//class ba�� ve sonu aras�nda public alan� buluyoruz.public: ifadesi ge�en sat�r benim public ba��md�r.
				//public kelimesinden sonra protected yada private kelimesine rastlarsam o sat�r public sonu olur
				//rastlamazsam demekki class sonuna kadar public aland�r. Class sonu ile public sonu ayn�d�r
				for (int k = classBasi; k <= classSonu; k++) {
					String suankiSatir2 = satirlar.get(k).replaceAll(" ", "");
					if (suankiSatir2.contains("public:")) {
						publicBasi = k;
						for (int z = k; z <= classSonu;) { // public ba��ndan class sonuna kadar gidiyoruz e�er
																// protected veya private alan varsa oraya kadar public
																// aland�r yoksa class sonuna kadar public alanday�z
																// demektir
							String suankiSatir3 = satirlar.get(z).replaceAll(" ", "");
							if (suankiSatir3.contains("protected:") || suankiSatir3.contains("private:")) {
								publicSonu = k;								
								break;
							} else {
								publicSonu = classSonu;								
								break;
							}

						}
					}
				}
				
				//public ba�� ve sonunu buldu�uma g�re b�t�n i�lemlerimi burada yapabiilirim art�k
				
				for (int m = publicBasi; m <= publicSonu; m++) {
					String satiR = satirlar.get(m).replaceAll("\\s+", " ").trim();
					String boslukTemizlenmisSatir = satiR.replaceAll(" ", "");
					// geri d�n�� tipi olan(void,double,string,int..) ve kurucu y�k�c� metotlar kontrol�

					if (!satiR.contains("friend")) {

						// kurucu ve y�k�c� method kontrol�

						// kurucu
						if (boslukTemizlenmisSatir.contains(classAd + "(") && !satiR.contains("new")
								&& !boslukTemizlenmisSatir.contains("~" + classAd + "(")) {
							String methodAdi = satiR.substring(0, satiR.indexOf("("));
							System.out.println("	" + methodAdi);
							String butunParametreler = satiR.substring(satiR.indexOf("(") + 1, satiR.indexOf(")"));
							String parametreVarMi = butunParametreler.replaceAll(" ", "");
							if (parametreVarMi.matches("")) {
								System.out.println("		Parametre: 0 ");
								System.out.println("		Donus Tipi: Nesne Adresi");
							} else {
								String[] ayrilmisParametreler = butunParametreler.split(",");
								List<String> parametreler = new ArrayList<>();
								String duzenliParametreler = "";
								for (String f : ayrilmisParametreler) {
									if (f.contains("*")) {
										String pointerParametre = f.replaceAll(" ", "");
										pointerParametre = pointerParametre.substring(0,
												pointerParametre.indexOf("*") + 1);
										parametreler.add(pointerParametre);

									} else {
										String[] z = f.split(" ");
										parametreler.add(z[0]);
									}
								}
								for (String f : parametreler) {
									duzenliParametreler += f + ",";

								}
								System.out.println("		Parametre: " + parametreler.size() + " ("
										+ duzenliParametreler.replaceAll(".$", "") + ")");
								System.out.println("		Donus Tipi: Nesne Adresi");
							}

						} // y�k�c�
						if (boslukTemizlenmisSatir.contains("~" + classAd + "(")) {
							String methodAdi = boslukTemizlenmisSatir.substring(boslukTemizlenmisSatir.indexOf("~"),
									boslukTemizlenmisSatir.indexOf("("));
							System.out.println("	" + methodAdi);
							System.out.println("		Donus Tipi: Void");

						}

						// donus tipi olan methodlar

						Pattern regexFonksiyonIsim = Pattern.compile(
								"([a-zA-Z0-9<>._?,* ]+) +([a-zA-Z0-9_]+) *\\([a-zA-Z0-9<>\\[\\]._?,* \n]*\\) *([a-zA-Z0-9_ *,\n]*) *\\{");
						Matcher matcher = regexFonksiyonIsim.matcher(satiR);

						while (matcher.find()) {
							String donusTipi = "";
							String metotAdi = "";
							String donusTuruPointerMi = satiR.substring(0, satiR.indexOf("(") + 1);
							if (donusTuruPointerMi.contains("*")) {
								donusTuruPointerMi = donusTuruPointerMi.replaceAll(" ", "");
								donusTipi = donusTuruPointerMi.substring(0, donusTuruPointerMi.indexOf("*") + 1);
								metotAdi = donusTuruPointerMi.substring(donusTuruPointerMi.indexOf("*") + 1,
										donusTuruPointerMi.indexOf("("));
							} else {

								metotAdi = satiR.substring(satiR.indexOf(" "), satiR.indexOf("("));
								donusTipi = satiR.substring(0, satiR.indexOf(" "));
								System.out.println("	" + metotAdi);
							}
							String butunParametreler = satiR.substring(satiR.indexOf("(") + 1, satiR.indexOf(")"));
							String parametreVarMi = butunParametreler.replaceAll(" ", "");
							if (parametreVarMi.matches("")) {
								System.out.println("		Parametre: 0");
								System.out.println("		Donus Tipi: " + donusTipi);
							} else {
								String[] ayrilmisParametreler = butunParametreler.split(",");
								List<String> parametreler = new ArrayList<>();
								String duzenliParametreler = "";
								for (String f : ayrilmisParametreler) {

									if (f.contains("*")) {
										String pointerParametre = f.replaceAll(" ", "");
										pointerParametre = pointerParametre.substring(0,
												pointerParametre.indexOf("*") + 1);
										parametreler.add(pointerParametre);
									} else {
										String[] z = f.split(" ");
										parametreler.add(z[0]);
									}
								}
								for (String f : parametreler) {
									duzenliParametreler += f + ",";
								}
								System.out.println("		Parametre: " + parametreler.size() + " ("
										+ duzenliParametreler.replaceAll(".$", "") + ")");
								System.out.println("		Donus Tipi: " + donusTipi);
							}

						}
					} else { // friend method ve ifstream ostream t�r kontrol�

						String methodAdi = satiR.substring(satiR.indexOf("operator"), satiR.indexOf("("));
						methodAdi = methodAdi.replaceAll(" ", "");
						System.out.println("	" + methodAdi);
						String butunParametreler = satiR.substring(satiR.indexOf("("), satiR.indexOf(")"));
						String[] ayrilmisParametreler = butunParametreler.split(",");
						System.out.println("		Parametre: " + ayrilmisParametreler.length);
						String donusTipi = satiR.substring(satiR.indexOf("friend") + 6, satiR.indexOf("operator"));
						donusTipi = donusTipi.replaceAll(" ", "");
						System.out.println("		Donus Tipi: " + donusTipi);

					}

				}

			}

		}
		System.out.println("Super S�n�flar:");
		for(int y=0;y<superSiniflar.size();y++) {
			System.out.println("	"+superSiniflar.get(y).getSinifAdi()+":"+superSiniflar.get(y).getAdet());
		}

	}
}
