#ifndef EEPROM_OP_H
#define EEPROM_OP_H
#include "StringList.h"
#include "Eeprom.h"

class EepromOp {
public:
  EepromOp(int address, StringList *commands);
  void begin();
  boolean shouldLoad();
  /*
   * run forward and then reverse in a loop
   */
  boolean shouldRunLoop();
  boolean shouldRunDirectMode();
  boolean shouldRunLoopDirectMode();
  void setAutoMode(char type);
  void writeAllComands();
  unsigned int size();
  char* readNextCommand();
  char* readPreviousCommand();
  void appendCommand(char *command);
  void reset();
  void clear();
private:
  Eeprom _eeprom;
  StringList *_commands;
  unsigned int _position;
  unsigned int _lastWritedCommand;
  byte _autoMode;
  unsigned int _size;
};

#endif
