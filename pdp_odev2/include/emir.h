#ifndef EMIR_H
#define EMIR_H
#include "stdio.h"
#include "stdlib.h"
struct EMIR
{
    char *sembol;
    char *islem;
    int adet;
    void (*emirYokEt)(struct EMIR *);
};

typedef struct EMIR *Emir;

Emir EmirOlustur(char *,char*,int);
void EmirYoket(Emir);

#endif