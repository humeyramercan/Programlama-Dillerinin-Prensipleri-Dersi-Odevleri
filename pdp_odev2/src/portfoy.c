#include "portfoy.h"
Portfoy PortfoyOlustur(char * sembol, double maliyet, int adet){
    Portfoy this;
    this=(Portfoy) malloc(sizeof(struct PORTFOY));
    this->sembol=sembol;
    this->maliyet=maliyet;
    this->adet=adet;
    this->portfoyYoket=&PortfoyYoket;
    return this;
}
void PortfoyYoket(Portfoy this){
    free(this);
}