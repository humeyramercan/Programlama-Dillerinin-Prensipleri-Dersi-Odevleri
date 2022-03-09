#ifndef BANKA_H
#define BANKA_H
#include "stdio.h"
#include "stdlib.h"
#include "string.h"
struct BANKA
{
    char* sembol;
    int emirlerdekiAdet,portfoydekiAdet;
    double hisselerdekiFiyat,portfoydekiMaaliyet;
    double (*satis) (struct BANKA*);
    void (*alis) (struct BANKA*,int);
    void (*bankaYokEt) (struct BANKA*);
};

typedef struct BANKA *Banka;


Banka BankaOlustur(char*,int,double,int,double);

double Satis(const Banka);
void Alis(const Banka,int);
void BankaYokEt(Banka);
#endif