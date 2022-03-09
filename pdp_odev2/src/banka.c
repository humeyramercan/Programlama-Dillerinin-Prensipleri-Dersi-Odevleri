#include "banka.h"

Banka BankaOlustur(char *sembol, int emirlerdekiAdet, double hisselerdekiFiyat, int portfoydekiAdet, double portfoydekiMaliyet)
{
    Banka this;
    this = (Banka)malloc(sizeof(struct BANKA));
    this->sembol = sembol;
    this->emirlerdekiAdet = emirlerdekiAdet;
    this->hisselerdekiFiyat = hisselerdekiFiyat;
    this->portfoydekiAdet = portfoydekiAdet;
    this->portfoydekiMaaliyet = portfoydekiMaliyet;
    this->alis = &Alis;
    this->satis = &Satis;
    this->bankaYokEt = &BankaYokEt;
    return this;
}

double Satis(const Banka this)
{
    double karZarar = 0;

    double guncelFiyat = this->emirlerdekiAdet * this->hisselerdekiFiyat;
    double maliyet = this->emirlerdekiAdet * this->portfoydekiMaaliyet;
    karZarar = guncelFiyat - maliyet;
    if (karZarar > 0)
    {
        
        printf("*******SATIS KAR ZARAR*******\n");
        printf("%s : %lf Kar \n\n", this->sembol, karZarar);
    }
    else
    {
        double x = -1 * karZarar;
        printf("****** SATIS KAR ZARAR********\n");
        printf("%s : %lf Zarar \n\n", this->sembol, x);
    }
    int adet = this->portfoydekiAdet - this->emirlerdekiAdet;
    if (adet > 0)
    {
        printf("******GUNCEL PORTFOY********\n");
        printf("Hisse: %s \n", this->sembol);
        printf("Adet: %d \n", adet);
        printf("Maliyet: %lf \n\n", this->portfoydekiMaaliyet);
    }
    
    return karZarar;
}

void Alis(const Banka this, int i)
{
    if (i == 1)
    {
        double guncelFiyat = this->emirlerdekiAdet * this->hisselerdekiFiyat;
        int toplamAdet = this->portfoydekiAdet + this->emirlerdekiAdet;
        double yeniMaliyet = ((this->portfoydekiMaaliyet * this->portfoydekiAdet) + guncelFiyat) / toplamAdet;
        printf("******GUNCEL PORTFOY********\n");
        printf("Hisse: %s \n", this->sembol);
        printf("Adet: %d \n", toplamAdet);
        printf("Maliyet: %lf \n\n", yeniMaliyet);
    }
    else if (i == 0)
    {
        printf("*******GUNCEL PORTFOY*******\n");
        printf("Hisse: %s \n", this->sembol);
        printf("Adet: %d \n", this->emirlerdekiAdet);
        printf("Maliyet: %lf \n\n", this->hisselerdekiFiyat);
    }
}
void BankaYokEt(Banka this)
{
    free(this);
}