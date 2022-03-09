#ifndef PORTFOY_H
#define PORTFOY_H
#include "stdio.h"
#include "stdlib.h"

struct PORTFOY
{
    char *sembol;
    double maliyet;
    int adet;
     void (*portfoyYoket) (struct PORTFOY*);
};
typedef struct PORTFOY *Portfoy;

Portfoy PortfoyOlustur(char *, double, int);
void PortfoyYoket(Portfoy);
#endif