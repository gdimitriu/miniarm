#include "EepromOp.h"

#define LOOP 1
#define LOAD 2
#define DIRECTMODE 3
#define LOOPDIRECTMODE 4
#define COMMANDS_START_POSITION 3

EepromOp::EepromOp(int address, StringList *commands) : _eeprom(address) {
  _commands = commands;
  _position = COMMANDS_START_POSITION;
  _autoMode = -1;
  _lastWritedCommand = COMMANDS_START_POSITION;
  _size = 0;
}

void EepromOp::clear() {
  _position = COMMANDS_START_POSITION;
  _autoMode = -1;
  _lastWritedCommand = COMMANDS_START_POSITION;
  _size = 0;
  _eeprom.writeBuffer(1,(byte *)&_size,2);
}

void EepromOp::reset() {
  _lastWritedCommand = _position;
  _position = COMMANDS_START_POSITION;
  if (_commands->size() == 0) {
    _eeprom.readBuffer(1,(byte *)&_size,2);
  } else {
    _size = _commands->size();
  }
}

void EepromOp::begin() {
  _eeprom.begin();
  _eeprom.readBuffer(1,(byte *)&_size,2);
}

boolean EepromOp::shouldRunLoop() {
   _autoMode = _eeprom.readByte(0);
  return _autoMode == LOOP ? true : false;
}

boolean EepromOp::shouldLoad() {
  _autoMode = _eeprom.readByte(0);
  return _autoMode == LOAD ? true : false;
}

boolean EepromOp::shouldRunDirectMode() {
  _autoMode = _eeprom.readByte(0);
  return _autoMode == DIRECTMODE ? true : false;
}

boolean EepromOp::shouldRunLoopDirectMode() {
  _autoMode = _eeprom.readByte(0);
  return _autoMode == LOOPDIRECTMODE ? true : false;
}

void EepromOp::setAutoMode(char type) {
  int value = 0;
  switch(type) {
    case 'l':
      value = LOAD;
      break;
    case 'L':
      value = LOOP;
      break;
    case 'd':
      value = DIRECTMODE;
      break;
    case 'D':
      value = LOOPDIRECTMODE;
      break;
  }
  _eeprom.writeByte(0,value);
  delay(100);
  _autoMode = value;
}

void EepromOp::writeAllComands() {
  unsigned int tmp = _commands->size();
  unsigned int pos = 3;
  unsigned int lastCommand = pos;
  _eeprom.writeBuffer(1,(byte *)&tmp,2);
  delay(100);
  for (int i = 0; i < tmp; i++) {
    char *str = _commands->getForwardValue();
    unsigned int sz = strlen(str);
    sz++;//terminator
    lastCommand = pos;
    _eeprom.writeBuffer(pos,(byte *)&sz,2);
    pos += 2;
    delay(100);
    _eeprom.writeBuffer(pos,str,sz);
    pos += sz;
    delay(100);
    _eeprom.writeBuffer(pos,(byte *)&lastCommand,2);
    pos += 2;
    delay(100);
  }
  _lastWritedCommand = pos;
}

void EepromOp::appendCommand(char *command) {
  unsigned int sz = strlen(command);
  unsigned int lastPosition = _lastWritedCommand;
  sz++;//terminator
  _eeprom.writeBuffer(_lastWritedCommand,(byte *)&sz,2);
  _lastWritedCommand += 2;
  delay(100);
  _eeprom.writeBuffer(_lastWritedCommand,command,sz);
  _lastWritedCommand += sz;
  delay(100);
  _size++;
  _eeprom.writeBuffer(1,(byte *)&_size,2);
  delay(100);
  _eeprom.writeBuffer(_lastWritedCommand,(byte *)&lastPosition,2);
  delay(100);
  _lastWritedCommand += 2;
}

unsigned int EepromOp::size() {
  unsigned int ret = 0;
  _eeprom.readBuffer(1,(byte *)&ret,2);
  return ret;
}

char* EepromOp::readNextCommand() {
  unsigned int sz = 0;
  _eeprom.readBuffer(_position,(byte *)&sz,2);
  _position += 2;
  char *ret = new char[sz];
  _eeprom.readBuffer(_position,(byte *)ret,sz);
  _position += sz;
  _position += 2; //skip start position of this command which is used by previousCommand  
  return ret;
}

char* EepromOp::readPreviousCommand() {
  if (_position == COMMANDS_START_POSITION) {
    return NULL;
  }
  unsigned int sz = 0;
  unsigned int pos = 0;
  _position -= 2;
  _eeprom.readBuffer(_position,(byte *)&pos,2);
  _position = pos;
  _eeprom.readBuffer(pos,(byte *)&sz,2);
  pos += 2;
  char *ret = new char[sz];
  _eeprom.readBuffer(pos,(byte *)ret,sz);
  return ret;
}
