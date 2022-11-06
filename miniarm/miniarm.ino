/*
 * mini robot arm
 * Copyright 2022 Gabriel Dimitriu
 *
 * This file is part of miniarm project.

 * miniarm is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * miniarm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with miniarm; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
*/

#include <Arduino.h>
#include <Servo.h>
#include <NeoSWSerial.h>
#include <EnableInterrupt.h>
#include "StringList.h"
#include "EepromOp.h"

#define RxD 2
#define TxD 3
#define servoPin_Gripper 4
#define servoPin_Elbow 5
#define servoPin_Shoulder 6
#define servoPin_Waist 7
#define SERIAL_DEBUG 1
#define SERIAL_DEBUG_MENU 1

Servo servo_Gripper;
Servo servo_Elbow;
Servo servo_Shoulder;
Servo servo_Waist;

//for communication
char inDataBT[20]; // Allocate some space for the string
char inCharBT; // Where to store the character read
byte indexBT = 0; // Index into array; where to store the character
boolean cleanupBT;
StringList commands;
EepromOp eeprom(0x50,&commands);
boolean isAutoMode = false;
boolean isDirectMode = false;
boolean isStopped = true;
unsigned int nrCommands = 0;
boolean isForward = true;
unsigned int position = 0;

NeoSWSerial BTSerial(RxD, TxD);
const int prefixCommandNr = 5;
const char prefixCommands[prefixCommandNr] = {'w','s','e','g','d'};


void neoSSerial1ISR() {
    NeoSWSerial::rxISR(*portInputRegister(digitalPinToPort(RxD)));
}

boolean isValidNumber(char *data) {
  if (strlen(data) == 0 ) return false;
  if(!(data[0] == '+' || data[0] == '-' || isDigit(data[0]))) return false;

  for(byte i=0;i<strlen(data);i++) {
    if(!(isDigit(data[i]) || data[i] == '.')) return false;
  }
  return true;
}

#ifdef SERIAL_DEBUG_MENU
void printMenu() {
  if (!Serial)
    return;
  Serial.println( "---------------------------------" );
  Serial.println( "Mini Robotic arm Menu:");
  Serial.println( "wxx# servo waist xx degree");
  Serial.println( "sxx# servo shoulder xx degree");
  Serial.println( "exx# servo elbow xx degree");
  Serial.println( "gxx# gripper xx degree");
  Serial.println( "dxx# delay of xx miliseconds");
  Serial.println( "S[w/s/e/g/d]xx# save the command");
  Serial.println( "Rf# run in forward order");
  Serial.println( "Rr# run in reverse order");
  Serial.println( "C# clear the saved commands");
  Serial.println( "E[l,L,D,a,c]# save EEPROM");
  Serial.println( "h# stop/start the loop");
  Serial.println( "-----------------------------" );
}
#endif

void setup() {
#ifdef SERIAL_DEBUG  
  Serial.begin(9600);
  Serial.println("before init");
#endif  
  BTSerial.begin(38400);  
  enableInterrupt(RxD, neoSSerial1ISR, CHANGE);
  cleanupBT = false;
  servo_Gripper.attach(servoPin_Gripper);
  servo_Elbow.attach(servoPin_Elbow);
  servo_Shoulder.attach(servoPin_Shoulder);
  servo_Waist.attach(servoPin_Waist);
  eeprom.begin();
#ifdef SERIAL_DEBUG_MENU
  if (Serial) {
    Serial.println("after init");
    printMenu();
  }
#endif
  isAutoMode = eeprom.shouldRunLoop();
  if (eeprom.shouldLoad() || isAutoMode) {
    unsigned int sz = eeprom.size();
    nrCommands = sz;
    for (int i = 0; i < sz; i++) {
      char *val = eeprom.readNextCommand();
      commands.addTail(val);
      delete[] val;
    }
    eeprom.reset();
#ifdef SERIAL_DEBUG
    Serial.println("Commands loaded");
    for(int i = 0 ;i < commands.size();i++) {
      Serial.println(commands.getForwardValue());
    }
    commands.reset();
#endif    
  }
  if (isAutoMode)
    isStopped = false;
  isDirectMode = eeprom.shouldRunDirectMode();
  if (isDirectMode)
    isAutoMode = true;
  if (eeprom.shouldRunLoopDirectMode()) {
    nrCommands = eeprom.size();
    isDirectMode = true;
    isAutoMode = true;
    isStopped = false;
  }
  BTSerial.println("Starting");BTSerial.flush();
}

void makeCleanupBT() {
  BTSerial.println("OK");
  BTSerial.flush();
   for (indexBT = 0; indexBT < 20; indexBT++) {
      inDataBT[indexBT] = '\0';
   }
   inCharBT = '0';
   indexBT = 0;
   cleanupBT = false;
}

bool isPrefixCommand(char prefix) {
  for(int i = 0 ;i < prefixCommandNr; i++) {
    if (prefix == prefixCommands[i])
      return true;
  }
  return false;
}

bool moveWaist(char *inData) {
  char *temp = inData;
  //remove w
  temp +=1;
  if (!isValidNumber(temp)) {
     return false;
  }
#ifdef SERIAL_DEBUG
  if (Serial) {
    Serial.print("move servo waist ");
    Serial.print(atoi(temp));
    Serial.println("degree");
  }
#endif     
  servo_Waist.write(atoi(temp));
  return true;  
}

bool moveShoulder(char *inData) {
  char *temp = inData;
  //remove s
  temp +=1;
  if (!isValidNumber(temp)) {
    return false;
  }
#ifdef SERIAL_DEBUG
  if (Serial) {
    Serial.print("move servo shoulder ");
    Serial.print(atoi(temp));
    Serial.println("degree");
  }
#endif     
  servo_Shoulder.write(180-atoi(temp));
  return true;
}

bool moveElbow(char *inData) {
  char *temp = inData;
  //remove e
  temp +=1;
  if (!isValidNumber(temp)) {
     return false;
  }
#ifdef SERIAL_DEBUG
  if (Serial) {
    Serial.print("move servo elbow ");
    Serial.print(atoi(temp));
    Serial.println("degree");
  }
#endif     
  servo_Elbow.write(atoi(temp));
  return true;
}

bool moveGripper(char *inData) {
  char *temp = inData;
  //remove g
  temp +=1;
  if (!isValidNumber(temp)) {
    return false;
  }
#ifdef SERIAL_DEBUG
  if (Serial) {
    Serial.print("move servo grabber ");
    Serial.print(atoi(temp));
    Serial.println("degree");
  }
#endif     
  servo_Gripper.write(atoi(temp));
  return true;
}

bool applyDelay(char *inData) {
  char *temp = inData;
  //remove d
  temp +=1;
  if (!isValidNumber(temp)) {
    return false;
  }
#ifdef SERIAL_DEBUG
  if (Serial) {
    Serial.print("sleep ");
    Serial.print(atol(temp));
    Serial.println("miliseconds");
  }
#endif     
  delay(atol(temp));
  return true;
}

bool makeMove(char *inData, bool isBT) {
  bool retValue = false;
  if (inData[0] == 'w') {
    retValue = moveWaist(inData);
  } else if (inData[0] == 's') {
    retValue = moveShoulder(inData);
  } else if (inData[0] == 'e') {
    retValue = moveElbow(inData);
  } else if (inData[0] == 'g') {
    retValue = moveGripper(inData);
  } else if (inData[0] == 'd') {
    retValue = applyDelay(inData);
  } else if (inData[0] == 'h') {
    isStopped = !isStopped;
    if (!isStopped) {
      if (isAutoMode) {
        if (isDirectMode)
          nrCommands = eeprom.size();
        else
          nrCommands = commands.size();
        position = 0;
        isForward = true;
      }
    }
    retValue = true;
  } else if (inData[0] == 'S') {
    isStopped = true;
    //remove S from command
    for (int i = 0 ; i < strlen(inData); i++) {
       inData[i]=inData[i+1];
    }
    if (isPrefixCommand(inData[0])) {      
      char *temp = &inData[1];
      if (!isValidNumber(temp)) {
        retValue = false;
      } else {
        if (isDirectMode) {
          eeprom.appendCommand(inData);
        } else {
          commands.addTail(inData);
        }
        retValue = true;
#ifdef SERIAL_DEBUG
        if (Serial) {
          Serial.print("Save command ");Serial.println(inData);
        }
#endif        
      }
    } else {
      retValue = false;
    }
  } else if (inData[0] == 'C') {
    commands.clear();
    eeprom.clear();
    nrCommands = 0;
    isStopped = true;
    retValue = true;
  } else if (inData[0] == 'R') {
    //remove R from command
    for (int i = 0 ; i < strlen(inData); i++) {
       inData[i]=inData[i+1];
    }
    if (inData[0] == 'f') {
#ifdef SERIAL_DEBUG
      if (Serial) {
        Serial.println("Move commands in forward order");
      }
#endif      
      commands.reset();
      char *temp = commands.getForwardValue();
      while (temp != NULL) {
        makeMove(temp,false);
        temp = commands.getForwardValue();
      }
      retValue = true;
    } else if (inData[0] == 'r') {
#ifdef SERIAL_DEBUG
      if (Serial) {
        Serial.println("Move commands in reverse order");
      }
#endif
      commands.reset();
      char *temp = commands.getReverseValue();      
      while (temp != NULL) {
        makeMove(temp,false);
        temp = commands.getReverseValue();
      }
      retValue = true;
    }
  } else if (inData[0] == 'E') {
    //remove E from command
    for (int i = 0 ; i < strlen(inData); i++) {
       inData[i]=inData[i+1];
    }
    if (inData[0] == 'd') {
#ifdef SERIAL_DEBUG
      if (Serial) {
        Serial.println("Set direct mode");
      }
#endif
       isDirectMode = true;
       eeprom.reset();
       eeprom.setAutoMode(inData[0]);
       retValue = true;
    } else if (inData[0] == 'l') {
#ifdef SERIAL_DEBUG
      if (Serial) {
        Serial.println("Set load mode");      
      }
#endif
      eeprom.setAutoMode(inData[0]);      
      retValue = true;
    } else if (inData[0] == 'L') {
      isAutoMode = true;
#ifdef SERIAL_DEBUG
      if (Serial) {
        Serial.println("Set auto loop mode");
      }
#endif
      eeprom.setAutoMode(inData[0]);
      retValue = true;
    } else if (inData[0] == 'a') {
#ifdef SERIAL_DEBUG
      if (Serial) {
        Serial.println("Save all commands");
      }
#endif
      eeprom.reset();
      eeprom.writeAllComands();
      retValue = true;
    } else if (inData[0] == 'c') {
      eeprom.clear();
      retValue = true;
    } else if (inData[0] == 'D') {
#ifdef SERIAL_DEBUG
      if (Serial) {
        Serial.println("Set loop direct mode");
      }
#endif
      isDirectMode = true;
      isAutoMode = true;
      eeprom.reset();
      eeprom.setAutoMode(inData[0]);
      retValue = true;
    }
  }
  if (isBT) {
    makeCleanupBT();
  }
  return retValue;
}

void readData() {
   if (!BTSerial.available())
     return;
   while(BTSerial.available() > 0) // Don't read unless there you know there is data
   {
     if(indexBT < 19) // One less than the size of the array
     {
        inCharBT = BTSerial.read(); // Read a character
        if (inCharBT == '\r' || inCharBT == '\n' || inCharBT == '\0' || inCharBT < 35 || inCharBT > 122) {
          continue;
        }
        //commands start with a letter capital or small
        if (indexBT == 0 && !((inCharBT > 64 && inCharBT < 91) || (inCharBT > 96 && inCharBT < 123))) {
          continue;
        }    
        inDataBT[indexBT++] = inCharBT; // Store it
        inDataBT[indexBT] = '\0'; // Null terminate the string
        if (inCharBT == '#') {
          break;
        }
     } else {
        makeCleanupBT();
     }
   }
   if (indexBT > 0 && inDataBT[indexBT-1] == '#') {
     inDataBT[indexBT-1] = '\0';
     if (!makeMove(inDataBT, true)) {
#ifdef SERIAL_DEBUG_MENU        
       printMenu();
#endif         
     }
   }
}

void loop() {
   
  while(!BTSerial.available()) {
      if (!isStopped && isAutoMode)
        break;
  }
  readData();
  if (!isStopped && isAutoMode) {
    if (isForward) {
      if (position == nrCommands) {
        isForward = false;
        char * command = eeprom.readPreviousCommand();
        makeMove(command, false);
        delete [] command;
        position--;
      } else {
        char * command = eeprom.readNextCommand();
        makeMove(command, false);
        delete [] command;
        position++;
      }
    } else {
      if (position == 0) {
        isForward = true;
        char * command = eeprom.readNextCommand();
        makeMove(command, false);
        delete [] command;
        position++;
      } else {
        char * command = eeprom.readPreviousCommand();
        makeMove(command, false);
        delete [] command;
        position--;
      }
    }
    delay(100);
  }
}
