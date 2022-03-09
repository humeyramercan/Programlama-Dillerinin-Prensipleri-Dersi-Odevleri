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
				classBasi = i;//class kelimesi olan satýr class baþýdýr
				String classAd;

				// kalýtým almayan sýnýflar
				if (!suankiSatir.contains(":")) {
					classAd = suankiSatir.substring(suankiSatir.indexOf("class") + 5);
					classAd = classAd.replace("{", "");
					classAdi.add(classAd);
					System.out.println("Sýnýf: " + classAd);

				} else {// satirda class varsa ve : içeriyorsa kalýtým almýþtýr demektir

					// class kelimesinin sonundan : ya kadar olan kýsým sýnýf adýmýzdýr. Onu
					// alýyoruz ve alýrken tüm boþluklarý temizliyoruz.
					classAd = suankiSatir.substring(suankiSatir.indexOf("class") + 5, suankiSatir.indexOf(':'));							
					classAdi.add(classAd);
					System.out.println("Sýnýf: " + classAd);
					// satirin : dan sonra gelen kýsmý aldýk ve bu kýsýmdan public eriþim
					// belirtecini ve { karakterini çýkardýk

					String ikiNoktaSonrasi = suankiSatir.substring(suankiSatir.indexOf(':') + 1);

					ikiNoktaSonrasi = ikiNoktaSonrasi.replaceAll("public", "");
					ikiNoktaSonrasi = ikiNoktaSonrasi.replace("{", "");
					
					//: dan sonrasýný düzenledikten sonra , a göre böldük
					String[] siniflar = ikiNoktaSonrasi.split(",");
					//bu dizideki süper sýnýflarý listeye ekleyeceðim.Eklerken bu süper sýnýf listede eðer önceden varsa, onun sadece adetini 1 artýracaðým.
					//Yoksa bir superSinif nesnesi oluþturup bu nesnenin adýna dizideki elemaný vereceðim adetini de 1 yapacaðým
					//Liste boþ ise dizideki iilk elemaný direkt nesne oluþturup listeye ekleyeceðim
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

				
				// enum yada struct varsa onlarýn da sonu }; ile ve class sonu da }; bittiðinden class sonu ile karýþmamasý için
				// satýrda enum yada struct kelimesi varsa okumaya devam ettik ve }; gödüðümüzde continue diyerek satýr okumaya devam ettik çünkü bu }; class deðil enum yada struct sonudur
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
				//class baþý ve sonu arasýnda public alaný buluyoruz.public: ifadesi geçen satýr benim public baþýmdýr.
				//public kelimesinden sonra protected yada private kelimesine rastlarsam o satýr public sonu olur
				//rastlamazsam demekki class sonuna kadar public alandýr. Class sonu ile public sonu aynýdýr
				for (int k = classBasi; k <= classSonu; k++) {
					String suankiSatir2 = satirlar.get(k).replaceAll(" ", "");
					if (suankiSatir2.contains("public:")) {
						publicBasi = k;
						for (int z = k; z <= classSonu;) { // public baþýndan class sonuna kadar gidiyoruz eðer
																// protected veya private alan varsa oraya kadar public
																// alandýr yoksa class sonuna kadar public alandayýz
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
				
				//public baþý ve sonunu bulduðuma göre bütün iþlemlerimi burada yapabiilirim artýk
				
				for (int m = publicBasi; m <= publicSonu; m++) {
					String satiR = satirlar.get(m).replaceAll("\\s+", " ").trim();
					String boslukTemizlenmisSatir = satiR.replaceAll(" ", "");
					// geri dönüþ tipi olan(void,double,string,int..) ve kurucu yýkýcý metotlar kontrolü

					if (!satiR.contains("friend")) {

						// kurucu ve yýkýcý method kontrolü

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

						} // yýkýcý
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
					} else { // friend method ve ifstream ostream tür kontrolü

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
		System.out.println("Super Sýnýflar:");
		for(int y=0;y<superSiniflar.size();y++) {
			System.out.println("	"+superSiniflar.get(y).getSinifAdi()+":"+superSiniflar.get(y).getAdet());
		}

	}
}
