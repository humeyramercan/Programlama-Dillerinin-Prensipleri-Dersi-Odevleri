#include "hisse.h"
Hisse HisseOlustur(char* sembol,double fiyat){
    Hisse this;
    this=(Hisse) malloc(sizeof(struct HISSE));
    this->sembol=sembol;
    this->fiyat=fiyat;
    this->hisseYoket=&HisseYoket;
   
    return this;
}

void HisseYoket(Hisse this){
    free(this);
}