#ifndef EEPROM_H
#define EEPROM_H

#include <Arduino.h>
#include <Wire.h>

class Eeprom {
public:
  Eeprom(int address);
  void begin();
  byte readByte(unsigned int address);
  void writeByte(unsigned int address, byte data);
  void readBuffer(unsigned int address, byte * buffer, int lengthBuff);
  void writeBuffer(unsigned int address, byte *data, byte lengthBuff);
private:
  int addressDevice;
};

#endif
