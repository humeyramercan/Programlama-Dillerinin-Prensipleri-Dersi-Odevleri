#ifndef HISSE_H
#define HISSE_H

#include "stdio.h"
#include "stdlib.h"
struct HISSE
{
    char* sembol;
    double fiyat;
    void (*hisseYoket) (struct HISSE*);
   
};
typedef struct HISSE* Hisse;

Hisse HisseOlustur(char*,double);
void HisseYoket(Hisse);

#endif