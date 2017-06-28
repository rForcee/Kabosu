int read_LCD_buttons() {              // read the buttons
  adc_key_in = analogRead(0);       // read the value from the sensor

  if (adc_key_in < 50)   return btnRIGHT;
  if (adc_key_in < 650)  return btnLEFT;
}

typedef enum {
  CLICK_IDLE,
  CLICK_INCREASE,
  CLICK_DECREASE
} CLICK_STATE;

CLICK_STATE CLICKState;

void CLICK_init()
{
  tmp = 0;
  btnRIGHT;
  btnLEFT;
  boolean state_button;
}

void CLICK_update() {
  lcd_key = read_LCD_buttons();
  CLICK_STATE nextState = CLICKState;
  switch (CLICKState) {              // depending on which button was pushed, we perform an action
    case CLICK_IDLE :
      if (btnRIGHT && state_button && (millis() - tmp > 50)) {
        tmp = millis();
        nextState = CLICK_INCREASE;
      }
      if (btnLEFT && state_button && (millis() - tmp > 50)) {
        tmp = millis();
        nextState = CLICK_DECREASE;
      }
      break;

    case CLICK_INCREASE :
      nextState = CLICK_IDLE;
      break;

    case CLICK_DECREASE :
      nextState = CLICK_IDLE;

      break;

      /*case btnRIGHT: {            //  push button "RIGHT" and show the word on the screen

          break;
        }
        case btnLEFT: {

          break;
        }*/
  }
}

void CLICK_output() {
  switch (CLICKState) {
    
    case CLICK_IDLE:
      break;

    case CLICK_INCREASE :
      Speed -= 100;
      break;

    case CLICK_DECREASE :
      Speed += 100;
      break;
  }
}

