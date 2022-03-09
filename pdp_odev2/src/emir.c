#include "emir.h"

Emir EmirOlustur(char *sembol,char* islem,int adet){
    Emir this;
    this=(Emir) malloc(sizeof(struct EMIR));
    this->sembol=sembol;
    this->islem=islem;
    this->adet=adet;
    this->emirYokEt=&EmirYoket;
    return this;
}
void EmirYoket(Emir this){
    free(this);
}