
#include "Eeprom.h"

Eeprom::Eeprom(int address) : addressDevice(address) {
}

void Eeprom::begin() {
  Wire.begin();
}

byte Eeprom::readByte(unsigned int address) {
  byte rdata = 0xFF;
  Wire.beginTransmission(addressDevice);
  Wire.write((int) (address >>8)); //MSB
  Wire.write((int) (address & 0xFF)); //LSB
  Wire.endTransmission();
  Wire.requestFrom(addressDevice, 1);
  if (Wire.available()) {
    rdata = Wire.read();
  }
  return rdata;
}

void Eeprom::readBuffer(unsigned int address, byte * buffer, int lengthBuff) {
  Wire.beginTransmission(addressDevice);
  Wire.write((int)(address >> 8)); //MSB
  Wire.write((int) (address & 0xFF)); //LSB
  Wire.endTransmission();
  Wire.requestFrom(addressDevice, lengthBuff);
  int idx = 0;
  for(idx = 0; idx < lengthBuff; idx++) {
    if (Wire.available()) {
      buffer[idx] = Wire.read();
    }
  }
}

void Eeprom::writeBuffer(unsigned int address, byte *data, byte lengthBuff) {
  Wire.beginTransmission(addressDevice);
  Wire.write((int) (address >> 8)); //MSB
  Wire.write((int) (address & 0xFF)); //LSB
  byte idx;
  for(idx = 0; idx < lengthBuff; idx++) {
    Wire.write(data[idx]);
  }
  Wire.endTransmission();
}

void Eeprom::writeByte(unsigned int address, byte data) {
  int rdata  = data;
  Wire.beginTransmission(addressDevice);
  Wire.write((int) (address >> 8)); //MSB
  Wire.write((int) (address & 0xFF)); //LSB
  Wire.write(rdata);
  Wire.endTransmission();
}
