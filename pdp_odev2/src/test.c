#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "hisse.h"
#include "portfoy.h"
#include "emir.h"
#include "banka.h"
void istenmeyenleriTemizle(char *ifade)
{
    const char *temp = ifade;
    do
    {
        while (*temp == ' ' || *temp == ':' || *temp == '\"' || *temp == '}')
        {
            ++temp;
        }
    } while (*ifade++ = *temp++);
}
int countOfLinesFromFile(char *filename)
{
    FILE *myfile = fopen(filename, "r");
    int ch, number_of_lines = 0;
    do
    {
        ch = fgetc(myfile);
        if (ch == '\n')
            number_of_lines++;
    } while (ch != EOF);
    if (ch != '\n' && number_of_lines != 0)
        number_of_lines++;
    fclose(myfile);
    return number_of_lines;
}
int main(void)
{
    FILE *fp1, *fp2, *fp3, *fp4, *fp5, *fp6;
    char *line1 = NULL;
    char *line2 = NULL;
    char *line3 = NULL;
    char *line4 = NULL;
    char *line5 = NULL;
    char *line6 = NULL;
    size_t len1 = 0;
    size_t len2 = 0;
    size_t len3 = 0;
    size_t len4 = 0;
    size_t len5 = 0;
    size_t len6 = 0;
    size_t read1, read2, read3, read4, read5, read6;
    int sayac = 0;

    double KARZARAR = 0;

    int portfoySatirSayisi = countOfLinesFromFile("doc/portfoy.json");
    portfoySatirSayisi = portfoySatirSayisi - 2;
   
//ilk olarak hangi emir verildi öğrenmek için emirler.json dosyasını satır satır okuyoruz
    fp3 = fopen("doc/emirler.json", "r");
    if (fp3 == NULL)
        exit(EXIT_FAILURE);
devam:
    while ((read3 = getline(&line3, &len3, fp3)) != -1)
    {
        //her satırı ,'e göre ayırıyourz
        char delim3[] = ",";

        char *ptr3 = strtok(line3, delim3);
        char *emirlerSembol, *emirlerIslem, *emirlerAdet;

        while (ptr3 != NULL)
        {
            //bu ayırdığımız stringlerde eğer ki Sembol, Islem ve Adet kelimeleri geçiyorsa 
            if (strstr(ptr3, "Sembol") != NULL)
            { //bu string içerisindeki : krakterinin olduğu yerden itibaren yeni string değerlerini değişkenlere atıyoruz. Bu değişkenlerin hepsinin başlangıç karakteri : 'dır
                emirlerSembol = strstr(ptr3, ":");
            }
            if (strstr(ptr3, "Islem") != NULL)
            {//örneğin bu satırda emirlerIslem değişkenine atanacak bir değer : "SATIS"  gibi bir değer olacaktır
                emirlerIslem = strstr(ptr3, ":");
            }
            if (strstr(ptr3, "Adet") != NULL)
            {
                emirlerAdet = strstr(ptr3, ":");
            }

            ptr3 = strtok(NULL, delim3);
        }//eğer ki bu stringlerden herhangi birinin başlangıç karakteri : ise ve boş değilse  istenmeyen karakterleri temizliyoruz 
        if (emirlerIslem != NULL && *emirlerIslem == ':')
        {
            
            istenmeyenleriTemizle(emirlerSembol);
            istenmeyenleriTemizle(emirlerIslem);
            istenmeyenleriTemizle(emirlerAdet);
            int adet = atoi(emirlerAdet);
        //artık gerekli bilgileri aldık, istenmeyen karakterleri temizledik, cast işlemlerini yaptık. Emir yapııs oluşturup bu değerleri parametre olarak veriyoruz
            Emir emir = EmirOlustur(emirlerSembol, emirlerIslem, adet);
            if (strcmp(emir->islem, "SATIS") == 0)
            {//eğer ki işlem satış ise bize güncel fiyat hesabı için hisselerdeki fiyat gerek bu sebeple hisseler.json dosyasını satır satır okuyoruz

                fp1 = fopen("doc/hisseler.json", "r");
                if (fp1 == NULL)
                    exit(EXIT_FAILURE);

                while ((read1 = getline(&line1, &len1, fp1)) != -1)
                {//yine aynı parçalama işlemini bu dosya için de yapıyourz

                    char delim1[] = ",";

                    char *ptr1 = strtok(line1, delim1);
                    char *hisselerSembol, *hisselerFiyat;

                    while (ptr1 != NULL)
                    {

                        if (strstr(ptr1, "Sembol") != NULL)
                        {
                            hisselerSembol = strstr(ptr1, ":");
                        }
                        if (strstr(ptr1, "Fiyat") != NULL)
                        {//örneğin burada hisselerFiyat değişkenine atanacak değer : 4.87} gibi bir değer olacaktır
                            hisselerFiyat = strstr(ptr1, ":");
                        }

                        ptr1 = strtok(NULL, delim1);
                    }
                    
                    if (hisselerFiyat != NULL && *hisselerFiyat == ':')
                    {   
                        //ilgili değerleri aldıktan sonra istenmeyen karakterleri temizliyoruz ve cast işlemlerini yapıyoruz
                        istenmeyenleriTemizle(hisselerSembol);

                        istenmeyenleriTemizle(hisselerFiyat);
                        double fiyat;
                        sscanf(hisselerFiyat, "%lf", &fiyat);
                       
                        //eğer ki verdiğimiz emrin sembolü ile elde hisselerden elde ettiğimiz sembol eşit ise doğru hisseyi bulmuşuzdur
                        if (strcmp(emir->sembol, hisselerSembol) == 0)
                        {  //artık hisse yapısını oluşturup elde ettiğimiz değerleri parametre olarak veriyoruz
                        Hisse hisse = HisseOlustur(hisselerSembol, fiyat);
                        //maliyet hesabı için de portfoydeki maliyet bilgisi gerekli olduğu için portfoy.json dosyasını satır satır okuyoruz
                            fp2 = fopen("doc/portfoy.json", "r");
                            if (fp2 == NULL)
                                exit(EXIT_FAILURE);

                            while ((read2 = getline(&line2, &len2, fp2)) != -1)
                            {
                                //aynı parçalama işlemini yine yapıyourz
                                char delim2[] = ",";

                                char *ptr2 = strtok(line2, delim2);
                                char *portfoySembol, *portfoyMaliyet, *portfoyAdet;

                                while (ptr2 != NULL)
                                {

                                    if (strstr(ptr2, "Sembol") != NULL)
                                    {
                                        portfoySembol = strstr(ptr2, ":");
                                    }
                                    if (strstr(ptr2, "Maliyet") != NULL)
                                    {//örneğin burada portfoyMaliyet değişkenine atanacak değer : 3.15 benzeri bir değer olacaktır
                                        portfoyMaliyet = strstr(ptr2, ":");
                                    }
                                    if (strstr(ptr2, "Adet") != NULL)
                                    {
                                        portfoyAdet = strstr(ptr2, ":");
                                    }

                                    ptr2 = strtok(NULL, delim2);
                                }

                                if (portfoyMaliyet != NULL && *portfoyMaliyet == ':')
                                {
                                    istenmeyenleriTemizle(portfoySembol);
                                    istenmeyenleriTemizle(portfoyMaliyet);
                                    double maliyet;
                                    sscanf(portfoyMaliyet, "%lf", &maliyet);
                                    istenmeyenleriTemizle(portfoyAdet);
                                    int adet = atoi(portfoyAdet);

                                    //eğer ki verdiğimiz emrin sembolü ile portfoy sembolü aynı ise ve satış emri için girilen adet portfoydeki adete eşit veya küçük ise artık satış işlemi yapılır
                                    if (strcmp(emir->sembol, portfoySembol) == 0 && emir->adet <= adet)
                                    {//artık portfoy yapıısına elde ettiğimiz bilgileri atıyoruz
                                        Portfoy portfoy = PortfoyOlustur(portfoySembol, maliyet, adet);
                                        //oluşturduğumuz banka yapısına elde ettiğimiz tüm bilgileri parametre olarak veriyoruz
                                        Banka banka = BankaOlustur(emir->sembol, emir->adet, hisse->fiyat, portfoy->adet, portfoy->maliyet);
                                        //ve bu banka yapısının satis metodu sayesinde satis işlemini gerçekleştiriyoruz
                                         banka->satis(banka);
                                        //sonrasında oluşturduğumuz tüm yapıları yokediyoruz
                                        emir->emirYokEt(emir);
                                        hisse->hisseYoket(hisse);
                                        portfoy->portfoyYoket(portfoy);
                                        banka->bankaYokEt(banka);
                                        fclose(fp2);
                                        fclose(fp1);
                                        free(fp2);
                                        free(fp1);
                                    }
                                }
                            }
                        }
                    }
                }
            }//eğer ki yapılacak işlem alış ise
            if (strcmp(emir->islem, "ALIS") == 0)
            {
                sayac = 0; //bu sayac portfoy dosyasındaki tüm portfoyleri okuyup okumadığımızı kontrol etmek için oluşturulmuştur
                //öncelikle alış işlemi yeni bir alım mı yoksa portföyde olup güncellenecek bir alım mı bunu belirlemek için portfoy.json dosyasını okuyoruz
                fp5 = fopen("doc/portfoy.json", "r");
                if (fp5 == NULL)
                    exit(EXIT_FAILURE);

                while ((read5 = getline(&line5, &len5, fp5)) != -1)
                {//parçalama işlemleri yine birebir aynıdır

                    char delim2[] = ",";

                    char *ptr2 = strtok(line5, delim2);
                    char *portfoySembol, *portfoyMaliyet, *portfoyAdet;

                    while (ptr2 != NULL)
                    {

                        if (strstr(ptr2, "Sembol") != NULL)
                        {
                            portfoySembol = strstr(ptr2, ":");
                        }
                        if (strstr(ptr2, "Maliyet") != NULL)
                        {
                            portfoyMaliyet = strstr(ptr2, ":");
                        }
                        if (strstr(ptr2, "Adet") != NULL)
                        {
                            portfoyAdet = strstr(ptr2, ":");
                        }

                        ptr2 = strtok(NULL, delim2);
                    }
                    //bu if içerisine girmişsek demekki biz dosyamızdaki ilk satır gibi anlamsız satırda değil de gerçekten portfoy bilgilerinin olduğu satırlardayız demektir
                    if (portfoyMaliyet != NULL && *portfoyMaliyet == ':')
                    { //yukarıda portfoy dosyasındaki portfoy bilgilerinin olduğu satır sayısını bulduk. Bu if içerisinde her seferinde sayacı 1 artırarak sonrasında bu satır sayısı ile karşılaştırcağız. Eğer eşitlerse demekki biz butun portfoy bilgisi içeren satırların sonuna gelmişizdir.
                        sayac = sayac + 1;

                        istenmeyenleriTemizle(portfoySembol);
                        istenmeyenleriTemizle(portfoyMaliyet);
                        double maliyet;
                        sscanf(portfoyMaliyet, "%lf", &maliyet);
                        istenmeyenleriTemizle(portfoyAdet);
                        int adet = atoi(portfoyAdet);
                        //bu portfoy değişkenini if içerisinde oluşturmadık çünkü else durumu için de kullanacağız
                        Portfoy portfoy = PortfoyOlustur(portfoySembol, maliyet, adet);

                        if (strcmp(portfoy->sembol, emir->sembol) == 0)
                        {//eğer ki portfoy ile emrimizin sembolleri eşitse biz portfoyde olan bir firmanın bilgilerini güncelleyeceğiz demektir
                           //güncel fiyat için hisseler.json dosyasındaki fiyat bilgisi gerekli olduğundan hisseler.json dosyasını okuyoruz
                            fp4 = fopen("doc/hisseler.json", "r");
                            if (fp4 == NULL)
                                exit(EXIT_FAILURE);

                            while ((read4 = getline(&line4, &len4, fp4)) != -1)
                            {
                                //yine aynı parçalama işlemlerini yapıyourz
                                char delim1[] = ",";

                                char *ptr1 = strtok(line4, delim1);
                                char *hisselerSembol, *hisselerFiyat;

                                while (ptr1 != NULL)
                                {

                                    if (strstr(ptr1, "Sembol") != NULL)
                                    {
                                        hisselerSembol = strstr(ptr1, ":");
                                    }
                                    if (strstr(ptr1, "Fiyat") != NULL)
                                    {
                                        hisselerFiyat = strstr(ptr1, ":");
                                    }

                                    ptr1 = strtok(NULL, delim1);
                                }

                                if (hisselerSembol != NULL && *hisselerSembol == ':')
                                {
                                    istenmeyenleriTemizle(hisselerSembol);
                                    istenmeyenleriTemizle(hisselerFiyat);
                                    double fiyat;
                                    sscanf(hisselerFiyat, "%lf", &fiyat);
                                    //eğer ki portfoydeki sembol ile hissselerdeki sembol eşit ise artık aynı sembol için bütün bilgileri edinmiş olduk. Alış işlemini yapabiliriz
                                    if (strcmp(hisselerSembol, portfoy->sembol) == 0)
                                    {   //hisse yapımızı oluşturduktan sonra banka yapımızı oluşturup gerekli tüm bilgileri verdik
                                        Hisse hisse = HisseOlustur(hisselerSembol, fiyat);
                                        Banka banka = BankaOlustur(emir->sembol, emir->adet, hisse->fiyat, portfoy->adet, portfoy->maliyet);
                                     //bankanın alış metodunu çağırdık ve ikinci parametresine 1 verdik. Yeni alım değil de portfoyde olup da güncellenecek bir alım yapıyorsak bu parametreye 1 veriyoruz. Metot bu şekilde ayarlandı
                                        banka->alis(banka, 1);

                                        emir->emirYokEt(emir);
                                        portfoy->portfoyYoket(portfoy);
                                        hisse->hisseYoket(hisse);
                                        banka->bankaYokEt(banka);
                                        fclose(fp4);
                                        fclose(fp5);
                                        free(fp4);
                                        free(fp5);
                                        goto devam;
                                    }
                                }
                            }
                        }
                        else
                        {//eğer ki portfoy.json dosyamızdaki ilk ve son satır hariç satır satısı ile yani portfoy bilgisi olan satır sayısı ile sayacımız eşitse demekki biz tüm portfoy bilgisi olan satırların sonuna gelmişizdir 
                        //ve buna rağmen hala emirlerdeki sembol ile portfoydeki sembol birbirine eşit değilse demekki biz yeni bir alım yapacağız
                            if (sayac == portfoySatirSayisi)
                            {
                                //güncel fiyat hesabı için hisselerdeki adet bilgisini almamız gerek. hisseler.json dosyasını okuyoruz
                                fp6 = fopen("doc/hisseler.json", "r");
                                if (fp6 == NULL)
                                    exit(EXIT_FAILURE);

                                while ((read6 = getline(&line6, &len6, fp6)) != -1)
                                {
                                    //aynı parçalama işlemleri yine yapılıyor
                                    char delim1[] = ",";

                                    char *ptr1 = strtok(line6, delim1);
                                    char *hisselerSembol, *hisselerFiyat;

                                    while (ptr1 != NULL)
                                    {

                                        if (strstr(ptr1, "Sembol") != NULL)
                                        {
                                            hisselerSembol = strstr(ptr1, ":");
                                        }
                                        if (strstr(ptr1, "Fiyat") != NULL)
                                        {
                                            hisselerFiyat = strstr(ptr1, ":");
                                        }

                                        ptr1 = strtok(NULL, delim1);
                                    }

                                    if (hisselerSembol != NULL && *hisselerSembol == ':')
                                    {
                                        istenmeyenleriTemizle(hisselerSembol);
                                        istenmeyenleriTemizle(hisselerFiyat);
                                        double fiyat;
                                        sscanf(hisselerFiyat, "%lf", &fiyat);
                                       
                                    //verilen emir ile okunan hissenin sembolü aynı ise artık aynı sembol için gerekli tüm bilgileri edinmiş olduk. Alış işlemini yapabiliriz
                                        if (strcmp(hisselerSembol, emir->sembol) == 0)
                                        {   //hisse yapımıız oluşturduktan sonra banka yapımızı oluşturup gerekli bilgileri veriyoruz
                                            Hisse hisse = HisseOlustur(hisselerSembol, fiyat);
                                            Banka banka = BankaOlustur(emir->sembol, emir->adet, hisse->fiyat, 0, 0);
                                            //banka yapımızın alış metodunu çağırıyoruz ve yeni alım işlemi olduğundan ikinci parametresine 0 değerini veriyoruz
                                            banka->alis(banka, 0);
                                            emir->emirYokEt(emir);
                                            portfoy->portfoyYoket(portfoy);
                                            hisse->hisseYoket(hisse);
                                            banka->bankaYokEt(banka);
                                            fclose(fp6);
                                            free(fp6);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fclose(fp1);
    fclose(fp3);
    free(fp1);
    free(fp3);

    exit(EXIT_SUCCESS);
    return 0;
}